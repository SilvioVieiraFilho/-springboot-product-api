package com.produtoapi.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.produtoapi.specification.ProdutoSpecification;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.produtoapi.dto.ProdutoRequestDTO;
import com.produtoapi.dto.ProdutoResponseDTO;
import com.produtoapi.enums.ProdutoStatus;
import com.produtoapi.exception.BusinessException;
import com.produtoapi.exception.ProdutoNotFoundExcepetion;
import com.produtoapi.mapper.ProdutoMapper;
import com.produtoapi.model.*;
import com.produtoapi.repository.ProdutoRepository;


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

		
		Optional <Produto> produtoExistente = produtorepository.findByNomeAndPrecoAndStatus(dto.getNome(), dto.getPreco(), dto.getStatus());


		/**
		 * Evita duplicidade de produtos no estoque.
		 * Se já existir, incrementa a quantidade; senão, cria um novo.
		 */
		
		if(produtoExistente.isPresent()) {
			
			
			Produto produto = produtoExistente.get();
			produto.setQuantidade(produto.getQuantidade() + dto.getQuantidade());
			
			Produto atualizado = produtorepository.save(produto);
			
			return mapper.toDTO(atualizado);

			
		}

		    Produto novo = mapper.toEntity(dto);
		    System.out.println("Entity nome: " + novo.getNome());
		    Produto salvo = produtorepository.save(novo);

		    return mapper.toDTO(salvo);

		}
		    
	

	public void deletarProduto(Long id) {


			if (!produtorepository.existsById(id)) {
				throw new ProdutoNotFoundExcepetion(id);
			}

			produtorepository.deleteById(id);
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



	public List<ProdutoResponseDTO> buscarFiltro(
			String nome,
			ProdutoStatus status,
			Double precoMin,
			Double precoMax
	) {

		if (nome == null && status == null && precoMin == null && precoMax == null) {
			throw new BusinessException("Informe pelo menos um filtro para a busca");
		}

		Specification<Produto> spec = (root, query, cb) -> cb.conjunction();

		if (nome != null) {
			spec = spec.and(ProdutoSpecification.nome(nome));
		}

		if (status != null) {
			spec = spec.and(ProdutoSpecification.status(status));
		}

		if (precoMin != null) {
			spec = spec.and(ProdutoSpecification.minPreco(precoMin));
		}

		if (precoMax != null) {
			spec = spec.and(ProdutoSpecification.maxPreco(precoMax));
		}

		List<Produto> produtos = produtorepository.findAll(spec);

		if (produtos.isEmpty()) {
			throw new BusinessException("Nenhum produto encontrado com os filtros informados");
		}

		return produtos.stream()
				.map(mapper::toDTO)
				.toList();
	}

}
