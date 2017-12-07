-- merge from asset-org by xiongying20171129
CREATE TABLE `eh_community_organization_detail_display` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT,
  `detail_flag` TINYINT NOT NULL DEFAULT 0,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_communities` ADD COLUMN `community_number` VARCHAR(64) COMMENT '项目编号';
ALTER TABLE `eh_buildings` ADD COLUMN `building_number` VARCHAR(64) COMMENT '楼栋编号';


ALTER TABLE `eh_enterprise_customers` ADD COLUMN `version` VARCHAR(32) COMMENT '版本号';
ALTER TABLE `eh_contracts` ADD COLUMN `version` VARCHAR(32) COMMENT '版本号';
ALTER TABLE `eh_contracts` ADD COLUMN `building_rename` VARCHAR(64) COMMENT '房间别名';

ALTER TABLE `eh_contracts` ADD COLUMN `namespace_contract_type` VARCHAR(128);
ALTER TABLE `eh_contracts` ADD COLUMN `namespace_contract_token` VARCHAR(128);


-- merge from forum-2.6 start 201711291416
-- 论坛发布类型  add by yanjun 20171122
CREATE TABLE `eh_forum_service_types` (
  `id` BIGINT(20) NOT NULL,
  `namespace_id` INT(11) NOT NULL DEFAULT '0',
  `module_type` TINYINT(4) DEFAULT NULL COMMENT 'module type, 1-forum,2-activity......',
  `category_id` BIGINT(20) DEFAULT NULL,
  `service_type` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `sort_num` INT(11) NOT NULL DEFAULT '0',
  `create_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 热门标签，增加模块类型，1-论坛、2-活动、3-公告...等等  add by yanjun 20171123
ALTER TABLE `eh_hot_tags` ADD COLUMN `module_type`  TINYINT(4) DEFAULT NULL COMMENT 'module type, 1-forum 2-activity...........';
-- merge from forum-2.6 end 201711291416



-- merge from  fixPm-1.0   by jiarui  20171130
-- 增加设备字段  by jiarui
ALTER TABLE `eh_equipment_inspection_equipments`
  ADD COLUMN `brand_name` VARCHAR(1024) COMMENT 'brand_name',
  ADD COLUMN `construction_party` VARCHAR(1024) COMMENT 'construction party',
  ADD COLUMN `discard_time` DATETIME COMMENT 'discard time ',
  ADD COLUMN `manager_contact` VARCHAR(1024) ,
  ADD COLUMN `detail` VARCHAR(1024) ,
  ADD COLUMN `factory_time` DATETIME ,
  ADD COLUMN `provenance` VARCHAR(1024) ,
  ADD COLUMN `price` DECIMAL  ,
  ADD COLUMN `buy_time` DATETIME ,
  ADD COLUMN `depreciation_years` BIGINT(10) COMMENT '折旧年限' ;

ALTER TABLE `eh_equipment_inspection_equipments`
  MODIFY COLUMN `category_id`  BIGINT(20) NULL DEFAULT 0 COMMENT 'reference to the id of eh_categories' AFTER `equipment_model`;

-- 选项表增加业务自定义枚举值字段
ALTER TABLE `eh_var_field_items` ADD COLUMN `business_value` TINYINT COMMENT 'the value defined in special business like status';
ALTER TABLE `eh_var_field_item_scopes` ADD COLUMN `business_value` TINYINT COMMENT 'the value defined in special business like status';
-- merge from  fixPm-1.0   by jiarui  20171130

-- merge from yuekongjian 1.0  by yanjun 201711301748  start

-- web页面“我的”里面的菜单表
CREATE TABLE `eh_me_web_menus` (
  `id` BIGINT(20) NOT NULL,
  `namespace_id` INT(11) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `action_path` VARCHAR(255) NOT NULL,
  `action_data` VARCHAR(255) DEFAULT NULL,
  `icon_uri` VARCHAR(255) DEFAULT NULL,
  `position_flag` TINYINT(4) DEFAULT '1' COMMENT 'position, 1-NORMAL, 2-bottom',
  `sort_num` INT(11) DEFAULT '0',
  `status` TINYINT(4) DEFAULT '2' COMMENT '0: inactive, 2: active',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- layout 增加广场的背景图片 add by yanjun 201711271158
ALTER TABLE `eh_launch_pad_layouts` ADD COLUMN `bg_image_uri`  VARCHAR(255) NULL;

-- added by Janson
ALTER TABLE `eh_door_access` ADD COLUMN `namespace_id` INT(11) NOT NULL DEFAULT 0 AFTER `id`;
ALTER TABLE `eh_door_access` ADD COLUMN `display_name` VARCHAR(128) NULL DEFAULT NULL AFTER `name`;
ALTER TABLE `eh_door_auth` ADD COLUMN `namespace_id` INT(11) NOT NULL DEFAULT 0 AFTER `id`;
-- ALTER TABLE `eh_door_auth_level` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT 0 AFTER `id`;
ALTER TABLE `eh_door_auth_logs` ADD COLUMN `namespace_id` INT(11) NOT NULL DEFAULT 0 AFTER `id`;
ALTER TABLE `eh_door_command` ADD COLUMN `namespace_id` INT(11) NOT NULL DEFAULT 0 AFTER `id`;
ALTER TABLE `eh_aclink_firmware` ADD COLUMN `namespace_id` INT(11) NOT NULL DEFAULT 0 AFTER `id`;
ALTER TABLE `eh_aclinks` ADD COLUMN `namespace_id` INT(11) NOT NULL DEFAULT 0 AFTER `id`;
ALTER TABLE `eh_aclink_undo_key` ADD COLUMN `namespace_id` INT(11) NOT NULL DEFAULT 0 AFTER `id`;

-- merge from yuekongjian 1.0  by yanjun 201711301748  start

-- by wentian
ALTER TABLE `eh_payment_bill_groups_rules` ADD `brother_rule_id` BIGINT DEFAULT NULL COMMENT '兄弟账单组收费项id';

-- 二维码加路由   add by xq.tian  2017/11/15
ALTER TABLE eh_qrcodes ADD COLUMN `route_uri` VARCHAR(256) COMMENT 'route uri, like zl://xxx/xxx';
ALTER TABLE eh_qrcodes ADD COLUMN `handler` VARCHAR(32) COMMENT 'module handler';
ALTER TABLE eh_qrcodes ADD COLUMN `extra` TEXT COMMENT 'module handler';

ALTER TABLE eh_flow_cases ADD COLUMN `route_uri` VARCHAR(128) COMMENT 'route uri';

