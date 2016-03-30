ALTER TABLE `eh_conf_orders` ADD COLUMN `buyer_name` VARCHAR(128);
ALTER TABLE `eh_conf_orders` ADD COLUMN `buyer_contact` VARCHAR(128);
ALTER TABLE `eh_conf_orders` ADD COLUMN `vendor_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'vendor type 0: none, 1: Alipay, 2: Wechat';


DROP TABLE IF EXISTS `eh_conf_account_categories`;
CREATE TABLE `eh_conf_account_categories` (
	`id` BIGINT NOT NULL COMMENT 'id',
	`display_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'display when online or offline, 0: all, 1: online, 2: offline',
	`single_account_price` DECIMAL(10,2),
	`multiple_account_price` DECIMAL(10,2),
	`multiple_account_threshold` INTEGER,
	`conf_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: 25方仅视频, 2: 25方支持电话, 3: 100方仅视频, 4: 100方支持电话',
	`min_period` INTEGER NOT NULL DEFAULT 1 COMMENT 'the minimum count of months',
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	
	PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;