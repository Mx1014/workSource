-- In some module, there are one or more tables which have lots of fields, it need to be grouped to be more readable in the view,
-- this table will store these groups. The groups may be hierarchical, mainly for subgroups.
-- DROP TABLE IF EXISTS `eh_var_field_groups`;
CREATE TABLE `eh_var_field_groups` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `module_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the module who has many fields need to be grouped',
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of the parent group, it is 0 when there is no parent',
  `path` VARCHAR(128) COMMENT 'path from the root',
  `title` VARCHAR(128) COMMENT 'the title of the group',
  `name` VARCHAR(128) COMMENT 'user name',
  `mandatory_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'the field is mandatory to input something or not, 0-not mandatory, 1-mandatory',
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
 
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
 
 
-- The definition of available fields in db, it is global instead of related to the namespace.
-- When the db table of module, the fields are determinated, all of them need to be copied to 
-- this table, and help to display in the view.
-- DROP TABLE IF EXISTS `eh_var_fields`;
CREATE TABLE `eh_var_fields` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `module_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the module which the field belong to',
  `name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'The field logic name, it map to the field in db',
  `display_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'The field display name',
  `field_type` VARCHAR(128) COMMENT '',
  `group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to eh_var_field_groups',
  `group_path` VARCHAR(128) COMMENT 'path from the root',
  `mandatory_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'the field is mandatory to input something or not, 0-not mandatory, 1-mandatory',
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
 
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
 
 
-- For some fields, there is a list of items attaching to it, such as <select> tag.
-- This table stores these items, and fetch by genneral api.
-- DROP TABLE IF EXISTS `eh_var_field_items`;
CREATE TABLE `eh_var_field_items` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `module_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the module which the field belong to',
  `field_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'The field which the item belong to',
  `display_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'The item display name',
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
 
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
 
 
-- The really used groups in namespace
-- DROP TABLE IF EXISTS `eh_var_field_group_scopes`;
CREATE TABLE `eh_var_field_group_scopes` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `module_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the module which the field belong to',
  `group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to eh_var_field_groups',
  `group_display_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the group name, it will use the name in eh_var_field_groups if not defined',
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
 
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
 
 
-- The really used fields in namespace
-- DROP TABLE IF EXISTS `eh_var_field_scopes`;
CREATE TABLE `eh_var_field_scopes` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `module_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the module which the field belong to',
  `group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to eh_var_field_groups',
  `group_path` VARCHAR(128) COMMENT 'path from the root',
  `field_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to eh_var_fields',
  `field_param` VARCHAR(128) COMMENT '',
  `field_display_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the field name, it will use the name in eh_var_fields if not defined',
  `mandatory_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'the field is mandatory to input something or not, 0-not mandatory, 1-mandatory',
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
 
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
 
 
-- The really used field items in namespace
-- DROP TABLE IF EXISTS `eh_var_field_item_scopes`;
CREATE TABLE `eh_var_field_item_scopes` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `module_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the module which the field belong to',
  `field_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to eh_var_fields',
  `item_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to eh_var_field_items',
  `item_display_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the field name, it will use the name in eh_var_field_items if not defined',
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
 
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;