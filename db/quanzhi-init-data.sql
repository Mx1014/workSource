INSERT INTO `eh_namespaces`(`id`, `name`) VALUES(999980, '全至科技创新园');

SET @eh_namespace_details = (SELECT MAX(id) FROM `eh_namespace_details`);
INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES((@eh_namespace_details := @eh_namespace_details + 1), 999980, 'community_commercial', UTC_TIMESTAMP()); 

SET @eh_configurations = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ((@eh_configurations := @eh_configurations + 1), 'app.agreements.url', 'http://core.zuolin.com/mobile/static/app_agreements/zzh_agreements.html', 'the relative path for kexin app agreements', '999980', NULL);

SET @version_realm_id = (SELECT MAX(id) FROM `eh_version_realm`);
SET @version_upgrade_rule_id = (SELECT MAX(id) FROM `eh_version_upgrade_rules`);
INSERT INTO `eh_version_realm` VALUES ((@version_realm_id := @version_realm_id + 1), 'Android_Quanzhi', null, UTC_TIMESTAMP(), '999980');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	values((@version_upgrade_rule_id := @version_upgrade_rule_id + 1),@version_realm_id,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());
	
INSERT INTO `eh_version_realm` VALUES ((@version_realm_id := @version_realm_id + 1), 'iOS_Quanzhi', null, UTC_TIMESTAMP(), '999980');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	values((@version_upgrade_rule_id := @version_upgrade_rule_id + 1),@version_realm_id,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());

INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) 
	VALUES(999980, 'sms.default.yzx', 1, 'zh_CN', '验证码-全至', '37614');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) 
	VALUES(999980, 'sms.default.yzx', 4, 'zh_CN', '派单-全至', '37615');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) 
	VALUES(999980, 'sms.default.yzx', 5, 'zh_CN', '任务-全至', '37616');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) 
	VALUES(999980, 'sms.default.yzx', 6, 'zh_CN', '任务2-全至', '37617');

INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) 
	VALUES(999980, 'sms.default.yzx', 8, 'zh_CN', '门禁-全至', '37618');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) 
	VALUES(999980, 'sms.default.yzx', 7, 'zh_CN', '新报修-全至', '37619');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) 
	VALUES(999980, 'sms.default.yzx', 15, 'zh_CN', '物业任务3-全至', '37620');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) 
	VALUES(999980, 'sms.default.yzx', 12, 'zh_CN', '预定1-全至', '37621');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) 
	VALUES(999980, 'sms.default.yzx', 13, 'zh_CN', '预定2-全至', '37622');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) 
	VALUES(999980, 'sms.default.yzx', 14, 'zh_CN', '预定3-全至', '37623');

SET @eh_categories = (SELECT max(id) FROM `eh_categories`);   
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES((@eh_categories := @eh_categories + 1), 1, 0, '普通', '帖子/普通', 1, 2, UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES((@eh_categories := @eh_categories + 1), 1, 0, '二手和租售', '帖子/二手和租售', 1, 2, UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES((@eh_categories := @eh_categories + 1), 1, 0, '免费物品', '帖子/免费物品', 1, 2, UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES((@eh_categories := @eh_categories + 1), 1, 0, '失物招领', '帖子/失物招领', 1, 2, UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES((@eh_categories := @eh_categories + 1), 1, 0, '紧急通知', '帖子/紧急通知', 1, 2, UTC_TIMESTAMP(), 999980);    

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (248630, UUID(), '13823662850', '陈炯康', 'cs://1/image/aW1hZ2UvTVRvMlkySmhNbVZqTm1SaU1UQXdPREkxWkRjME5HVmxNVFU1TXpBNE5UUTBZdw', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 999980);

SET @eh_user_identifiers = (SELECT max(id) FROM `eh_user_identifiers`);   
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES ((@eh_user_identifiers := @eh_user_identifiers + 1) , 248630 ,  '0',  '13823662850',  '221616',  3, UTC_TIMESTAMP(), 999980);

SET @group_id = 1009450;  
SET @organization_id = 1011090;  	
SET @forum_id = 188100;
SET @feedback_forum_id = @forum_id + 1; 
SET @community_forum_id = @forum_id + 2;   
SET @community_id = 240111044331056650; 
SET @org_member_id = 2123830; 
SET @role_assignment_id = 15485; 
SET @namespace_resource_id = 17800; 
SET @sheng_id = 16005; 
SET @shi_id = @sheng_id + 1; 
SET @qu_id = @shi_id + 1; 
SET @community_geopoint_id = 240111044331052546; 
SET @building1_id = 182720;
SET @building2_id = @building1_id + 1;
SET @building3_id = @building1_id + 2;
SET @building4_id = @building1_id + 3;
SET @building5_id = @building1_id + 4;
SET @building6_id = @building1_id + 5;
SET @address_mapping_id = (SELECT max(id) FROM `eh_organization_address_mappings`);  

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(@group_id, UUID(), '深圳市全至产业新城运营有限公司', '深圳市全至产业新城运营有限公司', 1, 1, @organization_id, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), @forum_id, 1, 999980); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(@forum_id, UUID(), 999980, 2, 'EhGroups', @group_id,'深圳市全至产业新城运营有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());	
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(@organization_id, 0, 'PM', '深圳市全至产业新城运营有限公司', 0, '', CONCAT("/",@organization_id), 1, 2, 'ENTERPRISE', 999980, @group_id);


SET @organization_community_request_id = (SELECT max(id) FROM `eh_organization_community_requests`); 	
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES((@organization_community_request_id := @organization_community_request_id + 1), @community_id, 'organization', @organization_id, 3, 0, UTC_TIMESTAMP());

INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`)
	VALUES(@org_member_id, @organization_id, 'USER', 248630  , 'manager', '陈炯康', 0, '13823662850', 3, 999980);	

INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(@role_assignment_id, 'EhOrganizations', @organization_id, 'EhUsers', 248630  , 1001, 1, UTC_TIMESTAMP());

	

INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES(@namespace_resource_id, 999980, 'COMMUNITY', @community_id, UTC_TIMESTAMP());

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(@community_forum_id, UUID(), 999980, 2, 'EhGroups', 0,'深圳市全至产业新城运营有限公司论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(@feedback_forum_id, UUID(), 999980, 2, 'EhGroups', 0,'深圳市全至产业新城运营有限公司反馈论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 


INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (@sheng_id, '0', '广东', 'GUANGDONG', 'GD', '/广东', '1', '1', '', '', '2', '2', '999980');
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (@shi_id, @sheng_id, '深圳市', 'SHENZHENSHI', 'SZS', '/广东/深圳市', '2', '2', NULL, '0755', '2', '1', '999980');
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (@qu_id, @shi_id, '宝安区', 'NANSHANQU', 'NSQ', '/广东/深圳市/宝安区', '3', '3', NULL, '0755', '2', '0', '999980');


INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES(@community_id, UUID(), @shi_id, '深圳市',  @qu_id, '宝安区', '全至科技创新园', '全至科技创新园', '沙井街道后亭沙松路全至科技创新园', NULL, '全至科技创新园把握社会经济发展大势，紧紧围绕“中国制造2025”国家战略和，面向工业4.0全球标准，以智能装备及机器人为主导产业，着力打造成为研产一体化的中国制造2025示范园区，面向全球竞争的智能制造产业生态圈。作为深圳市重点打造的高端智造载体，集研发办公、高端生产、产品展示、生活商业配套于一体集聚一批工业4.0智能制造领域极具发展潜力的企业和机构。',NULL, NULL, NULL, NULL, NULL, NULL,NULL, 229, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'1', @forum_id, @feedback_forum_id, UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES(@community_geopoint_id, @community_id, '', 113.829877, 22.770851, 'ws0cw323u6k1');
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(@organization_id, @community_id);



INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(@building1_id, @community_id, '科创大厦', '科创大厦', 0, '0755-23308666', '深圳市宝安区沙井街道后亭沙松路全至科技创新园', NULl, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999980);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(@building2_id, @community_id, '壹号楼', '壹号楼', 0, '0755-23308666', '深圳市宝安区沙井街道后亭沙松路全至科技创新园', NULl, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999980);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(@building3_id, @community_id, '贰号楼', '贰号楼', 0, '0755-23308666', '深圳市宝安区沙井街道后亭沙松路全至科技创新园', NULl, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999980);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(@building4_id, @community_id, '叁号楼', '叁号楼', 0, '0755-23308666', '深圳市宝安区沙井街道后亭沙松路全至科技创新园', NULl, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999980);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(@building5_id, @community_id, '佳公寓A栋', '佳公寓A栋', 0, '0755-23308666', '深圳市宝安区沙井街道后亭沙松路全至科技创新园', NULl, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999980);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(@building6_id, @community_id, '佳公寓B栋', '佳公寓B栋', 0, '0755-23308666', '深圳市宝安区沙井街道后亭沙松路全至科技创新园', NULl, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999980);

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138770,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-2A','科创大厦','2A','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138771,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-3A','科创大厦','3A','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138772,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-4A','科创大厦','4A','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138773,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-5A','科创大厦','5A','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138774,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-6A','科创大厦','6A','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138775,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-7A','科创大厦','7A','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138776,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-8A','科创大厦','8A','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138777,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-9A','科创大厦','9A','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138778,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-10A','科创大厦','10A','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138779,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-11A','科创大厦','11A','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138780,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-12A','科创大厦','12A','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138781,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-13A','科创大厦','13A','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138782,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-14A','科创大厦','14A','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138783,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-15A','科创大厦','15A','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138784,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-16A','科创大厦','16A','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138785,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-17A','科创大厦','17A','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138786,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-18A','科创大厦','18A','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138787,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-19A','科创大厦','19A','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138788,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-20A','科创大厦','20A','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138789,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-21A','科创大厦','21A','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138790,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-22A','科创大厦','22A','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138791,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-23A','科创大厦','23A','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138792,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-2B','科创大厦','2B','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138793,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-3B','科创大厦','3B','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138794,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-4B','科创大厦','4B','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138795,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-5B','科创大厦','5B','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138796,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-6B','科创大厦','6B','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138797,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-7B','科创大厦','7B','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138798,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-8B','科创大厦','8B','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138799,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-9B','科创大厦','9B','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138800,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-10B','科创大厦','10B','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138801,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-11B','科创大厦','11B','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138802,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-12B','科创大厦','12B','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138803,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-13B','科创大厦','13B','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138804,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-14B','科创大厦','14B','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138805,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-15B','科创大厦','15B','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138806,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-16B','科创大厦','16B','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138807,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-17B','科创大厦','17B','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138808,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-18B','科创大厦','18B','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138809,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-19B','科创大厦','19B','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138810,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-20B','科创大厦','20B','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138811,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-21B','科创大厦','21B','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138812,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-22B','科创大厦','22B','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138813,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-23B','科创大厦','23B','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138814,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-2C','科创大厦','2C','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138815,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-3C','科创大厦','3C','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138816,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-4C','科创大厦','4C','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138817,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-5C','科创大厦','5C','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138818,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-6C','科创大厦','6C','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138819,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-7C','科创大厦','7C','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138820,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-8C','科创大厦','8C','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138821,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-9C','科创大厦','9C','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138822,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-10C','科创大厦','10C','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138823,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-11C','科创大厦','11C','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138824,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-12C','科创大厦','12C','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138825,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-13C','科创大厦','13C','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138826,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-14C','科创大厦','14C','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138827,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-15C','科创大厦','15C','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138828,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-16C','科创大厦','16C','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138829,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-17C','科创大厦','17C','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138830,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-18C','科创大厦','18C','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138831,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-19C','科创大厦','19C','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138832,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-20C','科创大厦','20C','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138833,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-21C','科创大厦','21C','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138834,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-22C','科创大厦','22C','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138835,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-23C','科创大厦','23C','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138836,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-2D','科创大厦','2D','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138837,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-3D','科创大厦','3D','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138838,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-4D','科创大厦','4D','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138839,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-5D','科创大厦','5D','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138840,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-6D','科创大厦','6D','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138841,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-7D','科创大厦','7D','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138842,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-8D','科创大厦','8D','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138843,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-9D','科创大厦','9D','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138844,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-10D','科创大厦','10D','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138845,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-11D','科创大厦','11D','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138846,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-12D','科创大厦','12D','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138847,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-13D','科创大厦','13D','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138848,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-14D','科创大厦','14D','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138849,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-15D','科创大厦','15D','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138850,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-16D','科创大厦','16D','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138851,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-17D','科创大厦','17D','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138852,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-18D','科创大厦','18D','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138853,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-19D','科创大厦','19D','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138854,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-20D','科创大厦','20D','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138855,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-21D','科创大厦','21D','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138856,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-22D','科创大厦','22D','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138857,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-23D','科创大厦','23D','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138858,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-2E','科创大厦','2E','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138859,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-3E','科创大厦','3E','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138860,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-4E','科创大厦','4E','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138861,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-5E','科创大厦','5E','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138862,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-6E','科创大厦','6E','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138863,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-7E','科创大厦','7E','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138864,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-8E','科创大厦','8E','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138865,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-9E','科创大厦','9E','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138866,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-10E','科创大厦','10E','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138867,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-11E','科创大厦','11E','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138868,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-12E','科创大厦','12E','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138869,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-13E','科创大厦','13E','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138870,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-14E','科创大厦','14E','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138871,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-15E','科创大厦','15E','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138872,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-16E','科创大厦','16E','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138873,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-17E','科创大厦','17E','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138874,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-18E','科创大厦','18E','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138875,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-19E','科创大厦','19E','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138876,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-20E','科创大厦','20E','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138877,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-21E','科创大厦','21E','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138878,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-22E','科创大厦','22E','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138879,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-23E','科创大厦','23E','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138880,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-2F','科创大厦','2F','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138881,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-3F','科创大厦','3F','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138882,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-4F','科创大厦','4F','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138883,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-5F','科创大厦','5F','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138884,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-6F','科创大厦','6F','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138885,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-7F','科创大厦','7F','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138886,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-8F','科创大厦','8F','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138887,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-9F','科创大厦','9F','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138888,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-10F','科创大厦','10F','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138889,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-11F','科创大厦','11F','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138890,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-12F','科创大厦','12F','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138891,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-13F','科创大厦','13F','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138892,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-14F','科创大厦','14F','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138893,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-15F','科创大厦','15F','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138894,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-16F','科创大厦','16F','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138895,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-17F','科创大厦','17F','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138896,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-18F','科创大厦','18F','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138897,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-19F','科创大厦','19F','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138898,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-20F','科创大厦','20F','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138899,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-21F','科创大厦','21F','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138900,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-22F','科创大厦','22F','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138901,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-23F','科创大厦','23F','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138902,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-2G','科创大厦','2G','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138903,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-3G','科创大厦','3G','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138904,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-4G','科创大厦','4G','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138905,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-5G','科创大厦','5G','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138906,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-6G','科创大厦','6G','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138907,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-7G','科创大厦','7G','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138908,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-8G','科创大厦','8G','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138909,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-9G','科创大厦','9G','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138910,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-10G','科创大厦','10G','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138911,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-11G','科创大厦','11G','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138912,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-12G','科创大厦','12G','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138913,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-13G','科创大厦','13G','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138914,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-14G','科创大厦','14G','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138915,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-15G','科创大厦','15G','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138916,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-16G','科创大厦','16G','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138917,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-17G','科创大厦','17G','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138918,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-18G','科创大厦','18G','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138919,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-19G','科创大厦','19G','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138920,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-20G','科创大厦','20G','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138921,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-21G','科创大厦','21G','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138922,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-22G','科创大厦','22G','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138923,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'科创大厦-23G','科创大厦','23G','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138924,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'壹号楼-1J','壹号楼','1J','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138925,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'壹号楼-2J','壹号楼','2J','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138926,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'壹号楼-3J','壹号楼','3J','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138927,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'壹号楼-4J','壹号楼','4J','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138928,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'壹号楼-5J','壹号楼','5J','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138929,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'壹号楼-1K','壹号楼','1K','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138930,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'壹号楼-2K','壹号楼','2K','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138931,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'壹号楼-3K','壹号楼','3K','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138932,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'壹号楼-4K','壹号楼','4K','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138933,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'壹号楼-5K','壹号楼','5K','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138934,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'壹号楼-1L','壹号楼','1L','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138935,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'壹号楼-2L','壹号楼','2L','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138936,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'壹号楼-3L','壹号楼','3L','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138937,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'壹号楼-4L','壹号楼','4L','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138938,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'壹号楼-5L','壹号楼','5L','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138939,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'壹号楼-1M','壹号楼','1M','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138940,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'壹号楼-2M','壹号楼','2M','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138941,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'壹号楼-3M','壹号楼','3M','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138942,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'壹号楼-4M','壹号楼','4M','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138943,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'壹号楼-5M','壹号楼','5M','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138944,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'壹号楼-1H','壹号楼','1H','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138945,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'壹号楼-2H','壹号楼','2H','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138946,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'壹号楼-3H','壹号楼','3H','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138947,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'壹号楼-4H','壹号楼','4H','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138948,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'壹号楼-5H','壹号楼','5H','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138949,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'贰号楼-1P','贰号楼','1P','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138950,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'贰号楼-2P','贰号楼','2P','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138951,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'贰号楼-3P','贰号楼','3P','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138952,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'贰号楼-4P','贰号楼','4P','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138953,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'贰号楼-5P','贰号楼','5P','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138954,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'贰号楼-1Q','贰号楼','1Q','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138955,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'贰号楼-2Q','贰号楼','2Q','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138956,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'贰号楼-3Q','贰号楼','3Q','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138957,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'贰号楼-4Q','贰号楼','4Q','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138958,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'贰号楼-5Q','贰号楼','5Q','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138959,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'贰号楼-1R','贰号楼','1R','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138960,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'贰号楼-2R','贰号楼','2R','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138961,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'贰号楼-3R','贰号楼','3R','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138962,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'贰号楼-4R','贰号楼','4R','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138963,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'贰号楼-5R','贰号楼','5R','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138964,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'贰号楼-1N','贰号楼','1N','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138965,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'贰号楼-2N','贰号楼','2N','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138966,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'贰号楼-3N','贰号楼','3N','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138967,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'贰号楼-4N','贰号楼','4N','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138968,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'贰号楼-5N','贰号楼','5N','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138969,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-1X','叁号楼','1X','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138970,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-2X','叁号楼','2X','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138971,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-3X','叁号楼','3X','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138972,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-4X','叁号楼','4X','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138973,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-5X','叁号楼','5X','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138974,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-1W','叁号楼','1W','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138975,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-2W','叁号楼','2W','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138976,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-3W','叁号楼','3W','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138977,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-4W','叁号楼','4W','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138978,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-5W','叁号楼','5W','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138979,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-1V','叁号楼','1V','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138980,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-2V','叁号楼','2V','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138981,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-3V','叁号楼','3V','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138982,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-4V','叁号楼','4V','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138983,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-5V','叁号楼','5V','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138984,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-1S','叁号楼','1S','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138985,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-2S','叁号楼','2S','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138986,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-3S','叁号楼','3S','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138987,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-4S','叁号楼','4S','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138988,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-5S','叁号楼','5S','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138989,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-1T','叁号楼','1T','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138990,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-2T','叁号楼','2T','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138991,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-3T','叁号楼','3T','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138992,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-4T','叁号楼','4T','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138993,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-5T','叁号楼','5T','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138994,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-1U','叁号楼','1U','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138995,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-2U','叁号楼','2U','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138996,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-3U','叁号楼','3U','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138997,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-4U','叁号楼','4U','2','0',UTC_TIMESTAMP(), 999980);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387138998,UUID(),240111044331056650, 16006, '深圳市',  16007, '宝安区' ,'叁号楼-5U','叁号楼','5U','2','0',UTC_TIMESTAMP(), 999980);

INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138770, '科创大厦-2A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138771, '科创大厦-3A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138772, '科创大厦-4A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138773, '科创大厦-5A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138774, '科创大厦-6A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138775, '科创大厦-7A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138776, '科创大厦-8A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138777, '科创大厦-9A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138778, '科创大厦-10A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138779, '科创大厦-11A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138780, '科创大厦-12A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138781, '科创大厦-13A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138782, '科创大厦-14A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138783, '科创大厦-15A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138784, '科创大厦-16A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138785, '科创大厦-17A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138786, '科创大厦-18A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138787, '科创大厦-19A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138788, '科创大厦-20A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138789, '科创大厦-21A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138790, '科创大厦-22A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138791, '科创大厦-23A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138792, '科创大厦-2B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138793, '科创大厦-3B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138794, '科创大厦-4B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138795, '科创大厦-5B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138796, '科创大厦-6B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138797, '科创大厦-7B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138798, '科创大厦-8B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138799, '科创大厦-9B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138800, '科创大厦-10B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138801, '科创大厦-11B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138802, '科创大厦-12B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138803, '科创大厦-13B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138804, '科创大厦-14B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138805, '科创大厦-15B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138806, '科创大厦-16B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138807, '科创大厦-17B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138808, '科创大厦-18B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138809, '科创大厦-19B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138810, '科创大厦-20B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138811, '科创大厦-21B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138812, '科创大厦-22B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138813, '科创大厦-23B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138814, '科创大厦-2C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138815, '科创大厦-3C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138816, '科创大厦-4C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138817, '科创大厦-5C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138818, '科创大厦-6C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138819, '科创大厦-7C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138820, '科创大厦-8C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138821, '科创大厦-9C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138822, '科创大厦-10C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138823, '科创大厦-11C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138824, '科创大厦-12C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138825, '科创大厦-13C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138826, '科创大厦-14C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138827, '科创大厦-15C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138828, '科创大厦-16C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138829, '科创大厦-17C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138830, '科创大厦-18C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138831, '科创大厦-19C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138832, '科创大厦-20C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138833, '科创大厦-21C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138834, '科创大厦-22C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138835, '科创大厦-23C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138836, '科创大厦-2D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138837, '科创大厦-3D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138838, '科创大厦-4D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138839, '科创大厦-5D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138840, '科创大厦-6D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138841, '科创大厦-7D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138842, '科创大厦-8D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138843, '科创大厦-9D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138844, '科创大厦-10D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138845, '科创大厦-11D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138846, '科创大厦-12D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138847, '科创大厦-13D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138848, '科创大厦-14D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138849, '科创大厦-15D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138850, '科创大厦-16D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138851, '科创大厦-17D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138852, '科创大厦-18D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138853, '科创大厦-19D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138854, '科创大厦-20D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138855, '科创大厦-21D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138856, '科创大厦-22D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138857, '科创大厦-23D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138858, '科创大厦-2E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138859, '科创大厦-3E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138860, '科创大厦-4E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138861, '科创大厦-5E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138862, '科创大厦-6E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138863, '科创大厦-7E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138864, '科创大厦-8E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138865, '科创大厦-9E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138866, '科创大厦-10E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138867, '科创大厦-11E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138868, '科创大厦-12E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138869, '科创大厦-13E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138870, '科创大厦-14E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138871, '科创大厦-15E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138872, '科创大厦-16E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138873, '科创大厦-17E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138874, '科创大厦-18E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138875, '科创大厦-19E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138876, '科创大厦-20E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138877, '科创大厦-21E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138878, '科创大厦-22E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138879, '科创大厦-23E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138880, '科创大厦-2F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138881, '科创大厦-3F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138882, '科创大厦-4F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138883, '科创大厦-5F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138884, '科创大厦-6F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138885, '科创大厦-7F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138886, '科创大厦-8F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138887, '科创大厦-9F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138888, '科创大厦-10F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138889, '科创大厦-11F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138890, '科创大厦-12F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138891, '科创大厦-13F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138892, '科创大厦-14F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138893, '科创大厦-15F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138894, '科创大厦-16F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138895, '科创大厦-17F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138896, '科创大厦-18F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138897, '科创大厦-19F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138898, '科创大厦-20F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138899, '科创大厦-21F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138900, '科创大厦-22F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138901, '科创大厦-23F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138902, '科创大厦-2G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138903, '科创大厦-3G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138904, '科创大厦-4G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138905, '科创大厦-5G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138906, '科创大厦-6G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138907, '科创大厦-7G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138908, '科创大厦-8G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138909, '科创大厦-9G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138910, '科创大厦-10G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138911, '科创大厦-11G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138912, '科创大厦-12G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138913, '科创大厦-13G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138914, '科创大厦-14G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138915, '科创大厦-15G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138916, '科创大厦-16G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138917, '科创大厦-17G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138918, '科创大厦-18G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138919, '科创大厦-19G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138920, '科创大厦-20G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138921, '科创大厦-21G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138922, '科创大厦-22G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138923, '科创大厦-23G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138924, '壹号楼-1J', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138925, '壹号楼-2J', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138926, '壹号楼-3J', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138927, '壹号楼-4J', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138928, '壹号楼-5J', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138929, '壹号楼-1K', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138930, '壹号楼-2K', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138931, '壹号楼-3K', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138932, '壹号楼-4K', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138933, '壹号楼-5K', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138934, '壹号楼-1L', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138935, '壹号楼-2L', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138936, '壹号楼-3L', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138937, '壹号楼-4L', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138938, '壹号楼-5L', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138939, '壹号楼-1M', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138940, '壹号楼-2M', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138941, '壹号楼-3M', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138942, '壹号楼-4M', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138943, '壹号楼-5M', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138944, '壹号楼-1H', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138945, '壹号楼-2H', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138946, '壹号楼-3H', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138947, '壹号楼-4H', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138948, '壹号楼-5H', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138949, '贰号楼-1P', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138950, '贰号楼-2P', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138951, '贰号楼-3P', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138952, '贰号楼-4P', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138953, '贰号楼-5P', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138954, '贰号楼-1Q', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138955, '贰号楼-2Q', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138956, '贰号楼-3Q', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138957, '贰号楼-4Q', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138958, '贰号楼-5Q', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138959, '贰号楼-1R', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138960, '贰号楼-2R', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138961, '贰号楼-3R', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138962, '贰号楼-4R', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138963, '贰号楼-5R', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138964, '贰号楼-1N', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138965, '贰号楼-2N', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138966, '贰号楼-3N', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138967, '贰号楼-4N', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138968, '贰号楼-5N', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138969, '叁号楼-1X', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138970, '叁号楼-2X', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138971, '叁号楼-3X', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138972, '叁号楼-4X', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138973, '叁号楼-5X', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138974, '叁号楼-1W', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138975, '叁号楼-2W', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138976, '叁号楼-3W', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138977, '叁号楼-4W', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138978, '叁号楼-5W', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138979, '叁号楼-1V', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138980, '叁号楼-2V', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138981, '叁号楼-3V', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138982, '叁号楼-4V', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138983, '叁号楼-5V', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138984, '叁号楼-1S', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138985, '叁号楼-2S', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138986, '叁号楼-3S', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138987, '叁号楼-4S', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138988, '叁号楼-5S', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138989, '叁号楼-1T', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138990, '叁号楼-2T', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138991, '叁号楼-3T', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138992, '叁号楼-4T', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138993, '叁号楼-5T', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138994, '叁号楼-1U', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138995, '叁号楼-2U', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138996, '叁号楼-3U', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138997, '叁号楼-4U', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@address_mapping_id := @address_mapping_id + 1), 1011090, 240111044331056650, 239825274387138998, '叁号楼-5U', '0');

	
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES (10510, '会议室预订', 0, NULL, 0, 999980);
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES (10511, '电子屏预订', 0, NULL, 0, 999980);
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES (10512, '场地预约', 0, NULL, 0, 999980);
	
SET @layout_id = (SELECT max(id) FROM `eh_launch_pad_layouts`);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) VALUES ((@layout_id := @layout_id + 1) ,999980,'ServiceMarketLayout','{\"displayName\": \"\服务市场\", \"groups\": [{\"defaultOrder\": 1, \"groupName\": \"\", \"instanceConfig\": {\"itemGroup\": \"Default\"}, \"separatorFlag\": 0, \"separatorHeight\": 0, \"style\": \"Default\", \"widget\": \"Banners\"}, {\"columnCount\": 4, \"defaultOrder\": 2, \"groupName\": \"\", \"instanceConfig\": {\"itemGroup\": \"Gallery\"}, \"separatorFlag\": 1, \"separatorHeight\": 21, \"style\": \"Default\", \"widget\": \"Coupons\"}, {\"defaultOrder\": 3, \"groupName\": \"\\u5546\\u5bb6\\u670d\\u52a1\", \"instanceConfig\": {\"itemGroup\": \"Bizs\"}, \"separatorFlag\": 0, \"separatorHeight\": 0, \"style\": \"Default\", \"widget\": \"Navigator\"}, {\"defaultOrder\": 4, \"groupName\": \"\", \"instanceConfig\": {\"itemGroup\": \"wSdgPleaseChangeItToBeOnlyOne\"}, \"separatorFlag\": 1, \"separatorHeight\": 21, \"style\": \"Default\", \"widget\": \"Bulletins\"}, {\"columnCount\": 4, \"defaultOrder\": 5, \"groupName\": \"\", \"instanceConfig\": {\"itemGroup\": \"JMvTPleaseChangeItToBeOnlyOne\"}, \"separatorFlag\": 0, \"separatorHeight\": 0, \"style\": \"Default\", \"widget\": \"Coupons\"}], \"layoutName\": \"ServiceMarketLayout\", \"versionCode\": \"2017022340\", \"versionName\": \"4.1.3\"}',2017022340,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) VALUES ((@layout_id := @layout_id + 1),999980,'leaseLayout','{\"displayName\": \"\招商租赁\", \"groups\": [{\"defaultOrder\": 1, \"groupName\": \"\", \"instanceConfig\": {\"itemGroup\": \"6ei4\"}, \"separatorFlag\": 0, \"separatorHeight\": 0, \"style\": \"Default\", \"widget\": \"Navigator\"}], \"layoutName\": \"1uofZjLayout\", \"versionCode\": \"2017022340\", \"versionName\": \"3.3.0\"}',2017022340,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);

INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) VALUES ((@layout_id := @layout_id + 1) ,999980,'ServiceMarketLayout','{\"displayName\": \"\服务市场\", \"groups\": [{\"defaultOrder\": 1, \"groupName\": \"\", \"instanceConfig\": {\"itemGroup\": \"Default\"}, \"separatorFlag\": 0, \"separatorHeight\": 0, \"style\": \"Default\", \"widget\": \"Banners\"}, {\"columnCount\": 4, \"defaultOrder\": 2, \"groupName\": \"\", \"instanceConfig\": {\"itemGroup\": \"Gallery\"}, \"separatorFlag\": 1, \"separatorHeight\": 21, \"style\": \"Default\", \"widget\": \"Coupons\"}, {\"defaultOrder\": 3, \"groupName\": \"\\u5546\\u5bb6\\u670d\\u52a1\", \"instanceConfig\": {\"itemGroup\": \"Bizs\"}, \"separatorFlag\": 0, \"separatorHeight\": 0, \"style\": \"Default\", \"widget\": \"Navigator\"}, {\"defaultOrder\": 4, \"groupName\": \"\", \"instanceConfig\": {\"itemGroup\": \"wSdgPleaseChangeItToBeOnlyOne\"}, \"separatorFlag\": 1, \"separatorHeight\": 21, \"style\": \"Default\", \"widget\": \"Bulletins\"}, {\"columnCount\": 4, \"defaultOrder\": 5, \"groupName\": \"\", \"instanceConfig\": {\"itemGroup\": \"JMvTPleaseChangeItToBeOnlyOne\"}, \"separatorFlag\": 0, \"separatorHeight\": 0, \"style\": \"Default\", \"widget\": \"Coupons\"}], \"layoutName\": \"ServiceMarketLayout\", \"versionCode\": \"2017022340\", \"versionName\": \"4.1.3\"}',2017022340,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) VALUES ((@layout_id := @layout_id + 1),999980,'leaseLayout','{\"displayName\": \"\招商租赁\", \"groups\": [{\"defaultOrder\": 1, \"groupName\": \"\", \"instanceConfig\": {\"itemGroup\": \"6ei4\"}, \"separatorFlag\": 0, \"separatorHeight\": 0, \"style\": \"Default\", \"widget\": \"Navigator\"}], \"layoutName\": \"1uofZjLayout\", \"versionCode\": \"2017022340\", \"versionName\": \"3.3.0\"}',2017022340,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);

SET @banner_id = (SELECT max(id) FROM `eh_banners`);
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`, `update_time`) VALUES ((@banner_id := @banner_id + 1),999980,0,'/home','Default',0,0,'','','cs://1/image/aW1hZ2UvTVRwallXVTBNak16Wm1SaE5qVXhabUppTmpBME16UTVaakkwTXpFNE5qSXdOZw',0,'{}',NULL,NULL,2,10,0,UTC_TIMESTAMP(),NULL,'pm_admin',0,NULL);
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`, `update_time`) VALUES ((@banner_id := @banner_id + 1),999980,0,'/home','Default',0,0,'','','cs://1/image/aW1hZ2UvTVRvek1tSmlZMkV4WVdJMk0yVXdaRGcwT0RVNE5qTTRaV0k0TVRRME5XWXdOZw',0,'{}',NULL,NULL,2,10,0,UTC_TIMESTAMP(),NULL,'pm_admin',0,NULL);

INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`, `update_time`) VALUES ((@banner_id := @banner_id + 1),999980,0,'/home','Default',0,0,'','','cs://1/image/aW1hZ2UvTVRwallXVTBNak16Wm1SaE5qVXhabUppTmpBME16UTVaakkwTXpFNE5qSXdOZw',0,'{}',NULL,NULL,2,10,0,UTC_TIMESTAMP(),NULL,'park_tourist',0,NULL);
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`, `update_time`) VALUES ((@banner_id := @banner_id + 1),999980,0,'/home','Default',0,0,'','','cs://1/image/aW1hZ2UvTVRvek1tSmlZMkV4WVdJMk0yVXdaRGcwT0RVNE5qTTRaV0k0TVRRME5XWXdOZw',0,'{}',NULL,NULL,2,10,0,UTC_TIMESTAMP(),NULL,'park_tourist',0,NULL);


SET @item_id = (SELECT max(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home','Gallery','','产业服务','cs://1/image/aW1hZ2UvTVRwaFkyVTJZek0xWVRBd1pUazRNMkprTm1Ga05qUXdNemRsWWpRMFpXUTRPQQ',2,1,14,'{\"url\": \"http://zuolin.com/mobile/static/coming_soon/index.html\"}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home','Gallery','','招商租赁','cs://1/image/aW1hZ2UvTVRvM04yRmpaVGcxWkdKaE56UTRPR00wTlRRME1qZGtaV1kwTTJZM016UXlaZw',2,1,2,'{\"itemLocation\": \"/home/lease\", \"layoutName\": \"leaseLayout\", \"title\": \"招商租赁\"}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home','Bizs','','智能门禁','cs://1/image/aW1hZ2UvTVRvM04yWTBNRFprWm1JMk9EVTJNamxoT0RFd1pUZ3haR0prTnpFek5UY3hNUQ',1,1,40,'{}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home','Bizs','','物业报修','cs://1/image/aW1hZ2UvTVRwbFlqRmlaREJtTnpnek56RTRZVGd5TkdNMk9ETm1ZakJsT0RrME56QTROUQ',1,1,51,'{}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home','Bizs','','服务热线','cs://1/image/aW1hZ2UvTVRvMFpqY3haakptWmpabFkySmtOakV6Tm1FeVl6STVNemRrWTJabU1EVXlaQQ',1,1,45,'{}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home','Bizs','','办事指南','cs://1/image/aW1hZ2UvTVRveFpUZ3dPVGczTWpjMllUWXhZV0ZqWm1Sak16Vm1ZamRrWTJVNU56WXhaZw',1,1,33,'{}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home','Bizs','','停车缴费','cs://1/image/aW1hZ2UvTVRvMU1HTTJabUV5TkRjeU1UTTNPVFZtWTJNMFpEZzROVE5tTVRWak5tWXdOdw',1,1,30,'{}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home','Bizs','','园区快讯','cs://1/image/aW1hZ2UvTVRvMk5UYzFNREkxWVRaaFlqUXdPR016T0dOa05qWmtaVGhoTVRReU5XSTVaZw',1,1,48,'{\"categoryId\": \"\"}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home','Bizs','','园区巴士','cs://1/image/aW1hZ2UvTVRvd05ESTJPRFEwTmpVME1tRm1aR1ppTVdKbE1qUmhOekZpTm1JMk1UaGlNZw',1,1,14,'{\"url\": \"\"}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home','Bizs','','企业服务','cs://1/image/aW1hZ2UvTVRvNU1EazRNREk0TURka01XSmtNek01WlRKaFpXTmtNMlUwT0Rrd01qYzBPQQ',1,1,14,'{\"url\": \"http://zuolin.com/mobile/static/coming_soon/index.html\"}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home','Gallery','','左邻小站','cs://1/image/aW1hZ2UvTVRvMk9EUmxZamxqTVdVMll6Y3pabUpqWldObFptWmpNalZpT1dNelpEVmhOUQ',4,1,14,'{\"url\": \"\"}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home/lease','lease','','园区介绍','cs://1/image/aW1hZ2UvTVRwbE1tRXhNV1ZpTXpKaFltUTRPR1kwTjJFNU5tSTJNVEk1WVRCbE1UTXlZUQ',1,1,14,'{\"url\": \"\"}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home/lease','lease','','园区企业','cs://1/image/aW1hZ2UvTVRveVlUZ3hPRGczWWpGa00yTmxNbVl4WXpBNVpXVTNNMkpsWkRVeVpUZzJPUQ',1,1,34,'{\"ownerId\": \"\", \"ownerType\": \"\", \"type\": \"3\"}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home/lease','lease','','园区入驻','cs://1/image/aW1hZ2UvTVRwa1lXWTJOV1kxWWpreU5UVTFOV1k0T0dFeU1UTmxaalEzWldRMllqQmtOdw',1,1,28,'{}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home/lease','lease','','会议室预订','cs://1/image/aW1hZ2UvTVRwbFlqaGpNemsyTldNMk1UazJOekl4WW1RNE0yWXpPVEZsT1RnNE9ESTRPQQ',1,1,49,'{\"resourceTypeId\":10510, \"pageType\": \"0\"}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home/lease','lease','','电子屏预订','cs://1/image/aW1hZ2UvTVRvNU5UTmpPV00yWkRZd09UZ3haRGMwTUdKbE5qTTVNVFV6WVdRNE4yVmlZZw',1,1,49,'{\"resourceTypeId\":10511, \"pageType\": \"0\"}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home/lease','lease','','场地预约','cs://1/image/aW1hZ2UvTVRwbVptTmtaV015TWpKaU56YzFPRGxqWXpnNFlUWXhOelV5TnpRNVl6VXdNZw',1,1,49,'{\"resourceTypeId\":10512, \"pageType\": \"0\"}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'pm_admin',0,NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home','Gallery','','产业服务','cs://1/image/aW1hZ2UvTVRwaFkyVTJZek0xWVRBd1pUazRNMkprTm1Ga05qUXdNemRsWWpRMFpXUTRPQQ',2,1,14,'{\"url\": \"http://zuolin.com/mobile/static/coming_soon/index.html\"}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'park_tourist',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home','Gallery','','招商租赁','cs://1/image/aW1hZ2UvTVRvM04yRmpaVGcxWkdKaE56UTRPR00wTlRRME1qZGtaV1kwTTJZM016UXlaZw',2,1,2,'{\"itemLocation\": \"/home/lease\", \"layoutName\": \"leaseLayout\", \"title\": \"招商租赁\"}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'park_tourist',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home','Bizs','','智能门禁','cs://1/image/aW1hZ2UvTVRvM04yWTBNRFprWm1JMk9EVTJNamxoT0RFd1pUZ3haR0prTnpFek5UY3hNUQ',1,1,40,'{}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'park_tourist',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home','Bizs','','物业报修','cs://1/image/aW1hZ2UvTVRwbFlqRmlaREJtTnpnek56RTRZVGd5TkdNMk9ETm1ZakJsT0RrME56QTROUQ',1,1,51,'{}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'park_tourist',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home','Bizs','','服务热线','cs://1/image/aW1hZ2UvTVRvMFpqY3haakptWmpabFkySmtOakV6Tm1FeVl6STVNemRrWTJabU1EVXlaQQ',1,1,45,'{}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'park_tourist',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home','Bizs','','办事指南','cs://1/image/aW1hZ2UvTVRveFpUZ3dPVGczTWpjMllUWXhZV0ZqWm1Sak16Vm1ZamRrWTJVNU56WXhaZw',1,1,33,'{}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'park_tourist',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home','Bizs','','停车缴费','cs://1/image/aW1hZ2UvTVRvMU1HTTJabUV5TkRjeU1UTTNPVFZtWTJNMFpEZzROVE5tTVRWak5tWXdOdw',1,1,30,'{}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'park_tourist',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home','Bizs','','园区快讯','cs://1/image/aW1hZ2UvTVRvMk5UYzFNREkxWVRaaFlqUXdPR016T0dOa05qWmtaVGhoTVRReU5XSTVaZw',1,1,48,'{\"categoryId\": \"\"}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'park_tourist',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home','Bizs','','园区巴士','cs://1/image/aW1hZ2UvTVRvd05ESTJPRFEwTmpVME1tRm1aR1ppTVdKbE1qUmhOekZpTm1JMk1UaGlNZw',1,1,14,'{\"url\": \"\"}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'park_tourist',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home','Bizs','','企业服务','cs://1/image/aW1hZ2UvTVRvNU1EazRNREk0TURka01XSmtNek01WlRKaFpXTmtNMlUwT0Rrd01qYzBPQQ',1,1,14,'{\"url\": \"http://zuolin.com/mobile/static/coming_soon/index.html\"}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'park_tourist',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home','Gallery','','左邻小站','cs://1/image/aW1hZ2UvTVRvMk9EUmxZamxqTVdVMll6Y3pabUpqWldObFptWmpNalZpT1dNelpEVmhOUQ',4,1,14,'{\"url\": \"\"}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'park_tourist',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home/lease','lease','','园区介绍','cs://1/image/aW1hZ2UvTVRwbE1tRXhNV1ZpTXpKaFltUTRPR1kwTjJFNU5tSTJNVEk1WVRCbE1UTXlZUQ',1,1,14,'{\"url\": \"\"}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'park_tourist',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home/lease','lease','','园区企业','cs://1/image/aW1hZ2UvTVRveVlUZ3hPRGczWWpGa00yTmxNbVl4WXpBNVpXVTNNMkpsWkRVeVpUZzJPUQ',1,1,34,'{\"ownerId\": \"\", \"ownerType\": \"\", \"type\": \"3\"}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'park_tourist',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home/lease','lease','','园区入驻','cs://1/image/aW1hZ2UvTVRwa1lXWTJOV1kxWWpreU5UVTFOV1k0T0dFeU1UTmxaalEzWldRMllqQmtOdw',1,1,28,'{}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'park_tourist',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home/lease','lease','','会议室预订','cs://1/image/aW1hZ2UvTVRwbFlqaGpNemsyTldNMk1UazJOekl4WW1RNE0yWXpPVEZsT1RnNE9ESTRPQQ',1,1,49,'{\"resourceTypeId\":10510, \"pageType\": \"0\"}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'park_tourist',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home/lease','lease','','电子屏预订','cs://1/image/aW1hZ2UvTVRvNU5UTmpPV00yWkRZd09UZ3haRGMwTUdKbE5qTTVNVFV6WVdRNE4yVmlZZw',1,1,49,'{\"resourceTypeId\":10511, \"pageType\": \"0\"}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'park_tourist',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1),999980,0,0,0,'/home/lease','lease','','场地预约','cs://1/image/aW1hZ2UvTVRwbVptTmtaV015TWpKaU56YzFPRGxqWXpnNFlUWXhOelV5TnpRNVl6VXdNZw',1,1,49,'{\"resourceTypeId\":10512, \"pageType\": \"0\"}',0,0,1,1,'1',0,NULL,NULL,NULL,0,'park_tourist',0,NULL);
    
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10000,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10100,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10400,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10600,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10610,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10750,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10751,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10752,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),11000,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20400,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20410,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20420,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40000,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40300,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40400,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40410,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40420,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40430,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40440,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40500,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40510,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40520,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40530,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40540,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41300,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41310,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41320,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41000,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41010,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41020,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41030,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41040,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41050,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41060,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),30000,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),30500,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),31000,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),32000,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),33000,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),34000,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),35000,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50000,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50100,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50110,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50200,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50210,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50220,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50300,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50400,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50500,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50600,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50630,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50631,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50632,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50633,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50640,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50650,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50651,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50652,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50653,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56161,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50700,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50710,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50720,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50730,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),60000,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),60100,'', 'EhNamespaces', 999980,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),60200,'', 'EhNamespaces', 999980,2);


SET @module_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`) 
SELECT (@module_id := @module_id + 1), owner_id, menu_id, '', NULL, '2' FROM eh_web_menu_scopes WHERE 
menu_id IN (select id from eh_service_modules) AND `owner_id` = 999980;
