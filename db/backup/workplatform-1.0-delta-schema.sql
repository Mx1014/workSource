
-- 模块对应的菜单是否需要授权, add by yanjun 20180517
ALTER TABLE `eh_service_modules` ADD COLUMN `menu_auth_flag`  tinyint(4) NOT NULL DEFAULT 1 COMMENT 'if its menu need auth' ;

ALTER TABLE `eh_service_modules` ADD COLUMN `category`  varchar(255) NULL COMMENT 'classify, module, subModule';
