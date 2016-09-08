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
VALUES ((@configuration_id := @configuration_id + 1), 'journal.posterPath', 'cs://1/image/aW1hZ2UvTVRvNVpUYzNNREJpWW1SbE5HRTJaRFF6T1RRMU1qSTJaV1JrTmpoaU5EYzVZZw', NULL, '0', NULL);

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



-- 威新Link服务广场
UPDATE eh_communities SET NAME = '深圳威新' WHERE id = 240111044331053517;
UPDATE eh_groups SET NAME = '深圳威新', display_name = '深圳威新' WHERE id = 1003093;
UPDATE eh_forums SET NAME = '深圳威新' WHERE id = 180772;
UPDATE eh_forums SET NAME = '深圳威新论坛' WHERE id = 180773;
UPDATE eh_forums SET NAME = '深圳威新意见反馈论坛' WHERE id = 180774;
UPDATE eh_launch_pad_items SET icon_uri = 'cs://1/image/aW1hZ2UvTVRveFpXRmtObUkzWWprd05tTXhaREV4WlRJMU1EQmlaVEU1TjJObE9ESXpZZw' WHERE id IN (109995, 110005);
DELETE FROM eh_yellow_pages WHERE id = 200219;
INSERT INTO `eh_yellow_pages` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `nick_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
	VALUES(200219, 0,'community','240111044331053517','创客空间','创客空间','1','深圳市南山区高新南九道','075526716888','高新区里程碑式的研发办公建筑，企业总部基地。运用科技和设计，打造甲级品质的节能、低耗、绿色生态商务空间，塑造立体的艺术、活力、科技体验生活方式中心，为高新园区产业升级提供了宝贵的空间载体。','cs://1/image/aW1hZ2UvTVRvek1qQXpNbVZpTmpVMU5tSXhNekZqTWpOaE5USmpNVFprTXpWaFlqazFNQQ','2',NULL, 113.956081, 22.533245,'',NULL,NULL,NULL,NULL,NULL,'苏娇娇','13760240661',NULL,NULL,NULL,NULL,NULL);
INSERT INTO `eh_yellow_page_attachments` (`id`, `owner_id`, `content_type`, `content_uri`, `creator_uid`, `create_time`)
	VALUES(131, 200219,'image','cs://1/image/aW1hZ2UvTVRvek1qQXpNbVZpTmpVMU5tSXhNekZqTWpOaE5USmpNVFprTXpWaFlqazFNQQ','0',UTC_TIMESTAMP());


-- 考勤管理 by sfyan 20160811
DELETE FROM `eh_acl_privileges` WHERE `id` IN (544, 545, 546, 547);
DELETE FROM `eh_web_menu_privileges` WHERE `id` IN (62, 63, 64, 65);
DELETE FROM `eh_web_menus` WHERE `id` IN (56100, 56110, 56120, 56130, 56140);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56100,'考勤管理',56000,NULL,NULL,1,2,'/50000/56000/56100','park',561);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56105,'考勤规则',56100,NULL,NULL,1,2,'/50000/56000/56100/56105','park',561);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56110,'规则管理',56105,NULL,'punch_rule',0,2,'/50000/56000/56100/56105/56110','park',563);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56120,'时间管理',56105,NULL,'punch_time',0,2,'/50000/56000/56100/56105/56120','park',564);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56130,'地点管理',56105,NULL,'punch_location',0,2,'/50000/56000/56100/56105/56130','park',565);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56140,'wifi管理',56105,NULL,'punch_wifi',0,2,'/50000/56000/56100/56105/56140','park',567);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56150,'排班管理',56105,NULL,'punch_scheduling',0,2,'/50000/56000/56100/56105/56150','park',568);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56160,'通用设置',56105,NULL,'punch_setting',0,2,'/50000/56000/56100/56105/56160','park',569);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56170,'个人设置',56105,NULL,'punch_personal_setting',0,2,'/50000/56000/56100/56105/56170','park',570);


INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (790,0,'规则管理','规则管理',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (791,0,'时间管理','时间管理',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (792,0,'地点管理','地点管理',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (793,0,'wifi管理','wifi管理',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (794,0,'排班管理','排班管理',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (795,0,'通用设置','通用设置',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (796,0,'个人设置','个人设置',NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),790,56110,'规则管理',1,1,'规则管理  全部权限',570);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),791,56120,'时间管理',1,1,'时间管理 全部权限',571);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),792,56130,'地点管理',1,1,'地点管理 全部权限',572);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),793,56140,'wifi管理',1,1,'wifi管理 全部权限',573);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),794,56150,'排班管理',1,1,'排班管理 全部权限',574);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),795,56160,'通用设置',1,1,'通用设置 全部权限',575);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),796,56170,'个人设置',1,1,'个人设置 全部权限',576);

-- 除了深业 其他屏蔽
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 56100,'', 'EhNamespaces', 1000000 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',1000000,0  FROM `eh_web_menus` WHERE `path` LIKE '%56100/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 56100,'', 'EhNamespaces', 999999 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999999,0  FROM `eh_web_menus` WHERE `path` LIKE '%56100/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 56100,'', 'EhNamespaces', 999989 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999989,0  FROM `eh_web_menus` WHERE `path` LIKE '%56100/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 56100,'', 'EhNamespaces', 999991 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999991,0  FROM `eh_web_menus` WHERE `path` LIKE '%56100/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 56100,'', 'EhNamespaces', 999993 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999993,0  FROM `eh_web_menus` WHERE `path` LIKE '%56100/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 56100,'', 'EhNamespaces', 999990 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999990,0  FROM `eh_web_menus` WHERE `path` LIKE '%56100/%';


-- 添加业主自动关联
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
  VALUES (36000,'业主管理',30000,NULL,'apartment_info',0,2,'/30000/36000','park',360);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
  VALUES (411,0,'业主管理','业主管理',NULL);
SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),411,36000,'业主管理',1,1,'业主管理 全部权限',167);

-- 威新Link 添加namspace_detail
INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`)
VALUES ('1013', '999991', 'community_commercial', UTC_TIMESTAMP());



-- 科技园加上资源预定，去掉场所预定  by sfyan 20160815
DELETE FROM `eh_web_menu_scopes` WHERE `menu_id` = 43400 AND `owner_type` = 'EhNamespaces' AND `owner_id` = 1000000;
DELETE FROM `eh_web_menu_scopes` WHERE `menu_id` IN (SELECT `id`  FROM `eh_web_menus` WHERE `path` LIKE '%43400/%') AND `owner_type` = 'EhNamespaces' AND `owner_id` = 1000000;

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 42000,'', 'EhNamespaces', 1000000 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',1000000,0  FROM `eh_web_menus` WHERE `path` LIKE '%42000/%';

-- 深业物业的设备巡检
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58200,'后台',58000,NULL,NULL,1,2,'/50000/58000/58200','park',800);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58210,'参考标准',58200,NULL,NULL,1,2,'/50000/58000/58200/58210','park',802);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58211,'标准列表',58210,NULL,'equipment_inspection_standard_list',0,2,'/50000/58000/58200/58210/58211','park',804);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58212,'设备关联审批',58210,NULL,'equipment_inspection_check_attachment',0,2,'/50000/58000/58200/58210/58212','park',806);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58220,'设备台帐',58200,NULL,NULL,1,2,'/50000/58000/58200/58220','park',820);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58221,'设备列表',58220,NULL,'equipment_inspection_equipment_list',0,2,'/50000/58000/58200/58220/58221','park',822);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58222,'备品备件',58220,NULL,'equipment_inspection_sparepart_list',0,2,'/50000/58000/58200/58220/58222','park',824);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58230,'任务列表',58200,NULL,NULL,1,2,'/50000/58000/58200/58230','park',830);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (58231,'任务列表',58230,NULL,'equipment_inspection_task_list',0,2,'/50000/58000/58200/58230/58231','park',832);


INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (800,0,'标准列表','标准列表',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (801,0,'设备关联审批','设备关联审批',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (802,0,'设备列表','设备列表',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (803,0,'备品备件','备品备件',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (804,0,'设备 任务列表','设备 任务列表',NULL);


SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),800,58211,'标准列表',1,1,'标准列表  全部权限',600);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),801,58212,'设备关联审批',1,1,'设备关联审批 全部权限',601);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),802,58221,'设备列表',1,1,'设备列表 全部权限',602);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),803,58222,'备品备件',1,1,'备品备件 全部权限',603);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),804,58231,'任务列表',1,1,'任务列表 全部权限',604);

-- 屏蔽 除深业之外其他园区的设备巡检菜单
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 58200,'', 'EhNamespaces', 1000000 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',1000000,0  FROM `eh_web_menus` WHERE `path` LIKE '%58200/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 58200,'', 'EhNamespaces', 999999 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999999,0  FROM `eh_web_menus` WHERE `path` LIKE '%58200/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 58200,'', 'EhNamespaces', 999989 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999989,0  FROM `eh_web_menus` WHERE `path` LIKE '%58200/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 58200,'', 'EhNamespaces', 999991 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999991,0  FROM `eh_web_menus` WHERE `path` LIKE '%58200/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 58200,'', 'EhNamespaces', 999993 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999993,0  FROM `eh_web_menus` WHERE `path` LIKE '%58200/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 58200,'', 'EhNamespaces', 999990 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999990,0  FROM `eh_web_menus` WHERE `path` LIKE '%58200/%';

-- 更新服务联盟的菜单
DELETE FROM `eh_web_menus` WHERE `id` = 44000;
DELETE FROM `eh_acl_privileges` WHERE `id` IN (540, 541);
DELETE FROM `eh_web_menu_privileges` WHERE `id` IN (40, 41);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (44000,'服务联盟',40000,NULL,NULL,1,2,'/40000/44000','park',455);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (44100,'类型管理',44000,NULL,'service_type_management',0,2,'/40000/44000/44100','park',456);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (44200,'机构管理',44000,NULL,'service_alliance',0,2,'/40000/44000/44200','park',457);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (540,0,'服务联盟 类型管理','服务联盟 类型管理 全部权限',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (541,0,'服务联盟 机构管理','服务联盟 机构管理 全部权限',NULL);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),540,44100,'类型管理',1,1,'备品备件 全部权限',603);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),541,44200,'机构管理',1,1,'任务列表 全部权限',604);

-- 屏蔽 某些园区的服务联盟菜单
DELETE FROM `eh_web_menu_scopes` WHERE `menu_id` = 44000;
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 44000,'', 'EhNamespaces', 999992 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999992,0  FROM `eh_web_menus` WHERE `path` LIKE '%44000/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 44000,'', 'EhNamespaces', 999993 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999993,0  FROM `eh_web_menus` WHERE `path` LIKE '%44000/%';

-- 其它登录设备已经被踢出提示 by Janson
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'messaging', '5', 'zh_CN', '其它登录设备已经被踢出');


-- 设备巡检服务广场icon by xiongying
SET @item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) VALUES ((@item_id := @item_id + 1), '999992', '0', '0', '0', '/home', 'Bizs', 'EquipmentInspection', '设备巡检', 'cs://1/image/aW1hZ2UvTVRwaU5tRTRZalZqTlRJNE5XUTVNelZoT1ROak9UWXlabUk1TURRMVpEZzROUQ', '1', '1', '14', '{\"url\":\"https://core.zuolin.com/equipment-inspection/dist/index.html?hideNavigationBar=1#sign_suffix\"}', '0', '0', '1', '1', '', '0', '', '', NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) VALUES ((@item_id := @item_id + 1), '999992', '0', '0', '0', '/home', 'Bizs', 'EquipmentInspection', '设备巡检', 'cs://1/image/aW1hZ2UvTVRwaU5tRTRZalZqTlRJNE5XUTVNelZoT1ROak9UWXlabUk1TURRMVpEZzROUQ', '1', '1', '14', '{\"url\":\"https://core.zuolin.com/equipment-inspection/dist/index.html?hideNavigationBar=1#sign_suffix\"}', '0', '0', '1', '1', '', '0', '', '', NULL, '1', 'park_tourist');

-- 创客空间论坛 by lqs 20160815
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ((@configuration_id := @configuration_id + 1), 'makerzone.forum_id', '177000', '创客空间论坛ID', '0', '创客空间论坛');

-- 资源预订推送模板 by wuhan 20160815
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'rental.notification', 1, 'zh_CN', '在开始前给预定用户发送推送提醒', '您预约的${resourceName}已临近使用时间，使用时间为${startTime}');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'rental.notification', 2, 'zh_CN', '预定成功给管理员发推送', '${userName}预约了${resourceName}\n使用详情：${useDetail}\n预约数：${rentalCount}');

-- 资源预订文字 by wuhan 20160815
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'rental.notification', '0', 'zh_CN', '早上');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'rental.notification', '1', 'zh_CN', '下午');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'rental.notification', '2', 'zh_CN', '晚上');

-- 屏蔽威新菜单 by sfyan 201860816
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 11200,'', 'EhNamespaces', 999991 , 0);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 30500,'', 'EhNamespaces', 999991 , 0);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 41000,'', 'EhNamespaces', 999991 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999991,0  FROM `eh_web_menus` WHERE `path` LIKE '%41000/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 42000,'', 'EhNamespaces', 999991 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999991,0  FROM `eh_web_menus` WHERE `path` LIKE '%42000/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 47000,'', 'EhNamespaces', 999991 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999991,0  FROM `eh_web_menus` WHERE `path` LIKE '%47000/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 43300,'', 'EhNamespaces', 999991 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999991,0  FROM `eh_web_menus` WHERE `path` LIKE '%43300/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 43500,'', 'EhNamespaces', 999991 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999991,0  FROM `eh_web_menus` WHERE `path` LIKE '%43500/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 43600,'', 'EhNamespaces', 999991 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999991,0  FROM `eh_web_menus` WHERE `path` LIKE '%43600/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 48000,'', 'EhNamespaces', 999991 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999991,0  FROM `eh_web_menus` WHERE `path` LIKE '%48000/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 46000,'', 'EhNamespaces', 999991 , 0);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 46100,'', 'EhNamespaces', 999991 , 0);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 51000,'', 'EhNamespaces', 999991 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999991,0  FROM `eh_web_menus` WHERE `path` LIKE '%51000/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 53000,'', 'EhNamespaces', 999991 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999991,0  FROM `eh_web_menus` WHERE `path` LIKE '%53000/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 56000,'', 'EhNamespaces', 999991 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999991,0  FROM `eh_web_menus` WHERE `path` LIKE '%56000/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 58000,'', 'EhNamespaces', 999991 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999991,0  FROM `eh_web_menus` WHERE `path` LIKE '%58000/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 59000,'', 'EhNamespaces', 999991 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999991,0  FROM `eh_web_menus` WHERE `path` LIKE '%59000/%';


-- 左邻域下新增企业:深圳市嘉宏达建材有限公司
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1003199, UUID(), '深圳市嘉宏达建材有限公司', '深圳市嘉宏达建材有限公司', 1, 0, 0, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 180855, 1, 0);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(180855, UUID(), 0, 2, 'EhGroups', 1003199,'深圳市嘉宏达建材有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());

INSERT INTO `eh_user_groups` (`id`,  `owner_uid`,  `group_discriminator`,  `group_id`,  `region_scope`,  `region_scope_id`,  `member_role`,  `member_status`,  `create_time`)
	VALUES (318492, 227276, 'enterprise', 1002797, 0, 0, 7, 3, UTC_TIMESTAMP());

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`,`level`, `status`, `group_type`, `group_id`, `namespace_id`)
	VALUES(1002797, 0, 'ENTERPRISE', '深圳市嘉宏达建材有限公司', 0, NULL, '/1002797', 1, 2, 'ENTERPRISE', 1003199, 0);
INSERT INTO `eh_organization_members` (`id`,  `organization_id`,  `target_type`,  `target_id`,  `member_group`,  `contact_name`,  `contact_type`,  `contact_token`,  `contact_description`,  `status`)
	VALUES (2105690, 1002797, 'USER', 227276, 'manager', '钱立维', 0, '13392803588', NULL, 3);
INSERT INTO `eh_acl_role_assignments` (`id`,  `owner_type`,  `owner_id`,  `target_type`,  `target_id`,  `role_id`,  `creator_uid`,  `create_time`)
	VALUES (11068, 'EhOrganizations', 1002797, 'EhUsers', 227276, 1005, 0, UTC_TIMESTAMP());
INSERT INTO `eh_organization_community_requests`(`id`, `community_id`, `member_type`, `member_id`, `member_status`, `create_time`, `update_time`)
    VALUES(1111264,240111044331051380, 'organization', 1002797, 3, UTC_TIMESTAMP(), UTC_TIMESTAMP());


INSERT INTO `eh_acl_roles` (`id`, `app_id`, `name`, `description`, `tag`, `namespace_id`, `owner_type`, `owner_id`) VALUES ('1010', '32', '设备巡检', '设备巡检', NULL, '999992', 'EhOrganizations', '1000750');

-- 活动消息修改  by sfyan 20160816
UPDATE `eh_locale_templates` SET `text` = '${userName}报名参加了您发起的活动【${postName}】' WHERE `scope` = 'activity.notification' AND `code` = 1;
UPDATE `eh_locale_templates` SET `text` = '${userName}取消了您发起的活动【${postName}】报名' WHERE `scope` = 'activity.notification' AND `code` = 2;

-- 东方建富菜单
DELETE FROM `eh_web_menu_scopes` WHERE `menu_id` = 33000 AND `owner_type` = 'EhNamespaces' AND `owner_id` = 0;

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhOrganizations',1002756,0  FROM `eh_web_menus` WHERE `path` LIKE '%44000/%';

INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhOrganizations',1002756,0  FROM `eh_web_menus` WHERE `path` LIKE '%49500/%';

-- 增加固定角色
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1010,0,1,NOW() FROM `eh_web_menu_privileges` WHERE `menu_id` = 58210;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1010,0,1,NOW() FROM `eh_web_menu_privileges` WHERE `menu_id` = 58220;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1010,0,1,NOW() FROM `eh_web_menu_privileges` WHERE `menu_id` = 58230;

-- 发帖要收到消息的配置
SET @organization_task_target_id = (SELECT MAX(id) FROM `eh_organization_task_targets`);
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331052506,'EhUsers',222503,'REPAIRS','push');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331052507,'EhUsers',222502,'REPAIRS','push');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331052507,'EhUsers',226508,'REPAIRS','push');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331052508,'EhUsers',222501,'REPAIRS','push');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331052505,'EhUsers',226521,'REPAIRS','push');

INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331053517,'EhUsers',221616,'REPAIRS','push');

-- 修改园区 名称
UPDATE `eh_communities` SET `name` = 'ibase北京金地中心', `alias_name` = 'ibase北京金地中心' WHERE `id` = 240111044331052505;
UPDATE `eh_communities` SET `name` = 'ibase深圳龙井问山', `alias_name` = 'ibase深圳龙井问山' WHERE `id` = 240111044331052506;
UPDATE `eh_communities` SET `name` = 'ibase深圳威新中心', `alias_name` = 'ibase深圳威新中心' WHERE `id` = 240111044331052507;
UPDATE `eh_communities` SET `name` = 'ibase深圳园博园创意集群社', `alias_name` = 'ibase深圳园博园创意集群社' WHERE `id` = 240111044331052508;

UPDATE `eh_web_menus` SET `name` = '设备巡检' WHERE `id` = 58200;

-- kickof message
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'user', '100018', 'zh_CN', '你已被其它登录设备踢出');

-- 去掉东方建富的服务联盟和园区报 by lqs 20160816
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 49500,'', 'EhOrganizations', 1002756 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 44000,'', 'EhOrganizations', 1002756 , 0);

-- 只给深业留 业主管理 by lqs 20160816
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 36000,'', 'EhNamespaces', 1000000 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',1000000,0  FROM `eh_web_menus` WHERE `path` LIKE '%58200/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 36000,'', 'EhNamespaces', 999999 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999999,0  FROM `eh_web_menus` WHERE `path` LIKE '%36000/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 36000,'', 'EhNamespaces', 999989 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999989,0  FROM `eh_web_menus` WHERE `path` LIKE '%36000/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 36000,'', 'EhNamespaces', 999991 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999991,0  FROM `eh_web_menus` WHERE `path` LIKE '%36000/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 36000,'', 'EhNamespaces', 999993 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999993,0  FROM `eh_web_menus` WHERE `path` LIKE '%36000/%';

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 36000,'', 'EhNamespaces', 999990 , 0);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999990,0  FROM `eh_web_menus` WHERE `path` LIKE '%36000/%';

-- 园区入驻发短信模板给威新之外的添加 by wuhan 20160817
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(0, 'sms.default.yzx', 9, 'zh_CN', '看楼申请-左邻', '28063');  
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(1000000, 'sms.default.yzx', 9, 'zh_CN', '看楼申请-科技园', '28064');  
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999999, 'sms.default.yzx', 9, 'zh_CN', '看楼申请-讯美', '28065');  
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999995, 'sms.default.yzx', 9, 'zh_CN', '看楼申请-金隅', '28066');  
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999993, 'sms.default.yzx', 9, 'zh_CN', '看楼申请-海岸', '28067');  
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999992, 'sms.default.yzx', 9, 'zh_CN', '看楼申请-深业', '28068');  
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999990, 'sms.default.yzx', 9, 'zh_CN', '看楼申请-储能', '28069');  
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999989, 'sms.default.yzx', 9, 'zh_CN', '看楼申请-Ibase', '28070');   


-- 活动消息修改  by sfyan 20160817
UPDATE `eh_locale_strings` SET `text` = '您报名参加的活动[${subject}]已被管理员拒绝，原因：${reason}' WHERE `scope` = 'activity' AND `code` = 5;
UPDATE `eh_locale_templates` SET `text` = '${userName}报名参加了您发起的活动[${postName}]' WHERE `scope` = 'activity.notification' AND `code` = 1;
UPDATE `eh_locale_templates` SET `text` = '${userName}取消了您发起的活动[${postName}]报名' WHERE `scope` = 'activity.notification' AND `code` = 2;
UPDATE `eh_locale_templates` SET `text` = '您报名参加的活动“${postName}”已被管理员通过' WHERE `scope` = 'activity.notification' AND `code` = 3;

-- 发帖物业报修要收到短信配置
SET @organization_task_target_id = (SELECT MAX(id) FROM `eh_organization_task_targets`);
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331052506,'EhUsers',222503,'REPAIRS','sms');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331052507,'EhUsers',222502,'REPAIRS','sms');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331052507,'EhUsers',226508,'REPAIRS','sms');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331052508,'EhUsers',222501,'REPAIRS','sms');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331052505,'EhUsers',226521,'REPAIRS','sms');

INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331053517,'EhUsers',221616,'REPAIRS','sms');

INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331052505,'EhUsers',222568,'REPAIRS','sms');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331052505,'EhUsers',222569,'REPAIRS','sms');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331052506,'EhUsers',222568,'REPAIRS','sms');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331052506,'EhUsers',222569,'REPAIRS','sms');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331052507,'EhUsers',222568,'REPAIRS','sms');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331052507,'EhUsers',222569,'REPAIRS','sms');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331052508,'EhUsers',222568,'REPAIRS','sms');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331052508,'EhUsers',222569,'REPAIRS','sms');

-- 科技园的不屏蔽 by sfyan 20160817 
DELETE FROM `eh_web_menu_scopes` WHERE `menu_id` = 56100 AND `owner_type` = 'EhNamespaces' AND `owner_id` = 1000000;
DELETE FROM `eh_web_menu_scopes` WHERE `menu_id` in (SELECT `id` FROM `eh_web_menus` WHERE `path` LIKE '%56100/%') AND `owner_type` = 'EhNamespaces' AND `owner_id` = 1000000;

-- 把打卡考勤的老功能重新开放  by sfyan 20160817  
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56172,'打卡详情',56105,null,'attendance_record',0,2,'/50000/56000/56100/56105/56172','park',571);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56174,'异常统计',56105,null,'attendance_result',0,2,'/50000/56000/56100/56105/56174','park',572);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56176,'异常处理',56105,null,'attendance_manage',0,2,'/50000/56000/56100/56105/56176','park',573);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (797,0,'打卡详情','打卡详情',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (798,0,'异常统计','异常统计',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (799,0,'异常处理','异常处理',NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),797,56172,'打卡详情',1,1,'打卡详情  全部权限',577);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),798,56174,'异常统计',1,1,'异常统计 全部权限',578);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),799,56176,'异常处理',1,1,'异常处理 全部权限',579);

-- 屏蔽 菜单
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999999,0  FROM `eh_web_menus` WHERE `id` IN (56172, 56174, 56176); 

INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999989,0  FROM `eh_web_menus` WHERE `id` IN (56172, 56174, 56176); 

INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999991,0  FROM `eh_web_menus` WHERE `id` IN (56172, 56174, 56176); 

INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999993,0  FROM `eh_web_menus` WHERE `id` IN (56172, 56174, 56176); 

INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999990,0  FROM `eh_web_menus` WHERE `id` IN (56172, 56174, 56176); 

-- 给角色加权限
set @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1001,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (56172, 56174, 56176);

INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1002,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (56172, 56174, 56176);

INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%56100/%');

INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%56100/%');





-- 左邻域下新增企业:北京同方信息安全技术股份有限公司
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1003198, UUID(), '北京同方信息安全技术股份有限公司', '北京同方信息安全技术股份有限公司', 1, 0, 0, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 180854, 1, 0);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(180854, UUID(), 0, 2, 'EhGroups', 1003198,'北京同方信息安全技术股份有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());

INSERT INTO `eh_user_groups` (`id`,  `owner_uid`,  `group_discriminator`,  `group_id`,  `region_scope`,  `region_scope_id`,  `member_role`,  `member_status`,  `create_time`)
	VALUES (318491, 227447, 'enterprise', 1002796, 0, 0, 7, 3, UTC_TIMESTAMP());

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`,`level`, `status`, `group_type`, `group_id`, `namespace_id`)
	VALUES(1002796, 0, 'ENTERPRISE', '北京同方信息安全技术股份有限公司', 0, NULL, '/1002796', 1, 2, 'ENTERPRISE', 1003198, 0);
INSERT INTO `eh_organization_members` (`id`,  `organization_id`,  `target_type`,  `target_id`,  `member_group`,  `contact_name`,  `contact_type`,  `contact_token`,  `contact_description`,  `status`)
	VALUES (2105689, 1002796, 'USER', 227447, 'manager', '张煦', 0, '13801399811', NULL, 3);
INSERT INTO `eh_acl_role_assignments` (`id`,  `owner_type`,  `owner_id`,  `target_type`,  `target_id`,  `role_id`,  `creator_uid`,  `create_time`)
	VALUES (11067, 'EhOrganizations', 1002796, 'EhUsers', 227447, 1005, 0, UTC_TIMESTAMP());
INSERT INTO `eh_organization_community_requests`(`id`, `community_id`, `member_type`, `member_id`, `member_status`, `create_time`, `update_time`)
    VALUES(1111263,240111044331051380, 'organization', 1002796, 3, UTC_TIMESTAMP(), UTC_TIMESTAMP());
    
    
-- 储能屏蔽 服务热线
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 46000,'', 'EhNamespaces', 999990 , 0);

