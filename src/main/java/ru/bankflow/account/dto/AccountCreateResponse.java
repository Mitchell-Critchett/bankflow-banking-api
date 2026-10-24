package ru.bankflow.account.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import ru.bankflow.account.AccountStatus;
import ru.bankflow.client.ClientEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record AccountCreateResponse(
        @Null(message = "тело не должно иметь id")
        UUID id,

        @NotNull(message = "должен содержать clientId")
        UUID clientId,

        @NotNull(message = "Должен содержать номер аккаунта")
        String accountNumber,

        @Null(message = "Не должен содержать баланс")
        BigDecimal balance,

        String currency,

        AccountStatus status,

        OffsetDateTime createdAt
) {
}
