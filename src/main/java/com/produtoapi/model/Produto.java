package com.produtoapi.model;

import com.produtoapi.enums.ProdutoStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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


public void setId(Long id) {
	this.id = id;
}
}

