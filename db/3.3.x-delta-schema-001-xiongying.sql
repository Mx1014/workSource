ALTER TABLE `eh_conf_orders` ADD COLUMN `buyer_name` VARCHAR(128);
ALTER TABLE `eh_conf_orders` ADD COLUMN `buyer_contact` VARCHAR(128);
ALTER TABLE `eh_conf_orders` ADD COLUMN `vendor_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'vendor type 0: none, 1: Alipay, 2: Wechat';

ALTER TABLE `eh_conf_account_categories` ADD COLUMN `online_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'is visible online 0: no, 1: yes';
ALTER TABLE `eh_conf_account_categories` CHANGE `channel_type` `mutiple_num` INT NOT NULL DEFAULT 0 COMMENT 'the limit value of mutiple buy channel';