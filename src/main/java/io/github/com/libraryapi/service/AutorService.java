package io.github.com.libraryapi.service;

import io.github.com.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.com.libraryapi.model.Autor;
import io.github.com.libraryapi.model.Usuario;
import io.github.com.libraryapi.repository.AutorRepository;
import io.github.com.libraryapi.repository.LivroRepository;
import io.github.com.libraryapi.security.SecurityService;
import io.github.com.libraryapi.validator.AutorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final SecurityService securityService;
    private final AutorRepository repository;
    private final AutorValidator validator;
    private final LivroRepository livroRepository;

    public Optional<Autor>  obterPorId(UUID id) {
        return repository.findById(id);
    }

    public List<Autor> pesquisa(String nome, String nacionalidade) {
        var autor = new Autor();

        autor.setNome(nome);
        autor.setNacionalidade(nacionalidade);

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING); // 3 estratégias.
                // 1 - Começa com a string
                // 2 - finaliza com a string
                // 3 - Está no meio da string

        Example<Autor> autorExample = Example.of(autor, matcher);

        return repository.findAll(autorExample);
    }

    public Autor salvar(Autor autor) {
        validator.validar(autor);
        Usuario usuario = securityService.obterUsuarioLogado();
        autor.setUsuario(usuario);
        return repository.save(autor);
    }

    public void atualizar(Autor autor) {
        if (autor.getId() == null) {
            throw new IllegalArgumentException("Para atualizar é necessário ter o autor no banco de dados");
        }
        validator.validar(autor);
        repository.save(autor);
    }

    public boolean possuiLivro(Autor autor) {
        return livroRepository.existsByAutor(autor);
    }

    public void deletarAutor(Autor autor) {
        if(possuiLivro(autor)) {
            throw new OperacaoNaoPermitidaException(
                    "Não é permitido excluir Autor que possui livros cadastrados!"
            );
        }
        repository.delete(autor);
    }
}
