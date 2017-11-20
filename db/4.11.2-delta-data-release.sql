-- from club 3.2 start

-- 俱乐部详情页  add by yanjun 20171107
SET @id = (SELECT MAX(id) from eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@id := @id + 1), 'club.description.url', '/mobile/static/club_detail/index.html', 'club description url', '0', NULL);

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('10760', '行业协会', '10000', NULL, 'groups/guild', '0', '2', '/10000/10760', 'park', '180', '10750', '2', NULL, 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('10761', '行业协会管理', '10760', NULL, 'groups_management/guild', '0', '2', '/10000/10760/10761', 'park', '181', '10750', '3', NULL, 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('10762', '审核行业协会', '10760', NULL, 'react:/club-audit/audit-list', '0', '2', '/10000/10760/10762', 'park', '182', '10750', '3', NULL, 'module');

UPDATE eh_web_menus SET data_type = 'react:/club-audit/audit-list/1' where id = 10752;

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`) VALUES ('10760', '行业协会', '10000', '/10000/10760', '1', '2', '2', '0', NOW(), NULL, '38', NOW(), '0', '0', '0', '0');

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10760,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10761,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10762,'', 'EhNamespaces', 1000000,2);

SET @id = (SELECT MAX(id) from eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ((@id := @id + 1), 'group.notification', '60', 'zh_CN', '行业协会申请加入', '${userName}申请“${organizationName}”加入“${groupName}”，是否同意？', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ((@id := @id + 1), 'group.notification', '61', 'zh_CN', '行业协会通过申请', '您提交的申请：“${organizationName}”加入“${groupName}”，已被管理员通过。', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ((@id := @id + 1), 'group.notification', '62', 'zh_CN', '行业协会拒绝申请', '您提交的申请：“${organizationName}”加入“${groupName}”，已被管理员拒绝，理由：“${rejectText}”。', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ((@id := @id + 1), 'group.notification', '63', 'zh_CN', '行业协会解散', '您加入的行业协会“${groupName}”已解散', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ((@id := @id + 1), 'group.notification', '64', 'zh_CN', '俱乐部解散', '您加入的俱乐部“${groupName}”已解散', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ((@id := @id + 1), 'group.notification', '65', 'zh_CN', '退出俱乐部', '您已退出俱乐部“${groupName}”', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ((@id := @id + 1), 'group.notification', '66', 'zh_CN', '退出行业协会', '您已退出行业协会“${groupName}”', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ((@id := @id + 1), 'group.notification', '67', 'zh_CN', '退出俱乐部发给管理员', '${userName}已退出俱乐部“${groupName}”', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ((@id := @id + 1), 'group.notification', '68', 'zh_CN', '退出行业协会发给管理员', '${userName}已退出行业协会“${groupName}”', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ((@id := @id + 1), 'group.notification', '69', 'zh_CN', '加入免费俱乐部发给管理员', '${userName}已加入俱乐部“${groupName}”', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ((@id := @id + 1), 'group.notification', '70', 'zh_CN', '加入免费行业协会发给管理员', '${userName}已加入行业协会“${groupName}”', '0');

SET @id = (SELECT MAX(id) from eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'group', '10036', 'zh_CN', '俱乐部创建者无法被删除');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'group', '10037', 'zh_CN', '行业协会创建者无法被删除');

-- from club 3.2 end


