# Desafio Técnico - API de Tabela Tarifária de Água
Este projeto implementa uma API RESTful para gerenciar tabelas tarifárias de água e realizar cálculos de tarifas com base em faixas de consumo configuráveis.

## Arquitetura do Projeto

O sistema segue uma abordagem de **Monólito Modular**, onde cada domínio funcional é isolado em seu próprio módulo, garantindo baixo acoplamento e alta coesão.

### Estrutura de Módulos
*   `auth`: Gerenciamento de autenticação, JWT e segurança.
*   `tariff`: Domínio principal contendo tabelas tarifárias, categorias e cálculo de tarifas.
*   `user`: Gerenciamento de usuários e perfis.
*   `shared`: Componentes reutilizáveis (paginação, exceções, validações, utilitários).

---

## Instalação e Execução

### Pré-requisitos
*   **JDK:** 17 ou superior.
*   **Gradle:** 8.x (wrapper incluso).
*   **PostgreSQL:** 13 ou superior.
*   **Docker e Docker Compose** (para o banco de dados).

### Passo a Passo
1.  **Iniciar Banco de Dados:**
    ```bash
    docker-compose up -d
    ```
2.  **Configurar Variáveis:**
    Verifique `src/main/resources/application.properties` para configurações de conexão.
3.  **Executar Aplicação:**
    ```bash
    ./gradlew bootRun
    ```

A API estará disponível em `http://localhost:8080`.

---

## Autenticação e Swagger

1.  A aplicação possui um usuário administrador pré-populado na migração inicial:
    *   **Email:** `joao.alguem@gruporas.com.br`
    *   **Senha:** `senhaSegura123`
2.  Para utilizar a API, faça login em `/api/v1/auth/login` para receber o token JWT.
3.  **Swagger UI:** Acesse `http://localhost:8080/swagger-ui/index.html`.
    *   Clique no botão **Authorize** (topo da página).
    *   Insira `Bearer <seu-token-aqui>`.

---

## Endpoints da API

### 1. Autenticação (`/api/v1/auth`)
*   **POST /api/v1/auth/login**: Login.

### 2. Usuários (`/api/v1/usuarios`)
*   **POST /api/v1/usuarios**: Criação de usuário (Retorna o **ID** do usuário criado):
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

### 3. Tabelas Tarifárias (`/api/v1/tabelas-tarifarias`)
*   **POST /api/v1/tabelas-tarifarias**: Criar nova tabela (Retorna o **ID** do item criado e atribui o usuario logado ao registro da tabela):
    ```json
    {
      "nome": "Tabela Tarifária Geral 2026",
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
        },
        {
          "nome": "PÚBLICO",
          "faixas": [
            { "inicio": 0, "fim": 100, "valorUnitario": 2.50 },
            { "inicio": 101, "fim": 9999999, "valorUnitario": 4.00 }
          ]
        }
      ]
    }
    ```
*   **GET /api/v1/tabelas-tarifarias**: Lista tabelas (Resumo, sem faixas).
    *   *Exemplo de retorno:*
    ```json
    {
      "content": [
        {
          "id": "8f5fee04-f363-4678-92ac-61ba35ee6402",
          "nome": "Tabela Tarifa 2026",
          "dataVigencia": "2026-01-01",
          "criadoPor": { "id": "...", "username": "...", ... }
        },
        {
          "id": "9z5fee04-f363-4638-92ac-61ba35ee6403",
          "nome": "Tabela Tarifa 2025",
          "dataVigencia": "2025-01-01",
          "criadoPor": { "id": "...", "username": "...", ... }
        }
      ],
      "currentPage": 0,
       "totalPages": 5,
       "totalElements": 10,
       "pageSize": 2,
       "isFirst": true,
       "isLast": false
    }
    ```
*   **GET /api/v1/tabelas-tarifarias/atual**: Retorna tabela vigente (completa):
    ```json
    {
      "id": "7d1ecf0c-b108-4ea9-ab08-e2f1a87eccd7",
      "nome": "Tabela Tarifária Vigente 2026",
      "dataVigencia": "2026-06-27",
      "criadoPor": {
        "id": "1f172121-15e1-6461-88f3-2556ed32fbc5",
        "username": "admin_ras",
        "primeiroNome": "Administrador",
        "ultimoNome": "Sistema"
      },
      "faixasConsumo": [
        {
          "id": "f548be07-c762-4256-9987-f66eb7ea630d",
          "categoria": { "id": "698c027f...", "nome": "INDUSTRIAL" },
          "inicio": 0,
          "fim": 10,
          "valorUnitario": "1.00"
        }
      ]
    }
    ```
*   **GET /api/v1/tabelas-tarifarias/{id}**: Retorna detalhe completo de tabela específica.

### 4. Cálculo de Tarifa (`/api/v1/calculos`)
*   **POST**: Calcular custo.
    ```json
    { "categoria": "INDUSTRIAL", "consumo": 18 }
    ```
    *Exemplo de Resposta:*
    ```json
    {
      "categoria": "INDUSTRIAL",
      "consumoTotal": 18,
      "valorTotal": "26.00",
      "detalhamento": [
        {
          "faixa": { "inicio": 0, "fim": 10 },
          "m3Cobrados": 10,
          "valorUnitario": "1.00",
          "subtotal": "10.00"
        },
        {
          "faixa": { "inicio": 11, "fim": 20 },
          "m3Cobrados": 8,
          "valorUnitario": "2.00",
          "subtotal": "16.00"
        }
      ]
    }
    ```

---

## Testando a Aplicação
1.  **Testes Automatizados:** Execute `./gradlew test`.
