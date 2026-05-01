package com.produtoapi.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.produtoapi.domain.ProdutoDomainService;
import com.produtoapi.factory.ProdutoFactory;
import com.produtoapi.specification.ProdutoSpecification;


import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtorepository;
    private final ProdutoMapper mapper;
    private final ProdutoFactory factory;
    private final ProdutoDomainService domain;





    public List<ProdutoResponseDTO> listarTodos() {

        return produtorepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }


    public ProdutoResponseDTO salvar(ProdutoRequestDTO dto) {

        domain.validarStatusEsgotado(dto); // 🚨 bloqueia aqui

        Produto produto = produtorepository
                .findByNomeAndPrecoAndStatus(
                        dto.getNome(),
                        dto.getPreco(),
                        dto.getStatus()
                )
                .map(p -> domain.atualizar(p, dto))
                .orElseGet(() -> {
                    Produto novo = factory.criar(dto);
                    domain.inicializar(novo);
                    return novo;
                });

        return mapper.toDTO(produtorepository.save(produto));
    }

    public void deletarProduto(Long id) {


        Produto produto = produtorepository.findById(id)
                .orElseThrow(() -> new ProdutoNotFoundExcepetion(id));

        produtorepository.delete(produto);
    }

    public ProdutoResponseDTO atualizarProduto(Long id, ProdutoRequestDTO dto) {

        Produto produto = produtorepository.findById(id)
                .orElseThrow(() -> new ProdutoNotFoundExcepetion(id));

        domain.atualizarDadosBasicos(produto, dto);

        return mapper.toDTO(produtorepository.save(produto));
    }

    public ProdutoResponseDTO buscarPorId(Long id) {

        Produto produto = produtorepository.findById(id).orElseThrow(() -> new ProdutoNotFoundExcepetion(id));

        return mapper.toDTO(produto);
    }

    public List<ProdutoResponseDTO> salvarLista(List<ProdutoRequestDTO> listaDTO) {
        return listaDTO.stream()
                .map(this::salvar)
                .toList();
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
