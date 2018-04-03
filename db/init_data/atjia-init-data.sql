INSERT INTO `eh_namespaces`(`id`, `name`) VALUES(999988, '爱特家迷你居');
INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES(1031, 999988, 'community_residential', UTC_TIMESTAMP());
    
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (228854, UUID(), '19055783293', '孙海红', 'cs://1/image/aW1hZ2UvTVRvMlkySmhNbVZqTm1SaU1UQXdPREkxWkRjME5HVmxNVFU1TXpBNE5UUTBZdw', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 999988);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (225367, 228854,  '0',  '13331813881',  '221616',  3, UTC_TIMESTAMP(), 999988);

INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES ('14989', '0', '辽宁', 'LIAONING', 'LN', '/辽宁', '1', '1', '', '', '2', '2', '999988');
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES ('14990', '14989', '沈阳市', 'SHENYANGSHI', 'SYS', '/辽宁/沈阳市', '2', '2', NULL, '024', '2', '1', '999988');
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES ('14991', '14990', '于洪区', 'YUHONGQU', 'YHQ', '/辽宁/沈阳市/于洪区', '3', '3', NULL, '024', '2', '0', '999988');


INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES( 240111044331053633, UUID(), 14990, '沈阳市',  14991, '于洪区', '爱特家迷你居', '爱特家迷你居', '沈阳市于洪区文大路435', NULL, '总规划用地面积： 73.65万平方米，总计容建筑面积： 110.47万平方米(约2.4万户，4个幼儿园+1个小学）；其中住宅：101.57万平方米;商业：5.53万平方米;小学：1.62万平方米；幼儿园：1.75万平方米;项目规划贯彻以人为本的思想，在互联网井喷发展的今天，创造一个设施智能化、功能多元化，布局合理、交通便捷、生活方便、绿意盎然充满活力的现代住区。以现代设计理念，设计手法，极致创新适用型全能MINI住宅是规划设计、建筑设计的基本出发点和最终目的。完善的配套设施，方便的交通系统，智能化系统的引入，宜人的空间设计以及健身、休闲、娱乐场所的设置，将让社区充满活力。', NULL, NULL, NULL, NULL, NULL, NULL,NULL, 3, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'0', 1, 2, UTC_TIMESTAMP(), 999988);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES(240111044331049236, 240111044331053633, '', 123.412761, 41.913723, 'wxry2q4mju22');
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(1003855, 240111044331053633);    

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1004258, UUID(), '万盈置业', '万盈置业', 1, 1, 1003855, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 181861, 1, 999988); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(181861, UUID(), 999988, 2, 'EhGroups', 1004258,'万盈置业','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());       

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(1003855, 0, 'PM', '沈阳万盈置业有限公司', '沈阳万盈置业有限公司是一家专业开发房地产的中外合资企业，公司现正重金打造位于沈阳市于洪区超百万平米的大型居住区项目。本案规模宏大，理念超前，精品定位，技术领先，建成后必将成为本市新的市场热点。', '/1003855', 1, 2, 'ENTERPRISE', 999988, 1004258);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1112283, 240111044331053633, 'organization', 1003855, 3, 0, UTC_TIMESTAMP());    
  
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status)
	VALUES(2109906, 1003855, 'USER', 228854, 'manager', '孙海红', 0, '13331813881', 3);	    

INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(11407, 'EhOrganizations', 1003855, 'EhUsers', 228854, 1001, 1, UTC_TIMESTAMP());

INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES(1235, 999988, 'COMMUNITY', 240111044331053633, UTC_TIMESTAMP());	

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('163', 'app.agreements.url', '', 'the relative path for at mini ju app agreements', '999988', NULL);	
	
INSERT INTO `eh_version_realm` VALUES ('58', 'Android_AtMini', null, UTC_TIMESTAMP(), '999988');
INSERT INTO `eh_version_realm` VALUES ('59', 'iOS_AtMini', null, UTC_TIMESTAMP(), '999988');

INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES(90,58,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES(91,59,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());


INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999988, 'sms.default.yzx', 1, 'zh_CN', '验证码-@家', '28964');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999988, 'sms.default.yzx', 4, 'zh_CN', '派单-@家', '28965');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999988, 'sms.default.yzx', 6, 'zh_CN', '任务2-@家', '28967');    
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999988, 'sms.default.yzx', 6, 'zh_CN', '门禁授权-@家', '28968');    
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999988, 'sms.default.yzx', 7, 'zh_CN', '看楼申请-@家', '28969');    
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999988, 'sms.default.yzx', 7, 'zh_CN', '新报修-@家', '28970');    


INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200993, 1, 0, '普通', '帖子/普通', 1, 2, UTC_TIMESTAMP(), 999988);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200994, 1, 0, '二手和租售', '帖子/二手和租售', 1, 2, UTC_TIMESTAMP(), 999988);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200995, 1, 0, '免费物品', '帖子/免费物品', 1, 2, UTC_TIMESTAMP(), 999988);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200996, 1, 0, '失物招领', '帖子/失物招领', 1, 2, UTC_TIMESTAMP(), 999988);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(200997, 1, 0, '紧急通知', '帖子/紧急通知', 1, 2, UTC_TIMESTAMP(), 999988);


INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
    VALUES ('263', '999988', 'ServiceMarketLayout', '{\"versionCode\":\"2016081701\",\"versionName\":\"3.3.0\",\"layoutName\":\"ServiceMarketLayout\",\"displayName\":\"服务市场\",\"groups\":[{\"groupName\":\"\",\"widget\":\"Banners\",\"instanceConfig\":{\"itemGroup\":\"Default\"},\"style\":\"Default\",\"defaultOrder\":1,\"separatorFlag\":1,\"separatorHeight\":21},{\"groupName\":\"滚动广告\",\"widget\":\"Bulletins\",\"instanceConfig\":{\"itemGroup\":\"\"},\"style\":\"Default\",\"defaultOrder\":4,\"separatorFlag\":1,\"separatorHeight\":21},{\"groupName\":\"\",\"widget\":\"Coupons\",\"instanceConfig\":{\"itemGroup\":\"Coupons\"},\"style\":\"Default\",\"defaultOrder\":3,\"separatorFlag\":1,\"separatorHeight\":21},{\"groupName\":\"商家服务\",\"widget\":\"Navigator\",\"instanceConfig\":{\"itemGroup\":\"Bizs\"},\"style\":\"Default\",\"defaultOrder\":5,\"separatorFlag\":0,\"separatorHeight\":0,\"editFlag\":1}]}', '2016081701', '0', '2', '2016-06-01 10:41:25', 'default', '0', '0', '0');
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
    VALUES ('264', '999988', 'ServiceMarketLayout', '{\"versionCode\":\"2016081701\",\"versionName\":\"3.3.0\",\"layoutName\":\"ServiceMarketLayout\",\"displayName\":\"服务市场\",\"groups\":[{\"groupName\":\"\",\"widget\":\"Banners\",\"instanceConfig\":{\"itemGroup\":\"Default\"},\"style\":\"Default\",\"defaultOrder\":1,\"separatorFlag\":1,\"separatorHeight\":21},{\"groupName\":\"滚动广告\",\"widget\":\"Bulletins\",\"instanceConfig\":{\"itemGroup\":\"\"},\"style\":\"Default\",\"defaultOrder\":4,\"separatorFlag\":1,\"separatorHeight\":21},{\"groupName\":\"\",\"widget\":\"Coupons\",\"instanceConfig\":{\"itemGroup\":\"Coupons\"},\"style\":\"Default\",\"defaultOrder\":3,\"separatorFlag\":1,\"separatorHeight\":21},{\"groupName\":\"商家服务\",\"widget\":\"Navigator\",\"instanceConfig\":{\"itemGroup\":\"Bizs\"},\"style\":\"Default\",\"defaultOrder\":5,\"separatorFlag\":0,\"separatorHeight\":0,\"editFlag\":1}]}', '2016081701', '0', '2', '2016-03-12 19:16:25', 'pm_admin', '0', '0', '0');

    
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`) 
    VALUES ('10666', '999988', '0', '/home', 'Default', '0', '0', '爱特家迷你居', 'atjia', 'cs://1/image/aW1hZ2UvTVRwak5tTXhOelUxWm1ReE9ETmlaV0l3T0RnMk1qTXdOekEzWTJaa01tWXlNQQ', 14, '{"url":"https://core.zuolin.com/park-introduction/index.html?hideNavigationBar=1#sign_suffix"}', NULL, NULL, '2', '1', '0', UTC_TIMESTAMP(), NULL, 'default', '0');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`) 
    VALUES ('10667', '999988', '0', '/home', 'Default', '0', '0', '爱特家迷你居', 'atjia', 'cs://1/image/aW1hZ2UvTVRwak5tTXhOelUxWm1ReE9ETmlaV0l3T0RnMk1qTXdOekEzWTJaa01tWXlNQQ', 14, '{"url":"https://core.zuolin.com/park-introduction/index.html?hideNavigationBar=1#sign_suffix"}', NULL, NULL, '2', '1', '0', UTC_TIMESTAMP(), NULL, 'pm_admin', '0');
    

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES('10012','资源预订','0',NULL,'0','999988');
-- default 小区游客场景    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
    VALUES (110150, 999988, 0, 0, 0, '/home', 'Bizs', '物业报修2.0', '物业服务', 'cs://1/image/aW1hZ2UvTVRvNE1USmhZakV3WTJSa1pqUmxPVGN5T1dFME1qUmhNVEprTVRFNE0yRmxZdw', 1, 1, 14, '{"url":"http://core.zuolin.com/property_service/index.html?hideNavigationBar=1#/my_service#sign_suffix"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'default', '1');    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
    VALUES (110151, 999988, '0', '0', '0', '/home', 'Bizs', 'PAY', '物业缴费', 'cs://1/image/aW1hZ2UvTVRvMVpqZGlOak00TXpCaE5tTXdaREJpWkdWallXWTRNVEF5Tm1ReFlXUTJOUQ', '1', '1', '14', '{\"url\":\"https://core.zuolin.com/property_fee/index.html?hideNavigationBar=1#/bill_query?sign_suffix\"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'default', '1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
    VALUES (110152, 999988, '0', '0', '0', '/home', 'Bizs', 'PARKING_RECHARGE', '停车充值', 'cs://1/image/aW1hZ2UvTVRwaFpXRmtZek5qTWpobE1UWTRaVE5qWlRjek4yWTFaRFU1WlRJeVlqUXlNQQ', '1', '1', '30', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'default', '1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
    VALUES (110153, 999988, '0', '0', '0', '/home', 'Bizs', '资源预约', '资源预约', 'cs://1/image/aW1hZ2UvTVRvd01USTFaRGMyTURnMk5qUXlZV1l3WTJVMlpXVmpNemMyTldNME5tWTBNdw','1','1','49','{\"resourceTypeId\":10012,\"pageType\":0,\"communityFilterFlag\":0}','0','0','1','1','1','0',NULL,NULL,NULL,'1','default','1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
    VALUES (110154, 999988, '0', '0', '0', '/home', 'Bizs', 'SERVICE_HOT_LINE', '服务热线', 'cs://1/image/aW1hZ2UvTVRvMFkyRXhZMk5qTVRWaVpUZzBNamM0TUdGbE1HUmtNRE5qWkRrMk56TmhNQQ', '1', '1', '45', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'default', '1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
    VALUES (110155, 999988, '0', '0', '0', '/home', 'Bizs', '门禁', '门禁', 'cs://1/image/aW1hZ2UvTVRwbU9XTm1abU0xWXpjMU0yTXhOMlUzTURGaE9XSm1OR1JsWkRZelpqQTNaUQ', '1', '1', '40', '{\"isSupportQR\":1,\"isSupportSmart\":1}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'default', '1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
    VALUES (110156, 999988, '0', '0', '0', '/home', 'Bizs', '视频点播', '视频点播', 'cs://1/image/aW1hZ2UvTVRvM1lqVTBNekptWW1ZeU4ySTNaVEl5WVRnNE0ySmxNemcwWldNM05EUmtOUQ', '1', '1', '14', '{\"url\":\"http://172.17.1.1:8080"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'default', '1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
    VALUES (110157, 999988, '0', '0', '0', '/home', 'Bizs', '更多', '更多', 'cs://1/image/aW1hZ2UvTVRwaE9UVTRORE0wTjJOaFlXUmpZamhoWkRFeU9XSXdNMlkyWkRrNE1UZzNZdw', '1', '1', '1', '{\"itemLocation\":\"/home\",\"itemGroup\":\"Bizs\"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'default', '1');
-- 为验收而添加俱乐部 by lqs 20170120
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
    VALUES (111501, 999988, '0', '0', '0', '/home', 'Bizs', 'CLUB', '俱乐部', 'cs://1/image/aW1hZ2UvTVRveE4yVmxOak0wWkdReU9UY3dPVGMzTlRrM05UWmxOV1U1TVRneFltTTVaZw', '1', '1', '36', '{"privateFlag": 0}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'default', '1');

	
-- pm_admin 小区物业管理公司场景
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
    VALUES (110158, 999988, '0', '0', '0', '/home', 'Bizs', 'PUNCH', '打卡考勤', 'cs://1/image/aW1hZ2UvTVRveVltSmhNMlkzTjJJMU5URmtOMkl5WXpZNE9UaG1ZV0l6WlRBd01EVXhOUQ', '1', '1', '23', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
    VALUES (110159, 999988, '0', '0', '0', '/home', 'Bizs', 'CONTACTS', '通讯录', 'cs://1/image/aW1hZ2UvTVRvelpHTmtaR0UwWXpOaFl6QXpOelJsT1dJNE1qRTRPVEpqWmpRMVpXTmlNQQ', '1', '1', '46', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
    VALUES (110160, 999988, '0', '0', '0', '/home', 'Bizs', '视频会议', '视频会议', 'cs://1/image/aW1hZ2UvTVRvNU5UazVaVGxoT1dVNVkyTTRNRFkwTldJMk56QmlObU15T0RWa1pHSTJZUQ', '1', '1', '27', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
    VALUES (110161, 999988, '0', '0', '0', '/home', 'Bizs', 'PM_TASK', '任务管理', 'cs://1/image/aW1hZ2UvTVRveE1XUmxOamRqWWpNd1pHVXlabVpqT0RreVpEUTJOakU1TmpSbFptVTNNZw', '1', '1', '51', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
    VALUES (110162, 999988, '0', '0', '0', '/home', 'Bizs', '门禁', '门禁', 'cs://1/image/aW1hZ2UvTVRwbU9XTm1abU0xWXpjMU0yTXhOMlUzTURGaE9XSm1OR1JsWkRZelpqQTNaUQ', '1', '1', '40', '{\"isSupportQR\":1,\"isSupportSmart\":1}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
    VALUES (110163, 999988, '0', '0', '0', '/home', 'Bizs', '视频点播', '视频点播', 'cs://1/image/aW1hZ2UvTVRvM1lqVTBNekptWW1ZeU4ySTNaVEl5WVRnNE0ySmxNemcwWldNM05EUmtOUQ', '1', '1', '14', '{\"url\":\"http://172.17.1.1:8080"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin', '1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
    VALUES (110164, 999988, '0', '0', '0', '/home', 'Bizs', '更多', '更多', 'cs://1/image/aW1hZ2UvTVRwaE9UVTRORE0wTjJOaFlXUmpZamhoWkRFeU9XSXdNMlkyWkRrNE1UZzNZdw', '1', '1', '1', '{\"itemLocation\":\"/home\",\"itemGroup\":\"Bizs\"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin', '1');
-- 为验收而添加俱乐部 by lqs 20170120
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
    VALUES (111502, 999988, '0', '0', '0', '/home', 'Bizs', 'CLUB', '俱乐部', 'cs://1/image/aW1hZ2UvTVRveE4yVmxOak0wWkdReU9UY3dPVGMzTlRrM05UWmxOV1U1TVRneFltTTVaZw', '1', '1', '36', '{"privateFlag": 0}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin', '1');

    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
    VALUES (110165, 999988, '0', '0', '0', '/home', 'Coupons', '优惠券', '优惠券', 'cs://1/image/aW1hZ2UvTVRwak1tUmxORGRqTlRWall6RXlaVEEyTVdFeFpXUmtNMk0yWkRRMk0yRTRZdw', '3', '1', '14', '{\"url\":\"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fpromotion%2Fall%3F_k%3Dzlbiz#sign_suffix\"}', '1', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'default', '1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
    VALUES (110166, 999988, '0', '0', '0', '/home', 'Coupons', '左邻小站', '左邻小站', 'cs://1/image/aW1hZ2UvTVRvNFpEWTBORGsxTVRRMFpUVTRZMlV5WlRReVlUTmtOVEpqTVRReVltTTBaZw', '5', '1', '14', '{\"url\":\"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14736680001469255617%3F_k%3Dzlbiz#sign_suffix\"}', '2', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'default', '1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
    VALUES (110167, 999988, '0', '0', '0', '/home', 'Coupons', '优惠券', '优惠券', 'cs://1/image/aW1hZ2UvTVRwak1tUmxORGRqTlRWall6RXlaVEEyTVdFeFpXUmtNMk0yWkRRMk0yRTRZdw', '3', '1', '14', '{\"url\":\"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fpromotion%2Fall%3F_k%3Dzlbiz#sign_suffix\"}', '1', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin', '1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
    VALUES (110168, 999988, '0', '0', '0', '/home', 'Coupons', '左邻小站', '左邻小站', 'cs://1/image/aW1hZ2UvTVRvNFpEWTBORGsxTVRRMFpUVTRZMlV5WlRReVlUTmtOVEpqTVRReVltTTBaZw', '5', '1', '14', '{\"url\":\"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14736680001469255617%3F_k%3Dzlbiz#sign_suffix\"}', '2', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin', '1');

    
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
	VALUES(179957, 240111044331053633, '19栋', '19栋', 0, '', '', 0, NULL, NULL, NULL, '', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999988);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
	VALUES(179958, 240111044331053633, '20栋', '20栋', 0, '', '', 0, NULL, NULL, NULL, '', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999988);

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
    VALUES(239825274387102737,UUID(),240111044331053633, 14990, '沈阳市',  14991, '于洪区' ,'19栋-1-501','19栋','1-501','2','0',UTC_TIMESTAMP(), 999988);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
    VALUES(239825274387102738,UUID(),240111044331053633, 14990, '沈阳市',  14991, '于洪区' ,'20栋-3-603','20栋','3-603','2','0',UTC_TIMESTAMP(), 999988);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
    VALUES(239825274387102739,UUID(),240111044331053633, 14990, '沈阳市',  14991, '于洪区' ,'20栋-3-504','20栋','3-504','2','0',UTC_TIMESTAMP(), 999988);	
    
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
    VALUES (20590, 1003855, 240111044331053633, 239825274387102737, '19栋-1-501', '0');     
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
    VALUES (20591, 1003855, 240111044331053633, 239825274387102738, '20栋-3-603', '0');     
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
    VALUES (20592, 1003855, 240111044331053633, 239825274387102739, '20栋-3-504', '0');     





-- add by xiongying 20160921 任务 #2187
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`) 
    VALUES ('10790', '999988', '0', '/home', 'Default', '0', '0', '活动1', 'atjia', 'cs://1/image/aW1hZ2UvTVRvMk1UbGhPVGswWTJFMk9ETmhNVGRtWm1Rd05HVmtNbUV4Tm1Vd09HSm1NZw', 14, '{"url":"https://core.zuolin.com/park-introduction/index.html?hideNavigationBar=1#sign_suffix"}', NULL, NULL, '2', '1', '0', UTC_TIMESTAMP(), NULL, 'default', '0');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`) 
    VALUES ('10791', '999988', '0', '/home', 'Default', '0', '0', '活动1', 'atjia', 'cs://1/image/aW1hZ2UvTVRvMk1UbGhPVGswWTJFMk9ETmhNVGRtWm1Rd05HVmtNbUV4Tm1Vd09HSm1NZw', 14, '{"url":"https://core.zuolin.com/park-introduction/index.html?hideNavigationBar=1#sign_suffix"}', NULL, NULL, '2', '1', '0', UTC_TIMESTAMP(), NULL, 'pm_admin', '0');

INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`) 
    VALUES ('10792', '999988', '0', '/home', 'Default', '0', '0', '活动2', 'atjia', 'cs://1/image/aW1hZ2UvTVRwbU9HWmpNVE0zTWpsak1UVXhNV0V4TnpRelpqa3hORFExWlRSallURTVOZw', 14, '{"url":"https://core.zuolin.com/park-introduction/index.html?hideNavigationBar=1#sign_suffix"}', NULL, NULL, '2', '1', '0', UTC_TIMESTAMP(), NULL, 'default', '0');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`) 
    VALUES ('10793', '999988', '0', '/home', 'Default', '0', '0', '活动2', 'atjia', 'cs://1/image/aW1hZ2UvTVRwbU9HWmpNVE0zTWpsak1UVXhNV0V4TnpRelpqa3hORFExWlRSallURTVOZw', 14, '{"url":"https://core.zuolin.com/park-introduction/index.html?hideNavigationBar=1#sign_suffix"}', NULL, NULL, '2', '1', '0', UTC_TIMESTAMP(), NULL, 'pm_admin', '0');

INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`) 
    VALUES ('10794', '999988', '0', '/home', 'Default', '0', '0', '活动3', 'atjia', 'cs://1/image/aW1hZ2UvTVRvelpEaG1OMk0yTldNME5EZzVaak15WTJabE1UZzNaRGszTVdRNVlqTm1ZZw', 14, '{"url":"https://core.zuolin.com/park-introduction/index.html?hideNavigationBar=1#sign_suffix"}', NULL, NULL, '2', '1', '0', UTC_TIMESTAMP(), NULL, 'default', '0');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`) 
    VALUES ('10795', '999988', '0', '/home', 'Default', '0', '0', '活动3', 'atjia', 'cs://1/image/aW1hZ2UvTVRvelpEaG1OMk0yTldNME5EZzVaak15WTJabE1UZzNaRGszTVdRNVlqTm1ZZw', 14, '{"url":"https://core.zuolin.com/park-introduction/index.html?hideNavigationBar=1#sign_suffix"}', NULL, NULL, '2', '1', '0', UTC_TIMESTAMP(), NULL, 'pm_admin', '0');

INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`) 
    VALUES ('10796', '999988', '0', '/home', 'Default', '0', '0', '活动4', 'atjia', 'cs://1/image/aW1hZ2UvTVRwak5HSTRZVFl4TUdZMU1qRTJPRGxtTVRsaE1HSTJNREZqTlRBeU56RTVaUQ', 14, '{"url":"https://core.zuolin.com/park-introduction/index.html?hideNavigationBar=1#sign_suffix"}', NULL, NULL, '2', '1', '0', UTC_TIMESTAMP(), NULL, 'default', '0');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`) 
    VALUES ('10797', '999988', '0', '/home', 'Default', '0', '0', '活动4', 'atjia', 'cs://1/image/aW1hZ2UvTVRwak5HSTRZVFl4TUdZMU1qRTJPRGxtTVRsaE1HSTJNREZqTlRBeU56RTVaUQ', 14, '{"url":"https://core.zuolin.com/park-introduction/index.html?hideNavigationBar=1#sign_suffix"}', NULL, NULL, '2', '1', '0', UTC_TIMESTAMP(), NULL, 'pm_admin', '0');


-- 
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203001, 2, 0, '运动与音乐', '兴趣/运动与音乐', 10, 2, UTC_TIMESTAMP(), 999988);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203002, 2, 0, '旅游摄影', '兴趣/旅游摄影', 20, 2, UTC_TIMESTAMP(), 999988);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203003, 2, 0, '亲子与教育', '兴趣/亲子与教育', 30, 2, UTC_TIMESTAMP(), 999988);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203004, 2, 0, '美食与厨艺', '兴趣/美食与厨艺', 40, 2, UTC_TIMESTAMP(), 999988);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203005, 2, 0, '家庭装饰', '兴趣/家庭装饰', 50, 2, UTC_TIMESTAMP(), 999988);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203006, 2, 0, '美容化妆', '兴趣/美容化妆', 60, 2, UTC_TIMESTAMP(), 999988);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203007, 2, 0, '宠物会', '兴趣/宠物会', 70, 2, UTC_TIMESTAMP(), 999988);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203008, 2, 0, '名牌汇', '兴趣/名牌汇', 80, 2, UTC_TIMESTAMP(), 999988);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203009, 2, 0, '同事群', '兴趣/同事群', 90, 2, UTC_TIMESTAMP(), 999988);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203010, 2, 0, '老乡群', '兴趣/老乡群', 100, 2, UTC_TIMESTAMP(), 999988);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203011, 2, 0, '同学群', '兴趣/同学群', 110, 2, UTC_TIMESTAMP(), 999988);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203012, 2, 0, '拼车', '兴趣/拼车', 120, 2, UTC_TIMESTAMP(), 999988);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203013, 2, 0, '其他', '兴趣/其他', 200, 2, UTC_TIMESTAMP(), 999988);
    