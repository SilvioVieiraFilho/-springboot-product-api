package com.produtoapi.usuario.enums;

import com.produtoapi.exception.BusinessException;

public enum StatusUsuario {

    ATIVO,
    DESATIVADO,
    BLOQUEADO;

    public boolean podeLogar() {
        return this == ATIVO;
    }

    public void validarLogin() {
        if (!podeLogar()) {
            throw new BusinessException("Usuário inativo ou bloqueado");        }
    }
}