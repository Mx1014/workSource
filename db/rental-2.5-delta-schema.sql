-- by wuhan 资源预约2.5 增加字段
ALTER TABLE eh_rentalv2_resources ADD COLUMN `confirmation_prompt` VARCHAR(200);
ALTER TABLE eh_rentalv2_resources ADD COLUMN `offline_cashier_address` VARCHAR(200);
ALTER TABLE eh_rentalv2_resources ADD COLUMN `offline_payee_uid` BIGINT ;
ALTER TABLE eh_rentalv2_resource_types ADD COLUMN `pay_mode` TINYINT DEFAULT 0 COMMENT 'pay mode :0-online pay 1-offline';
ALTER TABLE eh_rentalv2_orders ADD COLUMN `pay_mode` TINYINT DEFAULT 0 COMMENT 'pay mode :0-online pay 1-offline';
ALTER TABLE eh_rentalv2_orders ADD COLUMN `offline_cashier_address` VARCHAR(200);
ALTER TABLE eh_rentalv2_orders ADD COLUMN `offline_payee_uid` BIGINT ;


