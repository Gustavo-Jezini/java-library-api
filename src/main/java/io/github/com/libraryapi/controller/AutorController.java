package io.github.com.libraryapi.controller;

import io.github.com.libraryapi.controller.dto.AutorDTO;
import io.github.com.libraryapi.controller.mappers.AutorMapper;
import io.github.com.libraryapi.model.Autor;
import io.github.com.libraryapi.model.Usuario;
import io.github.com.libraryapi.security.SecurityService;
import io.github.com.libraryapi.service.AutorService;
import io.github.com.libraryapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("autores")
@RequiredArgsConstructor
@Tag(name = "Autores")
public class AutorController implements GenericController {

    private final AutorService service;
    private final UsuarioService usuarioService;
    private final AutorMapper mapper;

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Obter detalhes", description = "Retorna os dados de um autor cadastrado, a partir do seu id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autor encontrado"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado")
    })
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
    @PreAuthorize("hasAnyRole('GERENTE')")
    @Operation(summary = "Pesquisar", description = "Realiza busca de autores por nome e/ou nacionalidade")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
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
    @PreAuthorize("hasAnyRole('GERENTE')")
    @Operation(summary = "Salvar", description = "cadastrar novo autor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cadastrado com sucesso."),
            @ApiResponse(responseCode = "422", description = "Erro de validação"),
            @ApiResponse(responseCode = "409", description = "Autor já cadastrado.")
    })
    public ResponseEntity<Object> salvar(@RequestBody @Valid AutorDTO dto,
                                         Authentication authentication) {
        UserDetails usuarioAutenticado = (UserDetails) authentication.getPrincipal();
        Usuario usuario = usuarioService.obterPorLogin(usuarioAutenticado.getUsername());

        Autor autor = mapper.toEntity(dto);
        autor.setUsuario(usuario);
        service.salvar(autor);

        URI location = gerarHeaderLocation(autor.getId());

        return ResponseEntity.created(location).build();
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('GERENTE')")
    @Operation(summary = "Atualizar", description = "Atualiza os dados de um autor cadastrado")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado"),
            @ApiResponse(responseCode = "422", description = "Erro de validação")
    })
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
    @PreAuthorize("hasAnyRole('GERENTE')")
    @Operation(summary = "Deletar", description = "Deleta um autor cadastrado, desde que não possua livros vinculados")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado"),
            @ApiResponse(responseCode = "400", description = "Autor possui livros cadastrados")
    })
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
