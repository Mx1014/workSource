-- 配置参数  by sfyan 20160810
set @configuration_id = (SELECT MAX(id) FROM `eh_configurations`);
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
VALUES (49000,'结算管理',40000,null,'settlement_management',1,2,'/40000/49000','park',454);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (770,0,'结算管理','结算管理 全部功能',null);

set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),770,49000,'结算管理',0,1,'结算管理',349);

-- 修改停车充值费率
UPDATE eh_parking_recharge_rates SET rate_name='3个月', card_type = '普通月卡' where id = 10001;
UPDATE eh_parking_recharge_rates SET rate_name='6个月', card_type = '普通月卡' where id = 10002;
UPDATE eh_parking_recharge_rates SET rate_name='3个月', card_type = '固定车位卡' where id = 10003;
UPDATE eh_parking_recharge_rates SET rate_name='6个月', card_type = '固定车位卡' where id = 10004;
set @eh_locale_templates_id = (SELECT MAX(id) FROM `eh_locale_templates`);
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
set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (780,0,'园区报管理','园区报管理 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (781,0,'约稿须知','约稿须知  全部功能',null);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (49500,'园区报',40000,null,null,1,2,'/40000/43400','park',453);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (49510,'园区报管理',49500,null,'park_epaper_management',0,2,'/40000/49500/49510','park',452);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (49520,'约稿须知',49500,null,'manuscripts_notice',0,2,'/40000/49500/49520','park',453);

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


