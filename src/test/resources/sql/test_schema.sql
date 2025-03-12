CREATE TABLE IF NOT EXISTS `conversion_history` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `transaction_id` varchar(150) UNIQUE,
    `source_currency` varchar(3) NOT NULL,
    `target_currency` varchar(3) NOT NULL,
    `amount` DECIMAL(30, 8) NOT NULL,
    `converted_amount` DECIMAL(30, 8) NOT NULL,
    `created_at` date NOT NULL,
    `created_by` varchar(20) NOT NULL,
    `version` BIGINT,
     PRIMARY KEY (`id`)
    );