package ru.bankflow.modules.ledger;


import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bankflow.modules.ledger.dto.EntryResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/accounts/entries")
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

    @GetMapping("/findByAccount/{accountId}")
    public ResponseEntity<List<EntryResponse>> flowHistoryByAccount(
            @PathVariable(value = "accountId") UUID accountId,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber
    ) {
        log.info("called flowHistoryByAccount");

        return ResponseEntity.ok(entryService.findEntriesByAccount(accountId, pageSize, pageNumber));
    }
}
