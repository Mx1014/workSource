-- 俱乐部详情页  add by yanjun 20171107
SET @id = (SELECT MAX(id) from eh_configurations);
INSERT INTO `ehcore`.`eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@id := @id + 1), 'group.content.url', '/web/lib/html/group_text_review.html', 'group content url', '0', NULL);

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('10760', '行业协会', '10000', NULL, 'groups/guild', '0', '2', '/10000/10760', 'park', '180', '10750', '2', NULL, 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('10761', '行业协会管理', '10760', NULL, 'groups_management/guild', '0', '2', '/10000/10760/10761', 'park', '181', '10750', '3', NULL, 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('10762', '审核行业协会', '10760', NULL, 'audit_groups/guild', '0', '2', '/10000/10760/10762', 'park', '182', '10750', '3', NULL, 'module');

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`) VALUES ('10760', '行业协会', '10000', '/10000/10760', '1', '2', '2', '0', NOW(), NULL, '38', NOW(), '0', '0', '0', '0');

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10760,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10761,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10762,'', 'EhNamespaces', 1000000,2);
