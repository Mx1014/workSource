#
#SELECT MAX(id) FROM `eh_web_menu_privileges`;
#
set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (11100,'活动管理',10000,null,'forum_activity',0,2,'/10000/11100','park',115);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),200,11100,'活动管理',1,1,'查询活动贴以及发布活动',15);



#20160525
#
#SELECT MAX(id) FROM `eh_web_menu_privileges`;
#
set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (713,0,'缴费记录','缴费记录 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (714,0,'设置','设置 全部功能',null);


INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43500,'缴费管理',40000,null,null,1,2,'/40000/43500','park',435);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43501,'缴费记录',43500,null,'property_fee_record',0,2,'/40000/43500/43501','park',436);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43502,'设置',43500,null,'property_fee_config',0,2,'/40000/43500/43502','park',437);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),713,43501,'缴费记录',1,1,'缴费记录',341);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),714,43502,'设置',1,1,'设置',342);







#
#20160526
#
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'organization', '10101', 'zh_CN', '要审核的人员已经退出了公司！');




#
#20160530
#
set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (715,0,'Wifi热点','Wifi热点 全部功能',null);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (46100,'Wifi热点',40000,null,'wifi_hotspot',0,2,'/40000/46100','park',458);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),715,46100,'Wifi热点',1,1,'Wifi热点',401);





#
#20160602
#
UPDATE `eh_forum_posts` SET category_id = 1010,`category_path`='帖子/活动' WHERE `embedded_app_id` = 3 and `category_id` = 1003;



#
#20160615
#
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (725,0,'门禁管理','门禁管理 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (726,0,'用户授权','用户授权 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (727,0,'访客授权','访客授权 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (728,0,'版本管理 ','版本管理  全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (729,0,'门禁分组','门禁分组 全部功能',null);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (59000,'内部门禁',50000,null,null,1,2,'/50000/59000','park',700);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (59100,'门禁管理',59000,null,'access_manage_inside',0,2,'/50000/59000/59100','park',702);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (59150,'版本管理',59000,null,'version_manage_inside',0,2,'/50000/59000/59150','park',704);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (59160,'门禁分组',59000,null,'access_group_inside',0,2,'/50000/59000/59160','park',706);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (59200,'用户授权',59000,null,'user_auth_inside',0,2,'/50000/59000/59200','park',708);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (59300,'访客授权',59000,null,'visitor_auth_inside',0,2,'/50000/59000/59300','park',710);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (105,725,59100,'门禁管理',1,1,'门禁管理',780);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (106,726,59200,'用户授权',1,1,'用户授权',810);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (107,727,59300,'访客授权',1,1,'访客授权',820);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (108,728,59150,'版本管理',1,1,'版本管理',790);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (109,729,59160,'门禁分组',1,1,'门禁分组',800);

