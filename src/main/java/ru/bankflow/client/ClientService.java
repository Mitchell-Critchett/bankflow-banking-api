package ru.bankflow.client;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bankflow.client.dto.ClientResponse;
import ru.bankflow.client.dto.CreateClientRequest;

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

        ClientEntity clientEntity = new ClientEntity(
                request.fullName(),
                request.email());

        ClientEntity saved = repository.save(clientEntity);

        return toResponse(saved);
    }

    @Transactional
    public ClientResponse findById(UUID id) {
        ClientEntity clientEntity = repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(String.format("Клиент с id=%s не найден", id))
                );
        return toResponse(clientEntity);
    }

    private  ClientResponse toResponse(ClientEntity clientEntity) {
        return new ClientResponse(
            clientEntity.getId(),
            clientEntity.getFullName(),
            clientEntity.getEmail(),
            clientEntity.getCreatedAt()
        );
    }
}


