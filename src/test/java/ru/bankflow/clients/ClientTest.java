package ru.bankflow.clients;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.bankflow.modules.client.ClientRepository;
import ru.bankflow.modules.client.ClientService;
import ru.bankflow.modules.client.dto.ClientResponse;
import ru.bankflow.modules.client.dto.CreateClientRequest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class ClientTest {

    @Container
    static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("bankflow_test")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private ClientService clientService;

    private ClientRepository clientRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldCreateClientWithCorrectDataAndGetThatClientById() throws Exception {

        ClientResponse responseCreated = clientService.create(
                new CreateClientRequest("Test client", "test@example.com"));

        assertEquals("Test client", responseCreated.fullName());
        assertEquals("test@example.com", responseCreated.email());

        ClientResponse response = clientService.findById(responseCreated.id());

        assertEquals("Test client", response.fullName());
        assertEquals("test@example.com", response.email());
    }

    @Test
    void shouldThrow404WhenIdNotExist(){

        try {
            ClientResponse responseFounded = clientService.findById(UUID.randomUUID());
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("не найден"), "Подстрока не найдена!");
        }
    }



}
