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
  `source_id` BIGINT NOT NULL COMMENT '授权用户id',
  `source_type` VARCHAR(32) NOT NULL COMMENT '用户类型',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `update_time` DATETIME NULL COMMENT '记录创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='企业支付授权表';

-- 增加商户管理模块及菜单 by st.zheng
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES ('210000', '商户管理', '170000', '/200/170000/210000', '1', '3', '2', '110', '2018-03-19 17:52:57', NULL, NULL, '2018-03-19 17:53:11', '0', '0', '0', '0', 'community_control', '1', '1', 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('79820000', '商户管理', '16400000', NULL, 'business-management', '1', '2', '/16000000/16400000/79820000', 'zuolin', '120', '210000', '3', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('79830000', '商户管理', '56000000', NULL, 'business-management', '1', '2', '/40000040/56000000/79830000', 'park', '120', '210000', '3', 'system', 'module', '2');
