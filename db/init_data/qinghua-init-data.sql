INSERT INTO `eh_namespaces`(`id`, `name`) VALUES(999984, '清华信息港');

INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`)
	VALUES(1122, 999984, 'community_commercial', '2016-10-21 18:07:50');

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES (804, 'app.agreements.url', 'http://core.zuolin.com/mobile/static/app_agreements/qh_agreements.html', 'the relative path for qinghua app agreements', '999984', NULL);

INSERT INTO `eh_version_realm` VALUES ('72', 'Android_Qinghua', null, UTC_TIMESTAMP(), '999984');
INSERT INTO `eh_version_realm` VALUES ('73', 'iOS_Qinghua', null, UTC_TIMESTAMP(), '999984');

insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`)
	values(164,72,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`)
	values(165,73,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());

INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES(999984, 'sms.default.yzx', 1, 'zh_CN', '验证码-清华信息港', '34268');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES(999984, 'sms.default.yzx', 4, 'zh_CN', '派单-清华信息港', '34577');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES(999984, 'sms.default.yzx', 6, 'zh_CN', '任务2-清华信息港', '34578');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('sms.default.yzx', '11', 'zh_CN', '物业任务-清华信息港', '34730', '999984');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('sms.default.yzx', '10', 'zh_CN', '物业任务2-清华信息港', '34731', '999984');

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (248953  , UUID(), '9205218', '李叶', 'cs://1/image/aW1hZ2UvTVRvMlkySmhNbVZqTm1SaU1UQXdPREkxWkRjME5HVmxNVFU1TXpBNE5UUTBZdw', 1, 45, '1', '2',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (238435 , 248953  ,  '0',  '13600161256',  '221616',  3, UTC_TIMESTAMP(), 999984);

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1009090, UUID(), '清华信息港', '清华信息港', 1, 1, 1008218, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185910, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185910, UUID(), 999984, 2, 'EhGroups', 1009090,'清华信息港','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008218, 0, 'PM', '清华信息港', 0, '', '/1008218', 1, 2, 'ENTERPRISE', 999984, 1009090);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118025, 240111044331055835, 'organization', 1008218, 3, 0, UTC_TIMESTAMP());

INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`)
	VALUES(2118979, 1008218, 'USER', 248953  , 'manager', '李叶', 0, '13600161256', 3, 999984);
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(13003, 'EhOrganizations', 1008218, 'EhUsers', 248953  , 1001, 1, UTC_TIMESTAMP());

INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`)
	VALUES(1874, 999984, 'COMMUNITY', 240111044331055835, UTC_TIMESTAMP());

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185911, UUID(), 999984, 2, 'EhGroups', 0,'清华信息港论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185912, UUID(), 999984, 2, 'EhGroups', 0,'清华信息港意见反馈论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());


INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`)
	VALUES ('15305', '0', '广东', 'GUANGDONG', 'GD', '/广东', '1', '1', '', '', '2', '2', '999984');
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`)
	VALUES ('15306', '15305', '深圳市', 'SHENZHENSHI', 'SZS', '/广东/深圳市', '2', '2', NULL, '0755', '2', '1', '999984');
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`)
	VALUES ('15307', '15306', '南山区', 'NANSHANQU', 'NSQ', '/广东/深圳市/南山区', '3', '3', NULL, '0755', '2', '0', '999984');


INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES(240111044331055835, UUID(), 15306, '深圳市',  15307, '南山区', '清华信息港', '清华信息港', '深圳市南山区科技园北区清华信息港', NULL, '2001年3月3日，深圳市人民政府和清华大学共同召开“清华信息港启动建设”新闻发布会，开始建设清华信息港。清华信息港园区占地4万平方米，规划建筑面积13.8万平方米，一期工程建筑面积6.7万平方米，2003年10月正式开园投入使用。二期工程（科研楼）建筑面积7.1万平方米，2014年12月正式投入使用。园区依托清华大学的科技创新优势，加强基础配套服务，营造创新创业环境。',
	NULL, NULL, NULL, NULL, NULL, NULL,NULL, 168, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'1', 185911, 185912, UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`)
	VALUES(240111044331051838, 240111044331055835, '', 113.960095, 22.547367, 'ws1030x5ytve');
INSERT INTO `eh_organization_communities`(organization_id, community_id)
	VALUES(1008218, 240111044331055835);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(181587, 240111044331055835, '综合楼', '综合楼', 0, NULL, '清华信息港', NULl, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999984);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(181588, 240111044331055835, 'A栋', 'A栋', 0, NULL, '清华信息港', NULl, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999984);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(181589, 240111044331055835, 'B栋', 'B栋', 0, NULL, '清华信息港', NULl, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999984);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(181590, 240111044331055835, '科研楼', '科研楼', 0, NULL, '清华信息港', NULl, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999984);


INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129871,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-101-2','综合楼','101-2','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129872,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-103','综合楼','103','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129873,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-105B','综合楼','105B','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129874,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-106','综合楼','106','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129875,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-107-108','综合楼','107-108','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129876,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-109','综合楼','109','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129877,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-110','综合楼','110','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129878,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-201','综合楼','201','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129879,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-202','综合楼','202','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129880,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-205','综合楼','205','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129881,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-206','综合楼','206','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129882,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-208','综合楼','208','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129883,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-301','综合楼','301','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129884,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-302','综合楼','302','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129885,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-303','综合楼','303','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129886,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-304','综合楼','304','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129887,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-306-8','综合楼','306-8','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129888,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-401-2','综合楼','401-2','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129889,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-403-4','综合楼','403-4','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129890,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-406','综合楼','406','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129891,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-408','综合楼','408','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129892,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-501-2','综合楼','501-2','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129893,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-503','综合楼','503','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129894,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-504','综合楼','504','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129895,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-505','综合楼','505','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129896,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-506','综合楼','506','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129897,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-601-2','综合楼','601-2','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129898,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-603','综合楼','603','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129899,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-604','综合楼','604','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129900,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-605','综合楼','605','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129901,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-606','综合楼','606','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129902,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-701-3','综合楼','701-3','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129903,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-704','综合楼','704','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129904,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-705','综合楼','705','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129905,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-706','综合楼','706','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129906,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-707','综合楼','707','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129907,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-708','综合楼','708','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129908,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-709','综合楼','709','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129909,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-801-3','综合楼','801-3','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129910,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-804','综合楼','804','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129911,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-805','综合楼','805','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129912,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-806','综合楼','806','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129913,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-807','综合楼','807','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129914,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-808A','综合楼','808A','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129915,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-808B','综合楼','808B','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129916,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-809','综合楼','809','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129917,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-926','综合楼','926','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129918,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-925','综合楼','925','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129919,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-923','综合楼','923','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129920,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-921','综合楼','921','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129921,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-918、920','综合楼','918、920','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129922,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-919','综合楼','919','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129923,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-915、917','综合楼','915、917','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129924,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-910','综合楼','910','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129925,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-909','综合楼','909','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129926,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'综合楼-901','综合楼','901','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129927,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'A栋-首层','A栋','首层','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129928,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'A栋-1F、2F','A栋','1F、2F','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129929,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'A栋-301','A栋','301','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129930,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'A栋-302','A栋','302','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129931,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'A栋-4F','A栋','4F','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129932,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'A栋-501-2','A栋','501-2','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129933,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'A栋-601B','A栋','601B','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129934,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'A栋-602','A栋','602','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129935,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'A栋-603','A栋','603','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129936,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'A栋-604','A栋','604','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129937,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'A栋-605','A栋','605','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129938,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'A栋-701','A栋','701','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129939,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'A栋-702','A栋','702','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129940,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'A栋-703','A栋','703','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129941,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'A栋-704','A栋','704','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129942,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'A栋-801','A栋','801','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129943,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'A栋-802','A栋','802','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129944,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'A栋-901-2','A栋','901-2','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129945,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'A栋-10F','A栋','10F','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129946,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'A栋-11F','A栋','11F','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129947,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'A栋-12F','A栋','12F','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129948,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'B栋-首层A区','B栋','首层A区','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129949,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'B栋-首层B区','B栋','首层B区','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129950,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'B栋-1FC区','B栋','1FC区','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129951,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'B栋-1F ','B栋','1F ','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129952,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'B栋-2F','B栋','2F','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129953,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'B栋-3F','B栋','3F','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129954,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'B栋-4F','B栋','4F','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129955,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'B栋-501','B栋','501','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129956,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'B栋-502','B栋','502','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129957,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'B栋-601','B栋','601','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129958,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'B栋-602','B栋','602','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129959,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'B栋-603-4','B栋','603-4','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129960,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'B栋-7F','B栋','7F','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129961,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'B栋-8F','B栋','8F','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129962,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'B栋-9F ','B栋','9F ','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129963,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'B栋-10F','B栋','10F','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129964,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'B栋-11F','B栋','11F','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129965,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'B栋-12F','B栋','12F','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129966,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-1208-10','科研楼','1208-10','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129967,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-1206-7','科研楼','1206-7','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129968,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-1204','科研楼','1204','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129969,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-1205','科研楼','1205','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129970,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-11F','科研楼','11F','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129971,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-1010','科研楼','1010','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129972,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-1008-9','科研楼','1008-9','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129973,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-1007','科研楼','1007','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129974,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-1006','科研楼','1006','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129975,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-1005','科研楼','1005','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129976,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-910','科研楼','910','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129977,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-909','科研楼','909','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129978,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-908','科研楼','908','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129979,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-908-1、1008-1','科研楼','908-1、1008-1','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129980,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-907','科研楼','907','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129981,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-906','科研楼','906','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129982,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-905','科研楼','905','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129983,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-904','科研楼','904','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129984,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-903','科研楼','903','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129985,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-902','科研楼','902','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129986,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-901','科研楼','901','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129987,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-810','科研楼','810','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129988,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-809','科研楼','809','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129989,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-808-1','科研楼','808-1','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129990,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-805-8','科研楼','805-8','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129991,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-804','科研楼','804','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129992,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-803','科研楼','803','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129993,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-802','科研楼','802','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129994,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-801','科研楼','801','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129995,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-708-1','科研楼','708-1','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129996,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-7F','科研楼','7F','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129997,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-609-10','科研楼','609-10','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129998,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-608-1','科研楼','608-1','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387129999,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-608','科研楼','608','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130000,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-607','科研楼','607','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130001,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-605-6','科研楼','605-6','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130002,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-603-4','科研楼','603-4','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130003,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-601-2','科研楼','601-2','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130004,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-508-1','科研楼','508-1','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130005,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-508-10','科研楼','508-10','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130006,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-503-7','科研楼','503-7','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130007,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-501-2','科研楼','501-2','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130008,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-410','科研楼','410','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130009,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-409','科研楼','409','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130010,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-408-1','科研楼','408-1','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130011,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-408','科研楼','408','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130012,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-407','科研楼','407','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130013,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-406','科研楼','406','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130014,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-403-5','科研楼','403-5','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130015,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-402','科研楼','402','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130016,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-401','科研楼','401','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130017,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-310','科研楼','310','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130018,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-309','科研楼','309','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130019,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-308','科研楼','308','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130020,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-308-1','科研楼','308-1','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130021,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-306-7','科研楼','306-7','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130022,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-305','科研楼','305','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130023,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-304','科研楼','304','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130024,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-303','科研楼','303','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130025,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-301-2','科研楼','301-2','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130026,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-210','科研楼','210','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130027,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-209','科研楼','209','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130028,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-208','科研楼','208','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130029,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-206-7','科研楼','206-7','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130030,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-205','科研楼','205','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130031,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-204','科研楼','204','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130032,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-203','科研楼','203','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130033,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-201-2','科研楼','201-2','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130034,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-111','科研楼','111','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130035,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-107-108','科研楼','107-108','2','0',UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387130036,UUID(),240111044331055835, 15306, '深圳市',  15307, '南山区' ,'科研楼-103-104','科研楼','103-104','2','0',UTC_TIMESTAMP(), 999984);

INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27321, 1008218, 240111044331055835, 239825274387129871, '综合楼-101-2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27322, 1008218, 240111044331055835, 239825274387129872, '综合楼-103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27323, 1008218, 240111044331055835, 239825274387129873, '综合楼-105B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27324, 1008218, 240111044331055835, 239825274387129874, '综合楼-106', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27325, 1008218, 240111044331055835, 239825274387129875, '综合楼-107-108', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27326, 1008218, 240111044331055835, 239825274387129876, '综合楼-109', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27327, 1008218, 240111044331055835, 239825274387129877, '综合楼-110', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27328, 1008218, 240111044331055835, 239825274387129878, '综合楼-201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27329, 1008218, 240111044331055835, 239825274387129879, '综合楼-202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27330, 1008218, 240111044331055835, 239825274387129880, '综合楼-205', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27331, 1008218, 240111044331055835, 239825274387129881, '综合楼-206', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27332, 1008218, 240111044331055835, 239825274387129882, '综合楼-208', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27333, 1008218, 240111044331055835, 239825274387129883, '综合楼-301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27334, 1008218, 240111044331055835, 239825274387129884, '综合楼-302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27335, 1008218, 240111044331055835, 239825274387129885, '综合楼-303', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27336, 1008218, 240111044331055835, 239825274387129886, '综合楼-304', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27337, 1008218, 240111044331055835, 239825274387129887, '综合楼-306-8', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27338, 1008218, 240111044331055835, 239825274387129888, '综合楼-401-2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27339, 1008218, 240111044331055835, 239825274387129889, '综合楼-403-4', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27340, 1008218, 240111044331055835, 239825274387129890, '综合楼-406', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27341, 1008218, 240111044331055835, 239825274387129891, '综合楼-408', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27342, 1008218, 240111044331055835, 239825274387129892, '综合楼-501-2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27343, 1008218, 240111044331055835, 239825274387129893, '综合楼-503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27344, 1008218, 240111044331055835, 239825274387129894, '综合楼-504', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27345, 1008218, 240111044331055835, 239825274387129895, '综合楼-505', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27346, 1008218, 240111044331055835, 239825274387129896, '综合楼-506', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27347, 1008218, 240111044331055835, 239825274387129897, '综合楼-601-2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27348, 1008218, 240111044331055835, 239825274387129898, '综合楼-603', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27349, 1008218, 240111044331055835, 239825274387129899, '综合楼-604', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27350, 1008218, 240111044331055835, 239825274387129900, '综合楼-605', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27351, 1008218, 240111044331055835, 239825274387129901, '综合楼-606', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27352, 1008218, 240111044331055835, 239825274387129902, '综合楼-701-3', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27353, 1008218, 240111044331055835, 239825274387129903, '综合楼-704', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27354, 1008218, 240111044331055835, 239825274387129904, '综合楼-705', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27355, 1008218, 240111044331055835, 239825274387129905, '综合楼-706', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27356, 1008218, 240111044331055835, 239825274387129906, '综合楼-707', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27357, 1008218, 240111044331055835, 239825274387129907, '综合楼-708', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27358, 1008218, 240111044331055835, 239825274387129908, '综合楼-709', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27359, 1008218, 240111044331055835, 239825274387129909, '综合楼-801-3', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27360, 1008218, 240111044331055835, 239825274387129910, '综合楼-804', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27361, 1008218, 240111044331055835, 239825274387129911, '综合楼-805', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27362, 1008218, 240111044331055835, 239825274387129912, '综合楼-806', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27363, 1008218, 240111044331055835, 239825274387129913, '综合楼-807', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27364, 1008218, 240111044331055835, 239825274387129914, '综合楼-808A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27365, 1008218, 240111044331055835, 239825274387129915, '综合楼-808B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27366, 1008218, 240111044331055835, 239825274387129916, '综合楼-809', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27367, 1008218, 240111044331055835, 239825274387129917, '综合楼-926', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27368, 1008218, 240111044331055835, 239825274387129918, '综合楼-925', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27369, 1008218, 240111044331055835, 239825274387129919, '综合楼-923', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27370, 1008218, 240111044331055835, 239825274387129920, '综合楼-921', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27371, 1008218, 240111044331055835, 239825274387129921, '综合楼-918、920', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27372, 1008218, 240111044331055835, 239825274387129922, '综合楼-919', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27373, 1008218, 240111044331055835, 239825274387129923, '综合楼-915、917', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27374, 1008218, 240111044331055835, 239825274387129924, '综合楼-910', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27375, 1008218, 240111044331055835, 239825274387129925, '综合楼-909', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27376, 1008218, 240111044331055835, 239825274387129926, '综合楼-901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27377, 1008218, 240111044331055835, 239825274387129927, 'A栋-首层', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27378, 1008218, 240111044331055835, 239825274387129928, 'A栋-1F、2F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27379, 1008218, 240111044331055835, 239825274387129929, 'A栋-301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27380, 1008218, 240111044331055835, 239825274387129930, 'A栋-302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27381, 1008218, 240111044331055835, 239825274387129931, 'A栋-4F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27382, 1008218, 240111044331055835, 239825274387129932, 'A栋-501-2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27383, 1008218, 240111044331055835, 239825274387129933, 'A栋-601B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27384, 1008218, 240111044331055835, 239825274387129934, 'A栋-602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27385, 1008218, 240111044331055835, 239825274387129935, 'A栋-603', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27386, 1008218, 240111044331055835, 239825274387129936, 'A栋-604', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27387, 1008218, 240111044331055835, 239825274387129937, 'A栋-605', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27388, 1008218, 240111044331055835, 239825274387129938, 'A栋-701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27389, 1008218, 240111044331055835, 239825274387129939, 'A栋-702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27390, 1008218, 240111044331055835, 239825274387129940, 'A栋-703', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27391, 1008218, 240111044331055835, 239825274387129941, 'A栋-704', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27392, 1008218, 240111044331055835, 239825274387129942, 'A栋-801', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27393, 1008218, 240111044331055835, 239825274387129943, 'A栋-802', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27394, 1008218, 240111044331055835, 239825274387129944, 'A栋-901-2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27395, 1008218, 240111044331055835, 239825274387129945, 'A栋-10F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27396, 1008218, 240111044331055835, 239825274387129946, 'A栋-11F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27397, 1008218, 240111044331055835, 239825274387129947, 'A栋-12F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27398, 1008218, 240111044331055835, 239825274387129948, 'B栋-首层A区', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27399, 1008218, 240111044331055835, 239825274387129949, 'B栋-首层B区', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27400, 1008218, 240111044331055835, 239825274387129950, 'B栋-1FC区', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27401, 1008218, 240111044331055835, 239825274387129951, 'B栋-1F ', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27402, 1008218, 240111044331055835, 239825274387129952, 'B栋-2F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27403, 1008218, 240111044331055835, 239825274387129953, 'B栋-3F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27404, 1008218, 240111044331055835, 239825274387129954, 'B栋-4F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27405, 1008218, 240111044331055835, 239825274387129955, 'B栋-501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27406, 1008218, 240111044331055835, 239825274387129956, 'B栋-502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27407, 1008218, 240111044331055835, 239825274387129957, 'B栋-601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27408, 1008218, 240111044331055835, 239825274387129958, 'B栋-602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27409, 1008218, 240111044331055835, 239825274387129959, 'B栋-603-4', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27410, 1008218, 240111044331055835, 239825274387129960, 'B栋-7F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27411, 1008218, 240111044331055835, 239825274387129961, 'B栋-8F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27412, 1008218, 240111044331055835, 239825274387129962, 'B栋-9F ', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27413, 1008218, 240111044331055835, 239825274387129963, 'B栋-10F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27414, 1008218, 240111044331055835, 239825274387129964, 'B栋-11F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27415, 1008218, 240111044331055835, 239825274387129965, 'B栋-12F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27416, 1008218, 240111044331055835, 239825274387129966, '科研楼-1208-10', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27417, 1008218, 240111044331055835, 239825274387129967, '科研楼-1206-7', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27418, 1008218, 240111044331055835, 239825274387129968, '科研楼-1204', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27419, 1008218, 240111044331055835, 239825274387129969, '科研楼-1205', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27420, 1008218, 240111044331055835, 239825274387129970, '科研楼-11F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27421, 1008218, 240111044331055835, 239825274387129971, '科研楼-1010', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27422, 1008218, 240111044331055835, 239825274387129972, '科研楼-1008-9', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27423, 1008218, 240111044331055835, 239825274387129973, '科研楼-1007', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27424, 1008218, 240111044331055835, 239825274387129974, '科研楼-1006', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27425, 1008218, 240111044331055835, 239825274387129975, '科研楼-1005', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27426, 1008218, 240111044331055835, 239825274387129976, '科研楼-910', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27427, 1008218, 240111044331055835, 239825274387129977, '科研楼-909', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27428, 1008218, 240111044331055835, 239825274387129978, '科研楼-908', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27429, 1008218, 240111044331055835, 239825274387129979, '科研楼-908-1、1008-1', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27430, 1008218, 240111044331055835, 239825274387129980, '科研楼-907', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27431, 1008218, 240111044331055835, 239825274387129981, '科研楼-906', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27432, 1008218, 240111044331055835, 239825274387129982, '科研楼-905', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27433, 1008218, 240111044331055835, 239825274387129983, '科研楼-904', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27434, 1008218, 240111044331055835, 239825274387129984, '科研楼-903', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27435, 1008218, 240111044331055835, 239825274387129985, '科研楼-902', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27436, 1008218, 240111044331055835, 239825274387129986, '科研楼-901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27437, 1008218, 240111044331055835, 239825274387129987, '科研楼-810', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27438, 1008218, 240111044331055835, 239825274387129988, '科研楼-809', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27439, 1008218, 240111044331055835, 239825274387129989, '科研楼-808-1', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27440, 1008218, 240111044331055835, 239825274387129990, '科研楼-805-8', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27441, 1008218, 240111044331055835, 239825274387129991, '科研楼-804', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27442, 1008218, 240111044331055835, 239825274387129992, '科研楼-803', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27443, 1008218, 240111044331055835, 239825274387129993, '科研楼-802', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27444, 1008218, 240111044331055835, 239825274387129994, '科研楼-801', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27445, 1008218, 240111044331055835, 239825274387129995, '科研楼-708-1', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27446, 1008218, 240111044331055835, 239825274387129996, '科研楼-7F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27447, 1008218, 240111044331055835, 239825274387129997, '科研楼-609-10', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27448, 1008218, 240111044331055835, 239825274387129998, '科研楼-608-1', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27449, 1008218, 240111044331055835, 239825274387129999, '科研楼-608', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27450, 1008218, 240111044331055835, 239825274387130000, '科研楼-607', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27451, 1008218, 240111044331055835, 239825274387130001, '科研楼-605-6', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27452, 1008218, 240111044331055835, 239825274387130002, '科研楼-603-4', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27453, 1008218, 240111044331055835, 239825274387130003, '科研楼-601-2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27454, 1008218, 240111044331055835, 239825274387130004, '科研楼-508-1', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27455, 1008218, 240111044331055835, 239825274387130005, '科研楼-508-10', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27456, 1008218, 240111044331055835, 239825274387130006, '科研楼-503-7', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27457, 1008218, 240111044331055835, 239825274387130007, '科研楼-501-2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27458, 1008218, 240111044331055835, 239825274387130008, '科研楼-410', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27459, 1008218, 240111044331055835, 239825274387130009, '科研楼-409', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27460, 1008218, 240111044331055835, 239825274387130010, '科研楼-408-1', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27461, 1008218, 240111044331055835, 239825274387130011, '科研楼-408', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27462, 1008218, 240111044331055835, 239825274387130012, '科研楼-407', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27463, 1008218, 240111044331055835, 239825274387130013, '科研楼-406', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27464, 1008218, 240111044331055835, 239825274387130014, '科研楼-403-5', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27465, 1008218, 240111044331055835, 239825274387130015, '科研楼-402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27466, 1008218, 240111044331055835, 239825274387130016, '科研楼-401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27467, 1008218, 240111044331055835, 239825274387130017, '科研楼-310', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27468, 1008218, 240111044331055835, 239825274387130018, '科研楼-309', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27469, 1008218, 240111044331055835, 239825274387130019, '科研楼-308', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27470, 1008218, 240111044331055835, 239825274387130020, '科研楼-308-1', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27471, 1008218, 240111044331055835, 239825274387130021, '科研楼-306-7', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27472, 1008218, 240111044331055835, 239825274387130022, '科研楼-305', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27473, 1008218, 240111044331055835, 239825274387130023, '科研楼-304', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27474, 1008218, 240111044331055835, 239825274387130024, '科研楼-303', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27475, 1008218, 240111044331055835, 239825274387130025, '科研楼-301-2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27476, 1008218, 240111044331055835, 239825274387130026, '科研楼-210', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27477, 1008218, 240111044331055835, 239825274387130027, '科研楼-209', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27478, 1008218, 240111044331055835, 239825274387130028, '科研楼-208', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27479, 1008218, 240111044331055835, 239825274387130029, '科研楼-206-7', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27480, 1008218, 240111044331055835, 239825274387130030, '科研楼-205', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27481, 1008218, 240111044331055835, 239825274387130031, '科研楼-204', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27482, 1008218, 240111044331055835, 239825274387130032, '科研楼-203', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27483, 1008218, 240111044331055835, 239825274387130033, '科研楼-201-2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27484, 1008218, 240111044331055835, 239825274387130034, '科研楼-111', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27485, 1008218, 240111044331055835, 239825274387130035, '科研楼-107-108', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (27486, 1008218, 240111044331055835, 239825274387130036, '科研楼-103-104', '0');


INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009093, UUID(), '深圳日海通讯技术股份有限公司', '深圳日海通讯技术股份有限公司', 1, 1, 1008221, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185916, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185916, UUID(), 999984, 2, 'EhGroups', 1009093,'深圳日海通讯技术股份有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008221, 0, 'ENTERPRISE', '深圳日海通讯技术股份有限公司', 0, '', '/1008221', 1, 2, 'ENTERPRISE', 999984, 1009093);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118028, 240111044331055835, 'organization', 1008221, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009094, UUID(), '深圳市慧脉科技有限公司', '深圳市慧脉科技有限公司', 1, 1, 1008222, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185917, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185917, UUID(), 999984, 2, 'EhGroups', 1009094,'深圳市慧脉科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008222, 0, 'ENTERPRISE', '深圳市慧脉科技有限公司', 0, '', '/1008222', 1, 2, 'ENTERPRISE', 999984, 1009094);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118029, 240111044331055835, 'organization', 1008222, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009095, UUID(), '深圳欧德蒙科技有限公司', '深圳欧德蒙科技有限公司', 1, 1, 1008223, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185918, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185918, UUID(), 999984, 2, 'EhGroups', 1009095,'深圳欧德蒙科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008223, 0, 'ENTERPRISE', '深圳欧德蒙科技有限公司', 0, '', '/1008223', 1, 2, 'ENTERPRISE', 999984, 1009095);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118030, 240111044331055835, 'organization', 1008223, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009096, UUID(), '中太数据通信（深圳）有限公司', '中太数据通信（深圳）有限公司', 1, 1, 1008224, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185919, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185919, UUID(), 999984, 2, 'EhGroups', 1009096,'中太数据通信（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008224, 0, 'ENTERPRISE', '中太数据通信（深圳）有限公司', 0, '', '/1008224', 1, 2, 'ENTERPRISE', 999984, 1009096);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118031, 240111044331055835, 'organization', 1008224, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009097, UUID(), '深圳市极酷威视科技有现公司', '深圳市极酷威视科技有现公司', 1, 1, 1008225, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185920, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185920, UUID(), 999984, 2, 'EhGroups', 1009097,'深圳市极酷威视科技有现公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008225, 0, 'ENTERPRISE', '深圳市极酷威视科技有现公司', 0, '', '/1008225', 1, 2, 'ENTERPRISE', 999984, 1009097);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118032, 240111044331055835, 'organization', 1008225, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009098, UUID(), '深圳市中码易通科技有限公司', '深圳市中码易通科技有限公司', 1, 1, 1008226, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185921, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185921, UUID(), 999984, 2, 'EhGroups', 1009098,'深圳市中码易通科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008226, 0, 'ENTERPRISE', '深圳市中码易通科技有限公司', 0, '', '/1008226', 1, 2, 'ENTERPRISE', 999984, 1009098);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118033, 240111044331055835, 'organization', 1008226, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009099, UUID(), '深圳市惠泰医疗器械有限公司', '深圳市惠泰医疗器械有限公司', 1, 1, 1008227, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185922, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185922, UUID(), 999984, 2, 'EhGroups', 1009099,'深圳市惠泰医疗器械有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008227, 0, 'ENTERPRISE', '深圳市惠泰医疗器械有限公司', 0, '', '/1008227', 1, 2, 'ENTERPRISE', 999984, 1009099);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118034, 240111044331055835, 'organization', 1008227, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009100, UUID(), '深圳市新良田科技有限公司', '深圳市新良田科技有限公司', 1, 1, 1008228, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185923, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185923, UUID(), 999984, 2, 'EhGroups', 1009100,'深圳市新良田科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008228, 0, 'ENTERPRISE', '深圳市新良田科技有限公司', 0, '', '/1008228', 1, 2, 'ENTERPRISE', 999984, 1009100);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118035, 240111044331055835, 'organization', 1008228, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009101, UUID(), '深圳市通易信科技开发有限公司', '深圳市通易信科技开发有限公司', 1, 1, 1008229, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185924, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185924, UUID(), 999984, 2, 'EhGroups', 1009101,'深圳市通易信科技开发有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008229, 0, 'ENTERPRISE', '深圳市通易信科技开发有限公司', 0, '', '/1008229', 1, 2, 'ENTERPRISE', 999984, 1009101);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118036, 240111044331055835, 'organization', 1008229, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009102, UUID(), '深圳市华元智能系统集成有限公司', '深圳市华元智能系统集成有限公司', 1, 1, 1008230, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185925, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185925, UUID(), 999984, 2, 'EhGroups', 1009102,'深圳市华元智能系统集成有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008230, 0, 'ENTERPRISE', '深圳市华元智能系统集成有限公司', 0, '', '/1008230', 1, 2, 'ENTERPRISE', 999984, 1009102);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118037, 240111044331055835, 'organization', 1008230, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009103, UUID(), '深圳市七社网络科技有限公司', '深圳市七社网络科技有限公司', 1, 1, 1008231, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185926, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185926, UUID(), 999984, 2, 'EhGroups', 1009103,'深圳市七社网络科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008231, 0, 'ENTERPRISE', '深圳市七社网络科技有限公司', 0, '', '/1008231', 1, 2, 'ENTERPRISE', 999984, 1009103);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118038, 240111044331055835, 'organization', 1008231, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009104, UUID(), '深圳市叮咚互联网酒店管理有限公司', '深圳市叮咚互联网酒店管理有限公司', 1, 1, 1008232, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185927, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185927, UUID(), 999984, 2, 'EhGroups', 1009104,'深圳市叮咚互联网酒店管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008232, 0, 'ENTERPRISE', '深圳市叮咚互联网酒店管理有限公司', 0, '', '/1008232', 1, 2, 'ENTERPRISE', 999984, 1009104);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118039, 240111044331055835, 'organization', 1008232, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009105, UUID(), '深圳市益科光电技术有限公司', '深圳市益科光电技术有限公司', 1, 1, 1008233, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185928, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185928, UUID(), 999984, 2, 'EhGroups', 1009105,'深圳市益科光电技术有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008233, 0, 'ENTERPRISE', '深圳市益科光电技术有限公司', 0, '', '/1008233', 1, 2, 'ENTERPRISE', 999984, 1009105);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118040, 240111044331055835, 'organization', 1008233, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009106, UUID(), '北京居天下网络科技有限公司', '北京居天下网络科技有限公司', 1, 1, 1008234, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185929, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185929, UUID(), 999984, 2, 'EhGroups', 1009106,'北京居天下网络科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008234, 0, 'ENTERPRISE', '北京居天下网络科技有限公司', 0, '', '/1008234', 1, 2, 'ENTERPRISE', 999984, 1009106);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118041, 240111044331055835, 'organization', 1008234, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009107, UUID(), '升迈科技（深圳）有限公司', '升迈科技（深圳）有限公司', 1, 1, 1008235, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185930, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185930, UUID(), 999984, 2, 'EhGroups', 1009107,'升迈科技（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008235, 0, 'ENTERPRISE', '升迈科技（深圳）有限公司', 0, '', '/1008235', 1, 2, 'ENTERPRISE', 999984, 1009107);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118042, 240111044331055835, 'organization', 1008235, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009108, UUID(), '深圳市礼魔方科技发展有限公司', '深圳市礼魔方科技发展有限公司', 1, 1, 1008236, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185931, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185931, UUID(), 999984, 2, 'EhGroups', 1009108,'深圳市礼魔方科技发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008236, 0, 'ENTERPRISE', '深圳市礼魔方科技发展有限公司', 0, '', '/1008236', 1, 2, 'ENTERPRISE', 999984, 1009108);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118043, 240111044331055835, 'organization', 1008236, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009109, UUID(), '东莞轩朗实业有限公司', '东莞轩朗实业有限公司', 1, 1, 1008237, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185932, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185932, UUID(), 999984, 2, 'EhGroups', 1009109,'东莞轩朗实业有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008237, 0, 'ENTERPRISE', '东莞轩朗实业有限公司', 0, '', '/1008237', 1, 2, 'ENTERPRISE', 999984, 1009109);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118044, 240111044331055835, 'organization', 1008237, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009110, UUID(), '深圳市南电云商有限公司', '深圳市南电云商有限公司', 1, 1, 1008238, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185933, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185933, UUID(), 999984, 2, 'EhGroups', 1009110,'深圳市南电云商有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008238, 0, 'ENTERPRISE', '深圳市南电云商有限公司', 0, '', '/1008238', 1, 2, 'ENTERPRISE', 999984, 1009110);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118045, 240111044331055835, 'organization', 1008238, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009111, UUID(), '深圳中科华恒科技有限公司', '深圳中科华恒科技有限公司', 1, 1, 1008239, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185934, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185934, UUID(), 999984, 2, 'EhGroups', 1009111,'深圳中科华恒科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008239, 0, 'ENTERPRISE', '深圳中科华恒科技有限公司', 0, '', '/1008239', 1, 2, 'ENTERPRISE', 999984, 1009111);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118046, 240111044331055835, 'organization', 1008239, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009112, UUID(), '迈科微电子（深圳）有限公司', '迈科微电子（深圳）有限公司', 1, 1, 1008240, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185935, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185935, UUID(), 999984, 2, 'EhGroups', 1009112,'迈科微电子（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008240, 0, 'ENTERPRISE', '迈科微电子（深圳）有限公司', 0, '', '/1008240', 1, 2, 'ENTERPRISE', 999984, 1009112);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118047, 240111044331055835, 'organization', 1008240, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009113, UUID(), '艾体威尔电子技术(北京)有限公司', '艾体威尔电子技术(北京)有限公司', 1, 1, 1008241, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185936, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185936, UUID(), 999984, 2, 'EhGroups', 1009113,'艾体威尔电子技术(北京)有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008241, 0, 'ENTERPRISE', '艾体威尔电子技术(北京)有限公司', 0, '', '/1008241', 1, 2, 'ENTERPRISE', 999984, 1009113);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118048, 240111044331055835, 'organization', 1008241, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009114, UUID(), '石家庄诚志永华显示材料有限公司', '石家庄诚志永华显示材料有限公司', 1, 1, 1008242, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185937, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185937, UUID(), 999984, 2, 'EhGroups', 1009114,'石家庄诚志永华显示材料有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008242, 0, 'ENTERPRISE', '石家庄诚志永华显示材料有限公司', 0, '', '/1008242', 1, 2, 'ENTERPRISE', 999984, 1009114);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118049, 240111044331055835, 'organization', 1008242, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009115, UUID(), '深圳市百川视界数字技术有限责任公司', '深圳市百川视界数字技术有限责任公司', 1, 1, 1008243, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185938, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185938, UUID(), 999984, 2, 'EhGroups', 1009115,'深圳市百川视界数字技术有限责任公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008243, 0, 'ENTERPRISE', '深圳市百川视界数字技术有限责任公司', 0, '', '/1008243', 1, 2, 'ENTERPRISE', 999984, 1009115);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118050, 240111044331055835, 'organization', 1008243, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009116, UUID(), '深圳小鹿乱撞影视传媒有限公司', '深圳小鹿乱撞影视传媒有限公司', 1, 1, 1008244, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185939, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185939, UUID(), 999984, 2, 'EhGroups', 1009116,'深圳小鹿乱撞影视传媒有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008244, 0, 'ENTERPRISE', '深圳小鹿乱撞影视传媒有限公司', 0, '', '/1008244', 1, 2, 'ENTERPRISE', 999984, 1009116);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118051, 240111044331055835, 'organization', 1008244, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009117, UUID(), '深圳市立鼎通移动技术有限公司', '深圳市立鼎通移动技术有限公司', 1, 1, 1008245, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185940, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185940, UUID(), 999984, 2, 'EhGroups', 1009117,'深圳市立鼎通移动技术有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008245, 0, 'ENTERPRISE', '深圳市立鼎通移动技术有限公司', 0, '', '/1008245', 1, 2, 'ENTERPRISE', 999984, 1009117);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118052, 240111044331055835, 'organization', 1008245, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009118, UUID(), '深圳市前海堃元智慧有限公司', '深圳市前海堃元智慧有限公司', 1, 1, 1008246, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185941, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185941, UUID(), 999984, 2, 'EhGroups', 1009118,'深圳市前海堃元智慧有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008246, 0, 'ENTERPRISE', '深圳市前海堃元智慧有限公司', 0, '', '/1008246', 1, 2, 'ENTERPRISE', 999984, 1009118);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118053, 240111044331055835, 'organization', 1008246, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009119, UUID(), '深圳市鲁西人力资源开发有限公司', '深圳市鲁西人力资源开发有限公司', 1, 1, 1008247, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185942, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185942, UUID(), 999984, 2, 'EhGroups', 1009119,'深圳市鲁西人力资源开发有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008247, 0, 'ENTERPRISE', '深圳市鲁西人力资源开发有限公司', 0, '', '/1008247', 1, 2, 'ENTERPRISE', 999984, 1009119);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118054, 240111044331055835, 'organization', 1008247, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009120, UUID(), '创扬通信技术（深圳）有限公司', '创扬通信技术（深圳）有限公司', 1, 1, 1008248, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185943, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185943, UUID(), 999984, 2, 'EhGroups', 1009120,'创扬通信技术（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008248, 0, 'ENTERPRISE', '创扬通信技术（深圳）有限公司', 0, '', '/1008248', 1, 2, 'ENTERPRISE', 999984, 1009120);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118055, 240111044331055835, 'organization', 1008248, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009121, UUID(), '深圳市光迹科技有限公司', '深圳市光迹科技有限公司', 1, 1, 1008249, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185944, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185944, UUID(), 999984, 2, 'EhGroups', 1009121,'深圳市光迹科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008249, 0, 'ENTERPRISE', '深圳市光迹科技有限公司', 0, '', '/1008249', 1, 2, 'ENTERPRISE', 999984, 1009121);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118056, 240111044331055835, 'organization', 1008249, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009122, UUID(), '北京释码大华科技有限公司', '北京释码大华科技有限公司', 1, 1, 1008250, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185945, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185945, UUID(), 999984, 2, 'EhGroups', 1009122,'北京释码大华科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008250, 0, 'ENTERPRISE', '北京释码大华科技有限公司', 0, '', '/1008250', 1, 2, 'ENTERPRISE', 999984, 1009122);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118057, 240111044331055835, 'organization', 1008250, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009123, UUID(), '深圳市合晟鑫电子有限公司', '深圳市合晟鑫电子有限公司', 1, 1, 1008251, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185946, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185946, UUID(), 999984, 2, 'EhGroups', 1009123,'深圳市合晟鑫电子有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008251, 0, 'ENTERPRISE', '深圳市合晟鑫电子有限公司', 0, '', '/1008251', 1, 2, 'ENTERPRISE', 999984, 1009123);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118058, 240111044331055835, 'organization', 1008251, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009124, UUID(), '深圳市朗特伟业科技有限公司', '深圳市朗特伟业科技有限公司', 1, 1, 1008252, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185947, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185947, UUID(), 999984, 2, 'EhGroups', 1009124,'深圳市朗特伟业科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008252, 0, 'ENTERPRISE', '深圳市朗特伟业科技有限公司', 0, '', '/1008252', 1, 2, 'ENTERPRISE', 999984, 1009124);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118059, 240111044331055835, 'organization', 1008252, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009125, UUID(), '尖锋晶片（香港）有限公司深圳代表处', '尖锋晶片（香港）有限公司深圳代表处', 1, 1, 1008253, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185948, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185948, UUID(), 999984, 2, 'EhGroups', 1009125,'尖锋晶片（香港）有限公司深圳代表处','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008253, 0, 'ENTERPRISE', '尖锋晶片（香港）有限公司深圳代表处', 0, '', '/1008253', 1, 2, 'ENTERPRISE', 999984, 1009125);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118060, 240111044331055835, 'organization', 1008253, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009126, UUID(), '深圳市空管通信技术发展有限公司', '深圳市空管通信技术发展有限公司', 1, 1, 1008254, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185949, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185949, UUID(), 999984, 2, 'EhGroups', 1009126,'深圳市空管通信技术发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008254, 0, 'ENTERPRISE', '深圳市空管通信技术发展有限公司', 0, '', '/1008254', 1, 2, 'ENTERPRISE', 999984, 1009126);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118061, 240111044331055835, 'organization', 1008254, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009127, UUID(), '深圳市金溢实业有限公司', '深圳市金溢实业有限公司', 1, 1, 1008255, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185950, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185950, UUID(), 999984, 2, 'EhGroups', 1009127,'深圳市金溢实业有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008255, 0, 'ENTERPRISE', '深圳市金溢实业有限公司', 0, '', '/1008255', 1, 2, 'ENTERPRISE', 999984, 1009127);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118062, 240111044331055835, 'organization', 1008255, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009128, UUID(), '深圳立尊科技有限公司', '深圳立尊科技有限公司', 1, 1, 1008256, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185951, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185951, UUID(), 999984, 2, 'EhGroups', 1009128,'深圳立尊科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008256, 0, 'ENTERPRISE', '深圳立尊科技有限公司', 0, '', '/1008256', 1, 2, 'ENTERPRISE', 999984, 1009128);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118063, 240111044331055835, 'organization', 1008256, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009129, UUID(), '深圳市华星视讯科技有限公司', '深圳市华星视讯科技有限公司', 1, 1, 1008257, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185952, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185952, UUID(), 999984, 2, 'EhGroups', 1009129,'深圳市华星视讯科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008257, 0, 'ENTERPRISE', '深圳市华星视讯科技有限公司', 0, '', '/1008257', 1, 2, 'ENTERPRISE', 999984, 1009129);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118064, 240111044331055835, 'organization', 1008257, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009130, UUID(), '深圳市徽通科技有限公司', '深圳市徽通科技有限公司', 1, 1, 1008258, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185953, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185953, UUID(), 999984, 2, 'EhGroups', 1009130,'深圳市徽通科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008258, 0, 'ENTERPRISE', '深圳市徽通科技有限公司', 0, '', '/1008258', 1, 2, 'ENTERPRISE', 999984, 1009130);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118065, 240111044331055835, 'organization', 1008258, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009131, UUID(), '深圳市天达康基因工程有限公司', '深圳市天达康基因工程有限公司', 1, 1, 1008259, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185954, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185954, UUID(), 999984, 2, 'EhGroups', 1009131,'深圳市天达康基因工程有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008259, 0, 'ENTERPRISE', '深圳市天达康基因工程有限公司', 0, '', '/1008259', 1, 2, 'ENTERPRISE', 999984, 1009131);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118066, 240111044331055835, 'organization', 1008259, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009132, UUID(), '深圳市汇北川电子技术有限公司', '深圳市汇北川电子技术有限公司', 1, 1, 1008260, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185955, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185955, UUID(), 999984, 2, 'EhGroups', 1009132,'深圳市汇北川电子技术有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008260, 0, 'ENTERPRISE', '深圳市汇北川电子技术有限公司', 0, '', '/1008260', 1, 2, 'ENTERPRISE', 999984, 1009132);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118067, 240111044331055835, 'organization', 1008260, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009133, UUID(), '深圳力合高科技有限公司', '深圳力合高科技有限公司', 1, 1, 1008261, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185956, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185956, UUID(), 999984, 2, 'EhGroups', 1009133,'深圳力合高科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008261, 0, 'ENTERPRISE', '深圳力合高科技有限公司', 0, '', '/1008261', 1, 2, 'ENTERPRISE', 999984, 1009133);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118068, 240111044331055835, 'organization', 1008261, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009134, UUID(), '深圳市辉途致信汽车科技有限公司', '深圳市辉途致信汽车科技有限公司', 1, 1, 1008262, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185957, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185957, UUID(), 999984, 2, 'EhGroups', 1009134,'深圳市辉途致信汽车科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008262, 0, 'ENTERPRISE', '深圳市辉途致信汽车科技有限公司', 0, '', '/1008262', 1, 2, 'ENTERPRISE', 999984, 1009134);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118069, 240111044331055835, 'organization', 1008262, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009135, UUID(), '深圳市企慧信息科技有限公司', '深圳市企慧信息科技有限公司', 1, 1, 1008263, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185958, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185958, UUID(), 999984, 2, 'EhGroups', 1009135,'深圳市企慧信息科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008263, 0, 'ENTERPRISE', '深圳市企慧信息科技有限公司', 0, '', '/1008263', 1, 2, 'ENTERPRISE', 999984, 1009135);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118070, 240111044331055835, 'organization', 1008263, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009136, UUID(), '深圳市伟康信息咨询有限公司', '深圳市伟康信息咨询有限公司', 1, 1, 1008264, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185959, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185959, UUID(), 999984, 2, 'EhGroups', 1009136,'深圳市伟康信息咨询有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008264, 0, 'ENTERPRISE', '深圳市伟康信息咨询有限公司', 0, '', '/1008264', 1, 2, 'ENTERPRISE', 999984, 1009136);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118071, 240111044331055835, 'organization', 1008264, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009137, UUID(), '深圳市弘光大有科技有限公司', '深圳市弘光大有科技有限公司', 1, 1, 1008265, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185960, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185960, UUID(), 999984, 2, 'EhGroups', 1009137,'深圳市弘光大有科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008265, 0, 'ENTERPRISE', '深圳市弘光大有科技有限公司', 0, '', '/1008265', 1, 2, 'ENTERPRISE', 999984, 1009137);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118072, 240111044331055835, 'organization', 1008265, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009138, UUID(), '深圳前海中清龙图教育科技有限公司', '深圳前海中清龙图教育科技有限公司', 1, 1, 1008266, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185961, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185961, UUID(), 999984, 2, 'EhGroups', 1009138,'深圳前海中清龙图教育科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008266, 0, 'ENTERPRISE', '深圳前海中清龙图教育科技有限公司', 0, '', '/1008266', 1, 2, 'ENTERPRISE', 999984, 1009138);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118073, 240111044331055835, 'organization', 1008266, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009139, UUID(), '深圳市杰和科技发展有限公司', '深圳市杰和科技发展有限公司', 1, 1, 1008267, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185962, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185962, UUID(), 999984, 2, 'EhGroups', 1009139,'深圳市杰和科技发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008267, 0, 'ENTERPRISE', '深圳市杰和科技发展有限公司', 0, '', '/1008267', 1, 2, 'ENTERPRISE', 999984, 1009139);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118074, 240111044331055835, 'organization', 1008267, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009140, UUID(), '宇星科技发展（深圳）有限公司', '宇星科技发展（深圳）有限公司', 1, 1, 1008268, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185963, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185963, UUID(), 999984, 2, 'EhGroups', 1009140,'宇星科技发展（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008268, 0, 'ENTERPRISE', '宇星科技发展（深圳）有限公司', 0, '', '/1008268', 1, 2, 'ENTERPRISE', 999984, 1009140);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118075, 240111044331055835, 'organization', 1008268, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009141, UUID(), '中太数据通信(深圳)有限公司', '中太数据通信(深圳)有限公司', 1, 1, 1008269, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185964, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185964, UUID(), 999984, 2, 'EhGroups', 1009141,'中太数据通信(深圳)有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008269, 0, 'ENTERPRISE', '中太数据通信(深圳)有限公司', 0, '', '/1008269', 1, 2, 'ENTERPRISE', 999984, 1009141);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118076, 240111044331055835, 'organization', 1008269, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009142, UUID(), '凯润银科信息技术（深圳）有限公司', '凯润银科信息技术（深圳）有限公司', 1, 1, 1008270, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185965, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185965, UUID(), 999984, 2, 'EhGroups', 1009142,'凯润银科信息技术（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008270, 0, 'ENTERPRISE', '凯润银科信息技术（深圳）有限公司', 0, '', '/1008270', 1, 2, 'ENTERPRISE', 999984, 1009142);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118077, 240111044331055835, 'organization', 1008270, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009143, UUID(), '华润万家有限公司', '华润万家有限公司', 1, 1, 1008271, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185966, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185966, UUID(), 999984, 2, 'EhGroups', 1009143,'华润万家有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008271, 0, 'ENTERPRISE', '华润万家有限公司', 0, '', '/1008271', 1, 2, 'ENTERPRISE', 999984, 1009143);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118078, 240111044331055835, 'organization', 1008271, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009144, UUID(), '深圳市成为智能交通系统有限公司', '深圳市成为智能交通系统有限公司', 1, 1, 1008272, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185967, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185967, UUID(), 999984, 2, 'EhGroups', 1009144,'深圳市成为智能交通系统有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008272, 0, 'ENTERPRISE', '深圳市成为智能交通系统有限公司', 0, '', '/1008272', 1, 2, 'ENTERPRISE', 999984, 1009144);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118079, 240111044331055835, 'organization', 1008272, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009145, UUID(), '伟龙金溢科技(深圳)有限公司', '伟龙金溢科技(深圳)有限公司', 1, 1, 1008273, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185968, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185968, UUID(), 999984, 2, 'EhGroups', 1009145,'伟龙金溢科技(深圳)有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008273, 0, 'ENTERPRISE', '伟龙金溢科技(深圳)有限公司', 0, '', '/1008273', 1, 2, 'ENTERPRISE', 999984, 1009145);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118080, 240111044331055835, 'organization', 1008273, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009146, UUID(), '深圳红立信金融科技有限公司', '深圳红立信金融科技有限公司', 1, 1, 1008274, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185969, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185969, UUID(), 999984, 2, 'EhGroups', 1009146,'深圳红立信金融科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008274, 0, 'ENTERPRISE', '深圳红立信金融科技有限公司', 0, '', '/1008274', 1, 2, 'ENTERPRISE', 999984, 1009146);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118081, 240111044331055835, 'organization', 1008274, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009147, UUID(), '诺德基金管理有限公司', '诺德基金管理有限公司', 1, 1, 1008275, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185970, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185970, UUID(), 999984, 2, 'EhGroups', 1009147,'诺德基金管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008275, 0, 'ENTERPRISE', '诺德基金管理有限公司', 0, '', '/1008275', 1, 2, 'ENTERPRISE', 999984, 1009147);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118082, 240111044331055835, 'organization', 1008275, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009148, UUID(), '深圳市易讯诚信息技术有限公司', '深圳市易讯诚信息技术有限公司', 1, 1, 1008276, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185971, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185971, UUID(), 999984, 2, 'EhGroups', 1009148,'深圳市易讯诚信息技术有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008276, 0, 'ENTERPRISE', '深圳市易讯诚信息技术有限公司', 0, '', '/1008276', 1, 2, 'ENTERPRISE', 999984, 1009148);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118083, 240111044331055835, 'organization', 1008276, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009149, UUID(), '深圳市浩择科技有限公司', '深圳市浩择科技有限公司', 1, 1, 1008277, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185972, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185972, UUID(), 999984, 2, 'EhGroups', 1009149,'深圳市浩择科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008277, 0, 'ENTERPRISE', '深圳市浩择科技有限公司', 0, '', '/1008277', 1, 2, 'ENTERPRISE', 999984, 1009149);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118084, 240111044331055835, 'organization', 1008277, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009150, UUID(), '深圳市沃达孚科技有限公司', '深圳市沃达孚科技有限公司', 1, 1, 1008278, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185973, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185973, UUID(), 999984, 2, 'EhGroups', 1009150,'深圳市沃达孚科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008278, 0, 'ENTERPRISE', '深圳市沃达孚科技有限公司', 0, '', '/1008278', 1, 2, 'ENTERPRISE', 999984, 1009150);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118085, 240111044331055835, 'organization', 1008278, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009151, UUID(), '深圳市瑟浮电子有限公司', '深圳市瑟浮电子有限公司', 1, 1, 1008279, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185974, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185974, UUID(), 999984, 2, 'EhGroups', 1009151,'深圳市瑟浮电子有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008279, 0, 'ENTERPRISE', '深圳市瑟浮电子有限公司', 0, '', '/1008279', 1, 2, 'ENTERPRISE', 999984, 1009151);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118086, 240111044331055835, 'organization', 1008279, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009152, UUID(), '深圳市泓腾生物科技有限公司', '深圳市泓腾生物科技有限公司', 1, 1, 1008280, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185975, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185975, UUID(), 999984, 2, 'EhGroups', 1009152,'深圳市泓腾生物科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008280, 0, 'ENTERPRISE', '深圳市泓腾生物科技有限公司', 0, '', '/1008280', 1, 2, 'ENTERPRISE', 999984, 1009152);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118087, 240111044331055835, 'organization', 1008280, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009153, UUID(), '深圳路迪网络有限公司', '深圳路迪网络有限公司', 1, 1, 1008281, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185976, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185976, UUID(), 999984, 2, 'EhGroups', 1009153,'深圳路迪网络有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008281, 0, 'ENTERPRISE', '深圳路迪网络有限公司', 0, '', '/1008281', 1, 2, 'ENTERPRISE', 999984, 1009153);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118088, 240111044331055835, 'organization', 1008281, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009154, UUID(), '八零年代', '八零年代', 1, 1, 1008282, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185977, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185977, UUID(), 999984, 2, 'EhGroups', 1009154,'八零年代','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008282, 0, 'ENTERPRISE', '八零年代', 0, '', '/1008282', 1, 2, 'ENTERPRISE', 999984, 1009154);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118089, 240111044331055835, 'organization', 1008282, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009155, UUID(), '深圳市神州龙脉信息工程有限公司', '深圳市神州龙脉信息工程有限公司', 1, 1, 1008283, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185978, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185978, UUID(), 999984, 2, 'EhGroups', 1009155,'深圳市神州龙脉信息工程有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008283, 0, 'ENTERPRISE', '深圳市神州龙脉信息工程有限公司', 0, '', '/1008283', 1, 2, 'ENTERPRISE', 999984, 1009155);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118090, 240111044331055835, 'organization', 1008283, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009156, UUID(), '深圳市金溢科技股份有限公司', '深圳市金溢科技股份有限公司', 1, 1, 1008284, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185979, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185979, UUID(), 999984, 2, 'EhGroups', 1009156,'深圳市金溢科技股份有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008284, 0, 'ENTERPRISE', '深圳市金溢科技股份有限公司', 0, '', '/1008284', 1, 2, 'ENTERPRISE', 999984, 1009156);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118091, 240111044331055835, 'organization', 1008284, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009157, UUID(), '深圳市瀚海电子技术有限公司', '深圳市瀚海电子技术有限公司', 1, 1, 1008285, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185980, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185980, UUID(), 999984, 2, 'EhGroups', 1009157,'深圳市瀚海电子技术有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008285, 0, 'ENTERPRISE', '深圳市瀚海电子技术有限公司', 0, '', '/1008285', 1, 2, 'ENTERPRISE', 999984, 1009157);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118092, 240111044331055835, 'organization', 1008285, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009158, UUID(), '深圳市励拓软件有限公司', '深圳市励拓软件有限公司', 1, 1, 1008286, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185981, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185981, UUID(), 999984, 2, 'EhGroups', 1009158,'深圳市励拓软件有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008286, 0, 'ENTERPRISE', '深圳市励拓软件有限公司', 0, '', '/1008286', 1, 2, 'ENTERPRISE', 999984, 1009158);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118093, 240111044331055835, 'organization', 1008286, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009159, UUID(), '深圳爱尔创口腔技术有限公司', '深圳爱尔创口腔技术有限公司', 1, 1, 1008287, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185982, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185982, UUID(), 999984, 2, 'EhGroups', 1009159,'深圳爱尔创口腔技术有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008287, 0, 'ENTERPRISE', '深圳爱尔创口腔技术有限公司', 0, '', '/1008287', 1, 2, 'ENTERPRISE', 999984, 1009159);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118094, 240111044331055835, 'organization', 1008287, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009160, UUID(), '深圳市中富港企业管理有限公司', '深圳市中富港企业管理有限公司', 1, 1, 1008288, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185983, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185983, UUID(), 999984, 2, 'EhGroups', 1009160,'深圳市中富港企业管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008288, 0, 'ENTERPRISE', '深圳市中富港企业管理有限公司', 0, '', '/1008288', 1, 2, 'ENTERPRISE', 999984, 1009160);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118095, 240111044331055835, 'organization', 1008288, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009161, UUID(), '深圳市斯维尔科技股份有限公司', '深圳市斯维尔科技股份有限公司', 1, 1, 1008289, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185984, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185984, UUID(), 999984, 2, 'EhGroups', 1009161,'深圳市斯维尔科技股份有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008289, 0, 'ENTERPRISE', '深圳市斯维尔科技股份有限公司', 0, '', '/1008289', 1, 2, 'ENTERPRISE', 999984, 1009161);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118096, 240111044331055835, 'organization', 1008289, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009162, UUID(), '深圳市矽旺半导体有限公司', '深圳市矽旺半导体有限公司', 1, 1, 1008290, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185985, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185985, UUID(), 999984, 2, 'EhGroups', 1009162,'深圳市矽旺半导体有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008290, 0, 'ENTERPRISE', '深圳市矽旺半导体有限公司', 0, '', '/1008290', 1, 2, 'ENTERPRISE', 999984, 1009162);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118097, 240111044331055835, 'organization', 1008290, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009163, UUID(), '深圳市方浩实业有限公司', '深圳市方浩实业有限公司', 1, 1, 1008291, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185986, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185986, UUID(), 999984, 2, 'EhGroups', 1009163,'深圳市方浩实业有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008291, 0, 'ENTERPRISE', '深圳市方浩实业有限公司', 0, '', '/1008291', 1, 2, 'ENTERPRISE', 999984, 1009163);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118098, 240111044331055835, 'organization', 1008291, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009164, UUID(), '深圳市凹凸通讯技术有限公司', '深圳市凹凸通讯技术有限公司', 1, 1, 1008292, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185987, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185987, UUID(), 999984, 2, 'EhGroups', 1009164,'深圳市凹凸通讯技术有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008292, 0, 'ENTERPRISE', '深圳市凹凸通讯技术有限公司', 0, '', '/1008292', 1, 2, 'ENTERPRISE', 999984, 1009164);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118099, 240111044331055835, 'organization', 1008292, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009165, UUID(), '广通电子科技有限公司', '广通电子科技有限公司', 1, 1, 1008293, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185988, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185988, UUID(), 999984, 2, 'EhGroups', 1009165,'广通电子科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008293, 0, 'ENTERPRISE', '广通电子科技有限公司', 0, '', '/1008293', 1, 2, 'ENTERPRISE', 999984, 1009165);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118100, 240111044331055835, 'organization', 1008293, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009166, UUID(), '深圳市铱云云计算有限公司', '深圳市铱云云计算有限公司', 1, 1, 1008294, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185989, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185989, UUID(), 999984, 2, 'EhGroups', 1009166,'深圳市铱云云计算有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008294, 0, 'ENTERPRISE', '深圳市铱云云计算有限公司', 0, '', '/1008294', 1, 2, 'ENTERPRISE', 999984, 1009166);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118101, 240111044331055835, 'organization', 1008294, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009167, UUID(), '博彦科技（深圳）有限公司', '博彦科技（深圳）有限公司', 1, 1, 1008295, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185990, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185990, UUID(), 999984, 2, 'EhGroups', 1009167,'博彦科技（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008295, 0, 'ENTERPRISE', '博彦科技（深圳）有限公司', 0, '', '/1008295', 1, 2, 'ENTERPRISE', 999984, 1009167);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118102, 240111044331055835, 'organization', 1008295, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009168, UUID(), '深圳博芯科技股份有限公司', '深圳博芯科技股份有限公司', 1, 1, 1008296, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185991, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185991, UUID(), 999984, 2, 'EhGroups', 1009168,'深圳博芯科技股份有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008296, 0, 'ENTERPRISE', '深圳博芯科技股份有限公司', 0, '', '/1008296', 1, 2, 'ENTERPRISE', 999984, 1009168);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118103, 240111044331055835, 'organization', 1008296, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009169, UUID(), '红豆投资有限公司', '红豆投资有限公司', 1, 1, 1008297, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185992, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185992, UUID(), 999984, 2, 'EhGroups', 1009169,'红豆投资有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008297, 0, 'ENTERPRISE', '红豆投资有限公司', 0, '', '/1008297', 1, 2, 'ENTERPRISE', 999984, 1009169);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118104, 240111044331055835, 'organization', 1008297, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009170, UUID(), '深圳楼兰辉煌科技有限公司', '深圳楼兰辉煌科技有限公司', 1, 1, 1008298, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185993, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185993, UUID(), 999984, 2, 'EhGroups', 1009170,'深圳楼兰辉煌科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008298, 0, 'ENTERPRISE', '深圳楼兰辉煌科技有限公司', 0, '', '/1008298', 1, 2, 'ENTERPRISE', 999984, 1009170);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118105, 240111044331055835, 'organization', 1008298, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009171, UUID(), '深圳市盛和达智能科技有限公司', '深圳市盛和达智能科技有限公司', 1, 1, 1008299, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185994, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185994, UUID(), 999984, 2, 'EhGroups', 1009171,'深圳市盛和达智能科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008299, 0, 'ENTERPRISE', '深圳市盛和达智能科技有限公司', 0, '', '/1008299', 1, 2, 'ENTERPRISE', 999984, 1009171);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118106, 240111044331055835, 'organization', 1008299, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009172, UUID(), '深圳市盈瑞技术有限公司', '深圳市盈瑞技术有限公司', 1, 1, 1008300, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185995, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185995, UUID(), 999984, 2, 'EhGroups', 1009172,'深圳市盈瑞技术有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008300, 0, 'ENTERPRISE', '深圳市盈瑞技术有限公司', 0, '', '/1008300', 1, 2, 'ENTERPRISE', 999984, 1009172);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118107, 240111044331055835, 'organization', 1008300, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009173, UUID(), '深圳指芯智能科技有限公司', '深圳指芯智能科技有限公司', 1, 1, 1008301, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185996, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185996, UUID(), 999984, 2, 'EhGroups', 1009173,'深圳指芯智能科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008301, 0, 'ENTERPRISE', '深圳指芯智能科技有限公司', 0, '', '/1008301', 1, 2, 'ENTERPRISE', 999984, 1009173);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118108, 240111044331055835, 'organization', 1008301, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009174, UUID(), '广州巨杉软件开发有限公司', '广州巨杉软件开发有限公司', 1, 1, 1008302, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185997, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185997, UUID(), 999984, 2, 'EhGroups', 1009174,'广州巨杉软件开发有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008302, 0, 'ENTERPRISE', '广州巨杉软件开发有限公司', 0, '', '/1008302', 1, 2, 'ENTERPRISE', 999984, 1009174);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118109, 240111044331055835, 'organization', 1008302, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009175, UUID(), '广东深宏盾律师事务所', '广东深宏盾律师事务所', 1, 1, 1008303, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185998, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185998, UUID(), 999984, 2, 'EhGroups', 1009175,'广东深宏盾律师事务所','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008303, 0, 'ENTERPRISE', '广东深宏盾律师事务所', 0, '', '/1008303', 1, 2, 'ENTERPRISE', 999984, 1009175);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118110, 240111044331055835, 'organization', 1008303, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009176, UUID(), '新译信息技术（深圳）有限公司', '新译信息技术（深圳）有限公司', 1, 1, 1008304, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 185999, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(185999, UUID(), 999984, 2, 'EhGroups', 1009176,'新译信息技术（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008304, 0, 'ENTERPRISE', '新译信息技术（深圳）有限公司', 0, '', '/1008304', 1, 2, 'ENTERPRISE', 999984, 1009176);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118111, 240111044331055835, 'organization', 1008304, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009177, UUID(), '深圳市易瑞来科技股份有限公司', '深圳市易瑞来科技股份有限公司', 1, 1, 1008305, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186000, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186000, UUID(), 999984, 2, 'EhGroups', 1009177,'深圳市易瑞来科技股份有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008305, 0, 'ENTERPRISE', '深圳市易瑞来科技股份有限公司', 0, '', '/1008305', 1, 2, 'ENTERPRISE', 999984, 1009177);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118112, 240111044331055835, 'organization', 1008305, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009178, UUID(), '深圳力合数字电视有限公司', '深圳力合数字电视有限公司', 1, 1, 1008306, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186001, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186001, UUID(), 999984, 2, 'EhGroups', 1009178,'深圳力合数字电视有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008306, 0, 'ENTERPRISE', '深圳力合数字电视有限公司', 0, '', '/1008306', 1, 2, 'ENTERPRISE', 999984, 1009178);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118113, 240111044331055835, 'organization', 1008306, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009179, UUID(), '深圳中琛源科技股份有限公司', '深圳中琛源科技股份有限公司', 1, 1, 1008307, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186002, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186002, UUID(), 999984, 2, 'EhGroups', 1009179,'深圳中琛源科技股份有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008307, 0, 'ENTERPRISE', '深圳中琛源科技股份有限公司', 0, '', '/1008307', 1, 2, 'ENTERPRISE', 999984, 1009179);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118114, 240111044331055835, 'organization', 1008307, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009180, UUID(), '深圳市瑞雷特电子技术有限公司', '深圳市瑞雷特电子技术有限公司', 1, 1, 1008308, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186003, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186003, UUID(), 999984, 2, 'EhGroups', 1009180,'深圳市瑞雷特电子技术有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008308, 0, 'ENTERPRISE', '深圳市瑞雷特电子技术有限公司', 0, '', '/1008308', 1, 2, 'ENTERPRISE', 999984, 1009180);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118115, 240111044331055835, 'organization', 1008308, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009181, UUID(), '深圳市智搜信息技术有限公司', '深圳市智搜信息技术有限公司', 1, 1, 1008309, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186004, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186004, UUID(), 999984, 2, 'EhGroups', 1009181,'深圳市智搜信息技术有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008309, 0, 'ENTERPRISE', '深圳市智搜信息技术有限公司', 0, '', '/1008309', 1, 2, 'ENTERPRISE', 999984, 1009181);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118116, 240111044331055835, 'organization', 1008309, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009182, UUID(), '深圳前海零距物联网科技有限公司', '深圳前海零距物联网科技有限公司', 1, 1, 1008310, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186005, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186005, UUID(), 999984, 2, 'EhGroups', 1009182,'深圳前海零距物联网科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008310, 0, 'ENTERPRISE', '深圳前海零距物联网科技有限公司', 0, '', '/1008310', 1, 2, 'ENTERPRISE', 999984, 1009182);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118117, 240111044331055835, 'organization', 1008310, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009183, UUID(), '深圳市沃夫德尔科技有限公司', '深圳市沃夫德尔科技有限公司', 1, 1, 1008311, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186006, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186006, UUID(), 999984, 2, 'EhGroups', 1009183,'深圳市沃夫德尔科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008311, 0, 'ENTERPRISE', '深圳市沃夫德尔科技有限公司', 0, '', '/1008311', 1, 2, 'ENTERPRISE', 999984, 1009183);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118118, 240111044331055835, 'organization', 1008311, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009184, UUID(), '深圳市海邻科信息技术有限公司', '深圳市海邻科信息技术有限公司', 1, 1, 1008312, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186007, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186007, UUID(), 999984, 2, 'EhGroups', 1009184,'深圳市海邻科信息技术有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008312, 0, 'ENTERPRISE', '深圳市海邻科信息技术有限公司', 0, '', '/1008312', 1, 2, 'ENTERPRISE', 999984, 1009184);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118119, 240111044331055835, 'organization', 1008312, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009185, UUID(), '深圳市威宇智通科技有限公司', '深圳市威宇智通科技有限公司', 1, 1, 1008313, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186008, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186008, UUID(), 999984, 2, 'EhGroups', 1009185,'深圳市威宇智通科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008313, 0, 'ENTERPRISE', '深圳市威宇智通科技有限公司', 0, '', '/1008313', 1, 2, 'ENTERPRISE', 999984, 1009185);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118120, 240111044331055835, 'organization', 1008313, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009186, UUID(), '生活帮（深圳）科技有限公司', '生活帮（深圳）科技有限公司', 1, 1, 1008314, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186009, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186009, UUID(), 999984, 2, 'EhGroups', 1009186,'生活帮（深圳）科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008314, 0, 'ENTERPRISE', '生活帮（深圳）科技有限公司', 0, '', '/1008314', 1, 2, 'ENTERPRISE', 999984, 1009186);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118121, 240111044331055835, 'organization', 1008314, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009187, UUID(), '东莞市车友互联信息科技有限公司', '东莞市车友互联信息科技有限公司', 1, 1, 1008315, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186010, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186010, UUID(), 999984, 2, 'EhGroups', 1009187,'东莞市车友互联信息科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008315, 0, 'ENTERPRISE', '东莞市车友互联信息科技有限公司', 0, '', '/1008315', 1, 2, 'ENTERPRISE', 999984, 1009187);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118122, 240111044331055835, 'organization', 1008315, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009188, UUID(), '深圳市神盾信息技术有限公司', '深圳市神盾信息技术有限公司', 1, 1, 1008316, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186011, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186011, UUID(), 999984, 2, 'EhGroups', 1009188,'深圳市神盾信息技术有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008316, 0, 'ENTERPRISE', '深圳市神盾信息技术有限公司', 0, '', '/1008316', 1, 2, 'ENTERPRISE', 999984, 1009188);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118123, 240111044331055835, 'organization', 1008316, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009189, UUID(), '深圳云安宝科技有限公司', '深圳云安宝科技有限公司', 1, 1, 1008317, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186012, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186012, UUID(), 999984, 2, 'EhGroups', 1009189,'深圳云安宝科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008317, 0, 'ENTERPRISE', '深圳云安宝科技有限公司', 0, '', '/1008317', 1, 2, 'ENTERPRISE', 999984, 1009189);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118124, 240111044331055835, 'organization', 1008317, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009190, UUID(), '深圳烯旺新材料科技股份有限公司', '深圳烯旺新材料科技股份有限公司', 1, 1, 1008318, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186013, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186013, UUID(), 999984, 2, 'EhGroups', 1009190,'深圳烯旺新材料科技股份有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008318, 0, 'ENTERPRISE', '深圳烯旺新材料科技股份有限公司', 0, '', '/1008318', 1, 2, 'ENTERPRISE', 999984, 1009190);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118125, 240111044331055835, 'organization', 1008318, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009191, UUID(), '深圳捷通华声科技有限公司', '深圳捷通华声科技有限公司', 1, 1, 1008319, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186014, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186014, UUID(), 999984, 2, 'EhGroups', 1009191,'深圳捷通华声科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008319, 0, 'ENTERPRISE', '深圳捷通华声科技有限公司', 0, '', '/1008319', 1, 2, 'ENTERPRISE', 999984, 1009191);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118126, 240111044331055835, 'organization', 1008319, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009192, UUID(), '深圳市汇丰软件服务有限公司', '深圳市汇丰软件服务有限公司', 1, 1, 1008320, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186015, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186015, UUID(), 999984, 2, 'EhGroups', 1009192,'深圳市汇丰软件服务有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008320, 0, 'ENTERPRISE', '深圳市汇丰软件服务有限公司', 0, '', '/1008320', 1, 2, 'ENTERPRISE', 999984, 1009192);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118127, 240111044331055835, 'organization', 1008320, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009193, UUID(), '深圳市盈正实业有限公司', '深圳市盈正实业有限公司', 1, 1, 1008321, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186016, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186016, UUID(), 999984, 2, 'EhGroups', 1009193,'深圳市盈正实业有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008321, 0, 'ENTERPRISE', '深圳市盈正实业有限公司', 0, '', '/1008321', 1, 2, 'ENTERPRISE', 999984, 1009193);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118128, 240111044331055835, 'organization', 1008321, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009194, UUID(), '深圳市中鼎天下实业有限公司', '深圳市中鼎天下实业有限公司', 1, 1, 1008322, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186017, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186017, UUID(), 999984, 2, 'EhGroups', 1009194,'深圳市中鼎天下实业有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008322, 0, 'ENTERPRISE', '深圳市中鼎天下实业有限公司', 0, '', '/1008322', 1, 2, 'ENTERPRISE', 999984, 1009194);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118129, 240111044331055835, 'organization', 1008322, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009195, UUID(), '深圳旭展达科技有限公司', '深圳旭展达科技有限公司', 1, 1, 1008323, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186018, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186018, UUID(), 999984, 2, 'EhGroups', 1009195,'深圳旭展达科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008323, 0, 'ENTERPRISE', '深圳旭展达科技有限公司', 0, '', '/1008323', 1, 2, 'ENTERPRISE', 999984, 1009195);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118130, 240111044331055835, 'organization', 1008323, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009196, UUID(), '深圳中融天下企业管理有限公司', '深圳中融天下企业管理有限公司', 1, 1, 1008324, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186019, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186019, UUID(), 999984, 2, 'EhGroups', 1009196,'深圳中融天下企业管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008324, 0, 'ENTERPRISE', '深圳中融天下企业管理有限公司', 0, '', '/1008324', 1, 2, 'ENTERPRISE', 999984, 1009196);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118131, 240111044331055835, 'organization', 1008324, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009197, UUID(), '深圳中晶海旺科技有限公司', '深圳中晶海旺科技有限公司', 1, 1, 1008325, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186020, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186020, UUID(), 999984, 2, 'EhGroups', 1009197,'深圳中晶海旺科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008325, 0, 'ENTERPRISE', '深圳中晶海旺科技有限公司', 0, '', '/1008325', 1, 2, 'ENTERPRISE', 999984, 1009197);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118132, 240111044331055835, 'organization', 1008325, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009198, UUID(), '深圳中鼎天下金融服务有限公司', '深圳中鼎天下金融服务有限公司', 1, 1, 1008326, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186021, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186021, UUID(), 999984, 2, 'EhGroups', 1009198,'深圳中鼎天下金融服务有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008326, 0, 'ENTERPRISE', '深圳中鼎天下金融服务有限公司', 0, '', '/1008326', 1, 2, 'ENTERPRISE', 999984, 1009198);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118133, 240111044331055835, 'organization', 1008326, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009199, UUID(), '深圳市苏成投资发展有限公司', '深圳市苏成投资发展有限公司', 1, 1, 1008327, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186022, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186022, UUID(), 999984, 2, 'EhGroups', 1009199,'深圳市苏成投资发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008327, 0, 'ENTERPRISE', '深圳市苏成投资发展有限公司', 0, '', '/1008327', 1, 2, 'ENTERPRISE', 999984, 1009199);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118134, 240111044331055835, 'organization', 1008327, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009200, UUID(), '深圳市成合泰科技有限公司', '深圳市成合泰科技有限公司', 1, 1, 1008328, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186023, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186023, UUID(), 999984, 2, 'EhGroups', 1009200,'深圳市成合泰科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008328, 0, 'ENTERPRISE', '深圳市成合泰科技有限公司', 0, '', '/1008328', 1, 2, 'ENTERPRISE', 999984, 1009200);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118135, 240111044331055835, 'organization', 1008328, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009201, UUID(), '深圳市银证通云网科技有限公司', '深圳市银证通云网科技有限公司', 1, 1, 1008329, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186024, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186024, UUID(), 999984, 2, 'EhGroups', 1009201,'深圳市银证通云网科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008329, 0, 'ENTERPRISE', '深圳市银证通云网科技有限公司', 0, '', '/1008329', 1, 2, 'ENTERPRISE', 999984, 1009201);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118136, 240111044331055835, 'organization', 1008329, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009202, UUID(), '深圳市娱动源力信息技术有限公司', '深圳市娱动源力信息技术有限公司', 1, 1, 1008330, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186025, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186025, UUID(), 999984, 2, 'EhGroups', 1009202,'深圳市娱动源力信息技术有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008330, 0, 'ENTERPRISE', '深圳市娱动源力信息技术有限公司', 0, '', '/1008330', 1, 2, 'ENTERPRISE', 999984, 1009202);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118137, 240111044331055835, 'organization', 1008330, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009203, UUID(), '深圳市鼎飞技术有限公司', '深圳市鼎飞技术有限公司', 1, 1, 1008331, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186026, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186026, UUID(), 999984, 2, 'EhGroups', 1009203,'深圳市鼎飞技术有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008331, 0, 'ENTERPRISE', '深圳市鼎飞技术有限公司', 0, '', '/1008331', 1, 2, 'ENTERPRISE', 999984, 1009203);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118138, 240111044331055835, 'organization', 1008331, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009204, UUID(), '深圳市艾奇泰科技有限公司', '深圳市艾奇泰科技有限公司', 1, 1, 1008332, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186027, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186027, UUID(), 999984, 2, 'EhGroups', 1009204,'深圳市艾奇泰科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008332, 0, 'ENTERPRISE', '深圳市艾奇泰科技有限公司', 0, '', '/1008332', 1, 2, 'ENTERPRISE', 999984, 1009204);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118139, 240111044331055835, 'organization', 1008332, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009205, UUID(), '深圳企大信息技术有限公司', '深圳企大信息技术有限公司', 1, 1, 1008333, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186028, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186028, UUID(), 999984, 2, 'EhGroups', 1009205,'深圳企大信息技术有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008333, 0, 'ENTERPRISE', '深圳企大信息技术有限公司', 0, '', '/1008333', 1, 2, 'ENTERPRISE', 999984, 1009205);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118140, 240111044331055835, 'organization', 1008333, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009206, UUID(), '深圳市荣道科技有限公司', '深圳市荣道科技有限公司', 1, 1, 1008334, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186029, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186029, UUID(), 999984, 2, 'EhGroups', 1009206,'深圳市荣道科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008334, 0, 'ENTERPRISE', '深圳市荣道科技有限公司', 0, '', '/1008334', 1, 2, 'ENTERPRISE', 999984, 1009206);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118141, 240111044331055835, 'organization', 1008334, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009207, UUID(), '北京云知声信息技术有限公司', '北京云知声信息技术有限公司', 1, 1, 1008335, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186030, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186030, UUID(), 999984, 2, 'EhGroups', 1009207,'北京云知声信息技术有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008335, 0, 'ENTERPRISE', '北京云知声信息技术有限公司', 0, '', '/1008335', 1, 2, 'ENTERPRISE', 999984, 1009207);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118142, 240111044331055835, 'organization', 1008335, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009208, UUID(), '无锡中太数据通信股份有限公司深圳分公司', '无锡中太数据通信股份有限公司深圳分公司', 1, 1, 1008336, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186031, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186031, UUID(), 999984, 2, 'EhGroups', 1009208,'无锡中太数据通信股份有限公司深圳分公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008336, 0, 'ENTERPRISE', '无锡中太数据通信股份有限公司深圳分公司', 0, '', '/1008336', 1, 2, 'ENTERPRISE', 999984, 1009208);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118143, 240111044331055835, 'organization', 1008336, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009209, UUID(), '深圳中汽零投资有限公司', '深圳中汽零投资有限公司', 1, 1, 1008337, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186032, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186032, UUID(), 999984, 2, 'EhGroups', 1009209,'深圳中汽零投资有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008337, 0, 'ENTERPRISE', '深圳中汽零投资有限公司', 0, '', '/1008337', 1, 2, 'ENTERPRISE', 999984, 1009209);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118144, 240111044331055835, 'organization', 1008337, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009210, UUID(), '深圳量旌科技有限公司', '深圳量旌科技有限公司', 1, 1, 1008338, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186033, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186033, UUID(), 999984, 2, 'EhGroups', 1009210,'深圳量旌科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008338, 0, 'ENTERPRISE', '深圳量旌科技有限公司', 0, '', '/1008338', 1, 2, 'ENTERPRISE', 999984, 1009210);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118145, 240111044331055835, 'organization', 1008338, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009211, UUID(), '深圳云创车联网有限公司', '深圳云创车联网有限公司', 1, 1, 1008339, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186034, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186034, UUID(), 999984, 2, 'EhGroups', 1009211,'深圳云创车联网有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008339, 0, 'ENTERPRISE', '深圳云创车联网有限公司', 0, '', '/1008339', 1, 2, 'ENTERPRISE', 999984, 1009211);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118146, 240111044331055835, 'organization', 1008339, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009212, UUID(), '深圳市大数据世纪科技有限公司', '深圳市大数据世纪科技有限公司', 1, 1, 1008340, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186035, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186035, UUID(), 999984, 2, 'EhGroups', 1009212,'深圳市大数据世纪科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008340, 0, 'ENTERPRISE', '深圳市大数据世纪科技有限公司', 0, '', '/1008340', 1, 2, 'ENTERPRISE', 999984, 1009212);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118147, 240111044331055835, 'organization', 1008340, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009213, UUID(), '东莞市内资经济促进中心', '东莞市内资经济促进中心', 1, 1, 1008341, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186036, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186036, UUID(), 999984, 2, 'EhGroups', 1009213,'东莞市内资经济促进中心','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008341, 0, 'ENTERPRISE', '东莞市内资经济促进中心', 0, '', '/1008341', 1, 2, 'ENTERPRISE', 999984, 1009213);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118148, 240111044331055835, 'organization', 1008341, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009214, UUID(), '深圳市指尖城市网络科技有限公司', '深圳市指尖城市网络科技有限公司', 1, 1, 1008342, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186037, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186037, UUID(), 999984, 2, 'EhGroups', 1009214,'深圳市指尖城市网络科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008342, 0, 'ENTERPRISE', '深圳市指尖城市网络科技有限公司', 0, '', '/1008342', 1, 2, 'ENTERPRISE', 999984, 1009214);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118149, 240111044331055835, 'organization', 1008342, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009215, UUID(), '杭州友声科技有限公司深圳分公司', '杭州友声科技有限公司深圳分公司', 1, 1, 1008343, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186038, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186038, UUID(), 999984, 2, 'EhGroups', 1009215,'杭州友声科技有限公司深圳分公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008343, 0, 'ENTERPRISE', '杭州友声科技有限公司深圳分公司', 0, '', '/1008343', 1, 2, 'ENTERPRISE', 999984, 1009215);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118150, 240111044331055835, 'organization', 1008343, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009216, UUID(), '善水融互联网金融服务（深圳）有限公司', '善水融互联网金融服务（深圳）有限公司', 1, 1, 1008344, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186039, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186039, UUID(), 999984, 2, 'EhGroups', 1009216,'善水融互联网金融服务（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008344, 0, 'ENTERPRISE', '善水融互联网金融服务（深圳）有限公司', 0, '', '/1008344', 1, 2, 'ENTERPRISE', 999984, 1009216);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118151, 240111044331055835, 'organization', 1008344, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009217, UUID(), '深圳前海捷联金融服务有限公司', '深圳前海捷联金融服务有限公司', 1, 1, 1008345, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186040, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186040, UUID(), 999984, 2, 'EhGroups', 1009217,'深圳前海捷联金融服务有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008345, 0, 'ENTERPRISE', '深圳前海捷联金融服务有限公司', 0, '', '/1008345', 1, 2, 'ENTERPRISE', 999984, 1009217);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118152, 240111044331055835, 'organization', 1008345, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009218, UUID(), '深圳市创高移动安防有限公司', '深圳市创高移动安防有限公司', 1, 1, 1008346, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186041, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186041, UUID(), 999984, 2, 'EhGroups', 1009218,'深圳市创高移动安防有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008346, 0, 'ENTERPRISE', '深圳市创高移动安防有限公司', 0, '', '/1008346', 1, 2, 'ENTERPRISE', 999984, 1009218);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118153, 240111044331055835, 'organization', 1008346, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009219, UUID(), '深圳市脑立方科技有限公司', '深圳市脑立方科技有限公司', 1, 1, 1008347, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186042, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186042, UUID(), 999984, 2, 'EhGroups', 1009219,'深圳市脑立方科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008347, 0, 'ENTERPRISE', '深圳市脑立方科技有限公司', 0, '', '/1008347', 1, 2, 'ENTERPRISE', 999984, 1009219);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118154, 240111044331055835, 'organization', 1008347, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009220, UUID(), '深圳市欣易辰信息科技有限公司', '深圳市欣易辰信息科技有限公司', 1, 1, 1008348, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186043, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186043, UUID(), 999984, 2, 'EhGroups', 1009220,'深圳市欣易辰信息科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008348, 0, 'ENTERPRISE', '深圳市欣易辰信息科技有限公司', 0, '', '/1008348', 1, 2, 'ENTERPRISE', 999984, 1009220);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118155, 240111044331055835, 'organization', 1008348, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009221, UUID(), '北京金马成工程咨询有限公司深圳分公司', '北京金马成工程咨询有限公司深圳分公司', 1, 1, 1008349, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186044, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186044, UUID(), 999984, 2, 'EhGroups', 1009221,'北京金马成工程咨询有限公司深圳分公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008349, 0, 'ENTERPRISE', '北京金马成工程咨询有限公司深圳分公司', 0, '', '/1008349', 1, 2, 'ENTERPRISE', 999984, 1009221);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118156, 240111044331055835, 'organization', 1008349, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009222, UUID(), '深圳市云中鹤科技股份有限公司', '深圳市云中鹤科技股份有限公司', 1, 1, 1008350, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186045, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186045, UUID(), 999984, 2, 'EhGroups', 1009222,'深圳市云中鹤科技股份有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008350, 0, 'ENTERPRISE', '深圳市云中鹤科技股份有限公司', 0, '', '/1008350', 1, 2, 'ENTERPRISE', 999984, 1009222);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118157, 240111044331055835, 'organization', 1008350, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009223, UUID(), '深圳市卓智达科技有限公司', '深圳市卓智达科技有限公司', 1, 1, 1008351, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186046, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186046, UUID(), 999984, 2, 'EhGroups', 1009223,'深圳市卓智达科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008351, 0, 'ENTERPRISE', '深圳市卓智达科技有限公司', 0, '', '/1008351', 1, 2, 'ENTERPRISE', 999984, 1009223);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118158, 240111044331055835, 'organization', 1008351, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009224, UUID(), '深圳饭范空间食创互联网有限公司', '深圳饭范空间食创互联网有限公司', 1, 1, 1008352, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186047, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186047, UUID(), 999984, 2, 'EhGroups', 1009224,'深圳饭范空间食创互联网有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008352, 0, 'ENTERPRISE', '深圳饭范空间食创互联网有限公司', 0, '', '/1008352', 1, 2, 'ENTERPRISE', 999984, 1009224);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118159, 240111044331055835, 'organization', 1008352, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009225, UUID(), '深圳市微惠宝智能技术有限公司', '深圳市微惠宝智能技术有限公司', 1, 1, 1008353, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186048, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186048, UUID(), 999984, 2, 'EhGroups', 1009225,'深圳市微惠宝智能技术有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008353, 0, 'ENTERPRISE', '深圳市微惠宝智能技术有限公司', 0, '', '/1008353', 1, 2, 'ENTERPRISE', 999984, 1009225);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118160, 240111044331055835, 'organization', 1008353, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1009226, UUID(), '深圳国控医疗有限公司', '深圳国控医疗有限公司', 1, 1, 1008354, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186049, 1, 999984);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(186049, UUID(), 999984, 2, 'EhGroups', 1009226,'深圳国控医疗有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1008354, 0, 'ENTERPRISE', '深圳国控医疗有限公司', 0, '', '/1008354', 1, 2, 'ENTERPRISE', 999984, 1009226);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1118161, 240111044331055835, 'organization', 1008354, 3, 0, UTC_TIMESTAMP());

-- 菜单
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10000,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),11000,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),11100,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),11200,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),12000,'', 'EhNamespaces', 999984,2);


INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20000,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),24000,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),28000,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),25000,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),26000,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),27000,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),29000,'', 'EhNamespaces', 999984,2);


INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),30000,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),30500,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),31000,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),32000,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),33000,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),34000,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),35000,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),36000,'', 'EhNamespaces', 999984,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40000,'', 'EhNamespaces', 999984,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41000,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41100,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41300,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41400,'', 'EhNamespaces', 999984,2);


INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43000,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43100,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43200,'', 'EhNamespaces', 999984,2);


INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43400,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43410,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43420,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43430,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43440,'', 'EhNamespaces', 999984,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),44000,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),44100,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),44200,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),44300,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),44400,'', 'EhNamespaces', 999984,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),46000,'', 'EhNamespaces', 999984,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50000,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),51000,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),51100,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),52000,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),52100,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),52200,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),52300,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),52400,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),53000,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),53100,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56000,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56100,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56110,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56111,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56112,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56113,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56114,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56115,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56120,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56121,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56122,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56130,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56131,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56132,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56140,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56141,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56150,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56151,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56152,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56160,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56161,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56170,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56171,'', 'EhNamespaces', 999984,2);
-- 产品宋嘉扬要求去掉视频会议模块 by lqs 20161121
-- INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56200,'', 'EhNamespaces', 999984,2);
-- INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56210,'', 'EhNamespaces', 999984,2);
-- INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56220,'', 'EhNamespaces', 999984,2);
-- INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56230,'', 'EhNamespaces', 999984,2);


-- 小店
-- INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
--	VALUES ('600', 'business.url', 'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdefault%3Fpos%3D1%26_k%3Dzlbiz#sign_suffix', 'business url', '999984', NULL);

-- INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
--	VALUES ('805', 'video.official.support', '2', 'offical video support', '999984', NULL);

INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`)
    VALUES (12105, 999984, 0, '/home', 'Default', '0', '0', 'innospring', 'innospring', 'cs://1/image/aW1hZ2UvTVRwaE1tTmxNV05sT1RFeFpEWmtZbUl3TkdZMVlXTmpaRFprT0dZd01qWXpZZw', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'park_tourist');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`)
    VALUES (12106, 999984, 0, '/home', 'Default', '0', '0', 'innospring', 'innospring', 'cs://1/image/aW1hZ2UvTVRwa1pEYzBOVEJtT1RFNVpqYzJPVGswTm1NMk4ySmxOelJtWlRaaU9EZ3pZdw', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'pm_admin');

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES('10100','会议室预约','0',NULL,'0','999984');

SET @search_type_id = (SELECT MAX(id) FROM `eh_search_types`);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`)
	VALUES ((@search_type_id := @search_type_id + 1), '999984', '', '0', '活动', 'activity', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`)
	VALUES ((@search_type_id := @search_type_id + 1), '999984', '', '0', '快讯', 'news', '1', NULL, NULL);


INSERT INTO `eh_news_categories` (`id`,  `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`)
    VALUES('65','0','新闻',NULL,NULL,'0','1',UTC_TIMESTAMP(),'0',NULL,'999984', 'cs://1/image/aW1hZ2UvTVRvNU1qRmtOamxtT0dJMU5qRmpNVGhqT1RZM1lUSTBOelZrTVRFeE9UVXlPQQ');


INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203136, 2, 0, '运动与音乐', '兴趣/运动与音乐', 10, 2, UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203137, 2, 0, '旅游摄影', '兴趣/旅游摄影', 20, 2, UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203138, 2, 0, '亲子与教育', '兴趣/亲子与教育', 30, 2, UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203139, 2, 0, '美食与厨艺', '兴趣/美食与厨艺', 40, 2, UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203140, 2, 0, '家庭装饰', '兴趣/家庭装饰', 50, 2, UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203141, 2, 0, '美容化妆', '兴趣/美容化妆', 60, 2, UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203142, 2, 0, '宠物会', '兴趣/宠物会', 70, 2, UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203143, 2, 0, '名牌汇', '兴趣/名牌汇', 80, 2, UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203144, 2, 0, '同事群', '兴趣/同事群', 90, 2, UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203145, 2, 0, '老乡群', '兴趣/老乡群', 100, 2, UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203146, 2, 0, '同学群', '兴趣/同学群', 110, 2, UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203147, 2, 0, '拼车', '兴趣/拼车', 120, 2, UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203148, 2, 0, '其他', '兴趣/其他', 200, 2, UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203149, 2001, 0, '亲子', '兴趣/亲子与教育/亲子', 0, 2, UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203150, 2001, 0, '教育', '兴趣/亲子与教育/教育', 0, 2, UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203151, 2002, 0, '运动', '兴趣/运动与音乐/运动', 0, 2, UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203152, 2002, 0, '音乐', '兴趣/运动与音乐/音乐', 0, 2, UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203153, 2008, 0, '旅游', '兴趣/旅游摄影/旅游', 0, 2, UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203154, 2008, 0, '摄影', '兴趣/旅游摄影/摄影', 0, 2, UTC_TIMESTAMP(), 999984);

SET @eh_categories = (SELECT max(id) FROM `eh_categories`);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`)
	VALUES ((@eh_categories := @eh_categories + 1), '4', '0', '科技园大讲堂', '活动/科技园大讲堂', '0', '2', UTC_TIMESTAMP(), NULL, NULL, NULL, '999984');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`)
	VALUES ((@eh_categories := @eh_categories + 1), '4', '0', '项目对接会', '活动/项目对接会', '0', '2', UTC_TIMESTAMP(), NULL, NULL, NULL, '999984');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`)
	VALUES ((@eh_categories := @eh_categories + 1), '4', '0', '总裁沙龙', '活动/总裁沙龙', '0', '2', UTC_TIMESTAMP(), NULL, NULL, NULL, '999984');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`)
	VALUES ((@eh_categories := @eh_categories + 1), '4', '0', '创客', '活动/创客', '0', '2', UTC_TIMESTAMP(), NULL, NULL, NULL, '999984');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`)
	VALUES ((@eh_categories := @eh_categories + 1), '4', '0', '联谊', '活动/联谊', '0', '2', UTC_TIMESTAMP(), NULL, NULL, NULL, '999984');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`)
	VALUES ((@eh_categories := @eh_categories + 1), '4', '0', '其他', '活动/其他', '1', '2', UTC_TIMESTAMP(), NULL, NULL, NULL, '999984');




-- 服务广场数据
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) VALUES ('428', '999984', 'ServiceMarketLayout', '{\"versionCode\":\"2016122701\",\"versionName\":\"3.11.0\",\"layoutName\":\"ServiceMarketLayout\",\"displayName\":\"服务市场\",\"groups\":[{\"groupName\":\"\",\"widget\":\"Banners\",\"instanceConfig\":{\"itemGroup\":\"Default\"},\"style\":\"Default\",\"defaultOrder\":1,\"separatorFlag\":0,\"separatorHeight\":0},{\"groupName\":\"商家服务\",\"widget\":\"Navigator\",\"instanceConfig\":{\"itemGroup\":\"Bizs\"},\"style\":\"Default\",\"defaultOrder\":5,\"separatorFlag\":1,\"separatorHeight\":21,\"title\":\"服务\",\"iconUrl\":\"http://zijing-cs.lihekefu.com/image/aW1hZ2UvTVRvME9ESmxPR1pqT0dFM1pHSmpaRFkwTWpRNE16TmtaVEUwTWpRMFl6UTNaUQ?token=-udQSeJUGbMS0hSbPFl8rQvC_FB5qQkPgb8zMmu8CcCs_udZWepmGMDY7SgjhNvBmt9M5AX9Y-IX7hHEdaExVsMEopuw6rk15anzus6vy9A\",\"align\":\"0\",\"columnCount\":5},{\"groupName\":\"\",\"widget\":\"Bulletins\",\"instanceConfig\":{\"itemGroup\":\"Default\"},\"style\":\"Default\",\"defaultOrder\":1,\"separatorFlag\":1,\"separatorHeight\":21},{\"groupName\":\"\",\"widget\":\"NewsFlash\",\"instanceConfig\":{\"timeWidgetStyle\":\"datetime\",\"categoryId\":65,\"itemGroup\":\"Default\",\"newsSize\":5},\"style\":\"Default\",\"defaultOrder\":1,\"separatorFlag\":0,\"separatorHeight\":0}]}', '2016122701', '0', '2', '2016-10-28 17:02:30', 'pm_admin', '0', '0', '0');
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) VALUES ('429', '999984', 'ServiceMarketLayout', '{\"versionCode\":\"2016122701\",\"versionName\":\"3.11.0\",\"layoutName\":\"ServiceMarketLayout\",\"displayName\":\"服务市场\",\"groups\":[{\"groupName\":\"\",\"widget\":\"Banners\",\"instanceConfig\":{\"itemGroup\":\"Default\"},\"style\":\"Default\",\"defaultOrder\":1,\"separatorFlag\":0,\"separatorHeight\":0},{\"groupName\":\"商家服务\",\"widget\":\"Navigator\",\"instanceConfig\":{\"itemGroup\":\"Bizs\"},\"style\":\"Default\",\"defaultOrder\":5,\"separatorFlag\":1,\"separatorHeight\":21,\"title\":\"服务\",\"iconUrl\":\"http://zijing-cs.lihekefu.com/image/aW1hZ2UvTVRvME9ESmxPR1pqT0dFM1pHSmpaRFkwTWpRNE16TmtaVEUwTWpRMFl6UTNaUQ?token=-udQSeJUGbMS0hSbPFl8rQvC_FB5qQkPgb8zMmu8CcCs_udZWepmGMDY7SgjhNvBmt9M5AX9Y-IX7hHEdaExVsMEopuw6rk15anzus6vy9A\",\"align\":\"0\",\"columnCount\":5},{\"groupName\":\"\",\"widget\":\"Bulletins\",\"instanceConfig\":{\"itemGroup\":\"Default\"},\"style\":\"Default\",\"defaultOrder\":1,\"separatorFlag\":1,\"separatorHeight\":21},{\"groupName\":\"\",\"widget\":\"NewsFlash\",\"instanceConfig\":{\"timeWidgetStyle\":\"datetime\",\"categoryId\":65,\"itemGroup\":\"Default\",\"newsSize\":5},\"style\":\"Default\",\"defaultOrder\":1,\"separatorFlag\":0,\"separatorHeight\":0}]}', '2016122701', '0', '2', '2016-10-28 17:02:30', 'park_tourist', '0', '0', '0');

INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`)
	VALUES ('205200', '6', '0', '物业报修', '任务/物业报修', '0', '0', UTC_TIMESTAMP(), NULL, NULL, NULL, '999984');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`)
	VALUES ('205201', '6', '0', '投诉建议', '任务/投诉建议', '0', '2', UTC_TIMESTAMP(), NULL, NULL, NULL, '999984');

INSERT INTO `eh_item_service_categries`(`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`)
	VALUES(3, '科创服务', 'cs://1/image/aW1hZ2UvTVRvME9ESmxPR1pqT0dFM1pHSmpaRFkwTWpRNE16TmtaVEUwTWpRMFl6UTNaUQ', 1, 0, 1, 999984);
INSERT INTO `eh_item_service_categries`(`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`)
	VALUES(4, '专业服务', 'cs://1/image/aW1hZ2UvTVRvME9ESmxPR1pqT0dFM1pHSmpaRFkwTWpRNE16TmtaVEUwTWpRMFl6UTNaUQ', 1, 0, 1, 999984);
INSERT INTO `eh_item_service_categries`(`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`)
	VALUES(5, '园区服务', 'cs://1/image/aW1hZ2UvTVRvME9ESmxPR1pqT0dFM1pHSmpaRFkwTWpRNE16TmtaVEUwTWpRMFl6UTNaUQ', 1, 0, 1, 999984);
INSERT INTO `eh_item_service_categries`(`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`)
	VALUES(6, '生活服务', 'cs://1/image/aW1hZ2UvTVRvME9ESmxPR1pqT0dFM1pHSmpaRFkwTWpRNE16TmtaVEUwTWpRMFl6UTNaUQ', 1, 0, 1, 999984);
INSERT INTO `eh_item_service_categries`(`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`)
	VALUES(7, '企业服务', 'cs://1/image/aW1hZ2UvTVRvME9ESmxPR1pqT0dFM1pHSmpaRFkwTWpRNE16TmtaVEUwTWpRMFl6UTNaUQ', 1, 0, 1, 999984);

SET @eh_launch_pad_items = (SELECT max(id) FROM `eh_launch_pad_items`);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', 'MEETING', '会议室', 'cs://1/image/aW1hZ2UvTVRvNE16UTFaakZtWlRBeE5ETmhZVEkxT0ROa1pEVXdNall5WkRObU0yUmlOQQ', '1', '1', '49', '{\"resourceTypeId\":10100,\"pageType\":0}', '0', '0', '1', '0', '', '1', NULL, NULL, NULL, '1', 'pm_admin', '0', 5);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', 'MEETING', '会议室', 'cs://1/image/aW1hZ2UvTVRvNE16UTFaakZtWlRBeE5ETmhZVEkxT0ROa1pEVXdNall5WkRObU0yUmlOQQ', '1', '1', '49', '{\"resourceTypeId\":10100,\"pageType\":0}', '0', '0', '1', '0', '', '1', NULL, NULL, NULL, '1', 'park_tourist', '0', 5);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '园区入驻', '园区入驻', 'cs://1/image/aW1hZ2UvTVRvd1pqaGxOVEl4WldZMlpUbGpNRGs1TURsallUQTFNR0kzWVdZME5UTmxPUQ', '1', '1', '28', '', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '5');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '园区入驻', '园区入驻', 'cs://1/image/aW1hZ2UvTVRvd1pqaGxOVEl4WldZMlpUbGpNRGs1TURsallUQTFNR0kzWVdZME5UTmxPUQ', '1', '1', '28', '', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '5');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '资金扶持', '资金扶持', 'cs://1/image/aW1hZ2UvTVRwbE1ESTNZVFZtTTJNMU9XVXpPREExWmpjMU4yUTFZemRrT1RjNVpXVXdZZw', '1', '1', '33', '{"type":200985,"parentId":200985,"displayType": "list"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '5');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '资金扶持', '资金扶持', 'cs://1/image/aW1hZ2UvTVRwbE1ESTNZVFZtTTJNMU9XVXpPREExWmpjMU4yUTFZemRrT1RjNVpXVXdZZw', '1', '1', '33', '{"type":200985,"parentId":200985,"displayType": "list"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '5');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', 'PARKING_RECHARGE', '停车充值', 'cs://1/image/aW1hZ2UvTVRwaFlqUTBNbVprWTJNd01HUmtZek0yTnpSa01qVm1NR1ppTlRRNE5qQmtNUQ', '1', '1', '14', '{\"url\":\"http://zuolin.com/mobile/static/coming_soon/index.html\"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '5');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', 'PARKING_RECHARGE', '停车充值', 'cs://1/image/aW1hZ2UvTVRwaFlqUTBNbVprWTJNd01HUmtZek0yTnpSa01qVm1NR1ppTlRRNE5qQmtNUQ', '1', '1', '14', '{\"url\":\"http://zuolin.com/mobile/static/coming_soon/index.html\"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '5');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '车辆放行', '车辆放行', 'cs://1/image/aW1hZ2UvTVRwaFlqUTBNbVprWTJNd01HUmtZek0yTnpSa01qVm1NR1ppTlRRNE5qQmtNUQ', '1', '1', '14', '{\"url\":\"http://zuolin.com/mobile/static/coming_soon/index.html\"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '5');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', 'PM_TASK', '投诉建议', 'cs://1/image/aW1hZ2UvTVRwa05XWmhOell4WVdFMVpUUTRaamRoWVRWa1pqazBZMlUwWm1VMll6RmtaQQ', '1', '1', '51', '', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '5');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '投诉建议', '投诉建议', 'cs://1/image/aW1hZ2UvTVRwa05XWmhOell4WVdFMVpUUTRaamRoWVRWa1pqazBZMlUwWm1VMll6RmtaQQ', '1', '1', '14', '{\"url\":\"http://beta.zuolin.com/property_service/index.html?taskCategoryId=205201&hideNavigationBar=1#/my_service#sign_suffix\"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '5');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '服务维修', '服务维修', 'cs://1/image/aW1hZ2UvTVRvM01UUXlNRGhrTVdFeU5UWXdZMlZrTWpBNE1EUXlZVGt5TkRZMU4yWXdNdw', '1', '1', '', '', '0', '0', '1', '0', '', '0', NULL, 'biz', '137', '1', 'pm_admin', '0', '5');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '服务维修', '服务维修', 'cs://1/image/aW1hZ2UvTVRvM01UUXlNRGhrTVdFeU5UWXdZMlZrTWpBNE1EUXlZVGt5TkRZMU4yWXdNdw', '1', '1', '', '', '0', '0', '1', '0', '', '0', NULL, 'biz', '137', '1', 'park_tourist', '0', '5');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '物业报修', '物业报修', 'cs://1/image/aW1hZ2UvTVRwa05XWmhOell4WVdFMVpUUTRaamRoWVRWa1pqazBZMlUwWm1VMll6RmtaQQ', '1', '1', '14', '{\"url\":\"http://beta.zuolin.com/property_service/index.html?taskCategoryId=205200&hideNavigationBar=1#/my_service#sign_suffix\"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '5');


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '绿植租摆', '绿植租摆', 'cs://1/image/aW1hZ2UvTVRwall6ZGxZVFUxWVRNNE1HWTBabUkzWlRrMk9UZGlOek13WldWbFl6ZGhZUQ', '1', '1', '14', '', '0', '0', '1', '0', '', '0', NULL, 'biz', '136', '1', 'pm_admin', '0', '5');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '绿植租摆', '绿植租摆', 'cs://1/image/aW1hZ2UvTVRwall6ZGxZVFUxWVRNNE1HWTBabUkzWlRrMk9UZGlOek13WldWbFl6ZGhZUQ', '1', '1', '14', '', '0', '0', '1', '0', '', '0', NULL, 'biz', '136', '1', 'park_tourist', '0', '5');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '北环门禁', '北环门禁', 'cs://1/image/aW1hZ2UvTVRvMk5UTmpOalprWldSak1EZzVaVEkzTmpCbE1URXdNRFUwT1RCaVlXSTROUQ', '1', '1', '40', '{\"isSupportQR\":1,\"isSupportSmart\":1}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '5');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '北环门禁', '北环门禁', 'cs://1/image/aW1hZ2UvTVRvMk5UTmpOalprWldSak1EZzVaVEkzTmpCbE1URXdNRFUwT1RCaVlXSTROUQ', '1', '1', '40', '{\"isSupportQR\":1,\"isSupportSmart\":1}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '5');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '水木之家', '水木之家', 'cs://1/image/aW1hZ2UvTVRwak1UVmxOR0l6WldVM016ZzBZVEEyWkRnek0yUmhZekUzTURWaVlqazBZZw', '1', '1', '', '', '0', '0', '1', '0', '', '0', NULL, 'biz', '134', '1', 'pm_admin', '0', '6');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '水木之家', '水木之家', 'cs://1/image/aW1hZ2UvTVRwak1UVmxOR0l6WldVM016ZzBZVEEyWkRnek0yUmhZekUzTURWaVlqazBZZw', '1', '1', '', '', '0', '0', '1', '0', '', '0', NULL, 'biz', '134', '1', 'park_tourist', '0', '6');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', 'CONTACTS', '通讯录', 'cs://1/image/aW1hZ2UvTVRwbU1ERm1ObUV6TW1KbVlUZzRZMkUwWkRsa1lUSmtaRFkyWVRjek1XTTFPUQ', '1', '1', '46', '', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '7');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', 'CONTACTS', '通讯录', 'cs://1/image/aW1hZ2UvTVRwbU1ERm1ObUV6TW1KbVlUZzRZMkUwWkRsa1lUSmtaRFkyWVRjek1XTTFPUQ', '1', '1', '46', '', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '7');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', 'PUNCH', '打卡考勤', 'cs://1/image/aW1hZ2UvTVRvNVpXUTJOalUzT1dRNU16SmxOMkl3TVRSa09XVXpOV1EzT1RVeE16WXlPQQ', '1', '1', '23', '', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '7');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', 'PUNCH', '打卡考勤', 'cs://1/image/aW1hZ2UvTVRvNVpXUTJOalUzT1dRNU16SmxOMkl3TVRSa09XVXpOV1EzT1RVeE16WXlPQQ', '1', '1', '23', '', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '7');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '更多', '更多', 'cs://1/image/aW1hZ2UvTVRvMk1HUTVNamt3TldKak1tSTRNemxoTjJZMVl6azBOVEl5WmpRMU1tUXlZZw', '1', '1', '53', '{\"itemLocation\":\"/home\", \"itemGroup\":\"Bizs\"}', '10000', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin', '0', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '更多', '更多', 'cs://1/image/aW1hZ2UvTVRvMk1HUTVNamt3TldKak1tSTRNemxoTjJZMVl6azBOVEl5WmpRMU1tUXlZZw', '1', '1', '53', '{\"itemLocation\":\"/home\", \"itemGroup\":\"Bizs\"}', '10000', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist', '0', NULL);

	

INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ('200977', 'community', '240111044331055835', '0', '我要投资', '我要投资', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999984', '');
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ('200978', 'community', '240111044331055835', '0', '我要融资', '我要融资', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999984', '');
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ('200979', 'community', '240111044331055835', '0', '我有项目', '我有项目', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999984', '');
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ('200980', 'community', '240111044331055835', '0', '星空孵化', '星空孵化', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999984', '');

INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ('200981', 'community', '240111044331055835', '0', '研发平台', '研发平台', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999984', '');
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ('200982', 'community', '240111044331055835', '0', '载物咨询', '载物咨询', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999984', '');
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ('200983', 'community', '240111044331055835', '0', '国际合作', '国际合作', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999984', '');
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ('200984', 'community', '240111044331055835', '0', '科技成果', '科技成果', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999984', '');
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ('200985', 'community', '240111044331055835', '0', '资金扶持', '资金扶持', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999984', '');
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ('200986', 'community', '240111044331055835', '0', '知识产权', '知识产权', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999984', '');
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ('200987', 'community', '240111044331055835', '0', '财税咨询', '财税咨询', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999984', '');
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ('200988', 'community', '240111044331055835', '0', '法律服务', '法律服务', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999984', '');
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ('200989', 'community', '240111044331055835', '0', '人力资源', '人力资源', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999984', '');
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ('200990', 'community', '240111044331055835', '0', '科技金融', '科技金融', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999984', '');
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ('200991', 'community', '240111044331055835', '0', '云服务', '云服务', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999984', '');
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ('200992', 'community', '240111044331055835', '0', '供应链', '供应链', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999984', '');
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ('200993', 'community', '240111044331055835', '0', '产品测试', '产品测试', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999984', '');
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ('200994', 'community', '240111044331055835', '0', '福利管家', '福利管家', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999984', '');

SET @sa_id = (SELECT max(id) FROM `eh_service_alliances`);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331055835', '我要投资', '我要投资', '200977', '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331055835', '我要融资', '我要融资', '200978', '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331055835', '我有项目', '我有项目', '200979', '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331055835', '星空孵化', '星空孵化', '200980', '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331055835', '研发平台', '研发平台', '200981', '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331055835', '载物咨询', '载物咨询', '200982', '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331055835', '国际合作', '国际合作', '200983', '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331055835', '科技成果', '科技成果', '200984', '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331055835', '资金扶持', '资金扶持', '200985', '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331055835', '知识产权', '知识产权', '200986', '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331055835', '财税咨询', '财税咨询', '200987', '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331055835', '法律服务', '法律服务', '200988', '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331055835', '人力资源', '人力资源', '200989', '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331055835', '科技金融', '科技金融', '200990', '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331055835', '云服务', '云服务', '200991', '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331055835', '供应链', '供应链', '200992', '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331055835', '产品测试', '产品测试', '200993', '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331055835', '福利管家', '福利管家', '200994', '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '我要投资', '我要投资', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200977,"parentId":200977,"displayType": "list"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '我要融资', '我要融资', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200978,"parentId":200978,"displayType": "list"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '我有项目', '我有项目', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200979,"parentId":200979,"displayType": "list"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '星空孵化', '星空孵化', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200980,"parentId":200980,"displayType": "list"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '3');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '研发平台', '研发平台', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200981,"parentId":200981,"displayType": "list"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '载物咨询', '载物咨询', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200982,"parentId":200982,"displayType": "list"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '国际合作', '国际合作', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200983,"parentId":200983,"displayType": "list"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '科技成果', '科技成果', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200984,"parentId":200984,"displayType": "list"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '紫荆汇', '紫荆汇', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', '', '', '0', '0', '1', '1', '', '0', NULL, 'biz', '135', '1', 'pm_admin', '0', '3');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '知识产权', '知识产权', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200986,"parentId":200986,"displayType": "list"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '4');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '财税咨询', '财税咨询', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200987,"parentId":200987,"displayType": "list"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '4');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '法律服务', '法律服务', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200988,"parentId":200988,"displayType": "list"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '4');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '人力资源', '人力资源', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200989,"parentId":200989,"displayType": "list"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '4');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '科技金融', '科技金融', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200990,"parentId":200990,"displayType": "list"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '4');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '云服务', '云服务', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200991,"parentId":200991,"displayType": "list"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '4');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '供应链', '供应链', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200992,"parentId":200992,"displayType": "list"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '4');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '产品测试', '产品测试', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200993,"parentId":200993,"displayType": "list"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '4');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '福利管家', '福利管家', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200994,"parentId":200994,"displayType": "list"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '4');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '我要投资', '我要投资', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200977,"parentId":200977,"displayType": "list"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '我要融资', '我要融资', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200978,"parentId":200978,"displayType": "list"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '我有项目', '我有项目', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200979,"parentId":200979,"displayType": "list"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '星空孵化', '星空孵化', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200980,"parentId":200980,"displayType": "list"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '3');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '研发平台', '研发平台', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200981,"parentId":200981,"displayType": "list"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '载物咨询', '载物咨询', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200982,"parentId":200982,"displayType": "list"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '国际合作', '国际合作', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200983,"parentId":200983,"displayType": "list"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '科技成果', '科技成果', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200984,"parentId":200984,"displayType": "list"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '紫荆汇', '紫荆汇', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', '', '', '0', '0', '1', '1', '', '0', NULL, 'biz', '135', '1', 'park_tourist', '0', '3');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '知识产权', '知识产权', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200986,"parentId":200986,"displayType": "list"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '4');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '财税咨询', '财税咨询', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200987,"parentId":200987,"displayType": "list"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '4');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '法律服务', '法律服务', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200988,"parentId":200988,"displayType": "list"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '4');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '人力资源', '人力资源', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200989,"parentId":200989,"displayType": "list"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '4');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '科技金融', '科技金融', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200990,"parentId":200990,"displayType": "list"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '4');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '云服务', '云服务', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200991,"parentId":200991,"displayType": "list"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '4');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '供应链', '供应链', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200992,"parentId":200992,"displayType": "list"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '4');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '产品测试', '产品测试', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200993,"parentId":200993,"displayType": "list"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '4');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '福利管家', '福利管家', 'cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw', '1', '1', 33, '{"type":200994,"parentId":200994,"displayType": "list"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '4');



SET @eh_service_alliance_skip_rule = (SELECT max(id) FROM `eh_service_alliance_skip_rule`);
INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) VALUES ((@eh_service_alliance_skip_rule := @eh_service_alliance_skip_rule + 1), '999984', '200977');
INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) VALUES ((@eh_service_alliance_skip_rule := @eh_service_alliance_skip_rule + 1), '999984', '200978');
INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) VALUES ((@eh_service_alliance_skip_rule := @eh_service_alliance_skip_rule + 1), '999984', '200979');
INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) VALUES ((@eh_service_alliance_skip_rule := @eh_service_alliance_skip_rule + 1), '999984', '200980');



--
-- 车辆放行模块   add by xq.tian  2016/12/16
--
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`)
VALUES ('20900', '车辆放行', '20000', '/20000/20900', '0', '2', '2', '0', UTC_TIMESTAMP());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES ('10056', '0', '车辆放行 申请放行', '车辆放行 申请放行权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES ('10057', '0', '车辆放行 处理放行任务', '车辆放行 处理放行任务权限', NULL);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `role_type`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 10056, 1001, 0, 'EhAclRoles', 1, NOW());
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `role_type`,  `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 10057, 1001, 0, 'EhAclRoles', 1, NOW());

SET @eh_service_module_privileges = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges := @eh_service_module_privileges + 1), '20900', '1', '10056', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges := @eh_service_module_privileges + 1), '20900', '1', '10057', NULL, '0', UTC_TIMESTAMP());

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10056, 20900, '车辆放行', 1, 1, '车辆放行  全部权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10057, 20900, '车辆放行', 1, 1, '车辆放行  全部权限', 202);

INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10056, 20910, '车辆放行', 1, 1, '车辆放行  全部权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10057, 20910, '车辆放行', 1, 1, '车辆放行  全部权限', 202);

INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10056, 20920, '车辆放行', 1, 1, '车辆放行  全部权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10057, 20920, '车辆放行', 1, 1, '车辆放行  全部权限', 202);

INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10056, 20930, '车辆放行', 1, 1, '车辆放行  全部权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10057, 20930, '车辆放行', 1, 1, '车辆放行  全部权限', 202);

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (20900, '车辆放行', 20000, NULL, 'parking_clearance', 1, 2, '/20000/20900', 'park', 300);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (20910, '权限设置', 20900, NULL, 'vehicle_setting', 0, 2, '/20000/20900/20910', 'park', 301);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (20920, '放行记录', 20900, NULL, 'release_record', 0, 2, '/20000/20900/20920', 'park', 302);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (20930, '工作流设置', 20900, NULL, 'react:/working-flow/flow-list/vehicle-release/20900', 0, 2, '/20000/20900/20930', 'park', 303);

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 20900, '', 'EhNamespaces', 999984, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 20910, '', 'EhNamespaces', 999984, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 20920, '', 'EhNamespaces', 999984, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 20930, '', 'EhNamespaces', 999984, 2);

--
-- 模板
--
SET @eh_locale_templates = (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@eh_locale_templates := @eh_locale_templates + 1), 'parking.clearance', '1', 'zh_CN', '停车放行申请人看到的内容', '[{"key":"停车场名","value":"${parkingLotName}","entityType":"list"},{"key":"车牌号码","value":"${plateNumber}","entityType":"list"},{"key":"预计来访时间","value":"${clearanceTime}","entityType":"list"},{"key":"备注","value":"${remarks}","entityType":"multi_line"}]', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@eh_locale_templates := @eh_locale_templates + 1), 'parking.clearance', '2', 'zh_CN', '停车放行处理人看到的内容', '[{"key":"停车场名","value":"${parkingLotName}","entityType":"list"},{"key":"申请人","value":"${applicant}","entityType":"list"},{"key":"手机号","value":"${identifierToken}","entityType":"list"},{"key":"车牌号码","value":"${plateNumber}","entityType":"list"},{"key":"预计来访时间","value":"${clearanceTime}","entityType":"list"},{"key":"备注","value":"${remarks}","entityType":"multi_line"}]', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@eh_locale_templates := @eh_locale_templates + 1), 'parking.clearance', '3', 'zh_CN', '工作流摘要内容', '车牌号码：${plateNumber}\n来访时间：${clearanceTime}', '0');

SET @eh_locale_strings = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance', '1', 'zh_CN', '无');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance', '2', 'zh_CN', '对不起,您没有权限申请放行');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance', '3', 'zh_CN', '对不起,您没有权限处理放行任务');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance', '10001', 'zh_CN', '删除用户失败');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance', '10002', 'zh_CN', '没有启用的工作流');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance.log.status', '1', 'zh_CN', '处理中');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance.log.status', '2', 'zh_CN', '已完成');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance.log.status', '3', 'zh_CN', '已取消');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance.log.status', '4', 'zh_CN', '待处理');

SELECT max(id) FROM `eh_launch_pad_items` INTO @launch_pad_item_id;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), '999984', '0', '0', '0', '/home', 'Bizs', 'PARKING_CLEARANCE', '车辆放行', 'cs://1/image/aW1hZ2UvTVRvME5tTXpZVEEwWlRKa1lqQXlaVFEwTmpkaU5XRTJORGN5WVdJM056QmpZUQ', '1', '1', '57', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '1', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), '999984', '0', '0', '0', '/home', 'Bizs', 'PARKING_CLEARANCE_TASK', '放行任务', 'cs://1/image/aW1hZ2UvTVRvME5tTXpZVEEwWlRKa1lqQXlaVFEwTmpkaU5XRTJORGN5WVdJM056QmpZUQ', '1', '1', '58', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '1', NULL);

--
-- 新增的模板  add by xq.tian  2016/12/16
--
SET @eh_locale_strings = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance', '10003', 'zh_CN', '用户已添加');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parameters.error', '10001', 'zh_CN', '参数长度超过限制');


--  图片修改
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRwaE16azVaREE0WmpkaFltRmhOVFJrTW1ZMU56WTFZVEJrTTJOa1lqRmlPQQ"  where namespace_id=999984 and item_label="云服务";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRwallXRTNNamcwTWpjMU1UaGpOMk14TURrNFlqSXpOamxtT0RRd09XWm1OUQ"  where namespace_id=999984 and item_label="产品测试";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRvNVlUaG1abVl3WlRObU5qRTJNVGhqTXpJd05XTTBZVFEwT0RGaE4ySXpPQQ"  where namespace_id=999984 and item_label="人力咨询";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRvNE16UTFaakZtWlRBeE5ETmhZVEkxT0ROa1pEVXdNall5WkRObU0yUmlOQQ"  where namespace_id=999984 and item_label="会议室预约";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRvMk5tWm1PVEl5TVRoak56WTNabU13T0RFMk9UWTFZVEl6TURjeFpURmpZZw"  where namespace_id=999984 and item_label="供应链";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRwa1lqZzJNRFkyTmpBeE5EVXlNR0V4TXpJM05ERTRNV1EzTVRSallqZzJZdw"  where namespace_id=999984 and item_label="保洁服务";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRwaU9UWmtZVEprT1dKa1pEUXpaRGs0TVdFd05tRTNaakppT0RFeVlqWmtZZw"  where namespace_id=999984 and item_label="停车充值";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRwbE9UWTBObU0xTjJFM09HWXpNak5tT0RWalpUTXhOV0ptT0dJMVpHWXlNdw"  where namespace_id=999984 and item_label="车辆放行";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRwaVpUVmhZV1ppTlRnM016UXlZMkZtWkdGbFpEUmlORFk1WTJWbVpEVXlZZw"  where namespace_id=999984 and item_label="双创服务";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRvd1pqaGxOVEl4WldZMlpUbGpNRGs1TURsallUQTFNR0kzWVdZME5UTmxPUQ"  where namespace_id=999984 and item_label="园区入驻";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRvd01qa3haREZpTURVMU5UQmxZbUppWXpnMFpUZzNPV1psTW1aalptUTFNQQ"  where namespace_id=999984 and item_label="国际合作";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRwak9HSmxabUl5T0RkbU9HRTBNMk0xWkRjNE1HTXpOak13TURCak9XSm1Ndw"  where namespace_id=999984 and item_label="我有项目";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRvME4ySmlaamN3WkdGaU1qSmpOREkyTlRVNVpEWmtOamN5Wm1Fd1lUZzJPUQ"  where namespace_id=999984 and item_label="我要投资";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRvM016QTBPV05oTnpJME1XWmlPV0V3WXpKaVpXUXlZVFV5WmpNeVl6WTJNZw"  where namespace_id=999984 and item_label="我要融资";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRvNVpXUTJOalUzT1dRNU16SmxOMkl3TVRSa09XVXpOV1EzT1RVeE16WXlPQQ"  where namespace_id=999984 and item_label="打卡考勤";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRvellqSm1PV1V6WVRObE16VXdaalE1TXpJelpqQXlNV0kyWmpOaU9UTmtOdw"  where namespace_id=999984 and item_label="投诉建议";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRvME9UTXdOV0kzTURJNU9XWXdPV0UxWkRZMk5HWmxOalppT0RNd1ptRmpOZw"  where namespace_id=999984 and item_label="星空孵化";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRwbU1qQTJPVEJoWlRZek1XUTFZV1kxWlRkbE56Y3daV1poTXpZek1qTTFZUQ"  where namespace_id=999984 and item_label="更多";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRwak1UVmxOR0l6WldVM016ZzBZVEEyWkRnek0yUmhZekUzTURWaVlqazBZZw"  where namespace_id=999984 and item_label="水木之家";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRvNU56ZGhNRGc1WmpObU9UVmhOak14TXpneE5Ea3dORGMyT1dFd01EWTFNdw"  where namespace_id=999984 and item_label="法律服务";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRwa05XWmhOell4WVdFMVpUUTRaamRoWVRWa1pqazBZMlUwWm1VMll6RmtaQQ"  where namespace_id=999984 and item_label="物业报修";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRvM016RmxZV1l5WXprMVlqQXpZalZtTURNM1pXSmlPV014WmpCak56UXpPUQ"  where namespace_id=999984 and item_label="知识产权";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRwbFl6UmtZamd5WVRjMk1URXdPRFZoTkRCbE56UTVZekE1TldVMk16ZGtaZw"  where namespace_id=999984 and item_label="研发平台";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRwbFptRm1NREl6WkdFM05tUmtZalZoTkRkaU9EWTJNREpoT0dKa1lXUm1aZw"  where namespace_id=999984 and item_label="福利管家";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRvNVlUa3hOVEprWkRFeVpEaGxOVFpqTURjNE9EQXpNR1k0TnpoaVlqSTRZZw"  where namespace_id=999984 and item_label="科技成果";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRwaU1EY3haVEJoT0dFek5ERm1aamc0WWpWa01HVTFNRFprWldGa01UUTRPQQ"  where namespace_id=999984 and item_label="资金扶持";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRwa016RTVZekExWldSbFlUTTNOelE1TVRjMVkyWTJZbVkzTnpSbE5ERXhNUQ"  where namespace_id=999984 and item_label="科技金融";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRvMU1XUmtNV1kyWWpFMFltRXhOMkkwWkRBek5ESTRZekl3WkRBMU5UbGpNUQ"  where namespace_id=999984 and item_label="紫荆汇";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRvM01UUXlNRGhrTVdFeU5UWXdZMlZrTWpBNE1EUXlZVGt5TkRZMU4yWXdNdw"  where namespace_id=999984 and item_label="维修服务";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRwall6ZGxZVFUxWVRNNE1HWTBabUkzWlRrMk9UZGlOek13WldWbFl6ZGhZUQ"  where namespace_id=999984 and item_label="绿植租摆";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRvd05EVTBNMkprTkRGaE9XSTNaVEZtTmpWaVl6bGtPVGt3WlRsaU9EQTJOUQ"  where namespace_id=999984 and item_label="财税咨询";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRvMU5XUTFOV1JpWVdFNU5qVTBPVGxsWlRoa1l6ZGlNREZpWldVMVpUTXpNUQ"  where namespace_id=999984 and item_label="载物咨询";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRwbU1ERm1ObUV6TW1KbVlUZzRZMkUwWkRsa1lUSmtaRFkyWVRjek1XTTFPUQ"  where namespace_id=999984 and item_label="通讯录";
update  eh_launch_pad_items set icon_uri="cs://1/image/aW1hZ2UvTVRvMk5UTmpOalprWldSak1EZzVaVEkzTmpCbE1URXdNRFUwT1RCaVlXSTROUQ"  where namespace_id=999984 and item_label="钥匙圈";


update eh_launch_pad_items set action_data = '{\"url\":\"https://biz.lihekefu.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.lihekefu.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14479281351428699655%3F_k%3Dzlbiz#sign_suffix\"}' where item_label = '水木之家' and namespace_id = 999984;
update eh_launch_pad_items set action_data = '{\"url\":\"http://zijing.lihekefu.com/mobile/static/coming_soon/index.html\"}' where item_label = '绿植租摆' and namespace_id = 999984;
update eh_launch_pad_items set action_data = '{\"url\":\"http://zijing.lihekefu.com/mobile/static/coming_soon/index.html\"}' where item_label = '服务维修' and namespace_id = 999984;
update eh_launch_pad_items set action_data = '{\"url\":\"http://zijing.lihekefu.com/property_service/index.html?taskCategoryId=205201&hideNavigationBar=1#/my_service#sign_suffix\"}' where item_label = '投诉建议' and namespace_id = 999984;
update eh_launch_pad_items set action_data = '{\"url\":\"http://zijing.lihekefu.com/mobile/static/coming_soon/index.html\"}' where item_label = '资金扶持' and namespace_id = 999984;
update eh_launch_pad_items set action_data = '{\"url\":\"http://zijing.lihekefu.com/mobile/static/coming_soon/index.html\"}' where item_label = '停车充值' and namespace_id = 999984;
update eh_launch_pad_items set action_data = '{\"url\":\"http://zijing.lihekefu.com/mobile/static/coming_soon/index.html\"}' where item_label = '车辆放行' and namespace_id = 999984;


UPDATE eh_launch_pad_items set target_type = NULl, target_id = NULl where item_label = '水木之家';
UPDATE eh_launch_pad_items set target_type = NULl, target_id = NULl where item_label = '服务维修';
UPDATE eh_launch_pad_items set target_type = NULl, target_id = NULl where item_label = '绿植租摆';
UPDATE eh_launch_pad_items set target_type = NULl, target_id = NULl where item_label = '紫荆汇';

UPDATE eh_launch_pad_items set action_type = 14, action_data = '{\"url\":\"https://biz.lihekefu.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.lihekefu.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14828292658152925777%3F_k%3Dzlbiz#sign_suffix"}' where item_label = '水木之家';
UPDATE eh_launch_pad_items set action_type = 14, action_data = '{\"url\":\"https://biz.lihekefu.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.lihekefu.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14828296650914172669%3F_k%3Dzlbiz#sign_suffix"}' where item_label = '服务维修';
UPDATE eh_launch_pad_items set action_type = 14, action_data = '{\"url\":\"https://biz.lihekefu.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.lihekefu.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14828295497182208491%3F_k%3Dzlbiz#sign_suffix"}' where item_label = '绿植租摆';
UPDATE eh_launch_pad_items set action_type = 14, action_data = '{\"url\":\"https://biz.lihekefu.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.lihekefu.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14828293251069258160%3F_k%3Dzlbiz#sign_suffix"}' where item_label = '紫荆汇';

DELETE from eh_launch_pad_items where item_label = '车辆放行' and scene_type = 'park_tourist' and namespace_id = 999984;

DELETE from eh_web_menu_scopes where menu_id = 50700;
DELETE from eh_web_menu_scopes where menu_id = 50710;
DELETE from eh_web_menu_scopes where menu_id = 50720;
DELETE from eh_web_menu_scopes where menu_id = 50730;

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41000,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41010,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41020,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41030,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41040,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41050,'', 'EhNamespaces', 999984,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41060,'', 'EhNamespaces', 999984,2);


-- 放行任务不放在首页，位于更多车辆放行右边
update eh_launch_pad_items set icon_uri='cs://1/image/aW1hZ2UvTVRvd05UVXdPREUyTUdWaE1tUXdZMkU1WW1NNFlqSmpabVk1TVRBek56RTRZdw', display_flag=0,service_categry_id=5, id = 112972 where   item_label  = "放行任务";

update eh_launch_pad_items set action_type=57 where   item_label  = "车辆放行";
-- 删除门禁
delete from eh_launch_pad_items where item_label = "北环门禁";


INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `card_reserve_days`, `status`, `creator_uid`, `create_time`, `max_request_num`, `tempfee_flag`, `rate_flag`, `recharge_month_count`, `recharge_type`, `namespace_id`, `is_support_recharge`)
	VALUES ('10007', 'community', '240111044331055835', '清华信息港停车场', '', NULL, '1200', '2', '1025', '2016-12-16 17:07:20', '2', '0', '0', '2', '2', '0', '1');


INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
	VALUES (1, '999984', '紫荆', '1', '', '', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
	VALUES (2, '999984', '紫荆', '2', '', '', '移动平台聚合服务，助力园区效能提升');

--北环门禁 by janson
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`,`service_categry_id`)
VALUES ('1112290', '999984', '0', '0', '0', '/home', 'Bizs', '北环门禁', '北环门禁', 'cs://1/image/aW1hZ2UvTVRvMk5UTmpOalprWldSak1EZzVaVEkzTmpCbE1URXdNRFUwT1RCaVlXSTROUQ', '1', '1', '63', '{\"hardwareId\":\"C7:7C:FE:2A:AD:43\"}', '30', '0', '1', '0', NULL, '0', NULL, '', '', '1', 'park_tourist', '1', '5');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`,`service_categry_id`)
VALUES ('1112291', '999984', '0', '0', '0', '/home', 'Bizs', '北环门禁', '北环门禁', 'cs://1/image/aW1hZ2UvTVRvMk5UTmpOalprWldSak1EZzVaVEkzTmpCbE1URXdNRFUwT1RCaVlXSTROUQ', '1', '1', '63', '{\"hardwareId\":\"C7:7C:FE:2A:AD:43\"}', '30', '0', '1', '0', NULL, '0', NULL, '', '', '1', 'pm_admin', '1', '5');

-- update 北环门禁 mac by janson
update `eh_launch_pad_items` set `action_data`='{\"hardwareId\":\"FA:10:D8:2D:F9:37\"}' where id=1112290;
update `eh_launch_pad_items` set `action_data`='{\"hardwareId\":\"FA:10:D8:2D:F9:37\"}' where id=1112291;




--
-- 文件管理菜单   add by xq.tian  2017/02/24
--
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (41500, '文件管理', 40000, NULL, 'react:/file-management/file-upload/41500', 0, 2, '/40000/41500', 'park', 390);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10135, 0, '文件管理', '文件管理 全部权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10135, 41500, '文件管理', 1, 1, '文件管理  全部权限', 202);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, 10135, 1001, 'EhAclRoles', 0, 1, NOW());

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 41500, '', 'EhNamespaces', 999984, 2);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`)
VALUES (41500, '文件管理', 40000, '/40000/41500', 0, 2, 2, 0, UTC_TIMESTAMP());

SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), 41500, 1, 10135, NULL, '0', UTC_TIMESTAMP());

SET @eh_service_module_scopes_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999984, 41500, '文件管理', 'EhNamespaces', 999984, NULL, 2);

--
-- 文件上传菜单   add by xq.tian  2017/02/24
--
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (80600, '文件下载', 80000, NULL, 'react:/file-management/file-download', 1, 2, '/80000/80600', 'park', 500);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10136, 0, '文件下载', '文件下载 全部权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10136, 80600, '文件下载', 1, 1, '文件下载  全部权限', 202);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, 10136, 1005, 'EhAclRoles', 0, 1, NOW());

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 80000, '', 'EhNamespaces', 999984, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 80600, '', 'EhNamespaces', 999984, 2);

SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), 41500, 1, 10136, NULL, 0, UTC_TIMESTAMP());

-- update 北环门禁 mac by janson
update `eh_launch_pad_items` set `action_data`='{\"hardwareId\":\"E8:9F:54:9D:A5:A8\"}' where id=1112290;
update `eh_launch_pad_items` set `action_data`='{\"hardwareId\":\"E8:9F:54:9D:A5:A8\"}' where id=1112291;

-- added by wh 2017-4-19 18:56:55 

SET @id = (SELECT MAX(id) FROM  eh_web_menu_scopes );
INSERT INTO  `eh_web_menu_scopes`(`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@id :=@id +1), '40541', '', 'EhNamespaces', '999984', '2');
INSERT INTO  `eh_web_menu_scopes`(`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@id :=@id +1), '40542', '', 'EhNamespaces', '999984', '2');
INSERT INTO  `eh_web_menu_scopes`(`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@id :=@id +1), '12200', '', 'EhNamespaces', '999984', '2');
INSERT INTO  `eh_web_menu_scopes`(`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@id :=@id +1), '40540', '', 'EhNamespaces', '999984', '2');
INSERT INTO  `eh_web_menu_scopes`(`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@id :=@id +1), '41300', '', 'EhNamespaces', '999984', '2');
INSERT INTO  `eh_web_menu_scopes`(`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@id :=@id +1), '41310', '', 'EhNamespaces', '999984', '2');
INSERT INTO  `eh_web_menu_scopes`(`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@id :=@id +1), '41320', '', 'EhNamespaces', '999984', '2');
INSERT INTO  `eh_web_menu_scopes`(`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@id :=@id +1), '50900', '', 'EhNamespaces', '999984', '2');


UPDATE  eh_launch_pad_items SET default_order =3 WHERE namespace_id = 999984 AND service_categry_id ='4';
UPDATE  eh_launch_pad_items SET service_categry_id ='4' ,default_order =2 WHERE namespace_id = 999984 AND  item_label ='资金扶持';  



	
-- 许娟在升级4.5.0的时候，把清华信息港从4.3.2版本升级到4.5.0，出现服务市场为空的问题，这是因为在4.3.3版本eh_item_service_categries加上了scene_type字段，却没有做数据迁移；
-- 数据迁移时，需要把原来的几个类型的scene_type改为pm_admin(加字段时默认值为default)，然后再添加park_tourist场景的数据，并修改eh_launch_pad_items的service_categry_id值
-- by lqs 20170503
UPDATE `eh_item_service_categries` SET `scene_type`='pm_admin' WHERE `id` IN (3, 4, 5, 6, 7);
INSERT INTO `eh_item_service_categries`(`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`)
	VALUES(8, '科创服务', 'cs://1/image/aW1hZ2UvTVRvME9ESmxPR1pqT0dFM1pHSmpaRFkwTWpRNE16TmtaVEUwTWpRMFl6UTNaUQ', 1, 0, 1, 999984,'park_tourist');
INSERT INTO `eh_item_service_categries`(`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`)
	VALUES(9, '专业服务', 'cs://1/image/aW1hZ2UvTVRvME9ESmxPR1pqT0dFM1pHSmpaRFkwTWpRNE16TmtaVEUwTWpRMFl6UTNaUQ', 1, 0, 1, 999984,'park_tourist');
INSERT INTO `eh_item_service_categries`(`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`)
	VALUES(10, '园区服务', 'cs://1/image/aW1hZ2UvTVRvME9ESmxPR1pqT0dFM1pHSmpaRFkwTWpRNE16TmtaVEUwTWpRMFl6UTNaUQ', 1, 0, 1, 999984,'park_tourist');
INSERT INTO `eh_item_service_categries`(`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`)
	VALUES(11, '生活服务', 'cs://1/image/aW1hZ2UvTVRvME9ESmxPR1pqT0dFM1pHSmpaRFkwTWpRNE16TmtaVEUwTWpRMFl6UTNaUQ', 1, 0, 1, 999984,'park_tourist');
INSERT INTO `eh_item_service_categries`(`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`)
	VALUES(12, '企业服务', 'cs://1/image/aW1hZ2UvTVRvME9ESmxPR1pqT0dFM1pHSmpaRFkwTWpRNE16TmtaVEUwTWpRMFl6UTNaUQ', 1, 0, 1, 999984,'park_tourist');		
	
-- 修改eh_launch_pad_items的service_categry_id值
update eh_launch_pad_items set service_categry_id=12 where id=112858 AND namespace_id=999984; -- 打卡考勤  park_tourist  7
update eh_launch_pad_items set service_categry_id=10 where id=112897 AND namespace_id=999984; -- 会议室  park_tourist  5
update eh_launch_pad_items set service_categry_id=10 where id=112899 AND namespace_id=999984; -- 园区入驻  park_tourist  5
update eh_launch_pad_items set service_categry_id=10 where id=112910 AND namespace_id=999984; -- 物业报修  park_tourist  5
update eh_launch_pad_items set service_categry_id=10 where id=112916 AND namespace_id=999984; -- 报修任务  park_tourist  5
update eh_launch_pad_items set service_categry_id=12 where id=112918 AND namespace_id=999984; -- 通讯录  park_tourist  7
update eh_launch_pad_items set service_categry_id=10 where id=112926 AND namespace_id=999984; -- 服务维修  park_tourist  5
update eh_launch_pad_items set service_categry_id=11 where id=112928 AND namespace_id=999984; -- 水木之家  park_tourist  6
update eh_launch_pad_items set service_categry_id=9 where id=112958 AND namespace_id=999984; -- 知识产权  park_tourist  4
update eh_launch_pad_items set service_categry_id=9 where id=112959 AND namespace_id=999984; -- 财税咨询  park_tourist  4
update eh_launch_pad_items set service_categry_id=9 where id=112960 AND namespace_id=999984; -- 法律服务  park_tourist  4
update eh_launch_pad_items set service_categry_id=9 where id=112961 AND namespace_id=999984; -- 人力资源  park_tourist  4
update eh_launch_pad_items set service_categry_id=9 where id=112962 AND namespace_id=999984; -- 科技金融  park_tourist  4
update eh_launch_pad_items set service_categry_id=9 where id=112963 AND namespace_id=999984; -- 云服务  park_tourist  4
update eh_launch_pad_items set service_categry_id=9 where id=112964 AND namespace_id=999984; -- 供应链  park_tourist  4
update eh_launch_pad_items set service_categry_id=9 where id=112965 AND namespace_id=999984; -- 产品测试  park_tourist  4
update eh_launch_pad_items set service_categry_id=9 where id=112966 AND namespace_id=999984; -- 福利管家  park_tourist  4
update eh_launch_pad_items set service_categry_id=9 where id=112970 AND namespace_id=999984; -- 资金扶持  park_tourist  4
update eh_launch_pad_items set service_categry_id=10 where id=112974 AND namespace_id=999984; -- 投诉建议  park_tourist  5
update eh_launch_pad_items set service_categry_id=10 where id=112976 AND namespace_id=999984; -- 绿植租摆  park_tourist  5
update eh_launch_pad_items set service_categry_id=10 where id=1112290 AND namespace_id=999984; -- 北环门禁  park_tourist  5
update eh_launch_pad_items set service_categry_id=8 where id=112949 AND namespace_id=999984; -- 我要投资  park_tourist  3
update eh_launch_pad_items set service_categry_id=8 where id=112950 AND namespace_id=999984; -- 我要融资  park_tourist  3
update eh_launch_pad_items set service_categry_id=8 where id=112951 AND namespace_id=999984; -- 我有项目  park_tourist  3
update eh_launch_pad_items set service_categry_id=8 where id=112952 AND namespace_id=999984; -- 星空孵化  park_tourist  3
update eh_launch_pad_items set service_categry_id=8 where id=112953 AND namespace_id=999984; -- 研发平台  park_tourist  3
update eh_launch_pad_items set service_categry_id=8 where id=112954 AND namespace_id=999984; -- 载物咨询  park_tourist  3
update eh_launch_pad_items set service_categry_id=8 where id=112955 AND namespace_id=999984; -- 国际合作  park_tourist  3
update eh_launch_pad_items set service_categry_id=8 where id=112956 AND namespace_id=999984; -- 科技成果  park_tourist  3
update eh_launch_pad_items set service_categry_id=8 where id=112957 AND namespace_id=999984; -- 紫荆汇  park_tourist  3

-- redmine 9164 add by xiongying 20170503
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES (201015, 'community', '240111044331055835', '0', '供求信息', '供求信息', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999984', '');

SET @sa_id = (SELECT max(id) FROM `eh_service_alliances`);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331055835', '供求信息', '供求信息', 201015, '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

    
SET @eh_launch_pad_items = (SELECT max(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '供求信息', '供求信息', 'cs://1/image/aW1hZ2UvTVRvM09HVmxNalUyTXpnNU5qZ3lNVEEzTVRGbE9HUTBZMlk1T0dSbU1qZzNNZw', '1', '1', 33, '{"type":201015,"parentId":201015,"displayType": "list"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999984', '0', '0', '0', '/home', 'Bizs', '供求信息', '供求信息', 'cs://1/image/aW1hZ2UvTVRvM09HVmxNalUyTXpnNU5qZ3lNVEEzTVRGbE9HUTBZMlk1T0dSbU1qZzNNZw', '1', '1', 33, '{"type":201015,"parentId":201015,"displayType": "list"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '3');

update eh_launch_pad_items set more_order = 10 where namespace_id = 999984 and item_label in('知识产权','财税咨询','法律服务','人力资源','科技金融','云服务','供应链','产品测试','福利管家');

update eh_launch_pad_items set display_flag = 0 where item_label = '供求信息' and namespace_id = 999984;

-- 修改服务市场“供求信息”item的位置及规则，add by tt, 20170510（已执行）
SET @eh_service_alliance_skip_rule = (SELECT max(id) FROM `eh_service_alliance_skip_rule`);
INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) VALUES ((@eh_service_alliance_skip_rule := @eh_service_alliance_skip_rule + 1), '999984', '0');
update eh_launch_pad_items set service_categry_id = 5 where namespace_id = 999984 and item_name = '供求信息' and scene_type= 'pm_admin';
update eh_launch_pad_items set service_categry_id = 10 where namespace_id = 999984 and item_name = '供求信息' and scene_type= 'park_tourist';

-- 车辆放行导出excel无数据提示，add by tt, 20170510
select max(id) into @id from `eh_locale_strings`; 
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id+1, 'parking.clearance', '10011', 'zh_CN', '没有数据');

-- 更改服务联盟是否支持审批, add by tt, 20170510
select max(id) into @id from `eh_service_alliance_jump_module`;
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_url`, `parent_id`) VALUES (@id+1, 999984, '审批', 'zl://approval/create?approvalId={}&sourceId={}', 0);


-- 添加业务授权模块范围
SET @id = (SELECT MAX(id) FROM  eh_service_module_scopes );
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
	VALUES ((@id := @id + 1, '999984', '60200', '业务授权', 'EhNamespaces', '999984', NULL, '2');

	
	
-- 清华信息港 增加创业导师item add sfyan 20170526
SET @service_alliance_categorie_id = (SELECT max(id) FROM `eh_service_alliance_categories`);
SET @launch_pad_item_id = (SELECT max(id) FROM `eh_launch_pad_items`);
SET @item_service_categry_id = (SELECT id FROM `eh_item_service_categries` where namespace_id = 999984 and `scene_type` = 'pm_admin' and name = '专业服务');
SET @sa_id = (SELECT max(id) FROM `eh_service_alliances`);

INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ((@service_alliance_categorie_id := @service_alliance_categorie_id + 1), 'community', '240111044331055835', '0', '创业导师', '创业导师', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999984', '');
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331055835', '创业导师', '创业导师', @service_alliance_categorie_id , '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) values((@launch_pad_item_id := @launch_pad_item_id + 1),'999984','0','0','0','/home','Bizs','创业导师','创业导师','cs://1/image/aW1hZ2UvTVRveU1USXpZV00zWm1Jd01EY3hOemt4WXpjME5EWm1PRGd5WkdaaE5EWXlPUQ','1','1','33',CONCAT('{"type":', @service_alliance_categorie_id, ',"parentId":', @service_alliance_categorie_id, ',"displayType": "list"}'),'0','0','1','1','','0',NULL,NULL,NULL,'1','pm_admin','1',@item_service_categry_id,NULL,20);

SET @item_service_categry_id = (SELECT id FROM `eh_item_service_categries` where namespace_id = 999984 and `scene_type` = 'park_tourist' and name = '专业服务');
insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) values((@launch_pad_item_id := @launch_pad_item_id + 1),'999984','0','0','0','/home','Bizs','创业导师','创业导师','cs://1/image/aW1hZ2UvTVRveU1USXpZV00zWm1Jd01EY3hOemt4WXpjME5EWm1PRGd5WkdaaE5EWXlPUQ','1','1','33',CONCAT('{"type":', @service_alliance_categorie_id, ',"parentId":', @service_alliance_categorie_id, ',"displayType": "list"}'),'0','0','1','1','','0',NULL,NULL,NULL,'1','park_tourist','1',@item_service_categry_id,NULL,20);

-- 清除报修任务数据 孙稳提供
DELETE from eh_pm_tasks;
DELETE from eh_pm_task_logs;
DELETE from eh_pm_task_statistics;
DELETE from eh_pm_task_targets;
DELETE from eh_pm_task_target_statistics;

-- 删除保修任务icon，增加保修任务icon
delete from eh_launch_pad_items where namespace_id = 999984 and item_label = '报修任务';
SET @launch_pad_item_id = (SELECT max(id) FROM `eh_launch_pad_items`);
insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) values((@launch_pad_item_id := @launch_pad_item_id + 1),'999984','0','0','0','/home','Bizs','任务管理','任务管理','cs://1/image/aW1hZ2UvTVRvMU5HUTVZak15WkdJMFl6ZzFOREUwWlRCa1lUWmhOemxsTWpFM09UVmxNUQ','1','1','56','','0','0','1','1','','0',NULL,NULL,NULL,'1','pm_admin','1',0,NULL,0);

insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) values((@launch_pad_item_id := @launch_pad_item_id + 1),'999984','0','0','0','/home','Bizs','任务管理','任务管理','cs://1/image/aW1hZ2UvTVRvMU5HUTVZak15WkdJMFl6ZzFOREUwWlRCa1lUWmhOemxsTWpFM09UVmxNUQ','1','1','56','','0','0','1','1','','0',NULL,NULL,NULL,'1','park_tourist','1',0,NULL,0);

-- 删除执行人员设置菜单，增加工作流设置菜单
delete from eh_web_menu_scopes where owner_type = 'EhNamespaces' and owner_id = 999984 and menu_id = 20160;
SET @service_module_scope_id = (SELECT MAX(id) FROM  eh_service_module_scopes );
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
	VALUES ((@service_module_scope_id := @service_module_scope_id + 1), '999984', '20158', '', NULL, '2');
	
	
-- 增加园区	
SET @community_id = (SELECT MAX(id) FROM `eh_communities`) + 5; 
SET @community_geopoint_id = (SELECT MAX(id) FROM `eh_community_geopoints`) + 1; 
SET @building_id = (SELECT MAX(id) FROM `eh_buildings`) + 1; 
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES(@community_id, UUID(), 15306, '深圳市',  15307, '南山区', '清华信息港B', '清华信息港B', '深圳市南山区科技园北区清华信息港B', NULL, '2001年3月3日，深圳市人民政府和清华大学共同召开“清华信息港启动建设”新闻发布会，开始建设清华信息港。清华信息港园区占地4万平方米，规划建筑面积13.8万平方米，一期工程建筑面积6.7万平方米，2003年10月正式开园投入使用。二期工程（科研楼）建筑面积7.1万平方米，2014年12月正式投入使用。园区依托清华大学的科技创新优势，加强基础配套服务，营造创新创业环境。',
	NULL, NULL, NULL, NULL, NULL, NULL,NULL, 168, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'1', 185911, 185912, UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`)
	VALUES(@community_geopoint_id, @community_id, '', 113.960095, 22.547367, 'ws1030x5ytve');
	
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(@building_id, @community_id, 'A栋', 'A栋', 0, NULL, '清华信息港', NULl, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999984);

INSERT INTO `eh_organization_communities`(organization_id, community_id)
	VALUES(1008218, @community_id);
	
-- 补充紫荆数据 add by sfyan 20170714
SET @namespace_resource_id = (select max(id) from eh_namespace_resources) + 1; 
SET @community_id = (select id from eh_communities where name = '南海科技园' and namespace_id = 999984); 
INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES(@namespace_resource_id, 999984, 'COMMUNITY', @community_id, UTC_TIMESTAMP());

-- #13421 add by xq.tian 2017/08/03
SELECT max(id) FROM `eh_launch_pad_items` INTO @max_id;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
VALUES ((@max_id := @max_id + 1), 999984, 0, 0, 0, '/home', 'Bizs', 'PARKING_RECHARGE', '停车缴费', 'cs://1/image/aW1hZ2UvTVRwaU9UWmtZVEprT1dKa1pEUXpaRGs0TVdFd05tRTNaakppT0RFeVlqWmtZZw', 1, 1, 30, '', 29, 0, 1, 1, '1', 0, NULL, NULL, NULL, 1, 'park_tourist', 0, 5, NULL, 500, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
VALUES ((@max_id := @max_id + 1), 999984, 0, 0, 0, '/home', 'Bizs', 'PARKING_RECHARGE', '停车缴费', 'cs://1/image/aW1hZ2UvTVRwaU9UWmtZVEprT1dKa1pEUXpaRGs0TVdFd05tRTNaakppT0RFeVlqWmtZZw', 1, 1, 30, '', 29, 0, 1, 1, '1', 0, NULL, NULL, NULL, 1, 'pm_admin', 0, 5, NULL, 500, NULL);

-- #13496 add by xq.tian 2017/08/03
select max(id) from `eh_web_menu_scopes` into @max_id;
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@max_id := @max_id + 1), 56161, '', 'EhNamespaces', 999984, 2);

SELECT max(id) FROM `eh_web_menu_scopes` INTO @menu_sc_id;
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy)
VALUES ((@menu_sc_id := @menu_sc_id + 1), 40800, '', 'EhNamespaces', 999984, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy)
VALUES ((@menu_sc_id := @menu_sc_id + 1), 40810, '', 'EhNamespaces', 999984, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy)
VALUES ((@menu_sc_id := @menu_sc_id + 1), 40840, '', 'EhNamespaces', 999984, 2);
SELECT max(id) FROM `eh_web_menu_scopes` INTO @menu_sc_id;
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy)
VALUES ((@menu_sc_id := @menu_sc_id + 1), 50660, '', 'EhNamespaces', 999984, 2);
SELECT max(id) FROM `eh_web_menu_scopes` INTO @menu_sc_id;
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy)
VALUES ((@menu_sc_id := @menu_sc_id + 1), 41300, '', 'EhNamespaces', 999984, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy)
VALUES ((@menu_sc_id := @menu_sc_id + 1), 41310, '', 'EhNamespaces', 999984, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy)
VALUES ((@menu_sc_id := @menu_sc_id + 1), 41320, '', 'EhNamespaces', 999984, 2);
update eh_web_menus set status=2 where id in(41300, 41310, 41320);

-- 停车缴费 add by sw 20170808
UPDATE eh_parking_lots set vendor_name = 'JIN_YI' where id = 10007;


INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`)
	VALUES ('18191', '15305', '东莞市', 'DONGGUANSHI', 'DGS', '/广东/东莞市', '2', '2', NULL, '0769', '2', '0', 999984);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`)
	VALUES ('18192', '18191', '清溪镇', 'QINGXIZHEN', 'QXZ', '/广东/东莞市/清溪镇', '3', '3', NULL, '0769', '2', '0', 999984);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`)
	VALUES ('18193', '15305', '珠海市', 'ZHUHAISHI', 'ZHS', '/广东/珠海市', '2', '2', NULL, '0756', '2', '0', 999984);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`)
	VALUES ('18194', '18193', '香洲区', 'XIANGZHOUQU', 'XZQ', '/广东/珠海市/香洲区', '3', '3', NULL, '0756', '2', '0', 999984);

SET @community_geopoint_id = (SELECT max(id) FROM `eh_community_geopoints`);
SET @namespace_resource_id = (SELECT max(id) FROM `eh_namespace_resources`);

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(190620, UUID(), @namespace_id, 2, 'EhGroups', 0,'力合双清创新基地论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(190619, UUID(), @namespace_id, 2, 'EhGroups', 0,'力合双清创新基地意见反馈论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(190618, UUID(), @namespace_id, 2, 'EhGroups', 0,'大湾区科创园论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(190617, UUID(), @namespace_id, 2, 'EhGroups', 0,'大湾区科创园意见反馈论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());

INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES(240111044331050379, UUID(), 18191, '东莞市',  18192, '清溪镇', '力合双清创新基地', '力合双清创新基地', '“力合双清创新基地”位居深莞惠几何中心——东莞清溪，占地面积1200亩，建筑面积140万平方米。基地承载清华大学、深圳清华大学研究院、清华东莞创新中心科技成果产业化转移的功能，通过整合清华体系的科研、人才等优质资源，构建“创业苗圃+孵化器+加速器+企业总部基地”的全链条发展环境，为企业提供全生命周期的科技创新服务，致力于成为东莞市乃至广东省产城融合创新孵化示范基地、产业转型升级示范园区。', NULL, '',NULL, NULL, NULL, NULL, NULL, NULL,NULL, 98, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,1, 190620, 190619, UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`)
	VALUES((@community_geopoint_id := @community_geopoint_id + 1), 240111044331050379, '', 114.174205, 22.816999, 'ws11y1eneucs');
INSERT INTO `eh_organization_communities`(organization_id, community_id)
	VALUES(1008218, 240111044331050379);
INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`)
	VALUES((@namespace_resource_id := @namespace_resource_id + 1), 999984, 'COMMUNITY', 240111044331050379, UTC_TIMESTAMP());


INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES(240111044331050378, UUID(), 18193, '珠海市',  18194, '香洲区', '清华科技园（珠海）', '大湾区科创园', '清华科技园（珠海）是清华大学和珠海市人民政府于2000年协议共建的大学科技园区与高科技企业孵化器，依托清华大学的科技、人才优势，形成具有持续创新能力和国际竞争能力的产学研资结合体。它位于美丽的海滨城市珠海高新区，毗邻中山大学珠海校区，占地约14.46万㎡，规划总建筑面积36.15万㎡，一期工程6.52万㎡，于2002年投入使用；二期工程29.63万㎡，总投资19亿元，含研发办公、总部经济、中试基地、检测中心和生活配套等建筑形态。园区将打造集苗圃、孵化器、加速器为一体的创新孵化体系，实现战略新兴产业的集聚。', NULL, '',NULL, NULL, NULL, NULL, NULL, NULL,NULL, 98, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,1, 190618, 190617, UTC_TIMESTAMP(), 999984);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`)
	VALUES((@community_geopoint_id := @community_geopoint_id + 1), 240111044331050378, '', 113.592252, 22.349651, 'webz0uek5kfj');
INSERT INTO `eh_organization_communities`(organization_id, community_id)
	VALUES(1008218, 240111044331050378);
INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`)
	VALUES((@namespace_resource_id := @namespace_resource_id + 1), 999984, 'COMMUNITY', 240111044331050378, UTC_TIMESTAMP());

SET @building_id = 1960773;
INSERT INTO `eh_buildings` (`id`, `community_id`, `default_order`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(@building_id, 240111044331050378, @building_id, '创业大厦A座', '创业大厦A座', 0, '', '生态园', NULL, NULL, NULL, NULL, '', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999984);
SET @building_id = @building_id + 1;
INSERT INTO `eh_buildings` (`id`, `community_id`, `default_order`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(@building_id, 240111044331050378, @building_id, '创业大厦B座', '创业大厦B座', 0, '', '生态园', NULL, NULL, NULL, NULL, '', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999984);
SET @building_id = @building_id + 1;
INSERT INTO `eh_buildings` (`id`, `community_id`, `default_order`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(@building_id, 240111044331050378, @building_id, '综合楼C座', '综合楼C座', 0, '', '广东省珠海市香洲区大学路101号', NULL, NULL, NULL, NULL, '', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999984);
SET @building_id = @building_id + 1;
INSERT INTO `eh_buildings` (`id`, `community_id`, `default_order`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(@building_id, 240111044331050378, @building_id, '综合楼D座', '综合楼D座', 0, '', '广东省珠海市香洲区大学路101号', NULL, NULL, NULL, NULL, '', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999984);
SET @building_id = @building_id + 1;
INSERT INTO `eh_buildings` (`id`, `community_id`, `default_order`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(@building_id, 240111044331050378, @building_id, '创新大厦G座', '创新大厦G座', 0, '', '广东省珠海市香洲区大学路101号', NULL, NULL, NULL, NULL, '', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999984);
SET @building_id = @building_id + 1;
INSERT INTO `eh_buildings` (`id`, `community_id`, `default_order`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(@building_id, 240111044331050378, @building_id, '创新大厦H座', '创新大厦H座', 0, '', '广东省珠海市香洲区大学路101号', NULL, NULL, NULL, NULL, '', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999984);

SET @building_id = @building_id + 1;
INSERT INTO `eh_buildings` (`id`, `community_id`, `default_order`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(@building_id, 240111044331050378, @building_id, '清湖工业园办公楼', '办公楼', 0, '', '东莞市清溪镇青湖工业区富士工业城办公楼', NULL, NULL, NULL, NULL, '', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999984);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1159445', '999984', '0', '1', '240111044331055840', '/home', 'Bizs', '星空孵化', '星空孵化', 'cs://1/image/aW1hZ2UvTVRvMk5XWXhZbVV4WlRSa01qbGhORGcwT0dFMk5qTmxOV016TlRnd00ySTRPQQ', '1', '1', '33', '{\"type\":200980,\"parentId\":200980,\"displayType\": \"list\"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '3', NULL, '0', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1159425', '999984', '0', '1', '240111044331055840', '/home', 'Bizs', '星空孵化', '星空孵化', 'cs://1/image/aW1hZ2UvTVRvMk5XWXhZbVV4WlRSa01qbGhORGcwT0dFMk5qTmxOV016TlRnd00ySTRPQQ', '1', '1', '33', '{\"type\":200980,\"parentId\":200980,\"displayType\": \"list\"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '8', NULL, '0', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1159445', '999984', '0', '1', '240111044331055835', '/home', 'Bizs', '星空孵化', '星空孵化', 'cs://1/image/aW1hZ2UvTVRvMk5XWXhZbVV4WlRSa01qbGhORGcwT0dFMk5qTmxOV016TlRnd00ySTRPQQ', '1', '1', '33', '{\"type\":200980,\"parentId\":200980,\"displayType\": \"list\"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '3', NULL, '0', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1159425', '999984', '0', '1', '240111044331055835', '/home', 'Bizs', '星空孵化', '星空孵化', 'cs://1/image/aW1hZ2UvTVRvMk5XWXhZbVV4WlRSa01qbGhORGcwT0dFMk5qTmxOV016TlRnd00ySTRPQQ', '1', '1', '33', '{\"type\":200980,\"parentId\":200980,\"displayType\": \"list\"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '8', NULL, '0', NULL);


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1149450', '999984', '0', '1', '240111044331055840', '/home', 'Bizs', '云服务', '云服务', 'cs://1/image/aW1hZ2UvTVRwaE16azVaREE0WmpkaFltRmhOVFJrTW1ZMU56WTFZVEJrTTJOa1lqRmlPQQ', '1', '1', '33', '{\"type\":200991,\"parentId\":200991,\"displayType\": \"list\"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '4', NULL, '10', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1149460', '999984', '0', '1', '240111044331055840', '/home', 'Bizs', '供应链', '供应链', 'cs://1/image/aW1hZ2UvTVRvMk5tWm1PVEl5TVRoak56WTNabU13T0RFMk9UWTFZVEl6TURjeFpURmpZZw', '1', '1', '33', '{\"type\":200992,\"parentId\":200992,\"displayType\": \"list\"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '4', NULL, '10', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1149470', '999984', '0', '1', '240111044331055840', '/home', 'Bizs', '产品测试', '产品测试', 'cs://1/image/aW1hZ2UvTVRwallXRTNNamcwTWpjMU1UaGpOMk14TURrNFlqSXpOamxtT0RRd09XWm1OUQ', '1', '1', '33', '{\"type\":200993,\"parentId\":200993,\"displayType\": \"list\"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '4', NULL, '10', NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1149630', '999984', '0', '1', '240111044331055840', '/home', 'Bizs', '云服务', '云服务', 'cs://1/image/aW1hZ2UvTVRwaE16azVaREE0WmpkaFltRmhOVFJrTW1ZMU56WTFZVEJrTTJOa1lqRmlPQQ', '1', '1', '33', '{\"type\":200991,\"parentId\":200991,\"displayType\": \"list\"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '9', NULL, '10', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1149640', '999984', '0', '1', '240111044331055840', '/home', 'Bizs', '供应链', '供应链', 'cs://1/image/aW1hZ2UvTVRvMk5tWm1PVEl5TVRoak56WTNabU13T0RFMk9UWTFZVEl6TURjeFpURmpZZw', '1', '1', '33', '{\"type\":200992,\"parentId\":200992,\"displayType\": \"list\"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '9', NULL, '10', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1149650', '999984', '0', '1', '240111044331055840', '/home', 'Bizs', '产品测试', '产品测试', 'cs://1/image/aW1hZ2UvTVRwallXRTNNamcwTWpjMU1UaGpOMk14TURrNFlqSXpOamxtT0RRd09XWm1OUQ', '1', '1', '33', '{\"type\":200993,\"parentId\":200993,\"displayType\": \"list\"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '9', NULL, '10', NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1150451', '999984', '0', '1', '240111044331055835', '/home', 'Bizs', '云服务', '云服务', 'cs://1/image/aW1hZ2UvTVRwaE16azVaREE0WmpkaFltRmhOVFJrTW1ZMU56WTFZVEJrTTJOa1lqRmlPQQ', '1', '1', '33', '{\"type\":200991,\"parentId\":200991,\"displayType\": \"list\"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '4', NULL, '10', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1150461', '999984', '0', '1', '240111044331055835', '/home', 'Bizs', '供应链', '供应链', 'cs://1/image/aW1hZ2UvTVRvMk5tWm1PVEl5TVRoak56WTNabU13T0RFMk9UWTFZVEl6TURjeFpURmpZZw', '1', '1', '33', '{\"type\":200992,\"parentId\":200992,\"displayType\": \"list\"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '4', NULL, '10', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1150471', '999984', '0', '1', '240111044331055835', '/home', 'Bizs', '产品测试', '产品测试', 'cs://1/image/aW1hZ2UvTVRwallXRTNNamcwTWpjMU1UaGpOMk14TURrNFlqSXpOamxtT0RRd09XWm1OUQ', '1', '1', '33', '{\"type\":200993,\"parentId\":200993,\"displayType\": \"list\"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '4', NULL, '10', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1150631', '999984', '0', '1', '240111044331055835', '/home', 'Bizs', '云服务', '云服务', 'cs://1/image/aW1hZ2UvTVRwaE16azVaREE0WmpkaFltRmhOVFJrTW1ZMU56WTFZVEJrTTJOa1lqRmlPQQ', '1', '1', '33', '{\"type\":200991,\"parentId\":200991,\"displayType\": \"list\"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '9', NULL, '10', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1150641', '999984', '0', '1', '240111044331055835', '/home', 'Bizs', '供应链', '供应链', 'cs://1/image/aW1hZ2UvTVRvMk5tWm1PVEl5TVRoak56WTNabU13T0RFMk9UWTFZVEl6TURjeFpURmpZZw', '1', '1', '33', '{\"type\":200992,\"parentId\":200992,\"displayType\": \"list\"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '9', NULL, '10', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1150651', '999984', '0', '1', '240111044331055835', '/home', 'Bizs', '产品测试', '产品测试', 'cs://1/image/aW1hZ2UvTVRwallXRTNNamcwTWpjMU1UaGpOMk14TURrNFlqSXpOamxtT0RRd09XWm1OUQ', '1', '1', '33', '{\"type\":200993,\"parentId\":200993,\"displayType\": \"list\"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '9', NULL, '10', NULL);


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1149481', '999984', '0', '1', '240111044331055840', '/home', 'Bizs', '福利管家', '福利管家', 'cs://1/image/aW1hZ2UvTVRwbFptRm1NREl6WkdFM05tUmtZalZoTkRkaU9EWTJNREpoT0dKa1lXUm1aZw', '1', '1', '33', '{\"type\":200994,\"parentId\":200994,\"displayType\": \"list\"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '4', NULL, '10', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1149661', '999984', '0', '1', '240111044331055840', '/home', 'Bizs', '福利管家', '福利管家', 'cs://1/image/aW1hZ2UvTVRwbFptRm1NREl6WkdFM05tUmtZalZoTkRkaU9EWTJNREpoT0dKa1lXUm1aZw', '1', '1', '33', '{\"type\":200994,\"parentId\":200994,\"displayType\": \"list\"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '9', NULL, '10', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1149482', '999984', '0', '1', '240111044331055835', '/home', 'Bizs', '福利管家', '福利管家', 'cs://1/image/aW1hZ2UvTVRwbFptRm1NREl6WkdFM05tUmtZalZoTkRkaU9EWTJNREpoT0dKa1lXUm1aZw', '1', '1', '33', '{\"type\":200994,\"parentId\":200994,\"displayType\": \"list\"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '4', NULL, '10', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1149662', '999984', '0', '1', '240111044331055835', '/home', 'Bizs', '福利管家', '福利管家', 'cs://1/image/aW1hZ2UvTVRwbFptRm1NREl6WkdFM05tUmtZalZoTkRkaU9EWTJNREpoT0dKa1lXUm1aZw', '1', '1', '33', '{\"type\":200994,\"parentId\":200994,\"displayType\": \"list\"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '9', NULL, '10', NULL);


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1159341', '999984', '0', '1', '240111044331050378', '/home', 'Bizs', '清创空间', '清创空间', 'cs://1/image/aW1hZ2UvTVRvMk5XWXhZbVV4WlRSa01qbGhORGcwT0dFMk5qTmxOV016TlRnd00ySTRPQQ', '1', '1', '33', '{\"type\":200980,\"parentId\":200980,\"displayType\": \"list\"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '3', NULL, '0', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1159521', '999984', '0', '1', '240111044331050378', '/home', 'Bizs', '清创空间', '清创空间', 'cs://1/image/aW1hZ2UvTVRvMk5XWXhZbVV4WlRSa01qbGhORGcwT0dFMk5qTmxOV016TlRnd00ySTRPQQ', '1', '1', '33', '{\"type\":200980,\"parentId\":200980,\"displayType\": \"list\"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '8', NULL, '0', NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1159481', '999984', '0', '1', '240111044331050378', '/home', 'Bizs', '创业培训', '创业培训', 'cs://1/image/aW1hZ2UvTVRwbFptRm1NREl6WkdFM05tUmtZalZoTkRkaU9EWTJNREpoT0dKa1lXUm1aZw', '1', '1', '33', '{\"type\":200994,\"parentId\":200994,\"displayType\": \"list\"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '4', NULL, '10', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1159661', '999984', '0', '1', '240111044331050378', '/home', 'Bizs', '创业培训', '创业培训', 'cs://1/image/aW1hZ2UvTVRwbFptRm1NREl6WkdFM05tUmtZalZoTkRkaU9EWTJNREpoT0dKa1lXUm1aZw', '1', '1', '33', '{\"type\":200994,\"parentId\":200994,\"displayType\": \"list\"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '9', NULL, '10', NULL);


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1159751', '999984', '0', '1', '240111044331050378', '/home', 'Bizs', '场地费用', '场地费用', 'cs://1/image/aW1hZ2UvTVRwall6ZGxZVFUxWVRNNE1HWTBabUkzWlRrMk9UZGlOek13WldWbFl6ZGhZUQ', '1', '1', '13', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '5', NULL, '0', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1159761', '999984', '0', '1', '240111044331050378', '/home', 'Bizs', '场地费用', '场地费用', 'cs://1/image/aW1hZ2UvTVRwall6ZGxZVFUxWVRNNE1HWTBabUkzWlRrMk9UZGlOek13WldWbFl6ZGhZUQ', '1', '1', '13', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '10', NULL, '0', NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1115290', '999984', '0', '1', '240111044331050378', '/home', 'Bizs', '公寓租赁', '公寓租赁', 'cs://1/image/aW1hZ2UvTVRvMk5UTmpOalprWldSak1EZzVaVEkzTmpCbE1URXdNRFUwT1RCaVlXSTROUQ', '1', '1', '13', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '10', '0', '1', '0', NULL, '0', NULL, '', '', '1', 'park_tourist', '1', '10', NULL, '0', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1115291', '999984', '0', '1', '240111044331050378', '/home', 'Bizs', '公寓租赁', '公寓租赁', 'cs://1/image/aW1hZ2UvTVRvMk5UTmpOalprWldSak1EZzVaVEkzTmpCbE1URXdNRFUwT1RCaVlXSTROUQ', '1', '1', '13', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '10', '0', '1', '0', NULL, '0', NULL, '', '', '1', 'pm_admin', '1', '5', NULL, '0', NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1115293', '999984', '0', '1', '240111044331050378', '/home', 'Bizs', '共享汽车', '共享汽车', 'cs://1/image/aW1hZ2UvTVRvMk5UTmpOalprWldSak1EZzVaVEkzTmpCbE1URXdNRFUwT1RCaVlXSTROUQ', '1', '1', '13', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '10', '0', '1', '0', NULL, '0', NULL, '', '', '1', 'park_tourist', '1', '10', NULL, '0', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1115294', '999984', '0', '1', '240111044331050378', '/home', 'Bizs', '共享汽车', '共享汽车', 'cs://1/image/aW1hZ2UvTVRvMk5UTmpOalprWldSak1EZzVaVEkzTmpCbE1URXdNRFUwT1RCaVlXSTROUQ', '1', '1', '13', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '10', '0', '1', '0', NULL, '0', NULL, '', '', '1', 'pm_admin', '1', '5', NULL, '0', NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1159345', '999984', '0', '1', '240111044331050378', '/home', 'Bizs', '双清孵化', '双清孵化', 'cs://1/image/aW1hZ2UvTVRvMk5XWXhZbVV4WlRSa01qbGhORGcwT0dFMk5qTmxOV016TlRnd00ySTRPQQ', '1', '1', '33', '{\"type\":200980,\"parentId\":200980,\"displayType\": \"list\"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '3', NULL, '0', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1159525', '999984', '0', '1', '240111044331050378', '/home', 'Bizs', '双清孵化', '双清孵化', 'cs://1/image/aW1hZ2UvTVRvMk5XWXhZbVV4WlRSa01qbGhORGcwT0dFMk5qTmxOV016TlRnd00ySTRPQQ', '1', '1', '33', '{\"type\":200980,\"parentId\":200980,\"displayType\": \"list\"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '8', NULL, '0', NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1150456', '999984', '0', '1', '240111044331050379', '/home', 'Bizs', '创业工坊', '创业工坊', 'cs://1/image/aW1hZ2UvTVRwaE16azVaREE0WmpkaFltRmhOVFJrTW1ZMU56WTFZVEJrTTJOa1lqRmlPQQ', '1', '1', '33', '{\"type\":200991,\"parentId\":200991,\"displayType\": \"list\"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '4', NULL, '10', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1150466', '999984', '0', '1', '240111044331050379', '/home', 'Bizs', '供应链', '供应链', 'cs://1/image/aW1hZ2UvTVRvMk5tWm1PVEl5TVRoak56WTNabU13T0RFMk9UWTFZVEl6TURjeFpURmpZZw', '1', '1', '33', '{\"type\":200992,\"parentId\":200992,\"displayType\": \"list\"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '4', NULL, '10', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1150476', '999984', '0', '1', '240111044331050379', '/home', 'Bizs', '产品检测', '产品检测', 'cs://1/image/aW1hZ2UvTVRwallXRTNNamcwTWpjMU1UaGpOMk14TURrNFlqSXpOamxtT0RRd09XWm1OUQ', '1', '1', '33', '{\"type\":200993,\"parentId\":200993,\"displayType\": \"list\"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '4', NULL, '10', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1150636', '999984', '0', '1', '240111044331050379', '/home', 'Bizs', '创业工坊', '创业工坊', 'cs://1/image/aW1hZ2UvTVRwaE16azVaREE0WmpkaFltRmhOVFJrTW1ZMU56WTFZVEJrTTJOa1lqRmlPQQ', '1', '1', '33', '{\"type\":200991,\"parentId\":200991,\"displayType\": \"list\"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '9', NULL, '10', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1150646', '999984', '0', '1', '240111044331050379', '/home', 'Bizs', '供应链', '供应链', 'cs://1/image/aW1hZ2UvTVRvMk5tWm1PVEl5TVRoak56WTNabU13T0RFMk9UWTFZVEl6TURjeFpURmpZZw', '1', '1', '33', '{\"type\":200992,\"parentId\":200992,\"displayType\": \"list\"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '9', NULL, '10', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1150656', '999984', '0', '1', '240111044331050379', '/home', 'Bizs', '产品检测', '产品检测', 'cs://1/image/aW1hZ2UvTVRwallXRTNNamcwTWpjMU1UaGpOMk14TURrNFlqSXpOamxtT0RRd09XWm1OUQ', '1', '1', '33', '{\"type\":200993,\"parentId\":200993,\"displayType\": \"list\"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '9', NULL, '10', NULL);


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1159754', '999984', '0', '1', '240111044331050379', '/home', 'Bizs', '场地费用', '场地费用', 'cs://1/image/aW1hZ2UvTVRwall6ZGxZVFUxWVRNNE1HWTBabUkzWlRrMk9UZGlOek13WldWbFl6ZGhZUQ', '1', '1', '13', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '5', NULL, '0', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1159764', '999984', '0', '1', '240111044331050379', '/home', 'Bizs', '场地费用', '场地费用', 'cs://1/image/aW1hZ2UvTVRwall6ZGxZVFUxWVRNNE1HWTBabUkzWlRrMk9UZGlOek13WldWbFl6ZGhZUQ', '1', '1', '13', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '10', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '10', NULL, '0', NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1115297', '999984', '0', '1', '240111044331050379', '/home', 'Bizs', '公寓租赁', '公寓租赁', 'cs://1/image/aW1hZ2UvTVRvMk5UTmpOalprWldSak1EZzVaVEkzTmpCbE1URXdNRFUwT1RCaVlXSTROUQ', '1', '1', '13', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '10', '0', '1', '0', NULL, '0', NULL, '', '', '1', 'park_tourist', '1', '10', NULL, '0', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES ('1115298', '999984', '0', '1', '240111044331050379', '/home', 'Bizs', '公寓租赁', '公寓租赁', 'cs://1/image/aW1hZ2UvTVRvMk5UTmpOalprWldSak1EZzVaVEkzTmpCbE1URXdNRFUwT1RCaVlXSTROUQ', '1', '1', '13', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '10', '0', '1', '0', NULL, '0', NULL, '', '', '1', 'pm_admin', '1', '5', NULL, '0', NULL);

