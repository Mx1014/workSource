-- 基础
-- -- 支付方式
-- SET @eh_configurations_id = (SELECT MAX(id) from eh_configurations);
-- INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@eh_configurations_id := @eh_configurations_id+ 1), 'pay.v2.callback.url', '/pay/payNotify', '新支付回调接口url', '0', NULL);
-- INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@eh_configurations_id := @eh_configurations_id+ 1), 'default.bind.phone', '12100001111', '绑手机号默认值', '0', NULL);
-- INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@eh_configurations_id := @eh_configurations_id+ 1), 'pay.v2.orderPaymentStatusQueryUri', '/order/queryOrderPaymentStatus', '查询支付单信息', '0', NULL);
-- INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@eh_configurations_id := @eh_configurations_id+ 1), 'pay.v2.home.url', 'http://paytest.zuolin.com:8080/pay', '新支付homeUrl', '0', NULL);
UPDATE `eh_configurations` SET `value` = 'https://payv2.zuolin.com/pay' where name = 'pay.v2.home.url';
-- 园区账号, review app_id, xxxx ...
-- INSERT INTO `eh_payment_accounts` (`id`, `name`, `account_id`, `system_id`, `app_key`, `secret_key`, `create_time`)
-- VALUES
-- ('1', 'payv2-account-20170929', '10000', '1', '136890e5-41f9-4494-8dc2-46d63ff015b7', 'fgFUqv7/GPfx1zZX9I3cHSt5+zJNKNKHiSDuLFoMC8WFIeOaZqTDps8zIWPZioRx/XGta5anMlDpM6NBlCwabg==', '2017-09-08 14:58:46');




TRUNCATE `eh_payment_service_configs`;
TRUNCATE `eh_payment_users`;

-- 闻天

-- config
INSERT INTO `eh_payment_service_configs` (`id`, `name`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_split_rule_id`, `payment_user_type`, `payment_user_id`, `create_time`, `update_time`)
VALUES
('1', '张江高科缴费', 'zjgkrentalcode','999971', 'EhOrganizations', '1012516', null, null, null, '2', '1145', UTC_TIMESTAMP(), NULL);


-- 支付类型
set @eh_payment_types_id = (select max(id) from `eh_payment_types`);
INSERT INTO `eh_payment_types`
(`id`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_type`, `payment_name`, `payment_logo`, `paymentParams`, `create_time`, `update_time`)
VALUES
(@eh_payment_types_id:=@eh_payment_types_id+1, 'zjgkrentalcode', '999971', 'EhOrganizations', '1012516', null, null, '8', '支付宝', 'cs://1/image/aW1hZ2UvTVRvelpEZ3pZalV6WmpGbFkyRXhNamRoTkdJd04yWTFNR0ZrTnpGaE5ERm1Zdw', '{\"payType\":\"A01\"}', UTC_TIMESTAMP(), NULL);


-- 收款方
INSERT INTO `eh_payment_users`
(`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`)
VALUES
('1', 'EhOrganizations', '1012516', '2', '1145', UTC_TIMESTAMP());



-- 爽

-- config
set @eh_payment_service_configs_id = (select MAX(id) from `eh_payment_service_configs`);
INSERT INTO `eh_payment_service_configs` (`id`, `name`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_split_rule_id`, `payment_user_type`, `payment_user_id`, `create_time`, `update_time`)
VALUES
(@eh_payment_service_configs_id:=@eh_payment_service_configs_id+1, '快递订单','expressOrder', 999985, 'EhOrganizations', 1007144, null, null, null, '2', '1141', UTC_TIMESTAMP(), NULL);


-- 支付类型
set @eh_payment_types_id = (select max(id) from `eh_payment_types`);
INSERT INTO `eh_payment_types`
(`id`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_type`, `payment_name`, `payment_logo`, `paymentParams`, `create_time`, `update_time`)
VALUES
(@eh_payment_types_id:=@eh_payment_types_id+1, 'expressOrder', 999985, 'EhOrganizations', 1007144, null, null, '8', '支付宝', 'cs://1/image/aW1hZ2UvTVRvelpEZ3pZalV6WmpGbFkyRXhNamRoTkdJd04yWTFNR0ZrTnpGaE5ERm1Zdw', '{\"payType\":\"A01\"}', UTC_TIMESTAMP(), NULL);


-- 收款方
set @eh_payment_users_id = (select MAX(id) from `eh_payment_users`);
INSERT INTO `eh_payment_users`
(`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`)
VALUES
(@eh_payment_users_id:=@eh_payment_users_id+1, 'EhOrganizations', 1007144, '2', '1141', UTC_TIMESTAMP());


-- config
set @eh_payment_service_configs_id = (select MAX(id) from `eh_payment_service_configs`);
INSERT INTO `eh_payment_service_configs` (`id`, `name`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_split_rule_id`, `payment_user_type`, `payment_user_id`, `create_time`, `update_time`)
VALUES
(@eh_payment_service_configs_id:=@eh_payment_service_configs_id+1, '打印订单','printOrder', 1, 'EhOrganizations', 1023080, null, null, null, '2', '1142', UTC_TIMESTAMP(), NULL);


-- 支付类型
set @eh_payment_types_id = (select max(id) from `eh_payment_types`);
INSERT INTO `eh_payment_types`
(`id`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_type`, `payment_name`, `payment_logo`, `paymentParams`, `create_time`, `update_time`)
VALUES
(@eh_payment_types_id:=@eh_payment_types_id+1, 'printOrder', 1, 'EhOrganizations', 1023080, null, null, '8', '支付宝', 'cs://1/image/aW1hZ2UvTVRvelpEZ3pZalV6WmpGbFkyRXhNamRoTkdJd04yWTFNR0ZrTnpGaE5ERm1Zdw', '{\"payType\":\"A01\"}', UTC_TIMESTAMP(), NULL);


-- 收款方
set @eh_payment_users_id = (select MAX(id) from `eh_payment_users`);
INSERT INTO `eh_payment_users`
(`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`)
VALUES
(@eh_payment_users_id:=@eh_payment_users_id+1, 'EhOrganizations', 1023080, '2', '1142', UTC_TIMESTAMP());




-- 思婷
-- config
set @eh_payment_service_configs_id = (select MAX(id) from `eh_payment_service_configs`);
INSERT INTO `eh_payment_service_configs` (`id`, `name`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_split_rule_id`, `payment_user_type`, `payment_user_id`, `create_time`, `update_time`)
VALUES
(@eh_payment_service_configs_id:=@eh_payment_service_configs_id+1, '一卡通','paymentCard', 999990, 'EhOrganizations', 179043, null, null, null, '2', '1143', UTC_TIMESTAMP(), NULL);


-- 支付类型
set @eh_payment_types_id = (select max(id) from `eh_payment_types`);
INSERT INTO `eh_payment_types`
(`id`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_type`, `payment_name`, `payment_logo`, `paymentParams`, `create_time`, `update_time`)
VALUES
(@eh_payment_types_id:=@eh_payment_types_id+1, 'paymentCard', 999990, 'EhOrganizations', 179043, null, null, '8', '支付宝', 'cs://1/image/aW1hZ2UvTVRvelpEZ3pZalV6WmpGbFkyRXhNamRoTkdJd04yWTFNR0ZrTnpGaE5ERm1Zdw', '{\"payType\":\"A01\"}', UTC_TIMESTAMP(), NULL);


-- 收款方
set @eh_payment_users_id = (select MAX(id) from `eh_payment_users`);
INSERT INTO `eh_payment_users`
(`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`)
VALUES
(@eh_payment_users_id:=@eh_payment_users_id+1, 'EhOrganizations', 179043, '2', '1143', UTC_TIMESTAMP());




-- 严军
-- 'activitySignupOrder'
-- config


-- 保集

-- config
set @eh_payment_service_configs_id = (select MAX(id) from `eh_payment_service_configs`);
INSERT INTO `eh_payment_service_configs` (`id`, `name`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_split_rule_id`, `payment_user_type`, `payment_user_id`, `create_time`, `update_time`)
VALUES
(@eh_payment_service_configs_id:=@eh_payment_service_configs_id+1, '活动','activitySignupOrder', 999973, 'EhOrganizations', '1010579', null, null, null, '2', '1144', UTC_TIMESTAMP(), NULL);


-- 支付类型
set @eh_payment_types_id = (select max(id) from `eh_payment_types`);
INSERT INTO `eh_payment_types`
(`id`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_type`, `payment_name`, `payment_logo`, `paymentParams`, `create_time`, `update_time`)
VALUES
(@eh_payment_types_id:=@eh_payment_types_id+1, 'activitySignupOrder', 999973, 'EhOrganizations', '1010579', null, null, '8', '支付宝', 'cs://1/image/aW1hZ2UvTVRvelpEZ3pZalV6WmpGbFkyRXhNamRoTkdJd04yWTFNR0ZrTnpGaE5ERm1Zdw', '{\"payType\":\"A01\"}', UTC_TIMESTAMP(), NULL);


-- 收款方
set @eh_payment_users_id = (select MAX(id) from `eh_payment_users`);
INSERT INTO `eh_payment_users`
(`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`)
VALUES
(@eh_payment_users_id:=@eh_payment_users_id+1, 'EhOrganizations', '1010579', '2', '1144', UTC_TIMESTAMP());






-- 张江：

-- config
set @eh_payment_service_configs_id = (select MAX(id) from `eh_payment_service_configs`);
INSERT INTO `eh_payment_service_configs` (`id`, `name`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_split_rule_id`, `payment_user_type`, `payment_user_id`, `create_time`, `update_time`)
VALUES
(@eh_payment_service_configs_id:=@eh_payment_service_configs_id+1, '活动','activitySignupOrder', 999971, 'EhOrganizations', '1012516', null, null, null, '2', '1145', UTC_TIMESTAMP(), NULL);


-- 支付类型
set @eh_payment_types_id = (select max(id) from `eh_payment_types`);
INSERT INTO `eh_payment_types`
(`id`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_type`, `payment_name`, `payment_logo`, `paymentParams`, `create_time`, `update_time`)
VALUES
(@eh_payment_types_id:=@eh_payment_types_id+1, 'activitySignupOrder', 999971, 'EhOrganizations', '1012516', null, null, '8', '支付宝', 'cs://1/image/aW1hZ2UvTVRvelpEZ3pZalV6WmpGbFkyRXhNamRoTkdJd04yWTFNR0ZrTnpGaE5ERm1Zdw', '{\"payType\":\"A01\"}', UTC_TIMESTAMP(), NULL);


-- 收款方
-- set @eh_payment_users_id = (select MAX(id) from `eh_payment_users`);
-- INSERT INTO `eh_payment_users`
-- (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`)
-- VALUES
-- (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhOrganizations', '1012516', '2', '1145', UTC_TIMESTAMP());
--




-- 深圳弯：999966 ''


-- config
set @eh_payment_service_configs_id = (select MAX(id) from `eh_payment_service_configs`);
INSERT INTO `eh_payment_service_configs` (`id`, `name`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_split_rule_id`, `payment_user_type`, `payment_user_id`, `create_time`, `update_time`)
VALUES
(@eh_payment_service_configs_id:=@eh_payment_service_configs_id+1, '活动','activitySignupOrder', 999966, 'EhOrganizations', '1035830', null, null, null, '2', '1146', UTC_TIMESTAMP(), NULL);


-- 支付类型
set @eh_payment_types_id = (select max(id) from `eh_payment_types`);
INSERT INTO `eh_payment_types`
(`id`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_type`, `payment_name`, `payment_logo`, `paymentParams`, `create_time`, `update_time`)
VALUES
(@eh_payment_types_id:=@eh_payment_types_id+1, 'activitySignupOrder', 999966, 'EhOrganizations', '1035830', null, null, '8', '支付宝', 'cs://1/image/aW1hZ2UvTVRvelpEZ3pZalV6WmpGbFkyRXhNamRoTkdJd04yWTFNR0ZrTnpGaE5ERm1Zdw', '{\"payType\":\"A01\"}', UTC_TIMESTAMP(), NULL);


-- 收款方
set @eh_payment_users_id = (select MAX(id) from `eh_payment_users`);
INSERT INTO `eh_payment_users`
(`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`)
VALUES
(@eh_payment_users_id:=@eh_payment_users_id+1, 'EhOrganizations', '1035830', '2', '1146', UTC_TIMESTAMP());




-- vlgo：

-- config
set @eh_payment_service_configs_id = (select MAX(id) from `eh_payment_service_configs`);
INSERT INTO `eh_payment_service_configs` (`id`, `name`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_split_rule_id`, `payment_user_type`, `payment_user_id`, `create_time`, `update_time`)
VALUES
(@eh_payment_service_configs_id:=@eh_payment_service_configs_id+1, '活动','activitySignupOrder', 1, 'EhOrganizations', '1023080', null, null, null, '2', '1087', UTC_TIMESTAMP(), NULL);


-- 支付类型
set @eh_payment_types_id = (select max(id) from `eh_payment_types`);
INSERT INTO `eh_payment_types`
(`id`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_type`, `payment_name`, `payment_logo`, `paymentParams`, `create_time`, `update_time`)
VALUES
(@eh_payment_types_id:=@eh_payment_types_id+1, 'activitySignupOrder', 1, 'EhOrganizations', '1023080', null, null, '8', '支付宝', 'cs://1/image/aW1hZ2UvTVRvelpEZ3pZalV6WmpGbFkyRXhNamRoTkdJd04yWTFNR0ZrTnpGaE5ERm1Zdw', '{\"payType\":\"A01\"}', UTC_TIMESTAMP(), NULL);

-- DUPLICATED -- ANNOTATED BY WENTIAN
-- -- 收款方
-- set @eh_payment_users_id = (select MAX(id) from `eh_payment_users`);
-- INSERT INTO `eh_payment_users`
-- (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`)
-- VALUES
-- (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhOrganizations', '1023080', '2', '1087', UTC_TIMESTAMP());






-- 吴寒

-- -- 添加收款方的会员
-- set @eh_payment_user_id = (select max(id) from `eh_payment_users`);
-- INSERT INTO `eh_payment_users` VALUES (@eh_payment_user_id:=@eh_payment_user_id+1, 'EhOrganizations', '1023080', '2', '1087', UTC_TIMESTAMP());
-- 添加收款方
set @eh_payment_service_configs_id = (select max(id) from `eh_payment_service_configs`);
INSERT INTO `eh_payment_service_configs` VALUES (@eh_payment_service_configs_id:=@eh_payment_service_configs_id+1, '视频会议', 'videoConf', '1', 'EhOrganizations', '1023080', null, null, NULL, '2', '1087', UTC_TIMESTAMP(), null);
-- 增加支付的方式
set @eh_payment_types_id = (select max(id) from `eh_payment_types`);
INSERT INTO `eh_payment_types` VALUES (@eh_payment_types_id:=@eh_payment_types_id+1, 'videoConf', '1', 'EhOrganizations', '1023080', null, null, '8', '支付宝', 'cs://1/image/aW1hZ2UvTVRvelpEZ3pZalV6WmpGbFkyRXhNamRoTkdJd04yWTFNR0ZrTnpGaE5ERm1Zdw', '{\"payType\":\"A01\"}', UTC_TIMESTAMP(), null);


-- wenge

-- DUPLICATE
-- -- 添加收款方的会员
-- set @eh_payment_user_id = (select max(id) from `eh_payment_users`);
-- INSERT INTO `eh_payment_users` VALUES (@eh_payment_user_id:=@eh_payment_user_id+1, 'EhOrganizations', '1023080', '2', '1087', UTC_TIMESTAMP());
-- 添加收款方
set @eh_payment_service_configs_id = (select max(id) from `eh_payment_service_configs`);
INSERT INTO `eh_payment_service_configs` VALUES (@eh_payment_service_configs_id:=@eh_payment_service_configs_id+1, '资源预约', 'rentalOrder', '1', 'EhOrganizations', '1023080', null, null, NULL, '2', '1087', UTC_TIMESTAMP(), null);
-- 增加支付的方式
set @eh_payment_types_id = (select max(id) from `eh_payment_types`);
INSERT INTO `eh_payment_types` VALUES (@eh_payment_types_id:=@eh_payment_types_id+1, 'rentalOrder', '1', 'EhOrganizations', '1023080', null, null, '8', '支付宝', 'cs://1/image/aW1hZ2UvTVRvelpEZ3pZalV6WmpGbFkyRXhNamRoTkdJd04yWTFNR0ZrTnpGaE5ERm1Zdw', '{\"payType\":\"A01\"}', UTC_TIMESTAMP(), null);


-- 添加收款方的会员
set @eh_payment_user_id = (select max(id) from `eh_payment_users`);
INSERT INTO `eh_payment_users` VALUES (@eh_payment_user_id:=@eh_payment_user_id+1, 'EhOrganizations', '1008900', '2', '1149', UTC_TIMESTAMP());
-- 添加收款方
set @eh_payment_service_configs_id = (select max(id) from `eh_payment_service_configs`);
INSERT INTO `eh_payment_service_configs` VALUES (@eh_payment_service_configs_id:=@eh_payment_service_configs_id+1, '停车充值', 'parking', '999983', 'EhOrganizations', '1008900', null, null, NULL, '2', '1149', UTC_TIMESTAMP(), null);
-- 增加支付的方式
set @eh_payment_types_id = (select max(id) from `eh_payment_types`);
INSERT INTO `eh_payment_types` VALUES (@eh_payment_types_id:=@eh_payment_types_id+1, 'parking', '999983', 'EhOrganizations', '1008900', null, null, '8', '支付宝', 'cs://1/image/aW1hZ2UvTVRvelpEZ3pZalV6WmpGbFkyRXhNamRoTkdJd04yWTFNR0ZrTnpGaE5ERm1Zdw', '{\"payType\":\"A01\"}', UTC_TIMESTAMP(), null);



set @eh_payment_users_id = (select max(id) from `eh_payment_users`);
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhOrganizations', '147248', '1', '1087', '2017-09-08 14:58:52');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '147249', '2', '19030101', '2017-09-08 14:58:52');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '249517', '1', '19030119', '2017-09-08 21:23:54');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '249518', '1', '19030129', '2017-09-10 23:47:21');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '999991014081', '1', '1009', '2017-09-14 16:47:38');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '238717', '1', '1069', '2017-09-26 14:38:35');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhOrganizations', '1007144', '2', '1074', '2017-09-26 09:53:36');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '377516', '1', '1084', '2017-09-27 11:22:22');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '265001', '1', '1088', '2017-09-27 15:42:52');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhOrganizations', '1023080', '2', '1087', '2017-09-27 07:53:40');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '244850', '1', '1091', '2017-09-27 17:11:49');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhOrganizations', '179043', '2', '1098', '2017-09-28 02:06:28');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhOrganizations', '1008900', '2', '1087', '2017-09-28 02:07:55');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '241964', '1', '1109', '2017-09-28 11:15:35');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '214000', '1', '1110', '2017-09-28 14:39:51');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '377559', '1', '1111', '2017-09-28 15:13:00');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '249545', '1', '1124', '2017-09-29 11:39:31');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '9999910140812', '1', '1126', '2017-09-29 13:08:42');



INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhOrganizations', '147248', '1', '19030100', '2017-09-08 14:58:52');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '147249', '2', '19030101', '2017-09-08 14:58:52');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '249517', '1', '19030119', '2017-09-08 21:23:54');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '249518', '1', '19030129', '2017-09-10 23:47:21');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '252520', '1', '1127', '2017-09-29 14:47:35');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '367622', '1', '1128', '2017-09-29 14:49:19');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '414688', '1', '1129', '2017-09-29 15:06:18');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '249972', '1', '1130', '2017-09-29 15:18:58');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '999991014081', '1', '1009', '2017-09-14 16:47:38');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '238717', '1', '1069', '2017-09-26 14:38:35');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhOrganizations', '1007144', '2', '1074', '2017-09-26 09:53:36');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '377516', '1', '1084', '2017-09-27 11:22:22');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '274170', '1', '1133', '2017-09-29 18:03:21');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '1036355', '1', '1134', '2017-09-29 18:08:55');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '265001', '1', '1088', '2017-09-27 15:42:52');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhOrganizations', '1023080', '2', '1087', '2017-09-27 07:53:40');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '244850', '1', '1091', '2017-09-27 17:11:49');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhOrganizations', '179043', '2', '1098', '2017-09-28 02:06:28');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhOrganizations', '1008900', '2', '1087', '2017-09-28 02:07:55');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '241964', '1', '1109', '2017-09-28 11:15:35');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '214000', '1', '1110', '2017-09-28 14:39:51');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '377559', '1', '1111', '2017-09-28 15:13:00');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '249545', '1', '1124', '2017-09-29 11:39:31');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '250473', '1', '1137', '2017-09-29 19:22:03');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '251867', '1', '1138', '2017-09-29 19:34:14');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '1036353', '1', '1139', '2017-09-29 20:17:32');
INSERT INTO `eh_payment_users` (`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`) VALUES (@eh_payment_users_id:=@eh_payment_users_id+1, 'EhUsers', '251455', '1', '1140', '2017-09-29 20:59:02');


