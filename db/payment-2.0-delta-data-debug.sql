-- 支付方式
SET @eh_configurations_id = (SELECT MAX(id) from eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@eh_configurations_id := @eh_configurations_id+ 1), 'pay.v2.callback.url', '/pay/payNotify', '新支付回调接口url', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@eh_configurations_id := @eh_configurations_id+ 1), 'default.bind.phone', '12100001111', '绑手机号默认值', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@eh_configurations_id := @eh_configurations_id+ 1), 'pay.v2.orderPaymentStatusQueryUri', '/order/queryOrderPaymentStatus', '查询支付单信息', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@eh_configurations_id := @eh_configurations_id+ 1), 'pay.v2.home.url', 'http://paytest.zuolin.com:8080/pay', '新支付homeUrl', '0', NULL);

-- 园区账号
INSERT INTO `eh_payment_accounts` (`id`, `name`, `account_id`, `system_id`, `app_key`, `secret_key`, `create_time`) VALUES ('1', 'biz-test-account-20170914-2', '10000', '1', '136890e5-41f9-4494-8dc2-46d63ff015b7', 'fgFUqv7/GPfx1zZX9I3cHSt5+zJNKNKHiSDuLFoMC8WFIeOaZqTDps8zIWPZioRx/XGta5anMlDpM6NBlCwabg==', now());

-- 收款方会员id=19030100
-- 付款方会员id=19030101
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES ('1', 'EhOrganizations', '147248', '1', '19030100', '2017-09-08 14:58:52');

-- select * from tbl_shop_info where shop_name like '%jw%';
INSERT INTO `eh_payment_service_configs` (`id`, `name`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_split_rule_id`, `payment_user_type`, `payment_user_id`, `create_time`, `update_time`) VALUES ('1', '左邻APP-测试限时折扣-from-jw店铺服务', 'activitySignupOrder', '1000000', 'EhOrganizations', '147248', '1', '1001', '5', '1', '1000', '2017-09-08 15:01:30', NULL);

-- insert into tbl_payment_type values ('{id}','{type}','{mallId}','{ownerType}','{ownerNo}','{resourceType}','{resourceNo}','{paymentType}','{[paymentName}','{paymentLogo}','{paymentParams}',now(),null);
INSERT INTO `eh_payment_types` (`id`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_type`, `payment_name`, `payment_logo`, `paymentParams`, `create_time`, `update_time`) VALUES ('15047780545380001', 'activitySignupOrder', '1000000', 'EhOrganizations', '147248', '1', '1001', '1', '微信', 'cs://1/image/aW1hZ2UvTVRveU1UUmtaRFExTTJSbFpETXpORE5rTjJNME9Ua3dOVFkxTVRNek1HWXpOZw', '{\"payType\":\"no_credit\"}', '2017-09-08 15:01:37', NULL);
INSERT INTO `eh_payment_types` (`id`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_type`, `payment_name`, `payment_logo`, `paymentParams`, `create_time`, `update_time`) VALUES ('15047780545380002', 'activitySignupOrder', '1000000', 'EhOrganizations', '147248', '1', '1001', '8', '支付宝', 'cs://1/image/aW1hZ2UvTVRvelpEZ3pZalV6WmpGbFkyRXhNamRoTkdJd04yWTFNR0ZrTnpGaE5ERm1Zdw', '{\"payType\":\"A01\"}', '2017-09-08 15:01:38', NULL);
INSERT INTO `eh_payment_types` (`id`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_type`, `payment_name`, `payment_logo`, `paymentParams`, `create_time`, `update_time`) VALUES ('15047780545380003', 'activitySignupOrder', '1000000', 'EhOrganizations', '147248', '1', '1001', '9', '微信公众号支付', 'cs://1/image/aW1hZ2UvTVRveU1UUmtaRFExTTJSbFpETXpORE5rTjJNME9Ua3dOVFkxTVRNek1HWXpOZw', '{\"payType\":\"no_credit\"}', '2017-09-08 15:01:38', NULL);
