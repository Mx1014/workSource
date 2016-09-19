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
INSERT INTO eh_configurations(`id`,`name`,`value`,`description`,`namespace_id`)VALUES((@configuration_id := @configuration_id + 1), 'get.ware.info.api','zl-ec/rest/openapi/model/listByCondition','获取商品信息',0);

-- 增加结算服务配置 by sfyan 20160829
INSERT INTO `eh_stat_service` (`id`,`namespace_id`,`owner_type`,`owner_id`,`service_type`,`service_name`,`status`,`create_time`) values (1, 1000000, 'EhOrganizations', 1000001, 'parking_recharge','停车充值', 1, now());
INSERT INTO `eh_stat_service` (`id`,`namespace_id`,`owner_type`,`owner_id`,`service_type`,`service_name`,`status`,`create_time`) values (2, 1000000, 'EhOrganizations', 1000001, 'pmsy','物业缴费', 1, now());
INSERT INTO `eh_stat_service` (`id`,`namespace_id`,`owner_type`,`owner_id`,`service_type`,`service_name`,`status`,`create_time`) values (3, 1000000, 'EhOrganizations', 1000001, 'payment_card','一卡通', 1, now());