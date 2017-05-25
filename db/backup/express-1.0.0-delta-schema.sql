-- 自寄服务地址表，add by tt, 20170413
-- DROP TABLE IF EXISTS `eh_express_service_addresses`;
CREATE TABLE `eh_express_service_addresses` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `name` VARCHAR(128) COMMENT 'the name of express service address',
  `status` TINYINT(4) NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 快递公司表，左邻配一套全局的，各园区在此选择，add by tt, 20170413
-- DROP TABLE IF EXISTS `eh_express_companies`;
CREATE TABLE `eh_express_companies` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'parent id, the id of express company under zuolin',
  `name` VARCHAR(128) COMMENT 'the name of express company name',
  `logo` VARCHAR(512),
  `status` TINYINT(4) NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 快递员表，add by tt, 20170413
-- DROP TABLE IF EXISTS `eh_express_users`;
CREATE TABLE `eh_express_users` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `service_address_id` BIGINT,
  `express_company_id` BIGINT,
  `organization_id` BIGINT COMMENT 'the id of organization',
  `organization_member_id` BIGINT COMMENT 'the id of organization member',
  `user_id` BIGINT,
  `status` TINYINT(4) NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 快递地址表，add by tt, 20170413
-- DROP TABLE IF EXISTS `eh_express_addresses`;
CREATE TABLE `eh_express_addresses` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `user_name` VARCHAR(128),
  `organization_id` BIGINT,
  `organization_name` VARCHAR(128),
  `phone` VARCHAR(16),
  `province_id` BIGINT,
  `city_id` BIGINT,
  `county_id` BIGINT,
  `province` VARCHAR(64),
  `city` VARCHAR(64),
  `county` VARCHAR(64),
  `detail_address` VARCHAR(512),
  `default_flag` TINYINT COMMENT '0. false, 1 true',
  `category` TINYINT COMMENT '1. send address, 2. receive address',
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 快递订单表，add by tt, 20170413
-- DROP TABLE IF EXISTS `eh_express_orders`;
CREATE TABLE `eh_express_orders` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `order_no` VARCHAR(64) COMMENT 'order number',
  `bill_no` VARCHAR(64) COMMENT 'bill number',
  `send_name` VARCHAR(128),
  `send_phone` VARCHAR(16),
  `send_organization` VARCHAR(128),
  `send_province` VARCHAR(64),
  `send_city` VARCHAR(64),
  `send_county` VARCHAR(64),
  `send_detail_address` VARCHAR(512),
  `receive_name` VARCHAR(128),
  `receive_phone` VARCHAR(16),
  `receive_organization` VARCHAR(128),
  `receive_province` VARCHAR(64),
  `receive_city` VARCHAR(64),
  `receive_county` VARCHAR(64),
  `receive_detail_address` VARCHAR(512),
  `service_address_id` BIGINT COMMENT 'service address id',
  `express_company_id` BIGINT COMMENT 'express company id',
  `send_type` TINYINT COMMENT '1. standard express',
  `send_mode` TINYINT COMMENT '1. self send',
  `pay_type` TINYINT COMMENT '1. cash',
  `pay_summary` DECIMAL(10,2) COMMENT 'pay money',
  `internal` VARCHAR(256) COMMENT 'internal things',
  `insured_price` DECIMAL(10,2) COMMENT 'insured price',
  `status` TINYINT NOT NULL COMMENT '1. waiting for pay, 2. paid, 3. printed, 4. cancelled',
  `paid_flag` TINYINT COMMENT 'whether the user has pushed the pay button, 0. false, 1 true',
  `print_time` DATETIME,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  
  PRIMARY KEY (`id`),
  UNIQUE INDEX `order_no` (`order_no`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 快递查询历史表，add by tt, 20170413
-- DROP TABLE IF EXISTS `eh_express_query_histories`;
CREATE TABLE `eh_express_query_histories` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `express_company_id` BIGINT COMMENT 'express company id',
  `bill_no` VARCHAR(64) COMMENT 'bill number',
  `status` TINYINT(4) NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 快递订单日志表，add by tt, 20170413
-- DROP TABLE IF EXISTS `eh_express_order_logs`;
CREATE TABLE `eh_express_order_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `order_id` BIGINT,
  `action` VARCHAR(64),
  `remark` VARCHAR(512),
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;