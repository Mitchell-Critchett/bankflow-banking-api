package ru.bankflow.account;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.bankflow.account.dto.AccountCreateResponse;
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
    public AccountCreateResponse createAccount(AccountCreateResponse accountToCreate) {

        if (!clientRepository.existsById(accountToCreate.clientId())) {
            throw new EntityNotFoundException(
                    "Клиент с id= %s не существует".formatted(accountToCreate.clientId())
            );
        }

        if (repository.existsByAccountNumber(accountToCreate.accountNumber())) {
            throw new IllegalArgumentException(
                    "Счёт с номером: %s уже существует".formatted(accountToCreate.accountNumber())
            );
        }


        if (accountToCreate.accountNumber().length() != 20
        || !accountToCreate.accountNumber().matches("\\d+")) {
            throw new IllegalArgumentException(
                    "Некорректный номер счёта"
            );
        }

        var entityToSave = mapper.toEntity(
                accountToCreate,
                clientRepository.getReferenceById(accountToCreate.clientId())
        );

        entityToSave.setCurrencyCode("RUB");
        entityToSave.setBalance(BigDecimal.ZERO);
        entityToSave.setStatus(AccountStatus.ACTIVE);

        var savedEntity = repository.save(entityToSave);

        return mapper.toDomain(savedEntity);
    }

    public AccountCreateResponse getAccountById(UUID id) {
        AccountEntity accountEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Аккаунт с id %s не найден".formatted(id)
                ));

        return mapper.toDomain(accountEntity);
    }

    public List<AccountCreateResponse> findAccountsByClient(UUID clientId) {

        var pageable = Pageable
                .ofSize(10)
                .withPage(0);

        if (!clientRepository.existsById(clientId)) {
            throw new EntityNotFoundException(
                    "Клиент с id= %s не существует".formatted(clientId)
            );
        }

        List<AccountEntity> clientAccounts = repository.searchAccountsByClientId(
                clientId,
                pageable
        );

        return clientAccounts.stream()
                .map(mapper::toDomain)
                .toList();
    }
}
