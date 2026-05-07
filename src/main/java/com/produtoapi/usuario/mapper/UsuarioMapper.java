package com.produtoapi.usuario.mapper;

import com.produtoapi.usuario.domain.Usuario;
import com.produtoapi.usuario.dto.UsuarioRequestDTO;
import com.produtoapi.usuario.dto.UsuarioResponseDTO;
import org.mapstruct.Mapper;



@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioResponseDTO toResponse(Usuario usuario);

    Usuario toEntity(UsuarioRequestDTO dto);


}