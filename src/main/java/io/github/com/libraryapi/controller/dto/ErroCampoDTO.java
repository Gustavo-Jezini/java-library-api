package io.github.com.libraryapi.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ErroCampo")
public record ErroCampoDTO(String campo, String erro) {

}
