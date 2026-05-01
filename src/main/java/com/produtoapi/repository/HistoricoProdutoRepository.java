package com.produtoapi.repository;


import com.produtoapi.model.HistoricosProdutos;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HistoricoProdutoRepository extends JpaRepository<HistoricosProdutos,Long> {



}
