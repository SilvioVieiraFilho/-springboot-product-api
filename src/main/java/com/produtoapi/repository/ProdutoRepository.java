package com.produtoapi.repository;
import com.produtoapi.enums.ProdutoStatus;
import com.produtoapi.model.Produto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProdutoRepository extends JpaRepository<Produto,Long>,
	JpaSpecificationExecutor<Produto> {
	boolean existsByNomeAndPrecoAndStatus(String nome, Double preco, ProdutoStatus status);
List<Produto> findByNome(String nome);
Optional<Produto> findByNomeAndPrecoAndStatus(String nome, Double preco, ProdutoStatus status);


	
}
