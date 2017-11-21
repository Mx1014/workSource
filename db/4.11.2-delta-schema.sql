-- from club 3.2 start
-- 行业协会类型
CREATE TABLE `eh_industry_types` (
  `id` bigint(20) NOT NULL,
  `uuid` varchar(128) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `name` varchar(32) NOT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_guild_applies` (
  `id` bigint(20) NOT NULL,
  `uuid` varchar(128) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `group_id` bigint(22) NOT NULL,
  `applicant_uid` bigint(22) NOT NULL,
  `group_member_id` bigint(22) NOT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(18) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `organization_name` varchar(255) DEFAULT NULL,
  `registered_capital` varchar(255) DEFAULT NULL,
  `industry_type` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_uid` bigint(22) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 俱乐部类型，普通俱乐部、行业协会
ALTER TABLE `eh_group_settings` ADD COLUMN `club_type`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '0-normal club, 1-guild club' ;
-- 未加入俱乐部成员在俱乐部论坛的权限 0-不可见，1-可见，2-可交互
ALTER TABLE `eh_groups` ADD COLUMN `tourist_post_policy`  tinyint(4) NULL DEFAULT 2 COMMENT '0-hide, 1-see only, 2-interact';
-- 俱乐部类型，普通俱乐部、行业协会
ALTER TABLE `eh_groups` ADD COLUMN `club_type`  tinyint(4) NULL DEFAULT 0 COMMENT '0-normal club, 1-guild club' ;

ALTER TABLE `eh_groups` ADD COLUMN `phone_number`  varchar(18) NULL ;

ALTER TABLE `eh_groups`  ADD COLUMN `description_type`  tinyint(4) NULL DEFAULT 0;

-- 拒绝理由
ALTER TABLE `eh_group_member_logs` ADD COLUMN `reject_text`  varchar(255) NULL;

-- from club 3.2 end

-- 加表单关联字段  add by xq.tian  2017/11/20
ALTER TABLE eh_flows ADD COLUMN `form_origin_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'flow ref form id';
ALTER TABLE eh_flows ADD COLUMN `form_version` BIGINT NOT NULL DEFAULT 1 COMMENT 'flow ref form version';
ALTER TABLE eh_flows ADD COLUMN `form_update_time` DATETIME COMMENT 'form update time';

ALTER TABLE eh_flow_event_logs ADD COLUMN `extra` TEXT COMMENT 'extra data, json format';

ALTER TABLE eh_flow_condition_expressions ADD COLUMN `variable_extra1` VARCHAR(256) COMMENT 'variable 1 extra';
ALTER TABLE eh_flow_condition_expressions ADD COLUMN `variable_extra2` VARCHAR(256) COMMENT 'variable 2 extra';

-- added by R for approval-1.6
ALTER TABLE `eh_general_forms` ADD COLUMN `form_template_id` BIGINT COMMENT 'the id in eh_general_form_templates';
ALTER TABLE `eh_general_forms` ADD COLUMN `form_template_version` BIGINT COMMENT 'the version in eh_general_form_templates';
ALTER TABLE `eh_general_forms` ADD COLUMN `form_attribute` VARCHAR(128) DEFAULT 'CUSTOMIZE' COMMENT 'DEFAULT,CUSTOMIZE';
ALTER TABLE `eh_general_forms` ADD COLUMN `modify_flag` TINYINT DEFAULT 1 COMMENT 'whether the form can be modified from desk, 0: no, 1: yes';
ALTER TABLE `eh_general_forms` ADD COLUMN `delete_flag` TINYINT DEFAULT 1 COMMENT 'whether the form can be deleted from desk, 0: no, 1: yes';

ALTER TABLE `eh_general_approvals` ADD COLUMN `approval_template_id` BIGINT COMMENT 'the id in eh_general_approval_templates';
ALTER TABLE `eh_general_approvals` ADD COLUMN `approval_template_version` BIGINT COMMENT 'the version in eh_general_approval_templates';
ALTER TABLE `eh_general_approvals` ADD COLUMN `approval_attribute` VARCHAR(128) DEFAULT 'CUSTOMIZE' COMMENT 'DEFAULT,CUSTOMIZE';
ALTER TABLE `eh_general_approvals` ADD COLUMN `modify_flag` TINYINT DEFAULT 1 COMMENT 'whether the approval can be modified from desk, 0: no, 1: yes';
ALTER TABLE `eh_general_approvals` ADD COLUMN `delete_flag` TINYINT DEFAULT 1 COMMENT 'whether the approval can be deleted from desk, 0: no, 1: yes';
ALTER TABLE `eh_general_approvals` ADD COLUMN `icon_uri` VARCHAR(1024) COMMENT 'the avatar of the approval';

DROP TABLE IF EXISTS `eh_general_approval_templates`;
CREATE TABLE `eh_general_approval_templates` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	`owner_id` BIGINT,
	`owner_type` VARCHAR(64),
	`organization_id` BIGINT NOT NULL DEFAULT 0,
	`module_id` BIGINT DEFAULT 0 COMMENT 'the module id',
	`module_type` VARCHAR(64),
	`project_id` BIGINT NOT NULL DEFAULT 0,
  `project_type` VARCHAR(64),
  `form_template_id` bigint(20) NOT NULL DEFAULT 0 COMMENT 'The id of the template form',
  `support_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'APP:0, WEB:1, APP_WEB: 2',
  `approval_name` VARCHAR(128) NOT NULL,
  `approval_attribute` VARCHAR(128) DEFAULT 'CUSTOMIZE' COMMENT 'DEFAULT,CUSTOMIZE',
  `modify_flag` TINYINT DEFAULT 1 COMMENT 'whether the approval can be modified from desk, 0: no, 1: yes',
  `delete_flag` TINYINT DEFAULT 1 COMMENT 'whether the approval can be deleted from desk, 0: no, 1: yes',
  `icon_uri` VARCHAR(1024) COMMENT 'the avatar of the approval',
  `update_time` DATETIME DEFAULT NULL COMMENT 'last update time',
  `create_time` DATETIME COMMENT 'record create time',
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `eh_general_form_templates`;
CREATE TABLE `eh_general_form_templates` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_id` BIGINT NOT NULL DEFAULT '0',
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT DEFAULT 0 COMMENT 'the module id',
  `module_type` VARCHAR(64),
  `form_name` VARCHAR(64) NOT NULL,
  `version` BIGINT NOT NULL DEFAULT '0' COMMENT 'the version of the form template',
  `template_type` VARCHAR(128) NOT NULL COMMENT 'the type of template text',
  `template_text` TEXT COMMENT 'json 存放表单字段',
  `modify_flag` TINYINT DEFAULT 1 COMMENT 'whether the form can be modified from desk, 0: no, 1: yes',
  `delete_flag` TINYINT DEFAULT 1 COMMENT 'whether the form can be deleted from desk, 0: no, 1: yes',
  `update_time` DATETIME DEFAULT NULL COMMENT 'last update time',
  `create_time` DATETIME DEFAULT NULL COMMENT 'record create time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ended by R