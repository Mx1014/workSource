-- merge doorAuth by sfyan 20170112
-- 运营统计的数据修改 add sfyan 20170112
UPDATE `eh_terminal_day_statistics` SET `start_change_rate` = `start_change_rate` * -1, `new_change_rate` = `new_change_rate` * -1, `active_change_rate` = `active_change_rate` * -1, `cumulative_change_rate` = `cumulative_change_rate` * -1;

-- 重复审批提示 add sfyan 20170114
SELECT max(id) FROM eh_locale_strings INTO @max_id;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@max_id := @max_id + 1), 'organization', '500005', 'zh_CN', '已被其他管理员审批！');
  

-- app版本数据整理  add sfyan 20170117
SET @version_id = (SELECT MAX(id) FROM `eh_app_version`);
INSERT INTO eh_app_version (`id`,`type`,`name`,`namespace_id`,`default_order`,`create_time`) SELECT (@version_id := @version_id + 1), 'ios', '3.12.0', id, 3158016.0, now() FROM `eh_namespaces`;

INSERT INTO eh_app_version (`id`,`type`,`name`,`namespace_id`,`default_order`,`create_time`) SELECT (@version_id := @version_id + 1), 'ios', '3.12.4', id, 3158020.0, now() FROM `eh_namespaces`;

INSERT INTO eh_app_version (`id`,`type`,`name`,`namespace_id`,`default_order`,`create_time`) SELECT (@version_id := @version_id + 1), 'ios', '4.1.0', id, 4195328.0, now() FROM `eh_namespaces`;

INSERT INTO eh_app_version (`id`,`type`,`name`,`namespace_id`,`default_order`,`create_time`) SELECT (@version_id := @version_id + 1), 'ios', '4.1.2', id, 4195330.0, now() FROM `eh_namespaces`;

INSERT INTO eh_app_version (`id`,`type`,`name`,`namespace_id`,`default_order`,`create_time`) SELECT (@version_id := @version_id + 1), 'ios', '4.2.2', id, 4196354.0, now() FROM `eh_namespaces`;

INSERT INTO eh_app_version (`id`,`type`,`name`,`namespace_id`,`default_order`,`create_time`) SELECT (@version_id := @version_id + 1), 'android', '3.12.0', id, 3158016.0, now() FROM `eh_namespaces`;

INSERT INTO eh_app_version (`id`,`type`,`name`,`namespace_id`,`default_order`,`create_time`) SELECT (@version_id := @version_id + 1), 'android', '3.12.4', id, 3158020.0, now() FROM `eh_namespaces`;

INSERT INTO eh_app_version (`id`,`type`,`name`,`namespace_id`,`default_order`,`create_time`) SELECT (@version_id := @version_id + 1), 'android', '4.1.0', id, 4195328.0, now() FROM `eh_namespaces`;

INSERT INTO eh_app_version (`id`,`type`,`name`,`namespace_id`,`default_order`,`create_time`) SELECT (@version_id := @version_id + 1), 'android', '4.1.2', id, 4195330.0, now() FROM `eh_namespaces`;

INSERT INTO eh_app_version (`id`,`type`,`name`,`namespace_id`,`default_order`,`create_time`) SELECT (@version_id := @version_id + 1), 'android', '4.2.2', id, 4196354.0, now() FROM `eh_namespaces`;
  

  -- 黑名单配置到清华信息港 by sfyan 20170117
 SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
 INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
 VALUES ((@menu_scope_id := @menu_scope_id + 1), 30600, '', 'EhNamespaces', 999984, 2);
 
 -- 黑名单配置到创源 by sfyan 20170117
 SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
 INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
 VALUES ((@menu_scope_id := @menu_scope_id + 1), 30600, '', 'EhNamespaces', 999986, 2);
 
 -- 黑名单配置到左邻 by sfyan 20170117
  SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
 INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
 VALUES ((@menu_scope_id := @menu_scope_id + 1), 30600, '', 'EhNamespaces', 0, 2);

-- 机构人员字段填值   by sfyan 20170118
UPDATE `eh_organization_members` eom SET `group_type` = (SELECT `group_type` FROM `eh_organizations` where `id` = eom.organization_id), `group_path` = (SELECT `path` FROM `eh_organizations` where `id` = eom.organization_id);