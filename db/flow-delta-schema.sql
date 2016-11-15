-- eh_flows
-- DROP TABLE IF EXISTS `eh_flows`;
CREATE TABLE `eh_flows` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `module_id` INTEGER NOT NULL COMMENT 'the module id',
  `module_type` VARCHAR(64) NOT NULL,

  `flow_main_id` NOT NULL DEFAULT 0 COMMENT 'the real flow id for all copy, the first flow_main_id=0',
  `version` INTEGER NOT NULL COMMENT 'current flow version',
  `flow_name` VARCHAR(64) NOT NULL COMMENT 'the name of flow',

  `status` TINYINT NOT NULL COMMENT 'invalid, config, running, pending, stop',
  `stop_time` DATETIME NOT NULL COMMENT 'last stop time',
  `run_time` DATETIME NOT NULL COMMENT 'last run time',
  `create_time` DATETIME NOT NULL COMMENT 'record create time',

  `start_node` BIGINT NOT NULL DEFAULT 0,
  `end_node` BIGINT NOT NULL DEFAULT 0,
  `last_node` BIGINT NOT NULL DEFAULT 0,
  `superviser_id` BIGINT NOT NULL DEFAULT 0,

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_nodes`;
CREATE TABLE `eh_flow_nodes` (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,

    `flow_main_id` BIGINT NOT NULL,
    `flow_version` INTEGER NOT NULL,
    `node_name` VARCHAR(64) NOT NULL,
    `description` VARCHAR(1024) NOT NULL,
    `node_level` INTEGER NOT NULL,
    `auto_step_hour` INTEGER NOT NULL DEFAULT 0 COMMENT 'after hour, step next',
    `create_time` DATETIME NOT NULL COMMENT 'record create time',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'invalid, valid',

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_buttons`;
-- TODO form values
CREATE TABLE `eh_flow_buttons` (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,

    `flow_main_id` BIGINT NOT NULL,
    `flow_version` INTEGER NOT NULL,
    `flow_node_id` BIGINT NOT NULL,
    `button_name` VARCHAR(64),
    `flow_step_type` VARCHAR(64) COMMENT 'ApproveStep, RejectStep, TransferStep, CommentStep, EndStep, EndNotifyStep',
    `goto_level` INTEGER NOT NULL DEFAULT 0,
    `create_time` DATETIME NOT NULL COMMENT 'record create time',
    `status` TINYINT NOT NULL COMMENT 'invalid, valid',

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_actions`;
CREATE TABLE `eh_flow_actions` (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,

    `flow_main_id` BIGINT NOT NULL,
    `flow_version` INTEGER NOT NULL,
    `action_type` VARCHAR(64) NOT NULL COMMENT 'sms, message, tick, scripts',
    `belong_to` BIGINT NOT NULL,
    `belong_type` VARCHAR(64) NOT NULL COMMENT 'flow_node, flow_button, flow',
    `step_type` VARCHAR(64) NOT NULL COMMENT 'step_none, step_timeout, step_enter, step_leave',
    `status` TINYINT NOT NULL COMMENT 'invalid, valid',
    `create_time` DATETIME NOT NULL COMMENT 'record create time',
    `render_text` VARCHAR(256),

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_user_selections`;
CREATE TABLE `eh_flow_user_selections` (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,

    `flow_main_id` BIGINT NOT NULL,
    `flow_version` INTEGER NOT NULL,

    `select_type` VARCHAR(64) NOT NULL COMMENT 'group_selection, position_selection, manager_selection, variable_selection',
    `source_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to other user object id',
    `source_type` VARCHAR(64) COMMENT 'community, organization, user, variable',
    `belong_to` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to other flow object id',
    `belong_type` VARCHAR(64) NOT NULL COMMENT 'flow_superviser, flow_node_processor, flow_node_applier, flow_button_clicker, flow_action_processor',
    `status` TINYINT NOT NULL COMMENT 'invalid, valid',
    `create_time` DATETIME NOT NULL COMMENT 'record create time',

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_cases`;
CREATE TABLE `eh_flow_cases` (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,

    `owner_id` BIGINT NOT NULL,
    `owner_type` VARCHAR(64) NOT NULL,
    `module_id` INTEGER NOT NULL COMMENT 'the module id',
    `module_type` VARCHAR(64) NOT NULL,

    `flow_main_id` BIGINT NOT NULL,
    `flow_version` INTEGER NOT NULL,

    `apply_user_id` BIGINT NOT NULL,
    `process_user_id` BIGINT NOT NULL,
    `refer_id` BIGINT NOT NULL DEFAULT 0,
    `refer_type` VARCHAR(64) NOT NULL,
    `current_node_id` BIGINT NOT NULL DEFAULT 0,
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'invalid, initial, process, end',
    `reject_count` INTEGER NOT NULL DEFAULT 0,
    `reject_node_id` BIGINT NOT NULL DEFAULT 0,
    `last_step_time` DATETIME NOT NULL COMMENT 'state change time',
    `create_time` DATETIME NOT NULL COMMENT 'record create time',
    `content` TEXT,

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_event_logs`;
CREATE TABLE `eh_flow_event_logs` (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,

    `parent_id` BIGINT NOT NULL,
    `flow_main_id` BIGINT NOT NULL,
    `flow_version` INTEGER NOT NULL,
    `flow_node_id` BIGINT NOT NULL DEFAULT 0,
    `flow_case_id` BIGINT NOT NULL DEFAULT 0,
    `flow_button_id` BIGINT NOT NULL DEFAULT 0,
    `flow_action_id` BIGINT NOT NULL DEFAULT 0,
    `flow_user_id` BIGINT NOT NULL DEFAULT 0,
    `flow_selection_id` BIGINT NOT NULL DEFAULT 0,
    `subject_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the post id for this event',
    `log_type` VARCHAR(64) NOT NULL COMMENT 'flow_step, button_click, action_result',
    `log_title` VARCHAR(64) COMMENT 'the title of this log',
    `log_content` TEXT,

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_variables`;
CREATE TABLE `eh_flow_variables` (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
    `name` VARCHAR(64),

    `owner_id` BIGINT NOT NULL,
    `owner_type` VARCHAR(64) NOT NULL,
    `module_id` INTEGER NOT NULL COMMENT 'the module id',
    `module_type` VARCHAR(64) NOT NULL,

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_evaluates`;
CREATE TABLE `eh_flow_evaluates` (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,

    `owner_id` BIGINT NOT NULL,
    `owner_type` VARCHAR(64) NOT NULL,
    `module_id` INTEGER NOT NULL COMMENT 'the module id',
    `module_type` VARCHAR(64) NOT NULL,

    `star` TINYINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `flow_case_id` BIGINT NOT NULL,
    `flow_main_id` BIGINT NOT NULL,
    `flow_version` INTEGER NOT NULL,

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

