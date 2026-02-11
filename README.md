# dt-tabela-tarifa-agua

Este projeto implementa uma API RESTful para gerenciar tabelas tarifárias de água e calcular tarifas com base em faixas de consumo por categoria.

## Instalação e Execução

### Pré-requisitos

Certifique-se de ter as seguintes ferramentas instaladas em seu ambiente:

*   **Java Development Kit (JDK):** Versão 17 ou superior.
*   **Gradle:** Versão 8.x (gerenciada pelo wrapper do projeto).
*   **PostgreSQL:** Versão 13 ou superior.
*   **Docker e Docker Compose:** Para fácil configuração do banco de dados (opcional, mas recomendado).

### Configuração do Banco de Dados

Este projeto utiliza PostgreSQL. A configuração padrão está definida em `src/main/resources/application.properties`.

1.  **Usando Docker Compose (Recomendado):**
    Navegue até a raiz do projeto e execute:
    ```bash
    docker-compose up -d
    ```
    Isso iniciará um contêiner PostgreSQL na porta `5432`.

2.  **Configuração Manual:**
    Certifique-se de ter um banco de dados PostgreSQL configurado. Atualize `src/main/resources/application.properties` com as credenciais do seu banco de dados:

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/dt_tabela_tarifa_agua
    spring.datasource.username=seu_usuario
    spring.datasource.password=sua_senha
    spring.jpa.hibernate.ddl-auto=none
    ```

    O projeto utiliza [Flyway](https://flywaydb.org/) para gerenciar as migrações do banco de dados. As migrações serão aplicadas automaticamente na inicialização da aplicação.

### Compilação e Execução da Aplicação

1.  **Compilar o Projeto:**
    ```bash
    ./gradlew clean build
    ```

2.  **Executar a Aplicação:**
    ```bash
    java -jar build/libs/dt-tabela-tarifa-agua-0.0.1-SNAPSHOT.jar
    ```
    Ou, para executar via Gradle:
    ```bash
    ./gradlew bootRun
    ```

A API estará disponível em `http://localhost:8080`.

## Como Testar a Aplicação

Para executar os testes unitários e de integração:

```bash
./gradlew test
```

## Endpoints da API

Todos os endpoints estão prefixados com `/api/v1`.

### 1. Gerenciamento de Tabela Tarifária (`/api/v1/tabelas-tarifarias`)

#### `POST /api/v1/tabelas-tarifarias` - Criar Nova Tabela Tarifária

Cria uma nova tabela tarifária com suas faixas de consumo associadas.

*   **Request Body (JSON):** `TabelaTarifariaCreateUpdateDTO`
    ```json
    {
      "nome": "Tabela Tarifa Água 2024 - Exemplo",
      "dataVigencia": "2024-01-01",
      "faixasConsumo": [
        {
          "categoriaConsumidor": { "nome": "PARTICULAR" },
          "inicio": 0,
          "fim": 10,
          "valorUnitario": 3.50
        },
        {
          "categoriaConsumidor": { "nome": "PARTICULAR" },
          "inicio": 11,
          "fim": 20,
          "valorUnitario": 5.00
        },
        {
          "categoriaConsumidor": { "nome": "PARTICULAR" },
          "inicio": 21,
          "fim": 999999,
          "valorUnitario": 7.00
        }
      ]
    }
    ```
*   **Response (Sucesso - 201 Created):** (Sem conteúdo no corpo)
*   **Response (Erro de Validação - 400 Bad Request):**
    ```json
    {
      "message": "A primeira faixa de consumo deve iniciar em 0 m³."
    }
    ```
*   **Response (Erro de Conflito - 409 Conflict):**
    ```json
    {
      "message": "Há uma lacuna entre as faixas de consumo. Faixa 0-10 e faixa 12-20"
    }
    ```

#### `GET /api/v1/tabelas-tarifarias` - Listar Todas as Tabelas Tarifárias

Lista todas as tabelas tarifárias cadastradas.

*   **Response (Sucesso - 200 OK):** `List<TabelaTarifariaResponseDTO>` (exemplo acima)

#### `GET /api/v1/tabelas-tarifarias/{id}` - Obter Tabela Tarifária por ID

Obtém os detalhes de uma tabela tarifária específica pelo seu ID.

*   **Parâmetros de Path:**
    *   `id`: UUID da tabela tarifária (ex: `a1b2c3d4-e5f6-7890-1234-567890abcdef`)
*   **Response (Sucesso - 200 OK):** `TabelaTarifariaResponseDTO` (exemplo acima)
*   **Response (Não Encontrado - 404 Not Found):**
    ```json
    {
      "message": "Tabela Tarifária não encontrada com o id: [ID_INVALIDO]"
    }
    ```

#### `PUT /api/v1/tabelas-tarifarias/{id}` - Atualizar Tabela Tarifária

Atualiza informações de uma tabela tarifária existente. Atualmente, apenas `nome` e `dataVigencia` são atualizáveis via este endpoint. As faixas de consumo associadas não são modificadas por esta operação (requer implementação adicional se necessário).

*   **Parâmetros de Path:**
    *   `id`: UUID da tabela tarifária a ser atualizada.
*   **Request Body (JSON):** `TabelaTarifariaCreateUpdateDTO`
    ```json
    {
      "nome": "Tabela Tarifa Água 2025 (Atualizada)",
      "dataVigencia": "2025-01-01",
      "faixasConsumo": []
    }
    ```
    *Nota: `faixasConsumo` pode ser enviado vazio ou com os dados existentes; o serviço irá ignorá-los na atualização de `nome` e `dataVigencia`.*
*   **Response (Sucesso - 200 OK):** `TabelaTarifariaResponseDTO` (da tabela atualizada)

#### `DELETE /api/v1/tabelas-tarifarias/{id}` - Excluir Tabela Tarifária

Exclui uma tabela tarifária pelo seu ID.

*   **Parâmetros de Path:**
    *   `id`: UUID da tabela tarifária a ser excluída.
*   **Response (Sucesso - 204 No Content)**

### 2. Cálculo de Tarifa (`/api/calculos`)

#### `POST /api/calculos` - Calcular Tarifa de Consumo

Calcula o valor da tarifa de água para um determinado consumo e categoria, usando a tabela tarifária mais recente.

*   **Request Body (JSON):** `TariffCalculationRequest`
    ```json
    {
      "categoria": "PARTICULAR",
      "consumo": 15
    }
    ```
*   **Response (Sucesso - 200 OK):** `TariffCalculationResponse`
    ```json
    {
      "categoria": "PARTICULAR",
      "consumoTotal": 15,
      "valorTotal": 60.50,
      "detalhamento": [
        {
          "faixa": {
            "inicio": 0,
            "fim": 10
          },
          "m3Cobrados": 10,
          "valorUnitario": 3.50,
          "subtotal": 35.00
        },
        {
          "faixa": {
            "inicio": 11,
            "fim": 20
          },
          "m3Cobrados": 5,
          "valorUnitario": 5.00,
          "subtotal": 25.50
        }
      ]
    }
    ```
*   **Response (Erro - 404 Not Found):**
    ```json
    {
      "message": "Nenhuma tabela de tarifa ativa encontrada."
    }
    ```

## Documentação da API

A documentação interativa da API está disponível via Swagger UI no seguinte link:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Scripts de Banco de Dados

As migrações do banco de dados são gerenciadas pelo Flyway e estão localizadas em `src/main/resources/db/migration/`.

### `V1__Create_initial_tables.sql`

```sql
CREATE TABLE tabela_tarifaria (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    data_vigencia DATE NOT NULL
);

CREATE TABLE categoria_consumidor (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE faixa_consumo (
    id UUID PRIMARY KEY,
    tabela_tarifaria_id UUID NOT NULL,
    categoria_consumidor_id UUID NOT NULL,
    inicio INTEGER NOT NULL,
    fim INTEGER NOT NULL,
    valor_unitario NUMERIC(19, 2) NOT NULL,
    FOREIGN KEY (tabela_tarifaria_id) REFERENCES tabela_tarifaria(id),
    FOREIGN KEY (categoria_consumidor_id) REFERENCES categoria_consumidor(id),
    UNIQUE (tabela_tarifaria_id, categoria_consumidor_id, inicio, fim)
);

-- Inserir categorias de consumidor padrão (seed data)
INSERT INTO categoria_consumidor (id, nome) VALUES
(gen_random_uuid(), 'COMERCIAL'),
(gen_random_uuid(), 'INDUSTRIAL'),
(gen_random_uuid(), 'PARTICULAR'),
(gen_random_uuid(), 'PÚBLICO');
```

### `V2__Insert_sample_data.sql` (Opcional - Dados de Exemplo)

```sql
INSERT INTO tabela_tarifaria (id, nome, data_vigencia)
VALUES ('7b9c1d0a-2e3f-4567-89ab-cdef01234567', 'Tabela de Exemplo - Valida', '2024-01-01');

INSERT INTO faixa_consumo (id, tabela_tarifaria_id, categoria_consumidor_id, inicio, fim, valor_unitario)
VALUES
(gen_random_uuid(), '7b9c1d0a-2e3f-4567-89ab-cdef01234567', (SELECT id FROM categoria_consumidor WHERE nome = 'PARTICULAR'), 0, 10, 3.50),
(gen_random_uuid(), '7b9c1d0a-2e3f-4567-89ab-cdef01234567', (SELECT id FROM categoria_consumidor WHERE nome = 'PARTICULAR'), 11, 20, 5.00),
(gen_random_uuid(), '7b9c1d0a-2e3f-4567-89ab-cdef01234567', (SELECT id FROM categoria_consumidor WHERE nome = 'PARTICULAR'), 21, 9999999, 7.00),

(gen_random_uuid(), '7b9c1d0a-2e3f-4567-89ab-cdef01234567', (SELECT id FROM categoria_consumidor WHERE nome = 'COMERCIAL'), 0, 50, 6.00),
(gen_random_uuid(), '7b9c1d0a-2e3f-4567-89ab-cdef01234567', (SELECT id FROM categoria_consumidor WHERE nome = 'COMERCIAL'), 51, 9999999, 9.00),

(gen_random_uuid(), '7b9c1d0a-2e3f-4567-89ab-cdef01234567', (SELECT id FROM categoria_consumidor WHERE nome = 'INDUSTRIAL'), 0, 10, 1.00),
(gen_random_uuid(), '7b9c1d0a-2e3f-4567-89ab-cdef01234567', (SELECT id FROM categoria_consumidor WHERE nome = 'INDUSTRIAL'), 11, 20, 2.00),
(gen_random_uuid(), '7b9c1d0a-2e3f-4567-89ab-cdef01234567', (SELECT id FROM categoria_consumidor WHERE nome = 'INDUSTRIAL'), 21, 9999999, 3.00);

As migrações do banco de dados são gerenciadas pelo Flyway e estão localizadas em `src/main/resources/db/migration/`.

### `V1__Create_initial_tables.sql`

```sql
CREATE TABLE tabela_tarifaria (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    data_vigencia DATE NOT NULL
);

CREATE TABLE categoria_consumidor (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE faixa_consumo (
    id UUID PRIMARY KEY,
    tabela_tarifaria_id UUID NOT NULL,
    categoria_consumidor_id UUID NOT NULL,
    inicio INTEGER NOT NULL,
    fim INTEGER NOT NULL,
    valor_unitario NUMERIC(19, 2) NOT NULL,
    FOREIGN KEY (tabela_tarifaria_id) REFERENCES tabela_tarifaria(id),
    FOREIGN KEY (categoria_consumidor_id) REFERENCES categoria_consumidor(id),
    UNIQUE (tabela_tarifaria_id, categoria_consumidor_id, inicio, fim)
);

-- Inserir categorias de consumidor padrão (seed data)
INSERT INTO categoria_consumidor (id, nome) VALUES
(gen_random_uuid(), 'COMERCIAL'),
(gen_random_uuid(), 'INDUSTRIAL'),
(gen_random_uuid(), 'PARTICULAR'),
(gen_random_uuid(), 'PÚBLICO');
```

### `V2__Insert_sample_data.sql` (Opcional - Dados de Exemplo)

```sql
INSERT INTO tabela_tarifaria (id, nome, data_vigencia)
VALUES ('7b9c1d0a-2e3f-4567-89ab-cdef01234567', 'Tabela de Exemplo - Valida', '2024-01-01');

INSERT INTO faixa_consumo (id, tabela_tarifaria_id, categoria_consumidor_id, inicio, fim, valor_unitario)
VALUES
(gen_random_uuid(), '7b9c1d0a-2e3f-4567-89ab-cdef01234567', (SELECT id FROM categoria_consumidor WHERE nome = 'PARTICULAR'), 0, 10, 3.50),
(gen_random_uuid(), '7b9c1d0a-2e3f-4567-89ab-cdef01234567', (SELECT id FROM categoria_consumidor WHERE nome = 'PARTICULAR'), 11, 20, 5.00),
(gen_random_uuid(), '7b9c1d0a-2e3f-4567-89ab-cdef01234567', (SELECT id FROM categoria_consumidor WHERE nome = 'PARTICULAR'), 21, 9999999, 7.00),

(gen_random_uuid(), '7b9c1d0a-2e3f-4567-89ab-cdef01234567', (SELECT id FROM categoria_consumidor WHERE nome = 'COMERCIAL'), 0, 50, 6.00),
(gen_random_uuid(), '7b9c1d0a-2e3f-4567-89ab-cdef01234567', (SELECT id FROM categoria_consumidor WHERE nome = 'COMERCIAL'), 51, 9999999, 9.00),

(gen_random_uuid(), '7b9c1d0a-2e3f-4567-89ab-cdef01234567', (SELECT id FROM categoria_consumidor WHERE nome = 'INDUSTRIAL'), 0, 10, 1.00),
(gen_random_uuid(), '7b9c1d0a-2e3f-4567-89ab-cdef01234567', (SELECT id FROM categoria_consumidor WHERE nome = 'INDUSTRIAL'), 11, 20, 2.00),
(gen_random_uuid(), '7b9c1d0a-2e3f-4567-89ab-cdef01234567', (SELECT id FROM categoria_consumidor WHERE nome = 'INDUSTRIAL'), 21, 9999999, 3.00);