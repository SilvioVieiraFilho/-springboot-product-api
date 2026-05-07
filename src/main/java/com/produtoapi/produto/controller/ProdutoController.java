package com.produtoapi.produto.controller;

import java.util.List;

import com.produtoapi.produto.service.ProdutoService;
import com.produtoapi.produto.dto.ProdutoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.produtoapi.produto.dto.ProdutoRequestDTO;
import com.produtoapi.produto.enums.ProdutoStatus;

import jakarta.validation.Valid;
import response.ApiResponse;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

	@Autowired
	private ProdutoService serviceProduto;


	@GetMapping
	public ResponseEntity<ApiResponse<List<ProdutoResponseDTO>>> listarTodos() {

		List<ProdutoResponseDTO> produto = serviceProduto.listarTodos();

		ApiResponse<List<ProdutoResponseDTO>> response = new ApiResponse<>("Produtos listado com sucesso", produto);

		return ResponseEntity.ok(response);
	}


	@PostMapping

	public ResponseEntity<ApiResponse<ProdutoResponseDTO>> salvarProduto(@Valid @RequestBody ProdutoRequestDTO dto) {

		ProdutoResponseDTO produto = serviceProduto.salvar(dto);

		ApiResponse<ProdutoResponseDTO> response = new ApiResponse<>("Produto cadastrado com sucesso", produto);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}


	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deletarProduto(@PathVariable Long id) {

		serviceProduto.deletarProduto(id);

		ApiResponse<Void> response = new ApiResponse<>("Produto deletado com sucesso", null);

		return ResponseEntity.ok(response);
	}




	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<ProdutoResponseDTO>> atualizarProduto(@PathVariable Long id,
			@Valid @RequestBody ProdutoRequestDTO dto) {
		ProdutoResponseDTO produto = serviceProduto.atualizarProduto(id, dto);
		ApiResponse<ProdutoResponseDTO> response = new ApiResponse<>("Produto atualizado com sucesso!", produto);
		return ResponseEntity.status(HttpStatus.OK).body(response);

	}

	@PostMapping("/salvarLista")

	public ResponseEntity<ApiResponse<List<ProdutoResponseDTO>>> salvarLista(
			@Valid @RequestBody List<@Valid ProdutoRequestDTO> dto) {

		List<ProdutoResponseDTO> produtos = serviceProduto.salvarLista(dto);
		ApiResponse<List<ProdutoResponseDTO>> response = new ApiResponse<>("Produtos criados com sucesso!", produtos);

		System.out.println(dto.getClass().getName());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);

	}

	@GetMapping("/filtro")

	public ResponseEntity<ApiResponse<List<ProdutoResponseDTO>>> buscarFiltro(
			@RequestParam(required = false) String nome, @RequestParam(required = false) ProdutoStatus status,
			@RequestParam(required = false) Double precoMin, @RequestParam(required = false) Double precoMax) {

		List<ProdutoResponseDTO> produtos = serviceProduto.buscarFiltro(nome, status, precoMin, precoMax);
		ApiResponse<List<ProdutoResponseDTO>> response = new ApiResponse<List<ProdutoResponseDTO>>(
				"Filtro realizado com sucesso", produtos);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/{id:\\d+}")
	public ResponseEntity<ApiResponse<ProdutoResponseDTO>> buscarProdutoPorId(@PathVariable Long id) {

		ProdutoResponseDTO produto = serviceProduto.buscarPorId(id);

		ApiResponse<ProdutoResponseDTO> response = new ApiResponse<>("Sua busca por id foi requisitada com sucesso",
				produto);

		return ResponseEntity.ok(response);
	}
}
