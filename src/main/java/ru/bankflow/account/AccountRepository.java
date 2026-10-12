package ru.bankflow.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository
        extends JpaRepository<Account, UUID> {

    // Query Метод поиска по id возвращает Account
}
