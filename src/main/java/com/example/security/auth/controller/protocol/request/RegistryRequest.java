package com.example.security.auth.controller.protocol.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistryRequest(
        @NotBlank
        @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
        String username,
        @NotBlank
        @Size(min = 6, message = "Password must have at least 6 characters")
        String password
) {
}
