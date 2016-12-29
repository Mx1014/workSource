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
  `template_type` VARCHAR(256) NOT NULL COMMENT 'the type of template text',
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

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
