package ru.bankflow.modules.ledger.dto;

import ru.bankflow.modules.ledger.OperationType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record EntryResponse(
    UUID id,
    UUID accountId,
    BigDecimal flowFunds,
    OperationType operationType,
    UUID transferId,
    OffsetDateTime createdAt
) {}
