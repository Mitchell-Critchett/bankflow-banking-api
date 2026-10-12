package ru.bankflow.client.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ClientResponse (
    UUID id,
    String fullName,
    String email,
    OffsetDateTime createdAt
){}
