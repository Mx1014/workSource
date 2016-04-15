
ALTER TABLE `eh_organization_tasks` ADD COLUMN `task_category` VARCHAR(128) COMMENT '1:PUBLIC_AREA 2:PRIVATE_OWNER';

ALTER TABLE `eh_forum_posts` ADD COLUMN `start_time` DATETIME COMMENT 'publish start time';
ALTER TABLE `eh_forum_posts` ADD COLUMN `end_time` DATETIME COMMENT 'publish end time';

ALTER TABLE `eh_communities` ADD COLUMN `area_size` DOUBLE COMMENT 'area size';

ALTER TABLE `eh_organization_role_map` ADD COLUMN `role_name` VARCHAR(128) COMMENT 'role name';

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




ALTER TABLE `eh_organization_tasks` ADD COLUMN `visible_region_type` TINYINT DEFAULT NULL COMMENT 'define the visible region type';
ALTER TABLE `eh_organization_tasks` ADD COLUMN `visible_region_id` BIGINT DEFAULT NULL COMMENT 'visible region id';