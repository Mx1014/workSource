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
  `category_id` BIGINT NOT NULL DEFAULT 1,
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

-- todo 账单管理设置添加category id
ALTER TABLE `eh_payment_charging_item_scopes` ADD COLUMN `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id';
ALTER TABLE `eh_payment_charging_standards_scopes` ADD COLUMN `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id';
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id';


