package ru.bankflow.account;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bankflow.account.dto.AccountCreateResponse;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final static Logger log = LoggerFactory.getLogger(AccountController.class);

    private final AccountService service;

    @PostMapping
    public ResponseEntity<AccountCreateResponse> createAccount(
            @RequestBody @Valid AccountCreateResponse accountCreateResponseToCreate
    ) {
        log.info("called createAccount");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createAccount(accountCreateResponseToCreate));
    }


    @GetMapping("/{id}")
    public ResponseEntity<AccountCreateResponse> getAccountByID(
            @PathVariable UUID id
    ) {
        log.info("called findAccoundById id= {}", id);

        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(service.getAccountById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404)
                    .build();
        }
    }

    @GetMapping("/searchByClient/{clientId}")
    public  ResponseEntity<List<AccountCreateResponse>> searchAccountsByClient(
            @PathVariable UUID clientId
    ) {
        log.info("called searchAccountsByClient");

        return ResponseEntity.ok(service.findAccountsByClient(clientId));
    }
}
