
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

ALTER TABLE `eh_news` ADD COLUMN `create_type` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '0-后台创建 1-第三方调用接口' ;

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
