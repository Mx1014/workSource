-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 杨崇鑫  20180724
-- REMARK: 物业缴费V6.5所需新增的字段
-- REMARK: 修改域空间发布保存相关应用配置
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `contract_originId` BIGINT(20) COMMENT '合同管理应用的originId';
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `contract_changeFlag` TINYINT COMMENT '是否走合同变更，1、0';
-- REMARK: 修改域空间发布保存相关应用配置
ALTER TABLE `eh_payment_charging_item_scopes` ADD COLUMN `tax_rate` DECIMAL(10,2) COMMENT '税率';
-- REMARK: 账单表：增加应收不含税字段,税额字段：tax_amount
ALTER TABLE `eh_payment_bills` ADD COLUMN `amount_receivable_without_tax` DECIMAL(10,2) COMMENT '应收（不含税）' after amount_receivable;
ALTER TABLE `eh_payment_bills` ADD COLUMN `amount_received_without_tax` DECIMAL(10,2) COMMENT '已收（不含税）' after amount_received;
ALTER TABLE `eh_payment_bills` ADD COLUMN `amount_owed_without_tax` DECIMAL(10,2) COMMENT '待收（不含税）' after amount_owed;
ALTER TABLE `eh_payment_bills` ADD COLUMN `tax_amount` DECIMAL(10,2) COMMENT '税额' after amount_receivable_without_tax;

ALTER TABLE `eh_payment_bill_items` ADD COLUMN `amount_receivable_without_tax` DECIMAL(10,2) COMMENT '应收（不含税）' after amount_receivable;
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `amount_received_without_tax` DECIMAL(10,2) COMMENT '已收（不含税）' after amount_received;
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `amount_owed_without_tax` DECIMAL(10,2) COMMENT '待收（不含税）' after amount_owed;
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `tax_amount` DECIMAL(10,2) COMMENT '税额' after amount_receivable_without_tax;
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `tax_rate` DECIMAL(10,2) COMMENT '税率' after tax_amount;

-- --------------------- SECTION END ---------------------------------------------------------


