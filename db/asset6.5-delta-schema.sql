-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 杨崇鑫  20180724
-- REMARK: 物业缴费V6.5所需新增的字段
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `contract_originId` BIGINT(20) COMMENT '合同管理应用的originId';
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `contract_changeFlag` BIGINT(20) COMMENT '是否走合同变更，1、0';

-- --------------------- SECTION END ---------------------------------------------------------


