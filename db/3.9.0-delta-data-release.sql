-- 屏蔽任务菜单 by sfyan 20160902
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
-- 任务列表
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 24000,'', 'EhNamespaces', 0 , 0);
-- 服务类型设置
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 25000,'', 'EhNamespaces', 0 , 0);
-- 分类设置
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 26000,'', 'EhNamespaces', 0 , 0);
-- 统计
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 27000,'', 'EhNamespaces', 0 , 0);



