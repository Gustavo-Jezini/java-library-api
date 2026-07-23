package io.github.com.libraryapi.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "Autor")
public record AutorDTO(
        UUID id,

        @NotBlank(message = "Campo Obrigatório") // Para strings -> não nula e não vazia != ''
        @Size(max = 100, min = 2, message = "Campo fora do tamanho padrão")
        String nome,

        @Past(message = "Não pode ser um data futura")
        @NotNull(message = "Campo Obrigatório")
        LocalDate dataNascimento,

        @Size(max = 50, min = 2, message = "Campo fora do tamanho padrão")
        @NotBlank(message = "Campo Obrigatório")
        String nacionalidade
) {}
