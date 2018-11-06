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



