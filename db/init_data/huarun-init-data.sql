INSERT INTO `eh_namespaces`(`id`, `name`) VALUES(999985, '华润');

INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES(1120, 999985, 'community_commercial', '2016-10-21 18:07:50'); 

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES (806, 'app.agreements.url', 'http://officeasy.zuolin.com/mobile/static/app_agreements/officeasy_agreements.html', 'the relative path for huarun app agreements', '999985', NULL);

INSERT INTO `eh_version_realm` VALUES ('70', 'Android_Huarun', null, UTC_TIMESTAMP(), '999985');
INSERT INTO `eh_version_realm` VALUES ('71', 'iOS_Huarun', null, UTC_TIMESTAMP(), '999985');

insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	values(151,70,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	values(152,71,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());

INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) 
	VALUES(999985, 'sms.default.yzx', 1, 'zh_CN', '验证码-华润', '30888');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) 
	VALUES(999985, 'sms.default.yzx', 4, 'zh_CN', '派单-华润', '30889');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) 
	VALUES(999985, 'sms.default.yzx', 6, 'zh_CN', '任务2-华润', '30891');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('sms.default.yzx', '11', 'zh_CN', '物业任务-华润', '30895', '999985');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('sms.default.yzx', '10', 'zh_CN', '物业任务2-华润', '30896', '999985');

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (238716  , UUID(), '9202216', '闫杨', 'cs://1/image/aW1hZ2UvTVRvMlkySmhNbVZqTm1SaU1UQXdPREkxWkRjME5HVmxNVFU1TXpBNE5UUTBZdw', 1, 45, '1', '2',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (233212 , 238716  ,  '0',  '18664367996',  '221616',  3, UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (238717  , UUID(), '9202217', '姜华盛', 'cs://1/image/aW1hZ2UvTVRvMlkySmhNbVZqTm1SaU1UQXdPREkxWkRjME5HVmxNVFU1TXpBNE5UUTBZdw', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (233213 , 238717  ,  '0',  '18601155930',  '221616',  3, UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (238718  , UUID(), '9202218', '姜璐', 'cs://1/image/aW1hZ2UvTVRvMlkySmhNbVZqTm1SaU1UQXdPREkxWkRjME5HVmxNVFU1TXpBNE5UUTBZdw', 1, 45, '1', '2',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (233214 , 238718  ,  '0',  '13924632771',  '221616',  3, UTC_TIMESTAMP(), 999985);

	
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`) 
	VALUES(1007144, 0, 'PM', '华润置地', 0, '', '/1007144', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1115951, 240111044331055035, 'organization', 1007144, 3, 0, UTC_TIMESTAMP());

INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`)
	VALUES(2114977, 1007144, 'USER', 238716  , 'manager', '闫杨', 0, '18664367996', 3, 999985);	
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(12501, 'EhOrganizations', 1007144, 'EhUsers', 238716  , 1001, 1, UTC_TIMESTAMP());
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`)
	VALUES(2114978, 1007144, 'USER', 238717  , 'manager', '姜华盛', 0, '18601155930', 3, 999985);	
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(12502, 'EhOrganizations', 1007144, 'EhUsers', 238717  , 1001, 1, UTC_TIMESTAMP());
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`)
	VALUES(2114979, 1007144, 'USER', 238718  , 'manager', '姜璐', 0, '13924632771', 3, 999985);	
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(12503, 'EhOrganizations', 1007144, 'EhUsers', 238718  , 1001, 1, UTC_TIMESTAMP());

	
INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES(1674, 999985, 'COMMUNITY', 240111044331055035, UTC_TIMESTAMP());

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(184834, UUID(), 999985, 2, 'EhGroups', 0,'华润置地大厦E座论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(184835, UUID(), 999985, 2, 'EhGroups', 0,'华润置地大厦E座意见反馈论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1007016, UUID(), '华润置地', '华润置地', 1, 1, 1007144, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184836, 1, 999985); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(184836, UUID(), 999985, 2, 'EhGroups', 1007016,'华润置地','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());

INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES ('15302', '0', '广东', 'GUANGDONG', 'GD', '/广东', '1', '1', '', '', '2', '2', '999985');
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES ('15303', '15302', '深圳市', 'SHENZHENSHI', 'SZS', '/广东/深圳市', '2', '2', NULL, '0755', '2', '1', '999985');
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES ('15304', '15303', '南山区', 'NANSHANQU', 'NSQ', '/广东/深圳市/南山区', '3', '3', NULL, '0755', '2', '0', '999985');

	
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES(240111044331055035, UUID(), 15303, '深圳市',  15304, '南山区', '华润置地大厦E座', '华润置地大厦E座', '深圳市南山区大冲一路18号', NULL, '华润置地大厦，以“中国首个城市人文综合体”华润城为依托，坐拥280万m2人文综合体多元配套——“创新升级版万象城”万象天地、城市高端住宅润府、精品公寓、五星级酒店、综合书店、人文剧场…更拥有山、海、河、公园、高尔夫五大稀缺景观资源。华润置地大厦E座，作为深圳高新园片区、深南大道唯一可售的地铁上盖写字楼，备受明星企业追捧。其中，全球通讯科技企业龙头、台湾上市公司--联发科技及全国著名商贸物流中心开发商、香港上市公司--毅德国际，合共布局商务空间超15000m2，对其他企业的入驻有一呼百应的典范作用。企业入驻属于“深南大道地标级写字楼群落”的华润置地大厦E座，无需科技园入园认证，即可与IT巨头为邻。',
	NULL, NULL, NULL, NULL, NULL, NULL,NULL, 168, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'1', 184834, 184835, UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES(240111044331051438, 240111044331055035, '', 113.960095, 22.547367, 'ws1030x5ytve');
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(1007144, 240111044331055035);
	
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(181507, 240111044331055035, '华润置地大厦E座', '华润置地大厦E座', 0, NULL, '广东省深圳市南山区大冲路18号', NULl, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999985);

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127703,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-3F','华润置地大厦E座','3F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127704,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-4F','华润置地大厦E座','4F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127705,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-6F','华润置地大厦E座','6F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127706,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-8A1','华润置地大厦E座','8A1','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127707,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-8A2','华润置地大厦E座','8A2','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127708,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-8B','华润置地大厦E座','8B','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127709,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-8B2','华润置地大厦E座','8B2','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127710,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-8C','华润置地大厦E座','8C','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127711,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-8D','华润置地大厦E座','8D','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127712,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-8E','华润置地大厦E座','8E','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127713,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-8F','华润置地大厦E座','8F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127714,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-9F','华润置地大厦E座','9F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127715,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-10F','华润置地大厦E座','10F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127716,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-11F','华润置地大厦E座','11F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127717,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-12F','华润置地大厦E座','12F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127718,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-13F','华润置地大厦E座','13F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127719,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-14A','华润置地大厦E座','14A','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127720,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-14B1','华润置地大厦E座','14B1','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127721,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-14B2','华润置地大厦E座','14B2','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127722,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-14C','华润置地大厦E座','14C','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127723,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-14D','华润置地大厦E座','14D','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127724,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-14E1','华润置地大厦E座','14E1','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127725,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-14E2','华润置地大厦E座','14E2','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127726,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-15A','华润置地大厦E座','15A','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127727,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-15B','华润置地大厦E座','15B','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127728,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-15C','华润置地大厦E座','15C','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127729,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-15D','华润置地大厦E座','15D','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127730,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-15E','华润置地大厦E座','15E','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127731,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-15F','华润置地大厦E座','15F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127732,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-16A','华润置地大厦E座','16A','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127733,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-16B','华润置地大厦E座','16B','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127734,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-16C','华润置地大厦E座','16C','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127735,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-16D','华润置地大厦E座','16D','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127736,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-16F','华润置地大厦E座','16F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127737,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-17A','华润置地大厦E座','17A','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127738,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-17B','华润置地大厦E座','17B','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127739,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-17C','华润置地大厦E座','17C','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127740,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-17D','华润置地大厦E座','17D','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127741,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-18A','华润置地大厦E座','18A','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127742,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-18B','华润置地大厦E座','18B','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127743,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-18C','华润置地大厦E座','18C','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127744,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-18D','华润置地大厦E座','18D','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127745,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-18E','华润置地大厦E座','18E','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127746,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-18F','华润置地大厦E座','18F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127747,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-18G','华润置地大厦E座','18G','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127748,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-18H','华润置地大厦E座','18H','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127749,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-18I','华润置地大厦E座','18I','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127750,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-19A','华润置地大厦E座','19A','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127751,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-19B','华润置地大厦E座','19B','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127752,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-19C','华润置地大厦E座','19C','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127753,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-19D','华润置地大厦E座','19D','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127754,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-19E','华润置地大厦E座','19E','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127755,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-19F','华润置地大厦E座','19F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127756,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-20F','华润置地大厦E座','20F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127757,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-21A','华润置地大厦E座','21A','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127758,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-21B','华润置地大厦E座','21B','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127759,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-21C','华润置地大厦E座','21C','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127760,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-21D','华润置地大厦E座','21D','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127761,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-21E','华润置地大厦E座','21E','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127762,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-21F','华润置地大厦E座','21F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127763,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-23A','华润置地大厦E座','23A','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127764,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-23B','华润置地大厦E座','23B','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127765,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-23C','华润置地大厦E座','23C','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127766,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-23D','华润置地大厦E座','23D','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127767,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-23E','华润置地大厦E座','23E','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127768,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-23F','华润置地大厦E座','23F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127769,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-23H','华润置地大厦E座','23H','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127770,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-23I','华润置地大厦E座','23I','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127771,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-24F','华润置地大厦E座','24F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127772,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-25A','华润置地大厦E座','25A','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127773,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-25B','华润置地大厦E座','25B','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127774,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-25C','华润置地大厦E座','25C','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127775,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-25D','华润置地大厦E座','25D','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127776,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-25E','华润置地大厦E座','25E','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127777,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-25F','华润置地大厦E座','25F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127778,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-26A1','华润置地大厦E座','26A1','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127779,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-26A2','华润置地大厦E座','26A2','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127780,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-26B','华润置地大厦E座','26B','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127781,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-26C','华润置地大厦E座','26C','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127782,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-26D','华润置地大厦E座','26D','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127783,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-26E','华润置地大厦E座','26E','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127784,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-26F','华润置地大厦E座','26F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127785,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-26G','华润置地大厦E座','26G','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127786,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-27A','华润置地大厦E座','27A','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127787,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-27B','华润置地大厦E座','27B','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127788,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-27C','华润置地大厦E座','27C','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127789,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-27D','华润置地大厦E座','27D','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127790,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-27E','华润置地大厦E座','27E','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127791,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-27F','华润置地大厦E座','27F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127792,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-27G','华润置地大厦E座','27G','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127793,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-27H1','华润置地大厦E座','27H1','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127794,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-27H2','华润置地大厦E座','27H2','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127795,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-27I','华润置地大厦E座','27I','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127796,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-27J','华润置地大厦E座','27J','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127797,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-28A','华润置地大厦E座','28A','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127798,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-28B','华润置地大厦E座','28B','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127799,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-28C','华润置地大厦E座','28C','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127800,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-28D1','华润置地大厦E座','28D1','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127801,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-28D2','华润置地大厦E座','28D2','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127802,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-28E','华润置地大厦E座','28E','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127803,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-28F','华润置地大厦E座','28F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127804,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-28G','华润置地大厦E座','28G','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127805,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-29A','华润置地大厦E座','29A','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127806,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-29B','华润置地大厦E座','29B','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127807,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-29C','华润置地大厦E座','29C','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127808,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-29D','华润置地大厦E座','29D','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127809,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-30A','华润置地大厦E座','30A','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127810,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-30B','华润置地大厦E座','30B','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127811,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-30C','华润置地大厦E座','30C','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127812,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-30D','华润置地大厦E座','30D','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127813,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-30E','华润置地大厦E座','30E','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127814,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-30F','华润置地大厦E座','30F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127815,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-31A','华润置地大厦E座','31A','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127816,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-31B','华润置地大厦E座','31B','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127817,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-31C','华润置地大厦E座','31C','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127818,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-31D','华润置地大厦E座','31D','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127819,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-31E','华润置地大厦E座','31E','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127820,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-31F','华润置地大厦E座','31F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127821,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-31G','华润置地大厦E座','31G','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127822,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-32A','华润置地大厦E座','32A','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127823,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-32B','华润置地大厦E座','32B','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127824,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-32C','华润置地大厦E座','32C','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127825,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-32D','华润置地大厦E座','32D','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127826,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-32E','华润置地大厦E座','32E','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127827,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-32F','华润置地大厦E座','32F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127828,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-32G','华润置地大厦E座','32G','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127829,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-33A1','华润置地大厦E座','33A1','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127830,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-33A2','华润置地大厦E座','33A2','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127831,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-33B','华润置地大厦E座','33B','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127832,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-33C1','华润置地大厦E座','33C1','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127833,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-33C2','华润置地大厦E座','33C2','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127834,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-33D','华润置地大厦E座','33D','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127835,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-33E','华润置地大厦E座','33E','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127836,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-33F','华润置地大厦E座','33F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127837,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-33G','华润置地大厦E座','33G','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127838,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-33H','华润置地大厦E座','33H','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127839,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-33I','华润置地大厦E座','33I','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127840,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-34A','华润置地大厦E座','34A','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127841,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-34B1','华润置地大厦E座','34B1','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127842,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-34C','华润置地大厦E座','34C','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127843,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-34D','华润置地大厦E座','34D','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127844,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-34E','华润置地大厦E座','34E','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127845,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-34F','华润置地大厦E座','34F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127846,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-34G','华润置地大厦E座','34G','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127847,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-35B','华润置地大厦E座','35B','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127848,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-35C','华润置地大厦E座','35C','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127849,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-35D','华润置地大厦E座','35D','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127850,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-35E','华润置地大厦E座','35E','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127851,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-35F','华润置地大厦E座','35F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127852,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-35G','华润置地大厦E座','35G','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127853,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-36A','华润置地大厦E座','36A','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127854,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-36B','华润置地大厦E座','36B','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127855,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-36C','华润置地大厦E座','36C','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127856,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-36D','华润置地大厦E座','36D','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127857,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-38A','华润置地大厦E座','38A','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127858,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-38B','华润置地大厦E座','38B','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127859,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-38C','华润置地大厦E座','38C','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127860,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-38D','华润置地大厦E座','38D','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127861,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-39F','华润置地大厦E座','39F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127862,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-40F','华润置地大厦E座','40F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127863,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-41F','华润置地大厦E座','41F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127864,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-42F','华润置地大厦E座','42F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127865,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-43F','华润置地大厦E座','43F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127866,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-44F','华润置地大厦E座','44F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127867,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-45F','华润置地大厦E座','45F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127868,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-46F','华润置地大厦E座','46F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127869,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-47F','华润置地大厦E座','47F','2','0',UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387127870,UUID(),240111044331055035, 15303, '深圳市',  15304, '南山区' ,'华润置地大厦E座-48F','华润置地大厦E座','48F','2','0',UTC_TIMESTAMP(), 999985);

INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26153, 1007144, 240111044331055035, 239825274387127703, '华润置地大厦E座-3F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26154, 1007144, 240111044331055035, 239825274387127704, '华润置地大厦E座-4F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26155, 1007144, 240111044331055035, 239825274387127705, '华润置地大厦E座-6F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26156, 1007144, 240111044331055035, 239825274387127706, '华润置地大厦E座-8A1', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26157, 1007144, 240111044331055035, 239825274387127707, '华润置地大厦E座-8A2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26158, 1007144, 240111044331055035, 239825274387127708, '华润置地大厦E座-8B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26159, 1007144, 240111044331055035, 239825274387127709, '华润置地大厦E座-8B2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26160, 1007144, 240111044331055035, 239825274387127710, '华润置地大厦E座-8C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26161, 1007144, 240111044331055035, 239825274387127711, '华润置地大厦E座-8D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26162, 1007144, 240111044331055035, 239825274387127712, '华润置地大厦E座-8E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26163, 1007144, 240111044331055035, 239825274387127713, '华润置地大厦E座-8F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26164, 1007144, 240111044331055035, 239825274387127714, '华润置地大厦E座-9F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26165, 1007144, 240111044331055035, 239825274387127715, '华润置地大厦E座-10F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26166, 1007144, 240111044331055035, 239825274387127716, '华润置地大厦E座-11F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26167, 1007144, 240111044331055035, 239825274387127717, '华润置地大厦E座-12F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26168, 1007144, 240111044331055035, 239825274387127718, '华润置地大厦E座-13F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26169, 1007144, 240111044331055035, 239825274387127719, '华润置地大厦E座-14A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26170, 1007144, 240111044331055035, 239825274387127720, '华润置地大厦E座-14B1', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26171, 1007144, 240111044331055035, 239825274387127721, '华润置地大厦E座-14B2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26172, 1007144, 240111044331055035, 239825274387127722, '华润置地大厦E座-14C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26173, 1007144, 240111044331055035, 239825274387127723, '华润置地大厦E座-14D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26174, 1007144, 240111044331055035, 239825274387127724, '华润置地大厦E座-14E1', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26175, 1007144, 240111044331055035, 239825274387127725, '华润置地大厦E座-14E2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26176, 1007144, 240111044331055035, 239825274387127726, '华润置地大厦E座-15A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26177, 1007144, 240111044331055035, 239825274387127727, '华润置地大厦E座-15B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26178, 1007144, 240111044331055035, 239825274387127728, '华润置地大厦E座-15C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26179, 1007144, 240111044331055035, 239825274387127729, '华润置地大厦E座-15D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26180, 1007144, 240111044331055035, 239825274387127730, '华润置地大厦E座-15E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26181, 1007144, 240111044331055035, 239825274387127731, '华润置地大厦E座-15F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26182, 1007144, 240111044331055035, 239825274387127732, '华润置地大厦E座-16A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26183, 1007144, 240111044331055035, 239825274387127733, '华润置地大厦E座-16B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26184, 1007144, 240111044331055035, 239825274387127734, '华润置地大厦E座-16C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26185, 1007144, 240111044331055035, 239825274387127735, '华润置地大厦E座-16D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26186, 1007144, 240111044331055035, 239825274387127736, '华润置地大厦E座-16F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26187, 1007144, 240111044331055035, 239825274387127737, '华润置地大厦E座-17A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26188, 1007144, 240111044331055035, 239825274387127738, '华润置地大厦E座-17B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26189, 1007144, 240111044331055035, 239825274387127739, '华润置地大厦E座-17C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26190, 1007144, 240111044331055035, 239825274387127740, '华润置地大厦E座-17D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26191, 1007144, 240111044331055035, 239825274387127741, '华润置地大厦E座-18A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26192, 1007144, 240111044331055035, 239825274387127742, '华润置地大厦E座-18B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26193, 1007144, 240111044331055035, 239825274387127743, '华润置地大厦E座-18C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26194, 1007144, 240111044331055035, 239825274387127744, '华润置地大厦E座-18D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26195, 1007144, 240111044331055035, 239825274387127745, '华润置地大厦E座-18E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26196, 1007144, 240111044331055035, 239825274387127746, '华润置地大厦E座-18F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26197, 1007144, 240111044331055035, 239825274387127747, '华润置地大厦E座-18G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26198, 1007144, 240111044331055035, 239825274387127748, '华润置地大厦E座-18H', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26199, 1007144, 240111044331055035, 239825274387127749, '华润置地大厦E座-18I', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26200, 1007144, 240111044331055035, 239825274387127750, '华润置地大厦E座-19A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26201, 1007144, 240111044331055035, 239825274387127751, '华润置地大厦E座-19B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26202, 1007144, 240111044331055035, 239825274387127752, '华润置地大厦E座-19C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26203, 1007144, 240111044331055035, 239825274387127753, '华润置地大厦E座-19D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26204, 1007144, 240111044331055035, 239825274387127754, '华润置地大厦E座-19E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26205, 1007144, 240111044331055035, 239825274387127755, '华润置地大厦E座-19F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26206, 1007144, 240111044331055035, 239825274387127756, '华润置地大厦E座-20F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26207, 1007144, 240111044331055035, 239825274387127757, '华润置地大厦E座-21A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26208, 1007144, 240111044331055035, 239825274387127758, '华润置地大厦E座-21B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26209, 1007144, 240111044331055035, 239825274387127759, '华润置地大厦E座-21C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26210, 1007144, 240111044331055035, 239825274387127760, '华润置地大厦E座-21D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26211, 1007144, 240111044331055035, 239825274387127761, '华润置地大厦E座-21E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26212, 1007144, 240111044331055035, 239825274387127762, '华润置地大厦E座-21F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26213, 1007144, 240111044331055035, 239825274387127763, '华润置地大厦E座-23A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26214, 1007144, 240111044331055035, 239825274387127764, '华润置地大厦E座-23B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26215, 1007144, 240111044331055035, 239825274387127765, '华润置地大厦E座-23C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26216, 1007144, 240111044331055035, 239825274387127766, '华润置地大厦E座-23D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26217, 1007144, 240111044331055035, 239825274387127767, '华润置地大厦E座-23E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26218, 1007144, 240111044331055035, 239825274387127768, '华润置地大厦E座-23F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26219, 1007144, 240111044331055035, 239825274387127769, '华润置地大厦E座-23H', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26220, 1007144, 240111044331055035, 239825274387127770, '华润置地大厦E座-23I', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26221, 1007144, 240111044331055035, 239825274387127771, '华润置地大厦E座-24F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26222, 1007144, 240111044331055035, 239825274387127772, '华润置地大厦E座-25A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26223, 1007144, 240111044331055035, 239825274387127773, '华润置地大厦E座-25B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26224, 1007144, 240111044331055035, 239825274387127774, '华润置地大厦E座-25C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26225, 1007144, 240111044331055035, 239825274387127775, '华润置地大厦E座-25D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26226, 1007144, 240111044331055035, 239825274387127776, '华润置地大厦E座-25E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26227, 1007144, 240111044331055035, 239825274387127777, '华润置地大厦E座-25F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26228, 1007144, 240111044331055035, 239825274387127778, '华润置地大厦E座-26A1', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26229, 1007144, 240111044331055035, 239825274387127779, '华润置地大厦E座-26A2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26230, 1007144, 240111044331055035, 239825274387127780, '华润置地大厦E座-26B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26231, 1007144, 240111044331055035, 239825274387127781, '华润置地大厦E座-26C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26232, 1007144, 240111044331055035, 239825274387127782, '华润置地大厦E座-26D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26233, 1007144, 240111044331055035, 239825274387127783, '华润置地大厦E座-26E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26234, 1007144, 240111044331055035, 239825274387127784, '华润置地大厦E座-26F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26235, 1007144, 240111044331055035, 239825274387127785, '华润置地大厦E座-26G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26236, 1007144, 240111044331055035, 239825274387127786, '华润置地大厦E座-27A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26237, 1007144, 240111044331055035, 239825274387127787, '华润置地大厦E座-27B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26238, 1007144, 240111044331055035, 239825274387127788, '华润置地大厦E座-27C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26239, 1007144, 240111044331055035, 239825274387127789, '华润置地大厦E座-27D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26240, 1007144, 240111044331055035, 239825274387127790, '华润置地大厦E座-27E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26241, 1007144, 240111044331055035, 239825274387127791, '华润置地大厦E座-27F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26242, 1007144, 240111044331055035, 239825274387127792, '华润置地大厦E座-27G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26243, 1007144, 240111044331055035, 239825274387127793, '华润置地大厦E座-27H1', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26244, 1007144, 240111044331055035, 239825274387127794, '华润置地大厦E座-27H2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26245, 1007144, 240111044331055035, 239825274387127795, '华润置地大厦E座-27I', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26246, 1007144, 240111044331055035, 239825274387127796, '华润置地大厦E座-27J', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26247, 1007144, 240111044331055035, 239825274387127797, '华润置地大厦E座-28A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26248, 1007144, 240111044331055035, 239825274387127798, '华润置地大厦E座-28B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26249, 1007144, 240111044331055035, 239825274387127799, '华润置地大厦E座-28C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26250, 1007144, 240111044331055035, 239825274387127800, '华润置地大厦E座-28D1', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26251, 1007144, 240111044331055035, 239825274387127801, '华润置地大厦E座-28D2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26252, 1007144, 240111044331055035, 239825274387127802, '华润置地大厦E座-28E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26253, 1007144, 240111044331055035, 239825274387127803, '华润置地大厦E座-28F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26254, 1007144, 240111044331055035, 239825274387127804, '华润置地大厦E座-28G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26255, 1007144, 240111044331055035, 239825274387127805, '华润置地大厦E座-29A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26256, 1007144, 240111044331055035, 239825274387127806, '华润置地大厦E座-29B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26257, 1007144, 240111044331055035, 239825274387127807, '华润置地大厦E座-29C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26258, 1007144, 240111044331055035, 239825274387127808, '华润置地大厦E座-29D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26259, 1007144, 240111044331055035, 239825274387127809, '华润置地大厦E座-30A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26260, 1007144, 240111044331055035, 239825274387127810, '华润置地大厦E座-30B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26261, 1007144, 240111044331055035, 239825274387127811, '华润置地大厦E座-30C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26262, 1007144, 240111044331055035, 239825274387127812, '华润置地大厦E座-30D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26263, 1007144, 240111044331055035, 239825274387127813, '华润置地大厦E座-30E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26264, 1007144, 240111044331055035, 239825274387127814, '华润置地大厦E座-30F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26265, 1007144, 240111044331055035, 239825274387127815, '华润置地大厦E座-31A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26266, 1007144, 240111044331055035, 239825274387127816, '华润置地大厦E座-31B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26267, 1007144, 240111044331055035, 239825274387127817, '华润置地大厦E座-31C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26268, 1007144, 240111044331055035, 239825274387127818, '华润置地大厦E座-31D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26269, 1007144, 240111044331055035, 239825274387127819, '华润置地大厦E座-31E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26270, 1007144, 240111044331055035, 239825274387127820, '华润置地大厦E座-31F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26271, 1007144, 240111044331055035, 239825274387127821, '华润置地大厦E座-31G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26272, 1007144, 240111044331055035, 239825274387127822, '华润置地大厦E座-32A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26273, 1007144, 240111044331055035, 239825274387127823, '华润置地大厦E座-32B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26274, 1007144, 240111044331055035, 239825274387127824, '华润置地大厦E座-32C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26275, 1007144, 240111044331055035, 239825274387127825, '华润置地大厦E座-32D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26276, 1007144, 240111044331055035, 239825274387127826, '华润置地大厦E座-32E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26277, 1007144, 240111044331055035, 239825274387127827, '华润置地大厦E座-32F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26278, 1007144, 240111044331055035, 239825274387127828, '华润置地大厦E座-32G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26279, 1007144, 240111044331055035, 239825274387127829, '华润置地大厦E座-33A1', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26280, 1007144, 240111044331055035, 239825274387127830, '华润置地大厦E座-33A2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26281, 1007144, 240111044331055035, 239825274387127831, '华润置地大厦E座-33B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26282, 1007144, 240111044331055035, 239825274387127832, '华润置地大厦E座-33C1', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26283, 1007144, 240111044331055035, 239825274387127833, '华润置地大厦E座-33C2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26284, 1007144, 240111044331055035, 239825274387127834, '华润置地大厦E座-33D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26285, 1007144, 240111044331055035, 239825274387127835, '华润置地大厦E座-33E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26286, 1007144, 240111044331055035, 239825274387127836, '华润置地大厦E座-33F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26287, 1007144, 240111044331055035, 239825274387127837, '华润置地大厦E座-33G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26288, 1007144, 240111044331055035, 239825274387127838, '华润置地大厦E座-33H', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26289, 1007144, 240111044331055035, 239825274387127839, '华润置地大厦E座-33I', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26290, 1007144, 240111044331055035, 239825274387127840, '华润置地大厦E座-34A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26291, 1007144, 240111044331055035, 239825274387127841, '华润置地大厦E座-34B1', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26292, 1007144, 240111044331055035, 239825274387127842, '华润置地大厦E座-34C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26293, 1007144, 240111044331055035, 239825274387127843, '华润置地大厦E座-34D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26294, 1007144, 240111044331055035, 239825274387127844, '华润置地大厦E座-34E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26295, 1007144, 240111044331055035, 239825274387127845, '华润置地大厦E座-34F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26296, 1007144, 240111044331055035, 239825274387127846, '华润置地大厦E座-34G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26297, 1007144, 240111044331055035, 239825274387127847, '华润置地大厦E座-35B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26298, 1007144, 240111044331055035, 239825274387127848, '华润置地大厦E座-35C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26299, 1007144, 240111044331055035, 239825274387127849, '华润置地大厦E座-35D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26300, 1007144, 240111044331055035, 239825274387127850, '华润置地大厦E座-35E', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26301, 1007144, 240111044331055035, 239825274387127851, '华润置地大厦E座-35F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26302, 1007144, 240111044331055035, 239825274387127852, '华润置地大厦E座-35G', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26303, 1007144, 240111044331055035, 239825274387127853, '华润置地大厦E座-36A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26304, 1007144, 240111044331055035, 239825274387127854, '华润置地大厦E座-36B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26305, 1007144, 240111044331055035, 239825274387127855, '华润置地大厦E座-36C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26306, 1007144, 240111044331055035, 239825274387127856, '华润置地大厦E座-36D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26307, 1007144, 240111044331055035, 239825274387127857, '华润置地大厦E座-38A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26308, 1007144, 240111044331055035, 239825274387127858, '华润置地大厦E座-38B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26309, 1007144, 240111044331055035, 239825274387127859, '华润置地大厦E座-38C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26310, 1007144, 240111044331055035, 239825274387127860, '华润置地大厦E座-38D', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26311, 1007144, 240111044331055035, 239825274387127861, '华润置地大厦E座-39F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26312, 1007144, 240111044331055035, 239825274387127862, '华润置地大厦E座-40F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26313, 1007144, 240111044331055035, 239825274387127863, '华润置地大厦E座-41F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26314, 1007144, 240111044331055035, 239825274387127864, '华润置地大厦E座-42F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26315, 1007144, 240111044331055035, 239825274387127865, '华润置地大厦E座-43F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26316, 1007144, 240111044331055035, 239825274387127866, '华润置地大厦E座-44F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26317, 1007144, 240111044331055035, 239825274387127867, '华润置地大厦E座-45F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26318, 1007144, 240111044331055035, 239825274387127868, '华润置地大厦E座-46F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26319, 1007144, 240111044331055035, 239825274387127869, '华润置地大厦E座-47F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26320, 1007144, 240111044331055035, 239825274387127870, '华润置地大厦E座-48F', '0');


INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007018, UUID(), '华润（深圳）有限公司', '华润（深圳）有限公司', 1, 1, 1007146, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184838, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184838, UUID(), 999985, 2, 'EhGroups', 1007018,'华润（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007146, 0, 'ENTERPRISE', '华润（深圳）有限公司', 0, '', '/1007146', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115953, 240111044331055035, 'organization', 1007146, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007019, UUID(), '深圳华润物业管理有限公司', '深圳华润物业管理有限公司', 1, 1, 1007147, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184839, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184839, UUID(), 999985, 2, 'EhGroups', 1007019,'深圳华润物业管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007147, 0, 'ENTERPRISE', '深圳华润物业管理有限公司', 0, '', '/1007147', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115954, 240111044331055035, 'organization', 1007147, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007020, UUID(), '深圳市徽德网络科技有限公司', '深圳市徽德网络科技有限公司', 1, 1, 1007148, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184840, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184840, UUID(), 999985, 2, 'EhGroups', 1007020,'深圳市徽德网络科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007148, 0, 'ENTERPRISE', '深圳市徽德网络科技有限公司', 0, '', '/1007148', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115955, 240111044331055035, 'organization', 1007148, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007021, UUID(), '维时科技有限公司', '维时科技有限公司', 1, 1, 1007149, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184841, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184841, UUID(), 999985, 2, 'EhGroups', 1007021,'维时科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007149, 0, 'ENTERPRISE', '维时科技有限公司', 0, '', '/1007149', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115956, 240111044331055035, 'organization', 1007149, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007022, UUID(), '深圳香山生命动力科技有限公司', '深圳香山生命动力科技有限公司', 1, 1, 1007150, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184842, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184842, UUID(), 999985, 2, 'EhGroups', 1007022,'深圳香山生命动力科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007150, 0, 'ENTERPRISE', '深圳香山生命动力科技有限公司', 0, '', '/1007150', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115957, 240111044331055035, 'organization', 1007150, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007023, UUID(), '懿德汇睿企业管理咨询有限公司', '懿德汇睿企业管理咨询有限公司', 1, 1, 1007151, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184843, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184843, UUID(), 999985, 2, 'EhGroups', 1007023,'懿德汇睿企业管理咨询有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007151, 0, 'ENTERPRISE', '懿德汇睿企业管理咨询有限公司', 0, '', '/1007151', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115958, 240111044331055035, 'organization', 1007151, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007024, UUID(), '深圳市欧品电子有限公司', '深圳市欧品电子有限公司', 1, 1, 1007152, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184844, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184844, UUID(), 999985, 2, 'EhGroups', 1007024,'深圳市欧品电子有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007152, 0, 'ENTERPRISE', '深圳市欧品电子有限公司', 0, '', '/1007152', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115959, 240111044331055035, 'organization', 1007152, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007025, UUID(), '深圳前海库卡智能车库投资发展有限公司', '深圳前海库卡智能车库投资发展有限公司', 1, 1, 1007153, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184845, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184845, UUID(), 999985, 2, 'EhGroups', 1007025,'深圳前海库卡智能车库投资发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007153, 0, 'ENTERPRISE', '深圳前海库卡智能车库投资发展有限公司', 0, '', '/1007153', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115960, 240111044331055035, 'organization', 1007153, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007026, UUID(), '晨星软件研发（深圳）有限公司', '晨星软件研发（深圳）有限公司', 1, 1, 1007154, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184846, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184846, UUID(), 999985, 2, 'EhGroups', 1007026,'晨星软件研发（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007154, 0, 'ENTERPRISE', '晨星软件研发（深圳）有限公司', 0, '', '/1007154', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115961, 240111044331055035, 'organization', 1007154, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007027, UUID(), '四国化研（上海）有限公司深圳办事处', '四国化研（上海）有限公司深圳办事处', 1, 1, 1007155, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184847, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184847, UUID(), 999985, 2, 'EhGroups', 1007027,'四国化研（上海）有限公司深圳办事处','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007155, 0, 'ENTERPRISE', '四国化研（上海）有限公司深圳办事处', 0, '', '/1007155', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115962, 240111044331055035, 'organization', 1007155, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007028, UUID(), '深圳市众讯网络信息技术有限公司', '深圳市众讯网络信息技术有限公司', 1, 1, 1007156, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184848, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184848, UUID(), 999985, 2, 'EhGroups', 1007028,'深圳市众讯网络信息技术有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007156, 0, 'ENTERPRISE', '深圳市众讯网络信息技术有限公司', 0, '', '/1007156', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115963, 240111044331055035, 'organization', 1007156, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007029, UUID(), '中原（中国）地产代理（深圳）有限公司', '中原（中国）地产代理（深圳）有限公司', 1, 1, 1007157, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184849, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184849, UUID(), 999985, 2, 'EhGroups', 1007029,'中原（中国）地产代理（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007157, 0, 'ENTERPRISE', '中原（中国）地产代理（深圳）有限公司', 0, '', '/1007157', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115964, 240111044331055035, 'organization', 1007157, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007030, UUID(), '深圳埃克斯咖啡连锁有限公司', '深圳埃克斯咖啡连锁有限公司', 1, 1, 1007158, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184850, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184850, UUID(), 999985, 2, 'EhGroups', 1007030,'深圳埃克斯咖啡连锁有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007158, 0, 'ENTERPRISE', '深圳埃克斯咖啡连锁有限公司', 0, '', '/1007158', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115965, 240111044331055035, 'organization', 1007158, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007031, UUID(), '深圳市尊资资产管理有限公司', '深圳市尊资资产管理有限公司', 1, 1, 1007159, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184851, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184851, UUID(), 999985, 2, 'EhGroups', 1007031,'深圳市尊资资产管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007159, 0, 'ENTERPRISE', '深圳市尊资资产管理有限公司', 0, '', '/1007159', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115966, 240111044331055035, 'organization', 1007159, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007032, UUID(), '深圳市泰锋装潢工程有限公司', '深圳市泰锋装潢工程有限公司', 1, 1, 1007160, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184852, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184852, UUID(), 999985, 2, 'EhGroups', 1007032,'深圳市泰锋装潢工程有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007160, 0, 'ENTERPRISE', '深圳市泰锋装潢工程有限公司', 0, '', '/1007160', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115967, 240111044331055035, 'organization', 1007160, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007033, UUID(), '深圳市乾德电子股份有限公司', '深圳市乾德电子股份有限公司', 1, 1, 1007161, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184853, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184853, UUID(), 999985, 2, 'EhGroups', 1007033,'深圳市乾德电子股份有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007161, 0, 'ENTERPRISE', '深圳市乾德电子股份有限公司', 0, '', '/1007161', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115968, 240111044331055035, 'organization', 1007161, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007034, UUID(), '中新科技集团股份有限公司', '中新科技集团股份有限公司', 1, 1, 1007162, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184854, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184854, UUID(), 999985, 2, 'EhGroups', 1007034,'中新科技集团股份有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007162, 0, 'ENTERPRISE', '中新科技集团股份有限公司', 0, '', '/1007162', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115969, 240111044331055035, 'organization', 1007162, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007035, UUID(), '国融证券深圳深南大道证劵营业部', '国融证券深圳深南大道证劵营业部', 1, 1, 1007163, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184855, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184855, UUID(), 999985, 2, 'EhGroups', 1007035,'国融证券深圳深南大道证劵营业部','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007163, 0, 'ENTERPRISE', '国融证券深圳深南大道证劵营业部', 0, '', '/1007163', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115970, 240111044331055035, 'organization', 1007163, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007036, UUID(), '深圳前海金源金融服务有限公司', '深圳前海金源金融服务有限公司', 1, 1, 1007164, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184856, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184856, UUID(), 999985, 2, 'EhGroups', 1007036,'深圳前海金源金融服务有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007164, 0, 'ENTERPRISE', '深圳前海金源金融服务有限公司', 0, '', '/1007164', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115971, 240111044331055035, 'organization', 1007164, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007037, UUID(), '深圳青云万里资产管理有限公司', '深圳青云万里资产管理有限公司', 1, 1, 1007165, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184857, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184857, UUID(), 999985, 2, 'EhGroups', 1007037,'深圳青云万里资产管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007165, 0, 'ENTERPRISE', '深圳青云万里资产管理有限公司', 0, '', '/1007165', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115972, 240111044331055035, 'organization', 1007165, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007038, UUID(), '深圳市世华房地产投资顾问有限公司', '深圳市世华房地产投资顾问有限公司', 1, 1, 1007166, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184858, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184858, UUID(), 999985, 2, 'EhGroups', 1007038,'深圳市世华房地产投资顾问有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007166, 0, 'ENTERPRISE', '深圳市世华房地产投资顾问有限公司', 0, '', '/1007166', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115973, 240111044331055035, 'organization', 1007166, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007039, UUID(), '君达资本投资（深圳）有限公司', '君达资本投资（深圳）有限公司', 1, 1, 1007167, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184859, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184859, UUID(), 999985, 2, 'EhGroups', 1007039,'君达资本投资（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007167, 0, 'ENTERPRISE', '君达资本投资（深圳）有限公司', 0, '', '/1007167', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115974, 240111044331055035, 'organization', 1007167, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007040, UUID(), '深圳市亘讯智慧科技实业发展有限公司', '深圳市亘讯智慧科技实业发展有限公司', 1, 1, 1007168, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184860, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184860, UUID(), 999985, 2, 'EhGroups', 1007040,'深圳市亘讯智慧科技实业发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007168, 0, 'ENTERPRISE', '深圳市亘讯智慧科技实业发展有限公司', 0, '', '/1007168', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115975, 240111044331055035, 'organization', 1007168, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007041, UUID(), '深圳前海华松资产管理有限公司', '深圳前海华松资产管理有限公司', 1, 1, 1007169, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184861, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184861, UUID(), 999985, 2, 'EhGroups', 1007041,'深圳前海华松资产管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007169, 0, 'ENTERPRISE', '深圳前海华松资产管理有限公司', 0, '', '/1007169', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115976, 240111044331055035, 'organization', 1007169, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007042, UUID(), '深圳市坚果基金管理有限公司', '深圳市坚果基金管理有限公司', 1, 1, 1007170, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184862, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184862, UUID(), 999985, 2, 'EhGroups', 1007042,'深圳市坚果基金管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007170, 0, 'ENTERPRISE', '深圳市坚果基金管理有限公司', 0, '', '/1007170', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115977, 240111044331055035, 'organization', 1007170, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007043, UUID(), '深圳市雅庭精品公寓管理有限公司', '深圳市雅庭精品公寓管理有限公司', 1, 1, 1007171, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184863, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184863, UUID(), 999985, 2, 'EhGroups', 1007043,'深圳市雅庭精品公寓管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007171, 0, 'ENTERPRISE', '深圳市雅庭精品公寓管理有限公司', 0, '', '/1007171', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115978, 240111044331055035, 'organization', 1007171, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007044, UUID(), '深圳市名雕丹迪设计有限公司', '深圳市名雕丹迪设计有限公司', 1, 1, 1007172, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184864, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184864, UUID(), 999985, 2, 'EhGroups', 1007044,'深圳市名雕丹迪设计有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007172, 0, 'ENTERPRISE', '深圳市名雕丹迪设计有限公司', 0, '', '/1007172', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115979, 240111044331055035, 'organization', 1007172, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007045, UUID(), '合兴永盛（深圳）投资控股有限公司', '合兴永盛（深圳）投资控股有限公司', 1, 1, 1007173, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184865, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184865, UUID(), 999985, 2, 'EhGroups', 1007045,'合兴永盛（深圳）投资控股有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007173, 0, 'ENTERPRISE', '合兴永盛（深圳）投资控股有限公司', 0, '', '/1007173', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115980, 240111044331055035, 'organization', 1007173, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007046, UUID(), '华润保险经纪有限公司', '华润保险经纪有限公司', 1, 1, 1007174, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184866, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184866, UUID(), 999985, 2, 'EhGroups', 1007046,'华润保险经纪有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007174, 0, 'ENTERPRISE', '华润保险经纪有限公司', 0, '', '/1007174', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115981, 240111044331055035, 'organization', 1007174, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007047, UUID(), '深圳双开颜金融控股集团有限公司', '深圳双开颜金融控股集团有限公司', 1, 1, 1007175, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184867, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184867, UUID(), 999985, 2, 'EhGroups', 1007047,'深圳双开颜金融控股集团有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007175, 0, 'ENTERPRISE', '深圳双开颜金融控股集团有限公司', 0, '', '/1007175', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115982, 240111044331055035, 'organization', 1007175, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007048, UUID(), '深圳市乐方互联信息技术有限公司', '深圳市乐方互联信息技术有限公司', 1, 1, 1007176, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184868, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184868, UUID(), 999985, 2, 'EhGroups', 1007048,'深圳市乐方互联信息技术有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007176, 0, 'ENTERPRISE', '深圳市乐方互联信息技术有限公司', 0, '', '/1007176', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115983, 240111044331055035, 'organization', 1007176, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007049, UUID(), '深圳市新宇星电子科技有限公司', '深圳市新宇星电子科技有限公司', 1, 1, 1007177, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184869, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184869, UUID(), 999985, 2, 'EhGroups', 1007049,'深圳市新宇星电子科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007177, 0, 'ENTERPRISE', '深圳市新宇星电子科技有限公司', 0, '', '/1007177', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115984, 240111044331055035, 'organization', 1007177, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007050, UUID(), '深圳市利彤实业有限公司', '深圳市利彤实业有限公司', 1, 1, 1007178, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184870, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184870, UUID(), 999985, 2, 'EhGroups', 1007050,'深圳市利彤实业有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007178, 0, 'ENTERPRISE', '深圳市利彤实业有限公司', 0, '', '/1007178', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115985, 240111044331055035, 'organization', 1007178, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007051, UUID(), '深圳前海中仁基金管理有限公司', '深圳前海中仁基金管理有限公司', 1, 1, 1007179, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184871, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184871, UUID(), 999985, 2, 'EhGroups', 1007051,'深圳前海中仁基金管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007179, 0, 'ENTERPRISE', '深圳前海中仁基金管理有限公司', 0, '', '/1007179', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115986, 240111044331055035, 'organization', 1007179, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007052, UUID(), '深圳前海中仁互联网金融服务有限公司', '深圳前海中仁互联网金融服务有限公司', 1, 1, 1007180, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184872, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184872, UUID(), 999985, 2, 'EhGroups', 1007052,'深圳前海中仁互联网金融服务有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007180, 0, 'ENTERPRISE', '深圳前海中仁互联网金融服务有限公司', 0, '', '/1007180', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115987, 240111044331055035, 'organization', 1007180, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007053, UUID(), '深圳财富星资本管理有限公司', '深圳财富星资本管理有限公司', 1, 1, 1007181, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184873, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184873, UUID(), 999985, 2, 'EhGroups', 1007053,'深圳财富星资本管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007181, 0, 'ENTERPRISE', '深圳财富星资本管理有限公司', 0, '', '/1007181', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115988, 240111044331055035, 'organization', 1007181, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007054, UUID(), '深圳市低烧眼镜有限公司', '深圳市低烧眼镜有限公司', 1, 1, 1007182, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184874, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184874, UUID(), 999985, 2, 'EhGroups', 1007054,'深圳市低烧眼镜有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007182, 0, 'ENTERPRISE', '深圳市低烧眼镜有限公司', 0, '', '/1007182', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115989, 240111044331055035, 'organization', 1007182, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007055, UUID(), '深圳市深度装饰设计有限公司', '深圳市深度装饰设计有限公司', 1, 1, 1007183, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184875, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184875, UUID(), 999985, 2, 'EhGroups', 1007055,'深圳市深度装饰设计有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007183, 0, 'ENTERPRISE', '深圳市深度装饰设计有限公司', 0, '', '/1007183', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115990, 240111044331055035, 'organization', 1007183, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007056, UUID(), '深圳市益彰教育咨询有限公司', '深圳市益彰教育咨询有限公司', 1, 1, 1007184, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184876, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184876, UUID(), 999985, 2, 'EhGroups', 1007056,'深圳市益彰教育咨询有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007184, 0, 'ENTERPRISE', '深圳市益彰教育咨询有限公司', 0, '', '/1007184', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115991, 240111044331055035, 'organization', 1007184, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007057, UUID(), '博富商务中心', '博富商务中心', 1, 1, 1007185, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184877, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184877, UUID(), 999985, 2, 'EhGroups', 1007057,'博富商务中心','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007185, 0, 'ENTERPRISE', '博富商务中心', 0, '', '/1007185', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115992, 240111044331055035, 'organization', 1007185, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007058, UUID(), '深圳电票之家网络科技有限公司', '深圳电票之家网络科技有限公司', 1, 1, 1007186, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184878, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184878, UUID(), 999985, 2, 'EhGroups', 1007058,'深圳电票之家网络科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007186, 0, 'ENTERPRISE', '深圳电票之家网络科技有限公司', 0, '', '/1007186', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115993, 240111044331055035, 'organization', 1007186, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007059, UUID(), '深圳前海华锴资产管理有限公司', '深圳前海华锴资产管理有限公司', 1, 1, 1007187, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184879, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184879, UUID(), 999985, 2, 'EhGroups', 1007059,'深圳前海华锴资产管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007187, 0, 'ENTERPRISE', '深圳前海华锴资产管理有限公司', 0, '', '/1007187', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115994, 240111044331055035, 'organization', 1007187, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007060, UUID(), '华润置地商业管理服务（深圳）有限公司', '华润置地商业管理服务（深圳）有限公司', 1, 1, 1007188, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184880, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184880, UUID(), 999985, 2, 'EhGroups', 1007060,'华润置地商业管理服务（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007188, 0, 'ENTERPRISE', '华润置地商业管理服务（深圳）有限公司', 0, '', '/1007188', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115995, 240111044331055035, 'organization', 1007188, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007061, UUID(), '弘浩保险经纪有限公司', '弘浩保险经纪有限公司', 1, 1, 1007189, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184881, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184881, UUID(), 999985, 2, 'EhGroups', 1007061,'弘浩保险经纪有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007189, 0, 'ENTERPRISE', '弘浩保险经纪有限公司', 0, '', '/1007189', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115996, 240111044331055035, 'organization', 1007189, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007062, UUID(), '深圳立智投资有限公司', '深圳立智投资有限公司', 1, 1, 1007190, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184882, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184882, UUID(), 999985, 2, 'EhGroups', 1007062,'深圳立智投资有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007190, 0, 'ENTERPRISE', '深圳立智投资有限公司', 0, '', '/1007190', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115997, 240111044331055035, 'organization', 1007190, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007063, UUID(), '联合股权交易资讯服务有限公司', '联合股权交易资讯服务有限公司', 1, 1, 1007191, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184883, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184883, UUID(), 999985, 2, 'EhGroups', 1007063,'联合股权交易资讯服务有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007191, 0, 'ENTERPRISE', '联合股权交易资讯服务有限公司', 0, '', '/1007191', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115998, 240111044331055035, 'organization', 1007191, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007064, UUID(), '云创谷（深圳）投资咨询有限公司', '云创谷（深圳）投资咨询有限公司', 1, 1, 1007192, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184884, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184884, UUID(), 999985, 2, 'EhGroups', 1007064,'云创谷（深圳）投资咨询有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007192, 0, 'ENTERPRISE', '云创谷（深圳）投资咨询有限公司', 0, '', '/1007192', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1115999, 240111044331055035, 'organization', 1007192, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007065, UUID(), '深圳华利控股集团有限公司', '深圳华利控股集团有限公司', 1, 1, 1007193, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184885, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184885, UUID(), 999985, 2, 'EhGroups', 1007065,'深圳华利控股集团有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007193, 0, 'ENTERPRISE', '深圳华利控股集团有限公司', 0, '', '/1007193', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116000, 240111044331055035, 'organization', 1007193, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007066, UUID(), '中惠金融控股（深圳）有限公司', '中惠金融控股（深圳）有限公司', 1, 1, 1007194, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184886, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184886, UUID(), 999985, 2, 'EhGroups', 1007066,'中惠金融控股（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007194, 0, 'ENTERPRISE', '中惠金融控股（深圳）有限公司', 0, '', '/1007194', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116001, 240111044331055035, 'organization', 1007194, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007067, UUID(), '深圳森藤资产管理有限公司', '深圳森藤资产管理有限公司', 1, 1, 1007195, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184887, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184887, UUID(), 999985, 2, 'EhGroups', 1007067,'深圳森藤资产管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007195, 0, 'ENTERPRISE', '深圳森藤资产管理有限公司', 0, '', '/1007195', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116002, 240111044331055035, 'organization', 1007195, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007068, UUID(), '深圳中塑化科技有限公司', '深圳中塑化科技有限公司', 1, 1, 1007196, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184888, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184888, UUID(), 999985, 2, 'EhGroups', 1007068,'深圳中塑化科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007196, 0, 'ENTERPRISE', '深圳中塑化科技有限公司', 0, '', '/1007196', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116003, 240111044331055035, 'organization', 1007196, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007069, UUID(), '深圳阳灿众筹金融服务集团', '深圳阳灿众筹金融服务集团', 1, 1, 1007197, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184889, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184889, UUID(), 999985, 2, 'EhGroups', 1007069,'深圳阳灿众筹金融服务集团','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007197, 0, 'ENTERPRISE', '深圳阳灿众筹金融服务集团', 0, '', '/1007197', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116004, 240111044331055035, 'organization', 1007197, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007070, UUID(), '华设资产管理（上海）有限公司深圳分公司', '华设资产管理（上海）有限公司深圳分公司', 1, 1, 1007198, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184890, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184890, UUID(), 999985, 2, 'EhGroups', 1007070,'华设资产管理（上海）有限公司深圳分公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007198, 0, 'ENTERPRISE', '华设资产管理（上海）有限公司深圳分公司', 0, '', '/1007198', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116005, 240111044331055035, 'organization', 1007198, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007071, UUID(), '北京首创期货有限责任公司深圳营业部', '北京首创期货有限责任公司深圳营业部', 1, 1, 1007199, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184891, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184891, UUID(), 999985, 2, 'EhGroups', 1007071,'北京首创期货有限责任公司深圳营业部','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007199, 0, 'ENTERPRISE', '北京首创期货有限责任公司深圳营业部', 0, '', '/1007199', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116006, 240111044331055035, 'organization', 1007199, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007072, UUID(), '浙江超威创元实业有限公司', '浙江超威创元实业有限公司', 1, 1, 1007200, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184892, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184892, UUID(), 999985, 2, 'EhGroups', 1007072,'浙江超威创元实业有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007200, 0, 'ENTERPRISE', '浙江超威创元实业有限公司', 0, '', '/1007200', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116007, 240111044331055035, 'organization', 1007200, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007073, UUID(), '深圳佳汇融资租赁有限公司', '深圳佳汇融资租赁有限公司', 1, 1, 1007201, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184893, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184893, UUID(), 999985, 2, 'EhGroups', 1007073,'深圳佳汇融资租赁有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007201, 0, 'ENTERPRISE', '深圳佳汇融资租赁有限公司', 0, '', '/1007201', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116008, 240111044331055035, 'organization', 1007201, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007074, UUID(), '华润租赁有限公司', '华润租赁有限公司', 1, 1, 1007202, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184894, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184894, UUID(), 999985, 2, 'EhGroups', 1007074,'华润租赁有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007202, 0, 'ENTERPRISE', '华润租赁有限公司', 0, '', '/1007202', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116009, 240111044331055035, 'organization', 1007202, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007075, UUID(), '全堆栈设计（深圳）有限公司', '全堆栈设计（深圳）有限公司', 1, 1, 1007203, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184895, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184895, UUID(), 999985, 2, 'EhGroups', 1007075,'全堆栈设计（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007203, 0, 'ENTERPRISE', '全堆栈设计（深圳）有限公司', 0, '', '/1007203', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116010, 240111044331055035, 'organization', 1007203, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007076, UUID(), '华峰联合控股（深圳）有限公司', '华峰联合控股（深圳）有限公司', 1, 1, 1007204, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184896, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184896, UUID(), 999985, 2, 'EhGroups', 1007076,'华峰联合控股（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007204, 0, 'ENTERPRISE', '华峰联合控股（深圳）有限公司', 0, '', '/1007204', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116011, 240111044331055035, 'organization', 1007204, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007077, UUID(), '深圳市尊荣投资发展有限公司', '深圳市尊荣投资发展有限公司', 1, 1, 1007205, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184897, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184897, UUID(), 999985, 2, 'EhGroups', 1007077,'深圳市尊荣投资发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007205, 0, 'ENTERPRISE', '深圳市尊荣投资发展有限公司', 0, '', '/1007205', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116012, 240111044331055035, 'organization', 1007205, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007078, UUID(), '深圳瑞雪资产管理有限公司', '深圳瑞雪资产管理有限公司', 1, 1, 1007206, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184898, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184898, UUID(), 999985, 2, 'EhGroups', 1007078,'深圳瑞雪资产管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007206, 0, 'ENTERPRISE', '深圳瑞雪资产管理有限公司', 0, '', '/1007206', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116013, 240111044331055035, 'organization', 1007206, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007079, UUID(), '深圳市塞纳河水务有限公司', '深圳市塞纳河水务有限公司', 1, 1, 1007207, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184899, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184899, UUID(), 999985, 2, 'EhGroups', 1007079,'深圳市塞纳河水务有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007207, 0, 'ENTERPRISE', '深圳市塞纳河水务有限公司', 0, '', '/1007207', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116014, 240111044331055035, 'organization', 1007207, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007080, UUID(), '深圳市信兴高科科技有限公司', '深圳市信兴高科科技有限公司', 1, 1, 1007208, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184900, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184900, UUID(), 999985, 2, 'EhGroups', 1007080,'深圳市信兴高科科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007208, 0, 'ENTERPRISE', '深圳市信兴高科科技有限公司', 0, '', '/1007208', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116015, 240111044331055035, 'organization', 1007208, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007081, UUID(), '铸金财富（北京）资产管理有限公司深圳分公司', '铸金财富（北京）资产管理有限公司深圳分公司', 1, 1, 1007209, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184901, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184901, UUID(), 999985, 2, 'EhGroups', 1007081,'铸金财富（北京）资产管理有限公司深圳分公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007209, 0, 'ENTERPRISE', '铸金财富（北京）资产管理有限公司深圳分公司', 0, '', '/1007209', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116016, 240111044331055035, 'organization', 1007209, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007082, UUID(), '深圳市东方佳腾投资合伙企业（有限合伙）', '深圳市东方佳腾投资合伙企业（有限合伙）', 1, 1, 1007210, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184902, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184902, UUID(), 999985, 2, 'EhGroups', 1007082,'深圳市东方佳腾投资合伙企业（有限合伙）','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007210, 0, 'ENTERPRISE', '深圳市东方佳腾投资合伙企业（有限合伙）', 0, '', '/1007210', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116017, 240111044331055035, 'organization', 1007210, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007083, UUID(), '深圳市云图网络科技有限公司', '深圳市云图网络科技有限公司', 1, 1, 1007211, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184903, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184903, UUID(), 999985, 2, 'EhGroups', 1007083,'深圳市云图网络科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007211, 0, 'ENTERPRISE', '深圳市云图网络科技有限公司', 0, '', '/1007211', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116018, 240111044331055035, 'organization', 1007211, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007084, UUID(), '新态互联（深圳）科技有限公司', '新态互联（深圳）科技有限公司', 1, 1, 1007212, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184904, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184904, UUID(), 999985, 2, 'EhGroups', 1007084,'新态互联（深圳）科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007212, 0, 'ENTERPRISE', '新态互联（深圳）科技有限公司', 0, '', '/1007212', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116019, 240111044331055035, 'organization', 1007212, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007085, UUID(), '深圳华瑞智生活科技有限公司', '深圳华瑞智生活科技有限公司', 1, 1, 1007213, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184905, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184905, UUID(), 999985, 2, 'EhGroups', 1007085,'深圳华瑞智生活科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007213, 0, 'ENTERPRISE', '深圳华瑞智生活科技有限公司', 0, '', '/1007213', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116020, 240111044331055035, 'organization', 1007213, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007086, UUID(), '上海华讯网络系统有限公司深圳分公司', '上海华讯网络系统有限公司深圳分公司', 1, 1, 1007214, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184906, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184906, UUID(), 999985, 2, 'EhGroups', 1007086,'上海华讯网络系统有限公司深圳分公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007214, 0, 'ENTERPRISE', '上海华讯网络系统有限公司深圳分公司', 0, '', '/1007214', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116021, 240111044331055035, 'organization', 1007214, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007087, UUID(), '深圳市蒲融管理咨询有限公司', '深圳市蒲融管理咨询有限公司', 1, 1, 1007215, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184907, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184907, UUID(), 999985, 2, 'EhGroups', 1007087,'深圳市蒲融管理咨询有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007215, 0, 'ENTERPRISE', '深圳市蒲融管理咨询有限公司', 0, '', '/1007215', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116022, 240111044331055035, 'organization', 1007215, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007088, UUID(), '深圳市东山防水隔热工程有限公司', '深圳市东山防水隔热工程有限公司', 1, 1, 1007216, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184908, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184908, UUID(), 999985, 2, 'EhGroups', 1007088,'深圳市东山防水隔热工程有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007216, 0, 'ENTERPRISE', '深圳市东山防水隔热工程有限公司', 0, '', '/1007216', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116023, 240111044331055035, 'organization', 1007216, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007089, UUID(), '深圳前海阿凡达物流网络科技有限公司', '深圳前海阿凡达物流网络科技有限公司', 1, 1, 1007217, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184909, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184909, UUID(), 999985, 2, 'EhGroups', 1007089,'深圳前海阿凡达物流网络科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007217, 0, 'ENTERPRISE', '深圳前海阿凡达物流网络科技有限公司', 0, '', '/1007217', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116024, 240111044331055035, 'organization', 1007217, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1007090, UUID(), '毅德控股', '毅德控股', 1, 1, 1007218, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184910, 1, 999985);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184910, UUID(), 999985, 2, 'EhGroups', 1007090,'毅德控股','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1007218, 0, 'ENTERPRISE', '毅德控股', 0, '', '/1007218', 1, 2, 'ENTERPRISE', 999985);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1116025, 240111044331055035, 'organization', 1007218, 3, 0, UTC_TIMESTAMP());
-- 更新公司 group_id
UPDATE eh_organizations r INNER JOIN eh_groups g ON r.id = g.visible_region_id
SET r.group_id = g.id where r.namespace_id = 999985 and g.visible_region_type = 1;

INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES (11982, 999985, 0, '/home', 'Default', '0', '0', 'innospring', 'innospring', 'cs://1/image/aW1hZ2UvTVRwa1pEYzBOVEJtT1RFNVpqYzJPVGswTm1NMk4ySmxOelJtWlRaaU9EZ3pZdw', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'park_tourist');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES (11983, 999985, 0, '/home', 'Default', '0', '0', 'innospring', 'innospring', 'cs://1/image/aW1hZ2UvTVRwa1pEYzBOVEJtT1RFNVpqYzJPVGswTm1NMk4ySmxOelJtWlRaaU9EZ3pZdw', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'pm_admin');

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES('10096','会议室预定','0',NULL,'0','999985');
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ('150', 'community', '240111044331055035', '0', '服务联盟', '服务联盟', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999985', '');

SET @sa_id = (SELECT max(id) FROM `eh_service_alliances`);    
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`) 
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331055035', '服务联盟', '服务联盟', '150', '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);


-- 园区管理员场景
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (417, 999985, 'ServiceMarketLayout', '{"versionCode":"2016102701","versionName":"3.10.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0}]}', '2016102701', '0', '2', '2016-10-24 09:02:30', 'pm_admin');	

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112573, 999985, '0', '0', '0', '/home', 'GovAgencies', 'LIST_GROUPS', '白领社团', 'cs://1/image/aW1hZ2UvTVRveFl6VTFaVEl5TURFelkySXlNVGhoTURreU9XTTRaRE0xWlRVM00yRmlZZw', '1', '1', 36,'{"privateFlag": 0}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112795, 999985, '0', '0', '0', '/home', 'GovAgencies', 'OE图书馆', 'OE图书馆', 'cs://1/image/aW1hZ2UvTVRwbE1qVTNabUkzWVdGaE9XTTNNamc0TUdGaVpqbGtaV05sTnpNeU56TXhNdw', '1', '1', 14,'{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112574, 999985, '0', '0', '0', '/home', 'GovAgencies', 'OFFICIAL_ACTIVITY', 'OE大讲堂', 'cs://1/image/aW1hZ2UvTVRvM1l6UTFPRE00TnpGaU5XTm1ZMkkwTkRjek5URm1PVE0wTkRObE0yUmlPUQ', '1', '1', 50,'', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112575, 999985, '0', '0', '0', '/home', 'GovAgencies', 'OE微商城', 'OE微商城', 'cs://1/image/aW1hZ2UvTVRveE5qZGlOREUwT0RCaFkyUTFNakl4Wm1ReU4yTmxNV1F6WkRNeVpEVXlZdw', '1', '1', 14,'{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14772951464964816937%3F_k%3Dzlbiz#sign_suffix"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');

INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (112576, 999985, 0, 0, 0, '/home', 'Bizs', 'PUNCH', '打卡考勤', 'cs://1/image/aW1hZ2UvTVRwa05ERTJaRGN4TXpZME5USXdNR0V4TlRkbU1HRTNaR1U0TVdZNVpHUTFOdw', '1', '1', '23', '', 0, 0, 1, 1, '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112577, 999985, '0', '0', '0', '/home', 'Bizs', 'VIDEO_MEETING', '视频会议', 'cs://1/image/aW1hZ2UvTVRvd1ptRXdNMkZqTUdJd1lqQTNaalUwTW1NNU5XVmpOVEk0TjJJeFpqSXpaZw', '1', '1', '27', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112578, 999985, 0, 0, 0, '/home', 'Bizs', 'RENTAL', '会议室预定', 'cs://1/image/aW1hZ2UvTVRvMk9XSmhNell5TW1FMFlUQmtObVV4TlRoak0yVm1NbVkwWTJJd09EWXdOUQ', 1, 1, 49,'{\"resourceTypeId\":10096,\"pageType\":0}', 0, 0, 1, 1, '', '1', NULL, NULL, NULL, '1', 'pm_admin');        
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112579, 999985, '0', '0', '0', '/home', 'Bizs', 'CONTACTS', '企业通讯录', 'cs://1/image/aW1hZ2UvTVRvNU1UaGpZbVl3T1dNMk5UUTFaVEpsT1RjMlptRTJOVGczWmpWak5EWTBaUQ', '1', '1', '46', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (112580, 999985, '0', '0', '0', '/home', 'Bizs', '项目介绍', '项目介绍', 'cs://1/image/aW1hZ2UvTVRvd056SmtNakE1WWpZNVltVmhPRGN6TlRGaFpEQTBOVEF3WTJZM09UVXdaUQ', '1', '1', '28', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (112581, 999985, '0', '0', '0', '/home', 'Bizs', 'SERVICE_HOT_LINE', '服务热线', 'cs://1/image/aW1hZ2UvTVRvelpXRTFOR1U0T1Raa016UTVOV0l3TkRWak5USTBPVGMzTldNelltUmtZZw', '1', '1', '45', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
    VALUES (112582, 999985, 0, 0, 0, '/home', 'Bizs', '服务联盟', '服务联盟', 'cs://1/image/aW1hZ2UvTVRvMVkyVm1Oak5oWlRoaE56UTNNV1EwWVRKaU4yRmtNalptT1RZNFpEazFPQQ', 1, 1, 33, '{"type":150,"parentId":150,"displayType": "grid"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'pm_admin');    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112797, 999985, '0', '0', '0', '/home', 'Bizs', '资产管理', '资产管理', 'cs://1/image/aW1hZ2UvTVRveU9EZzJZV1F6TldFeFpESTVObUptTkRWaU5tTmpOVEptWW1Rd00ySXpOZw', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112798, 999985, '0', '0', '0', '/home', 'Bizs', 'OE跑团', 'OE跑团', 'cs://1/image/aW1hZ2UvTVRwa1pUUmpNMkZrTkRGa05EWXdaVGd6TjJWbU9UZzVPVEZrT0dKaVlUbG1PUQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112799, 999985, '0', '0', '0', '/home', 'Bizs', 'DoorManagement', '门禁', 'cs://1/image/aW1hZ2UvTVRvek5UQmlNRGd4TkRRNVpXUTROV1poWTJRMk5qVXlZVE5tWm1JMFl6QTROUQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112800, 999985, '0', '0', '0', '/home', 'Bizs', 'PARKING_RECHARGE', '停车缴费', 'cs://1/image/aW1hZ2UvTVRvM056QXhZMk01TURneFl6YzVZMk0xTXpZME5ETXdNVFJsTldGaFl6SmtZUQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112583, 999985, '0', '0', '0', '/home', 'Bizs', '更多', '更多', 'cs://1/image/aW1hZ2UvTVRwaVpqRmxPRE5sWTJJMU9USXhaVFJoWVdZME1UbGhNakl3TVdWak5USmpNUQ', '1', '1', '1', '{\"itemLocation\":\"/home\", \"itemGroup\":\"Bizs\"}', '30', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');


-- 园区游客场景    
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (418, 999985, 'ServiceMarketLayout', '{"versionCode":"2016102701","versionName":"3.10.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0}]}', '2016102701', '0', '2', '2016-10-24 09:02:30', 'park_tourist');	

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112584, 999985, '0', '0', '0', '/home', 'GovAgencies', 'LIST_GROUPS', '白领社团', 'cs://1/image/aW1hZ2UvTVRveFl6VTFaVEl5TURFelkySXlNVGhoTURreU9XTTRaRE0xWlRVM00yRmlZZw', '1', '1', 36,'{"privateFlag": 0}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112796, 999985, '0', '0', '0', '/home', 'GovAgencies', 'OE图书馆', 'OE图书馆', 'cs://1/image/aW1hZ2UvTVRwbE1qVTNabUkzWVdGaE9XTTNNamc0TUdGaVpqbGtaV05sTnpNeU56TXhNdw', '1', '1', 14,'{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112585, 999985, '0', '0', '0', '/home', 'GovAgencies', 'OFFICIAL_ACTIVITY', 'OE大讲堂', 'cs://1/image/aW1hZ2UvTVRvM1l6UTFPRE00TnpGaU5XTm1ZMkkwTkRjek5URm1PVE0wTkRObE0yUmlPUQ', '1', '1', 50,'', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112586, 999985, '0', '0', '0', '/home', 'GovAgencies', 'OE微商城', 'OE微商城', 'cs://1/image/aW1hZ2UvTVRveE5qZGlOREUwT0RCaFkyUTFNakl4Wm1ReU4yTmxNV1F6WkRNeVpEVXlZdw', '1', '1', 14,'{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14772951464964816937%3F_k%3Dzlbiz#sign_suffix"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');

INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (112587, 999985, 0, 0, 0, '/home', 'Bizs', 'PUNCH', '打卡考勤', 'cs://1/image/aW1hZ2UvTVRwa05ERTJaRGN4TXpZME5USXdNR0V4TlRkbU1HRTNaR1U0TVdZNVpHUTFOdw', '1', '1', '23', '', 0, 0, 1, 1, '', '0', NULL, NULL, NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112588, 999985, '0', '0', '0', '/home', 'Bizs', 'VIDEO_MEETING', '视频会议', 'cs://1/image/aW1hZ2UvTVRvd1ptRXdNMkZqTUdJd1lqQTNaalUwTW1NNU5XVmpOVEk0TjJJeFpqSXpaZw', '1', '1', '27', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112589, 999985, 0, 0, 0, '/home', 'Bizs', 'RENTAL', '会议室预定', 'cs://1/image/aW1hZ2UvTVRvMk9XSmhNell5TW1FMFlUQmtObVV4TlRoak0yVm1NbVkwWTJJd09EWXdOUQ', 1, 1, 49,'{\"resourceTypeId\":10096,\"pageType\":0}', 0, 0, 1, 1, '', '1', NULL, NULL, NULL, '1', 'park_tourist');        
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112590, 999985, '0', '0', '0', '/home', 'Bizs', 'CONTACTS', '企业通讯录', 'cs://1/image/aW1hZ2UvTVRvNU1UaGpZbVl3T1dNMk5UUTFaVEpsT1RjMlptRTJOVGczWmpWak5EWTBaUQ', '1', '1', '46', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (112591, 999985, '0', '0', '0', '/home', 'Bizs', '项目介绍', '项目介绍', 'cs://1/image/aW1hZ2UvTVRvd056SmtNakE1WWpZNVltVmhPRGN6TlRGaFpEQTBOVEF3WTJZM09UVXdaUQ', '1', '1', '28', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (112592, 999985, '0', '0', '0', '/home', 'Bizs', 'SERVICE_HOT_LINE', '服务热线', 'cs://1/image/aW1hZ2UvTVRvelpXRTFOR1U0T1Raa016UTVOV0l3TkRWak5USTBPVGMzTldNelltUmtZZw', '1', '1', '45', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
    VALUES (112593, 999985, 0, 0, 0, '/home', 'Bizs', '服务联盟', '服务联盟', 'cs://1/image/aW1hZ2UvTVRvMVkyVm1Oak5oWlRoaE56UTNNV1EwWVRKaU4yRmtNalptT1RZNFpEazFPQQ', 1, 1, 33, '{"type":150,"parentId":150,"displayType": "grid"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'park_tourist');    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112801, 999985, '0', '0', '0', '/home', 'Bizs', '资产管理', '资产管理', 'cs://1/image/aW1hZ2UvTVRveU9EZzJZV1F6TldFeFpESTVObUptTkRWaU5tTmpOVEptWW1Rd00ySXpOZw', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112802, 999985, '0', '0', '0', '/home', 'Bizs', 'OE跑团', 'OE跑团', 'cs://1/image/aW1hZ2UvTVRwa1pUUmpNMkZrTkRGa05EWXdaVGd6TjJWbU9UZzVPVEZrT0dKaVlUbG1PUQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112803, 999985, '0', '0', '0', '/home', 'Bizs', 'DoorManagement', '门禁', 'cs://1/image/aW1hZ2UvTVRvek5UQmlNRGd4TkRRNVpXUTROV1poWTJRMk5qVXlZVE5tWm1JMFl6QTROUQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112804, 999985, '0', '0', '0', '/home', 'Bizs', 'PARKING_RECHARGE', '停车缴费', 'cs://1/image/aW1hZ2UvTVRvM056QXhZMk01TURneFl6YzVZMk0xTXpZME5ETXdNVFJsTldGaFl6SmtZUQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112594, 999985, '0', '0', '0', '/home', 'Bizs', '更多', '更多', 'cs://1/image/aW1hZ2UvTVRwaVpqRmxPRE5sWTJJMU9USXhaVFJoWVdZME1UbGhNakl3TVdWak5USmpNUQ', '1', '1', '1', '{\"itemLocation\":\"/home\", \"itemGroup\":\"Bizs\"}', '30', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');

	
-- 菜单	
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10000,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),11000,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),11100,'', 'EhNamespaces', 999985,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),12000,'', 'EhNamespaces', 999985,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),30000,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),30500,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),31000,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),32000,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),33000,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),34000,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),35000,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),36000,'', 'EhNamespaces', 999985,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40000,'', 'EhNamespaces', 999985,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43000,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43100,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43200,'', 'EhNamespaces', 999985,2);


INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43400,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43410,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43420,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43430,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43440,'', 'EhNamespaces', 999985,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),44000,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),44100,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),44200,'', 'EhNamespaces', 999985,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),46000,'', 'EhNamespaces', 999985,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50000,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),51000,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),51100,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),52000,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),52100,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),52200,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),52300,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),52400,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),53000,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),53100,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56000,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56100,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56110,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56111,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56112,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56113,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56114,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56115,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56120,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56121,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56122,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56130,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56131,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56132,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56140,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56141,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56150,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56151,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56152,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56160,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56161,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56170,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56171,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56200,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56210,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56220,'', 'EhNamespaces', 999985,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56230,'', 'EhNamespaces', 999985,2);


-- 物业报修

INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
	VALUES ('61', '0', '0', '任务', '任务', '0', '2', '2016-09-29 06:09:03', NULL, NULL, NULL, '999985');
-- 小店
-- INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
--	VALUES ('600', 'business.url', 'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdefault%3Fpos%3D1%26_k%3Dzlbiz#sign_suffix', 'business url', '999985', NULL);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('805', 'video.official.support', '2', 'offical video support', '999985', NULL);

-- 更新
delete from eh_launch_pad_layouts where id in (417, 418);
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (417, 999985, 'ServiceMarketLayout', '{"versionCode":"2016102701","versionName":"3.10.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0}]}', '2016102701', '0', '2', '2016-10-24 09:02:30', 'pm_admin');	
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (418, 999985, 'ServiceMarketLayout', '{"versionCode":"2016102701","versionName":"3.10.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0}]}', '2016102701', '0', '2', '2016-10-24 09:02:30', 'park_tourist');	

update eh_launch_pad_items set default_order = 1 where id >=112795 and id <=112804 ;

update eh_launch_pad_items set default_order = 2 where id in (112574, 112575, 112585, 112586);
	

	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(202136, 2, 0, '运动与音乐', '兴趣/运动与音乐', 10, 2, UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(202137, 2, 0, '旅游摄影', '兴趣/旅游摄影', 20, 2, UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(202138, 2, 0, '亲子与教育', '兴趣/亲子与教育', 30, 2, UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(202139, 2, 0, '美食与厨艺', '兴趣/美食与厨艺', 40, 2, UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(202140, 2, 0, '家庭装饰', '兴趣/家庭装饰', 50, 2, UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(202141, 2, 0, '美容化妆', '兴趣/美容化妆', 60, 2, UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(202142, 2, 0, '宠物会', '兴趣/宠物会', 70, 2, UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(202143, 2, 0, '名牌汇', '兴趣/名牌汇', 80, 2, UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(202144, 2, 0, '同事群', '兴趣/同事群', 90, 2, UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(202145, 2, 0, '老乡群', '兴趣/老乡群', 100, 2, UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(202146, 2, 0, '同学群', '兴趣/同学群', 110, 2, UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(202147, 2, 0, '拼车', '兴趣/拼车', 120, 2, UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(202148, 2, 0, '其他', '兴趣/其他', 200, 2, UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(202149, 2001, 0, '亲子', '兴趣/亲子与教育/亲子', 0, 2, UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(202150, 2001, 0, '教育', '兴趣/亲子与教育/教育', 0, 2, UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(202151, 2002, 0, '运动', '兴趣/运动与音乐/运动', 0, 2, UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(202152, 2002, 0, '音乐', '兴趣/运动与音乐/音乐', 0, 2, UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(202153, 2008, 0, '旅游', '兴趣/旅游摄影/旅游', 0, 2, UTC_TIMESTAMP(), 999985);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(202154, 2008, 0, '摄影', '兴趣/旅游摄影/摄影', 0, 2, UTC_TIMESTAMP(), 999985);
	
-- 添加表单管理菜单和服务联盟的审批跳转
SET @menu_scope_id = (SELECT max(id) FROM `eh_web_menu_scopes`);
insert into `eh_web_menu_scopes` (`id`, `menu_id`,`owner_type`, `owner_id`,`apply_policy`) values ((@menu_scope_id := @menu_scope_id + 1),50900,'EhNamespaces',999985,2);
SET @jump_id = (SELECT MAX FROM `eh_service_alliance_jump_module`);
insert into eh_service_alliance_jump_module (id, namespace_id, module_name, module_url, parent_id) values ((@jump_id := @jump_id + 1),999985,'审批','zl://approval/create?approvalId={}&sourceId={}', 0);

-- 加服务联盟表单中医理疗
set @eh_request_templates_id = (select max(id) from eh_request_templates);
INSERT INTO `eh_request_templates` (`id`, `template_type`, `name`, `button_title`, `email_flag`, `msg_flag`, `fields_json`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`) VALUES
((@eh_request_templates_id := @eh_request_templates_id + 1), 'ReserveChinesePhysiotherapy', '中医理疗', '我要报名', 1, 1, '{"fields": [{"fieldName": "contact","fieldDisplayName": "姓名","fieldType": "string","fieldContentType": "text","fieldDesc": "请输入您的姓名","requiredFlag": "1"},{"fieldName": "remarks","fieldDisplayName": "性别","fieldType": "string","fieldContentType": "text","fieldDesc": "请输入您的性别","requiredFlag": "1"},{"fieldName": "reserveOrganization","fieldDisplayName": "公司名称","fieldType": "string","fieldContentType": "text","fieldDesc": "请输入您所在公司","requiredFlag": "1"},{"fieldName": "mobile","fieldDisplayName": "联系电话","fieldType": "string","fieldContentType": "text","fieldDesc": "请输入您的联系电话","requiredFlag": "1"}]}', 1, 1, NOW(), 0, NULL);
set @mapping_id = (select max(id) from eh_request_templates_namespace_mapping);
INSERT INTO `eh_request_templates_namespace_mapping` (`id`, `namespace_id`, `template_id`) VALUES ((@mapping_id := @mapping_id + 1), 999985, @eh_request_templates_id);