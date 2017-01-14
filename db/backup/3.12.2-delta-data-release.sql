-- merge from terminal-stat-delta-release.sql by by sfyan 20161214
-- 运营统计相关数据
SET @configuration_id = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO eh_configurations(`id`,`name`,`value`,`description`,`namespace_id`)VALUES((@configuration_id := @configuration_id + 1), 'terminal.statistics.cron.expression','0 0 2 * * ?','schedule cron expression',0);

-- 统计分析菜单
DELETE FROM `eh_web_menus` WHERE id IN (40700);
DELETE FROM `eh_web_menu_scopes` WHERE menu_id IN (40700);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (41300,'统计分析',40000,NULL,NULL,1,2,'/40000/41300','park',462);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (41310,'应用统计',41300,NULL,'application_statistic',0,2,'/40000/41300/41310','park',462);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (41320, '结算管理', 41300, NULL, 'settlement_management', '1', '2', '/40000/41300/41320', 'park', '462');

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (10060,0,'统计分析','统计分析 管理员权限',NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),10060,41300,'统计分析',1,1,'统计分析 管理员权限',710);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),10060,41310,'统计分析',1,1,'统计分析 管理员权限',710);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),10060,41320,'结算管理',1,1,'结算管理 管理员权限',710);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),10026,41320,'结算管理',1,1,'结算管理 全部权限',710);


DELETE FROM `eh_service_modules` WHERE id IN (40700);
DELETE FROM `eh_service_module_scopes` WHERE module_id IN (40700);
SET @module_privilege_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('41300', '统计分析', '40000', '/40000/41300', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@module_privilege_id := @module_privilege_id + 1), '41300', '1', '10060', NULL, '0', UTC_TIMESTAMP());

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`owner_id`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`,`namespace_id`,`role_type`,`scope`) VALUES ((@acl_id := @acl_id + 1),'EhOrganizations',NULL,1,10060,1001,0,0,UTC_TIMESTAMP(),0,'EhAclRoles','');
INSERT INTO `eh_acls` (`id`,`owner_type`,`owner_id`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`,`namespace_id`,`role_type`,`scope`) VALUES ((@acl_id := @acl_id + 1),'EhOrganizations',1000750,1,10060,226707,0,0,UTC_TIMESTAMP(),0,'EhUsers','');


-- app版本数据整理
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('2','android','1.0.8','','0','1048584','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('3','android','1.0.9','','0','1048585','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('4','android','1.0.10','','0','1048586','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('5','android','1.0.12','','0','1048588','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('6','android','1.0.13','','0','1048589','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('7','android','1.0.15','','0','1048591','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('8','android','2.0','','0','2097150','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('9','android','2.0.2','','0','2097154','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('10','android','2.1.0','','0','2098176','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('11','android','2.2.0','','0','2099200','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('12','android','2.2.2','','0','2099202','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('13','android','2.3','','0','2099212','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('14','android','2.4.1','','0','2101249','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('15','android','2.4.2','','0','2101250','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('16','android','2.5.0','','0','2102272','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('17','android','2.6.0','','0','2103296','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('18','android','2.7.0','','0','2104320','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('19','android','2.7.2','','0','2104322','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('20','android','2.8.0','','0','2105344','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('21','android','3.0.0','','0','3145728','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('22','android','3.0.4','','0','3145732','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('23','android','3.0.6','','0','3145734','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('24','android','3.2.0','','0','3147776','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('25','android','3.2.2','','0','3147778','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('26','android','3.2.4','','0','3147780','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('27','android','3.4.0','','0','3149824','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('28','android','3.8.0','','0','3153920','2016-12-01 14:57:55');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('29','android','3.9.0','','0','3154944','2016-12-01 14:57:56');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('30','android','3.10.0','','0','3155968','2016-12-01 14:57:56');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('31','android','3.11.0','','0','3156992','2016-12-01 14:57:56');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('32','android','3.11.2','','0','3156994','2016-12-01 14:57:56');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('33','android','3.11.3','','0','3156995','2016-12-01 14:57:56');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('34','ios','2.0.4','','0','2097156','2016-12-01 14:57:56');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('35','ios','2.2.0','','0','2099200','2016-12-01 14:57:56');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('36','ios','2.2.2','','0','2099202','2016-12-01 14:57:56');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('37','ios','2.3.0','','0','2100224','2016-12-01 14:57:56');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('38','ios','2.4.0','','0','2101248','2016-12-01 14:57:56');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('39','ios','2.4.2','','0','2101250','2016-12-01 14:57:56');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('40','ios','2.5.0','','0','2102272','2016-12-01 14:57:57');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('41','ios','2.6.0','','0','2103296','2016-12-01 14:57:57');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('42','ios','2.7.2','','0','2104322','2016-12-01 14:57:57');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('43','ios','2.8.0','','0','2105344','2016-12-01 14:57:57');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('44','ios','2.8.2','','0','2105346','2016-12-01 14:57:57');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('45','ios','3.0.0','','0','3145728','2016-12-01 14:57:57');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('46','ios','3.0.4','','0','3145732','2016-12-01 14:57:57');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('47','ios','3.0.6','','0','3145734','2016-12-01 14:57:57');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('48','ios','3.2.0','','0','3147776','2016-12-01 14:57:57');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('49','ios','3.2.2','','0','3147778','2016-12-01 14:57:57');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('50','ios','3.4.0','','0','3149824','2016-12-01 14:57:57');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('51','ios','3.8.0','','0','3153920','2016-12-01 14:57:57');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('52','ios','3.8.2','','0','3153922','2016-12-01 14:57:57');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('53','ios','3.9.0','','0','3154944','2016-12-01 14:57:57');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('54','ios','3.10.0','','0','3155968','2016-12-01 14:57:57');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('55','ios','3.11.0','','0','3156992','2016-12-01 14:57:57');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('56','ios','3.11.2','','0','3156994','2016-12-01 14:57:57');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('57','android','1.0.0','','1000000','1048576','2016-12-01 14:57:57');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('58','android','1.0.2','','1000000','1048578','2016-12-01 14:57:57');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('59','android','1.1.0','','1000000','1049600','2016-12-01 14:57:57');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('60','android','3.4.0','','1000000','3149824','2016-12-01 14:57:57');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('61','android','3.6.0','','1000000','3151872','2016-12-01 14:57:57');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('62','android','3.10.0','','1000000','3155968','2016-12-01 14:57:57');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('63','android','3.11.0','','1000000','3156992','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('64','ios','1.0.0','','1000000','1048576','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('65','ios','1.0.2','','1000000','1048578','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('66','ios','1.1.0','','1000000','1049600','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('67','ios','1.1.2','','1000000','1049602','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('68','ios','3.4.0','','1000000','3149824','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('69','ios','3.6.0','','1000000','3151872','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('70','ios','3.7.0','','1000000','3152896','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('71','ios','3.7.4','','1000000','3152900','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('72','ios','3.8.0','','1000000','3153920','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('73','ios','3.8.2','','1000000','3153922','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('74','ios','3.9.0','','1000000','3154944','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('75','ios','3.9.2','','1000000','3154946','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('76','ios','3.10.0','','1000000','3155968','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('77','ios','3.10.1','','1000000','3155969','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('78','ios','3.11.0','','1000000','3156992','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('79','android','3.0.2','','999999','3145730','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('80','android','3.1.0','','999999','3146752','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('81','android','3.6.2','','999999','3151874','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('82','android','3.7.0','','999999','3152896','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('83','android','3.9.0','','999999','3154944','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('84','android','3.9.2','','999999','3154946','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('85','android','3.10.0','','999999','3155968','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('86','android','3.10.2','','999999','3155970','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('87','android','3.11.0','','999999','3156992','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('88','ios','3.0.2','','999999','3145730','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('89','ios','3.1.0','','999999','3146752','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('90','ios','3.6.1','','999999','3151873','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('91','ios','3.7.0','','999999','3152896','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('92','ios','3.9.0','','999999','3154944','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('93','ios','3.9.2','','999999','3154946','2016-12-01 14:57:58');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('94','ios','3.10.0','','999999','3155968','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('95','ios','3.10.2','','999999','3155970','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('96','ios','3.11.0','','999999','3156992','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('97','android','1.0.0','','999993','1048576','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('98','android','3.6.2','','999993','3151874','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('99','android','3.9.0','','999993','3154944','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('100','android','3.11.0','','999993','3156992','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('101','ios','1.0.0','','999993','1048576','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('102','ios','3.6.0','','999993','3151872','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('103','ios','3.9.0','','999993','3154944','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('104','ios','3.11.0','','999993','3156992','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('105','android','1.0.0','','999992','1048576','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('106','android','3.7.0','','999992','3152896','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('107','android','3.9.0','','999992','3154944','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('108','android','3.9.2','','999992','3154946','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('109','android','3.10.0','','999992','3155968','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('110','android','3.11.0','','999992','3156992','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('111','android','3.11.2','','999992','3156994','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('112','android','3.11.3','','999992','3156995','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('113','ios','1.0.0','','999992','1048576','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('114','ios','3.7.0','','999992','3152896','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('115','ios','3.7.1','','999992','3152897','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('116','ios','3.9.0','','999992','3154944','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('117','ios','3.9.2','','999992','3154946','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('118','ios','3.10.2','','999992','3155970','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('119','ios','3.11.0','','999992','3156992','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('120','ios','3.11.2','','999992','3156994','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('121','android','3.6.2','','999990','3151874','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('122','android','3.7.0','','999990','3152896','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('123','android','3.8.0','','999990','3153920','2016-12-01 14:57:59');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('124','android','3.9.0','','999990','3154944','2016-12-01 14:58:00');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('125','android','3.10.2','','999990','3155970','2016-12-01 14:58:00');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('126','android','3.11.2','','999990','3156994','2016-12-01 14:58:00');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('127','android','3.11.3','','999990','3156995','2016-12-01 14:58:00');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('128','ios','3.6','','999990','3151870','2016-12-01 14:58:00');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('129','ios','3.7','','999990','3152895','2016-12-01 14:58:00');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('130','ios','3.7.1','','999990','3152897','2016-12-01 14:58:00');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('131','ios','3.8','','999990','3154900','2016-12-01 14:58:00');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('132','ios','3.9','','999990','3154945','2016-12-01 14:58:00');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('133','ios','3.10.2','','999990','3155970','2016-12-01 14:58:00');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('134','ios','3.11.2','','999990','3156994','2016-12-01 14:58:00');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('135','ios','3.11.3','','999990','3156995','2016-12-01 14:58:00');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('136','android','3.8.0','','999989','3153920','2016-12-01 14:58:00');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('137','android','3.9.0','','999989','3154944','2016-12-01 14:58:00');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('138','android','3.9.2','','999989','3154946','2016-12-01 14:58:00');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('139','android','3.10.0','','999989','3155968','2016-12-01 14:58:00');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('140','ios','3.8.0','','999989','3153920','2016-12-01 14:58:00');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('141','ios','3.8.1','','999989','3153921','2016-12-01 14:58:00');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('142','ios','3.9.0','','999989','3154944','2016-12-01 14:58:00');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('143','ios','3.9.2','','999989','3154946','2016-12-01 14:58:00');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('144','ios','3.10.0','','999989','3155968','2016-12-01 14:58:00');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('145','android','3.8.2','','999991','3153922','2016-12-01 14:58:00');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('146','android','3.9.2','','999991','3154946','2016-12-01 14:58:00');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('147','android','3.10.0','','999991','3155968','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('148','android','3.11.2','','999991','3156994','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('149','android','3.11.3','','999991','3156995','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('150','ios','3.8.0','','999991','3153920','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('151','ios','3.8.2','','999991','3153922','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('152','ios','3.9.2','','999991','3154946','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('153','ios','3.10.0','','999991','3155968','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('154','ios','3.11.2','','999991','3156994','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('155','android','3.10.0','','999987','3155968','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('156','android','3.10.2','','999987','3155970','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('157','ios','3.10.0','','999987','3155968','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('158','ios','3.10.2','','999987','3155970','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('159','ios','3.10.4','','999987','3155972','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('160','android','3.10.0','','999986','3155968','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('161','android','3.10.2','','999986','3155970','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('162','android','3.11.3','','999986','3156995','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('163','ios','3.10.0','','999986','3155968','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('164','ios','3.10.2','','999986','3155970','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('165','ios','3.10.6','','999986','3155974','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('166','ios','3.11.2','','999986','3156994','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('167','android','3.10.4','','999984','3155972','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('168','android','3.11.0','','999984','3156992','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('169','android','3.11.3','','999984','3156995','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('170','ios','3.10.4','','999984','3155972','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('171','ios','3.10.6','','999984','3155974','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('172','ios','3.11.0','','999984','3156992','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('173','ios','3.11.2','','999984','3156994','2016-12-01 14:58:01');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('174','android','3.9.2','','999988','3154946','2016-12-01 14:58:02');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('175','android','3.10.2','','999988','3155970','2016-12-01 14:58:02');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('176','android','3.10.4','','999988','3155972','2016-12-01 14:58:02');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('177','ios','3.9.0','','999988','3154944','2016-12-01 14:58:02');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('178','ios','3.9.2','','999988','3154946','2016-12-01 14:58:02');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('179','ios','3.10.0','','999988','3155968','2016-12-01 14:58:02');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('180','ios','3.10.3','','999988','3155971','2016-12-01 14:58:02');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('181','ios','3.10.4','','999988','3155972','2016-12-01 14:58:02');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('182','ios','3.10.6','','999988','3155974','2016-12-01 14:58:02');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES('183','android','3.10.2','','0','3155970','2016-12-02 16:23:18');

-- 给菜单都配置上业务模块
UPDATE `eh_web_menus` SET `module_id` = id WHERE `parent_id` IN (10000,20000,30000,40000);

UPDATE `eh_web_menus` SET `module_id` = 10850 WHERE `path` LIKE '%/10850/%';
UPDATE `eh_web_menus` SET `module_id` = 20100 WHERE `path` LIKE '%/20100/%';
UPDATE `eh_web_menus` SET `module_id` = 20400 WHERE `path` LIKE '%/20400/%';
UPDATE `eh_web_menus` SET `module_id` = 20600 WHERE `path` LIKE '%/20600/%';
UPDATE `eh_web_menus` SET `module_id` = 20800 WHERE `path` LIKE '%/20800/%';
UPDATE `eh_web_menus` SET `module_id` = 40100 WHERE `path` LIKE '%/40100/%';
UPDATE `eh_web_menus` SET `module_id` = 40200 WHERE `path` LIKE '%/40200/%';
UPDATE `eh_web_menus` SET `module_id` = 40400 WHERE `path` LIKE '%/40400/%';
UPDATE `eh_web_menus` SET `module_id` = 40500 WHERE `path` LIKE '%/40500/%';
UPDATE `eh_web_menus` SET `module_id` = 40800 WHERE `path` LIKE '%/40800/%';
UPDATE `eh_web_menus` SET `module_id` = 41000 WHERE `path` LIKE '%/41000/%';
UPDATE `eh_web_menus` SET `module_id` = 41200 WHERE `path` LIKE '%/41200/%';
UPDATE `eh_web_menus` SET `module_id` = 41300 WHERE `path` LIKE '%/41300/%';
UPDATE `eh_web_menus` SET `module_id` = 41400 WHERE `path` LIKE '%/41400/%';
UPDATE `eh_web_menus` SET `module_id` = 10750 WHERE `path` LIKE '%/10750/%';



-- merge from flow-delta-data-release.sql by lqs 20161214
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.start_step', '开始', 'start-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.approve_step', '下一步', 'approve-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.reject_step', '驳回', 'reject-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.transfer_step', '转交', 'transfer-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.comment_step', '附言', 'comment-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.absort_step', '终止', 'absort-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.reminder_step', '催办', 'reminder-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.evaluate_step', '评价', 'evaluate-step');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'flow.stepname.end_step', '结束', 'end-step');

-- step message templates
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES
( 'flow', 10001, 'zh_CN', '${nodeName} 已完成', '${nodeName} 已完成');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES
( 'flow', 10002, 'zh_CN', '${nodeName} 被驳回', '${nodeName} 驳回');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES
( 'flow', 10003, 'zh_CN', '${applierName} 已取消任务', '${applierName} 已取消任务');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES
( 'flow', 10004, 'zh_CN', '${nodeName} 已转交', '${nodeName} 已转交');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES
( 'flow', 10005, 'zh_CN', '${nodeName} 上传了 ${imageCount}张图片', '${nodeName} 上传了 ${imageCount}张图片');

-- text variables
INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1000', '0', '0', '', '0', '', 'applierName', '发起人姓名', 'text', 'bean_id', 'flow-variable-applier-name', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1001', '0', '0', '', '0', '', 'applierPhone', '发起人手机号码', 'text', 'bean_id', 'flow-variable-applier-phone', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1002', '0', '0', '', '0', '', 'currProcessorName', '本节点处理人姓名', 'text', 'bean_id', 'flow-variable-curr-processor-name', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('1003', '0', '0', '', '0', '', 'currProcessorPhone', '本节点处理人手机号码', 'text', 'bean_id', 'flow-variable-curr-processor-phone', '1');


-- user variables
INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('2000', '0', '0', '', '0', '', 'applier', '发起人', 'node_user', 'bean_id', 'flow-variable-applier', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('2001', '0', '0', '', '0', '', 'prefixProcessor', '上一节点处理人', 'node_user', 'bean_id', 'flow-variable-prefix-node-processor', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('2002', '0', '0', '', '0', '', 'currProcessor', '本节点处理人', 'node_user', 'bean_id', 'flow-variable-current-node-processor', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('2003', '0', '0', '', '0', '', 'nextProcessor', '下个节点处理人', 'node_user', 'bean_id', 'flow-variable-next-node-processor', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('2004', '0', '0', '', '0', '', 'numberProcessor', 'N节点处理人', 'node_user', 'bean_id', 'flow-variable-n-node-processor', '1');

INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('2005', '0', '0', '', '0', '', 'supervisor', '督办', 'node_user', 'bean_id', 'flow-variable-supervisor', '1');


UPDATE `eh_web_menus` SET `module_id` = 10850 WHERE `path` LIKE '%/10850/%';
UPDATE `eh_web_menus` SET `module_id` = 20100 WHERE `path` LIKE '%/20100/%';
UPDATE `eh_web_menus` SET `module_id` = 20400 WHERE `path` LIKE '%/20400/%';
UPDATE `eh_web_menus` SET `module_id` = 20600 WHERE `path` LIKE '%/20600/%';
UPDATE `eh_web_menus` SET `module_id` = 20800 WHERE `path` LIKE '%/20800/%';
UPDATE `eh_web_menus` SET `module_id` = 40100 WHERE `path` LIKE '%/40100/%';
UPDATE `eh_web_menus` SET `module_id` = 40200 WHERE `path` LIKE '%/40200/%';
UPDATE `eh_web_menus` SET `module_id` = 40400 WHERE `path` LIKE '%/40400/%';
UPDATE `eh_web_menus` SET `module_id` = 40500 WHERE `path` LIKE '%/40500/%';
UPDATE `eh_web_menus` SET `module_id` = 40800 WHERE `path` LIKE '%/40800/%';
UPDATE `eh_web_menus` SET `module_id` = 41000 WHERE `path` LIKE '%/41000/%';
UPDATE `eh_web_menus` SET `module_id` = 41200 WHERE `path` LIKE '%/41200/%';
UPDATE `eh_web_menus` SET `module_id` = 41300 WHERE `path` LIKE '%/41300/%';
UPDATE `eh_web_menus` SET `module_id` = 41400 WHERE `path` LIKE '%/41400/%';
UPDATE `eh_web_menus` SET `module_id` = 10750 WHERE `path` LIKE '%/10750/%';

-- 创源 服务热线
SET @id := (SELECT MAX(id) FROM eh_launch_pad_items);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
VALUES((@id := @id+1),'999986','0','0','0','/home','Bizs','SERVICE_HOT_LINE','咨询热线','cs://1/image/aW1hZ2UvTVRvME1UWXpZak01WkdSa05USmxNekppT1RWaVlUa3lZemt3WkRabFlUSXhZZw','1','1','45','','0','0','1','0','','0',NULL,NULL,NULL,'1','pm_admin','0',NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
VALUES((@id := @id+1),'999986','0','0','0','/home','Bizs','SERVICE_HOT_LINE','咨询热线','cs://1/image/aW1hZ2UvTVRvME1UWXpZak01WkdSa05USmxNekppT1RWaVlUa3lZemt3WkRabFlUSXhZZw','1','1','45','','0','0','1','0','','0',NULL,NULL,NULL,'1','park_tourist','0',NULL);



-- 停车充值
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ( 'parking', '10011', 'zh_CN', '抱歉，你申请的月卡数量已到上限');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ( 'parking', '10012', 'zh_CN', '发放月卡资格数量不可大于当前剩余月卡数');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ( 'parking', '10013', 'zh_CN', '发放月卡资格数量不可大于当前排队数');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ( 'parking', '10014', 'zh_CN', '操作失败，当前无剩余月卡');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ( 'parking', '10015', 'zh_CN', '发放月卡数量不可大于当前剩余月卡数');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ( 'parking', '10016', 'zh_CN', '发放月卡数量不可大于当前待办理月卡数');
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `card_reserve_days`, `status`, `creator_uid`, `create_time`, `max_request_num`, `tempfee_flag`, `rate_flag`, `recharge_month_count`, `recharge_type`, `namespace_id`, `is_support_recharge`)
	VALUES ('10006', 'community', '240111044331055940', '科兴科学园停车场', 'KETUO2', NULL, '41', '2', '1025', '2016-12-16 17:07:20', '2', '0', '0', '2', '2', '0', '0');
SET @eh_configurations := (SELECT MAX(id) FROM eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ((@eh_configurations := @eh_configurations+1), 'parking.kexing.url', 'http://220.160.111.114:9090', '科兴停车充值key', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ((@eh_configurations := @eh_configurations+1), 'parking.kexing.key', 'F7A0B971B199FD2A1017CEC5', '科兴停车充值key', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ((@eh_configurations := @eh_configurations+1), 'parking.kexing.user', 'ktapi', '科兴停车充值用户名', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ((@eh_configurations := @eh_configurations+1), 'parking.kexing.pwd', '0306A9', '科兴停车充值密码', '0', NULL);


-- 更新 资源预订  默认参数 菜单 data_type add by sw 20161215
UPDATE eh_web_menus SET data_type = 'resource--defaultParameter' WHERE id = 40410;

-- 科兴菜单 by sfyan 20161216
DELETE FROM eh_web_menu_scopes WHERE menu_id IN (20400,20410,20420) AND owner_type = 'EhNamespaces' AND owner_id = 999983;
UPDATE eh_web_menu_scopes SET apply_policy = 1, menu_name = '投诉建议' WHERE menu_id = 20100 AND owner_type = 'EhNamespaces' AND owner_id = 999983;

UPDATE eh_web_menu_scopes SET apply_policy = 1, menu_name = '充值管理' WHERE menu_id = 40810 AND owner_type = 'EhNamespaces' AND owner_id = 999983;

--
-- 参数检查错误模板  add by xq.tian  2016/12/16
--
SET @eh_locale_strings = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parameters.error', '10001', 'zh_CN', '参数长度超过限制');

-- 添加工作流菜单  add by sw 20161216
-- 添加工作流菜单  add by sw 20161216
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('70000', '任务管理', '0', '/70000', '2', '2', '2', '0', UTC_TIMESTAMP());

INSERT INTO `eh_web_menus` VALUES ('40850', '工作流设置', '40800', NULL, 'react:/working-flow/flow-list/parking-payment/40800', '0', '2', '/40000/40800/40850', 'park', '475', 40800);
SET @eh_web_menu_privileges = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10028', '40850', '停车缴费', '1', '1', '停车缴费 全部权限', '710');

INSERT INTO `eh_web_menus` VALUES ('70000', '任务管理', '0', 'fa fa-group', NULL, '1', '2', '/70000', 'park', '600', 70000);

INSERT INTO `eh_web_menus` VALUES ('70100', '任务列表', '70000', NULL, 'react:/task-management/task-list/70100', '0', '2', '/70000/70100', 'park', '610', 70000);
INSERT INTO `eh_web_menus` VALUES ('70200', '业务授权视图', '70000', NULL, 'flow_view', '0', '2', '/70000/70200', 'park', '620', 70000);

SET @eh_web_menu_privileges = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10028', '40850', '停车缴费', '1', '1', '停车缴费 全部权限', '710');

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10078, '0', '任务管理 管理员', '任务管理 业务模块权限', NULL);


INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10078', '70000', '任务管理', '1', '1', '任务管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10078', '70100', '任务管理', '1', '1', '任务管理 全部权限', '711');
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10078', '70200', '任务管理', '1', '1', '任务管理 全部权限', '712');

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 40850, '', 'EhNamespaces', 1000000, 2);
INSERT INTO `eh_web_menu_scopes`    (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 70000, '', 'EhNamespaces', 1000000, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 70100, '', 'EhNamespaces', 1000000, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 70200, '', 'EhNamespaces', 1000000, 2);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`, `role_type`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `id`, 1001,0,1,NOW(), 'EhAclRoles' FROM `eh_acl_privileges` WHERE id = 10078 ;

-- add target node processor variable, by Janson 20161216
INSERT INTO `eh_flow_variables`
(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ('2006', '0', '0', '', '0', '', 'targetProcessor', '目标节点处理人', 'node_user', 'bean_id', 'flow-variable-target-node-processor', '1');


-- 添加消息标题 by lqs 20161217
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'message.title', '左邻App', '消息标题：左邻APP');
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (999993, 'message.title', '海岸馨服务', '消息标题：海岸馨服务');

-- 科兴菜单 by sfyan 20161219
UPDATE eh_web_menus SET NAME = '充值管理' WHERE id = 40810;
DELETE FROM eh_web_menu_scopes WHERE menu_id IN (40750) AND owner_type = 'EhNamespaces' AND owner_id = 999983;


-- 资源预订工作流模板，add by wh, 20161219
SET @id := (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES (@id:=@id+1, 'rental.flow', 1, 'zh_CN', '工作流列表内容', '资源名称：${resourceName}\n使用时间：${useDetail}', 0);

-- 资源预订工作流中文，added by wh ,2016-12-19
SET @id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'user', 'zh_CN', '发起人');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'contact', 'zh_CN', '联系电话');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'organization', 'zh_CN', '企业');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'resourceName', 'zh_CN', '资源名称');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'useDetail', 'zh_CN', '使用时间');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'count', 'zh_CN', '预约数量');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'price', 'zh_CN', '订单金额');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'item', 'zh_CN', '购买商品');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'content', 'zh_CN', '显示内容');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'license', 'zh_CN', '车牌');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'remark', 'zh_CN', '备注');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'rental.flow', 'attachment', 'zh_CN', '附件');

-- 资源预订新增工作流菜单  add by sw 20161220
INSERT INTO `eh_web_menus` VALUES ('40450', '工作流设置', '40400', NULL, 'react:/working-flow/flow-list/resource-reservation/40400', '0', '2', '/40000/40400/40450', 'park', '475', 40800);
SET @eh_web_menu_privileges = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10023', '40450', '资源预订', '1', '1', '资源预订 全部权限', '710');
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 40450, '', 'EhNamespaces', 1000000, 2);



-- 要上线的app版本 by sfyan 20161220
SET @app_version_id = (SELECT MAX(id) FROM `eh_app_version`);
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES((@app_version_id := @app_version_id + 1),'android','3.12.2','','0','3156995','2016-12-01 14:57:56');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES((@app_version_id := @app_version_id + 1),'ios','3.12.2','','0','2097156','2016-12-01 14:57:56');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES((@app_version_id := @app_version_id + 1),'android','3.12.2','','1000000','3156995','2016-12-01 14:57:56');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES((@app_version_id := @app_version_id + 1),'ios','3.12.2','','1000000','2097156','2016-12-01 14:57:56');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES((@app_version_id := @app_version_id + 1),'android','3.12.2','','999988','3156995','2016-12-01 14:57:56');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES((@app_version_id := @app_version_id + 1),'ios','3.12.2','','999988','2097156','2016-12-01 14:57:56');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES((@app_version_id := @app_version_id + 1),'android','3.12.2','','999983','3156995','2016-12-01 14:57:56');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES((@app_version_id := @app_version_id + 1),'ios','3.12.2','','999983','2097156','2016-12-01 14:57:56');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES((@app_version_id := @app_version_id + 1),'android','3.12.2','','999984','3156995','2016-12-01 14:57:56');
INSERT INTO `eh_app_version` (`id`, `type`, `name`, `realm`, `namespace_id`, `default_order`, `create_time`) VALUES((@app_version_id := @app_version_id + 1),'ios','3.12.2','','999984','2097156','2016-12-01 14:57:56');


--
-- 园区入驻 2.3  add by xq.tian  2016/12/20
--
SET @eh_locale_templates = (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@eh_locale_templates := @eh_locale_templates + 1), 'expansion', '1', 'zh_CN', '园区入驻工作流摘要内容', '申请类型: ${applyType}\n面积需求: ${areaSize} 平米', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@eh_locale_templates := @eh_locale_templates + 1), 'expansion', '2', 'zh_CN', '园区入驻工作流详情内容', '[{"key":"发起人","value":"${applyUserName}","entityType":"list"},{"key":"联系电话","value":"${contactPhone}","entityType":"list"},{"key":"企业","value":"${enterpriseName}","entityType":"list"},{"key":"申请类型","value":"${applyType}","entityType":"list"},{"key":"面积需求","value":"${areaSize} 平米","entityType":"list"},{"key":"申请来源","value":"${sourceType}","entityType":"list"},{"key":"备注","value":"${description}","entityType":"multi_line"}]', '0');

SET @eh_locale_strings = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'expansion.applyType', '1', 'zh_CN', '入驻申请');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'expansion.applyType', '2', 'zh_CN', '扩租申请');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'expansion.applyType', '3', 'zh_CN', '续租申请');

--
-- 工作流设置菜单
--
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (40130, '工作流设置', 40100, NULL, 'react:/working-flow/flow-list/rent-manage/40100', 0, 2, '/40000/40100/40130', 'park', 419);

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 40130, '', 'EhNamespaces', 1000000, 2);
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 40130, '', 'EhNamespaces', 999983, 2);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10020, 40850, '招租管理', 1, 1, '招租管理 工作流设置 全部权限', 419);

-- Added by Janson 20161220
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES
( 'flow', 10006, 'zh_CN', '用户评价：${score}分', '用户评价：${score}分');

-- 要上线的app版本 by sfyan 20161221
UPDATE `eh_app_version` SET default_order = 3158018.0 WHERE NAME = '3.12.2';

--
-- 修改没有权限时的提示语  add by xq.tian  2016/12/21
--
UPDATE `eh_locale_strings` SET `text`='对不起,您没有权限执行此操作' WHERE (`scope`='general' AND `code`='505');


-- 左邻的http的链接 改成https by sfyan 20161222   建议备份一下表
-- update `eh_launch_pad_items` set `action_data` = replace(`action_data`,'http', 'https') where `action_data` like '%zuolin%' and  `action_data` like '%http:%';
-- update `eh_banners` set `action_data` = replace(`action_data`,'http', 'https') where `action_data` like '%zuolin%' and  `action_data` like '%http:%';
-- update `eh_configurations` set `value` = replace(`value`,'http', 'https')  where `value` like '%zuolin%' and  `value` like '%http:%' and `value` like '%.html%';


-- 增加企业后台的 管理员管理 by sfyan 20161226
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) VALUES('60400','管理员管理','60000',NULL,'react:/other-admin-management/admin','0','2','/60000/60400','park','361',60400);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10095, 0, '普通企业管理员管理', '普通企业管理员管理 全部权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10095, 60400, '普通企业管理员管理', 1, 1, '普通企业管理员管理 全部权限', 361);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 10095, 1005,'EhAclRoles', 0, 1, NOW());


-- 整理业务模块的scope
UPDATE `eh_service_module_scopes` SET `owner_type` = null, `owner_id` = null  where `owner_type` = 'EhNamespaces';

-- 科技园添加服务广场任务管理 add by sw 20161227
SET @eh_launch_pad_items = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '1000000', '0', '0', '0', '/home', 'Bizs', 'FLOW_TASKS', '任务管理', 'cs://1/image/aW1hZ2UvTVRveE4yVmxOak0wWkdReU9UY3dPVGMzTlRrM05UWmxOV1U1TVRneFltTTVaZw', '1', '1', '56', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '1', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '1000000', '0', '0', '0', '/home', 'Bizs', 'FLOW_TASKS', '任务管理', 'cs://1/image/aW1hZ2UvTVRveE4yVmxOak0wWkdReU9UY3dPVGMzTlRrM05UWmxOV1U1TVRneFltTTVaZw', '1', '1', '56', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '1', NULL);


-- remove next processor and n processor by Janson
delete from eh_flow_variables where id=2003;
delete from eh_flow_variables where id=2004;


-- merge from sa1.8 by xiongying
update eh_settle_requests set template_type = 'Settle';
update eh_service_alliance_requests set template_type = 'ServiceAlliance';
update eh_service_alliance_apartment_requests set template_type = 'Apartment';

-- SET @eh_request_templates = (SELECT MAX(id) FROM `eh_request_templates`);
INSERT INTO `eh_request_templates` (`id`, `template_type`, `name`, `button_title`, `email_flag`, `msg_flag`, `fields_json`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`) 
    VALUES (1, 'Invest', '我要投资', '我有意向', '1', '1', '{"fields":[{"fieldName":"name","fieldDisplayName":"姓名","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入您的姓名","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"mobile","fieldDisplayName":"手机号","fieldType":"number","fieldContentType":"text","fieldDesc":"请输入您的手机号","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"organizationName","fieldDisplayName":"公司","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入您的公司","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"financingAmount","fieldDisplayName":"意向投资金额（万元）","fieldType":"decimal","fieldContentType":"text","fieldDesc":"","requiredFlag":"1"},{"fieldName":"industry","fieldDisplayName":"投资行业","fieldType":"string","fieldContentType":"text","fieldDesc":"","requiredFlag":"1"},{"fieldName":"investPeriod","fieldDisplayName":"投资年限","fieldType":"number","fieldContentType":"text","fieldDesc":"","requiredFlag":"1"},{"fieldName":"annualYield","fieldDisplayName":"预期年化收益（%）","fieldType":"decimal","fieldContentType":"text","fieldDesc":"","requiredFlag":"1"},{"fieldName":"remarks","fieldDisplayName":"备注","fieldType":"string","fieldContentType":"text","fieldDesc":"（选填）其他说明","requiredFlag":"0"}]}', '1', '1', UTC_TIMESTAMP(), '0', NULL);

INSERT INTO `eh_request_templates` (`id`, `template_type`, `name`, `button_title`, `email_flag`, `msg_flag`, `fields_json`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`) 
    VALUES (2, 'ServiceAllianceFinancing', '我要融资', '我有意向', '1', '1', '{"fields":[{"fieldName":"name","fieldDisplayName":"姓名","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入您的姓名","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"mobile","fieldDisplayName":"手机号","fieldType":"number","fieldContentType":"text","fieldDesc":"请输入您的手机号","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"organizationName","fieldDisplayName":"公司","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入您的公司","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"financingAmount","fieldDisplayName":"意向融资金额（万元）","fieldType":"decimal","fieldContentType":"text","fieldDesc":"","requiredFlag":"1"},{"fieldName":"projectDesc","fieldDisplayName":"担保物","fieldType":"string","fieldContentType":"text","fieldDesc":"选填","requiredFlag":"0"},{"fieldName":"remarks","fieldDisplayName":"备注","fieldType":"string","fieldContentType":"text","fieldDesc":"（选填）其他说明","requiredFlag":"0"}]}', '1', '1', UTC_TIMESTAMP(), '0', NULL);
INSERT INTO `eh_request_templates` (`id`, `template_type`, `name`, `button_title`, `email_flag`, `msg_flag`, `fields_json`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`) 
    VALUES (3, 'ServiceAllianceProjcet', '我有项目', '我有意向', '1', '1', '{"fields":[{"fieldName":"name","fieldDisplayName":"姓名","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入您的姓名","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"mobile","fieldDisplayName":"手机号","fieldType":"number","fieldContentType":"text","fieldDesc":"请输入您的手机号","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"organizationName","fieldDisplayName":"公司","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入您的公司","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"projectDesc","fieldDisplayName":"项目名称","fieldType":"string","fieldContentType":"text","fieldDesc":"","requiredFlag":"1"},{"fieldName":"transferShares","fieldDisplayName":"意向出让股份比例（%）","fieldType":"number","fieldContentType":"text","fieldDesc":"","requiredFlag":"1"},{"fieldName":"financingAmount","fieldDisplayName":"融资金额（万元）","fieldType":"decimal","fieldContentType":"text","fieldDesc":"","requiredFlag":"1"},{"fieldName":"remarks","fieldDisplayName":"备注","fieldType":"string","fieldContentType":"text","fieldDesc":"（选填）其他说明","requiredFlag":"0"}]}', '1', '1', UTC_TIMESTAMP(), '0', NULL);
INSERT INTO `eh_request_templates` (`id`, `template_type`, `name`, `button_title`, `email_flag`, `msg_flag`, `fields_json`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`) 
    VALUES (4, 'ServiceAllianceTech', '科技成果', '我有意向', '1', '1', '{"fields":[{"fieldName":"name","fieldDisplayName":"姓名","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入您的姓名","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"mobile","fieldDisplayName":"手机号","fieldType":"number","fieldContentType":"text","fieldDesc":"请输入您的手机号","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"organizationName","fieldDisplayName":"公司","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入您的公司","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"financingAmount","fieldDisplayName":"拟投资金额（百万元）","fieldType":"decimal","fieldContentType":"text","fieldDesc":"","requiredFlag":"1"},{"fieldName":"remarks","fieldDisplayName":"备注","fieldType":"string","fieldContentType":"text","fieldDesc":"（选填）其他说明","requiredFlag":"0"}]}', '1', '1', UTC_TIMESTAMP(), '0', NULL);
    
INSERT INTO `eh_request_templates` (`id`, `template_type`, `name`, `button_title`, `email_flag`, `msg_flag`, `fields_json`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`) 
    VALUES (5, 'SettleIncubator', '星空孵化器', '我有意向', '1', '1', '{"fields":[{"fieldName":"name","fieldDisplayName":"姓名","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入您的姓名","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"mobile","fieldDisplayName":"手机号","fieldType":"number","fieldContentType":"text","fieldDesc":"请输入您的手机号","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"stringTag1","fieldDisplayName":"项目名称","fieldType":"string","fieldContentType":"text","fieldDesc":"","requiredFlag":"1"},{"fieldName":"integralTag1","fieldDisplayName":"意向工位数","fieldType":"number","fieldContentType":"text","fieldDesc":"","requiredFlag":"1"},{"fieldName":"remarks","fieldDisplayName":"备注","fieldType":"string","fieldContentType":"text","fieldDesc":"（选填）其他说明","requiredFlag":"0"}]}', '1', '1', UTC_TIMESTAMP(), '0', NULL);
INSERT INTO `eh_request_templates` (`id`, `template_type`, `name`, `button_title`, `email_flag`, `msg_flag`, `fields_json`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`) 
    VALUES (6, 'SettleResearchPlat', '研发平台', '我有意向', '1', '1', '{"fields":[{"fieldName":"name","fieldDisplayName":"姓名","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入您的姓名","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"mobile","fieldDisplayName":"手机号","fieldType":"number","fieldContentType":"text","fieldDesc":"请输入您的手机号","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"organizationName","fieldDisplayName":"公司","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入您的公司","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"stringTag1","fieldDisplayName":"项目名称","fieldType":"string","fieldContentType":"text","fieldDesc":"","requiredFlag":"1"},{"fieldName":"remarks","fieldDisplayName":"备注","fieldType":"string","fieldContentType":"text","fieldDesc":"（选填）其他说明","requiredFlag":"0"}]}', '1', '1', UTC_TIMESTAMP(), '0', NULL);
INSERT INTO `eh_request_templates` (`id`, `template_type`, `name`, `button_title`, `email_flag`, `msg_flag`, `fields_json`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`) 
    VALUES (7, 'SettleProfessionalService', '专业服务', '我有意向', '1', '1', '{"fields":[{"fieldName":"name","fieldDisplayName":"姓名","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入您的姓名","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"mobile","fieldDisplayName":"手机号","fieldType":"number","fieldContentType":"text","fieldDesc":"请输入您的手机号","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"organizationName","fieldDisplayName":"公司","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入您的公司","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"stringTag1","fieldDisplayName":"企业需求","fieldType":"string","fieldContentType":"text","fieldDesc":"（必填）请输入您的需求","requiredFlag":"1"},{"fieldName":"remarks","fieldDisplayName":"备注","fieldType":"string","fieldContentType":"text","fieldDesc":"（选填）其他说明","requiredFlag":"0"}]}', '1', '1', UTC_TIMESTAMP(), '0', NULL);
INSERT INTO `eh_request_templates` (`id`, `template_type`, `name`, `button_title`, `email_flag`, `msg_flag`, `fields_json`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`) 
    VALUES (8, 'SettleConsultationCooperation', '载物微咨询和国际合作', '我有意向', '1', '1', '{"fields":[{"fieldName":"name","fieldDisplayName":"姓名","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入您的姓名","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"mobile","fieldDisplayName":"手机号","fieldType":"number","fieldContentType":"text","fieldDesc":"请输入您的手机号","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"organizationName","fieldDisplayName":"公司","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入您的公司","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"remarks","fieldDisplayName":"备注","fieldType":"string","fieldContentType":"text","fieldDesc":"（选填）其他说明","requiredFlag":"0"}]}', '1', '1', UTC_TIMESTAMP(), '0', NULL);
INSERT INTO `eh_request_templates` (`id`, `template_type`, `name`, `button_title`, `email_flag`, `msg_flag`, `fields_json`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`) 
    VALUES (9, 'SettleFundSupport', '资金扶持', '我有意向', '1', '1', '{"fields":[{"fieldName":"name","fieldDisplayName":"姓名","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入您的姓名","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"mobile","fieldDisplayName":"手机号","fieldType":"number","fieldContentType":"text","fieldDesc":"请输入您的手机号","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"organizationName","fieldDisplayName":"公司","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入您的公司","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"remarks","fieldDisplayName":"备注","fieldType":"string","fieldContentType":"text","fieldDesc":"（选填）其他说明","requiredFlag":"0"}]}', '1', '1', UTC_TIMESTAMP(), '0', NULL);

    
update eh_service_alliances set integral_tag1 = '1' where string_tag2 in(select template_type from eh_request_templates);




-- 企业管理员管理菜单scope配置
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
SELECT (@menu_scope_id := @menu_scope_id + 1), 60400, '', 'EhNamespaces', id, 2 FROM `eh_namespaces`;

-- 更新菜单
update eh_web_menus set name = '入驻申请' where id = 40120; 
-- 储能 工作流设置菜单
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 40850, '', 'EhNamespaces', 999990, 2);

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);	
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20650, '', 'EhNamespaces', 999992, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20660, '', 'EhNamespaces', 999992, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20661, '', 'EhNamespaces', 999992, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20662, '', 'EhNamespaces', 999992, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20670, '', 'EhNamespaces', 999992, 2);

