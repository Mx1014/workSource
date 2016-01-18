#
# member of global parition
# the relationship between owner and role, owner can be organization, enterprise, etc
#
DROP TABLE IF EXISTS `eh_organization_role_map`;
CREATE TABLE `eh_organization_role_map`(
    `id` BIGINT NOT NULL,
    `owner_type` VARCHAR(64),
    `owner_id` BIGINT NOT NULL DEFAULT 0,
    `role_id` BIGINT NOT NULL DEFAULT 0,
    `private_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: public, 1: private',
    `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 2: active',
    `create_time` DATETIME,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


#
# member of global parition
# the function menu structure information
#
DROP TABLE IF EXISTS `eh_menus`;
CREATE TABLE `eh_menus`(
    `id` BIGINT NOT NULL,
    `name` VARCHAR(64),
    `parent_id` BIGINT NOT NULL,
    `class_name` BIGINT NOT NULL DEFAULT 0,
    `data_type` BIGINT NOT NULL DEFAULT 0,
    `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 2: active',
    `path` VARCHAR(64),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


#
# member of global parition
# the relationship between owner and menu, owner can be role,user
#
DROP TABLE IF EXISTS `eh_role_menu_map`;
CREATE TABLE `eh_role_menu_map`(
    `id` BIGINT NOT NULL,
    `owner_type` VARCHAR(64),
    `owner_id` BIGINT NOT NULL DEFAULT 0,
    `menu_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;