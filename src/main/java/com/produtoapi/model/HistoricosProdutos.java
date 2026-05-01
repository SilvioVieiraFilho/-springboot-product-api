package com.produtoapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.produtoapi.model.Produto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class HistoricosProdutos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantidadeAnterior;
    private int quantidadeNova;
    private int diferenca;

    private LocalDateTime dataRegistro;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    @JsonIgnore
    private Produto produto;


}