package io.github.com.libraryapi.service;

import io.github.com.libraryapi.model.Livro;
import io.github.com.libraryapi.model.Usuario;
import io.github.com.libraryapi.model.model.GeneroLivro;
import io.github.com.libraryapi.repository.LivroRepository;
import io.github.com.libraryapi.security.SecurityService;
import io.github.com.libraryapi.validator.LivroValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.github.com.libraryapi.repository.specs.LivroSpecs.*;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final LivroValidator validator;
    private final SecurityService securityService;

    public Livro salvar(Livro livro) {
        validator.validator(livro);

        Usuario usuario = securityService.obterUsuarioLogado();
        livro.setUsuario(usuario);
        return livroRepository.save(livro);
    }

    public Optional<Livro> findById(UUID id) {
        return livroRepository.findById(id);
    }

    public void delete(Livro livro) {
        livroRepository.delete(livro);
    }

    // isbn, titulo, nome autor*, genero, ano de publicacao
    public Page<Livro> pesquisa(
            String isbn,
            String titulo,
            String nomeAutor,
            GeneroLivro genero,
            Integer anoPublicacao,
            Integer pagina,
            Integer tamanhoPagina
    ) {
        // caso em que os dados existem -> Não tem caso base
//        Specification<Livro> specs = Specification
//                .where(LivroSpecs.isbnEqual(isbn))
//                .and(LivroSpecs.tituloLike(titulo))
//                .and(LivroSpecs.generoEqual(genero));

        // select * from livro where 0 = 0
        Specification<Livro> specs = Specification.where(
                ((root, query, criteriaBuilder) -> criteriaBuilder.conjunction())
        );

        if (isbn != null) {
            specs = specs.and(isbnEqual(isbn));
        }

        if (genero != null) {
            specs = specs.and(generoEqual(genero));
        }

        if (titulo != null) {
            specs = specs.and(tituloLike(titulo));
        }

        if (anoPublicacao != null) {
            specs = specs.and(anoPublicacaoEqual(anoPublicacao));
        }

        if (nomeAutor != null) {
            specs = specs.and(nomeAutorLike(nomeAutor));
        }

        Pageable pageRequest = PageRequest.of(pagina, tamanhoPagina);

        return livroRepository.findAll(specs, pageRequest);
    }

    public void update(Livro livro) {
        if (livro.getId() == null) {
            throw new IllegalArgumentException("Para atualizar é necessário ter o livro no banco de dados");
        }

        livroRepository.save(livro);
    }
}
