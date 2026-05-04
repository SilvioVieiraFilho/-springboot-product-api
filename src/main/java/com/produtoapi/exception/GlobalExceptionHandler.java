package com.produtoapi.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({
			org.springframework.web.servlet.NoHandlerFoundException.class,
			org.springframework.web.servlet.resource.NoResourceFoundException.class
	})
	public ResponseEntity<ErrorResponse> handleNotFound(
			Exception ex,
			HttpServletRequest request) {

		ErrorResponse error = new ErrorResponse(
				LocalDateTime.now(),
				404,
				"NOT_FOUND",
				"Endpoint não encontrado",
				request.getRequestURI()
		);

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusiness(
			BusinessException ex,
			HttpServletRequest request) {

		ErrorResponse error = new ErrorResponse(
				LocalDateTime.now(),
				400,
				"BAD_REQUEST",
				ex.getMessage(),
				request.getRequestURI()
		);

		return ResponseEntity.badRequest().body(error);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneric(
			Exception ex,
			HttpServletRequest request) {

		ErrorResponse error = new ErrorResponse(
				LocalDateTime.now(),
				500,
				"INTERNAL_SERVER_ERROR",
				"Erro inesperado no sistema",
				request.getRequestURI()
		);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<String>> handleValidation(
			MethodArgumentNotValidException ex) {

		List<String> erros = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(e -> e.getField() + ": " + e.getDefaultMessage())
				.toList();

		return ResponseEntity.badRequest().body(erros);
	}
	@ExceptionHandler(ProdutoNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleProdutoNotFound(
			ProdutoNotFoundException ex,
			HttpServletRequest request) {

		ErrorResponse error = new ErrorResponse(
				LocalDateTime.now(),
				HttpStatus.NOT_FOUND.value(),
				"NOT_FOUND",
				ex.getMessage(),
				request.getRequestURI()
		);

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
}