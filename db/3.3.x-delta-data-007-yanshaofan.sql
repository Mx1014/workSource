
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 11, 'zh_CN','接受任务时回复的帖子消息', '该任务已由{targetUName}（{targetUToken}）确认，将会很快联系您。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 12, 'zh_CN', '新发布一条任务短信消息','您有一个新的任务，请尽快处理。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 13, 'zh_CN', '处理任务时发送的短信消息','{operatorUName}已分配给你一个物业任务，请尽快联系业主（{createUName}）处理。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 14, 'zh_CN', '处理任务时回复的帖子消息','{operatorUName}（{operatorUToken}）已将该任务指派给{targetUName}处理，请等待。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 15, 'zh_CN', '任务被拒绝收到的短信消息','该任务已被{targetUName}（{targetUToken}）拒绝，请重新分配');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 16, 'zh_CN', '任务已完成后的短信消息','您的服务已完成，滑动去查看详情');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'organization.notification', 17, 'zh_CN', '任务已完成后回复的帖子消息','该服务已由{operatorUName}完成，稍后我们会将进行回访');


INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`) values (305,0,'all.task.posts.list',null,'MANAGER_TASK');
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`) values (303,0,'guarantee.task.posts.list',null,'MANAGER_TASK');
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`) values (304,0,'seekHelp.task.posts.list',null,'MANAGER_TASK');



INSERT INTO `eh_acls` (`id`,`owner_type`,`owner_id`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`) values (313,'EhOrganizations',null,1,305,1001,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`owner_id`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`) values (314,'EhOrganizations',null,1,303,1001,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`owner_id`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`) values (315,'EhOrganizations',null,1,304,1001,0,1,now());

INSERT INTO `eh_acl_role_assignments` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`role_id`,`creator_uid`,`create_time`) values (20000,'system',null,'EhUsers',202608,1006,1,now());
INSERT INTO `eh_acl_role_assignments` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`role_id`,`creator_uid`,`create_time`) values (20001,'system',null,'EhUsers',196607,1007,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`owner_id`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`) values (316,'EhOrganizations',null,1,303,1006,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`owner_id`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`) values (317,'EhOrganizations',null,1,304,1003,0,1,now());




INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (10000,'消息管理',0,'fa fa-volume-up',null,1,2,'/10000','park',100);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (11000,'论坛/公告',10000,null,'forum_notice',0,2,'/10000/11000','park',110);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (12000,'一键推送',10000,null,'messagepush',0,2,'/10000/12000','park',120);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (20000,'任务管理',0,'fa fa-coffee',null,1,2,'/20000','park',200);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (21000,'全部任务',20000,null,'all_task',0,2,'/20000/21000','park',210);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (22000,'我的任务',20000,null,'my_task',0,2,'/20000/22000','park',220);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (23000,'统计',20000,null,'statistics',0,2,'/20000/23000','park',230);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (30000,'园区/小区管理',0,'fa fa-building',null,1,2,'/30000','park',300);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (31000,'楼栋管理',30000,null,'building_management',0,2,'/30000/31000','park',310);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (32000,'门牌管理',30000,null,'apartment_statistics',0,2,'/30000/32000','park',320);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (33000,'企业管理',30000,null,'enterprise_management',0,2,'/30000/33000','park',330);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (34000,'用户管理',30000,null,'user_management',0,2,'/30000/34000','park',340);


INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (40000,'运营服务',0,'fa fa-comment',null,1,2,'/40000','park',400);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (41000,'停车充值',40000,null,null,1,2,'/40000/41000','park',410);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (41100,'停车设置',41000,null,'park_setting',0,2,'/40000/41000/41100','park',411);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (44000,'服务联盟',30000,null,'service_alliance',0,2,'/30000/31000','park',320);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (45000,'创客空间',30000,null,'market_zone',0,2,'/30000/32000','park',330);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (46000,'服务热线',30000,null,'service_hotline',0,2,'/30000/33000','park',340);


INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (1,200,11000,'发帖',1,1,'发公告和任务贴',10);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (2,201,12000,'推送消息',1,1,'推送消息',20);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (3,301,21000,'分配人员',0,1,'分配人员',30);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (4,302,21000,'修改任务状态',0,1,'修改任务状态',40);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (5,303,21000,'查询报修任务帖子',1,1,'查询报修任务帖子',50);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (6,304,21000,'查看求助任务帖子',1,1,'查看求助任务帖子',60);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (20,305,21000,'查看所有任务帖子',1,1,'查看所有任务帖子',70);


INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (7,301,22000,'分配人员',0,1,'分配人员',80);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (8,302,22000,'修改任务状态',0,1,'修改任务状态',90);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (9,303,22000,'查询报修任务帖子',1,1,'查询报修任务帖子',100);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (10,304,22000,'查看求助任务帖子',1,1,'查看求助任务帖子',110);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (11,305,22000,'查看所有任务帖子',1,1,'查看所有任务帖子',120);


INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (12,400,31000,'楼栋的增删改',1,1,'楼栋的增删改',130);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (13,401,33000,'查询园区企业',1,1,'查询园区企业',140);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (14,402,33000,'增加企业',0,1,'增加企业',150);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (15,405,33000,'开通企业管理账号',0,1,'开通企业管理账号',180);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES (16,500,41100,'增加停车充值项',1,1,'增加停车充值项',180);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (200,0,'publish.topic',null,null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (201,0,'push.message',null,null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (301,0,'personnel.allotment',null,null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (302,0,'modify.task.status',null,null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (303,0,'guarantee.task.posts.list',null,'MANAGER_TASK');
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (304,0,'seekHelp.task.posts.list',null,'MANAGER_TASK');
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (305,0,'all.task.posts.list',null,'MANAGER_TASK');

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (401,0,'building.add.delete.update',null,null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (401,0,'enterprise.list',null,null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (402,0,'enterprise.add',null,null);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (405,0,'enterprise.create.account',null,null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (500,0,'increase.parking.charge',null,null);




INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (403,0,null,null,'enterprise.create.account');
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (404,0,null,null,'');



INSERT INTO `eh_acls` (`id`,`owner_type`,`owner_id`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
VALUES (20,305,21000,'查看所有任务帖子',1,1,'查看所有任务帖子',70);