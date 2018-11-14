-- 标注版zuolin-base-2.1之前的脚本


-- -- 广告管理 v1.4 加字段    add by xq.tian  2018/03/07
-- ALTER TABLE eh_banners ADD COLUMN target_type VARCHAR(32) NOT NULL COMMENT 'e.g: NONE, POST_DETAIL, ACTIVITY_DETAIL, APP, URL, ROUTE';
-- ALTER TABLE eh_banners ADD COLUMN target_data VARCHAR(1024) DEFAULT NULL COMMENT 'It is different by different target_type';
--
-- ALTER TABLE eh_banners MODIFY COLUMN scene_type VARCHAR(32) DEFAULT NULL;
-- ALTER TABLE eh_banners MODIFY COLUMN apply_policy TINYINT DEFAULT NULL;
--
-- -- 启动广告 v1.1          add by xq.tian  2018/03/07
-- ALTER TABLE eh_launch_advertisements ADD COLUMN target_type VARCHAR(32) NOT NULL COMMENT 'e.g: NONE, POST_DETAIL, ACTIVITY_DETAIL, APP, URL, ROUTE';
-- ALTER TABLE eh_launch_advertisements ADD COLUMN target_data VARCHAR(1024) DEFAULT NULL COMMENT 'It is different by different target_type';
-- ALTER TABLE eh_launch_advertisements ADD COLUMN content_uri_origin VARCHAR(1024) DEFAULT NULL COMMENT 'Content uri for origin file.';
--
-- -- 用户认证 V2.3 #13692
-- ALTER TABLE `eh_users` ADD COLUMN `third_data` varchar(2048) DEFAULT NULL COMMENT 'third_data for AnBang';
--
--
--
-- -- 标准item 顺序 by jiarui
-- ALTER TABLE `eh_equipment_inspection_items` ADD COLUMN `default_order`  int(11) NOT NULL DEFAULT 0 AFTER `value_jason`;
--
-- /**
--  * Designer:yilin Liu
--  * Description:ISSUE#26184 门禁人脸识别
--  * Created：2018-4-9
--  */
--
-- -- 门禁多公司管理
-- ALTER TABLE `eh_door_access`
-- ADD `mac_copy` VARCHAR(128) COMMENT '原mac地址';
--
-- /**
-- * End by: yilin Liu
-- */
--
-- -- Already delete in 5.5.1
--
-- -- TODO 这里本来是注释的，因为后面报错了，现在先放开  201807131646
-- -- ALTER TABLE `eh_organization_member_details` ADD COLUMN `profile_integrity` INTEGER NOT NULL DEFAULT '0';
-- -- ALTER TABLE eh_organization_member_details ADD COLUMN department VARCHAR(256) COMMENT '部门';
-- -- ALTER TABLE eh_organization_member_details ADD COLUMN department_ids VARCHAR(256) COMMENT '部门Id';
-- -- ALTER TABLE eh_organization_member_details ADD COLUMN job_position VARCHAR(256) COMMENT '岗位';
-- -- ALTER TABLE eh_organization_member_details ADD COLUMN job_position_ids VARCHAR(256) COMMENT '岗位Id';
-- -- ALTER TABLE eh_organization_member_details ADD COLUMN job_level VARCHAR(256) COMMENT '职级';
-- -- ALTER TABLE eh_organization_member_details ADD COLUMN job_level_ids VARCHAR(256) COMMENT '职级Id';
-- -- end Janson
--
-- -- 园区表增加namespace_id索引 add by yanjun 20180615
-- alter table eh_communities add index namespace_id_index(`namespace_id`);
--
-- -- fix for zuolinbase only, remove this after 5.5.2
-- -- ALTER TABLE `eh_organization_member_details` CHANGE COLUMN `profile_integrity` `profile_integrity` INT(11) NULL DEFAULT '0' ;
-- -- end Janson
--
-- -- 通用脚本
-- -- ADD BY 梁燕龙
-- -- issue-30013 初始化短信白名单配置项
-- -- 短信白名单 #30013
-- CREATE TABLE `eh_phone_white_list` (
-- 	`id` BIGINT NOT NULL COMMENT '主键',
-- 	`namespace_id` INT NOT NULL DEFAULT 0 COMMENT '域空间',
-- 	`phone_number` VARCHAR(128) NOT NULL COMMENT '白名单手机号码',
-- 	`creator_uid` BIGINT COMMENT '记录创建人userID',
-- 	`create_time` DATETIME COMMENT '记录创建时间',
-- 	PRIMARY KEY(`id`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '短信白名单';
-- -- END BY 梁燕龙
--
--


-- -----------------------------------------------------  以上为 5.6.1-02 的脚本 ----------------------------------------

-- -----------------------------------------------------  以下为 5.6.1 新增的脚本 ----------------------------------------

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

-- 定制版已经执行过，在ehcore-server-schema.sql中有了。 edit by jun.yan
-- ALTER TABLE `eh_organizations` ADD COLUMN `admin_target_id`  bigint(20) NULL ;
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
-- DROP TABLE IF EXISTS `eh_communityandbuilding_relationes`;
-- DROP TABLE IF EXISTS `eh_communityAndbuilding_relationes`;

CREATE TABLE `eh_communityandbuilding_relationes` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `building_id` bigint(20) DEFAULT NULL COMMENT '楼栋id',
  `community_id` bigint(20) DEFAULT NULL COMMENT '所在项目id' ,
  `address_id` bigint(20) DEFAULT NULL COMMENT '地址id' ,
  `create_time` datetime NOT NULL DEFAULT now() COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 修复 workplace 的问题 janson TODO 这里需要弄新的分支
ALTER TABLE `eh_communityandbuilding_relationes` ADD COLUMN `workplace_id` BIGINT(20) NOT NULL DEFAULT 0 AFTER `update_time`;
-- by janson end

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


-- 定制版已经执行过，在ehcore-server-schema.sql中有了。 edit by jun.yan
-- -- 通用脚本
-- -- ADD BY 黄良铭
-- -- 20180522-huangliangming-配置项管理-#30016
-- -- 创建配置项信息变更记录表
-- CREATE TABLE `eh_configurations_record_change` (
--   `id` INT(11)  NOT NULL COMMENT '主键',
--   `namespace_id` INT(11) NOT NULL COMMENT '域空间ID',
--   `conf_pre_json` VARCHAR(1024)  COMMENT '变动前信息JSON字符串',
--   `conf_aft_json` VARCHAR(1024)  COMMENT '变动后信息JSON字符串',
--   `record_change_type` INT(3) COMMENT '变动类型。0，新增；1，修改；3，删除',
--   `operator_uid` BIGINT(20)   COMMENT '操作人userId',
--   `operate_time` DATETIME    COMMENT '操作时间',
--   `operator_ip` VARCHAR(50)   COMMENT '操作者的IP地址',
--
--   PRIMARY KEY(`id`)
-- ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '配置项信息变更记录表';
--
-- -- 配置项信息表新增一列（字段 ） is_readyonly
-- ALTER  TABLE eh_configurations  ADD  is_readonly  INT(3)  COMMENT '是否只读：1，是 ；null 或其他值为 否';
-- -- END BY 黄良铭


-- -----------------------------------------------------  以上为 5.6.3 以前的脚本 ----------------------------------------

-- -----------------------------------------------------  以下为 5.6.3 新增的脚本 ----------------------------------------


-- 定制版已经执行过，在ehcore-server-schema.sql中有了。 edit by jun.yan
-- -- 人事档案 2.7 (基线已经执行过) start by ryan
-- ALTER TABLE `eh_organization_member_details` DROP COLUMN `profile_integrity`;
-- ALTER TABLE `eh_organization_member_details` DROP COLUMN `department`;
-- ALTER TABLE `eh_organization_member_details` DROP COLUMN `department_ids`;
-- ALTER TABLE `eh_organization_member_details` DROP COLUMN `job_position`;
-- ALTER TABLE `eh_organization_member_details` DROP COLUMN `job_position_ids`;
-- ALTER TABLE `eh_organization_member_details` DROP COLUMN `job_level`;
-- ALTER TABLE `eh_organization_member_details` DROP COLUMN `job_level_ids`;
--
-- ALTER TABLE `eh_organization_member_details` ADD COLUMN `check_in_time_index` VARCHAR(64) NOT NULL DEFAULT '0000' COMMENT'only month&day like 0304' AFTER `check_in_time`;
-- ALTER TABLE `eh_organization_member_details` ADD COLUMN `birthday_index` VARCHAR(64) COMMENT'only month like 0304' AFTER `birthday`;
--
-- ALTER TABLE `eh_archives_notifications` DROP COLUMN `notify_emails`;
-- ALTER TABLE `eh_archives_notifications` CHANGE COLUMN `notify_hour` `notify_time` INTEGER COMMENT 'the hour of sending notifications';
-- ALTER TABLE `eh_archives_notifications` ADD COLUMN `mail_flag` TINYINT DEFAULT 0 NOT NULL COMMENT 'email sending, 0-no 1-yes' AFTER `notify_time`;
-- ALTER TABLE `eh_archives_notifications` ADD COLUMN `message_flag` TINYINT DEFAULT 0 NOT NULL COMMENT 'message sending, 0-no 1-yes' AFTER `mail_flag`;
-- ALTER TABLE `eh_archives_notifications` ADD COLUMN `notify_target` TEXT COMMENT 'the target email address' AFTER `message_flag`;
--
-- ALTER TABLE `eh_organization_member_details` MODIFY `check_in_time` DATE COMMENT '入职日期';
-- ALTER TABLE `eh_organization_member_details` MODIFY `check_in_time_index` VARCHAR(64) COMMENT '入职日期索引字段';
--
-- -- DROP TABLE IF EXISTS `eh_archives_operational_configurations`;
-- CREATE TABLE `eh_archives_operational_configurations` (
-- 	`id` BIGINT NOT NULL,
-- 	`namespace_id` INT NOT NULL DEFAULT '0',
-- 	`organization_id` BIGINT NOT NULL DEFAULT '0',
--   `detail_id` BIGINT NOT NULL COMMENT 'the detail id that belongs to the employee which is the change target',
--   `operation_type` TINYINT NOT NULL COMMENT 'the type of operation',
--   `operation_date` DATE COMMENT 'the date of executing the operation',
--   `additional_info` TEXT COMMENT 'the addition information for the operation',
--   `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-cancel, 1-pending, 2-complete',
--   `create_time` DATETIME DEFAULT NULL COMMENT 'create time',
--   `operator_uid` BIGINT DEFAULT NULL COMMENT 'the id of the operator',
-- 	PRIMARY KEY (`id`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--
-- -- DROP TABLE IF EXISTS `eh_archives_operational_logs`;
-- CREATE TABLE `eh_archives_operational_logs` (
--   `id` BIGINT NOT NULL COMMENT 'id of the log',
--   `namespace_id` INT NOT NULL DEFAULT '0',
--   `organization_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'the id of the organization',
--   `detail_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'the detail id that belongs to the employee',
--   `operation_type` TINYINT NOT NULL COMMENT 'the type of the operate',
--   `operation_time` DATE NOT NULL COMMENT 'the time of the operate',
--   `string_tag1` VARCHAR(2048) COMMENT 'redundant information for the operate',
--   `string_tag2` VARCHAR(2048) COMMENT 'redundant information for the operate',
--   `string_tag3` VARCHAR(2048) COMMENT 'redundant information for the operate',
--   `string_tag4` VARCHAR(2048) COMMENT 'redundant information for the operate',
--   `string_tag5` VARCHAR(2048) COMMENT 'redundant information for the operate',
--   `string_tag6` VARCHAR(2048) COMMENT 'redundant information for the operate',
--   `operator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'the id of the operator',
--   `operator_name` VARCHAR(64) NOT NULL DEFAULT '0' COMMENT 'the id of the operator',
--   `create_time` DATETIME DEFAULT NULL COMMENT 'create time',
-- 	PRIMARY KEY (`id`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--
-- -- end

-- 定制版已经执行过，在ehcore-server-schema.sql中有了。 edit by jun.yan
-- -- 下载中心 搬迁代码  by yanjun start
-- -- 注意：core已经上线过了，此处是搬迁代码过来的。以后合并分支的时候要注意
--
-- -- 任务中心添加执行开始时间和上传开始时间  add by yanjun 201805241345
-- ALTER TABLE `eh_tasks` ADD COLUMN `execute_start_time`  datetime NULL;
-- ALTER TABLE `eh_tasks` ADD COLUMN `upload_file_start_time`  datetime NULL;
-- ALTER TABLE `eh_tasks` ADD COLUMN `upload_file_finish_time`  datetime NULL;

-- 下载中心 搬迁代码  by yanjun end

-- 定制版已经执行过，在ehcore-server-schema.sql中有了。 edit by jun.yan
-- -- 修复 workplace 的问题 janson TODO 这里需要弄新的分支
-- ALTER TABLE `eh_communityandbuilding_relationes` ADD COLUMN `workplace_id` BIGINT(20) NOT NULL DEFAULT 0 AFTER `update_time`;
-- -- by janson end






-- ------------------------------------------------- 以下为zuolin-base-2.1(5.8.2)新增的schema脚本   start ---------------------------------


-- 模块icon
ALTER TABLE `eh_service_modules` ADD COLUMN `icon_uri`  varchar(255) NULL;

-- 分类结构
ALTER TABLE `eh_second_app_types` ADD COLUMN `parent_id`  bigint(22) NOT NULL DEFAULT 0 ;
ALTER TABLE `eh_second_app_types` ADD COLUMN `location_type`  tinyint(4) NULL COMMENT '参考枚举ServiceModuleLocationType';
ALTER TABLE `eh_second_app_types` ADD COLUMN `default_order`  bigint(22) NULL DEFAULT 0;


CREATE TABLE `eh_app_categories` (
  `id` bigint(22) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `parent_id` bigint(22) NOT NULL DEFAULT '0',
  `location_type` tinyint(4) DEFAULT NULL COMMENT '参考枚举ServiceModuleLocationType',
  `app_type` tinyint(4) DEFAULT NULL COMMENT '一级分类，0-oa，1-community，2-service。参考ServiceModuleAppType',
  `default_order` bigint(22) DEFAULT '0',
  `leaf_flag` tinyint(4) DEFAULT NULL COMMENT 'is leaf category, 0-no, 1-yes',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER TABLE `eh_service_module_entries` CHANGE COLUMN `second_app_type` `app_category_id`  bigint(22) NOT NULL DEFAULT 0;

-- 用户自定义的广场应用
CREATE TABLE `eh_user_apps` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `app_id` bigint(20) NOT NULL,
  `location_type` tinyint(4) DEFAULT NULL COMMENT '位置信息，参考枚举ServiceModuleLocationType',
  `location_target_id` bigint(20) DEFAULT NULL COMMENT '位置对应的对象Id，eg：广场是communityId，工作台企业办公是organizationId',
  `order` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `u_eh_user_app_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户自定义显示的应用';


-- 用户自定义的广场应用
CREATE TABLE `eh_recommend_apps` (
  `id` bigint(20) NOT NULL,
  `app_id` bigint(20) NOT NULL,
  `scope_type` tinyint(4) DEFAULT NULL COMMENT '范围，1-园区，4-公司',
  `scope_id` bigint(20) DEFAULT NULL COMMENT '范围对象id',
  `order` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `u_eh_recommend_app_scope_id` (`scope_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户自定义显示的应用';

-- 服务广场通用配置表
CREATE TABLE `eh_launch_pad_configs` (
  `id` bigint(20) NOT NULL,
  `owner_type` tinyint(4) NOT NULL,
  `owner_id` bigint(20) NOT NULL,
  `navigator_all_icon_uri` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_eh_owner_id` (`owner_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 新接口使用group_id代替itemGroup和itemLocation  add by yanjun 20180828
ALTER TABLE `eh_launch_pad_items` ADD COLUMN `group_id`  bigint(20) NULL AFTER `app_id`;

ALTER TABLE `eh_item_service_categries` ADD COLUMN `group_id`  bigint(20) NULL;

ALTER TABLE `eh_user_launch_pad_items` ADD COLUMN `group_id`  bigint(20) NULL AFTER `item_id`;

-- 通用脚本
-- 增加动态表单的ownerId
ALTER TABLE `eh_var_field_scopes` ADD COLUMN `owner_id`  bigint(20) NOT NULL  DEFAULT  0  AFTER `namespace_id`;
ALTER TABLE `eh_var_field_item_scopes` ADD COLUMN `owner_id`  bigint(20) NOT NULL  DEFAULT  0 AFTER `namespace_id`;
ALTER TABLE `eh_var_field_group_scopes` ADD COLUMN `owner_id`  bigint(20) NOT NULL  DEFAULT  0 AFTER `namespace_id`;


ALTER TABLE `eh_var_field_scopes` ADD COLUMN `owner_type`  VARCHAR(1024)  NULL  AFTER `owner_id`;
ALTER TABLE `eh_var_field_item_scopes` ADD COLUMN `owner_type`  VARCHAR(1024)  NULL AFTER `owner_id`;
ALTER TABLE `eh_var_field_group_scopes` ADD COLUMN `owner_type`  VARCHAR(1024)  NULL AFTER `owner_id`;
-- end

-- 合同参数配置增加owner
ALTER TABLE  eh_contract_params ADD COLUMN  `owner_id`  BIGINT(20) NOT NULL  DEFAULT  0 AFTER  `namespace_id`;
ALTER TABLE  eh_contract_params ADD COLUMN  `ownerType` VARCHAR(1024) NULL AFTER  `namespace_id`;
ALTER TABLE  eh_contract_templates add COLUMN  `org_id`  BIGINT(20) NOT NULL  DEFAULT  0 AFTER  `namespace_id`;


-- 缴费收费项增加orgId
ALTER  TABLE  eh_payment_charging_item_scopes ADD  COLUMN  `org_id` BIGINT(20) NOT NULL   DEFAULT 0;
ALTER  TABLE  eh_payment_charging_standards_scopes ADD  COLUMN  `org_id` BIGINT(20) NOT NULL  NULL  DEFAULT 0;
ALTER  TABLE  eh_payment_bill_groups ADD  COLUMN  `org_id` BIGINT(20) NOT NULL  NULL  DEFAULT 0;


-- 通用脚本
-- AUHOR:jiarui 20180730
-- REMARK:物业巡检通知参数设置增加targetId,targetType
ALTER  TABLE  `eh_pm_notify_configurations` ADD  COLUMN `target_id` BIGINT(20) NOT NULL COMMENT 'organization id' DEFAULT  0 AFTER  `owner_type`;
ALTER  TABLE  `eh_pm_notify_configurations` ADD  COLUMN `target_type` VARCHAR(1024) NULL AFTER  `target_id`;
ALTER  TABLE  `eh_equipment_inspection_review_date` ADD  COLUMN `target_id` BIGINT(20) NOT NULL COMMENT 'organization id' DEFAULT  0 AFTER  `owner_type`;
ALTER  TABLE  `eh_equipment_inspection_review_date` ADD  COLUMN `target_type` VARCHAR(1024) NULL AFTER  `target_id`;
-- end

--
-- 工作流 key-value 表  add by xq.tian  20180814
--
DROP TABLE IF EXISTS `eh_flow_kv_configs`;
CREATE TABLE `eh_flow_kv_configs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `project_type` VARCHAR(64) NOT NULL DEFAULT '',
  `project_id` BIGINT NOT NULL DEFAULT 0,
  `module_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `owner_type` VARCHAR(64) NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `key` VARCHAR(64) NOT NULL,
  `value` VARCHAR(64) NOT NULL,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: invalid, 1:waiting_for_approval, 2: valid',
  `create_time` DATETIME(3) NOT NULL COMMENT 'record create time',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `update_time` DATETIME(3) COMMENT 'record update time',
  `updater_uid` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 表单 key-value 表  add by xq.tian  20180814
--
DROP TABLE IF EXISTS `eh_general_form_kv_configs`;
CREATE TABLE `eh_general_form_kv_configs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `project_type` VARCHAR(64) NOT NULL DEFAULT '',
  `project_id` BIGINT NOT NULL DEFAULT 0,
  `module_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `owner_type` VARCHAR(64) NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `key` VARCHAR(64) NOT NULL,
  `value` VARCHAR(64) NOT NULL,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: invalid, 1:waiting_for_approval, 2: valid',
  `create_time` DATETIME(3) NOT NULL COMMENT 'record create time',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `update_time` DATETIME(3) COMMENT 'record update time',
  `updater_uid` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE eh_general_forms ADD COLUMN project_type VARCHAR(64) NOT NULL DEFAULT 'EhCommunities';
ALTER TABLE eh_general_forms ADD COLUMN project_id BIGINT NOT NULL DEFAULT 0;

ALTER TABLE `eh_service_alliances` CHANGE COLUMN `integral_tag1` `integral_tag1` BIGINT(20) NULL DEFAULT NULL COMMENT '跳转类型 0-不跳转 2-表单/表单+工作流 3-跳转应用' ;
ALTER TABLE `eh_service_alliances` 	ADD COLUMN `form_id` BIGINT NULL DEFAULT NULL COMMENT '表单id' ;
ALTER TABLE `eh_service_alliances` 	ADD COLUMN `flow_id` BIGINT NULL DEFAULT NULL COMMENT '工作流id' ;
ALTER TABLE `eh_service_alliance_categories` ADD COLUMN `skip_type` TINYINT NOT NULL DEFAULT '0' COMMENT '1-当该服务类型下只有一个服务时，点击服务类型直接进入服务。0-反之';

ALTER TABLE `eh_service_alliance_categories` ADD COLUMN `type` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '服务联盟类型' ;

ALTER TABLE `eh_alliance_tag` ADD COLUMN `owner_type` VARCHAR(15) NOT NULL DEFAULT 'organization';

ALTER TABLE `eh_alliance_tag` ADD COLUMN `owner_id` BIGINT(20) NOT NULL DEFAULT '0' ;


-- by st.zheng 允许表单为空
ALTER TABLE `eh_lease_form_requests`
MODIFY COLUMN `source_id`  bigint(20) NULL AFTER `owner_type`;


-- 工位预订 城市管理 通用修改 shiheng.ma 20180824
ALTER TABLE `eh_office_cubicle_cities` ADD COLUMN `org_id` BIGINT(20) DEFAULT NULL COMMENT '所属管理公司Id';
ALTER TABLE `eh_office_cubicle_cities` ADD COLUMN `owner_type` VARCHAR(128) DEFAULT NULL COMMENT '项目类型';
ALTER TABLE `eh_office_cubicle_cities` ADD COLUMN `owner_id` BIGINT(20) DEFAULT NULL COMMENT '项目Id';

CREATE TABLE `eh_office_cubicle_configs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `org_id` BIGINT NOT NULL DEFAULT 0 COMMENT '管理公司Id',
  `owner_type` VARCHAR(64) NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `customize_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: general configure, 1:customize configure',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: invalid, 1:waiting_for_approval, 2: valid',
  `create_time` DATETIME(3) NOT NULL COMMENT 'record create time',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `update_time` DATETIME(3) COMMENT 'record update time',
  `updater_uid` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- END 工位预订



-- 菜单增加“管理端”、“用户端”分类
ALTER TABLE `eh_web_menus` ADD COLUMN `scene_type`  tinyint(4) NOT NULL DEFAULT 1 COMMENT '形态，1-管理端，2-客户端，参考枚举ServiceModuleSceneType';


-- ------------------------------------------------- zuolin-base-2.1(5.8.2)新增的数据脚本   end ---------------------------------






-- ------------------------------------------------- 5.8.4.20180925 新增的数据脚本   start ---------------------------------


-- 如下所说，和5.9.0后面加上的重复了。

-- -- --------------企业OA相关功能提前融合到标准版，在5.9.0全量合并到标准版发布时需要跳过这部分脚本的执行-----------
--
-- -- AUTHOR: 张智伟 20180822
-- -- REMARK: issue-36367 考勤规则新增打卡提醒设置
-- ALTER TABLE eh_punch_rules ADD COLUMN punch_remind_flag TINYINT NOT NULL DEFAULT 0 COMMENT '是否开启上下班打卡提醒：1 开启 0 关闭' AFTER china_holiday_flag;
-- ALTER TABLE eh_punch_rules ADD COLUMN remind_minutes_on_duty INT NOT NULL DEFAULT 0 COMMENT '上班提前分钟数打卡提醒' AFTER punch_remind_flag;
--
-- -- AUTHOR: 张智伟 20180822
-- -- REMARK: issue-36367 考勤规则新增打卡提醒设置,该表保存生成的提醒记录
-- CREATE TABLE `eh_punch_notifications` (
--   `id` BIGINT NOT NULL COMMENT '主键',
--   `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间',
--   `enterprise_id` BIGINT NOT NULL COMMENT '总公司id',
--   `user_id` BIGINT NOT NULL COMMENT '被提醒人的uid',
--   `detail_id` BIGINT NOT NULL COMMENT '被提醒人的detailId',
--   `punch_rule_id` BIGINT NOT NULL COMMENT '所属考勤规则',
--   `punch_type` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0- 上班打卡 ; 1- 下班打卡',
--   `punch_interval_no` INT(11) DEFAULT '1' COMMENT '第几次排班的打卡',
--   `punch_date` DATE NOT NULL COMMENT '打卡日期',
--   `rule_time` DATETIME NOT NULL COMMENT '规则设置的该次打卡时间',
--   `except_remind_time` DATETIME NOT NULL COMMENT '规则设置的打卡提醒时间',
--   `act_remind_time` DATETIME NULL COMMENT '实际提醒时间',
--   `invalid_reason` VARCHAR(512) COMMENT '提醒记录失效的原因',
--   `invalid_flag` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0- 有效 ; 1- 无效',
--   `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
--   `update_time` DATETIME NULL COMMENT '记录创建时间',
--   PRIMARY KEY (`id`),
--   KEY i_eh_enterprise_detail_id(`namespace_id`,`enterprise_id`,`detail_id`)
-- ) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='打卡提醒队列，该数据只保留一天';
--
-- -- AUTHOR: 张智伟 20180822
-- -- REMARK: issue-36367 打卡记录报表排序
-- ALTER TABLE eh_punch_logs ADD COLUMN detail_id BIGINT COMMENT '员工 的detail Id' AFTER user_id;
-- ALTER TABLE eh_punch_log_files ADD COLUMN detail_id BIGINT COMMENT '员工 的detail Id' AFTER user_id;
--
--
-- -- AUTHOR: 吴寒
-- -- REMARK: issue-36405 公告1.8 修改表结构
-- ALTER TABLE `eh_enterprise_notices` ADD COLUMN `stick_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '是否置顶，0-否，1-是';
-- ALTER TABLE `eh_enterprise_notices` ADD COLUMN `stick_time` DATETIME;
--
-- -- AUTHOR: 吴寒
-- -- REMARK: issue-33887: 增加操作人姓名到目录/文件表
-- ALTER TABLE `eh_file_management_contents` ADD COLUMN `operator_name`  VARCHAR(256) ;
-- ALTER TABLE `eh_file_management_catalogs` ADD COLUMN `operator_name`  VARCHAR(256) ;
-- -- REMARK: issue-33887: 给文件表增加索引
-- ALTER TABLE `eh_file_management_contents` ADD INDEX  `i_eh_content_catalog_id` (`catalog_id`);
-- ALTER TABLE `eh_file_management_contents` ADD INDEX  `i_eh_content_parent_id` (`parent_id`);
--
-- -- AUTHOR: 吴寒
-- -- REMARK: issue-33943 日程提醒1.2
-- ALTER TABLE eh_remind_settings ADD COLUMN app_version VARCHAR(32) DEFAULT '5.8.0' COMMENT '对应app版本(历史数据5.8.0),根据APP版本选择性展示';
-- ALTER TABLE eh_remind_settings ADD COLUMN before_time BIGINT COMMENT '提前多少时间(毫秒数)不超过1天的部分在这里减';
--
--
-- -- AUTHOR: 吴寒
-- -- REMARK: 会议管理V1.2
-- ALTER TABLE `eh_meeting_reservations`  CHANGE `content` `content` TEXT COMMENT '会议详细内容';
-- ALTER TABLE `eh_meeting_reservations`  ADD COLUMN `attachment_flag` TINYINT DEFAULT 0 COMMENT '是否有附件 1-是 0-否';
-- ALTER TABLE `eh_meeting_records`  ADD COLUMN `attachment_flag` TINYINT DEFAULT 0 COMMENT '是否有附件 1-是 0-否';
--
-- -- 增加附件表 会议预定和会议纪要共用
-- CREATE TABLE `eh_meeting_attachments` (
--   `id` BIGINT NOT NULL COMMENT 'id of the record',
--   `namespace_id` INTEGER NOT NULL DEFAULT 0,
--   `owner_type` VARCHAR(32) NOT NULL COMMENT 'owner type EhMeetingRecords/EhMeetingReservations',
--   `owner_id` BIGINT NOT NULL COMMENT 'key of the owner',
--   `content_name` VARCHAR(1024) COMMENT 'attachment object content name like: abc.jpg',
--   `content_type` VARCHAR(32) COMMENT 'attachment object content type',
--   `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
--   `content_size` INT(11)  COMMENT 'attachment object size',
--   `content_icon_uri` VARCHAR(1024) COMMENT 'attachment object link of content icon',
--   `creator_uid` BIGINT NOT NULL,
--   `create_time` DATETIME NOT NULL,
--   PRIMARY KEY (`id`)
-- ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
--
-- -- AUTHOR: 荣楠
-- -- REMARK: issue-34029 工作汇报1.2
-- ALTER TABLE `eh_work_report_val_receiver_map` ADD COLUMN `organization_id` BIGINT DEFAULT 0 NOT NULL COMMENT 'the orgId for the user' AFTER `namespace_id`;
-- ALTER TABLE `eh_work_report_val_receiver_map` ADD INDEX `i_work_report_receiver_id` (`receiver_user_id`) ;
--
-- ALTER TABLE `eh_work_reports` ADD COLUMN `validity_setting` VARCHAR(512) COMMENT 'the expiry date of the work report' AFTER `form_version`;
-- ALTER TABLE `eh_work_reports` ADD COLUMN `receiver_msg_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'the type of the receiver message settings' AFTER `validity_setting`;
-- ALTER TABLE `eh_work_reports` ADD COLUMN `receiver_msg_seeting` VARCHAR(512) COMMENT 'the time range of the receiver message' AFTER `receiver_msg_type`;
-- ALTER TABLE `eh_work_reports` ADD COLUMN `author_msg_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'the type of the author message settings' AFTER `receiver_msg_seeting`;
-- ALTER TABLE `eh_work_reports` ADD COLUMN `author_msg_seeting` VARCHAR(512) COMMENT 'the time range of the author message' AFTER `author_msg_type`;
--
-- ALTER TABLE `eh_work_report_vals` ADD COLUMN `receiver_avatar` VARCHAR(1024) COMMENT 'the avatar of the fisrt receiver' AFTER `report_type`;
-- ALTER TABLE `eh_work_report_vals` ADD COLUMN `applier_avatar` VARCHAR(1024) COMMENT 'the avatar of the author' AFTER `receiver_avatar`;
--
-- ALTER TABLE `eh_work_report_vals` MODIFY COLUMN `report_time` DATE COMMENT 'the target time of the report';
--
--
-- CREATE TABLE `eh_work_report_val_receiver_msg` (
--   `id` BIGINT NOT NULL,
--   `namespace_id` INTEGER,
--   `organization_id` BIGINT NOT NULL DEFAULT 0,
--   `report_id` BIGINT NOT NULL COMMENT 'the id of the report',
--   `report_val_id` BIGINT NOT NULL COMMENT 'id of the report val',
--   `report_name` VARCHAR(128) NOT NULL,
--   `report_type` TINYINT COMMENT '0-Day, 1-Week, 2-Month',
--   `report_time` DATE NOT NULL COMMENT 'the target time of the report',
--   `reminder_time` DATETIME COMMENT 'the reminder time of the record',
--   `receiver_user_id` BIGINT NOT NULL COMMENT 'the id of the receiver',
--   `create_time` DATETIME COMMENT 'record create time',
--
--   KEY `i_eh_work_report_val_receiver_msg_report_id`(`report_id`),
--   KEY `i_eh_work_report_val_receiver_msg_report_val_id`(`report_val_id`),
--   KEY `i_eh_work_report_val_receiver_msg_report_time`(`report_time`),
--   PRIMARY KEY (`id`)
-- ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
--
-- CREATE TABLE `eh_work_report_scope_msg` (
--   `id` BIGINT NOT NULL,
--   `namespace_id` INTEGER,
--   `organization_id` BIGINT NOT NULL DEFAULT 0,
--   `report_id` BIGINT NOT NULL COMMENT 'the id of the report',
--   `report_name` VARCHAR(128) NOT NULL,
--   `report_type` TINYINT COMMENT '0-Day, 1-Week, 2-Month',
--   `report_time` DATE NOT NULL COMMENT 'the target time of the report',
--   `reminder_time` DATETIME COMMENT 'the reminder time of the record',
--   `end_time` DATETIME COMMENT 'the deadline of the report',
--   `scope_ids` TEXT COMMENT 'the id list of the receiver',
--   `create_time` DATETIME COMMENT 'record create time',
--
--   KEY `i_eh_work_report_scope_msg_report_id`(`report_id`),
--   KEY `i_eh_work_report_scope_msg_report_time`(`report_time`),
--   PRIMARY KEY (`id`)
-- ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- -- END issue-34029
-- -- --------------企业OA相关功能提前融合到标准版，END 张智伟 -----------

-- 用户启用自定义配置的标记 add by yanjun 20180920
CREATE TABLE `eh_user_app_flags` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `location_type` tinyint(4) DEFAULT NULL COMMENT '位置信息，参考枚举ServiceModuleLocationType',
  `location_target_id` bigint(20) DEFAULT NULL COMMENT '位置对应的对象Id，eg：广场是communityId，工作台企业办公是organizationId',
  PRIMARY KEY (`id`),
  KEY `u_eh_user_app_flag_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户启用自定义配置的标记';

-- AUTHOR: xq.tian
-- REMARK: 漏掉的工作流表, 需要删除原来的表重建
DROP TABLE IF EXISTS `eh_flow_scripts`;
CREATE TABLE `eh_flow_scripts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `module_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the module id',
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `script_category` VARCHAR(64) NOT NULL COMMENT 'system_script, user_script',
  `script_type` VARCHAR(64) NOT NULL COMMENT 'javascript, groovy, java and other',
  `script_main_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_scripts',
  `script_version` INTEGER NOT NULL DEFAULT 0 COMMENT 'script version',
  `name` VARCHAR(128) COMMENT 'script name',
  `description` TEXT COMMENT 'script description',
  `script` LONGTEXT COMMENT 'script content',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: invalid, 1: valid',
  `create_time` datetime(3),
  `creator_uid` BIGINT,
  `update_time` DATETIME(3),
  `update_uid` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `integral_tag1` BIGINT NOT NULL DEFAULT 0,
  `integral_tag2` BIGINT NOT NULL DEFAULT 0,
  `integral_tag3` BIGINT NOT NULL DEFAULT 0,
  `integral_tag4` BIGINT NOT NULL DEFAULT 0,
  `integral_tag5` BIGINT NOT NULL DEFAULT 0,
  `last_commit` VARCHAR(40) COMMENT 'repository last commit id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='flow scripts in dev mode';

-- 合同字段名称修改 add by jiarui 20180925
ALTER TABLE  eh_contract_params CHANGE  ownerType owner_type VARCHAR(1024);



-- AUTHOR: 严军
-- REMARK: 组件表增加标题栏信息  20181001
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `title_flag`  tinyint(4) NULL COMMENT '0-none,1-left,2-center，reference  TitleFlag.java';
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `title`  varchar(255) NULL ;
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `title_uri`  varchar(1024) NULL ;
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `title_style`  int(11) NULL COMMENT 'title style, reference TitleStyle.java' ;
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `sub_title`  varchar(255) NULL ;
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `title_size`  tinyint(4) NULL COMMENT '0-small, 1-medium, 2-large    TitleSize.java' ;
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `title_more_flag`  tinyint(4) NULL COMMENT '0-no, 1-yes. reference trueOrFalseFlag.java' ;

-- AUTHOR: 严军
-- REMARK: 公司头像字段太短 128 -> 512
ALTER TABLE `eh_organization_details` MODIFY COLUMN `avatar`  varchar(512)  DEFAULT NULL ;

-- AUTHOR: djm
-- REMARK: 合同添加押金状态字段
ALTER TABLE eh_contracts ADD COLUMN `deposit_status`  tinyint(4) NULL COMMENT '押金状态, 0-未缴, 2-已缴' AFTER deposit;

-- AUTHOR: 荣楠
-- REMARK: 组织架构4.6 增加了唯一标识账号给通讯录表
ALTER TABLE `eh_organization_member_details` ADD COLUMN `account` VARCHAR(32) COMMENT 'the unique symbol of the member' AFTER `target_id`;

-- AUTHOR: 梁燕龙
-- REMARK: 用户增加会员等级信息。
ALTER TABLE eh_users ADD COLUMN `vip_level_text` VARCHAR(128) COMMENT '会员等级文本';

-- AUTHOR: 马世亨
-- REMARK: 访客办公地点表  20181001
ALTER TABLE `eh_visitor_sys_office_locations` ADD COLUMN `refer_type` varchar(64) NULL COMMENT '关联数据类型';
ALTER TABLE `eh_visitor_sys_office_locations` ADD COLUMN `refer_id` bigint(20) NULL COMMENT '关联数据id';
-- end

-- AUTHOR: 黄明波
-- REMARK: 服务联盟通用配置修复
CREATE TABLE `eh_alliance_config_state` (
	`id` BIGINT(20) NOT NULL,
	`namespace_id` INT(11) NOT NULL,
	`type` BIGINT(20) NOT NULL,
	`project_id` BIGINT(20) NOT NULL COMMENT 'community为项目id， organaization为公司id',
	`status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0-取默认配置 1-取自定义配置。当owner_type为organization时，该值必定为1。',
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ,
	`create_uid` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'user_id of creater' ,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `u_eh_prefix` (`type`, `project_id`)
)
COMMENT='储存应用不同项目下的配置情况。'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `eh_alliance_service_category_match` (
	`id` BIGINT(20) NOT NULL,
	`namespace_id` INT(11) NOT NULL,
        `type` BIGINT(20) NOT NULL,
	`owner_type` VARCHAR(20) NOT NULL,
	`owner_id` BIGINT(20) NOT NULL,
	`service_id` BIGINT(20) NOT NULL COMMENT '服务id',
	`category_id` BIGINT(20) NOT NULL COMMENT '服务类型id',
	`category_name` VARCHAR(64) NOT NULL COMMENT '服务类型名称',
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ,
	`create_uid` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'user_id of creater' ,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `u_eh_service_category` (`service_id`, `category_id`)
)
COMMENT='服务与服务类型的匹配表，生成/删除项目配置后需要新增/删除服务与服务类型的匹配关系。这样客户端才能获取到以前对应的服务。'
ENGINE=InnoDB
;

ALTER TABLE `eh_service_alliance_categories` ADD COLUMN `enable_provider` TINYINT NOT NULL DEFAULT '0' COMMENT '0-关闭服务商功能 1-开启' ;
ALTER TABLE `eh_service_alliance_categories` ADD COLUMN `enable_comment` TINYINT NOT NULL DEFAULT '0' COMMENT '0-关闭评论功能 1-开启评论功能' ;
ALTER TABLE `eh_service_alliance_categories` ADD COLUMN `description` MEDIUMTEXT NULL COMMENT '首页样式描述文字';



-- AUTHOR: 梁燕龙
-- REMARK: 用户认证审核权限配置表
CREATE TABLE `eh_user_authentication_organizations`(
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL  COMMENT '域空间ID',
  `organization_id` BIGINT NOT NULL COMMENT '企业ID',
  `creator_uid` BIGINT NOT NULL COMMENT 'assignment creator uid',
  `create_time` DATETIME COMMENT 'record create time',

  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '用户认证审核权限配置表';


-- AUTHOR: 黄良铭
-- REMARK: 场景记录表添加字段
ALTER TABLE eh_user_current_scene ADD COLUMN  sign_token VARCHAR(2048);


-- AUTHOR: 严军
-- REMARK: 授权表加索引
ALTER TABLE `eh_service_module_app_authorizations` ADD INDEX `organization_id_index` (`organization_id`) ;
ALTER TABLE `eh_service_module_app_authorizations` ADD INDEX `project_id_index` (`project_id`) ;
ALTER TABLE `eh_service_module_app_authorizations` ADD INDEX `owner_id_imdex` (`owner_id`) ;

-- 模块增加模块路由host
ALTER TABLE `eh_service_modules` ADD COLUMN `host`  varchar(255) NULL;

-- AUTHOR: 杨崇鑫
-- REMARK: 瑞安CM对接 为每个域空间初始化一个默认账单组，因此加上一个标识是否是默认账单组的字段
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `is_default` TINYINT DEFAULT 0 COMMENT '标识是否是默认账单组的字段：1：默认；0：非默认';
-- REMARK: 瑞安CM对接 账单、费项表增加是否是只读字段
ALTER TABLE `eh_payment_bills` ADD COLUMN `is_readonly` TINYINT DEFAULT 0 COMMENT '只读状态：0：非只读；1：只读';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `is_readonly` TINYINT DEFAULT 0 COMMENT '只读状态：0：非只读；1：只读';
-- AUTHOR: djm
alter table eh_contracts modify column sponsor_uid varchar(50);

-- AUTHOR: 荣楠
-- REMARK：OA增加域账号
-- ALTER TABLE `eh_organization_member_details` ADD COLUMN `account` VARCHAR(32) COMMENT 'the unique symbol of the member' AFTER `target_id`;

-- AUTHOR: 缪洲 20180930
-- REMARK: issue-34780 企业支付授权应用列表
ALTER TABLE `eh_siyin_print_orders` ADD COLUMN `general_bill_id` VARCHAR(64) NULL DEFAULT NULL COMMENT '统一账单id' ;

ALTER TABLE `eh_service_modules` ADD COLUMN `enable_enterprise_pay_flag`  tinyint(4) NULL COMMENT '企业支付标志，0-否，1-是';
ALTER TABLE `eh_service_module_apps` ADD COLUMN `enable_enterprise_pay_flag`  tinyint(4) NULL COMMENT '企业支付标志，0-否，1-是';

-- AUTHOR: 缪洲 20180930
-- REMARK: issue-34780 企业支付授权表
CREATE TABLE `eh_enterprise_payment_auths` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间',
  `enterprise_id` BIGINT NOT NULL COMMENT '公司id',
  `app_id` BIGINT NOT NULL COMMENT '授权应用id',
  `app_name` VARCHAR(32) NOT NULL  COMMENT '授权应用名称',
  `source_id` BIGINT NOT NULL COMMENT '授权用户id',
  `source_name` VARCHAR(32) COMMENT '授权用户名称',
  `source_type` VARCHAR(32) NOT NULL COMMENT '用户类型',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `update_time` DATETIME NULL COMMENT '记录创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='企业支付授权表';



-- AUTHOR: 杨崇鑫 20180930
-- REMARK: 物业缴费V7.1（企业记账流程打通）
-- REMARK: 删除上个版本遗留的弃用字段
ALTER TABLE `eh_asset_module_app_mappings` DROP COLUMN `energy_flag`;
ALTER TABLE `eh_asset_module_app_mappings` DROP COLUMN `contract_originId`;
ALTER TABLE `eh_asset_module_app_mappings` DROP COLUMN `contract_changeFlag`;

-- REMARK：物业缴费V7.1（企业记账流程打通）：统一订单定义的唯一标识
ALTER TABLE `eh_payment_bills` ADD COLUMN `merchant_order_id` VARCHAR(128) COMMENT '统一账单加入的：统一订单定义的唯一标识';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `merchant_order_id` VARCHAR(128) COMMENT '统一账单加入的：统一订单定义的唯一标识';

-- REMARK：  物业缴费V7.1（企业记账流程打通）:增加业务对应的相关信息
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_serve_type` VARCHAR(1024) COMMENT '商品-服务类别';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_namespace` VARCHAR(1024) COMMENT '商品-域空间';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_tag1` VARCHAR(1024) COMMENT '商品-服务提供方标识1';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_tag2` VARCHAR(1024) COMMENT '商品-服务提供方标识2';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_tag3` VARCHAR(1024) COMMENT '商品-服务提供方标识3';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_tag4` VARCHAR(1024) COMMENT '商品-服务提供方标识4';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_tag5` VARCHAR(1024) COMMENT '商品-服务提供方标识5';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_serve_apply_name` VARCHAR(1024) COMMENT '商品-服务提供方名称';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_tag` VARCHAR(1024) COMMENT '商品标识，如：活动ID、商品ID';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_name` VARCHAR(1024) COMMENT '商品名称';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_description` VARCHAR(1024) COMMENT '商品说明';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_counts` INTEGER COMMENT '商品数量';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_price` DECIMAL(10,2) COMMENT '商品单价';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_totalPrice` DECIMAL(10,2) COMMENT '商品总金额';

ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `goods_serve_type` VARCHAR(1024) COMMENT '商品-服务类别';
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `goods_namespace` VARCHAR(1024) COMMENT '商品-域空间';
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `goods_tag1` VARCHAR(1024) COMMENT '商品-服务提供方标识1';
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `goods_tag2` VARCHAR(1024) COMMENT '商品-服务提供方标识2';
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `goods_tag3` VARCHAR(1024) COMMENT '商品-服务提供方标识3';
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `goods_tag4` VARCHAR(1024) COMMENT '商品-服务提供方标识4';
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `goods_tag5` VARCHAR(1024) COMMENT '商品-服务提供方标识5';
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `goods_serve_apply_name` VARCHAR(1024) COMMENT '商品-服务提供方名称';

-- REMARK： 物业缴费V7.1（企业记账流程打通）: 增加记账人名称
ALTER TABLE `eh_payment_bills` ADD COLUMN `consume_user_name` VARCHAR(128) COMMENT '记账人名称' after `consume_user_id`;
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `consume_user_name` VARCHAR(128) COMMENT '记账人名称' after `consume_user_id`;

-- REMARK： 物业缴费V7.1（企业记账流程打通）: 修改consume_user_id注释为“记账人ID”
ALTER TABLE `eh_payment_bills` modify COLUMN `consume_user_id` BIGINT COMMENT '记账人ID';
ALTER TABLE `eh_payment_bill_items` modify COLUMN `consume_user_id` BIGINT COMMENT '记账人ID';


-- AUTHOR: 黄明波 20181007
-- REMARK： 云打印 添加发票标识
ALTER TABLE `eh_siyin_print_orders` ADD COLUMN `is_invoiced` TINYINT(4) NULL DEFAULT '0' COMMENT '是否开具发票 0-未开发票 1-已发票';
ALTER TABLE `eh_siyin_print_printers` ADD COLUMN `printer_name` VARCHAR(128) NOT NULL COMMENT 'printer name' ;
ALTER TABLE `eh_siyin_print_records` ADD COLUMN `serial_number` VARCHAR(128) NULL DEFAULT NULL COMMENT 'reader_name' ;
ALTER TABLE `eh_siyin_print_orders` ADD COLUMN `printer_name` VARCHAR(128) NULL DEFAULT NULL COMMENT '打印机名称';
ALTER TABLE `eh_siyin_print_business_payee_accounts` ADD COLUMN `merchant_id` bigint(20) NULL  DEFAULT '0' COMMENT '商户ID';



-- AUTHOR: 缪洲 20181010
-- REMARK： 云打印 添加支付方式字段
ALTER TABLE `eh_siyin_print_orders` ADD COLUMN `pay_mode` TINYINT(4) COMMENT '支付方式';

-- AUTHOR: 郑思挺 20181011
-- REMARK： 资源预约3.7.1
ALTER TABLE `eh_rentalv2_orders` ADD COLUMN `pay_channel`  VARCHAR(128) NULL  COMMENT '支付类型 ' ;
ALTER TABLE `eh_rentalv2_order_records` ADD COLUMN `pay_url`  varchar(1024) NULL AFTER `pay_info`;
ALTER TABLE `eh_rentalv2_order_records` ADD COLUMN `merchant_id`  bigint(20) NULL AFTER `pay_url`;
ALTER TABLE `eh_rentalv2_order_records` ADD COLUMN `merchant_order_id`  bigint(20) NULL AFTER `merchant_id`;
ALTER TABLE `eh_rentalv2_pay_accounts` ADD COLUMN `merchant_id`  bigint(20) NULL AFTER `account_id`;

-- AUTHOR: 缪洲 20181011
-- REMARK： 停车6.7.2 添加支付方式与支付类型字段
ALTER TABLE `eh_parking_recharge_orders` ADD COLUMN `pay_mode` TINYINT(4) COMMENT '0:个人支付，1：已记账，2：已支付，支付类型';
ALTER TABLE `eh_parking_recharge_orders` ADD COLUMN `general_order_id` varchar(64) COMMENT '统一订单ID';
ALTER TABLE `eh_parking_business_payee_accounts` ADD COLUMN `merchant_id` bigint(20) NULL COMMENT '商户ID';




-- ------------------------------------------------- 5.8.4.20180925 新增的数据脚本   end ---------------------------------
