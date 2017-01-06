CREATE TABLE `eh_quality_inspection_task_templates` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, organization, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `standard_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_quality_inspection_standards',
  `task_number` VARCHAR(128) DEFAULT NULL,
  `task_name` VARCHAR(1024) DEFAULT NULL,
  `task_type` TINYINT NOT NULL DEFAULT '0' COMMENT '0: none, 1: verify task, 2: rectify task',
  `parent_id` BIGINT NOT NULL DEFAULT '0' COMMENT '0: parent task, others childrean-task',
  `child_count` BIGINT NOT NULL DEFAULT '0',
  `executive_group_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organizations',
  `executive_start_time` DATETIME DEFAULT NULL,
  `executive_expire_time` DATETIME DEFAULT NULL,
  `executive_time` DATETIME DEFAULT NULL,
  `executor_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who executes the task, organization, etc',
  `executor_id` BIGINT NOT NULL DEFAULT '0',
  `operator_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who executes the task, organization, etc',
  `operator_id` BIGINT NOT NULL DEFAULT '0',
  `process_expire_time` DATETIME DEFAULT NULL,
  `process_result` TINYINT NOT NULL DEFAULT '0' COMMENT '0:none, 11: rectified ok(waiting approval), 12: rectify closed(waiting approval)',
  `process_time` DATETIME DEFAULT NULL,
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: none, 1: waiting for executing, 2: rectifing, 3: rectified and waiting approval, 4: rectify closed and waiting approval, 5: closed',
  `result` TINYINT NOT NULL DEFAULT '0' COMMENT '0: none, 1: inspect ok, 2: inspect close, 3: rectified ok, 4: rectify closed, 5: inspect delay, 6: rectify delay',
  `reviewer_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who review the task, organization, etc',
  `reviewer_id` BIGINT NOT NULL DEFAULT '0',
  `review_result` TINYINT NOT NULL DEFAULT '0' COMMENT '0:none, 1: qualified, 2: unqualified',
  `review_time` DATETIME DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `category_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_categories',
  `category_path` VARCHAR(128) DEFAULT NULL COMMENT 'refernece to the path of eh_categories',
  `manual_flag` BIGINT NOT NULL DEFAULT '0' COMMENT '0: auto 1:manual',
  `target_id` BIGINT NOT NULL DEFAULT '0',
  `target_type` VARCHAR(32) NOT NULL DEFAULT '',
  `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_quality_inspection_tasks` DROP COLUMN `create_uid`;
ALTER TABLE `eh_quality_inspection_tasks` ADD COLUMN `executive_position_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organization_job_positions';
ALTER TABLE `eh_quality_inspection_standard_group_map` ADD COLUMN `position_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organization_job_positions';
ALTER TABLE `eh_quality_inspection_task_templates` ADD COLUMN `executive_position_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organization_job_positions';

-- 3.12.4
ALTER TABLE `eh_resource_categories` ADD COLUMN `type` tinyint(4)  DEFAULT '1' COMMENT '1:分类, 2：子项目';