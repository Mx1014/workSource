-- 配置参数  by sfyan 20160810
SET @configuration_id = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO eh_configurations(`id`,`name`,`value`,`description`,`namespace_id`)VALUES((@configuration_id := @configuration_id + 1), 'statistics.cron.expression','0 0 1 * * ?','schedule cron expression',0);
INSERT INTO eh_configurations(`id`,`name`,`value`,`description`,`namespace_id`)VALUES((@configuration_id := @configuration_id + 1), 'stat.biz.server.url','http://biz.zuolin.com/','电商服务地址',0);
INSERT INTO eh_configurations(`id`,`name`,`value`,`description`,`namespace_id`)VALUES((@configuration_id := @configuration_id + 1), 'stat.paid.server.url','http://pay.zuolin.com/','支付服务地址',0);
INSERT INTO eh_configurations(`id`,`name`,`value`,`description`,`namespace_id`)VALUES((@configuration_id := @configuration_id + 1), 'stat.biz.appkey','39628d1c-0646-4ff6-9691-2c327b03f9c4','电商appkey',0);
INSERT INTO eh_configurations(`id`,`name`,`value`,`description`,`namespace_id`)VALUES((@configuration_id := @configuration_id + 1), 'stat.biz.secretkey','PSsIB9nZm3ENS3stei8oAvGa2afRW7wT+UxBn9li4C7JCfjCtHYJY6x76XDtUCUcXOUhkPYK9V/5r03pD2rquQ==','电商秘钥',0);
INSERT INTO eh_configurations(`id`,`name`,`value`,`description`,`namespace_id`)VALUES((@configuration_id := @configuration_id + 1), 'stat.paid.appkey','7bbb5727-9d37-443a-a080-55bbf37dc8e1','支付appkey',0);
INSERT INTO eh_configurations(`id`,`name`,`value`,`description`,`namespace_id`)VALUES((@configuration_id := @configuration_id + 1), 'stat.paid.secretkey','1k0ty3aZPC8bjMm8V9+pFmsU5B7cImfQXB4GUm4ACSFPP1IhZI5basNbUBXe7p6gJ7OC8J03DW1U8fvvtpim6Q==','支付秘钥',0);
INSERT INTO eh_configurations(`id`,`name`,`value`,`description`,`namespace_id`)VALUES((@configuration_id := @configuration_id + 1), 'get.paid.order.api','zl-ec/rest/openapi/order/listPaidOrders','查询支付订单',0);
INSERT INTO eh_configurations(`id`,`name`,`value`,`description`,`namespace_id`)VALUES((@configuration_id := @configuration_id + 1), 'get.refund.order.api','zl-ec/rest/openapi/refundOrder/listRefundOrders','查询退款订单',0);
INSERT INTO eh_configurations(`id`,`name`,`value`,`description`,`namespace_id`)VALUES((@configuration_id := @configuration_id + 1), 'get.transaction.api','EDS_PAY/rest/pay_common/payInfo_record/listPaidTransactions','查询支付流水',0);
INSERT INTO eh_configurations(`id`,`name`,`value`,`description`,`namespace_id`)VALUES((@configuration_id := @configuration_id + 1), 'get.refund.api','EDS_PAY/rest/pay_common/refund/listRefundTransactions','查询退款流水',0);

-- 结算菜单  by sfyan 20160810
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (49000,'结算管理',40000,NULL,'settlement_management',1,2,'/40000/49000','park',454);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (770,0,'结算管理','结算管理 全部功能',NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),770,49000,'结算管理',0,1,'结算管理',349);

-- 修改停车充值费率
UPDATE eh_parking_recharge_rates SET rate_name='3个月', card_type = '普通月卡' WHERE id = 10001;
UPDATE eh_parking_recharge_rates SET rate_name='6个月', card_type = '普通月卡' WHERE id = 10002;
UPDATE eh_parking_recharge_rates SET rate_name='3个月', card_type = '固定车位卡' WHERE id = 10003;
UPDATE eh_parking_recharge_rates SET rate_name='6个月', card_type = '固定车位卡' WHERE id = 10004;
SET @eh_locale_templates_id = (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'park.notification', '2', 'zh_CN', '停车充值默认费率', '${count}个月', '0');

-- 园区电子报初始数据
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
VALUES ((@configuration_id := @configuration_id + 1), 'journal.posterPath', 'cs://1/image/aW1hZ2UvTVRveU4yRXpNbVEzWXpCaU16azFaVE5pT0RBNFkyVmxNRFkzTmpRNE5EVm1aZw', NULL, '0', NULL);

SET @eh_launch_pad_items_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 999991, 0, 0, 0, '/home', 'Bizs', '威新视界', '威新视界', 'cs://1/image/aW1hZ2UvTVRwbU1qYzFObUptTm1JNU5EUXhOalJsTldVMU9UZG1NR1UxTm1NNVlqSXhZUQ', 1, 1, 14, '{"url":"http://core.zuolin.com/park-paper/index.html?hideNavigationBar=1#/epaper_index#sign_suffix"}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 999991, 0, 0, 0, '/home', 'Bizs', '威新视界', '威新视界', 'cs://1/image/aW1hZ2UvTVRwbU1qYzFObUptTm1JNU5EUXhOalJsTldVMU9UZG1NR1UxTm1NNVlqSXhZUQ', 1, 1, 14, '{"url":"http://core.zuolin.com/park-paper/index.html?hideNavigationBar=1#/epaper_index#sign_suffix"}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, '1', 'pm_admin');

-- 园区报 by sfyan 20160811
SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (780,0,'园区报管理','园区报管理 全部功能',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (781,0,'约稿须知','约稿须知  全部功能',NULL);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (49500,'园区报',40000,NULL,NULL,1,2,'/40000/43400','park',453);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (49510,'园区报管理',49500,NULL,'park_epaper_management',0,2,'/40000/49500/49510','park',452);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (49520,'约稿须知',49500,NULL,'manuscripts_notice',0,2,'/40000/49500/49520','park',453);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),780,49510,'园区报管理',1,1,'园区报管理  全部权限',346);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),781,49520,'约稿须知',1,1,'约稿须知 全部权限',347);

-- 除了威新 其他屏蔽
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 49500,'', 'EhNamespaces', 1000000 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',1000000,0  FROM `eh_web_menus` WHERE `path` LIKE '%49500/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 49500,'', 'EhNamespaces', 999999 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999999,0  FROM `eh_web_menus` WHERE `path` LIKE '%49500/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 49500,'', 'EhNamespaces', 999989 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999989,0  FROM `eh_web_menus` WHERE `path` LIKE '%49500/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 49500,'', 'EhNamespaces', 999992 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999992,0  FROM `eh_web_menus` WHERE `path` LIKE '%49500/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 49500,'', 'EhNamespaces', 999993 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999993,0  FROM `eh_web_menus` WHERE `path` LIKE '%49500/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 49500,'', 'EhNamespaces', 999990 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999990,0  FROM `eh_web_menus` WHERE `path` LIKE '%49500/%';

 
-- 打卡2.0 新规则下的旧数据迁移
-- 插入 工作日规则表

INSERT INTO `eh_punch_workday_rules` (`id`, `owner_type`, `owner_id`, `name`, `description`, `work_week_dates`, `creator_uid`, `create_time`) VALUES('1','organization','178395','正常工作日','周一到周五上班,周末放假','0111110','195506','2016-08-12 14:13:59');
INSERT INTO `eh_punch_workday_rules` (`id`, `owner_type`, `owner_id`, `name`, `description`, `work_week_dates`, `creator_uid`, `create_time`) VALUES('2','organization','178945','正常工作日','周一到周五上班,周末放假','0111110','195506','2016-08-12 14:13:59');
INSERT INTO `eh_punch_workday_rules` (`id`, `owner_type`, `owner_id`, `name`, `description`, `work_week_dates`, `creator_uid`, `create_time`) VALUES('3','organization','1000001','正常工作日','周一到周五上班,周末放假','0111110','195506','2016-08-12 14:13:59');
INSERT INTO `eh_punch_workday_rules` (`id`, `owner_type`, `owner_id`, `name`, `description`, `work_week_dates`, `creator_uid`, `create_time`) VALUES('4','organization','180041','正常工作日','周一到周五上班,周末放假','0111110','203600','2016-08-12 14:13:59');
INSERT INTO `eh_punch_workday_rules` (`id`, `owner_type`, `owner_id`, `name`, `description`, `work_week_dates`, `creator_uid`, `create_time`) VALUES('5','organization','1000750','正常工作日','周一到周五上班,周末放假','0111110','212500','2016-08-12 14:13:59');
INSERT INTO `eh_punch_workday_rules` (`id`, `owner_type`, `owner_id`, `name`, `description`, `work_week_dates`, `creator_uid`, `create_time`) VALUES('6','organization','1001706','正常工作日','周一到周五上班,周末放假','0111110','222990','2016-08-12 14:13:59');

-- 插入 时间规则表

INSERT INTO `eh_punch_time_rules` (`id`, `owner_type`, `owner_id`, `name`, `start_early_time`, `start_late_time`, `work_time`, `noon_leave_time`, `afternoon_arrive_time`, `punch_times_per_day`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES('1','organization','178395','时间规则','08:45:00','08:45:00','09:00:00','12:00:00','14:00:00','4','195506','2015-12-09 15:56:45',NULL,NULL);
INSERT INTO `eh_punch_time_rules` (`id`, `owner_type`, `owner_id`, `name`, `start_early_time`, `start_late_time`, `work_time`, `noon_leave_time`, `afternoon_arrive_time`, `punch_times_per_day`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES('2','organization','178945','时间规则','08:30:00','09:30:00','09:30:00','12:00:00','13:30:00','2','195606','2015-12-16 14:49:05',NULL,NULL);
INSERT INTO `eh_punch_time_rules` (`id`, `owner_type`, `owner_id`, `name`, `start_early_time`, `start_late_time`, `work_time`, `noon_leave_time`, `afternoon_arrive_time`, `punch_times_per_day`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES('3','organization','1000001','时间规则','08:30:00','08:45:00','09:00:00','12:00:00','14:00:00','4','195506','2016-03-09 14:08:02',NULL,NULL);
INSERT INTO `eh_punch_time_rules` (`id`, `owner_type`, `owner_id`, `name`, `start_early_time`, `start_late_time`, `work_time`, `noon_leave_time`, `afternoon_arrive_time`, `punch_times_per_day`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES('4','organization','180041','时间规则','07:45:00','08:45:00','09:15:00','12:00:00','14:00:00','2','203600','2016-04-20 11:50:29',NULL,NULL);
INSERT INTO `eh_punch_time_rules` (`id`, `owner_type`, `owner_id`, `name`, `start_early_time`, `start_late_time`, `work_time`, `noon_leave_time`, `afternoon_arrive_time`, `punch_times_per_day`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES('5','organization','1000750','时间规则','08:00:00','08:45:00','09:05:00','12:00:00','14:00:00','2','212500','2016-05-05 11:54:00',NULL,NULL);
INSERT INTO `eh_punch_time_rules` (`id`, `owner_type`, `owner_id`, `name`, `start_early_time`, `start_late_time`, `work_time`, `noon_leave_time`, `afternoon_arrive_time`, `punch_times_per_day`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES('6','organization','1001706','时间规则','08:00:00','19:00:00','11:00:00','13:00:00','13:00:00','2','222990','2016-07-19 09:49:44',NULL,NULL);

-- 插入 地点规则表

INSERT INTO `eh_punch_location_rules` (`id`, `owner_type`, `owner_id`, `name`, `description`, `creator_uid`, `create_time`) VALUES('1','organization','178395','地点规则','地点规则','195506','2016-08-12 14:11:50');
INSERT INTO `eh_punch_location_rules` (`id`, `owner_type`, `owner_id`, `name`, `description`, `creator_uid`, `create_time`) VALUES('2','organization','178945','地点规则','地点规则','195506','2016-08-12 14:11:52');
INSERT INTO `eh_punch_location_rules` (`id`, `owner_type`, `owner_id`, `name`, `description`, `creator_uid`, `create_time`) VALUES('3','organization','1000001','地点规则','地点规则','195506','2016-08-12 14:11:54');
INSERT INTO `eh_punch_location_rules` (`id`, `owner_type`, `owner_id`, `name`, `description`, `creator_uid`, `create_time`) VALUES('4','organization','180041','地点规则','地点规则','203600','2016-08-12 14:11:55');
INSERT INTO `eh_punch_location_rules` (`id`, `owner_type`, `owner_id`, `name`, `description`, `creator_uid`, `create_time`) VALUES('5','organization','1000750','地点规则','地点规则','212500','2016-08-12 14:11:57');
INSERT INTO `eh_punch_location_rules` (`id`, `owner_type`, `owner_id`, `name`, `description`, `creator_uid`, `create_time`) VALUES('6','organization','1001706','地点规则','地点规则','222990','2016-08-12 14:11:58');

-- 更新 具体地点表
 
UPDATE `eh_punch_geopoints`  SET owner_type = 'organization' ,owner_id = enterprise_id , location_rule_id =1 WHERE enterprise_id=178395; 
UPDATE `eh_punch_geopoints`  SET owner_type = 'organization' ,owner_id = enterprise_id , location_rule_id =2 WHERE enterprise_id=178945;
UPDATE `eh_punch_geopoints`  SET owner_type = 'organization' ,owner_id = enterprise_id , location_rule_id =3 WHERE enterprise_id=1000001;
UPDATE `eh_punch_geopoints`  SET owner_type = 'organization' ,owner_id = enterprise_id , location_rule_id =4 WHERE enterprise_id=180041;
UPDATE `eh_punch_geopoints`  SET owner_type = 'organization' ,owner_id = enterprise_id , location_rule_id =5 WHERE enterprise_id=1000750;
UPDATE `eh_punch_geopoints`  SET owner_type = 'organization' ,owner_id = enterprise_id , location_rule_id =6 WHERE enterprise_id=1001706;

-- 插入 规则表

INSERT INTO `eh_punch_rules` (`id`, `owner_type`, `owner_id`, `name`, `time_rule_id`, `location_rule_id`, `wifi_rule_id`, `workday_rule_id`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES('1','organization','178395',NULL,'1','1',NULL,'1','195506','2015-12-09 15:56:45',NULL,NULL);
INSERT INTO `eh_punch_rules` (`id`, `owner_type`, `owner_id`, `name`, `time_rule_id`, `location_rule_id`, `wifi_rule_id`, `workday_rule_id`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES('2','organization','178945',NULL,'2','2',NULL,'2','195606','2015-12-16 14:49:05',NULL,NULL);
INSERT INTO `eh_punch_rules` (`id`, `owner_type`, `owner_id`, `name`, `time_rule_id`, `location_rule_id`, `wifi_rule_id`, `workday_rule_id`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES('3','organization','1000001',NULL,'3','3',NULL,'3','195506','2016-03-09 14:08:02',NULL,NULL);
INSERT INTO `eh_punch_rules` (`id`, `owner_type`, `owner_id`, `name`, `time_rule_id`, `location_rule_id`, `wifi_rule_id`, `workday_rule_id`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES('4','organization','180041',NULL,'4','4',NULL,'4','203600','2016-04-20 11:50:29',NULL,NULL);
INSERT INTO `eh_punch_rules` (`id`, `owner_type`, `owner_id`, `name`, `time_rule_id`, `location_rule_id`, `wifi_rule_id`, `workday_rule_id`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES('5','organization','1000750',NULL,'5','5',NULL,'5','212500','2016-05-05 11:54:00',NULL,NULL);
INSERT INTO `eh_punch_rules` (`id`, `owner_type`, `owner_id`, `name`, `time_rule_id`, `location_rule_id`, `wifi_rule_id`, `workday_rule_id`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`) VALUES('6','organization','1001706',NULL,'6','6',NULL,'6','222990','2016-07-19 09:49:44',NULL,NULL);

-- 插入 规则映射表

INSERT INTO `eh_punch_rule_owner_map` (`id`, `owner_type`, `owner_id`, `target_type`, `target_id`, `punch_rule_id`, `creator_uid`, `create_time`) VALUES('1','organization','178395','organization','178395','1','195506','2015-12-09 15:56:45');
INSERT INTO `eh_punch_rule_owner_map` (`id`, `owner_type`, `owner_id`, `target_type`, `target_id`, `punch_rule_id`, `creator_uid`, `create_time`) VALUES('2','organization','178945','organization','178945','2','195606','2015-12-16 14:49:05');
INSERT INTO `eh_punch_rule_owner_map` (`id`, `owner_type`, `owner_id`, `target_type`, `target_id`, `punch_rule_id`, `creator_uid`, `create_time`) VALUES('3','organization','1000001','organization','1000001','3','195506','2016-03-09 14:08:02');
INSERT INTO `eh_punch_rule_owner_map` (`id`, `owner_type`, `owner_id`, `target_type`, `target_id`, `punch_rule_id`, `creator_uid`, `create_time`) VALUES('4','organization','180041','organization','180041','4','203600','2016-04-20 11:50:29');
INSERT INTO `eh_punch_rule_owner_map` (`id`, `owner_type`, `owner_id`, `target_type`, `target_id`, `punch_rule_id`, `creator_uid`, `create_time`) VALUES('5','organization','1000750','organization','1000750','5','212500','2016-05-05 11:54:00');
INSERT INTO `eh_punch_rule_owner_map` (`id`, `owner_type`, `owner_id`, `target_type`, `target_id`, `punch_rule_id`, `creator_uid`, `create_time`) VALUES('6','organization','1001706','organization','1001706','6','222990','2016-07-19 09:49:44');

-- 打卡错误提示
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'punch', '12000', 'zh_CN', '名称重复');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'punch', '12001', 'zh_CN', '规则已经被使用');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'punch', '10003', 'zh_CN', '打卡规则有错');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'punch', '10010', 'zh_CN', '您还没有设置打卡规则，请联系管理员设置');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'punch', '10001', 'zh_CN', '您不在打卡范围，打卡失败');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'punch', '10006', 'zh_CN', '没有获取到您的WIFI，请检测您是否连上WIFI');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'punch', '10007', 'zh_CN', '您没有连上指定的WIFI');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'punch', '10008', 'zh_CN', '您没有连上指定WIFI，也不在打卡范围，打卡失败');
 
-- 设备巡检 add by xiongying 20160812
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
VALUES(7,0,'0','设备类型','设备类型','0','2',UTC_TIMESTAMP(),NULL,NULL,NULL,'0');

SET @category_id = (SELECT MAX(id) FROM `eh_categories`);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
VALUES((@category_id := @category_id + 1),7,'0','消防','设备类型/消防','0','2',UTC_TIMESTAMP(),NULL,NULL,NULL,'0');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
VALUES((@category_id := @category_id + 1),7,'0','强电','设备类型/强电','0','2',UTC_TIMESTAMP(),NULL,NULL,NULL,'0');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
VALUES((@category_id := @category_id + 1),7,'0','弱电','设备类型/弱电','0','2',UTC_TIMESTAMP(),NULL,NULL,NULL,'0');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
VALUES((@category_id := @category_id + 1),7,'0','其他','设备类型/其他','0','2',UTC_TIMESTAMP(),NULL,NULL,NULL,'0');

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10001', 'zh_CN', '设备不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10002', 'zh_CN', '设备没有设置经纬度');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10003', 'zh_CN', '不在设备附近');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10004', 'zh_CN', '设备标准不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10005', 'zh_CN', '设备标准已失效');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10006', 'zh_CN', '设备经纬度不能修改');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10007', 'zh_CN', '设备状态后台不能设为维修中');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10008', 'zh_CN', '设备已失效');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10009', 'zh_CN', '备品备件不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10010', 'zh_CN', '备品备件已失效');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10011', 'zh_CN', '只有已失效的设备-标准关联关系可以删除');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10012', 'zh_CN', '只有待审核的设备-标准关联关系可以审核');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10013', 'zh_CN', '任务不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10014', 'zh_CN', '只有待执行和维修中的任务可以上报');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10015', 'zh_CN', '设备参数记录');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10016', 'zh_CN', '生成excel信息有问题');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10017', 'zh_CN', '下载excel信息有问题');

INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'equipment.notification', 1, 'zh_CN', '生成核查任务', '您有新的巡检任务，请及时处理，截止日期为：“${deadline}”');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'equipment.notification', 2, 'zh_CN', '指派任务给维修人', '您有新的维修任务，请及时处理，截止日期为：“${deadline}”');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'equipment.notification', 3, 'zh_CN', '指派任务记录信息', '“${reviewerName}”分配维修任务给“${operatorName}”，截止日期为：“${deadline}”');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'equipment.notification', 4, 'zh_CN', '设备-标准关联审阅不合格通知','设备“${equipmentName}”被审批为不合格，请及时选择新的标准');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'equipment.notification', 5, 'zh_CN', '设备-标准关联审阅合格通知','设备“${equipmentName}”已审批合格，可生成巡检/保养任务');
