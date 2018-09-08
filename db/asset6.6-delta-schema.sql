-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.6（对接统一账单） 业务应用与缴费的关联关系表
-- REMARK: 1、contract_category_id字段改名为source_id
ALTER TABLE eh_asset_module_app_mappings CHANGE `contract_category_id` `source_id` BIGINT COMMENT '各个业务系统定义的唯一标识（id）';
-- REMARK: 2、增加相关字段
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `source_type` VARCHAR(1024) COMMENT '各个业务系统定义的唯一标识（类型）' after `source_id`;
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `config` VARCHAR(1024) COMMENT '各个业务系统自定义的JSON配置' after `source_type`;
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `owner_id` BIGINT COMMENT '园区ID' after `config`;
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `owner_type` VARCHAR(64) COMMENT '园区类型' after `owner_id`;
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `bill_group_id` BIGINT COMMENT '账单组ID';
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `charging_item_id` BIGINT COMMENT '费项ID';
-- REMARK: 3、删除无效字段
ALTER TABLE `eh_asset_module_app_mappings` DROP COLUMN `energy_category_id`;
-- REMARK: 4、去掉原来的限制索引
ALTER TABLE eh_asset_module_app_mappings DROP INDEX u_asset_category_id;
ALTER TABLE eh_asset_module_app_mappings DROP INDEX u_contract_category_id;


-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.6（对接统一账单） 账单要增加来源字段
ALTER TABLE `eh_payment_bills` ADD COLUMN `source_type` VARCHAR(1024) COMMENT '各个业务系统定义的唯一标识（类型）';
ALTER TABLE `eh_payment_bills` ADD COLUMN `source_id` BIGINT COMMENT '各个业务系统定义的唯一标识（id）';
ALTER TABLE `eh_payment_bills` ADD COLUMN `source_name` VARCHAR(1024) COMMENT '账单来源（如：停车缴费，缴费的新增/导入等）';
ALTER TABLE `eh_payment_bills` ADD COLUMN `consume_user_id` BIGINT COMMENT '企业下面的某个人的ID';
-- REMARK: 物业缴费V6.6（对接统一账单） 账单费项要增加来源字段
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `source_type` VARCHAR(1024) COMMENT '各个业务系统定义的唯一标识（类型）';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `source_id` BIGINT COMMENT '各个业务系统定义的唯一标识（id）';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `source_name` VARCHAR(1024) COMMENT '账单来源（如：停车缴费，缴费的新增/导入等）';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `consume_user_id` BIGINT COMMENT '企业下面的某个人的ID';















