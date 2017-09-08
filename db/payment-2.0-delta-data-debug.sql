-- 支付方式
SET @eh_configurations_id = (SELECT MAX(id) from eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@eh_configurations_id := @eh_configurations_id+ 1), 'pay.v2.callback.url', '/pay/payNotify', '新支付回调接口url', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@eh_configurations_id := @eh_configurations_id+ 1), 'default.bind.phone', '12100001111', '绑手机号默认值', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@eh_configurations_id := @eh_configurations_id+ 1), 'pay.v2.orderPaymentStatusQueryUri', '/order/queryOrderPaymentStatus', '查询支付单信息', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@eh_configurations_id := @eh_configurations_id+ 1), 'pay.v2.home.url', 'http://paytest.zuolin.com:8080/pay', '新支付homeUrl', '0', NULL);

-- 园区账号
INSERT INTO `eh_payment_accounts` (`id`, `name`, `account_id`, `system_id`, `app_key`, `secret_key`, `create_time`) VALUES ('1', 'zuolin-account1', '10000', '1', '2f9086c1-a76a-48b3-9ec9-39e187b8004b', 'UK15uLXoyfAIoWPlqaBYzAo2IdPHmmr80Y9bLrXbZ3jtedj50G0+boyoPlSI9m29gWNEbh2o9I7m9r9zxCwiRg==', '2017-09-08 14:58:46');

-- 收款方会员id=19030100
-- 付款方会员id=19030101
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES ('1', '1', '147248', '1', '19030100', '2017-09-08 14:58:52');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES ('2', '2', '147249', '2', '19030101', '2017-09-08 14:58:52');


-- select * from tbl_shop_info where shop_name like '%jw%';
INSERT INTO `eh_payment_service_configs` (`id`, `name`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_split_rule_id`, `payment_user_type`, `payment_user_id`, `create_time`, `update_time`) VALUES ('1', '左邻APP-测试限时折扣-from-jw店铺服务', 'dianshang', '1000000', '1', '147248', '1', '1001', '1', '1', '19030100', '2017-09-08 15:01:30', NULL);

-- insert into tbl_payment_type values ('{id}','{type}','{mallId}','{ownerType}','{ownerNo}','{resourceType}','{resourceNo}','{paymentType}','{[paymentName}','{paymentLogo}','{paymentParams}',now(),null);
INSERT INTO `eh_payment_types` (`id`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_type`, `payment_name`, `payment_logo`, `paymentParams`, `create_time`, `update_time`) VALUES ('15047780545380001', NULL, NULL, NULL, NULL, NULL, NULL, '1', '微信', 'cs://1/image/aW1hZ2UvTVRveU1UUmtaRFExTTJSbFpETXpORE5rTjJNME9Ua3dOVFkxTVRNek1HWXpOZw', '{\"payType\":“no_credit\"}', '2017-09-08 15:01:37', NULL);
INSERT INTO `eh_payment_types` (`id`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_type`, `payment_name`, `payment_logo`, `paymentParams`, `create_time`, `update_time`) VALUES ('15047780545380002', NULL, NULL, NULL, NULL, NULL, NULL, '1', '支付宝', 'cs://1/image/aW1hZ2UvTVRvelpEZ3pZalV6WmpGbFkyRXhNamRoTkdJd04yWTFNR0ZrTnpGaE5ERm1Zdw', '{\"payType\":\"A01\"}', '2017-09-08 15:01:38', NULL);

