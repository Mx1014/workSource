-- AUTHOR: 缪洲
-- REMARK: issue-34780 企业支付授权应用列表
ALTER TABLE `eh_siyin_print_orders` ADD COLUMN `general_bill_id` VARCHAR(64) NULL DEFAULT NULL COMMENT '统一账单id' ;

ALTER TABLE `eh_service_modules` ADD COLUMN `enable_enterprise_pay_flag`  tinyint(4) NULL COMMENT '企业支付标志，0-否，1-是';
ALTER TABLE `eh_service_module_apps` ADD COLUMN `enable_enterprise_pay_flag`  tinyint(4) NULL COMMENT '企业支付标志，0-否，1-是';

-- AUTHOR: 缪洲
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



-- AUTHOR: 缪洲
-- REMARK: issue-34780 增加企业支付授权页面
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES (79800000, '企业支付授权', 16300000, NULL, 'payment-privileges', 1, 2, '/16000000/16300000/79800000', 'zuolin', 8, 200000, 3, 'system', 'module', NULL, 1);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES (79810000, '企业支付授权', 55000000, NULL, 'payment-privileges', 1, 2, '/40000040/55000000/79810000', 'park', 2, 200000, 3, 'system', 'module', NULL, 1);
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`, `app_type`, `client_handler_type`, `system_app_flag`, `icon_uri`) VALUES (200000, '企业支付授权', 140000, '/200/140000', 1, 3, 2, 10, '2018-09-26 16:51:46', '{}', 13, '2018-09-26 16:51:46', 0, 0, '0', NULL, 'community_control', 1, 1, 'module', NULL, 0, NULL, NULL);

-- AUTHOR: 杨崇鑫 20180930
-- REMARK: 物业缴费V7.1（企业记账流程打通）
-- REMARK：  增加业务对应的相关信息
ALTER TABLE `eh_payment_bills` ADD COLUMN `service_provider_remark1` VARCHAR(1024) COMMENT '服务提供方标识1';
ALTER TABLE `eh_payment_bills` ADD COLUMN `service_provider_remark2` VARCHAR(1024) COMMENT '服务提供方标识2';
ALTER TABLE `eh_payment_bills` ADD COLUMN `service_provider_remark3` VARCHAR(1024) COMMENT '服务提供方标识3';
ALTER TABLE `eh_payment_bills` ADD COLUMN `service_provider_remark4` VARCHAR(1024) COMMENT '服务提供方标识4';
ALTER TABLE `eh_payment_bills` ADD COLUMN `service_provider_remark5` VARCHAR(1024) COMMENT '服务提供方标识5';
ALTER TABLE `eh_payment_bills` ADD COLUMN `service_provider_name` VARCHAR(1024) COMMENT '服务提供方名称';
ALTER TABLE `eh_payment_bills` ADD COLUMN `goods_name` VARCHAR(1024) COMMENT '商品名称';
ALTER TABLE `eh_payment_bills` ADD COLUMN `goods_number` VARCHAR(1024) COMMENT '商品数量';
ALTER TABLE `eh_payment_bills` ADD COLUMN `goods_extend_info` VARCHAR(1024) COMMENT '商品说明';

ALTER TABLE `eh_payment_bill_items` ADD COLUMN `service_provider_remark1` VARCHAR(1024) COMMENT '服务提供方标识1';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `service_provider_remark2` VARCHAR(1024) COMMENT '服务提供方标识2';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `service_provider_remark3` VARCHAR(1024) COMMENT '服务提供方标识3';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `service_provider_remark4` VARCHAR(1024) COMMENT '服务提供方标识4';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `service_provider_remark5` VARCHAR(1024) COMMENT '服务提供方标识5';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `service_provider_name` VARCHAR(1024) COMMENT '服务提供方名称';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_name` VARCHAR(1024) COMMENT '商品名称';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_number` VARCHAR(1024) COMMENT '商品数量';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_extend_info` VARCHAR(1024) COMMENT '商品说明';









