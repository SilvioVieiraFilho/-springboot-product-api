package com.produtoapi.produtos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.produtoapi.exception.BusinessException;
import com.produtoapi.exception.ProdutoNotFoundExcepetion;
import org.apache.catalina.mapper.Mapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties.Build;

import com.produtoapi.dto.ProdutoRequestDTO;
import com.produtoapi.dto.ProdutoResponseDTO;
import com.produtoapi.enums.ProdutoStatus;
import com.produtoapi.mapper.ProdutoMapper;
import com.produtoapi.model.Produto;
import com.produtoapi.repository.ProdutoRepository;
import com.produtoapi.service.ProdutoService;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)

public class ServiceProdutoTest {


	@InjectMocks
	private ProdutoService service;

	@Mock
	private ProdutoMapper mapper;
	@Mock
	private ProdutoRepository repository;


	@Test
	void deveListarTodosProdutosComSucesso() {

		// entidades simuladas
		Produto produto1 = Produto.builder()
				.id(1L)
				.nome("Mouse")
				.quantidade(10)
				.preco(50.0)
				.status(ProdutoStatus.ATIVO)
				.build();

		Produto produto2 = Produto.builder()
				.id(2L)
				.nome("Teclado")
				.quantidade(5)
				.preco(100.0)
				.status(ProdutoStatus.ATIVO)
				.build();

		List<Produto> listaProdutos = List.of(produto1, produto2);

		// DTOs esperados
		ProdutoResponseDTO dto1 = ProdutoResponseDTO.builder()
				.id(1L)
				.nome("Mouse")
				.quantidade(10)
				.preco(50.0)
				.status(ProdutoStatus.ATIVO)
				.build();

		ProdutoResponseDTO dto2 = ProdutoResponseDTO.builder()
				.id(2L)
				.nome("Teclado")
				.quantidade(5)
				.preco(100.0)
				.status(ProdutoStatus.ATIVO)
				.build();

		// mocks
		when(repository.findAll()).thenReturn(listaProdutos);
		when(mapper.toDTO(produto1)).thenReturn(dto1);
		when(mapper.toDTO(produto2)).thenReturn(dto2);

		// execução
		List<ProdutoResponseDTO> resultado = service.listarTodos();

		// validações
		assertNotNull(resultado);
		assertEquals(2, resultado.size());
		assertEquals("Mouse", resultado.get(0).getNome());
		assertEquals("Teclado", resultado.get(1).getNome());

		// verifica interações
		verify(repository).findAll();
		verify(mapper).toDTO(produto1);
		verify(mapper).toDTO(produto2);
	}

	@Test
	void deveRetornarListaVaziaQuandoNaoHouverProdutos() {

		when(repository.findAll()).thenReturn(List.of());

		List<ProdutoResponseDTO> resultado = service.listarTodos();

		assertNotNull(resultado);
		assertEquals(0, resultado.size());

		verify(repository).findAll();
	}


	@Test
	void deveSalvarProdutoComSucesso() {

		ProdutoRequestDTO dto = ProdutoRequestDTO.builder()
				.nome("Mouse")
				.quantidade(1)
				.preco(20.0)
				.status(ProdutoStatus.ATIVO)
				.build();
		// entidade

		Produto produto = Produto.builder()
				.nome("Mouse")
				.quantidade(1)
				.preco(20.0)
				.status(ProdutoStatus.ATIVO)
				.build();

		// entidade salva (com ID)
		Produto produtoSalvo = Produto.builder()

				.nome("Mouse")
				.quantidade(1)
				.preco(20.0)
				.status(ProdutoStatus.ATIVO)
				.build();

		produtoSalvo.setId(1L);

		// resposta
		ProdutoResponseDTO response = ProdutoResponseDTO.builder().
				id(1L)
				.nome("Mouse")
				.quantidade(1)
				.preco(20.0)
				.status(ProdutoStatus.ATIVO)
				.build();

		// comportamento dos mocks
		when(mapper.toEntity(dto)).thenReturn(produto);
		when(repository.save(produto)).thenReturn(produtoSalvo);
		when(mapper.toDTO(produtoSalvo)).thenReturn(response);

		// execução
		ProdutoResponseDTO resultado = service.salvar(dto);

		// validações
		assertNotNull(resultado);
		assertEquals(1L, resultado.getId());
		assertEquals("Mouse", resultado.getNome());

		// verifica se chamou corretamente
		verify(repository).save(produto);


	}

	@Test
	void deveSomarQuantidadeQuandoProdutoJaExistir() {

		// DTO recebido (nova entrada)
		ProdutoRequestDTO dto = ProdutoRequestDTO.builder()
				.nome("Mouse")
				.quantidade(5) // nova quantidade
				.preco(20.0)
				.status(ProdutoStatus.ATIVO)
				.build();

		// Produto já existente no banco
		Produto produtoExistente = Produto.builder()
				.id(1L)
				.nome("Mouse")
				.quantidade(10) // já tinha 10
				.preco(20.0)
				.status(ProdutoStatus.ATIVO)
				.build();

		// Produto após atualização (10 + 5 = 15)
		Produto produtoAtualizado = Produto.builder()
				.id(1L)
				.nome("Mouse")
				.quantidade(15)
				.preco(20.0)
				.status(ProdutoStatus.ATIVO)
				.build();

		// DTO de resposta
		ProdutoResponseDTO response = ProdutoResponseDTO.builder()
				.id(1L)
				.nome("Mouse")
				.quantidade(15)
				.preco(20.0)
				.status(ProdutoStatus.ATIVO)
				.build();

		// MOCKS

		// produto já existe
		when(repository.findByNomeAndPrecoAndStatus(
				dto.getNome(), dto.getPreco(), dto.getStatus()
		)).thenReturn(Optional.of(produtoExistente));

		// salva produto atualizado
		when(repository.save(any(Produto.class))).thenReturn(produtoAtualizado);

		// mapper de saída
		when(mapper.toDTO(produtoAtualizado)).thenReturn(response);

		// EXECUÇÃO
		ProdutoResponseDTO resultado = service.salvar(dto);

		// VALIDAÇÕES
		assertNotNull(resultado);
		assertEquals(15, resultado.getQuantidade());

		//  verifica se somou corretamente

		verify(repository).save(produtoExistente);
	}


	@Test
	void deveLancarErroQuandoDtoForNulo() {

		assertThrows(RuntimeException.class, () -> {
			service.salvar(null);
		});


	}

	@Test
	void deveCriarNovoProdutoQuandoNaoExistir() {

		ProdutoRequestDTO dto = ProdutoRequestDTO.builder()
				.nome("Teclado")
				.quantidade(2)
				.preco(100.0)
				.status(ProdutoStatus.ATIVO)
				.build();

		Produto produto = Produto.builder()
				.nome("Teclado")
				.quantidade(2)
				.preco(100.0)
				.status(ProdutoStatus.ATIVO)
				.build();

		Produto produtoSalvo = Produto.builder()
				.id(2L)
				.nome("Teclado")
				.quantidade(2)
				.preco(100.0)
				.status(ProdutoStatus.ATIVO)
				.build();

		ProdutoResponseDTO response = ProdutoResponseDTO.builder()
				.id(2L)
				.nome("Teclado")
				.quantidade(2)
				.preco(100.0)
				.status(ProdutoStatus.ATIVO)
				.build();

		when(repository.findByNomeAndPrecoAndStatus(
				dto.getNome(), dto.getPreco(), dto.getStatus()
		)).thenReturn(Optional.empty());

		when(mapper.toEntity(dto)).thenReturn(produto);
		when(repository.save(produto)).thenReturn(produtoSalvo);
		when(mapper.toDTO(produtoSalvo)).thenReturn(response);

		ProdutoResponseDTO resultado = service.salvar(dto);

		assertNotNull(resultado);
		assertEquals("Teclado", resultado.getNome());

		// 🔥 valida fluxo correto
		verify(mapper).toEntity(dto);
		verify(repository).save(produto);
	}


	@Test
	void deveAtualizarQuantidadeCorretamente() {

		ProdutoRequestDTO dto = ProdutoRequestDTO.builder()
				.nome("Mouse")
				.quantidade(3)
				.preco(20.0)
				.status(ProdutoStatus.ATIVO)
				.build();

		Produto existente = Produto.builder()
				.id(1L)
				.nome("Mouse")
				.quantidade(7)
				.preco(20.0)
				.status(ProdutoStatus.ATIVO)
				.build();

		when(repository.findByNomeAndPrecoAndStatus(
				dto.getNome(), dto.getPreco(), dto.getStatus()
		)).thenReturn(Optional.of(existente));

		when(repository.save(any())).thenReturn(existente);
		when(mapper.toDTO(any())).thenReturn(new ProdutoResponseDTO());

		service.salvar(dto);

		// 🔥 valida alteração real
		assertEquals(10, existente.getQuantidade());
	}

	@Test
	void deveDeletarProdutoComSucesso() {

		when(repository.existsById(1L)).thenReturn(true);

		service.deletarProduto(1L);

		verify(repository).existsById(1L);
		verify(repository).deleteById(1L);
	}

	@Test
	void deveLancarExcecaoQuandoProdutoNaoExistir() {

		when(repository.existsById(1L)).thenReturn(false);

		assertThrows(ProdutoNotFoundExcepetion.class, () -> {
			service.deletarProduto(1L);
		});

		verify(repository).existsById(1L);
		verify(repository, org.mockito.Mockito.never()).deleteById(any());
	}
	@Test
	void deveAtualizarProdutoComSucesso() {

		Long id = 1L;

		ProdutoRequestDTO dto = ProdutoRequestDTO.builder()
				.nome("Mouse Atualizado")
				.quantidade(10)
				.preco(50.0)
				.status(ProdutoStatus.ATIVO)
				.build();

		Produto existente = Produto.builder()
				.id(id)
				.nome("Mouse")
				.quantidade(1)
				.preco(20.0)
				.status(ProdutoStatus.ATIVO)
				.build();

		Produto atualizado = Produto.builder()
				.id(id)
				.nome("Mouse Atualizado")
				.quantidade(10)
				.preco(50.0)
				.status(ProdutoStatus.ATIVO)
				.build();

		ProdutoResponseDTO response = ProdutoResponseDTO.builder()
				.id(id)
				.nome("Mouse Atualizado")
				.quantidade(10)
				.preco(50.0)
				.status(ProdutoStatus.ATIVO)
				.build();

		when(repository.findById(id)).thenReturn(Optional.of(existente));
		when(repository.save(existente)).thenReturn(atualizado);
		when(mapper.toDTO(atualizado)).thenReturn(response);

		ProdutoResponseDTO resultado = service.atualizarProduto(id, dto);

		assertNotNull(resultado);
		assertEquals("Mouse Atualizado", resultado.getNome());
		assertEquals(10, resultado.getQuantidade());
		assertEquals(50.0, resultado.getPreco());

		verify(repository).findById(id);
		verify(repository).save(existente);
	}

	@Test
	void deveLancarExcecaoQuandoProdutoNaoExistirAoAtualizar() {

		Long id = 1L;

		ProdutoRequestDTO dto = ProdutoRequestDTO.builder()
				.nome("Mouse")
				.quantidade(1)
				.preco(20.0)
				.status(ProdutoStatus.ATIVO)
				.build();

		when(repository.findById(id)).thenReturn(Optional.empty());

		assertThrows(ProdutoNotFoundExcepetion.class, () -> {
			service.atualizarProduto(id, dto);
		});

		verify(repository).findById(id);
		verify(repository, org.mockito.Mockito.never()).save(any());
	}

	@Test
	void deveAlterarTodosOsCamposDoProduto() {

		Long id = 1L;

		ProdutoRequestDTO dto = ProdutoRequestDTO.builder()
				.nome("Teclado")
				.quantidade(5)
				.preco(100.0)
				.status(ProdutoStatus.DESATIVADO)
				.build();

		Produto existente = Produto.builder()
				.id(id)
				.nome("Mouse")
				.quantidade(1)
				.preco(20.0)
				.status(ProdutoStatus.ATIVO)
				.build();

		when(repository.findById(id)).thenReturn(Optional.of(existente));
		when(repository.save(existente)).thenReturn(existente);
		when(mapper.toDTO(existente)).thenReturn(new ProdutoResponseDTO());

		service.atualizarProduto(id, dto);

		assertEquals("Teclado", existente.getNome());
		assertEquals(5, existente.getQuantidade());
		assertEquals(100.0, existente.getPreco());
		assertEquals(ProdutoStatus.DESATIVADO, existente.getStatus());
	}
	@Test
	void deveBuscarProdutoPorIdComSucesso() {

		Long id = 1L;

		Produto produto = Produto.builder()
				.id(id)
				.nome("Mouse")
				.preco(20.0)
				.quantidade(1)
				.status(ProdutoStatus.ATIVO)
				.build();

		ProdutoResponseDTO response = ProdutoResponseDTO.builder()
				.id(id)
				.nome("Mouse")
				.preco(20.0)
				.quantidade(1)
				.status(ProdutoStatus.ATIVO)
				.build();

		when(repository.findById(id)).thenReturn(Optional.of(produto));
		when(mapper.toDTO(produto)).thenReturn(response);

		ProdutoResponseDTO resultado = service.buscarPorId(id);

		assertNotNull(resultado);
		assertEquals(id, resultado.getId());
		assertEquals("Mouse", resultado.getNome());

		verify(repository).findById(id);
		verify(mapper).toDTO(produto);
	}

	@Test
	void deveLancarExcecaoQuandoProdutoNaoExistirQuandoListarUmaTabelaComID() {

		Long id = 1L;

		when(repository.findById(id)).thenReturn(Optional.empty());

		assertThrows(ProdutoNotFoundExcepetion.class, () -> {
			service.buscarPorId(id);
		});

		verify(repository).findById(id);
		verify(mapper, org.mockito.Mockito.never()).toDTO(any());
	}

	@Test
	void deveSalvarListaDeProdutosComSucesso() {

		List<ProdutoRequestDTO> listaDTO = List.of(
				ProdutoRequestDTO.builder()
						.nome("Mouse")
						.preco(20.0)
						.quantidade(1)
						.status(ProdutoStatus.ATIVO)
						.build(),
				ProdutoRequestDTO.builder()
						.nome("Teclado")
						.preco(100.0)
						.quantidade(2)
						.status(ProdutoStatus.ATIVO)
						.build()
		);

		List<Produto> listaEntity = List.of(
				Produto.builder()
						.nome("Mouse")
						.preco(20.0)
						.quantidade(1)
						.status(ProdutoStatus.ATIVO)
						.build(),
				Produto.builder()
						.nome("Teclado")
						.preco(100.0)
						.quantidade(2)
						.status(ProdutoStatus.ATIVO)
						.build()
		);

		List<Produto> listaSalva = List.of(
				Produto.builder()
						.id(1L)
						.nome("Mouse")
						.preco(20.0)
						.quantidade(1)
						.status(ProdutoStatus.ATIVO)
						.build(),
				Produto.builder()
						.id(2L)
						.nome("Teclado")
						.preco(100.0)
						.quantidade(2)
						.status(ProdutoStatus.ATIVO)
						.build()
		);

		List<ProdutoResponseDTO> response = List.of(
				ProdutoResponseDTO.builder()
						.id(1L)
						.nome("Mouse")
						.preco(20.0)
						.quantidade(1)
						.status(ProdutoStatus.ATIVO)
						.build(),
				ProdutoResponseDTO.builder()
						.id(2L)
						.nome("Teclado")
						.preco(100.0)
						.quantidade(2)
						.status(ProdutoStatus.ATIVO)
						.build()
		);

		when(mapper.toEntityList(listaDTO)).thenReturn(listaEntity);
		when(repository.saveAll(listaEntity)).thenReturn(listaSalva);
		when(mapper.toDTOList(listaSalva)).thenReturn(response);

		List<ProdutoResponseDTO> resultado = service.salvarLista(listaDTO);

		assertNotNull(resultado);
		assertEquals(2, resultado.size());
		assertEquals("Mouse", resultado.get(0).getNome());
		assertEquals("Teclado", resultado.get(1).getNome());

		verify(mapper).toEntityList(listaDTO);
		verify(repository).saveAll(listaEntity);
		verify(mapper).toDTOList(listaSalva);
	}


	@Test
	void deveLancarExcecaoQuandoNenhumFiltroForInformado() {

		assertThrows(BusinessException.class, () -> {
			service.buscarFiltro(null, null, null, null);
		});

		verify(repository, org.mockito.Mockito.never())
				.findAll(any(Specification.class));
	}

	@Test
	void deveLancarExcecaoQuandoNaoEncontrarProdutos() {

		when(repository.findAll(any(Specification.class)))
				.thenReturn(List.of());

		assertThrows(BusinessException.class, () -> {
			service.buscarFiltro("Mouse", null, null, null);
		});

		verify(repository).findAll(any(Specification.class));
	}
	@Test
	void deveBuscarProdutosComFiltroComSucesso() {

		Produto produto = Produto.builder()
				.id(1L)
				.nome("Mouse")
				.preco(20.0)
				.quantidade(1)
				.status(ProdutoStatus.ATIVO)
				.build();

		ProdutoResponseDTO dto = ProdutoResponseDTO.builder()
				.id(1L)
				.nome("Mouse")
				.preco(20.0)
				.quantidade(1)
				.status(ProdutoStatus.ATIVO)
				.build();

		when(repository.findAll(any(Specification.class)))
				.thenReturn(List.of(produto));

		when(mapper.toDTO(produto)).thenReturn(dto);

		List<ProdutoResponseDTO> result =
				service.buscarFiltro("Mouse", ProdutoStatus.ATIVO, 10.0, 50.0);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("Mouse", result.get(0).getNome());

		ArgumentCaptor<Specification<Produto>> captor =
				ArgumentCaptor.forClass(Specification.class);

		verify(repository).findAll(captor.capture());
	}
	@Test
	void deveLancarErroQuandoNaoInformarFiltro() {

		BusinessException ex = assertThrows(BusinessException.class, () -> {
			service.buscarFiltro(null, null, null, null);
		});

		assertEquals("Informe pelo menos um filtro para a busca", ex.getMessage());
	}

	@Test
	void deveLancarErroQuandoNaoEncontrarProdutos() {

		when(repository.findAll(any(Specification.class)))
				.thenReturn(List.of());

		BusinessException ex = assertThrows(BusinessException.class, () -> {
			service.buscarFiltro("Mouse", ProdutoStatus.ATIVO, 10.0, 50.0);
		});

		assertEquals("Nenhum produto encontrado com os filtros informados", ex.getMessage());
	}

	@Test
	void deveBuscarSomentePorNome() {

		Produto produto = Produto.builder()
				.id(1L)
				.nome("Teclado")
				.build();

		ProdutoResponseDTO dto = ProdutoResponseDTO.builder()
				.id(1L)
				.nome("Teclado")
				.build();

		when(repository.findAll(any(Specification.class)))
				.thenReturn(List.of(produto));

		when(mapper.toDTO(produto)).thenReturn(dto);

		List<ProdutoResponseDTO> result =
				service.buscarFiltro("Teclado", null, null, null);

		assertEquals(1, result.size());
		assertEquals("Teclado", result.get(0).getNome());

		ArgumentCaptor<Specification<Produto>> captor =
				ArgumentCaptor.forClass(Specification.class);

		verify(repository).findAll(captor.capture());
	}



}