
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (225500, UUID(), '9201905', '苏娇娇', 'cs://1/image/aW1hZ2UvTVRvMlkySmhNbVZqTm1SaU1UQXdPREkxWkRjME5HVmxNVFU1TXpBNE5UUTBZdw', 1, 45, '1', '2',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (223767, 225500,  '0',  '13760240661',  '221616',  3, UTC_TIMESTAMP(), 999991);

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (225501, UUID(), '9201906', '唐季春', 'cs://1/image/aW1hZ2UvTVRvMlkySmhNbVZqTm1SaU1UQXdPREkxWkRjME5HVmxNVFU1TXpBNE5UUTBZdw', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (223768, 225501,  '0',  '15818603192',  '221616',  3, UTC_TIMESTAMP(), 999991);

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (225502, UUID(), '9201907', '吴珊', 'cs://1/image/aW1hZ2UvTVRvMlkySmhNbVZqTm1SaU1UQXdPREkxWkRjME5HVmxNVFU1TXpBNE5UUTBZdw', 1, 45, '1', '2',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (223769, 225502,  '0',  '13798204538',  '221616',  3, UTC_TIMESTAMP(), 999991);
    

INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES ('14977', '0', '广东', 'GUANGDONG', 'GD', '/广东', '1', '1', '', '', '2', '2', 999991);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES ('14978', '14977', '深圳市', 'SHENZHENSHI', 'SZS', '/广东/深圳市', '2', '2', NULL, '0755', '2', '1', 999991);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES ('14979', '14978', '南山区', 'NANSHANQU', 'NSQ', '/广东/深圳市/南山区', '3', '3', NULL, '0755', '2', '0', 999991);


     
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES( 240111044331053517, UUID(), 14978, '深圳市',  14979, '南山区', '深圳威新软件园', '深圳威新', '深圳市南山区高新南九道', NULL, '高新区里程碑式的研发办公建筑，企业总部基地。运用科技和设计，打造甲级品质的节能、低耗、绿色生态商务空间，塑造立体的艺术、活力、科技体验生活方式中心，为高新园区产业升级提供了宝贵的空间载体。', NULL, NULL, NULL, NULL, NULL, NULL,NULL, 113, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'1', 180773, 180774, UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES(240111044331049117, 240111044331053517, '', 113.956081, 22.533245, 'ws101nh39jkd');	
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(1002757, 240111044331053517);


    
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1003093, UUID(), '深圳威新软件园', '深圳威新软件园', 1, 1, 1002757, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 180772, 1, 999991); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(180772, UUID(), 999991, 2, 'EhGroups', 1003093,'深圳威新软件园','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(180773, UUID(), 999991, 2, 'EhGroups', 0,'深圳威新软件园论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(180774, UUID(), 999991, 2, 'EhGroups', 0,'深圳威新软件园意见反馈论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());    

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(1002757, 0, 'PM', '深圳威新', '', '/1002757', 1, 2, 'ENTERPRISE', 999991, 1003093);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1111161, 240111044331053517, 'organization', 1002757, 3, 0, UTC_TIMESTAMP());    
  
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status)
	VALUES(2105192, 1002757, 'USER', 225501, 'manager', '唐季春', 0, '15818603192', 3);	
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status)
	VALUES(2105193, 1002757, 'USER', 225502, 'manager', '吴珊', 0, '13798204538', 3);	
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status)
	VALUES(2105194, 1002757, 'USER', 225500, 'manager', '吴珊', 0, '13798204538', 3);

INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(10995, 'EhOrganizations', 1002757, 'EhUsers', 225501, 1001, 1, UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(10996, 'EhOrganizations', 1002757, 'EhUsers', 225502, 1001, 1, UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(10997, 'EhOrganizations', 1002757, 'EhUsers', 225500, 1001, 1, UTC_TIMESTAMP());

  
INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES(1225, 999991, 'COMMUNITY', 240111044331053517, UTC_TIMESTAMP());


INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('124', 'app.agreements.url', 'https://core.zuolin.com/mobile/static/app_agreements/ibase_agreements.html', 'the relative path for chuneng app agreements', '999991', NULL);	
	
INSERT INTO `eh_version_realm` VALUES ('40', 'Android_WeixinLink', null, UTC_TIMESTAMP(), '999991');
INSERT INTO `eh_version_realm` VALUES ('41', 'iOS_WeixinLink', null, UTC_TIMESTAMP(), '999991');

insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) values(47,40,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) values(48,41,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());


INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999991, 'sms.default.yzx', 1, 'zh_CN', '验证码-威新', '23050');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999991, 'sms.default.yzx', 4, 'zh_CN', '派单-威新', '23051');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999991, 'sms.default.yzx', 6, 'zh_CN', '任务2-威新', '23053');    
 


-- 园区管理员场景
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (228, 999991, 'ServiceMarketLayout', '{"versionCode":"2015120406","versionName":"3.0.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Coupons","instanceConfig":{"itemGroup":"Coupons"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0}]}', '2015120406', '0', '2', '2016-07-12 17:02:30', 'pm_admin');
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (230, 999991, 'BizLayout', '{"versionCode":"2015120406","versionName":"3.0.0","displayName":"店铺列表","layoutName":"BizLayout","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"BizList"},"style":"Gallery","defaultOrder":2,"separatorFlag":0,"separatorHeight":0,"columnCount":1}]}', '2015120406', '0', '2', '2015-06-27 14:04:57', 'pm_admin');

INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (229, 999991, 'ServiceMarketLayout', '{"versionCode":"2015120406","versionName":"3.0.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Coupons","instanceConfig":{"itemGroup":"Coupons"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0}]}', '2015120406', '0', '2', '2016-07-12 17:02:30', 'park_tourist');
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (231, 999991, 'BizLayout', '{"versionCode":"2015120406","versionName":"3.0.0","displayName":"店铺列表","layoutName":"BizLayout","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"BizList"},"style":"Gallery","defaultOrder":1,"separatorFlag":0,"separatorHeight":0,"columnCount":1}]}', '2015120406', '0', '2', '2015-06-27 14:04:57', 'park_tourist');

INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (232, 999991, 'PmLayout', '{"versionCode":"2015120406","versionName":"3.0.0","displayName":"物业","layoutName":"PmLayout","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GaActions"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"Posts","instanceConfig":{"itemGroup":"GaPosts"},"style":"Default","defaultOrder":3,"separatorFlag":0,"separatorHeight":0},{"groupName":"CallPhone","widget":"CallPhones","instanceConfig":{"itemGroup":"CallPhones","position":"bottom"},"style":"Default","defaultOrder":3,"separatorFlag":0,"separatorHeight":0}]}', '2015120406', '0', '2', '2015-06-27 14:04:57', 'pm_admin');
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (233, 999991, 'PmLayout', '{"versionCode":"2015120406","versionName":"3.0.0","displayName":"物业","layoutName":"PmLayout","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GaActions"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"Posts","instanceConfig":{"itemGroup":"GaPosts"},"style":"Default","defaultOrder":3,"separatorFlag":0,"separatorHeight":0},{"groupName":"CallPhone","widget":"CallPhones","instanceConfig":{"itemGroup":"CallPhones","position":"bottom"},"style":"Default","defaultOrder":3,"separatorFlag":0,"separatorHeight":0}]}', '2015120406', '0', '2', '2015-06-27 14:04:57', 'park_tourist');


INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES (10559, 999991, 0, '/home', 'Default', '0', '0', 'weixinLink', 'weixinLink', 'cs://1/image/aW1hZ2UvTVRvek1qQXpNbVZpTmpVMU5tSXhNekZqTWpOaE5USmpNVFprTXpWaFlqazFNQQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'park_tourist');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES (10560, 999991, 0, '/home', 'Default', '0', '0', 'weixinLink', 'weixinLink', 'cs://1/image/aW1hZ2UvTVRvek1qQXpNbVZpTmpVMU5tSXhNekZqTWpOaE5USmpNVFprTXpWaFlqazFNQQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'pm_admin');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES (10561, 999991, 0, '/home', 'Default', '0', '0', 'weixinLink', 'weixinLink', 'cs://1/image/aW1hZ2UvTVRvek5XSTVNell4TTJRME0yTXlaRFZsT1RZeE1HTTBOVGxrWWpJeFpHTmpNUQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'park_tourist');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES (10562, 999991, 0, '/home', 'Default', '0', '0', 'weixinLink', 'weixinLink', 'cs://1/image/aW1hZ2UvTVRvek5XSTVNell4TTJRME0yTXlaRFZsT1RZeE1HTTBOVGxrWWpJeFpHTmpNUQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'pm_admin');


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (109878, 999991, 0, 0, 0, '/home', 'Coupons', '优惠', '优惠', 'cs://1/image/aW1hZ2UvTVRvell6RXlNVEE0TjJNelpEVTFPREZsWTJKaVptVXdNRFZtWm1FNVlUWTRZZw', 3, 1, 14,'{"url":"https://biz.zuolin.com/zl-ec?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fallpromotions#sign_suffix"}', 1, 0, 1, 1, '', '0', NULL, NULL, NULL, '0', 'pm_admin');  
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (109879, 999991, 0, 0, 0, '/home', 'Coupons', '外卖点送', '外卖点送', 'cs://1/image/aW1hZ2UvTVRvell6RXlNVEE0TjJNelpEVTFPREZsWTJKaVptVXdNRFZtWm1FNVlUWTRZZw', 5, 1, 2,'{"itemLocation":"/home/Biz","layoutName":"BizLayout","title":"外卖点送"}', 2, 0, 1, 1, '', '0', NULL, NULL, NULL, '0', 'pm_admin');        

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (109893, 999991, 0, 0, 0, '/home', 'Coupons', '优惠', '优惠', 'cs://1/image/aW1hZ2UvTVRvell6RXlNVEE0TjJNelpEVTFPREZsWTJKaVptVXdNRFZtWm1FNVlUWTRZZw', 3, 1, 14,'{"url":"https://biz.zuolin.com/zl-ec?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fallpromotions#sign_suffix"}', 1, 0, 1, 1, '', '0', NULL, NULL, NULL, '0', 'park_tourist');  
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (109894, 999991, 0, 0, 0, '/home', 'Coupons', '外卖点送', '外卖点送', 'cs://1/image/aW1hZ2UvTVRvell6RXlNVEE0TjJNelpEVTFPREZsWTJKaVptVXdNRFZtWm1FNVlUWTRZZw', 5, 1, 2,'{"itemLocation":"/home/Biz","layoutName":"BizLayout","title":"外卖点送"}', 2, 0, 1, 1, '', '0', NULL, NULL, NULL, '0', 'park_tourist');        


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (109880, 999991, 0, 0, 0, '/home', 'Bizs', 'PM', '物业报修', 'cs://1/image/aW1hZ2UvTVRwa01HVXpObVZoTnprNE1XRmtabU0yTVRVMk9EVmlOVGc1WlROaE5XRTNaUQ', 1, 1, 2,'{"itemLocation":"/home/Pm","layoutName":"PmLayout","title":"物业报修","entityTag":"PM"}', 0, 0, 1, 1, '', '0', NULL, NULL, NULL, '0', 'pm_admin');  
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (109881, 999991, 0, 0, 0, '/home', 'Bizs', '场地预约', '场地预约', 'cs://1/image/aW1hZ2UvTVRwa01EZGtZMlJtWmpWak0yUTNZekkxTVRSaFpEVTBORGRrTWpFNE5tWTVNQQ', 1, 1, 49,'{\"resourceTypeId\":2,\"pageType\":0}', 0, 0, 1, 1, '', '0', NULL, NULL, NULL, '0', 'pm_admin');        
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (109882, 999991, 0, 0, 0, '/home', 'Bizs', '物品租赁', '物品租赁', 'cs://1/image/aW1hZ2UvTVRvd09EQmlZMkZpWmpZNE4yVXlOREZtWVdNd09XVXhaalUzTUdOaVpERmhNdw', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14691826891539874080%3F_k%3Dzlbiz#sign_suffix"}', 0, 0, 1, 1, '', '0', NULL, NULL, NULL, '0', 'pm_admin');    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (109883, 999991, 0, 0, 0, '/home', 'Bizs', '迎宾接待', '迎宾接待', 'cs://1/image/aW1hZ2UvTVRvNFpUUXpOVEJsTXpZM05UbGpNemhrWkdZeFpqQmpZemczT0dOaU4yTmlNdw', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F1469183917679873112%3F_k%3Dzlbiz#sign_suffix"}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (109884, 999991, 0, 0, 0, '/home', 'Bizs', '今日食堂', '今日食堂', 'cs://1/image/aW1hZ2UvTVRwak5ETXlaakJsWkRJNFlqQmhabVJpTmpnd1lXSTFOelF4TjJGbU0yUXpOQQ', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14692418502942296174%3F_k%3Dzlbiz#sign_suffix"}', 0, 0, 1 , 1,'','0',NULL,NULL,NULL, '0', 'pm_admin');    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (109885, 999991, 0, 0, 0, '/home', 'Bizs', '爱车管家', '爱车管家', 'cs://1/image/aW1hZ2UvTVRvNE16VmxPRGxtTVRGaE1qZzNNVE00TjJZellUVTRPV014TXpsbE5URTBOdw', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14692455110592651331%3F_k%3Dzlbiz#sign_suffix"}', 0, 0, 1, 1, '', 0,NULL,NULL,NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (109886, 999991, 0, 0, 0, '/home', 'Bizs', '办公租赁', '办公租赁', 'cs://1/image/aW1hZ2UvTVRwak1EUTRPREZsWkRFM1lXWTFOVGRqWVRaaU5XVXdOREpqTWpGaFptVTBOZw', 1, 1, 14, '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', 0, 0, 1, 1, '', 0,NULL,NULL,NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (109887, 999991, 0, 0, 0, '/home', 'Bizs', 'MAKERZONE', '创客空间', 'cs://1/image/aW1hZ2UvTVRwaVpHRTRPR1F5WkdVME4yTmlZVFZsTVRJMU9EWTFPR1F5WVRFd01HTmhOZw', 1, 1, 32, '{"type":1,"forumId":180773,"categoryId":4,"parentId":110001,"tag":"创客"}', 0, 0, 1, 1, '', 0,NULL,NULL,NULL, '0', 'pm_admin');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (109888, 999991, 0, 0, 0, '/home', 'Bizs', '租车预约', '租车预约', 'cs://1/image/aW1hZ2UvTVRwbU5tWTVPVEk1WkRka01HRTNOamN4TlRZeFlqRTBNRGcyTVdRMVpUTmpOdw', 1, 1, 14, '{"url":"http://www.car2share.com.cn/"}', 0, 0, 1, 1, '', 0,NULL,NULL,NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (109892, 999991, 0, 0, 0, '/home', 'Bizs', '更多服务', '更多服务', 'cs://1/image/aW1hZ2UvTVRvMVpUaG1NRFl5TUdFM1lqQXpNRFV3WVdRME9XTXhaalJtTXpCaU5qaGlOQQ', 1, 1, 1, '{\"itemLocation\":\"/home\", \"itemGroup\":\"Bizs\"}', 30, 0, 1, 1, '', '0', NULL, NULL, NULL, '1', 'pm_admin');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (109895, 999991, 0, 0, 0, '/home', 'Bizs', 'PM', '物业报修', 'cs://1/image/aW1hZ2UvTVRwa01HVXpObVZoTnprNE1XRmtabU0yTVRVMk9EVmlOVGc1WlROaE5XRTNaUQ', 1, 1, 2,'{"itemLocation":"/home/Pm","layoutName":"PmLayout","title":"物业报修","entityTag":"PM"}', 0, 0, 1, 1, '', '0', NULL, NULL, NULL, '0', 'park_tourist');  
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (109896, 999991, 0, 0, 0, '/home', 'Bizs', '场地预约', '场地预约', 'cs://1/image/aW1hZ2UvTVRwa01EZGtZMlJtWmpWak0yUTNZekkxTVRSaFpEVTBORGRrTWpFNE5tWTVNQQ', 1, 1, 49,'{\"resourceTypeId\":2,\"pageType\":0}', 0, 0, 1, 1, '', '0', NULL, NULL, NULL, '0', 'park_tourist');        
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (109897, 999991, 0, 0, 0, '/home', 'Bizs', '物品租赁', '物品租赁', 'cs://1/image/aW1hZ2UvTVRvd09EQmlZMkZpWmpZNE4yVXlOREZtWVdNd09XVXhaalUzTUdOaVpERmhNdw', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14691826891539874080%3F_k%3Dzlbiz#sign_suffix"}', 0, 0, 1, 1, '', '0', NULL, NULL, NULL, '0', 'park_tourist');    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (109898, 999991, 0, 0, 0, '/home', 'Bizs', '迎宾接待', '迎宾接待', 'cs://1/image/aW1hZ2UvTVRvNFpUUXpOVEJsTXpZM05UbGpNemhrWkdZeFpqQmpZemczT0dOaU4yTmlNdw', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F1469183917679873112%3F_k%3Dzlbiz#sign_suffix"}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (109899, 999991, 0, 0, 0, '/home', 'Bizs', '今日食堂', '今日食堂', 'cs://1/image/aW1hZ2UvTVRwak5ETXlaakJsWkRJNFlqQmhabVJpTmpnd1lXSTFOelF4TjJGbU0yUXpOQQ', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14692418502942296174%3F_k%3Dzlbiz#sign_suffix"}', 0,0 , 1, 1,'','0',NULL,NULL,NULL, '0', 'park_tourist');    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (109900, 999991, 0, 0, 0, '/home', 'Bizs', '爱车管家', '爱车管家', 'cs://1/image/aW1hZ2UvTVRvNE16VmxPRGxtTVRGaE1qZzNNVE00TjJZellUVTRPV014TXpsbE5URTBOdw', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14692455110592651331%3F_k%3Dzlbiz#sign_suffix"}', 0, 0, 1, 1, '', 0,NULL,NULL,NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (109901, 999991, 0, 0, 0, '/home', 'Bizs', '办公租赁', '办公租赁', 'cs://1/image/aW1hZ2UvTVRwak1EUTRPREZsWkRFM1lXWTFOVGRqWVRaaU5XVXdOREpqTWpGaFptVTBOZw', 1, 1, 14, '{"url":"http://wx.dudubashi.com"}', 0, 0, 1, 1, '', 0,NULL,NULL,NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (109902, 999991, 0, 0, 0, '/home', 'Bizs', 'MAKERZONE', '创客空间', 'cs://1/image/aW1hZ2UvTVRwaVpHRTRPR1F5WkdVME4yTmlZVFZsTVRJMU9EWTFPR1F5WVRFd01HTmhOZw', 1, 1, 32, '{"type":1,"forumId":180773,"categoryId":4,"parentId":110001,"tag":"创客"}', 0, 0, 1, 1, '', 0,NULL,NULL,NULL, '0', 'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (109903, 999991, 0, 0, 0, '/home', 'Bizs', '租车预约', '租车预约', 'cs://1/image/aW1hZ2UvTVRwbU5tWTVPVEk1WkRka01HRTNOamN4TlRZeFlqRTBNRGcyTVdRMVpUTmpOdw', 1, 1, 14, '{"url":"http://www.car2share.com.cn/"}', 0, 0, 1, 1, '', 0,NULL,NULL,NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (109904, 999991, 0, 0, 0, '/home', 'Bizs', '更多服务', '更多服务', 'cs://1/image/aW1hZ2UvTVRvMVpUaG1NRFl5TUdFM1lqQXpNRFV3WVdRME9XTXhaalJtTXpCaU5qaGlOQQ', 1, 1, 1, '{\"itemLocation\":\"/home\", \"itemGroup\":\"Bizs\"}', 30, 0, 1, 1, '', '0', NULL, NULL, NULL, '1', 'park_tourist');


 INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (109889, 999991, 0, 0, 0, '/home/Biz', 'BizList', '说粉', '说粉', 'cs://1/image/aW1hZ2UvTVRvellqRTBaR1kyWTJNd05HWTJORGMxT0dSak56QXlabVJrT1dSa01XVXdaZw', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14692459898518874242%3F_k%3Dzlbiz#sign_suffix"}', 0, 0, 1, 1, '', 0,NULL,NULL,NULL, '0', 'pm_admin');
 INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (109890, 999991, 0, 0, 0, '/home/Biz', 'BizList', '够咖啡', '够咖啡', 'cs://1/image/aW1hZ2UvTVRveU56UmhZV05rWVRneVlqSTJNalF5TkdZek9XTXpaRGs0Tm1ZeU5qaGlaQQ', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14692461598558384133%3F_k%3Dzlbiz#sign_suffix"}', 0, 0, 1, 1, '', 0,NULL,NULL,NULL, '0', 'pm_admin');
 INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (109891, 999991, 0, 0, 0, '/home/Biz', 'BizList', 'Urban Table', 'Urban Table', 'cs://1/image/aW1hZ2UvTVRwa056RTFZelV4WXpjMU1URmtaamhsTVdRMlpUQTRaV0U0T1RneVpHTTFPQQ', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14692462917469898322%3F_k%3Dzlbiz#sign_suffix"}', 0, 0, 1, 1, '', 0,NULL,NULL,NULL, '0', 'pm_admin');

 INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (109905, 999991, 0, 0, 0, '/home/Biz', 'BizList', '说粉', '说粉', 'cs://1/image/aW1hZ2UvTVRvellqRTBaR1kyWTJNd05HWTJORGMxT0dSak56QXlabVJrT1dSa01XVXdaZw', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14692459898518874242%3F_k%3Dzlbiz#sign_suffix"}', 0, 0, 1, 1, '', 0,NULL,NULL,NULL, '0', 'park_tourist');
 INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (109906, 999991, 0, 0, 0, '/home/Biz', 'BizList', '够咖啡', '够咖啡', 'cs://1/image/aW1hZ2UvTVRveU56UmhZV05rWVRneVlqSTJNalF5TkdZek9XTXpaRGs0Tm1ZeU5qaGlaQQ', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14692461598558384133%3F_k%3Dzlbiz#sign_suffix"}', 0, 0, 1, 1, '', 0,NULL,NULL,NULL, '0', 'park_tourist');
 INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (109907, 999991, 0, 0, 0, '/home/Biz', 'BizList', 'Urban Table', 'Urban Table', 'cs://1/image/aW1hZ2UvTVRwa056RTFZelV4WXpjMU1URmtaamhsTVdRMlpUQTRaV0U0T1RneVpHTTFPQQ', 1, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14692462917469898322%3F_k%3Dzlbiz#sign_suffix"}', 0, 0, 1, 1, '', 0,NULL,NULL,NULL, '0', 'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
	 VALUES (109908, '999991', '0', '0', '0', '/home/Pm', 'GaActions', 'REPAIR', '报修', 'cs://1/image/aW1hZ2UvTVRvME5ESTJaVFZsTlRVd1ptSXhNVEUyTW1FMk0yUm1ZamcyWVdZNU5qUmxOdw', '1', '1', '19', '{\"contentCategory\":1004,\"actionCategory\":0,\"forumId\":176520,\"targetEntityTag\":\"PM\",\"embedAppId\":27,\"visibleRegionType\":0}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
	 VALUES (109909, '999991', '0', '0', '0', '/home/Pm', 'GaPosts', 'REPAIR', '报修', NULL, '1', '1', '15', '{\"contentCategory\":1004,\"actionCategory\":0,\"forumId\":176520,\"embedAppId\":27}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin', '0');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
 	VALUES (109910, '999991', '0', '0', '0', '/home/Pm', 'GaActions', 'REPAIR', '报修', 'cs://1/image/aW1hZ2UvTVRvME5ESTJaVFZsTlRVd1ptSXhNVEUyTW1FMk0yUm1ZamcyWVdZNU5qUmxOdw', '1', '1', '19', '{\"contentCategory\":1004,\"actionCategory\":0,\"forumId\":176520,\"targetEntityTag\":\"PM\",\"embedAppId\":27,\"visibleRegionType\":0}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
	VALUES (109911, '999991', '0', '0', '0', '/home/Pm', 'GaPosts', 'REPAIR', '报修', NULL, '1', '1', '15', '{\"contentCategory\":1004,\"actionCategory\":0,\"forumId\":176520,\"embedAppId\":27}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist', '0');


INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES('2','测试资源类型','0',NULL,'0','999991');

INSERT INTO `eh_yellow_pages` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `nick_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`) 
	VALUES(200219, 0,'community','240111044331053517','创智联盟','威新软件园创智联盟','1','深圳市南山区高新南九道','075526716888','“创智联盟”是原点空间创立的品牌联盟，为进驻的个人及团队提供包括公司注册、招聘、财务、法律、咨询等全方位服务，服务合作方涵括商务类、成长类、社交类以及生活类的数十个不同领域的领头品牌。','cs://1/image/aW1hZ2UvTVRvek1qQXpNbVZpTmpVMU5tSXhNekZqTWpOaE5USmpNVFprTXpWaFlqazFNQQ','2',NULL,'113.955397','22.536039','',NULL,NULL,NULL,NULL,NULL,'苏娇娇','13760240661',NULL,NULL,NULL,NULL,NULL);
INSERT INTO `eh_yellow_page_attachments` (`id`, `owner_id`, `content_type`, `content_uri`, `creator_uid`, `create_time`) 
	VALUES(131, 200219,'image','cs://1/image/aW1hZ2UvTVRvek1qQXpNbVZpTmpVMU5tSXhNekZqTWpOaE5USmpNVFprTXpWaFlqazFNQQ','0',UTC_TIMESTAMP());


INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
	VALUES(179056, 240111044331053517, '园区一期', '威新一期', manager_uid, '15818603192', '深圳市南山区高新南九道', 1790, NULL, NULL, NULL, '高新区里程碑式的研发办公建筑，企业总部基地。运用科技和设计，打造甲级品质的节能、低耗、绿色生态商务空间，塑造立体的艺术、活力、科技体验生活方式中心，为高新园区产业升级提供了宝贵的空间载体。', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999991);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
	VALUES(179057, 240111044331053517, '园区二期', '威新二期', manager_uid, '15818603192', '深圳市南山区高新南九道', 1790, NULL, NULL, NULL, '高新区里程碑式的研发办公建筑，企业总部基地。运用科技和设计，打造甲级品质的节能、低耗、绿色生态商务空间，塑造立体的艺术、活力、科技体验生活方式中心，为高新园区产业升级提供了宝贵的空间载体。', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999991);
    
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101548,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-1号楼1层北翼东侧','园区一期','1号楼1层北翼东侧','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101549,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-1号楼1层东翼','园区一期','1号楼1层东翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101550,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-1号楼1层南翼','园区一期','1号楼1层南翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101551,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-1号楼1层南翼东侧','园区一期','1号楼1层南翼东侧','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101552,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-1号楼1层西翼01C-2','园区一期','1号楼1层西翼01C-2','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101553,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-1号楼2层北翼','园区一期','1号楼2层北翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101554,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-1号楼2层北翼并东翼03A-1','园区一期','1号楼2层北翼并东翼03A-1','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101555,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-1号楼2层东南翼','园区一期','1号楼2层东南翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101556,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-1号楼2层西翼','园区一期','1号楼2层西翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101557,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-1号楼3层东翼03A-2','园区一期','1号楼3层东翼03A-2','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101558,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-1号楼3层南翼','园区一期','1号楼3层南翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101559,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-1号楼3层西翼','园区一期','1号楼3层西翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101560,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-1号楼4层北翼','园区一期','1号楼4层北翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101561,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-1号楼4层东翼','园区一期','1号楼4层东翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101562,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-1号楼4层南翼','园区一期','1号楼4层南翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101563,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-1号楼4层西翼','园区一期','1号楼4层西翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101564,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-1号楼4层西翼01C-1','园区一期','1号楼4层西翼01C-1','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101565,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-1号楼5层东翼','园区一期','1号楼5层东翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101566,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-1号楼5层南翼','园区一期','1号楼5层南翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101567,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-1号楼5层西翼','园区一期','1号楼5层西翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101568,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼1层北翼01D-1','园区一期','2号楼1层北翼01D-1','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101569,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼1层北翼01D-2','园区一期','2号楼1层北翼01D-2','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101570,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼1层东翼','园区一期','2号楼1层东翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101571,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼1层东翼西侧','园区一期','2号楼1层东翼西侧','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101572,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼1层南翼','园区一期','2号楼1层南翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101573,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼1层南翼西侧','园区一期','2号楼1层南翼西侧','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101574,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼1层西翼','园区一期','2号楼1层西翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101575,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼2层北翼','园区一期','2号楼2层北翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101576,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼2层东翼','园区一期','2号楼2层东翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101577,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼2层南翼并西翼','园区一期','2号楼2层南翼并西翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101578,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼3层北翼','园区一期','2号楼3层北翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101579,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼3层东翼','园区一期','2号楼3层东翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101580,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼3层南翼','园区一期','2号楼3层南翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101581,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼3层西翼','园区一期','2号楼3层西翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101582,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼4层北翼04D-1','园区一期','2号楼4层北翼04D-1','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101583,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼4层北翼04D-2','园区一期','2号楼4层北翼04D-2','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101584,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼4层东翼并南翼','园区一期','2号楼4层东翼并南翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101585,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼4层西翼','园区一期','2号楼4层西翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101586,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼5层北翼05D-1','园区一期','2号楼5层北翼05D-1','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101587,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼5层北翼501-502','园区一期','2号楼5层北翼501-502','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101588,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼5层北翼502','园区一期','2号楼5层北翼502','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101589,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼5层北翼503并504','园区一期','2号楼5层北翼503并504','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101590,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼5层东翼501-502','园区一期','2号楼5层东翼501-502','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101591,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼5层东翼503','园区一期','2号楼5层东翼503','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101592,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼5层东翼503A','园区一期','2号楼5层东翼503A','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101593,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼5层南翼','园区一期','2号楼5层南翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101594,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-2号楼5层西翼','园区一期','2号楼5层西翼','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101595,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-3号楼1层101','园区一期','3号楼1层101','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101596,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-3号楼1层102A','园区一期','3号楼1层102A','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101597,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-3号楼1层102B','园区一期','3号楼1层102B','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101598,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-3号楼1层102C','园区一期','3号楼1层102C','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101599,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-3号楼1层103','园区一期','3号楼1层103','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101600,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-3号楼2层','园区一期','3号楼2层','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101601,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-3号楼3层','园区一期','3号楼3层','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101602,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-3号楼4层','园区一期','3号楼4层','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101603,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区一期-3号楼5层','园区一期','3号楼5层','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101604,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-5层北翼05D','园区二期','5层北翼05D','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101605,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-5号楼1层','园区二期','5号楼1层','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101606,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-5号楼1层101','园区二期','5号楼1层101','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101607,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-5号楼1层102','园区二期','5号楼1层102','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101608,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-5号楼2层','园区二期','5号楼2层','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101609,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-5号楼3层','园区二期','5号楼3层','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101610,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-5号楼4层04-11','园区二期','5号楼4层04-11','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101611,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-5号楼4层401-403','园区二期','5号楼4层401-403','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101612,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-5号楼4层412','园区二期','5号楼4层412','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101613,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-5号楼501-511','园区二期','5号楼501-511','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101614,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-5号楼5层512室','园区二期','5号楼5层512室','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101615,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-5号楼6层','园区二期','5号楼6层','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101616,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-6号楼1层','园区二期','6号楼1层','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101617,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-6号楼2层','园区二期','6号楼2层','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101618,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-6号楼3层','园区二期','6号楼3层','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101619,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-6号楼4层','园区二期','6号楼4层','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101620,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-6号楼5层','园区二期','6号楼5层','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101621,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-6号楼6层','园区二期','6号楼6层','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101622,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-6号楼7层','园区二期','6号楼7层','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101623,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-7号楼1层101A','园区二期','7号楼1层101A','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101624,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-7号楼1层101B','园区二期','7号楼1层101B','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101625,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-7号楼1层102','园区二期','7号楼1层102','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101626,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-7号楼1层103','园区二期','7号楼1层103','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101627,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-7号楼1层104','园区二期','7号楼1层104','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101628,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-7号楼2层','园区二期','7号楼2层','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101629,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-7号楼3层301-308','园区二期','7号楼3层301-308','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101630,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-7号楼3层309-312','园区二期','7号楼3层309-312','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101631,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-7号楼5层','园区二期','7号楼5层','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101632,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-7号楼6层','园区二期','7号楼6层','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101633,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-7号楼7层','园区二期','7号楼7层','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101634,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-7号楼8层','园区二期','7号楼8层','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101635,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-7号楼9层','园区二期','7号楼9层','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101636,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-8号楼103A','园区二期','8号楼103A','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101637,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-8号楼1层101','园区二期','8号楼1层101','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101638,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-8号楼1层1025','园区二期','8号楼1层1025','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101639,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-8号楼1层102A','园区二期','8号楼1层102A','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101640,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-8号楼1层103','园区二期','8号楼1层103','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101641,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-8号楼2层201-212','园区二期','8号楼2层201-212','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101642,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-8号楼3层','园区二期','8号楼3层','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101643,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-8号楼4层401-402','园区二期','8号楼4层401-402','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101644,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-8号楼4层403-406','园区二期','8号楼4层403-406','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101645,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-8号楼4层407-408','园区二期','8号楼4层407-408','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101646,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-8号楼4层409-411','园区二期','8号楼4层409-411','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101647,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-8号楼4层412','园区二期','8号楼4层412','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101648,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-8号楼5层501-502','园区二期','8号楼5层501-502','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101649,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-8号楼5层503-506','园区二期','8号楼5层503-506','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101650,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-8号楼5层512','园区二期','8号楼5层512','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101651,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-8号楼6层601-612','园区二期','8号楼6层601-612','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101652,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-8号楼7层','园区二期','8号楼7层','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101653,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-8号楼8层','园区二期','8号楼8层','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101654,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-8号楼9层02-06','园区二期','8号楼9层02-06','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101655,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-8号楼9层07-10','园区二期','8号楼9层07-10','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101656,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-8号楼9层901','园区二期','8号楼9层901','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101657,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-8号楼9层901','园区二期','8号楼9层901','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101658,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-8号楼9层911-912','园区二期','8号楼9层911-912','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101659,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-9号楼1至2层','园区二期','9号楼1至2层','2','0',UTC_TIMESTAMP(), 999991);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101660,UUID(),240111044331053517, 14978, '深圳市',  14979, '宝安区' ,'园区二期-9号楼3层','园区二期','9号楼3层','2','0',UTC_TIMESTAMP(), 999991);




INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19341, 1002757, 240111044331053517, 239825274387101548, '园区一期-1号楼1层北翼东侧', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19342, 1002757, 240111044331053517, 239825274387101549, '园区一期-1号楼1层东翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19343, 1002757, 240111044331053517, 239825274387101550, '园区一期-1号楼1层南翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19344, 1002757, 240111044331053517, 239825274387101551, '园区一期-1号楼1层南翼东侧', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19345, 1002757, 240111044331053517, 239825274387101552, '园区一期-1号楼1层西翼01C-2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19346, 1002757, 240111044331053517, 239825274387101553, '园区一期-1号楼2层北翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19347, 1002757, 240111044331053517, 239825274387101554, '园区一期-1号楼2层北翼并东翼03A-1', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19348, 1002757, 240111044331053517, 239825274387101555, '园区一期-1号楼2层东南翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19349, 1002757, 240111044331053517, 239825274387101556, '园区一期-1号楼2层西翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19350, 1002757, 240111044331053517, 239825274387101557, '园区一期-1号楼3层东翼03A-2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19351, 1002757, 240111044331053517, 239825274387101558, '园区一期-1号楼3层南翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19352, 1002757, 240111044331053517, 239825274387101559, '园区一期-1号楼3层西翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19353, 1002757, 240111044331053517, 239825274387101560, '园区一期-1号楼4层北翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19354, 1002757, 240111044331053517, 239825274387101561, '园区一期-1号楼4层东翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19355, 1002757, 240111044331053517, 239825274387101562, '园区一期-1号楼4层南翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19356, 1002757, 240111044331053517, 239825274387101563, '园区一期-1号楼4层西翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19357, 1002757, 240111044331053517, 239825274387101564, '园区一期-1号楼4层西翼01C-1', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19358, 1002757, 240111044331053517, 239825274387101565, '园区一期-1号楼5层东翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19359, 1002757, 240111044331053517, 239825274387101566, '园区一期-1号楼5层南翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19360, 1002757, 240111044331053517, 239825274387101567, '园区一期-1号楼5层西翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19361, 1002757, 240111044331053517, 239825274387101568, '园区一期-2号楼1层北翼01D-1', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19362, 1002757, 240111044331053517, 239825274387101569, '园区一期-2号楼1层北翼01D-2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19363, 1002757, 240111044331053517, 239825274387101570, '园区一期-2号楼1层东翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19364, 1002757, 240111044331053517, 239825274387101571, '园区一期-2号楼1层东翼西侧', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19365, 1002757, 240111044331053517, 239825274387101572, '园区一期-2号楼1层南翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19366, 1002757, 240111044331053517, 239825274387101573, '园区一期-2号楼1层南翼西侧', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19367, 1002757, 240111044331053517, 239825274387101574, '园区一期-2号楼1层西翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19368, 1002757, 240111044331053517, 239825274387101575, '园区一期-2号楼2层北翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19369, 1002757, 240111044331053517, 239825274387101576, '园区一期-2号楼2层东翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19370, 1002757, 240111044331053517, 239825274387101577, '园区一期-2号楼2层南翼并西翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19371, 1002757, 240111044331053517, 239825274387101578, '园区一期-2号楼3层北翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19372, 1002757, 240111044331053517, 239825274387101579, '园区一期-2号楼3层东翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19373, 1002757, 240111044331053517, 239825274387101580, '园区一期-2号楼3层南翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19374, 1002757, 240111044331053517, 239825274387101581, '园区一期-2号楼3层西翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19375, 1002757, 240111044331053517, 239825274387101582, '园区一期-2号楼4层北翼04D-1', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19376, 1002757, 240111044331053517, 239825274387101583, '园区一期-2号楼4层北翼04D-2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19377, 1002757, 240111044331053517, 239825274387101584, '园区一期-2号楼4层东翼并南翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19378, 1002757, 240111044331053517, 239825274387101585, '园区一期-2号楼4层西翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19379, 1002757, 240111044331053517, 239825274387101586, '园区一期-2号楼5层北翼05D-1', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19380, 1002757, 240111044331053517, 239825274387101587, '园区一期-2号楼5层北翼501-502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19381, 1002757, 240111044331053517, 239825274387101588, '园区一期-2号楼5层北翼502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19382, 1002757, 240111044331053517, 239825274387101589, '园区一期-2号楼5层北翼503并504', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19383, 1002757, 240111044331053517, 239825274387101590, '园区一期-2号楼5层东翼501-502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19384, 1002757, 240111044331053517, 239825274387101591, '园区一期-2号楼5层东翼503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19385, 1002757, 240111044331053517, 239825274387101592, '园区一期-2号楼5层东翼503A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19386, 1002757, 240111044331053517, 239825274387101593, '园区一期-2号楼5层南翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19387, 1002757, 240111044331053517, 239825274387101594, '园区一期-2号楼5层西翼', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19388, 1002757, 240111044331053517, 239825274387101595, '园区一期-3号楼1层101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19389, 1002757, 240111044331053517, 239825274387101596, '园区一期-3号楼1层102A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19390, 1002757, 240111044331053517, 239825274387101597, '园区一期-3号楼1层102B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19391, 1002757, 240111044331053517, 239825274387101598, '园区一期-3号楼1层102C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19392, 1002757, 240111044331053517, 239825274387101599, '园区一期-3号楼1层103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19393, 1002757, 240111044331053517, 239825274387101600, '园区一期-3号楼2层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19394, 1002757, 240111044331053517, 239825274387101601, '园区一期-3号楼3层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19395, 1002757, 240111044331053517, 239825274387101602, '园区一期-3号楼4层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19396, 1002757, 240111044331053517, 239825274387101603, '园区一期-3号楼5层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19397, 1002757, 240111044331053517, 239825274387101604, '园区二期-5层北翼05D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19398, 1002757, 240111044331053517, 239825274387101605, '园区二期-5号楼1层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19399, 1002757, 240111044331053517, 239825274387101606, '园区二期-5号楼1层101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19400, 1002757, 240111044331053517, 239825274387101607, '园区二期-5号楼1层102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19401, 1002757, 240111044331053517, 239825274387101608, '园区二期-5号楼2层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19402, 1002757, 240111044331053517, 239825274387101609, '园区二期-5号楼3层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19403, 1002757, 240111044331053517, 239825274387101610, '园区二期-5号楼4层04-11', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19404, 1002757, 240111044331053517, 239825274387101611, '园区二期-5号楼4层401-403', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19405, 1002757, 240111044331053517, 239825274387101612, '园区二期-5号楼4层412', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19406, 1002757, 240111044331053517, 239825274387101613, '园区二期-5号楼501-511', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19407, 1002757, 240111044331053517, 239825274387101614, '园区二期-5号楼5层512室', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19408, 1002757, 240111044331053517, 239825274387101615, '园区二期-5号楼6层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19409, 1002757, 240111044331053517, 239825274387101616, '园区二期-6号楼1层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19410, 1002757, 240111044331053517, 239825274387101617, '园区二期-6号楼2层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19411, 1002757, 240111044331053517, 239825274387101618, '园区二期-6号楼3层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19412, 1002757, 240111044331053517, 239825274387101619, '园区二期-6号楼4层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19413, 1002757, 240111044331053517, 239825274387101620, '园区二期-6号楼5层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19414, 1002757, 240111044331053517, 239825274387101621, '园区二期-6号楼6层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19415, 1002757, 240111044331053517, 239825274387101622, '园区二期-6号楼7层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19416, 1002757, 240111044331053517, 239825274387101623, '园区二期-7号楼1层101A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19417, 1002757, 240111044331053517, 239825274387101624, '园区二期-7号楼1层101B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19418, 1002757, 240111044331053517, 239825274387101625, '园区二期-7号楼1层102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19419, 1002757, 240111044331053517, 239825274387101626, '园区二期-7号楼1层103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19420, 1002757, 240111044331053517, 239825274387101627, '园区二期-7号楼1层104', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19421, 1002757, 240111044331053517, 239825274387101628, '园区二期-7号楼2层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19422, 1002757, 240111044331053517, 239825274387101629, '园区二期-7号楼3层301-308', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19423, 1002757, 240111044331053517, 239825274387101630, '园区二期-7号楼3层309-312', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19424, 1002757, 240111044331053517, 239825274387101631, '园区二期-7号楼5层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19425, 1002757, 240111044331053517, 239825274387101632, '园区二期-7号楼6层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19426, 1002757, 240111044331053517, 239825274387101633, '园区二期-7号楼7层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19427, 1002757, 240111044331053517, 239825274387101634, '园区二期-7号楼8层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19428, 1002757, 240111044331053517, 239825274387101635, '园区二期-7号楼9层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19429, 1002757, 240111044331053517, 239825274387101636, '园区二期-8号楼103A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19430, 1002757, 240111044331053517, 239825274387101637, '园区二期-8号楼1层101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19431, 1002757, 240111044331053517, 239825274387101638, '园区二期-8号楼1层1025', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19432, 1002757, 240111044331053517, 239825274387101639, '园区二期-8号楼1层102A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19433, 1002757, 240111044331053517, 239825274387101640, '园区二期-8号楼1层103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19434, 1002757, 240111044331053517, 239825274387101641, '园区二期-8号楼2层201-212', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19435, 1002757, 240111044331053517, 239825274387101642, '园区二期-8号楼3层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19436, 1002757, 240111044331053517, 239825274387101643, '园区二期-8号楼4层401-402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19437, 1002757, 240111044331053517, 239825274387101644, '园区二期-8号楼4层403-406', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19438, 1002757, 240111044331053517, 239825274387101645, '园区二期-8号楼4层407-408', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19439, 1002757, 240111044331053517, 239825274387101646, '园区二期-8号楼4层409-411', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19440, 1002757, 240111044331053517, 239825274387101647, '园区二期-8号楼4层412', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19441, 1002757, 240111044331053517, 239825274387101648, '园区二期-8号楼5层501-502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19442, 1002757, 240111044331053517, 239825274387101649, '园区二期-8号楼5层503-506', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19443, 1002757, 240111044331053517, 239825274387101650, '园区二期-8号楼5层512', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19444, 1002757, 240111044331053517, 239825274387101651, '园区二期-8号楼6层601-612', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19445, 1002757, 240111044331053517, 239825274387101652, '园区二期-8号楼7层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19446, 1002757, 240111044331053517, 239825274387101653, '园区二期-8号楼8层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19447, 1002757, 240111044331053517, 239825274387101654, '园区二期-8号楼9层02-06', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19448, 1002757, 240111044331053517, 239825274387101655, '园区二期-8号楼9层07-10', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19449, 1002757, 240111044331053517, 239825274387101656, '园区二期-8号楼9层901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19450, 1002757, 240111044331053517, 239825274387101657, '园区二期-8号楼9层901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19451, 1002757, 240111044331053517, 239825274387101658, '园区二期-8号楼9层911-912', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19452, 1002757, 240111044331053517, 239825274387101659, '园区二期-9号楼1至2层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (19453, 1002757, 240111044331053517, 239825274387101660, '园区二期-9号楼3层', '0');
