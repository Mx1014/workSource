#
# member of global parition
# shared among namespaces, no application module specific information
#
DROP TABLE IF EXISTS `eh_scene_types`;
CREATE TABLE `eh_scene_types`(
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
    `name` VARCHAR(64) NOT NULL COMMENT 'the identifier of the scene type',
	`display_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the name used to display',
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_ns_scene`(`namespace_id`, `name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER TABLE `eh_launch_pad_layouts` ADD COLUMN `scene_type` VARCHAR(64) NOT NULL DEFAULT 'default';
ALTER TABLE `eh_launch_pad_items` ADD COLUMN `scene_type` VARCHAR(64) NOT NULL DEFAULT 'default';
ALTER TABLE `eh_banners` ADD COLUMN `scene_type` VARCHAR(64) NOT NULL DEFAULT 'default';

#
# member of global parition
# shared among namespaces, no application module specific information
#
DROP TABLE IF EXISTS `eh_op_promotion_policies`;
CREATE TABLE `eh_op_promotion_policies`(
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
    `name` VARCHAR(512) NOT NULL DEFAULT '' COMMENT 'the identifer of the policy',
	`display_name` VARCHAR(512) DEFAULT '' COMMENT 'the name to display',
	`description` TEXT,
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global parition
# shared among namespaces, no application module specific information
#
DROP TABLE IF EXISTS `eh_op_promotion_settings`;
CREATE TABLE `eh_op_promotion_settings`(
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
	`scene_type` VARCHAR(64) NOT NULL DEFAULT 'default',
    `title` VARCHAR(512) NOT NULL DEFAULT '' COMMENT 'the title of the activity',
	`description` TEXT,
	`policy_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to the id of eh_op_promotion_policies',
	`policy_data` VARCHAR(1024) COMMENT 'json format, the parameters which help executing the policy',
	`start_time` DATETIME,
    `end_time` DATETIME,
	`scope_code` TINYINT NOT NULL DEFAULT 0 COMMENT '0: all, 1: community, 2: city, 3: user',
    `scope_id` BIGINT,
	`action_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'according to document',
    `action_data` TEXT COMMENT 'the parameters depend on item_type, json format',
	`valid_count` INTEGER NOT NULL DEFAULT 0 COMMENT '0: unlimit, 1: only once, others',
    `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global parition
# shared among namespaces, no application module specific information
# Give user some operation promotions planed by zl market department
#
DROP TABLE IF EXISTS `eh_op_promotions`;
CREATE TABLE `eh_op_promotions`(
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
	`scene_type` VARCHAR(64) NOT NULL DEFAULT 'default',
	`trigger_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to the id of eh_promotion_activity_triggers',
    `title` VARCHAR(512) NOT NULL DEFAULT 0 COMMENT 'the title of the activity',
	`description` TEXT,
	`start_time` DATETIME,
    `end_time` DATETIME,
	`scope_code` TINYINT NOT NULL DEFAULT 0 COMMENT '0: all, 1: community, 2: city, 3: user',
    `scope_id` BIGINT,
	`action_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'according to document',
    `action_data` TEXT COMMENT 'the parameters depend on item_type, json format',    
	`valid_count` INTEGER NOT NULL DEFAULT 0 COMMENT '0: unlimit, 1: only once, others',
	`status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

