-- 积分使用消息
SET @locale_templates_id = IFNULL((SELECT MAX(id) FROM `eh_locale_templates`), 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@locale_templates_id := @locale_templates_id + 1), 'point', 10000, 'zh_CN', '您的积分将于12月31日过期，请及时使用。', '您的积分将于12月31日过期，请及时使用。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@locale_templates_id := @locale_templates_id + 1), 'point', 10001, 'zh_CN', '积分通用的模板', '{"messageTitle":"积分消息","resetPointDesc":"积分重置","resetPointCate":"系统","exportLogTitle":"昵称,手机,功能模块,行为,积分变化,日期","exportLogFileName":"用户积分历史"}', 0);

DELETE FROM `eh_point_rules`;

DELETE FROM `eh_point_rule_to_event_mappings`;

--
-- 积分管理菜单   add by xq.tian  2017/12/22
--
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
VALUES (47000, '积分管理', 40000, NULL, '', 0, 2, '/40000/47000', 'park', 500, 47000, 2, NULL, 'module');

SET @web_menu_scopes_id = IFNULL((SELECT MAX(id) FROM `eh_web_menu_scopes`), 0);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@web_menu_scopes_id := @web_menu_scopes_id + 1), 47000, '', 'EhNamespaces', 1000000, 2);

SET @acl_privileges_id = IFNULL((SELECT MAX(id) FROM `eh_acl_privileges`), 0);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES ((@acl_privileges_id := @acl_privileges_id + 1), 0, '积分管理', '积分管理 全部权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @acl_privileges_id, 47000, '积分管理', 1, 1, '积分管理  全部权限', 202);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @acl_privileges_id, 1001, 'EhAclRoles', 0, 1, NOW());

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `operator_uid`, `creator_uid`)
VALUES (47000, '积分管理', '40000', '/40000/47000', '0', '2', '2', '0', UTC_TIMESTAMP(), 0, 0);

SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), 47000, '1', @acl_privileges_id, NULL, '0', UTC_TIMESTAMP());


INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
VALUES (5200000, '积分系统', 5000000, NULL, 'integral-management', 0, 2, '/5000000/5200000', 'zuolin', 10, 47000, 2, 'namespace', 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
VALUES (5210000, '积分系统', 5200000, NULL, 'integral-management', 0, 2, '/5000000/5200000/5210000', 'zuolin', 10, 47000, 2, 'namespace', 'page');

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
VALUES (1200000, '短信管理', 1000000, NULL, 'sms-management', 0, 2, '/1100000/1100000', 'zuolin', 10, 12200, 2, 'namespace', 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
VALUES (1210000, '短信管理', 1200000, NULL, 'sms-management', 0, 2, '/1100000/1100000/1110000', 'zuolin', 10, 12200, 2, 'namespace', 'page');

SET @locale_strings_id = IFNULL((SELECT MAX(id) FROM `eh_locale_strings`), 0);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@locale_strings_id := @locale_strings_id + 1), 'point', '10000', 'zh_CN', '积分系统不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@locale_strings_id := @locale_strings_id + 1), 'point', '10002', 'zh_CN', '积分指南不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@locale_strings_id := @locale_strings_id + 1), 'point', '10004', 'zh_CN', 'Banner不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@locale_strings_id := @locale_strings_id + 1), 'point', '10005', 'zh_CN', '商品获取失败');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@locale_strings_id := @locale_strings_id + 1), 'point', '10006', 'zh_CN', '验证码错误');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@locale_strings_id := @locale_strings_id + 1), 'point', '10007', 'zh_CN', '积分系统名称已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@locale_strings_id := @locale_strings_id + 1), 'point', '10008', 'zh_CN', '积分名称已存在');
