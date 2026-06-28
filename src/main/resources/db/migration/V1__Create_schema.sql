CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE tariff_table (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    effective_date DATE NOT NULL,
    created_by UUID NOT NULL
);

CREATE TABLE consumer_category (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE consumption_range (
    id UUID PRIMARY KEY,
    tariff_table_id UUID NOT NULL,
    consumer_category_id UUID NOT NULL,
    start_range INTEGER NOT NULL,
    end_range INTEGER NOT NULL,
    unit_value NUMERIC(19, 2) NOT NULL,
    FOREIGN KEY (tariff_table_id) REFERENCES tariff_table(id),
    FOREIGN KEY (consumer_category_id) REFERENCES consumer_category(id),
    UNIQUE (tariff_table_id, consumer_category_id, start_range, end_range)
);
