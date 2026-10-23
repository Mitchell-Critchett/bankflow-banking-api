package ru.bankflow.validation;

import java.time.LocalDateTime;

public record ErrorResponseDto(
    String message,
    String detailedMessage,
    LocalDateTime timeError
) {

}
