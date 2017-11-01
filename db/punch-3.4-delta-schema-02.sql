--
-- 条件
--
DROP TABLE IF EXISTS `eh_flow_conditions`;
CREATE TABLE `eh_flow_conditions` (
  `id` BIGINT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `condition_level` INTEGER NOT NULL DEFAULT 0,
  `flow_node_id` BIGINT NOT NULL,
  `flow_node_level` INTEGER,
  `flow_link_id` BIGINT,
  `flow_link_level` INTEGER,
  `next_node_id` BIGINT NOT NULL DEFAULT 0,
  `next_node_level` INTEGER NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 条件表达式
--
DROP TABLE IF EXISTS `eh_flow_condition_expressions`;
CREATE TABLE `eh_flow_condition_expressions` (
  `id` BIGINT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `flow_condition_id` BIGINT NOT NULL COMMENT 'ref eh_flow_conditions',
  `logic_operator` VARCHAR(24) NOT NULL COMMENT '&&, ||, !',
  `relational_operator` VARCHAR(24) NOT NULL COMMENT '>, <, ==, !=',
  `variable_type1` VARCHAR(32) NOT NULL DEFAULT 1 COMMENT 'const, variable',
  `variable1` VARCHAR(64) NOT NULL DEFAULT 1 COMMENT '${varName} or 2',
  `variable_type2` VARCHAR(32) NOT NULL DEFAULT 1 COMMENT 'const, variable',
  `variable2` VARCHAR(64) NOT NULL DEFAULT 1 COMMENT '${varName} or 2',
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 泳道
--
DROP TABLE IF EXISTS `eh_flow_lanes`;
CREATE TABLE `eh_flow_lanes` (
  `id` BIGINT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `display_name` VARCHAR(128) COMMENT 'lane name',
  `display_name_absort` VARCHAR(128) COMMENT 'when flowCase absort display this',
  `flow_node_level` INTEGER COMMENT 'flow_node_level',
  `identifier_node_level` INTEGER COMMENT '标识这个用泳道里的那个节点里的申请人按钮',
  `identifier_node_id` BIGINT COMMENT '标识这个用泳道里的那个节点里的申请人按钮',
  `lane_level` INTEGER COMMENT 'lane level',
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 流程图的连接
--
DROP TABLE IF EXISTS `eh_flow_links`;
CREATE TABLE `eh_flow_links` (
  `id` BIGINT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `display_name` VARCHAR(64) COMMENT 'display name',
  `link_level` INTEGER NOT NULL,
  `from_node_id` BIGINT NOT NULL,
  `from_node_level` INTEGER NOT NULL,
  `to_node_id` BIGINT NOT NULL,
  `to_node_level` INTEGER NOT NULL,
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 分支
--
DROP TABLE IF EXISTS `eh_flow_branches`;
CREATE TABLE `eh_flow_branches` (
  `id` BIGINT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `original_node_id` BIGINT NOT NULL COMMENT '分发开始节点id',
  `original_node_level` INTEGER NOT NULL COMMENT '分发开始节点level',
  `convergence_node_id` BIGINT NOT NULL COMMENT '最终收敛节点id',
  `convergence_node_level` INTEGER NOT NULL COMMENT '最终收敛节点level',
  `process_mode` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '单一路径：single, 并发执行：concurrent',
  `branch_decider` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'processor, condition',
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 节点和按钮的预定义参数
--
DROP TABLE IF EXISTS `eh_flow_predefined_params`;
CREATE TABLE `eh_flow_predefined_params` (
  `id` BIGINT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `module_type` VARCHAR(64) NOT NULL,
  `entity_type` VARCHAR(64) NOT NULL COMMENT 'flow_node, flow_button',
  `display_name` VARCHAR(128) COMMENT 'display name',
  `name` VARCHAR(64) COMMENT 'param name',
  `text` TEXT,
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 业务类别
--
DROP TABLE IF EXISTS `eh_flow_service_types`;
CREATE TABLE `eh_flow_service_types` (
  `id` BIGINT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `service_name` VARCHAR(64),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 描述字段
ALTER TABLE `eh_flows` ADD COLUMN `description` VARCHAR(128) COMMENT 'flow description';
ALTER TABLE `eh_flows` ADD COLUMN `allow_flow_case_end_evaluate` TINYINT NOT NULL DEFAULT 0 COMMENT 'allow_flow_case_end_evaluate';

-- 节点类型： 开始，结束，普通节点，前置条件，后置条件
ALTER TABLE `eh_flow_nodes` ADD COLUMN `node_type` VARCHAR(32) NOT NULL DEFAULT 'normal' COMMENT 'start, end, normal, condition_front, condition_back';
ALTER TABLE `eh_flow_nodes` ADD COLUMN `goto_process_button_name` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'start, end, normal, condition_front, condition_back';
ALTER TABLE `eh_flow_nodes` ADD COLUMN `flow_lane_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_lanes';
ALTER TABLE `eh_flow_nodes` ADD COLUMN `flow_lane_level` INTEGER NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_lanes';
-- 节点会签开关
ALTER TABLE `eh_flow_nodes` ADD COLUMN `need_all_processor_complete` TINYINT NOT NULL DEFAULT 0 COMMENT '节点会签开关';
--
ALTER TABLE `eh_flow_cases` ADD COLUMN `current_lane_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_lanes';
ALTER TABLE `eh_flow_cases` ADD COLUMN `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'parentId, ref eh_flow_cases';
ALTER TABLE `eh_flow_cases` ADD COLUMN `start_node_id` BIGINT NOT NULL DEFAULT 0 COMMENT '开始节点id';
ALTER TABLE `eh_flow_cases` ADD COLUMN `end_node_id` BIGINT NOT NULL DEFAULT 0 COMMENT '结束节点id';
ALTER TABLE `eh_flow_cases` ADD COLUMN `start_link_id` BIGINT NOT NULL DEFAULT 0 COMMENT '开始linkId';
ALTER TABLE `eh_flow_cases` ADD COLUMN `end_link_id` BIGINT NOT NULL DEFAULT 0 COMMENT '结束linkId';
ALTER TABLE `eh_flow_cases` ADD COLUMN `evaluate_status` TINYINT NOT NULL DEFAULT 0 COMMENT '评价状态，一般指结束后还可以评价的情况';
ALTER TABLE `eh_flow_cases` ADD COLUMN `service_type` VARCHAR(64) COMMENT 'service type';

ALTER TABLE `eh_flow_buttons` ADD COLUMN `param` VARCHAR(64) COMMENT 'the params from other module';
ALTER TABLE `eh_flow_buttons` ADD COLUMN `default_order` INTEGER NOT NULL DEFAULT 0 COMMENT 'default order';
ALTER TABLE `eh_flow_buttons` ADD COLUMN `evaluate_step` VARCHAR(64) COMMENT 'default order';

ALTER TABLE `eh_flow_cases` MODIFY COLUMN `last_step_time` DATETIME(3) COMMENT 'state change time';

-- flowCase增加附加字段，给业务使用
ALTER TABLE `eh_flow_cases` ADD COLUMN `string_tag6` VARCHAR(128);
ALTER TABLE `eh_flow_cases` ADD COLUMN `string_tag7` VARCHAR(128);
ALTER TABLE `eh_flow_cases` ADD COLUMN `string_tag8` VARCHAR(128);
ALTER TABLE `eh_flow_cases` ADD COLUMN `string_tag9` VARCHAR(128);
ALTER TABLE `eh_flow_cases` ADD COLUMN `string_tag10` VARCHAR(128);
ALTER TABLE `eh_flow_cases` ADD COLUMN `string_tag11` VARCHAR(128);
ALTER TABLE `eh_flow_cases` ADD COLUMN `string_tag12` VARCHAR(128);
ALTER TABLE `eh_flow_cases` ADD COLUMN `string_tag13` VARCHAR(128);
ALTER TABLE `eh_flow_cases` ADD COLUMN `integral_tag6` BIGINT;
ALTER TABLE `eh_flow_cases` ADD COLUMN `integral_tag7` BIGINT;
ALTER TABLE `eh_flow_cases` ADD COLUMN `integral_tag8` BIGINT;
ALTER TABLE `eh_flow_cases` ADD COLUMN `integral_tag9` BIGINT;
ALTER TABLE `eh_flow_cases` ADD COLUMN `integral_tag10` BIGINT;
ALTER TABLE `eh_flow_cases` ADD COLUMN `integral_tag11` BIGINT;
ALTER TABLE `eh_flow_cases` ADD COLUMN `integral_tag12` BIGINT;
ALTER TABLE `eh_flow_cases` ADD COLUMN `integral_tag13` BIGINT;

-- added by R for form
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
-- ALTER TABLE `eh_general_approvals` ADD COLUMN `approval_type` VARCHAR(64) DEFAULT 'CUSTOMIZE' COMMENT 'the type of the approval';

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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;