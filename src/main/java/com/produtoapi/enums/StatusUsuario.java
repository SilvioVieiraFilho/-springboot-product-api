package com.produtoapi.enums;

public enum StatusUsuario {

    ATIVO,
    DESATIVADO,
    BLOQUEADO;

    public boolean podeLogar() {
        return this == ATIVO;
    }

    public void validarLogin() {
        if (!podeLogar()) {
            throw new RuntimeException("Usuário não pode logar: " + this);
        }
    }
}