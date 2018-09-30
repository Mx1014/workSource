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



-- AUTHOR: 杨崇鑫 20180930
-- REMARK: 物业缴费V7.1（企业记账流程打通）
-- REMARK: 删除上个版本遗留的弃用字段
ALTER TABLE `eh_asset_module_app_mappings` DROP COLUMN `energy_flag`;
ALTER TABLE `eh_asset_module_app_mappings` DROP COLUMN `contract_originId`;
ALTER TABLE `eh_asset_module_app_mappings` DROP COLUMN `contract_changeFlag`;

-- REMARK：  统一账单加入的：统一订单定义的唯一标识
ALTER TABLE `eh_payment_bills` ADD COLUMN `merchant_order_id` VARCHAR(128) COMMENT '统一账单加入的：统一订单定义的唯一标识';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `merchant_order_id` VARCHAR(128) COMMENT '统一账单加入的：统一订单定义的唯一标识';

-- REMARK：  增加业务对应的相关信息
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
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `goods_counts` BIGINT COMMENT '商品数量';
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
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `goods_tag` VARCHAR(1024) COMMENT '商品标识，如：活动ID、商品ID';
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `goods_name` VARCHAR(1024) COMMENT '商品名称';
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `goods_description` VARCHAR(1024) COMMENT '商品说明';
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `goods_counts` BIGINT COMMENT '商品数量';
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `goods_price` DECIMAL(10,2) COMMENT '商品单价';
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `goods_totalPrice` DECIMAL(10,2) COMMENT '商品总金额';


