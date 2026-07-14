package io.github.com.libraryapi.validator;

import io.github.com.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.com.libraryapi.model.Autor;
import io.github.com.libraryapi.repository.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AutorValidator {

    private final AutorRepository repository;

    public void validar(Autor autor) {
        if(existeAutorCadastrado(autor)) {
            throw new RegistroDuplicadoException("Autor já cadastrado!");
        }
    }

    private boolean existeAutorCadastrado(Autor autor) {
        Optional<Autor> autorEncontrado = repository.findByNomeAndDataNascimentoAndNacionalidade(
                autor.getNome(),
                autor.getDataNascimento(),
                autor.getNacionalidade()
        );

        if (autor.getId() == null) {
            return autorEncontrado.isPresent();
        }

        return autorEncontrado.isPresent()
                && !autor.getId().equals(autorEncontrado.get().getId());
    }
}
