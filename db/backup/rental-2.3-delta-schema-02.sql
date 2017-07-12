ALTER TABLE `eh_acls` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_acls` ADD COLUMN `role_type` VARCHAR(32) COMMENT 'NULL: EhAclRole';
ALTER TABLE `eh_acls` ADD COLUMN `scope` VARCHAR(128);
ALTER TABLE `eh_acls` ADD COLUMN `integral_tag1` BIGINT;
ALTER TABLE `eh_acls` ADD COLUMN `integral_tag2` BIGINT;
ALTER TABLE `eh_acls` ADD COLUMN `integral_tag3` BIGINT;
ALTER TABLE `eh_acls` ADD COLUMN `integral_tag4` BIGINT;
ALTER TABLE `eh_acls` ADD COLUMN `integral_tag5` BIGINT;
ALTER TABLE `eh_acls` ADD COLUMN `comment_tag1` VARCHAR(128);
ALTER TABLE `eh_acls` ADD COLUMN `comment_tag2` VARCHAR(128);
ALTER TABLE `eh_acls` ADD COLUMN `comment_tag3` VARCHAR(128);
ALTER TABLE `eh_acls` ADD COLUMN `comment_tag4` VARCHAR(128);
ALTER TABLE `eh_acls` ADD COLUMN `comment_tag5` VARCHAR(128);
ALTER TABLE `eh_acls` ADD INDEX `i_eh_acl_namespace_id`(`namespace_id`); 
ALTER TABLE `eh_acls` ADD INDEX `i_eh_acl_scope`(`scope`); 
ALTER TABLE `eh_acls` ADD INDEX `i_eh_acl_itag1`(`integral_tag1`); 
ALTER TABLE `eh_acls` ADD INDEX `i_eh_acl_itag2`(`integral_tag2`); 
ALTER TABLE `eh_acls` ADD INDEX `i_eh_acl_ctag1`(`comment_tag1`);
ALTER TABLE `eh_acls` ADD INDEX `i_eh_acl_ctag2`(`comment_tag2`);

ALTER TABLE `eh_acl_role_assignments` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries';
ALTER TABLE `eh_acl_role_assignments` ADD INDEX `i_eh_acl_role_asgn_namespace_id`(`namespace_id`);

-- merge from organization-delta-schema.sql by lqs 20161128
-- 资源分类定义 by sfyan 20161025
-- DROP TABLE IF EXISTS `eh_resource_categories`;
CREATE TABLE `eh_resource_categories` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `name` VARCHAR(64) NOT NULL COMMENT 'resource categry name',
  `owner_type` VARCHAR(32) NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `path` VARCHAR(128) NOT NULL,
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
  `parent_id` BIGINT NOT NULL,
  `path` VARCHAR(128) NOT NULL,
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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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






-- merge from quality2-delta-schema.sql by lqs 20161128
ALTER TABLE eh_quality_inspection_standards ADD COLUMN `target_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE eh_quality_inspection_standards ADD COLUMN `target_type` VARCHAR(32) NOT NULL DEFAULT '';
ALTER TABLE eh_quality_inspection_standards ADD COLUMN `review_result` TINYINT NOT NULL DEFAULT '0' COMMENT '0:none, 1: qualified, 2: unqualified';
ALTER TABLE eh_quality_inspection_standards ADD COLUMN `reviewer_uid` BIGINT NOT NULL DEFAULT '0';
ALTER TABLE eh_quality_inspection_standards ADD COLUMN `review_time` DATETIME;
ALTER TABLE eh_quality_inspection_standards ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT '0';
ALTER TABLE eh_quality_inspection_standards DROP COLUMN `category_id`;

ALTER TABLE eh_quality_inspection_tasks ADD COLUMN `target_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE eh_quality_inspection_tasks ADD COLUMN `target_type` VARCHAR(32) NOT NULL DEFAULT '';
ALTER TABLE eh_quality_inspection_tasks ADD COLUMN `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id';

-- DROP TABLE IF EXISTS `eh_quality_inspection_standard_specification_map`;
CREATE TABLE `eh_quality_inspection_standard_specification_map` (
  `id` BIGINT NOT NULL COMMENT 'id', 
  `standard_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'reference to the id of eh_equipment_inspection_standards',
  `specification_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'reference to the id of eh_quality_inspection_specifications',
  `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: disabled, 1: waiting for approval, 2: active',
  `deleter_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'deleter id',
  `delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_quality_inspection_specifications`;
CREATE TABLE `eh_quality_inspection_specifications` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `scope_code` TINYINT NOT NULL DEFAULT '0' COMMENT '0: all, 1: community',
  `scope_id` BIGINT NOT NULL DEFAULT '0',
  `parent_id` BIGINT NOT NULL DEFAULT '0',
  `name` VARCHAR(64) NOT NULL DEFAULT '',
  `path` VARCHAR(128),
  `score` DOUBLE NOT NULL DEFAULT '100',
  `description` TEXT COMMENT 'content data',
  `weight` DOUBLE NOT NULL DEFAULT '1.00',
  `inspection_type` TINYINT NOT NULL DEFAULT '0' COMMENT '0: category, 1: specification, 2: specification item',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `apply_policy` TINYINT NOT NULL DEFAULT '0' COMMENT '0: add, 1: modify, 2: delete',
  `refer_id` BIGINT NOT NULL DEFAULT '0',
  `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: disabled, 1: waiting for approval, 2: active',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_quality_inspection_specification_item_results`;
CREATE TABLE `eh_quality_inspection_specification_item_results` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the item, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '',
  `task_record_id` BIGINT NOT NULL COMMENT 'id of the eh_quality_inspection_task_records',
  `task_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_quality_inspection_tasks',
  `specification_parent_id` BIGINT NOT NULL DEFAULT '0',
  `specification_id` BIGINT NOT NULL DEFAULT '0',
  `specification_path` VARCHAR(128),
  `item_description` VARCHAR(512),
  `item_score` DOUBLE NOT NULL DEFAULT '0',
  `quantity` INTEGER NOT NULL DEFAULT '0',
  `total_score` DOUBLE NOT NULL DEFAULT '0',
  `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- merge from activityentry-delta-schema.sql by xiongying 20161128
ALTER TABLE eh_activities ADD COLUMN `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'activity category id';
ALTER TABLE eh_activities ADD COLUMN `forum_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'activity post forum that it belongs';
ALTER TABLE eh_activities ADD COLUMN `creator_tag` VARCHAR(128) NOT NULL DEFAULT 0 COMMENT 'activity post creator tag';
ALTER TABLE eh_activities ADD COLUMN `target_tag` VARCHAR(128) NOT NULL DEFAULT 0 COMMENT 'activity post target tag';
ALTER TABLE eh_activities ADD COLUMN `visible_region_type` TINYINT COMMENT 'define the visible region type';
ALTER TABLE eh_activities ADD COLUMN `visible_region_id` BIGINT COMMENT 'visible region id';

-- DROP TABLE IF EXISTS `eh_activity_categories`;
CREATE TABLE `eh_activity_categories` (
  `id` BIGINT(20) NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT(20) NOT NULL DEFAULT '0',
  `parent_id` BIGINT(20) NOT NULL DEFAULT '0',
  `name` VARCHAR(64) NOT NULL,
  `path` VARCHAR(128) DEFAULT NULL,
  `default_order` INT(11) DEFAULT NULL,
  `status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` DATETIME DEFAULT NULL,
  `delete_uid` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'record deleter user id',
  `delete_time` DATETIME DEFAULT NULL,
  `namespace_id` INT(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- merge from sa1.7-delta-schema.sql by xiongying 20161128
-- 预约看楼信息
-- DROP TABLE IF EXISTS `eh_service_alliance_apartment_requests`;
CREATE TABLE `eh_service_alliance_apartment_requests` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `type` BIGINT NOT NULL DEFAULT 0,
  `category_id` BIGINT NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `creator_name` VARCHAR(128),
  `creator_mobile` VARCHAR(128),
  `creator_organization_id` BIGINT NOT NULL DEFAULT 0,
  `service_alliance_id` BIGINT NOT NULL DEFAULT 0,
  
  `name` VARCHAR(128),
  `mobile` VARCHAR(128),
  `organization_name` VARCHAR(128),
  `area_size` DOUBLE COMMENT 'area size',
  `remarks` VARCHAR(1024),
  
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_service_alliance_skip_rule`;
CREATE TABLE `eh_service_alliance_skip_rule` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `service_alliance_category_id` BIGINT NOT NULL DEFAULT 0,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 物业报修2.6 merge from pmtask-delta-schema.sql by sw 20161128
ALTER TABLE eh_pm_tasks ADD COLUMN `operator_star` TINYINT NOT NULL DEFAULT 0 COMMENT 'task star of operator';
ALTER TABLE eh_pm_tasks ADD COLUMN `address_type` TINYINT COMMENT '1: family , 2:organization';
ALTER TABLE eh_pm_tasks ADD COLUMN `address_org_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'organization of address';
-- DROP TABLE IF EXISTS `eh_pm_task_target_statistics`;
CREATE TABLE `eh_pm_task_target_statistics` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,

  `target_id` BIGINT NOT NULL DEFAULT 0,
  `avg_star` DECIMAL(10,2) NOT NULL DEFAULT 0,
  `task_category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'task category id',

  `date_str` DATETIME,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;






