-- 发公告权限 add by sfyan 20170329
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10155, '0', '发公告权限', '发公告权限', NULL);
SET @service_module_privilege_id = (SELECT max(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@service_module_privilege_id := @service_module_privilege_id + 1), '10100', '0', '10155',  '发公告权限', '0', UTC_TIMESTAMP());

SET @menu_privilege_id = (SELECT max(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `menu_id`, `show_flag`, `privilege_id`, `name`, `status`, `discription`, `sort_num`) VALUES ((@menu_privilege_id := @menu_privilege_id + 1), '10100', '1', '10155',  '发公告权限', 1,'发公告权限', 1);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10155', '1001', '0', '1', UTC_TIMESTAMP());

-- 用户管理 add by sw 20170329
delete from eh_web_menus where id = 34000;
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
	VALUES ('34000', '用户管理', '30000', NULL, 'user--user_management/1', '0', '2', '/30000/34000', 'park', '340', '34000');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
	VALUES ('34100', '用户管理', '30000', NULL, 'user--user_management/0', '0', '2', '/30000/34100', 'park', '340', '34000');
