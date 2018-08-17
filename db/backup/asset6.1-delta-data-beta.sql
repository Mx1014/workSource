-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 杨崇鑫
-- REMARK: 根据域空间判断是否展示能耗数据，测试环境初始化鼎峰汇支持展示能耗数据
delete from eh_service_module_exclude_functions where namespace_id=999951 and module_id=20400 and function_id=101;-- 后台

SET @id = ifnull((SELECT MAX(id) FROM `eh_payment_app_views`),0); -- APP
INSERT INTO `eh_payment_app_views`(`id`, `namespace_id`, `community_id`, `has_view`, `view_item`, `remark1_type`, `remark1_identifier`, `remark2_type`, `remark2_identifier`, `remark3_type`, `remark3_identifier`) 
VALUES (@id := @id + 1, 999951, NULL, 1, 'ENERGY', NULL, NULL, NULL, NULL, NULL, NULL);

-- --------------------- SECTION END ---------------------------------------------------------


