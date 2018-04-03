-- 黑名单权限 
insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) values('30550','子项目管理','30000',NULL,'react:/child-project/project-list/30550','0','2','/30000/30550','park',306,30550);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10091, 0, '子项目管理', '子项目 管理员权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10091, 30550, '子项目管理', 1, 1, '子项目 管理员权限', 306);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 10091, 1001,'EhAclRoles', 0, 1, NOW());

DELETE FROM `eh_service_module_scopes` WHERE `module_id` in (10000, 20000, 40000);
SET @service_module_scope_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) 
SELECT (@service_module_scope_id := @service_module_scope_id + 1), id, 10000, '', NULL, NULL, NULL, '2' FROM `eh_namespaces`; 

INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) 
SELECT (@service_module_scope_id := @service_module_scope_id + 1), id, 20000, '', NULL, NULL, NULL, '2' FROM `eh_namespaces`; 

INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) 
SELECT (@service_module_scope_id := @service_module_scope_id + 1), id, 40000, '', NULL, NULL, NULL, '2' FROM `eh_namespaces`; 