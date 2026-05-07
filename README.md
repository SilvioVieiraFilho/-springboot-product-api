# 🚀 Spring Boot Product API

<p align="center">
  <img src="https://capsule-render.vercel.app/api?type=waving&color=0:0d6efd,100:6610f2&height=180&section=header&text=Product%20API%20System&fontSize=28&fontColor=ffffff" />
</p>

## 📌 Sobre o Projeto

API REST desenvolvida com **Java + Spring Boot**, com foco em boas práticas de arquitetura moderna, segurança com JWT, testes automatizados e organização por domínio (Feature-Based Architecture).

O sistema simula uma aplicação real de gestão de produtos com autenticação de usuários, regras de negócio complexas e histórico de alterações.

---

## 🧠 Objetivos

- Criar uma API escalável e organizada por domínio
- Aplicar arquitetura moderna (Feature-Based Architecture)
- Implementar autenticação com JWT
- Aplicar testes unitários com JUnit e Mockito
- Trabalhar com regras de negócio reais
- Garantir código limpo e de fácil manutenção

---

## 🏗️ Arquitetura do Projeto

O projeto foi estruturado em **Feature-Based Architecture**, separando cada domínio de forma independente.

com.produtoapi
├── produto
├── usuario
├── security
├── historicoproduto
├── exception
├── configuration
└── response

---

## 🧩 Estrutura por Domínio

### 🟦 Produto

produto
├── controller
├── service
├── domain
├── dto
├── repository
├── mapper
├── enums
├── specification
└── historico


---

### 🟩 Usuário

usuario
├── controller
├── service
├── domain
├── dto
├── repository
├── mapper
└── enums


---

### 🟪 Security (JWT)

security
├── config
├── filter
├── service
└── controller



---

## 🔐 Autenticação

A API utiliza **JWT (JSON Web Token)** para autenticação stateless.

### Fluxo:

1. Usuário realiza login
2. API valida credenciais
3. Token JWT é gerado
4. Token enviado no header:

Authorization: Bearer <token>


---

## 🛡️ Segurança

- Autenticação JWT
- Filtro de requisições protegidas
- Controle de acesso por roles (USER / ADMIN)
- Validação de token em todas as rotas protegidas

---

## 📦 Funcionalidades

### 🟦 Produto
- Criar produto
- Atualizar produto
- Buscar por ID
- Listar produtos
- Filtros dinâmicos (Specification)
- Merge de produtos (regra de negócio)
- Histórico de alterações

---

### 🟩 Usuário
- Cadastro de usuário
- Autenticação
- Controle de status:
    - ATIVO
    - DESATIVADO
    - BLOQUEADO
- Validação de login

---

## 🔍 Regras de Negócio

- Produtos duplicados (mesmo nome, preço e status) somam quantidade automaticamente
- Produtos com status **ESGOTADO** não podem receber quantidade
- Usuários com status **DESATIVADO ou BLOQUEADO** não podem autenticar
- Login exige e-mail e senha válidos
- Usuário inativo não pode acessar o sistema

---

## 🧪 Testes

- JUnit 5 para testes unitários
- Mockito para mocks
- Cobertura de testes com JaCoCo
- Testes de service, domain e security

---

# 📊 Tratamento de Erros

```json
{
  "timestamp": "2026-05-06T10:30:00",
  "status": 400,
  "error": "BusinessException",
  "message": "Usuário inativo ou bloqueado"
}
```
___
## 🛠️ Tecnologias utilizadas
- Java 17+
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- JWT
- MapStruct
- JUnit 5
- Mockito
- Maven
- H2 Database

___

## 📁 Estrutura do projeto

src/main/java
└── com.produtoapi
├── produto
├── usuario
├── security
├── historicoproduto
├── exception
├── configuration
└── response

## 🎯 Arquitetura e decisões técnicas
- Separação por domínio (Feature-Based Architecture)
- Camada Domain para regras de negócio
- DTOs para desacoplamento da entidade 
- Mappers com MapStruct
- Autenticação stateless com JWT
- Histórico de produtos para auditoria
- Filtros dinâmicos com Specification
- Código organizado seguindo Clean Code e SOLID
___
 
## 📈 Evolução do projeto

Este projeto evoluiu para um nível próximo de aplicações reais de mercado:


✔️ Arquitetura escalável  
✔️ Segurança com JWT  
✔️ Domínios isolados  
✔️ Regras de negócio reais  
✔️ Testes automatizados  
✔️ Código organizado e limpo  
✔️ Pronto para evolução para microsserviços
___


## ▶️ Como executar o projeto

### 1. Clonar o repositório

```bash
git clone https://github.com/SEU-USUARIO/springboot-product-api.git

cd springboot-product-api

./mvnw spring-boot:run
```
___



👨‍💻 **Autor**  
Silvio Rodrigues Vieira Filho  

📌 Projeto pessoal focado em evolução como desenvolvedor backend Java