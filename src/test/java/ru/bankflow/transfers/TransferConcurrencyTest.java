package ru.bankflow.transfers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import ru.bankflow.modules.account.AccountEntity;
import ru.bankflow.modules.account.AccountRepository;
import ru.bankflow.modules.client.ClientEntity;
import ru.bankflow.modules.client.ClientRepository;
import ru.bankflow.exceptions.InsufficientFundsException;
import ru.bankflow.modules.transfer.TransferRepository;
import ru.bankflow.modules.transfer.TransferService;
import ru.bankflow.modules.transfer.dto.CreateTransferRequest;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
class TransferConcurrencyTest {

    @Container
    static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("bankflow_test")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldAllowOnlyOneConcurrentTransferWhenBalanceIsInsufficientForBoth()
            throws Exception {

        // Arrange: создаём клиента и три счёта.
        ClientEntity client = clientRepository.saveAndFlush(
                new ClientEntity("Test Client", "test@example.com")
        );

        AccountEntity source = accountRepository.saveAndFlush(
                new AccountEntity(client, "RUB")
        );

        AccountEntity destinationA = accountRepository.saveAndFlush(
                new AccountEntity(client, "RUB")
        );

        AccountEntity destinationB = accountRepository.saveAndFlush(
                new AccountEntity(client, "RUB")
        );

        // Устанавливаем начальные балансы для теста.
        jdbcTemplate.update(
                "UPDATE accounts SET balance = ? WHERE id = ?",
                new BigDecimal("1000.00"),
                source.getId()
        );

        jdbcTemplate.update(
                "UPDATE accounts SET balance = ? WHERE id IN (?, ?)",
                BigDecimal.ZERO,
                destinationA.getId(),
                destinationB.getId()
        );

        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);

        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {

            Future<Boolean> transferA = executor.submit(() -> {
                ready.countDown();
                start.await();

                try {
                    transferService.createTransfer(
                            new CreateTransferRequest(
                                    source.getId(),
                                    destinationA.getId(),
                                    new BigDecimal("700.00")
                            ),
                            UUID.randomUUID()
                    );
                    return true;
                } catch (InsufficientFundsException e) {
                    return false;
                }
            });

            Future<Boolean> transferB = executor.submit(() -> {
                ready.countDown();
                start.await();

                try {
                    transferService.createTransfer(
                            new CreateTransferRequest(
                                    source.getId(),
                                    destinationB.getId(),
                                    new BigDecimal("700.00")
                            ),
                            UUID.randomUUID()
                    );
                    return true;
                } catch (InsufficientFundsException e) {
                    return false;
                }
            });

            // Ждём, пока оба потока подготовятся к запуску.
            assertTrue(ready.await(10, TimeUnit.SECONDS));

            // Разрешаем обоим потокам начать перевод.
            start.countDown();

            boolean resultA = transferA.get(20, TimeUnit.SECONDS);
            boolean resultB = transferB.get(20, TimeUnit.SECONDS);

            // Assert: ровно один перевод должен завершиться успешно.
            assertTrue(resultA ^ resultB,
                    "Ровно один перевод должен быть успешным");
        }

        // Читаем состояние из БД после завершения обеих транзакций.
        BigDecimal sourceBalance = accountRepository
                .findById(source.getId())
                .orElseThrow()
                .getBalance();

        BigDecimal destinationABalance = accountRepository
                .findById(destinationA.getId())
                .orElseThrow()
                .getBalance();

        BigDecimal destinationBBalance = accountRepository
                .findById(destinationB.getId())
                .orElseThrow()
                .getBalance();

        assertEquals(0, sourceBalance.compareTo(new BigDecimal("300.00")));

        BigDecimal recipientsTotal =
                destinationABalance.add(destinationBBalance);

        assertEquals(0, recipientsTotal.compareTo(new BigDecimal("700.00")));

        assertEquals(
                0,
                sourceBalance
                        .add(destinationABalance)
                        .add(destinationBBalance)
                        .compareTo(new BigDecimal("1000.00")),
                "Сумма денег на всех счетах должна сохраниться"
        );

        assertEquals(
                1L,
                transferRepository.count(),
                "В БД должен остаться один успешный перевод"
        );
    }
}