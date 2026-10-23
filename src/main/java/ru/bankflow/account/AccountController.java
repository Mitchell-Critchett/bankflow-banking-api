package ru.bankflow.account;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final static Logger log = LoggerFactory.getLogger(AccountController.class);

    private final AccountService service;




    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountByID(
            @PathVariable UUID id
    ) {
        log.info("called findAccoundById id= " + id);

        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(service.getAccountById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404)
                    .build();
        }
    }
}
