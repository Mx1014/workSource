-- merge from pmtask-delta-schema.sql
ALTER TABLE eh_pm_tasks ADD COLUMN `task_category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'task category id';
ALTER TABLE eh_pm_tasks ADD COLUMN `reserve_time` DATETIME;
ALTER TABLE eh_pm_tasks ADD COLUMN `priority` TINYINT NOT NULL DEFAULT 0 COMMENT 'task rank of request';
ALTER TABLE eh_pm_tasks ADD COLUMN `source_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'task come from ,such as app ,email';
ALTER TABLE eh_pm_tasks ADD COLUMN `organization_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE eh_pm_tasks ADD COLUMN `requestor_name` VARCHAR(64) COMMENT 'the name of requestor';
ALTER TABLE eh_pm_tasks ADD COLUMN `requestor_phone` VARCHAR(64) COMMENT 'the phone of requestor';
ALTER TABLE eh_pm_tasks ADD COLUMN `address_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'address id';
ALTER TABLE eh_pm_tasks ADD COLUMN `revisit_content` TEXT COMMENT 'revisit content';
ALTER TABLE eh_pm_tasks ADD COLUMN `revisit_time` DATETIME;
ALTER TABLE eh_pm_task_statistics ADD COLUMN `task_category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'task category id';

CREATE TABLE `eh_pm_task_targets` (
  `id` bigint(20) NOT NULL,
  `owner_type` varchar(64) NOT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `target_type` varchar(64) NOT NULL COMMENT 'target object(user/group) type',
  `target_id` bigint(20) DEFAULT NULL COMMENT 'target object(user/group) id',
  `role_id` BIGINT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 2: active',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- merge from customer-manage-1.1-delta-schema.sql 20161025 by lqs
--
-- 车辆停车类型     add by xq.tian 2016/10/11
--
CREATE TABLE `eh_parking_card_categories` (
  `id` BIGINT NOT NULL,
  `namespace_id` INT NOT NULL DEFAULT '0',
  `owner_type` VARCHAR(32) DEFAULT NULL,
  `owner_id` BIGINT DEFAULT NULL,
  `card_type` TINYINT NOT NULL COMMENT '1. temp, 2. month, 3. free ,etc.',
  `category_name` VARCHAR(64) NOT NULL COMMENT 'name of category',
  `status` TINYINT NOT NULL COMMENT '1: normal, 0: delete',
  `creator_uid` BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- item 类别 by sfyan 20161025
-- DROP TABLE IF EXISTS `eh_item_service_categries`;
CREATE TABLE `eh_item_service_categries` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `name` VARCHAR(64) NOT NULL COMMENT 'service categry name',
  `icon_uri` VARCHAR(1024) COMMENT 'service categry icon uri',
  `order` INTEGER COMMENT 'order ',
  `align` TINYINT DEFAULT '0' COMMENT '0: left, 1: center',
  `status` TINYINT NOT NULL DEFAULT '1' COMMENT '0: inactive, 1: active',
  `namespace_id` INTEGER,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_launch_pad_items` ADD COLUMN `service_categry_id` BIGINT COMMENT 'service categry id';




-- 以下为3.10.4合过来的脚本-------------

--
-- 服务联盟category添加显示类型字段   add by xq.tian  2016/10/18
--
-- ALTER TABLE `eh_service_alliance_categories` ADD COLUMN `display_mode` TINYINT DEFAULT 1;


-- 服务联盟添加新的预约表 add by xiongying20161027
-- CREATE TABLE `eh_service_alliance_reservation_requests` (
--   `id` bigint(20) NOT NULL,
--   `namespace_id` int(11) NOT NULL DEFAULT '0',
--   `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
--   `owner_id` bigint(20) NOT NULL DEFAULT '0',
--   `type` bigint(20) NOT NULL DEFAULT '0',
--   `category_id` bigint(20) NOT NULL DEFAULT '0',
--   `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'record creator user id',
--   `creator_name` varchar(128) DEFAULT NULL,
--   `creator_mobile` varchar(128) DEFAULT NULL,
--   `creator_organization_id` bigint(20) NOT NULL DEFAULT '0',
--   `service_alliance_id` bigint(20) NOT NULL DEFAULT '0',
--   `reserve_type` varchar(128) DEFAULT NULL,
--   `reserve_organization` varchar(128) DEFAULT NULL,
--   `reserve_time` varchar(128) DEFAULT NULL,
--   `contact` varchar(128) DEFAULT NULL,
--   `mobile` varchar(128) DEFAULT NULL,
--   `remarks` varchar(1024) DEFAULT NULL,
--   `create_time` datetime DEFAULT NULL,
-- 
--   PRIMARY KEY (`id`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- 
-- -- 添加人数限制字段, add by tt, 20161027
-- -- ALTER TABLE `eh_forum_posts` ADD COLUMN `max_quantity` INT COMMENT 'max person quantity';
-- -- ALTER TABLE `eh_activities` ADD COLUMN `max_quantity` INT COMMENT 'max person quantity';
-- -- ALTER TABLE `eh_activities` ADD COLUMN `content_type` varchar(128) COMMENT 'content type, text/rich_text';
-- -- ALTER TABLE `eh_activities` ADD COLUMN `version` varchar(128) COMMENT 'version';
-- 
-- -- 添加消息提醒设置表, add by tt, 20161027
-- -- DROP TABLE IF EXISTS `eh_warning_settings`;
-- -- CREATE TABLE `eh_warning_settings` (
-- --   `id` BIGINT NOT NULL,
-- --   `namespace_id` INTEGER NOT NULL COMMENT 'namespace id',
-- --   `type` varchar(64) COMMENT 'type',
-- --   `time` BIGINT COMMENT 'millisecond',
-- --   `create_time` DATETIME,
-- --   `creator_uid` BIGINT,
-- --   `update_time` DATETIME,
-- --   `operator_uid` BIGINT,
-- 
-- --   PRIMARY KEY (`id`)
-- -- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- 
-- -- 以上为3.10.4合过来的脚本-------------
-- 
-- -- global table. 添加门禁特殊权限相关用户类型 add by Janson 20161028
-- -- DROP TABLE IF EXISTS `eh_door_user_permission`;
CREATE TABLE `eh_door_user_permission` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `namespace_id` INT NOT NULL DEFAULT '0',
    `user_id` BIGINT NOT NULL,
    `approve_user_id` BIGINT NOT NULL,
    `auth_type` TINYINT NOT NULL COMMENT '0: Door Guard',
    `owner_type` TINYINT NOT NULL COMMENT '0:community, 1:enterprise, 2: family, 3: user',
    `owner_id` BIGINT NOT NULL DEFAULT 0,

    `integral_tag1` BIGINT DEFAULT 0 NOT NULL,
    `integral_tag2` BIGINT DEFAULT 0 NOT NULL,
    `integral_tag3` BIGINT DEFAULT 0 NOT NULL,
    `integral_tag4` BIGINT DEFAULT 0 NOT NULL,
    `string_tag1` VARCHAR(128),
    `string_tag2` VARCHAR(128),
    `string_tag3` VARCHAR(128),
    `string_tag4` VARCHAR(128),

    `description` VARCHAR(1024),

    `create_time` DATETIME,
    `status` TINYINT NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- 资源分类定义 by sfyan 20161025
-- DROP TABLE IF EXISTS `eh_resource_categories`;
CREATE TABLE `eh_resource_categories` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `name` VARCHAR(64) NOT NULL COMMENT 'resource categry name',
  `owner_type` VARCHAR(32) NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `parent_id` BIGINT NOT NUll,
  `path` VARCHAR(128) NOT NUll,
  `status` TINYINT NOT NULL DEFAULT '1' COMMENT '0: inactive, 2: active',
  `create_time` DATETIME DEFAULT NULL,
  `creator_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 资源分配类型 by sfyan 20161029
-- DROP TABLE IF EXISTS `eh_resource_category_assignments`;
CREATE TABLE `eh_resource_category_assignments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `resource_categry_id` BIGINT NOT NULL COMMENT 'service categry id',
  `resource_type` VARCHAR(32),
  `resource_id` BIGINT,
  `creator_uid`  BIGINT NOT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;



-- 机构通用岗位 by sfyan 20161029
-- DROP TABLE IF EXISTS `eh_organization_job_positions`;
CREATE TABLE `eh_organization_job_positions` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'organization',
  `owner_id` BIGINT NOT NULL COMMENT 'orgnaization member id',
  `name` VARCHAR(64) NOT NULL,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 2: active',
  `discription` VARCHAR(128),
  `creator_uid`  BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 增加组织架构类型 岗位 ， 职级， 经理组
ALTER TABLE `eh_organizations` MODIFY `group_type` VARCHAR(64) DEFAULT NULL COMMENT 'ENTERPRISE, DEPARTMENT, GROUP, JOB_POSITION, JOB_LEVEL, MANAGER';

-- 增加组织架构大小 目前只用于职级大小
ALTER TABLE `eh_organizations` ADD `size` INTEGER COMMENT 'job level size';

-- 机构岗位所属的通用岗位 by sfyan 20161029
-- DROP TABLE IF EXISTS `eh_organization_job_position_maps`;
CREATE TABLE `eh_organization_job_position_maps` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `job_position_id` BIGINT NOT NULL,
  `organization_id` BIGINT NOT NULL COMMENT 'orgnaization member id',
  `creator_uid`  BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 业务模块 by sfyan 20161029
-- DROP TABLE IF EXISTS `eh_service_modules`;
CREATE TABLE `eh_service_modules` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `name` BIGINT NOT NULL COMMENT 'organization',
  `parent_id` BIGINT NOT NUll,
  `path` VARCHAR(128) NOT NUll,
  `type` TINYINT NOT NULL DEFAULT '0' COMMENT '0: park, 1: organization, 3:manager',
  `level` INTEGER NOT NULL DEFAULT '0',
  `status` TINYINT NOT NULL DEFAULT '2' COMMENT '0: inactive, 2: active',
  `default_order` INTEGER NULL COMMENT 'order number',
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 业务模块范围配置 by sfyan 20161029
-- DROP TABLE IF EXISTS `eh_service_module_scopes`;
CREATE TABLE `eh_service_module_scopes` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `module_id` BIGINT DEFAULT NULL,
  `module_name` VARCHAR(64) DEFAULT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `owner_id` BIGINT DEFAULT NULL,
  `default_order` INTEGER NULL COMMENT 'order number',
  `apply_policy` TINYINT NOT NULL DEFAULT '0' COMMENT '0: delete , 1: override, 2: revert',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 业务模块 by sfyan 20161029
-- DROP TABLE IF EXISTS `eh_service_module_privileges`;
CREATE TABLE `eh_service_module_privileges` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `module_id` BIGINT NOT NULL COMMENT 'service module id',
  `privilege_type` TINYINT NOT NULL COMMENT '0: general, 1: super',
  `privilege_id` BIGINT NOT NULL COMMENT 'privilege id',
  `remark` VARCHAR(128) NULL COMMENT'remark',
  `default_order` INTEGER NULL COMMENT'order number',
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 业务模块分配 by sfyan 20161029
-- 超级管理员 定义一个超管权限
-- 公司管理员 定义一个公司管理员的权限 
-- 每个模块都定义自己独有的超管权限
-- DROP TABLE IF EXISTS `eh_service_module_assignments`;
CREATE TABLE `eh_service_module_assignments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `organization_id` BIGINT NOT NULL COMMENT 'organization id',
  `target_type` VARCHAR(32) NOT NULL COMMENT 'organization user',
  `target_id` BIGINT NOT NULL ,
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'community',
  `owner_id` BIGINT NOT NULL,
  `module_id` BIGINT NOT NULL,
  `create_uid` BIGINT NOT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;







