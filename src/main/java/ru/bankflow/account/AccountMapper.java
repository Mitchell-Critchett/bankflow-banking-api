package ru.bankflow.account;

public class AccountMapper {

    public Account toDomain(
            AccountEntity accountEntity
    ) {
        return new Account(
                accountEntity.getId(),
                accountEntity.getClient_id(),
                accountEntity.getAccount_number(),
                accountEntity.getBalance(),
                accountEntity.getCurrencyCode(),
                accountEntity.getStatus(),
                accountEntity.getCreated_at()
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
