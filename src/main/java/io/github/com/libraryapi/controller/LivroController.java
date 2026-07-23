package io.github.com.libraryapi.controller;

import io.github.com.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.com.libraryapi.controller.dto.PesquisaLivroDTO;
import io.github.com.libraryapi.controller.mappers.LivroMapper;
import io.github.com.libraryapi.model.Livro;
import io.github.com.libraryapi.model.model.GeneroLivro;
import io.github.com.libraryapi.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("livros")
@RequiredArgsConstructor
@Tag(name = "Livros")
public class LivroController implements GenericController {

    private final LivroService livroService;
    private final LivroMapper mapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Salvar", description = "Cadastrar novo livro")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cadastrado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Erro de validação")
    })
    public ResponseEntity<Object> create(
            @RequestBody @Valid CadastroLivroDTO dto
    ) {
        Livro livro = mapper.toEntity(dto);
        livroService.salvar(livro);

        var url = gerarHeaderLocation(livro.getId());
        return ResponseEntity.created(url).build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Obter detalhes", description = "Retorna os dados de um livro cadastrado, a partir do seu id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro encontrado"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public ResponseEntity<PesquisaLivroDTO> getDetails(
            @PathVariable("id") String id
    ) {
        return livroService.findById(UUID.fromString(id))
                .map(livro -> {
                    var dto = mapper.toDTO(livro);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Deletar", description = "Deleta um livro cadastrado")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public ResponseEntity<Object> delete(
            @PathVariable("id") String id
    ) {
        return livroService.findById(UUID.fromString(id))
                .map(livro -> {
                    livroService.delete(livro);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Pesquisar", description = "Realiza busca paginada de livros por isbn, título, gênero, autor e ano de publicação")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    public ResponseEntity<Page<PesquisaLivroDTO>> search(
            @RequestParam(value = "isbn", required = false)
            String isbn,
            @RequestParam(value = "titulo", required = false)
            String titulo,
            @RequestParam(value = "genero", required = false)
            GeneroLivro genero,
            @RequestParam(value = "nomeAutor", required = false)
            String nomeAutor,
            @RequestParam(value = "anoPublicacao", required = false)
            Integer anoPublicacao,
            @RequestParam(value = "pagina", defaultValue = "0")
            Integer pagina,
            @RequestParam(value = "tamanhoPagina", defaultValue = "")
            Integer tamanhoPagina
    ) {
        Page<Livro> paginaResultado = livroService.pesquisa(
                isbn,
                titulo,
                nomeAutor,
                genero,
                anoPublicacao,
                pagina,
                tamanhoPagina
        );

        Page<PesquisaLivroDTO> resultado = paginaResultado.map(mapper::toDTO);

        return ResponseEntity.ok(resultado);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Atualizar", description = "Atualiza os dados de um livro cadastrado")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
            @ApiResponse(responseCode = "422", description = "Erro de validação")
    })
    public  ResponseEntity<Object> update(
            @PathVariable("id") String id, @RequestBody @Valid CadastroLivroDTO dto
    ) {
        return livroService.findById(UUID.fromString(id))
                .map(livro -> {
                    Livro entidadeAux = mapper.toEntity(dto);

                    livro.setTitulo(entidadeAux.getTitulo());
                    livro.setIsbn(entidadeAux.getIsbn());
                    livro.setPreco(entidadeAux.getPreco());
                    livro.setAutor(entidadeAux.getAutor());
                    livro.setDataPublicacao(entidadeAux.getDataPublicacao());
                    livro.setGenero(entidadeAux.getGenero());

                    livroService.update(livro);

                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}





















