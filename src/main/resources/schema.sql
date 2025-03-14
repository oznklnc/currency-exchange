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

CREATE TABLE IF NOT EXISTS `exchange_rate` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `source_currency` VARCHAR(3) NOT NULL,
    `target_currency` VARCHAR(3) NOT NULL,
    `rate` DECIMAL(18,6) NOT NULL,
    `created_at` date DEFAULT NOT NULL,
    `created_by` varchar(20) NOT NULL,
    `updated_by` varchar(20),
    `valid_date` date NOT NULL,
    `version` BIGINT,
     PRIMARY KEY (`id`),
     UNIQUE (`source_currency`, `target_currency`)
);