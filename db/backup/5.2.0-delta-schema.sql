-- 账单item关联滞纳金 by wentian
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `late_fine_standard_id` BIGINT DEFAULT NULL COMMENT '滞纳金标准id';

# DROP TABLE IF EXISTS `eh_payment_late_fine`;
CREATE TABLE `eh_payment_late_fine`(
  `id` BIGINT NOT NULL COMMENT 'primary key',
  `name` VARCHAR(20) COMMENT '滞纳金名称',
  `amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT 'amount of overdue payment',
  `bill_id` BIGINT NOT NULL COMMENT 'the id of the corresponding bill, one to one',
  `bill_item_id` BIGINT NOT NULL COMMENT 'the id of the corresponding bill item id, one to one',
  `create_time` DATETIME DEFAULT NOW(),
  `upate_time` DATETIME DEFAULT NOW(),
  `update_uid` BIGINT DEFAULT NULL,
  `namespace_id` INTEGER DEFAULT NULL COMMENT 'location info, for possible statistics later',
  `community_id` BIGINT DEFAULT NULL,
  `customer_id` BIGINT NOT NULL COMMENT 'allows searching taking advantage of it',
  `customer_type` VARCHAR(20) NOT NULL COMMENT 'break of user info benefits',
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4;

-- end of script by wentian

-- deng，云打印相关2018.03.19
ALTER TABLE eh_siyin_print_printers ADD COLUMN qrcode_logo_uri VARCHAR(500);
ALTER TABLE eh_siyin_print_printers ADD COLUMN qrcode_logo_url VARCHAR(500);
