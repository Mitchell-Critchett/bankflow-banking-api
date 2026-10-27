package ru.bankflow.modules.account;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository
        extends JpaRepository<AccountEntity, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT a FROM AccountEntity a
        WHERE a.id = :id
    """)
    Optional<AccountEntity> findAccountForUpdate(@Param("id") UUID id);

    List<AccountEntity> searchAccountsByClientId(UUID client_id, Pageable pageable);
}
