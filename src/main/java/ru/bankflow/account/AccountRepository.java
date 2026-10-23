package ru.bankflow.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository
        extends JpaRepository<AccountEntity, UUID> {

    // Query Метод поиска по id возвращает Account
    boolean existByAccountNumber(String accountNumber);
}
