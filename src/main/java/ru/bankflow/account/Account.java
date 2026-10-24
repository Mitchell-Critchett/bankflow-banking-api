package ru.bankflow.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import ru.bankflow.client.ClientEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record Account(
        @Null
        UUID id,

        @NotNull
        ClientEntity clientId,

        @NotNull
        String accountNumber,

        @Null
        BigDecimal balance,

        @Null
        String currency,

        AccountStatus status,

        @NotNull
        OffsetDateTime createdAt
) {
}
