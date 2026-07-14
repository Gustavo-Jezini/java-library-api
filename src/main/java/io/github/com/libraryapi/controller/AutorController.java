package io.github.com.libraryapi.controller;

import io.github.com.libraryapi.controller.dto.AutorDTO;
import io.github.com.libraryapi.controller.mappers.AutorMapper;
import io.github.com.libraryapi.model.Autor;
import io.github.com.libraryapi.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("autores")
@RequiredArgsConstructor
public class AutorController implements GenericController {

    private final AutorService service;
    private final AutorMapper mapper;

    @GetMapping("{id}")
    public ResponseEntity<AutorDTO> obterDetalhes(@PathVariable("id") String id) {
      var idAutor = UUID.fromString(id);

      return service
              .obterPorId(idAutor)
              .map(autor -> {
                  AutorDTO dto = mapper.toDTO(autor);
                  return ResponseEntity.ok(dto);
              }).orElseGet(() -> ResponseEntity.notFound().build());
    };

    @GetMapping
    public ResponseEntity<List<AutorDTO>> pesquisa(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionalidade
    ) {
        List<Autor> autores = service.pesquisa(nome, nacionalidade);

        List<AutorDTO> lista = autores
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody @Valid AutorDTO dto) {
        Autor autor = mapper.toEntity(dto);
        service.salvar(autor);

        URI location = gerarHeaderLocation(autor.getId());

        return ResponseEntity.created(location).build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> atualizar(
            @PathVariable String id,
            @RequestBody @Valid  AutorDTO autorDTO
    ) {
        var autorId = UUID.fromString(id);
        Optional<Autor> autorOptional = service.obterPorId(autorId);

        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Autor autor = autorOptional.get();
        autor.setNome(autorDTO.nome());
        autor.setNacionalidade(autorDTO.nacionalidade());
        autor.setDataNascimento(autorDTO.dataNascimento());

        service.atualizar(autor);

        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletarAutor(@PathVariable String id) {
        var autorId = UUID.fromString(id);
        Optional<Autor> autorOptional = service.obterPorId(autorId);

        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.deletarAutor(autorOptional.get());
        return ResponseEntity.noContent().build();
    }

}
