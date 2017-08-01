-- 添加审批
INSERT INTO `ehcore`.`eh_community_approve` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `project_id`,
 `approve_name`, `status`, `form_origin_id`, `form_version`, `update_time`, `create_time`) VALUES ('1', '999983', '1008900', '240111044331055940', 'community',
 '41600', 'EhCommunityApprove', '0', '审批测试', '1', '0', '0', NOW(), NOW());

-- 添加菜单 -- 按照产品要求，添加菜单到左邻域

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`,`category`)
VALUES (41600, '园区审批', '40000', NULL, NULL, '1', '2', '/40000/41600', 'park', '499', 41600,'module');

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`,`category`)
VALUES (41610, '表单管理', '41600', NULL, 'react:/park-approval/form-management/form-list/41600', '0', '2', '/40000/41600/41610', 'park', '500', NULL,'module');

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`,`category`)
VALUES (41620, '审批管理', '41600', NULL, 'react:/park-approval/approval-list/41600/EhCommunityApprove', '0', '2', '/40000/41600/41620', 'park', '501', NULL,'module');

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`,`category`)
VALUES (41630, '申请记录', '41600', NULL, 'react:/park-approval/apply-record/41600', '0', '2', '/40000/41600/41630', 'park', '502', NULL,'module');


-- 添加权限
set @eh_acl_privileges_id = (select max(id) from eh_acl_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES ((@eh_acl_privileges_id := @eh_acl_privileges_id+1), 0, '园区审批', '园区审批 全部权限', NULL);

-- 菜单对应的权限
SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, 41400, '园区审批', 1, 1, '园区审批  全部权限', 499);

INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, 41410, '表单管理', 1, 1, '表单管理  全部权限', 500);

INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, 41420, '审批管理', 1, 1, '审批管理  全部权限', 501);

INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, 41430, '申请记录', 1, 1, '打印价格  全部权限', 502);

-- 角色对应的菜单权限
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 999983, 'EhOrganizations', NULL, 1, @eh_acl_privileges_id, 1001, 'EhAclRoles', 0, 1, NOW());

-- 菜单的范围 --0域不加scope
 SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
 INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
 VALUES ((@menu_scope_id := @menu_scope_id + 1), 41600, '', 'EhNamespaces', 999983, 2);
 INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
 VALUES ((@menu_scope_id := @menu_scope_id + 1), 41610, '', 'EhNamespaces', 999983, 2);
 INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
 VALUES ((@menu_scope_id := @menu_scope_id + 1), 41620, '', 'EhNamespaces', 999983, 2);
 INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
 VALUES ((@menu_scope_id := @menu_scope_id + 1), 41630, '', 'EhNamespaces', 999983, 2);

-- 模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`)
VALUES (41600, '园区审批', 40000, '/40000/41600', 0, 2, 2, 0, UTC_TIMESTAMP());

-- 模块权限
SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), 41600, 1, @eh_acl_privileges_id, NULL, '0', UTC_TIMESTAMP());

-- 模块权限范围
SET @eh_service_module_scopes_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999983, 41600, '园区审批', NULL, NULL, NULL, 2);

-- by dengs 添加菜单 end

-- 服务广场 -- 添加到左邻域
 SET @eh_launch_pad_items_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
 INSERT INTO `eh_launch_pad_items`
(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`,
 `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, 
`min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, 
`scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 999983, 0, 0, 0, '/home', 'Bizs', '园区审批测试', '园区审批测试',
'', 1, 1, 14, '{"url":"zl://form/create?sourceType=COMMUNITY_APPROVE&sourceId=1&ownerType=EhcommunityApprove&ownerId=1008900&displayName=审批测试&metaObject="}', 3, 0,
 1, 1, '', 0, NULL, NULL, NULL, 0,
 'park_tourist', 1, NULL, NULL, 0, NULL);
 
 INSERT INTO `eh_launch_pad_items` 
(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`,
 `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, 
`min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, 
`scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 999983, 0, 0, 0, '/home', 'Bizs', '园区审批测试', '园区审批测试',
'', 1, 1, 14, '{"url":"zl://form/create?sourceType=COMMUNITY_APPROVE&sourceId=1&ownerType=EhcommunityApprove&ownerId=1008900&displayName=审批测试&metaObject="}', 3, 0,
 1, 1, '', 0, NULL, NULL, NULL, 0,
 'pm_admin', 1, NULL, NULL, 0, NULL);
