-- 增加广告管理菜单 by xq.tian 20160825
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (49600,'广告管理',40000,null,null,0,2,'/40000/49600','park',458);

-- 添加权限
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (830,0,'广告管理','广告管理',null);

-- 添加菜单的权限
set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),830,49600,'广告管理',1,1,'广告管理  全部权限',577);

-- 添加菜单与权限的关联关系    role_id: 1001为物业公司超级管理员
set @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1001,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%47000%');


-- 添加banner激活数量配置
set @eh_configurations_id = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `ehcore`.`eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
VALUES ((@eh_configurations_id := @eh_configurations_id + 1), 'banner.max.active.count', '8', '一个域空间下激活banner的最多个数', '0', NULL);

-- 添加banner提示
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'banner', '10003', 'zh_CN', '广告激活数量超过最大值啦!');

-- 修改banner的关闭状态从0变为3
UPDATE `ehcore`.`eh_banners` SET `status` = 3 WHERE `status` = 0;

-- 修改场景类型的display_name
UPDATE `ehcore`.`eh_scene_types` SET `display_name`='普通用户场景' WHERE (`name`='default');
UPDATE `ehcore`.`eh_scene_types` SET `display_name`='管理公司场景' WHERE (`name`='pm_admin');