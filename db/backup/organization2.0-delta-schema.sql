

-- 资源分类定义 by sfyan 20161025
-- DROP TABLE IF EXISTS `eh_resource_categories`;
CREATE TABLE `eh_resource_categories` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `name` VARCHAR(64) NOT NULL COMMENT 'resource categry name',
  `owner_type` VARCHAR(32) NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `parent_id` BIGINT NOT NUll DEFAULT 0,
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
  `name` VARCHAR(64) DEFAULT NULL,
  `parent_id` BIGINT NOT NUll,
  `path` VARCHAR(128) NOT NUll,
  `type` TINYINT NOT NULL DEFAULT '0' COMMENT '0: park, 1: organization, 2:manager',
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
  `owner_type` VARCHAR(64) DEFAULT NULL,
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

ALTER TABLE `eh_launch_pad_items` CHANGE `target_id` `target_id` VARCHAR(64);





