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



#
#20160618
#
DELETE FROM `eh_web_menus` WHERE `id` in (58000,58100,58110,58111,58112,58120,58121,58122,58130,58131,58132,58140);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58000,'物业服务',50000,null,null,1,2,'/50000/58000','park',600);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58100,'品质核查',58000,null,null,1,2,'/50000/58000/58100','park',610);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58110,'手动核查',58100,null,null,1,2,'/50000/58000/58100/58110','park',611);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58111,'行为规范',58110,null,'behavior_standard',0,2,'/50000/58000/58100/58110/58111','park',612);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58112,'绩效统计',58110,null,'performance_statistics',0,2,'/50000/58000/58100/58110/58112','park',613);


INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58120,'作业标准',58100,null,null,1,2,'/50000/58000/58100/58120','park',614);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58121,'参考标准',58120,null,'reference_standard',0,2,'/50000/58000/58100/58120/58121','park',615);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58122,'类型管理',58120,null,'type_management',0,2,'/50000/58000/58100/58120/58122','park',616);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58130,'任务列表',58100,null,null,1,2,'/50000/58000/58100/58130','park',617);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58131,'任务列表',58130,null,'task_list',0,2,'/50000/58000/58100/58130/58131','park',618);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58132,'任务审阅',58130,null,'task_review',0,2,'/50000/58000/58100/58130/58132','park',619);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58140,'修改记录',58100,null,'edit_record',0,2,'/50000/58000/58100/58140','park',620);


DELETE FROM `eh_acl_privileges` WHERE `id` in (700,701,702,710,711,712);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (751,0,'行为规范','行为规范 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (752,0,'绩效统计','绩效统计 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (753,0,'参考标准','参考标准 全部功能',null);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (754,0,'类型管理','类型管理 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (755,0,'任务列表','任务列表 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (756,0,'任务审阅','任务审阅 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (757,0,'修改记录','修改记录 全部功能',null);


DELETE FROM `eh_web_menu_privileges` WHERE `id` in (81,82,83,84,85,86);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (120,751,58111,'行为规范',1,1,'行为规范 全部权限',700);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (121,752,58112,'绩效统计',1,1,'绩效统计 全部权限',710);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (122,753,58121,'参考标准',1,1,'参考标准 全部权限',720);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (123,754,58122,'类型管理',1,1,'类型管理 全部权限',730);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (124,755,58131,'任务列表',1,1,'任务列表 全部权限',740);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (125,756,58132,'任务审阅',1,1,'任务审阅 全部权限',750);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (126,757,58140,'修改记录',1,1,'修改记录 全部权限',760);

#
#20160618
#
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (760,0,'开卡用户','开卡用户 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (761,0,'充值记录','充值记录 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (762,0,'消费记录','消费记录 全部功能',null);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43500,'一卡通',40000,null,null,1,2,'/40000/43500','park',440);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43510,'开卡用户',43500,null,'card_user',0,2,'/40000/43500/43510','park',441);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43520,'充值记录',43500,null,'card_recharge_record',0,2,'/40000/43500/43520','park',442);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (43530,'消费记录',43500,null,'card_purchase_record',0,2,'/40000/43500/43530','park',443);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (130,760,43510,'开卡用户',1,1,'开卡用户 全部权限',345);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (131,761,43520,'充值记录',1,1,'充值记录 全部权限',346);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (132,762,43530,'消费记录',1,1,'消费记录 全部权限',347);
