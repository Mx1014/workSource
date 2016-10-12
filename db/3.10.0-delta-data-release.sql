-- added by wh 2016-10-11 设置活跃用户数的区间
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'active.count','6-10','active count ');
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (999987, 'active.count','6-10','active count ');
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (1000000, 'active.count','6-10','active count ');

-- added by sfyan 20161011
set @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`) VALUES((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 315, 1001,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`) VALUES((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 315, 1002,0,1,now());

-- added by sfyan 20161011
UPDATE `eh_web_menu_scopes` SET `menu_name` = '创业活动', `apply_policy` = 1 WHERE `owner_type` = 'EhNamespaces' AND `owner_id` = 999987 AND `menu_id` = 11100;
UPDATE `eh_web_menu_scopes` SET `menu_name` = '路演直播', `apply_policy` = 1 WHERE `owner_type` = 'EhNamespaces' AND `owner_id` = 999987 AND `menu_id` = 11400;

-- 增加日活统计菜单 added by sfyan 20161011
set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (716,0,'日活统计','日活统计 全部功能',null);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (49800,'日活统计',40000,null,'dailyActive--dailyActive',0,2,'/40000/49800','park',459);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),716,49800,'日活统计',1,1,'日活统计  全部权限',16);

set @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`) VALUES((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 716, 1001,0,1,now());

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),49800,'', 'EhNamespaces', 1000000,2);