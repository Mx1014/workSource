-- 电商运营数据api   add by xq.tian 2017/03/01
SET @conf_id := (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES(@conf_id := @conf_id+1, 'biz.business.promotion.api', '/Zl-MallMgt/shopCommo/admin/queryRecommendList.ihtml', 'biz promotion data api', '0', '电商首页运营数据api');

UPDATE `eh_launch_pad_items` SET `action_data`='{"url":"https://biz-beta.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3a%2f%2fbiz-beta.zuolin.com%2fnar%2fbiz%2fweb%2fapp%2fuser%2findex.html%23%2fmicroshop%2fhome%3fisfromindex%3d0%26_k%3dzlbiz%23sign_suffix"}'
WHERE `namespace_id`=999985 AND `item_group`='OPPushBiz' AND `item_label`='OE优选';


--
-- 文件管理菜单   add by xq.tian  2017/03/01
--
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (41500, '文件管理', 40000, NULL, 'file_management', 0, 2, '/40000/41500', 'park', 390);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10135, 0, '文件管理', '文件管理 全部权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10135, 41500, '文件管理', 1, 1, '文件管理  全部权限', 202);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, 10135, 1001, 'EhAclRoles', 0, 1, NOW());

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 41500, '', 'EhNamespaces', 999984, 2);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`)
VALUES (41500, '文件管理', 40000, '/40000/41500', 0, 2, 2, 0, UTC_TIMESTAMP());

SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), 41500, 1, 10135, NULL, '0', UTC_TIMESTAMP());

SET @eh_service_module_scopes_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999984, 41500, '文件管理', 'EhNamespaces', 999984, NULL, 2);

--
-- 文件上传菜单   add by xq.tian  2017/02/24
--
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (80600, '文件下载', 80000, NULL, 'react:/file-management/file-download', 1, 2, '/80000/80600', 'park', 500);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10136, 0, '文件下载', '文件下载 全部权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10136, 80600, '文件下载', 1, 1, '文件下载  全部权限', 202);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, 10136, 1005, 'EhAclRoles', 0, 1, NOW());

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 80000, '', 'EhNamespaces', 999984, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 80600, '', 'EhNamespaces', 999984, 2);

SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), 41500, 1, 10136, NULL, 0, UTC_TIMESTAMP());

--
-- 华润OE首页布局修改   add by xq.tian 2017/03/01
--
UPDATE `eh_launch_pad_layouts` SET version_code='2017030101', `layout_json`='{"versionCode": "2017030101","versionName": "4.3.1","layoutName": "ServiceMarketLayout","displayName": "服务市场","groups": [{"groupName": "","widget": "Banners","instanceConfig": {"itemGroup": "Default"},"style": "Default","defaultOrder": 1,"separatorFlag": 0,"separatorHeight": 0}, {"groupName": "","widget": "Bulletins","instanceConfig": {"itemGroup": "Default"},"style": "Default","defaultOrder": 3,"separatorFlag": 1,"separatorHeight": 2}, {"groupName": "商家服务","widget": "Navigator","instanceConfig": {"itemGroup": "Bizs"},"style": "Default","defaultOrder": 5,"separatorFlag": 1,"separatorHeight": 21}, {"groupName": "","widget": "OPPush","instanceConfig": {"itemGroup": "OPPushActivity","newsSize": 3,"entityCount": 3,"subjectHeight": 1,"descriptionHeight": 0},"style": "ListView","defaultOrder": 6,"separatorFlag": 1,"separatorHeight": 21,"columnCount": 1}, {"groupName": "","widget": "OPPush","instanceConfig": {"itemGroup": "OPPushBiz","newsSize": 6,"entityCount": 6,"subjectHeight": 1,"descriptionHeight": 0},"style": "HorizontalScrollView","defaultOrder": 7,"separatorFlag": 1,"separatorHeight": 0,"columnCount": 0}]}'
WHERE `namespace_id`=999985 AND `name`='ServiceMarketLayout';