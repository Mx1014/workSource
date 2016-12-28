-- eh_flows
DROP TABLE IF EXISTS `eh_flows`;
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

  `need_evaluate` TINYINT NOT NULL DEFAULT 0 COMMENT '0: no evaluate, 1: need evaluate',
  `evaluate_start` BIGINT NOT NULL DEFAULT 0,
  `evaluate_end` BIGINT NOT NULL DEFAULT 0,
  `evaluate_step` VARCHAR(64) COMMENT 'NoStep, ApproveStep',

  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `integral_tag1` BIGINT NOT NULL DEFAULT 0,
  `integral_tag2` BIGINT NOT NULL DEFAULT 0,
  `integral_tag3` BIGINT NOT NULL DEFAULT 0,
  `integral_tag4` BIGINT NOT NULL DEFAULT 0,
  `integral_tag5` BIGINT NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_stats`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_nodes`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_buttons`;
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
    `integral_tag1` BIGINT NOT NULL DEFAULT 0,
    `integral_tag2` BIGINT NOT NULL DEFAULT 0,
    `integral_tag3` BIGINT NOT NULL DEFAULT 0,
    `integral_tag4` BIGINT NOT NULL DEFAULT 0,
    `integral_tag5` BIGINT NOT NULL DEFAULT 0,

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_forms`;
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
    `integral_tag1` BIGINT NOT NULL DEFAULT 0,
    `integral_tag2` BIGINT NOT NULL DEFAULT 0,
    `integral_tag3` BIGINT NOT NULL DEFAULT 0,
    `integral_tag4` BIGINT NOT NULL DEFAULT 0,
    `integral_tag5` BIGINT NOT NULL DEFAULT 0,

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_actions`;
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
    `integral_tag1` BIGINT NOT NULL DEFAULT 0,
    `integral_tag2` BIGINT NOT NULL DEFAULT 0,
    `integral_tag3` BIGINT NOT NULL DEFAULT 0,
    `integral_tag4` BIGINT NOT NULL DEFAULT 0,
    `integral_tag5` BIGINT NOT NULL DEFAULT 0,

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_user_selections`;
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
    `params` VARCHAR(64),
    `status` TINYINT NOT NULL COMMENT 'invalid, valid',
    `create_time` DATETIME NOT NULL COMMENT 'record create time',

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_cases`;
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
    `integral_tag1` BIGINT NOT NULL DEFAULT 0,
    `integral_tag2` BIGINT NOT NULL DEFAULT 0,
    `integral_tag3` BIGINT NOT NULL DEFAULT 0,
    `integral_tag4` BIGINT NOT NULL DEFAULT 0,
    `integral_tag5` BIGINT NOT NULL DEFAULT 0,

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_event_logs`;
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
    `integral_tag1` BIGINT NOT NULL DEFAULT 0,
    `integral_tag2` BIGINT NOT NULL DEFAULT 0,
    `integral_tag3` BIGINT NOT NULL DEFAULT 0,
    `integral_tag4` BIGINT NOT NULL DEFAULT 0,
    `integral_tag5` BIGINT NOT NULL DEFAULT 0,

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_variables`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_evaluates`;
CREATE TABLE `eh_flow_evaluates` (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,

    `owner_id` BIGINT NOT NULL,
    `owner_type` VARCHAR(64) NOT NULL,
    `project_id` BIGINT NOT NULL,
    `project_type` VARCHAR(64),
    `module_id` BIGINT NOT NULL COMMENT 'the module id',
    `module_type` VARCHAR(64) NOT NULL,

    `star` TINYINT NOT NULL,
    `evaluate_item_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `flow_node_id` BIGINT NOT NULL DEFAULT 0,
    `flow_case_id` BIGINT NOT NULL,
    `flow_main_id` BIGINT NOT NULL,
    `flow_version` INTEGER NOT NULL,
    `create_time` DATETIME NOT NULL COMMENT 'record create time',

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_evaluate_items`;
CREATE TABLE `eh_flow_evaluate_items` (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,

    `flow_main_id` BIGINT NOT NULL,
    `flow_version` INTEGER NOT NULL,
    `name` VARCHAR(128) NOT NULL,

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- scripts for this module
DROP TABLE IF EXISTS `eh_flow_scripts`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_subjects`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_attachments`;
CREATE TABLE `eh_flow_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_id` BIGINT NOT NULL COMMENT 'owner id, e.g comment_id',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_timeouts`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


