package io.github.com.libraryapi.repository;


import io.github.com.libraryapi.model.Autor;
import io.github.com.libraryapi.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class AutorRepositoryTest {

    @Autowired(required = true)
    AutorRepository repository;

    @Autowired
    LivroRepository livroRepository;

    @Test
    public void salvarTest() {
        Autor autor = new Autor();
        autor.setNome("José");
        autor.setNacionalidade("Brasileiro");
        autor.setDataNascimento(LocalDate.of(1950,1,31));

        var autorSalvo = repository.save(autor);

        System.out.println("Autor Salvo: " + autorSalvo);
    }

    @Test
    public void atualizarTest() {
        var id = UUID.fromString("6e992fd1-efc1-49b6-9d2e-27f3d235c6e5");

        Optional<Autor> possivelAutor = repository.findById(id);

        if (possivelAutor.isPresent()) {

            Autor autorEncontrado = possivelAutor.get();
            System.out.println("Dados do Autor: ");
            System.out.println(autorEncontrado);

            autorEncontrado.setDataNascimento(LocalDate.of(1960, 1, 30));

            repository.save(autorEncontrado);
        }
    }

    @Test
    public void listarTest() {
        List<Autor> lista = repository.findAll();

        lista.forEach(System.out::println);
    }

    @Test
    void listarLivrosAutor() {
        var autorId = UUID.fromString("6e992fd1-efc1-49b6-9d2e-27f3d235c6e5");
        var autor = repository.findById(autorId).get();

        List<Livro> livrosLista = livroRepository.findByAutor(autor);
        autor.setLivros(livrosLista);

        autor.getLivros().forEach(System.out::println);
    }
}
