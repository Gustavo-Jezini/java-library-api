package io.github.com.libraryapi.controller.mappers;

import io.github.com.libraryapi.controller.dto.UsuarioDTO;
import io.github.com.libraryapi.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity(UsuarioDTO dto);
}
