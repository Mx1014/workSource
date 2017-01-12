DROP TABLE IF EXISTS `eh_equipment_inspection_categories`;
CREATE TABLE `eh_equipment_inspection_categories` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(64) NOT NULL,
  `path` VARCHAR(128),
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `deletor_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record deleter user id',
  `delete_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_equipment_inspection_equipments` ADD COLUMN `inspection_category_id` BIGINT;
ALTER TABLE `eh_equipment_inspection_standards` ADD COLUMN `inspection_category_id` BIGINT;
ALTER TABLE `eh_equipment_inspection_tasks` ADD COLUMN `inspection_category_id` BIGINT;
ALTER TABLE `eh_equipment_inspection_equipments` ADD COLUMN `namespace_id` INTEGER;
ALTER TABLE `eh_equipment_inspection_standards` ADD COLUMN `namespace_id` INTEGER;
ALTER TABLE `eh_equipment_inspection_tasks` ADD COLUMN `namespace_id` INTEGER;
ALTER TABLE `eh_equipment_inspection_tasks` ADD COLUMN `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the group of who own the task, etc';
ALTER TABLE `eh_equipment_inspection_tasks` ADD COLUMN `target_id` BIGINT NOT NULL DEFAULT '0';
ALTER TABLE `eh_equipment_inspection_tasks` ADD COLUMN `position_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organization_job_positions';


  
DROP TABLE IF EXISTS `eh_equipment_inspection_standard_group_map`;
CREATE TABLE `eh_equipment_inspection_standard_group_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `group_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: executive group, 2: review group',
  `standard_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_equipment_inspection_standards',
  `group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organizations',
  `position_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organization_job_positions',
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;