ALTER TABLE `eh_buildings` ADD COLUMN `product_type` VARCHAR(128);
ALTER TABLE `eh_buildings` ADD COLUMN `complete_date` DATETIME;
ALTER TABLE `eh_buildings` ADD COLUMN `joinin_date` DATETIME;
ALTER TABLE `eh_buildings` ADD COLUMN `floor_count` VARCHAR(64);
ALTER TABLE `eh_buildings` ADD COLUMN `namespace_building_type` VARCHAR(128);
ALTER TABLE `eh_buildings` ADD COLUMN `namespace_building_token` VARCHAR(128);

ALTER TABLE `eh_addresses` ADD COLUMN `rent_area` DOUBLE;
ALTER TABLE `eh_addresses` ADD COLUMN `build_area` DOUBLE;
ALTER TABLE `eh_addresses` ADD COLUMN `inner_area` DOUBLE;
ALTER TABLE `eh_addresses` ADD COLUMN `layout` VARCHAR(128);
ALTER TABLE `eh_addresses` ADD COLUMN `living_status` TINYINT;
ALTER TABLE `eh_addresses` ADD COLUMN `namespace_address_type` VARCHAR(128);
ALTER TABLE `eh_addresses` ADD COLUMN `namespace_address_token` VARCHAR(128);

-- DROP TABLE IF EXISTS `eh_contracts`;
CREATE TABLE `eh_contracts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `contract_number` VARCHAR(128) NOT NULL,
  `contract_end_date` DATETIME NOT NULL,
  `status` TINYINT,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_contract_building_mappings`;
CREATE TABLE `eh_contract_building_mappings` (
  `id` BIGINT NOT NULL,
  `contract_id` INTEGER NOT NULL,
  `building_name` VARCHAR(128),
  `apartment_name` DATETIME,
  `area_size` DOUBLE;
  `status` TINYINT,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;