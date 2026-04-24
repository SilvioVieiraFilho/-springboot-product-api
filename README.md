# 🚀 Product API - Spring Boot

API REST para gerenciamento de produtos desenvolvida com Spring Boot, aplicando arquitetura em camadas e boas práticas de desenvolvimento backend.

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
- ✅ Atualização de produtos  
- ✅ Remoção de produtos  
- ✅ Separação em camadas com DTOs  
- ✅ Uso de Request e Response objects  
- ✅ Mapeamento com Mapper  

---

## 🧱 Arquitetura do projeto

O projeto segue uma arquitetura em camadas baseada no padrão **MVC + DTO Pattern**:

- **Model (Entity)** → Representa a tabela `Produto` no banco  
- **Repository** → Interface de acesso ao banco de dados (JPA)  
- **Service** → Regras de negócio da aplicação  
- **Controller** → Exposição dos endpoints REST  
- **DTO (Data Transfer Object)** → Transporte de dados entre camadas  
- **Request / Response** → Organização de entrada e saída da API  
- **Mapper** → Conversão entre Entity ⇄ DTO  

---

---

## 🎨 Interface Web (HTML + JavaScript)

O projeto também conta com uma interface simples desenvolvida com **HTML, CSS e JavaScript puro**, permitindo interagir com a API de forma visual.

### 💡 Funcionalidades da tela

- 🟢 Cadastro de produtos via formulário  
- 📋 Listagem de produtos em tabela  
- ✏️ Edição de produtos  
- 🗑️ Remoção de produtos  
- 🔄 Consumo da API REST via `fetch`

---

### 🖥️ Tecnologias da interface

- HTML5  
- CSS3  
- JavaScript (Fetch API)

---

### 🔗 Integração com a API

A interface consome a API Spring Boot utilizando requisições HTTP:

- `GET /produtos` → listar produtos  
- `POST /produtos` → criar produto  
- `PUT /produtos/{id}` → atualizar produto  
- `DELETE /produtos/{id}` → remover produto  

---

### 🚀 Observação

A interface foi criada apenas para fins de estudo, com foco em praticar integração frontend + backend.

## 📦 Estrutura do projeto
