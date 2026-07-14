package io.github.com.libraryapi.controller.mappers;

import io.github.com.libraryapi.controller.dto.AutorDTO;
import io.github.com.libraryapi.model.Autor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // transforma em componente para ser injetável
public interface AutorMapper {

    Autor toEntity(AutorDTO dto);

    AutorDTO toDTO(Autor autor);
}
