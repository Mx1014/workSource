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


-- 公司安装应用表
CREATE TABLE `eh_organization_apps` (
  `id` bigint(20) NOT NULL,
  `app_origin_id` bigint(20) DEFAULT NULL,
  `org_id` bigint(20) DEFAULT NULL,
  `visibility_flag` tinyint(4) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operator_time` datetime DEFAULT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_organization_apps` ADD INDEX `org_app_orgid` (`org_id`) ;
ALTER TABLE `eh_organization_apps` ADD INDEX `org_app_appid` (`app_origin_id`) ;

-- 园区应用配置表（不跟随管理公司时的自定义配置）
CREATE TABLE `eh_app_community_configs` (
  `id` bigint(20) NOT NULL,
  `app_origin_id` bigint(20) DEFAULT NULL COMMENT 'app_origin_id',
  `community_id` bigint(20) DEFAULT NULL,
  `visibility_flag` tinyint(4) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operator_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `community_id` (`community_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 应用档案表
CREATE TABLE `eh_service_module_app_profile` (
  `id` bigint(20) NOT NULL,
  `origin_id` bigint(20) NOT NULL,
  `app_no` varchar(255) DEFAULT NULL,
  `display_version` varchar(128) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `mobile_flag` tinyint(4) DEFAULT 0,
  `mobile_uris` varchar(1024) DEFAULT NULL,
  `pc_flag` tinyint(4) DEFAULT 0,
  `pc_uris` varchar(1024) DEFAULT NULL,
  `app_entry_infos` varchar(2048) DEFAULT NULL,
  `independent_config_flag` tinyint(4) DEFAULT 0,
  `dependent_app_ids` varchar(128) DEFAULT NULL,
  `support_third_flag` tinyint(4) DEFAULT 0,
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
  `type` tinyint(4) NOT NULL,
  `name` varchar(64) DEFAULT NULL,
  `config_json` text,
  `default_order` int(11) NOT NULL DEFAULT '0',
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

alter table eh_launch_pad_indexs add index namespace_id_index(`namespace_id`);

-- 一卡通实现
CREATE TABLE `eh_smart_card_keys` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(256) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 无效, 1 有效',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DELETE FROM eh_portal_navigation_bars;
ALTER TABLE `eh_portal_navigation_bars` CHANGE COLUMN `target_type` `type`  tinyint(4) NOT NULL;
ALTER TABLE `eh_portal_navigation_bars` CHANGE COLUMN `target_id` `config_json`  varchar(1024) NOT NULL;
ALTER TABLE `eh_portal_navigation_bars` ADD COLUMN `version_id`  bigint(20) NULL ;
ALTER TABLE `eh_portal_navigation_bars` ADD COLUMN `default_order`  int(11) NOT NULL DEFAULT '0' ;

-- layout 类型、背景颜色
ALTER TABLE `eh_launch_pad_layouts` ADD COLUMN `type`  tinyint(4) NULL;
ALTER TABLE `eh_launch_pad_layouts` ADD COLUMN `bg_color`  varchar(255) NULL ;
-- layout 类型
ALTER TABLE `eh_portal_layouts` ADD COLUMN `type`  tinyint(4) NULL;
ALTER TABLE `eh_portal_layouts` ADD COLUMN `bg_color`  varchar(255) NULL;

-- 功能模块入口列表
CREATE TABLE `eh_service_module_entries` (
  `id` bigint(20) NOT NULL,
  `module_id` bigint(20) NOT NULL,
  `module_name` varchar(256) DEFAULT NULL,
  `entry_name` varchar(256) DEFAULT NULL,
  `terminal_type` tinyint(4) NOT NULL COMMENT '终端列表，1-mobile,2-pc',
  `location_type` tinyint(4) NOT NULL COMMENT '位置，参考枚举ServiceModuleLocationType',
  `scene_type` tinyint(4) NOT NULL COMMENT '形态，1-管理端，2-客户端，参考枚举ServiceModuleSceneType',
  `second_app_type` int(11) NOT NULL DEFAULT '0',
  `default_order` int(11) NOT NULL DEFAULT '0',
  `icon_uri` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 应用二级分类
CREATE TABLE `eh_second_app_types` (
  `id` bigint(22) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `app_type` tinyint(4) DEFAULT NULL COMMENT '一级分类，0-oa，1-community，2-service。参考ServiceModuleAppType',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_service_module_entries` ADD INDEX `module_entry_module_id` (`module_id`);


-- 增加字段member_range人员规模
-- add by lei yuan
alter table eh_organization_details add member_range varchar(25) default null comment '人员规模';
-- 增加字段 pm_flag 是否是管理公司 1-是，0-否
ALTER TABLE eh_organization_details ADD COLUMN `pm_flag` tinyint(4) DEFAULT NULL COMMENT '是否是管理公司 1-是，0-否';
-- 增加字段 service_support_flag 是否是服务商 1-是，0-否
ALTER TABLE eh_organization_details ADD COLUMN `service_support_flag` tinyint(4) DEFAULT NULL COMMENT '是否是服务商 1-是，0-否';
-- 增加字段 pm_flag 是否是管理公司 1-是，0-否
ALTER TABLE eh_organizations ADD COLUMN `pm_flag` tinyint(4) DEFAULT NULL COMMENT '是否是管理公司 1-是，0-否';
-- 增加字段 service_support_flag 是否是服务商 1-是，0-否
ALTER TABLE eh_organizations ADD COLUMN `service_support_flag` tinyint(4) DEFAULT NULL COMMENT '是否是服务商 1-是，0-否';


-- 增加办公地点表
-- add by leiyuan
CREATE TABLE `eh_organization_workplaces` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '组织id',
  `workplace_name` varchar(50) DEFAULT NULL COMMENT '办公点名称',
  `community_id` bigint(20) DEFAULT NULL COMMENT '所在项目id' ,
  `create_time` datetime NOT NULL DEFAULT now() COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- 增加办公地点与楼栋门牌的关系表
CREATE TABLE `eh_communityandbuilding_relationes` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `building_id` bigint(20) DEFAULT NULL COMMENT '楼栋id',
  `community_id` bigint(20) DEFAULT NULL COMMENT '所在项目id' ,
  `address_id` bigint(20) DEFAULT NULL COMMENT '地址id' ,
  `create_time` datetime NOT NULL DEFAULT now() COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 增加 应用icon信息  add by yanjun 20180426
ALTER TABLE `eh_service_module_app_profile` ADD COLUMN `icon_uri`  varchar(255) NULL;

-- 标准版里app的配置是否跟随默认配置
ALTER TABLE `eh_communities` ADD COLUMN `app_self_config_flag`  tinyint(4) NULL ;


-- 标准版本的 key 增加用户 ID
ALTER TABLE `eh_smart_card_keys` ADD COLUMN `user_id` BIGINT(20) NOT NULL DEFAULT 0 AFTER `namespace_id`;
ALTER TABLE `eh_smart_card_keys` ADD COLUMN `cardkey` VARCHAR(1024) AFTER `namespace_id`;

-- 客户端处理方式0-native, 1-outside url, 2-inside url, 3-offline package  add by yanjun 201805171140
ALTER TABLE `eh_service_modules` ADD COLUMN `client_handler_type`  tinyint(4) NULL DEFAULT 0 COMMENT '0-native, 1-outside url, 2-inside url, 3-offline package' AFTER `app_type`;

-- 标准版本的 add by yuanlei
alter table eh_organization_workplaces add column province_id bigint(20) default null comment '省份id';
alter table eh_organization_workplaces add column city_id bigint(20) default null comment '城市id';
alter table eh_organization_workplaces add column area_id bigint(20) default null comment '区域id';
alter table eh_organization_workplaces add column whole_address_name varchar(128) default null comment '地址详细名称';

-- 增加省份字段  add by yanjun 201805251851
-- 增加省份字段  add by yanjun 201805251851
ALTER TABLE `eh_communities` ADD COLUMN `province_id`  bigint(20) NULL AFTER `uuid`;
ALTER TABLE `eh_communities` ADD COLUMN `province_name`  varchar(64) NULL AFTER `province_id`;

-- 系统应用标志、默认安装应用标志 add by yanjun 201805280955
ALTER TABLE `eh_service_modules` ADD COLUMN `system_app_flag`  tinyint(4) NULL COMMENT '0-no, 1-yes';
ALTER TABLE `eh_service_module_apps` ADD COLUMN `system_app_flag`  tinyint(4) NULL COMMENT '0-no, 1-yes';
ALTER TABLE `eh_service_module_apps` ADD COLUMN `default_app_flag`  tinyint(4) NULL COMMENT 'installed when organiation was created, 0-no, 1-yes';

-- 修改appId名字，实际为应用originId
ALTER TABLE `eh_banners` CHANGE COLUMN `appId` `app_id`  bigint(20) NULL DEFAULT NULL;

-- 园区广场电商 add by yanjun 20180703
CREATE TABLE `eh_community_bizs` (
  `id` bigint(20) NOT NULL,
  `organization_id` bigint(20) DEFAULT NULL,
  `community_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `biz_url` varchar(255) DEFAULT NULL,
  `logo_uri` varchar(255) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '2' COMMENT '0-delete，1-disable，2-enable',
  PRIMARY KEY (`id`),
  KEY `community_id` (`community_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 人事档案 2.7 (基线已经执行过) start by ryan
ALTER TABLE `eh_organization_member_details` DROP COLUMN `profile_integrity`;
ALTER TABLE `eh_organization_member_details` DROP COLUMN `department`;
ALTER TABLE `eh_organization_member_details` DROP COLUMN `department_ids`;
ALTER TABLE `eh_organization_member_details` DROP COLUMN `job_position`;
ALTER TABLE `eh_organization_member_details` DROP COLUMN `job_position_ids`;
ALTER TABLE `eh_organization_member_details` DROP COLUMN `job_level`;
ALTER TABLE `eh_organization_member_details` DROP COLUMN `job_level_ids`;

ALTER TABLE `eh_organization_member_details` ADD COLUMN `check_in_time_index` VARCHAR(64) NOT NULL DEFAULT '0000' COMMENT'only month&day like 0304' AFTER `check_in_time`;
ALTER TABLE `eh_organization_member_details` ADD COLUMN `birthday_index` VARCHAR(64) COMMENT'only month like 0304' AFTER `birthday`;

ALTER TABLE `eh_archives_notifications` DROP COLUMN `notify_emails`;
ALTER TABLE `eh_archives_notifications` CHANGE COLUMN `notify_hour` `notify_time` INTEGER COMMENT 'the hour of sending notifications';
ALTER TABLE `eh_archives_notifications` ADD COLUMN `mail_flag` TINYINT DEFAULT 0 NOT NULL COMMENT 'email sending, 0-no 1-yes' AFTER `notify_time`;
ALTER TABLE `eh_archives_notifications` ADD COLUMN `message_flag` TINYINT DEFAULT 0 NOT NULL COMMENT 'message sending, 0-no 1-yes' AFTER `mail_flag`;
ALTER TABLE `eh_archives_notifications` ADD COLUMN `notify_target` TEXT COMMENT 'the target email address' AFTER `message_flag`;

ALTER TABLE `eh_organization_member_details` MODIFY `check_in_time` DATE COMMENT '入职日期';
ALTER TABLE `eh_organization_member_details` MODIFY `check_in_time_index` VARCHAR(64) COMMENT '入职日期索引字段';

-- DROP TABLE IF EXISTS `eh_archives_operational_configurations`;
CREATE TABLE `eh_archives_operational_configurations` (
	`id` BIGINT NOT NULL,
	`namespace_id` INT NOT NULL DEFAULT '0',
	`organization_id` BIGINT NOT NULL DEFAULT '0',
  `detail_id` BIGINT NOT NULL COMMENT 'the detail id that belongs to the employee which is the change target',
  `operation_type` TINYINT NOT NULL COMMENT 'the type of operation',
  `operation_date` DATE COMMENT 'the date of executing the operation',
  `additional_info` TEXT COMMENT 'the addition information for the operation',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-cancel, 1-pending, 2-complete',
  `create_time` DATETIME DEFAULT NULL COMMENT 'create time',
  `operator_uid` BIGINT DEFAULT NULL COMMENT 'the id of the operator',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_archives_operational_logs`;
CREATE TABLE `eh_archives_operational_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the log',
  `namespace_id` INT NOT NULL DEFAULT '0',
  `organization_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'the id of the organization',
  `detail_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'the detail id that belongs to the employee',
  `operation_type` TINYINT NOT NULL COMMENT 'the type of the operate',
  `operation_time` DATE NOT NULL COMMENT 'the time of the operate',
  `string_tag1` VARCHAR(2048) COMMENT 'redundant information for the operate',
  `string_tag2` VARCHAR(2048) COMMENT 'redundant information for the operate',
  `string_tag3` VARCHAR(2048) COMMENT 'redundant information for the operate',
  `string_tag4` VARCHAR(2048) COMMENT 'redundant information for the operate',
  `string_tag5` VARCHAR(2048) COMMENT 'redundant information for the operate',
  `string_tag6` VARCHAR(2048) COMMENT 'redundant information for the operate',
  `operator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'the id of the operator',
  `operator_name` VARCHAR(64) NOT NULL DEFAULT '0' COMMENT 'the id of the operator',
  `create_time` DATETIME DEFAULT NULL COMMENT 'create time',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- end


-- 下载中心 搬迁代码  by yanjun start
-- 注意：core已经上线过了，此处是搬迁代码过来的。以后合并分支的时候要注意

-- 任务中心添加执行开始时间和上传开始时间  add by yanjun 201805241345
ALTER TABLE `eh_tasks` ADD COLUMN `execute_start_time`  datetime NULL;
ALTER TABLE `eh_tasks` ADD COLUMN `upload_file_start_time`  datetime NULL;
ALTER TABLE `eh_tasks` ADD COLUMN `upload_file_finish_time`  datetime NULL;

-- 下载中心 搬迁代码  by yanjun end

-- 修复 workplace 的问题 janson TODO 这里需要弄新的分支
ALTER TABLE `eh_communityandbuilding_relationes` ADD COLUMN `workplace_id` BIGINT(20) NOT NULL DEFAULT 0 AFTER `update_time`;
-- by janson end
