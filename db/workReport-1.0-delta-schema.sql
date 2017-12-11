-- 工作汇报1.0 add by nan.rong
-- 工作汇报模板表
CREATE TABLE `eh_work_report_templates` (
  `id` BIGINT NOT NULL COMMENT 'id of the report template',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `module_id` BIGINT COMMENT 'the module id',
  `module_type` VARCHAR(64) COMMENT 'the module type',
  `report_name` VARCHAR(128) NOT NULL,
  `report_type` TINYINT COMMENT '0-Day, 1-Week, 2-Month',
  `report_attribute` VARCHAR(128) NOT NULL DEFAULT 'CUSTOMIZE' COMMENT 'DEFAULT,CUSTOMIZE',
  `form_template_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'The id of the template form',
  `modify_flag` TINYINT NOT NULL  DEFAULT 1 COMMENT '0: no, 1: yes',
  `delete_flag` TINYINT NOT NULL DEFAULT 1 COMMENT '0: no, 1: yes',
  `icon_uri` VARCHAR(1024) COMMENT 'the avatar of the approval',

  `update_time` DATETIME COMMENT 'last update time',
  `create_time` DATETIME COMMENT 'record create time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 工作汇报表
CREATE TABLE `eh_work_reports` (
  `id` BIGINT NOT NULL COMMENT 'id of the report',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `module_id` BIGINT COMMENT 'the module id',
  `module_type` VARCHAR(64) COMMENT 'the module type',
  `report_name` VARCHAR(128) NOT NULL,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-invalid, 1-valid, 2-running',
  `report_type` TINYINT COMMENT '0-Day, 1-Week, 2-Month',
  `report_attribute` NOT NULL VARCHAR(128) DEFAULT 'CUSTOMIZE' COMMENT 'DEFAULT,CUSTOMIZE',
  `form_origin_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'The id of the original form',
  `form_version` BIGINT NOT NULL DEFAULT 0 COMMENT 'the current using version',
  `report_template_id` BIGINT DEFAULT 0 COMMENT 'the id in eh_general_approval_templates',
  `modify_flag` TINYINT NOT NULL DEFAULT 1 COMMENT '0: no, 1: yes',
  `delete_flag` TINYINT NOT NULL DEFAULT 1 COMMENT '0: no, 1: yes',
  `icon_uri` VARCHAR(1024) COMMENT 'the avatar of the approval',

  `operator_user_id` BIGINT DEFAULT 0 COMMENT 'the user id of the operator',
  `operator_name` VARCHAR(128) COMMENT 'the name of the operator',
  `update_time` DATETIME COMMENT 'last update time',
  `create_time` DATETIME COMMENT 'record create time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 工作汇报可见范围映射表
CREATE TABLE `eh_work_report_scope_map` (
  `id` BIGINT NOT NULL COMMENT 'id of the report scope id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `report_id` BIGINT NOT NULL COMMENT 'id of the report',
  `source_type` VARCHAR(64) NOT NULL COMMENT 'ORGANIZATION, MEMBERDETAIL',
  `source_id` BIGINT NOT NULL COMMENT 'id of the scope',
  `source_description` VARCHAR(128) COMMENT 'the description of the scope class',

  `create_time` DATETIME COMMENT 'record create time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 工作汇报值表
CREATE TABLE `eh_work_report_vals` (
  `id` BIGINT NOT NULL COMMENT 'id of the report val',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `module_id` BIGINT COMMENT 'the module id',
  `module_type` VARCHAR(64) COMMENT 'the module type',
  `module_name` VARCHAR(64),
  `content` VARCHAR(128) COMMENT 'the content of the report',
  `report_id` BIGINT NOT NULL COMMENT 'the id of the report',
  `report_time` DATETIME COMMENT 'the target time of the report',
  `applier_name` VARCHAR(64) COMMENT 'the name of the applier',
  `applier_user_id` BIGINT COMMENT 'the userId of the applier',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-invalid, 1-valid',
  `report_type` TINYINT NOT NULL COMMENT '0-Day, 1-Week, 2-Month',

  `update_time` DATETIME COMMENT 'last update time',
  `create_time` DATETIME COMMENT 'record create time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 工作汇报值接收人映射表
CREATE TABLE `eh_work_report_val_receiver_map` (
  `id` BIGINT NOT NULL COMMENT 'the id of the report val map',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `report_val_id` NOT NULL BIGINT COMMENT 'the id of the report val',
  `receiver_user_id` NOT NULL BIGINT COMMENT 'the id of the receiver',
  `receiver_name` VARCHAR(256) COMMENT 'the name of the receiver',
  `receiver_avatar` VARCHAR(2048) COMMENT 'the avatar of the receiver',
  `read_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0-unread 1-read',

  `create_time` DATETIME COMMENT 'record create time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- end by nan.rong
