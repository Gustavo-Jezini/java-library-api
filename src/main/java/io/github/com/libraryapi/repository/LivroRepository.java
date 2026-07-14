package io.github.com.libraryapi.repository;

import io.github.com.libraryapi.model.Autor;
import io.github.com.libraryapi.model.Livro;
import io.github.com.libraryapi.model.model.GeneroLivro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID>, JpaSpecificationExecutor<Livro> {

    // Query method
    // select * from livro l where l.id_autor = UUID
    // https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
    List<Livro> findByAutor(Autor autor);

    boolean existsByIsbn(String isbn);


    // JPQL -> Referencia as entidades e as propriedades
    // Ou seja, não importa como está na tabela, importa como está na @Entity Livro
    @Query(" select l from Livro as l order by l.titulo, l.preco ")
    List<Livro> listarTodosOrdenarPorTituloAndPreco();

    @Query(" select a from Livro l join l.autor a ")
    List<Autor> listarAutoresDosLivros();

    @Query("""
    select l.genero
    from Livro l
    join l.autor a 
    where a.nacionalidade = 'Brasileiro'
    order by l.genero
    """)
    List<String> listarGeneroDosAutoresBrasileiros();

    // Named parameters
    @Query(" select l from Livro l where l.genero = :podeSerQualquerNomeAqui order by :paramOrdenacao ")
    List<Livro> findByGenero(
            @Param("podeSerQualquerNomeAqui") GeneroLivro generoLivro,
            @Param("paramOrdenacao") String nomePropriedade
            );

    // Positional Parameters
    @Query(" select l from Livro l where l.genero = ?1 order by ?2 ")
    List<Livro> findByGeneroPositional(GeneroLivro generoLivro,String nomePropriedade);

    @Transactional
    @Modifying
    @Query(" delete from Livro where genero = ?1  ")
    void deleteByGenero(GeneroLivro genero);

    boolean existsByAutor(Autor autor);
}
