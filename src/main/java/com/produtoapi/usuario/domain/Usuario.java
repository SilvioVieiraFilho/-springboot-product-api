package com.produtoapi.usuario.domain;

import com.produtoapi.usuario.enums.StatusUsuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)

    private String email;
    private String senha;

    private String role;
    @Enumerated(EnumType.STRING)
    private StatusUsuario status;

    public void validarParaLogin() {
        if (status == null) {
            throw new RuntimeException("Usuário sem status definido");
        }
        status.validarLogin();
    }

    public void aplicarDefaults() {
        if (status == null) {
            status = StatusUsuario.ATIVO;
        }
        if (role == null) {
            role = "USER";
        }
    }


}
