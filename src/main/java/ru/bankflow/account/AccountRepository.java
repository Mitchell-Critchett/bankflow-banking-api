package ru.bankflow.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository
        extends JpaRepository<AccountEntity, UUID> {

    // Query Метод поиска по id возвращает Account
//    @Query("""
//        SELECT a.id FROM accounts
//                WHERE a.id = :id
//        """)
//    Account findAccount(
//            @Param(UUID id)
//    )
}
