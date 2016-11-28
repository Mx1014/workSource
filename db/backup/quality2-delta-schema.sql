ALTER TABLE eh_quality_inspection_standards ADD COLUMN `target_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE eh_quality_inspection_standards ADD COLUMN `target_type` VARCHAR(32) NOT NULL DEFAULT '';
ALTER TABLE eh_quality_inspection_standards ADD COLUMN `review_result` TINYINT NOT NULL DEFAULT '0' COMMENT '0:none, 1: qualified, 2: unqualified';
ALTER TABLE eh_quality_inspection_standards ADD COLUMN `reviewer_uid` BIGINT NOT NULL DEFAULT '0';
ALTER TABLE eh_quality_inspection_standards ADD COLUMN `review_time` DATETIME;
ALTER TABLE eh_quality_inspection_standards ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT '0';
ALTER TABLE eh_quality_inspection_standards DROP COLUMN `category_id`;

ALTER TABLE eh_quality_inspection_tasks ADD COLUMN `target_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE eh_quality_inspection_tasks ADD COLUMN `target_type` VARCHAR(32) NOT NULL DEFAULT '';
ALTER TABLE eh_quality_inspection_tasks ADD COLUMN `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id';

DROP TABLE IF EXISTS `eh_quality_inspection_standard_specification_map`;
CREATE TABLE `eh_quality_inspection_standard_specification_map` (
  `id` BIGINT NOT NULL COMMENT 'id', 
  `standard_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'reference to the id of eh_equipment_inspection_standards',
  `specification_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'reference to the id of eh_quality_inspection_specifications',
  `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: disabled, 1: waiting for approval, 2: active',
  `deleter_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'deleter id',
  `delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_quality_inspection_specifications`;
CREATE TABLE `eh_quality_inspection_specifications` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `scope_code` TINYINT NOT NULL DEFAULT '0' COMMENT '0: all, 1: community',
  `scope_id` BIGINT NOT NULL DEFAULT '0',
  `parent_id` BIGINT NOT NULL DEFAULT '0',
  `name` VARCHAR(64) NOT NULL DEFAULT '',,
  `path` VARCHAR(128),
  `score` DOUBLE NOT NULL DEFAULT '100',
  `description` TEXT COMMENT 'content data',
  `weight` DOUBLE NOT NULL DEFAULT '1.00',
  `inspection_type` TINYINT NOT NULL DEFAULT '0' COMMENT '0: category, 1: specification, 2: specification item',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `apply_policy` TINYINT NOT NULL DEFAULT '0' COMMENT '0: add, 1: modify, 2: delete',
  `refer_id` BIGINT NOT NULL DEFAULT '0',
  `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: disabled, 1: waiting for approval, 2: active',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_quality_inspection_specification_item_results`;
CREATE TABLE `eh_quality_inspection_specification_item_results` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the item, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '',
  `task_record_id` BIGINT NOT NULL COMMENT 'id of the eh_quality_inspection_task_records',
  `task_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_quality_inspection_tasks',
  `specification_parent_id` BIGINT NOT NULL DEFAULT '0',
  `specification_id` BIGINT NOT NULL DEFAULT '0',
  `specification_path` VARCHAR(128),
  `item_description` VARCHAR(512),
  `item_score` DOUBLE NOT NULL DEFAULT '0',
  `quantity` INTEGER NOT NULL DEFAULT '0',
  `total_score` DOUBLE NOT NULL DEFAULT '0',
  `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;