package ru.bankflow.client.Client;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bankflow.client.Client.dto.ClientResponse;
import ru.bankflow.client.Client.dto.CreateClientRequest;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repository;

    @Transactional
    public ClientResponse create(CreateClientRequest request) {

        if (repository.existsByEmail(request.email())) {
            throw new IllegalArgumentException(
                    "Клиент с такой почтой уже существует"
            );
        }

        Client client = new Client(
                request.fullName(),
                request.email());

        Client saved = repository.save(client);

        return toResponse(saved);
    }

    @Transactional
    public ClientResponse findById(UUID id) {
        Client client = repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(String.format("Клиент с id= {} не найден", id.toString()))
                );
        return toResponse(client);
    }

    private  ClientResponse toResponse(Client client) {
        return new ClientResponse(
            client.getId(),
            client.getFullName(),
            client.getEmail(),
            client.getCreatedAt()
        );
    }
}


