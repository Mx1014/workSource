-- 基础的三种参数 
INSERT INTO `eh_energy_meter_formula_variables` (`id`, `namespace_id`, `name`, `display_name`, `description`, `status`, `creator_uid`, `create_time`)
VALUES ('1', '0', 'p', '单价', '当期单价', '2', '1', '2016-11-01 19:06:09');
INSERT INTO `eh_energy_meter_formula_variables` (`id`, `namespace_id`, `name`, `display_name`, `description`, `status`, `creator_uid`, `create_time`)
VALUES ('2', '0', 't', '倍率', '当期倍率', '2', '1', '2016-11-01 19:06:12');
INSERT INTO `eh_energy_meter_formula_variables` (`id`, `namespace_id`, `name`, `display_name`, `description`, `status`, `creator_uid`, `create_time`)
VALUES ('3', '0', 'a', '读表用量差', '每日或每月读表差', '2', '1', '2016-11-01 19:06:13');

--
-- 表记默认分类(深业域空间下)
--
INSERT INTO `eh_energy_meter_categories` (`id`, `namespace_id`, `name`, `status`, `category_type`, `delete_flag`, `creator_uid`, `create_time`, `update_uid`, `update_time`) VALUES ('1', '999992', '应收部分', '2', '1', '0', NULL, NULL, NULL, NULL);
INSERT INTO `eh_energy_meter_categories` (`id`, `namespace_id`, `name`, `status`, `category_type`, `delete_flag`, `creator_uid`, `create_time`, `update_uid`, `update_time`) VALUES ('2', '999992', '应付部分', '2', '1', '0', NULL, NULL, NULL, NULL);

--
-- 字符串
--
SET @eh_locale_strings_id = (SELECT max(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy', '10001', 'zh_CN', '表记不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy', '10002', 'zh_CN', '表记分类不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy', '10003', 'zh_CN', '公式不能计算');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy', '10004', 'zh_CN', '公式格式错误');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy', '10005', 'zh_CN', '读表记录不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy', '10006', 'zh_CN', '只允许删除今天的读表记录');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy', '10008', 'zh_CN', '公式被引用,无法删除');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy', '10009', 'zh_CN', '起始读数大于最大量程');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy', '10010', 'zh_CN', '默认分类无法删除');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy', '10011', 'zh_CN', '导入失败,请检查数据准确性');

INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy.meter.type', '1', 'zh_CN', '水表');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy.meter.type', '2', 'zh_CN', '电表');

INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy.meter.status', '2', 'zh_CN', '正常');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'energy.meter.status', '3', 'zh_CN', '已报废');

--
-- 菜单添加
--
-- 新增能耗管理菜单
--
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (49100, '能耗管理', 40000, NULL, 'energy_management', 1, 2, '/40000/49100', 'park', 390);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (422, 0, '能耗管理', '能耗管理 全部权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 422, 49100, '能耗管理', 1, 1, '能耗管理  全部权限', 202);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 422, 1001, 0, 1, now());

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 49100, '', 'EhNamespaces', 999992, 2);

-- 表记管理菜单
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (49110, '表记管理', 49100, NULL, 'energy_table_management', 0, 2, '/40000/49100/49110', 'park', 391);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (423, 0, '表记管理', '能耗管理 表记管理 全部权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 423, 49110, '表记管理', 1, 1, '能耗管理 表记管理 全部权限', 203);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 423, 1001, 0, 1, now());

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 49110, '', 'EhNamespaces', 999992, 2);

-- 抄表记录菜单
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (49120, '抄表记录', 49100, NULL, 'energy_table_record', 0, 2, '/40000/49100/49120', 'park', 392);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (424, 0, '抄表记录', '能耗管理 抄表记录 全部权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 424, 49120, '抄表记录', 1, 1, '能耗管理 抄表记录 全部权限', 204);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 424, 1001, 0, 1, now());

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 49120, '', 'EhNamespaces', 999992, 2);

-- 统计信息菜单
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (49130, '统计信息', 49100, NULL, 'energy_statistics_info', 0, 2, '/40000/49100/49130', 'park', 393);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (425, 0, '统计信息', '能耗管理 统计信息 全部权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 425, 49130, '统计信息', 1, 1, '能耗管理 统计信息 全部权限', 205);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 425, 1001, 0, 1, now());

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 49130, '', 'EhNamespaces', 999992, 2);

-- 参数设置菜单
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (49140, '参数设置', 49100, NULL, 'energy_param_setting', 0, 2, '/40000/49100/49140', 'park', 394);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (426, 0, '参数设置', '能耗管理 参数设置 全部权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 426, 49140, '参数设置', 1, 1, '能耗管理 参数设置 全部权限', 206);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 426, 1001, 0, 1, now());

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 49140, '', 'EhNamespaces', 999992, 2);