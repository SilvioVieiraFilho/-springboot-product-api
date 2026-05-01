package com.produtoapi.model;

import com.produtoapi.enums.ProdutoStatus;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Produto {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	@NotEmpty(message = "informe um nome.")
	private String nome;
	private int quantidade;
	private double preco;
    @Enumerated(EnumType.STRING)
    private ProdutoStatus status;

	@OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<HistoricosProdutos> historicos = new ArrayList<>();

	public void adicionarHistorico(HistoricosProdutos historico) { if (historicos == null) { historicos = new ArrayList<>(); } historicos.add(historico); historico.setProduto(this); }

}

