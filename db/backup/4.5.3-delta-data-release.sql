-- 解决邮箱验证不通过的问题，bug号#10051，吕磊
UPDATE eh_configurations set `value` = 'webmail.zuolin.com' WHERE `name` = 'mail.smtp.address' and `namespace_id` = 1000000;
UPDATE eh_configurations set `value` = '465' WHERE `name` = 'mail.smtp.port' and `namespace_id` = 1000000;

-- 楼栋名称修改 光大We谷产业园 add by sfyan 20170522
UPDATE `eh_buildings` SET `name` = '光大We谷产业园2栋4号楼' WHERE `name` = '光大We谷产业园2栋2号楼' AND `community_id` = 240111044331056800;
UPDATE `eh_buildings` SET `name` = '光大We谷产业园2栋3号楼' WHERE `name` = '光大We谷产业园2栋1号楼' AND `community_id` = 240111044331056800;

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 35000, '', 'EhNamespaces', 1000000, 2); 
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 30600, '', 'EhNamespaces', 999983, 2); 

SET @eh_service_module_scopes_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 1000000, 35000, null, NULL, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999983, 30600, null, NULL, 2);
