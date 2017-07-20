set @namespaceId = 23456; -- 国贸namespaceId-待定，园区等，待定
-- 国贸EMS快递公司 -- 图标待定 --url等，待定 -- TODO
INSERT INTO `eh_express_companies` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `logo`, `description`, `url`, `app_key`, `app_secret`, `authorization`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES ('2', @namespaceId, '', '0', '0', 'EMS', 'cs://1/image/aW1hZ2UvTVRwak9XSTJOVFJqWXpjMVkyTmtNVGt4WW1NNU1qaGlNR0k1WlRNelpXRTJNdw', '国贸项目，EMS快递公司', NULL, NULL, NULL, NULL, '2', '0', '2017-07-21 00:45:35', '2017-07-21 00:45:35', '0');
-- 国贸中国邮政快递公司
INSERT INTO `eh_express_companies` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `logo`, `description`, `url`, `app_key`, `app_secret`, `authorization`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES ('3', @namespaceId, '', '0', '0', '中国邮政', 'cs://1/image/aW1hZ2UvTVRwak9XSTJOVFJqWXpjMVkyTmtNVGt4WW1NNU1qaGlNR0k1WlRNelpXRTJNdw', '国贸项目，中国邮政快递公司', NULL, NULL, NULL, NULL, '2', '0', '2017-07-21 00:45:35', '2017-07-21 00:45:35', '0');

-- 华润的EMS的namespace改成 华润namespaceid 999985
  UPDATE `eh_express_companies` SET `namespace_id`=999985 WHERE `id`= 1;
  
-- 标签的基本设置
INSERT INTO `ehcore`.`eh_express_param_settings` (`id`, `namespace_id`, `owner_type`, `owner_id`, `express_user_setting_show_flag`, `business_note_setting_show_flag`, `hotline_setting_show_flag`, `hotline_flag`, `business_note`, `business_note_flag`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (1, 999985, 'EhNamespaces', 999985, 1, 1, 1, NULL, NULL, NULL, 2, 0, NOW(), NOW(), 0);
INSERT INTO `ehcore`.`eh_express_param_settings` (`id`, `namespace_id`, `owner_type`, `owner_id`, `express_user_setting_show_flag`, `business_note_setting_show_flag`, `hotline_setting_show_flag`, `hotline_flag`, `business_note`, `business_note_flag`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES (2, @namespaceId, 'EhNamespaces', @namespaceId, 0, 1, 1, NULL, NULL, NULL, 2, 0, NOW(), NOW(), 0);
