ALTER TABLE `eh_conf_orders` ADD COLUMN `buyer_name` VARCHAR(128);
ALTER TABLE `eh_conf_orders` ADD COLUMN `buyer_contact` VARCHAR(128);
ALTER TABLE `eh_conf_orders` ADD COLUMN `vendor_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'vendor type 0: none, 1: Alipay, 2: Wechat';

ALTER TABLE `eh_conf_orders` ADD COLUMN `email` VARCHAR(128);


ALTER TABLE `eh_conf_account_categories` CHANGE `channel_type` `multiple_account_threshold` INTEGER NOT NULL DEFAULT 0 COMMENT 'the limit value of mutiple buy channel';
ALTER TABLE `eh_conf_account_categories` ADD COLUMN `display_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'display when online or offline, 0: all, 1: online, 2: offline';
ALTER TABLE `eh_conf_account_categories` CHANGE `amount` `single_account_price` DECIMAL(10,2);
ALTER TABLE `eh_conf_account_categories` ADD COLUMN `multiple_account_price` DECIMAL(10,2);
