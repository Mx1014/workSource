
DROP TABLE IF EXISTS `eh_pmsy_orders`;
CREATE TABLE `eh_pmsy_orders` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `user_name` VARCHAR(64) COMMENT 'the name of address resource',
  `user_contact` VARCHAR(64) COMMENT 'the phone of address resource',
  `order_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT 'the amount of money paid',
  `paid_time` DATETIME COMMENT 'the pay time',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'the status of the order, 0: inactive, 1: unpaid, 2: paid',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `paid_type` VARCHAR(32) COMMENT 'the type of paid 10001:zhifubao 10002: weixin',
  `project_id` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the id of siyuan project',
  `customer_id` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the id of customer according the third system, siyuan',

  PRIMARY KEY (`id`)             
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_pmsy_order_items`;
CREATE TABLE `eh_pmsy_order_items` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `order_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'eh_pmsy_orders id',  
  `bill_id` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the id of bill according the third system, siyuan',
  `item_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the name of item according the third system, siyuan',
  `bill_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT 'the money of bill',
  `resource_id` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the id of resource,door according the third system, siyuan',
  `customer_id` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the id of customer according the third system, siyuan',
  `create_time` DATETIME,
  `bill_date` DATETIME COMMENT 'the date of bill',
  `status` TINYINT COMMENT 'the status of the order, 0: fail, 1: success',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_pmsy_payers`;
CREATE TABLE `eh_pmsy_payers` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `user_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'the name of user fill in',  
  `user_contact` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'the contact of user fill in',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'the status of the payer, 0: inactive, 1: wating, 2: active',
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_pmsy_communities`;
CREATE TABLE `eh_pmsy_communities` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'community id',
  `community_token` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the id of community according the third system, siyuan',  
  `contact` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'the contact of user fill in',
  `bill_tip` VARCHAR(256) NOT NULL DEFAULT '' COMMENT 'the bill_tip of user fill in',
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;


