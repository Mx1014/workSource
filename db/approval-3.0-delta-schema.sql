-- 审批类型组表
-- DROP TABLE IF EXISTS `eh_general_approval_groups`;
CREATE TABLE `eh_general_approval_groups` (
	`id` BIGINT NOT NULL,
	`namespace_id` INT NOT NULL DEFAULT '0',
	`owner_type` VARCHAR(32),
	`owner_id` BIGINT,
	`name` VARCHAR(64) NOT NULL COMMENT 'name of the approval group',
	`status` TINYINT NOT NULL COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
	`group_attribute` NOT NULL DEFAULT 'CUSTOMIZE' COMMENT 'DEFAULT, CUSTOMIZE',
	`operator_uid` BIGINT,
	`operator_time` DATETIME,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;