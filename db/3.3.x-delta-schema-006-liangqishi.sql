#
# member of global parition
# shared among namespaces, no application module specific information
#
DROP TABLE IF EXISTS `eh_scenes`;
CREATE TABLE `eh_scenes`(
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
    `name` VARCHAR(64) NOT NULL COMMENT 'the identifier of the scene',
	`display_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the name used to display',
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_ns_scene`(`namespace_id`, `name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER TABLE `eh_launch_pad_layouts` ADD COLUMN `scene` VARCHAR(64) NOT NULL DEFAULT 'default';
ALTER TABLE `eh_launch_pad_items` ADD COLUMN `scene` VARCHAR(64) NOT NULL DEFAULT 'default';
ALTER TABLE `eh_banners` ADD COLUMN `scene` VARCHAR(64) NOT NULL DEFAULT 'default';

#
# member of global parition
# shared among namespaces, no application module specific information
#
DROP TABLE IF EXISTS `eh_promotion_activity_triggers`;
CREATE TABLE `eh_promotion_activity_triggers`(
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
    `title` VARCHAR(512) NOT NULL DEFAULT 0 COMMENT 'the title of the activity',
	`trigger_type` TINYINT NOT NULL DEFAULT 1 COMMENT '0: none, 1: trigger at start time, 2: trigger by register, 3: trigger by biz cost',
	`trigger_data` VARCHAR(1024) COMMENT 'json format, the parameters which help trigger type to decine when to start the activity',
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
#
DROP TABLE IF EXISTS `eh_promotion_activities`;
CREATE TABLE `eh_promotion_activities`(
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
	`trigger_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to the id of eh_promotion_activity_triggers',
    `title` VARCHAR(512) NOT NULL DEFAULT 0 COMMENT 'the title of the activity',
	`scope_code` TINYINT NOT NULL DEFAULT 0 COMMENT '0: all, 1: community, 2: city, 3: user',
    `scope_id` BIGINT,
	`action_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'according to document',
    `action_data` TEXT COMMENT 'the parameters depend on item_type, json format',    
	`valid_count` INTEGER NOT NULL DEFAULT 0 COMMENT '0: unlimit, 1: only once, others',
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
