package com.produtoapi.historicoproduto.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.produtoapi.produto.domain.Produto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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