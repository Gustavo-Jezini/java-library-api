package io.github.com.libraryapi.controller;

import io.github.com.libraryapi.controller.dto.ClientDTO;
import io.github.com.libraryapi.controller.mappers.ClientMapper;
import io.github.com.libraryapi.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("clients")
public class ClientController {

    private final ClientService service;
    private final ClientMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('GERENTE')")
    @ResponseStatus(HttpStatus.CREATED)
    public void salvar(@RequestBody @Valid ClientDTO dto) {
        var client = mapper.toEntity(dto);
        service.salvar(client);
    }
}
