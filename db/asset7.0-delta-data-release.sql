-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境

-- AUTHOR:唐岑2018年10月23日14:29:41
-- REMARK:添加资产报表定时配置
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('schedule.property.task.time', '0 30 2 * * ?', '资产报表定时任务', '0', NULL, '1');

-- end
-- --------------------- SECTION END ALL -----------------------------------------------------