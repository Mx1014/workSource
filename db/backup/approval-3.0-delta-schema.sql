-- 审批3.0 start.
-- 审批类型组表
-- DROP TABLE IF EXISTS `eh_enterprise_approval_groups`;
CREATE TABLE `eh_enterprise_approval_groups` (
	`id` BIGINT NOT NULL,
	`namespace_id` INT NOT NULL DEFAULT '0',
	`owner_type` VARCHAR(32),
	`owner_id` BIGINT,
	`name` VARCHAR(64) NOT NULL COMMENT 'name of the approval group',
	`status` TINYINT NOT NULL DEFAULT 1 COMMENT '0. inactive, 1.active',
	`group_attribute` VARCHAR(128) NOT NULL DEFAULT 'CUSTOMIZE' COMMENT 'DEFAULT, CUSTOMIZE',
	`approval_icon` VARCHAR(1024) COMMENT 'the default icon that belongs to the group',
	`operator_uid` BIGINT,
	`operator_time` DATETIME,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_general_approvals` ADD COLUMN `integral_tag1` BIGINT NOT NULL DEFAULT 0 AFTER `default_order`;
ALTER TABLE `eh_general_approvals` ADD COLUMN `integral_tag2` BIGINT NOT NULL DEFAULT 0 AFTER `integral_tag1`;
ALTER TABLE `eh_general_approvals` ADD COLUMN `integral_tag3` BIGINT NOT NULL DEFAULT 0 AFTER `integral_tag2`;
ALTER TABLE `eh_general_approvals` ADD COLUMN `string_tag1` VARCHAR(128) AFTER `integral_tag3`;
ALTER TABLE `eh_general_approvals` ADD COLUMN `string_tag2` VARCHAR(128) AFTER `string_tag1`;
ALTER TABLE `eh_general_approvals` ADD COLUMN `string_tag3` VARCHAR(128) AFTER `string_tag2`;

RENAME TABLE `eh_general_approval_templates` to `eh_enterprise_approval_templates`;
ALTER TABLE `eh_enterprise_approval_templates` ADD COLUMN `group_id` BIGINT NOT NULL DEFAULT 5 COMMENT 'the enterprise group id' AFTER `approval_name`;
-- 审批3.0 end.

-- 人事2.7 start.
-- ALTER TABLE `eh_archives_logs` ADD COLUMN `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0-cancel,1-pending,2-finish' AFTER `operation_remark` ;

-- DROP TABLE IF EXISTS `eh_archives_operational_configurations`;
CREATE TABLE `eh_archives_operational_configurations` (
	`id` BIGINT NOT NULL,
	`namespace_id` INT NOT NULL DEFAULT '0',
	`organization_id` BIGINT NOT NULL DEFAULT '0',
  `detail_id` BIGINT NOT NULL COMMENT 'the detail id that belongs to the employee which is the change target',
  `operate_type` TINYINT NOT NULL COMMENT 'the type of operation',
  `operate_date` DATE COMMENT 'the date of executing the operation',
  `additional_info` TEXT COMMENT 'the addition information for the operation',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-cancel, 1-pending, 2-complete',
  `create_time` DATETIME DEFAULT NULL COMMENT 'create time',
  `operator_uid` BIGINT DEFAULT NULL COMMENT 'the id of the operator',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_archives_operational_logs`;
CREATE TABLE `eh_archives_operational_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the log',
  `namespace_id` INT NOT NULL DEFAULT '0',
  `organization_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'the id of the organization',
  `detail_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'the detail id that belongs to the employee',
  `operation_type` TINYINT NOT NULL COMMENT 'the type of the operate',
  `operation_time` DATE NOT NULL COMMENT 'the time of the operate',
  `string_tag1` VARCHAR(1024) COMMENT 'redundant information for the operate',
  `string_tag2` VARCHAR(1024) COMMENT 'redundant information for the operate',
  `string_tag3` VARCHAR(1024) COMMENT 'redundant information for the operate',
  `string_tag4` VARCHAR(1024) COMMENT 'redundant information for the operate',
  `string_tag5` VARCHAR(1024) COMMENT 'redundant information for the operate',
  `string_tag6` VARCHAR(1024) COMMENT 'redundant information for the operate',
  `operator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'the id of the operator',
  `operator_name` VARCHAR(64) NOT NULL DEFAULT '0' COMMENT 'the id of the operator',
  `create_time` DATETIME DEFAULT NULL COMMENT 'create time',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;