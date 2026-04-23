# 🚀 Product API - Spring Boot

API REST para gerenciamento de produtos desenvolvida com Spring Boot, seguindo o padrão MVC e boas práticas de desenvolvimento backend.

---

## 🛠️ Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- Lombok
- Maven
- SQLite

---

## 📌 Funcionalidades

- ✅ Cadastro de produtos  
- ✅ Listagem de produtos  
- ✅ Atualização de dados  
- ✅ Remoção de produtos  

---

## 🧱 Arquitetura

O projeto segue o padrão **MVC (Model-View-Controller)**:

- **Model** → Entidade `Produto`
- **Repository** → Interface JPA para acesso ao banco
- **Service** → Regras de negócio
- **Controller** → Exposição da API REST

---

## ▶️ Como executar o projeto

### 🔧 Pré-requisitos
- Java 21
- Maven
- STS ou outra IDE Java

---

### 🚀 Passos

```bash
# Clonar repositório
git clone https://github.com/seu-usuario/springboot-product-api.git

# Entrar na pasta
cd springboot-product-api

# Rodar o projeto
mvn spring-boot:run
