package ru.bankflow.exceptions.processing;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.bankflow.exceptions.AccountNotActiveException;
import ru.bankflow.exceptions.InsufficientFundsException;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(exception = {
            MethodArgumentNotValidException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ErrorResponseDto> handleBadRequest (
            Exception e
    ) {
        log.error("handle exception", e);

        var errorResponseDto = new ErrorResponseDto(
                "Bad request",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponseDto);
    }

    @ExceptionHandler(exception = {
            EntityNotFoundException.class
    })
    public ResponseEntity<ErrorResponseDto> handleNotFound (
            Exception e
    ) {
        log.error("handle not found entity exception");

        var errorResponseDto = new ErrorResponseDto(
                "Not found",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponseDto);
    }

    @ExceptionHandler(exception = InsufficientFundsException.class)
    public ResponseEntity<ErrorResponseDto> handleInsufficientFounds(
            Exception e
    ) {
        log.error("handle insufficient founds");

        var errorResponseDto = new ErrorResponseDto(
                "Insufficient founds",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(errorResponseDto);
    }

    @ExceptionHandler(exception = AccountNotActiveException.class)
    public ResponseEntity<ErrorResponseDto> handleAccountNotActive(
            Exception e
    ) {
        log.error("Operation with non active account");

        var errorResponseDto = new ErrorResponseDto(
                "Operation with not active account",
                e.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(errorResponseDto);
    }
}
