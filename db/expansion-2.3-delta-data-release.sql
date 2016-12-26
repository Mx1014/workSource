--
-- 园区入驻 2.3  add by xq.tian  2016/12/20
--
SET @eh_locale_templates = (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@eh_locale_templates := @eh_locale_templates + 1), 'expansion', '1', 'zh_CN', '园区入驻工作流摘要内容', '申请类型: ${applyType}\n面积需求: ${areaSize} 平米', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@eh_locale_templates := @eh_locale_templates + 1), 'expansion', '2', 'zh_CN', '园区入驻工作流详情内容', '[{"key":"发起人","value":"${applyUserName}","entityType":"list"},{"key":"联系电话","value":"${contactPhone}","entityType":"list"},{"key":"企业","value":"${enterpriseName}","entityType":"list"},{"key":"申请类型","value":"${applyType}","entityType":"list"},{"key":"面积需求","value":"${areaSize} 平米","entityType":"list"},{"key":"申请来源","value":"${sourceType}","entityType":"list"},{"key":"备注","value":"${description}","entityType":"multi_line"}]', '0');

SET @eh_locale_strings = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'expansion.applyType', '1', 'zh_CN', '入驻申请');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'expansion.applyType', '2', 'zh_CN', '扩租申请');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'expansion.applyType', '3', 'zh_CN', '续租申请');

--
-- 工作流设置菜单
--
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (40130, '工作流设置', 40100, NULL, 'react:/working-flow/flow-list/rent-manage/40100', 0, 2, '/40000/40100/40130', 'park', 419);

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 40130, '', 'EhNamespaces', 1000000, 2);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10020, 40130, '招租管理', 1, 1, '招租管理 工作流设置 全部权限', 419);