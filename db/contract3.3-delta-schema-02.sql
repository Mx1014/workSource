-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.1（展示能耗数据）
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `energy_consume` VARCHAR(1024) COMMENT '能耗用量';

-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.1 新增账单上传附件
CREATE TABLE `eh_payment_bill_attachments` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id，对应应用的origin_id',
  `owner_id` BIGINT,
  `owner_type` VARCHAR(64),
  `bill_id` BIGINT NOT NULL DEFAULT 0,
  `bill_group_id` BIGINT,
  `filename` VARCHAR(1024) COMMENT '附件名称',
  `content_type` VARCHAR(64) COMMENT '附件类型：word/pdf/png...',
  `content_uri` VARCHAR(1024) COMMENT '附件uri',
  `content_url` VARCHAR(1024) COMMENT '附件url',
  `creator_uid` BIGINT COMMENT '创建者ID',
  `create_time` DATETIME,
  `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- --------------------- SECTION END ---------------------------------------------------------

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
