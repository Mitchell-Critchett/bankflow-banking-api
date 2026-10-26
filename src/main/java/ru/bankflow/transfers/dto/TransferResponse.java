package ru.bankflow.transfers.dto;

import ru.bankflow.transfers.TransferStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TransferResponse (
    UUID id,
    UUID sourceAccount,
    UUID destinationAccount,
    BigDecimal amount,
    TransferStatus status,
    OffsetDateTime createdAt,
    UUID idempotencyKey
) {}
