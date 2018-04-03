--
-- 车辆放行模块   add by xq.tian  2016/12/05
--
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`)
VALUES ('20900', '车辆放行', '20000', '/20000/20900', '0', '2', '2', '0', UTC_TIMESTAMP());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES ('10056', '0', '车辆放行 申请放行', '车辆放行 申请放行权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES ('10057', '0', '车辆放行 处理放行任务', '车辆放行 处理放行任务权限', NULL);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `role_type`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 10056, 1001, 0, 'EhAclRoles', 1, NOW());
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `role_type`,  `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 10057, 1001, 0, 'EhAclRoles', 1, NOW());

SET @eh_service_module_privileges = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges := @eh_service_module_privileges + 1), '20900', '1', '10056', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges := @eh_service_module_privileges + 1), '20900', '1', '10057', NULL, '0', UTC_TIMESTAMP());

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10056, 20900, '车辆放行', 1, 1, '车辆放行  全部权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10057, 20900, '车辆放行', 1, 1, '车辆放行  全部权限', 202);

INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10056, 20910, '车辆放行', 1, 1, '车辆放行  全部权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10057, 20910, '车辆放行', 1, 1, '车辆放行  全部权限', 202);

INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10056, 20920, '车辆放行', 1, 1, '车辆放行  全部权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10057, 20920, '车辆放行', 1, 1, '车辆放行  全部权限', 202);

INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10056, 20930, '车辆放行', 1, 1, '车辆放行  全部权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10057, 20930, '车辆放行', 1, 1, '车辆放行  全部权限', 202);

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (20900, '车辆放行', 20000, NULL, 'parking_clearance', 1, 2, '/20000/20900', 'park', 300);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (20910, '权限设置', 20900, NULL, 'vehicle_setting', 0, 2, '/20000/20900/20910', 'park', 301);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (20920, '放行记录', 20900, NULL, 'release_record', 0, 2, '/20000/20900/20920', 'park', 302);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (20930, '工作流设置', 20900, NULL, 'react:/working-flow/flow-list/vehicle-release/20900', 0, 2, '/20000/20900/20930', 'park', 303);

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 20900, '', 'EhNamespaces', 999984, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 20910, '', 'EhNamespaces', 999984, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 20920, '', 'EhNamespaces', 999984, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 20930, '', 'EhNamespaces', 999984, 2);

--
-- 模板
--
SET @eh_locale_templates = (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@eh_locale_templates := @eh_locale_templates + 1), 'parking.clearance', '1', 'zh_CN', '停车放行申请人看到的内容', '[{"key":"停车场名","value":"${parkingLotName}","entityType":"list"},{"key":"车牌号码","value":"${plateNumber}","entityType":"list"},{"key":"预计来访时间","value":"${clearanceTime}","entityType":"list"},{"key":"备注","value":"${remarks}","entityType":"text"}]', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@eh_locale_templates := @eh_locale_templates + 1), 'parking.clearance', '2', 'zh_CN', '停车放行处理人看到的内容', '[{"key":"停车场名","value":"${parkingLotName}","entityType":"list"},{"key":"申请人","value":"${applicant}","entityType":"list"},{"key":"手机号","value":"${identifierToken}","entityType":"list"},{"key":"车牌号码","value":"${plateNumber}","entityType":"list"},{"key":"预计来访时间","value":"${clearanceTime}","entityType":"list"},{"key":"备注","value":"${remarks}","entityType":"text"}]', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@eh_locale_templates := @eh_locale_templates + 1), 'parking.clearance', '3', 'zh_CN', '工作流摘要内容', '车牌号码：${plateNumber}\n来访时间：${clearanceTime}', '0');

SET @eh_locale_strings = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance', '1', 'zh_CN', '无');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance', '2', 'zh_CN', '对不起,您没有权限申请放行');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance', '3', 'zh_CN', '对不起,您没有权限处理放行任务');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance', '10001', 'zh_CN', '删除用户失败');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance', '10002', 'zh_CN', '没有启用的工作流');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance', '10003', 'zh_CN', '用户已添加');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance.log.status', '1', 'zh_CN', '处理中');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance.log.status', '2', 'zh_CN', '已完成');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance.log.status', '3', 'zh_CN', '已取消');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance.log.status', '4', 'zh_CN', '待处理');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parameters.error', '10001', 'zh_CN', '参数长度超过限制');

SELECT max(id) FROM `eh_launch_pad_items` INTO @launch_pad_item_id;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)

VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), '999984', '0', '0', '0', '/home', 'Bizs', 'PARKING_CLEARANCE', '车辆放行', 'cs://1/image/aW1hZ2UvTVRvME5tTXpZVEEwWlRKa1lqQXlaVFEwTmpkaU5XRTJORGN5WVdJM056QmpZUQ', '1', '1', '57', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '1', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), '999984', '0', '0', '0', '/home', 'Bizs', 'PARKING_CLEARANCE_TASK', '放行任务', 'cs://1/image/aW1hZ2UvTVRvME5tTXpZVEEwWlRKa1lqQXlaVFEwTmpkaU5XRTJORGN5WVdJM056QmpZUQ', '1', '1', '58', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '1', NULL);

--
-- 修改没有权限时的提示语  add by xq.tian  2016/12/21
--
UPDATE `eh_locale_strings` SET `text`='对不起,您没有权限执行此操作' WHERE (`scope`='general' AND `code`='505');