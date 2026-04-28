
<p align="center">
  <img src="https://capsule-render.vercel.app/api?type=waving&color=0:0d6efd,100:6610f2&height=180&section=header&text=Spring%20Boot%20Product%20API&fontSize=28&fontColor=ffffff" />
</p>

# 🚀 Spring Boot Product API

API REST desenvolvida com Java + Spring Boot, focada em boas práticas de desenvolvimento backend, arquitetura em camadas, testes unitários e qualidade de código.

---

# 🏷️ Tecnologias Utilizadas

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Framework-brightgreen)
![JUnit](https://img.shields.io/badge/Tests-JUnit5-red)
![Mockito](https://img.shields.io/badge/Mock-Mockito-blue)
![Coverage](https://img.shields.io/badge/Coverage-JaCoCo-yellow)
![Maven](https://img.shields.io/badge/Maven-Build-orange)

---

# 📌 Objetivo do Projeto

Este projeto tem como objetivo praticar e demonstrar conhecimentos em:

- Desenvolvimento de APIs REST com Spring Boot  
- Arquitetura em camadas (Controller, Service, Repository)  
- Uso de DTOs para desacoplamento de entidades  
- Validação de dados com Bean Validation  
- Tratamento global de exceções  
- Regras de negócio na camada de serviço  
- Testes unitários com JUnit e Mockito  
- Filtros dinâmicos com Specification  
- Cobertura de testes com JaCoCo  

---

# 🧠 Arquitetura

O projeto segue arquitetura em camadas:

**Controller → Service → Repository**

Com separação clara de responsabilidades e uso de DTOs para comunicação segura entre camadas.

---

# 🛠️ Tecnologias Utilizadas

- Java 17+ / 21  
- Spring Boot  
- Spring Web  
- Spring Data JPA  
- Hibernate   
- Maven  
- JUnit 5  
- Mockito  
- MapStruct  
- JaCoCo  
- h2 database(banco local) 
---

# 🧪 Qualidade de Código

- Testes unitários com JUnit 5 + Mockito  
- Cobertura de testes com JaCoCo  
- Tratamento global de exceções  
- Validações com Bean Validation  
- Código baseado em Clean Code  

---

# 📦 Funcionalidades da API

- Criar produto  
- Buscar por ID  
- Listar produtos  
- Atualizar produto  
- Remover produto  
- Buscar com filtros dinâmicos  
- Merge de produtos (regra de negócio de quantidade)  

---

# 🔍 Regras de Negócio

- Se um produto já existir (mesmo nome, preço e status), sua quantidade é somada ao invés de criar um novo registro.

---

# 📊 Tratamento de Erros

```json
{
  "timestamp": "2026-04-27T10:30:00",
  "status": 404,
  "error": "Produto não encontrado",
  "message": "ID informado não existe na base"
}
````

---

# 📁 Estrutura do Projeto

```
src/main/java
└── com.produtoapi
    ├── controller
    ├── service
    ├── repository
    ├── dto
    ├── model
    ├── mapper
    ├── specification
    └── exception
```

---

# 🎯 Decisões de Arquitetura

* Swagger removido para prática manual de responses
* DTOs usados para desacoplamento total da entidade
* Merge de produtos implementado como regra de negócio real
* Foco em testes unitários ao invés de apenas CRUD básico

---

# 📈 Evolução do Projeto

Este projeto evoluiu para nível profissional com:

* Regras de negócio reais
* Testes automatizados
* Filtros dinâmicos (Specification)
* Estrutura escalável
* Cobertura de testes validada

---

# ▶️ Como Executar o Projeto

```bash
git clone https://github.com/SEU-USUARIO/springboot-product-api.git

cd springboot-product-api

./mvnw spring-boot:run
```

---

# 👨‍💻 Autor

**Silvio Rodrigues Vieira Filho**

📌 Projeto de estudo e evolução contínua em Java Backend


