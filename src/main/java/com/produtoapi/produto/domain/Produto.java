package com.produtoapi.produto.domain;
import com.produtoapi.enums.ProdutoStatus;
import com.produtoapi.exception.BusinessException;
import com.produtoapi.model.HistoricosProdutos;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "informe um nome.")
	private String nome;

	private int quantidade;

	private double preco;

	@Enumerated(EnumType.STRING)
	private ProdutoStatus status;

	@OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<HistoricosProdutos> historicos = new ArrayList<>();

	// =====================================================
	// 🔥 ADICIONAR QUANTIDADE
	// =====================================================
	public void adicionarQuantidade(int quantidade) {

		if (quantidade <= 0) {
			throw new BusinessException("Quantidade deve ser maior que zero");
		}

		int anterior = this.quantidade;
		this.quantidade += quantidade;

		registrarHistorico(anterior, this.quantidade, quantidade);

		aplicarRegraEstoque();
	}

	// =====================================================
	// 🔥 DEFINIR QUANTIDADE DIRETA
	// =====================================================
	public void definirQuantidade(int novaQuantidade) {

		if (novaQuantidade < 0) {
			throw new BusinessException("Quantidade não pode ser negativa");
		}

		int anterior = this.quantidade;

		this.quantidade = novaQuantidade;

		registrarHistorico(anterior, novaQuantidade, novaQuantidade - anterior);

		aplicarRegraEstoque();
	}

	// =====================================================
	// 🔥 REGRA DE ESTOQUE (ÚNICA FONTE DA VERDADE)
	// =====================================================
	public void aplicarRegraEstoque() {
		this.status = (this.quantidade == 0)
				? ProdutoStatus.ESGOTADO
				: ProdutoStatus.ATIVO;
	}

	// =====================================================
	// 🔥 VALIDAÇÃO DE NEGÓCIO
	// =====================================================
	public void validar() {

		if (status == ProdutoStatus.ATIVO && quantidade == 0) {
			throw new BusinessException("Produto ATIVO não pode ter quantidade 0");
		}

		if (status == ProdutoStatus.ESGOTADO && quantidade > 0) {
			throw new BusinessException("Produto ESGOTADO não pode ter quantidade maior que 0");
		}
	}

	// =====================================================
	// 🔥 HISTÓRICO
	// =====================================================
	private void registrarHistorico(int anterior, int nova, int diferenca) {

		if (historicos == null) {
			historicos = new ArrayList<>();
		}

		HistoricosProdutos h = new HistoricosProdutos();
		h.setQuantidadeAnterior(anterior);
		h.setQuantidadeNova(nova);
		h.setDiferenca(diferenca);
		h.setDataRegistro(LocalDateTime.now());
		h.setProduto(this);

		historicos.add(h);
	}
	private void adicionarHistorico(int anterior, int nova, int diferenca) {

		historicos.add(
				HistoricosProdutos.builder()
						.quantidadeAnterior(anterior)
						.quantidadeNova(nova)
						.diferenca(diferenca)
						.dataRegistro(LocalDateTime.now())
						.produto(this)
						.build()
		);
	}

	public void removerQuantidade(int quantidade) {

		if (quantidade <= 0) {
			throw new BusinessException("Quantidade deve ser maior que zero");
		}

		if (this.quantidade - quantidade < 0) {
			throw new BusinessException("Estoque não pode ficar negativo");
		}

		int anterior = this.quantidade;
		this.quantidade -= quantidade;

		adicionarHistorico(anterior, this.quantidade, -quantidade);

		aplicarRegraEstoque();
	}
}

