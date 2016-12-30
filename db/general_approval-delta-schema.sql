-- general forms support
DROP TABLE IF EXISTS `eh_general_forms`;
CREATE TABLE `eh_general_forms` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `module_type` VARCHAR(64) NOT NULL,

  `form_name` VARCHAR(64) NOT NULL,
  `form_origin_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'The id of the original form',
  `form_version` BIGINT NOT NULL DEFAULT 0 COMMENT 'the current using version',
  `template_type` VARCHAR(128) NOT NULL COMMENT 'the type of template text',
  `template_text` TEXT,

  `status` TINYINT NOT NULL COMMENT 'invalid, config, running',
  `update_time` DATETIME NOT NULL COMMENT 'last update time',
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

CREATE TABLE `eh_general_approvals` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `module_type` VARCHAR(64) NOT NULL,
  `project_id` BIGINT NOT NULL DEFAULT 0,
  `project_type` VARCHAR(64),

  `form_origin_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'The id of the original form',
  `form_version` BIGINT NOT NULL DEFAULT 0 COMMENT 'the current using version',
  `support_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'APP:0, WEB:1, APP_WEB: 2',

  `status` TINYINT NOT NULL COMMENT 'invalid, config, running',
  `update_time` DATETIME NOT NULL COMMENT 'last update time',
  `create_time` DATETIME NOT NULL COMMENT 'record create time',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- the values of form from request
DROP TABLE IF EXISTS `eh_general_approval_vals`;
CREATE TABLE `eh_general_approval_vals` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `flow_case_id` BIGINT NOT NULL DEFAULT 0,
  `request_id` BIGINT NOT NULL,
  `approval_id` BIGINT NOT NULL,
  `form_origin_id` BIGINT NOT NULL,
  `form_version` BIGINT NOT NULL,
  `field_name` VARCHAR(128),
  `field_type` VARCHAR(128),
  `data_source_type` VARCHAR(128),
  `val_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: invalid, 1: request_root, 2: request_item',

  `field_str1` VARCHAR(128),
  `field_str2` VARCHAR(128),
  `field_str3` TEXT,
  `field_int1` BIGINT NOT NULL DEFAULT 0,
  `field_int2` BIGINT NOT NULL DEFAULT 0,
  `field_int3` BIGINT NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

