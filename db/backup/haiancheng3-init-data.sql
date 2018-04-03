INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (233182  , UUID(), '9202001', '黄锡杨', 'cs://1/image/aW1hZ2UvTVRvMlkySmhNbVZqTm1SaU1UQXdPREkxWkRjME5HVmxNVFU1TXpBNE5UUTBZdw', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (228115 , 233182  ,  '0',  '13714562296',  '221616',  3, UTC_TIMESTAMP(), 999993);

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1004926, UUID(), '海岸大厦东座管理处', '海岸大厦东座管理处', 1, 1, 1004937, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183390, 1, 999993); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(183390, UUID(), 999993, 2, 'EhGroups', 1004926,'海岸大厦东座管理处','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());       
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(1004937, 1000631, 'PM', '海岸大厦东座管理处', '', '/1000631/1004937', 1, 2, 'ENTERPRISE', 999993, 1004926);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1113898, 240111044331054835, 'organization', 1004937, 3, 0, UTC_TIMESTAMP());

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(183102, UUID(), 999993, 2, 'EhGroups', 0,'海岸大厦东座论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(183103, UUID(), 999993, 2, 'EhGroups', 0,'海岸大厦东座意见反馈论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 

INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES(240111044331054835, UUID(), 14953, '深圳市',  14954, '南山区', '海岸大厦东座', '海岸大厦东座', '广东省深圳市南山区文心五路', NULL, '海岸大厦东座写字楼位于深圳市南山区后海新商业文化中心区，东邻凯宾斯基酒店，南接保利文化广场，项目占地面积1.69万平方米，总建筑面积8.57万平方米，其中商业面积约2万平方米。主体建筑包括24层高的主楼和14层高的附楼，主体建筑高度103米。大厦共有360户（其中写字楼211户，商铺149户）写字楼管理费为20元/平方米，商铺管理费为15元/平方米，2006年11月22日入伙，一、二层为商铺，三楼为商务酒店，四楼为管理处办公区域和商务会所，地下负一、负二层为停车场及大厦设备机房，其他楼层为办公用途。',
	NULL, NULL, NULL, NULL, NULL, NULL,NULL, 364, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'0', 183102, 183103, UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES(240111044331050438, 240111044331054835, '', 113.944794, 22.523969, 'ws100uk5dhhk');
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(1004937, 240111044331054835);
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, STATUS, `namespace_id`)
	VALUES(2111853, 1004937, 'USER', 233182  , 'manager', '黄锡杨', 0, '13714562296', 3, 999993);	

INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(11768, 'EhOrganizations', 1004937, 'EhUsers', 233182, 1001, 1, UTC_TIMESTAMP());
	
INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES(1574, 999993, 'COMMUNITY', 240111044331054835, UTC_TIMESTAMP());
	
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(180300, 240111044331054835, '海岸大厦东座', '海岸大厦东座', 0, NULL, '广东省深圳市南山区文心五路', 85700, NULL, NULL, NULL, '海岸大厦东座写字楼位于深圳市南山区后海新商业文化中心区，东邻凯宾斯基酒店，南接保利文化广场，项目占地面积1.69万平方米，总建筑面积8.57万平方米，其中商业面积约2万平方米。主体建筑包括24层高的主楼和14层高的附楼，主体建筑高度103米。大厦共有360户（其中写字楼211户，商铺149户）写字楼管理费为20元/平方米，商铺管理费为15元/平方米，2006年11月22日入伙，一、二层为商铺，三楼为商务酒店，四楼为管理处办公区域和商务会所，地下负一、负二层为停车场及大厦设备机房，其他楼层为办公用途。', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999993);


INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123865,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-101','海岸大厦东座','101','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123866,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-102','海岸大厦东座','102','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123867,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-103','海岸大厦东座','103','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123868,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-104','海岸大厦东座','104','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123869,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-105','海岸大厦东座','105','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123870,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-106','海岸大厦东座','106','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123871,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-107','海岸大厦东座','107','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123872,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-108','海岸大厦东座','108','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123873,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-109','海岸大厦东座','109','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123874,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-110','海岸大厦东座','110','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123875,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-111','海岸大厦东座','111','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123876,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-112','海岸大厦东座','112','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123877,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-113','海岸大厦东座','113','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123878,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-114','海岸大厦东座','114','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123879,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-115','海岸大厦东座','115','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123880,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-116','海岸大厦东座','116','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123881,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-117','海岸大厦东座','117','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123882,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-118','海岸大厦东座','118','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123883,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-119','海岸大厦东座','119','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123884,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-120','海岸大厦东座','120','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123885,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-121','海岸大厦东座','121','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123886,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-122','海岸大厦东座','122','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123887,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-123','海岸大厦东座','123','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123888,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-124','海岸大厦东座','124','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123889,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-125','海岸大厦东座','125','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123890,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-126','海岸大厦东座','126','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123891,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-127','海岸大厦东座','127','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123892,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-128','海岸大厦东座','128','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123893,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-129','海岸大厦东座','129','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123894,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-130','海岸大厦东座','130','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123895,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-131','海岸大厦东座','131','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123896,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-132','海岸大厦东座','132','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123897,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-133','海岸大厦东座','133','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123898,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-134','海岸大厦东座','134','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123899,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-135','海岸大厦东座','135','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123900,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-136','海岸大厦东座','136','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123901,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-137','海岸大厦东座','137','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123902,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-138','海岸大厦东座','138','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123903,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-139','海岸大厦东座','139','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123904,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-140','海岸大厦东座','140','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123905,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-141','海岸大厦东座','141','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123906,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-142','海岸大厦东座','142','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123907,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-143','海岸大厦东座','143','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123908,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-144','海岸大厦东座','144','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123909,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-145','海岸大厦东座','145','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123910,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-146','海岸大厦东座','146','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123911,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-147','海岸大厦东座','147','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123912,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-148','海岸大厦东座','148','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123913,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-149','海岸大厦东座','149','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123914,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-150','海岸大厦东座','150','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123915,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-151','海岸大厦东座','151','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123916,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-152','海岸大厦东座','152','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123917,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-153','海岸大厦东座','153','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123918,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-154','海岸大厦东座','154','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123919,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-155','海岸大厦东座','155','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123920,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-156','海岸大厦东座','156','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123921,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-157','海岸大厦东座','157','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123922,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-158','海岸大厦东座','158','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123923,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-159','海岸大厦东座','159','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123924,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-160','海岸大厦东座','160','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123925,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-161','海岸大厦东座','161','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123926,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-162','海岸大厦东座','162','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123927,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-163','海岸大厦东座','163','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123928,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-164','海岸大厦东座','164','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123929,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-165','海岸大厦东座','165','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123930,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-166','海岸大厦东座','166','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123931,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-167','海岸大厦东座','167','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123932,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-168','海岸大厦东座','168','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123933,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-169','海岸大厦东座','169','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123934,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-170','海岸大厦东座','170','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123935,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-171','海岸大厦东座','171','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123936,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-172','海岸大厦东座','172','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123937,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-173','海岸大厦东座','173','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123938,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-174','海岸大厦东座','174','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123939,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-175','海岸大厦东座','175','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123940,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-176','海岸大厦东座','176','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123941,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-177','海岸大厦东座','177','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123942,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-178','海岸大厦东座','178','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123943,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-201','海岸大厦东座','201','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123944,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-202','海岸大厦东座','202','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123945,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-203','海岸大厦东座','203','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123946,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-204','海岸大厦东座','204','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123947,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-205','海岸大厦东座','205','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123948,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-206','海岸大厦东座','206','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123949,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-207','海岸大厦东座','207','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123950,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-208','海岸大厦东座','208','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123951,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-209','海岸大厦东座','209','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123952,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-210','海岸大厦东座','210','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123953,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-211','海岸大厦东座','211','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123954,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-212','海岸大厦东座','212','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123955,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-213','海岸大厦东座','213','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123956,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-214','海岸大厦东座','214','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123957,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-215','海岸大厦东座','215','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123958,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-216','海岸大厦东座','216','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123959,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-217','海岸大厦东座','217','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123960,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-218','海岸大厦东座','218','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123961,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-219','海岸大厦东座','219','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123962,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-220','海岸大厦东座','220','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123963,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-221','海岸大厦东座','221','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123964,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-222','海岸大厦东座','222','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123965,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-223','海岸大厦东座','223','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123966,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-224','海岸大厦东座','224','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123967,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-225','海岸大厦东座','225','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123968,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-226','海岸大厦东座','226','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123969,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-227','海岸大厦东座','227','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123970,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-228','海岸大厦东座','228','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123971,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-229','海岸大厦东座','229','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123972,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-230','海岸大厦东座','230','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123973,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-231','海岸大厦东座','231','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123974,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-232','海岸大厦东座','232','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123975,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-233','海岸大厦东座','233','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123976,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-234','海岸大厦东座','234','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123977,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-235','海岸大厦东座','235','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123978,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-236','海岸大厦东座','236','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123979,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-237','海岸大厦东座','237','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123980,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-238','海岸大厦东座','238','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123981,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-239','海岸大厦东座','239','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123982,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-240','海岸大厦东座','240','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123983,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-241','海岸大厦东座','241','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123984,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-242','海岸大厦东座','242','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123985,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-243','海岸大厦东座','243','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123986,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-244','海岸大厦东座','244','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123987,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-245','海岸大厦东座','245','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123988,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-246','海岸大厦东座','246','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123989,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-247','海岸大厦东座','247','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123990,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-248','海岸大厦东座','248','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123991,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-249','海岸大厦东座','249','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123992,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-250','海岸大厦东座','250','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123993,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-301','海岸大厦东座','301','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123994,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-302','海岸大厦东座','302','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123995,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-303','海岸大厦东座','303','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123996,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-304','海岸大厦东座','304','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123997,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-305','海岸大厦东座','305','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123998,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-306','海岸大厦东座','306','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387123999,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-307','海岸大厦东座','307','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124000,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-308','海岸大厦东座','308','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124001,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-309','海岸大厦东座','309','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124002,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-310','海岸大厦东座','310','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124003,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-311','海岸大厦东座','311','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124004,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-312','海岸大厦东座','312','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124005,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-313','海岸大厦东座','313','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124006,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-314','海岸大厦东座','314','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124007,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-315','海岸大厦东座','315','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124008,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-316','海岸大厦东座','316','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124009,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-317','海岸大厦东座','317','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124010,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-318','海岸大厦东座','318','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124011,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-319','海岸大厦东座','319','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124012,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-320','海岸大厦东座','320','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124013,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-321','海岸大厦东座','321','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124014,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-501','海岸大厦东座','501','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124015,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-502','海岸大厦东座','502','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124016,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-503','海岸大厦东座','503','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124017,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-504','海岸大厦东座','504','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124018,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-505','海岸大厦东座','505','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124019,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-506','海岸大厦东座','506','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124020,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-507','海岸大厦东座','507','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124021,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-508','海岸大厦东座','508','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124022,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-509','海岸大厦东座','509','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124023,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-510','海岸大厦东座','510','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124024,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-511','海岸大厦东座','511','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124025,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-512','海岸大厦东座','512','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124026,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-513','海岸大厦东座','513','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124027,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-514','海岸大厦东座','514','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124028,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-515','海岸大厦东座','515','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124029,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-601','海岸大厦东座','601','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124030,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-602','海岸大厦东座','602','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124031,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-603','海岸大厦东座','603','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124032,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-604','海岸大厦东座','604','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124033,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-605','海岸大厦东座','605','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124034,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-606','海岸大厦东座','606','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124035,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-607','海岸大厦东座','607','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124036,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-608','海岸大厦东座','608','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124037,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-609','海岸大厦东座','609','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124038,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-610','海岸大厦东座','610','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124039,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-611','海岸大厦东座','611','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124040,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-612','海岸大厦东座','612','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124041,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-613','海岸大厦东座','613','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124042,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-701','海岸大厦东座','701','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124043,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-702','海岸大厦东座','702','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124044,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-703','海岸大厦东座','703','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124045,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-704','海岸大厦东座','704','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124046,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-705','海岸大厦东座','705','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124047,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-706','海岸大厦东座','706','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124048,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-707','海岸大厦东座','707','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124049,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-708','海岸大厦东座','708','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124050,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-709','海岸大厦东座','709','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124051,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-710','海岸大厦东座','710','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124052,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-711','海岸大厦东座','711','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124053,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-712','海岸大厦东座','712','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124054,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-713','海岸大厦东座','713','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124055,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-714','海岸大厦东座','714','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124056,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-715','海岸大厦东座','715','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124057,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-716','海岸大厦东座','716','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124058,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-801','海岸大厦东座','801','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124059,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-802','海岸大厦东座','802','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124060,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-803','海岸大厦东座','803','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124061,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-804','海岸大厦东座','804','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124062,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-805','海岸大厦东座','805','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124063,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-806','海岸大厦东座','806','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124064,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-807','海岸大厦东座','807','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124065,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-808','海岸大厦东座','808','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124066,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-809','海岸大厦东座','809','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124067,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-810','海岸大厦东座','810','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124068,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-811','海岸大厦东座','811','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124069,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-812','海岸大厦东座','812','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124070,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-813','海岸大厦东座','813','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124071,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-901','海岸大厦东座','901','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124072,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-902','海岸大厦东座','902','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124073,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-903','海岸大厦东座','903','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124074,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-904','海岸大厦东座','904','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124075,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-905','海岸大厦东座','905','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124076,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-906','海岸大厦东座','906','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124077,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-907','海岸大厦东座','907','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124078,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-908','海岸大厦东座','908','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124079,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-909','海岸大厦东座','909','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124080,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-910','海岸大厦东座','910','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124081,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-911','海岸大厦东座','911','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124082,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-912','海岸大厦东座','912','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124083,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-913','海岸大厦东座','913','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124084,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-914','海岸大厦东座','914','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124085,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-915','海岸大厦东座','915','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124086,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-916','海岸大厦东座','916','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124087,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-917','海岸大厦东座','917','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124088,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1001','海岸大厦东座','1001','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124089,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1002','海岸大厦东座','1002','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124090,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1003','海岸大厦东座','1003','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124091,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1004','海岸大厦东座','1004','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124092,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1005','海岸大厦东座','1005','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124093,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1006','海岸大厦东座','1006','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124094,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1007','海岸大厦东座','1007','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124095,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1008','海岸大厦东座','1008','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124096,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1009','海岸大厦东座','1009','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124097,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1010','海岸大厦东座','1010','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124098,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1011','海岸大厦东座','1011','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124099,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1012','海岸大厦东座','1012','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124100,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1013','海岸大厦东座','1013','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124101,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1101','海岸大厦东座','1101','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124102,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1102','海岸大厦东座','1102','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124103,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1103','海岸大厦东座','1103','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124104,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1104','海岸大厦东座','1104','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124105,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1105','海岸大厦东座','1105','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124106,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1106','海岸大厦东座','1106','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124107,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1107','海岸大厦东座','1107','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124108,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1108','海岸大厦东座','1108','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124109,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1109','海岸大厦东座','1109','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124110,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1110','海岸大厦东座','1110','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124111,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1111','海岸大厦东座','1111','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124112,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1112','海岸大厦东座','1112','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124113,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1113','海岸大厦东座','1113','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124114,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1114','海岸大厦东座','1114','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124115,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1115','海岸大厦东座','1115','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124116,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1116','海岸大厦东座','1116','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124117,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1117','海岸大厦东座','1117','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124118,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1201','海岸大厦东座','1201','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124119,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1202','海岸大厦东座','1202','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124120,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1203','海岸大厦东座','1203','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124121,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1204','海岸大厦东座','1204','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124122,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1205','海岸大厦东座','1205','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124123,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1206','海岸大厦东座','1206','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124124,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1207','海岸大厦东座','1207','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124125,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1208','海岸大厦东座','1208','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124126,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1209','海岸大厦东座','1209','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124127,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1210','海岸大厦东座','1210','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124128,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1211','海岸大厦东座','1211','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124129,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1212','海岸大厦东座','1212','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124130,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1213A','海岸大厦东座','1213A','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124131,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1213B','海岸大厦东座','1213B','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124132,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1301','海岸大厦东座','1301','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124133,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1302','海岸大厦东座','1302','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124134,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1303','海岸大厦东座','1303','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124135,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1304','海岸大厦东座','1304','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124136,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1305','海岸大厦东座','1305','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124137,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1306','海岸大厦东座','1306','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124138,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1307','海岸大厦东座','1307','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124139,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1308','海岸大厦东座','1308','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124140,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1309','海岸大厦东座','1309','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124141,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1310','海岸大厦东座','1310','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124142,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1311','海岸大厦东座','1311','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124143,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1312','海岸大厦东座','1312','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124144,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1313','海岸大厦东座','1313','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124145,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1315','海岸大厦东座','1315','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124146,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1316','海岸大厦东座','1316','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124147,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1317','海岸大厦东座','1317','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124148,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1318','海岸大厦东座','1318','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124149,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1401','海岸大厦东座','1401','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124150,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1402','海岸大厦东座','1402','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124151,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1403','海岸大厦东座','1403','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124152,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1404','海岸大厦东座','1404','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124153,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1405','海岸大厦东座','1405','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124154,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1406','海岸大厦东座','1406','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124155,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1407','海岸大厦东座','1407','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124156,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1408','海岸大厦东座','1408','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124157,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1409','海岸大厦东座','1409','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124158,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1410','海岸大厦东座','1410','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124159,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1411','海岸大厦东座','1411','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124160,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1412','海岸大厦东座','1412','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124161,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1501','海岸大厦东座','1501','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124162,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1502','海岸大厦东座','1502','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124163,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1503','海岸大厦东座','1503','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124164,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1504','海岸大厦东座','1504','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124165,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1505','海岸大厦东座','1505','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124166,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1506','海岸大厦东座','1506','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124167,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1507','海岸大厦东座','1507','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124168,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1508','海岸大厦东座','1508','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124169,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1509','海岸大厦东座','1509','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124170,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1601','海岸大厦东座','1601','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124171,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1602','海岸大厦东座','1602','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124172,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1603','海岸大厦东座','1603','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124173,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1604','海岸大厦东座','1604','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124174,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1605A','海岸大厦东座','1605A','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124175,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1605B','海岸大厦东座','1605B','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124176,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1606','海岸大厦东座','1606','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124177,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1607','海岸大厦东座','1607','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124178,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1608A','海岸大厦东座','1608A','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124179,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1608B','海岸大厦东座','1608B','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124180,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1701','海岸大厦东座','1701','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124181,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1702','海岸大厦东座','1702','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124182,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1703','海岸大厦东座','1703','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124183,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1704','海岸大厦东座','1704','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124184,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1705','海岸大厦东座','1705','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124185,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1706','海岸大厦东座','1706','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124186,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1801','海岸大厦东座','1801','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124187,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1802','海岸大厦东座','1802','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124188,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1803','海岸大厦东座','1803','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124189,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1804','海岸大厦东座','1804','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124190,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1805','海岸大厦东座','1805','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124191,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1806','海岸大厦东座','1806','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124192,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1901','海岸大厦东座','1901','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124193,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1902','海岸大厦东座','1902','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124194,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1903','海岸大厦东座','1903','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124195,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1904','海岸大厦东座','1904','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124196,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1905','海岸大厦东座','1905','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124197,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-1906','海岸大厦东座','1906','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124198,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2001','海岸大厦东座','2001','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124199,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2002','海岸大厦东座','2002','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124200,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2003','海岸大厦东座','2003','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124201,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2004','海岸大厦东座','2004','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124202,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2005','海岸大厦东座','2005','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124203,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2006','海岸大厦东座','2006','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124204,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2101','海岸大厦东座','2101','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124205,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2102','海岸大厦东座','2102','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124206,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2103','海岸大厦东座','2103','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124207,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2104','海岸大厦东座','2104','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124208,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2105','海岸大厦东座','2105','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124209,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2106','海岸大厦东座','2106','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124210,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2201','海岸大厦东座','2201','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124211,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2202','海岸大厦东座','2202','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124212,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2203','海岸大厦东座','2203','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124213,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2204','海岸大厦东座','2204','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124214,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2205','海岸大厦东座','2205','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124215,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2206','海岸大厦东座','2206','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124216,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2301','海岸大厦东座','2301','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124217,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2302','海岸大厦东座','2302','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124218,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2303','海岸大厦东座','2303','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124219,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2303B','海岸大厦东座','2303B','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124220,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2304','海岸大厦东座','2304','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124221,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2305','海岸大厦东座','2305','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124222,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2306','海岸大厦东座','2306','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124223,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2401','海岸大厦东座','2401','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124224,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2402','海岸大厦东座','2402','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124225,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2403','海岸大厦东座','2403','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124226,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2404','海岸大厦东座','2404','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124227,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2405','海岸大厦东座','2405','2','0',UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387124228,UUID(),240111044331054835, 14953, '深圳市',  14954, '南山区' ,'海岸大厦东座-2406','海岸大厦东座','2406','2','0',UTC_TIMESTAMP(), 999993);

INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23331, 1004937, 240111044331054835, 239825274387123865, '海岸大厦东座-101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23332, 1004937, 240111044331054835, 239825274387123866, '海岸大厦东座-102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23333, 1004937, 240111044331054835, 239825274387123867, '海岸大厦东座-103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23334, 1004937, 240111044331054835, 239825274387123868, '海岸大厦东座-104', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23335, 1004937, 240111044331054835, 239825274387123869, '海岸大厦东座-105', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23336, 1004937, 240111044331054835, 239825274387123870, '海岸大厦东座-106', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23337, 1004937, 240111044331054835, 239825274387123871, '海岸大厦东座-107', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23338, 1004937, 240111044331054835, 239825274387123872, '海岸大厦东座-108', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23339, 1004937, 240111044331054835, 239825274387123873, '海岸大厦东座-109', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23340, 1004937, 240111044331054835, 239825274387123874, '海岸大厦东座-110', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23341, 1004937, 240111044331054835, 239825274387123875, '海岸大厦东座-111', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23342, 1004937, 240111044331054835, 239825274387123876, '海岸大厦东座-112', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23343, 1004937, 240111044331054835, 239825274387123877, '海岸大厦东座-113', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23344, 1004937, 240111044331054835, 239825274387123878, '海岸大厦东座-114', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23345, 1004937, 240111044331054835, 239825274387123879, '海岸大厦东座-115', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23346, 1004937, 240111044331054835, 239825274387123880, '海岸大厦东座-116', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23347, 1004937, 240111044331054835, 239825274387123881, '海岸大厦东座-117', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23348, 1004937, 240111044331054835, 239825274387123882, '海岸大厦东座-118', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23349, 1004937, 240111044331054835, 239825274387123883, '海岸大厦东座-119', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23350, 1004937, 240111044331054835, 239825274387123884, '海岸大厦东座-120', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23351, 1004937, 240111044331054835, 239825274387123885, '海岸大厦东座-121', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23352, 1004937, 240111044331054835, 239825274387123886, '海岸大厦东座-122', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23353, 1004937, 240111044331054835, 239825274387123887, '海岸大厦东座-123', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23354, 1004937, 240111044331054835, 239825274387123888, '海岸大厦东座-124', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23355, 1004937, 240111044331054835, 239825274387123889, '海岸大厦东座-125', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23356, 1004937, 240111044331054835, 239825274387123890, '海岸大厦东座-126', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23357, 1004937, 240111044331054835, 239825274387123891, '海岸大厦东座-127', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23358, 1004937, 240111044331054835, 239825274387123892, '海岸大厦东座-128', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23359, 1004937, 240111044331054835, 239825274387123893, '海岸大厦东座-129', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23360, 1004937, 240111044331054835, 239825274387123894, '海岸大厦东座-130', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23361, 1004937, 240111044331054835, 239825274387123895, '海岸大厦东座-131', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23362, 1004937, 240111044331054835, 239825274387123896, '海岸大厦东座-132', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23363, 1004937, 240111044331054835, 239825274387123897, '海岸大厦东座-133', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23364, 1004937, 240111044331054835, 239825274387123898, '海岸大厦东座-134', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23365, 1004937, 240111044331054835, 239825274387123899, '海岸大厦东座-135', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23366, 1004937, 240111044331054835, 239825274387123900, '海岸大厦东座-136', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23367, 1004937, 240111044331054835, 239825274387123901, '海岸大厦东座-137', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23368, 1004937, 240111044331054835, 239825274387123902, '海岸大厦东座-138', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23369, 1004937, 240111044331054835, 239825274387123903, '海岸大厦东座-139', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23370, 1004937, 240111044331054835, 239825274387123904, '海岸大厦东座-140', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23371, 1004937, 240111044331054835, 239825274387123905, '海岸大厦东座-141', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23372, 1004937, 240111044331054835, 239825274387123906, '海岸大厦东座-142', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23373, 1004937, 240111044331054835, 239825274387123907, '海岸大厦东座-143', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23374, 1004937, 240111044331054835, 239825274387123908, '海岸大厦东座-144', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23375, 1004937, 240111044331054835, 239825274387123909, '海岸大厦东座-145', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23376, 1004937, 240111044331054835, 239825274387123910, '海岸大厦东座-146', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23377, 1004937, 240111044331054835, 239825274387123911, '海岸大厦东座-147', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23378, 1004937, 240111044331054835, 239825274387123912, '海岸大厦东座-148', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23379, 1004937, 240111044331054835, 239825274387123913, '海岸大厦东座-149', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23380, 1004937, 240111044331054835, 239825274387123914, '海岸大厦东座-150', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23381, 1004937, 240111044331054835, 239825274387123915, '海岸大厦东座-151', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23382, 1004937, 240111044331054835, 239825274387123916, '海岸大厦东座-152', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23383, 1004937, 240111044331054835, 239825274387123917, '海岸大厦东座-153', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23384, 1004937, 240111044331054835, 239825274387123918, '海岸大厦东座-154', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23385, 1004937, 240111044331054835, 239825274387123919, '海岸大厦东座-155', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23386, 1004937, 240111044331054835, 239825274387123920, '海岸大厦东座-156', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23387, 1004937, 240111044331054835, 239825274387123921, '海岸大厦东座-157', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23388, 1004937, 240111044331054835, 239825274387123922, '海岸大厦东座-158', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23389, 1004937, 240111044331054835, 239825274387123923, '海岸大厦东座-159', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23390, 1004937, 240111044331054835, 239825274387123924, '海岸大厦东座-160', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23391, 1004937, 240111044331054835, 239825274387123925, '海岸大厦东座-161', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23392, 1004937, 240111044331054835, 239825274387123926, '海岸大厦东座-162', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23393, 1004937, 240111044331054835, 239825274387123927, '海岸大厦东座-163', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23394, 1004937, 240111044331054835, 239825274387123928, '海岸大厦东座-164', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23395, 1004937, 240111044331054835, 239825274387123929, '海岸大厦东座-165', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23396, 1004937, 240111044331054835, 239825274387123930, '海岸大厦东座-166', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23397, 1004937, 240111044331054835, 239825274387123931, '海岸大厦东座-167', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23398, 1004937, 240111044331054835, 239825274387123932, '海岸大厦东座-168', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23399, 1004937, 240111044331054835, 239825274387123933, '海岸大厦东座-169', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23400, 1004937, 240111044331054835, 239825274387123934, '海岸大厦东座-170', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23401, 1004937, 240111044331054835, 239825274387123935, '海岸大厦东座-171', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23402, 1004937, 240111044331054835, 239825274387123936, '海岸大厦东座-172', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23403, 1004937, 240111044331054835, 239825274387123937, '海岸大厦东座-173', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23404, 1004937, 240111044331054835, 239825274387123938, '海岸大厦东座-174', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23405, 1004937, 240111044331054835, 239825274387123939, '海岸大厦东座-175', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23406, 1004937, 240111044331054835, 239825274387123940, '海岸大厦东座-176', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23407, 1004937, 240111044331054835, 239825274387123941, '海岸大厦东座-177', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23408, 1004937, 240111044331054835, 239825274387123942, '海岸大厦东座-178', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23409, 1004937, 240111044331054835, 239825274387123943, '海岸大厦东座-201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23410, 1004937, 240111044331054835, 239825274387123944, '海岸大厦东座-202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23411, 1004937, 240111044331054835, 239825274387123945, '海岸大厦东座-203', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23412, 1004937, 240111044331054835, 239825274387123946, '海岸大厦东座-204', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23413, 1004937, 240111044331054835, 239825274387123947, '海岸大厦东座-205', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23414, 1004937, 240111044331054835, 239825274387123948, '海岸大厦东座-206', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23415, 1004937, 240111044331054835, 239825274387123949, '海岸大厦东座-207', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23416, 1004937, 240111044331054835, 239825274387123950, '海岸大厦东座-208', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23417, 1004937, 240111044331054835, 239825274387123951, '海岸大厦东座-209', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23418, 1004937, 240111044331054835, 239825274387123952, '海岸大厦东座-210', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23419, 1004937, 240111044331054835, 239825274387123953, '海岸大厦东座-211', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23420, 1004937, 240111044331054835, 239825274387123954, '海岸大厦东座-212', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23421, 1004937, 240111044331054835, 239825274387123955, '海岸大厦东座-213', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23422, 1004937, 240111044331054835, 239825274387123956, '海岸大厦东座-214', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23423, 1004937, 240111044331054835, 239825274387123957, '海岸大厦东座-215', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23424, 1004937, 240111044331054835, 239825274387123958, '海岸大厦东座-216', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23425, 1004937, 240111044331054835, 239825274387123959, '海岸大厦东座-217', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23426, 1004937, 240111044331054835, 239825274387123960, '海岸大厦东座-218', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23427, 1004937, 240111044331054835, 239825274387123961, '海岸大厦东座-219', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23428, 1004937, 240111044331054835, 239825274387123962, '海岸大厦东座-220', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23429, 1004937, 240111044331054835, 239825274387123963, '海岸大厦东座-221', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23430, 1004937, 240111044331054835, 239825274387123964, '海岸大厦东座-222', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23431, 1004937, 240111044331054835, 239825274387123965, '海岸大厦东座-223', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23432, 1004937, 240111044331054835, 239825274387123966, '海岸大厦东座-224', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23433, 1004937, 240111044331054835, 239825274387123967, '海岸大厦东座-225', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23434, 1004937, 240111044331054835, 239825274387123968, '海岸大厦东座-226', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23435, 1004937, 240111044331054835, 239825274387123969, '海岸大厦东座-227', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23436, 1004937, 240111044331054835, 239825274387123970, '海岸大厦东座-228', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23437, 1004937, 240111044331054835, 239825274387123971, '海岸大厦东座-229', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23438, 1004937, 240111044331054835, 239825274387123972, '海岸大厦东座-230', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23439, 1004937, 240111044331054835, 239825274387123973, '海岸大厦东座-231', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23440, 1004937, 240111044331054835, 239825274387123974, '海岸大厦东座-232', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23441, 1004937, 240111044331054835, 239825274387123975, '海岸大厦东座-233', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23442, 1004937, 240111044331054835, 239825274387123976, '海岸大厦东座-234', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23443, 1004937, 240111044331054835, 239825274387123977, '海岸大厦东座-235', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23444, 1004937, 240111044331054835, 239825274387123978, '海岸大厦东座-236', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23445, 1004937, 240111044331054835, 239825274387123979, '海岸大厦东座-237', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23446, 1004937, 240111044331054835, 239825274387123980, '海岸大厦东座-238', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23447, 1004937, 240111044331054835, 239825274387123981, '海岸大厦东座-239', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23448, 1004937, 240111044331054835, 239825274387123982, '海岸大厦东座-240', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23449, 1004937, 240111044331054835, 239825274387123983, '海岸大厦东座-241', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23450, 1004937, 240111044331054835, 239825274387123984, '海岸大厦东座-242', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23451, 1004937, 240111044331054835, 239825274387123985, '海岸大厦东座-243', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23452, 1004937, 240111044331054835, 239825274387123986, '海岸大厦东座-244', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23453, 1004937, 240111044331054835, 239825274387123987, '海岸大厦东座-245', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23454, 1004937, 240111044331054835, 239825274387123988, '海岸大厦东座-246', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23455, 1004937, 240111044331054835, 239825274387123989, '海岸大厦东座-247', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23456, 1004937, 240111044331054835, 239825274387123990, '海岸大厦东座-248', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23457, 1004937, 240111044331054835, 239825274387123991, '海岸大厦东座-249', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23458, 1004937, 240111044331054835, 239825274387123992, '海岸大厦东座-250', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23459, 1004937, 240111044331054835, 239825274387123993, '海岸大厦东座-301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23460, 1004937, 240111044331054835, 239825274387123994, '海岸大厦东座-302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23461, 1004937, 240111044331054835, 239825274387123995, '海岸大厦东座-303', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23462, 1004937, 240111044331054835, 239825274387123996, '海岸大厦东座-304', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23463, 1004937, 240111044331054835, 239825274387123997, '海岸大厦东座-305', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23464, 1004937, 240111044331054835, 239825274387123998, '海岸大厦东座-306', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23465, 1004937, 240111044331054835, 239825274387123999, '海岸大厦东座-307', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23466, 1004937, 240111044331054835, 239825274387124000, '海岸大厦东座-308', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23467, 1004937, 240111044331054835, 239825274387124001, '海岸大厦东座-309', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23468, 1004937, 240111044331054835, 239825274387124002, '海岸大厦东座-310', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23469, 1004937, 240111044331054835, 239825274387124003, '海岸大厦东座-311', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23470, 1004937, 240111044331054835, 239825274387124004, '海岸大厦东座-312', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23471, 1004937, 240111044331054835, 239825274387124005, '海岸大厦东座-313', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23472, 1004937, 240111044331054835, 239825274387124006, '海岸大厦东座-314', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23473, 1004937, 240111044331054835, 239825274387124007, '海岸大厦东座-315', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23474, 1004937, 240111044331054835, 239825274387124008, '海岸大厦东座-316', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23475, 1004937, 240111044331054835, 239825274387124009, '海岸大厦东座-317', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23476, 1004937, 240111044331054835, 239825274387124010, '海岸大厦东座-318', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23477, 1004937, 240111044331054835, 239825274387124011, '海岸大厦东座-319', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23478, 1004937, 240111044331054835, 239825274387124012, '海岸大厦东座-320', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23479, 1004937, 240111044331054835, 239825274387124013, '海岸大厦东座-321', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23480, 1004937, 240111044331054835, 239825274387124014, '海岸大厦东座-501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23481, 1004937, 240111044331054835, 239825274387124015, '海岸大厦东座-502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23482, 1004937, 240111044331054835, 239825274387124016, '海岸大厦东座-503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23483, 1004937, 240111044331054835, 239825274387124017, '海岸大厦东座-504', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23484, 1004937, 240111044331054835, 239825274387124018, '海岸大厦东座-505', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23485, 1004937, 240111044331054835, 239825274387124019, '海岸大厦东座-506', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23486, 1004937, 240111044331054835, 239825274387124020, '海岸大厦东座-507', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23487, 1004937, 240111044331054835, 239825274387124021, '海岸大厦东座-508', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23488, 1004937, 240111044331054835, 239825274387124022, '海岸大厦东座-509', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23489, 1004937, 240111044331054835, 239825274387124023, '海岸大厦东座-510', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23490, 1004937, 240111044331054835, 239825274387124024, '海岸大厦东座-511', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23491, 1004937, 240111044331054835, 239825274387124025, '海岸大厦东座-512', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23492, 1004937, 240111044331054835, 239825274387124026, '海岸大厦东座-513', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23493, 1004937, 240111044331054835, 239825274387124027, '海岸大厦东座-514', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23494, 1004937, 240111044331054835, 239825274387124028, '海岸大厦东座-515', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23495, 1004937, 240111044331054835, 239825274387124029, '海岸大厦东座-601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23496, 1004937, 240111044331054835, 239825274387124030, '海岸大厦东座-602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23497, 1004937, 240111044331054835, 239825274387124031, '海岸大厦东座-603', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23498, 1004937, 240111044331054835, 239825274387124032, '海岸大厦东座-604', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23499, 1004937, 240111044331054835, 239825274387124033, '海岸大厦东座-605', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23500, 1004937, 240111044331054835, 239825274387124034, '海岸大厦东座-606', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23501, 1004937, 240111044331054835, 239825274387124035, '海岸大厦东座-607', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23502, 1004937, 240111044331054835, 239825274387124036, '海岸大厦东座-608', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23503, 1004937, 240111044331054835, 239825274387124037, '海岸大厦东座-609', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23504, 1004937, 240111044331054835, 239825274387124038, '海岸大厦东座-610', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23505, 1004937, 240111044331054835, 239825274387124039, '海岸大厦东座-611', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23506, 1004937, 240111044331054835, 239825274387124040, '海岸大厦东座-612', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23507, 1004937, 240111044331054835, 239825274387124041, '海岸大厦东座-613', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23508, 1004937, 240111044331054835, 239825274387124042, '海岸大厦东座-701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23509, 1004937, 240111044331054835, 239825274387124043, '海岸大厦东座-702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23510, 1004937, 240111044331054835, 239825274387124044, '海岸大厦东座-703', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23511, 1004937, 240111044331054835, 239825274387124045, '海岸大厦东座-704', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23512, 1004937, 240111044331054835, 239825274387124046, '海岸大厦东座-705', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23513, 1004937, 240111044331054835, 239825274387124047, '海岸大厦东座-706', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23514, 1004937, 240111044331054835, 239825274387124048, '海岸大厦东座-707', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23515, 1004937, 240111044331054835, 239825274387124049, '海岸大厦东座-708', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23516, 1004937, 240111044331054835, 239825274387124050, '海岸大厦东座-709', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23517, 1004937, 240111044331054835, 239825274387124051, '海岸大厦东座-710', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23518, 1004937, 240111044331054835, 239825274387124052, '海岸大厦东座-711', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23519, 1004937, 240111044331054835, 239825274387124053, '海岸大厦东座-712', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23520, 1004937, 240111044331054835, 239825274387124054, '海岸大厦东座-713', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23521, 1004937, 240111044331054835, 239825274387124055, '海岸大厦东座-714', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23522, 1004937, 240111044331054835, 239825274387124056, '海岸大厦东座-715', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23523, 1004937, 240111044331054835, 239825274387124057, '海岸大厦东座-716', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23524, 1004937, 240111044331054835, 239825274387124058, '海岸大厦东座-801', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23525, 1004937, 240111044331054835, 239825274387124059, '海岸大厦东座-802', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23526, 1004937, 240111044331054835, 239825274387124060, '海岸大厦东座-803', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23527, 1004937, 240111044331054835, 239825274387124061, '海岸大厦东座-804', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23528, 1004937, 240111044331054835, 239825274387124062, '海岸大厦东座-805', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23529, 1004937, 240111044331054835, 239825274387124063, '海岸大厦东座-806', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23530, 1004937, 240111044331054835, 239825274387124064, '海岸大厦东座-807', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23531, 1004937, 240111044331054835, 239825274387124065, '海岸大厦东座-808', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23532, 1004937, 240111044331054835, 239825274387124066, '海岸大厦东座-809', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23533, 1004937, 240111044331054835, 239825274387124067, '海岸大厦东座-810', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23534, 1004937, 240111044331054835, 239825274387124068, '海岸大厦东座-811', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23535, 1004937, 240111044331054835, 239825274387124069, '海岸大厦东座-812', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23536, 1004937, 240111044331054835, 239825274387124070, '海岸大厦东座-813', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23537, 1004937, 240111044331054835, 239825274387124071, '海岸大厦东座-901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23538, 1004937, 240111044331054835, 239825274387124072, '海岸大厦东座-902', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23539, 1004937, 240111044331054835, 239825274387124073, '海岸大厦东座-903', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23540, 1004937, 240111044331054835, 239825274387124074, '海岸大厦东座-904', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23541, 1004937, 240111044331054835, 239825274387124075, '海岸大厦东座-905', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23542, 1004937, 240111044331054835, 239825274387124076, '海岸大厦东座-906', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23543, 1004937, 240111044331054835, 239825274387124077, '海岸大厦东座-907', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23544, 1004937, 240111044331054835, 239825274387124078, '海岸大厦东座-908', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23545, 1004937, 240111044331054835, 239825274387124079, '海岸大厦东座-909', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23546, 1004937, 240111044331054835, 239825274387124080, '海岸大厦东座-910', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23547, 1004937, 240111044331054835, 239825274387124081, '海岸大厦东座-911', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23548, 1004937, 240111044331054835, 239825274387124082, '海岸大厦东座-912', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23549, 1004937, 240111044331054835, 239825274387124083, '海岸大厦东座-913', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23550, 1004937, 240111044331054835, 239825274387124084, '海岸大厦东座-914', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23551, 1004937, 240111044331054835, 239825274387124085, '海岸大厦东座-915', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23552, 1004937, 240111044331054835, 239825274387124086, '海岸大厦东座-916', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23553, 1004937, 240111044331054835, 239825274387124087, '海岸大厦东座-917', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23554, 1004937, 240111044331054835, 239825274387124088, '海岸大厦东座-1001', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23555, 1004937, 240111044331054835, 239825274387124089, '海岸大厦东座-1002', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23556, 1004937, 240111044331054835, 239825274387124090, '海岸大厦东座-1003', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23557, 1004937, 240111044331054835, 239825274387124091, '海岸大厦东座-1004', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23558, 1004937, 240111044331054835, 239825274387124092, '海岸大厦东座-1005', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23559, 1004937, 240111044331054835, 239825274387124093, '海岸大厦东座-1006', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23560, 1004937, 240111044331054835, 239825274387124094, '海岸大厦东座-1007', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23561, 1004937, 240111044331054835, 239825274387124095, '海岸大厦东座-1008', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23562, 1004937, 240111044331054835, 239825274387124096, '海岸大厦东座-1009', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23563, 1004937, 240111044331054835, 239825274387124097, '海岸大厦东座-1010', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23564, 1004937, 240111044331054835, 239825274387124098, '海岸大厦东座-1011', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23565, 1004937, 240111044331054835, 239825274387124099, '海岸大厦东座-1012', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23566, 1004937, 240111044331054835, 239825274387124100, '海岸大厦东座-1013', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23567, 1004937, 240111044331054835, 239825274387124101, '海岸大厦东座-1101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23568, 1004937, 240111044331054835, 239825274387124102, '海岸大厦东座-1102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23569, 1004937, 240111044331054835, 239825274387124103, '海岸大厦东座-1103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23570, 1004937, 240111044331054835, 239825274387124104, '海岸大厦东座-1104', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23571, 1004937, 240111044331054835, 239825274387124105, '海岸大厦东座-1105', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23572, 1004937, 240111044331054835, 239825274387124106, '海岸大厦东座-1106', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23573, 1004937, 240111044331054835, 239825274387124107, '海岸大厦东座-1107', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23574, 1004937, 240111044331054835, 239825274387124108, '海岸大厦东座-1108', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23575, 1004937, 240111044331054835, 239825274387124109, '海岸大厦东座-1109', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23576, 1004937, 240111044331054835, 239825274387124110, '海岸大厦东座-1110', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23577, 1004937, 240111044331054835, 239825274387124111, '海岸大厦东座-1111', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23578, 1004937, 240111044331054835, 239825274387124112, '海岸大厦东座-1112', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23579, 1004937, 240111044331054835, 239825274387124113, '海岸大厦东座-1113', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23580, 1004937, 240111044331054835, 239825274387124114, '海岸大厦东座-1114', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23581, 1004937, 240111044331054835, 239825274387124115, '海岸大厦东座-1115', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23582, 1004937, 240111044331054835, 239825274387124116, '海岸大厦东座-1116', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23583, 1004937, 240111044331054835, 239825274387124117, '海岸大厦东座-1117', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23584, 1004937, 240111044331054835, 239825274387124118, '海岸大厦东座-1201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23585, 1004937, 240111044331054835, 239825274387124119, '海岸大厦东座-1202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23586, 1004937, 240111044331054835, 239825274387124120, '海岸大厦东座-1203', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23587, 1004937, 240111044331054835, 239825274387124121, '海岸大厦东座-1204', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23588, 1004937, 240111044331054835, 239825274387124122, '海岸大厦东座-1205', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23589, 1004937, 240111044331054835, 239825274387124123, '海岸大厦东座-1206', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23590, 1004937, 240111044331054835, 239825274387124124, '海岸大厦东座-1207', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23591, 1004937, 240111044331054835, 239825274387124125, '海岸大厦东座-1208', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23592, 1004937, 240111044331054835, 239825274387124126, '海岸大厦东座-1209', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23593, 1004937, 240111044331054835, 239825274387124127, '海岸大厦东座-1210', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23594, 1004937, 240111044331054835, 239825274387124128, '海岸大厦东座-1211', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23595, 1004937, 240111044331054835, 239825274387124129, '海岸大厦东座-1212', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23596, 1004937, 240111044331054835, 239825274387124130, '海岸大厦东座-1213A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23597, 1004937, 240111044331054835, 239825274387124131, '海岸大厦东座-1213B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23598, 1004937, 240111044331054835, 239825274387124132, '海岸大厦东座-1301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23599, 1004937, 240111044331054835, 239825274387124133, '海岸大厦东座-1302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23600, 1004937, 240111044331054835, 239825274387124134, '海岸大厦东座-1303', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23601, 1004937, 240111044331054835, 239825274387124135, '海岸大厦东座-1304', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23602, 1004937, 240111044331054835, 239825274387124136, '海岸大厦东座-1305', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23603, 1004937, 240111044331054835, 239825274387124137, '海岸大厦东座-1306', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23604, 1004937, 240111044331054835, 239825274387124138, '海岸大厦东座-1307', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23605, 1004937, 240111044331054835, 239825274387124139, '海岸大厦东座-1308', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23606, 1004937, 240111044331054835, 239825274387124140, '海岸大厦东座-1309', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23607, 1004937, 240111044331054835, 239825274387124141, '海岸大厦东座-1310', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23608, 1004937, 240111044331054835, 239825274387124142, '海岸大厦东座-1311', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23609, 1004937, 240111044331054835, 239825274387124143, '海岸大厦东座-1312', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23610, 1004937, 240111044331054835, 239825274387124144, '海岸大厦东座-1313', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23611, 1004937, 240111044331054835, 239825274387124145, '海岸大厦东座-1315', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23612, 1004937, 240111044331054835, 239825274387124146, '海岸大厦东座-1316', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23613, 1004937, 240111044331054835, 239825274387124147, '海岸大厦东座-1317', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23614, 1004937, 240111044331054835, 239825274387124148, '海岸大厦东座-1318', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23615, 1004937, 240111044331054835, 239825274387124149, '海岸大厦东座-1401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23616, 1004937, 240111044331054835, 239825274387124150, '海岸大厦东座-1402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23617, 1004937, 240111044331054835, 239825274387124151, '海岸大厦东座-1403', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23618, 1004937, 240111044331054835, 239825274387124152, '海岸大厦东座-1404', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23619, 1004937, 240111044331054835, 239825274387124153, '海岸大厦东座-1405', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23620, 1004937, 240111044331054835, 239825274387124154, '海岸大厦东座-1406', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23621, 1004937, 240111044331054835, 239825274387124155, '海岸大厦东座-1407', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23622, 1004937, 240111044331054835, 239825274387124156, '海岸大厦东座-1408', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23623, 1004937, 240111044331054835, 239825274387124157, '海岸大厦东座-1409', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23624, 1004937, 240111044331054835, 239825274387124158, '海岸大厦东座-1410', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23625, 1004937, 240111044331054835, 239825274387124159, '海岸大厦东座-1411', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23626, 1004937, 240111044331054835, 239825274387124160, '海岸大厦东座-1412', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23627, 1004937, 240111044331054835, 239825274387124161, '海岸大厦东座-1501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23628, 1004937, 240111044331054835, 239825274387124162, '海岸大厦东座-1502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23629, 1004937, 240111044331054835, 239825274387124163, '海岸大厦东座-1503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23630, 1004937, 240111044331054835, 239825274387124164, '海岸大厦东座-1504', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23631, 1004937, 240111044331054835, 239825274387124165, '海岸大厦东座-1505', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23632, 1004937, 240111044331054835, 239825274387124166, '海岸大厦东座-1506', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23633, 1004937, 240111044331054835, 239825274387124167, '海岸大厦东座-1507', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23634, 1004937, 240111044331054835, 239825274387124168, '海岸大厦东座-1508', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23635, 1004937, 240111044331054835, 239825274387124169, '海岸大厦东座-1509', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23636, 1004937, 240111044331054835, 239825274387124170, '海岸大厦东座-1601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23637, 1004937, 240111044331054835, 239825274387124171, '海岸大厦东座-1602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23638, 1004937, 240111044331054835, 239825274387124172, '海岸大厦东座-1603', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23639, 1004937, 240111044331054835, 239825274387124173, '海岸大厦东座-1604', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23640, 1004937, 240111044331054835, 239825274387124174, '海岸大厦东座-1605A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23641, 1004937, 240111044331054835, 239825274387124175, '海岸大厦东座-1605B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23642, 1004937, 240111044331054835, 239825274387124176, '海岸大厦东座-1606', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23643, 1004937, 240111044331054835, 239825274387124177, '海岸大厦东座-1607', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23644, 1004937, 240111044331054835, 239825274387124178, '海岸大厦东座-1608A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23645, 1004937, 240111044331054835, 239825274387124179, '海岸大厦东座-1608B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23646, 1004937, 240111044331054835, 239825274387124180, '海岸大厦东座-1701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23647, 1004937, 240111044331054835, 239825274387124181, '海岸大厦东座-1702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23648, 1004937, 240111044331054835, 239825274387124182, '海岸大厦东座-1703', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23649, 1004937, 240111044331054835, 239825274387124183, '海岸大厦东座-1704', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23650, 1004937, 240111044331054835, 239825274387124184, '海岸大厦东座-1705', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23651, 1004937, 240111044331054835, 239825274387124185, '海岸大厦东座-1706', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23652, 1004937, 240111044331054835, 239825274387124186, '海岸大厦东座-1801', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23653, 1004937, 240111044331054835, 239825274387124187, '海岸大厦东座-1802', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23654, 1004937, 240111044331054835, 239825274387124188, '海岸大厦东座-1803', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23655, 1004937, 240111044331054835, 239825274387124189, '海岸大厦东座-1804', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23656, 1004937, 240111044331054835, 239825274387124190, '海岸大厦东座-1805', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23657, 1004937, 240111044331054835, 239825274387124191, '海岸大厦东座-1806', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23658, 1004937, 240111044331054835, 239825274387124192, '海岸大厦东座-1901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23659, 1004937, 240111044331054835, 239825274387124193, '海岸大厦东座-1902', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23660, 1004937, 240111044331054835, 239825274387124194, '海岸大厦东座-1903', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23661, 1004937, 240111044331054835, 239825274387124195, '海岸大厦东座-1904', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23662, 1004937, 240111044331054835, 239825274387124196, '海岸大厦东座-1905', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23663, 1004937, 240111044331054835, 239825274387124197, '海岸大厦东座-1906', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23664, 1004937, 240111044331054835, 239825274387124198, '海岸大厦东座-2001', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23665, 1004937, 240111044331054835, 239825274387124199, '海岸大厦东座-2002', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23666, 1004937, 240111044331054835, 239825274387124200, '海岸大厦东座-2003', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23667, 1004937, 240111044331054835, 239825274387124201, '海岸大厦东座-2004', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23668, 1004937, 240111044331054835, 239825274387124202, '海岸大厦东座-2005', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23669, 1004937, 240111044331054835, 239825274387124203, '海岸大厦东座-2006', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23670, 1004937, 240111044331054835, 239825274387124204, '海岸大厦东座-2101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23671, 1004937, 240111044331054835, 239825274387124205, '海岸大厦东座-2102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23672, 1004937, 240111044331054835, 239825274387124206, '海岸大厦东座-2103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23673, 1004937, 240111044331054835, 239825274387124207, '海岸大厦东座-2104', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23674, 1004937, 240111044331054835, 239825274387124208, '海岸大厦东座-2105', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23675, 1004937, 240111044331054835, 239825274387124209, '海岸大厦东座-2106', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23676, 1004937, 240111044331054835, 239825274387124210, '海岸大厦东座-2201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23677, 1004937, 240111044331054835, 239825274387124211, '海岸大厦东座-2202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23678, 1004937, 240111044331054835, 239825274387124212, '海岸大厦东座-2203', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23679, 1004937, 240111044331054835, 239825274387124213, '海岸大厦东座-2204', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23680, 1004937, 240111044331054835, 239825274387124214, '海岸大厦东座-2205', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23681, 1004937, 240111044331054835, 239825274387124215, '海岸大厦东座-2206', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23682, 1004937, 240111044331054835, 239825274387124216, '海岸大厦东座-2301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23683, 1004937, 240111044331054835, 239825274387124217, '海岸大厦东座-2302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23684, 1004937, 240111044331054835, 239825274387124218, '海岸大厦东座-2303', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23685, 1004937, 240111044331054835, 239825274387124219, '海岸大厦东座-2303B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23686, 1004937, 240111044331054835, 239825274387124220, '海岸大厦东座-2304', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23687, 1004937, 240111044331054835, 239825274387124221, '海岸大厦东座-2305', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23688, 1004937, 240111044331054835, 239825274387124222, '海岸大厦东座-2306', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23689, 1004937, 240111044331054835, 239825274387124223, '海岸大厦东座-2401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23690, 1004937, 240111044331054835, 239825274387124224, '海岸大厦东座-2402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23691, 1004937, 240111044331054835, 239825274387124225, '海岸大厦东座-2403', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23692, 1004937, 240111044331054835, 239825274387124226, '海岸大厦东座-2404', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23693, 1004937, 240111044331054835, 239825274387124227, '海岸大厦东座-2405', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (23694, 1004937, 240111044331054835, 239825274387124228, '海岸大厦东座-2406', '0');
	
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004670, UUID(), '美联物业', '美联物业', 1, 1, 1004672, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183134, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183134, UUID(), 999993, 2, 'EhGroups', 1004670,'美联物业','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004672, 0, 'ENTERPRISE', '美联物业', 0, '', '/1004672', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113543, 240111044331054835, 'organization', 1004672, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004671, UUID(), '中国工商银行深圳南山支行', '中国工商银行深圳南山支行', 1, 1, 1004673, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183135, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183135, UUID(), 999993, 2, 'EhGroups', 1004671,'中国工商银行深圳南山支行','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004673, 0, 'ENTERPRISE', '中国工商银行深圳南山支行', 0, '', '/1004673', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113544, 240111044331054835, 'organization', 1004673, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004672, UUID(), '深圳市百华盛联科技有限公司', '深圳市百华盛联科技有限公司', 1, 1, 1004674, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183136, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183136, UUID(), 999993, 2, 'EhGroups', 1004672,'深圳市百华盛联科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004674, 0, 'ENTERPRISE', '深圳市百华盛联科技有限公司', 0, '', '/1004674', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113545, 240111044331054835, 'organization', 1004674, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004673, UUID(), '中原地产', '中原地产', 1, 1, 1004675, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183137, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183137, UUID(), 999993, 2, 'EhGroups', 1004673,'中原地产','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004675, 0, 'ENTERPRISE', '中原地产', 0, '', '/1004675', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113546, 240111044331054835, 'organization', 1004675, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004674, UUID(), '日春茶业', '日春茶业', 1, 1, 1004676, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183138, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183138, UUID(), 999993, 2, 'EhGroups', 1004674,'日春茶业','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004676, 0, 'ENTERPRISE', '日春茶业', 0, '', '/1004676', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113547, 240111044331054835, 'organization', 1004676, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004675, UUID(), '深圳市美得堡抗衰老美容中心有限公司', '深圳市美得堡抗衰老美容中心有限公司', 1, 1, 1004677, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183139, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183139, UUID(), 999993, 2, 'EhGroups', 1004675,'深圳市美得堡抗衰老美容中心有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004677, 0, 'ENTERPRISE', '深圳市美得堡抗衰老美容中心有限公司', 0, '', '/1004677', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113548, 240111044331054835, 'organization', 1004677, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004676, UUID(), '百里臣', '百里臣', 1, 1, 1004678, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183140, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183140, UUID(), 999993, 2, 'EhGroups', 1004676,'百里臣','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004678, 0, 'ENTERPRISE', '百里臣', 0, '', '/1004678', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113549, 240111044331054835, 'organization', 1004678, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004677, UUID(), '木雕精品', '木雕精品', 1, 1, 1004679, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183141, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183141, UUID(), 999993, 2, 'EhGroups', 1004677,'木雕精品','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004679, 0, 'ENTERPRISE', '木雕精品', 0, '', '/1004679', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113550, 240111044331054835, 'organization', 1004679, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004678, UUID(), '云石咖啡店', '云石咖啡店', 1, 1, 1004680, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183142, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183142, UUID(), 999993, 2, 'EhGroups', 1004678,'云石咖啡店','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004680, 0, 'ENTERPRISE', '云石咖啡店', 0, '', '/1004680', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113551, 240111044331054835, 'organization', 1004680, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004679, UUID(), '亿家烟酒连锁', '亿家烟酒连锁', 1, 1, 1004681, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183143, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183143, UUID(), 999993, 2, 'EhGroups', 1004679,'亿家烟酒连锁','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004681, 0, 'ENTERPRISE', '亿家烟酒连锁', 0, '', '/1004681', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113552, 240111044331054835, 'organization', 1004681, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004680, UUID(), '敏萨藏式虫草店', '敏萨藏式虫草店', 1, 1, 1004682, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183144, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183144, UUID(), 999993, 2, 'EhGroups', 1004680,'敏萨藏式虫草店','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004682, 0, 'ENTERPRISE', '敏萨藏式虫草店', 0, '', '/1004682', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113553, 240111044331054835, 'organization', 1004682, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004681, UUID(), '发现名品', '发现名品', 1, 1, 1004683, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183145, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183145, UUID(), 999993, 2, 'EhGroups', 1004681,'发现名品','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004683, 0, 'ENTERPRISE', '发现名品', 0, '', '/1004683', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113554, 240111044331054835, 'organization', 1004683, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004682, UUID(), '康美人生', '康美人生', 1, 1, 1004684, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183146, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183146, UUID(), 999993, 2, 'EhGroups', 1004682,'康美人生','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004684, 0, 'ENTERPRISE', '康美人生', 0, '', '/1004684', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113555, 240111044331054835, 'organization', 1004684, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004683, UUID(), '泡菜韩式炭烧烤肉店', '泡菜韩式炭烧烤肉店', 1, 1, 1004685, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183147, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183147, UUID(), 999993, 2, 'EhGroups', 1004683,'泡菜韩式炭烧烤肉店','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004685, 0, 'ENTERPRISE', '泡菜韩式炭烧烤肉店', 0, '', '/1004685', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113556, 240111044331054835, 'organization', 1004685, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004684, UUID(), '还在想概念螺丝粉', '还在想概念螺丝粉', 1, 1, 1004686, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183148, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183148, UUID(), 999993, 2, 'EhGroups', 1004684,'还在想概念螺丝粉','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004686, 0, 'ENTERPRISE', '还在想概念螺丝粉', 0, '', '/1004686', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113557, 240111044331054835, 'organization', 1004686, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004685, UUID(), '老渔翁烤全鱼', '老渔翁烤全鱼', 1, 1, 1004687, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183149, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183149, UUID(), 999993, 2, 'EhGroups', 1004685,'老渔翁烤全鱼','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004687, 0, 'ENTERPRISE', '老渔翁烤全鱼', 0, '', '/1004687', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113558, 240111044331054835, 'organization', 1004687, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004686, UUID(), '黄焖鸡米饭', '黄焖鸡米饭', 1, 1, 1004688, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183150, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183150, UUID(), 999993, 2, 'EhGroups', 1004686,'黄焖鸡米饭','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004688, 0, 'ENTERPRISE', '黄焖鸡米饭', 0, '', '/1004688', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113559, 240111044331054835, 'organization', 1004688, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004687, UUID(), '大椰一粉', '大椰一粉', 1, 1, 1004689, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183151, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183151, UUID(), 999993, 2, 'EhGroups', 1004687,'大椰一粉','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004689, 0, 'ENTERPRISE', '大椰一粉', 0, '', '/1004689', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113560, 240111044331054835, 'organization', 1004689, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004688, UUID(), '极味道', '极味道', 1, 1, 1004690, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183152, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183152, UUID(), 999993, 2, 'EhGroups', 1004688,'极味道','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004690, 0, 'ENTERPRISE', '极味道', 0, '', '/1004690', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113561, 240111044331054835, 'organization', 1004690, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004689, UUID(), '钟路馆', '钟路馆', 1, 1, 1004691, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183153, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183153, UUID(), 999993, 2, 'EhGroups', 1004689,'钟路馆','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004691, 0, 'ENTERPRISE', '钟路馆', 0, '', '/1004691', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113562, 240111044331054835, 'organization', 1004691, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004690, UUID(), '国旅（深圳）国际旅行社有限公司', '国旅（深圳）国际旅行社有限公司', 1, 1, 1004692, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183154, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183154, UUID(), 999993, 2, 'EhGroups', 1004690,'国旅（深圳）国际旅行社有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004692, 0, 'ENTERPRISE', '国旅（深圳）国际旅行社有限公司', 0, '', '/1004692', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113563, 240111044331054835, 'organization', 1004692, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004691, UUID(), '牛小牛湖南常德米粉', '牛小牛湖南常德米粉', 1, 1, 1004693, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183155, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183155, UUID(), 999993, 2, 'EhGroups', 1004691,'牛小牛湖南常德米粉','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004693, 0, 'ENTERPRISE', '牛小牛湖南常德米粉', 0, '', '/1004693', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113564, 240111044331054835, 'organization', 1004693, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004692, UUID(), '老车家', '老车家', 1, 1, 1004694, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183156, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183156, UUID(), 999993, 2, 'EhGroups', 1004692,'老车家','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004694, 0, 'ENTERPRISE', '老车家', 0, '', '/1004694', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113565, 240111044331054835, 'organization', 1004694, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004693, UUID(), '形象会所', '形象会所', 1, 1, 1004695, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183157, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183157, UUID(), 999993, 2, 'EhGroups', 1004693,'形象会所','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004695, 0, 'ENTERPRISE', '形象会所', 0, '', '/1004695', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113566, 240111044331054835, 'organization', 1004695, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004694, UUID(), '邓氏三及第', '邓氏三及第', 1, 1, 1004696, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183158, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183158, UUID(), 999993, 2, 'EhGroups', 1004694,'邓氏三及第','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004696, 0, 'ENTERPRISE', '邓氏三及第', 0, '', '/1004696', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113567, 240111044331054835, 'organization', 1004696, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004695, UUID(), 'COCO', 'COCO', 1, 1, 1004697, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183159, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183159, UUID(), 999993, 2, 'EhGroups', 1004695,'COCO','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004697, 0, 'ENTERPRISE', 'COCO', 0, '', '/1004697', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113568, 240111044331054835, 'organization', 1004697, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004696, UUID(), '喵记', '喵记', 1, 1, 1004698, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183160, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183160, UUID(), 999993, 2, 'EhGroups', 1004696,'喵记','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004698, 0, 'ENTERPRISE', '喵记', 0, '', '/1004698', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113569, 240111044331054835, 'organization', 1004698, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004697, UUID(), '研磨时光', '研磨时光', 1, 1, 1004699, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183161, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183161, UUID(), 999993, 2, 'EhGroups', 1004697,'研磨时光','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004699, 0, 'ENTERPRISE', '研磨时光', 0, '', '/1004699', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113570, 240111044331054835, 'organization', 1004699, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004698, UUID(), '深圳泰椰爷餐饮管理有限公司', '深圳泰椰爷餐饮管理有限公司', 1, 1, 1004700, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183162, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183162, UUID(), 999993, 2, 'EhGroups', 1004698,'深圳泰椰爷餐饮管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004700, 0, 'ENTERPRISE', '深圳泰椰爷餐饮管理有限公司', 0, '', '/1004700', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113571, 240111044331054835, 'organization', 1004700, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004699, UUID(), '飞扬达航空', '飞扬达航空', 1, 1, 1004701, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183163, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183163, UUID(), 999993, 2, 'EhGroups', 1004699,'飞扬达航空','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004701, 0, 'ENTERPRISE', '飞扬达航空', 0, '', '/1004701', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113572, 240111044331054835, 'organization', 1004701, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004700, UUID(), '广东中旅（深圳）旅行社有限公司', '广东中旅（深圳）旅行社有限公司', 1, 1, 1004702, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183164, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183164, UUID(), 999993, 2, 'EhGroups', 1004700,'广东中旅（深圳）旅行社有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004702, 0, 'ENTERPRISE', '广东中旅（深圳）旅行社有限公司', 0, '', '/1004702', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113573, 240111044331054835, 'organization', 1004702, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004701, UUID(), '怡康之家', '怡康之家', 1, 1, 1004703, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183165, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183165, UUID(), 999993, 2, 'EhGroups', 1004701,'怡康之家','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004703, 0, 'ENTERPRISE', '怡康之家', 0, '', '/1004703', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113574, 240111044331054835, 'organization', 1004703, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004702, UUID(), '深圳沙拉塔餐饮有限公司', '深圳沙拉塔餐饮有限公司', 1, 1, 1004704, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183166, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183166, UUID(), 999993, 2, 'EhGroups', 1004702,'深圳沙拉塔餐饮有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004704, 0, 'ENTERPRISE', '深圳沙拉塔餐饮有限公司', 0, '', '/1004704', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113575, 240111044331054835, 'organization', 1004704, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004703, UUID(), '思麦乐商贸（深圳）有限公司', '思麦乐商贸（深圳）有限公司', 1, 1, 1004705, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183167, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183167, UUID(), 999993, 2, 'EhGroups', 1004703,'思麦乐商贸（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004705, 0, 'ENTERPRISE', '思麦乐商贸（深圳）有限公司', 0, '', '/1004705', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113576, 240111044331054835, 'organization', 1004705, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004704, UUID(), '海之恋寿司店', '海之恋寿司店', 1, 1, 1004706, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183168, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183168, UUID(), 999993, 2, 'EhGroups', 1004704,'海之恋寿司店','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004706, 0, 'ENTERPRISE', '海之恋寿司店', 0, '', '/1004706', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113577, 240111044331054835, 'organization', 1004706, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004705, UUID(), '无敌小BABY', '无敌小BABY', 1, 1, 1004707, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183169, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183169, UUID(), 999993, 2, 'EhGroups', 1004705,'无敌小BABY','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004707, 0, 'ENTERPRISE', '无敌小BABY', 0, '', '/1004707', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113578, 240111044331054835, 'organization', 1004707, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004706, UUID(), '格物', '格物', 1, 1, 1004708, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183170, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183170, UUID(), 999993, 2, 'EhGroups', 1004706,'格物','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004708, 0, 'ENTERPRISE', '格物', 0, '', '/1004708', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113579, 240111044331054835, 'organization', 1004708, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004707, UUID(), '小米姑娘', '小米姑娘', 1, 1, 1004709, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183171, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183171, UUID(), 999993, 2, 'EhGroups', 1004707,'小米姑娘','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004709, 0, 'ENTERPRISE', '小米姑娘', 0, '', '/1004709', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113580, 240111044331054835, 'organization', 1004709, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004708, UUID(), '爱家味', '爱家味', 1, 1, 1004710, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183172, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183172, UUID(), 999993, 2, 'EhGroups', 1004708,'爱家味','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004710, 0, 'ENTERPRISE', '爱家味', 0, '', '/1004710', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113581, 240111044331054835, 'organization', 1004710, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004709, UUID(), '江南阁苑', '江南阁苑', 1, 1, 1004711, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183173, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183173, UUID(), 999993, 2, 'EhGroups', 1004709,'江南阁苑','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004711, 0, 'ENTERPRISE', '江南阁苑', 0, '', '/1004711', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113582, 240111044331054835, 'organization', 1004711, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004710, UUID(), '牛王庙', '牛王庙', 1, 1, 1004712, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183174, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183174, UUID(), 999993, 2, 'EhGroups', 1004710,'牛王庙','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004712, 0, 'ENTERPRISE', '牛王庙', 0, '', '/1004712', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113583, 240111044331054835, 'organization', 1004712, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004711, UUID(), '西安老王家', '西安老王家', 1, 1, 1004713, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183175, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183175, UUID(), 999993, 2, 'EhGroups', 1004711,'西安老王家','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004713, 0, 'ENTERPRISE', '西安老王家', 0, '', '/1004713', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113584, 240111044331054835, 'organization', 1004713, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004712, UUID(), '小禾寿司', '小禾寿司', 1, 1, 1004714, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183176, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183176, UUID(), 999993, 2, 'EhGroups', 1004712,'小禾寿司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004714, 0, 'ENTERPRISE', '小禾寿司', 0, '', '/1004714', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113585, 240111044331054835, 'organization', 1004714, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004713, UUID(), '极草专卖', '极草专卖', 1, 1, 1004715, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183177, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183177, UUID(), 999993, 2, 'EhGroups', 1004713,'极草专卖','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004715, 0, 'ENTERPRISE', '极草专卖', 0, '', '/1004715', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113586, 240111044331054835, 'organization', 1004715, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004714, UUID(), 'GARMIN', 'GARMIN', 1, 1, 1004716, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183178, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183178, UUID(), 999993, 2, 'EhGroups', 1004714,'GARMIN','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004716, 0, 'ENTERPRISE', 'GARMIN', 0, '', '/1004716', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113587, 240111044331054835, 'organization', 1004716, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004715, UUID(), '大众银行(香港)有限公司深圳分公司', '大众银行(香港)有限公司深圳分公司', 1, 1, 1004717, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183179, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183179, UUID(), 999993, 2, 'EhGroups', 1004715,'大众银行(香港)有限公司深圳分公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004717, 0, 'ENTERPRISE', '大众银行(香港)有限公司深圳分公司', 0, '', '/1004717', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113588, 240111044331054835, 'organization', 1004717, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004716, UUID(), '欧蒙洋服', '欧蒙洋服', 1, 1, 1004718, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183180, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183180, UUID(), 999993, 2, 'EhGroups', 1004716,'欧蒙洋服','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004718, 0, 'ENTERPRISE', '欧蒙洋服', 0, '', '/1004718', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113589, 240111044331054835, 'organization', 1004718, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004717, UUID(), '茶の井', '茶の井', 1, 1, 1004719, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183181, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183181, UUID(), 999993, 2, 'EhGroups', 1004717,'茶の井','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004719, 0, 'ENTERPRISE', '茶の井', 0, '', '/1004719', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113590, 240111044331054835, 'organization', 1004719, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004718, UUID(), '猪扒酸辣米线', '猪扒酸辣米线', 1, 1, 1004720, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183182, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183182, UUID(), 999993, 2, 'EhGroups', 1004718,'猪扒酸辣米线','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004720, 0, 'ENTERPRISE', '猪扒酸辣米线', 0, '', '/1004720', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113591, 240111044331054835, 'organization', 1004720, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004719, UUID(), '老张牛肉面', '老张牛肉面', 1, 1, 1004721, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183183, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183183, UUID(), 999993, 2, 'EhGroups', 1004719,'老张牛肉面','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004721, 0, 'ENTERPRISE', '老张牛肉面', 0, '', '/1004721', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113592, 240111044331054835, 'organization', 1004721, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004720, UUID(), '印象清迈', '印象清迈', 1, 1, 1004722, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183184, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183184, UUID(), 999993, 2, 'EhGroups', 1004720,'印象清迈','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004722, 0, 'ENTERPRISE', '印象清迈', 0, '', '/1004722', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113593, 240111044331054835, 'organization', 1004722, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004721, UUID(), '花语生活', '花语生活', 1, 1, 1004723, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183185, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183185, UUID(), 999993, 2, 'EhGroups', 1004721,'花语生活','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004723, 0, 'ENTERPRISE', '花语生活', 0, '', '/1004723', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113594, 240111044331054835, 'organization', 1004723, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004722, UUID(), 'HELLO CICI', 'HELLO CICI', 1, 1, 1004724, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183186, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183186, UUID(), 999993, 2, 'EhGroups', 1004722,'HELLO CICI','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004724, 0, 'ENTERPRISE', 'HELLO CICI', 0, '', '/1004724', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113595, 240111044331054835, 'organization', 1004724, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004723, UUID(), '上上千', '上上千', 1, 1, 1004725, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183187, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183187, UUID(), 999993, 2, 'EhGroups', 1004723,'上上千','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004725, 0, 'ENTERPRISE', '上上千', 0, '', '/1004725', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113596, 240111044331054835, 'organization', 1004725, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004724, UUID(), '广发隆江猪脚饭', '广发隆江猪脚饭', 1, 1, 1004726, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183188, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183188, UUID(), 999993, 2, 'EhGroups', 1004724,'广发隆江猪脚饭','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004726, 0, 'ENTERPRISE', '广发隆江猪脚饭', 0, '', '/1004726', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113597, 240111044331054835, 'organization', 1004726, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004725, UUID(), '叫面', '叫面', 1, 1, 1004727, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183189, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183189, UUID(), 999993, 2, 'EhGroups', 1004725,'叫面','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004727, 0, 'ENTERPRISE', '叫面', 0, '', '/1004727', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113598, 240111044331054835, 'organization', 1004727, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004726, UUID(), '元豚家', '元豚家', 1, 1, 1004728, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183190, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183190, UUID(), 999993, 2, 'EhGroups', 1004726,'元豚家','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004728, 0, 'ENTERPRISE', '元豚家', 0, '', '/1004728', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113599, 240111044331054835, 'organization', 1004728, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004727, UUID(), '桂林米粉', '桂林米粉', 1, 1, 1004729, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183191, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183191, UUID(), 999993, 2, 'EhGroups', 1004727,'桂林米粉','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004729, 0, 'ENTERPRISE', '桂林米粉', 0, '', '/1004729', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113600, 240111044331054835, 'organization', 1004729, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004728, UUID(), '妍服饰配装工作室', '妍服饰配装工作室', 1, 1, 1004730, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183192, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183192, UUID(), 999993, 2, 'EhGroups', 1004728,'妍服饰配装工作室','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004730, 0, 'ENTERPRISE', '妍服饰配装工作室', 0, '', '/1004730', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113601, 240111044331054835, 'organization', 1004730, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004729, UUID(), '沙县小吃', '沙县小吃', 1, 1, 1004731, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183193, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183193, UUID(), 999993, 2, 'EhGroups', 1004729,'沙县小吃','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004731, 0, 'ENTERPRISE', '沙县小吃', 0, '', '/1004731', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113602, 240111044331054835, 'organization', 1004731, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004730, UUID(), '泰芒了', '泰芒了', 1, 1, 1004732, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183194, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183194, UUID(), 999993, 2, 'EhGroups', 1004730,'泰芒了','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004732, 0, 'ENTERPRISE', '泰芒了', 0, '', '/1004732', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113603, 240111044331054835, 'organization', 1004732, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004731, UUID(), '曼思诺', '曼思诺', 1, 1, 1004733, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183195, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183195, UUID(), 999993, 2, 'EhGroups', 1004731,'曼思诺','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004733, 0, 'ENTERPRISE', '曼思诺', 0, '', '/1004733', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113604, 240111044331054835, 'organization', 1004733, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004732, UUID(), '深圳市民心大药房连锁有限公司', '深圳市民心大药房连锁有限公司', 1, 1, 1004734, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183196, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183196, UUID(), 999993, 2, 'EhGroups', 1004732,'深圳市民心大药房连锁有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004734, 0, 'ENTERPRISE', '深圳市民心大药房连锁有限公司', 0, '', '/1004734', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113605, 240111044331054835, 'organization', 1004734, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004733, UUID(), '名将洋服', '名将洋服', 1, 1, 1004735, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183197, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183197, UUID(), 999993, 2, 'EhGroups', 1004733,'名将洋服','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004735, 0, 'ENTERPRISE', '名将洋服', 0, '', '/1004735', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113606, 240111044331054835, 'organization', 1004735, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004734, UUID(), '恒大地产集团恩平公司', '恒大地产集团恩平公司', 1, 1, 1004736, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183198, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183198, UUID(), 999993, 2, 'EhGroups', 1004734,'恒大地产集团恩平公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004736, 0, 'ENTERPRISE', '恒大地产集团恩平公司', 0, '', '/1004736', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113607, 240111044331054835, 'organization', 1004736, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004735, UUID(), '米芝莲', '米芝莲', 1, 1, 1004737, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183199, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183199, UUID(), 999993, 2, 'EhGroups', 1004735,'米芝莲','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004737, 0, 'ENTERPRISE', '米芝莲', 0, '', '/1004737', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113608, 240111044331054835, 'organization', 1004737, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004736, UUID(), '光美芸', '光美芸', 1, 1, 1004738, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183200, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183200, UUID(), 999993, 2, 'EhGroups', 1004736,'光美芸','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004738, 0, 'ENTERPRISE', '光美芸', 0, '', '/1004738', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113609, 240111044331054835, 'organization', 1004738, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004737, UUID(), 'MEX', 'MEX', 1, 1, 1004739, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183201, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183201, UUID(), 999993, 2, 'EhGroups', 1004737,'MEX','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004739, 0, 'ENTERPRISE', 'MEX', 0, '', '/1004739', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113610, 240111044331054835, 'organization', 1004739, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004738, UUID(), '蔓延视觉', '蔓延视觉', 1, 1, 1004740, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183202, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183202, UUID(), 999993, 2, 'EhGroups', 1004738,'蔓延视觉','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004740, 0, 'ENTERPRISE', '蔓延视觉', 0, '', '/1004740', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113611, 240111044331054835, 'organization', 1004740, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004739, UUID(), '迷可祛痘', '迷可祛痘', 1, 1, 1004741, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183203, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183203, UUID(), 999993, 2, 'EhGroups', 1004739,'迷可祛痘','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004741, 0, 'ENTERPRISE', '迷可祛痘', 0, '', '/1004741', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113612, 240111044331054835, 'organization', 1004741, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004740, UUID(), '泰芒了B', '泰芒了B', 1, 1, 1004742, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183204, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183204, UUID(), 999993, 2, 'EhGroups', 1004740,'泰芒了B','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004742, 0, 'ENTERPRISE', '泰芒了B', 0, '', '/1004742', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113613, 240111044331054835, 'organization', 1004742, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004741, UUID(), '品客服饰店', '品客服饰店', 1, 1, 1004743, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183205, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183205, UUID(), 999993, 2, 'EhGroups', 1004741,'品客服饰店','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004743, 0, 'ENTERPRISE', '品客服饰店', 0, '', '/1004743', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113614, 240111044331054835, 'organization', 1004743, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004742, UUID(), '美甲沙龙', '美甲沙龙', 1, 1, 1004744, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183206, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183206, UUID(), 999993, 2, 'EhGroups', 1004742,'美甲沙龙','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004744, 0, 'ENTERPRISE', '美甲沙龙', 0, '', '/1004744', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113615, 240111044331054835, 'organization', 1004744, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004743, UUID(), '橄榄屋', '橄榄屋', 1, 1, 1004745, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183207, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183207, UUID(), 999993, 2, 'EhGroups', 1004743,'橄榄屋','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004745, 0, 'ENTERPRISE', '橄榄屋', 0, '', '/1004745', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113616, 240111044331054835, 'organization', 1004745, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004744, UUID(), '喜乐库', '喜乐库', 1, 1, 1004746, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183208, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183208, UUID(), 999993, 2, 'EhGroups', 1004744,'喜乐库','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004746, 0, 'ENTERPRISE', '喜乐库', 0, '', '/1004746', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113617, 240111044331054835, 'organization', 1004746, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004745, UUID(), '雪冰', '雪冰', 1, 1, 1004747, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183209, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183209, UUID(), 999993, 2, 'EhGroups', 1004745,'雪冰','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004747, 0, 'ENTERPRISE', '雪冰', 0, '', '/1004747', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113618, 240111044331054835, 'organization', 1004747, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004746, UUID(), '阿巴町', '阿巴町', 1, 1, 1004748, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183210, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183210, UUID(), 999993, 2, 'EhGroups', 1004746,'阿巴町','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004748, 0, 'ENTERPRISE', '阿巴町', 0, '', '/1004748', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113619, 240111044331054835, 'organization', 1004748, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004747, UUID(), '小王子国际少儿英语', '小王子国际少儿英语', 1, 1, 1004749, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183211, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183211, UUID(), 999993, 2, 'EhGroups', 1004747,'小王子国际少儿英语','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004749, 0, 'ENTERPRISE', '小王子国际少儿英语', 0, '', '/1004749', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113620, 240111044331054835, 'organization', 1004749, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004748, UUID(), '麦田印象', '麦田印象', 1, 1, 1004750, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183212, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183212, UUID(), 999993, 2, 'EhGroups', 1004748,'麦田印象','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004750, 0, 'ENTERPRISE', '麦田印象', 0, '', '/1004750', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113621, 240111044331054835, 'organization', 1004750, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004749, UUID(), '深圳新一城', '深圳新一城', 1, 1, 1004751, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183213, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183213, UUID(), 999993, 2, 'EhGroups', 1004749,'深圳新一城','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004751, 0, 'ENTERPRISE', '深圳新一城', 0, '', '/1004751', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113622, 240111044331054835, 'organization', 1004751, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004750, UUID(), '深圳市前海乐富电子商务有限公司', '深圳市前海乐富电子商务有限公司', 1, 1, 1004752, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183214, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183214, UUID(), 999993, 2, 'EhGroups', 1004750,'深圳市前海乐富电子商务有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004752, 0, 'ENTERPRISE', '深圳市前海乐富电子商务有限公司', 0, '', '/1004752', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113623, 240111044331054835, 'organization', 1004752, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004751, UUID(), '非常视觉婚纱摄影', '非常视觉婚纱摄影', 1, 1, 1004753, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183215, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183215, UUID(), 999993, 2, 'EhGroups', 1004751,'非常视觉婚纱摄影','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004753, 0, 'ENTERPRISE', '非常视觉婚纱摄影', 0, '', '/1004753', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113624, 240111044331054835, 'organization', 1004753, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004752, UUID(), '皇女美睫生活馆', '皇女美睫生活馆', 1, 1, 1004754, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183216, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183216, UUID(), 999993, 2, 'EhGroups', 1004752,'皇女美睫生活馆','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004754, 0, 'ENTERPRISE', '皇女美睫生活馆', 0, '', '/1004754', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113625, 240111044331054835, 'organization', 1004754, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004753, UUID(), '皇甫设计', '皇甫设计', 1, 1, 1004755, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183217, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183217, UUID(), 999993, 2, 'EhGroups', 1004753,'皇甫设计','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004755, 0, 'ENTERPRISE', '皇甫设计', 0, '', '/1004755', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113626, 240111044331054835, 'organization', 1004755, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004754, UUID(), '东尚聚美', '东尚聚美', 1, 1, 1004756, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183218, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183218, UUID(), 999993, 2, 'EhGroups', 1004754,'东尚聚美','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004756, 0, 'ENTERPRISE', '东尚聚美', 0, '', '/1004756', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113627, 240111044331054835, 'organization', 1004756, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004755, UUID(), '妙音禅茶', '妙音禅茶', 1, 1, 1004757, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183219, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183219, UUID(), 999993, 2, 'EhGroups', 1004755,'妙音禅茶','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004757, 0, 'ENTERPRISE', '妙音禅茶', 0, '', '/1004757', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113628, 240111044331054835, 'organization', 1004757, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004756, UUID(), '校服专卖', '校服专卖', 1, 1, 1004758, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183220, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183220, UUID(), 999993, 2, 'EhGroups', 1004756,'校服专卖','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004758, 0, 'ENTERPRISE', '校服专卖', 0, '', '/1004758', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113629, 240111044331054835, 'organization', 1004758, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004757, UUID(), '爱衣加加', '爱衣加加', 1, 1, 1004759, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183221, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183221, UUID(), 999993, 2, 'EhGroups', 1004757,'爱衣加加','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004759, 0, 'ENTERPRISE', '爱衣加加', 0, '', '/1004759', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113630, 240111044331054835, 'organization', 1004759, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004758, UUID(), '深圳市快足足疗保健中心', '深圳市快足足疗保健中心', 1, 1, 1004760, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183222, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183222, UUID(), 999993, 2, 'EhGroups', 1004758,'深圳市快足足疗保健中心','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004760, 0, 'ENTERPRISE', '深圳市快足足疗保健中心', 0, '', '/1004760', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113631, 240111044331054835, 'organization', 1004760, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004759, UUID(), '顺品甜品', '顺品甜品', 1, 1, 1004761, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183223, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183223, UUID(), 999993, 2, 'EhGroups', 1004759,'顺品甜品','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004761, 0, 'ENTERPRISE', '顺品甜品', 0, '', '/1004761', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113632, 240111044331054835, 'organization', 1004761, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004760, UUID(), '软糖咖啡', '软糖咖啡', 1, 1, 1004762, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183224, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183224, UUID(), 999993, 2, 'EhGroups', 1004760,'软糖咖啡','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004762, 0, 'ENTERPRISE', '软糖咖啡', 0, '', '/1004762', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113633, 240111044331054835, 'organization', 1004762, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004761, UUID(), '色美人', '色美人', 1, 1, 1004763, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183225, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183225, UUID(), 999993, 2, 'EhGroups', 1004761,'色美人','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004763, 0, 'ENTERPRISE', '色美人', 0, '', '/1004763', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113634, 240111044331054835, 'organization', 1004763, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004762, UUID(), 'JS美发店', 'JS美发店', 1, 1, 1004764, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183226, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183226, UUID(), 999993, 2, 'EhGroups', 1004762,'JS美发店','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004764, 0, 'ENTERPRISE', 'JS美发店', 0, '', '/1004764', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113635, 240111044331054835, 'organization', 1004764, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004763, UUID(), '三藩阳光（球鞋专门店）', '三藩阳光（球鞋专门店）', 1, 1, 1004765, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183227, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183227, UUID(), 999993, 2, 'EhGroups', 1004763,'三藩阳光（球鞋专门店）','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004765, 0, 'ENTERPRISE', '三藩阳光（球鞋专门店）', 0, '', '/1004765', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113636, 240111044331054835, 'organization', 1004765, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004764, UUID(), '木子美容', '木子美容', 1, 1, 1004766, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183228, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183228, UUID(), 999993, 2, 'EhGroups', 1004764,'木子美容','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004766, 0, 'ENTERPRISE', '木子美容', 0, '', '/1004766', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113637, 240111044331054835, 'organization', 1004766, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004765, UUID(), '私人定制', '私人定制', 1, 1, 1004767, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183229, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183229, UUID(), 999993, 2, 'EhGroups', 1004765,'私人定制','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004767, 0, 'ENTERPRISE', '私人定制', 0, '', '/1004767', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113638, 240111044331054835, 'organization', 1004767, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004766, UUID(), '华尔街英语培训中心', '华尔街英语培训中心', 1, 1, 1004768, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183230, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183230, UUID(), 999993, 2, 'EhGroups', 1004766,'华尔街英语培训中心','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004768, 0, 'ENTERPRISE', '华尔街英语培训中心', 0, '', '/1004768', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113639, 240111044331054835, 'organization', 1004768, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004767, UUID(), '肆姑娘', '肆姑娘', 1, 1, 1004769, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183231, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183231, UUID(), 999993, 2, 'EhGroups', 1004767,'肆姑娘','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004769, 0, 'ENTERPRISE', '肆姑娘', 0, '', '/1004769', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113640, 240111044331054835, 'organization', 1004769, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004768, UUID(), '家乐缘', '家乐缘', 1, 1, 1004770, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183232, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183232, UUID(), 999993, 2, 'EhGroups', 1004768,'家乐缘','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004770, 0, 'ENTERPRISE', '家乐缘', 0, '', '/1004770', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113641, 240111044331054835, 'organization', 1004770, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004769, UUID(), '卷福', '卷福', 1, 1, 1004771, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183233, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183233, UUID(), 999993, 2, 'EhGroups', 1004769,'卷福','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004771, 0, 'ENTERPRISE', '卷福', 0, '', '/1004771', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113642, 240111044331054835, 'organization', 1004771, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004770, UUID(), '五耕自助烤肉店', '五耕自助烤肉店', 1, 1, 1004772, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183234, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183234, UUID(), 999993, 2, 'EhGroups', 1004770,'五耕自助烤肉店','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004772, 0, 'ENTERPRISE', '五耕自助烤肉店', 0, '', '/1004772', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113643, 240111044331054835, 'organization', 1004772, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004771, UUID(), '百度烤肉专门店', '百度烤肉专门店', 1, 1, 1004773, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183235, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183235, UUID(), 999993, 2, 'EhGroups', 1004771,'百度烤肉专门店','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004773, 0, 'ENTERPRISE', '百度烤肉专门店', 0, '', '/1004773', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113644, 240111044331054835, 'organization', 1004773, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004772, UUID(), '水煮鱼乡', '水煮鱼乡', 1, 1, 1004774, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183236, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183236, UUID(), 999993, 2, 'EhGroups', 1004772,'水煮鱼乡','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004774, 0, 'ENTERPRISE', '水煮鱼乡', 0, '', '/1004774', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113645, 240111044331054835, 'organization', 1004774, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004773, UUID(), '杰斯特鲜榨果汁吧', '杰斯特鲜榨果汁吧', 1, 1, 1004775, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183237, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183237, UUID(), 999993, 2, 'EhGroups', 1004773,'杰斯特鲜榨果汁吧','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004775, 0, 'ENTERPRISE', '杰斯特鲜榨果汁吧', 0, '', '/1004775', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113646, 240111044331054835, 'organization', 1004775, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004774, UUID(), '新桃园酒店', '新桃园酒店', 1, 1, 1004776, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183238, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183238, UUID(), 999993, 2, 'EhGroups', 1004774,'新桃园酒店','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004776, 0, 'ENTERPRISE', '新桃园酒店', 0, '', '/1004776', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113647, 240111044331054835, 'organization', 1004776, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004775, UUID(), '深圳联合信安资产管理有限公司', '深圳联合信安资产管理有限公司', 1, 1, 1004777, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183239, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183239, UUID(), 999993, 2, 'EhGroups', 1004775,'深圳联合信安资产管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004777, 0, 'ENTERPRISE', '深圳联合信安资产管理有限公司', 0, '', '/1004777', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113648, 240111044331054835, 'organization', 1004777, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004776, UUID(), '深圳市鸿利来典当有限公司', '深圳市鸿利来典当有限公司', 1, 1, 1004778, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183240, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183240, UUID(), 999993, 2, 'EhGroups', 1004776,'深圳市鸿利来典当有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004778, 0, 'ENTERPRISE', '深圳市鸿利来典当有限公司', 0, '', '/1004778', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113649, 240111044331054835, 'organization', 1004778, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004777, UUID(), '上海现代精密工业（香港）有限公司', '上海现代精密工业（香港）有限公司', 1, 1, 1004779, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183241, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183241, UUID(), 999993, 2, 'EhGroups', 1004777,'上海现代精密工业（香港）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004779, 0, 'ENTERPRISE', '上海现代精密工业（香港）有限公司', 0, '', '/1004779', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113650, 240111044331054835, 'organization', 1004779, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004778, UUID(), '中国移动', '中国移动', 1, 1, 1004780, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183242, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183242, UUID(), 999993, 2, 'EhGroups', 1004778,'中国移动','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004780, 0, 'ENTERPRISE', '中国移动', 0, '', '/1004780', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113651, 240111044331054835, 'organization', 1004780, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004779, UUID(), '美国爱姆斯斯（中国）有限公司深圳代表处', '美国爱姆斯斯（中国）有限公司深圳代表处', 1, 1, 1004781, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183243, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183243, UUID(), 999993, 2, 'EhGroups', 1004779,'美国爱姆斯斯（中国）有限公司深圳代表处','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004781, 0, 'ENTERPRISE', '美国爱姆斯斯（中国）有限公司深圳代表处', 0, '', '/1004781', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113652, 240111044331054835, 'organization', 1004781, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004780, UUID(), '香港汉风科技有限公司', '香港汉风科技有限公司', 1, 1, 1004782, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183244, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183244, UUID(), 999993, 2, 'EhGroups', 1004780,'香港汉风科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004782, 0, 'ENTERPRISE', '香港汉风科技有限公司', 0, '', '/1004782', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113653, 240111044331054835, 'organization', 1004782, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004781, UUID(), '深圳美今家居生活服务有限公司', '深圳美今家居生活服务有限公司', 1, 1, 1004783, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183245, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183245, UUID(), 999993, 2, 'EhGroups', 1004781,'深圳美今家居生活服务有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004783, 0, 'ENTERPRISE', '深圳美今家居生活服务有限公司', 0, '', '/1004783', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113654, 240111044331054835, 'organization', 1004783, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004782, UUID(), '深圳市阿维兰国际货运代理有限公司', '深圳市阿维兰国际货运代理有限公司', 1, 1, 1004784, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183246, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183246, UUID(), 999993, 2, 'EhGroups', 1004782,'深圳市阿维兰国际货运代理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004784, 0, 'ENTERPRISE', '深圳市阿维兰国际货运代理有限公司', 0, '', '/1004784', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113655, 240111044331054835, 'organization', 1004784, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004783, UUID(), '深圳春天海岸医疗美容门诊部有限公司', '深圳春天海岸医疗美容门诊部有限公司', 1, 1, 1004785, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183247, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183247, UUID(), 999993, 2, 'EhGroups', 1004783,'深圳春天海岸医疗美容门诊部有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004785, 0, 'ENTERPRISE', '深圳春天海岸医疗美容门诊部有限公司', 0, '', '/1004785', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113656, 240111044331054835, 'organization', 1004785, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004784, UUID(), '北京搜诺思科技有限公司深圳分公司', '北京搜诺思科技有限公司深圳分公司', 1, 1, 1004786, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183248, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183248, UUID(), 999993, 2, 'EhGroups', 1004784,'北京搜诺思科技有限公司深圳分公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004786, 0, 'ENTERPRISE', '北京搜诺思科技有限公司深圳分公司', 0, '', '/1004786', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113657, 240111044331054835, 'organization', 1004786, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004785, UUID(), '深圳市云石科技有限公司', '深圳市云石科技有限公司', 1, 1, 1004787, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183249, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183249, UUID(), 999993, 2, 'EhGroups', 1004785,'深圳市云石科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004787, 0, 'ENTERPRISE', '深圳市云石科技有限公司', 0, '', '/1004787', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113658, 240111044331054835, 'organization', 1004787, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004786, UUID(), '深圳荷包金融信息咨询有限公司', '深圳荷包金融信息咨询有限公司', 1, 1, 1004788, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183250, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183250, UUID(), 999993, 2, 'EhGroups', 1004786,'深圳荷包金融信息咨询有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004788, 0, 'ENTERPRISE', '深圳荷包金融信息咨询有限公司', 0, '', '/1004788', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113659, 240111044331054835, 'organization', 1004788, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004787, UUID(), '阿海珐北京（咨询）有限公司', '阿海珐北京（咨询）有限公司', 1, 1, 1004789, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183251, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183251, UUID(), 999993, 2, 'EhGroups', 1004787,'阿海珐北京（咨询）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004789, 0, 'ENTERPRISE', '阿海珐北京（咨询）有限公司', 0, '', '/1004789', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113660, 240111044331054835, 'organization', 1004789, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004788, UUID(), '深圳市航天高科投资管理有限公司', '深圳市航天高科投资管理有限公司', 1, 1, 1004790, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183252, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183252, UUID(), 999993, 2, 'EhGroups', 1004788,'深圳市航天高科投资管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004790, 0, 'ENTERPRISE', '深圳市航天高科投资管理有限公司', 0, '', '/1004790', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113661, 240111044331054835, 'organization', 1004790, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004789, UUID(), '深圳市长润发涂料有限公司', '深圳市长润发涂料有限公司', 1, 1, 1004791, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183253, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183253, UUID(), 999993, 2, 'EhGroups', 1004789,'深圳市长润发涂料有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004791, 0, 'ENTERPRISE', '深圳市长润发涂料有限公司', 0, '', '/1004791', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113662, 240111044331054835, 'organization', 1004791, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004790, UUID(), 'WORLD', 'WORLD', 1, 1, 1004792, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183254, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183254, UUID(), 999993, 2, 'EhGroups', 1004790,'WORLD','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004792, 0, 'ENTERPRISE', 'WORLD', 0, '', '/1004792', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113663, 240111044331054835, 'organization', 1004792, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004791, UUID(), '北京泰豪智能工程有限公司', '北京泰豪智能工程有限公司', 1, 1, 1004793, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183255, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183255, UUID(), 999993, 2, 'EhGroups', 1004791,'北京泰豪智能工程有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004793, 0, 'ENTERPRISE', '北京泰豪智能工程有限公司', 0, '', '/1004793', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113664, 240111044331054835, 'organization', 1004793, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004792, UUID(), '中原地产代理(深圳)有限公司', '中原地产代理(深圳)有限公司', 1, 1, 1004794, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183256, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183256, UUID(), 999993, 2, 'EhGroups', 1004792,'中原地产代理(深圳)有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004794, 0, 'ENTERPRISE', '中原地产代理(深圳)有限公司', 0, '', '/1004794', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113665, 240111044331054835, 'organization', 1004794, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004793, UUID(), '深圳贺戎博闻展览有限公司', '深圳贺戎博闻展览有限公司', 1, 1, 1004795, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183257, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183257, UUID(), 999993, 2, 'EhGroups', 1004793,'深圳贺戎博闻展览有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004795, 0, 'ENTERPRISE', '深圳贺戎博闻展览有限公司', 0, '', '/1004795', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113666, 240111044331054835, 'organization', 1004795, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004794, UUID(), '盛世景资产管理股份有限公司深圳分公司', '盛世景资产管理股份有限公司深圳分公司', 1, 1, 1004796, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183258, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183258, UUID(), 999993, 2, 'EhGroups', 1004794,'盛世景资产管理股份有限公司深圳分公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004796, 0, 'ENTERPRISE', '盛世景资产管理股份有限公司深圳分公司', 0, '', '/1004796', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113667, 240111044331054835, 'organization', 1004796, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004795, UUID(), '深圳特瑞姿生物科技有限公司', '深圳特瑞姿生物科技有限公司', 1, 1, 1004797, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183259, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183259, UUID(), 999993, 2, 'EhGroups', 1004795,'深圳特瑞姿生物科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004797, 0, 'ENTERPRISE', '深圳特瑞姿生物科技有限公司', 0, '', '/1004797', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113668, 240111044331054835, 'organization', 1004797, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004796, UUID(), '深圳市恒传精品酒业有限公司', '深圳市恒传精品酒业有限公司', 1, 1, 1004798, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183260, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183260, UUID(), 999993, 2, 'EhGroups', 1004796,'深圳市恒传精品酒业有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004798, 0, 'ENTERPRISE', '深圳市恒传精品酒业有限公司', 0, '', '/1004798', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113669, 240111044331054835, 'organization', 1004798, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004797, UUID(), '深圳市金华商会', '深圳市金华商会', 1, 1, 1004799, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183261, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183261, UUID(), 999993, 2, 'EhGroups', 1004797,'深圳市金华商会','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004799, 0, 'ENTERPRISE', '深圳市金华商会', 0, '', '/1004799', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113670, 240111044331054835, 'organization', 1004799, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004798, UUID(), '深圳市航天高科投资管理有限公司', '深圳市航天高科投资管理有限公司', 1, 1, 1004800, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183262, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183262, UUID(), 999993, 2, 'EhGroups', 1004798,'深圳市航天高科投资管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004800, 0, 'ENTERPRISE', '深圳市航天高科投资管理有限公司', 0, '', '/1004800', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113671, 240111044331054835, 'organization', 1004800, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004799, UUID(), '迪唯智环保科技（深圳）有限公司', '迪唯智环保科技（深圳）有限公司', 1, 1, 1004801, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183263, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183263, UUID(), 999993, 2, 'EhGroups', 1004799,'迪唯智环保科技（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004801, 0, 'ENTERPRISE', '迪唯智环保科技（深圳）有限公司', 0, '', '/1004801', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113672, 240111044331054835, 'organization', 1004801, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004800, UUID(), '深圳市金富瑞科技发展有限公司', '深圳市金富瑞科技发展有限公司', 1, 1, 1004802, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183264, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183264, UUID(), 999993, 2, 'EhGroups', 1004800,'深圳市金富瑞科技发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004802, 0, 'ENTERPRISE', '深圳市金富瑞科技发展有限公司', 0, '', '/1004802', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113673, 240111044331054835, 'organization', 1004802, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004801, UUID(), '深圳春天海岸医疗美容门诊部有限公司', '深圳春天海岸医疗美容门诊部有限公司', 1, 1, 1004803, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183265, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183265, UUID(), 999993, 2, 'EhGroups', 1004801,'深圳春天海岸医疗美容门诊部有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004803, 0, 'ENTERPRISE', '深圳春天海岸医疗美容门诊部有限公司', 0, '', '/1004803', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113674, 240111044331054835, 'organization', 1004803, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004802, UUID(), '万达豪商贸（深圳）有限公司', '万达豪商贸（深圳）有限公司', 1, 1, 1004804, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183266, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183266, UUID(), 999993, 2, 'EhGroups', 1004802,'万达豪商贸（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004804, 0, 'ENTERPRISE', '万达豪商贸（深圳）有限公司', 0, '', '/1004804', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113675, 240111044331054835, 'organization', 1004804, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004803, UUID(), '广东都源律师事务所', '广东都源律师事务所', 1, 1, 1004805, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183267, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183267, UUID(), 999993, 2, 'EhGroups', 1004803,'广东都源律师事务所','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004805, 0, 'ENTERPRISE', '广东都源律师事务所', 0, '', '/1004805', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113676, 240111044331054835, 'organization', 1004805, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004804, UUID(), '深圳市百世特欧达商贸信息咨询有限公司', '深圳市百世特欧达商贸信息咨询有限公司', 1, 1, 1004806, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183268, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183268, UUID(), 999993, 2, 'EhGroups', 1004804,'深圳市百世特欧达商贸信息咨询有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004806, 0, 'ENTERPRISE', '深圳市百世特欧达商贸信息咨询有限公司', 0, '', '/1004806', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113677, 240111044331054835, 'organization', 1004806, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004805, UUID(), '深圳市泰成五金有限公司', '深圳市泰成五金有限公司', 1, 1, 1004807, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183269, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183269, UUID(), 999993, 2, 'EhGroups', 1004805,'深圳市泰成五金有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004807, 0, 'ENTERPRISE', '深圳市泰成五金有限公司', 0, '', '/1004807', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113678, 240111044331054835, 'organization', 1004807, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004806, UUID(), '深圳市奥思瑞科技发展有限公司', '深圳市奥思瑞科技发展有限公司', 1, 1, 1004808, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183270, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183270, UUID(), 999993, 2, 'EhGroups', 1004806,'深圳市奥思瑞科技发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004808, 0, 'ENTERPRISE', '深圳市奥思瑞科技发展有限公司', 0, '', '/1004808', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113679, 240111044331054835, 'organization', 1004808, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004807, UUID(), '上海特殊陶业有限公司深圳分公司', '上海特殊陶业有限公司深圳分公司', 1, 1, 1004809, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183271, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183271, UUID(), 999993, 2, 'EhGroups', 1004807,'上海特殊陶业有限公司深圳分公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004809, 0, 'ENTERPRISE', '上海特殊陶业有限公司深圳分公司', 0, '', '/1004809', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113680, 240111044331054835, 'organization', 1004809, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004808, UUID(), '深圳市广聚投资控股集团有限公司', '深圳市广聚投资控股集团有限公司', 1, 1, 1004810, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183272, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183272, UUID(), 999993, 2, 'EhGroups', 1004808,'深圳市广聚投资控股集团有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004810, 0, 'ENTERPRISE', '深圳市广聚投资控股集团有限公司', 0, '', '/1004810', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113681, 240111044331054835, 'organization', 1004810, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004809, UUID(), '深圳南海国际融资租赁有限公司', '深圳南海国际融资租赁有限公司', 1, 1, 1004811, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183273, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183273, UUID(), 999993, 2, 'EhGroups', 1004809,'深圳南海国际融资租赁有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004811, 0, 'ENTERPRISE', '深圳南海国际融资租赁有限公司', 0, '', '/1004811', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113682, 240111044331054835, 'organization', 1004811, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004810, UUID(), '深圳市尹颖鸿福贸易有限公司', '深圳市尹颖鸿福贸易有限公司', 1, 1, 1004812, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183274, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183274, UUID(), 999993, 2, 'EhGroups', 1004810,'深圳市尹颖鸿福贸易有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004812, 0, 'ENTERPRISE', '深圳市尹颖鸿福贸易有限公司', 0, '', '/1004812', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113683, 240111044331054835, 'organization', 1004812, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004811, UUID(), '紫天经贸', '紫天经贸', 1, 1, 1004813, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183275, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183275, UUID(), 999993, 2, 'EhGroups', 1004811,'紫天经贸','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004813, 0, 'ENTERPRISE', '紫天经贸', 0, '', '/1004813', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113684, 240111044331054835, 'organization', 1004813, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004812, UUID(), '深圳市星云海资产管理有限公司', '深圳市星云海资产管理有限公司', 1, 1, 1004814, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183276, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183276, UUID(), 999993, 2, 'EhGroups', 1004812,'深圳市星云海资产管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004814, 0, 'ENTERPRISE', '深圳市星云海资产管理有限公司', 0, '', '/1004814', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113685, 240111044331054835, 'organization', 1004814, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004813, UUID(), '捷智安科技（深圳）有限公司', '捷智安科技（深圳）有限公司', 1, 1, 1004815, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183277, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183277, UUID(), 999993, 2, 'EhGroups', 1004813,'捷智安科技（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004815, 0, 'ENTERPRISE', '捷智安科技（深圳）有限公司', 0, '', '/1004815', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113686, 240111044331054835, 'organization', 1004815, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004814, UUID(), '泰利特无线通讯（深圳）有限公司', '泰利特无线通讯（深圳）有限公司', 1, 1, 1004816, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183278, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183278, UUID(), 999993, 2, 'EhGroups', 1004814,'泰利特无线通讯（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004816, 0, 'ENTERPRISE', '泰利特无线通讯（深圳）有限公司', 0, '', '/1004816', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113687, 240111044331054835, 'organization', 1004816, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004815, UUID(), '深圳乐善德源文化传播有限公司', '深圳乐善德源文化传播有限公司', 1, 1, 1004817, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183279, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183279, UUID(), 999993, 2, 'EhGroups', 1004815,'深圳乐善德源文化传播有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004817, 0, 'ENTERPRISE', '深圳乐善德源文化传播有限公司', 0, '', '/1004817', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113688, 240111044331054835, 'organization', 1004817, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004816, UUID(), '新福港房地产(深圳)有限公司', '新福港房地产(深圳)有限公司', 1, 1, 1004818, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183280, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183280, UUID(), 999993, 2, 'EhGroups', 1004816,'新福港房地产(深圳)有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004818, 0, 'ENTERPRISE', '新福港房地产(深圳)有限公司', 0, '', '/1004818', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113689, 240111044331054835, 'organization', 1004818, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004817, UUID(), '深圳市膳庭餐饮管理服务有限公司', '深圳市膳庭餐饮管理服务有限公司', 1, 1, 1004819, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183281, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183281, UUID(), 999993, 2, 'EhGroups', 1004817,'深圳市膳庭餐饮管理服务有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004819, 0, 'ENTERPRISE', '深圳市膳庭餐饮管理服务有限公司', 0, '', '/1004819', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113690, 240111044331054835, 'organization', 1004819, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004818, UUID(), '途家网', '途家网', 1, 1, 1004820, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183282, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183282, UUID(), 999993, 2, 'EhGroups', 1004818,'途家网','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004820, 0, 'ENTERPRISE', '途家网', 0, '', '/1004820', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113691, 240111044331054835, 'organization', 1004820, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004819, UUID(), '锦宫（香港）有限公司深圳代表处', '锦宫（香港）有限公司深圳代表处', 1, 1, 1004821, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183283, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183283, UUID(), 999993, 2, 'EhGroups', 1004819,'锦宫（香港）有限公司深圳代表处','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004821, 0, 'ENTERPRISE', '锦宫（香港）有限公司深圳代表处', 0, '', '/1004821', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113692, 240111044331054835, 'organization', 1004821, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004820, UUID(), '深圳市兴和投资有限公司', '深圳市兴和投资有限公司', 1, 1, 1004822, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183284, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183284, UUID(), 999993, 2, 'EhGroups', 1004820,'深圳市兴和投资有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004822, 0, 'ENTERPRISE', '深圳市兴和投资有限公司', 0, '', '/1004822', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113693, 240111044331054835, 'organization', 1004822, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004821, UUID(), '长江证券股份有限公司深圳分公司', '长江证券股份有限公司深圳分公司', 1, 1, 1004823, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183285, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183285, UUID(), 999993, 2, 'EhGroups', 1004821,'长江证券股份有限公司深圳分公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004823, 0, 'ENTERPRISE', '长江证券股份有限公司深圳分公司', 0, '', '/1004823', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113694, 240111044331054835, 'organization', 1004823, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004822, UUID(), '深圳快来意家私有限公司', '深圳快来意家私有限公司', 1, 1, 1004824, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183286, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183286, UUID(), 999993, 2, 'EhGroups', 1004822,'深圳快来意家私有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004824, 0, 'ENTERPRISE', '深圳快来意家私有限公司', 0, '', '/1004824', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113695, 240111044331054835, 'organization', 1004824, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004823, UUID(), '中国太平洋财产保险股份有限公司深圳分公司', '中国太平洋财产保险股份有限公司深圳分公司', 1, 1, 1004825, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183287, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183287, UUID(), 999993, 2, 'EhGroups', 1004823,'中国太平洋财产保险股份有限公司深圳分公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004825, 0, 'ENTERPRISE', '中国太平洋财产保险股份有限公司深圳分公司', 0, '', '/1004825', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113696, 240111044331054835, 'organization', 1004825, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004824, UUID(), '深圳可视工场数字科技有限公司', '深圳可视工场数字科技有限公司', 1, 1, 1004826, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183288, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183288, UUID(), 999993, 2, 'EhGroups', 1004824,'深圳可视工场数字科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004826, 0, 'ENTERPRISE', '深圳可视工场数字科技有限公司', 0, '', '/1004826', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113697, 240111044331054835, 'organization', 1004826, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004825, UUID(), '纯信资产管理有限公司', '纯信资产管理有限公司', 1, 1, 1004827, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183289, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183289, UUID(), 999993, 2, 'EhGroups', 1004825,'纯信资产管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004827, 0, 'ENTERPRISE', '纯信资产管理有限公司', 0, '', '/1004827', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113698, 240111044331054835, 'organization', 1004827, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004826, UUID(), '中国下一代教育基金会', '中国下一代教育基金会', 1, 1, 1004828, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183290, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183290, UUID(), 999993, 2, 'EhGroups', 1004826,'中国下一代教育基金会','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004828, 0, 'ENTERPRISE', '中国下一代教育基金会', 0, '', '/1004828', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113699, 240111044331054835, 'organization', 1004828, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004827, UUID(), '深圳市金牛牛金融服务有限公司', '深圳市金牛牛金融服务有限公司', 1, 1, 1004829, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183291, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183291, UUID(), 999993, 2, 'EhGroups', 1004827,'深圳市金牛牛金融服务有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004829, 0, 'ENTERPRISE', '深圳市金牛牛金融服务有限公司', 0, '', '/1004829', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113700, 240111044331054835, 'organization', 1004829, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004828, UUID(), '深圳市地博源实业有限公司', '深圳市地博源实业有限公司', 1, 1, 1004830, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183292, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183292, UUID(), 999993, 2, 'EhGroups', 1004828,'深圳市地博源实业有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004830, 0, 'ENTERPRISE', '深圳市地博源实业有限公司', 0, '', '/1004830', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113701, 240111044331054835, 'organization', 1004830, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004829, UUID(), '中科远洋国际贸易（深圳）有限公司', '中科远洋国际贸易（深圳）有限公司', 1, 1, 1004831, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183293, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183293, UUID(), 999993, 2, 'EhGroups', 1004829,'中科远洋国际贸易（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004831, 0, 'ENTERPRISE', '中科远洋国际贸易（深圳）有限公司', 0, '', '/1004831', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113702, 240111044331054835, 'organization', 1004831, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004830, UUID(), '深圳市大可信息咨询有限公司', '深圳市大可信息咨询有限公司', 1, 1, 1004832, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183294, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183294, UUID(), 999993, 2, 'EhGroups', 1004830,'深圳市大可信息咨询有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004832, 0, 'ENTERPRISE', '深圳市大可信息咨询有限公司', 0, '', '/1004832', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113703, 240111044331054835, 'organization', 1004832, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004831, UUID(), '三菱化学（中国）商贸有限公司深圳分公司', '三菱化学（中国）商贸有限公司深圳分公司', 1, 1, 1004833, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183295, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183295, UUID(), 999993, 2, 'EhGroups', 1004831,'三菱化学（中国）商贸有限公司深圳分公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004833, 0, 'ENTERPRISE', '三菱化学（中国）商贸有限公司深圳分公司', 0, '', '/1004833', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113704, 240111044331054835, 'organization', 1004833, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004832, UUID(), '东和电气(深圳)有限公司', '东和电气(深圳)有限公司', 1, 1, 1004834, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183296, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183296, UUID(), 999993, 2, 'EhGroups', 1004832,'东和电气(深圳)有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004834, 0, 'ENTERPRISE', '东和电气(深圳)有限公司', 0, '', '/1004834', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113705, 240111044331054835, 'organization', 1004834, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004833, UUID(), '华威新桃园', '华威新桃园', 1, 1, 1004835, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183297, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183297, UUID(), 999993, 2, 'EhGroups', 1004833,'华威新桃园','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004835, 0, 'ENTERPRISE', '华威新桃园', 0, '', '/1004835', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113706, 240111044331054835, 'organization', 1004835, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004834, UUID(), '中小企业(深圳)产业投资基金管理有限公司', '中小企业(深圳)产业投资基金管理有限公司', 1, 1, 1004836, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183298, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183298, UUID(), 999993, 2, 'EhGroups', 1004834,'中小企业(深圳)产业投资基金管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004836, 0, 'ENTERPRISE', '中小企业(深圳)产业投资基金管理有限公司', 0, '', '/1004836', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113707, 240111044331054835, 'organization', 1004836, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004835, UUID(), '雅克筑景设计（深圳）有限公司', '雅克筑景设计（深圳）有限公司', 1, 1, 1004837, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183299, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183299, UUID(), 999993, 2, 'EhGroups', 1004835,'雅克筑景设计（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004837, 0, 'ENTERPRISE', '雅克筑景设计（深圳）有限公司', 0, '', '/1004837', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113708, 240111044331054835, 'organization', 1004837, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004836, UUID(), '深圳兴泰股份有限公司', '深圳兴泰股份有限公司', 1, 1, 1004838, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183300, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183300, UUID(), 999993, 2, 'EhGroups', 1004836,'深圳兴泰股份有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004838, 0, 'ENTERPRISE', '深圳兴泰股份有限公司', 0, '', '/1004838', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113709, 240111044331054835, 'organization', 1004838, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004837, UUID(), '上海岩辰资产管理合伙企业（有限合伙）', '上海岩辰资产管理合伙企业（有限合伙）', 1, 1, 1004839, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183301, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183301, UUID(), 999993, 2, 'EhGroups', 1004837,'上海岩辰资产管理合伙企业（有限合伙）','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004839, 0, 'ENTERPRISE', '上海岩辰资产管理合伙企业（有限合伙）', 0, '', '/1004839', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113710, 240111044331054835, 'organization', 1004839, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004838, UUID(), '东亚银行', '东亚银行', 1, 1, 1004840, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183302, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183302, UUID(), 999993, 2, 'EhGroups', 1004838,'东亚银行','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004840, 0, 'ENTERPRISE', '东亚银行', 0, '', '/1004840', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113711, 240111044331054835, 'organization', 1004840, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004839, UUID(), '万维通', '万维通', 1, 1, 1004841, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183303, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183303, UUID(), 999993, 2, 'EhGroups', 1004839,'万维通','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004841, 0, 'ENTERPRISE', '万维通', 0, '', '/1004841', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113712, 240111044331054835, 'organization', 1004841, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004840, UUID(), '深圳市华智远电子科技有限公司', '深圳市华智远电子科技有限公司', 1, 1, 1004842, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183304, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183304, UUID(), 999993, 2, 'EhGroups', 1004840,'深圳市华智远电子科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004842, 0, 'ENTERPRISE', '深圳市华智远电子科技有限公司', 0, '', '/1004842', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113713, 240111044331054835, 'organization', 1004842, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004841, UUID(), '正孪健康研究发展有限公司', '正孪健康研究发展有限公司', 1, 1, 1004843, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183305, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183305, UUID(), 999993, 2, 'EhGroups', 1004841,'正孪健康研究发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004843, 0, 'ENTERPRISE', '正孪健康研究发展有限公司', 0, '', '/1004843', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113714, 240111044331054835, 'organization', 1004843, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004842, UUID(), '长江证券股份有限公司深圳分公司', '长江证券股份有限公司深圳分公司', 1, 1, 1004844, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183306, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183306, UUID(), 999993, 2, 'EhGroups', 1004842,'长江证券股份有限公司深圳分公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004844, 0, 'ENTERPRISE', '长江证券股份有限公司深圳分公司', 0, '', '/1004844', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113715, 240111044331054835, 'organization', 1004844, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004843, UUID(), '维新深圳代表处', '维新深圳代表处', 1, 1, 1004845, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183307, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183307, UUID(), 999993, 2, 'EhGroups', 1004843,'维新深圳代表处','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004845, 0, 'ENTERPRISE', '维新深圳代表处', 0, '', '/1004845', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113716, 240111044331054835, 'organization', 1004845, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004844, UUID(), '深圳市鑫洋船务有限公司', '深圳市鑫洋船务有限公司', 1, 1, 1004846, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183308, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183308, UUID(), 999993, 2, 'EhGroups', 1004844,'深圳市鑫洋船务有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004846, 0, 'ENTERPRISE', '深圳市鑫洋船务有限公司', 0, '', '/1004846', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113717, 240111044331054835, 'organization', 1004846, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004845, UUID(), '融达金融', '融达金融', 1, 1, 1004847, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183309, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183309, UUID(), 999993, 2, 'EhGroups', 1004845,'融达金融','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004847, 0, 'ENTERPRISE', '融达金融', 0, '', '/1004847', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113718, 240111044331054835, 'organization', 1004847, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004846, UUID(), '盈石企业管理（深圳）有限公司', '盈石企业管理（深圳）有限公司', 1, 1, 1004848, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183310, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183310, UUID(), 999993, 2, 'EhGroups', 1004846,'盈石企业管理（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004848, 0, 'ENTERPRISE', '盈石企业管理（深圳）有限公司', 0, '', '/1004848', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113719, 240111044331054835, 'organization', 1004848, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004847, UUID(), '香港International Supplychain (Asia) Limited深圳代表处', '香港International Supplychain (Asia) Limited深圳代表处', 1, 1, 1004849, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183311, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183311, UUID(), 999993, 2, 'EhGroups', 1004847,'香港International Supplychain (Asia) Limited深圳代表处','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004849, 0, 'ENTERPRISE', '香港International Supplychain (Asia) Limited深圳代表处', 0, '', '/1004849', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113720, 240111044331054835, 'organization', 1004849, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004848, UUID(), '奕丰金融服务（深圳）有限公司', '奕丰金融服务（深圳）有限公司', 1, 1, 1004850, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183312, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183312, UUID(), 999993, 2, 'EhGroups', 1004848,'奕丰金融服务（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004850, 0, 'ENTERPRISE', '奕丰金融服务（深圳）有限公司', 0, '', '/1004850', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113721, 240111044331054835, 'organization', 1004850, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004849, UUID(), '深圳市瑞祥海洋工程设备有限公司', '深圳市瑞祥海洋工程设备有限公司', 1, 1, 1004851, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183313, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183313, UUID(), 999993, 2, 'EhGroups', 1004849,'深圳市瑞祥海洋工程设备有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004851, 0, 'ENTERPRISE', '深圳市瑞祥海洋工程设备有限公司', 0, '', '/1004851', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113722, 240111044331054835, 'organization', 1004851, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004850, UUID(), '诗梦娜实业（深圳)有限公司', '诗梦娜实业（深圳)有限公司', 1, 1, 1004852, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183314, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183314, UUID(), 999993, 2, 'EhGroups', 1004850,'诗梦娜实业（深圳)有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004852, 0, 'ENTERPRISE', '诗梦娜实业（深圳)有限公司', 0, '', '/1004852', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113723, 240111044331054835, 'organization', 1004852, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004851, UUID(), '台积电（中国）有限公司', '台积电（中国）有限公司', 1, 1, 1004853, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183315, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183315, UUID(), 999993, 2, 'EhGroups', 1004851,'台积电（中国）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004853, 0, 'ENTERPRISE', '台积电（中国）有限公司', 0, '', '/1004853', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113724, 240111044331054835, 'organization', 1004853, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004852, UUID(), '深圳市天音科技发展有限公司', '深圳市天音科技发展有限公司', 1, 1, 1004854, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183316, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183316, UUID(), 999993, 2, 'EhGroups', 1004852,'深圳市天音科技发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004854, 0, 'ENTERPRISE', '深圳市天音科技发展有限公司', 0, '', '/1004854', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113725, 240111044331054835, 'organization', 1004854, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004853, UUID(), '鹏正律师', '鹏正律师', 1, 1, 1004855, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183317, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183317, UUID(), 999993, 2, 'EhGroups', 1004853,'鹏正律师','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004855, 0, 'ENTERPRISE', '鹏正律师', 0, '', '/1004855', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113726, 240111044331054835, 'organization', 1004855, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004854, UUID(), '深圳市融顺鑫担保有限公司', '深圳市融顺鑫担保有限公司', 1, 1, 1004856, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183318, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183318, UUID(), 999993, 2, 'EhGroups', 1004854,'深圳市融顺鑫担保有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004856, 0, 'ENTERPRISE', '深圳市融顺鑫担保有限公司', 0, '', '/1004856', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113727, 240111044331054835, 'organization', 1004856, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004855, UUID(), '帕莱德实业公司远东代表处', '帕莱德实业公司远东代表处', 1, 1, 1004857, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183319, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183319, UUID(), 999993, 2, 'EhGroups', 1004855,'帕莱德实业公司远东代表处','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004857, 0, 'ENTERPRISE', '帕莱德实业公司远东代表处', 0, '', '/1004857', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113728, 240111044331054835, 'organization', 1004857, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004856, UUID(), '深圳恺业管理咨询有限公司', '深圳恺业管理咨询有限公司', 1, 1, 1004858, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183320, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183320, UUID(), 999993, 2, 'EhGroups', 1004856,'深圳恺业管理咨询有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004858, 0, 'ENTERPRISE', '深圳恺业管理咨询有限公司', 0, '', '/1004858', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113729, 240111044331054835, 'organization', 1004858, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004857, UUID(), '喜而利幕墙工程有限公司', '喜而利幕墙工程有限公司', 1, 1, 1004859, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183321, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183321, UUID(), 999993, 2, 'EhGroups', 1004857,'喜而利幕墙工程有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004859, 0, 'ENTERPRISE', '喜而利幕墙工程有限公司', 0, '', '/1004859', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113730, 240111044331054835, 'organization', 1004859, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004858, UUID(), '深圳前海南方盛世互联网金融服务有限公司', '深圳前海南方盛世互联网金融服务有限公司', 1, 1, 1004860, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183322, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183322, UUID(), 999993, 2, 'EhGroups', 1004858,'深圳前海南方盛世互联网金融服务有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004860, 0, 'ENTERPRISE', '深圳前海南方盛世互联网金融服务有限公司', 0, '', '/1004860', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113731, 240111044331054835, 'organization', 1004860, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004859, UUID(), '济南培莘教育咨询有限公司（深圳分公司）', '济南培莘教育咨询有限公司（深圳分公司）', 1, 1, 1004861, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183323, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183323, UUID(), 999993, 2, 'EhGroups', 1004859,'济南培莘教育咨询有限公司（深圳分公司）','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004861, 0, 'ENTERPRISE', '济南培莘教育咨询有限公司（深圳分公司）', 0, '', '/1004861', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113732, 240111044331054835, 'organization', 1004861, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004860, UUID(), '1213A深圳市银鸿实业有限公司', '1213A深圳市银鸿实业有限公司', 1, 1, 1004862, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183324, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183324, UUID(), 999993, 2, 'EhGroups', 1004860,'1213A深圳市银鸿实业有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004862, 0, 'ENTERPRISE', '1213A深圳市银鸿实业有限公司', 0, '', '/1004862', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113733, 240111044331054835, 'organization', 1004862, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004861, UUID(), '深圳市宝余投资管理有限公司', '深圳市宝余投资管理有限公司', 1, 1, 1004863, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183325, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183325, UUID(), 999993, 2, 'EhGroups', 1004861,'深圳市宝余投资管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004863, 0, 'ENTERPRISE', '深圳市宝余投资管理有限公司', 0, '', '/1004863', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113734, 240111044331054835, 'organization', 1004863, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004862, UUID(), '深圳市和硕投资管理咨询有限公司', '深圳市和硕投资管理咨询有限公司', 1, 1, 1004864, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183326, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183326, UUID(), 999993, 2, 'EhGroups', 1004862,'深圳市和硕投资管理咨询有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004864, 0, 'ENTERPRISE', '深圳市和硕投资管理咨询有限公司', 0, '', '/1004864', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113735, 240111044331054835, 'organization', 1004864, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004863, UUID(), '深圳峨星贸易有限公司', '深圳峨星贸易有限公司', 1, 1, 1004865, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183327, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183327, UUID(), 999993, 2, 'EhGroups', 1004863,'深圳峨星贸易有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004865, 0, 'ENTERPRISE', '深圳峨星贸易有限公司', 0, '', '/1004865', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113736, 240111044331054835, 'organization', 1004865, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004864, UUID(), '深圳市拓元河谷设计顾问有限公司', '深圳市拓元河谷设计顾问有限公司', 1, 1, 1004866, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183328, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183328, UUID(), 999993, 2, 'EhGroups', 1004864,'深圳市拓元河谷设计顾问有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004866, 0, 'ENTERPRISE', '深圳市拓元河谷设计顾问有限公司', 0, '', '/1004866', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113737, 240111044331054835, 'organization', 1004866, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004865, UUID(), '天晟管理咨询（深圳）有限公司', '天晟管理咨询（深圳）有限公司', 1, 1, 1004867, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183329, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183329, UUID(), 999993, 2, 'EhGroups', 1004865,'天晟管理咨询（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004867, 0, 'ENTERPRISE', '天晟管理咨询（深圳）有限公司', 0, '', '/1004867', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113738, 240111044331054835, 'organization', 1004867, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004866, UUID(), '汕头市美致模型有限公司', '汕头市美致模型有限公司', 1, 1, 1004868, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183330, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183330, UUID(), 999993, 2, 'EhGroups', 1004866,'汕头市美致模型有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004868, 0, 'ENTERPRISE', '汕头市美致模型有限公司', 0, '', '/1004868', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113739, 240111044331054835, 'organization', 1004868, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004867, UUID(), '泰利特无线通讯（深圳）有限公司', '泰利特无线通讯（深圳）有限公司', 1, 1, 1004869, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183331, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183331, UUID(), 999993, 2, 'EhGroups', 1004867,'泰利特无线通讯（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004869, 0, 'ENTERPRISE', '泰利特无线通讯（深圳）有限公司', 0, '', '/1004869', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113740, 240111044331054835, 'organization', 1004869, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004868, UUID(), '维景资本', '维景资本', 1, 1, 1004870, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183332, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183332, UUID(), 999993, 2, 'EhGroups', 1004868,'维景资本','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004870, 0, 'ENTERPRISE', '维景资本', 0, '', '/1004870', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113741, 240111044331054835, 'organization', 1004870, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004869, UUID(), '日本株式会社佳帕纳深圳代表处', '日本株式会社佳帕纳深圳代表处', 1, 1, 1004871, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183333, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183333, UUID(), 999993, 2, 'EhGroups', 1004869,'日本株式会社佳帕纳深圳代表处','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004871, 0, 'ENTERPRISE', '日本株式会社佳帕纳深圳代表处', 0, '', '/1004871', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113742, 240111044331054835, 'organization', 1004871, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004870, UUID(), '长江证券股份有限公司深圳后海海岸城证券营业部', '长江证券股份有限公司深圳后海海岸城证券营业部', 1, 1, 1004872, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183334, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183334, UUID(), 999993, 2, 'EhGroups', 1004870,'长江证券股份有限公司深圳后海海岸城证券营业部','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004872, 0, 'ENTERPRISE', '长江证券股份有限公司深圳后海海岸城证券营业部', 0, '', '/1004872', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113743, 240111044331054835, 'organization', 1004872, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004871, UUID(), '安信证券股份有限公司深圳海岸城海德三道证券营业部', '安信证券股份有限公司深圳海岸城海德三道证券营业部', 1, 1, 1004873, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183335, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183335, UUID(), 999993, 2, 'EhGroups', 1004871,'安信证券股份有限公司深圳海岸城海德三道证券营业部','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004873, 0, 'ENTERPRISE', '安信证券股份有限公司深圳海岸城海德三道证券营业部', 0, '', '/1004873', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113744, 240111044331054835, 'organization', 1004873, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004872, UUID(), '优客工场（深圳）创业服务有限公司', '优客工场（深圳）创业服务有限公司', 1, 1, 1004874, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183336, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183336, UUID(), 999993, 2, 'EhGroups', 1004872,'优客工场（深圳）创业服务有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004874, 0, 'ENTERPRISE', '优客工场（深圳）创业服务有限公司', 0, '', '/1004874', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113745, 240111044331054835, 'organization', 1004874, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004873, UUID(), '深圳市煜名城形象策划有限公司', '深圳市煜名城形象策划有限公司', 1, 1, 1004875, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183337, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183337, UUID(), 999993, 2, 'EhGroups', 1004873,'深圳市煜名城形象策划有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004875, 0, 'ENTERPRISE', '深圳市煜名城形象策划有限公司', 0, '', '/1004875', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113746, 240111044331054835, 'organization', 1004875, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004874, UUID(), '深圳招商国际旅游有限公司海月营业部', '深圳招商国际旅游有限公司海月营业部', 1, 1, 1004876, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183338, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183338, UUID(), 999993, 2, 'EhGroups', 1004874,'深圳招商国际旅游有限公司海月营业部','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004876, 0, 'ENTERPRISE', '深圳招商国际旅游有限公司海月营业部', 0, '', '/1004876', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113747, 240111044331054835, 'organization', 1004876, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004875, UUID(), '香港迪杰欧亚太有限公司深圳代表处', '香港迪杰欧亚太有限公司深圳代表处', 1, 1, 1004877, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183339, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183339, UUID(), 999993, 2, 'EhGroups', 1004875,'香港迪杰欧亚太有限公司深圳代表处','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004877, 0, 'ENTERPRISE', '香港迪杰欧亚太有限公司深圳代表处', 0, '', '/1004877', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113748, 240111044331054835, 'organization', 1004877, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004876, UUID(), '信凌实业有限公司（泓首翔电器（深圳）有限公司）', '信凌实业有限公司（泓首翔电器（深圳）有限公司）', 1, 1, 1004878, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183340, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183340, UUID(), 999993, 2, 'EhGroups', 1004876,'信凌实业有限公司（泓首翔电器（深圳）有限公司）','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004878, 0, 'ENTERPRISE', '信凌实业有限公司（泓首翔电器（深圳）有限公司）', 0, '', '/1004878', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113749, 240111044331054835, 'organization', 1004878, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004877, UUID(), '苏州同源投资顾问有限公司', '苏州同源投资顾问有限公司', 1, 1, 1004879, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183341, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183341, UUID(), 999993, 2, 'EhGroups', 1004877,'苏州同源投资顾问有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004879, 0, 'ENTERPRISE', '苏州同源投资顾问有限公司', 0, '', '/1004879', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113750, 240111044331054835, 'organization', 1004879, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004878, UUID(), '深圳帧观德芯科技有限公司', '深圳帧观德芯科技有限公司', 1, 1, 1004880, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183342, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183342, UUID(), 999993, 2, 'EhGroups', 1004878,'深圳帧观德芯科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004880, 0, 'ENTERPRISE', '深圳帧观德芯科技有限公司', 0, '', '/1004880', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113751, 240111044331054835, 'organization', 1004880, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004879, UUID(), '深圳拱石创新科技有限公司', '深圳拱石创新科技有限公司', 1, 1, 1004881, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183343, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183343, UUID(), 999993, 2, 'EhGroups', 1004879,'深圳拱石创新科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004881, 0, 'ENTERPRISE', '深圳拱石创新科技有限公司', 0, '', '/1004881', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113752, 240111044331054835, 'organization', 1004881, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004880, UUID(), '深圳市亚动机械有限公司', '深圳市亚动机械有限公司', 1, 1, 1004882, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183344, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183344, UUID(), 999993, 2, 'EhGroups', 1004880,'深圳市亚动机械有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004882, 0, 'ENTERPRISE', '深圳市亚动机械有限公司', 0, '', '/1004882', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113753, 240111044331054835, 'organization', 1004882, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004881, UUID(), '深圳市承熙科技开发有限公司', '深圳市承熙科技开发有限公司', 1, 1, 1004883, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183345, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183345, UUID(), 999993, 2, 'EhGroups', 1004881,'深圳市承熙科技开发有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004883, 0, 'ENTERPRISE', '深圳市承熙科技开发有限公司', 0, '', '/1004883', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113754, 240111044331054835, 'organization', 1004883, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004882, UUID(), '深圳市永豪网络推广有限公司', '深圳市永豪网络推广有限公司', 1, 1, 1004884, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183346, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183346, UUID(), 999993, 2, 'EhGroups', 1004882,'深圳市永豪网络推广有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004884, 0, 'ENTERPRISE', '深圳市永豪网络推广有限公司', 0, '', '/1004884', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113755, 240111044331054835, 'organization', 1004884, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004883, UUID(), '深圳市金发拉比妇婴童用品有限公司', '深圳市金发拉比妇婴童用品有限公司', 1, 1, 1004885, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183347, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183347, UUID(), 999993, 2, 'EhGroups', 1004883,'深圳市金发拉比妇婴童用品有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004885, 0, 'ENTERPRISE', '深圳市金发拉比妇婴童用品有限公司', 0, '', '/1004885', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113756, 240111044331054835, 'organization', 1004885, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004884, UUID(), '深圳市甜与乐国际商贸有限公司', '深圳市甜与乐国际商贸有限公司', 1, 1, 1004886, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183348, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183348, UUID(), 999993, 2, 'EhGroups', 1004884,'深圳市甜与乐国际商贸有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004886, 0, 'ENTERPRISE', '深圳市甜与乐国际商贸有限公司', 0, '', '/1004886', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113757, 240111044331054835, 'organization', 1004886, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004885, UUID(), '深圳市海纳汇通实业发展有限公司', '深圳市海纳汇通实业发展有限公司', 1, 1, 1004887, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183349, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183349, UUID(), 999993, 2, 'EhGroups', 1004885,'深圳市海纳汇通实业发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004887, 0, 'ENTERPRISE', '深圳市海纳汇通实业发展有限公司', 0, '', '/1004887', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113758, 240111044331054835, 'organization', 1004887, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004886, UUID(), '天津威盛电子有限公司', '天津威盛电子有限公司', 1, 1, 1004888, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183350, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183350, UUID(), 999993, 2, 'EhGroups', 1004886,'天津威盛电子有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004888, 0, 'ENTERPRISE', '天津威盛电子有限公司', 0, '', '/1004888', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113759, 240111044331054835, 'organization', 1004888, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004887, UUID(), '英属维尔京群岛', '英属维尔京群岛', 1, 1, 1004889, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183351, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183351, UUID(), 999993, 2, 'EhGroups', 1004887,'英属维尔京群岛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004889, 0, 'ENTERPRISE', '英属维尔京群岛', 0, '', '/1004889', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113760, 240111044331054835, 'organization', 1004889, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004888, UUID(), '深圳市优佰融合教育咨询有限公司', '深圳市优佰融合教育咨询有限公司', 1, 1, 1004890, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183352, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183352, UUID(), 999993, 2, 'EhGroups', 1004888,'深圳市优佰融合教育咨询有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004890, 0, 'ENTERPRISE', '深圳市优佰融合教育咨询有限公司', 0, '', '/1004890', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113761, 240111044331054835, 'organization', 1004890, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004889, UUID(), '英海特工程咨询（北京）有限公司深圳分公司', '英海特工程咨询（北京）有限公司深圳分公司', 1, 1, 1004891, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183353, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183353, UUID(), 999993, 2, 'EhGroups', 1004889,'英海特工程咨询（北京）有限公司深圳分公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004891, 0, 'ENTERPRISE', '英海特工程咨询（北京）有限公司深圳分公司', 0, '', '/1004891', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113762, 240111044331054835, 'organization', 1004891, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004890, UUID(), '深圳市鼎丰时代担保投资有限公司', '深圳市鼎丰时代担保投资有限公司', 1, 1, 1004892, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183354, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183354, UUID(), 999993, 2, 'EhGroups', 1004890,'深圳市鼎丰时代担保投资有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004892, 0, 'ENTERPRISE', '深圳市鼎丰时代担保投资有限公司', 0, '', '/1004892', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113763, 240111044331054835, 'organization', 1004892, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004891, UUID(), '深圳市弘业源文化传播有限公司', '深圳市弘业源文化传播有限公司', 1, 1, 1004893, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183355, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183355, UUID(), 999993, 2, 'EhGroups', 1004891,'深圳市弘业源文化传播有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004893, 0, 'ENTERPRISE', '深圳市弘业源文化传播有限公司', 0, '', '/1004893', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113764, 240111044331054835, 'organization', 1004893, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004892, UUID(), '华创建筑设计有限公司深圳分公司', '华创建筑设计有限公司深圳分公司', 1, 1, 1004894, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183356, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183356, UUID(), 999993, 2, 'EhGroups', 1004892,'华创建筑设计有限公司深圳分公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004894, 0, 'ENTERPRISE', '华创建筑设计有限公司深圳分公司', 0, '', '/1004894', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113765, 240111044331054835, 'organization', 1004894, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004893, UUID(), '日写深圳商贸有限公司', '日写深圳商贸有限公司', 1, 1, 1004895, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183357, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183357, UUID(), 999993, 2, 'EhGroups', 1004893,'日写深圳商贸有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004895, 0, 'ENTERPRISE', '日写深圳商贸有限公司', 0, '', '/1004895', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113766, 240111044331054835, 'organization', 1004895, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004894, UUID(), '深圳市芳昌实业发展有限公司', '深圳市芳昌实业发展有限公司', 1, 1, 1004896, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183358, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183358, UUID(), 999993, 2, 'EhGroups', 1004894,'深圳市芳昌实业发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004896, 0, 'ENTERPRISE', '深圳市芳昌实业发展有限公司', 0, '', '/1004896', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113767, 240111044331054835, 'organization', 1004896, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004895, UUID(), '深圳市健禄投资有限公司', '深圳市健禄投资有限公司', 1, 1, 1004897, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183359, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183359, UUID(), 999993, 2, 'EhGroups', 1004895,'深圳市健禄投资有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004897, 0, 'ENTERPRISE', '深圳市健禄投资有限公司', 0, '', '/1004897', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113768, 240111044331054835, 'organization', 1004897, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004896, UUID(), '凯瑞雯（深圳）投资贸易有限公司', '凯瑞雯（深圳）投资贸易有限公司', 1, 1, 1004898, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183360, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183360, UUID(), 999993, 2, 'EhGroups', 1004896,'凯瑞雯（深圳）投资贸易有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004898, 0, 'ENTERPRISE', '凯瑞雯（深圳）投资贸易有限公司', 0, '', '/1004898', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113769, 240111044331054835, 'organization', 1004898, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004897, UUID(), '北京凯撒国际旅行社有限责任公司深圳分公司', '北京凯撒国际旅行社有限责任公司深圳分公司', 1, 1, 1004899, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183361, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183361, UUID(), 999993, 2, 'EhGroups', 1004897,'北京凯撒国际旅行社有限责任公司深圳分公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004899, 0, 'ENTERPRISE', '北京凯撒国际旅行社有限责任公司深圳分公司', 0, '', '/1004899', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113770, 240111044331054835, 'organization', 1004899, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004898, UUID(), '西部证券股份有限公司', '西部证券股份有限公司', 1, 1, 1004900, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183362, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183362, UUID(), 999993, 2, 'EhGroups', 1004898,'西部证券股份有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004900, 0, 'ENTERPRISE', '西部证券股份有限公司', 0, '', '/1004900', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113771, 240111044331054835, 'organization', 1004900, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004899, UUID(), '深圳市绿海城集团有限公司', '深圳市绿海城集团有限公司', 1, 1, 1004901, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183363, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183363, UUID(), 999993, 2, 'EhGroups', 1004899,'深圳市绿海城集团有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004901, 0, 'ENTERPRISE', '深圳市绿海城集团有限公司', 0, '', '/1004901', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113772, 240111044331054835, 'organization', 1004901, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004900, UUID(), '五矿经易期货有限公司', '五矿经易期货有限公司', 1, 1, 1004902, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183364, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183364, UUID(), 999993, 2, 'EhGroups', 1004900,'五矿经易期货有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004902, 0, 'ENTERPRISE', '五矿经易期货有限公司', 0, '', '/1004902', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113773, 240111044331054835, 'organization', 1004902, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004901, UUID(), '深圳市南江小额贷款有限公司', '深圳市南江小额贷款有限公司', 1, 1, 1004903, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183365, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183365, UUID(), 999993, 2, 'EhGroups', 1004901,'深圳市南江小额贷款有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004903, 0, 'ENTERPRISE', '深圳市南江小额贷款有限公司', 0, '', '/1004903', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113774, 240111044331054835, 'organization', 1004903, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004902, UUID(), '深圳中远保险经纪有限公司', '深圳中远保险经纪有限公司', 1, 1, 1004904, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183366, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183366, UUID(), 999993, 2, 'EhGroups', 1004902,'深圳中远保险经纪有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004904, 0, 'ENTERPRISE', '深圳中远保险经纪有限公司', 0, '', '/1004904', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113775, 240111044331054835, 'organization', 1004904, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004903, UUID(), '深圳市中通动力设备有限公司', '深圳市中通动力设备有限公司', 1, 1, 1004905, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183367, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183367, UUID(), 999993, 2, 'EhGroups', 1004903,'深圳市中通动力设备有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004905, 0, 'ENTERPRISE', '深圳市中通动力设备有限公司', 0, '', '/1004905', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113776, 240111044331054835, 'organization', 1004905, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004904, UUID(), '华泰证券股份有限责任公司', '华泰证券股份有限责任公司', 1, 1, 1004906, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183368, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183368, UUID(), 999993, 2, 'EhGroups', 1004904,'华泰证券股份有限责任公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004906, 0, 'ENTERPRISE', '华泰证券股份有限责任公司', 0, '', '/1004906', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113777, 240111044331054835, 'organization', 1004906, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004905, UUID(), '小西机电贸易（深圳）有限公司', '小西机电贸易（深圳）有限公司', 1, 1, 1004907, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183369, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183369, UUID(), 999993, 2, 'EhGroups', 1004905,'小西机电贸易（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004907, 0, 'ENTERPRISE', '小西机电贸易（深圳）有限公司', 0, '', '/1004907', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113778, 240111044331054835, 'organization', 1004907, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004906, UUID(), '宇览商贸（深圳）有限公司', '宇览商贸（深圳）有限公司', 1, 1, 1004908, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183370, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183370, UUID(), 999993, 2, 'EhGroups', 1004906,'宇览商贸（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004908, 0, 'ENTERPRISE', '宇览商贸（深圳）有限公司', 0, '', '/1004908', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113779, 240111044331054835, 'organization', 1004908, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004908, UUID(), '清珠（北京）创意文化发展有限公司', '清珠（北京）创意文化发展有限公司', 1, 1, 1004910, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183372, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183372, UUID(), 999993, 2, 'EhGroups', 1004908,'清珠（北京）创意文化发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004910, 0, 'ENTERPRISE', '清珠（北京）创意文化发展有限公司', 0, '', '/1004910', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113781, 240111044331054835, 'organization', 1004910, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004909, UUID(), '深圳市前海众贤投资有限公司', '深圳市前海众贤投资有限公司', 1, 1, 1004911, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183373, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183373, UUID(), 999993, 2, 'EhGroups', 1004909,'深圳市前海众贤投资有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004911, 0, 'ENTERPRISE', '深圳市前海众贤投资有限公司', 0, '', '/1004911', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113782, 240111044331054835, 'organization', 1004911, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004910, UUID(), '深圳市金石众人财富管理有限公司', '深圳市金石众人财富管理有限公司', 1, 1, 1004912, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183374, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183374, UUID(), 999993, 2, 'EhGroups', 1004910,'深圳市金石众人财富管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004912, 0, 'ENTERPRISE', '深圳市金石众人财富管理有限公司', 0, '', '/1004912', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113783, 240111044331054835, 'organization', 1004912, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004911, UUID(), '深圳市润德集团有限公司', '深圳市润德集团有限公司', 1, 1, 1004913, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183375, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183375, UUID(), 999993, 2, 'EhGroups', 1004911,'深圳市润德集团有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004913, 0, 'ENTERPRISE', '深圳市润德集团有限公司', 0, '', '/1004913', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113784, 240111044331054835, 'organization', 1004913, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004912, UUID(), '深圳市中油润德销售有限公司', '深圳市中油润德销售有限公司', 1, 1, 1004914, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183376, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183376, UUID(), 999993, 2, 'EhGroups', 1004912,'深圳市中油润德销售有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004914, 0, 'ENTERPRISE', '深圳市中油润德销售有限公司', 0, '', '/1004914', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113785, 240111044331054835, 'organization', 1004914, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004913, UUID(), '美德维实洛克（无锡）定量泵有限公司深圳分公司', '美德维实洛克（无锡）定量泵有限公司深圳分公司', 1, 1, 1004915, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183377, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183377, UUID(), 999993, 2, 'EhGroups', 1004913,'美德维实洛克（无锡）定量泵有限公司深圳分公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004915, 0, 'ENTERPRISE', '美德维实洛克（无锡）定量泵有限公司深圳分公司', 0, '', '/1004915', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113786, 240111044331054835, 'organization', 1004915, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004914, UUID(), '鑫恒美融资租赁有限公司', '鑫恒美融资租赁有限公司', 1, 1, 1004916, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183378, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183378, UUID(), 999993, 2, 'EhGroups', 1004914,'鑫恒美融资租赁有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004916, 0, 'ENTERPRISE', '鑫恒美融资租赁有限公司', 0, '', '/1004916', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113787, 240111044331054835, 'organization', 1004916, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004915, UUID(), '中国移动通信集体广东有限公司', '中国移动通信集体广东有限公司', 1, 1, 1004917, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183379, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183379, UUID(), 999993, 2, 'EhGroups', 1004915,'中国移动通信集体广东有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004917, 0, 'ENTERPRISE', '中国移动通信集体广东有限公司', 0, '', '/1004917', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113788, 240111044331054835, 'organization', 1004917, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004916, UUID(), '深圳市华域普风设计有限公司', '深圳市华域普风设计有限公司', 1, 1, 1004918, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183380, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183380, UUID(), 999993, 2, 'EhGroups', 1004916,'深圳市华域普风设计有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004918, 0, 'ENTERPRISE', '深圳市华域普风设计有限公司', 0, '', '/1004918', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113789, 240111044331054835, 'organization', 1004918, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004920, UUID(), '国泰君安证券股份有限公司深圳海德三道证券营业部', '国泰君安证券股份有限公司深圳海德三道证券营业部', 1, 1, 1004922, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183384, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183384, UUID(), 999993, 2, 'EhGroups', 1004920,'国泰君安证券股份有限公司深圳海德三道证券营业部','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004922, 0, 'ENTERPRISE', '国泰君安证券股份有限公司深圳海德三道证券营业部', 0, '', '/1004922', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113793, 240111044331054835, 'organization', 1004922, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004921, UUID(), '双日塑料（深圳）有限公司', '双日塑料（深圳）有限公司', 1, 1, 1004923, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183385, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183385, UUID(), 999993, 2, 'EhGroups', 1004921,'双日塑料（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004923, 0, 'ENTERPRISE', '双日塑料（深圳）有限公司', 0, '', '/1004923', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113794, 240111044331054835, 'organization', 1004923, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004922, UUID(), '亚太华企有限公司(香港亚太海运有限公司深圳代表处)', '亚太华企有限公司(香港亚太海运有限公司深圳代表处)', 1, 1, 1004924, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183386, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183386, UUID(), 999993, 2, 'EhGroups', 1004922,'亚太华企有限公司(香港亚太海运有限公司深圳代表处)','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004924, 0, 'ENTERPRISE', '亚太华企有限公司(香港亚太海运有限公司深圳代表处)', 0, '', '/1004924', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113795, 240111044331054835, 'organization', 1004924, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004923, UUID(), '数码士', '数码士', 1, 1, 1004925, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183387, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183387, UUID(), 999993, 2, 'EhGroups', 1004923,'数码士','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004925, 0, 'ENTERPRISE', '数码士', 0, '', '/1004925', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113796, 240111044331054835, 'organization', 1004925, 3, 0, UTC_TIMESTAMP());

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004925, UUID(), '惠州市西太华实业有限公司', '惠州市西太华实业有限公司', 1, 1, 1004927, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183389, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183389, UUID(), 999993, 2, 'EhGroups', 1004925,'惠州市西太华实业有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004927, 0, 'ENTERPRISE', '惠州市西太华实业有限公司', 0, '', '/1004927', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113798, 240111044331054835, 'organization', 1004927, 3, 0, UTC_TIMESTAMP());

--
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004938, UUID(), '深圳知租网房地产信息咨询有限公司', '深圳知租网房地产信息咨询有限公司', 1, 1, 1004921, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183383, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183383, UUID(), 999993, 2, 'EhGroups', 1004938,'深圳知租网房地产信息咨询有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004921, 0, 'ENTERPRISE', '深圳知租网房地产信息咨询有限公司', 0, '', '/1004921', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113792, 240111044331054835, 'organization', 1004921, 3, 0, UTC_TIMESTAMP());		
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004935, UUID(), '深圳市圆成投资有限公司', '深圳市圆成投资有限公司', 1, 1, 1004909, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183371, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183371, UUID(), 999993, 2, 'EhGroups', 1004935,'深圳市圆成投资有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004909, 0, 'ENTERPRISE', '深圳市圆成投资有限公司', 0, '', '/1004909', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113780, 240111044331054835, 'organization', 1004909, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004936, UUID(), '深圳市彩众传媒有限公司', '深圳市彩众传媒有限公司', 1, 1, 1004919, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183381, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183381, UUID(), 999993, 2, 'EhGroups', 1004936,'深圳市彩众传媒有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004919, 0, 'ENTERPRISE', '深圳市彩众传媒有限公司', 0, '', '/1004919', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113790, 240111044331054835, 'organization', 1004919, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004937, UUID(), '深圳市森森海实业有限公司', '深圳市森森海实业有限公司', 1, 1, 1004920, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183382, 1, 999993);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183382, UUID(), 999993, 2, 'EhGroups', 1004937,'深圳市森森海实业有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004920, 0, 'ENTERPRISE', '深圳市森森海实业有限公司', 0, '', '/1004920', 1, 2, 'ENTERPRISE', 999993);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113791, 240111044331054835, 'organization', 1004920, 3, 0, UTC_TIMESTAMP());

	
	
-- 物业缴费 初始数据
INSERT INTO `eh_pmsy_communities` (`id`, `namespace_id`, `community_id`, `community_token`, `contact`, `bill_tip`) 
	VALUES ('1', '0', '240111044331054835', '00100120131200000015', '075523685550', '<p>请在每月20日之前及时缴纳上月费用，否则将产生滞纳金。</p>');


-- 海岸增加打卡
SET @id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
 
INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES ((@id := @id + 1), 999993, 0, 0, 0, '/home', 'Bizs', 'PUNCH', '打卡考勤', 'cs://1/image/aW1hZ2UvTVRwa05ERTJaRGN4TXpZME5USXdNR0V4TlRkbU1HRTNaR1U0TVdZNVpHUTFOdw', '1', '1', '23', '', 0, 0, 1, 1, '', '0', NULL, NULL, NULL, '1', 'pm_admin');

INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES ((@id := @id + 1), 999993, 0, 0, 0, '/home', 'Bizs', 'PUNCH', '打卡考勤', 'cs://1/image/aW1hZ2UvTVRwa05ERTJaRGN4TXpZME5USXdNR0V4TlRkbU1HRTNaR1U0TVdZNVpHUTFOdw', '1', '1', '23', '', 0, 0, 1, 1, '', '0', NULL, NULL, NULL, '1', 'park_tourist');
  
  
  
-- added by wh  2017-4-18 去掉优惠券入口，左邻小站ICON图片更换
DELETE FROM eh_launch_pad_items WHERE namespace_id = 999993 AND item_label = '优惠券';
UPDATE eh_launch_pad_items SET item_width = 1, icon_uri = 'cs://1/image/aW1hZ2UvTVRwaFpUYzBOakJsTW1aak9URmxZV1ZqTnpZNFpXSmxNREJsTURNd05EbGxZUQ' WHERE namespace_id = 999993 AND item_label = '左邻小站';
UPDATE eh_launch_pad_layouts SET version_code ='2017041802', layout_json ='{"versionCode":"2017041802","versionName":"3.0.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"Coupons"},"style":"Gallery","defaultOrder":3,"columnCount":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0}]}' WHERE  namespace_id = 999993 AND NAME ='ServiceMarketLayout' AND scene_type = 'pm_admin';
UPDATE eh_launch_pad_layouts SET version_code ='2017041802', layout_json ='{"versionCode":"2017041802","versionName":"3.0.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"Coupons"},"style":"Gallery","defaultOrder":3,"columnCount":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0}]}' WHERE  namespace_id = 999993 AND NAME ='ServiceMarketLayout' AND scene_type = 'park_tourist';


-- added by wh 2017-4-21  add door managerment 海岸加门禁
SET @id = (SELECT MAX(id) FROM eh_launch_pad_items);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) 
	VALUES ((@id := @id +1), '999993', '0', '0', '0', '/home', 'Bizs', 'DoorManagement', '门禁', 'cs://1/image/aW1hZ2UvTVRwak9EbGhPVE16TTJWa05UTTVZbVUwWVRRM04ySTBORGt4WWpFME1UVmxaQQ', '1', '1', '40', '{\"isSupportQR\":1,\"isSupportSmart\":1}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist', '1', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) 
	VALUES ((@id := @id +1), '999993', '0', '0', '0', '/home', 'Bizs', 'DoorManagement', '门禁', 'cs://1/image/aW1hZ2UvTVRwak9EbGhPVE16TTJWa05UTTVZbVUwWVRRM04ySTBORGt4WWpFME1UVmxaQQ', '1', '1', '40', '{\"isSupportQR\":1,\"isSupportSmart\":1}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin', '1', NULL);


SET @id = (SELECT MAX(id) FROM eh_web_menu_scopes);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@id := @id +1),'41000','','EhNamespaces','999993','2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@id := @id +1),'41010','','EhNamespaces','999993','2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@id := @id +1),'41020','','EhNamespaces','999993','2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@id := @id +1),'41030','','EhNamespaces','999993','2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@id := @id +1),'41040','','EhNamespaces','999993','2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@id := @id +1),'41050','','EhNamespaces','999993','2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@id := @id +1),'41060','','EhNamespaces','999993','2');