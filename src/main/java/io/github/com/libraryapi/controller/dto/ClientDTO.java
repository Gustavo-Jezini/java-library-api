package io.github.com.libraryapi.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "Client")
public record ClientDTO (
    @NotBlank(message = "Inválido")
    String clientId,
    @NotBlank(message = "Inválido")
    String clientSecret,
    @NotBlank(message = "Inválido")
    String redirectUri,
    String scope
) {}
