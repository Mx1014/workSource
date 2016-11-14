-- eh_flows
-- DROP TABLE IF EXISTS `eh_flows`;
CREATE TABLE `eh_flows` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `version_id` INTEGER NOT NULL COMMENT 'current flow version',
  `flow_name` VARCHAR(64) NOT NULL COMMENT 'the name of flow',

  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `module_id` INTEGER NOT NULL COMMENT 'the module id',
  `module_type` VARCHAR(64) NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,

  `status` TINYINT NOT NULL COMMENT 'invalid, config, running, pending, stop',
  `stop_time` DATETIME NOT NULL COMMENT 'last stop time',
  `run_time` DATETIME NOT NULL COMMENT 'last create time',
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
  `flow_id` BIGINT NOT NULL,
  `flow_version_id` INTEGER NOT NULL,

  `state_level` INTEGER NOT NULL,
  `running_count` INTEGER NOT NULL,
  `enter_count` INTEGER NOT NULL,
  `leave_count` INTEGER NOT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_nodes`;
CREATE TABLE `eh_flow_nodes` (
    `id` BIGINT NOT NULL,
    `flow_id` BIGINT NOT NULL,
    `flow_version_id` INTEGER NOT NULL,

    `namespace_id` INTEGER NOT NULL DEFAULT 0,
    `module_id` INTEGER NOT NULL COMMENT 'the module id',
    `module_type` VARCHAR(64) NOT NULL,
    `owner_id` BIGINT NOT NULL,
    `owner_type` VARCHAR(64) NOT NULL,

    `priority` INTEGER NOT NULL,
    `dealing_id` BIGINT NOT NULL,
    `dealing_button_id` BIGINT NOT NULL,
    `apply_button_id` BIGINT NOT NULL,
    `enter_action_id` BIGINT NOT NULL DEFAULT 0,
    `run_action_id` BIGINT NOT NULL DEFAULT 0,
    `leave_action_id` BIGINT NOT NULL DEFAULT 0,
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'invalid, valid',
    `create_time` DATETIME NOT NULL COMMENT 'record create time',

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_buttons`;
CREATE TABLE `eh_flow_buttons` (
    `id` BIGINT NOT NULL,
    `flow_node_id` BIGINT NOT NULL,
    `flow_id` BIGINT NOT NULL,
    `flow_version_id` INTEGER NOT NULL,
    `button_name` VARCHAR(64),
    `flow_button_type` VARCHAR(64),
    `goto_level` INTEGER NOT NULL DEFAULT 0,
    `flow_action_id` BIGINT NOT NULL COMMENT 'actions attach to this buttons',
    `status` TINYINT NOT NULL COMMENT 'invalid, valid',

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_actions`;
CREATE TABLE `eh_flow_actions` (
    `id` BIGINT NOT NULL,
    `flow_id` BIGINT NOT NULL,
    `flow_version_id` INTEGER NOT NULL,
    `action_type` VARCHAR(64) NOT NULL COMMENT 'sms, message, tick, scripts',
    `belong_to` BIGINT NOT NULL,
    `belong_type` VARCHAR(64) NOT NULL COMMENT 'flow_node_change, flow_button_click',
    `action_target_id` BIGINT NOT NULL DEFAULT 0,
    `from_node_id` BIGINT NOT NULL DEFAULT 0,
    `target_node_id` BIGINT NOT NULL DEFAULT 0,

    `namespace_id` INTEGER NOT NULL DEFAULT 0,
    `module_id` INTEGER NOT NULL COMMENT 'the module id',
    `module_type` VARCHAR(64) NOT NULL,
    `status` TINYINT NOT NULL COMMENT 'invalid, valid',

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

-- DROP TABLE IF EXISTS `eh_user_selections`;
CREATE TABLE `eh_user_selections` (
    `id` BIGINT NOT NULL,
    `parent_id` BIGINT NOT NULL,
    `path` VARCHAR(64),
    `name` VARCHAR(64),
    `source_id` BIGINT NOT NULL DEFAULT 0,
    `source_type` VARCHAR(64) COMMENT 'community, organization, user, variable',
    `variable_type` VARCHAR(64),
    `flow_id` BIGINT NOT NULL,
    `flow_version_id` INTEGER NOT NULL,
    `status` TINYINT NOT NULL COMMENT 'invalid, valid',

    `namespace_id` INTEGER NOT NULL DEFAULT 0,
    `module_id` INTEGER NOT NULL COMMENT 'the module id',
    `module_type` VARCHAR(64) NOT NULL,
    `owner_id` BIGINT NOT NULL,
    `owner_type` VARCHAR(64) NOT NULL,

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_cases`;
CREATE TABLE `eh_flow_cases` (
    `id` BIGINT NOT NULL,
    `apply_user_id` BIGINT NOT NULL,
    `dealing_user_id` BIGINT NOT NULL,
    `dealing_selection_id` BIGINT NOT NULL,
    `refer_id` BIGINT NOT NULL DEFAULT 0,
    `refer_type` VARCHAR(64) NOT NULL,
    `current_node_id` BIGINT NOT NULL DEFAULT 0,
    `state` TINYINT NOT NULL DEFAULT 0,
    `reject_id` INTEGER NOT NULL DEFAULT 0,
    `change_time` DATETIME NOT NULL COMMENT 'state change time',
    `create_time` DATETIME NOT NULL COMMENT 'record create time',
    `content` TEXT,

    `flow_id` BIGINT NOT NULL,
    `flow_version_id` INTEGER NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
    `module_id` INTEGER NOT NULL COMMENT 'the module id',
    `module_type` VARCHAR(64) NOT NULL,
    `owner_id` BIGINT NOT NULL,
    `owner_type` VARCHAR(64) NOT NULL,

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
    `parent_id` BIGINT NOT NULL,
    `flow_node_id` BIGINT NOT NULL DEFAULT 0,
    `flow_case_id` BIGINT NOT NULL DEFAULT 0,
    `flow_button_id` BIGINT NOT NULL DEFAULT 0,
    `flow_action_id` BIGINT NOT NULL DEFAULT 0,
    `flow_user_id` BIGINT NOT NULL DEFAULT 0,
    `flow_selection_id` BIGINT NOT NULL DEFAULT 0,
    `subject_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the post id for this event',
    `log_type` VARCHAR(64) NOT NULL,
    `log_name` VARCHAR(64) COMMENT 'the title of this log',
    `content` TEXT,

    `flow_id` BIGINT NOT NULL,
    `flow_version_id` INTEGER NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
    `module_id` INTEGER NOT NULL COMMENT 'the module id',
    `module_type` VARCHAR(64) NOT NULL,
    `owner_id` BIGINT NOT NULL,
    `owner_type` VARCHAR(64) NOT NULL,

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
    `name` VARCHAR(64),

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_flow_evaluates`;
CREATE TABLE `eh_flow_evaluates` (
    `id` BIGINT NOT NULL,
    `star` TINYINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `flow_case_id` BIGINT NOT NULL,

    `flow_id` BIGINT NOT NULL,
    `flow_version_id` INTEGER NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
    `module_id` INTEGER NOT NULL COMMENT 'the module id',
    `module_type` VARCHAR(64) NOT NULL,
    `owner_id` BIGINT NOT NULL,
    `owner_type` VARCHAR(64) NOT NULL,

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

