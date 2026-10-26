package ru.bankflow.account;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bankflow.account.dto.AccountCreateRequest;
import ru.bankflow.account.dto.AccountResponse;
import ru.bankflow.client.ClientEntity;
import ru.bankflow.client.ClientRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository repository;

    private final ClientRepository clientRepository;

    @Transactional
    public AccountResponse createAccount(AccountCreateRequest accountToCreate) {

        if (!clientRepository.existsById(accountToCreate.clientId())) {
            throw new EntityNotFoundException(
                    "Клиент с id= %s не существует".formatted(accountToCreate.clientId())
            );
        }

        ClientEntity client = clientRepository.getReferenceById(accountToCreate.clientId());

        var entityToSave = new AccountEntity(
                client,
                "RUB"
        );

        var savedEntity = repository.save(entityToSave);

        return toResponse(savedEntity, client.getId());
    }

    public AccountResponse getAccountById(UUID id) {
        AccountEntity accountEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Аккаунт с id %s не найден".formatted(id)
                ));

        return toResponse(accountEntity, accountEntity.getId());
    }

    public List<AccountResponse> findAccountsByClient(
            UUID clientId, Integer pageSize, Integer pageNumber) {

        int validPageSize = pageSize != null
                ? pageSize : 0;

        int validPageNumber = pageNumber != null
                ? pageNumber : 0;

        var pageable = Pageable
                .ofSize(validPageSize)
                .withPage(validPageNumber);

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
                .map(entity -> toResponse(entity, clientId))
                .toList();
    }

    private AccountResponse toResponse(AccountEntity accountEntity, UUID client) {
        return new AccountResponse(
                accountEntity.getId(),
                client,
                accountEntity.getAccountNumber(),
                accountEntity.getBalance(),
                accountEntity.getCurrencyCode(),
                accountEntity.getStatus(),
                accountEntity.getCreatedAt()
        );
    }
}
