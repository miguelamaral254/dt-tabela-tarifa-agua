# dt-tabela-tarifa-agua

Este projeto implementa uma API RESTful para gerenciar tabelas tarifárias de água e calcular tarifas com base em faixas de consumo por categoria.

## Arquitetura do Projeto

O projeto adota uma arquitetura de **Monólito Modular**, organizado em domínios funcionais para garantir desacoplamento e manutenibilidade.

### Módulos Principais
*   `auth`: Gerenciamento de autenticação e tokens (JWT).
*   `tariff`: Domínio principal de tabelas tarifárias e cálculo de tarifas.
*   `user`: Gerenciamento de usuários do sistema.
*   `shared`: Componentes transversais.

### Estrutura de Camadas (por módulo)
*   `web`: Camada de entrada (Controllers) e DTOs.
*   `core`: Lógica de negócio (Use Cases).
*   `infrastructure`: Persistência (Repositories) e configurações técnicas.

## Instalação e Execução

### Pré-requisitos
*   **JDK:** 17+ | **Gradle:** 8.x | **PostgreSQL:** 13+ | **Docker**

### Configuração e Execução
1.  **DB:** `docker-compose up -d`
2.  **Executar:** `./gradlew bootRun`
A API estará em `http://localhost:8080`.

## Fluxos Principais e Exemplos

### 1. Usuários e Autenticação
*   **POST /api/v1/users**: Criação de usuário.
    ```json
    {
      "nomeUsuario": "joao.silva",
      "email": "joao@gruporas.com.br",
      "cpf": "12345678901",
      "senha": "senhaSegura123",
      "primeiroNome": "João",
      "ultimoNome": "Silva",
      "perfil": "ADMIN"
    }
    ```
*   **POST /api/v1/auth/login**: Autenticação.
    ```json
    { "email": "joao@gruporas.com.br", "senha": "senhaSegura123" }
    ```

### 2. Gerenciamento de Tabela Tarifária (`/api/v1/tariff-tables`)
*   **POST**: Criação de nova tabela:
    ```json
    {
      "nome": "Tabela Tarifa 2026",
      "dataVigencia": "2026-01-01",
      "categorias": [
        {
          "nome": "PARTICULAR",
          "faixas": [
            { "inicio": 0, "fim": 10, "valorUnitario": 3.50 },
            { "inicio": 11, "fim": 20, "valorUnitario": 5.00 },
            { "inicio": 21, "fim": 9999999, "valorUnitario": 7.00 }
          ]
        },
        {
          "nome": "COMERCIAL",
          "faixas": [
            { "inicio": 0, "fim": 50, "valorUnitario": 6.00 },
            { "inicio": 51, "fim": 9999999, "valorUnitario": 9.00 }
          ]
        },
        {
          "nome": "INDUSTRIAL",
          "faixas": [
            { "inicio": 0, "fim": 10, "valorUnitario": 1.00 },
            { "inicio": 11, "fim": 20, "valorUnitario": 2.00 },
            { "inicio": 21, "fim": 9999999, "valorUnitario": 3.00 }
          ]
        }
      ]
    }
    ```

*   **GET /api/v1/tariff-tables/{id}**: Detalhe completo (exemplo de resposta):
    ```json
    {
      "id": "7b9c1d0a-2e3f-4567-89ab-cdef01234567",
      "nome": "Example Table - Valid",
      "dataVigencia": "2024-01-01",
      "criadoPor": {
        "id": "1f172121-15e1-6461-88f3-2556ed32fbc5",
        "username": "joao.silva",
        "primeiroNome": "João",
        "ultimoNome": "Silva"
      },
      "faixasConsumo": [
        {
          "id": "67ea926e-674f-4618-9e79-1cf34dea55e7",
          "categoria": {
            "id": "c235d221-e110-46c8-8e88-f4398195fa63",
            "nome": "PARTICULAR"
          },
          "inicio": 0,
          "fim": 10,
          "valorUnitario": 3.5
        }
      ]
    }
    ```

### 3. Cálculo de Tarifa (`/api/v1/calculations`)
*   **POST**: Calcula o custo do consumo.
    ```json
    { "categoria": "INDUSTRIAL", "consumo": 15 }
    ```
    **Exemplo de Resposta:**
    ```json
    {
      "categoria": "INDUSTRIAL",
      "consumoTotal": 15,
      "valorTotal": 20.00,
      "detalhamento": [
        {
          "faixa": {
            "inicio": 0,
            "fim": 10
          },
          "m3Cobrados": 10,
          "valorUnitario": 1.00,
          "subtotal": 10.00
        },
        {
          "faixa": {
            "inicio": 11,
            "fim": 20
          },
          "m3Cobrados": 5,
          "valorUnitario": 2.00,
          "subtotal": 10.00
        }
      ]
    }
    ```
    **Lógica de Cálculo:**
    O endpoint `/api/v1/calculations` busca automaticamente a tabela tarifária que possui a `dataVigencia` mais recente em relação à data atual, garantindo que o cálculo utilize sempre as regras tarifárias corretas e atualizadas.

## Entidades e Relacionamentos
*   **`tariff_table`**: Tabela central de tarifas. Relacionada com `consumption_range` (1:N) e `users` (criada por).
*   **`consumer_category`**: Tipos de categoria (PARTICULAR, COMERCIAL, etc).
*   **`consumption_range`**: Define faixas e valores unitários. Relaciona `tariff_table` e `consumer_category`.
*   **`users`**: Entidade de usuários com controle de acesso (perfil).

---
*Documentação completa (Swagger):* `http://localhost:8080/swagger-ui/index.html`
