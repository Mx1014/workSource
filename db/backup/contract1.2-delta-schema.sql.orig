ALTER TABLE `eh_var_field_scopes` ADD COLUMN `community_id` BIGINT COMMENT '园区id';
ALTER TABLE `eh_var_field_item_scopes` ADD COLUMN `community_id` BIGINT COMMENT '园区id';
ALTER TABLE `eh_var_field_group_scopes` ADD COLUMN `community_id` BIGINT COMMENT '园区id';
ALTER TABLE eh_var_fields ADD COLUMN `field_param` VARCHAR(128);

ALTER TABLE `eh_buildings` ADD INDEX building_name ( `name`, `alias_name`);

ALTER TABLE `eh_contracts` ADD COLUMN `settled` VARCHAR(128);
ALTER TABLE `eh_contracts` ADD COLUMN `layout` VARCHAR(128);

ALTER TABLE `eh_addresses` ADD COLUMN `apartment_number` VARCHAR(32);
ALTER TABLE `eh_addresses` ADD COLUMN `address_unit` VARCHAR(32);
ALTER TABLE `eh_addresses` ADD COLUMN `address_ownership_id` BIGINT COMMENT '产权归属: 自有、出售、非产权..., refer to the id of eh_var_field_items';
ALTER TABLE `eh_addresses` ADD COLUMN `address_ownership_name` VARCHAR(128) COMMENT '产权归属: 自有、出售、非产权..., refer to the display_name of eh_var_field_items';
ALTER TABLE `eh_addresses` ADD COLUMN `remark` VARCHAR(128);

CREATE TABLE `eh_address_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `address_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to the id of eh_addresses',
  `name` VARCHAR(128),
  `file_size` INTEGER NOT NULL DEFAULT 0,
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `download_count` INTEGER NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
	
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;		
			
CREATE TABLE `eh_contract_charging_changes` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `contract_id` BIGINT NOT NULL COMMENT 'id of eh_contracts',
  `charging_item_id` BIGINT COMMENT '收费项',
  `change_type` TINYINT COMMENT '1: 调租; 2: 免租',
  `change_method` TINYINT COMMENT '1: 按金额递增; 2: 按金额递减; 3: 按比例递增; 4: 按比例递减',
  `change_period` INTEGER,
  `period_unit` TINYINT COMMENT '1: 天; 2: 月; 3: 年',
  `change_range` DECIMAL(10,2),
  `change_start_time` DATETIME,
  `change_expired_time` DATETIME,
  `remark` VARCHAR(1024),
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_contract_charging_change_addresses` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0' COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `charging_change_id` bigint(20) NOT NULL COMMENT 'id of eh_contract_charging_changes',
  `address_id` bigint(20) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `create_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 4.9.2
-- 增加域空间左上角显示场景名称的配置项
ALTER TABLE eh_namespace_details ADD COLUMN name_type tinyint(4) DEFAULT 0;

-- fix 15631 & 15636 add by xiongying20170919
ALTER TABLE eh_organizations ADD COLUMN website VARCHAR(256);
ALTER TABLE eh_organizations ADD COLUMN unified_social_credit_code VARCHAR(256);

