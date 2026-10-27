package ru.bankflow.modules.transfer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TransferRepository extends
        JpaRepository<TransferEntity, UUID> {


    boolean existsByIdempotencyKey(UUID idempotencyKey);

    Optional<TransferEntity> findByIdempotencyKey(UUID idempotencyKey);

}
