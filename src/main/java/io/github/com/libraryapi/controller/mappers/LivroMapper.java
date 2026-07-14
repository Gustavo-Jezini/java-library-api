package io.github.com.libraryapi.controller.mappers;

import io.github.com.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.com.libraryapi.controller.dto.PesquisaLivroDTO;
import io.github.com.libraryapi.model.Livro;
import io.github.com.libraryapi.repository.AutorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = { AutorMapper.class })
public abstract class LivroMapper {

    @Autowired
    AutorRepository autorRepository;

    @Mapping(target = "autor", expression = "java( autorRepository.findById( dto.idAutor() ).orElse(null) )")
    public abstract Livro toEntity(CadastroLivroDTO dto);

    public abstract PesquisaLivroDTO toDTO(Livro livro); // precisa do autorDTO
}
