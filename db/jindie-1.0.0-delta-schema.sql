-- 金蝶数据同步， add by tt, 20161117
ALTER TABLE `eh_buildings` ADD COLUMN `product_type` VARCHAR(128);
ALTER TABLE `eh_buildings` ADD COLUMN `complete_date` DATETIME;
ALTER TABLE `eh_buildings` ADD COLUMN `joinin_date` DATETIME;
ALTER TABLE `eh_buildings` ADD COLUMN `floor_count` VARCHAR(64);
ALTER TABLE `eh_buildings` ADD COLUMN `namespace_building_type` VARCHAR(128);
ALTER TABLE `eh_buildings` ADD COLUMN `namespace_building_token` VARCHAR(128);

-- 下面这种写法速度更快点
ALTER TABLE `eh_addresses` ADD COLUMN `rent_area` DOUBLE,
	ADD COLUMN `build_area` DOUBLE,
	ADD COLUMN `inner_area` DOUBLE,
	ADD COLUMN `layout` VARCHAR(128),
	ADD COLUMN `living_status` TINYINT,
	ADD COLUMN `namespace_address_type` VARCHAR(128),
	ADD COLUMN `namespace_address_token` VARCHAR(128);

-- DROP TABLE IF EXISTS `eh_contracts`;
CREATE TABLE `eh_contracts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_id` BIGINT,
  `organization_name` VARCHAR(64),
  `contract_number` VARCHAR(128) NOT NULL,
  `contract_end_date` DATETIME NOT NULL,
  `status` TINYINT,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_contract_building_mappings`;
CREATE TABLE `eh_contract_building_mappings` (
  `id` BIGINT NOT NULL,
  `organization_id` BIGINT,
  `organization_name` VARCHAR(64),
  `contract_id` INTEGER NOT NULL,
  `contract_number` VARCHAR(128) NOT NULL,
  `building_name` VARCHAR(128),
  `apartment_name` VARCHAR(128),
  `area_size` DOUBLE,
  `status` TINYINT,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_app_namespace_mappings`;
CREATE TABLE `eh_app_namespace_mappings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INT(11),
  `app_key` VARCHAR(64),
  `community_id` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;