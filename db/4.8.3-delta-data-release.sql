-- by dengs,2017.08.28 快递2.0
set @namespaceId = 999968; -- 国贸namespaceId
set @community_id = 240111044331050363;
-- 国贸EMS快递公司 -- 图标待定 --url等，待定 -- TODO
set @guo_mao_ems_logo = 'cs://1/image/aW1hZ2UvTVRwak9XSTJOVFJqWXpjMVkyTmtNVGt4WW1NNU1qaGlNR0k1WlRNelpXRTJNdw';
set @guo_mao_ems_order_url = 'http://60.205.8.187:18001/api/gateway';
set @guo_mao_ems_logistics_url = 'http://60.205.8.187:18001/api/gateway';
set @guo_mao_ems_app_key = '461bdd5e2655492434936e96fd3eab7d';
set @guo_mao_ems_app_secret = '9134a30fdfba770d1b64438d9d0aafed';
set @guo_mao_ems_authorization = '7cacaabcbb996fdcbe4b42096e09724c';
INSERT INTO `eh_express_companies` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `logo`, `description`, `order_url`, `logistics_url`, `app_key`, `app_secret`, `authorization`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES ('2', @namespaceId, 'EhNamespaces', @namespaceId, '0', 'EMS', @guo_mao_ems_logo, '国贸项目，EMS快递公司', @guo_mao_ems_order_url,@guo_mao_ems_logistics_url, @guo_mao_ems_app_key, @guo_mao_ems_app_secret, @guo_mao_ems_authorization, '2', '0', NOW(), NOW(), '0');
INSERT INTO `eh_express_companies` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `logo`, `description`, `order_url`, `logistics_url`, `app_key`, `app_secret`, `authorization`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES ('10003', @namespaceId, 'community', @community_id, '2', 'EMS', '', '国贸项目，EMS快递公司', NULL,NULL, NULL, NULL, NULL, '2', '0', NOW(), NOW(), '0');
-- 国贸中国邮政快递公司
set @guo_mao_chinapost_logo = 'cs://1/image/aW1hZ2UvTVRvM1pEWTBOamxrWW1SbE1EQXlZamcwTlRsbE1qVTFPR1UyWkRVd04yTXdaUQ';
set @guo_mao_chinapost_order_url = 'http://222.222.2.155:8001';
set @guo_mao_chinapost_logistics_url = 'http://211.156.198.97/zdxtJkServer/zhddws/MailTtService_Gn?wsdl';
set @guo_mao_chinapost_app_key = '123';
set @guo_mao_chinapost_app_secret = 'FC480127D90D26DE382506EE5D409F46';
INSERT INTO `eh_express_companies` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `logo`, `description`, `order_url`, `logistics_url`, `app_key`, `app_secret`, `authorization`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES ('3', @namespaceId, 'EhNamespaces', @namespaceId, '0', '中国邮政', @guo_mao_chinapost_logo, '国贸项目，中国邮政快递公司', @guo_mao_chinapost_order_url,@guo_mao_chinapost_logistics_url, @guo_mao_chinapost_app_key, @guo_mao_chinapost_app_secret, NULL, '2', '0', NOW(), NOW(), '0');
INSERT INTO `eh_express_companies` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `logo`, `description`, `order_url`, `logistics_url`, `app_key`, `app_secret`, `authorization`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES ('10004', @namespaceId, 'community', @community_id, '3', '中国邮政', '', '国贸项目，中国邮政快递公司', NULL,NULL, NULL, NULL, NULL, '2', '0', NOW(), NOW(), '0');
-- 华润的EMS的namespace改成 华润namespaceid 999985
UPDATE `eh_express_companies` SET `namespace_id`=999985, `owner_type`='EhNamespaces',`owner_id`=999985 WHERE `id`= 1;

-- 标签的基本设置
INSERT INTO `eh_express_param_settings` (`id`, `namespace_id`, `owner_type`, `owner_id`, `express_user_setting_show_flag`, `business_note_setting_show_flag`, `hotline_setting_show_flag`, `hotline_flag`, `business_note`, `business_note_flag`, `send_mode`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (1, 999985, 'EhNamespaces', 999985, 1, 1, 1, NULL, NULL, NULL,1, 2, 0, NOW(), NOW(), 0);
INSERT INTO `eh_express_param_settings` (`id`, `namespace_id`, `owner_type`, `owner_id`, `express_user_setting_show_flag`, `business_note_setting_show_flag`, `hotline_setting_show_flag`, `hotline_flag`, `business_note`, `business_note_flag`, `send_mode`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (2, @namespaceId, 'EhNamespaces', @namespaceId, 0, 1, 1, NULL, NULL, NULL,2, 2, 0, NOW(), NOW(), 0);

-- 国贸 热线与业务说明的初始化数据
INSERT INTO `eh_express_param_settings` (`id`, `namespace_id`, `owner_type`, `owner_id`, `express_user_setting_show_flag`, `business_note_setting_show_flag`, `hotline_setting_show_flag`, `hotline_flag`, `business_note`, `business_note_flag`, `send_mode`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (3, 999985, 'community',240111044331055035, 0, 1, 1, 0, '', 0, NULL, 2, 0, NOW(), NOW(), 0);
INSERT INTO `eh_express_param_settings` (`id`, `namespace_id`, `owner_type`, `owner_id`, `express_user_setting_show_flag`, `business_note_setting_show_flag`, `hotline_setting_show_flag`, `hotline_flag`, `business_note`, `business_note_flag`, `send_mode`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (4, 999985, 'community', 240111044331055036, 0, 1, 1, 0, '', 0, NULL, 2, 0, NOW(), NOW(), 0);
INSERT INTO `eh_express_param_settings` (`id`, `namespace_id`, `owner_type`, `owner_id`, `express_user_setting_show_flag`, `business_note_setting_show_flag`, `hotline_setting_show_flag`, `hotline_flag`, `business_note`, `business_note_flag`, `send_mode`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (5, @namespaceId, 'community', @community_id, 0, 1, 1, 0, '', 0, NULL, 2, 0, NOW(), NOW(), 0);
-- 业务包装类型文案等
INSERT INTO `eh_express_company_businesses` (`id`, `namespace_id`, `owner_type`, `owner_id`, `express_company_id`, `send_type`, `send_type_name`, `package_types`, `insured_documents`, `order_status_collections`, `pay_type`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES ('1', 999985, 'EhNamespaces', 999985, '1', '1', '标准快递', '[]', NULL, '[{"status": 1},{"status": 2},{"status": 3},{"status": 4}]', '1', '2', '0', NOW(), NOW(), '0');
-- EMS业务暂时 去掉包装类型 {"packageType": 3},{"packageType": 1}
INSERT INTO `eh_express_company_businesses` (`id`, `namespace_id`, `owner_type`, `owner_id`, `express_company_id`, `send_type`, `send_type_name`, `package_types`, `insured_documents`, `order_status_collections`, `pay_type`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES ('2', @namespaceId, 'EhNamespaces', @namespaceId, '2', '3', 'EMS标准快递', '[]', '请确认寄件物品价值不超过5万元，贵重物品务必保价，保价费=保价金额*0.5%，最低1元','[{"status": 1},{"status": 2},{"status": 5},{"status": 4}]', '2', '2', '0', NOW(), NOW(), '0');
INSERT INTO `eh_express_company_businesses` (`id`, `namespace_id`, `owner_type`, `owner_id`, `express_company_id`, `send_type`, `send_type_name`, `package_types`, `insured_documents`, `order_status_collections`, `pay_type`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES ('3', @namespaceId, 'EhNamespaces', @namespaceId, '3', '9', '邮政快递包裹', '[{"packageType": 3},{"packageType": 2},{"packageType": 1}]', '请确认寄件物品价值不超过2万元，贵重物品务必保价，保价费=保价金额*1%，最低2元', '[{"status": 1},{"status": 2},{"status": 5},{"status": 4}]','1',  '2', '0', NOW(), NOW(), '0');
INSERT INTO `eh_express_company_businesses` (`id`, `namespace_id`, `owner_type`, `owner_id`, `express_company_id`, `send_type`, `send_type_name`, `package_types`, `insured_documents`, `order_status_collections`, `pay_type`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES ('4', @namespaceId, 'EhNamespaces', @namespaceId, '3', '2', '同城信筒快件', '[{"packageType": 4},{"packageType": 5}]', NULL, '[{"status": 1},{"status": 2},{"status": 5},{"status": 4}]', '1', '2', '0', NOW(), NOW(), '0');

-- 国贸单点登录的appkey，和seckey
set @appKey = 'de875e40-1c5f-4a0c-94a6-0b37421b8554';
set @secretKey = 'k9+3iUUSlUah1Uggv5ZKbTEktcIuvs7834ZThJS/CmA4eVBR2msOBak9uvut1Io0gZ9tdFJ0LpJ9ELfes8XXZw==';

set @eh_apps_id = (select MAX(id) FROM eh_apps);
INSERT INTO `eh_apps` (`id`, `creator_uid`, `app_key`, `secret_key`, `name`, `description`, `status`, `create_time`, `update_uid`, `update_time`) VALUES ((@eh_apps_id:=@eh_apps_id+1), '1', @appKey, @secretKey, 'guomao', 'guomao app key', '1', NOW(), NULL, NULL);
-- appkey mapping namespace
set @eh_app_namespace_mappings_id = (select MAX(id) FROM eh_app_namespace_mappings);
INSERT INTO `eh_app_namespace_mappings` (`id`, `namespace_id`, `app_key`, `community_id`) VALUES ((@eh_app_namespace_mappings_id:=@eh_app_namespace_mappings_id+1), @namespace_id, @appKey, @community_id);

-- 菜单修改 by dengs 2017.07.31
UPDATE eh_web_menus SET  `name` = '参数设置' where id = 40710;
UPDATE eh_web_menus SET  `name` = '订单管理' where id = 40720;

-- payserverapp支付接口地址
SET @configuration_id = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
    VALUES (@configuration_id:=@configuration_id+1, 'guomao.payserver.url', 'http://pay.zuolin.com/EDS_PAY/rest/pay_common/payInfo_record/save_payInfo_record', '左邻支付平台地址', 0, NULL);
-- by dengs,2017.08.28 快递2.0 end