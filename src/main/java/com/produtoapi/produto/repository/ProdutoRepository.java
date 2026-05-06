package com.produtoapi.produto.repository;
import com.produtoapi.enums.ProdutoStatus;

import java.util.List;
import java.util.Optional;

import com.produtoapi.produto.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProdutoRepository extends JpaRepository<Produto,Long>,
	JpaSpecificationExecutor<Produto> {
	boolean existsByNomeAndPrecoAndStatus(String nome, Double preco, ProdutoStatus status);
List<Produto> findByNome(String nome);
Optional<Produto> findByNomeAndPrecoAndStatus(String nome, Double preco, ProdutoStatus status);


	
}
