SET FOREIGN_KEY_CHECKS = 0;
SET @namespace_id=999977;
SET @eh_core_serverURL = "https://core.zuolin.com"; -- 取具体环境连接core server的链接
SET @eh_biz_serverURL = "biz.zuolin.com"; -- 取具体环境连接core server的链接
SET @user_id = 274000;  -- 需要取现网eh_users的ID的最大值再加一定余量
SET @account_name='295585'; -- 需要取现网eh_users的acount_name的6位的最大值再加一定余量
SET @community_id = 240111044332059733; -- 需要取现网eh_communities的ID的最大值再加一定余量
SET @address_id = 239825274387352717; -- 需要取现网eh_addresses的ID的最大值再加一定余量
SET @organization_id = 1023180;  	-- 

SET @community_geopoint_id = (SELECT MAX(id) FROM `eh_community_geopoints`);  
SET @feedback_forum_id = 190430;   -- 取eh_forums的ID最大值再加一定余量
SET @community_forum_id = 190490;   -- 取eh_forums的ID最大值再加一定余量
SET @building_id = 194900;   -- 取eh_buildings的ID最大值再加一定余量

SET @region_id = 18151; 
SET @shi_id = 18151;  
SET @qu_id = 18151; 

SET @group_id = 1020469; 
SET @forum_id = 190510; 

SET @rental_res_id = 10812; 

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
	VALUES (@qu_id, @shi_id, '高新区', 'LONGGANGQU', 'LGQ', '/广东/深圳市/高新区', 3, 3, NULL, '0755', 2, 0, @namespace_id);

SET @forum_id := @forum_id + 1;
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES((@group_id := @group_id + 1), UUID(), '深圳市科技工业园物业管理有限公司', '深圳市科技工业园物业管理有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, @namespace_id); 
	
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', @group_id,'深圳市科技工业园物业管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());       

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'PM', '深圳市科技工业园物业管理有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @group_id);


INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(@community_forum_id, UUID(), @namespace_id, 2, 'EhGroups', 0,'科技园物业论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(@feedback_forum_id, UUID(), @namespace_id, 2, 'EhGroups', 0,'科技园物业反馈论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 


INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (@user_id, UUID(), @account_name, '郭永安', '', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES ((@user_identifier_id := @user_identifier_id + 1) , @user_id ,  '0',  '13714329720',  '221616',  3, UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`)
	VALUES((@organization_member_id := @organization_member_id + 1), @organization_id, 'USER', @user_id  , 'manager', '郭永安', 0, '13714329720', 3, @namespace_id);	
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES((@role_assignment_id := @role_assignment_id + 1), 'EhOrganizations', @organization_id, 'EhUsers', @user_id  , 1001, 1, UTC_TIMESTAMP());


SET @user_id = @user_id + 1;
SET @account_name = @account_name + 1;
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (@user_id, UUID(), @account_name, '唐俊强', '', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES ((@user_identifier_id := @user_identifier_id + 1) , @user_id ,  '0',  '13424374374',  '221616',  3, UTC_TIMESTAMP(), @namespace_id);
-- DELETE FROM `eh_organization_members` WHERE organization_id = @organization_id AND target_type = 'USER' AND target_id = @user_id;
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`)
	VALUES((@organization_member_id := @organization_member_id + 1), @organization_id, 'USER', @user_id  , 'manager', '唐俊强', 0, '13424374374', 3, @namespace_id);	
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES((@role_assignment_id := @role_assignment_id + 1), 'EhOrganizations', @organization_id, 'EhUsers', @user_id  , 1001, 1, UTC_TIMESTAMP());
	
SET @user_id = @user_id + 1;
SET @account_name = @account_name + 1;
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (@user_id, UUID(), @account_name, '郭强', '', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES ((@user_identifier_id := @user_identifier_id + 1) , @user_id ,  '0',  '13544221796',  '221616',  3, UTC_TIMESTAMP(), @namespace_id);
-- DELETE FROM `eh_organization_members` WHERE organization_id = @organization_id AND target_type = 'USER' AND target_id = @user_id;
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`)
	VALUES((@organization_member_id := @organization_member_id + 1), @organization_id, 'USER', @user_id  , 'manager', '郭强', 0, '13544221796', 3, @namespace_id);	
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES((@role_assignment_id := @role_assignment_id + 1), 'EhOrganizations', @organization_id, 'EhUsers', @user_id  , 1001, 1, UTC_TIMESTAMP());
	
SET @user_id = @user_id + 1;
SET @account_name = @account_name + 1;
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (@user_id, UUID(), @account_name, '周座名', '', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES ((@user_identifier_id := @user_identifier_id + 1) , @user_id ,  '0',  '13066883363',  '221616',  3, UTC_TIMESTAMP(), @namespace_id);
-- DELETE FROM `eh_organization_members` WHERE organization_id = @organization_id AND target_type = 'USER' AND target_id = @user_id;
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`)
	VALUES((@organization_member_id := @organization_member_id + 1), @organization_id, 'USER', @user_id  , 'manager', '周座名', 0, '13066883363', 3, @namespace_id);	
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES((@role_assignment_id := @role_assignment_id + 1), 'EhOrganizations', @organization_id, 'EhUsers', @user_id  , 1001, 1, UTC_TIMESTAMP());
	
SET @user_id = @user_id + 1;
SET @account_name = @account_name + 1;
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (@user_id, UUID(), @account_name, '孔祥炬', '', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES ((@user_identifier_id := @user_identifier_id + 1) , @user_id ,  '0',  '15818617695',  '221616',  3, UTC_TIMESTAMP(), @namespace_id);
-- DELETE FROM `eh_organization_members` WHERE organization_id = @organization_id AND target_type = 'USER' AND target_id = @user_id;
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`)
	VALUES((@organization_member_id := @organization_member_id + 1), @organization_id, 'USER', @user_id  , 'manager', '孔祥炬', 0, '15818617695', 3, @namespace_id);	
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES((@role_assignment_id := @role_assignment_id + 1), 'EhOrganizations', @organization_id, 'EhUsers', @user_id  , 1001, 1, UTC_TIMESTAMP());
	
SET @user_id = @user_id + 1;
SET @account_name = @account_name + 1;
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (@user_id, UUID(), @account_name, '郭利明', '', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES ((@user_identifier_id := @user_identifier_id + 1) , @user_id ,  '0',  '13691693831',  '221616',  3, UTC_TIMESTAMP(), @namespace_id);
-- DELETE FROM `eh_organization_members` WHERE organization_id = @organization_id AND target_type = 'USER' AND target_id = @user_id;
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`)
	VALUES((@organization_member_id := @organization_member_id + 1), @organization_id, 'USER', @user_id  , 'manager', '郭利明', 0, '13691693831', 3, @namespace_id);	
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES((@role_assignment_id := @role_assignment_id + 1), 'EhOrganizations', @organization_id, 'EhUsers', @user_id  , 1001, 1, UTC_TIMESTAMP());

	
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES(@community_id, UUID(), @shi_id, '深圳市',  @qu_id, '高新区', '中科大厦', '中科大厦', '南区', NULL, '',NULL, NULL, NULL, NULL, NULL, NULL,NULL, 0, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'1', @community_forum_id, @feedback_forum_id, UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES((@community_geopoint_id := @community_geopoint_id + 1), @community_id, '', 0, 0, '');
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(@organization_id, @community_id);
INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES((@namespace_resource_id := @namespace_resource_id+ 1), @namespace_id, 'COMMUNITY', @community_id, UTC_TIMESTAMP());	
	



-- 长园新材
SET @qu_id = @qu_id + 1;
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (@qu_id, @shi_id, '南山区', 'NANSHANQU', 'NSQ', '/广东/深圳市/南山区', 3, 3, NULL, '0755', 2, 0, @namespace_id);
	

SET @community_id = @community_id + 1;

INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES(@community_id, UUID(), @shi_id, '深圳市',  @qu_id, '南山区', '长园新材', '长园新材', '科苑路19号', NULL, '',NULL, NULL, NULL, NULL, NULL, NULL,NULL, 0, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'1', @community_forum_id, @feedback_forum_id, UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES((@community_geopoint_id := @community_geopoint_id + 1), @community_id, '', 0, 0, '');
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(@organization_id, @community_id);
INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES((@namespace_resource_id := @namespace_resource_id+ 1), @namespace_id, 'COMMUNITY', @community_id, UTC_TIMESTAMP());	
	


INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ((@configuration_id := @configuration_id + 1), 'app.agreements.url', CONCAT(@eh_core_serverURL, '/mobile/static/app_agreements/agreements.html?ns=', @namespace_id), 'the relative path for kangli app agreements', @namespace_id, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ((@configuration_id := @configuration_id + 1), 'business.url', CONCAT('https://',@eh_biz_serverURL,'/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2F', @eh_biz_serverURL, '%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix'), 'bussiness url', @namespace_id, '');


	
INSERT INTO `eh_version_realm` VALUES ((@version_realm_id := @version_realm_id + 1), 'Android_Ssippm', null, UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_version_realm` VALUES ((@version_realm_id := @version_realm_id + 1), 'iOS_Ssippm', null, UTC_TIMESTAMP(), @namespace_id);
	

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
        VALUES((@menu_scope_id := @menu_scope_id + 1), 11000,'', 'EhNamespaces', @namespace_id,2);
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
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40000,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40100,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40110,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40120,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40130,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40300,'', 'EhNamespaces', @namespace_id,2);
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
        VALUES((@menu_scope_id := @menu_scope_id + 1), 40600,'', 'EhNamespaces', @namespace_id,2);
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
        VALUES((@menu_scope_id := @menu_scope_id + 1), 35000,'', 'EhNamespaces', @namespace_id,2);
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
        VALUES((@menu_scope_id := @menu_scope_id + 1), 50900,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 60000,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 60100,'', 'EhNamespaces', @namespace_id,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
        VALUES((@menu_scope_id := @menu_scope_id + 1), 60200,'', 'EhNamespaces', @namespace_id,2);



SET @launch_pad_layout_id = (SELECT MAX(id) FROM `eh_launch_pad_layouts`);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
VALUES ((@launch_pad_layout_id := @launch_pad_layout_id + 1), @namespace_id, 'ServiceMarketLayout', '{"versionCode":"2017031001","versionName":"3.0.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":16,"columnCount":5},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":16},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0}]}', 2017042516, 0, 2, '2016-03-12 19:16:25', 'pm_admin', 0, 0, 0);


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'GovAgencies', 'CHECK_IN', '招商', 'cs://1/image/aW1hZ2UvTVRvd01qTTRNekZoTlRWaU5tWXdPRGhpTWpoak5tVTNZak5pTkRsaVpUSmtNdw', 1, 1, 28, '', 1, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'GovAgencies', 'CHECK_IN', '招商', 'cs://1/image/aW1hZ2UvTVRvd01qTTRNekZoTlRWaU5tWXdPRGhpTWpoak5tVTNZak5pTkRsaVpUSmtNdw', 1, 1, 28, '', 1, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'GovAgencies', 'DIDA_CAFE', '嘀嗒咖啡', 'cs://1/image/aW1hZ2UvTVRvMVptWTNNRE0wWkRka01HRXpNalV3TTJabE9EZ3hNVFV3T0RGak9ESXhNdw', 1, 1, 13, '{"url":""}', 2, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'GovAgencies', 'DIDA_CAFE', '嘀嗒咖啡', 'cs://1/image/aW1hZ2UvTVRvMVptWTNNRE0wWkRka01HRXpNalV3TTJabE9EZ3hNVFV3T0RGak9ESXhNdw', 1, 1, 13,'{"url":""}', 2, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

SET @forum_id := @forum_id + 1;
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 	
	VALUES(@forum_id , UUID(), @namespace_id, 2, 'EhGroups', 0,'创客空间','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'GovAgencies', 'MARKER_ZONE', '创客空间', 'cs://1/image/aW1hZ2UvTVRvNVltUTVaRE01TmpneU9USXhZamxqT1RSaU5URXlOVGsyTWpNNFpqY3dPQQ', 1, 1, 32, CONCAT('{"type":1,"forumId":',@forum_id,',"categoryId":1010,"parentId":110001,"tag":"创客"}'), 3, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'GovAgencies', 'MARKER_ZONE', '创客空间', 'cs://1/image/aW1hZ2UvTVRvNVltUTVaRE01TmpneU9USXhZamxqT1RSaU5URXlOVGsyTWpNNFpqY3dPQQ', 1, 1, 32, CONCAT('{"type":1,"forumId":',@forum_id,',"categoryId":1010,"parentId":110001,"tag":"创客"}'), 3, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'GovAgencies', 'MARKET', '商城', 'cs://1/image/aW1hZ2UvTVRwak5UbGxNekF3Wm1FMlpqUTRPVGczTmpJM05tVTBZemsxWldOalpHSXhPUQ', 1, 1, 13,'{"url":""}', 4, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'GovAgencies', 'MARKET', '商城', 'cs://1/image/aW1hZ2UvTVRwak5UbGxNekF3Wm1FMlpqUTRPVGczTmpJM05tVTBZemsxWldOalpHSXhPUQ', 1, 1, 13,'{"url":""}', 4, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'GovAgencies', 'PM', '物业', 'cs://1/image/aW1hZ2UvTVRwaU16ZzVPVE01WmpVMk5XTTRNamc0TkdNM1lqVmpOakF6WW1Fek16SXhZZw', 1, 1, 60, '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=物业服务"}', 5, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'GovAgencies', 'PM', '物业', 'cs://1/image/aW1hZ2UvTVRwaU16ZzVPVE01WmpVMk5XTTRNamc0TkdNM1lqVmpOakF6WW1Fek16SXhZZw', 1, 1, 60, '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=物业服务"}', 5, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

-- 

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) 
 VALUES((@rental_res_id := @rental_res_id + 1), '会议室', 0, NULL, 0, @namespace_id);
 
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'METTING_ROOM', '会议室', 'cs://1/image/aW1hZ2UvTVRvNE9UY3pNR05qTmpKa056VXlZakkxTjJNek16UXhNekUxTkRRM05HTmpPUQ', 1, 1, 49, CONCAT('{"resourceTypeId":', @rental_res_id, ',"pageType":0}'), 1, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'METTING_ROOM', '会议室', 'cs://1/image/aW1hZ2UvTVRvNE9UY3pNR05qTmpKa056VXlZakkxTjJNek16UXhNekUxTkRRM05HTmpPUQ', 1, 1, 49, CONCAT('{"resourceTypeId":', @rental_res_id, ',"pageType":0}'), 1, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) 
 VALUES((@rental_res_id := @rental_res_id + 1), '客房预定', 0, NULL, 0, @namespace_id);
 
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'METTING_ROOM', '客房预定', 'cs://1/image/aW1hZ2UvTVRwaE16aGxZV1JoWkRjMU5qQmpNell4WkRCbFlqQXhNekUwWm1VM01qazVOQQ', 1, 1, 49, CONCAT('{"resourceTypeId":', @rental_res_id, ',"pageType":0}'), 2, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'METTING_ROOM', '客房预定', 'cs://1/image/aW1hZ2UvTVRwaE16aGxZV1JoWkRjMU5qQmpNell4WkRCbFlqQXhNekUwWm1VM01qazVOQQ', 1, 1, 49, CONCAT('{"resourceTypeId":', @rental_res_id, ',"pageType":0}'), 2, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'PM', '物业服务', 'cs://1/image/aW1hZ2UvTVRveE5HUXlZelE0T1dJMlpXSmlOell3Wmpjek5HVm1NRGRsTURJeFpqQXpNUQ', 1, 1, 60, '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=物业服务"}', 3, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'PM', '物业服务', 'cs://1/image/aW1hZ2UvTVRveE5HUXlZelE0T1dJMlpXSmlOell3Wmpjek5HVm1NRGRsTURJeFpqQXpNUQ', 1, 1, 60, '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=物业服务"}', 3, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

SET @service_alliance_skip_rule_id = (SELECT MAX(id) FROM `eh_service_alliance_skip_rule`);
INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) 
VALUES ((@service_alliance_skip_rule_id := @service_alliance_skip_rule_id + 1), @namespace_id, 0);

SET @parent_id = (SELECT MAX(id) FROM `eh_service_alliance_categories`);
SET @parent_id = @parent_id + 1;
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES (@parent_id, 'community', @community_id, '0', '投放房源', '投放房源', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, @namespace_id, '');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'ROOM_RES', '投放房源', 'cs://1/image/aW1hZ2UvTVRwaE9UbGtNekEwTnpWbFpUVXpZV1kzTXpBNU9XTmhaR1ZqWldaa1lqQTJaQQ', 1, 1, 33, CONCAT('{"type":',@parent_id,',"parentId":',@parent_id,',"displayType": "list"}'), 4, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'ROOM_RES', '投放房源', 'cs://1/image/aW1hZ2UvTVRwaE9UbGtNekEwTnpWbFpUVXpZV1kzTXpBNU9XTmhaR1ZqWldaa1lqQTJaQQ', 1, 1, 33, CONCAT('{"type":',@parent_id,',"parentId":',@parent_id,',"displayType": "list"}'), 4, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

SET @parent_id = @parent_id + 1;
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES (@parent_id, 'community', @community_id, '0', '委托找房', '委托找房', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, @namespace_id, '');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'ROOM_RES', '委托找房', 'cs://1/image/aW1hZ2UvTVRvNE1tSXlaakU1TjJNMk16VTRNV0poTTJabU1tRm1ZalV3TVdJM1ptWXhOdw', 1, 1, 33, CONCAT('{"type":',@parent_id,',"parentId":',@parent_id,',"displayType": "list"}'), 5, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'ROOM_RES', '委托找房', 'cs://1/image/aW1hZ2UvTVRvNE1tSXlaakU1TjJNMk16VTRNV0poTTJabU1tRm1ZalV3TVdJM1ptWXhOdw', 1, 1, 33, CONCAT('{"type":',@parent_id,',"parentId":',@parent_id,',"displayType": "list"}'), 5, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'HOT_LINE', '服务热线', 'cs://1/image/aW1hZ2UvTVRvelpHVTNPV1V4T1dVek56azNaVEZrWW1ZME1HUmlaVGxsWVRFMk4ySXpPUQ', 1, 1, 45, '', 6, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'HOT_LINE', '服务热线', 'cs://1/image/aW1hZ2UvTVRvelpHVTNPV1V4T1dVek56azNaVEZrWW1ZME1HUmlaVGxsWVRFMk4ySXpPUQ', 1, 1, 45, '', 6, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'COMMUNITY_INTRO', '园区介绍', 'cs://1/image/aW1hZ2UvTVRvME1HSmxZVEEyWXpobE9ERXlObVEwTXpSa1lUQm1PVGRtWWpsbVpESTFOUQ', 1, 1, 13,'{"url":""}', 7, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'COMMUNITY_INTRO', '园区介绍', 'cs://1/image/aW1hZ2UvTVRvME1HSmxZVEEyWXpobE9ERXlObVEwTXpSa1lUQm1PVGRtWWpsbVpESTFOUQ', 1, 1, 13,'{"url":""}', 7, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'CONTACTS', '通讯录', 'cs://1/image/aW1hZ2UvTVRvMFpEUmxNMlU1T0RReFpEY3hOMlV3T0dKaFlqRXhNek0wWVdaak9UZ3pZUQ', 1, 1, 46, '', 8, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'CONTACTS', '通讯录', 'cs://1/image/aW1hZ2UvTVRvMFpEUmxNMlU1T0RReFpEY3hOMlV3T0dKaFlqRXhNek0wWVdaak9UZ3pZUQ', 1, 1, 46, '', 8, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

SET @parent_id = @parent_id + 1;
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES (@parent_id, 'community', @community_id, '0', '地产服务', '地产服务', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, @namespace_id, '');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'BUILD_SERVICE', '地产服务', 'cs://1/image/aW1hZ2UvTVRvNE5HSTRNMk5oWkRGaE5XRTRPV1UwTVRBNU5EUXlZV1JoTkRBNE0yVmpZUQ', 1, 1, 33, CONCAT('{"type":',@parent_id,',"parentId":',@parent_id,',"displayType": "list"}'), 9, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'BUILD_SERVICE', '地产服务', 'cs://1/image/aW1hZ2UvTVRvNE5HSTRNMk5oWkRGaE5XRTRPV1UwTVRBNU5EUXlZV1JoTkRBNE0yVmpZUQ', 1, 1, 33, CONCAT('{"type":',@parent_id,',"parentId":',@parent_id,',"displayType": "list"}'), 9, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'ACTIVITY', '活动', 'cs://1/image/aW1hZ2UvTVRwbVpXWmlabVl4TlRGa01HWTFaVFZtTkRBeVpqa3pNamswWVRNd01EY3lPUQ', 1, 1, 61, '{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":1,"scope":2,"style":4,"title": "活动"}', 10, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'ACTIVITY', '活动', 'cs://1/image/aW1hZ2UvTVRwbVpXWmlabVl4TlRGa01HWTFaVFZtTkRBeVpqa3pNamswWVRNd01EY3lPUQ', 1, 1, 61, '{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":1,"scope":2,"style":4,"title": "活动"}', 10, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'FORUM', '论坛', 'cs://1/image/aW1hZ2UvTVRvNU9HVXdaakl6WWpWaFl6TXlNVEJpWVRRek1XVXhZVEE0TjJJNFlUQTBaUQ', 1, 1, 0, '', 11, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'FORUM', '论坛', 'cs://1/image/aW1hZ2UvTVRvNU9HVXdaakl6WWpWaFl6TXlNVEJpWVRRek1XVXhZVEE0TjJJNFlUQTBaUQ', 1, 1, 0, '', 11, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'CLUB', '俱乐部', 'cs://1/image/aW1hZ2UvTVRvd1pEazFNakZqWVRVeFlUbGhZelF4TldVMFpXRmxZV0k0TWpaaFl6TTJOQQ', 1, 1, 36, '{"privateFlag": 0}', 12, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'CLUB', '俱乐部', 'cs://1/image/aW1hZ2UvTVRvd1pEazFNakZqWVRVeFlUbGhZelF4TldVMFpXRmxZV0k0TWpaaFl6TTJOQQ', 1, 1, 36, '{"privateFlag": 0}', 12, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'FLOW_TASKS', '任务管理', 'cs://1/image/aW1hZ2UvTVRwa1ltWmlPRGN5TTJObFlUYzFNV1l3WmpZNU9UVmlZMlEyTkdRd1pUUmxNUQ', 1, 1, 56, '', 13, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'FLOW_TASKS', '任务管理', 'cs://1/image/aW1hZ2UvTVRwa1ltWmlPRGN5TTJObFlUYzFNV1l3WmpZNU9UVmlZMlEyTkdRd1pUUmxNUQ', 1, 1, 56, '', 13, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'MORE', '', 'cs://1/image/aW1hZ2UvTVRwbFlURmlaREl5Wm1KaE1EaGxaakUzTURZNVlqUmhZalZpWVdVeE5EUmpPUQ', 1, 1, 1, '{"itemLocation":"/home","itemGroup":"More"}', 100, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'MORE', '更多', 'cs://1/image/aW1hZ2UvTVRwbFlURmlaREl5Wm1KaE1EaGxaakUzTURZNVlqUmhZalZpWVdVeE5EUmpPUQ', 1, 1, 1, '{"itemLocation":"/home","itemGroup":"More"}', 100, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);


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
	

SET @locale_template_id = (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates`(`id`, `namespace_id`, `scope`, `code`,`locale`, `description`, `text`) 
	VALUES((@locale_template_id := @locale_template_id + 1), @namespace_id, 'sms.default.yzx', 1, 'zh_CN', '验证码-创梦云', '43481');
	

DELETE FROM `eh_launch_pad_items` WHERE `namespace_id`=999977 AND `item_group`='Bizs' AND `item_name`='COMMUNITY_INTRO';

DELETE FROM `eh_launch_pad_items` WHERE `namespace_id`=999977 AND `item_group`='Bizs' AND `item_name`='FORUM';

UPDATE `eh_launch_pad_items` SET `action_data`='{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix"}' WHERE `namespace_id`=999977 AND `item_group`='Bizs' AND `item_name`='DIDA_CAFE';
UPDATE `eh_launch_pad_items` SET `action_data`='{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix"}' WHERE `namespace_id`=999977 AND `item_group`='Bizs' AND `item_name`='MARKET';
UPDATE `eh_launch_pad_items` SET `action_type`= 50,`action_data`='' WHERE `namespace_id`=999977 AND `item_group`='Bizs' AND `item_name`='ACTIVITY';

SET @eh_core_serverURL = "https://core.zuolin.com"; 
SET @launch_pad_item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), 999977, 0, 1, 240111044332059734, '/home', 'Bizs', 'COMMUNITY_INTRO', '园区介绍', 'cs://1/image/aW1hZ2UvTVRwaVlXVTNPV05tT0RJME5tWXdaRGRoT1RFM05tUXlZakV6TUdGbVpERXpOUQ', 1, 1, 13, CONCAT('{"url":"', @eh_core_serverURL, '/park-introduction/index.html?hideNavigationBar=1&rtToken=01ZHaSCn7HWyXR0RHls9FIK7sJoakyzaDhhriUmiVaf5ZHTb1cYFe4CesqjQ0widNEdEuE-5uhQHc_4xqx70gQHDH1S8kVcMvXj-Kfdu9NXbAUNs_omn50T_XT2pP9gI7J5NSA1U4WOE7QAbRsS-fksX6wOR6G_dRAcaFYBOds4#sign_suffix"}')
, 7, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), 999977, 0, 1, 240111044332059734, '/home', 'Bizs', 'COMMUNITY_INTRO', '园区介绍', 'cs://1/image/aW1hZ2UvTVRwaVlXVTNPV05tT0RJME5tWXdaRGRoT1RFM05tUXlZakV6TUdGbVpERXpOUQ', 1, 1, 13, CONCAT('{"url":"', @eh_core_serverURL, '/park-introduction/index.html?hideNavigationBar=1&rtToken=01ZHaSCn7HWyXR0RHls9FIK7sJoakyzaDhhriUmiVaf5ZHTb1cYFe4CesqjQ0widNEdEuE-5uhQHc_4xqx70gQHDH1S8kVcMvXj-Kfdu9NXbAUNs_omn50T_XT2pP9gI7J5NSA1U4WOE7QAbRsS-fksX6wOR6G_dRAcaFYBOds4#sign_suffix"}')
, 7, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), 999977, 0, 1, 240111044332059733, '/home', 'Bizs', 'COMMUNITY_INTRO', '园区介绍', 'cs://1/image/aW1hZ2UvTVRwaVlXVTNPV05tT0RJME5tWXdaRGRoT1RFM05tUXlZakV6TUdGbVpERXpOUQ', 1, 1, 13, CONCAT('{"url":"', @eh_core_serverURL, '/park-introduction/index.html?hideNavigationBar=1&rtToken=vsea8L9wTRCp40oGwQCy5_WKqyz8_S9M_FKgTY3PtFr5ZHTb1cYFe4CesqjQ0widAidFVcNwV-4Op4KAUIvBDwHDH1S8kVcMvXj-Kfdu9NXbAUNs_omn50T_XT2pP9gI7J5NSA1U4WOE7QAbRsS-fksX6wOR6G_dRAcaFYBOds4#sign_suffix"}')
, 7, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) 
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), 999977, 0, 1, 240111044332059733, '/home', 'Bizs', 'COMMUNITY_INTRO', '园区介绍', 'cs://1/image/aW1hZ2UvTVRwaVlXVTNPV05tT0RJME5tWXdaRGRoT1RFM05tUXlZakV6TUdGbVpERXpOUQ', 1, 1, 13, CONCAT('{"url":"', @eh_core_serverURL, '/park-introduction/index.html?hideNavigationBar=1&rtToken=vsea8L9wTRCp40oGwQCy5_WKqyz8_S9M_FKgTY3PtFr5ZHTb1cYFe4CesqjQ0widAidFVcNwV-4Op4KAUIvBDwHDH1S8kVcMvXj-Kfdu9NXbAUNs_omn50T_XT2pP9gI7J5NSA1U4WOE7QAbRsS-fksX6wOR6G_dRAcaFYBOds4#sign_suffix"}')
, 7, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

SET @lease_config_id = (SELECT MAX(id) FROM `eh_lease_configs`);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `rent_amount_flag`, `issuing_lease_flag`, `issuer_manage_flag`, `park_indroduce_flag`, `renew_flag`) 
VALUES ((@lease_config_id := @lease_config_id + 1), 999977, 1, 1, 1, 1, 1);


SET @category_id = (SELECT MAX(id) FROM `eh_categories`);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '亲子与教育', '兴趣/亲子与教育', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999977);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '运动与音乐', '兴趣/运动与音乐', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999977);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '美食与厨艺', '兴趣/美食与厨艺', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999977);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '美容化妆', '兴趣/美容化妆', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999977);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '家庭装饰', '兴趣/家庭装饰', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999977);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '名牌汇', '兴趣/名牌汇', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999977);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '宠物会', '兴趣/宠物会', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999977);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '旅游摄影', '兴趣/旅游摄影', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999977);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '拼车', '兴趣/拼车', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999977);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '老乡群', '兴趣/老乡群', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999977);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '同事群', '兴趣/同事群', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999977);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '同学群', '兴趣/同学群', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999977);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '其他', '兴趣/其他', 1, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999977);

SET @service_alliance_jump_module_id = (SELECT MAX(id) FROM `eh_service_alliance_jump_module`);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_url`, `parent_id`) VALUES ((@service_alliance_jump_module_id  := @service_alliance_jump_module_id  + 1), 999977, '物业报修', 'zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=物业报修', 0);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_url`, `parent_id`) VALUES ((@service_alliance_jump_module_id  := @service_alliance_jump_module_id  + 1), 999977, '月卡充值', 'zl://parking/query?displayName=停车', 0);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_url`, `parent_id`) VALUES ((@service_alliance_jump_module_id  := @service_alliance_jump_module_id  + 1), 999977, '审批', 'zl://approval/create?approvalId={}&sourceId={}', 0);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_url`, `parent_id`) VALUES ((@service_alliance_jump_module_id  := @service_alliance_jump_module_id  + 1), 999977, '电商', 'BIZS', 0);


INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('pmtask.handler-999977', 'flow', '', '0', NULL);

-- redmine 9365 add by xiongying 20170503
SET @launch_pad_layout_id = (SELECT MAX(id) FROM `eh_launch_pad_layouts`);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
VALUES ((@launch_pad_layout_id := @launch_pad_layout_id + 1), 999977, 'ServiceMarketLayout', '{"versionCode":"2017050301","versionName":"3.0.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":16,"columnCount":5},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":16},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0}]}', 2017050301, 0, 2, '2016-03-12 19:16:25', 'park_tourist', 0, 0, 0);

delete from eh_launch_pad_items where namespace_id = 999977 and item_label = '通讯录';
update eh_launch_pad_items set item_label = '公司简介' where namespace_id = 999977 and item_label = '园区介绍';

-- redmine 9383 add by xiongying 20170503
update eh_launch_pad_items set delete_flag = 1 where namespace_id = 999977 and item_label in('会议室','客房预定','物业服务','投放房源','委托找房','服务热线','公司简介','地产服务','活动','俱乐部','任务管理');

-- redmine 9283 add by xiongying 20170504
SET @id = (SELECT MAX(id) FROM eh_app_urls);    
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) VALUES ((@id := @id + 1), '999977', '创梦云', '2', '', '', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) VALUES ((@id := @id + 1), '999977', '创梦云', '1', '', '', '移动平台聚合服务，助力园区效能提升');

-- redmine 9431 add by xiongying20170504
update eh_launch_pad_items set action_data = '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14937991534523928845%3F_k%3Dzlbiz#sign_suffix"}' where namespace_id = 999977 and item_label = '嘀嗒咖啡';

-- redmine 10276 add by dengs 20170522
update eh_launch_pad_items set action_data ='{"itemLocation":"/home","itemGroup":"Bizs"}' WHERE namespace_id = '999977' AND item_name = 'More';

INSERT INTO `eh_namespaces` (`id`, `name`) VALUES (999977, '科技园物业');

SET FOREIGN_KEY_CHECKS = 1;

-- by dengs,服务联盟大分类添加到eh_service_alliances中，20170808
set @eh_service_alliances_id = (select Max(id) from eh_service_alliances);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`, `module_url`, `contact_memid`, `support_type`, `button_title`, `description_height`, `display_flag`) VALUES
((@eh_service_alliances_id:=@eh_service_alliances_id+1), '0', 'community', '240111044332059734', '投放房源', '投放房源', '0', '', NULL, NULL, NULL, '2', @eh_service_alliances_id, NULL, NULL, NULL, '1', '0', NULL, NULL, '', NULL, ' ', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', NOW(), NULL, NULL, '2', NULL, '2', '1');
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`, `module_url`, `contact_memid`, `support_type`, `button_title`, `description_height`, `display_flag`) VALUES
((@eh_service_alliances_id:=@eh_service_alliances_id+1), '0', 'community', '240111044332059734', '委托找房', '委托找房', '0', '', NULL, NULL, NULL, '2', @eh_service_alliances_id, NULL, NULL, NULL, '1', '0', NULL, NULL, '', NULL, ' ', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', NOW(), NULL, NULL, '2', NULL, '2', '1');
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`, `module_url`, `contact_memid`, `support_type`, `button_title`, `description_height`, `display_flag`) VALUES
((@eh_service_alliances_id:=@eh_service_alliances_id+1), '0', 'community', '240111044332059734', '地产服务', '地产服务', '0', '', NULL, NULL, NULL, '2', @eh_service_alliances_id, NULL, NULL, NULL, '1', '0', NULL, NULL, '', NULL, ' ', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', NOW(), NULL, NULL, '2', NULL, '2', '1');
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`, `module_url`, `contact_memid`, `support_type`, `button_title`, `description_height`, `display_flag`) VALUES
((@eh_service_alliances_id:=@eh_service_alliances_id+1), '0', 'community', '240111044332059733', '投放房源', '投放房源', '0', '', NULL, NULL, NULL, '2', @eh_service_alliances_id, NULL, NULL, NULL, '1', '0', NULL, NULL, '', NULL, ' ', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', NOW(), NULL, NULL, '2', NULL, '2', '1');
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`, `module_url`, `contact_memid`, `support_type`, `button_title`, `description_height`, `display_flag`) VALUES
((@eh_service_alliances_id:=@eh_service_alliances_id+1), '0', 'community', '240111044332059733', '委托找房', '委托找房', '0', '', NULL, NULL, NULL, '2', @eh_service_alliances_id, NULL, NULL, NULL, '1', '0', NULL, NULL, '', NULL, ' ', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', NOW(), NULL, NULL, '2', NULL, '2', '1');
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`, `module_url`, `contact_memid`, `support_type`, `button_title`, `description_height`, `display_flag`) VALUES
((@eh_service_alliances_id:=@eh_service_alliances_id+1), '0', 'community', '240111044332059733', '地产服务', '地产服务', '0', '', NULL, NULL, NULL, '2', @eh_service_alliances_id, NULL, NULL, NULL, '1', '0', NULL, NULL, '', NULL, ' ', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', NOW(), NULL, NULL, '2', NULL, '2', '1');
