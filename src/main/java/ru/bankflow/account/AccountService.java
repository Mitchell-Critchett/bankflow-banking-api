package ru.bankflow.account;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.bankflow.client.ClientRepository;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository repository;

    private final ClientRepository clientRepository;

    private final AccountMapper mapper;

    @Transactional
    public Account createAccount(Account accountToCreate) {

        if (repository.existByAccountNumber(accountToCreate.accountNumber())) {
            throw new IllegalArgumentException(
                    "Счёт с таким номером уже существует"
            );
        }

        if (!clientRepository.existsById(accountToCreate.clientId())) {
            throw new IllegalArgumentException(
                    "Клиент с id= %s не существует".formatted(accountToCreate.clientId())
            );
        }

        if (accountToCreate.accountNumber().length() != 20
        || !accountToCreate.accountNumber().matches("\\d+")) {
            throw new IllegalArgumentException(
                    "Некорректный номер счёта"
            );
        }

        var entityToSave = mapper.toEntity(accountToCreate);

        entityToSave.setCurrencyCode("RUB");
        entityToSave.setBalance(BigDecimal.ZERO);
        entityToSave.setStatus(AccountStatus.ACTIVE);

        var savedEntity = repository.save(entityToSave);

        return mapper.toDomain(savedEntity);
    }

    public Account getAccountById(UUID id) {
        AccountEntity accountEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Account with id %s not found".formatted(id)
                ));

        return mapper.toDomain(accountEntity);
    }

    public List<Account> findAccountsByClient(UUID clientId) {

        var pageable = Pageable
                .ofSize(10)
                .withPage(0);

        List<AccountEntity> clientAccounts = repository.searchAllAccountsByClient(
                clientId,
                pageable
        );

        return clientAccounts.stream()
                .map(mapper::toDomain)
                .toList();
    }
}
