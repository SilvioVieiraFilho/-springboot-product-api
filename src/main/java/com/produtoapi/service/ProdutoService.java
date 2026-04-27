package com.produtoapi.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.produtoapi.specification.ProdutoSpecification;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.produtoapi.dto.ProdutoRequestDTO;
import com.produtoapi.dto.ProdutoResponseDTO;
import com.produtoapi.enums.ProdutoStatus;
import com.produtoapi.exception.BusinessException;
import com.produtoapi.exception.ProdutoNotFoundExcepetion;
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

		return produtorepository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());

	}

	public ProdutoResponseDTO salvar(ProdutoRequestDTO dto) {

		Produto produto = mapper.toEntity(dto);
		Produto salvo = produtorepository.save(produto);

		return mapper.toDTO(salvo);

	}

	public void deletarProduto(Long id) {

		 Produto produto = produtorepository.findById(id)
			        .orElseThrow(() -> new ProdutoNotFoundExcepetion(id));

			    produtorepository.delete(produto);
	}

	public ProdutoResponseDTO atualizarProduto(Long id, ProdutoRequestDTO dto) {

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
			throw new ProdutoNotFoundExcepetion(id);
		}
	}

	public ProdutoResponseDTO buscarPorId(Long id) {

		Produto produto = produtorepository.findById(id).orElseThrow(() -> new ProdutoNotFoundExcepetion(id));

		return mapper.toDTO(produto);
	}

	public List<ProdutoResponseDTO> salvarLista(List<ProdutoRequestDTO> listaDTO) {

		List<Produto> lista = mapper.toEntityList(listaDTO);
		List<Produto> salvo = produtorepository.saveAll(lista);

		return mapper.toDTOList(salvo);

	}

	public List<ProdutoResponseDTO> buscarFiltro(String nome, ProdutoStatus status,  Double precoMin, Double precoMax) {

		if (nome == null && status == null && precoMax == null && precoMin == null) {

			throw new BusinessException("Informe pelo menos um filtro para a busca");

		}

		try {

			List<Produto> produtos = produtorepository.findAll(ProdutoSpecification.nome(nome).and(ProdutoSpecification
					.status(status)
					.and(ProdutoSpecification.minPreco(precoMin)
					.and(ProdutoSpecification.maxPreco(precoMax))))

			);

			if (produtos.isEmpty()) {

				throw new BusinessException("nenhum produto encontrado com os filtros que foram informados");
			}

			return produtos.stream().map(mapper::toDTO).toList();

		} catch (Exception e) {

			throw new RuntimeException("Erro ao buscar produtos", e);

		}
	}

}
