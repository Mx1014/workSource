-- from asset_multi
CREATE TABLE `eh_service_module_app_mappings`(
  `id` BIGINT NOT NULL ,
  `app_origin_id_male` BIGINT NOT NULL COMMENT 'the origin id of app',
  `app_module_id_male` BIGINT NOT NULL COMMENT 'the module id of app',
  `app_origin_id_female` BIGINT NOT NULL COMMENT 'the origin id of app',
  `app_module_id_female` BIGINT NOT NULL COMMENT 'the module id of app',
  `create_time` DATETIME NOT NULL DEFAULT now(),
  `create_uid` BIGINT NOT NULL,
  `update_time` DATETIME NOT NULL DEFAULT now(),
  `update_uid` BIGINT DEFAULT NULL,
  UNIQUE KEY `origin_id_mapping` (`app_origin_id_male`, `app_origin_id_female`),
  UNIQUE KEY `i_origin_module` (`app_origin_id_male`, `app_module_id_male`),
  UNIQUE KEY `i_origin_module_reverse` (`app_origin_id_female`, `app_module_id_male`),
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'relation mappings among applications';

-- multiple entry category id for asset module by wentian
CREATE TABLE `eh_asset_app_categories`(
  `id` BIGINT NOT NULL,
  `category_id` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL DEFAULT now(),
  `create_uid` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `instance_flag` VARCHAR(1024) DEFAULT NULL ,
  UNIQUE KEY `i_category_id` (`category_id`),
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'relation mappings among applications';

-- 账单数据添加categoryId by wentian 2018/5/25
ALTER TABLE `eh_payment_bills` ADD COLUMN `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id，对应应用的origin_id';

-- 账单催缴设置添加categoryId by wentian 2018/5/25
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id，对应应用的origin_id';

ALTER TABLE `eh_payment_charging_item_scopes` ADD COLUMN `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id';
ALTER TABLE `eh_payment_charging_standards_scopes` ADD COLUMN `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id';
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id';


ALTER TABLE `eh_payment_bill_items` ADD COLUMN `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id，对应应用的origin_id';

-- end of wentian's script

-- from testByDingjianminThree
-- --合同管理 基础设置合同规则
ALTER TABLE `eh_contract_params` ADD COLUMN `payorreceive_contract_type` tinyint(2) DEFAULT '0' COMMENT '0 收款合同 1付款合同';
ALTER TABLE `eh_contract_params` ADD COLUMN `contract_number_rulejson` text NULL COMMENT '合同规则';
ALTER TABLE `eh_contract_params` ADD COLUMN `update_time` datetime NULL COMMENT '更新时间';
ALTER TABLE `eh_contract_params` ADD COLUMN `category_id` bigint(20) NULL COMMENT 'contract category id';
-- --合同管理 合同多入口设置
ALTER TABLE `eh_contracts` ADD COLUMN `category_id` bigint(20) NULL COMMENT 'contract category id';
-- --合同管理，表单设置，动态字段
ALTER TABLE `eh_var_field_scopes` ADD COLUMN `category_id` bigint(20) NULL COMMENT 'category id';
ALTER TABLE `eh_var_field_item_scopes` ADD COLUMN `category_id` bigint(20) NULL COMMENT 'category id';
ALTER TABLE `eh_var_field_group_scopes` ADD COLUMN `category_id` bigint(20) NULL COMMENT 'category id';

CREATE TABLE `eh_contract_categories` (
  `id` bigint(20) NOT NULL,
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parent_id` bigint(20) NOT NULL DEFAULT '0',
  `name` varchar(64) NOT NULL,
  `path` varchar(128) DEFAULT NULL,
  `default_order` int(11) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` datetime DEFAULT NULL,
  `delete_uid` bigint(20) DEFAULT '0' COMMENT 'record deleter user id',
  `delete_time` datetime DEFAULT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `logo_uri` varchar(1024) DEFAULT NULL COMMENT 'default cover uri',
  `entry_id` int(11) DEFAULT NULL,
  `contract_application_scene` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 租赁合同场景 1 物业合同场景 2 综合合同场景',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- modify table eh_service_module_app_mappings
DROP TABLE IF EXISTS `eh_service_module_app_mappings`;
CREATE TABLE `eh_asset_module_app_mappings`(
  `id` BIGINT NOT NULL ,
  `namespace_id` INTEGER NOT NULL ,
  `asset_category_id` BIGINT DEFAULT NULL ,
  `contract_category_id` BIGINT DEFAULT NULL ,
  `energy_category_id` BIGINT DEFAULT NULL ,
  `energy_flag` TINYINT COMMENT '在每个域空间，只有一个energy flag为1，0为不启用',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '2:active; 0:inactive',
  `create_time` DATETIME NOT NULL DEFAULT now(),
  `create_uid` BIGINT NOT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `update_uid` BIGINT DEFAULT NULL,
  UNIQUE KEY `u_asset_category_id`(`asset_category_id`),
  UNIQUE KEY `u_contract_category_id`(`contract_category_id`),
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'relation mappings among applications';









