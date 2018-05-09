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
ALTER TABLE `eh_general_approvals` ADD COLUMN `integral_tag3` BIGINT NOT NULL DEFAULT 0 AFTER `integral_tag2`;;
ALTER TABLE `eh_general_approvals` ADD COLUMN `string_tag1` VARCHAR(128) AFTER `integral_tag3`;
ALTER TABLE `eh_general_approvals` ADD COLUMN `string_tag2` VARCHAR(128) AFTER `string_tag1`;
ALTER TABLE `eh_general_approvals` ADD COLUMN `string_tag3` VARCHAR(128) AFTER `string_tag2`;

RENAME TABLE `eh_general_approval_templates` to `eh_enterprise_approval_templates`;
ALTER TABLE `eh_enterprise_approval_templates` ADD COLUMN `group_id` BIGINT NOT NULL DEFAULT 5 COMMENT 'the enterprise group id' AFTER `approval_name`;
-- 审批3.0 end.

-- 人事2.7 start.
-- DROP TABLE IF EXISTS `eh_archives_operations`;
CREATE TABLE `eh_archives_operations` (
	`id` BIGINT NOT NULL,
	`namespace_id` INT NOT NULL DEFAULT '0',
	`organization_id` BIGINT NOT NULL DEFAULT '0',
  `operation_type` TINYINT NOT NULL COMMENT 'the type of operation',
  `operation_time` DATE COMMENT 'the time to execute the operation',
  `detail_id` BIGINT NOT NULL COMMENT 'the detail id that belongs to the employee which is the change target',

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

DROP TABLE IF EXISTS `eh_archives_configurations`;
CREATE TABLE `eh_archives_configurations` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `organization_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the id of organization',
  `operation_type` tinyint(4) NOT NULL COMMENT 'the type of operation',
  `operation_time` date DEFAULT NULL COMMENT 'the time to execute the operation',
  `operation_information` text COMMENT 'information about the operation',
  `remind_time` datetime DEFAULT NULL COMMENT 'time to send email to the corresponding member',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'pending, execution',
  `create_time` datetime DEFAULT NULL COMMENT 'create time',
  `operator_uid` bigint(20) DEFAULT NULL COMMENT 'the id of the operator',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;