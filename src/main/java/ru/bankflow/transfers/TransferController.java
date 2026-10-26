package ru.bankflow.transfers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bankflow.transfers.dto.CreateTransferRequest;
import ru.bankflow.transfers.dto.TransferResponse;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final Logger log = LoggerFactory.getLogger(TransferController.class);

    private final TransferService service;

    @GetMapping("/{id}")
    public ResponseEntity<TransferResponse> getById(@PathVariable UUID id)
    {
        log.info("called getById");

        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping("/create/{idempotency_key}")
    public ResponseEntity<TransferResponse> create
    (
            @PathVariable UUID idempotency_key,
            @RequestBody @Valid CreateTransferRequest request
            )
    {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createTransfer(request, idempotency_key));
    }
}
