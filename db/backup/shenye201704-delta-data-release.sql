update eh_equipment_inspection_tasks t set t.namespace_id = (select o.namespace_id from eh_organizations o where o.id = t.owner_id);
update eh_equipment_inspection_item_results r set r.standard_id = (select t.standard_id from eh_equipment_inspection_tasks t where t.id = r.task_id);
update eh_equipment_inspection_item_results r set r.equipment_id = (select t.equipment_id from eh_equipment_inspection_tasks t where t.id = r.task_id);
update eh_equipment_inspection_item_results r set r.community_id = (select t.target_id from eh_equipment_inspection_tasks t where t.id = r.task_id);
update eh_equipment_inspection_item_results r set r.inspection_category_id =(select t.inspection_category_id from eh_equipment_inspection_tasks t where t.id = r.task_id);
update eh_equipment_inspection_item_results r set r.namespace_id = (select t.namespace_id from eh_equipment_inspection_tasks t where t.id = r.task_id);

update eh_equipment_inspection_task_logs r set r.community_id = (select t.target_id from eh_equipment_inspection_tasks t where t.id = r.task_id);
update eh_equipment_inspection_task_logs r set r.inspection_category_id = (select t.inspection_category_id from eh_equipment_inspection_tasks t where t.id = r.task_id);
update eh_equipment_inspection_task_logs r set r.namespace_id = (select t.namespace_id from eh_equipment_inspection_tasks t where t.id = r.task_id);


update eh_web_menus set data_type = 'react:/equipment-inspection/equipment-list' where name like '巡检对象';
update eh_web_menus set data_type = '' where id = 20850;

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`) 
VALUES (20851, '总览', 20850, NULL, 'react:/equipment-inspection/statistics-pandect', 0, 2, '/20000/20800/20850/20851', 'park', 310);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`) 
VALUES (20852, '查看所有任务', 20850, NULL, 'react:/equipment-inspection/statistics-task', 0, 2, '/20000/20800/20850/20852', 'park', 311);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) 
VALUES (20030, 0, '总览', '总览', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) 
VALUES (20031, 0, '巡检统计查看所有任务', '巡检统计查看所有任务', NULL);
SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) 
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 20030, 20851, '总览', 1, 1, '总览', 310); 
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) 
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 20031, 20852, '查看所有任务', 1, 1, '查看所有任务', 311); 

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 20851, '', 'EhNamespaces', 999992, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 20852, '', 'EhNamespaces', 999992, 2);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) 
VALUES ('20851', '总览', '20850', '/20000/20800/20850/20851', '0', '2', '2', '0', UTC_TIMESTAMP()); 
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) 
VALUES ('20852', '查看所有任务', '20850', '/20000/20800/20850/20852', '0', '2', '2', '0', UTC_TIMESTAMP()); 

SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), '20851', '1', '20030', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), '20852', '1', '20031', NULL, '0', UTC_TIMESTAMP());

SET @eh_service_module_scopes_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999992, 20851, '总览', 'EhNamespaces', 999992, NULL, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999992, 20852, '查看所有任务', 'EhNamespaces', 999992, NULL, 2);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, 20030, 1005, 'EhAclRoles', 0, 1, NOW()); 
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, 20030, 1001, 'EhAclRoles', 0, 1, NOW()); 
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, 20031, 1005, 'EhAclRoles', 0, 1, NOW()); 
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, 20031, 1001, 'EhAclRoles', 0, 1, NOW()); 

INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES ('1238', '10011', '20851', '设备巡检', '1', '1', '设备巡检 管理员权限', '710');
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES ('1238', '10011', '20852', '设备巡检', '1', '1', '设备巡检 管理员权限', '710');
