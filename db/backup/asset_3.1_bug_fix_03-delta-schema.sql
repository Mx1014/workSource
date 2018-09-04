-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 唐岑
-- REMARK: 资产管理V3.1
ALTER TABLE `eh_addresses` ADD COLUMN `building_id` bigint(20) NULL DEFAULT NULL COMMENT '房源所在楼宇id' AFTER `community_id`;

ALTER TABLE `eh_addresses` ADD COLUMN `community_name` varchar(64) NULL DEFAULT NULL COMMENT '房源所在园区名称' AFTER `community_id`;
-- --------------------- SECTION END ------------------------------------------------------------------------------