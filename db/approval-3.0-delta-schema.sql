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