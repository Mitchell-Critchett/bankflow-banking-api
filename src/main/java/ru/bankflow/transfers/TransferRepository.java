package ru.bankflow.transfers;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransferRepository extends
        JpaRepository<TransferEntity, UUID> {


}
