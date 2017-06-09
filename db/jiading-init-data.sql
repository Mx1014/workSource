SET @namespace_id = 999974;
SET @configuration_id = (SELECT max(id) FROM `eh_configurations`);
SET @core_server_url = "core.zuolin.com";
SET @biz_url = 'biz.zuolin.com';
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ((@configuration_id := @configuration_id + 1), 'app.agreements.url', CONCAT('https://', @core_server_url, '/mobile/static/app_agreements/agreements.html?ns=999974'), 'the relative path for jiadingxincheng TEEC app agreements', @namespace_id, NULL);	   
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ((@configuration_id := @configuration_id + 1), 'business.url', 'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&mallId=1999973&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%3FmallId%3D1999973%23%2Fstore%2Fdetails%2F14913736792191045197#sign_suffix', 'biz access url for TEEC', @namespace_id, NULL);	   


INSERT INTO `eh_version_realm` VALUES (121, 'Android_TEEC', null, UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_version_realm` VALUES (122, 'iOS_TEEC', null, UTC_TIMESTAMP(), @namespace_id);

INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES(321,121,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES(322,122,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());
	
	
SET @namespace_detail_id = (SELECT max(id) FROM `eh_namespace_details`);
INSERT INTO `eh_namespaces`(`id`, `name`) VALUES(@namespace_id, '嘉定新城TEEC');
INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES((@namespace_detail_id := @namespace_detail_id + 1), @namespace_id, 'community_commercial', UTC_TIMESTAMP());


SET @sheng_id = (SELECT max(id) FROM `eh_regions`) + 5;   
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) VALUES (@sheng_id, '0', '上海', 'SHANGHAI', 'SH', '/上海', '1', '1', NULL, NULL, '2', '0', @namespace_id);
SET @shi_id = @sheng_id + 1;   
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) VALUES (@shi_id, @sheng_id, '上海市', 'SHANGHAISHI', 'SHS', '/上海/上海市', '2', '2', NULL, '021', '2', '1', @namespace_id);
SET @qu_id = @shi_id + 1;   
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) VALUES (@qu_id, @shi_id, '嘉定区', 'JIADINGQU', 'JDQ', '/上海/上海市/嘉定区', '3', '3', NULL, '021', '2', '0', @namespace_id);


SET @organization_id = (SELECT MAX(id) FROM `eh_organizations`) + 5;
SET @org_group_id = (SELECT MAX(id) FROM `eh_groups`) + 5; 
SET @org_forum_id = (SELECT MAX(id) FROM `eh_forums`) + 5; 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(@org_group_id, UUID(), '上海创源新城科技有限公司', '创源', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @org_forum_id, 1, @namespace_id); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(@org_forum_id, UUID(), @namespace_id, 2, 'EhGroups', @org_group_id,'上海创源新城科技有限公司论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());       

SET @org_cmnty_request_id = (SELECT max(id) FROM `eh_organization_community_requests`);
SET @community_id = (SELECT MAX(id) FROM `eh_communities`) + 5;	
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'PM', '上海创源新城科技有限公司', '', CONCAT('/', @organization_id), 1, 2, 'ENTERPRISE', @namespace_id, @org_group_id);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@org_cmnty_request_id := @org_cmnty_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP()); 
    
SET @detail_id = (SELECT MAX(id) FROM `eh_organization_details`);	
INSERT INTO `eh_organization_details` (`id`, `organization_id`, `description`, `contact`, `address`, `create_time`, `longitude`, `latitude`, `geohash`, `display_name`, `contactor`, `member_count`, `checkin_date`, `avatar`, `post_uri`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `service_user_id`, `namespace_organization_type`, `namespace_organization_token`) 
    VALUES ((@detail_id := @detail_id + 1), @organization_id, NULL, NULL, NULL, UTC_TIMESTAMP(), NULL, NULL, NULL, '创源', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);


SET @user_id = 243081;
SET @account_name = 19091791975;
SET @user_identifier_id = (SELECT max(id) FROM `eh_user_identifiers`);
SET @org_member_id = (SELECT MAX(id) FROM `eh_organization_members`); 
SET @role_assignment_id = (SELECT MAX(id) FROM `eh_acl_role_assignments`); 
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (@user_id, UUID(), @account_name, '许俊杰', '', 1, 45, '1', '2',  'zh_CN',  '5c2d56ce23e2f8ea6f525c580a051a89', 'b05efa7b64d7e5ddfbfd64ff74c5f3fda9ed66e593abad5a4b49b28d36aa7426', UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES ((@user_identifier_id := @user_identifier_id + 1) , @user_id ,  '0',  '18019007066',  '221616',  3, UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`)
	VALUES((@org_member_id := @org_member_id + 1), @organization_id, 'USER', @user_id  , 'manager', '许俊杰', 0, '18019007066', 3, @namespace_id);	
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES((@role_assignment_id := @role_assignment_id + 1), 'EhOrganizations', @organization_id, 'EhUsers', @user_id  , 1001, 1, UTC_TIMESTAMP());

 


SET @community_geopoint_id = (SELECT MAX(id) FROM `eh_community_geopoints`) + 5;  
SET @community_forum_id = (SELECT MAX(id) FROM `eh_forums`) + 5;   
SET @feedback_forum_id = @community_forum_id+1;  
SET @namespace_resource_id = (SELECT max(id) FROM `eh_namespace_resources`);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(@community_forum_id, UUID(), @namespace_id, 2, 'EhGroups', 0,'TEEC论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(@feedback_forum_id, UUID(), @namespace_id, 2, 'EhGroups', 0,'TEEC意见反馈论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 

INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES(@community_id, UUID(), @shi_id, '上海市',  @qu_id, '嘉定区', 'TEEC上海中心', 'TEEC上海中心', '云谷路599弄TEEC上海中心大厦', NULL, 'TEEC上海中心大厦位于上海市嘉定区云谷路599弄，是嘉定新城和TEEC（清华企业家协会）合作的新典范，园区将打造嘉定传统产业转型升级的新引擎（新能源智能网联汽车、物联网集成电路、先进制造和智慧医疗），并充分融入清华企业家元素的创新资源，由teec会员上海创源科技发展有限公司和嘉定区国有全资上海嘉定新城发展有限公司合资成立的上海创源新城科技有限公司进行运营管理。',NULL, NULL, NULL, NULL, NULL, NULL,NULL, 13, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,1, @community_forum_id, @feedback_forum_id, UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES((@community_geopoint_id := @community_geopoint_id + 1), @community_id, '', 121.2534427643, 31.3323967337, 'wtw4ppyh6n04');
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(@organization_id, @community_id);
INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES((@namespace_resource_id := @namespace_resource_id + 1), @namespace_id, 'COMMUNITY', @community_id, UTC_TIMESTAMP());	

SET @building_id = (SELECT MAX(id) FROM `eh_buildings`); 
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
	VALUES ((@building_id := @building_id + 1), @community_id, '创源新城TEEC上海中心大厦', 'TEEC上海中心大厦', 0, '021-61250766', '上海市嘉定区云谷路599弄6号楼21F', NULL, 121.2534427643, 31.3323967337, 'wtw4ppyh6n04', 'TEEC上海中心大厦位于上海市嘉定区云谷路599弄，是嘉定新城和TEEC（清华企业家协会）合作的新典范，园区将打造嘉定传统产业转型升级的新引擎（新能源智能网联汽车、物联网集成电路、先进制造和智慧医疗），并充分融入清华企业家元素的创新资源，由teec会员上海创源科技发展有限公司和嘉定区国有全资上海嘉定新城发展有限公司合资成立的上海创源新城科技有限公司进行运营管理。', '', '2', '1', UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, @namespace_id);

SET @address_id = (SELECT MAX(id) FROM `eh_addresses`) + 5;  
SET @address_mapping_id = (SELECT max(id) FROM `eh_organization_address_mappings`);  
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `apartment_floor`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '上海市', @qu_id, '嘉定区' ,'6号楼-601','创源新城TEEC上海中心大厦','601','2','0',UTC_TIMESTAMP(), @namespace_id, 6);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES ((@address_mapping_id := @address_mapping_id + 1), @organization_id, @community_id, @address_id, '6号楼-601', '0');
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `apartment_floor`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '上海市', @qu_id, '嘉定区' ,'6号楼-1601','创源新城TEEC上海中心大厦','1601','2','0',UTC_TIMESTAMP(), @namespace_id, 16);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES ((@address_mapping_id := @address_mapping_id + 1), @organization_id, @community_id, @address_id, '6号楼-1601', '0');
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `apartment_floor`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '上海市', @qu_id, '嘉定区' ,'6号楼-1602','创源新城TEEC上海中心大厦','1602','2','0',UTC_TIMESTAMP(), @namespace_id, 16);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES ((@address_mapping_id := @address_mapping_id + 1), @organization_id, @community_id, @address_id, '6号楼-1602', '0');
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `apartment_floor`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '上海市', @qu_id, '嘉定区' ,'6号楼-1603','创源新城TEEC上海中心大厦','1603','2','0',UTC_TIMESTAMP(), @namespace_id, 16);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES ((@address_mapping_id := @address_mapping_id + 1), @organization_id, @community_id, @address_id, '6号楼-1603', '0');
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `apartment_floor`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '上海市', @qu_id, '嘉定区' ,'6号楼-1605','创源新城TEEC上海中心大厦','1605','2','0',UTC_TIMESTAMP(), @namespace_id, 16);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES ((@address_mapping_id := @address_mapping_id + 1), @organization_id, @community_id, @address_id, '6号楼-1605', '0');
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `apartment_floor`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '上海市', @qu_id, '嘉定区' ,'6号楼-1606','创源新城TEEC上海中心大厦','1606','2','0',UTC_TIMESTAMP(), @namespace_id, 16);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES ((@address_mapping_id := @address_mapping_id + 1), @organization_id, @community_id, @address_id, '6号楼-1606', '0');
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `apartment_floor`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '上海市', @qu_id, '嘉定区' ,'6号楼-1607','创源新城TEEC上海中心大厦','1607','2','0',UTC_TIMESTAMP(), @namespace_id, 16);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES ((@address_mapping_id := @address_mapping_id + 1), @organization_id, @community_id, @address_id, '6号楼-1607', '0');
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `apartment_floor`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '上海市', @qu_id, '嘉定区' ,'6号楼-1608','创源新城TEEC上海中心大厦','1608','2','0',UTC_TIMESTAMP(), @namespace_id, 16);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES ((@address_mapping_id := @address_mapping_id + 1), @organization_id, @community_id, @address_id, '6号楼-1608', '0');
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `apartment_floor`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '上海市', @qu_id, '嘉定区' ,'6号楼-1609','创源新城TEEC上海中心大厦','1609','2','0',UTC_TIMESTAMP(), @namespace_id, 16);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES ((@address_mapping_id := @address_mapping_id + 1), @organization_id, @community_id, @address_id, '6号楼-1609', '0');
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `apartment_floor`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '上海市', @qu_id, '嘉定区' ,'6号楼-1610','创源新城TEEC上海中心大厦','1610','2','0',UTC_TIMESTAMP(), @namespace_id, 16);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES ((@address_mapping_id := @address_mapping_id + 1), @organization_id, @community_id, @address_id, '6号楼-1610', '0');
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `apartment_floor`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '上海市', @qu_id, '嘉定区' ,'6号楼-1615','创源新城TEEC上海中心大厦','1615','2','0',UTC_TIMESTAMP(), @namespace_id, 16);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES ((@address_mapping_id := @address_mapping_id + 1), @organization_id, @community_id, @address_id, '6号楼-1615', '0');
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `apartment_floor`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '上海市', @qu_id, '嘉定区' ,'6号楼-2001','创源新城TEEC上海中心大厦','2001','2','0',UTC_TIMESTAMP(), @namespace_id, 20);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES ((@address_mapping_id := @address_mapping_id + 1), @organization_id, @community_id, @address_id, '6号楼-2001', '0');
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `apartment_floor`)
	VALUES((@address_id := @address_id +1), UUID(), @community_id, @shi_id, '上海市', @qu_id, '嘉定区' ,'6号楼-2201','创源新城TEEC上海中心大厦','2201','2','0',UTC_TIMESTAMP(), @namespace_id, 22);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES ((@address_mapping_id := @address_mapping_id + 1), @organization_id, @community_id, @address_id, '6号楼-2201', '0');



SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10000,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10100,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10400,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10600,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10800,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),11000,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),12200,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20000,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20100,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20140,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20150,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20155,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20160,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20170,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20180,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20158,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20190,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20191,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20192,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40000,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40100,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40110,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40120,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40300,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40400,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40410,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40420,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40430,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40440,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40500,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40510,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40520,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40530,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40800,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40810,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40830,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40840,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),30000,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),33000,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),34000,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),35000,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50000,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50100,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50110,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50200,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50210,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50220,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50300,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50400,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50500,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50600,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50630,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50631,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50632,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50633,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50640,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50650,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50651,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50652,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56161,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50700,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50710,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50720,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50730,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50800,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50810,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50820,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50830,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50840,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50850,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50860,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),60000,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),60100,'', 'EhNamespaces', 999974,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),60200,'', 'EhNamespaces', 999974,2);


-- 服务广场
SET @launch_pad_layout_id = (SELECT MAX(id) FROM `eh_launch_pad_layouts`);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
    VALUES ((@launch_pad_layout_id := @launch_pad_layout_id + 1), '999974', 'ServiceMarketLayout', '{"versionCode":"2017060601","versionName":"4.6.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":16},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"News","instanceConfig":{"timeWidgetStyle":"datetime","categoryId":0,"itemGroup":"Default"},"style":"Default","defaultOrder":1}]}', '2017060601', '0', '2', '2017-06-06 12:15:25', 'park_tourist', '0', '0', '0');
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
    VALUES ((@launch_pad_layout_id := @launch_pad_layout_id + 1), '999974', 'ServiceMarketLayout', '{"versionCode":"2017060601","versionName":"4.6.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":16},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"News","instanceConfig":{"timeWidgetStyle":"datetime","categoryId":0,"itemGroup":"Default"},"style":"Default","defaultOrder":1}]}', '2017060601', '0', '2', '2017-06-06 12:15:25', 'pm_admin', '0', '0', '0');

SET @eh_banners = (SELECT max(id) FROM `eh_banners`);   
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES ((@eh_banners := @eh_banners + 1), 999974, 0, '/home', 'Default', '0', '0', 'jdxc', 'jdxc', '', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'park_tourist');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES ((@eh_banners := @eh_banners + 1), 999974, 0, '/home', 'Default', '0', '0', 'jdxc', 'jdxc', '', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'pm_admin');

SET @parent_id = (SELECT MAX(id) FROM `eh_service_alliance_categories`);
SET @parent_id = @parent_id + 1;
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES (@parent_id, 'community', @community_id, '0', '服务联盟', '服务联盟', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, @namespace_id, '');                         

SET @service_alliance_id = (SELECT MAX(id) FROM `eh_service_alliances`);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`, `module_url`, `contact_memid`, `support_type`, `button_title`)
VALUES ((@service_alliance_id := @service_alliance_id + 1), 0, 'community', @community_id, '服务联盟', '服务联盟首页', @parent_id, '', '', '', 'cs://1/image/aW1hZ2UvTVRvMU56TXpOV0l3T1RKaFlqQTRNVFJpWmpSaVlUazFNall5WldRNVlUZ3dZUQ', 2, NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL);
    
SET @resource_type_id = (SELECT MAX(id) FROM `eh_rentalv2_resource_types`);
SET @resource_type_id = @resource_type_id + 1;
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES (@resource_type_id, '会议室预订', 0, NULL, 0, 999974);    


SET @item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`); 
-- 园区管理员  
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999974, '0', '0', '0', '/home', 'Bizs', 'HOT_LINE', '园区热线', '', '1', '1', 45, '', 10, '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999974, '0', '0', '0', '/home', 'Bizs', 'OFFICIAL_ACTIVITY', '园区活动', '', '1', '1', 50,'', '20', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999974, '0', '0', '0', '/home', 'Bizs', 'ENTER_PARK', '园区入驻', '', '1', '1', 28,'', '30', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999974, '0', '0', '0', '/home', 'Bizs', 'WIFI', '一键上网', '', '1', '1', 47,'', '40', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999974, '0', '0', '0', '/home', 'Bizs', 'RENTAL', '会议室预订', '', '1', '1', 49,CONCAT('{"resourceTypeId":',@resource_type_id,',"pageType": 0}'), '50', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999974, '0', '0', '0', '/home', 'Bizs', 'PUNCH', '移动考勤', '', '1', '1', 23,'', '60', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999974, '0', '0', '0', '/home', 'Bizs', '物业报修', '物业服务', '', '1', '1', 60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=物业服务"}', '70', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999974, '0', '0', '0', '/home', 'Bizs', 'MORE', '更多', '', '1', '1', 1,'{"itemLocation":"/home","itemGroup":"Bizs"}', '100', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999974, '0', '0', '0', '/home', 'Bizs', 'SERVICEALLIANCE', '服务联盟', '', '1', '1', 33,CONCAT('{"type":',@parent_id,',"parentId":',@parent_id,',"displayType": "grid"}'), '71', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999974, '0', '0', '0', '/home', 'Bizs', 'ACLINK', '智能门禁', '', '1', '1', 40,'{"isSupportQR":1,"isSupportSmart":0}', '72', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999974, '0', '0', '0', '/home', 'Bizs', 'VIDEO_MEETING', '视频会议', '', '1', '1', 27,'', '73', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999974, '0', '0', '0', '/home', 'Bizs', 'CONTACTS', '企业通讯录', '', '1', '1', 46,'', '74', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');    

-- 园区游客
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999974, '0', '0', '0', '/home', 'Bizs', 'HOT_LINE', '园区热线', '', '1', '1', 45, '', 10, '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999974, '0', '0', '0', '/home', 'Bizs', 'OFFICIAL_ACTIVITY', '园区活动', '', '1', '1', 50,'', '20', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999974, '0', '0', '0', '/home', 'Bizs', 'ENTER_PARK', '园区入驻', '', '1', '1', 28,'', '30', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999974, '0', '0', '0', '/home', 'Bizs', 'WIFI', '一键上网', '', '1', '1', 47,'', '40', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999974, '0', '0', '0', '/home', 'Bizs', 'RENTAL', '会议室预订', '', '1', '1', 49,CONCAT('{"resourceTypeId":',@resource_type_id,',"pageType": 0}'), '50', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999974, '0', '0', '0', '/home', 'Bizs', 'PUNCH', '移动考勤', '', '1', '1', 23,'', '60', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999974, '0', '0', '0', '/home', 'Bizs', '物业报修', '物业服务', '', '1', '1', 60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=物业服务"}', '70', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999974, '0', '0', '0', '/home', 'Bizs', 'MORE', '更多', '', '1', '1', 1,'{"itemLocation":"/home","itemGroup":"Bizs"}', '100', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999974, '0', '0', '0', '/home', 'Bizs', 'SERVICEALLIANCE', '服务联盟', '', '1', '1', 33,CONCAT('{"type":',@parent_id,',"parentId":',@parent_id,',"displayType": "grid"}'), '71', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999974, '0', '0', '0', '/home', 'Bizs', 'ACLINK', '智能门禁', '', '1', '1', 40,'{"isSupportQR":1,"isSupportSmart":0}', '72', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999974, '0', '0', '0', '/home', 'Bizs', 'VIDEO_MEETING', '视频会议', '', '1', '1', 27,'', '73', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999974, '0', '0', '0', '/home', 'Bizs', 'CONTACTS', '企业通讯录', '', '1', '1', 46,'', '74', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');   


INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999974, 'sms.default.yzx', 1, 'zh_CN', '验证码-嘉定', '65527');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999974, 'sms.default.yzx', 4, 'zh_CN', '派单-嘉定', '65528');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999974, 'sms.default.yzx', 5, 'zh_CN', '任务-嘉定', '65530');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999974, 'sms.default.yzx', 6, 'zh_CN', '任务2-嘉定', '65531');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999974, 'sms.default.yzx', 7, 'zh_CN', '新报修-嘉定', '65532');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999974, 'sms.default.yzx', 9, 'zh_CN', '看楼-嘉定', '65912');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999974, 'sms.default.yzx', 15, 'zh_CN', '物业任务3-嘉定', '65533');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999974, 'sms.default.yzx', 12, 'zh_CN', '预定1-嘉定', '65913');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999974, 'sms.default.yzx', 13, 'zh_CN', '预定2-嘉定', '65917');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999974, 'sms.default.yzx', 14, 'zh_CN', '预定3-嘉定', '65918');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999974, 'sms.default.yzx', 51, 'zh_CN', '预定3-嘉定', '65920');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999974, 'sms.default.yzx', 52, 'zh_CN', '预定3-嘉定', '65921');

SET @category_id = (SELECT MAX(id) FROM `eh_categories`); 
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
    
SET @url_id = (SELECT MAX(id) FROM `eh_app_urls`); 
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) VALUES ((@url_id := @url_id + 1), '999974', '嘉定新城', '2', '', '', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) VALUES ((@url_id := @url_id + 1), '999974', '嘉定新城', '1', '', '', '移动平台聚合服务，助力园区效能提升');


update eh_banners set poster_path = 'cs://1/image/aW1hZ2UvTVRwaVpXTTFZbUl5T1dVeE1qTXpaR1k1WVdNNU5ESXpPR1prTkdKbU4yWTFZUQ' where namespace_id = 999974;
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvM05qUm1NMlJoTkRsall6Z3laak0xTURWbE1EY3dOMk0zTURka00yTm1OZw' where namespace_id = 999974 and item_label = '一键上网';
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRwaE9XWXpNekl6Wm1VNVpEQXhPV1F6WVRNeU9UVXpPV1V3T0RCbU5UUXlZUQ' where namespace_id = 999974 and item_label = '企业通讯录';
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRwbU0yUXdaalZoTUdRNU9XTXpZakkwWlRBME9UQTJNMlF6WVRrME9XWXlNUQ' where namespace_id = 999974 and item_label = '会议室预订';
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvMllqQXdaR05tTWpjeU5HTTNNekUzTmpJME5URTNOek0wTUdZek1UVXlaZw' where namespace_id = 999974 and item_label = '园区入驻';
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvd1l6ZzJaVGMwTURZeVlXWXlPRFZpWldRME5qWXdNbVJoTm1JellUVTROQQ' where namespace_id = 999974 and item_label = '园区活动';
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvd1l6UTBZbVl5WldRellqQXlOamhrWmpjNE1tVmpZemt4TmpsbVpESm1PQQ' where namespace_id = 999974 and item_label = '园区热线';
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvNU5ETTBNRE15WkRRNFpXRXlaamxoTUdOaE5XTmlNRFl6TTJOaU5UQmtPQQ' where namespace_id = 999974 and item_label = '智能门禁';
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRwallXTmtZelV5TkRSa09XRmlNR1E0WVRFek1HVTBPR1F3TmpabU9URm1OQQ' where namespace_id = 999974 and item_label = '更多';
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvME1EVmxNMlF6TVdSbU9URXhaREJoTVdKbVpXUmtNVGs1WTJKaE9UaG1NUQ' where namespace_id = 999974 and item_label = '服务联盟';
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvMVl6bGlZVEE1TTJJNFlUSmhZV1JtT0RNeE9XRmxPV0ZoWldVeVpqTXlaQQ' where namespace_id = 999974 and item_label = '物业服务';
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRwaU9XRXlNemt4WldVNE1HUmtNR1V5TURFM1l6VmtaR0V3TURNMk4yTXhZdw' where namespace_id = 999974 and item_label = '移动考勤';
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRwbE16QXlZbVZsTURKbVpXWTJaV1JqWVRFNU1Ua3lOMll6TVRaak1qUmxOdw' where namespace_id = 999974 and item_label = '视频会议';


SET @lease_config_id = (SELECT MAX(id) FROM `eh_lease_configs`); 
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `rent_amount_flag`, `issuing_lease_flag`, `issuer_manage_flag`, `park_indroduce_flag`, `renew_flag`, `area_search_flag`) VALUES ((@lease_config_id := @lease_config_id + 1), '999974', '1', '1', '1', '1', '1', '0');

SET @category_id = (SELECT MAX(id) FROM `eh_categories`);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '亲子与教育', '兴趣/亲子与教育', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999974);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '运动与音乐', '兴趣/运动与音乐', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999974);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '美食与厨艺', '兴趣/美食与厨艺', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999974);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '美容化妆', '兴趣/美容化妆', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999974);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '家庭装饰', '兴趣/家庭装饰', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999974);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '名牌汇', '兴趣/名牌汇', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999974);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '宠物会', '兴趣/宠物会', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999974);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '旅游摄影', '兴趣/旅游摄影', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999974);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '拼车', '兴趣/拼车', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999974);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '老乡群', '兴趣/老乡群', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999974);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '同事群', '兴趣/同事群', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999974);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '同学群', '兴趣/同学群', 0, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999974);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) VALUES ((@category_id := @category_id  + 1), 2, 0, '其他', '兴趣/其他', 1, 2, '2015-09-28 06:09:03', NULL, NULL, NULL, 999974);

delete from eh_launch_pad_items where namespace_id = 999974 and item_label = '一键上网';
update eh_launch_pad_items set display_flag = 0 where namespace_id = 999974 and item_label in('服务联盟', '智能门禁', '视频会议', '企业通讯录');
update eh_rentalv2_resource_types set status = 2 where namespace_id = 999974;