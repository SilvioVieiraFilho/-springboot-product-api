package com.produtoapi.model;

import jakarta.persistence.Entity;
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Long id;
	@NotEmpty(message = "informe um nome.")
	private String nome;
	private int quantidade;
	private double preco;
	private String status;


public void setId(Long id) {
	this.id = id;
}
}

