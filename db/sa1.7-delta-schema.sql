-- 预约看楼信息
DROP TABLE IF EXISTS `eh_service_alliance_apartment_requests`;
CREATE TABLE `eh_service_alliance_apartment_requests` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `type` BIGINT NOT NULL DEFAULT 0,
  `category_id` BIGINT NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `creator_name` VARCHAR(128),
  `creator_mobile` VARCHAR(128),
  `creator_organization_id` BIGINT NOT NULL DEFAULT 0,
  `service_alliance_id` BIGINT NOT NULL DEFAULT 0,
  
  `name` VARCHAR(128),
  `mobile` VARCHAR(128),
  `organization_name` VARCHAR(128),
  `area_size` DOUBLE COMMENT 'area size',
  `remarks` VARCHAR(1024),
  
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_service_alliance_skip_rule`;
CREATE TABLE `eh_service_alliance_skip_rule` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `service_alliance_category_id` BIGINT NOT NULL DEFAULT 0,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

