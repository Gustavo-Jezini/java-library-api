package io.github.com.libraryapi.controller;

import io.github.com.libraryapi.controller.dto.UsuarioDTO;
import io.github.com.libraryapi.controller.mappers.UsuarioMapper;
import io.github.com.libraryapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários")
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Salvar", description = "Cadastrar novo usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cadastrado com sucesso")
    })
    public void salvar(@RequestBody UsuarioDTO dto) {
        var usuario = mapper.toEntity(dto);
        service.salvar(usuario);
    }
}
