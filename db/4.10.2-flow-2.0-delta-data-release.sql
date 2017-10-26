--
-- flow 2.0   add by xq.tian  2017/09/28
--
SET @configurations_id = IFNULL((SELECT MAX(id) FROM `eh_configurations`), 1);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ((@configurations_id := @configurations_id + 1), 'flow.stepname.no_step', '自定义', 'no-step', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ((@configurations_id := @configurations_id + 1), 'flow.stepname.supervise', '督办', 'supervise', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ((@configurations_id := @configurations_id + 1), 'flow.stepname.go_to_process', '去处理', 'go_to_process', 0, NULL);


INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
  VALUES (70300, '我的待办', 70000, NULL, 'react:/task-management/todo-list/70300', 0, 2, '/50000/70000/70300', 'park', 610, 13000, 3, NULL, 'module');

SET @acl_privileges_id = IFNULL((SELECT MAX(id) FROM `eh_acl_privileges`), 1);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES ((@acl_privileges_id := @acl_privileges_id + 1), 0, '我的待办', '我的待办 全部权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @acl_privileges_id, 70300, '我的待办', 1, 1, '我的待办 全部权限', 202);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @acl_privileges_id, 1001, 'EhAclRoles', 0, 1, NOW());

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 70300, '', 'EhNamespaces', 1000000, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 70300, '', 'EhNamespaces', 999992, 2);


SET @locale_templates_id = IFNULL((SELECT MAX(id) FROM `eh_locale_templates`), 1);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@locale_templates_id := @locale_templates_id + 1), 'flow', 20001, 'zh_CN', '工作流通用按钮触发描述在', '在 ${nodeName} 执行 ${buttonName}', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@locale_templates_id := @locale_templates_id + 1), 'flow', 20003, 'zh_CN', '任务超时 已取消任务', '任务超时 已取消任务', 0);

SET @locale_strings_id = IFNULL((SELECT MAX(id) FROM `eh_locale_strings`), 1);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@locale_strings_id := @locale_strings_id + 1), 'flow', '100012', 'zh_CN', '泳道不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@locale_strings_id := @locale_strings_id + 1), 'flow', '100013', 'zh_CN', '节点链接为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@locale_strings_id := @locale_strings_id + 1), 'flow', '100014', 'zh_CN', '需要指定下一步节点，请升级App');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@locale_strings_id := @locale_strings_id + 1), 'flow', '100015', 'zh_CN', '驳回失败');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@locale_strings_id := @locale_strings_id + 1), 'flow', '100016', 'zh_CN', '获取详情失败');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@locale_strings_id := @locale_strings_id + 1), 'flow', '20004', 'zh_CN', '${text_tracker_curr_node_name} 已完成');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@locale_strings_id := @locale_strings_id + 1), 'flow', '20005', 'zh_CN', '任务已被 ${text_tracker_curr_processors_name} 驳回');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@locale_strings_id := @locale_strings_id + 1), 'flow', '20006', 'zh_CN', '你有1个 ${text_button_msg_flow_case_title} 任务尚未处理');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@locale_strings_id := @locale_strings_id + 1), 'flow', '20007', 'zh_CN', '你有1个任务尚未处理');

SET @flow_variables_id = IFNULL((SELECT MAX(id) FROM `eh_flow_variables`), 1);
INSERT INTO `eh_flow_variables` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
  VALUES ((@flow_variables_id := @flow_variables_id + 1), 0, 0, '', 0, '', 'text_tracker_curr_node_name', '本节点名称', 'text_tracker', 'bean_id', 'flow-variable-text-tracker-curr-node-name', 1);
INSERT INTO `eh_flow_variables` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
  VALUES ((@flow_variables_id := @flow_variables_id + 1), 0, 0, '', 0, '', 'text_tracker_curr_node_name', '本节点名称', 'text_button_msg', 'bean_id', 'flow-variable-text-tracker-curr-node-name', 1);
INSERT INTO `eh_flow_variables` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
  VALUES ((@flow_variables_id := @flow_variables_id + 1), 0, 0, '', 0, '', 'text_tracker_curr_node_name', '本节点名称', 'text_remind', 'bean_id', 'flow-variable-text-tracker-curr-node-name', 1);
INSERT INTO `eh_flow_variables` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
  VALUES ((@flow_variables_id := @flow_variables_id + 1), 0, 0, '', 0, '', 'text_button_msg_flow_case_title', '工作流标题', 'text_hidden', 'bean_id', 'text-hidden-button-msg-flow-case-title', 1);
INSERT INTO `eh_flow_variables` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
  VALUES ((@flow_variables_id := @flow_variables_id + 1), 0, 0, '', 0, '', 'all_current_node_processors', '所有任务的当前处理人', 'flow_var_hidden', 'bean_id', 'flow-variable-hidden-button-msg-all-current-processors', 1);
INSERT INTO `eh_flow_variables` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
  VALUES ((@flow_variables_id := @flow_variables_id + 1), 0, 0, '', 0, '', 'text_tracker_curr_operator_name', '操作执行人姓名', 'text_tracker', 'bean_id', 'flow-variable-curr-processor-name', 1);

-- 为张江高科增加客户资料菜单 by xiongying20171024
SET @menu_scope_id = IFNULL((SELECT MAX(id) FROM `eh_web_menu_scopes`), 1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '37000', '', 'EhNamespaces', '999971', '2');

-- add by sw 物业报修 资源预订 停车 节点配置值 20171024
SET @ns_id = 0;
SET @flow_predefined_params_id = IFNULL((SELECT MAX(id) FROM `eh_flow_predefined_params`), 1);

INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
	VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', 20100, 'any-module', 'flow_node', '待受理', '待受理', '{"nodeType":"ACCEPTING"}', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
	VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', 20100, 'any-module', 'flow_node', '待分配', '待分配', '{"nodeType":"ASSIGNING"}', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
	VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', 20100, 'any-module', 'flow_node', '待处理', '待处理', '{"nodeType":"PROCESSING"}', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
	VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', 20100, 'any-module', 'flow_node', '已完成', '已完成', '{"nodeType":"COMPLETED"}', 2, NULL, NULL, NULL, NULL);

INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
	VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', 40400, 'any-module', 'flow_node', '待审批', '待审批', 'agree', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
	VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', 40400, 'any-module', 'flow_node', '待付款', '待付款', 'unpaid', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
	VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', 40400, 'any-module', 'flow_node', '已预约', '已预约', 'paid', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
	VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', 40400, 'any-module', 'flow_node', '已完成', '已完成', 'complete', 2, NULL, NULL, NULL, NULL);

INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
	VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', 40800, 'any-module', 'flow_node', '待审核', '待审核', '{"nodeType":"AUDITING"}', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
	VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', 40800, 'any-module', 'flow_node', '排队中', '排队中', '{"nodeType":"QUEUEING"}', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
	VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', 40800, 'any-module', 'flow_node', '待处理', '待处理', '{"nodeType":"PROCESSING"}', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
	VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', 40800, 'any-module', 'flow_node', '办理成功', '办理成功', '{"nodeType":"SUCCEED"}', 2, NULL, NULL, NULL, NULL);
