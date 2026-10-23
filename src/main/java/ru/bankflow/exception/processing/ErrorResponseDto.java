package ru.bankflow.exception.processing;

import java.time.LocalDateTime;

public record ErrorResponseDto(
    String message,
    String detailedMessage,
    LocalDateTime timeError
) {

}
