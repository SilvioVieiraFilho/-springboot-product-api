package com.produtoapi.produtos;

import com.produtoapi.dto.ProdutoRequestDTO;
import com.produtoapi.dto.ProdutoResponseDTO;
import com.produtoapi.enums.ProdutoStatus;
import com.produtoapi.exception.BusinessException;
import com.produtoapi.exception.ProdutoNotFoundException;
import com.produtoapi.factory.ProdutoFactory;
import com.produtoapi.mapper.ProdutoMapper;
import com.produtoapi.model.Produto;
import com.produtoapi.repository.ProdutoRepository;
import com.produtoapi.service.ProdutoService;
import com.produtoapi.domain.ProdutoDomainService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

	@InjectMocks
	private ProdutoService service;

	@Mock
	private ProdutoRepository repository;

	@Mock
	private ProdutoMapper mapper;

	@Mock
	private ProdutoFactory factory;

	@Mock
	private ProdutoDomainService domain;



	@Nested
	class ListarTodos {

		@Test
		void deveListarTodosProdutos() {

			Produto produto = Produto.builder().id(1L).nome("Mouse").build();
			ProdutoResponseDTO dto = ProdutoResponseDTO.builder().id(1L).nome("Mouse").build();

			when(repository.findAll()).thenReturn(List.of(produto));
			when(mapper.toDTO(produto)).thenReturn(dto);

			List<ProdutoResponseDTO> result = service.listarTodos();

			assertEquals(1, result.size());
			assertEquals("Mouse", result.get(0).getNome());

			verify(repository).findAll();
		}
	}

	@Nested
	class Salvar {

		@Test
		void deveSalvarNovoProduto() {

			ProdutoRequestDTO dto = ProdutoRequestDTO.builder()
					.nome("Mouse")
					.quantidade(1)
					.preco(10.0)
					.status(ProdutoStatus.ATIVO)
					.build();

			Produto produto = Produto.builder().nome("Mouse").build();
			Produto salvo = Produto.builder().id(1L).nome("Mouse").build();
			ProdutoResponseDTO response = ProdutoResponseDTO.builder().id(1L).nome("Mouse").build();

			when(repository.findByNomeAndPrecoAndStatus(
					dto.getNome(), dto.getPreco(), dto.getStatus()
			)).thenReturn(Optional.empty());

			when(factory.criar(dto)).thenReturn(produto);
			when(repository.save(produto)).thenReturn(salvo);
			when(mapper.toDTO(salvo)).thenReturn(response);

			ProdutoResponseDTO result = service.salvar(dto);

			assertNotNull(result);
			assertEquals("Mouse", result.getNome());

			verify(domain).inicializar(produto);
			verify(factory).criar(dto);
			verify(repository).save(produto);
		}

		@Test
		void deveAtualizarProdutoExistenteNoSalvar() {

			ProdutoRequestDTO dto = ProdutoRequestDTO.builder().nome("Mouse").build();

			Produto existente = Produto.builder().id(1L).nome("Mouse").build();
			Produto atualizado = Produto.builder().id(1L).nome("Mouse Atualizado").build();
			ProdutoResponseDTO response = ProdutoResponseDTO.builder().id(1L).nome("Mouse Atualizado").build();

			when(repository.findByNomeAndPrecoAndStatus(
					dto.getNome(), dto.getPreco(), dto.getStatus()
			)).thenReturn(Optional.of(existente));

			when(domain.atualizar(existente, dto)).thenReturn(atualizado);
			when(repository.save(atualizado)).thenReturn(atualizado);
			when(mapper.toDTO(atualizado)).thenReturn(response);

			ProdutoResponseDTO result = service.salvar(dto);

			assertEquals("Mouse Atualizado", result.getNome());

			verify(domain).atualizar(existente, dto);
		}

		@Test
		void deveLancarErroQuandoDtoForNulo() {
			assertThrows(NullPointerException.class, () -> service.salvar(null));
		}
	}

	// ---------------- DELETE ----------------

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
		void deveLancarErroAoDeletarSeNaoExistir() {

			when(repository.findById(1L)).thenReturn(Optional.empty());

			assertThrows(ProdutoNotFoundException.class,
					() -> service.deletarProduto(1L));
		}
	}

	// ---------------- BUSCAR POR ID ----------------

	@Nested
	class BuscarPorId {

		@Test
		void deveBuscarPorId() {

			Produto produto = Produto.builder().id(1L).build();
			ProdutoResponseDTO dto = ProdutoResponseDTO.builder().id(1L).build();

			when(repository.findById(1L)).thenReturn(Optional.of(produto));
			when(mapper.toDTO(produto)).thenReturn(dto);

			ProdutoResponseDTO result = service.buscarPorId(1L);

			assertNotNull(result);
			assertEquals(1L, result.getId());
		}
	}

	// ---------------- FILTRO ----------------

	@Nested
	class BuscarFiltro {

		@Test
		void deveLancarErroQuandoFiltroForNulo() {

			assertThrows(BusinessException.class,
					() -> service.buscarFiltro(null, null, null, null));
		}

		@Test
		void deveBuscarComFiltro() {

			Produto produto = Produto.builder().id(1L).nome("Mouse").build();
			ProdutoResponseDTO dto = ProdutoResponseDTO.builder().id(1L).nome("Mouse").build();

			when(repository.findAll(any(Specification.class)))
					.thenReturn(List.of(produto));

			when(mapper.toDTO(produto)).thenReturn(dto);

			List<ProdutoResponseDTO> result =
					service.buscarFiltro("Mouse", null, null, null);

			assertEquals(1, result.size());
		}

		@Test
		void deveLancarErroSeNaoEncontrarProdutos() {

			when(repository.findAll(any(Specification.class)))
					.thenReturn(List.of());

			assertThrows(BusinessException.class,
					() -> service.buscarFiltro("Mouse", null, null, null));
		}
	}
}