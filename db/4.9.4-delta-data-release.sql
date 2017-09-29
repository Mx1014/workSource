-- 交易明细菜单配置 by wentian
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`,`module_id`,`level`,`condition_type`,`category`)
VALUES ('20430', '交易明细', 20400, NULL, 'react:/payment-management/bills-details/zjgkrentalcode', 1, 2, '/20000/20400/20430', 'park', 9941232,20400,3,NULL,'module');


SET @eh_web_menus_id_paym = '20430';


SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), @eh_web_menus_id_paym, '', 'EhNamespaces', 999971, 2);


SET @privilege_id = (SELECT MAX(id) FROM `eh_acl_privileges`);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES ((@privilege_id := @privilege_id + 1), 0, '交易明细', '交易明细 全部权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @privilege_id, @eh_web_menus_id_paym, '交易明细', 1, 1, '交易明细 全部权限', 990);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1001, 'EhAclRoles', 0, 1, UTC_TIMESTAMP());
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1005, 'EhAclRoles', 0, 1, UTC_TIMESTAMP());

SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), @eh_web_menus_id_paym, '1', @privilege_id, NULL, '0', UTC_TIMESTAMP());


INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`,`operator_uid`,`creator_uid`)
VALUES (@eh_web_menus_id_paym, '交易明细', '20400', '/20000/20400/20430', '0', '2', '2', '0', UTC_TIMESTAMP(),1,1);

SET @eh_service_module_scopes_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999971, @eh_web_menus_id_paym, '交易明细', 'EhNamespaces', 999971, NULL, 2);

-- wentian's done above



-- 支付2.0 init data by wentian


-- 基础
-- -- 支付方式
-- SET @eh_configurations_id = (SELECT MAX(id) from eh_configurations);
-- INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@eh_configurations_id := @eh_configurations_id+ 1), 'pay.v2.callback.url', '/pay/payNotify', '新支付回调接口url', '0', NULL);
-- INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@eh_configurations_id := @eh_configurations_id+ 1), 'default.bind.phone', '12100001111', '绑手机号默认值', '0', NULL);
-- INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@eh_configurations_id := @eh_configurations_id+ 1), 'pay.v2.orderPaymentStatusQueryUri', '/order/queryOrderPaymentStatus', '查询支付单信息', '0', NULL);
-- INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@eh_configurations_id := @eh_configurations_id+ 1), 'pay.v2.home.url', 'http://paytest.zuolin.com:8080/pay', '新支付homeUrl', '0', NULL);
UPDATE `eh_configurations` SET `value` = 'https://payv2.zuolin.com/pay/pay' where name = 'pay.v2.home.url';
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







