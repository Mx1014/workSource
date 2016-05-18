#
#SELECT MAX(id) FROM `eh_web_menu_privileges`;
#
set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (11100,'活动管理',10000,null,'forum_activity',0,2,'/10000/11100','park',115);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),200,11100,'活动管理',1,1,'查询活动贴以及发布活动',15);



