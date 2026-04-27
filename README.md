🚀 Spring Boot Product API

API REST desenvolvida com Java + Spring Boot, focada em boas práticas de desenvolvimento backend, organização de código e tratamento estruturado de respostas e erros.

📌 Objetivo do Projeto

Este projeto tem como objetivo praticar e demonstrar conhecimentos em:

Desenvolvimento de APIs REST com Spring Boot
Uso de DTOs para desacoplamento de entidades
Tratamento global de exceções
Padronização de responses da API
Organização de código em camadas
Boas práticas de versionamento e estrutura de projeto
🛠️ Tecnologias Utilizadas
Java 17+
Spring Boot
Spring Web
Spring Data JPA
Hibernate
Banco de dados (H2 / MySQL / SQLite – conforme configuração)
Maven

🧠 Principais Conceitos Aplicados
✔ Arquitetura em Camadas

Separação clara entre:

Controller
Service
Repository
DTOs
✔ Uso de DTO (Data Transfer Object)

Evita exposição direta das entidades, garantindo:

Segurança dos dados
Melhor controle de entrada e saída
Código mais limpo e desacoplado

✔ Tratamento Global de Erros

Implementação de respostas padronizadas para erros da API:

Erros de validação
Recurso não encontrado
Erros internos

Exemplo de resposta:

{
  "timestamp": "2026-04-27T10:30:00",
  "status": 404,
  "error": "Produto não encontrado",
  "message": "ID informado não existe na base"
}
✔ Padronização de Responses

Todas as respostas seguem um padrão estruturado:

{
  "message": "Operação realizada com sucesso",
  "data": {
    "id": 1,
    "nome": "Produto X",
    "preco": 50.0
  }
}
📦 Funcionalidades da API
Criar produto
Buscar produto por ID
Listar produtos
Atualizar produto
Remover produto
📁 Estrutura do Projeto
src/main/java
 └── com.seuprojeto
      ├── controller
      ├── service
      ├── repository
      ├── dto
      ├── model
      ├── exception
      └── config
🚫 Swagger

O Swagger foi removido propositalmente para manter o foco em:

Organização manual da API
Controle total das responses
Prática de documentação via README

📌 Boas Práticas Aplicadas
Código limpo (Clean Code)
Separação de responsabilidades (SOLID básico)
Uso de camadas bem definidas
Padronização de respostas
Tratamento global de exceções
Evitar exposição de entidades diretamente

📈 Diferencial do Projeto

Este projeto demonstra evolução em direção a um perfil backend profissional, com foco em:

Estrutura escalável
Código preparado para produção
Pensamento de arquitetura
Boas práticas reais de mercado

👨‍💻 Autor

Desenvolvido por Silvio Rodrigues Vieira Filho

📌 Projeto de estudo e evolução contínua em Java Backend




Desenvolvido por Silvio

📌 Projeto de estudo e evolução contínua em Java Backend
