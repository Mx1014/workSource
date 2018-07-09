-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 杨崇鑫
-- REMARK: 根据域空间判断是否展示能耗数据，测试环境初始化鼎峰汇支持展示能耗数据
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`) VALUES ( 'asset.showEnergy', '1', '1代表展示，0代表不展示', '999951');
-- --------------------- SECTION END ---------------------------------------------------------


