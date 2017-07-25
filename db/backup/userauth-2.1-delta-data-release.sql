--
-- 修改用户认证的菜单data_type
--
UPDATE `eh_web_menus` SET `data_type` = 'react:/identification-management/user-identification' WHERE `id` = 35000;
UPDATE `eh_web_menus` SET `name` = '员工认证', `data_type` = 'react:/identification-management/employee-identification' WHERE `id` = 50500;