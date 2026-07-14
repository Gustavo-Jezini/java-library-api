package io.github.com.libraryapi.repository;

import io.github.com.libraryapi.model.Autor;
import io.github.com.libraryapi.model.Livro;
import io.github.com.libraryapi.model.model.GeneroLivro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class LivroRepositoryTest {

    @Autowired
    LivroRepository livroRepository;

    @Autowired
    AutorRepository autorRepository;

    @Test
    void salvarTest() {
        Livro novoLivro = new Livro();

        novoLivro.setIsbn("098342-8790812");
        novoLivro.setGenero(GeneroLivro.ROMANCE);
        novoLivro.setPreco(BigDecimal.valueOf(89.9));
        novoLivro.setDataPublicacao(LocalDate.of(1969,6,9));
        novoLivro.setTitulo("A colina dos gatos miantes");

        Autor autor = autorRepository
                .findById(UUID.fromString("58c8637d-3b64-47cc-b876-88b3a34df585"))
                .orElse(null);

        System.out.println(autor);
        novoLivro.setAutor(autor);

        livroRepository.save(novoLivro);
    }

    @Test
    void salvarCascadeTest() {
        Livro novoLivro = new Livro();

        novoLivro.setIsbn("098342-8790812");
        novoLivro.setGenero(GeneroLivro.ROMANCE);
        novoLivro.setPreco(BigDecimal.valueOf(89.9));
        novoLivro.setDataPublicacao(LocalDate.of(1969,6,9));
        novoLivro.setTitulo("A colina dos gatos miantes");

        // Esse autor não está no banco de dados e portanto ainda não tem um ID
        // Porem, por conta da propriade @ManyToOne(cascade = CascadeType.ALL) ele será salvo na tabela de autor
        // e o livro poderá ser salvo tambem.
        // Dessa forma, pode-se criar um autor ao mesmo tempo que cria um livro
        // @ManyToOne(cascade = CascadeType.ALL)
        Autor autor = new Autor();
        autor.setNome("Asthon Curther");
        autor.setNacionalidade("Safado");
        autor.setDataNascimento(LocalDate.of(1950,1,31));

        novoLivro.setAutor(autor);

        livroRepository.save(novoLivro);
    }

    @Test
    void buscarLivroTest() {
        UUID id = UUID.fromString("0aa9ad24-2496-46cf-87fd-df33490391e9");
        Livro livro = livroRepository.findById(id).orElse(null);

        UUID idAutor = UUID.fromString("6e992fd1-efc1-49b6-9d2e-27f3d235c6e5");
        Autor autor = autorRepository.findById(idAutor).orElse(null);
    }

    @Test
    void listarLivrosComQueryJPQL() {
        var resultado = livroRepository.listarTodosOrdenarPorTituloAndPreco();
        resultado.forEach(System.out::println);
    }

    @Test
    void listarAutoresDosLivrosTest() {
        var resultado = livroRepository.listarAutoresDosLivros();
        resultado.forEach(System.out::println);
    }

    @Test
    void listarGeneroDosAutoresBrasileirosTest() {
        var resultado = livroRepository.listarGeneroDosAutoresBrasileiros();
        resultado.forEach(System.out::println);
    }

    @Test
    void findByGeneroTest() {
        var resultado = livroRepository.findByGenero(GeneroLivro.BIOGRAFIA, "titulo");
        resultado.forEach(System.out::println);
    }

    @Test
    void findByGeneroPositionalTest() {
        var resultado = livroRepository.findByGeneroPositional(GeneroLivro.BIOGRAFIA, "titulo");
        resultado.forEach(System.out::println);
    }

    @Test
    void deletePorGeneroTest() {
        livroRepository.deleteByGenero(GeneroLivro.ROMANCE);
    }
}
