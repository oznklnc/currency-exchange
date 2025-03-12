INSERT INTO conversion_history (transaction_id, source_currency, target_currency, amount, converted_amount, created_at, created_by, version)
VALUES ('txn001', 'USD', 'TRY', 100.00, 850.00, CURRENT_TIMESTAMP - INTERVAL '1' DAY, 'user1', 1);

INSERT INTO conversion_history (transaction_id, source_currency, target_currency, amount, converted_amount, created_at, created_by, version)
VALUES ('txn002', 'EUR', 'USD', 200.00, 220.00, CURRENT_TIMESTAMP - INTERVAL '1' DAY, 'user2', 1);

INSERT INTO conversion_history (transaction_id, source_currency, target_currency, amount, converted_amount, created_at, created_by, version)
VALUES ('txn003', 'USD', 'EUR', 150.00, 135.00, CURRENT_TIMESTAMP, 'user3', 1);

INSERT INTO conversion_history (transaction_id, source_currency, target_currency, amount, converted_amount, created_at, created_by, version)
VALUES ('txn004', 'GBP', 'USD', 300.00, 350.00, CURRENT_TIMESTAMP, 'user4', 1);

INSERT INTO conversion_history (transaction_id, source_currency, target_currency, amount, converted_amount, created_at, created_by, version)
VALUES ('txn005', 'AUD', 'NZD', 500.00, 540.00, CURRENT_TIMESTAMP, 'user5', 1);

INSERT INTO conversion_history (transaction_id, source_currency, target_currency, amount, converted_amount, created_at, created_by, version)
VALUES ('txn006', 'USD', 'JPY', 1000.00, 110000.00, CURRENT_TIMESTAMP, 'user6', 1);

INSERT INTO conversion_history (transaction_id, source_currency, target_currency, amount, converted_amount, created_at, created_by, version)
VALUES ('txn007', 'CAD', 'USD', 400.00, 320.00, CURRENT_TIMESTAMP, 'user7', 1);

INSERT INTO conversion_history (transaction_id, source_currency, target_currency, amount, converted_amount, created_at, created_by, version)
VALUES ('txn008', 'CHF', 'GBP', 600.00, 500.00, CURRENT_TIMESTAMP, 'user8', 1);

INSERT INTO conversion_history (transaction_id, source_currency, target_currency, amount, converted_amount, created_at, created_by, version)
VALUES ('txn009', 'INR', 'USD', 10000.00, 120.00, CURRENT_TIMESTAMP, 'user9', 1);

INSERT INTO conversion_history (transaction_id, source_currency, target_currency, amount, converted_amount, created_at, created_by, version)
VALUES ('txn010', 'JPY', 'EUR', 100000.00, 850.00, CURRENT_TIMESTAMP, 'user10', 1);