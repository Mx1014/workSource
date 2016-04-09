# merge the following sql files
# 3.3.x-delta-schema-001-xiongying.sql
# 3.3.x-delta-schema-006-liangqishi.sql
# 3.3.x-delta-schema-007-yanshaofan.sql
# 3.3.x-delta-schema-008-yanshaofan.sql

ALTER TABLE `eh_conf_orders` ADD COLUMN `buyer_name` VARCHAR(128);
ALTER TABLE `eh_conf_orders` ADD COLUMN `buyer_contact` VARCHAR(128);
ALTER TABLE `eh_conf_orders` ADD COLUMN `vendor_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'vendor type 0: none, 1: Alipay, 2: Wechat';
ALTER TABLE `eh_conf_orders` ADD COLUMN `email` VARCHAR(128);

ALTER TABLE `eh_conf_account_categories` CHANGE `channel_type` `multiple_account_threshold` INTEGER NOT NULL DEFAULT 0 COMMENT 'the limit value of mutiple buy channel';
ALTER TABLE `eh_conf_account_categories` ADD COLUMN `display_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'display when online or offline, 0: all, 1: online, 2: offline';
ALTER TABLE `eh_conf_account_categories` CHANGE `amount` `single_account_price` DECIMAL(10,2);
ALTER TABLE `eh_conf_account_categories` ADD COLUMN `multiple_account_price` DECIMAL(10,2);

ALTER TABLE `eh_organization_tasks` ADD COLUMN `task_category` VARCHAR(128) COMMENT '1:PUBLIC_AREA 2:PRIVATE_OWNER';
ALTER TABLE `eh_organization_tasks` ADD COLUMN `visible_region_type` TINYINT DEFAULT NULL COMMENT 'define the visible region type';
ALTER TABLE `eh_organization_tasks` ADD COLUMN `visible_region_id` BIGINT DEFAULT NULL COMMENT 'visible region id';

ALTER TABLE `eh_forum_posts` ADD COLUMN `start_time` DATETIME COMMENT 'publish start time';
ALTER TABLE `eh_forum_posts` ADD COLUMN `end_time` DATETIME COMMENT 'publish end time';

ALTER TABLE `eh_communities` ADD COLUMN `area_size` DOUBLE COMMENT 'area size';

ALTER TABLE `eh_organization_role_map` ADD COLUMN `role_name` VARCHAR(128) COMMENT 'role name';

ALTER TABLE `eh_launch_pad_layouts` ADD COLUMN `scene_type` VARCHAR(64) NOT NULL DEFAULT 'default';
ALTER TABLE `eh_launch_pad_items` ADD COLUMN `scene_type` VARCHAR(64) NOT NULL DEFAULT 'default';
ALTER TABLE `eh_banners` ADD COLUMN `scene_type` VARCHAR(64) NOT NULL DEFAULT 'default';

DROP TABLE IF EXISTS `eh_menus`;

# 
# web menu 
# 
DROP TABLE IF EXISTS `eh_web_menus`; 
CREATE TABLE `eh_web_menus`( 
`id` BIGINT NOT NULL, 
`name` VARCHAR(64),  
`parent_id` BIGINT NOT NULL DEFAULT 0, 
`icon_url` VARCHAR(64), 
`data_type` VARCHAR(64), 
`leaf_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'Whether leaf nodes, non leaf nodes can be folded 0: false, 1: true', 
`status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 2: active', 
`path` VARCHAR(64), 
`type` VARCHAR(64) NOT NULL DEFAULT 'zuolin' COMMENT 'zuolin, park', 
`sort_num` INT COMMENT 'sort number', 

PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; 

# 
# 菜单跟权限的关联表 
# 
DROP TABLE IF EXISTS `eh_web_menu_privileges`; 
CREATE TABLE `eh_web_menu_privileges`( 
`id` BIGINT NOT NULL, 
`privilege_id` BIGINT NOT NULL, 
`menu_id` BIGINT NOT NULL, 
`name` VARCHAR(64), 
`show_flag` TINYINT NOT NULL DEFAULT 1 COMMENT '0: Not related menu display, 1: Associated menu display', 
`status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: active', 
`discription` VARCHAR(128), 
`sort_num` INT COMMENT 'sort number',
PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_organization_task_targets`; 
CREATE TABLE `eh_organization_task_targets`( 
`id` BIGINT NOT NULL, 
`owner_type` VARCHAR(64) NOT NULL,
`owner_id` BIGINT DEFAULT NULL,
`target_type` VARCHAR(64) NOT NULL COMMENT 'target object(user/group) type',
`target_id` BIGINT DEFAULT NULL COMMENT 'target object(user/group) id',
`task_type` VARCHAR(64) NOT NULL,
PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; 

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



