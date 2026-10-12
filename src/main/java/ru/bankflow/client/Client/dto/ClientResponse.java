package ru.bankflow.client.Client.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ClientResponse (
    UUID id,
    String fullName,
    String email,
    OffsetDateTime createdAt
){}
