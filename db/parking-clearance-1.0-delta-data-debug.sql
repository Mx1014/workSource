--
-- 车辆放行模块   add by xq.tian  2016/12/05
--
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`)
VALUES ('41500', '车辆放行', '40000', '/40000/41500', '0', '2', '2', '0', UTC_TIMESTAMP());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES ('10056', '0', '车辆放行 申请放行', '车辆放行 申请放行权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES ('10057', '0', '车辆放行 处理放行任务', '车辆放行 处理放行任务权限', NULL);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 10056, 1001, 0, 1, NOW());
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 10057, 1001, 0, 1, NOW());

SET @eh_service_module_privileges = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges := @eh_service_module_privileges + 1), '41500', '1', '10056', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges := @eh_service_module_privileges + 1), '41500', '1', '10057', NULL, '0', UTC_TIMESTAMP());

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (41500, '车辆放行', 40000, NULL, 'parking_clearance', 1, 2, '/40000/41500', 'park', 390);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (41510, '权限设置', 40000, NULL, 'parking_clearance_privilege', 0, 2, '/40000/41500/41510', 'park', 390);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (41520, '放行记录', 40000, NULL, 'parking_clearance_log', 0, 2, '/40000/41500/41520', 'park', 390);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (41530, '工作流设置', 40000, NULL, 'parking_clearance_flow', 0, 2, '/40000/41500/41530', 'park', 390);

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 41500, '', 'EhNamespaces', 999984, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 41510, '', 'EhNamespaces', 999984, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 41520, '', 'EhNamespaces', 999984, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 41530, '', 'EhNamespaces', 999984, 2);

--
-- 模板
--
SET @eh_locale_templates = (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@eh_locale_templates := @eh_locale_templates + 1), 'parking.clearance', '10001', 'zh_CN', '停车放行申请人看到的内容', '[{"key":"停车场名","value":"${parkingLotName}","entityType":"list"},{"key":"车牌号码","value":"${plateNumber}","entityType":"list"},{"key":"预计来访时间","value":"${clearanceTime}","entityType":"list"},{"key":"备注","value":"${remarks}","entityType":"text"}]', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@eh_locale_templates := @eh_locale_templates + 1), 'parking.clearance', '10002', 'zh_CN', '停车放行处理人看到的内容', '[{"key":"停车场名","value":"${parkingLotName}","entityType":"list"},{"key":"申请人","value":"${applicant}","entityType":"list"},{"key":"手机号","value":"${identifierToken}","entityType":"list"},{"key":"车牌号码","value":"${plateNumber}","entityType":"list"},{"key":"预计来访时间","value":"${clearanceTime}","entityType":"list"},{"key":"备注","value":"${remarks}","entityType":"text"}]', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@eh_locale_templates := @eh_locale_templates + 1), 'parking.clearance', '10003', 'zh_CN', '工作流摘要内容', '车牌号码：${plateNumber}\n来访时间：${clearanceTime}', '0');


