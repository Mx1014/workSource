-- ------------------------------
-- 工作流动态函数     add by xq.tian  2018/04/24
-- ------------------------------
-- DROP TABLE `eh_flow_functions` IF EXISTS;
CREATE TABLE `eh_flow_functions` (
	`id` BIGINT NOT NULL,
	`namespace_id` INTEGER NOT NULL DEFAULT '0',

	`module_type` VARCHAR(64) NOT NULL,
	`module_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the module id',

	`owner_type` VARCHAR(64),
	`owner_id` BIGINT NOT NULL DEFAULT 0,

	`function_type` VARCHAR(64) NOT NULL COMMENT 'javascript, groovy, java and other',

	`function_main_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'ref eh_flow_functions',
	`function_version` INTEGER NOT NULL DEFAULT '0' COMMENT 'function version',

	`description` TEXT DEFAULT NULL COMMENT 'function description',
	`function` LONGTEXT DEFAULT NULL COMMENT 'function content',

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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'flow functions in dev mode';

-- ------------------------------
-- 工作流动态函数配置表     add by xq.tian  2018/04/24
-- ------------------------------
-- DROP TABLE `eh_flow_function_configs` IF EXISTS;
CREATE TABLE `eh_flow_function_configs` (
	`id` BIGINT NOT NULL,
	`namespace_id` INTEGER NOT NULL DEFAULT '0',

	`module_type` VARCHAR(64) NOT NULL,
	`module_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the module id',

	`flow_main_id` BIGINT NOT NULL COMMENT 'the module id',
	`flow_version` INTEGER NOT NULL DEFAULT '0' COMMENT 'flow version',

	`owner_type` VARCHAR(64),
	`owner_id` BIGINT NOT NULL DEFAULT 0,

	`function_type` VARCHAR(64) NOT NULL COMMENT 'javascript, groovy, java and other',

	`function_name` VARCHAR(128) NULL DEFAULT NULL COMMENT 'export function name, only for function type of java',
	`function_main_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'ref eh_flow_functions',
	`function_version` INTEGER NOT NULL DEFAULT '0' COMMENT 'function version',

	`config_name` VARCHAR(1024) DEFAULT NULL COMMENT 'function config name',
	`config_desc` TEXT DEFAULT NULL COMMENT 'function config description',
	`config_value` VARCHAR(1024) DEFAULT NULL COMMENT 'function config value',

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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'flow functions config in dev mode';