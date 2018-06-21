

-- 0-all, 1-logon, 2-auth
-- 一些模块设置为“所有用户”或者“仅认证用户”
UPDATE `eh_service_modules` SET access_control_type = 1;
-- 全部
UPDATE `eh_service_modules` SET access_control_type = 0 where id in(10800, 10800001, 40500, 92100, 10750, 10760, 10100, 10600);
-- 认证
UPDATE `eh_service_modules` SET access_control_type = 2 where id in(50600, 50100, 13000, 41020, 41010, 41000, 20400);


UPDATE `eh_service_module_apps` SET access_control_type = 1;
-- 全部
UPDATE `eh_service_module_apps` SET access_control_type = 0 where module_id in(10800, 10800001, 40500, 92100, 10750, 10760, 10100, 10600);
-- 认证
UPDATE `eh_service_module_apps` SET access_control_type = 2 where module_id in(50600, 50100, 13000, 41020, 41010, 41000, 20400);


UPDATE  eh_launch_pad_items  SET access_control_type = 0;