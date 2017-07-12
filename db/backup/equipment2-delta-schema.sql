ALTER TABLE `eh_equipment_inspection_standards` ADD COLUMN `review_expired_days` INTEGER NOT NULL DEFAULT '0';
ALTER TABLE `eh_equipment_inspection_standards` ADD COLUMN `template_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of eh_inspection_template';

ALTER TABLE `eh_equipment_inspection_tasks` ADD COLUMN `review_expired_date` DATETIME;
-- 设备表
DROP TABLE IF EXISTS `eh_equipment_inspection_equipments`;
CREATE TABLE `eh_equipment_inspection_equipments` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the equipment, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the group of who own the equipment, etc',
  `target_id` BIGINT NOT NULL DEFAULT '0',
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `equipment_type` TINYINT NOT NULL DEFAULT '0' COMMENT '0: equipment, 1: equipment group',
  `custom_number` VARCHAR(128),
  `name` VARCHAR(1024),
  `manufacturer` VARCHAR(1024),
  `location` VARCHAR(1024),
  `longitude` DOUBLE DEFAULT NULL,
  `latitude` DOUBLE DEFAULT NULL,
  `geohash` VARCHAR(64) DEFAULT NULL,
  `equipment_model` VARCHAR(1024),
  `category_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'reference to the id of eh_categories',
  `category_path` VARCHAR(128) DEFAULT NULL COMMENT 'reference to the path of eh_categories',  
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: incomplete(不完整), 2: in use(使用中), 3: in maintenance(维修中), 4: discarded(报废), 5: disabled(停用), 6: standby(备用)',
  `installation_time` DATETIME,
  `repair_time` DATETIME,
  `initial_asset_value` VARCHAR(128) DEFAULT NULL,
  `parameter` VARCHAR(1024),
  `quantity` BIGINT(20) NOT NULL DEFAULT '0',
  `remarks` TEXT,
  `sequence_no` VARCHAR(128),
  `version_no` VARCHAR(128),
  `manager` VARCHAR(128),
  `qr_code_token` TEXT,
  `qr_code_flag` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active',
  `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` DATETIME,
  `operator_uid` BIGINT COMMENT 'operator uid of last operation',
  `update_time` DATETIME,
  `deleter_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'deleter id',
  `delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful', 
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 设备-标准映射表
DROP TABLE IF EXISTS `eh_equipment_inspection_equipment_standard_map`;
CREATE TABLE `eh_equipment_inspection_equipment_standard_map` (
    `id` BIGINT NOT NULL COMMENT 'id',
    `standard_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of eh_equipment_inspection_standards',
    `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, equipment, etc',
    `target_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of who own the standard',
    `review_status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waiting for approval, 2: reviewed，3: delete',
    `review_result` TINYINT NOT NULL DEFAULT '0' COMMENT '0:none, 1: qualified, 2: unqualified',
    `reviewer_uid` BIGINT NOT NULL DEFAULT '0',
    `review_time` DATETIME DEFAULT NULL,
    `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
    `create_time` DATETIME,
    `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active',
    `deleter_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'deleter id',
    `delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful',
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 巡检模板
DROP TABLE IF EXISTS `eh_equipment_inspection_templates`;
CREATE TABLE `eh_equipment_inspection_templates` (
    `id` BIGINT NOT NULL,
    `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the template, enterprise, etc',
    `owner_id` BIGINT NOT NULL DEFAULT '0',
    `name` VARCHAR(128) NOT NULL,
    `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
    `create_time` DATETIME,
    `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active',
    `delete_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record deleter user id',
    `delete_time` DATETIME,
    PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 巡检模板-巡检项映射表
DROP TABLE IF EXISTS `eh_equipment_inspection_template_item_map`;
CREATE TABLE `eh_equipment_inspection_template_item_map` (
    `id` BIGINT NOT NULL COMMENT 'id',
    `template_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of eh_inspection_template',
    `item_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of eh_inspection_items',
    `create_time` DATETIME,
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 巡检项
DROP TABLE IF EXISTS `eh_equipment_inspection_items`;
CREATE TABLE `eh_equipment_inspection_items` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the item, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `name` VARCHAR(512) NOT NULL,
  `value_type` TINYINT NOT NULL DEFAULT '0' COMMENT '0-none、1-two-tuple、2-range',
  `unit` VARCHAR(32),
  `value_jason` VARCHAR(512),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 巡检项结果
DROP TABLE IF EXISTS `eh_equipment_inspection_item_results`;
CREATE TABLE `eh_equipment_inspection_item_results` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the item, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `task_log_id` BIGINT NOT NULL COMMENT 'id of the eh_equipment_inspection_task_logs',
  `task_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_equipment_inspection_tasks',
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the result, equipment, etc',
  `target_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of who own the result',
  `item_id` BIGINT NOT NULL DEFAULT '0',
  `item_name` VARCHAR(512) NOT NULL,
  `item_value_type` TINYINT NOT NULL DEFAULT '0' COMMENT '0-none、1-two-tuple、2-range',
  `item_unit` VARCHAR(32),
  `item_value` VARCHAR(128),
  `normal_flag` TINYINT NOT NULL DEFAULT '0' COMMENT '0: abnormal; 1: normal',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;