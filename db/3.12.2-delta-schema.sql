-- merge from flow-delta-schema.sql by lqs 20161214
-- eh_flows
-- DROP TABLE IF EXISTS `eh_flows`;
CREATE TABLE `eh_flows` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `module_type` VARCHAR(64) NOT NULL,
  `project_id` BIGINT NOT NULL DEFAULT 0,
  `project_type` VARCHAR(64),

  `flow_main_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the real flow id for all copy, the first flow_main_id=0',
  `flow_version` INTEGER NOT NULL DEFAULT 0 COMMENT 'current flow version',
  `flow_name` VARCHAR(64) NOT NULL COMMENT 'the name of flow',

  `status` TINYINT NOT NULL COMMENT 'invalid, config, running, pending, stop',
  `stop_time` DATETIME NOT NULL COMMENT 'last stop time',
  `run_time` DATETIME NOT NULL COMMENT 'last run time',
  `update_time` DATETIME NOT NULL COMMENT 'last run time',
  `create_time` DATETIME NOT NULL COMMENT 'record create time',

  `start_node` BIGINT NOT NULL DEFAULT 0,
  `end_node` BIGINT NOT NULL DEFAULT 0,
  `last_node` BIGINT NOT NULL DEFAULT 0,

  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `integral_tag1` BIGINT DEFAULT 0,
  `integral_tag2` BIGINT DEFAULT 0,
  `integral_tag3` BIGINT DEFAULT 0,
  `integral_tag4` BIGINT DEFAULT 0,
  `integral_tag5` BIGINT DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_stats`;
CREATE TABLE `eh_flow_stats` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,

  `node_level` INTEGER NOT NULL,
  `running_count` INTEGER NOT NULL,
  `enter_count` INTEGER NOT NULL,
  `leave_count` INTEGER NOT NULL,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_nodes`;
CREATE TABLE `eh_flow_nodes` (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,

    `flow_main_id` BIGINT NOT NULL,
    `flow_version` INTEGER NOT NULL,
    `node_name` VARCHAR(64) NOT NULL,
    `description` VARCHAR(1024),
    `node_level` INTEGER NOT NULL,
    `auto_step_minute` INTEGER NOT NULL DEFAULT 0 COMMENT 'after hour, step next',
    `auto_step_type` VARCHAR(64) COMMENT 'ApproveStep, RejectStep, EndStep',
    `allow_applier_update` TINYINT NOT NULL DEFAULT 0 COMMENT 'allow applier update content',
    `allow_timeout_action` TINYINT NOT NULL DEFAULT 0 COMMENT '1: allow timeout action',
    `create_time` DATETIME NOT NULL COMMENT 'record create time',
    `params` VARCHAR(64) COMMENT 'the params from other module',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'invalid, valid',

    PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_buttons`;
CREATE TABLE `eh_flow_buttons` (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,

    `flow_main_id` BIGINT NOT NULL,
    `flow_version` INTEGER NOT NULL,
    `flow_node_id` BIGINT NOT NULL,
    `button_name` VARCHAR(64),
    `description` VARCHAR(1024),
    `flow_step_type` VARCHAR(64) COMMENT 'no_step, start_step, approve_step, reject_step, transfer_step, comment_step, end_step, notify_step',
    `flow_user_type` VARCHAR(64) COMMENT 'applier, processor',
    `goto_level` INTEGER NOT NULL DEFAULT 0,
    `goto_node_id` BIGINT NOT NULL DEFAULT 0,
    `need_subject` TINYINT NOT NULL DEFAULT 0 COMMENT '0: not need subject for this step, 1: need subject for this step',
    `need_processor` TINYINT NOT NULL DEFAULT 0 COMMENT '0: not need processor, 1: need only one processor',
    `remind_count` INTEGER NOT NULL DEFAULT 0,
    `create_time` DATETIME NOT NULL COMMENT 'record create time',
    `status` TINYINT NOT NULL COMMENT '0: invalid, 1: disabled, 2: enabled',

    `string_tag1` VARCHAR(128),
    `string_tag2` VARCHAR(128),
    `string_tag3` VARCHAR(128),
    `string_tag4` VARCHAR(128),
    `string_tag5` VARCHAR(128),
    `integral_tag1` BIGINT DEFAULT 0,
    `integral_tag2` BIGINT DEFAULT 0,
    `integral_tag3` BIGINT DEFAULT 0,
    `integral_tag4` BIGINT DEFAULT 0,
    `integral_tag5` BIGINT DEFAULT 0,

    PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_forms`;
CREATE TABLE `eh_flow_forms` (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,

    `flow_main_id` BIGINT NOT NULL,
    `flow_version` INTEGER NOT NULL,

    `form_name` VARCHAR(64),
    `form_type` VARCHAR(64) COMMENT 'text, datetime, checkbox, radiobox, selection',
    `form_default` TEXT,
    `form_render` TEXT,
    `belong_to` BIGINT NOT NULL,
    `belong_entity` VARCHAR(64) NOT NULL COMMENT 'flow_node, flow_button, flow',
    `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',

    `string_tag1` VARCHAR(128),
    `string_tag2` VARCHAR(128),
    `string_tag3` VARCHAR(128),
    `string_tag4` VARCHAR(128),
    `string_tag5` VARCHAR(128),
    `integral_tag1` BIGINT DEFAULT 0,
    `integral_tag2` BIGINT DEFAULT 0,
    `integral_tag3` BIGINT DEFAULT 0,
    `integral_tag4` BIGINT DEFAULT 0,
    `integral_tag5` BIGINT DEFAULT 0,

    PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_actions`;
CREATE TABLE `eh_flow_actions` (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,

    `flow_main_id` BIGINT NOT NULL,
    `flow_version` INTEGER NOT NULL,
    `action_type` VARCHAR(64) NOT NULL COMMENT 'sms, message, tick_sms, tick_message, tracker, scripts',
    `belong_to` BIGINT NOT NULL,
    `belong_entity` VARCHAR(64) NOT NULL COMMENT 'flow_node, flow_button, flow',
    `flow_step_type` VARCHAR(64) COMMENT 'no_step, start_step, approve_step, reject_step, transfer_step, comment_step, end_step, notify_step',
    `action_step_type` VARCHAR(64) NOT NULL COMMENT 'step_none, step_timeout, step_enter, step_leave',
    `status` TINYINT NOT NULL COMMENT 'invalid, valid',
    `create_time` DATETIME NOT NULL COMMENT 'record create time',
    `render_text` VARCHAR(256) COMMENT 'the content for this message that have variables',

    `string_tag1` VARCHAR(128),
    `string_tag2` VARCHAR(128),
    `string_tag3` VARCHAR(128),
    `string_tag4` VARCHAR(128),
    `string_tag5` VARCHAR(128),
    `integral_tag1` BIGINT DEFAULT 0,
    `integral_tag2` BIGINT DEFAULT 0,
    `integral_tag3` BIGINT DEFAULT 0,
    `integral_tag4` BIGINT DEFAULT 0,
    `integral_tag5` BIGINT DEFAULT 0,

    PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_user_selections`;
CREATE TABLE `eh_flow_user_selections` (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,

    `flow_main_id` BIGINT NOT NULL,
    `flow_version` INTEGER NOT NULL,
    `organization_id` BIGINT NOT NULL DEFAULT 0,

    `select_type` VARCHAR(64) NOT NULL COMMENT 'department, position, manager, variable',
    `source_id_a` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to other user object id',
    `source_type_a` VARCHAR(64) COMMENT 'community, organization, user, variable',
    `source_id_b` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to other user object id',
    `source_type_b` VARCHAR(64) COMMENT 'community, organization, user, variable',
    `belong_to` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to other flow object id',
    `belong_entity` VARCHAR(64) NOT NULL COMMENT 'flow, flow_node, flow_button, flow_action',
    `belong_type` VARCHAR(64) NOT NULL COMMENT 'flow_superviser, flow_node_processor, flow_node_applier, flow_button_clicker, flow_action_processor',
    `selection_name` VARCHAR(64),
    `status` TINYINT NOT NULL COMMENT 'invalid, valid',
    `create_time` DATETIME NOT NULL COMMENT 'record create time',

    PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_cases`;
CREATE TABLE `eh_flow_cases` (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,

    `owner_id` BIGINT NOT NULL,
    `owner_type` VARCHAR(64) NOT NULL,
    `module_id` BIGINT NOT NULL COMMENT 'the module id',
    `module_type` VARCHAR(64) NOT NULL,
    `project_id` BIGINT NOT NULL DEFAULT 0,
    `project_type` VARCHAR(64),
    `module_name` VARCHAR(64),
    `applier_name` VARCHAR(64),
    `applier_phone` VARCHAR(64),

    `flow_main_id` BIGINT NOT NULL,
    `flow_version` INTEGER NOT NULL,

    `apply_user_id` BIGINT NOT NULL,
    `process_user_id` BIGINT NOT NULL DEFAULT 0,
    `refer_id` BIGINT NOT NULL DEFAULT 0,
    `refer_type` VARCHAR(64) NOT NULL,
    `current_node_id` BIGINT NOT NULL DEFAULT 0,
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'invalid, initial, process, end',
    `reject_count` INTEGER NOT NULL DEFAULT 0,
    `reject_node_id` BIGINT NOT NULL DEFAULT 0,
    `step_count` BIGINT NOT NULL DEFAULT 0,
    `last_step_time` DATETIME NOT NULL COMMENT 'state change time',
    `create_time` DATETIME NOT NULL COMMENT 'record create time',
    `case_type` VARCHAR(64) COMMENT 'inner, outer etc',
    `content` TEXT,
    `evaluate_score` INTEGER NOT NULL DEFAULT 0,

    `string_tag1` VARCHAR(128),
    `string_tag2` VARCHAR(128),
    `string_tag3` VARCHAR(128),
    `string_tag4` VARCHAR(128),
    `string_tag5` VARCHAR(128),
    `integral_tag1` BIGINT DEFAULT 0,
    `integral_tag2` BIGINT DEFAULT 0,
    `integral_tag3` BIGINT DEFAULT 0,
    `integral_tag4` BIGINT DEFAULT 0,
    `integral_tag5` BIGINT DEFAULT 0,

    PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_event_logs`;
CREATE TABLE `eh_flow_event_logs` (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,

    `parent_id` BIGINT NOT NULL DEFAULT 0,
    `flow_main_id` BIGINT NOT NULL,
    `flow_version` INTEGER NOT NULL,
    `flow_node_id` BIGINT NOT NULL DEFAULT 0,
    `flow_case_id` BIGINT NOT NULL DEFAULT 0,
    `flow_button_id` BIGINT NOT NULL DEFAULT 0,
    `flow_action_id` BIGINT NOT NULL DEFAULT 0,
    `flow_user_id` BIGINT NOT NULL DEFAULT 0,
    `flow_user_name` VARCHAR(64),
    `flow_selection_id` BIGINT NOT NULL DEFAULT 0,
    `subject_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the post id for this event',
    `step_count` BIGINT NOT NULL DEFAULT 0,
    `log_type` VARCHAR(64) NOT NULL COMMENT 'flow_step, button_click, action_result',
    `log_title` VARCHAR(64) COMMENT 'the title of this log',
    `log_content` TEXT,
    `create_time` DATETIME NOT NULL COMMENT 'record create time',

    `string_tag1` VARCHAR(128),
    `string_tag2` VARCHAR(128),
    `string_tag3` VARCHAR(128),
    `string_tag4` VARCHAR(128),
    `string_tag5` VARCHAR(128),
    `integral_tag1` BIGINT DEFAULT 0,
    `integral_tag2` BIGINT DEFAULT 0,
    `integral_tag3` BIGINT DEFAULT 0,
    `integral_tag4` BIGINT DEFAULT 0,
    `integral_tag5` BIGINT DEFAULT 0,

    PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_variables`;
CREATE TABLE `eh_flow_variables` (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,

    `owner_id` BIGINT NOT NULL,
    `owner_type` VARCHAR(64) NOT NULL,
    `module_id` BIGINT NOT NULL COMMENT 'the module id',
    `module_type` VARCHAR(64) NOT NULL,

    `name` VARCHAR(64),
    `label` VARCHAR(64),
    `var_type` VARCHAR(64) NOT NULL COMMENT 'text, node_user',
    `script_type` VARCHAR(64) NOT NULL COMMENT 'bean_id, prototype',
    `script_cls` VARCHAR(1024) NOT NULL COMMENT 'the class prototype in java',
    `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',

    PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_evaluates`;
CREATE TABLE `eh_flow_evaluates` (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,

    `owner_id` BIGINT NOT NULL,
    `owner_type` VARCHAR(64) NOT NULL,
    `module_id` BIGINT NOT NULL COMMENT 'the module id',
    `module_type` VARCHAR(64) NOT NULL,

    `star` TINYINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `flow_node_id` BIGINT NOT NULL,
    `flow_case_id` BIGINT NOT NULL,
    `flow_main_id` BIGINT NOT NULL,
    `flow_version` INTEGER NOT NULL,
    `create_time` DATETIME NOT NULL COMMENT 'record create time',

    PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- scripts for this module
-- DROP TABLE IF EXISTS `eh_flow_scripts`;
CREATE TABLE `eh_flow_scripts` (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,

    `owner_id` BIGINT NOT NULL,
    `owner_type` VARCHAR(64) NOT NULL,
    `module_id` BIGINT NOT NULL COMMENT 'the module id',
    `module_type` VARCHAR(64) NOT NULL,

    `name` VARCHAR(64) NOT NULL,
    `script_type` VARCHAR(64) NOT NULL COMMENT 'bean_id, prototype',
    `script_cls` VARCHAR(1024) NOT NULL COMMENT 'the class prototype in java',
    `flow_step_type` VARCHAR(64) COMMENT 'no_step, start_step, approve_step, reject_step, transfer_step, comment_step, end_step, notify_step',
    `step_type` VARCHAR(64) NOT NULL COMMENT 'step_none, step_timeout, step_enter, step_leave',

    PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_subjects`;
CREATE TABLE `eh_flow_subjects` (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,

    `title` VARCHAR(64),
    `content` TEXT,

    `belong_to` BIGINT NOT NULL,
    `belong_entity` VARCHAR(64) NOT NULL COMMENT 'flow_node, flow_button, flow',
    `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
    `create_time` DATETIME NOT NULL COMMENT 'record create time',

    PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_attachments`;
CREATE TABLE `eh_flow_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_id` BIGINT NOT NULL COMMENT 'owner id, e.g comment_id',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_timeouts`;
CREATE TABLE `eh_flow_timeouts` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `belong_to` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to other flow object id',
    `belong_entity` VARCHAR(64) NOT NULL COMMENT 'flow, flow_node, flow_button, flow_action',
    `timeout_type` VARCHAR(64) NOT NULL COMMENT 'flow_step_timeout',
    `timeout_tick` DATETIME NOT NULL,
    `json` TEXT,
    `create_time` DATETIME NOT NULL,
    `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;





-- merge from activity1.6-delta-schema.sql by lqs 20161214
ALTER TABLE eh_activities ADD COLUMN `achievement` TEXT;
ALTER TABLE eh_activities ADD COLUMN `achievement_type` VARCHAR(32) COMMENT 'richtext, link';
ALTER TABLE eh_activities ADD COLUMN `achievement_richtext_url` VARCHAR(512) COMMENT 'richtext page';

-- 活动附件表
-- DROP TABLE IF EXISTS  `eh_activity_attachments`;
CREATE TABLE `eh_activity_attachments` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`activity_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner id, e.g application_id',
    `name` VARCHAR(128),
    `file_size` INTEGER NOT NULL DEFAULT 0,
	`content_type` VARCHAR(32) COMMENT 'attachment object content type',
	`content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
    `download_count` INTEGER NOT NULL DEFAULT 0,
	`creator_uid` BIGINT NOT NULL DEFAULT 0,
	`create_time` DATETIME NOT NULL, 
	
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 活动物资管理表
-- DROP TABLE IF EXISTS  `eh_activity_goods`;
CREATE TABLE `eh_activity_goods` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`activity_id` BIGINT NOT NULL COMMENT 'owner id, e.g application_id',
	`name` VARCHAR(64) COMMENT 'attachment object content type',
	`price` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
    `quantity` INTEGER NOT NULL DEFAULT 0,
    `total_price` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
	`handlers` VARCHAR(64),
	`create_time` DATETIME NOT NULL, 
    `creator_uid` BIGINT,
	
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- merge from terminal-stat-delta-schema.sql by by sfyan 20161214
-- 运营统计 by sfyan 20161214
-- 终端app版本累计用户
-- DROP TABLE IF EXISTS `eh_terminal_app_version_cumulatives`;
CREATE TABLE `eh_terminal_app_version_cumulatives` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `app_version_realm` VARCHAR(128) DEFAULT NULL,
  `app_version` VARCHAR(128) DEFAULT NULL,
  `imei_number` VARCHAR(128) DEFAULT '',
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 终端app版本活跃用户
-- DROP TABLE IF EXISTS `eh_terminal_app_version_actives`;
CREATE TABLE `eh_terminal_app_version_actives` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `app_version_realm` VARCHAR(128) DEFAULT NULL,
  `app_version` VARCHAR(128) DEFAULT NULL,
  `imei_number` VARCHAR(128) DEFAULT '',
  `date` VARCHAR(32) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 终端日统计
-- DROP TABLE IF EXISTS `eh_terminal_day_statistics`;
CREATE TABLE `eh_terminal_day_statistics` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `new_user_number` BIGINT NOT NULL COMMENT 'number of new users',
  `active_user_number` BIGINT NOT NULL COMMENT 'number of active users',
  `start_change_rate` DECIMAL(10,2) NOT NULL COMMENT 'today compared to yesterdays rate of change.',
  `new_change_rate` DECIMAL(10,2) NOT NULL COMMENT 'today compared to yesterdays rate of change.',
  `active_change_rate` DECIMAL(10,2) NOT NULL COMMENT 'today compared to yesterdays rate of change.',
  `cumulative_change_rate` DECIMAL(10,2) NOT NULL COMMENT 'today compared to yesterdays rate of change.',
  `active_rate` DECIMAL(10,2) NOT NULL COMMENT 'active_user_number/accumulative_user_number',
  `start_number` BIGINT NOT NULL COMMENT 'number of starts',
  `cumulative_user_number` BIGINT NOT NULL COMMENT 'cumulative number of users',
  `seven_active_user_number` BIGINT NOT NULL COMMENT '7 active number of users',
  `thirty_active_user_number` BIGINT NOT NULL COMMENT '30 active number of users',
  `date` VARCHAR(32) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 终端时统计
-- DROP TABLE IF EXISTS `eh_terminal_hour_statistics`;
CREATE TABLE `eh_terminal_hour_statistics` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `new_user_number` BIGINT NOT NULL COMMENT 'number of new users',
  `active_user_number` BIGINT NOT NULL COMMENT 'number of active users',
  `change_rate` DECIMAL(10,2) NOT NULL COMMENT 'today compared to yesterdays rate of change.',
  `active_rate` DECIMAL(10,2) NOT NULL COMMENT 'active_user_number/accumulative_user_number',
  `start_number` BIGINT NOT NULL COMMENT 'number of starts',
  `cumulative_user_number` BIGINT NOT NULL COMMENT 'cumulative number of users',
  `date` VARCHAR(32) DEFAULT NULL,
  `hour` VARCHAR(16) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 终端app版本统计
-- DROP TABLE IF EXISTS `eh_terminal_app_version_statistics`;
CREATE TABLE `eh_terminal_app_version_statistics` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `app_version_realm` VARCHAR(128) DEFAULT NULL,
  `app_version` VARCHAR(128) DEFAULT NULL,
  `new_user_number` BIGINT NOT NULL COMMENT 'number of new users',
  `start_number` BIGINT NOT NULL COMMENT 'number of starts',
  `active_user_number` BIGINT NOT NULL COMMENT 'number of active users',
  `cumulative_user_number` BIGINT NOT NULL COMMENT 'cumulative of active users',
  `version_cumulative_rate` DECIMAL(10,2) NOT NULL,
  `version_active_rate` DECIMAL(10,2) NOT NULL,
  `date` VARCHAR(32) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 统计任务记录
-- DROP TABLE IF EXISTS `eh_app_version`;
CREATE TABLE `eh_terminal_statistics_tasks` (
  `id` BIGINT NOT NULL,
  `task_no` VARCHAR(20) NOT NULL,
  `status` TINYINT DEFAULT NULL COMMENT '10 生成日统计数据 20 生成时统计数据 30 生成版本统计数据 100 完成',
  `update_Time` DATETIME DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `task_no` (`task_no`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- app版本
-- DROP TABLE IF EXISTS `eh_app_version`;
CREATE TABLE `eh_app_version` (
  `id` BIGINT NOT NULL,
  `type` VARCHAR(20) NOT NULL,
  `name` VARCHAR(64) NOT NULL,
  `realm` VARCHAR(64) NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `default_order` INTEGER NOT NULL DEFAULT '0',
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 活动数据增加域空间字段
ALTER TABLE `eh_user_activities` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_user_activities` ADD COLUMN `version_realm` VARCHAR(128) DEFAULT NULL;

-- 菜单增加模块id字段
ALTER TABLE `eh_web_menus` ADD COLUMN `module_id` BIGINT DEFAULT NULL;



-- 
-- 服务热线配置表
-- 
-- DROP TABLE IF EXISTS `eh_service_configurations`;

CREATE TABLE `eh_service_configurations` (
  `id` BIGINT AUTO_INCREMENT COMMENT 'id of the record',
  `owner_type` VARCHAR(64) COMMENT 'community;group,organaization,exhibition,',
  `owner_id` BIGINT DEFAULT '0',
  `name` VARCHAR(64),
  `value` VARCHAR(64),
  `description` VARCHAR(256) ,
  `namespace_id` INT DEFAULT '0',
  `display_name` VARCHAR(128) ,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_config` (`owner_type`,`owner_id`,`name`,`value`,`namespace_id`)
) ENGINE=INNODB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8mb4 ;



-- 
-- 服务热线表
-- 
-- DROP TABLE IF EXISTS `eh_service_hotlines`;
CREATE TABLE `eh_service_hotlines` (
  `id` BIGINT COMMENT 'id of the record',
  `namespace_id` INTEGER DEFAULT '0',
  `owner_type` VARCHAR(64) COMMENT 'community;group,organaization,exhibition,',
  `owner_id` BIGINT(20) DEFAULT '0',
  `service_type` INT COMMENT'1-公共热线 2-专属客服 4- 8-', 
  `name` VARCHAR(64) COMMENT '热线/客服名称', 
  `contact` VARCHAR(64) COMMENT '热线/客服 联系电话',
  `user_id` BIGINT COMMENT '客服 userid', 
  `description` VARCHAR(400) COMMENT '客服 描述',
  `default_order` INTEGER DEFAULT '0' COMMENT '排序字段',
  `create_time` DATETIME DEFAULT NULL,
  `creator_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;



-- added by wh 2016-11-24 增加资源编号关联性
 
ALTER TABLE `eh_rentalv2_resource_numbers` ADD COLUMN `number_group` INTEGER COMMENT '同一个groupid的两个编号资源被认为是一组资源';
ALTER TABLE `eh_rentalv2_resource_numbers` ADD COLUMN `group_lock_flag` TINYINT COMMENT '一个资源被预约是否锁整个group,0-否,1-是';


ALTER TABLE `eh_rentalv2_cells` ADD COLUMN `number_group` INTEGER COMMENT '同一个groupid的两个编号资源被认为是一组资源';
ALTER TABLE `eh_rentalv2_cells` ADD COLUMN `group_lock_flag` TINYINT COMMENT '一个资源被预约是否锁整个group,0-否,1-是';

ALTER TABLE `eh_rentalv2_orders` ADD COLUMN `reminder_time` DATETIME     COMMENT '消息提醒时间';

ALTER TABLE `eh_rentalv2_resource_orders` ADD COLUMN `status` TINYINT  DEFAULT 0 COMMENT '状态 0-普通预定订单 1-不显示给用户的';

-- merge from parking-clearance-1.0-delta-schema.sql by lqs 20161215
--
-- 车辆放行申请人员与处理人员
--
-- DROP TABLE IF EXISTS `eh_parking_clearance_operators`;
CREATE TABLE `eh_parking_clearance_operators` (
  `id`              BIGINT  NOT NULL,
  `namespace_id`    INTEGER NOT NULL DEFAULT 0,
  `organization_id` BIGINT,
  `community_id`    BIGINT,
  `parking_lot_id`  BIGINT COMMENT 'eh_parking_lots id',
  `operator_type`   VARCHAR(32) COMMENT 'applicant, handler',
  `operator_id`     BIGINT,
  `status`          TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`     BIGINT,
  `create_time`     DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 车辆放行记录
--
-- DROP TABLE IF EXISTS `eh_parking_clearance_logs`;
CREATE TABLE `eh_parking_clearance_logs` (
  `id`             BIGINT  NOT NULL,
  `namespace_id`   INTEGER NOT NULL DEFAULT 0,
  `community_id`   BIGINT,
  `parking_lot_id` BIGINT COMMENT 'eh_parking_lots id',
  `applicant_id`   BIGINT COMMENT 'applicant id',
  `operator_id`    BIGINT COMMENT 'operator id',
  `plate_number`   VARCHAR(32) COMMENT 'plate number',
  `apply_time`     DATETIME COMMENT 'apply time',
  `clearance_time` DATETIME COMMENT 'The time the vehicle passed',
  `remarks`        VARCHAR(1024) COMMENT 'remarks',
  `status`         TINYINT COMMENT '0: inactive, 1: processing, 2: completed, 3: cancelled, 4: pending',
  `creator_uid`    BIGINT,
  `create_time`    DATETIME,
  `update_uid`     BIGINT,
  `update_time`    DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 停车充值5.0  merge from parking-delta-schema.sql by sw 20161215
ALTER TABLE eh_parking_lots ADD COLUMN `recharge_month_count` INT NOT NUll DEFAULT 0 COMMENT 'organization of address';
ALTER TABLE eh_parking_lots ADD COLUMN `recharge_type` TINYINT NOT NULL DEFAULT 0 COMMENT '1: all month, 2: number of days';
ALTER TABLE eh_parking_lots ADD COLUMN `is_support_recharge` TINYINT NOT NULL DEFAULT 0 COMMENT 'out date card recharge flag , 1: support recharge , 0: not ';
ALTER TABLE eh_parking_lots ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;

ALTER TABLE eh_parking_card_requests ADD COLUMN `car_brand` VARCHAR(64) COMMENT 'car brand';
ALTER TABLE eh_parking_card_requests ADD COLUMN `car_color` VARCHAR(64) COMMENT 'car color';
ALTER TABLE eh_parking_card_requests ADD COLUMN `car_serie_name` VARCHAR(64) COMMENT 'car serie name';
ALTER TABLE eh_parking_card_requests ADD COLUMN `car_serie_id` BIGINT COMMENT 'car serie id';
ALTER TABLE eh_parking_card_requests ADD COLUMN `flow_id` BIGINT COMMENT 'flow id';
ALTER TABLE eh_parking_card_requests ADD COLUMN `flow_version` INTEGER NOT NULL DEFAULT 0 COMMENT 'current flow version';
ALTER TABLE eh_parking_card_requests ADD COLUMN `flow_case_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'flow case id';

ALTER TABLE eh_parking_card_requests ADD COLUMN `audit_succeed_time` DATETIME;
ALTER TABLE eh_parking_card_requests ADD COLUMN `process_succeed_time` DATETIME;
ALTER TABLE eh_parking_card_requests ADD COLUMN `open_card_time` DATETIME;
ALTER TABLE eh_parking_card_requests ADD COLUMN `cancel_time` DATETIME;

-- DROP TABLE IF EXISTS `eh_parking_flow`;
CREATE TABLE `eh_parking_flow` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `parking_lot_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_parking_lots',

  `request_month_count` INT NOT NUll DEFAULT 0 COMMENT 'organization of address',
  `request_recharge_type` TINYINT NOT NULL DEFAULT 0 COMMENT '1: all month, 2: number of days',
  `card_request_tip_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '1: support , 0: not ',
  `card_agreement_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '1: support , 0: not ',
  `max_request_num_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '1: support , 0: not ',
  `max_issue_num_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '1: support , 0: not ',
  `card_request_tip` TEXT,
  `card_agreement` TEXT,
  `max_issue_num` INTEGER NOT NULL DEFAULT 1 COMMENT 'the max num of the issue card',
  `max_request_num` INTEGER NOT NULL DEFAULT 1 COMMENT 'the max num of the request card',
  `flow_id` BIGINT NOT NULL COMMENT 'flow id',


  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_parking_statistics`;
CREATE TABLE `eh_parking_statistics` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `parking_lot_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_parking_lots',

  `amount` DECIMAL(10,2),

  `date_str` DATETIME,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_parking_car_series`;
CREATE TABLE `eh_parking_car_series`(
  `id` BIGINT NOT NULL,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(64) NOT NULL,
  `path` VARCHAR(128),
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `create_time` DATETIME,
  `delete_time` DATETIME,

  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- DROP TABLE IF EXISTS `eh_parking_attachments`;
CREATE TABLE `eh_parking_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) COMMENT 'attachment object owner type',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner id, e.g comment_id',
  `data_type` TINYINT NOT NULL DEFAULT 0 COMMENT '1: 身份证, 2: 行驶证 3:驾驶证',

  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- App启动广告   add by xq.tian  2016/11/28
--
-- DROP TABLE IF EXISTS `eh_launch_advertisements`;
CREATE TABLE `eh_launch_advertisements` (
  `id` BIGINT  NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `content_type` VARCHAR(32) COMMENT '1: IMAGE, 2: VIDEO',
  `content_uri` VARCHAR(1024) COMMENT 'advertisement image/gif/video uri',
  `times_per_day` INTEGER DEFAULT 0 COMMENT 'The maximum number of times to display per day',
  `display_interval` INTEGER DEFAULT 0 COMMENT 'Minimum display time interval, ',
  `duration_time` INTEGER COMMENT 'duration time',
  `skip_flag` TINYINT COMMENT '0: can not skip, 1: skip',
  `action_type` TINYINT COMMENT '0: can not click, 1: click',
  `action_data` TEXT COMMENT 'If allow click, the jumped url',
  `status` TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4;



-- merge from flow branch by lqs 20161217
ALTER TABLE `eh_flows` ADD COLUMN `need_evaluate` TINYINT NOT NULL DEFAULT 0 COMMENT '0: no evaluate, 1: need evaluate';
ALTER TABLE `eh_flows` ADD COLUMN `evaluate_start` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flows` ADD COLUMN `evaluate_end` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flows` ADD COLUMN `evaluate_step` VARCHAR(64) COMMENT 'NoStep, ApproveStep';

UPDATE `eh_flows` SET `integral_tag1`=0;
UPDATE `eh_flows` SET `integral_tag2`=0;
UPDATE `eh_flows` SET `integral_tag3`=0;
UPDATE `eh_flows` SET `integral_tag4`=0;
UPDATE `eh_flows` SET `integral_tag5`=0;
ALTER TABLE `eh_flows` MODIFY COLUMN `integral_tag1` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flows` MODIFY COLUMN `integral_tag2` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flows` MODIFY COLUMN `integral_tag3` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flows` MODIFY COLUMN `integral_tag4` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flows` MODIFY COLUMN `integral_tag5` BIGINT NOT NULL DEFAULT 0;

UPDATE `eh_flow_buttons` SET `integral_tag1`=0;
UPDATE `eh_flow_buttons` SET `integral_tag2`=0;
UPDATE `eh_flow_buttons` SET `integral_tag3`=0;
UPDATE `eh_flow_buttons` SET `integral_tag4`=0;
UPDATE `eh_flow_buttons` SET `integral_tag5`=0;
ALTER TABLE `eh_flow_buttons` MODIFY COLUMN `integral_tag1` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flow_buttons` MODIFY COLUMN `integral_tag2` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flow_buttons` MODIFY COLUMN `integral_tag3` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flow_buttons` MODIFY COLUMN `integral_tag4` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flow_buttons` MODIFY COLUMN `integral_tag5` BIGINT NOT NULL DEFAULT 0;

UPDATE `eh_flow_forms` SET `integral_tag1`=0;
UPDATE `eh_flow_forms` SET `integral_tag2`=0;
UPDATE `eh_flow_forms` SET `integral_tag3`=0;
UPDATE `eh_flow_forms` SET `integral_tag4`=0;
UPDATE `eh_flow_forms` SET `integral_tag5`=0;
ALTER TABLE `eh_flow_forms` MODIFY COLUMN `integral_tag1` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flow_forms` MODIFY COLUMN `integral_tag2` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flow_forms` MODIFY COLUMN `integral_tag3` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flow_forms` MODIFY COLUMN `integral_tag4` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flow_forms` MODIFY COLUMN `integral_tag5` BIGINT NOT NULL DEFAULT 0;

UPDATE `eh_flow_actions` SET `integral_tag1`=0;
UPDATE `eh_flow_actions` SET `integral_tag2`=0;
UPDATE `eh_flow_actions` SET `integral_tag3`=0;
UPDATE `eh_flow_actions` SET `integral_tag4`=0;
UPDATE `eh_flow_actions` SET `integral_tag5`=0;
ALTER TABLE `eh_flow_actions` MODIFY COLUMN `integral_tag1` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flow_actions` MODIFY COLUMN `integral_tag2` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flow_actions` MODIFY COLUMN `integral_tag3` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flow_actions` MODIFY COLUMN `integral_tag4` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flow_actions` MODIFY COLUMN `integral_tag5` BIGINT NOT NULL DEFAULT 0;


ALTER TABLE `eh_flow_user_selections` ADD COLUMN `params` VARCHAR(64);


UPDATE `eh_flow_cases` SET `integral_tag1`=0;
UPDATE `eh_flow_cases` SET `integral_tag2`=0;
UPDATE `eh_flow_cases` SET `integral_tag3`=0;
UPDATE `eh_flow_cases` SET `integral_tag4`=0;
UPDATE `eh_flow_cases` SET `integral_tag5`=0;
ALTER TABLE `eh_flow_cases` MODIFY COLUMN `integral_tag1` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flow_cases` MODIFY COLUMN `integral_tag2` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flow_cases` MODIFY COLUMN `integral_tag3` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flow_cases` MODIFY COLUMN `integral_tag4` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flow_cases` MODIFY COLUMN `integral_tag5` BIGINT NOT NULL DEFAULT 0;

UPDATE `eh_flow_event_logs` SET `integral_tag1`=0;
UPDATE `eh_flow_event_logs` SET `integral_tag2`=0;
UPDATE `eh_flow_event_logs` SET `integral_tag3`=0;
UPDATE `eh_flow_event_logs` SET `integral_tag4`=0;
UPDATE `eh_flow_event_logs` SET `integral_tag5`=0;
ALTER TABLE `eh_flow_event_logs` MODIFY COLUMN `integral_tag1` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flow_event_logs` MODIFY COLUMN `integral_tag2` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flow_event_logs` MODIFY COLUMN `integral_tag3` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flow_event_logs` MODIFY COLUMN `integral_tag4` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_flow_event_logs` MODIFY COLUMN `integral_tag5` BIGINT NOT NULL DEFAULT 0;

ALTER TABLE `eh_flow_evaluates` ADD COLUMN `project_id` BIGINT NOT NULL;
ALTER TABLE `eh_flow_evaluates` ADD COLUMN `project_type` VARCHAR(64);
ALTER TABLE `eh_flow_evaluates` ADD COLUMN `evaluate_item_id` BIGINT NOT NULL;

UPDATE `eh_flow_evaluates` SET `flow_node_id`=0;
ALTER TABLE `eh_flow_evaluates` MODIFY COLUMN `flow_node_id` BIGINT NOT NULL DEFAULT 0;

-- DROP TABLE IF EXISTS `eh_flow_evaluate_items`;
CREATE TABLE `eh_flow_evaluate_items` (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,

    `flow_main_id` BIGINT NOT NULL,
    `flow_version` INTEGER NOT NULL,
    `name` VARCHAR(128) NOT NULL,

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;