package com.produtoapi.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.produtoapi.dto.ProdutoRequestDTO;
import com.produtoapi.dto.ProdutoResponseDTO;
import com.produtoapi.mapper.ProdutoMapper;
import com.produtoapi.model.*;
import com.produtoapi.repository.ProdutoRepository;

import tools.jackson.databind.ext.jdk8.Jdk8OptionalSerializer;


@Service
public class ProdutoService {

	@Autowired
	ProdutoMapper mapper;
	@Autowired
	ProdutoRepository produtorepository;

	public List<ProdutoResponseDTO> listarTodos() {

		return produtorepository.findAll().stream().map(mapper ::toDTO).collect(Collectors.toList());

	}

	public ProdutoResponseDTO  salvar(ProdutoRequestDTO dto) {

		   Produto produto = mapper.toEntity(dto);
		    Produto salvo = produtorepository.save(produto);

		   
		    return mapper.toDTO(salvo);

		   
	}

	public void deletarProduto(Long id) {

		produtorepository.deleteById(id);

	}

	public ProdutoResponseDTO update(Long id, ProdutoRequestDTO dto) {

	    Optional<Produto> produtoExistente = produtorepository.findById(id);

	    if (produtoExistente.isPresent()) {

	        Produto p = produtoExistente.get();

	        // atualiza os dados com base no DTO
	        p.setNome(dto.getNome());
	        p.setQuantidade(dto.getQuantidade());
	        p.setPreco(dto.getPreco());
	        p.setStatus(dto.getStatus());

	        Produto atualizado = produtorepository.save(p);

	        // converte para DTO de resposta
	        return mapper.toDTO(atualizado);

	    } else {
	        throw new RuntimeException("Produto não encontrado");
	    }
	}
	public ProdutoResponseDTO buscarPorId(Long id) {

	    Produto produto = produtorepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

	    return mapper.toDTO(produto);
	}
	
	
	public List<ProdutoResponseDTO> salvarLista (List<ProdutoRequestDTO> listaDTO){
		
		
		 List <Produto> lista = mapper.toEntityList(listaDTO);
		   List <Produto> salvo = produtorepository.saveAll(lista);
		
		return mapper.toDTOList(salvo);
		
	}
	

}
