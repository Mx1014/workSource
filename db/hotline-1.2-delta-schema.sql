-- 
-- 服务热线配置表
-- 
-- DROP TABLE IF EXISTS `eh_service_hotline_configs`;
CREATE TABLE `eh_service_hotline_configs` (
  `id` BIGINT COMMENT 'id of the record',
  `namespace_id` INTEGER DEFAULT '0',
  `owner_type` VARCHAR(64) COMMENT 'community;group,organaization,exhibition,',
  `owner_id` BIGINT(20) DEFAULT '0',
  `name` VARCHAR(64) COMMENT '不会用到的字段', 
  `service_type` INT COMMENT'2-专属客服', 
  `create_time` DATETIME DEFAULT NULL,
  `creator_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 
-- 服务热线表
-- 
-- DROP TABLE IF EXISTS `eh_service_hotlines`;
CREATE TABLE `eh_service_hotlines` (
  `id` BIGINT COMMENT 'id of the record',
  `namespace_id` INTEGER DEFAULT '0',
  `owner_type` VARCHAR(64) COMMENT 'community;group,organaization,exhibition,',
  `owner_id` BIGINT(20) DEFAULT '0',
  `service_type` INT COMMENT'1-公共热线 2-专属客服 4- 8-', 
  `name` VARCHAR(64) COMMENT '热线/客服名称', 
  `contact` VARCHAR(64) COMMENT '热线/客服 联系电话',
  `user_id` BIGINT COMMENT '客服 userid', 
  `description` VARCHAR(400) COMMENT '客服 描述',
  `default_order` INTEGER DEFAULT '0' COMMENT '排序字段',
  `create_time` DATETIME DEFAULT NULL,
  `creator_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

