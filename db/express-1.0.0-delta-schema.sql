-- 自寄服务地址表，add by tt, 20170413
-- DROP TABLE IF EXISTS `eh_express_service_addresses`;
CREATE TABLE `eh_express_service_addresses` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `name` VARCHAR(128) NOT NULL COMMENT 'the name of express service address',
  `status` TINYINT(4) NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 快递公司表，add by tt, 20170413
-- DROP TABLE IF EXISTS `eh_express_companies`;
CREATE TABLE `eh_express_companies` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `name` VARCHAR(128) NOT NULL COMMENT 'the name of express company name',
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
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `organization_id` BIGINT NOT NULL COMMENT 'the id of organization',
  `organization_member_id` BIGINT NOT NULL COMMENT 'the id of organization member',
  `status` TINYINT(4) NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;