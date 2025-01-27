INSERT INTO beer_inventory (beer_id, available_stock, created_date, last_modified_date)
VALUES
    ('b1d1a20e-fb93-4c38-b0f7-9ac1f28e03c1', 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('2e9f64d8-4bf2-4e5e-beb8-8a0e2f5d3a7a', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('f395b3c2-6468-4d81-a87a-2db89c23ae14', 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO beer_inventory_history (id, beer_id, event_type, quantity, created_date)
VALUES
    ('969c4ac7-eeb4-449b-ac8a-148beaa7b61f', 'b1d1a20e-fb93-4c38-b0f7-9ac1f28e03c1', 'BREWED', 20, CURRENT_TIMESTAMP),
    ('82f831b0-3e84-4229-b95a-b8776ceca94c', '2e9f64d8-4bf2-4e5e-beb8-8a0e2f5d3a7a', 'BREWED', 30, CURRENT_TIMESTAMP),
    ('cc6595e2-87c4-4801-8afc-a07b97d1f0fb', '2e9f64d8-4bf2-4e5e-beb8-8a0e2f5d3a7a', 'RESERVED', 30, CURRENT_TIMESTAMP),
    ('9a6eec32-516e-4476-8230-c68ee079f0c6', 'f395b3c2-6468-4d81-a87a-2db89c23ae14', 'BREWED', 100, CURRENT_TIMESTAMP),
    ('f79975ea-6ca8-4b3d-9912-433699f08599', 'f395b3c2-6468-4d81-a87a-2db89c23ae14', 'RESERVED', 30, CURRENT_TIMESTAMP),
    ('c410b5bb-c4c1-4f07-bf61-d6e2ef465854', 'f395b3c2-6468-4d81-a87a-2db89c23ae14', 'CANCELLED', 30, CURRENT_TIMESTAMP)
;