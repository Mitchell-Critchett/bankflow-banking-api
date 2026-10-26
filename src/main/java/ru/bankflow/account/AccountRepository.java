package ru.bankflow.account;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AccountRepository
        extends JpaRepository<AccountEntity, UUID> {

    List<AccountEntity> searchAccountsByClientId(UUID client_id, Pageable pageable);
}
