 -- 应用公司项目授权表 by lei.lv
 CREATE TABLE `eh_service_module_app_authorizations` (
  `id` BIGINT(20) NOT NULL,
  `namespace_id` INT(11) DEFAULT NULL,
  `owner_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'owner_id',
  `organization_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'organization_id',
  `project_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'community_id',
  `app_id` BIGINT(20)  NOT NULL DEFAULT '0' COMMENT 'app_id',
  `control_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'control type',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `eh_organization_apps` (
  `id` bigint(20) NOT NULL,
  `app_origin_id` bigint(20) DEFAULT NULL,
  `org_id` bigint(20) DEFAULT NULL,
  `alias_name` varchar(255) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operator_time` datetime DEFAULT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_organization_apps` ADD INDEX `org_app_orgid` (`org_id`) ;
ALTER TABLE `eh_organization_apps` ADD INDEX `org_app_appid` (`app_origin_id`) ;


CREATE TABLE `eh_app_community_config` (
  `id` bigint(20) NOT NULL,
  `organization_app_id` bigint(20) DEFAULT NULL,
  `community_id` bigint(20) DEFAULT NULL,
  `visibilityFlag` tinyint(4) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operator_time` datetime DEFAULT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_app_community_config` ADD INDEX `organization_app_id` (`organization_app_id`) ;
ALTER TABLE `eh_app_community_config` ADD INDEX `community_id` (`community_id`) ;

-- 应用档案表
CREATE TABLE `eh_service_module_app_profile` (
  `id` bigint(20) NOT NULL,
  `origin_id` bigint(20) NOT NULL,
  `app_category` varchar(255) DEFAULT NULL,
  `display_version` varchar(128) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `mobile_flag` tinyint(4) DEFAULT 0,
  `mobile_uri` varchar(1024) DEFAULT NULL,
  `pc_flag` tinyint(4) DEFAULT 0,
  `pc_uri` varchar(1024) DEFAULT NULL,
  `app_entry_info` varchar(2048) DEFAULT NULL,
  `independent_config_flag` tinyint(4) DEFAULT 0,
  `config_app_ids` varchar(128) DEFAULT NULL,
  `support_third_flag` tinyint(4) DEFAULT 0,
  `default_flag` tinyint(4) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 增加应用类型字段 0-oa应用、1-园区应用、2-服务应用   add by yanjun 201804081501
ALTER TABLE `eh_service_modules` ADD COLUMN `app_type`  tinyint(4) NULL COMMENT 'app type, 0-oaapp,1-communityapp,2-serviceapp';
ALTER TABLE `eh_service_module_apps` ADD COLUMN `app_type`  tinyint(4) NULL COMMENT 'app type, 0-oaapp,1-communityapp,2-serviceapp';

-- 新增修改时间的字段 add by lei.lv 201804091401
ALTER TABLE `eh_service_module_app_authorizations` ADD COLUMN `create_time`  datetime NULL COMMENT 'create_time';
ALTER TABLE `eh_service_module_app_authorizations` ADD COLUMN `update_time`  datetime NULL COMMENT 'update_time';

-- 新增开发者字段
ALTER TABLE `eh_service_module_app_profile` ADD COLUMN `develop_id`  bigint(20) NULL COMMENT 'developer owner id';

-- 增加 企业超级管理员id、是否开启工作台标志  add by yanjun 20180412
ALTER TABLE `eh_organizations` ADD COLUMN `admin_target_id`  bigint(20) NULL ;
ALTER TABLE `eh_organizations` ADD COLUMN `work_platform_flag`  tinyint(4) NULL COMMENT 'open work platform flag, 0-no, 1-yes' ;

-- 默认园区标志
ALTER TABLE `eh_communities` ADD COLUMN `default_community_flag`  tinyint(4) NULL COMMENT 'is the default community in his namespace, 0-no, 1-yes';

-- 主页签信息
CREATE TABLE `eh_launch_pad_indexs` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `type` varchar(64) NOT NULL,
  `name` varchar(64) DEFAULT NULL,
  `config_json` text,
  `icon_uri` varchar(1024) DEFAULT NULL,
  `selected_icon_uri` varchar(1024) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) NOT NULL,
  `creator_uid` bigint(20) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DELETE FROM eh_portal_navigation_bars;
ALTER TABLE `eh_portal_navigation_bars` CHANGE COLUMN `label` `name`  varchar(64) DEFAULT NULL;
ALTER TABLE `eh_portal_navigation_bars` CHANGE COLUMN `target_type` `type`  tinyint(4) NOT NULL;
ALTER TABLE `eh_portal_navigation_bars` CHANGE COLUMN `target_id` `config_json`  varchar(1024) NOT NULL;
