package ru.bankflow.account;

import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public Account toDomain(
            AccountEntity accountEntity
    ) {
        return new Account(
                accountEntity.getId(),
                accountEntity.getClient(),
                accountEntity.getAccountNumber(),
                accountEntity.getBalance(),
                accountEntity.getCurrencyCode(),
                accountEntity.getStatus(),
                accountEntity.getCreatedAt()
        );
    }

    public AccountEntity toEntity(
            Account account
    ) {
        return new AccountEntity(
                account.id(),
                account.clientId(),
                account.accountNumber(),
                account.balance(),
                account.currency(),
                account.status(),
                account.createdAt()
        );
    }
}
