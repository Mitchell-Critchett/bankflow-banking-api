package ru.bankflow.client.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateClientRequest (

        @NotBlank
        @Size(max = 150)
        String fullName,

        @NotBlank
        @Email
        String email
) {}
