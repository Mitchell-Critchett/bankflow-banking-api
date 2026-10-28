package ru.bankflow.modules.account;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bankflow.modules.account.dto.CreateAccountRequest;
import ru.bankflow.modules.account.dto.AccountResponse;
import ru.bankflow.modules.client.ClientEntity;
import ru.bankflow.modules.client.ClientRepository;
import org.springframework.data.domain.Pageable;
import ru.bankflow.modules.ledger.EntryService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;

    private final ClientRepository clientRepository;

    private final BalanceService balanceService;

    private final EntryService entryService;

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest accountToCreate) {

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

        return toResponse(savedEntity);
    }

    public AccountResponse getAccountById(UUID id) {
        AccountEntity accountEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Аккаунт с id %s не найден".formatted(id)
                ));

        return toResponse(accountEntity);
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
                .map(this::toResponse)
                .toList();
    }

    public AccountResponse createTestDebitOn1000RUBS(UUID id) {
        AccountEntity accountEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Аккаунт с id %s не найден".formatted(id)
                ));

        balanceService.processCredit(accountEntity, BigDecimal.valueOf(1000.0));

        var savedEntity = repository.save(accountEntity);

        return toResponse(savedEntity);
    }

    private AccountResponse toResponse(AccountEntity accountEntity) {

        return new AccountResponse(
                accountEntity.getId(),
                accountEntity.getClient().getId(),
                accountEntity.getAccountNumber(),
                accountEntity.getBalance(),
                accountEntity.getCurrencyCode(),
                accountEntity.getStatus(),
                accountEntity.getCreatedAt()
        );
    }
}
