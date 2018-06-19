
-- 一些模块设置为“所有用户”或者“仅认证用户”
UPDATE `eh_service_modules` SET access_control_type = 1 where id in(10800, 10800001, 40500, 92100, 10750, 10760, 10100, 10600);
UPDATE `eh_service_modules` SET access_control_type = 3 where id in(50600, 50100, 13000, 41020, 41010, 41000, 20400);