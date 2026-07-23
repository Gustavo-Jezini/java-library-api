package io.github.com.libraryapi.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record ClientDTO (
    @NotBlank(message = "Inválido")
    String clientId,
    @NotBlank(message = "Inválido")
    String clientSecret,
    @NotBlank(message = "Inválido")
    String redirectUri,
    String scope
) {}
