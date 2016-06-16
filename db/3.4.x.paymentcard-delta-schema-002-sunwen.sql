
ALTER TABLE `eh_payment_card_transactions` ADD COLUMN `comsume_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'the type of consume';
ALTER TABLE `eh_payment_card_transactions` ADD COLUMN `token` VARCHAR(512) NOT NULL COMMENT 'the token of card token to pay';
ALTER TABLE `eh_payment_card_transactions` ADD COLUMN `card_no` VARCHAR(256) NOT NULL COMMENT 'the number of card';
ALTER TABLE `eh_payment_card_transactions` ADD COLUMN `order_no` BIGINT(30) NOT NULL DEFAULT '0' COMMENT 'order no';



