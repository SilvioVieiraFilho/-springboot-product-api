package com.produtoapi.produtos;

import com.produtoapi.exception.BusinessException;
import com.produtoapi.exception.ProdutoNotFoundException;
import com.produtoapi.produto.domain.Produto;
import com.produtoapi.produto.domain.ProdutoDomainService;
import com.produtoapi.produto.domain.ProdutoFactory;
import com.produtoapi.produto.dto.ProdutoRequestDTO;
import com.produtoapi.produto.dto.ProdutoResponseDTO;
import com.produtoapi.produto.enums.ProdutoStatus;
import com.produtoapi.produto.mapper.ProdutoMapper;
import com.produtoapi.produto.repository.ProdutoRepository;
import com.produtoapi.produto.service.ProdutoService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService service;

    @Mock private ProdutoRepository repository;
    @Mock private ProdutoMapper mapper;
    @Mock private ProdutoFactory factory;
    @Mock private ProdutoDomainService domain;

    @Nested
    class Listar {

        @Test
        void deveListarProdutos() {

            Produto produto = Produto.builder().id(1L).nome("Mouse").build();
            ProdutoResponseDTO dto = ProdutoResponseDTO.builder().id(1L).nome("Mouse").build();

            when(repository.findAll()).thenReturn(List.of(produto));
            when(mapper.toDTO(produto)).thenReturn(dto);

            List<ProdutoResponseDTO> result = service.listarTodos();

            assertEquals(1, result.size());
            verify(repository).findAll();
        }

        @Test
        void deveListarProdutosComMapper() {

            Produto produto = Produto.builder().id(1L).nome("Mouse").build();
            ProdutoResponseDTO dto = ProdutoResponseDTO.builder().id(1L).nome("Mouse").build();

            when(repository.findAll()).thenReturn(List.of(produto));
            when(mapper.toDTO(produto)).thenReturn(dto);

            List<ProdutoResponseDTO> result = service.listarTodos();

            assertEquals(1, result.size());
            assertEquals("Mouse", result.get(0).getNome());
        }
    }

    @Nested
    class Salvar {

        @Test
        void deveCriarNovoProduto() {

            ProdutoRequestDTO dto = ProdutoRequestDTO.builder()
                    .nome("Mouse")
                    .quantidade(1)
                    .preco(10.0)
                    .status(ProdutoStatus.ATIVO)
                    .build();

            Produto produto = Produto.builder().nome("Mouse").build();
            Produto salvo = Produto.builder().id(1L).nome("Mouse").build();
            ProdutoResponseDTO response = ProdutoResponseDTO.builder().id(1L).nome("Mouse").build();

            when(repository.findByNomeAndPrecoAndStatus(dto.getNome(), dto.getPreco(), dto.getStatus()))
                    .thenReturn(Optional.empty());

            when(factory.criar(dto)).thenReturn(produto);
            when(repository.save(produto)).thenReturn(salvo);
            when(mapper.toDTO(salvo)).thenReturn(response);

            ProdutoResponseDTO result = service.salvar(dto);

            assertNotNull(result);
            verify(domain).inicializar(produto);
        }

        @Test
        void deveAtualizarProdutoExistente() {

            ProdutoRequestDTO dto = ProdutoRequestDTO.builder()
                    .nome("Mouse")
                    .quantidade(10)
                    .preco(10.0)
                    .status(ProdutoStatus.ATIVO)
                    .build();

            Produto existente = Produto.builder().id(1L).nome("Mouse").build();
            Produto salvo = Produto.builder().id(1L).nome("Mouse").build();
            ProdutoResponseDTO response = ProdutoResponseDTO.builder().id(1L).nome("Mouse").build();

            when(repository.findByNomeAndPrecoAndStatus(dto.getNome(), dto.getPreco(), dto.getStatus()))
                    .thenReturn(Optional.of(existente));

            when(repository.save(existente)).thenReturn(salvo);
            when(mapper.toDTO(salvo)).thenReturn(response);

            ProdutoResponseDTO result = service.salvar(dto);

            assertNotNull(result);
            verify(domain).atualizar(existente, dto.getQuantidade());
        }

        @Test
        void deveCriarNovoProdutoComRetornoValido() {

            ProdutoRequestDTO dto = ProdutoRequestDTO.builder()
                    .nome("Mouse")
                    .quantidade(1)
                    .preco(10.0)
                    .status(ProdutoStatus.ATIVO)
                    .build();

            Produto produto = Produto.builder().nome("Mouse").build();
            Produto salvo = Produto.builder().id(1L).nome("Mouse").build();
            ProdutoResponseDTO response = ProdutoResponseDTO.builder().id(1L).nome("Mouse").build();

            when(repository.findByNomeAndPrecoAndStatus(any(), any(), any()))
                    .thenReturn(Optional.empty());

            when(factory.criar(dto)).thenReturn(produto);
            when(repository.save(produto)).thenReturn(salvo);
            when(mapper.toDTO(salvo)).thenReturn(response);

            ProdutoResponseDTO result = service.salvar(dto);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("Mouse", result.getNome());
        }
    }

    @Nested
    class Atualizar {

        @Test
        void deveAtualizarProdutoComSucesso() {

            Long id = 1L;

            ProdutoRequestDTO dto = ProdutoRequestDTO.builder()
                    .nome("Mouse Gamer")
                    .preco(150.0)
                    .quantidade(10)
                    .build();

            Produto produto = Produto.builder().id(id).nome("Mouse").build();
            ProdutoResponseDTO response = ProdutoResponseDTO.builder().id(id).nome("Mouse Gamer").build();

            when(repository.findById(id)).thenReturn(Optional.of(produto));
            when(repository.save(produto)).thenReturn(produto);
            when(mapper.toDTO(produto)).thenReturn(response);

            ProdutoResponseDTO result = service.atualizarProduto(id, dto);

            assertEquals("Mouse Gamer", result.getNome());
            verify(domain).atualizarDadosBasicos(produto, dto.getNome(), dto.getPreco(), dto.getQuantidade());
        }

        @Test
        void deveLancarErroQuandoNaoEncontrar() {

            when(repository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ProdutoNotFoundException.class,
                    () -> service.atualizarProduto(1L, new ProdutoRequestDTO()));
        }
    }

    @Nested
    class Deletar {

        @Test
        void deveDeletarProduto() {

            Produto produto = Produto.builder().id(1L).build();

            when(repository.findById(1L)).thenReturn(Optional.of(produto));

            service.deletarProduto(1L);

            verify(repository).delete(produto);
        }

        @Test
        void deveLancarErro() {

            when(repository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ProdutoNotFoundException.class,
                    () -> service.deletarProduto(1L));
        }

        @Test
        void deveDeletarProdutoComSucesso() {

            Produto produto = Produto.builder().id(1L).build();

            when(repository.findById(1L)).thenReturn(Optional.of(produto));

            service.deletarProduto(1L);

            verify(repository).delete(produto);
        }
    }

    @Nested
    class Buscar {

        @Test
        void deveBuscarPorId() {

            Produto produto = Produto.builder().id(1L).build();
            ProdutoResponseDTO dto = ProdutoResponseDTO.builder().id(1L).build();

            when(repository.findById(1L)).thenReturn(Optional.of(produto));
            when(mapper.toDTO(produto)).thenReturn(dto);

            ProdutoResponseDTO result = service.buscarPorId(1L);

            assertEquals(1L, result.getId());
        }
    }

    @Nested
    class Filtro {

        @Test
        void deveLancarErroQuandoTudoNull() {
            assertThrows(BusinessException.class,
                    () -> service.buscarFiltro(null, null, null, null));
        }

        @Test
        void deveTrocarPrecoMinMax() {

            Produto produto = Produto.builder().id(1L).nome("Mouse").build();

            when(repository.findAll(any(Specification.class)))
                    .thenReturn(List.of(produto));

            when(mapper.toDTO(produto))
                    .thenReturn(ProdutoResponseDTO.builder().id(1L).nome("Mouse").build());

            service.buscarFiltro(null, null, 200.0, 100.0);

            verify(repository).findAll(any(Specification.class));
        }

        @Test
        void deveLancarErroListaVazia() {

            when(repository.findAll(any(Specification.class)))
                    .thenReturn(List.of());

            assertThrows(BusinessException.class,
                    () -> service.buscarFiltro("x", null, null, null));
        }

        @Test
        void deveBuscarComStatus() {

            Produto produto = Produto.builder().id(1L).build();

            when(repository.findAll(any(Specification.class)))
                    .thenReturn(List.of(produto));

            when(mapper.toDTO(produto))
                    .thenReturn(ProdutoResponseDTO.builder().id(1L).build());

            List<ProdutoResponseDTO> result =
                    service.buscarFiltro(null, ProdutoStatus.ATIVO, null, null);

            assertEquals(1, result.size());
        }

        @Test
        void deveBuscarComNomeEStatus() {

            Produto produto = Produto.builder().id(1L).nome("Mouse").build();

            when(repository.findAll(any(Specification.class)))
                    .thenReturn(List.of(produto));

            when(mapper.toDTO(produto))
                    .thenReturn(ProdutoResponseDTO.builder().id(1L).nome("Mouse").build());

            List<ProdutoResponseDTO> result =
                    service.buscarFiltro("Mouse", ProdutoStatus.ATIVO, null, null);

            assertEquals(1, result.size());
        }

        @Test
        void deveBuscarComPrecoMin() {

            Produto produto = Produto.builder().id(1L).build();

            when(repository.findAll(any(Specification.class)))
                    .thenReturn(List.of(produto));

            when(mapper.toDTO(produto))
                    .thenReturn(ProdutoResponseDTO.builder().id(1L).build());

            List<ProdutoResponseDTO> result =
                    service.buscarFiltro(null, null, 10.0, null);

            assertEquals(1, result.size());
        }

        @Test
        void deveBuscarComPrecoMax() {

            Produto produto = Produto.builder().id(1L).build();

            when(repository.findAll(any(Specification.class)))
                    .thenReturn(List.of(produto));

            when(mapper.toDTO(produto))
                    .thenReturn(ProdutoResponseDTO.builder().id(1L).build());

            List<ProdutoResponseDTO> result =
                    service.buscarFiltro(null, null, null, 100.0);

            assertEquals(1, result.size());
        }

        @Test
        void deveBuscarComTodosFiltros() {

            Produto produto = Produto.builder().id(1L).nome("Mouse").build();

            when(repository.findAll(any(Specification.class)))
                    .thenReturn(List.of(produto));

            when(mapper.toDTO(produto))
                    .thenReturn(ProdutoResponseDTO.builder().id(1L).nome("Mouse").build());

            List<ProdutoResponseDTO> result =
                    service.buscarFiltro("Mouse", ProdutoStatus.ATIVO, 10.0, 100.0);

            assertEquals(1, result.size());
        }

        @Test
        void deveLancarErroQuandoNaoEncontrarProdutos() {

            when(repository.findAll(any(Specification.class)))
                    .thenReturn(List.of());

            assertThrows(BusinessException.class,
                    () -> service.buscarFiltro("Mouse", null, null, null));
        }
    }
}