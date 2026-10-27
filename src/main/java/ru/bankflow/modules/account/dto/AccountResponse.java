package ru.bankflow.modules.account.dto;

import ru.bankflow.modules.account.AccountStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record AccountResponse (
    UUID id,
    UUID clientId,
    String accountNumber,
    BigDecimal balance,
    String currency,
    AccountStatus status,
    OffsetDateTime createdAt
){}
