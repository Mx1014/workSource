-- merge from mergeAsset by xiongying20170417
CREATE TABLE `eh_asset_vendor` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the vendor, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'used to display',
  `vendor_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'reference to name of eh_parking_vendors',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_asset_vendor_owner_id` (`owner_type`,`owner_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- organizationowner 修改身份证号码字段长度  2017-04-18 add by xq.tian
ALTER TABLE `eh_organization_owners` MODIFY COLUMN `id_card_number` VARCHAR(32) DEFAULT NULL COMMENT 'id card number';

-- add by sw 20170418
ALTER TABLE `eh_parking_lots` ADD COLUMN `lock_car_flag` TINYINT NOT NULL DEFAULT 0 COMMENT ' 1: support, 0: not ';
ALTER TABLE `eh_lease_configs` ADD COLUMN `area_search_flag` TINYINT NOT NULL DEFAULT 0 COMMENT ' 1: support, 0: not ';

ALTER TABLE `eh_lease_promotions` MODIFY COLUMN `rent_areas` VARCHAR(128);

DROP TABLE IF EXISTS `eh_pm_task_history_addresses`;
CREATE TABLE `eh_pm_task_history_addresses` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `building_name` varchar(128) DEFAULT NULL,
  `address` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'detail address',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive 1: wating, 2: active ',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `delete_uid` BIGINT NOT NULL DEFAULT 0,
  `delete_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
