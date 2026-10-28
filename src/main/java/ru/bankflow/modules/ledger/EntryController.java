package ru.bankflow.modules.ledger;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bankflow.modules.ledger.dto.EntryResponse;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/accounts/entry")
@RequiredArgsConstructor
public class EntryController {

    private final Logger log = LoggerFactory.getLogger(EntryController.class);

    private final EntryService entryService;

    @GetMapping("/{id}")
    public ResponseEntity<EntryResponse> getById(
            @PathVariable UUID id
    ){
        log.info("find by id called");

        return ResponseEntity.ok(entryService.findById(id));
    }
}
