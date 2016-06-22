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
