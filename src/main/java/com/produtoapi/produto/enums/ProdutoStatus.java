package com.produtoapi.produto.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ProdutoStatus {

    ATIVO,
    DESATIVADO,
    ESGOTADO;

    @JsonCreator
    public static ProdutoStatus from(String value) {
        if (value == null) return null;
        return ProdutoStatus.valueOf(value.toUpperCase());
    }
}