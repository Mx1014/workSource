-- from club 3.2 start
-- 行业协会类型
CREATE TABLE `eh_industry_types` (
  `id` BIGINT(20) NOT NULL,
  `uuid` VARCHAR(128) NOT NULL,
  `namespace_id` INT(11) NOT NULL,
  `name` VARCHAR(32) NOT NULL,
  `create_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_guild_applies` (
  `id` BIGINT(20) NOT NULL,
  `uuid` VARCHAR(128) NOT NULL,
  `namespace_id` INT(11) NOT NULL,
  `group_id` BIGINT(22) NOT NULL,
  `applicant_uid` BIGINT(22) NOT NULL,
  `group_member_id` BIGINT(22) NOT NULL,
  `avatar` VARCHAR(255) DEFAULT NULL,
  `name` VARCHAR(255) DEFAULT NULL,
  `phone` VARCHAR(18) DEFAULT NULL,
  `email` VARCHAR(255) DEFAULT NULL,
  `organization_name` VARCHAR(255) DEFAULT NULL,
  `registered_capital` VARCHAR(255) DEFAULT NULL,
  `industry_type` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_uid` BIGINT(22) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 俱乐部类型，普通俱乐部、行业协会
ALTER TABLE `eh_group_settings` ADD COLUMN `club_type`  TINYINT(4) NOT NULL DEFAULT 0 COMMENT '0-normal club, 1-guild club' ;
-- 未加入俱乐部成员在俱乐部论坛的权限 0-不可见，1-可见，2-可交互
ALTER TABLE `eh_groups` ADD COLUMN `tourist_post_policy`  TINYINT(4) NULL DEFAULT 2 COMMENT '0-hide, 1-see only, 2-interact';
-- 俱乐部类型，普通俱乐部、行业协会
ALTER TABLE `eh_groups` ADD COLUMN `club_type`  TINYINT(4) NULL DEFAULT 0 COMMENT '0-normal club, 1-guild club' ;

ALTER TABLE `eh_groups` ADD COLUMN `phone_number`  VARCHAR(18) NULL ;

ALTER TABLE `eh_groups`  ADD COLUMN `description_type`  TINYINT(4) NULL DEFAULT 0;

-- 拒绝理由
ALTER TABLE `eh_group_member_logs` ADD COLUMN `reject_text`  VARCHAR(255) NULL;

-- from club 3.2 end

-- 加表单关联字段  add by xq.tian  2017/11/20
ALTER TABLE eh_flows ADD COLUMN `form_origin_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'flow ref form id';
ALTER TABLE eh_flows ADD COLUMN `form_version` BIGINT NOT NULL DEFAULT 0 COMMENT 'flow ref form version';
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
  `form_template_id` BIGINT(20) NOT NULL DEFAULT 0 COMMENT 'The id of the template form',
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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- ended by R












-- added by wh punch-3.4
ALTER TABLE `eh_punch_exception_requests` ADD COLUMN `punch_type` TINYINT DEFAULT 2 COMMENT ' 0- 上班打卡 ; 1- 下班打卡';  
ALTER TABLE `eh_punch_exception_requests` ADD COLUMN `begin_time` DATETIME COMMENT ' 请假/加班 生效开始时间';  
ALTER TABLE `eh_punch_exception_requests` ADD COLUMN `end_time` DATETIME COMMENT ' 请假/加班 生效结束时间';  
ALTER TABLE `eh_punch_exception_requests` ADD COLUMN `duration` DOUBLE COMMENT ' 请假/加班 时长-可供计算';  
ALTER TABLE `eh_punch_exception_requests` ADD COLUMN `category_id` BIGINT COMMENT ' 请假类型';  
ALTER TABLE `eh_punch_exception_requests` ADD COLUMN `approval_attribute` VARCHAR(128) COMMENT 'DEFAULT,CUSTOMIZE'; 
ALTER TABLE `eh_punch_exception_requests` CHANGE `view_flag` `view_flag` TINYINT DEFAULT '1' COMMENT 'is view(0) not view(1)';

ALTER TABLE `eh_punch_exception_approvals` ADD COLUMN `punch_type` TINYINT DEFAULT 2 COMMENT ' 0- 上班打卡 ; 1- 下班打卡';  

ALTER TABLE `eh_punch_logs` ADD COLUMN `approval_status` TINYINT DEFAULT NULL COMMENT '校正后的打卡状态 0-正常 null-没有异常校准';
ALTER TABLE `eh_punch_logs` ADD COLUMN `smart_alignment` TINYINT DEFAULT 0 COMMENT '只能校准状态 0-非校准 1-校准';
ALTER TABLE `eh_punch_day_logs` ADD COLUMN `smart_alignment` VARCHAR(128) DEFAULT NULL COMMENT '智能校准状态:1-未智能校准 0-未校准 例如:0;1/0;1/1/0/1';

-- 工作流业务类型字段 add by xq.tian 2017/22/21
ALTER TABLE eh_flow_service_types ADD COLUMN `owner_type` VARCHAR(64) COMMENT 'ownerType, e.g: EhOrganizations';
ALTER TABLE eh_flow_service_types ADD COLUMN `owner_id` BIGINT COMMENT 'ownerId, e.g: eh_organizations id';

-- 条件表达式的默认值  add by xq.tian  2017/11/20
ALTER TABLE eh_flow_condition_expressions MODIFY variable1 VARCHAR(64) NOT NULL DEFAULT '';
ALTER TABLE eh_flow_condition_expressions MODIFY variable2 VARCHAR(64) NOT NULL DEFAULT '';

ALTER TABLE eh_flow_condition_expressions MODIFY variable_type1 VARCHAR(64) NOT NULL DEFAULT '';
ALTER TABLE eh_flow_condition_expressions MODIFY variable_type2 VARCHAR(64) NOT NULL DEFAULT '';

-- 表增加索引 add by xiongying20171128
ALTER TABLE eh_equipment_inspection_tasks UNIQUE INDEX `equipment_id_uniqueIndex` (`equipment_id`) ;
ALTER TABLE eh_energy_date_statistics UNIQUE INDEX `unionmeter_stat_uniqueIndex` (`meter_id`,`stat_date`) ;
ALTER TABLE eh_equipment_inspection_item_results UNIQUE INDEX `task_log_uniqueIndex` (`task_log_id`) ;
