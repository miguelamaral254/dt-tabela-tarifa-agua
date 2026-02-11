-- Inserir categorias de consumidor padrão (seed data)
INSERT INTO categoria_consumidor (id, nome) VALUES
(gen_random_uuid(), 'COMERCIAL'),
(gen_random_uuid(), 'INDUSTRIAL'),
(gen_random_uuid(), 'PARTICULAR'),
(gen_random_uuid(), 'PÚBLICO')
ON CONFLICT (nome) DO NOTHING;

INSERT INTO tabela_tarifaria (id, nome, data_vigencia)
VALUES ('7b9c1d0a-2e3f-4567-89ab-cdef01234567', 'Tabela de Exemplo - Valida', '2024-01-01')
ON CONFLICT (id) DO NOTHING;

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