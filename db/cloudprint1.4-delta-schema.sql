-- AUTHOR: 缪洲
-- REMARK: issue-34780 企业支付授权列表
ALTER TABLE `eh_siyin_print_orders` ADD COLUMN `general_bill_id` VARCHAR(64) NULL DEFAULT NULL COMMENT '统一账单id' ;

ALTER TABLE `eh_service_modules` ADD COLUMN `enable_enterprise_pay_flag`  tinyint(4) NULL COMMENT '企业支付标志，0-否，1-是';
ALTER TABLE `eh_service_module_apps` ADD COLUMN `enable_enterprise_pay_flag`  tinyint(4) NULL COMMENT '企业支付标志，0-否，1-是';