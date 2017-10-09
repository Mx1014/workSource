SET @eh_configurations_id = (SELECT MAX(id) from eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@eh_configurations_id := @eh_configurations_id+ 1), 'pay.v2.callback.url', '/pay/payNotify', '新支付回调接口url', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@eh_configurations_id := @eh_configurations_id+ 1), 'default.bind.phone', '12100001111', '绑手机号默认值', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@eh_configurations_id := @eh_configurations_id+ 1), 'pay.v2.orderPaymentStatusQueryUri', '/order/queryOrderPaymentStatus', '查询支付单信息', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@eh_configurations_id := @eh_configurations_id+ 1), 'pay.v2.home.url', 'http://paytest.zuolin.com:8080/pay', '新支付homeUrl', '0', NULL);


INSERT INTO `eh_payment_accounts` VALUES ('1', 'biz-test-account-20170914-2', '10000', '1', '136890e5-41f9-4494-8dc2-46d63ff015b7', 'fgFUqv7/GPfx1zZX9I3cHSt5+zJNKNKHiSDuLFoMC8WFIeOaZqTDps8zIWPZioRx/XGta5anMlDpM6NBlCwabg==', '2017-09-08 14:58:46');


INSERT INTO `eh_payment_service_configs` VALUES ('1', '左邻APP-测试限时折扣-from-jw店铺服务', 'zjgkrentalcode', '999971', 'EhOrganizations', '147248', '1', '1001', '5', '1', '1004', '2017-09-08 15:01:30', null);


INSERT INTO `eh_payment_types` VALUES ('15047780545380001', 'zjgkrentalcode', '999971', 'EhOrganizations', '147248', '1', '1001', '1', '微信', 'cs://1/image/aW1hZ2UvTVRveU1UUmtaRFExTTJSbFpETXpORE5rTjJNME9Ua3dOVFkxTVRNek1HWXpOZw', '{\"payType\":\"no_credit\"}', '2017-09-08 15:01:37', null);
INSERT INTO `eh_payment_types` VALUES ('15047780545380002', 'zjgkrentalcode', '999971', 'EhOrganizations', '147248', '1', '1001', '8', '支付宝', 'cs://1/image/aW1hZ2UvTVRvelpEZ3pZalV6WmpGbFkyRXhNamRoTkdJd04yWTFNR0ZrTnpGaE5ERm1Zdw', '{\"payType\":\"A01\"}', '2017-09-08 15:01:38', null);
INSERT INTO `eh_payment_types` VALUES ('15047780545380003', 'zjgkrentalcode', '999971', 'EhOrganizations', '147248', '1', '1001', '9', '微信公众号支付', 'cs://1/image/aW1hZ2UvTVRveU1UUmtaRFExTTJSbFpETXpORE5rTjJNME9Ua3dOVFkxTVRNek1HWXpOZw', '{\"payType\":\"no_credit\"}', '2017-09-08 15:01:38', null);


INSERT INTO `eh_payment_users` VALUES ('1', 'EhOrganizations', '147248', '1', '19030100', '2017-09-08 14:58:52');
INSERT INTO `eh_payment_users` VALUES ('2', 'EhUsers', '147249', '2', '19030101', '2017-09-08 14:58:52');
INSERT INTO `eh_payment_users` VALUES ('5', 'EhUsers', '249517', '1', '19030119', '2017-09-08 21:23:54');
INSERT INTO `eh_payment_users` VALUES ('6', 'EhUsers', '249518', '1', '19030129', '2017-09-10 23:47:21');
INSERT INTO `eh_payment_users` VALUES ('19', 'EhUsers', '999991014081', '1', '1009', '2017-09-14 16:47:38');