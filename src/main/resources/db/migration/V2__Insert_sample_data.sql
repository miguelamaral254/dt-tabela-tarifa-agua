-- Insert default consumer categories (seed data)
INSERT INTO consumer_category (id, name) VALUES
(gen_random_uuid(), 'COMERCIAL'),
(gen_random_uuid(), 'INDUSTRIAL'),
(gen_random_uuid(), 'PARTICULAR'),
(gen_random_uuid(), 'PÚBLICO')
ON CONFLICT (name) DO NOTHING;

INSERT INTO tariff_table (id, name, effective_date)
VALUES ('7b9c1d0a-2e3f-4567-89ab-cdef01234567', 'Example Table - Valid', '2024-01-01')
ON CONFLICT (id) DO NOTHING;

INSERT INTO consumption_range (id, tariff_table_id, consumer_category_id, start_range, end_range, unit_value)
VALUES
(gen_random_uuid(), '7b9c1d0a-2e3f-4567-89ab-cdef01234567', (SELECT id FROM consumer_category WHERE name = 'PARTICULAR'), 0, 10, 3.50),
(gen_random_uuid(), '7b9c1d0a-2e3f-4567-89ab-cdef01234567', (SELECT id FROM consumer_category WHERE name = 'PARTICULAR'), 11, 20, 5.00),
(gen_random_uuid(), '7b9c1d0a-2e3f-4567-89ab-cdef01234567', (SELECT id FROM consumer_category WHERE name = 'PARTICULAR'), 21, 9999999, 7.00),

(gen_random_uuid(), '7b9c1d0a-2e3f-4567-89ab-cdef01234567', (SELECT id FROM consumer_category WHERE name = 'COMERCIAL'), 0, 50, 6.00),
(gen_random_uuid(), '7b9c1d0a-2e3f-4567-89ab-cdef01234567', (SELECT id FROM consumer_category WHERE name = 'COMERCIAL'), 51, 9999999, 9.00),

(gen_random_uuid(), '7b9c1d0a-2e3f-4567-89ab-cdef01234567', (SELECT id FROM consumer_category WHERE name = 'INDUSTRIAL'), 0, 10, 1.00),
(gen_random_uuid(), '7b9c1d0a-2e3f-4567-89ab-cdef01234567', (SELECT id FROM consumer_category WHERE name = 'INDUSTRIAL'), 11, 20, 2.00),
(gen_random_uuid(), '7b9c1d0a-2e3f-4567-89ab-cdef01234567', (SELECT id FROM consumer_category WHERE name = 'INDUSTRIAL'), 21, 9999999, 3.00);
