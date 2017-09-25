--
-- flow 2.0   add by xq.tian  2017/09/28
--
SET @configurations_id = IFNULL((SELECT MAX(id) FROM `eh_configurations`), 1);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ((@configurations_id := @configurations_id + 1), 'flow.stepname.no_step', '自定义', 'no-step', 0, NULL);
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

SET @locale_templates_id = IFNULL((SELECT MAX(id) FROM `eh_locale_templates`), 1);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@locale_templates_id := @locale_templates_id + 1), 'flow', 20001, 'zh_CN', '在 ${nodeName} 执行 ${buttonName}', '工作流通用按钮触发描述', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@locale_templates_id := @locale_templates_id + 1), 'flow', 20002, 'zh_CN', '在 ${nodeName} 转交给 ${transferUser}', '工作流转交按钮触发描述', 0);
