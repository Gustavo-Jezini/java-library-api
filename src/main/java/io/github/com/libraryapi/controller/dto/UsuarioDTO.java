package io.github.com.libraryapi.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UsuarioDTO (
    @NotBlank(message = "Inválido")
    String login,
    @Email String email,
    @NotBlank(message = "Inválido")
    String senha,
    List<String> roles
) {}
