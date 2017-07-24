SET FOREIGN_KEY_CHECKS = 0;
SET @namespace_id=999978;
SET @eh_core_serverURL = "http://core.zuolin.com"; -- 取具体环境连接core server的链接
SET @eh_biz_serverURL = "biz.zuolin.com"; -- 取具体环境连接core server的链接
SET @user_id = 264000;  -- 需要取现网eh_users的ID的最大值再加一定余量
SET @account_name='285585'; -- 需要取现网eh_users的acount_name的6位的最大值再加一定余量
SET @community_id = 240111044331057733; -- 需要取现网eh_communities的ID的最大值再加一定余量
SET @address_id = 239825274387252717; -- 需要取现网eh_addresses的ID的最大值再加一定余量
SET @organization_id = 1022080;  	-- 

SET @community_geopoint_id = (SELECT MAX(id) FROM `eh_community_geopoints`);  
SET @feedback_forum_id = 189410;   -- 取eh_forums的ID最大值再加一定余量
SET @community_forum_id = 189400;   -- 取eh_forums的ID最大值再加一定余量
SET @building_id = 192900;   -- 取eh_buildings的ID最大值再加一定余量

SET @region_id = 17151;  -- 需要取现网已有城市“深圳市”的ID
SET @shi_id = 17151;  -- 需要取现网已有城市“深圳市”的ID
SET @qu_id = 17151;  -- 需要取现网已有城市“龙岗区”的ID

SET @group_id = 1011478; 
SET @forum_id = 189423; 

SET @rental_res_id = 10712; 

SET @launch_pad_item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`); 
SET @organization_member_id = (SELECT MAX(id) FROM `eh_organization_members`); 
SET @role_assignment_id = (SELECT MAX(id) FROM `eh_acl_role_assignments`); 
SET @user_identifier_id = (SELECT max(id) FROM `eh_user_identifiers`);
SET @organization_address_mapping_id = (SELECT max(id) FROM `eh_organization_address_mappings`);
SET @configuration_id = (SELECT MAX(id) FROM `eh_configurations`);
SET @organization_community_request_id = (SELECT MAX(id) FROM `eh_organization_community_requests`);
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
SET @category_id = (SELECT MAX(id) FROM `eh_categories`);
SET @version_realm_id = (SELECT MAX(id) FROM `eh_version_realm`);
SET @namespace_resource_id = (SELECT MAX(id) FROM `eh_namespace_resources`);
   
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES ((@region_id := @region_id + 1), 0, '广东', 'GUANGDONG', 'GD', '/广东', 1, 1, '', '', 2, 2, @namespace_id);

SET @shi_id = @region_id + 1;
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`)
	VALUES (@shi_id, @region_id, '深圳市', 'SHENZHENSHI', 'SZS', '/广东/深圳市', 2, 2, NULL, '0755', 2, 1, @namespace_id);
	
SET @qu_id = @shi_id + 1;
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (@qu_id, @shi_id, '龙岗区', 'LONGGANGQU', 'LGQ', '/广东/深圳市/龙岗区', 3, 3, NULL, '0755', 2, 0, @namespace_id);

SET @forum_id := @forum_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES((@group_id := @group_id + 1), UUID(), '康利置地', '康利置地', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
	
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'康利置地','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());       

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'PM', '深圳康利置地有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);


INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(@community_forum_id, UUID(), @namespace_id, 2, 'EhGroups', 0,'康利置地论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(@feedback_forum_id, UUID(), @namespace_id, 2, 'EhGroups', 0,'康利置地反馈论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
UPDATE `eh_communities` SET `default_forum_id` = @community_forum_id, `feedback_forum_id` = @feedback_forum_id WHERE `id` = @pi_community_id;


INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (@user_id, UUID(), @account_name, '苏畅', '', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES ((@user_identifier_id := @user_identifier_id + 1) , @user_id ,  '0',  '13214528344',  '221616',  3, UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`)
	VALUES((@organization_member_id := @organization_member_id + 1), @organization_id, 'USER', @user_id  , 'manager', '苏畅', 0, '13214528344', 3, @namespace_id);	
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES((@role_assignment_id := @role_assignment_id + 1), 'EhOrganizations', @organization_id, 'EhUsers', @user_id  , 1001, 1, UTC_TIMESTAMP());


SET @user_id = @user_id + 1;
SET @account_name = @account_name + 1;
SET @organization_member_id = (SELECT MAX(id) FROM `eh_organization_members`);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (@user_id, UUID(), @account_name, '吴惠祺', '', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES ((@user_identifier_id := @user_identifier_id + 1) , @user_id ,  '0',  '13510600750',  '221616',  3, UTC_TIMESTAMP(), @namespace_id);
-- DELETE FROM `eh_organization_members` WHERE organization_id = @organization_id AND target_type = 'USER' AND target_id = @user_id;
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`)
	VALUES((@organization_member_id := @organization_member_id + 1), @organization_id, 'USER', @user_id  , 'manager', '吴惠祺', 0, '13510600750', 3, @namespace_id);	
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES((@role_assignment_id := @role_assignment_id + 1), 'EhOrganizations', @organization_id, 'EhUsers', @user_id  , 1001, 1, UTC_TIMESTAMP());

	
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES(@community_id, UUID(), @shi_id, '深圳市',  @qu_id, '龙岗区', '康利城', '康利城', '平吉大道66号', NULL, '',NULL, NULL, NULL, NULL, NULL, NULL,NULL, 0, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'1', @community_forum_id, @feedback_forum_id, UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES((@community_geopoint_id := @community_geopoint_id + 1), @community_id, '', 114.134323, 22.664864, 'ws10vnn06kzs');
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(@organization_id, @community_id);
INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES((@namespace_resource_id := @namespace_resource_id+ 1), @namespace_id, 'COMMUNITY', @community_id, UTC_TIMESTAMP());	
	
-- 康利大厦
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
VALUES((@building_id := @building_id + 1), @community_id, '康利城1#楼', '', 0, '0755-89961999', '广东省深圳市龙岗区平吉大道66号', 37410.17, 114.134323, 22.664864, 'ws10vnn06kzs', '', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, @namespace_id);
	
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
VALUES((@building_id := @building_id + 1), @community_id, '康利城2#楼', '', 0, '0755-89961999', '广东省深圳市龙岗区平吉大道66号', 28006.97, 114.134323, 22.664864, 'ws10vnn06kzs', '', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, @namespace_id);
	
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
VALUES((@building_id := @building_id + 1), @community_id, '康利城3#楼', '', 0, '0755-89961999', '广东省深圳市龙岗区平吉大道66号', 3109.16, 114.134323, 22.664864, 'ws10vnn06kzs', '', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, @namespace_id);
	
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
VALUES((@building_id := @building_id + 1), @community_id, '康利城5#楼', '', 0, '0755-89961999', '广东省深圳市龙岗区平吉大道66号', 13925.15, 114.134323, 22.664864, 'ws10vnn06kzs', '', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, @namespace_id);
	
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
VALUES((@building_id := @building_id + 1), @community_id, '康利城6#楼', '', 0, '0755-89961999', '广东省深圳市龙岗区平吉大道66号', 19196.2, 114.134323, 22.664864, 'ws10vnn06kzs', '', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, @namespace_id);
	
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
VALUES((@building_id := @building_id + 1), @community_id, '康利城7#楼', '', 0, '0755-89961999', '广东省深圳市龙岗区平吉大道66号', 19143.6, 114.134323, 22.664864, 'ws10vnn06kzs', '', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, @namespace_id);
	
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
VALUES((@building_id := @building_id + 1), @community_id, '康利城8#楼', '', 0, '0755-89961999', '广东省深圳市龙岗区平吉大道66号', 24013.33, 114.134323, 22.664864, 'ws10vnn06kzs', '', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, @namespace_id);


-- 康利金融大厦	
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES ((@qu_id := @qu_id + 1), 0, '福建', 'FUJIAN', 'FJ', '/福建', 1, 1, '', '', 2, 2, @namespace_id);

SET @shi_id = @qu_id + 1;
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`)
	VALUES (@shi_id, @qu_id, '厦门市', 'XIAMENSHI', 'XMS', '/福建/厦门市', 2, 2, NULL, '0592', 2, 1, @namespace_id);
	
SET @qu_id = @shi_id + 1;
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (@qu_id, @shi_id, '思明区', 'SIMINGQU', 'SMQ', '/福建/厦门市/思明区', 3, 3, NULL, '0592', 2, 0, @namespace_id);
	

SET @community_id = @community_id + 1;
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES(@community_id, UUID(), @shi_id, '厦门市',  @qu_id, '思明区', '康利金融大厦', '康利金融大厦', '厦门市思明区观音山商务运营中心宜兰路9号', NULL, '',NULL, NULL, NULL, NULL, NULL, NULL,NULL, 74, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'1', @community_forum_id, @feedback_forum_id, UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES((@community_geopoint_id := @community_geopoint_id + 1), @community_id, '', 118.197012, 24.488344, 'wsk539uqyn0j');
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(@organization_id, @community_id);
INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES((@namespace_resource_id := @namespace_resource_id+ 1), @namespace_id, 'COMMUNITY', @community_id, UTC_TIMESTAMP());

	
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
VALUES((@building_id := @building_id + 1), @community_id, '康利金融大厦', '', 0, '0755-89961999', '厦门市观音山商务运营中心宜兰路9号', 30000, 114.134323, 22.664864, 'ws10vnn06kzs', '', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, @namespace_id);


SET @organization_id = @organization_id + 1;
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0200','康利金融大厦','K0200','2','0',UTC_TIMESTAMP(), @namespace_id, 908.796);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '﻿康利金融大厦-K0200', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0300','康利金融大厦','K0300','2','0',UTC_TIMESTAMP(), @namespace_id, 1471.409);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0300', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0501','康利金融大厦','K0501','2','0',UTC_TIMESTAMP(), @namespace_id, 217.12);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0501', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0502','康利金融大厦','K0502','2','0',UTC_TIMESTAMP(), @namespace_id, 349.08);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0502', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0503','康利金融大厦','K0503','2','0',UTC_TIMESTAMP(), @namespace_id, 446.63);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0503', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0505','康利金融大厦','K0505','2','0',UTC_TIMESTAMP(), @namespace_id, 348.98);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0505', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0506','康利金融大厦','K0506','2','0',UTC_TIMESTAMP(), @namespace_id, 205.5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0506', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0601','康利金融大厦','K0601','2','0',UTC_TIMESTAMP(), @namespace_id, 217.12);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0601', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0602','康利金融大厦','K0602','2','0',UTC_TIMESTAMP(), @namespace_id, 349.08);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0602', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0603','康利金融大厦','K0603','2','0',UTC_TIMESTAMP(), @namespace_id, 446.63);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0603', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0605','康利金融大厦','K0605','2','0',UTC_TIMESTAMP(), @namespace_id, 348.98);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0605', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0606','康利金融大厦','K0606','2','0',UTC_TIMESTAMP(), @namespace_id, 205.5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0606', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0701','康利金融大厦','K0701','2','0',UTC_TIMESTAMP(), @namespace_id, 218.43);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0701', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0702','康利金融大厦','K0702','2','0',UTC_TIMESTAMP(), @namespace_id, 348.96);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0702', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0703','康利金融大厦','K0703','2','0',UTC_TIMESTAMP(), @namespace_id, 420.89);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0703', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0705','康利金融大厦','K0705','2','0',UTC_TIMESTAMP(), @namespace_id, 348.96);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0705', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0706','康利金融大厦','K0706','2','0',UTC_TIMESTAMP(), @namespace_id, 205.65);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0706', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0801','康利金融大厦','K0801','2','0',UTC_TIMESTAMP(), @namespace_id, 218.43);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0801', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0802','康利金融大厦','K0802','2','0',UTC_TIMESTAMP(), @namespace_id, 348.96);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0802', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0803','康利金融大厦','K0803','2','0',UTC_TIMESTAMP(), @namespace_id, 420.89);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0803', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0805','康利金融大厦','K0805','2','0',UTC_TIMESTAMP(), @namespace_id, 348.96);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0805', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0806','康利金融大厦','K0806','2','0',UTC_TIMESTAMP(), @namespace_id, 205.65);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0806', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0901','康利金融大厦','K0901','2','0',UTC_TIMESTAMP(), @namespace_id, 218.43);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0901', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0902','康利金融大厦','K0902','2','0',UTC_TIMESTAMP(), @namespace_id, 348.96);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0902', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0903','康利金融大厦','K0903','2','0',UTC_TIMESTAMP(), @namespace_id, 420.89);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0903', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0905','康利金融大厦','K0905','2','0',UTC_TIMESTAMP(), @namespace_id, 348.96);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0905', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K0906','康利金融大厦','K0906','2','0',UTC_TIMESTAMP(), @namespace_id, 205.65);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K0906', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1001','康利金融大厦','K1001','2','0',UTC_TIMESTAMP(), @namespace_id, 218.43);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1001', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1002','康利金融大厦','K1002','2','0',UTC_TIMESTAMP(), @namespace_id, 348.96);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1002', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1003','康利金融大厦','K1003','2','0',UTC_TIMESTAMP(), @namespace_id, 420.89);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1003', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1005','康利金融大厦','K1005','2','0',UTC_TIMESTAMP(), @namespace_id, 348.96);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1005', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1006','康利金融大厦','K1006','2','0',UTC_TIMESTAMP(), @namespace_id, 205.65);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1006', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1101','康利金融大厦','K1101','2','0',UTC_TIMESTAMP(), @namespace_id, 218.43);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1101', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1102','康利金融大厦','K1102','2','0',UTC_TIMESTAMP(), @namespace_id, 348.96);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1102', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1103','康利金融大厦','K1103','2','0',UTC_TIMESTAMP(), @namespace_id, 420.89);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1103', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1105','康利金融大厦','K1105','2','0',UTC_TIMESTAMP(), @namespace_id, 348.96);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1105', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1106','康利金融大厦','K1106','2','0',UTC_TIMESTAMP(), @namespace_id, 205.65);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1106', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1201','康利金融大厦','K1201','2','0',UTC_TIMESTAMP(), @namespace_id, 218.43);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1201', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1202','康利金融大厦','K1202','2','0',UTC_TIMESTAMP(), @namespace_id, 348.96);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1202', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1203','康利金融大厦','K1203','2','0',UTC_TIMESTAMP(), @namespace_id, 420.89);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1203', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1205','康利金融大厦','K1205','2','0',UTC_TIMESTAMP(), @namespace_id, 348.96);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1205', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1206','康利金融大厦','K1206','2','0',UTC_TIMESTAMP(), @namespace_id, 205.65);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1206', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1501','康利金融大厦','K1501','2','0',UTC_TIMESTAMP(), @namespace_id, 218.43);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1501', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1502','康利金融大厦','K1502','2','0',UTC_TIMESTAMP(), @namespace_id, 348.96);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1502', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1503','康利金融大厦','K1503','2','0',UTC_TIMESTAMP(), @namespace_id, 420.89);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1503', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1505','康利金融大厦','K1505','2','0',UTC_TIMESTAMP(), @namespace_id, 348.96);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1505', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1506','康利金融大厦','K1506','2','0',UTC_TIMESTAMP(), @namespace_id, 205.65);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1506', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1601','康利金融大厦','K1601','2','0',UTC_TIMESTAMP(), @namespace_id, 218.43);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1601', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1602','康利金融大厦','K1602','2','0',UTC_TIMESTAMP(), @namespace_id, 348.96);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1602', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1603','康利金融大厦','K1603','2','0',UTC_TIMESTAMP(), @namespace_id, 420.89);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1603', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1605','康利金融大厦','K1605','2','0',UTC_TIMESTAMP(), @namespace_id, 348.96);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1605', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1606','康利金融大厦','K1606','2','0',UTC_TIMESTAMP(), @namespace_id, 205.65);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1606', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1701','康利金融大厦','K1701','2','0',UTC_TIMESTAMP(), @namespace_id, 218.43);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1701', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1702','康利金融大厦','K1702','2','0',UTC_TIMESTAMP(), @namespace_id, 348.96);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1702', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1703','康利金融大厦','K1703','2','0',UTC_TIMESTAMP(), @namespace_id, 420.89);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1703', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1705','康利金融大厦','K1705','2','0',UTC_TIMESTAMP(), @namespace_id, 348.96);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1705', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1706','康利金融大厦','K1706','2','0',UTC_TIMESTAMP(), @namespace_id, 205.65);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1706', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1801','康利金融大厦','K1801','2','0',UTC_TIMESTAMP(), @namespace_id, 567.38);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1801', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1802','康利金融大厦','K1802','2','0',UTC_TIMESTAMP(), @namespace_id, 420.89);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1802', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1803','康利金融大厦','K1803','2','0',UTC_TIMESTAMP(), @namespace_id, 554.6);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1803', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1901','康利金融大厦','K1901','2','0',UTC_TIMESTAMP(), @namespace_id, 567.38);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1901', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1902','康利金融大厦','K1902','2','0',UTC_TIMESTAMP(), @namespace_id, 420.89);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1902', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K1903','康利金融大厦','K1903','2','0',UTC_TIMESTAMP(), @namespace_id, 554.6);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K1903', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K2000','康利金融大厦','K2000','2','0',UTC_TIMESTAMP(), @namespace_id, 1542.87);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K2000', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K2101','康利金融大厦','K2101','2','0',UTC_TIMESTAMP(), @namespace_id, 520);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K2101', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K2102','康利金融大厦','K2102','2','0',UTC_TIMESTAMP(), @namespace_id, 420.89);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K2102', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K2103','康利金融大厦','K2103','2','0',UTC_TIMESTAMP(), @namespace_id, 554.89);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K2103', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K2200','康利金融大厦','K2200','2','0',UTC_TIMESTAMP(), @namespace_id, 1542.87);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K2200', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K2300','康利金融大厦','K2300','2','0',UTC_TIMESTAMP(), @namespace_id, 1542.87);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K2300', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K2501','康利金融大厦','K2501','2','0',UTC_TIMESTAMP(), @namespace_id, 180.44);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K2501', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K2502','康利金融大厦','K2502','2','0',UTC_TIMESTAMP(), @namespace_id, 350.12);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K2502', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K2503','康利金融大厦','K2503','2','0',UTC_TIMESTAMP(), @namespace_id, 334.81);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K2503', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K2505','康利金融大厦','K2505','2','0',UTC_TIMESTAMP(), @namespace_id, 350.08);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K2505', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '厦门市', @qu_id, '思明区' ,'康利金融大厦-K2506','康利金融大厦','K2506','2','0',UTC_TIMESTAMP(), @namespace_id, 169.41);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mapping_id := @organization_address_mapping_id + 1), @organization_id, @community_id, @address_id, '康利金融大厦-K2506', '0');


INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ((@configuration_id := @configuration_id + 1), 'app.agreements.url', CONCAT(@eh_core_serverURL, '/mobile/static/app_agreements/agreements.html?ns=999978'), 'the relative path for kangli app agreements', @namespace_id, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ((@configuration_id := @configuration_id + 1), 'business.url', CONCAT('https://',@eh_biz_serverURL,'/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2F', @eh_biz_serverURL, '%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix'), 'bussiness url', @namespace_id, '');


	
INSERT INTO `eh_version_realm` VALUES ((@version_realm_id := @version_realm_id + 1), 'Android_Kangli', null, UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_version_realm` VALUES ((@version_realm_id := @version_realm_id + 1), 'iOS_Kangli', null, UTC_TIMESTAMP(), @namespace_id);
	

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 10000,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 10100,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 10400,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 10200,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 10600,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 10750,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 10751,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 10752,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 20000,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 20100,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 20140,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 20150,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 20155,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 20170,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 20180,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 20158,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 20190,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 20191,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 20192,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 20800,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 20810,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 20811,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 20812,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 20820,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 20821,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 20822,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 20830,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 20831,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 20840,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 20841,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40000,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40400,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40410,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40420,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40430,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40440,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40450,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40500,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40510,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40520,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40530,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40540,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40541,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40542,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 41300,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 41310,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40800,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40810,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40830,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40840,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40850,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 41000,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 41010,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 41020,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 41030,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 41040,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 41050,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 41060,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 30000,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 30500,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 31000,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 32000,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 33000,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 34000,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50000,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50100,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50110,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50200,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50210,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50220,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50300,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50400,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50500,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50600,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50630,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50631,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50632,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50633,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50640,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50650,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50651,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50652,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50653,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 56161,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50700,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50710,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50720,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50730,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50800,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50810,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50820,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50830,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50840,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50850,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50860,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50900,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 60000,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 60100,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 60200,'', 'EhNamespaces', @namespace_id,2);


SET @launch_pad_layout_id = (SELECT MAX(id) FROM `eh_launch_pad_layouts`);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
VALUES ((@launch_pad_layout_id := @launch_pad_layout_id + 1), @namespace_id, 'ServiceMarketLayout', '{"displayName":"服务市场","groups":[{"defaultOrder":1,"groupName":"","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"style":"Default","widget":"Banners"},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Bull"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs1"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":16,"columnCount":4},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs2"},"style":"Gallery","defaultOrder":4,"separatorFlag":1,"separatorHeight":16,"columnCount":2}],"layoutName":"ServiceMarketLayout","versionCode":"2017042516","versionName":"4.4.3"}', 2017042516, 0, 2, '2016-03-12 19:16:25', 'pm_admin', 0, 0, 0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
VALUES ((@launch_pad_layout_id := @launch_pad_layout_id + 1), @namespace_id, 'ServiceMarketLayout', '{"displayName":"服务市场","groups":[{"defaultOrder":1,"groupName":"","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"style":"Default","widget":"Banners"},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Bull"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs1"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":16,"columnCount":4},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs2"},"style":"Gallery","defaultOrder":4,"separatorFlag":1,"separatorHeight":16,"columnCount":2}],"layoutName":"ServiceMarketLayout","versionCode":"2017042516","versionName":"4.4.3"}', 2017042516, 0, 2, '2016-03-12 19:16:25', 'park_tourist', 0, 0, 0);

INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
VALUES ((@launch_pad_layout_id := @launch_pad_layout_id + 1), @namespace_id, 'ResApplyLayout', '{"displayName":"资源申请","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":16,"columnCount":4}],"layoutName":"ResApplyLayout","versionCode":"2017042516","versionName":"4.4.3"}', 2017042516, 0, 2, '2016-03-12 19:16:25', 'park_tourist', 0, 0, 0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
VALUES ((@launch_pad_layout_id := @launch_pad_layout_id + 1), @namespace_id, 'ResApplyLayout', '{"displayName":"资源申请","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":16,"columnCount":4}],"layoutName":"ResApplyLayout","versionCode":"2017042516","versionName":"4.4.3"}', 2017042516, 0, 2, '2016-03-12 19:16:25', 'pm_admin', 0, 0, 0);

INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
VALUES ((@launch_pad_layout_id := @launch_pad_layout_id + 1), @namespace_id, 'IntelPmLayout', '{"displayName":"智慧物业","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":16,"columnCount":4}],"layoutName":"IntelPmLayout","versionCode":"2017042516","versionName":"4.4.3"}', 2017042516, 0, 2, '2016-03-12 19:16:25', 'park_tourist', 0, 0, 0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
VALUES ((@launch_pad_layout_id := @launch_pad_layout_id + 1), @namespace_id, 'IntelPmLayout', '{"displayName":"智慧物业","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":16,"columnCount":4}],"layoutName":"IntelPmLayout","versionCode":"2017042516","versionName":"4.4.3"}', 2017042516, 0, 2, '2016-03-12 19:16:25', 'pm_admin', 0, 0, 0);

INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
VALUES ((@launch_pad_layout_id := @launch_pad_layout_id + 1), @namespace_id, 'IntelTransLayout', '{"displayName":"智慧交通","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":16,"columnCount":4}],"layoutName":"IntelTransLayout","versionCode":"2017042516","versionName":"4.4.3"}', 2017042516, 0, 2, '2016-03-12 19:16:25', 'park_tourist', 0, 0, 0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
VALUES ((@launch_pad_layout_id := @launch_pad_layout_id + 1), @namespace_id, 'IntelTransLayout', '{"displayName":"智慧交通","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":16,"columnCount":4}],"layoutName":"IntelTransLayout","versionCode":"2017042516","versionName":"4.4.3"}', 2017042516, 0, 2, '2016-03-12 19:16:25', 'pm_admin', 0, 0, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs1', 'CONTACTS', '企业通讯录', 'cs://1/image/aW1hZ2UvTVRvMlptTTVPV1UzTW1VeE9HWTROV05qTVRFMFpqUm1PV1EwT1dZNVlqVTNNZw', 1, 1, 46, '', 3, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs1', 'CONTACTS', '企业通讯录', 'cs://1/image/aW1hZ2UvTVRvMlptTTVPV1UzTW1VeE9HWTROV05qTVRFMFpqUm1PV1EwT1dZNVlqVTNNZw', 1, 1, 46, '', 3, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs1', '3D', '园区3D图', 'cs://1/image/aW1hZ2UvTVRveE9XRXpZVEkzWVRnMll6bGhOR1ppTmpVek5tUTNNek16WmpVek9HRTRaQQ', 1, 1, 13, '{"url":"http://i.eqxiu.com/s/TOy5cR09?eqrcode=1"}', 4, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs1', '3D', '园区3D图', 'cs://1/image/aW1hZ2UvTVRveE9XRXpZVEkzWVRnMll6bGhOR1ppTmpVek5tUTNNek16WmpVek9HRTRaQQ', 1, 1, 13,'{"url":"http://i.eqxiu.com/s/TOy5cR09?eqrcode=1"}', 4, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs1', 'K-CLUB', 'K-CLUB', 'cs://1/image/aW1hZ2UvTVRvME16WmlaVGM0WkRJeE56QTJObU0yWWpZNU1qazFNMkZpT1dSalkyWXpaUQ', 1, 1, 14, '{"url":"http://klzd.m.yunhuiyuan.cn/Business/BindCard?bid=911f094c-219f-4d38-81ac-a0564fc95d58"}', 5, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs1', 'K-CLUB', 'K-CLUB', 'cs://1/image/aW1hZ2UvTVRvME16WmlaVGM0WkRJeE56QTJObU0yWWpZNU1qazFNMkZpT1dSalkyWXpaUQ', 1, 1, 14, '{"url":"http://klzd.m.yunhuiyuan.cn/Business/BindCard?bid=911f094c-219f-4d38-81ac-a0564fc95d58"}', 5, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs1', 'FORUM', '园区论坛', 'cs://1/image/aW1hZ2UvTVRveE1qWXhaamxsTVRNM016WXlNR1EzTkRCbU0yUXlOelZtTmpjNFkyUmhOdw', 1, 1, 62, '', 6, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs1', 'FORUM', '园区论坛', 'cs://1/image/aW1hZ2UvTVRveE1qWXhaamxsTVRNM016WXlNR1EzTkRCbU0yUXlOelZtTmpjNFkyUmhOdw', 1, 1, 62, '', 6, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs1', 'ORG_COM', '企业交流', 'cs://1/image/aW1hZ2UvTVRwbFpqbGxNVFZoWldZeU5tSTJNalUwT1RrMFpHUm1NVEppWVRnME9ETXlOdw', 1, 1, 36, '{"privateFlag": 0}', 7, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs1', 'ORG_COM', '企业交流', 'cs://1/image/aW1hZ2UvTVRwbFpqbGxNVFZoWldZeU5tSTJNalUwT1RrMFpHUm1NVEppWVRnME9ETXlOdw', 1, 1, 36, '{"privateFlag": 0}', 7, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs1', 'VIDEO_CONF', '视频会议', 'cs://1/image/aW1hZ2UvTVRvNU1UY3hOelkxWmpJeU5qazBZamxtTXpabFlXUmhOemt5WlRnME5qWTBPQQ', 1, 1, 27, '', 8, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs1', 'VIDEO_CONF', '视频会议', 'cs://1/image/aW1hZ2UvTVRvNU1UY3hOelkxWmpJeU5qazBZamxtTXpabFlXUmhOemt5WlRnME5qWTBPQQ', 1, 1, 27, '', 8, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs1', 'MORE', '更多', 'cs://1/image/aW1hZ2UvTVRveU9EQXlOR1pqTTJVME1UVXdOekEwTVRBeVlXSXdNRFpsTjJVNU5qRmxNdw', 1, 1, 1, '{"itemLocation":"/home","itemGroup":"More"}', 100, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs1', 'MORE', '更多', 'cs://1/image/aW1hZ2UvTVRveU9EQXlOR1pqTTJVME1UVXdOekEwTVRBeVlXSXdNRFpsTjJVNU5qRmxNdw', 1, 1, 1, '{"itemLocation":"/home","itemGroup":"More"}', 100, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'More', 'PUNCH', '打卡考勤', 'cs://1/image/aW1hZ2UvTVRvMFl6SmpNbU0wTW1FME9UTmxPV1kxT1RBeVpEbGhNRFEwWlRZNU1tUmpNZw', 1, 1, 23, '', 0, 0, 1, 0, NULL, 0, NULL, NULL, NULL, 3, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'More', 'PUNCH', '打卡考勤', 'cs://1/image/aW1hZ2UvTVRvMFl6SmpNbU0wTW1FME9UTmxPV1kxT1RBeVpEbGhNRFEwWlRZNU1tUmpNZw', 1, 1, 23, '', 0, 0, 1, 0, NULL, 0, NULL, NULL, NULL, 3, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs2', 'RES_APPLY', '资源申请', 'cs://1/image/aW1hZ2UvTVRvNVpqZzJZbU5oTkRVMk1EaGtaRE0xWXpkaFpEZ3lOakkyTmpJMllXSTVOZw', 1, 1, 2, '{"itemLocation":"/home/ResApply","layoutName":"ResApplyLayout","title":"资源申请"}', 3, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs2', 'RES_APPLY', '资源申请', 'cs://1/image/aW1hZ2UvTVRvNVpqZzJZbU5oTkRVMk1EaGtaRE0xWXpkaFpEZ3lOakkyTmpJMllXSTVOZw', 1, 1, 2, '{"itemLocation":"/home/ResApply","layoutName":"ResApplyLayout","title":"资源申请"}', 3, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs2', 'INCR_SER', '增值服务', 'cs://1/image/aW1hZ2UvTVRwbU0yWmtZelkxTm1ZNU1qRXdNelkwWXpBelptWXhZV0l6T1RjNFlXWXlPUQ', 1, 1, 33, '{"type":200979,"parentId":200979,"displayType": "grid"}', 4, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs2', 'INCR_SER', '增值服务', 'cs://1/image/aW1hZ2UvTVRwbU0yWmtZelkxTm1ZNU1qRXdNelkwWXpBelptWXhZV0l6T1RjNFlXWXlPUQ', 1, 1, 33, '{"type":200979,"parentId":200979,"displayType": "grid"}', 4, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs2', 'INTEL_PM', '智慧物业', 'cs://1/image/aW1hZ2UvTVRwak1UVXlNekl6TVdKak9XRmpZbU14T0dVM01EbG1NamxqWmpKaE5qWmtaUQ', 1, 1, 2, '{"itemLocation":"/home/IntelPm","layoutName":"IntelPmLayout","title":"智慧物业"}', 5, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs2', 'INTEL_PM', '智慧物业', 'cs://1/image/aW1hZ2UvTVRwak1UVXlNekl6TVdKak9XRmpZbU14T0dVM01EbG1NamxqWmpKaE5qWmtaUQ', 1, 1, 2, '{"itemLocation":"/home/IntelPm","layoutName":"IntelPmLayout","title":"智慧物业"}', 5, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs2', 'INTEL_TRANS', '智慧交通', 'cs://1/image/aW1hZ2UvTVRveFlqQXpPREZrWW1Nek5EVXdaV1l3TWpBNU4yVmxaalpsTWpFMk5EVmtaQQ', 1, 1, 2, '{"itemLocation":"/home/IntelTrans","layoutName":"IntelTransLayout","title":"智慧交通"}', 6, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs2', 'INTEL_TRANS', '智慧交通', 'cs://1/image/aW1hZ2UvTVRveFlqQXpPREZrWW1Nek5EVXdaV1l3TWpBNU4yVmxaalpsTWpFMk5EVmtaQQ', 1, 1, 2, '{"itemLocation":"/home/IntelTrans","layoutName":"IntelTransLayout","title":"智慧交通"}', 6, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);


INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) 
 VALUES((@rental_res_id := @rental_res_id + 1), '会议室预约', 0, NULL, 0, @namespace_id);
 
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/ResApply', 'Bizs', 'METTING_APPLY', '会议室预约', 'cs://1/image/aW1hZ2UvTVRvNFpHUTFNRFl5WVdFMk9UWTNNall4WWpobE9HSmlaVFU1T1RNNE56SXhOQQ', 1, 1, 49, CONCAT('{"resourceTypeId":', @rental_res_id, ',"pageType":0}'), 1, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/ResApply', 'Bizs', 'METTING_APPLY', '会议室预约', 'cs://1/image/aW1hZ2UvTVRvNFpHUTFNRFl5WVdFMk9UWTNNall4WWpobE9HSmlaVFU1T1RNNE56SXhOQQ', 1, 1, 49, CONCAT('{"resourceTypeId":', @rental_res_id, ',"pageType":0}'), 1, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) 
 VALUES((@rental_res_id := @rental_res_id + 1), '电子屏预约', 0, NULL, 0, @namespace_id);
 
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/ResApply', 'Bizs', 'LED_APPLY', '电子屏预约', 'cs://1/image/aW1hZ2UvTVRwaU5HRmxaRFZtWWpkbVltTm1ZV1kzTUdRd1pUaG1NamczTkdNelltWXhNQQ', 1, 1, 49, CONCAT('{"resourceTypeId":', @rental_res_id, ',"pageType":0}'), 2, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/ResApply', 'Bizs', 'LED_APPLY', '电子屏预约', 'cs://1/image/aW1hZ2UvTVRwaU5HRmxaRFZtWWpkbVltTm1ZV1kzTUdRd1pUaG1NamczTkdNelltWXhNQQ', 1, 1, 49, CONCAT('{"resourceTypeId":', @rental_res_id, ',"pageType":0}'), 2, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) 
 VALUES((@rental_res_id := @rental_res_id + 1), '会所订房', 0, NULL, 0, @namespace_id);
 
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/ResApply', 'Bizs', 'ROOM_APPLY', '会所订房', 'cs://1/image/aW1hZ2UvTVRvM01tWXlOR1F4WVRJek1tSm1Nek0zT1Rkak9UQm1OemsyWlRCbE9XWXlaZw', 1, 1, 49, CONCAT('{"resourceTypeId":', @rental_res_id, ',"pageType":0}'), 3, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/ResApply', 'Bizs', 'ROOM_APPLY', '会所订房', 'cs://1/image/aW1hZ2UvTVRvM01tWXlOR1F4WVRJek1tSm1Nek0zT1Rkak9UQm1OemsyWlRCbE9XWXlaZw', 1, 1, 49, CONCAT('{"resourceTypeId":', @rental_res_id, ',"pageType":0}'), 3, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) 
 VALUES((@rental_res_id := @rental_res_id + 1), '餐厅定位', 0, NULL, 0, @namespace_id);
 
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/ResApply', 'Bizs', 'REST_APPLY', '餐厅定位', 'cs://1/image/aW1hZ2UvTVRvd01EYzFNbU13TkRnNE9XVXhOalU0TURnNE5HRm1aakV3Tm1ZeE1EZGxNdw', 1, 1, 49, CONCAT('{"resourceTypeId":', @rental_res_id, ',"pageType":0}'), 4, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/ResApply', 'Bizs', 'REST_APPLY', '餐厅定位', 'cs://1/image/aW1hZ2UvTVRvd01EYzFNbU13TkRnNE9XVXhOalU0TURnNE5HRm1aakV3Tm1ZeE1EZGxNdw', 1, 1, 49, CONCAT('{"resourceTypeId":', @rental_res_id, ',"pageType":0}'), 4, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) 
 VALUES((@rental_res_id := @rental_res_id + 1), '产品发布厅', 0, NULL, 0, @namespace_id);
 
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/ResApply', 'Bizs', 'PRODUCT_APPLY', '产品发布厅', 'cs://1/image/aW1hZ2UvTVRvd05XRmlNMk0zTW1ObFlqUTRZbU13WVRsak1XVTNabU16Wm1WbFpqQmhaQQ', 1, 1, 49, CONCAT('{"resourceTypeId":', @rental_res_id, ',"pageType":0}'), 5, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/ResApply', 'Bizs', 'PRODUCT_APPLY', '产品发布厅', 'cs://1/image/aW1hZ2UvTVRvd05XRmlNMk0zTW1ObFlqUTRZbU13WVRsak1XVTNabU16Wm1WbFpqQmhaQQ', 1, 1, 49, CONCAT('{"resourceTypeId":', @rental_res_id, ',"pageType":0}'), 5, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) 
 VALUES((@rental_res_id := @rental_res_id + 1), 'VIP车位', 0, NULL, 0, @namespace_id);
 
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/ResApply', 'Bizs', 'VIP_APPLY', 'VIP车位', 'cs://1/image/aW1hZ2UvTVRwbFl6azBNVFEwTWpnelpUQmhZakkzWldRMU9UZGxNVGhoWkRkalpESTBNUQ', 1, 1, 49, CONCAT('{"resourceTypeId":', @rental_res_id, ',"pageType":0}'), 6, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/ResApply', 'Bizs', 'VIP_APPLY', 'VIP车位', 'cs://1/image/aW1hZ2UvTVRwbFl6azBNVFEwTWpnelpUQmhZakkzWldRMU9UZGxNVGhoWkRkalpESTBNUQ', 1, 1, 49, CONCAT('{"resourceTypeId":', @rental_res_id, ',"pageType":0}'), 6, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/IntelPm', 'Bizs', 'PM_SERVICE', '物业服务', 'cs://1/image/aW1hZ2UvTVRwak56VTNNbVEzWkRnMk5qTXlNak0wWWpoaU9EazNPVE5sTjJVeE9HRTVNdw', 1, 1, 60, '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=物业报修"}', 1, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/IntelPm', 'Bizs', 'PM_SERVICE', '物业服务', 'cs://1/image/aW1hZ2UvTVRwak56VTNNbVEzWkRnMk5qTXlNak0wWWpoaU9EazNPVE5sTjJVeE9HRTVNdw', 1, 1, 60, '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=物业报修"}', 1, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/IntelPm', 'Bizs', 'PMB_SERVICE', '物业费查询', 'cs://1/image/aW1hZ2UvTVRvd1lUaGtPRFkzWmpNell6RmxOR1U0Wmpaak56QmxPREJqWlRKbE5XSm1Zdw', 1, 1, 13, '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', 2, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/IntelPm', 'Bizs', 'PMB_SERVICE', '物业费查询', 'cs://1/image/aW1hZ2UvTVRvd1lUaGtPRFkzWmpNell6RmxOR1U0Wmpaak56QmxPREJqWlRKbE5XSm1Zdw', 1, 1, 13, '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', 2, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/IntelPm', 'Bizs', 'DOOR', '门禁', 'cs://1/image/aW1hZ2UvTVRvek56TTVNbVF5WkdJME4yWmlOR0ZpWVdNeE5HSm1ZMkkyWXpka01EWm1Odw', 1, 1, 40, '{"isSupportQR":1,"isSupportSmart":0}', 3, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/IntelPm', 'Bizs', 'DOOR', '门禁', 'cs://1/image/aW1hZ2UvTVRvek56TTVNbVF5WkdJME4yWmlOR0ZpWVdNeE5HSm1ZMkkyWXpka01EWm1Odw', 1, 1, 40, '{"isSupportQR":1,"isSupportSmart":0}', 3, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/IntelPm', 'Bizs', 'DEVICE_CHECK', '设备巡检', 'cs://1/image/aW1hZ2UvTVRvM1pEZ3daak15TWprNVltWTNOVE14T1dKaE1XUTFOV1JsWW1JM1lqTmtNdw', 1, 1, 14, CONCAT('{"url":"',@eh_core_serverURL,'/equipment-inspection/dist/index.html?hideNavigationBar=1#sign_suffix"}'), 4, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/IntelPm', 'Bizs', 'DEVICE_CHECK', '设备巡检', 'cs://1/image/aW1hZ2UvTVRvM1pEZ3daak15TWprNVltWTNOVE14T1dKaE1XUTFOV1JsWW1JM1lqTmtNdw', 1, 1, 14, CONCAT('{"url":"',@eh_core_serverURL,'/equipment-inspection/dist/index.html?hideNavigationBar=1#sign_suffix"}'), 4, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/IntelTrans', 'Bizs', 'PARKING_RECHARGE', '停车缴费', 'cs://1/image/aW1hZ2UvTVRvNU5qWm1aak5sWXpKallXWTVOVEUxWkRObFkyUm1OMlU0T1dFd05URTVNZw', 1, 1, 30, '', 1, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/IntelTrans', 'Bizs', 'PARKING_RECHARGE', '停车缴费', 'cs://1/image/aW1hZ2UvTVRvNU5qWm1aak5sWXpKallXWTVOVEUxWkRObFkyUm1OMlU0T1dFd05URTVNZw', 1, 1, 30, '', 1, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/IntelTrans', 'Bizs', 'DUDU_BUS', '嘟嘟巴士', 'cs://1/image/aW1hZ2UvTVRvM1lXUXdPV1kwT1RaaFl6VTNZall6TldRd1pEVTJaamRsT0RVek1UWXpOQQ', 1, 1, 14, '{"url":"http://wx.dudubashi.com/index.php/zuolin/Webauth/auth_login"}', 2, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/IntelTrans', 'Bizs', 'DUDU_BUS', '嘟嘟巴士', 'cs://1/image/aW1hZ2UvTVRvM1lXUXdPV1kwT1RaaFl6VTNZall6TldRd1pEVTJaamRsT0RVek1UWXpOQQ', 1, 1, 14, '{"url":"http://wx.dudubashi.com/index.php/zuolin/Webauth/auth_login"}', 2, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/IntelTrans', 'Bizs', 'PARKING_RECHARGE', '联城共享', 'cs://1/image/aW1hZ2UvTVRveVpEQTRabVJsTkRFMFlUSXlOV016Wm1VeE5qTXlPREV3TmpFMFpXWmxZUQ', 1, 1, 13, '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', 3, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home/IntelTrans', 'Bizs', 'PARKING_RECHARGE', '联城共享', 'cs://1/image/aW1hZ2UvTVRveVpEQTRabVJsTkRFMFlUSXlOV016Wm1VeE5qTXlPREV3TmpFMFpXWmxZUQ', 1, 1, 13, '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', 3, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);



INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES((@category_id := @category_id + 1), 1, 0, '普通', '帖子/普通', 1, 2, UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES((@category_id := @category_id + 1), 1, 0, '二手和租售', '帖子/二手和租售', 1, 2, UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES((@category_id := @category_id + 1), 1, 0, '免费物品', '帖子/免费物品', 1, 2, UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES((@category_id := @category_id + 1), 1, 0, '失物招领', '帖子/失物招领', 1, 2, UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES((@category_id := @category_id + 1), 1, 0, '紧急通知', '帖子/紧急通知', 1, 2, UTC_TIMESTAMP(), @namespace_id); 
	
	
SET @service_alliance_category_id = (SELECT MAX(id) FROM `eh_service_alliance_categories`);
SET @parent_id = @service_alliance_category_id + 1;
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES (@parent_id, 'community', @community_id, '0', '服务联盟', '服务联盟', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, @namespace_id, '');

UPDATE `eh_launch_pad_items` SET `action_data`=CONCAT('{"type":',@paren_id,'"parentId":',@paren_id,',"displayType": "grid"}') WHERE `namespace_id`=@namespace_id AND `item_group`='Bizs2' AND `item_name`='INCR_SER';
	
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ((@service_alliance_category_id := @service_alliance_category_id +1), 'community', @community_id, @parent_id, '律所', '律所', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, @namespace_id, '');
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ((@service_alliance_category_id := @service_alliance_category_id +1), 'community', @community_id, @parent_id, '工银服务', '工银服务', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, @namespace_id, '');
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ((@service_alliance_category_id := @service_alliance_category_id +1), 'community', @community_id, @parent_id, '人事', '人事', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, @namespace_id, '');
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ((@service_alliance_category_id := @service_alliance_category_id +1), 'community', @community_id, @parent_id, '健康', '健康', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, @namespace_id, '');
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ((@service_alliance_category_id := @service_alliance_category_id +1), 'community', @community_id, @parent_id, '知识产权', '知识产权', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, @namespace_id, '');
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ((@service_alliance_category_id := @service_alliance_category_id +1), 'community', @community_id, @parent_id, '财务税务', '财务税务', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, @namespace_id, '');

SET @locale_template_id = (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates`(`id`, `namespace_id`, `scope`, `code`,`locale`, `description`, `text`) 
	VALUES((@locale_template_id := @locale_template_id + 1), @namespace_id, 'sms.default.yzx', 1, 'zh_CN', '验证码-康利', '43070');

	
SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门蓝色海湾投资管理有限公司', '厦门蓝色海湾投资管理有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门蓝色海湾投资管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门蓝色海湾投资管理有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门和体投资有限公司', '厦门和体投资有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门和体投资有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门和体投资有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门欢乐逛科技股份有限公司', '厦门欢乐逛科技股份有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门欢乐逛科技股份有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门欢乐逛科技股份有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门欢乐逛科技股份有限公司', '厦门欢乐逛科技股份有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门欢乐逛科技股份有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门欢乐逛科技股份有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门欢乐逛科技股份有限公司', '厦门欢乐逛科技股份有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门欢乐逛科技股份有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门欢乐逛科技股份有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '福建经发招标代理有限公司', '福建经发招标代理有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'福建经发招标代理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '福建经发招标代理有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '福建经发招标代理有限公司', '福建经发招标代理有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'福建经发招标代理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '福建经发招标代理有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '中国电信股份有限公司厦门分公司', '中国电信股份有限公司厦门分公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'中国电信股份有限公司厦门分公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '中国电信股份有限公司厦门分公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门永盛金贸易有限公司', '厦门永盛金贸易有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门永盛金贸易有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门永盛金贸易有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门星汇财富资产管理有限公司', '厦门星汇财富资产管理有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门星汇财富资产管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门星汇财富资产管理有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '张建生', '张建生', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'张建生','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '张建生', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '福建瀚玮建设发展有限公司', '福建瀚玮建设发展有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'福建瀚玮建设发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '福建瀚玮建设发展有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '易点生活电子商务有限公司', '易点生活电子商务有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'易点生活电子商务有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '易点生活电子商务有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '正国香（厦门）茶叶有限公司', '正国香（厦门）茶叶有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'正国香（厦门）茶叶有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '正国香（厦门）茶叶有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门天意置业有限公司', '厦门天意置业有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门天意置业有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门天意置业有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门意外境界文化传播有限公司', '厦门意外境界文化传播有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门意外境界文化传播有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门意外境界文化传播有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门意外境界文化传播有限公司', '厦门意外境界文化传播有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门意外境界文化传播有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门意外境界文化传播有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '福建青旅海西投资管理有限公司', '福建青旅海西投资管理有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'福建青旅海西投资管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '福建青旅海西投资管理有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '福建青旅海西投资管理有限公司', '福建青旅海西投资管理有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'福建青旅海西投资管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '福建青旅海西投资管理有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门悍博体育用品有限公司', '厦门悍博体育用品有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门悍博体育用品有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门悍博体育用品有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门悍博体育用品有限公司', '厦门悍博体育用品有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门悍博体育用品有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门悍博体育用品有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门悍博体育用品有限公司', '厦门悍博体育用品有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门悍博体育用品有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门悍博体育用品有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门创惠工场科技有限公司', '厦门创惠工场科技有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门创惠工场科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门创惠工场科技有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '入夜（厦门）文化传媒有限公司', '入夜（厦门）文化传媒有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'入夜（厦门）文化传媒有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '入夜（厦门）文化传媒有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门百胜星联科技有限公司', '厦门百胜星联科技有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门百胜星联科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门百胜星联科技有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门金伦实业有限公司', '厦门金伦实业有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门金伦实业有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门金伦实业有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门邦畅贸易有限公司', '厦门邦畅贸易有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门邦畅贸易有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门邦畅贸易有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门中领精准医学产业发展有限公司', '厦门中领精准医学产业发展有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门中领精准医学产业发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门中领精准医学产业发展有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门中领精准医学产业发展有限公司', '厦门中领精准医学产业发展有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门中领精准医学产业发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门中领精准医学产业发展有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门中领精准医学产业发展有限公司', '厦门中领精准医学产业发展有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门中领精准医学产业发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门中领精准医学产业发展有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门中领精准医学产业发展有限公司', '厦门中领精准医学产业发展有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门中领精准医学产业发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门中领精准医学产业发展有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门中领精准医学产业发展有限公司', '厦门中领精准医学产业发展有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门中领精准医学产业发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门中领精准医学产业发展有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '程勇', '程勇', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'程勇','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '程勇', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '程勇', '程勇', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'程勇','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '程勇', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '程勇', '程勇', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'程勇','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '程勇', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '程勇', '程勇', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'程勇','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '程勇', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门蚌壳软件科技有限公司', '厦门蚌壳软件科技有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门蚌壳软件科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门蚌壳软件科技有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门焰翎网络科技有限公司', '厦门焰翎网络科技有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门焰翎网络科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门焰翎网络科技有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '福建欧曼蒂网络科技有限公司', '福建欧曼蒂网络科技有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'福建欧曼蒂网络科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '福建欧曼蒂网络科技有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门裕汇铭实业有限公司', '厦门裕汇铭实业有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门裕汇铭实业有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门裕汇铭实业有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门融汇恒通投资管理有限公司', '厦门融汇恒通投资管理有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门融汇恒通投资管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门融汇恒通投资管理有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '融泽（厦门）石化有限公司', '融泽（厦门）石化有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'融泽（厦门）石化有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '融泽（厦门）石化有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门维信创联信息科技有限公司', '厦门维信创联信息科技有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门维信创联信息科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门维信创联信息科技有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '深圳市洪涛装饰股份有限公司', '深圳市洪涛装饰股份有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'深圳市洪涛装饰股份有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '深圳市洪涛装饰股份有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '中粮期货有限公司厦门分公司', '中粮期货有限公司厦门分公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'中粮期货有限公司厦门分公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '中粮期货有限公司厦门分公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门大邦易众贸易有限公司', '厦门大邦易众贸易有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门大邦易众贸易有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门大邦易众贸易有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门大邦易众贸易有限公司', '厦门大邦易众贸易有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门大邦易众贸易有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门大邦易众贸易有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门翼康科技有限公司', '厦门翼康科技有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门翼康科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门翼康科技有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门翼康科技有限公司', '厦门翼康科技有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门翼康科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门翼康科技有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门鑫益至资产管理有限公司', '厦门鑫益至资产管理有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门鑫益至资产管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门鑫益至资产管理有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '山东云创蓝缘珠宝有限公司', '山东云创蓝缘珠宝有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'山东云创蓝缘珠宝有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '山东云创蓝缘珠宝有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '山东云创蓝缘珠宝有限公司', '山东云创蓝缘珠宝有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'山东云创蓝缘珠宝有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '山东云创蓝缘珠宝有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门市晋烨科技有限公司', '厦门市晋烨科技有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门市晋烨科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门市晋烨科技有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门市晋烨科技有限公司', '厦门市晋烨科技有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门市晋烨科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门市晋烨科技有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门市创敏资产管理有限公司', '厦门市创敏资产管理有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门市创敏资产管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门市创敏资产管理有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门融汇恒通投资管理有限公司', '厦门融汇恒通投资管理有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门融汇恒通投资管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门融汇恒通投资管理有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门融汇恒通投资管理有限公司', '厦门融汇恒通投资管理有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门融汇恒通投资管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门融汇恒通投资管理有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '石磊', '石磊', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'石磊','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '石磊', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门世纪万晟投资管理有限公司', '厦门世纪万晟投资管理有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门世纪万晟投资管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门世纪万晟投资管理有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '深圳市茂华装饰工程有限公司厦门分公司', '深圳市茂华装饰工程有限公司厦门分公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'深圳市茂华装饰工程有限公司厦门分公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '深圳市茂华装饰工程有限公司厦门分公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门蓝图创金投资有限公司', '厦门蓝图创金投资有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门蓝图创金投资有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门蓝图创金投资有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '福建瑶池集庆集团有限公司', '福建瑶池集庆集团有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'福建瑶池集庆集团有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '福建瑶池集庆集团有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '福建汇恒旺医药有限公司', '福建汇恒旺医药有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'福建汇恒旺医药有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '福建汇恒旺医药有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '越洋（中国）商业发展有限公司', '越洋（中国）商业发展有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'越洋（中国）商业发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '越洋（中国）商业发展有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '福建蟠桃会有限公司', '福建蟠桃会有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'福建蟠桃会有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '福建蟠桃会有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '福建瑶梦文化传播有限公司', '福建瑶梦文化传播有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'福建瑶梦文化传播有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '福建瑶梦文化传播有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门瑶梦慈善基金会', '厦门瑶梦慈善基金会', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门瑶梦慈善基金会','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门瑶梦慈善基金会', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门瑶梦慈善基金会', '厦门瑶梦慈善基金会', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门瑶梦慈善基金会','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门瑶梦慈善基金会', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门瑶梦慈善基金会', '厦门瑶梦慈善基金会', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门瑶梦慈善基金会','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门瑶梦慈善基金会', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门瑶梦慈善基金会', '厦门瑶梦慈善基金会', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门瑶梦慈善基金会','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门瑶梦慈善基金会', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

SET @forum_id := @forum_id + 1;
SET @organization_id := @organization_id + 1;
SET @group_id := @group_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(@group_id , UUID(), '厦门瑶梦慈善基金会', '厦门瑶梦慈善基金会', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'厦门瑶梦慈善基金会','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'ENTERPRISE', '厦门瑶梦慈善基金会', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 


DELETE FROM `eh_launch_pad_items` WHERE `namespace_id`=999978 AND `item_group`='Bizs1' AND `item_name`='FORUM';

UPDATE `eh_launch_pad_items` SET `display_flag`=1, `default_order`=20, `item_group`='Bizs1' WHERE `namespace_id`=999978 AND `item_group`='More';

SET @eh_core_serverURL = "https://core.zuolin.com"; 
SET @launch_pad_item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), 999978, 0, 1, 240111044331057734, '/home', 'Bizs1', 'COMMUNITY_INTRO', '园区介绍', 'cs://1/image/aW1hZ2UvTVRwaVlXVTNPV05tT0RJME5tWXdaRGRoT1RFM05tUXlZakV6TUdGbVpERXpOUQ', 1, 1, 13, CONCAT('{"url":"', @eh_core_serverURL, '/park-introduction/index.html?hideNavigationBar=1&rtToken=dRzM4a8CPxNInE2RiVMRANc1sfCega7zPdS8YjwD0TPJYP_1bytqkRljtvPgFTm6NEdEuE-5uhQHc_4xqx70gQHDH1S8kVcMvXj-Kfdu9NXbAUNs_omn50T_XT2pP9gI7J5NSA1U4WOE7QAbRsS-fiNMhU4Pl4WTLsInA58I5XI#sign_suffix"}')
, 1, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), 999978, 0, 1, 240111044331057734, '/home', 'Bizs1', 'COMMUNITY_INTRO', '园区介绍', 'cs://1/image/aW1hZ2UvTVRwaVlXVTNPV05tT0RJME5tWXdaRGRoT1RFM05tUXlZakV6TUdGbVpERXpOUQ', 1, 1, 13, CONCAT('{"url":"', @eh_core_serverURL, '/park-introduction/index.html?hideNavigationBar=1&rtToken=dRzM4a8CPxNInE2RiVMRANc1sfCega7zPdS8YjwD0TPJYP_1bytqkRljtvPgFTm6NEdEuE-5uhQHc_4xqx70gQHDH1S8kVcMvXj-Kfdu9NXbAUNs_omn50T_XT2pP9gI7J5NSA1U4WOE7QAbRsS-fiNMhU4Pl4WTLsInA58I5XI#sign_suffix"}')
, 1, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), 999978, 0, 1, 240111044331057733, '/home', 'Bizs1', 'COMMUNITY_INTRO', '园区介绍', 'cs://1/image/aW1hZ2UvTVRwaVlXVTNPV05tT0RJME5tWXdaRGRoT1RFM05tUXlZakV6TUdGbVpERXpOUQ', 1, 1, 13, CONCAT('{"url":"', @eh_core_serverURL, '/park-introduction/index.html?hideNavigationBar=1&rtToken=71ffA-rX6JobFBIFmMgxjivrDt3iwM6lrBEq0TmyrXbJYP_1bytqkRljtvPgFTm6AidFVcNwV-4Op4KAUIvBDwHDH1S8kVcMvXj-Kfdu9NXbAUNs_omn50T_XT2pP9gI7J5NSA1U4WOE7QAbRsS-fiNMhU4Pl4WTLsInA58I5XI#sign_suffix"}')
, 1, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), 999978, 0, 1, 240111044331057733, '/home', 'Bizs1', 'COMMUNITY_INTRO', '园区介绍', 'cs://1/image/aW1hZ2UvTVRwaVlXVTNPV05tT0RJME5tWXdaRGRoT1RFM05tUXlZakV6TUdGbVpERXpOUQ', 1, 1, 13, CONCAT('{"url":"', @eh_core_serverURL, '/park-introduction/index.html?hideNavigationBar=1&rtToken=71ffA-rX6JobFBIFmMgxjivrDt3iwM6lrBEq0TmyrXbJYP_1bytqkRljtvPgFTm6AidFVcNwV-4Op4KAUIvBDwHDH1S8kVcMvXj-Kfdu9NXbAUNs_omn50T_XT2pP9gI7J5NSA1U4WOE7QAbRsS-fiNMhU4Pl4WTLsInA58I5XI#sign_suffix"}')
, 1, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

SET @category_id = (SELECT MAX(id) FROM `eh_categories`);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '亲子与教育', '兴趣/亲子与教育', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999978);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '运动与音乐', '兴趣/运动与音乐', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999978);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '美食与厨艺', '兴趣/美食与厨艺', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999978);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '美容化妆', '兴趣/美容化妆', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999978);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '家庭装饰', '兴趣/家庭装饰', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999978);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '名牌汇', '兴趣/名牌汇', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999978);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '宠物会', '兴趣/宠物会', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999978);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '旅游摄影', '兴趣/旅游摄影', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999978);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '拼车', '兴趣/拼车', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999978);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '老乡群', '兴趣/老乡群', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999978);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '同事群', '兴趣/同事群', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999978);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '同学群', '兴趣/同学群', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999978);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '其他', '兴趣/其他', 1, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999978);

SET @service_alliance_jump_module_id = (SELECT MAX(id) FROM `eh_service_alliance_jump_module`);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_url`, `parent_id`) VALUES ((@service_alliance_jump_module_id  := @service_alliance_jump_module_id  + 1), 999977, '物业报修', 'zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=物业报修', 0);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_url`, `parent_id`) VALUES ((@service_alliance_jump_module_id  := @service_alliance_jump_module_id  + 1), 999977, '月卡充值', 'zl://parking/query?displayName=停车', 0);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_url`, `parent_id`) VALUES ((@service_alliance_jump_module_id  := @service_alliance_jump_module_id  + 1), 999977, '审批', 'zl://approval/create?approvalId={}&sourceId={}', 0);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_url`, `parent_id`) VALUES ((@service_alliance_jump_module_id  := @service_alliance_jump_module_id  + 1), 999977, '电商', 'BIZS', 0);

SET @service_alliance_id = (SELECT MAX(id) FROM `eh_service_alliances`);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`, `module_url`, `contact_memid`, `support_type`, `button_title`)
VALUES ((@service_alliance_id := @service_alliance_id + 1), 0, 'community', 240111044331057734, '服务联盟', '服务联盟首页', 201059, '', '', '', 'cs://1/image/aW1hZ2UvTVRvMU56TXpOV0l3T1RKaFlqQTRNVFJpWmpSaVlUazFNall5WldRNVlUZ3dZUQ', 2, NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`, `module_url`, `contact_memid`, `support_type`, `button_title`)
VALUES ((@service_alliance_id := @service_alliance_id + 1), 0, 'community', 240111044331057733, '服务联盟', '服务联盟首页', 201059, '', '', '', 'cs://1/image/aW1hZ2UvTVRvMU56TXpOV0l3T1RKaFlqQTRNVFJpWmpSaVlUazFNall5WldRNVlUZ3dZUQ', 2, NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL);

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('pmtask.handler-999978', 'flow', '', '0', NULL);


-- fix 9621 add by xiongying20170509
SET @equipment_category_id = (SELECT max(id) FROM `eh_equipment_inspection_categories`);    
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ((@equipment_category_id := @equipment_category_id + 1), 999978, '', 0, '0', '设备', '/设备', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ((@equipment_category_id := @equipment_category_id + 1), 999978, '', 0, '0', '装修', '/装修', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ((@equipment_category_id := @equipment_category_id + 1), 999978, '', 0, '0', '空置房', '/空置房', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ((@equipment_category_id := @equipment_category_id + 1), 999978, '', 0, '0', '安保', '/安保', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ((@equipment_category_id := @equipment_category_id + 1), 999978, '', 0, '0', '日常工作检查', '/日常工作检查', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ((@equipment_category_id := @equipment_category_id + 1), 999978, '', 0, '0', '公共设施检查', '/公共设施检查', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ((@equipment_category_id := @equipment_category_id + 1), 999978, '', 0, '0', '周末值班', '/周末值班', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ((@equipment_category_id := @equipment_category_id + 1), 999978, '', 0, '0', '安全检查', '/安全检查', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ((@equipment_category_id := @equipment_category_id + 1), 999978, '', 0, '0', '其他', '/其他', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);

SET FOREIGN_KEY_CHECKS = 1;

-- 更改服务联盟是否支持审批, add by tt, 20170510
select max(id) into @id from `eh_service_alliance_jump_module`;
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_url`, `parent_id`) VALUES (@id+1, 999978, '审批', 'zl://approval/create?approvalId={}&sourceId={}', 0);

-- 更改app info, add by tt, 20170510
select max(id) into @id from  `eh_app_urls`;
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) VALUES (@id:=@id+1, 999978, '康利k生活', 2, 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.kangli', 'cs://1/image/aW1hZ2UvTVRvNVkyWmtaakUxTXpKaVkyWTJNalExTldFeVltUmxaRFl5TkdGaU4ySTNPQQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) VALUES (@id:=@id+1, 999978, '康利k生活', 1, 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.kangli', 'cs://1/image/aW1hZ2UvTVRvNVkyWmtaakUxTXpKaVkyWTJNalExTldFeVltUmxaRFl5TkdGaU4ySTNPQQ', '移动平台聚合服务，助力园区效能提升');

-- update add by sw 20170516
UPDATE eh_launch_pad_items set action_data = '{"itemLocation":"/home","itemGroup":"Bizs1"}' where namespace_id = 999978 and item_name = 'MORE' AND item_label = '更多';
SET @launch_pad_item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), '999978', '0', '0', '0', '/home', 'Bizs1', 'FLOW_TASKS', '任务管理', 'cs://1/image/aW1hZ2UvTVRwbVpETmxPV1E1TVRNNE1XVXpZVFl4WVRoaU1XUmtNRGxpWW1ZNFkyVXlOZw', '1', '1', '56', '', '8', '0', '1', '0', NULL, '0', NULL, NULL, NULL, '0', 'park_tourist', '0', NULL, NULL, '0', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
	VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), '999978', '0', '0', '0', '/home', 'Bizs1', 'FLOW_TASKS', '任务管理', 'cs://1/image/aW1hZ2UvTVRwbVpETmxPV1E1TVRNNE1XVXpZVFl4WVRoaU1XUmtNRGxpWW1ZNFkyVXlOZw', '1', '1', '56', '', '8', '0', '1', '0', NULL, '0', NULL, NULL, NULL, '0', 'pm_admin', '0', NULL, NULL, '0', NULL);

INSERT INTO `eh_namespaces` (`id`, `name`) VALUES ('999978', '康利');

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50660,'', 'EhNamespaces', 999978,2);
		
-- 康利item url修改 add by sfyan 20170714
update `eh_launch_pad_items` set `action_data` = '{"url":"http://alpha.vrbrowserextern.bqlnv.com.cn/vreditor/view/64997823126520945"}' where `item_label` = '园区3D图' and namespace_id = 999978;