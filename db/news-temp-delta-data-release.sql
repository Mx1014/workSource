update eh_news_categories SET entry_id = 0 WHERE id in (0,1,3,60);
update eh_news_categories SET entry_id = 1 WHERE id in (2);

--添加菜单：通知公告----
set @menu_id=13000;
-- 设置菜单
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES (@menu_id, '通知公告', '10000', NULL, 'news_management/1', '0', '2', CONCAT('/10000/',@menu_id), 'park', '181', '10800', '2', NULL, 'module');
-- 设置菜单权限
set @eh_acl_privileges_id = (select max(id) from eh_acl_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)VALUES ((@eh_acl_privileges_id := @eh_acl_privileges_id+1), 0, '通知公告', '通知公告 全部权限', NULL);
-- 角色对应的菜单权限
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)VALUES ((@acl_id := @acl_id + 1), @namespace_id, 'EhOrganizations', NULL, 1, @eh_acl_privileges_id, 1001, 'EhAclRoles', 0, 1, NOW());
-- 菜单对应的权限
SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @eh_acl_privileges_id, @menu_id, '通知公告', 1, 1, '通知公告 全部权限', 1);

update eh_web_menus SET data_type = 'news_management/0' WHERE id = 10800;

-- 荣超股份的配置通知公告到app和后台
set @namespace_id = 999975;
INSERT INTO `eh_news_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`) VALUES ('4', '', '0', '0', '通知公告', NULL, NULL, '0', '1', now(), '0', NULL, @namespace_id, '', '1');
update eh_launch_pad_layouts SET version_code = '2017111001',layout_json='{\"versionCode\":\"2017111001\",\"layoutName\":\"ServiceMarketLayout\",\"displayName\":\"服务市场\",\"groups\":[{\"groupName\":\"\",\"widget\":\"Banners\",\"instanceConfig\":{\"itemGroup\":\"Default\"},\"style\":\"Default\",\"defaultOrder\":1},{\"groupName\":\"\",\"widget\":\"Bulletins\",\"instanceConfig\":{\"itemGroup\":\"Default\"},\"style\":\"Default\",\"defaultOrder\":3},{\"groupName\":\"商家服务\",\"widget\":\"Navigator\",\"instanceConfig\":{\"itemGroup\":\"Bizs\",\"paddingTop\":1,\"paddingLeft\":1,\"paddingBottom\":1,\"paddingRight\":1},\"style\":\"Default\",\"defaultOrder\":5},{\"groupName\":\"\",\"widget\":\"News\",\"instanceConfig\":{\"itemGroup\":\"Default\",\"categoryId\":4,\"timeWidgetStyle\":\"datetime\"},\"style\":\"Default\",\"defaultOrder\":5}]}' WHERE namespace_id = '999975';
-- 菜单范围添加
SET @scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@scope_id:=@scope_id+1), @menu_id,'', 'EhNamespaces', @namespace_id , 2);
