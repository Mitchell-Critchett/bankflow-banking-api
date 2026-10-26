package ru.bankflow.exceptions.processing;

import java.time.LocalDateTime;

public record ErrorResponseDto(
    String message,
    String detailedMessage,
    LocalDateTime timeError
) {

}
