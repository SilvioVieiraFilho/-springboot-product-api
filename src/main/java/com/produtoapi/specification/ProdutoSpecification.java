package com.produtoapi.specification;

import com.produtoapi.enums.ProdutoStatus;
import com.produtoapi.model.Produto;
import org.springframework.data.jpa.domain.Specification;

public class ProdutoSpecification {

	public static Specification<Produto> nome(String nome) {
		return (root, query, cb) -> {
			if (nome == null || nome.isBlank()) {
				return cb.conjunction(); // não filtra nada
			}
			return cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
		};
	}

	public static Specification<Produto> status(ProdutoStatus status) {
		return (root, query, cb) -> {
			if (status == null) {
				return cb.conjunction();
			}
			return cb.equal(root.get("status"), status);
		};
	}

	public static Specification<Produto> minPreco(Double precoMin) {
	    return (root, query, cb) -> {
	        if (precoMin == null) {
	            return null;
	        }
	        return cb.greaterThanOrEqualTo(root.get("preco"), precoMin);
	    };
	}

	public static Specification<Produto> maxPreco(Double precoMax) {
	    return (root, query, cb) -> {
	        if (precoMax == null) {
	            return null;
	        }
	        return cb.lessThanOrEqualTo(root.get("preco"), precoMax);
	    };
	}

}
