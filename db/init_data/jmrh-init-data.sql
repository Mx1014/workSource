
SET FOREIGN_KEY_CHECKS = 0;
-- SET @core_server_url = 'core.zuolin.com'; -- 取具体环境连接core server的链接
-- SET @biz_url = 'biz.zuolin.com'; -- 取具体环境的电商服务器连接，注意有两个域名要修改
-- SET @namespace_id=999972;
-- SET @user_id = 319545;  -- 需要取现网eh_users的ID的最大值再加一定余量
-- SET @account_name='19129838142'; -- 需要取现网eh_users的acount_name的6位的最大值再加一定余量
-- SET @community_id = 240111044331050361; -- 需要取现网eh_communities的ID的最大值再加一定余量
-- SET @organization_id = 1034291; 	-- 需要取eh_organizations的ID最大值并加一定余量，如果修改此值则其path也要改
-- SET @community_geopoint_id = 240111044331062642;  
-- SET @org_group_id = 1031239; -- 需要取eh_groups的ID的最大值再加一定余量
-- SET @org_forum_id = 191570;   -- @community_forum_id+2
-- SET @feedback_forum_id = 191575;   -- @community_forum_id+1
-- SET @community_forum_id = 191580;   -- 取eh_forums的ID最大值再加一定余量
-- SET @building_id = 195046;   -- 取eh_buildings的ID最大值再加一定余量
-- SET @sheng_id = 14805;  -- 取eh_regions的ID最大值再加一定余量
-- SET @shi_id = 14806;  -- 在@sheng_id上加1
-- SET @qu_id = 14807;    -- 在@shi_id上加1
-- SET @address_id = 239825274387270350; -- 需要取现网eh_addresses的ID的最大值再加一定余量
-- SET @rentalv_type_id = 10712; -- 需要取现网eh_rentalv2_resource_types的ID的最大值再加一定余量
-- SET @layout_id = 594 595; 
-- SET @item_id = 115454; 
-- SET @banner_id = 204875;
-- SET @org_member_id = 2145060; 
-- SET @role_assignment_id = 16697; 
-- SET @user_identifier_id = 283636;
-- SET @organization_address_mapping_id = 35745;
-- SET @org_cmnty_request_id = 1131889;
-- SET @namespace_resource_id = 18013;

-- eh_configurations: 1399  邓爽你加100，我家200
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES (1498, 'app.agreements.url', CONCAT('https://', 'core.zuolin.com', '/mobile/static/app_agreements/agreements.html?ns=999972'), 'the relative path for junminronghe app agreements', 999972, NULL);	   
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES (1499, 'business.url', CONCAT('https://', 'biz.zuolin.com', '/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2F', 'biz.zuolin.com', '%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix'), 'biz access url for junminronghe', 999972, NULL);	   

-- 我用125,126 yan军 127,128
INSERT INTO `eh_version_realm` VALUES (125, 'Android_JunMinRongHe', null, UTC_TIMESTAMP(), 999972);
INSERT INTO `eh_version_realm` VALUES (126, 'iOS_JunMinRongHe', null, UTC_TIMESTAMP(), 999972);

-- eh_version_upgrade_rules ： 我加20 你加40
-- eh_version_upgrade_rules 现在是 397
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES(517,125,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES(518,126,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());
	
-- 军民融合域空间
INSERT INTO `eh_namespaces`(`id`, `name`) VALUES(999972, '军民融合');
INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES(1138, 999972, 'community_commercial', UTC_TIMESTAMP());

-- eh_regions: 14804-14900中间的id   你从14804开始加，我从14814开始加
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (14805, '0', '北京', 'BEIJING', 'BG', '/北京', '1', '1', '', '', '2', '2', 999972);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (14806, 14805, '北京市', 'BEIJINGSHI', 'BJS', '/北京/北京市', '2', '2', NULL, '0755', '2', '1', 999972);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (14807, 14806, '海淀区', 'HAIDIANQU', 'HDQ', '/北京/北京市/海淀区', '3', '3', NULL, '0755', '2', '0', 999972);

-- 中关村四季青军民融合产业园是物业管理公司
-- eh_groups: 1021239 我加 10000 严军你加 20000
-- eh_forums:  190570-190572 190574 190575   190597 190598 这些id都可以用
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1031239, UUID(), '中关村四季青军民融合产业园圈', '中关村四季青军民融合产业园圈', 1, 1, 1034291, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 191570, 1, 999972); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(191570, UUID(), 999972, 2, 'EhGroups', 1031239,'中关村四季青军民融合产业园论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());       

-- eh_organizations: 1024291 我 加 1万 你加2万
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(1034291, 0, 'PM', '中关村四季青军民融合产业园', '中关村四季青军民融合产业园项目是海淀区军民融合产业“一体三园”黄金布局中的重要组成部分，也是四季青镇军民融合产业集群的核心园区。园区占地面积50000平米，总建筑面积90000余平米，可招商面积67000平米。园区将向广大入驻企业提供绿色生态的办公环境、丰富完善的配套服务、智能化的工作生活体验，并通过引入13个军种联络处、武器装备信息采购平台等多方资源，为入驻企业提供便利的军地对接一站式服务，真正成为“军转民、民参与”这一国家战略的重要成果转化平台。', '/1034291', 1, 2, 'ENTERPRISE', 999972, 1031239);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1131889, 240111044331050361, 'organization', 1034291, 3, 0, UTC_TIMESTAMP()); 

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (319545, UUID(), 19129838142, '关子忠', '', 1, 45, '1', '2',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 999972);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (283636 , 319545 ,  '0',  '13910753575',  '221616',  3, UTC_TIMESTAMP(), 999972);
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`)
	VALUES(2145060, 1034291, 'USER', 319545  , 'manager', '关子忠', 0, '13910753575', 3, 999972);	
-- todo
SET @role_assignment_id = (SELECT MAX(id) FROM eh_acl_role_assignments);
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES((@role_assignment_id := @role_assignment_id + 1), 'EhOrganizations', 1034291, 'EhUsers', 319545  , 1001, 1, UTC_TIMESTAMP());

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `comment_tag1`, `comment_tag2`, `comment_tag3`, `comment_tag4`, `comment_tag5`) 
VALUES((@acl_id := @acl_id + 1),'EhOrganizations','1034291','1','10','319545','0','319545','2017-07-05 16:04:09','999972','EhUsers','admin',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);


INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(191580, UUID(), 999972, 2, 'EhGroups', 0,'军民融合论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(191575, UUID(), 999972, 2, 'EhGroups', 0,'军民融合意见反馈论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 


INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES(240111044331050361, UUID(), 14806, '北京市',  14807, '海淀区', '中关村军民融合产业园', '中关村军民融合产业园', '昆明湖南路', NULL, '中关村四季青军民融合产业园项目是海淀区军民融合产业“一体三园”黄金布局中的重要组成部分，也是四季青镇军民融合产业集群的核心园区。园区占地面积50000平米，总建筑面积90000余平米，可招商面积67000平米。园区将向广大入驻企业提供绿色生态的办公环境、丰富完善的配套服务、智能化的工作生活体验，并通过引入13个军种联络处、武器装备信息采购平台等多方资源，为入驻企业提供便利的军地对接一站式服务，真正成为“军转民、民参与”这一国家战略的重要成果转化平台。',NULL, NULL, NULL, NULL, NULL, NULL,NULL, 682, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,1, 191580, 191575, UTC_TIMESTAMP(), 999972);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES(240111044331062642, 240111044331050361, '', 116.28473, 39.956716, 'wx4eq1guwxep');
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(1034291, 240111044331050361);
INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES(18013, 1, 'COMMUNITY', 240111044331050361, UTC_TIMESTAMP());	
-- todo
 INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, `default_order`) 
	VALUES (195046, 240111044331050361, 'A座', 'A座', '2100163', '13910753575', '北京市海淀区昆明湖南路51号', '9712', '116.28473', '39.956716', 'wx4eq1guwxep', '', 'cs://1/image/aW1hZ2UvTVRvNE1HUmlaRGszT1RkaU1EVmxaV1k0T0RJMk5UWmpOakEzWmpNMVltRXlaZw', '2', '1', '2017-04-24 23:01:31', '195506', '2015-12-09 14:57:13', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999972,1);
 INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, `default_order`) 
	VALUES (195047, 240111044331050361, 'B座', 'B座', '2100163', '13910753575', '北京市海淀区昆明湖南路51号', '13057', '116.28473', '39.956716', 'wx4eq1guwxep', '', 'cs://1/image/aW1hZ2UvTVRvNE1HUmlaRGszT1RkaU1EVmxaV1k0T0RJMk5UWmpOakEzWmpNMVltRXlaZw', '2', '1', '2017-04-24 23:01:31', '195506', '2015-12-09 14:57:13', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999972,2);
 INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, `default_order`) 
	VALUES (195048, 240111044331050361, 'C座', 'C座', '2100163', '13910753575', '北京市海淀区昆明湖南路51号', '13511', '116.28473', '39.956716', 'wx4eq1guwxep', '', 'cs://1/image/aW1hZ2UvTVRvNE1HUmlaRGszT1RkaU1EVmxaV1k0T0RJMk5UWmpOakEzWmpNMVltRXlaZw', '2', '1', '2017-04-24 23:01:31', '195506', '2015-12-09 14:57:13', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999972,3);
 INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, `default_order`) 
	VALUES (195049, 240111044331050361, 'D座', 'D座', '2100163', '13910753575', '北京市海淀区昆明湖南路51号', '13456', '116.28473', '39.956716', 'wx4eq1guwxep', '', 'cs://1/image/aW1hZ2UvTVRvNE1HUmlaRGszT1RkaU1EVmxaV1k0T0RJMk5UWmpOakEzWmpNMVltRXlaZw', '2', '1', '2017-04-24 23:01:31', '195506', '2015-12-09 14:57:13', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999972,4);
 INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, `default_order`) 
	VALUES (195050, 240111044331050361, '下沉式广场', '下沉式广场', '2100163', '13910753575', '北京市海淀区昆明湖南路51号', '12889', '116.28473', '39.956716', 'wx4eq1guwxep', '', 'cs://1/image/aW1hZ2UvTVRvNE1HUmlaRGszT1RkaU1EVmxaV1k0T0RJMk5UWmpOakEzWmpNMVltRXlaZw', '2', '1', '2017-04-24 23:01:31', '195506', '2015-12-09 14:57:13', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999972,5);

-- todo 改id
SET @address_id = (SELECT MAX(id) FROM `eh_addresses`); 
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES((@address_id := @address_id + 1),UUID(),240111044331050361, 14806, '北京市',  14807, '海淀区' ,'A座101','A座','101','2','0',UTC_TIMESTAMP(), 999972);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (35745, 1034291, 240111044331050361, @address_id, 'A座101', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES((@address_id := @address_id + 1),UUID(),240111044331050361, 14806, '北京市',  14807, '海淀区' ,'B座101','B座','101','2','0',UTC_TIMESTAMP(), 999972);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (35746, 1034291, 240111044331050361, @address_id, 'B座101', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES((@address_id := @address_id + 1),UUID(),240111044331050361, 14806, '北京市',  14807, '海淀区' ,'C座101','D座','101','2','0',UTC_TIMESTAMP(), 999972);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (35747, 1034291, 240111044331050361, @address_id, 'C座101', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES((@address_id := @address_id + 1),UUID(),240111044331050361, 14806, '北京市',  14807, '海淀区' ,'D座101','D座','101','2','0',UTC_TIMESTAMP(), 999972);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (35748, 1034291, 240111044331050361, @address_id, 'D座101', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES((@address_id := @address_id + 1),UUID(),240111044331050361, 14806, '北京市',  14807, '海淀区' ,'下沉式广场','下沉式广场','101','2','0',UTC_TIMESTAMP(), 999972);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (35749, 1034291, 240111044331050361, @address_id, '下沉式广场101', '0');

-- 园区管理员  
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES (204875, 999972, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRvNE5qQXpabVkzTkRFNFpXUXhZV1JtWVRReE1tVXlaVFkzT1dFNFpqVTJPUQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'pm_admin');

INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`) 
    VALUES (594, 999972, 'ServiceMarketLayout', '{"versionCode": "2017070401","versionName": "4.7.0","layoutName": "ServiceMarketLayout","displayName": "服务市场","groups": [{"groupName": "","widget": "Banners","instanceConfig": {"itemGroup": "Default"},"style": "Default","defaultOrder": 1,"separatorFlag": 0,"separatorHeight": 0},{"groupName": "","widget": "Navigator","instanceConfig": {"itemGroup": "GovAgencies"},"style": "Default","defaultOrder": 2,"separatorFlag": 1,"separatorHeight": 16,"columnCount": 4},{"groupName": "","widget": "Bulletins","instanceConfig": {"itemGroup": "Default"},"style": "Default","defaultOrder": 1,"separatorFlag": 1,"separatorHeight": 16},{"groupName": "商家服务","widget": "Navigator","instanceConfig": {"itemGroup": "Bizs"},"style": "Default","defaultOrder": 5,"separatorFlag": 0,"separatorHeight": 0}]}', '2017070401', '2017070401', '2', '2017-07-04 19:09:30', 'pm_admin');
-- 服务广场
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (115454, 999972, 0, 0, 0, '/home', 'GovAgencies', '交流大厅', '交流大厅', 'cs://1/image/aW1hZ2UvTVRvNFpqZ3dNRGM1WXpNelpqWmpaVGsyWkdFek1UVTBaalV3TWpneFpURTNOUQ', 1, 1, 50, '', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
	 VALUES(115457, 999972, 0, 0, 0, '/home', 'GovAgencies', '创客空间', '创客空间', 'cs://1/image/aW1hZ2UvTVRvMFpqTXlPREppWkdJMFpqSXdZemhsT1dFM1pUZzNNVEpoTXpobE1ERXhOQQ', 1, 1, 32, '{"type":1,"forumId":177000,"categoryId":1010,"parentId":110001,"tag":"创客"}', 2, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
	 VALUES(115455, 999972, 0, 0, 0, '/home', 'GovAgencies', '园区入驻', '园区入驻', 'cs://1/image/aW1hZ2UvTVRvME5EY3pZMlEyTVRobE1HRTNPVEpoTldNM1ptSTBaV1JtTVdaaE1EWmhZZw', 1, 1, 28, '', 4, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
	 VALUES(115456, 999972, 0, 0, 0, '/home', 'GovAgencies', '服务广场', '服务广场', 'cs://1/image/aW1hZ2UvTVRveVlqQTBNVFEzTkdJNE5tSmhOakE1WlRNMllqWXlPV1UyTnprM05XRXhZZw', 1, 1, 33, '{"type":11,"parentId":11,"displayType": "grid"}', 6, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115458, 999972, 0, 0, 0, '/home', 'Bizs', '门禁', '门禁', 'cs://1/image/aW1hZ2UvTVRwaFpqRTFZalEyWVdObFlXTTNObUUyTVRrMU1UUTJPR0k1TURVMVpXVTFPQQ', 1, 1, 40, '{"isSupportQR":1,"isSupportSmart":1}', 10, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115459, 999972, 0, 0, 0, '/home', 'Bizs', '热线', '热线', 'cs://1/image/aW1hZ2UvTVRwalpEWTFORFEyTmpWbE1XTTJNamt6TkdJd05tSXdOalZtTlRKallqQmxPUQ', 1, 1, 13, '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', 12, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115460, 999972, 0, 0, 0, '/home', 'Bizs', 'CLUB', '俱乐部', 'cs://1/image/aW1hZ2UvTVRvNVpHTTFaV0U1TkRNeU1qVTVZVFl4TURjNE1XUXlZMkl3TlRBeE1qTTBPQQ', 1, 1, 36, '{"privateFlag": 0}', 14, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115461, 999972, 0, 0, 0, '/home', 'Bizs', 'CONTACTS', '通讯录', 'cs://1/image/aW1hZ2UvTVRvek1Ua3pabU5oTWpNNE16STNObVE0WXpGak1EQmtPRGxsT1RZNU5UVmhNQQ', 1, 1, 46, '', 16, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 1, NULL, NULL, 0, NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115462, 999972, 0, 0, 0, '/home', 'Bizs', 'ELECSCREEN', '电子屏', 'cs://1/image/aW1hZ2UvTVRveU5HVmlPV1kxTlRNeVl6ZzROREl6Wmprd01UZGhORFl5TnpVM1kyRTNNdw', 1, 1, 13, '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', 18, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115463, 999972, 0, 0, 0, '/home', 'Bizs', 'ENTERPRISE', '园区企业', 'cs://1/image/aW1hZ2UvTVRveU16QXdZMll3WkRFMU1ERmhPR1JsTlRrNU9EbGxOVE01WlRGaE1UQXdPQQ', 1, 1, 34, '{"type":3}', 20, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115464, 999972, 0, 0, 0, '/home', 'Bizs', 'MEETINGROOM', '会议室', 'cs://1/image/aW1hZ2UvTVRwbU5HSXlabVV6WmpBMFltWmpNVFJpT0RGaU9EUTNaR1k0WXpsak0yRmpaUQ', 1, 1, 49, '{"resourceTypeId":10066,"pageType":0}', 22, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115465, 999972, 0, 0, 0, '/home', 'Bizs', '视频会议', '视频会议', 'cs://1/image/aW1hZ2UvTVRvNU5HSmlNak0yTXpnM01URmtZalpoTWpnMU1HSmlOR1E0TVdGallUTTJaZw', 1, 1, 27, '', 24, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 1, NULL, NULL, 0, NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115466, 999972, 0, 0, 0, '/home', 'Bizs', 'PUNCH', '考勤', 'cs://1/image/aW1hZ2UvTVRwaFptRTROR0V3Tm1Zd05USXlNMlkyTXpKbFl6UmxabUl3WkRaa01HWXlOZw', 1, 1, 23, '', 26, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115467, 999972, 0, 0, 0, '/home', 'Bizs', 'VIPPARKING', 'VIP车位', 'cs://1/image/aW1hZ2UvTVRwak4yWTFNelEzWmpoaE4ySTRZemhrTWpZNFptWTVNMll3T0RZd05qVXlPUQ', 1, 1, 13, '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', 28, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115468, 999972, 0, 0, 0, '/home', 'Bizs', 'FLOW_TASKS', '任务管理', 'cs://1/image/aW1hZ2UvTVRveE1EYzBPVFkyTVdZMFlqY3dZVEF3TXprMlpqUmlPR1UwWkdRd01EYzVPUQ', 1, 1, 13, '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', 30, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115469, 999972, 0, 0, 0, '/home', 'Bizs', '能量加油站', '能量加油站', 'cs://1/image/aW1hZ2UvTVRvM01tUTJOemd5TkdSbVlXVTRaRGt4WWpVMk5ETm1OelEwTldZME9HTXhZUQ', 1, 1, 13, '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', 32, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 0, NULL, NULL, 0, NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115470, 999972, 0, 0, 0, '/home', 'Bizs', 'wifi上网', 'wifi上网', 'cs://1/image/aW1hZ2UvTVRwallUZzNNemhpT1dSbE5EaGxOVGd3TmpZeFlqWTJaR0kzTm1ZMVlXVXpOZw', 1, 1, 13, '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', 34, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 0, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115471, 999972, 0, 0, 0, '/home', 'Bizs', 'PM', '物业报修', 'cs://1/image/aW1hZ2UvTVRwa016Z3lPV0l5TkRCaFlqWmpaRFExWm1Rd09HTTFNalJpTUdWaE1tTmlZdw', 1, 1, 13, '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', 36, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115472, 999972, 0, 0, 0, '/home', 'Bizs', '更多', '更多', 'cs://1/image/aW1hZ2UvTVRwa01qTm1OMkl3TXpsbFptUXdPRGsxTnpCaVltRXhObUprTkdVME9XUmpZUQ', 1, 1, 1, '{"itemLocation":"/home", "itemGroup":"Bizs"}', 38, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 1, NULL, NULL, 0, NULL);

	
-- 园区游客
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES (204876, 999972, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRvNE5qQXpabVkzTkRFNFpXUXhZV1JtWVRReE1tVXlaVFkzT1dFNFpqVTJPUQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'park_tourist');

INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`) 
    VALUES (595, 999972, 'ServiceMarketLayout', '{"versionCode": "2017070401","versionName": "4.7.0","layoutName": "ServiceMarketLayout","displayName": "服务市场","groups": [{"groupName": "","widget": "Banners","instanceConfig": {"itemGroup": "Default"},"style": "Default","defaultOrder": 1,"separatorFlag": 0,"separatorHeight": 0},{"groupName": "","widget": "Navigator","instanceConfig": {"itemGroup": "GovAgencies"},"style": "Default","defaultOrder": 2,"separatorFlag": 1,"separatorHeight": 16,"columnCount": 4},{"groupName": "","widget": "Bulletins","instanceConfig": {"itemGroup": "Default"},"style": "Default","defaultOrder": 1,"separatorFlag": 1,"separatorHeight": 16},{"groupName": "商家服务","widget": "Navigator","instanceConfig": {"itemGroup": "Bizs"},"style": "Default","defaultOrder": 5,"separatorFlag": 0,"separatorHeight": 0}]}', '2017070401', '2017070401', '2', '2017-07-04 19:09:30', 'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (115473, 999972, 0, 0, 0, '/home', 'GovAgencies', '交流大厅', '交流大厅', 'cs://1/image/aW1hZ2UvTVRvNFpqZ3dNRGM1WXpNelpqWmpaVGsyWkdFek1UVTBaalV3TWpneFpURTNOUQ', 1, 1, 50, '', 40, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
	 VALUES(115476, 999972, 0, 0, 0, '/home', 'GovAgencies', '创客空间', '创客空间', 'cs://1/image/aW1hZ2UvTVRvMFpqTXlPREppWkdJMFpqSXdZemhsT1dFM1pUZzNNVEpoTXpobE1ERXhOQQ', 1, 1, 32, '{"type":1,"forumId":177000,"categoryId":1010,"parentId":110001,"tag":"创客"}', 42, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
	 VALUES(115474, 999972, 0, 0, 0, '/home', 'GovAgencies', '园区入驻', '园区入驻', 'cs://1/image/aW1hZ2UvTVRvME5EY3pZMlEyTVRobE1HRTNPVEpoTldNM1ptSTBaV1JtTVdaaE1EWmhZZw', 1, 1, 28, '', 44, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
	 VALUES(115475, 999972, 0, 0, 0, '/home', 'GovAgencies', '服务广场', '服务广场', 'cs://1/image/aW1hZ2UvTVRveVlqQTBNVFEzTkdJNE5tSmhOakE1WlRNMllqWXlPV1UyTnprM05XRXhZZw', 1, 1, 33, '{"type":11,"parentId":11,"displayType": "grid"}', 46, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115477, 999972, 0, 0, 0, '/home', 'Bizs', '门禁', '门禁', 'cs://1/image/aW1hZ2UvTVRwaFpqRTFZalEyWVdObFlXTTNObUUyTVRrMU1UUTJPR0k1TURVMVpXVTFPQQ', 1, 1, 40, '{"isSupportQR":1,"isSupportSmart":1}', 48, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115478, 999972, 0, 0, 0, '/home', 'Bizs', '热线', '热线', 'cs://1/image/aW1hZ2UvTVRwalpEWTFORFEyTmpWbE1XTTJNamt6TkdJd05tSXdOalZtTlRKallqQmxPUQ', 1, 1, 13, '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', 50, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115479, 999972, 0, 0, 0, '/home', 'Bizs', 'CLUB', '俱乐部', 'cs://1/image/aW1hZ2UvTVRvNVpHTTFaV0U1TkRNeU1qVTVZVFl4TURjNE1XUXlZMkl3TlRBeE1qTTBPQQ', 1, 1, 36, '{"privateFlag": 0}', 52, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115480, 999972, 0, 0, 0, '/home', 'Bizs', 'CONTACTS', '通讯录', 'cs://1/image/aW1hZ2UvTVRvek1Ua3pabU5oTWpNNE16STNObVE0WXpGak1EQmtPRGxsT1RZNU5UVmhNQQ', 1, 1, 46, '', 54, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 1, NULL, NULL, 0, NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115481, 999972, 0, 0, 0, '/home', 'Bizs', 'ELECSCREEN', '电子屏', 'cs://1/image/aW1hZ2UvTVRveU5HVmlPV1kxTlRNeVl6ZzROREl6Wmprd01UZGhORFl5TnpVM1kyRTNNdw', 1, 1, 13, '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', 56, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115482, 999972, 0, 0, 0, '/home', 'Bizs', 'ENTERPRISE', '园区企业', 'cs://1/image/aW1hZ2UvTVRveU16QXdZMll3WkRFMU1ERmhPR1JsTlRrNU9EbGxOVE01WlRGaE1UQXdPQQ', 1, 1, 34, '{"type":3}', 58, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115483, 999972, 0, 0, 0, '/home', 'Bizs', 'MEETINGROOM', '会议室', 'cs://1/image/aW1hZ2UvTVRwbU5HSXlabVV6WmpBMFltWmpNVFJpT0RGaU9EUTNaR1k0WXpsak0yRmpaUQ', 1, 1, 49, '{"resourceTypeId":10066,"pageType":0}', 60, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115484, 999972, 0, 0, 0, '/home', 'Bizs', '视频会议', '视频会议', 'cs://1/image/aW1hZ2UvTVRvNU5HSmlNak0yTXpnM01URmtZalpoTWpnMU1HSmlOR1E0TVdGallUTTJaZw', 1, 1, 27, '', 62, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 1, NULL, NULL, 0, NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115485, 999972, 0, 0, 0, '/home', 'Bizs', 'PUNCH', '考勤', 'cs://1/image/aW1hZ2UvTVRwaFptRTROR0V3Tm1Zd05USXlNMlkyTXpKbFl6UmxabUl3WkRaa01HWXlOZw', 1, 1, 23, '', 64, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115486, 999972, 0, 0, 0, '/home', 'Bizs', 'VIPPARKING', 'VIP车位', 'cs://1/image/aW1hZ2UvTVRwak4yWTFNelEzWmpoaE4ySTRZemhrTWpZNFptWTVNMll3T0RZd05qVXlPUQ', 1, 1, 13, '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', 66, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115487, 999972, 0, 0, 0, '/home', 'Bizs', 'FLOW_TASKS', '任务管理', 'cs://1/image/aW1hZ2UvTVRveE1EYzBPVFkyTVdZMFlqY3dZVEF3TXprMlpqUmlPR1UwWkdRd01EYzVPUQ', 1, 1, 13, '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', 68, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115488, 999972, 0, 0, 0, '/home', 'Bizs', '能量加油站', '能量加油站', 'cs://1/image/aW1hZ2UvTVRvM01tUTJOemd5TkdSbVlXVTRaRGt4WWpVMk5ETm1OelEwTldZME9HTXhZUQ', 1, 1, 13, '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', 70, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 0, NULL, NULL, 0, NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115489, 999972, 0, 0, 0, '/home', 'Bizs', 'wifi上网', 'wifi上网', 'cs://1/image/aW1hZ2UvTVRwallUZzNNemhpT1dSbE5EaGxOVGd3TmpZeFlqWTJaR0kzTm1ZMVlXVXpOZw', 1, 1, 13, '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', 72, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 0, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115490, 999972, 0, 0, 0, '/home', 'Bizs', 'PM', '物业报修', 'cs://1/image/aW1hZ2UvTVRwa016Z3lPV0l5TkRCaFlqWmpaRFExWm1Rd09HTTFNalJpTUdWaE1tTmlZdw', 1, 1, 13, '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', 74, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES (115491, 999972, 0, 0, 0, '/home', 'Bizs', '更多', '更多', 'cs://1/image/aW1hZ2UvTVRwa01qTm1OMkl3TXpsbFptUXdPRGsxTnpCaVltRXhObUprTkdVME9XUmpZUQ', 1, 1, 1, '{"itemLocation":"/home", "itemGroup":"Bizs"}', 76, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 1, NULL, NULL, 0, NULL);


SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10000,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10100,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10400,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10600,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10750,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10751,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10752,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),11000,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20000,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20100,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20140,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20150,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20155,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20160,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20170,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20180,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20190,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20191,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20192,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40000,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40100,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40110,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40120,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40300,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40400,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40410,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40420,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40430,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40440,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40500,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40510,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40520,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40530,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40600,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40750,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40800,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40810,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40830,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40840,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),30000,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),31000,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),32000,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),33000,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),34000,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50000,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50100,'', 'EhNamespaces', 999972,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50200,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50210,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50220,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50300,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50400,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50500,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50600,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50630,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50631,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50632,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50633,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50640,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50650,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50651,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50652,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50653,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56161,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50700,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50710,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50720,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50730,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50800,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50810,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50820,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50830,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50840,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50850,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50860,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),60000,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),60100,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),60200,'', 'EhNamespaces', 999972,2);
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999972, 'sms.default.yzx', 1, 'zh_CN', '验证码-军民', '89972');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999972, 'sms.default.yzx', 4, 'zh_CN', '派单-军民', '90103');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999972, 'sms.default.yzx', 5, 'zh_CN', '任务-军民', '90296');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999972, 'sms.default.yzx', 6, 'zh_CN', '任务2-军民', '90298');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999972, 'sms.default.yzx', 7, 'zh_CN', '新报修-军民', '90327');

INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999972, 'sms.default.yzx', 15, 'zh_CN', '物业任务3-军民', '90343');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999972, 'sms.default.yzx', 12, 'zh_CN', '预定1-军民', '90345');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999972, 'sms.default.yzx', 13, 'zh_CN', '预定2-军民', '90346');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999972, 'sms.default.yzx', 14, 'zh_CN', '预定3-军民', '90347');

INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999972, 'sms.default.yzx', 51, 'zh_CN', '视频会-军民', '90350');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999972, 'sms.default.yzx', 52, 'zh_CN', '视测会-军民', '90352');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999972, 'sms.default.yzx', 53, 'zh_CN', '申诉-军民', '90348');

-- 缺少数据导致 园区入驻、招租管理出错。20170710 add by dengs
SET @eh_lease_configs_id = (SELECT MAX(id) FROM `eh_lease_configs`);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `rent_amount_flag`, `issuing_lease_flag`, `issuer_manage_flag`, `park_indroduce_flag`, `renew_flag`, `area_search_flag`, `display_name_str`, `display_order_str`) VALUES((@eh_lease_configs_id := @eh_lease_configs_id + 1),'999972','1','1','1','1','1','1','园区介绍, 虚位以待', '1,2');

-- 20170718 add by dengs
SET @eh_rentalv2_resource_types_id = (SELECT MAX(id) FROM `eh_rentalv2_resource_types`);
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES ((@eh_rentalv2_resource_types_id := @eh_rentalv2_resource_types_id+1), '会议室预订', 0, NULL, 2, 999972);
-- 20170719 add by dengs,增加俱乐部分类
set @eh_categories_id = (select max(id) FROM eh_categories);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'亲子与教育','兴趣/亲子与教育', 0, 2, UTC_TIMESTAMP(), 999972);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'运动与音乐','兴趣/运动与音乐', 0, 2, UTC_TIMESTAMP(), 999972);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'美食与厨艺','兴趣/美食与厨艺', 0, 2, UTC_TIMESTAMP(), 999972);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'美容化妆','兴趣/美容化妆', 0, 2, UTC_TIMESTAMP(), 999972);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'家庭装饰','兴趣/家庭装饰', 0, 2, UTC_TIMESTAMP(), 999972);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'名牌汇','兴趣/名牌汇', 0, 2, UTC_TIMESTAMP(), 999972);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'宠物汇','兴趣/宠物汇', 0, 2, UTC_TIMESTAMP(), 999972);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'旅游摄影','兴趣/旅游摄影', 0, 2, UTC_TIMESTAMP(), 999972);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'老乡群','兴趣/老乡群', 0, 2, UTC_TIMESTAMP(), 999972);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'同事群','兴趣/同事群', 0, 2, UTC_TIMESTAMP(), 999972);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'同学群','兴趣/同学群', 0, 2, UTC_TIMESTAMP(), 999972);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'二手交易','兴趣/二手交易', 0, 2, UTC_TIMESTAMP(), 999972);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'其他','兴趣/其他', 0, 2, UTC_TIMESTAMP(), 999972);

SET FOREIGN_KEY_CHECKS = 1;



