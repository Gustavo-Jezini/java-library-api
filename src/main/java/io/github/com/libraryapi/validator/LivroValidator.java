package io.github.com.libraryapi.validator;

import io.github.com.libraryapi.exceptions.CampoInvalidoException;
import io.github.com.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.com.libraryapi.model.Livro;
import io.github.com.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LivroValidator {

    private static final int ANO_EXIGENCIA_PRECO = 2020;

    @Autowired
    LivroRepository livroRepository;

    public void validator(Livro livro) {
        if(existeLivroComIsbn(livro.getIsbn())) {
            throw new RegistroDuplicadoException("ISBN Já cadastrado.");
        }

        if(isPrecoObrigatorioNulo(livro)) {
            throw new CampoInvalidoException("preco", "Para livros com ano de publicação a partir de 2020, o preço é obrigatório.");
        }
    }

    private boolean isPrecoObrigatorioNulo(Livro livro) {
        return livro.getPreco() == null &&
                livro.getDataPublicacao().getYear() >= ANO_EXIGENCIA_PRECO;
    }

    private boolean existeLivroComIsbn(String isbn) {
        return livroRepository.existsByIsbn(isbn);
    }
}
