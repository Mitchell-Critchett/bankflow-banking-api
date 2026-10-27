package ru.bankflow.modules.client;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ClientRepository
        extends JpaRepository<ClientEntity, UUID> {

    boolean existsByEmail(String email);
}
