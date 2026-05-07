package com.produtoapi.usuario.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDTO {

    private String token;
    private String type = "Bearer";
    private String email;
    private String role;

}
