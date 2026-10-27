package ru.bankflow.modules.account;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bankflow.modules.account.dto.CreateAccountRequest;
import ru.bankflow.modules.account.dto.AccountResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final static Logger log = LoggerFactory.getLogger(AccountController.class);

    private final AccountService service;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @RequestBody @Valid CreateAccountRequest accountCreateResponseToCreate
    ) {
        log.info("called createAccount");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createAccount(accountCreateResponseToCreate));
    }


    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountByID(
            @PathVariable UUID id
    ) {
        log.info("called findAccoundById id= {}", id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getAccountById(id));
    }

    @GetMapping("/searchByClient/{clientId}")
    public  ResponseEntity<List<AccountResponse>> searchAccountsByClient(
            @RequestParam UUID clientId,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber
    ) {
        log.info("called searchAccountsByClient");

        return ResponseEntity.ok(service.findAccountsByClient(clientId, pageSize, pageNumber));
    }

    @PostMapping("/testDebit/{id}")
    public ResponseEntity<AccountResponse> createTestDebitOn1000RUBS(@PathVariable UUID id)
    {
        return ResponseEntity.ok(service.createTestDebitOn1000RUBS(id));
    }
}
