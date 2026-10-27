package ru.bankflow.modules.transfer.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateTransferRequest(

    @NotNull(message = "Не введён счёт отправителя платежа")
    UUID sourceAccount,

    @NotNull(message = "Не введён счёт получателя платежа")
    UUID destinationAccount,

    @NotNull(message = "Не введена сумма транзакции")
    @Digits(integer = 10, fraction = 2, message = "Неверный формат перевода")
    @DecimalMin(value = "50", message = "Размер перевода должен быть не менее 50")
    BigDecimal amount

) {}
