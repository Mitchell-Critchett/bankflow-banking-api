package ru.bankflow.account;

import org.springframework.stereotype.Component;
import ru.bankflow.account.dto.AccountCreateResponse;
import ru.bankflow.client.ClientEntity;

@Component
public class AccountMapper {

    public AccountCreateResponse toDomain(
            AccountEntity accountEntity
    ) {
        return new AccountCreateResponse(
                accountEntity.getId(),
                accountEntity.getClient().getId(),
                accountEntity.getAccountNumber(),
                accountEntity.getBalance(),
                accountEntity.getCurrencyCode(),
                accountEntity.getStatus(),
                accountEntity.getCreatedAt()
        );
    }

    public AccountEntity toEntity(
            AccountCreateResponse accountCreateResponse,
            ClientEntity client
    ) {
        return new AccountEntity(
                accountCreateResponse.id(),
                client,
                accountCreateResponse.accountNumber(),
                accountCreateResponse.balance(),
                accountCreateResponse.currency(),
                accountCreateResponse.status(),
                accountCreateResponse.createdAt()
        );
    }
}
