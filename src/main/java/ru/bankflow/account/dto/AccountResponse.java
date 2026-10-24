package ru.bankflow.account.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import ru.bankflow.account.AccountStatus;

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
