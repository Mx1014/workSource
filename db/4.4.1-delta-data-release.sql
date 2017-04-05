-- 2017-03-31 19:17 add by yanjun
insert into `eh_locale_strings` (`scope`, `code`, `locale`, `text`) values('activity','11','zh_CN','活动时间：');

-- 全志添加 文件管理菜单 add by sw 20170405
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (41500, '文件管理', 40000, NULL, 'react:/file-management/file-upload/41500', 0, 2, '/40000/41500', 'park', 390);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10135, 0, '文件管理', '文件管理 全部权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10135, 41500, '文件管理', 1, 1, '文件管理  全部权限', 202);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, 10135, 1001, 'EhAclRoles', 0, 1, NOW());

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 41500, '', 'EhNamespaces', 999980, 2);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`)
VALUES (41500, '文件管理', 40000, '/40000/41500', 0, 2, 2, 0, UTC_TIMESTAMP());

SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), 41500, 1, 10135, NULL, '0', UTC_TIMESTAMP());

SET @eh_service_module_scopes_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999980, 41500, '文件管理', 'EhNamespaces', 999980, NULL, 2);

