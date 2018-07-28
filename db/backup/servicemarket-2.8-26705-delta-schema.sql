-- 用户端访问权限，即广场上的icon能否点击进去  add by yanjun 20180619
ALTER TABLE `eh_service_modules` ADD COLUMN `access_control_type`  tinyint(4) NULL DEFAULT 1 COMMENT '0-all, 1-logon, 2-auth';

ALTER TABLE `eh_service_module_apps` ADD COLUMN `access_control_type`  tinyint(4) NULL DEFAULT 1 COMMENT '0-all, 1-logon, 2-auth';

ALTER TABLE `eh_launch_pad_items` ADD COLUMN `access_control_type`  tinyint(4) NULL DEFAULT 1 COMMENT '0-all, 1-logon, 2-auth';