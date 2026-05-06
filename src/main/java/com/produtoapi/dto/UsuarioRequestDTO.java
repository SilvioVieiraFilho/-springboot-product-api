package com.produtoapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UsuarioRequestDTO {

    private String email;
    private String senha;
}
