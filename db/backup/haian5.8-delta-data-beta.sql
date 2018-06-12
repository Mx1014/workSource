-- 999993(海岸馨服务) 1000631 深圳市海岸物业管理有限公司   ZL-4443(release)
set @namespace_id=999993;
set @organization_id=1000631;
set @biz_user_id=2327;  -- 1210(beta)  2368(release) 2327(for tets)
set @user_type=2;
set @configuration_id = (select max(id) from eh_configurations);
set @payment_user_id = (select max(id) from eh_payment_users);
set @payment_service_id = (select max(id) from eh_payment_service_configs);
set @payment_type_id = (select max(id) from eh_payment_types);
set @menu_scope_id = (select max(id) from eh_web_menu_scopes);
 
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ((@configuration_id := @configuration_id + 1), 'pay.platform', '1', '支付类型', @namespace_id, NULL);
-- UPDATE `eh_configurations` SET `value`='1' WHERE `name`='pay.platform' AND `namespace_id`=@namespace_id;
-- SELECT * FROM `eh_configurations` WHERE `name` LIKE '%platform%' AND `namespace_id`=@namespace_id;
 
-- payment_user_type: 1-普通会员,2-企业会员  settlement_type: 0-Daily, 7-Weekly
-- SELECT * FROM eh_payment_users WHERE payment_user_id=@biz_user_id;
INSERT INTO `eh_payment_users`(`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `settlement_type`, `status`, `create_time`)
VALUES ((@payment_user_id := @payment_user_id + 1), 'EhOrganizations', @organization_id, @user_type, @biz_user_id, 0, 2, UTC_TIMESTAMP());
 
INSERT INTO `eh_payment_service_configs`(id, name, order_type, namespace_id, owner_type, owner_id, resource_type, resource_id, payment_split_rule_id, payment_user_type, payment_user_id, create_time, update_time)
VALUES((@payment_service_id := @payment_service_id + 1), '物业缴费', 'pmsy', @namespace_id, 'EhOrganizations', @organization_id, NULL, NULL, NULL, @user_type, @biz_user_id, UTC_TIMESTAMP(), NULL);
 
-- 支付宝
-- 停车充值
-- 物业缴费
INSERT INTO `eh_payment_types`(`id`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_type`, `payment_name`, `payment_logo`, `paymentParams`, `create_time`, `update_time`)
    VALUES((@payment_type_id := @payment_type_id + 1), 'pmsy', @namespace_id, 'EhOrganizations', @organization_id, null, null, '8', '支付宝', 'cs://1/image/aW1hZ2UvTVRvelpEZ3pZalV6WmpGbFkyRXhNamRoTkdJd04yWTFNR0ZrTnpGaE5ERm1Zdw', '{\"payType\":\"A01\"}', UTC_TIMESTAMP(), NULL);
    
    
update eh_payment_accounts set app_key='402bca3f-e1be-4c9a-a975-cfa28d9b9e12' where name='beta-zuolinAccount';
update eh_payment_accounts set secret_key='566HI0h7GiloRywFYobfP+Wy2gFTygxdwK/VQGRkB8PNz0AHhMrfvExLiYYGLUCIXHdOnFRvr//gyND0sgLBag==' where name='beta-zuolinAccount';   
    