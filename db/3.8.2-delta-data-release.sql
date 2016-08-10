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
VALUES (49000,'结算管理',40000,null,'settlement_management',0,2,'/40000/49000','park',454);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (770,0,'结算管理','结算管理 全部功能',null);

set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),770,49000,'结算管理',0,1,'结算管理',349);