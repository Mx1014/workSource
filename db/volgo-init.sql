SET FOREIGN_KEY_CHECKS = 0;
SET @core_server_url = "alpha.lab.everhomes.com"; -- 取具体环境连接core server的链接
SET @biz_url = 'biz-alpha.lab.everhomes.com'; -- 取具体环境的电商服务器连接，注意有两个域名要修改
SET @namespace_id=1;
SET @user_id = 265000;  -- 需要取现网eh_users的ID的最大值再加一定余量
SET @account_name='286585'; -- 需要取现网eh_users的acount_name的6位的最大值再加一定余量
SET @community_id = 240111044331058733; -- 需要取现网eh_communities的ID的最大值再加一定余量
SET @organization_id = 1023080; 	-- 需要取eh_organizations的ID最大值并加一定余量，如果修改此值则其path也要改
SET @community_geopoint_id = (SELECT MAX(id) FROM `eh_community_geopoints`);  
SET @org_group_id = 1012469; -- 需要取eh_groups的ID的最大值再加一定余量
SET @org_forum_id = 190412;   -- @community_forum_id+2
SET @feedback_forum_id = 190411;   -- @community_forum_id+1
SET @community_forum_id = 190410;   -- 取eh_forums的ID最大值再加一定余量
SET @building_id = 194001;   -- 取eh_buildings的ID最大值再加一定余量
SET @sheng_id = 17251;  -- 取eh_regions的ID最大值再加一定余量
SET @shi_id = @sheng_id + 1;  -- 在@sheng_id上加1
SET @qu_id = @shi_id + 1;    -- 在@shi_id上加1
SET @address_id = 239825274387253717; -- 需要取现网eh_addresses的ID的最大值再加一定余量
SET @rentalv_type_id = 10612; -- 需要取现网eh_rentalv2_resource_types的ID的最大值再加一定余量
SET @layout_id = (SELECT MAX(id) FROM `eh_launch_pad_layouts`); 
SET @item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`); 
SET @banner_id = (SELECT MAX(id) FROM `eh_banners`);
SET @org_member_id = (SELECT MAX(id) FROM `eh_organization_members`); 
SET @role_assignment_id = (SELECT MAX(id) FROM `eh_acl_role_assignments`); 
SET @user_identifier_id = (SELECT max(id) FROM `eh_user_identifiers`);
SET @organization_address_mapping_id = (SELECT max(id) FROM `eh_organization_address_mappings`);
SET @namespace_detail_id = (SELECT max(id) FROM `eh_namespace_details`);
SET @org_cmnty_request_id = (SELECT max(id) FROM `eh_organization_community_requests`);
SET @namespace_resource_id = (SELECT max(id) FROM `eh_namespace_resources`);
SET @configuration_id = (SELECT max(id) FROM `eh_configurations`);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ((@configuration_id := @configuration_id + 1), 'app.agreements.url', CONCAT('http://', @core_server_url, '/mobile/static/app_agreements/agreements.html?ns=1'), 'the relative path for volgo app agreements', @namespace_id, NULL);	   
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ((@configuration_id := @configuration_id + 1), 'business.url', CONCAT('http://', @biz_url, '/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2F', @biz_url, '%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix'), 'biz access url for volgo', @namespace_id, NULL);	   

INSERT INTO `eh_version_realm` VALUES (101, 'Android_OA', null, UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_version_realm` VALUES (102, 'iOS_OA', null, UTC_TIMESTAMP(), @namespace_id);

INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES(301,101,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES(302,102,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());
	
	
INSERT INTO `eh_namespaces`(`id`, `name`) VALUES(@namespace_id, 'Volgo');
INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES((@namespace_detail_id := @namespace_detail_id + 1), @namespace_id, 'community_commercial', UTC_TIMESTAMP());

   
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (@sheng_id, '0', '广东', 'GUANGDONG', 'GD', '/广东', '1', '1', '', '', '2', '2', @namespace_id);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (@shi_id, @sheng_id, '深圳市', 'SHENZHENSHI', 'SZS', '/广东/深圳市', '2', '2', NULL, '0755', '2', '1', @namespace_id);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (@qu_id, @shi_id, '南山区', 'NANSHANQU', 'NSQ', '/广东/深圳市/南山区', '3', '3', NULL, '0755', '2', '0', @namespace_id);

-- 左邻是物业管理公司
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(@org_group_id, UUID(), '左邻公司圈', '左邻公司圈', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @org_forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(@org_forum_id, UUID(), @namespace_id, 2, 'EhGroups', @org_group_id,'左邻公司论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());       
	
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'PM', '深圳市永佳天成科技发展有限公司', '左邻致力于通过移动互联网、智能互联和大数据技术，为园区、写字楼以及物业提供基于移动互联网的服务运营解决方案和联合运营服务，共营本地服务升级。', '/1023080', 1, 2, 'ENTERPRISE', @namespace_id, @org_group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@org_cmnty_request_id := @org_cmnty_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 
	
SET @user_id = @user_id + 1;
SET @account_name = @account_name + 1;
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (@user_id, UUID(), @account_name, '李祥涛', '', 1, 45, '1', '2',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES ((@user_identifier_id := @user_identifier_id + 1) , @user_id ,  '0',  '13590318460',  '221616',  3, UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`)
	VALUES((@org_member_id := @org_member_id + 1), @organization_id, 'USER', @user_id  , 'manager', '李祥涛', 0, '13889818001', 3, @namespace_id);	
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES((@role_assignment_id := @role_assignment_id + 1), 'EhOrganizations', @organization_id, 'EhUsers', @user_id  , 1001, 1, UTC_TIMESTAMP());


INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(@community_forum_id, UUID(), @namespace_id, 2, 'EhGroups', 0,'Volgo论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(@feedback_forum_id, UUID(), @namespace_id, 2, 'EhGroups', 0,'Volgo意见反馈论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 


INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES(@community_id, UUID(), @shi_id, '深圳市',  @qu_id, '南山区', '深圳科技园', '科技园', '科苑路', NULL, '深圳市科技园成立于1996年9月，面积11.5平方公里，是国家科技部“建设世界一流科技园区”发展战略的6家试点园区之一。是国家级高新技术产品出口基地、亚太经合组织开放园区、先进国家高新技术产业开发区、国家知识产权试点园区、中国青年科技创新行动示范基地、国家火炬计划软件产业基地、国家高新技术产业标准化示范区、国家海外高层次人才创新创业基地、科技与金融相结合全国试点园区以及国家文化和科技融合示范基地。',NULL, NULL, NULL, NULL, NULL, NULL,NULL, 682, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,1, @community_forum_id, @feedback_forum_id, UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES((@community_geopoint_id := @community_geopoint_id + 1), @community_id, '', 113.953143, 22.549789, 'ws103144h9mf');
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(@organization_id, @community_id);
INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES((@namespace_resource_id := @namespace_resource_id + 1), 1, 'COMMUNITY', @community_id, UTC_TIMESTAMP());	

-- INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
--	VALUES (194001, 240111044331058733, '金融基地', '金融基地', '2100163', '13509688398', '深圳市南山区科技园科苑路6号', NULL, '113.953143', '22.549789', 'ws103144h9mf', '科技园金融基地一期总建筑面积9.6万平方米，项目分A、B两栋，每栋12层，1-3层为商业裙楼，约14000平方米，4-12层为办公场所。科技园金融基地一期写字楼荣获深圳首家国际绿色建筑LEEDTM认证金奖，独享双花园广场景观，南面为3000平米外花园广场，拥有面积达5000平米的围合式中央花园。尤为值得一提的是在基地建设运营中，科技园金融基地与世界著名的物业顾问公司——世邦魏理仕全方位合作，全方位提升物业管理水平。', 'cs://1/image/aW1hZ2UvTVRvNE1HUmlaRGszT1RkaU1EVmxaV1k0T0RJMk5UWmpOakEzWmpNMVltRXlaZw', '2', '1', '2017-04-24 23:01:31', '195506', '2015-12-09 14:57:13', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, @namespace_id);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
	VALUES ((@building_id := @building_id + 1), @community_id, '金融基地', '金融基地', '2100163', '13509688398', '深圳市南山区科技园科苑路6号', NULL, '113.953143', '22.549789', 'ws103144h9mf', '科技园金融基地一期总建筑面积9.6万平方米，项目分A、B两栋，每栋12层，1-3层为商业裙楼，约14000平方米，4-12层为办公场所。科技园金融基地一期写字楼荣获深圳首家国际绿色建筑LEEDTM认证金奖，独享双花园广场景观，南面为3000平米外花园广场，拥有面积达5000平米的围合式中央花园。尤为值得一提的是在基地建设运营中，科技园金融基地与世界著名的物业顾问公司——世邦魏理仕全方位合作，全方位提升物业管理水平。', 'cs://1/image/aW1hZ2UvTVRvNE1HUmlaRGszT1RkaU1EVmxaV1k0T0RJMk5UWmpOakEzWmpNMVltRXlaZw', '2', '1', '2017-04-24 23:01:31', '195506', '2015-12-09 14:57:13', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, @namespace_id);

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387254851,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'金融基地-1A1','金融基地','1A1','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387254852,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'金融基地-1A2','金融基地','1A2','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387254853,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'金融基地-1B','金融基地','1B','2','0',UTC_TIMESTAMP(), @namespace_id);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387254854,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'金融基地-1C','金融基地','1C','2','0',UTC_TIMESTAMP(), @namespace_id);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387254855,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'金融基地-1D','金融基地','1D','2','0',UTC_TIMESTAMP(), @namespace_id);	

INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387254851, '金融基地-1A1', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387254852, '金融基地-1A2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387254853, '金融基地-1B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387254854, '金融基地-1C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387254855, '金融基地-1D', '0');
	
	
SET @community_id = @community_id + 1;
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES(@community_id, UUID(), @shi_id, '深圳市',  @qu_id, '南山区', '百度国际大厦', '百度大厦', '学府路', NULL, '百度国际大厦位于深圳市南山区高新技术产业园，大厦高181米，建筑面积超过22万平方米，由东西两座塔楼组成，可容纳万名员工，于2012年1月16日举行奠基仪式，预计在2015年完成竣工。大厦的立面设计以“百度”的命名为灵感，将“众里寻他千百度”这句中国古词巧妙转换为“二进制代码”的现代计算机语言，形成独一无二的百度国际大厦外立面。建成后的百度国际大厦除了是百度华南总部，还将扮演百度国际总部和深圳研发中心两个重要角色。',NULL, NULL, NULL, NULL, NULL, NULL,NULL, 682, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,1, @community_forum_id, @feedback_forum_id, UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES((@community_geopoint_id := @community_geopoint_id + 1), @community_id, '', 113.948913, 22.530194, 'ws100vrpfhef');
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(@organization_id, @community_id);
INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES((@namespace_resource_id := @namespace_resource_id + 1), 1, 'COMMUNITY', @community_id, UTC_TIMESTAMP());	
	
	
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
	VALUES((@building_id := @building_id + 1), @community_id, '百度国际大厦东塔', '百度大厦东塔', 0, '', '广东省深圳市南山区学府路', NULL, 113.950281, 22.53035, 'ws101j80ccxu', '', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, @namespace_id);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
	VALUES((@building_id := @building_id + 1), @community_id, '百度国际大厦西塔', '百度大厦西塔', 0, '', '广东省深圳市南山区学府路', NULL, 113.950281, 22.53035, 'ws101j80ccxu', '', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, @namespace_id);

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253718,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-0101','百度国际大厦东塔','0101','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253719,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-0102','百度国际大厦东塔','0102','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253720,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-0201','百度国际大厦东塔','0201','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253721,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-0202','百度国际大厦东塔','0202','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253722,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-0301','百度国际大厦东塔','0301','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253723,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-0302','百度国际大厦东塔','0302','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253724,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-0401','百度国际大厦东塔','0401','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253725,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-0402','百度国际大厦东塔','0402','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253726,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-0501','百度国际大厦东塔','0501','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253727,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-0502','百度国际大厦东塔','0502','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253728,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-0601','百度国际大厦东塔','0601','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253729,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-0602','百度国际大厦东塔','0602','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253730,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-0701','百度国际大厦东塔','0701','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253731,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-0702','百度国际大厦东塔','0702','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253732,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-0801','百度国际大厦东塔','0801','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253733,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-0802','百度国际大厦东塔','0802','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253734,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-0901','百度国际大厦东塔','0901','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253735,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-0902','百度国际大厦东塔','0902','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253736,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-1001','百度国际大厦东塔','1001','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253737,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-1002','百度国际大厦东塔','1002','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253738,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-1101','百度国际大厦东塔','1101','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253739,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-1102','百度国际大厦东塔','1102','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253740,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-1201','百度国际大厦东塔','1201','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253741,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-1202','百度国际大厦东塔','1202','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253742,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-1301','百度国际大厦东塔','1301','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253743,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-1302','百度国际大厦东塔','1302','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253744,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-1401','百度国际大厦东塔','1401','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253745,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-1402','百度国际大厦东塔','1402','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253746,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-1501','百度国际大厦东塔','1501','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253747,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-1502','百度国际大厦东塔','1502','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253748,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-1601','百度国际大厦东塔','1601','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253749,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-1602','百度国际大厦东塔','1602','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253750,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-1701','百度国际大厦东塔','1701','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253751,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-1702','百度国际大厦东塔','1702','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253752,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-1801','百度国际大厦东塔','1801','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253753,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-1802','百度国际大厦东塔','1802','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253754,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-1901','百度国际大厦东塔','1901','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253755,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-1902','百度国际大厦东塔','1902','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253756,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-2001','百度国际大厦东塔','2001','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253757,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-2002','百度国际大厦东塔','2002','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253758,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-2101','百度国际大厦东塔','2101','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253759,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-2102','百度国际大厦东塔','2102','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253760,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-2201','百度国际大厦东塔','2201','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253761,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-2202','百度国际大厦东塔','2202','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253762,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-2301','百度国际大厦东塔','2301','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253763,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-2301','百度国际大厦东塔','2301','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253764,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-2401','百度国际大厦东塔','2401','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253765,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-2402','百度国际大厦东塔','2402','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253766,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-2501','百度国际大厦东塔','2501','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253767,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-2601','百度国际大厦东塔','2601','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253768,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-2701','百度国际大厦东塔','2701','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253769,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-2702','百度国际大厦东塔','2702','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253770,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-2801','百度国际大厦东塔','2801','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253771,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-2802','百度国际大厦东塔','2802','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253772,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-2901','百度国际大厦东塔','2901','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253773,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-2902','百度国际大厦东塔','2902','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253774,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-3001','百度国际大厦东塔','3001','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253775,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-3002','百度国际大厦东塔','3002','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253776,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-3101','百度国际大厦东塔','3101','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253777,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-3102','百度国际大厦东塔','3102','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253778,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-3201','百度国际大厦东塔','3201','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253779,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-3202','百度国际大厦东塔','3202','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253780,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-3301','百度国际大厦东塔','3301','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253781,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-3302','百度国际大厦东塔','3302','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253782,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-3401','百度国际大厦东塔','3401','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253783,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-3402','百度国际大厦东塔','3402','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253784,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-3501','百度国际大厦东塔','3501','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253785,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-3502','百度国际大厦东塔','3502','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253786,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-3601','百度国际大厦东塔','3601','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253787,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-3602','百度国际大厦东塔','3602','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253788,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-3701','百度国际大厦东塔','3701','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253789,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-3702','百度国际大厦东塔','3702','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253790,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-3801','百度国际大厦东塔','3801','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253791,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-3802','百度国际大厦东塔','3802','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253792,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-3901','百度国际大厦东塔','3901','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253793,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦东塔-3902','百度国际大厦东塔','3902','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253794,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-0101','百度国际大厦西塔','0101','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253795,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-0102','百度国际大厦西塔','0102','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253796,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-0201','百度国际大厦西塔','0201','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253797,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-0202','百度国际大厦西塔','0202','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253798,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-0301','百度国际大厦西塔','0301','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253799,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-0302','百度国际大厦西塔','0302','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253800,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-0401','百度国际大厦西塔','0401','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253801,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-0402','百度国际大厦西塔','0402','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253802,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-0501','百度国际大厦西塔','0501','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253803,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-0502','百度国际大厦西塔','0502','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253804,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-0601','百度国际大厦西塔','0601','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253805,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-0602','百度国际大厦西塔','0602','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253806,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-0701','百度国际大厦西塔','0701','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253807,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-0702','百度国际大厦西塔','0702','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253808,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-0801','百度国际大厦西塔','0801','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253809,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-0802','百度国际大厦西塔','0802','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253810,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-0901','百度国际大厦西塔','0901','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253811,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-0902','百度国际大厦西塔','0902','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253812,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-1001','百度国际大厦西塔','1001','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253813,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-1002','百度国际大厦西塔','1002','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253814,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-1101','百度国际大厦西塔','1101','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253815,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-1102','百度国际大厦西塔','1102','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253816,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-1201','百度国际大厦西塔','1201','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253817,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-1202','百度国际大厦西塔','1202','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253818,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-1301','百度国际大厦西塔','1301','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253819,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-1302','百度国际大厦西塔','1302','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253820,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-1401','百度国际大厦西塔','1401','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253821,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-1402','百度国际大厦西塔','1402','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253822,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-1501','百度国际大厦西塔','1501','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253823,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-1502','百度国际大厦西塔','1502','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253824,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-1601','百度国际大厦西塔','1601','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253825,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-1602','百度国际大厦西塔','1602','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253826,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-1701','百度国际大厦西塔','1701','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253827,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-1702','百度国际大厦西塔','1702','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253828,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-1801','百度国际大厦西塔','1801','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253829,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-1802','百度国际大厦西塔','1802','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253830,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-1901','百度国际大厦西塔','1901','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253831,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-1902','百度国际大厦西塔','1902','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253832,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-2001','百度国际大厦西塔','2001','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253833,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-2002','百度国际大厦西塔','2002','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253834,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-2101','百度国际大厦西塔','2101','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253835,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-2102','百度国际大厦西塔','2102','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253836,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-2201','百度国际大厦西塔','2201','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253837,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-2202','百度国际大厦西塔','2202','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253838,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-2301','百度国际大厦西塔','2301','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253839,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-2301','百度国际大厦西塔','2301','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253840,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-2401','百度国际大厦西塔','2401','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253841,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-2402','百度国际大厦西塔','2402','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253842,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-2501','百度国际大厦西塔','2501','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253843,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-2601','百度国际大厦西塔','2601','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253844,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-2701','百度国际大厦西塔','2701','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253845,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-2702','百度国际大厦西塔','2702','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253846,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-2801','百度国际大厦西塔','2801','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253847,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-2802','百度国际大厦西塔','2802','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253848,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-2901','百度国际大厦西塔','2901','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253849,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-2902','百度国际大厦西塔','2902','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253850,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-3001','百度国际大厦西塔','3001','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253851,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-3002','百度国际大厦西塔','3002','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253852,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-3101','百度国际大厦西塔','3101','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253853,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-3102','百度国际大厦西塔','3102','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253854,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-3201','百度国际大厦西塔','3201','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253855,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-3202','百度国际大厦西塔','3202','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253856,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-3301','百度国际大厦西塔','3301','2','0',UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387253857,UUID(),@community_id, @shi_id, '深圳市',  @qu_id, '南山区' ,'百度国际大厦西塔-3302','百度国际大厦西塔','3302','2','0',UTC_TIMESTAMP(), @namespace_id);

INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253718, '百度国际大厦东塔-0101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253719, '百度国际大厦东塔-0102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253720, '百度国际大厦东塔-0201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253721, '百度国际大厦东塔-0202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253722, '百度国际大厦东塔-0301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253723, '百度国际大厦东塔-0302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253724, '百度国际大厦东塔-0401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253725, '百度国际大厦东塔-0402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253726, '百度国际大厦东塔-0501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253727, '百度国际大厦东塔-0502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253728, '百度国际大厦东塔-0601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253729, '百度国际大厦东塔-0602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253730, '百度国际大厦东塔-0701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253731, '百度国际大厦东塔-0702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253732, '百度国际大厦东塔-0801', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253733, '百度国际大厦东塔-0802', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253734, '百度国际大厦东塔-0901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253735, '百度国际大厦东塔-0902', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253736, '百度国际大厦东塔-1001', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253737, '百度国际大厦东塔-1002', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253738, '百度国际大厦东塔-1101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253739, '百度国际大厦东塔-1102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253740, '百度国际大厦东塔-1201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253741, '百度国际大厦东塔-1202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253742, '百度国际大厦东塔-1301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253743, '百度国际大厦东塔-1302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253744, '百度国际大厦东塔-1401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253745, '百度国际大厦东塔-1402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253746, '百度国际大厦东塔-1501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253747, '百度国际大厦东塔-1502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253748, '百度国际大厦东塔-1601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253749, '百度国际大厦东塔-1602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253750, '百度国际大厦东塔-1701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253751, '百度国际大厦东塔-1702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253752, '百度国际大厦东塔-1801', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253753, '百度国际大厦东塔-1802', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253754, '百度国际大厦东塔-1901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253755, '百度国际大厦东塔-1902', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253756, '百度国际大厦东塔-2001', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253757, '百度国际大厦东塔-2002', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253758, '百度国际大厦东塔-2101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253759, '百度国际大厦东塔-2102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253760, '百度国际大厦东塔-2201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253761, '百度国际大厦东塔-2202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253762, '百度国际大厦东塔-2301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253763, '百度国际大厦东塔-2301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253764, '百度国际大厦东塔-2401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253765, '百度国际大厦东塔-2402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253766, '百度国际大厦东塔-2501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253767, '百度国际大厦东塔-2601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253768, '百度国际大厦东塔-2701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253769, '百度国际大厦东塔-2702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253770, '百度国际大厦东塔-2801', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253771, '百度国际大厦东塔-2802', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253772, '百度国际大厦东塔-2901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253773, '百度国际大厦东塔-2902', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253774, '百度国际大厦东塔-3001', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253775, '百度国际大厦东塔-3002', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253776, '百度国际大厦东塔-3101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253777, '百度国际大厦东塔-3102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253778, '百度国际大厦东塔-3201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253779, '百度国际大厦东塔-3202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253780, '百度国际大厦东塔-3301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253781, '百度国际大厦东塔-3302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253782, '百度国际大厦东塔-3401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253783, '百度国际大厦东塔-3402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253784, '百度国际大厦东塔-3501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253785, '百度国际大厦东塔-3502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253786, '百度国际大厦东塔-3601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253787, '百度国际大厦东塔-3602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253788, '百度国际大厦东塔-3701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253789, '百度国际大厦东塔-3702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253790, '百度国际大厦东塔-3801', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253791, '百度国际大厦东塔-3802', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253792, '百度国际大厦东塔-3901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253793, '百度国际大厦东塔-3902', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253794, '百度国际大厦西塔-0101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253795, '百度国际大厦西塔-0102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253796, '百度国际大厦西塔-0201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253797, '百度国际大厦西塔-0202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253798, '百度国际大厦西塔-0301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253799, '百度国际大厦西塔-0302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253800, '百度国际大厦西塔-0401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253801, '百度国际大厦西塔-0402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253802, '百度国际大厦西塔-0501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253803, '百度国际大厦西塔-0502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253804, '百度国际大厦西塔-0601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253805, '百度国际大厦西塔-0602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253806, '百度国际大厦西塔-0701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253807, '百度国际大厦西塔-0702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253808, '百度国际大厦西塔-0801', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253809, '百度国际大厦西塔-0802', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253810, '百度国际大厦西塔-0901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253811, '百度国际大厦西塔-0902', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253812, '百度国际大厦西塔-1001', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253813, '百度国际大厦西塔-1002', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253814, '百度国际大厦西塔-1101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253815, '百度国际大厦西塔-1102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253816, '百度国际大厦西塔-1201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253817, '百度国际大厦西塔-1202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253818, '百度国际大厦西塔-1301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253819, '百度国际大厦西塔-1302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253820, '百度国际大厦西塔-1401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253821, '百度国际大厦西塔-1402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253822, '百度国际大厦西塔-1501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253823, '百度国际大厦西塔-1502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253824, '百度国际大厦西塔-1601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253825, '百度国际大厦西塔-1602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253826, '百度国际大厦西塔-1701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253827, '百度国际大厦西塔-1702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253828, '百度国际大厦西塔-1801', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253829, '百度国际大厦西塔-1802', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253830, '百度国际大厦西塔-1901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253831, '百度国际大厦西塔-1902', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253832, '百度国际大厦西塔-2001', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253833, '百度国际大厦西塔-2002', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253834, '百度国际大厦西塔-2101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253835, '百度国际大厦西塔-2102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253836, '百度国际大厦西塔-2201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253837, '百度国际大厦西塔-2202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253838, '百度国际大厦西塔-2301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253839, '百度国际大厦西塔-2301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253840, '百度国际大厦西塔-2401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253841, '百度国际大厦西塔-2402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253842, '百度国际大厦西塔-2501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253843, '百度国际大厦西塔-2601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253844, '百度国际大厦西塔-2701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253845, '百度国际大厦西塔-2702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253846, '百度国际大厦西塔-2801', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253847, '百度国际大厦西塔-2802', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253848, '百度国际大厦西塔-2901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253849, '百度国际大厦西塔-2902', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253850, '百度国际大厦西塔-3001', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253851, '百度国际大厦西塔-3002', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253852, '百度国际大厦西塔-3101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253853, '百度国际大厦西塔-3102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253854, '百度国际大厦西塔-3201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253855, '百度国际大厦西塔-3202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253856, '百度国际大厦西塔-3301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, 239825274387253857, '百度国际大厦西塔-3302', '0');

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES (10612, '会议室预订', 0, NULL, 0, @namespace_id);


	
-- 园区管理员  
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES ((@banner_id := @banner_id + 1), @namespace_id, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRvek1USXhNbVkyTURsalptVXlNemc0T1RFMllqUTJPR1ZqWXpsa016Y3haQQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'pm_admin');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES ((@banner_id := @banner_id + 1), @namespace_id, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRvM1lXTXhNVEV4TVRSa01HTm1PV016WmpObE9UYzNZalpqTW1Gak1tVTJZdw', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'pm_admin');
  
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`) 
    VALUES ((@layout_id := @layout_id + 1), @namespace_id, 'ServiceMarketLayout', '{"versionCode":"2017042501","versionName":"3.5.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":16,"columnCount":4},{"groupName":"","widget":"OPPush","instanceConfig":{"itemGroup":"OPPushBiz","entityCount":6,"subjectHeight":1,"descriptionHeight":0},"style":"HorizontalScrollView","defaultOrder":1,"separatorFlag":1,"separatorHeight":0,"columnCount":0}]}', '2017042501', '2017042501', '2', '2017-04-25 16:09:30', 'pm_admin');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), @namespace_id, '0', '0', '0', '/home', 'Bizs', 'ACLINK', '门禁', 'cs://1/image/aW1hZ2UvTVRwbE5tRTRaR0pqWkRCa01qQTJNR0prWm1Nd09XSXpPVFJoWlRjMU0ySXhOQQ', '1', '1', '40', '{"isSupportQR":1,"isSupportSmart":0}', 10, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), @namespace_id, '0', '0', '0', '/home', 'Bizs', 'PUNCH', '打卡考勤', 'cs://1/image/aW1hZ2UvTVRwak5EZzFOR05rTWpjd09UYzBaRGs1T0RsbE5EVmpZelkzTVRBMVpqSTNOdw', '1', '1', '23', '', 20, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), @namespace_id, '0', '0', '0', '/home', 'Bizs', 'MY_APPROVAL', '审批', 'cs://1/image/aW1hZ2UvTVRveE16RTRNalZsWXpBeE5qWXpPV1kxTnpWbVptTmxNbVF4TVRWak1qVXlPUQ', '1', '1', '54', '', 30, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), @namespace_id, '0', '0', '0', '/home', 'Bizs', 'CONTACTS', '通讯录', 'cs://1/image/aW1hZ2UvTVRwa1pEWXdPREkxTnpRNE1qWm1PVGxqTlRnME1qRXhNMlV3WmpCalpUbGlOQQ', '1', '1', '46', '', 40, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), @namespace_id, '0', '0', '0', '/home', 'Bizs', 'RENTAL', '会议室预定', 'cs://1/image/aW1hZ2UvTVRvMVpUTTRNRE01T0RNM1lXUXlZMkl6WTJOak1qWmpaakkyWkdOak56WTFOdw', '1', '1', '49', '{"resourceTypeId":10612,"pageType":0}', 50, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), @namespace_id, '0', '0', '0', '/home', 'Bizs', 'VIDEO_MEETING', '视频会议', 'cs://1/image/aW1hZ2UvTVRveE4yVmpORFZoT0RBNE9HVXpNekJtTmpKaE5UaGpPVEZtWWpjeFpXUmxOUQ', '1', '1', '27', '', 60, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
--    VALUES ((@item_id := @item_id + 1), @namespace_id, '0', '0', '0', '/home', 'Bizs', 'PRINT', '打印', 'cs://1/image/aW1hZ2UvTVRvMVl6aGlOekkxT0dGbVlXSTBZbVZtTURjMU5EQmtOMkU0WmpCbU1tSXpOdw', '1', '1', '14', '{"url": "http://core.zuolin.com/mobile/static/coming_soon/index.html"}', 70, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), @namespace_id, '0', '0', '0', '/home', 'OPPushBiz', 'Biz', '能量加油站', '', '1', '1', '14', CONCAT('{"url": "http://', @biz_url, '/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2F', @biz_url, '%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix"}'), 80, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');

	
-- 园区游客
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES ((@banner_id := @banner_id + 1), @namespace_id, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRvek1USXhNbVkyTURsalptVXlNemc0T1RFMllqUTJPR1ZqWXpsa016Y3haQQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'park_tourist');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES ((@banner_id := @banner_id + 1), @namespace_id, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRvM1lXTXhNVEV4TVRSa01HTm1PV016WmpObE9UYzNZalpqTW1Gak1tVTJZdw', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'park_tourist');

INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`) 
    VALUES ((@layout_id := @layout_id + 1), @namespace_id, 'ServiceMarketLayout', '{"versionCode":"2017042501","versionName":"3.5.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":16,"columnCount":4},{"groupName":"","widget":"OPPush","instanceConfig":{"itemGroup":"OPPushBiz","entityCount":6,"subjectHeight":1,"descriptionHeight":0},"style":"HorizontalScrollView","defaultOrder":1,"separatorFlag":1,"separatorHeight":0,"columnCount":0}]}', '2017042501', '2017042501', '2', '2017-04-25 16:09:30', 'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), @namespace_id, '0', '0', '0', '/home', 'Bizs', 'ACLINK', '门禁', 'cs://1/image/aW1hZ2UvTVRwbE5tRTRaR0pqWkRCa01qQTJNR0prWm1Nd09XSXpPVFJoWlRjMU0ySXhOQQ', '1', '1', '40', '{"isSupportQR":1,"isSupportSmart":0}', 10, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), @namespace_id, '0', '0', '0', '/home', 'Bizs', 'PUNCH', '打卡考勤', 'cs://1/image/aW1hZ2UvTVRwak5EZzFOR05rTWpjd09UYzBaRGs1T0RsbE5EVmpZelkzTVRBMVpqSTNOdw', '1', '1', '23', '', 20, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), @namespace_id, '0', '0', '0', '/home', 'Bizs', 'MY_APPROVAL', '审批', 'cs://1/image/aW1hZ2UvTVRveE16RTRNalZsWXpBeE5qWXpPV1kxTnpWbVptTmxNbVF4TVRWak1qVXlPUQ', '1', '1', '54', '', 30, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), @namespace_id, '0', '0', '0', '/home', 'Bizs', 'CONTACTS', '通讯录', 'cs://1/image/aW1hZ2UvTVRwa1pEWXdPREkxTnpRNE1qWm1PVGxqTlRnME1qRXhNMlV3WmpCalpUbGlOQQ', '1', '1', '46', '', 40, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), @namespace_id, '0', '0', '0', '/home', 'Bizs', 'RENTAL', '会议室预定', 'cs://1/image/aW1hZ2UvTVRvMVpUTTRNRE01T0RNM1lXUXlZMkl6WTJOak1qWmpaakkyWkdOak56WTFOdw', '1', '1', '49', '{"resourceTypeId":10612,"pageType":0}', 50, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), @namespace_id, '0', '0', '0', '/home', 'Bizs', 'VIDEO_MEETING', '视频会议', 'cs://1/image/aW1hZ2UvTVRveE4yVmpORFZoT0RBNE9HVXpNekJtTmpKaE5UaGpPVEZtWWpjeFpXUmxOUQ', '1', '1', '27', '', 60, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
--     VALUES ((@item_id := @item_id + 1), @namespace_id, '0', '0', '0', '/home', 'Bizs', 'PRINT', '打印', 'cs://1/image/aW1hZ2UvTVRvMVl6aGlOekkxT0dGbVlXSTBZbVZtTURjMU5EQmtOMkU0WmpCbU1tSXpOdw', '1', '1', '14', '{"url": "http://core.zuolin.com/mobile/static/coming_soon/index.html"}', 70, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), @namespace_id, '0', '0', '0', '/home', 'OPPushBiz', 'Biz', '能量加油站', '', '1', '1', '14', CONCAT('{"url": "http://', @biz_url, '/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2F', @biz_url, '%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix"}'), 80, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');


SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10000,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10100,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10400,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10600,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),11000,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40000,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40400,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40410,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40420,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40430,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40440,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41300,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41310,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41000,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41010,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41020,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41030,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41040,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41050,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41060,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),30000,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),30500,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),31000,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),32000,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),34000,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),35000,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50000,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50100,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50110,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50200,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50210,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50220,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50300,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50400,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50500,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50600,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50630,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50631,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50632,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50633,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50640,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50650,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50651,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50652,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50653,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56161,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50700,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50710,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50720,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50730,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50800,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50810,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50820,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50830,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50840,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50850,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50860,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),60000,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),60100,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),60200,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),33000,'', 'EhNamespaces', @namespace_id,2);



INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(@namespace_id, 'sms.default.yzx', 1, 'zh_CN', '验证码-Volgo', '43069');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(@namespace_id, 'sms.default.yzx', 4, 'zh_CN', '派单-Volgo', '43071');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(@namespace_id, 'sms.default.yzx', 5, 'zh_CN', '任务-Volgo', '43112');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(@namespace_id, 'sms.default.yzx', 6, 'zh_CN', '任务2-Volgo', '43113');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(@namespace_id, 'sms.default.yzx', 7, 'zh_CN', '新报修-Volgo', '43114');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(@namespace_id, 'sms.default.yzx', 9, 'zh_CN', '看楼-Volgo', '43115');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(@namespace_id, 'sms.default.yzx', 15, 'zh_CN', '物业任务3-Volgo', '43124');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(@namespace_id, 'sms.default.yzx', 12, 'zh_CN', '预定1-Volgo', '43119');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(@namespace_id, 'sms.default.yzx', 13, 'zh_CN', '预定2-Volgo', '43120');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(@namespace_id, 'sms.default.yzx', 14, 'zh_CN', '预定3-Volgo', '43121');
	
-- SET @service_module_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
-- INSERT INTO `eh_service_module_scopes`(`id`, `module_id`,`module_name`, `namespace_id`, `apply_policy`) VALUES( @service_module_id := @service_module_id + 1,20600,'', 999988,2);


SET FOREIGN_KEY_CHECKS = 1;