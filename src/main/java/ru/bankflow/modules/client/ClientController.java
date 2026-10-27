package ru.bankflow.modules.client;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.bankflow.modules.client.dto.ClientResponse;
import ru.bankflow.modules.client.dto.CreateClientRequest;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientResponse create(
            @Valid @RequestBody CreateClientRequest request
    ) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    public ClientResponse getById(@PathVariable UUID id) {
        return service.findById(id);
    }
}
