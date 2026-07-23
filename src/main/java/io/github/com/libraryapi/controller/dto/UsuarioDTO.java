package io.github.com.libraryapi.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Schema(name = "Usuario")
public record UsuarioDTO (
    @NotBlank(message = "Inválido")
    String login,
    @Email String email,
    @NotBlank(message = "Inválido")
    String senha,
    List<String> roles
) {}
