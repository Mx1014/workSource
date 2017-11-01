ALTER TABLE `eh_payment_bills` ADD COLUMN `next_switch` TINYINT DEFAULT 0 COMMENT '下一次switch的值';
ALTER TABLE `eh_payment_contract_receiver` ADD COLUMN ``