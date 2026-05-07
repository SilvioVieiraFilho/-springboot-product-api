package com.produtoapi.usuario.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequestDTO {

    private String email;
    private String senha;
}
