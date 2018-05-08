--
-- 一键推送的数据范围改成不限园区  add by xq.tian  2018/04/26
--
UPDATE eh_service_modules SET module_control_type = 'unlimit_control' WHERE name = '一键推送';