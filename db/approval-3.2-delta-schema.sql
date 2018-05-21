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

-- ------------------------------
-- 工作流动态函数     add by xq.tian  2018/04/24
-- ------------------------------
DROP TABLE IF EXISTS `eh_flow_scripts`;
  CREATE TABLE `eh_flow_scripts` (
	`id` BIGINT NOT NULL,
	`namespace_id` INTEGER NOT NULL DEFAULT '0',

	`module_type` VARCHAR(64) NOT NULL,
	`module_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the module id',

	`owner_type` VARCHAR(64),
	`owner_id` BIGINT NOT NULL DEFAULT 0,

	`script_category` VARCHAR(64) NOT NULL COMMENT 'system_script, user_script',
	`script_type` VARCHAR(64) NOT NULL COMMENT 'javascript, groovy, java and other',

	`script_main_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'ref eh_flow_scripts',
	`script_version` INTEGER NOT NULL DEFAULT '0' COMMENT 'script version',

	`name` VARCHAR(128) DEFAULT NULL COMMENT 'script name',
	`description` TEXT DEFAULT NULL COMMENT 'script description',
	`script` LONGTEXT DEFAULT NULL COMMENT 'script content',

	`status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: invalid, 1: valid',
  `create_time` DATETIME(3),
  `creator_uid` BIGINT,
  `update_time` DATETIME(3),
  `update_uid` BIGINT,

	`string_tag1` VARCHAR(128) DEFAULT NULL,
	`string_tag2` VARCHAR(128) DEFAULT NULL,
	`string_tag3` VARCHAR(128) DEFAULT NULL,
	`string_tag4` VARCHAR(128) DEFAULT NULL,
	`string_tag5` VARCHAR(128) DEFAULT NULL,
	`integral_tag1` BIGINT(20) NOT NULL DEFAULT '0',
	`integral_tag2` BIGINT(20) NOT NULL DEFAULT '0',
	`integral_tag3` BIGINT(20) NOT NULL DEFAULT '0',
	`integral_tag4` BIGINT(20) NOT NULL DEFAULT '0',
	`integral_tag5` BIGINT(20) NOT NULL DEFAULT '0',
	PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'flow scripts in dev mode';

-- ------------------------------
-- 工作流动态函数配置表     add by xq.tian  2018/04/24
-- ------------------------------
DROP TABLE IF EXISTS `eh_flow_script_configs`;
CREATE TABLE `eh_flow_script_configs` (
	`id` BIGINT NOT NULL,
	`namespace_id` INTEGER NOT NULL DEFAULT '0',

	`module_type` VARCHAR(64) NOT NULL,
	`module_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the module id',

	`flow_main_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the module id',
	`flow_version` INTEGER NOT NULL DEFAULT 0 COMMENT 'flow version',

	`owner_type` VARCHAR(64),
	`owner_id` BIGINT NOT NULL DEFAULT 0,

	`script_type` VARCHAR(64) NOT NULL COMMENT 'javascript, groovy, java and other',

	`script_name` VARCHAR(128) NULL DEFAULT NULL COMMENT 'export script name, only for script type of java',
	`script_main_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'ref eh_flow_scripts',
	`script_version` INTEGER NOT NULL DEFAULT '0' COMMENT 'script version',

	`field_name` VARCHAR(1024) DEFAULT NULL COMMENT 'field name',
	`field_desc` TEXT DEFAULT NULL COMMENT 'field description',
	`field_value` VARCHAR(1024) DEFAULT NULL COMMENT 'field value',

	`status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: invalid, 1: valid',
	`create_time` DATETIME(3),
  `creator_uid` BIGINT,
  `update_time` DATETIME(3),
  `update_uid` BIGINT,

	`string_tag1` VARCHAR(128) DEFAULT NULL,
	`string_tag2` VARCHAR(128) DEFAULT NULL,
	`string_tag3` VARCHAR(128) DEFAULT NULL,
	`string_tag4` VARCHAR(128) DEFAULT NULL,
	`string_tag5` VARCHAR(128) DEFAULT NULL,
	`integral_tag1` BIGINT(20) NOT NULL DEFAULT '0',
	`integral_tag2` BIGINT(20) NOT NULL DEFAULT '0',
	`integral_tag3` BIGINT(20) NOT NULL DEFAULT '0',
	`integral_tag4` BIGINT(20) NOT NULL DEFAULT '0',
	`integral_tag5` BIGINT(20) NOT NULL DEFAULT '0',
	PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'flow scripts config in dev mode';

ALTER TABLE eh_flow_evaluate_items ADD COLUMN flow_case_id BIGINT;

ALTER TABLE eh_flow_evaluate_items ADD COLUMN `string_tag6` VARCHAR(128) DEFAULT NULL;
ALTER TABLE eh_flow_evaluate_items ADD COLUMN `string_tag7` VARCHAR(128) DEFAULT NULL;
ALTER TABLE eh_flow_evaluate_items ADD COLUMN `string_tag8` VARCHAR(128) DEFAULT NULL;
ALTER TABLE eh_flow_evaluate_items ADD COLUMN `string_tag9` VARCHAR(128) DEFAULT NULL;
ALTER TABLE eh_flow_evaluate_items ADD COLUMN `string_tag10` VARCHAR(128) DEFAULT NULL;

ALTER TABLE eh_flow_evaluate_items ADD COLUMN `integral_tag6` BIGINT(20) NOT NULL DEFAULT '0';
ALTER TABLE eh_flow_evaluate_items ADD COLUMN `integral_tag7` BIGINT(20) NOT NULL DEFAULT '0';
ALTER TABLE eh_flow_evaluate_items ADD COLUMN `integral_tag8` BIGINT(20) NOT NULL DEFAULT '0';
ALTER TABLE eh_flow_evaluate_items ADD COLUMN `integral_tag9` BIGINT(20) NOT NULL DEFAULT '0';
ALTER TABLE eh_flow_evaluate_items ADD COLUMN `integral_tag10` BIGINT(20) NOT NULL DEFAULT '0';

ALTER TABLE eh_flow_cases ADD COLUMN path VARCHAR(1024) COMMENT 'flow case path';
