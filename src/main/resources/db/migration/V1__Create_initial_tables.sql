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
