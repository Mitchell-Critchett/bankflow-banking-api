package ru.bankflow.modules.ledger;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EntryRepository extends
        JpaRepository<EntryEntity, UUID> {

    Optional<List<EntryEntity>> findByAccountId(UUID accountId, Pageable pageable);
}
