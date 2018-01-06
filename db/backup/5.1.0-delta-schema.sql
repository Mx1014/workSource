-- 删除品质关联类型字段  start by  jiarui
ALTER TABLE `eh_quality_inspection_model_community_map`
  DROP COLUMN `category_id`;
--  删除品质关联类型字段  end  by  jiarui

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
  `modify_flag` TINYINT NOT NULL DEFAULT 1 COMMENT '0: no, 1: yes',
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
  `report_attribute` VARCHAR(128) NOT NULL DEFAULT 'CUSTOMIZE' COMMENT 'DEFAULT,CUSTOMIZE',
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

-- 工作汇报单表
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

-- 工作汇报单接收人映射表
CREATE TABLE `eh_work_report_val_receiver_map` (
  `id` BIGINT NOT NULL COMMENT 'the id of the report val map',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `report_val_id` BIGINT NOT NULL COMMENT 'the id of the report val',
  `receiver_user_id` BIGINT NOT NULL COMMENT 'the id of the receiver',
  `receiver_name` VARCHAR(256) COMMENT 'the name of the receiver',
  `receiver_avatar` VARCHAR(2048) COMMENT 'the avatar of the receiver',
  `read_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0-unread 1-read',

  `update_time` DATETIME COMMENT 'last update time',
  `create_time` DATETIME COMMENT 'record create time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 工作汇报单评论表
CREATE TABLE `eh_work_report_val_comments` (
  `id` BIGINT NOT NULL COMMENT 'the id of the report val map',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `owner_id` BIGINT,
  `owner_type` VARCHAR(64),
  `report_val_id` BIGINT NOT NULL COMMENT 'the id of the report val',
  `parent_comment_id` BIGINT COMMENT 'the parent id of the',
  `content_type` VARCHAR(32) COMMENT 'the type of the content',
  `content` TEXT COMMENT 'the comment',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: invalid, 1: valid',
  `creator_user_id` BIGINT NOT NULL COMMENT 'the user id of the creator',

  `create_time` DATETIME COMMENT 'record create time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 工作汇报单评论附件表
CREATE TABLE `eh_work_report_val_comment_attachments` (
  `id` BIGINT NOT NULL COMMENT 'the id of the report val map',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `comment_id` BIGINT NOT NULL COMMENT 'the id of the comment',
  `content_type` VARCHAR(32) COMMENT 'the type of the content',
  `content_uri` VARCHAR(2048) COMMENT 'attachment object link info on storage',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: invalid, 1: valid',
  `creator_user_id` BIGINT NOT NULL COMMENT 'the user id of the creator',

  `create_time` DATETIME COMMENT 'record create time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- end by nan.rong

-- 积分 1.2 add by xq.tian  2018/01/03
ALTER TABLE `eh_point_logs` ADD COLUMN `event_happen_time` BIGINT;
ALTER TABLE `eh_point_logs` ADD COLUMN `extra` TEXT;

-- 积分banner
-- DROP TABLE IF EXISTS `eh_point_banners`;
CREATE TABLE `eh_point_banners` (
  `id` BIGINT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `system_id` BIGINT,
  `name` VARCHAR(128),
  `poster_uri` VARCHAR(128),
  `action_type` TINYINT NOT NULL DEFAULT '0' COMMENT 'according to document',
  `action_data` TEXT COMMENT 'the parameters depend on item_type, json format',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: disabled, 2: enabled',
  `default_order` INTEGER NOT NULL DEFAULT 0,
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_point_goods` ADD COLUMN shop_number VARCHAR(128);
ALTER TABLE `eh_point_goods` ADD COLUMN system_id BIGINT NOT NULL DEFAULT 0;

ALTER TABLE `eh_point_goods` DROP COLUMN poster_uri;
ALTER TABLE `eh_point_goods` DROP COLUMN poster_url;
ALTER TABLE `eh_point_goods` DROP COLUMN detail_url;
ALTER TABLE `eh_point_goods` DROP COLUMN points;
ALTER TABLE `eh_point_goods` DROP COLUMN sold_amount;
ALTER TABLE `eh_point_goods` DROP COLUMN original_price;
ALTER TABLE `eh_point_goods` DROP COLUMN discount_price;
ALTER TABLE `eh_point_goods` DROP COLUMN point_rule;
ALTER TABLE `eh_point_goods` DROP COLUMN display_name;

ALTER TABLE `eh_point_rules` ADD COLUMN extra TEXT;
ALTER TABLE `eh_point_rules` MODIFY COLUMN `limit_type` TEXT;
ALTER TABLE `eh_point_rule_configs` MODIFY COLUMN `limit_type` TEXT;

ALTER TABLE `eh_point_tutorial_to_point_rule_mappings` MODIFY COLUMN description VARCHAR(128);