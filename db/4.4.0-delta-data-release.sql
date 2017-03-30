-- 发公告权限 add by sfyan 20170329
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10155, '0', '发公告权限', '发公告权限', NULL);
SET @service_module_privilege_id = (SELECT max(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@service_module_privilege_id := @service_module_privilege_id + 1), '10100', '0', '10155',  '发公告权限', '0', UTC_TIMESTAMP());

SET @menu_privilege_id = (SELECT max(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `menu_id`, `show_flag`, `privilege_id`, `name`, `status`, `discription`, `sort_num`) VALUES ((@menu_privilege_id := @menu_privilege_id + 1), '10100', '1', '10155',  '发公告权限', 1,'发公告权限', 1);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10155', '1001', '0', '1', UTC_TIMESTAMP());


-- 能耗管理 add by xiongying 20170329
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('energy', '10015', 'zh_CN', '单价方案已被引用，不可删除');

-- 更新用户管理菜单 add by sw 20170330
update eh_web_menus set data_type = 'user--user_identify' where id = 35000;
update eh_web_menus set data_type = 'user--user_management' where id = 34000;
