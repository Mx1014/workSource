-- merge from quality201706 by xiongying
-- 品质核查例行检查
CREATE TABLE `eh_quality_inspection_samples` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the template, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `name` VARCHAR(128) NOT NULL DEFAULT '',
  `sample_number` VARCHAR(128),
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME NOT NULL,
  `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` DATETIME ,
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active',
  `delete_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record deleter user id',
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 品质核查例行检查关联岗位部门
CREATE TABLE `eh_quality_inspection_sample_group_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `sample_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_equipment_inspection_sample',
  `organization_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organizations',
  `position_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organization_job_positions',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 品质核查例行检查关联项目
CREATE TABLE `eh_quality_inspection_sample_community_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `sample_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_equipment_inspection_sample',
  `community_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_communities',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 品质核查例行检查生成的任务统计表
CREATE TABLE `eh_quality_inspection_sample_score_stat` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the template, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `sample_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_equipment_inspection_sample',
  `community_count` INTEGER NOT NULL DEFAULT '0',
  `task_count` INTEGER NOT NULL DEFAULT '0',
  `correction_count` INTEGER NOT NULL DEFAULT '0',
  `deduct_score` DOUBLE NOT NULL DEFAULT '0.0',
  `highest_score` DOUBLE NOT NULL DEFAULT '0.0',
  `lowest_score` DOUBLE NOT NULL DEFAULT '0.0',
  `create_time` DATETIME ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE eh_quality_inspection_specification_item_results ADD COLUMN `manual_flag` BIGINT NOT NULL DEFAULT '0' COMMENT '0: auto 1:manual 2:sample';
ALTER TABLE eh_quality_inspection_specification_item_results ADD COLUMN `sample_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_equipment_inspection_sample';

-- 品质核查例行检查相关项目扣分项统计表
CREATE TABLE `eh_quality_inspection_sample_community_specification_stat` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the template, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `sample_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_equipment_inspection_sample',
  `community_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_communities',
  `specification_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_quality_inspection_specifications',
  `deduct_score` DOUBLE NOT NULL DEFAULT '0.0',
  `create_time` DATETIME ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE eh_quality_inspection_sample_score_stat ADD COLUMN `update_time` DATETIME;
ALTER TABLE eh_quality_inspection_sample_score_stat ADD COLUMN `correction_qualified_count` INTEGER NOT NULL DEFAULT '0';
ALTER TABLE eh_quality_inspection_sample_community_specification_stat ADD COLUMN `update_time` DATETIME;
ALTER TABLE eh_quality_inspection_sample_community_specification_stat ADD COLUMN `specification_path` VARCHAR(128);