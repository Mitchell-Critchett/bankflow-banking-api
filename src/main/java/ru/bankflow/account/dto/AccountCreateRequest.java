package ru.bankflow.account.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AccountCreateRequest(

        @NotNull(message = "должен содержать clientId")
        UUID clientId

) {}
