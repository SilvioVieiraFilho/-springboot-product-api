package com.produtoapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class UsuarioResponseDTO {

    private String token;
    private String type = "Bearer";
    private String email;
    private String role;

}
