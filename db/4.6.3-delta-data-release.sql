
-- 增加“真实姓名无效”等的错误提示   add by yanjun 20170628
SET @id = (SELECT MAX(id) FROM eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES(@id := @id + 1, 'activity','10028','zh_CN','呃，真实姓名无效');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES(@id := @id + 1, 'activity','10029','zh_CN','呃，手机号无效');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES(@id := @id + 1, 'activity','10030','zh_CN','呃，无效的报名信息');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES(@id := @id + 1, 'activity','10031','zh_CN','呃，报名信息已经存在，请勿重新报名');

-- merge from quality201706 by xionying
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) VALUES ('20655', '绩效考核', '20600', '', 'react:/sample-task/task-list', '0', '2', '/20000/20600/20655', 'park', '268', '20600');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) VALUES ('20663', '检查统计', '20660', '', 'react:/statistics-management/sample-report', '0', '2', '/20000/20600/20660/20663', 'park', '271', '20600');


SET @scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id := @scope_id + 1), '20655', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id := @scope_id + 1), '20663', '', 'EhNamespaces', '999992', '2');

SET @privilege_id = (SELECT MAX(id) FROM `eh_acl_privileges`);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES ((@privilege_id := @privilege_id + 1), 0, '绩效考核', '绩效考核', NULL);
SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @privilege_id, 20655, '绩效考核', 1, 1, '绩效考核', 268);
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1001, 'EhAclRoles', 0, 1, NOW());
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1005, 'EhAclRoles', 0, 1, NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES ((@privilege_id := @privilege_id + 1), 0, '检查统计', '检查统计', NULL);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @privilege_id, 20663, '检查统计', 1, 1, '检查统计', 271);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1001, 'EhAclRoles', 0, 1, NOW());
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1005, 'EhAclRoles', 0, 1, NOW());

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('quality', '10017', 'zh_CN', '检查不存在或已过截止日期');

-- 工作流添加按钮消息，节点消息的发起人企业管理员变量  add by xq.tian  2017/06/29
SET @max_flow_var_id = (SELECT max(id) FROM `eh_flow_variables`);
INSERT INTO `eh_flow_variables` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ((@max_flow_var_id := @max_flow_var_id + 1), 0, 0, '', 0, '', 'user_applier_organization_manager', '发起人的企业管理员', 'node_user_button_msg', 'bean_id', 'flow-variable-applier-organization-manager', 1);
INSERT INTO `eh_flow_variables` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ((@max_flow_var_id := @max_flow_var_id + 1), 0, 0, '', 0, '', 'user_applier_organization_manager', '发起人的企业管理员', 'node_user_remind', 'bean_id', 'flow-variable-applier-organization-manager', 1);

-- added by janson
DELETE from `eh_configurations` where `namespace_id`=1000000 and `name`= 'aclink.join_company_auto_auth' limit 2;
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (1000000, 'aclink.join_company_auto_auth', 'building,176121,E4:BC:E3:46:21:FF;company,1000001,E4:BC:E3:46:21:FF', '为生产力大楼自动授权');
