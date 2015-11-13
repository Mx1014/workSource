INSERT INTO `ehcore`.`eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`,  `avatar`,  `status`,  `points`,  `level`,  `gender`,  `locale`,  `salt`,  `password_hash`)
	VALUES (193501, UUID(), '9200857', '徐明晓', 'cs://1/image/aW1hZ2UvTVRvMU1EQTVZVEZrTkdVek9EQXhZbVE0WlRZd1l6UXdOVE0zWVdJNFkyTmlNUQ', 1, 45, '1', '0',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199');
INSERT INTO `ehcore`.`eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`)
	VALUES (190857,  193501,  '0',  '13632650699',  '221616',  3);

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`) 
		VALUES(1760000, 0, 'PARTNER', '科技园版', 0, '', '/1000000', 1, 2);
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`) 
		VALUES(1760001, 0, 'GANC', '科技园物业', 0, '', '/1000001', 1, 2);


INSERT INTO `eh_forums`(`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
		VALUES(176520, UUID(), 0, 2, '', 0, '科技园论坛', '', 0, 0, UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_forums`(`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
		VALUES(176521, UUID(), 0, 2, '', 0, '科技园意见反馈论坛', '', 0, 0, UTC_TIMESTAMP(), UTC_TIMESTAMP());	
	
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`) 
	VALUES( 240111044331046562, UUID(), 13905, '深圳市',  13908, '南山区', '科技园', '科技工业园', '深圳市南山区科技园', NULL, '深圳科技工业园是我国大陆第一个高新科技产业园', '深圳科技工业园是我国大陆第一个高新科技产业园，于1985年由深圳市政府和中国科学院共同创办。1991年，经国务院批准，深圳科技工业园成为首批国家级高新技术产业园区。\r\n\r\n园区占地面积1.15平方公里，经过不断的开发建设，深圳科技工业园已成为投资环境优越、高新技术企业云集、科技开发实力雄厚、人才济济的科技园区。\r\n\r\n近几年来，公司紧紧抓住深圳市产业结构调整的机遇，重点推进深圳市金融服务技术创新基地、深港动漫及网游产业孵化基地、深港生产力基地的建设工作。走出了一条金融产业、文化产业与科技产业相互融合、共同发展的新型道路，力将科技园打造成为具有高科技含量的金融创新区与文化创意园。科技园金融基地作为深圳市率先启用的金融基地于2009年3月投入使用，目前已吸引大批知名的金融企业、金融服务类企业入驻，品牌效应初步树立。', NULL, NULL, NULL, NULL, NULL,NULL,NULL,'1000087',NULL,'2','2015-11-05 14:43:25', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'1','3','4',NULL);
INSERT INTO `eh_organization_communities`(organization_id, community_id) VALUES(1760000, 240111044331046562);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`) 
		VALUES(176001, 240111044331046562, '金融基地', '金融基地', 193501, '13632650699', '深圳市南山区科技园科苑路6号', NULL, NULL, NULL, NULL, NULL, 'http://10.1.1.90:5005/image/aW1hZ2UvTVRwbE1tVmlPRFV3WkRZeFlXUTJORGN5Wmpka1pXWmxPRGcxTVRrNU4yTXdZZw?token=0Y1WmdqibLa2mY5rQCm7Osm', 2, 1, NULL, 1, '2015-11-05 14:56:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`) 
		VALUES(176002, 240111044331046562, '金融科技大厦', '金融科技', 193501, '13632650699', '南山区高新中区科苑大道16号', NULL, NULL, NULL, NULL, NULL, 'http://10.1.1.90:5005/image/aW1hZ2UvTVRvM04yVmhORE00WVRrM04yTmlaVGRsWVdJME56a3hZak5pTnpNNE5qVXlPQQ?token=0Y1WmdqibLa2mY5rQCm7Osm', 2, 1, NULL, 1, '2015-11-05 14:56:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`) 
		VALUES(176003, 240111044331046562, '科技工业园大厦', '科技园', 193501, '13632650699', '深圳市南山区科技园科苑路6号', NULL, NULL, NULL, NULL, NULL, 'http://10.1.1.90:5005/image/aW1hZ2UvTVRwbE1URmtPVE0wWmpGaFptUTFaVEUwTldJNFpqZGlZV0UxTURFd1l6UXpOQQ?token=0Y1WmdqibLa2mY5rQCm7Osm', 2, 1, NULL, 1, '2015-11-05 14:56:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`) 
		VALUES(176004, 240111044331046562, '生产力大楼', '生产力大楼', 193501, '13632650699', '广东省深圳市高新中二道5号', NULL, NULL, NULL, NULL, NULL, 'http://10.1.1.90:5005/image/aW1hZ2UvTVRvMFkyVmxZakF4WTJNd1ltSmlabU5tTlRabFltWTVNR1kzTUdRM01URTRaUQ?token=0Y1WmdqibLa2mY5rQCm7Osm', 2, 1, NULL, 1, '2015-11-05 14:56:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091000, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '二层东', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091001, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '二层西', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091002, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '二楼中', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091003, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '大厦202', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091004, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '三楼', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091005, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '四层', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091006, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东401', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091007, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东501', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091008, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东502', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091009, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东503', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091010, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东508', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091011, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东509', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091012, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东510', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091013, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东518', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091014, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '西502A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091015, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '西503', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091016, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '西506', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091017, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '西508', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091018, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '西510', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091019, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '西518', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091020, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '西588', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091021, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '西598', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091022, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东605', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091023, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东606', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091024, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东606A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091025, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '中602', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091026, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东608', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091027, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '西602', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091028, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '西603', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091029, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '西606', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091030, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '西608', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091031, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '西609', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091032, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '西610', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091033, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '西612', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091034, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '西616', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091035, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东702', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091036, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东703', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091037, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东705', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091038, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东706', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091039, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东708', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091040, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '701', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091041, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东702', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091042, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东704', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091043, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东802', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091044, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东805', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091045, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东806', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091046, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东808', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091047, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东809', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091048, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东810', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091049, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '西801', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091050, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '西802', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091051, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '西803', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091052, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '九层西', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091053, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '9层', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091054, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '10层', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091055, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东1101', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091056, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东1102', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091057, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '西1101', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091058, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '西1102', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091059, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '东1201', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091060, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '西1201', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091061, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '西1202', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091062, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '西1203', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091063, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '科技工业园大厦-F2', '科技工业园大厦', '西1203', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091064, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-5D2', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091065, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-1A02', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091066, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-1B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091067, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-2A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091068, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-2B1', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091069, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-6C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091070, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-2F', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091071, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-3A1', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091072, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-3D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091073, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-7A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091074, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-3B2', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091075, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-5B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091076, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-11A2', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091077, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-10A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091078, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-10B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091079, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-10C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091080, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-10E', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091081, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-10F', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091082, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-10D1', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091083, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-10D3', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091084, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-10D2', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091085, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-2D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091086, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-2C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091087, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-3A2', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091088, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-4E2', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091089, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-1C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091090, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-4C3', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091091, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-4D2', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091092, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-4A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091093, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-7C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091094, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-7E', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091095, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-7F', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091096, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-4C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091097, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-4F3', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091098, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-3C5', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091099, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-4B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091100, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-7B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091101, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-5A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091102, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-9A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091103, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-9B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091104, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-9E', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091105, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-7D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091106, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-5E', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091107, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-5F', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091108, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-8A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091109, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-8B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091110, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-8C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091111, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-8D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091112, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-8E', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091113, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-8F1', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091114, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-8F2', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091115, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-5D1', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091116, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-6F', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091117, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-11C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091118, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-11D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091119, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-11E', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091120, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-11F', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091121, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-3B1', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091122, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-6B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091123, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-6A ', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091124, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-3C1', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091125, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-11B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091126, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-5C2', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091127, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-4F2', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091128, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-6E', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091129, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-4D1', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091130, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-9F1', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091131, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-9F2', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091132, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-3C2', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091133, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-5C1', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091134, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-5D1', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091135, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-4E1', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091136, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '1-3C4', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091137, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-8A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091138, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-8B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091139, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-8C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091140, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-8D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091141, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-8E', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091142, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-8F', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091143, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-8A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091144, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-8B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091145, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-8C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091146, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-8D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091147, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-8E', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091148, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-8F', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091149, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-6A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091150, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-5E', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091151, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-5F2', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091152, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-7D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091153, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-7C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091154, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-7A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091155, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-7B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091156, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-5F1', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091157, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-1A02', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091158, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-1B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091159, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-3A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091160, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-3B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091161, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-3C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091162, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-3D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091163, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-3E', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091164, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-3F', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091165, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-1C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091166, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-1D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091167, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-10C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091168, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-10D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091169, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-6B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091170, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-6C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091171, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-6D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091172, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-9E', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091173, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-9F', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091174, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-4A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091175, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-4B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091176, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-4C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091177, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-4D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091178, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-4E', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091179, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-4F', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091180, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-7E', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091181, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-7F', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091182, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-11C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091183, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-11D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091184, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-11A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091185, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-11B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091186, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-11E', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091187, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-11F', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091188, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-10A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091189, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-10B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091190, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-9C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091191, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-9D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091192, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-9A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091193, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-9B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091194, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-12A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091195, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-12B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091196, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-12C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091197, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-12D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091198, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-12E', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091199, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-12F', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091200, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-10E', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091201, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-10F', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091202, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-5A1', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091203, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-2C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091204, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-2D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091205, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-2A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091206, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-2B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091207, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-5B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091208, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-5C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091209, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-5D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091210, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-6E', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091211, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '2-6F', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091212, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融基地-F2', '金融基地', '3-3栋整', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091213, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-15A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091214, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-15B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091215, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-14A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091216, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-14B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091217, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-24D01', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091218, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-24D02', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091219, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-24D03', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091220, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-24A01(D)', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091221, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-25A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091222, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-25B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091223, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-25C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091224, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-25D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091225, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-26A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091226, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-26B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091227, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-26C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091228, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-26D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091229, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-18C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091230, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-18D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091231, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-11A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091232, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-11B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091233, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-11C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091234, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-11D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091235, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-12A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091236, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-12B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091237, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-12C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091238, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-12D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091239, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-20A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091240, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-20B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091241, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-20D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091242, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-20C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091243, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-21A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091244, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-21B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091245, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-21C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091246, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-21D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091247, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-22A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091248, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-22B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091249, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-22C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091250, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-22D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091251, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-16A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091252, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-16B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091253, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-16C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091254, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-16D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091255, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-8A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091256, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-8B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091257, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-8C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091258, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-8D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091259, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-7A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091260, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-7B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091261, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'B-7A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091262, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'B-7B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091263, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-1A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091264, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-3A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091265, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-3B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091266, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-9C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091267, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-9D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091268, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-13C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091269, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-13D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091270, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-1B ', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091271, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'B-2A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091272, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'B-2B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091273, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-24C01', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091274, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-24C02', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091275, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-24C03', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091276, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-9A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091277, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-9B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091278, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-14C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091279, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-14D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091280, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-2A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091281, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-2B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091282, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-2C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091283, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-2D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091284, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-7C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091285, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-7D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091286, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-13A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091287, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-13B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091288, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-23A04', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091289, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'B-2C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091290, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'B-2D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091291, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-10A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091292, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-10B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091293, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-10C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091294, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-10D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091295, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'B-7A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091296, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'B-7B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091297, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-6A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091298, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-6B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091299, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-6C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091300, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-6D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091301, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'B-B6A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091302, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'B-B6B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091303, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'B-B6C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091304, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'B-B6D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091305, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-23B01', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091306, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-23B02', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091307, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-24A02', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091308, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-24A03', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091309, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-24A04', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091310, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-24B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091311, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'B-4A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091312, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'B-4B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091313, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'B-4C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091314, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'B-4D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091315, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'B-5A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091316, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'B-5B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091317, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'B-5C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091318, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'B-5D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091319, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'B-3A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091320, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'B-3B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091321, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'B-3C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091322, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'B-3D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091323, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-3C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091324, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-3D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091325, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-23D02', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091326, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-23D01', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091327, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-1D ', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091328, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-23C03', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091329, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-5D01', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091330, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-23A01', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091331, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-23D03', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091332, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-1C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091333, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-5A01', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091334, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-5A02', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091335, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-4A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091336, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-4B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091337, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-4C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091338, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-4D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091339, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-4E', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091340, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-17A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091341, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-17B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091342, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-17C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091343, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-17D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091344, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-23C01', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091345, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-23C02', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091346, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-23B03', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091347, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-23B04', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091348, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-18A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091349, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-18B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091350, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-19A', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091351, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-19B', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091352, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-19D', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091353, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-19C', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091354, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-23A02', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091355, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '金融科技大厦-F2', '金融科技大厦', 'A-23A02', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091356, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'A-1层', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091357, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'A-2层', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091358, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'A-3层', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091359, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'A-4层', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091360, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'A-5层', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091361, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'B-1层', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091362, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'B-301', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091363, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'C-1层', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091364, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'C-302', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091365, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'D-4层', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091366, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'D-5层', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091367, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'D-2层', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091368, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'D-3层', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091369, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'B-301', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091370, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', '多功能会议中心101', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091371, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', '多功能会议中心102', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091372, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', '多功能会议中心103', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091373, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'B-2楼东侧', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091374, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'C-301', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091375, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'B-502', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091376, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'B-501', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091377, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'C-501', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091378, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'C-502', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091379, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', '多功能会议中心301', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091380, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'D-1层', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091381, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'B-2层', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091382, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'C-2层', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091383, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'C-6层', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091384, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'B-6层', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091385, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'B-6层', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091386, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'A-601', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091387, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'D-1层', 2);
INSERT INTO `ehcore`.`eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`,`address`, `building_name`, `apartment_name`, `status`)
	VALUES (239825274387091388, UUID(), 240111044331046562, 13905, '深圳市', 13908, '南山区', '生产力大楼-F2', '生产力大楼', 'D-1层', 2);

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176000, UUID(), '深圳市宇轩网络技术有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176530); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176001, UUID(), '深圳市捷时行科技服务有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176531); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176002, UUID(), '深圳市沿海世纪地产有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176532); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176003, UUID(), '深圳市爱倍多科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176533); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176004, UUID(), '浙江宇视科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176534); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176005, UUID(), '网宿科技股份有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176535); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176006, UUID(), '深圳福江科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176536); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176007, UUID(), '深圳市睿智专利事务所','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176537); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176008, UUID(), '无锡盈达聚力科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176538); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176009, UUID(), '深圳市海润鑫文化传播有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176539); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176010, UUID(), '中联认证中心广东分中心','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176540); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176011, UUID(), '深圳大德飞天科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176541); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176012, UUID(), '深圳市文字传媒有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176542); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176013, UUID(), '无锡盈达聚力科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176543); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176014, UUID(), '费斯托(中国)有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176544); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176015, UUID(), '中正国际认证（深圳）有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176545); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176016, UUID(), '深圳市龙晟光电有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176546); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176017, UUID(), '王才梅','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176547); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176018, UUID(), '深圳市清源铸造材料有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176548); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176019, UUID(), '费斯托(中国)有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176549); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176020, UUID(), '郭皓','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176550); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176021, UUID(), '深圳市启智有声科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176551); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176022, UUID(), '苏州工业园区艾思科技公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176552); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176023, UUID(), '深圳今日文化发展有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176553); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176024, UUID(), '深圳市车音网科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176554); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176025, UUID(), '深圳今日文化发展有限公司办公用品中心','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176555); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176026, UUID(), '深圳市炫彩科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176556); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176027, UUID(), '深圳市爱倍多科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176557); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176028, UUID(), '深圳市爱倍多科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176558); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176029, UUID(), '深圳财富支付有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176559); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176030, UUID(), '深圳桑蒲通信设备有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176560); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176031, UUID(), '深圳市精玻仪器有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176561); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176032, UUID(), '新盛合绿科技（深圳）有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176562); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176033, UUID(), '深圳联智科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176563); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176034, UUID(), '深圳易莱特科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176564); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176035, UUID(), '深圳市华美视科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176565); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176036, UUID(), '深圳市拓展信息咨询有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176566); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176037, UUID(), '深圳市创飞领域科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176567); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176038, UUID(), '深圳市创飞领域科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176568); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176039, UUID(), '深圳市创飞领域科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176569); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176040, UUID(), '深圳市视显光电技术有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176570); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176041, UUID(), '深圳市视显光电技术有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176571); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176042, UUID(), '深圳市碧水蓝天科技发展有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176572); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176043, UUID(), '上海芯旺微电子技术有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176573); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176044, UUID(), '深圳市诺恒管理策划有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176574); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176045, UUID(), '深圳市万通食品有限责任公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176575); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176046, UUID(), '上海芯旺电子有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176576); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176047, UUID(), '深圳财富支付有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176577); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176048, UUID(), '深圳市天智未来科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176578); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176049, UUID(), '聚辰半导体（上海）有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176579); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176050, UUID(), '聚辰半导体（上海）有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176580); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176051, UUID(), '华瑞昇电子(深圳）有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176581); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176052, UUID(), '网宿科技股份有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176582); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176053, UUID(), '福瑞博德软件开发深圳有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176583); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176054, UUID(), '福瑞博德软件开发深圳有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176584); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176055, UUID(), '深圳市银证通云网科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176585); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176056, UUID(), '深圳市银证通云网科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176586); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176057, UUID(), '湖州明芯微电子设计有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176587); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176058, UUID(), '珠海奔图电子有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176588); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176059, UUID(), '深圳市巨龙城投资有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176589); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176060, UUID(), '深圳市富泓电子有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176590); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176061, UUID(), '深圳市安睿立电子有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176591); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176062, UUID(), '浙江宇视科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176592); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176063, UUID(), '广东乙纬电子有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176593); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176064, UUID(), '通联支付网络服务股份有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176594); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176065, UUID(), '中国建设银行股份有限公司深圳市分行','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176595); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176066, UUID(), '中国建设银行股份有限公司深圳市分行','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176596); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176067, UUID(), '中国建设银行股份有限公司深圳市分行','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176597); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176068, UUID(), '中国建设银行股份有限公司深圳市分行','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176598); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176069, UUID(), '安富利物流(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176599); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176070, UUID(), '神州数码系统集成服务有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176600); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176071, UUID(), '深圳市汇杰投资有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176601); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176072, UUID(), '深圳市深商创投资管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176602); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176073, UUID(), '阿尔科斯科技（深圳）有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176603); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176074, UUID(), '深圳容德投资有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176604); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176075, UUID(), '深圳市晶科迪电子有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176605); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176076, UUID(), '深圳市易联科软件研发有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176606); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176077, UUID(), '广州中慧电子有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176607); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176078, UUID(), '广州中慧电子有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176608); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176079, UUID(), '广州中慧电子有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176609); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176080, UUID(), '广州中慧电子有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176610); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176081, UUID(), '广州中慧电子有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176611); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176082, UUID(), '广州中慧电子有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176612); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176083, UUID(), '爱康科商贸（深圳）有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176613); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176084, UUID(), '新趣品商贸(深圳)有限公司(李继邦)','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176614); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176085, UUID(), '安信证券股份有限公司深圳科发路证券营业部','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176615); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176086, UUID(), '安信证券股份有限公司深圳科发路证券营业部','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176616); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176087, UUID(), '深圳市高科金信净化科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176617); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176088, UUID(), '深圳市康迈科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176618); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176089, UUID(), '深圳市四季轩餐饮有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176619); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176090, UUID(), '深圳市融泰衡实业有公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176620); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176091, UUID(), '深圳多多益善电子商务有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176621); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176092, UUID(), '深圳市小牛普惠投资管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176622); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176093, UUID(), '君丰创业投资基金管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176623); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176094, UUID(), '深圳证券通信有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176624); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176095, UUID(), '深圳证券通信有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176625); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176096, UUID(), '欣旺达电子股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176626); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176097, UUID(), '深圳市瓷爱谷文化有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176627); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176098, UUID(), '神州数码信息服务股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176628); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176099, UUID(), '北京三浦教育投资有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176629); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176100, UUID(), '深圳市独尊科技开发有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176630); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176101, UUID(), '深圳市信息大成网络有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176631); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176102, UUID(), '深圳通联金融网络科技服务有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176632); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176103, UUID(), '深圳通联金融网络科技服务有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176633); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176104, UUID(), '通联支付网络服务股份有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176634); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176105, UUID(), '深圳市软晶科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176635); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176106, UUID(), '深圳市江波龙电子有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176636); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176107, UUID(), '深圳市江波龙电子有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176637); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176108, UUID(), '深圳市江波龙电子有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176638); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176109, UUID(), '深圳市江波龙电子有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176639); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176110, UUID(), '深圳市江波龙电子有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176640); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176111, UUID(), '深圳市江波龙电子有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176641); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176112, UUID(), '深圳市江波龙电子有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176642); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176113, UUID(), '深圳市江波龙电子有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176643); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176114, UUID(), '深圳市江波龙商用设备有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176644); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176115, UUID(), '深圳市志鼎科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176645); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176116, UUID(), '北京银联金卡科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176646); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176117, UUID(), '深圳神州数码信息技术服务有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176647); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176118, UUID(), '神州数码(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176648); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176119, UUID(), '神州数码(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176649); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176120, UUID(), '上海神州数码有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176650); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176121, UUID(), '深圳市中天普创投资管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176651); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176122, UUID(), '深圳市安卓信创业投资有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176652); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176123, UUID(), '太平人寿保险有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176653); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176124, UUID(), '和阳（深圳）投资管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176654); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176125, UUID(), '深圳市昆鹏股权投资基金管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176655); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176126, UUID(), '深圳市盘龙环境技术有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176656); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176127, UUID(), '深圳壹昊金融控股股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176657); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176128, UUID(), '深圳市贷帮投资担保有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176658); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176129, UUID(), '深圳市人和投资集团有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176659); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176130, UUID(), '川奇光电科技（扬州）有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176660); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176131, UUID(), '川元电子（扬州）有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176661); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176132, UUID(), '深圳市志鼎科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176662); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176133, UUID(), '深圳市惠邦知识产权代理事务所','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176663); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176134, UUID(), '通联支付网络服务股份有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176664); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176135, UUID(), '深圳嘉德瑞碳资产股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176665); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176136, UUID(), '深圳前海南方增长资产管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176666); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176137, UUID(), '安富利物流(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176667); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176138, UUID(), '安富利物流(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176668); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176139, UUID(), '安富利物流(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176669); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176140, UUID(), '安富利物流(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176670); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176141, UUID(), '安富利物流(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176671); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176142, UUID(), '安富利物流(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176672); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176143, UUID(), '萃冠电子贸易（深圳）有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176673); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176144, UUID(), '萃冠电子贸易（深圳）有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176674); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176145, UUID(), '萃冠电子贸易（深圳）有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176675); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176146, UUID(), '萃冠电子贸易（深圳）有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176676); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176147, UUID(), '萃冠电子贸易（深圳）有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176677); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176148, UUID(), '萃冠电子贸易（深圳）有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176678); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176149, UUID(), '安富利物流(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176679); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176150, UUID(), '安霸半导体技术(上海)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176680); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176151, UUID(), '安霸半导体技术(上海)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176681); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176152, UUID(), '深圳中泽明芯集团有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176682); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176153, UUID(), '深圳市漫步者科技股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176683); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176154, UUID(), '是德科技（中国）有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176684); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176155, UUID(), '是德科技（中国）有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176685); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176156, UUID(), '博尔科通讯系统（深圳）有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176686); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176157, UUID(), '招商银行股份有限公司深圳车公庙支行','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176687); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176158, UUID(), '招商银行股份有限公司深圳车公庙支行','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176688); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176159, UUID(), '深圳市润利丰实业发展有限公司（老绍兴酒楼）','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176689); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176160, UUID(), '深圳市润利丰实业发展有限公司（老绍兴酒楼）','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176690); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176161, UUID(), '深圳市润利丰实业发展有限公司（老绍兴酒楼）','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176691); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176162, UUID(), '深圳市润利丰实业发展有限公司（老绍兴酒楼）','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176692); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176163, UUID(), '深圳市润利丰实业发展有限公司（老绍兴酒楼）','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176693); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176164, UUID(), '深圳市润利丰实业发展有限公司（老绍兴酒楼）','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176694); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176165, UUID(), '深圳市广源餐饮管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176695); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176166, UUID(), '深圳市广源餐饮管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176696); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176167, UUID(), '上海联广认证有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176697); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176168, UUID(), '上海联广认证有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176698); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176169, UUID(), '广东万诺律师事务所(李亚光)','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176699); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176170, UUID(), '杭州华三通信技术有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176700); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176171, UUID(), '杭州华三通信技术有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176701);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176172, UUID(), '深圳市恒源昊资产管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176702);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176173, UUID(), '深圳市恒源昊资产管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176703);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176174, UUID(), '深圳前海星润股权投资管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176704);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176175, UUID(), '深圳前海星润股权投资管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176705);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176176, UUID(), '深圳前海星润股权投资管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176706);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176177, UUID(), '深圳前海星润股权投资管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176707);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176178, UUID(), '深圳前海星润股权投资管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176708);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176179, UUID(), '深圳前海星润股权投资管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176709);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176180, UUID(), '塔塔信息技术(中国)股份有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176710);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176181, UUID(), '塔塔信息技术(中国)股份有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176711);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176182, UUID(), '深圳格兰泰克科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176712);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176183, UUID(), '深圳格兰泰克科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176713);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176184, UUID(), '深圳市民声科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176714);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176185, UUID(), '深圳市民声科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176715);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176186, UUID(), '深圳市民声科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176716);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176187, UUID(), '深圳市民声科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176717);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176188, UUID(), '深圳证券通信有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176718);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176189, UUID(), '深圳证券通信有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176719);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176190, UUID(), '深圳五维微品金融信息服务有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176720);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176191, UUID(), '深圳五维微品金融信息服务有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176721);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176192, UUID(), '深圳新为软件股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176722);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176193, UUID(), '深圳新为软件股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176723);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176194, UUID(), '深圳市瑞格尔健康管理科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176724);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176195, UUID(), '深圳市瑞格尔健康管理科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176725);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176196, UUID(), '深圳市瑞格尔健康管理科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176726);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176197, UUID(), '深圳市瑞格尔健康管理科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176727);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176198, UUID(), '深圳市瑞格尔健康管理科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176728);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176199, UUID(), '深圳市瑞格尔健康管理科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176729);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176200, UUID(), '深圳市瑞格尔健康管理科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176730);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176201, UUID(), '深圳市瑞格尔健康管理科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176731);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176202, UUID(), '深圳格兰泰克汽车电子有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176732);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176203, UUID(), '华美优科网络技术（深圳）有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176733);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176204, UUID(), '华美优科网络技术（深圳）有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176734);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176205, UUID(), '华美优科网络技术（深圳）有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176735);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176206, UUID(), '华美优科网络技术（深圳）有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176736);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176207, UUID(), '华美优科网络技术（深圳）有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176737);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176208, UUID(), '华美优科网络技术（深圳）有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176738);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176209, UUID(), '华美优科网络技术（深圳）有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176739);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176210, UUID(), '安霸半导体技术(上海)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176740);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176211, UUID(), '安霸半导体技术(上海)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176741);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176212, UUID(), '吴炎雄(深圳市南山区南风舍餐馆)','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176742);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176213, UUID(), '深圳集群壹家股权投资基金管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176743);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176214, UUID(), '深圳集群壹家股权投资基金管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176744);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176215, UUID(), '深圳华智融科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176745);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176216, UUID(), '深圳华智融科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176746);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176217, UUID(), '深圳华智融科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176747);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176218, UUID(), '深圳华智融科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176748);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176219, UUID(), '深圳华智融科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176749);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176220, UUID(), '深圳华智融科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176750);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176221, UUID(), '深圳市智美达科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176751);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176222, UUID(), '深圳市智美达科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176752);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176223, UUID(), '深圳市智美达科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176753);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176224, UUID(), '深圳市智美达科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176754);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176225, UUID(), '深圳市智美达科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176755);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176226, UUID(), '深圳市智美达科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176756);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176227, UUID(), '深圳市智美达科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176757);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176228, UUID(), '深圳市智美达科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176758);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176229, UUID(), '东吴证券股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176759);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176230, UUID(), '东吴证券股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176760);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176231, UUID(), '湖南南方稀贵金属交易所股份有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176761);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176232, UUID(), '湖南南方稀贵金属交易所股份有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176762);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176233, UUID(), '湖南南方稀贵金属交易所股份有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176763);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176234, UUID(), '湖南南方稀贵金属交易所股份有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176764);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176235, UUID(), '深圳市佳信捷技术股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176765);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176236, UUID(), '深圳市佳信捷技术股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176766);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176237, UUID(), '深圳市佳信捷技术股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176767);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176238, UUID(), '深圳市佳信捷技术股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176768);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176239, UUID(), '深圳市中兴小额贷款有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176769);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176240, UUID(), '深圳市中兴小额贷款有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176770);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176241, UUID(), '深圳市中兴小额贷款有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176771);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176242, UUID(), '深圳市研信科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176772);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176243, UUID(), '深圳中兴飞贷金融科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176773);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176244, UUID(), '深圳中兴飞贷金融科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176774);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176245, UUID(), '深圳中兴飞贷金融科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176775);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176246, UUID(), '深圳中兴飞贷金融科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176776);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176247, UUID(), '深圳中兴飞贷金融科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176777);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176248, UUID(), '深圳中兴飞贷金融科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176778);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176249, UUID(), '深圳中兴飞贷金融科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176779);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176250, UUID(), '深圳中兴飞贷金融科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176780);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176251, UUID(), '深圳市芯智科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176781);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176252, UUID(), '深圳市芯智科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176782);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176253, UUID(), '深圳市芯智科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176783);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176254, UUID(), '深圳市芯智科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176784);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176255, UUID(), '珠海华润银行股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176785);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176256, UUID(), '珠海华润银行股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176786);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176257, UUID(), '珠海华润银行股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176787);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176258, UUID(), '珠海华润银行股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176788);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176259, UUID(), '珠海华润银行股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176789);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176260, UUID(), '珠海华润银行股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176790);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176261, UUID(), '珠海华润银行股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176791);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176262, UUID(), '珠海华润银行股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176792);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176263, UUID(), '中国银行股份有限公司深圳市分行','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176793);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176264, UUID(), '中国银行股份有限公司深圳市分行','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176794);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176265, UUID(), '中国银行股份有限公司深圳市分行','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176795);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176266, UUID(), '众华会计师事务所(特殊普通合伙)深圳分所','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176796);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176267, UUID(), '众华会计师事务所(特殊普通合伙)深圳分所','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176797);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176268, UUID(), '同方(深圳)云计算技术股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176798);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176269, UUID(), '同方(深圳)云计算技术股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176799);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176270, UUID(), '杭州银行股份有限公司深圳分行','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176800);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176271, UUID(), '杭州银行股份有限公司深圳分行','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176801);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176272, UUID(), '杭州银行股份有限公司深圳分行','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176802);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176273, UUID(), '泰诺风保泰（苏州）隔热材料有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176803);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176274, UUID(), '泰诺风保泰（苏州）隔热材料有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176804);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176275, UUID(), '泰诺风保泰（苏州）隔热材料有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176805);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176276, UUID(), '深圳太东资本管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176806);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176277, UUID(), '深圳太东资本管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176807);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176278, UUID(), '中国人民健康保险公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176808);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176279, UUID(), '中国人民健康保险公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176809);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176280, UUID(), '深圳排放权交易所有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176810);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176281, UUID(), '深圳排放权交易所有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176811);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176282, UUID(), '深圳排放权交易所有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176812);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176283, UUID(), '深圳排放权交易所有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176813);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176284, UUID(), '深圳市慧峰高科产业园投资发展有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176814);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176285, UUID(), '深圳市慧峰高科产业园投资发展有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176815);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176286, UUID(), '丰益(上海)信息技术有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176816);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176287, UUID(), '丰益(上海)信息技术有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176817);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176288, UUID(), '深圳市鼎恒瑞投资有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176818);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176289, UUID(), '惠州硕贝德无线科技股份有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176819);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176290, UUID(), '惠州硕贝德无线科技股份有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176820);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176291, UUID(), '深圳市前海德融资本管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176821);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176292, UUID(), '深圳市前海德融资本管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176822);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176293, UUID(), '深圳市前海德融资本管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176823);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176294, UUID(), '深圳市前海德融资本管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176824);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176295, UUID(), '深圳市新天域文化产业有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176825);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176296, UUID(), '深圳市新天域文化产业有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176826);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176297, UUID(), '深圳前海厚诚敏投资控股有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176827);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176298, UUID(), '深圳前海厚诚敏投资控股有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176828);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176299, UUID(), '深圳前海厚诚敏投资控股有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176829);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176300, UUID(), '深圳前海厚诚敏投资控股有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176830);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176301, UUID(), '深圳市多元世纪信息技术有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176831);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176302, UUID(), '深圳市多元世纪信息技术有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176832);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176303, UUID(), '深圳市多元世纪信息技术有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176833);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176304, UUID(), '深圳市多元世纪信息技术有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176834);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176305, UUID(), '深圳市工夫百味投资有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176835);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176306, UUID(), '深圳市工夫百味投资有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176836);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176307, UUID(), '北京集创北方科技有限公司深圳办事处','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176837);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176308, UUID(), '北京集创北方科技有限公司深圳办事处','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176838);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176309, UUID(), '北京集创北方科技有限公司深圳办事处','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176839);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176310, UUID(), '北京集创北方科技有限公司深圳办事处','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176840);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176311, UUID(), '普联技术有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176841);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176312, UUID(), '普联技术有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176842);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176313, UUID(), '普联技术有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176843);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176314, UUID(), '普联技术有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176844);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176315, UUID(), '普联技术有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176845);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176316, UUID(), '普联技术有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176846);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176317, UUID(), '普联技术有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176847);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176318, UUID(), '普联技术有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176848);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176319, UUID(), '普联技术有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176849);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176320, UUID(), '普联技术有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176850);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176321, UUID(), '普联技术有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176851);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176322, UUID(), '普联技术有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176852);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176323, UUID(), '上海赫丝蒂化妆品有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176853);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176324, UUID(), '上海赫丝蒂化妆品有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176854);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176325, UUID(), '深圳市恒顺合鑫科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176855);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176326, UUID(), '深圳市华讯万通科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176856);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176327, UUID(), '中信证券股份有限公司深圳科技园科苑路证券营业部','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176857);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176328, UUID(), '深圳市百益亚太电效工程有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176858);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176329, UUID(), '移动财经软件（深圳）有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176859);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176330, UUID(), '深圳金桥融付科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176860);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176331, UUID(), '深圳金桥融付科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176861);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176332, UUID(), '深圳市浅葱小唱音乐餐厅有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176862);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176333, UUID(), '深圳市城道通环保科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176863);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176334, UUID(), '深圳市城道通环保科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176864);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176335, UUID(), '北京玖富时代投资顾问有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176865);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176336, UUID(), '深圳易兰德金融服务有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176866);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176337, UUID(), '深圳易兰德金融服务有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176867);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176338, UUID(), '深圳易兰德金融服务有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176868);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176339, UUID(), '深圳易兰德金融服务有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176869);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176340, UUID(), '深圳铂睿智恒科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176870);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176341, UUID(), '深圳铂睿智恒科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176871);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176342, UUID(), '深圳铂睿智恒科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176872);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176343, UUID(), '深圳铂睿智恒科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176873);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176344, UUID(), '深圳市宽域智联科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176874);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176345, UUID(), '深圳市宽域智联科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176875);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176346, UUID(), '深圳前海方舟资本管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176876);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176347, UUID(), '深圳前海方舟资本管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176877);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176348, UUID(), '前海东方金德资产管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176878);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176349, UUID(), '前海东方金德资产管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176879);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176350, UUID(), '中民保险经纪股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176880);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176351, UUID(), '中民保险经纪股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176881);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176352, UUID(), '中民保险经纪股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176882);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176353, UUID(), '中民电子商务股份有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176883);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176354, UUID(), '深圳市前海野文投资管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176884);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176355, UUID(), '深圳市前海野文投资管理有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176885);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176356, UUID(), '英伟达半导体(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176886);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176357, UUID(), '英伟达半导体(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176887);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176358, UUID(), '英伟达半导体(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176888);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176359, UUID(), '英伟达半导体(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176889);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176360, UUID(), '英伟达半导体(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176890);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176361, UUID(), '英伟达半导体(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176891);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176362, UUID(), '英伟达半导体(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176892);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176363, UUID(), '英伟达半导体(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176893);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176364, UUID(), '英伟达半导体(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176894);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176365, UUID(), '百度(中国)有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176895);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176366, UUID(), '百度(中国)有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176896);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176367, UUID(), '北京百度网讯科技有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176897);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176368, UUID(), '北京百度网讯科技有限公司深圳分公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176898);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176369, UUID(), '深圳市福尔科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176899);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176370, UUID(), '深圳市德维莱科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176900);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176371, UUID(), '深圳市宝富利科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176901);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176372, UUID(), '深圳市大维纳米科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176902);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176373, UUID(), '金宝通电子(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176903);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176374, UUID(), '深圳市亮信科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176904);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176375, UUID(), '汎达科技(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176905);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176376, UUID(), '群锋电子(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176906);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176377, UUID(), '诺华达电子(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176907);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176378, UUID(), '新纬科技(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176908);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176379, UUID(), '深圳市领平科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176909);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176380, UUID(), '北京美餐巧达科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176910);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176381, UUID(), '镭射谷科(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176911);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176382, UUID(), '镭射谷科(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176912);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176383, UUID(), '镭射谷科(深圳)有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176913); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176384, UUID(), '深圳市安普盛科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176914); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176385, UUID(), '深圳市安特讯科技有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176915); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176386, UUID(), '深圳市开拓汽车电子有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176916); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176387, UUID(), '深圳深港生产力基地有限公司','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176917); 
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`) 
	VALUES(176388, UUID(), '深圳市商业联合会','', 1, 0, 240111044331046562, 'enterprise',  1, 1, '2015-11-12 22:05:33', '2015-11-12 22:05:33', 176918); 


INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100000, 176000, 239825274387091000, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100001, 176001, 239825274387091001, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100002, 176002, 239825274387091002, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100003, 176003, 239825274387091003, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100004, 176004, 239825274387091004, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100005, 176005, 239825274387091005, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100006, 176006, 239825274387091006, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100007, 176007, 239825274387091007, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100008, 176008, 239825274387091008, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100009, 176009, 239825274387091009, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100010, 176010, 239825274387091010, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100011, 176011, 239825274387091011, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100012, 176012, 239825274387091012, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100013, 176013, 239825274387091013, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100014, 176014, 239825274387091014, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100015, 176015, 239825274387091015, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100016, 176016, 239825274387091016, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100017, 176017, 239825274387091017, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100018, 176018, 239825274387091018, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100019, 176019, 239825274387091019, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100020, 176020, 239825274387091020, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100021, 176021, 239825274387091021, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100022, 176022, 239825274387091022, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100023, 176023, 239825274387091023, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100024, 176024, 239825274387091024, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100025, 176025, 239825274387091025, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100026, 176026, 239825274387091026, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100027, 176027, 239825274387091027, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100028, 176028, 239825274387091028, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100029, 176029, 239825274387091029, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100030, 176030, 239825274387091030, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100031, 176031, 239825274387091031, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100032, 176032, 239825274387091032, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100033, 176033, 239825274387091033, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100034, 176034, 239825274387091034, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100035, 176035, 239825274387091035, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100036, 176036, 239825274387091036, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100037, 176037, 239825274387091037, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100038, 176038, 239825274387091038, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100039, 176039, 239825274387091039, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100040, 176040, 239825274387091040, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100041, 176041, 239825274387091041, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100042, 176042, 239825274387091042, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100043, 176043, 239825274387091043, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100044, 176044, 239825274387091044, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100045, 176045, 239825274387091045, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100046, 176046, 239825274387091046, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100047, 176047, 239825274387091047, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100048, 176048, 239825274387091048, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100049, 176049, 239825274387091049, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100050, 176050, 239825274387091050, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100051, 176051, 239825274387091051, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100052, 176052, 239825274387091052, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100053, 176053, 239825274387091053, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100054, 176054, 239825274387091054, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100055, 176055, 239825274387091055, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100056, 176056, 239825274387091056, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100057, 176057, 239825274387091057, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100058, 176058, 239825274387091058, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100059, 176059, 239825274387091059, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100060, 176060, 239825274387091060, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100061, 176061, 239825274387091061, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100062, 176062, 239825274387091062, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100063, 176063, 239825274387091063, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100064, 176064, 239825274387091064, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100065, 176065, 239825274387091065, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100066, 176066, 239825274387091066, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100067, 176067, 239825274387091067, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100068, 176068, 239825274387091068, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100069, 176069, 239825274387091069, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100070, 176070, 239825274387091070, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100071, 176071, 239825274387091071, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100072, 176072, 239825274387091072, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100073, 176073, 239825274387091073, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100074, 176074, 239825274387091074, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100075, 176075, 239825274387091075, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100076, 176076, 239825274387091076, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100077, 176077, 239825274387091077, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100078, 176078, 239825274387091078, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100079, 176079, 239825274387091079, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100080, 176080, 239825274387091080, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100081, 176081, 239825274387091081, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100082, 176082, 239825274387091082, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100083, 176083, 239825274387091083, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100084, 176084, 239825274387091084, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100085, 176085, 239825274387091085, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100086, 176086, 239825274387091086, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100087, 176087, 239825274387091087, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100088, 176088, 239825274387091088, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100089, 176089, 239825274387091089, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100090, 176090, 239825274387091090, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100091, 176091, 239825274387091091, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100092, 176092, 239825274387091092, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100093, 176093, 239825274387091093, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100094, 176094, 239825274387091094, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100095, 176095, 239825274387091095, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100096, 176096, 239825274387091096, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100097, 176097, 239825274387091097, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100098, 176098, 239825274387091098, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100099, 176099, 239825274387091099, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100100, 176100, 239825274387091100, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100101, 176101, 239825274387091101, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100102, 176102, 239825274387091102, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100103, 176103, 239825274387091103, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100104, 176104, 239825274387091104, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100105, 176105, 239825274387091105, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100106, 176106, 239825274387091106, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100107, 176107, 239825274387091107, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100108, 176108, 239825274387091108, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100109, 176109, 239825274387091109, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100110, 176110, 239825274387091110, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100111, 176111, 239825274387091111, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100112, 176112, 239825274387091112, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100113, 176113, 239825274387091113, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100114, 176114, 239825274387091114, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100115, 176115, 239825274387091115, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100116, 176116, 239825274387091116, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100117, 176117, 239825274387091117, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100118, 176118, 239825274387091118, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100119, 176119, 239825274387091119, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100120, 176120, 239825274387091120, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100121, 176121, 239825274387091121, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100122, 176122, 239825274387091122, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100123, 176123, 239825274387091123, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100124, 176124, 239825274387091124, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100125, 176125, 239825274387091125, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100126, 176126, 239825274387091126, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100127, 176127, 239825274387091127, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100128, 176128, 239825274387091128, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100129, 176129, 239825274387091129, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100130, 176130, 239825274387091130, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100131, 176131, 239825274387091131, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100132, 176132, 239825274387091132, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100133, 176133, 239825274387091133, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100134, 176134, 239825274387091134, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100135, 176135, 239825274387091135, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100136, 176136, 239825274387091136, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100137, 176137, 239825274387091137, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100138, 176138, 239825274387091138, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100139, 176139, 239825274387091139, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100140, 176140, 239825274387091140, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100141, 176141, 239825274387091141, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100142, 176142, 239825274387091142, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100143, 176143, 239825274387091143, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100144, 176144, 239825274387091144, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100145, 176145, 239825274387091145, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100146, 176146, 239825274387091146, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100147, 176147, 239825274387091147, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100148, 176148, 239825274387091148, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100149, 176149, 239825274387091149, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100150, 176150, 239825274387091150, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100151, 176151, 239825274387091151, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100152, 176152, 239825274387091152, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100153, 176153, 239825274387091153, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100154, 176154, 239825274387091154, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100155, 176155, 239825274387091155, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100156, 176156, 239825274387091156, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100157, 176157, 239825274387091157, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100158, 176158, 239825274387091158, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100159, 176159, 239825274387091159, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100160, 176160, 239825274387091160, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100161, 176161, 239825274387091161, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100162, 176162, 239825274387091162, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100163, 176163, 239825274387091163, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100164, 176164, 239825274387091164, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100165, 176165, 239825274387091165, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100166, 176166, 239825274387091166, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100167, 176167, 239825274387091167, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100168, 176168, 239825274387091168, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100169, 176169, 239825274387091169, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100170, 176170, 239825274387091170, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100171, 176171, 239825274387091171, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100172, 176172, 239825274387091172, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100173, 176173, 239825274387091173, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100174, 176174, 239825274387091174, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100175, 176175, 239825274387091175, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100176, 176176, 239825274387091176, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100177, 176177, 239825274387091177, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100178, 176178, 239825274387091178, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100179, 176179, 239825274387091179, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100180, 176180, 239825274387091180, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100181, 176181, 239825274387091181, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100182, 176182, 239825274387091182, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100183, 176183, 239825274387091183, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100184, 176184, 239825274387091184, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100185, 176185, 239825274387091185, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100186, 176186, 239825274387091186, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100187, 176187, 239825274387091187, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100188, 176188, 239825274387091188, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100189, 176189, 239825274387091189, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100190, 176190, 239825274387091190, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100191, 176191, 239825274387091191, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100192, 176192, 239825274387091192, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100193, 176193, 239825274387091193, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100194, 176194, 239825274387091194, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100195, 176195, 239825274387091195, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100196, 176196, 239825274387091196, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100197, 176197, 239825274387091197, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100198, 176198, 239825274387091198, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100199, 176199, 239825274387091199, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100200, 176200, 239825274387091200, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100201, 176201, 239825274387091201, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100202, 176202, 239825274387091202, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100203, 176203, 239825274387091203, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100204, 176204, 239825274387091204, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100205, 176205, 239825274387091205, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100206, 176206, 239825274387091206, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100207, 176207, 239825274387091207, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100208, 176208, 239825274387091208, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100209, 176209, 239825274387091209, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100210, 176210, 239825274387091210, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100211, 176211, 239825274387091211, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100212, 176212, 239825274387091212, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100213, 176213, 239825274387091213, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100214, 176214, 239825274387091214, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100215, 176215, 239825274387091215, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100216, 176216, 239825274387091216, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100217, 176217, 239825274387091217, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100218, 176218, 239825274387091218, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100219, 176219, 239825274387091219, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100220, 176220, 239825274387091220, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100221, 176221, 239825274387091221, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100222, 176222, 239825274387091222, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100223, 176223, 239825274387091223, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100224, 176224, 239825274387091224, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100225, 176225, 239825274387091225, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100226, 176226, 239825274387091226, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100227, 176227, 239825274387091227, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100228, 176228, 239825274387091228, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100229, 176229, 239825274387091229, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100230, 176230, 239825274387091230, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100231, 176231, 239825274387091231, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100232, 176232, 239825274387091232, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100233, 176233, 239825274387091233, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100234, 176234, 239825274387091234, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100235, 176235, 239825274387091235, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100236, 176236, 239825274387091236, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100237, 176237, 239825274387091237, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100238, 176238, 239825274387091238, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100239, 176239, 239825274387091239, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100240, 176240, 239825274387091240, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100241, 176241, 239825274387091241, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100242, 176242, 239825274387091242, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100243, 176243, 239825274387091243, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100244, 176244, 239825274387091244, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100245, 176245, 239825274387091245, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100246, 176246, 239825274387091246, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100247, 176247, 239825274387091247, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100248, 176248, 239825274387091248, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100249, 176249, 239825274387091249, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100250, 176250, 239825274387091250, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100251, 176251, 239825274387091251, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100252, 176252, 239825274387091252, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100253, 176253, 239825274387091253, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100254, 176254, 239825274387091254, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100255, 176255, 239825274387091255, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100256, 176256, 239825274387091256, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100257, 176257, 239825274387091257, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100258, 176258, 239825274387091258, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100259, 176259, 239825274387091259, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100260, 176260, 239825274387091260, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100261, 176261, 239825274387091261, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100262, 176262, 239825274387091262, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100263, 176263, 239825274387091263, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100264, 176264, 239825274387091264, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100265, 176265, 239825274387091265, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100266, 176266, 239825274387091266, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100267, 176267, 239825274387091267, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100268, 176268, 239825274387091268, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100269, 176269, 239825274387091269, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100270, 176270, 239825274387091270, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100271, 176271, 239825274387091271, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100272, 176272, 239825274387091272, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100273, 176273, 239825274387091273, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100274, 176274, 239825274387091274, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100275, 176275, 239825274387091275, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100276, 176276, 239825274387091276, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100277, 176277, 239825274387091277, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100278, 176278, 239825274387091278, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100279, 176279, 239825274387091279, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100280, 176280, 239825274387091280, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100281, 176281, 239825274387091281, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100282, 176282, 239825274387091282, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100283, 176283, 239825274387091283, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100284, 176284, 239825274387091284, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100285, 176285, 239825274387091285, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100286, 176286, 239825274387091286, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100287, 176287, 239825274387091287, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100288, 176288, 239825274387091288, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100289, 176289, 239825274387091289, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100290, 176290, 239825274387091290, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100291, 176291, 239825274387091291, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100292, 176292, 239825274387091292, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100293, 176293, 239825274387091293, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100294, 176294, 239825274387091294, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100295, 176295, 239825274387091295, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100296, 176296, 239825274387091296, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100297, 176297, 239825274387091297, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100298, 176298, 239825274387091298, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100299, 176299, 239825274387091299, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100300, 176300, 239825274387091300, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100301, 176301, 239825274387091301, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100302, 176302, 239825274387091302, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100303, 176303, 239825274387091303, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100304, 176304, 239825274387091304, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100305, 176305, 239825274387091305, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100306, 176306, 239825274387091306, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100307, 176307, 239825274387091307, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100308, 176308, 239825274387091308, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100309, 176309, 239825274387091309, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100310, 176310, 239825274387091310, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100311, 176311, 239825274387091311, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100312, 176312, 239825274387091312, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100313, 176313, 239825274387091313, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100314, 176314, 239825274387091314, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100315, 176315, 239825274387091315, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100316, 176316, 239825274387091316, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100317, 176317, 239825274387091317, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100318, 176318, 239825274387091318, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100319, 176319, 239825274387091319, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100320, 176320, 239825274387091320, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100321, 176321, 239825274387091321, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100322, 176322, 239825274387091322, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100323, 176323, 239825274387091323, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100324, 176324, 239825274387091324, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100325, 176325, 239825274387091325, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100326, 176326, 239825274387091326, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100327, 176327, 239825274387091327, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100328, 176328, 239825274387091328, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100329, 176329, 239825274387091329, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100330, 176330, 239825274387091330, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100331, 176331, 239825274387091331, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100332, 176332, 239825274387091332, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100333, 176333, 239825274387091333, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100334, 176334, 239825274387091334, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100335, 176335, 239825274387091335, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100336, 176336, 239825274387091336, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100337, 176337, 239825274387091337, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100338, 176338, 239825274387091338, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100339, 176339, 239825274387091339, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100340, 176340, 239825274387091340, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100341, 176341, 239825274387091341, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100342, 176342, 239825274387091342, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100343, 176343, 239825274387091343, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100344, 176344, 239825274387091344, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100345, 176345, 239825274387091345, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100346, 176346, 239825274387091346, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100347, 176347, 239825274387091347, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100348, 176348, 239825274387091348, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100349, 176349, 239825274387091349, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100350, 176350, 239825274387091350, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100351, 176351, 239825274387091351, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100352, 176352, 239825274387091352, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100353, 176353, 239825274387091353, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100354, 176354, 239825274387091354, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100355, 176355, 239825274387091355, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100356, 176356, 239825274387091356, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100357, 176357, 239825274387091357, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100358, 176358, 239825274387091358, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100359, 176359, 239825274387091359, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100360, 176360, 239825274387091360, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100361, 176361, 239825274387091361, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100362, 176362, 239825274387091362, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100363, 176363, 239825274387091363, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100364, 176364, 239825274387091364, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100365, 176365, 239825274387091365, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100366, 176366, 239825274387091366, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100367, 176367, 239825274387091367, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100368, 176368, 239825274387091368, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100369, 176369, 239825274387091369, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100370, 176370, 239825274387091370, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100371, 176371, 239825274387091371, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100372, 176372, 239825274387091372, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100373, 176373, 239825274387091373, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100374, 176374, 239825274387091374, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100375, 176375, 239825274387091375, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100376, 176376, 239825274387091376, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100377, 176377, 239825274387091377, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100378, 176378, 239825274387091378, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100379, 176379, 239825274387091379, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100380, 176380, 239825274387091380, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100381, 176381, 239825274387091381, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100382, 176382, 239825274387091382, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100383, 176383, 239825274387091383, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100384, 176384, 239825274387091384, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100385, 176385, 239825274387091385, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100386, 176386, 239825274387091386, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100387, 176387, 239825274387091387, 2, '2015-11-12 22:20:25');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`, `create_time`) VALUES(100388, 176388, 239825274387091388, 2, '2015-11-12 22:20:25');

INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100000, 240111044331046562, 'enterprise', 176000, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100001, 240111044331046562, 'enterprise', 176001, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100002, 240111044331046562, 'enterprise', 176002, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100003, 240111044331046562, 'enterprise', 176003, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100004, 240111044331046562, 'enterprise', 176004, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100005, 240111044331046562, 'enterprise', 176005, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100006, 240111044331046562, 'enterprise', 176006, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100007, 240111044331046562, 'enterprise', 176007, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100008, 240111044331046562, 'enterprise', 176008, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100009, 240111044331046562, 'enterprise', 176009, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100010, 240111044331046562, 'enterprise', 176010, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100011, 240111044331046562, 'enterprise', 176011, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100012, 240111044331046562, 'enterprise', 176012, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100013, 240111044331046562, 'enterprise', 176013, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100014, 240111044331046562, 'enterprise', 176014, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100015, 240111044331046562, 'enterprise', 176015, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100016, 240111044331046562, 'enterprise', 176016, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100017, 240111044331046562, 'enterprise', 176017, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100018, 240111044331046562, 'enterprise', 176018, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100019, 240111044331046562, 'enterprise', 176019, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100020, 240111044331046562, 'enterprise', 176020, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100021, 240111044331046562, 'enterprise', 176021, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100022, 240111044331046562, 'enterprise', 176022, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100023, 240111044331046562, 'enterprise', 176023, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100024, 240111044331046562, 'enterprise', 176024, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100025, 240111044331046562, 'enterprise', 176025, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100026, 240111044331046562, 'enterprise', 176026, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100027, 240111044331046562, 'enterprise', 176027, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100028, 240111044331046562, 'enterprise', 176028, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100029, 240111044331046562, 'enterprise', 176029, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100030, 240111044331046562, 'enterprise', 176030, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100031, 240111044331046562, 'enterprise', 176031, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100032, 240111044331046562, 'enterprise', 176032, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100033, 240111044331046562, 'enterprise', 176033, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100034, 240111044331046562, 'enterprise', 176034, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100035, 240111044331046562, 'enterprise', 176035, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100036, 240111044331046562, 'enterprise', 176036, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100037, 240111044331046562, 'enterprise', 176037, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100038, 240111044331046562, 'enterprise', 176038, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100039, 240111044331046562, 'enterprise', 176039, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100040, 240111044331046562, 'enterprise', 176040, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100041, 240111044331046562, 'enterprise', 176041, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100042, 240111044331046562, 'enterprise', 176042, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100043, 240111044331046562, 'enterprise', 176043, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100044, 240111044331046562, 'enterprise', 176044, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100045, 240111044331046562, 'enterprise', 176045, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100046, 240111044331046562, 'enterprise', 176046, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100047, 240111044331046562, 'enterprise', 176047, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100048, 240111044331046562, 'enterprise', 176048, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100049, 240111044331046562, 'enterprise', 176049, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100050, 240111044331046562, 'enterprise', 176050, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100051, 240111044331046562, 'enterprise', 176051, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100052, 240111044331046562, 'enterprise', 176052, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100053, 240111044331046562, 'enterprise', 176053, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100054, 240111044331046562, 'enterprise', 176054, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100055, 240111044331046562, 'enterprise', 176055, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100056, 240111044331046562, 'enterprise', 176056, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100057, 240111044331046562, 'enterprise', 176057, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100058, 240111044331046562, 'enterprise', 176058, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100059, 240111044331046562, 'enterprise', 176059, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100060, 240111044331046562, 'enterprise', 176060, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100061, 240111044331046562, 'enterprise', 176061, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100062, 240111044331046562, 'enterprise', 176062, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100063, 240111044331046562, 'enterprise', 176063, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100064, 240111044331046562, 'enterprise', 176064, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100065, 240111044331046562, 'enterprise', 176065, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100066, 240111044331046562, 'enterprise', 176066, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100067, 240111044331046562, 'enterprise', 176067, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100068, 240111044331046562, 'enterprise', 176068, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100069, 240111044331046562, 'enterprise', 176069, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100070, 240111044331046562, 'enterprise', 176070, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100071, 240111044331046562, 'enterprise', 176071, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100072, 240111044331046562, 'enterprise', 176072, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100073, 240111044331046562, 'enterprise', 176073, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100074, 240111044331046562, 'enterprise', 176074, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100075, 240111044331046562, 'enterprise', 176075, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100076, 240111044331046562, 'enterprise', 176076, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100077, 240111044331046562, 'enterprise', 176077, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100078, 240111044331046562, 'enterprise', 176078, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100079, 240111044331046562, 'enterprise', 176079, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100080, 240111044331046562, 'enterprise', 176080, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100081, 240111044331046562, 'enterprise', 176081, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100082, 240111044331046562, 'enterprise', 176082, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100083, 240111044331046562, 'enterprise', 176083, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100084, 240111044331046562, 'enterprise', 176084, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100085, 240111044331046562, 'enterprise', 176085, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100086, 240111044331046562, 'enterprise', 176086, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100087, 240111044331046562, 'enterprise', 176087, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100088, 240111044331046562, 'enterprise', 176088, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100089, 240111044331046562, 'enterprise', 176089, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100090, 240111044331046562, 'enterprise', 176090, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100091, 240111044331046562, 'enterprise', 176091, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100092, 240111044331046562, 'enterprise', 176092, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100093, 240111044331046562, 'enterprise', 176093, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100094, 240111044331046562, 'enterprise', 176094, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100095, 240111044331046562, 'enterprise', 176095, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100096, 240111044331046562, 'enterprise', 176096, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100097, 240111044331046562, 'enterprise', 176097, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100098, 240111044331046562, 'enterprise', 176098, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100099, 240111044331046562, 'enterprise', 176099, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100100, 240111044331046562, 'enterprise', 176100, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100101, 240111044331046562, 'enterprise', 176101, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100102, 240111044331046562, 'enterprise', 176102, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100103, 240111044331046562, 'enterprise', 176103, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100104, 240111044331046562, 'enterprise', 176104, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100105, 240111044331046562, 'enterprise', 176105, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100106, 240111044331046562, 'enterprise', 176106, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100107, 240111044331046562, 'enterprise', 176107, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100108, 240111044331046562, 'enterprise', 176108, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100109, 240111044331046562, 'enterprise', 176109, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100110, 240111044331046562, 'enterprise', 176110, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100111, 240111044331046562, 'enterprise', 176111, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100112, 240111044331046562, 'enterprise', 176112, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100113, 240111044331046562, 'enterprise', 176113, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100114, 240111044331046562, 'enterprise', 176114, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100115, 240111044331046562, 'enterprise', 176115, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100116, 240111044331046562, 'enterprise', 176116, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100117, 240111044331046562, 'enterprise', 176117, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100118, 240111044331046562, 'enterprise', 176118, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100119, 240111044331046562, 'enterprise', 176119, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100120, 240111044331046562, 'enterprise', 176120, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100121, 240111044331046562, 'enterprise', 176121, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100122, 240111044331046562, 'enterprise', 176122, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100123, 240111044331046562, 'enterprise', 176123, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100124, 240111044331046562, 'enterprise', 176124, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100125, 240111044331046562, 'enterprise', 176125, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100126, 240111044331046562, 'enterprise', 176126, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100127, 240111044331046562, 'enterprise', 176127, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100128, 240111044331046562, 'enterprise', 176128, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100129, 240111044331046562, 'enterprise', 176129, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100130, 240111044331046562, 'enterprise', 176130, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100131, 240111044331046562, 'enterprise', 176131, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100132, 240111044331046562, 'enterprise', 176132, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100133, 240111044331046562, 'enterprise', 176133, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100134, 240111044331046562, 'enterprise', 176134, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100135, 240111044331046562, 'enterprise', 176135, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100136, 240111044331046562, 'enterprise', 176136, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100137, 240111044331046562, 'enterprise', 176137, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100138, 240111044331046562, 'enterprise', 176138, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100139, 240111044331046562, 'enterprise', 176139, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100140, 240111044331046562, 'enterprise', 176140, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100141, 240111044331046562, 'enterprise', 176141, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100142, 240111044331046562, 'enterprise', 176142, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100143, 240111044331046562, 'enterprise', 176143, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100144, 240111044331046562, 'enterprise', 176144, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100145, 240111044331046562, 'enterprise', 176145, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100146, 240111044331046562, 'enterprise', 176146, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100147, 240111044331046562, 'enterprise', 176147, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100148, 240111044331046562, 'enterprise', 176148, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100149, 240111044331046562, 'enterprise', 176149, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100150, 240111044331046562, 'enterprise', 176150, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100151, 240111044331046562, 'enterprise', 176151, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100152, 240111044331046562, 'enterprise', 176152, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100153, 240111044331046562, 'enterprise', 176153, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100154, 240111044331046562, 'enterprise', 176154, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100155, 240111044331046562, 'enterprise', 176155, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100156, 240111044331046562, 'enterprise', 176156, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100157, 240111044331046562, 'enterprise', 176157, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100158, 240111044331046562, 'enterprise', 176158, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100159, 240111044331046562, 'enterprise', 176159, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100160, 240111044331046562, 'enterprise', 176160, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100161, 240111044331046562, 'enterprise', 176161, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100162, 240111044331046562, 'enterprise', 176162, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100163, 240111044331046562, 'enterprise', 176163, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100164, 240111044331046562, 'enterprise', 176164, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100165, 240111044331046562, 'enterprise', 176165, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100166, 240111044331046562, 'enterprise', 176166, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100167, 240111044331046562, 'enterprise', 176167, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100168, 240111044331046562, 'enterprise', 176168, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100169, 240111044331046562, 'enterprise', 176169, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100170, 240111044331046562, 'enterprise', 176170, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100171, 240111044331046562, 'enterprise', 176171, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100172, 240111044331046562, 'enterprise', 176172, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100173, 240111044331046562, 'enterprise', 176173, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100174, 240111044331046562, 'enterprise', 176174, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100175, 240111044331046562, 'enterprise', 176175, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100176, 240111044331046562, 'enterprise', 176176, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100177, 240111044331046562, 'enterprise', 176177, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100178, 240111044331046562, 'enterprise', 176178, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100179, 240111044331046562, 'enterprise', 176179, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100180, 240111044331046562, 'enterprise', 176180, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100181, 240111044331046562, 'enterprise', 176181, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100182, 240111044331046562, 'enterprise', 176182, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100183, 240111044331046562, 'enterprise', 176183, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100184, 240111044331046562, 'enterprise', 176184, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100185, 240111044331046562, 'enterprise', 176185, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100186, 240111044331046562, 'enterprise', 176186, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100187, 240111044331046562, 'enterprise', 176187, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100188, 240111044331046562, 'enterprise', 176188, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100189, 240111044331046562, 'enterprise', 176189, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100190, 240111044331046562, 'enterprise', 176190, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100191, 240111044331046562, 'enterprise', 176191, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100192, 240111044331046562, 'enterprise', 176192, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100193, 240111044331046562, 'enterprise', 176193, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100194, 240111044331046562, 'enterprise', 176194, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100195, 240111044331046562, 'enterprise', 176195, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100196, 240111044331046562, 'enterprise', 176196, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100197, 240111044331046562, 'enterprise', 176197, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100198, 240111044331046562, 'enterprise', 176198, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100199, 240111044331046562, 'enterprise', 176199, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100200, 240111044331046562, 'enterprise', 176200, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100201, 240111044331046562, 'enterprise', 176201, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100202, 240111044331046562, 'enterprise', 176202, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100203, 240111044331046562, 'enterprise', 176203, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100204, 240111044331046562, 'enterprise', 176204, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100205, 240111044331046562, 'enterprise', 176205, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100206, 240111044331046562, 'enterprise', 176206, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100207, 240111044331046562, 'enterprise', 176207, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100208, 240111044331046562, 'enterprise', 176208, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100209, 240111044331046562, 'enterprise', 176209, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100210, 240111044331046562, 'enterprise', 176210, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100211, 240111044331046562, 'enterprise', 176211, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100212, 240111044331046562, 'enterprise', 176212, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100213, 240111044331046562, 'enterprise', 176213, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100214, 240111044331046562, 'enterprise', 176214, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100215, 240111044331046562, 'enterprise', 176215, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100216, 240111044331046562, 'enterprise', 176216, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100217, 240111044331046562, 'enterprise', 176217, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100218, 240111044331046562, 'enterprise', 176218, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100219, 240111044331046562, 'enterprise', 176219, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100220, 240111044331046562, 'enterprise', 176220, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100221, 240111044331046562, 'enterprise', 176221, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100222, 240111044331046562, 'enterprise', 176222, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100223, 240111044331046562, 'enterprise', 176223, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100224, 240111044331046562, 'enterprise', 176224, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100225, 240111044331046562, 'enterprise', 176225, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100226, 240111044331046562, 'enterprise', 176226, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100227, 240111044331046562, 'enterprise', 176227, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100228, 240111044331046562, 'enterprise', 176228, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100229, 240111044331046562, 'enterprise', 176229, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100230, 240111044331046562, 'enterprise', 176230, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100231, 240111044331046562, 'enterprise', 176231, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100232, 240111044331046562, 'enterprise', 176232, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100233, 240111044331046562, 'enterprise', 176233, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100234, 240111044331046562, 'enterprise', 176234, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100235, 240111044331046562, 'enterprise', 176235, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100236, 240111044331046562, 'enterprise', 176236, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100237, 240111044331046562, 'enterprise', 176237, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100238, 240111044331046562, 'enterprise', 176238, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100239, 240111044331046562, 'enterprise', 176239, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100240, 240111044331046562, 'enterprise', 176240, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100241, 240111044331046562, 'enterprise', 176241, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100242, 240111044331046562, 'enterprise', 176242, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100243, 240111044331046562, 'enterprise', 176243, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100244, 240111044331046562, 'enterprise', 176244, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100245, 240111044331046562, 'enterprise', 176245, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100246, 240111044331046562, 'enterprise', 176246, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100247, 240111044331046562, 'enterprise', 176247, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100248, 240111044331046562, 'enterprise', 176248, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100249, 240111044331046562, 'enterprise', 176249, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100250, 240111044331046562, 'enterprise', 176250, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100251, 240111044331046562, 'enterprise', 176251, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100252, 240111044331046562, 'enterprise', 176252, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100253, 240111044331046562, 'enterprise', 176253, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100254, 240111044331046562, 'enterprise', 176254, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100255, 240111044331046562, 'enterprise', 176255, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100256, 240111044331046562, 'enterprise', 176256, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100257, 240111044331046562, 'enterprise', 176257, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100258, 240111044331046562, 'enterprise', 176258, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100259, 240111044331046562, 'enterprise', 176259, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100260, 240111044331046562, 'enterprise', 176260, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100261, 240111044331046562, 'enterprise', 176261, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100262, 240111044331046562, 'enterprise', 176262, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100263, 240111044331046562, 'enterprise', 176263, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100264, 240111044331046562, 'enterprise', 176264, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100265, 240111044331046562, 'enterprise', 176265, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100266, 240111044331046562, 'enterprise', 176266, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100267, 240111044331046562, 'enterprise', 176267, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100268, 240111044331046562, 'enterprise', 176268, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100269, 240111044331046562, 'enterprise', 176269, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100270, 240111044331046562, 'enterprise', 176270, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100271, 240111044331046562, 'enterprise', 176271, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100272, 240111044331046562, 'enterprise', 176272, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100273, 240111044331046562, 'enterprise', 176273, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100274, 240111044331046562, 'enterprise', 176274, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100275, 240111044331046562, 'enterprise', 176275, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100276, 240111044331046562, 'enterprise', 176276, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100277, 240111044331046562, 'enterprise', 176277, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100278, 240111044331046562, 'enterprise', 176278, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100279, 240111044331046562, 'enterprise', 176279, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100280, 240111044331046562, 'enterprise', 176280, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100281, 240111044331046562, 'enterprise', 176281, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100282, 240111044331046562, 'enterprise', 176282, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100283, 240111044331046562, 'enterprise', 176283, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100284, 240111044331046562, 'enterprise', 176284, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100285, 240111044331046562, 'enterprise', 176285, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100286, 240111044331046562, 'enterprise', 176286, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100287, 240111044331046562, 'enterprise', 176287, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100288, 240111044331046562, 'enterprise', 176288, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100289, 240111044331046562, 'enterprise', 176289, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100290, 240111044331046562, 'enterprise', 176290, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100291, 240111044331046562, 'enterprise', 176291, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100292, 240111044331046562, 'enterprise', 176292, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100293, 240111044331046562, 'enterprise', 176293, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100294, 240111044331046562, 'enterprise', 176294, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100295, 240111044331046562, 'enterprise', 176295, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100296, 240111044331046562, 'enterprise', 176296, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100297, 240111044331046562, 'enterprise', 176297, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100298, 240111044331046562, 'enterprise', 176298, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100299, 240111044331046562, 'enterprise', 176299, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100300, 240111044331046562, 'enterprise', 176300, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100301, 240111044331046562, 'enterprise', 176301, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100302, 240111044331046562, 'enterprise', 176302, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100303, 240111044331046562, 'enterprise', 176303, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100304, 240111044331046562, 'enterprise', 176304, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100305, 240111044331046562, 'enterprise', 176305, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100306, 240111044331046562, 'enterprise', 176306, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100307, 240111044331046562, 'enterprise', 176307, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100308, 240111044331046562, 'enterprise', 176308, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100309, 240111044331046562, 'enterprise', 176309, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100310, 240111044331046562, 'enterprise', 176310, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100311, 240111044331046562, 'enterprise', 176311, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100312, 240111044331046562, 'enterprise', 176312, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100313, 240111044331046562, 'enterprise', 176313, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100314, 240111044331046562, 'enterprise', 176314, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100315, 240111044331046562, 'enterprise', 176315, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100316, 240111044331046562, 'enterprise', 176316, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100317, 240111044331046562, 'enterprise', 176317, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100318, 240111044331046562, 'enterprise', 176318, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100319, 240111044331046562, 'enterprise', 176319, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100320, 240111044331046562, 'enterprise', 176320, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100321, 240111044331046562, 'enterprise', 176321, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100322, 240111044331046562, 'enterprise', 176322, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100323, 240111044331046562, 'enterprise', 176323, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100324, 240111044331046562, 'enterprise', 176324, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100325, 240111044331046562, 'enterprise', 176325, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100326, 240111044331046562, 'enterprise', 176326, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100327, 240111044331046562, 'enterprise', 176327, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100328, 240111044331046562, 'enterprise', 176328, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100329, 240111044331046562, 'enterprise', 176329, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100330, 240111044331046562, 'enterprise', 176330, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100331, 240111044331046562, 'enterprise', 176331, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100332, 240111044331046562, 'enterprise', 176332, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100333, 240111044331046562, 'enterprise', 176333, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100334, 240111044331046562, 'enterprise', 176334, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100335, 240111044331046562, 'enterprise', 176335, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100336, 240111044331046562, 'enterprise', 176336, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100337, 240111044331046562, 'enterprise', 176337, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100338, 240111044331046562, 'enterprise', 176338, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100339, 240111044331046562, 'enterprise', 176339, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100340, 240111044331046562, 'enterprise', 176340, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100341, 240111044331046562, 'enterprise', 176341, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100342, 240111044331046562, 'enterprise', 176342, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100343, 240111044331046562, 'enterprise', 176343, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100344, 240111044331046562, 'enterprise', 176344, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100345, 240111044331046562, 'enterprise', 176345, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100346, 240111044331046562, 'enterprise', 176346, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100347, 240111044331046562, 'enterprise', 176347, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100348, 240111044331046562, 'enterprise', 176348, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100349, 240111044331046562, 'enterprise', 176349, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100350, 240111044331046562, 'enterprise', 176350, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100351, 240111044331046562, 'enterprise', 176351, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100352, 240111044331046562, 'enterprise', 176352, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100353, 240111044331046562, 'enterprise', 176353, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100354, 240111044331046562, 'enterprise', 176354, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100355, 240111044331046562, 'enterprise', 176355, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100356, 240111044331046562, 'enterprise', 176356, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100357, 240111044331046562, 'enterprise', 176357, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100358, 240111044331046562, 'enterprise', 176358, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100359, 240111044331046562, 'enterprise', 176359, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100360, 240111044331046562, 'enterprise', 176360, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100361, 240111044331046562, 'enterprise', 176361, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100362, 240111044331046562, 'enterprise', 176362, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100363, 240111044331046562, 'enterprise', 176363, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100364, 240111044331046562, 'enterprise', 176364, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100365, 240111044331046562, 'enterprise', 176365, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100366, 240111044331046562, 'enterprise', 176366, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100367, 240111044331046562, 'enterprise', 176367, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100368, 240111044331046562, 'enterprise', 176368, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100369, 240111044331046562, 'enterprise', 176369, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100370, 240111044331046562, 'enterprise', 176370, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100371, 240111044331046562, 'enterprise', 176371, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100372, 240111044331046562, 'enterprise', 176372, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100373, 240111044331046562, 'enterprise', 176373, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100374, 240111044331046562, 'enterprise', 176374, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100375, 240111044331046562, 'enterprise', 176375, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100376, 240111044331046562, 'enterprise', 176376, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100377, 240111044331046562, 'enterprise', 176377, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100378, 240111044331046562, 'enterprise', 176378, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100379, 240111044331046562, 'enterprise', 176379, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100380, 240111044331046562, 'enterprise', 176380, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100381, 240111044331046562, 'enterprise', 176381, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100382, 240111044331046562, 'enterprise', 176382, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100383, 240111044331046562, 'enterprise', 176383, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100384, 240111044331046562, 'enterprise', 176384, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100385, 240111044331046562, 'enterprise', 176385, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100386, 240111044331046562, 'enterprise', 176386, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100387, 240111044331046562, 'enterprise', 176387, 2);
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES(100388, 240111044331046562, 'enterprise', 176388, 2);


INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176530, UUID(), 0, 2, 'EhGroups', 176000,'深圳市宇轩网络技术有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176531, UUID(), 0, 2, 'EhGroups', 176001,'深圳市捷时行科技服务有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176532, UUID(), 0, 2, 'EhGroups', 176002,'深圳市沿海世纪地产有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176533, UUID(), 0, 2, 'EhGroups', 176003,'深圳市爱倍多科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176534, UUID(), 0, 2, 'EhGroups', 176004,'浙江宇视科技有限公司','','0','0');                                                            
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176535, UUID(), 0, 2, 'EhGroups', 176005,'网宿科技股份有限公司深圳分公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176536, UUID(), 0, 2, 'EhGroups', 176006,'深圳福江科技有限公司','','0','0');                                                            
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176537, UUID(), 0, 2, 'EhGroups', 176007,'深圳市睿智专利事务所','','0','0');                                                            
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176538, UUID(), 0, 2, 'EhGroups', 176008,'无锡盈达聚力科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176539, UUID(), 0, 2, 'EhGroups', 176009,'深圳市海润鑫文化传播有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176540, UUID(), 0, 2, 'EhGroups', 176010,'中联认证中心广东分中心','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176541, UUID(), 0, 2, 'EhGroups', 176011,'深圳大德飞天科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176542, UUID(), 0, 2, 'EhGroups', 176012,'深圳市文字传媒有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176543, UUID(), 0, 2, 'EhGroups', 176013,'无锡盈达聚力科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176544, UUID(), 0, 2, 'EhGroups', 176014,'费斯托(中国)有限公司深圳分公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176545, UUID(), 0, 2, 'EhGroups', 176015,'中正国际认证（深圳）有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176546, UUID(), 0, 2, 'EhGroups', 176016,'深圳市龙晟光电有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176547, UUID(), 0, 2, 'EhGroups', 176017,'王才梅','','0','0');                                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176548, UUID(), 0, 2, 'EhGroups', 176018,'深圳市清源铸造材料有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176549, UUID(), 0, 2, 'EhGroups', 176019,'费斯托(中国)有限公司深圳分公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176550, UUID(), 0, 2, 'EhGroups', 176020,'郭皓','','0','0');                                                                            
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176551, UUID(), 0, 2, 'EhGroups', 176021,'深圳市启智有声科技有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176552, UUID(), 0, 2, 'EhGroups', 176022,'苏州工业园区艾思科技公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176553, UUID(), 0, 2, 'EhGroups', 176023,'深圳今日文化发展有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176554, UUID(), 0, 2, 'EhGroups', 176024,'深圳市车音网科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176555, UUID(), 0, 2, 'EhGroups', 176025,'深圳今日文化发展有限公司办公用品中心','','0','0');                                            
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176556, UUID(), 0, 2, 'EhGroups', 176026,'深圳市炫彩科技有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176557, UUID(), 0, 2, 'EhGroups', 176027,'深圳市爱倍多科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176558, UUID(), 0, 2, 'EhGroups', 176028,'深圳市爱倍多科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176559, UUID(), 0, 2, 'EhGroups', 176029,'深圳财富支付有限公司','','0','0');                                                            
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176560, UUID(), 0, 2, 'EhGroups', 176030,'深圳桑蒲通信设备有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176561, UUID(), 0, 2, 'EhGroups', 176031,'深圳市精玻仪器有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176562, UUID(), 0, 2, 'EhGroups', 176032,'新盛合绿科技（深圳）有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176563, UUID(), 0, 2, 'EhGroups', 176033,'深圳联智科技有限公司','','0','0');                                                            
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176564, UUID(), 0, 2, 'EhGroups', 176034,'深圳易莱特科技有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176565, UUID(), 0, 2, 'EhGroups', 176035,'深圳市华美视科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176566, UUID(), 0, 2, 'EhGroups', 176036,'深圳市拓展信息咨询有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176567, UUID(), 0, 2, 'EhGroups', 176037,'深圳市创飞领域科技有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176568, UUID(), 0, 2, 'EhGroups', 176038,'深圳市创飞领域科技有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176569, UUID(), 0, 2, 'EhGroups', 176039,'深圳市创飞领域科技有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176570, UUID(), 0, 2, 'EhGroups', 176040,'深圳市视显光电技术有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176571, UUID(), 0, 2, 'EhGroups', 176041,'深圳市视显光电技术有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176572, UUID(), 0, 2, 'EhGroups', 176042,'深圳市碧水蓝天科技发展有限公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176573, UUID(), 0, 2, 'EhGroups', 176043,'上海芯旺微电子技术有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176574, UUID(), 0, 2, 'EhGroups', 176044,'深圳市诺恒管理策划有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176575, UUID(), 0, 2, 'EhGroups', 176045,'深圳市万通食品有限责任公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176576, UUID(), 0, 2, 'EhGroups', 176046,'上海芯旺电子有限公司','','0','0');                                                            
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176577, UUID(), 0, 2, 'EhGroups', 176047,'深圳财富支付有限公司','','0','0');                                                            
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176578, UUID(), 0, 2, 'EhGroups', 176048,'深圳市天智未来科技有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176579, UUID(), 0, 2, 'EhGroups', 176049,'聚辰半导体（上海）有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176580, UUID(), 0, 2, 'EhGroups', 176050,'聚辰半导体（上海）有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176581, UUID(), 0, 2, 'EhGroups', 176051,'华瑞昇电子(深圳）有限公司','','0','0');                                                       
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176582, UUID(), 0, 2, 'EhGroups', 176052,'网宿科技股份有限公司深圳分公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176583, UUID(), 0, 2, 'EhGroups', 176053,'福瑞博德软件开发深圳有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176584, UUID(), 0, 2, 'EhGroups', 176054,'福瑞博德软件开发深圳有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176585, UUID(), 0, 2, 'EhGroups', 176055,'深圳市银证通云网科技有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176586, UUID(), 0, 2, 'EhGroups', 176056,'深圳市银证通云网科技有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176587, UUID(), 0, 2, 'EhGroups', 176057,'湖州明芯微电子设计有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176588, UUID(), 0, 2, 'EhGroups', 176058,'珠海奔图电子有限公司','','0','0');                                                            
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176589, UUID(), 0, 2, 'EhGroups', 176059,'深圳市巨龙城投资有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176590, UUID(), 0, 2, 'EhGroups', 176060,'深圳市富泓电子有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176591, UUID(), 0, 2, 'EhGroups', 176061,'深圳市安睿立电子有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176592, UUID(), 0, 2, 'EhGroups', 176062,'浙江宇视科技有限公司','','0','0');                                                            
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176593, UUID(), 0, 2, 'EhGroups', 176063,'广东乙纬电子有限公司','','0','0');                                                            
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176594, UUID(), 0, 2, 'EhGroups', 176064,'通联支付网络服务股份有限公司深圳分公司','','0','0');                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176595, UUID(), 0, 2, 'EhGroups', 176065,'中国建设银行股份有限公司深圳市分行','','0','0');                                              
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176596, UUID(), 0, 2, 'EhGroups', 176066,'中国建设银行股份有限公司深圳市分行','','0','0');                                              
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176597, UUID(), 0, 2, 'EhGroups', 176067,'中国建设银行股份有限公司深圳市分行','','0','0');                                              
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176598, UUID(), 0, 2, 'EhGroups', 176068,'中国建设银行股份有限公司深圳市分行','','0','0');                                              
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176599, UUID(), 0, 2, 'EhGroups', 176069,'安富利物流(深圳)有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176600, UUID(), 0, 2, 'EhGroups', 176070,'神州数码系统集成服务有限公司深圳分公司','','0','0');                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176601, UUID(), 0, 2, 'EhGroups', 176071,'深圳市汇杰投资有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176602, UUID(), 0, 2, 'EhGroups', 176072,'深圳市深商创投资管理有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176603, UUID(), 0, 2, 'EhGroups', 176073,'阿尔科斯科技（深圳）有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176604, UUID(), 0, 2, 'EhGroups', 176074,'深圳容德投资有限公司','','0','0');                                                            
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176605, UUID(), 0, 2, 'EhGroups', 176075,'深圳市晶科迪电子有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176606, UUID(), 0, 2, 'EhGroups', 176076,'深圳市易联科软件研发有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176607, UUID(), 0, 2, 'EhGroups', 176077,'广州中慧电子有限公司深圳分公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176608, UUID(), 0, 2, 'EhGroups', 176078,'广州中慧电子有限公司深圳分公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176609, UUID(), 0, 2, 'EhGroups', 176079,'广州中慧电子有限公司深圳分公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176610, UUID(), 0, 2, 'EhGroups', 176080,'广州中慧电子有限公司深圳分公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176611, UUID(), 0, 2, 'EhGroups', 176081,'广州中慧电子有限公司深圳分公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176612, UUID(), 0, 2, 'EhGroups', 176082,'广州中慧电子有限公司深圳分公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176613, UUID(), 0, 2, 'EhGroups', 176083,'爱康科商贸（深圳）有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176614, UUID(), 0, 2, 'EhGroups', 176084,'新趣品商贸(深圳)有限公司(李继邦)','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176615, UUID(), 0, 2, 'EhGroups', 176085,'安信证券股份有限公司深圳科发路证券营业部','','0','0');                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176616, UUID(), 0, 2, 'EhGroups', 176086,'安信证券股份有限公司深圳科发路证券营业部','','0','0');                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176617, UUID(), 0, 2, 'EhGroups', 176087,'深圳市高科金信净化科技有限公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176618, UUID(), 0, 2, 'EhGroups', 176088,'深圳市康迈科技有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176619, UUID(), 0, 2, 'EhGroups', 176089,'深圳市四季轩餐饮有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176620, UUID(), 0, 2, 'EhGroups', 176090,'深圳市融泰衡实业有公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176621, UUID(), 0, 2, 'EhGroups', 176091,'深圳多多益善电子商务有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176622, UUID(), 0, 2, 'EhGroups', 176092,'深圳市小牛普惠投资管理有限公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176623, UUID(), 0, 2, 'EhGroups', 176093,'君丰创业投资基金管理有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176624, UUID(), 0, 2, 'EhGroups', 176094,'深圳证券通信有限公司','','0','0');                                                            
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176625, UUID(), 0, 2, 'EhGroups', 176095,'深圳证券通信有限公司','','0','0');                                                            
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176626, UUID(), 0, 2, 'EhGroups', 176096,'欣旺达电子股份有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176627, UUID(), 0, 2, 'EhGroups', 176097,'深圳市瓷爱谷文化有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176628, UUID(), 0, 2, 'EhGroups', 176098,'神州数码信息服务股份有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176629, UUID(), 0, 2, 'EhGroups', 176099,'北京三浦教育投资有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176630, UUID(), 0, 2, 'EhGroups', 176100,'深圳市独尊科技开发有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176631, UUID(), 0, 2, 'EhGroups', 176101,'深圳市信息大成网络有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176632, UUID(), 0, 2, 'EhGroups', 176102,'深圳通联金融网络科技服务有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176633, UUID(), 0, 2, 'EhGroups', 176103,'深圳通联金融网络科技服务有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176634, UUID(), 0, 2, 'EhGroups', 176104,'通联支付网络服务股份有限公司深圳分公司','','0','0');                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176635, UUID(), 0, 2, 'EhGroups', 176105,'深圳市软晶科技有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176636, UUID(), 0, 2, 'EhGroups', 176106,'深圳市江波龙电子有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176637, UUID(), 0, 2, 'EhGroups', 176107,'深圳市江波龙电子有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176638, UUID(), 0, 2, 'EhGroups', 176108,'深圳市江波龙电子有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176639, UUID(), 0, 2, 'EhGroups', 176109,'深圳市江波龙电子有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176640, UUID(), 0, 2, 'EhGroups', 176110,'深圳市江波龙电子有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176641, UUID(), 0, 2, 'EhGroups', 176111,'深圳市江波龙电子有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176642, UUID(), 0, 2, 'EhGroups', 176112,'深圳市江波龙电子有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176643, UUID(), 0, 2, 'EhGroups', 176113,'深圳市江波龙电子有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176644, UUID(), 0, 2, 'EhGroups', 176114,'深圳市江波龙商用设备有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176645, UUID(), 0, 2, 'EhGroups', 176115,'深圳市志鼎科技有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176646, UUID(), 0, 2, 'EhGroups', 176116,'北京银联金卡科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176647, UUID(), 0, 2, 'EhGroups', 176117,'深圳神州数码信息技术服务有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176648, UUID(), 0, 2, 'EhGroups', 176118,'神州数码(深圳)有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176649, UUID(), 0, 2, 'EhGroups', 176119,'神州数码(深圳)有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176650, UUID(), 0, 2, 'EhGroups', 176120,'上海神州数码有限公司深圳分公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176651, UUID(), 0, 2, 'EhGroups', 176121,'深圳市中天普创投资管理有限公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176652, UUID(), 0, 2, 'EhGroups', 176122,'深圳市安卓信创业投资有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176653, UUID(), 0, 2, 'EhGroups', 176123,'太平人寿保险有限公司深圳分公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176654, UUID(), 0, 2, 'EhGroups', 176124,'和阳（深圳）投资管理有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176655, UUID(), 0, 2, 'EhGroups', 176125,'深圳市昆鹏股权投资基金管理有限公司','','0','0');                                              
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176656, UUID(), 0, 2, 'EhGroups', 176126,'深圳市盘龙环境技术有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176657, UUID(), 0, 2, 'EhGroups', 176127,'深圳壹昊金融控股股份有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176658, UUID(), 0, 2, 'EhGroups', 176128,'深圳市贷帮投资担保有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176659, UUID(), 0, 2, 'EhGroups', 176129,'深圳市人和投资集团有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176660, UUID(), 0, 2, 'EhGroups', 176130,'川奇光电科技（扬州）有限公司深圳分公司','','0','0');                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176661, UUID(), 0, 2, 'EhGroups', 176131,'川元电子（扬州）有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176662, UUID(), 0, 2, 'EhGroups', 176132,'深圳市志鼎科技有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176663, UUID(), 0, 2, 'EhGroups', 176133,'深圳市惠邦知识产权代理事务所','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176664, UUID(), 0, 2, 'EhGroups', 176134,'通联支付网络服务股份有限公司深圳分公司','','0','0');                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176665, UUID(), 0, 2, 'EhGroups', 176135,'深圳嘉德瑞碳资产股份有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176666, UUID(), 0, 2, 'EhGroups', 176136,'深圳前海南方增长资产管理有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176667, UUID(), 0, 2, 'EhGroups', 176137,'安富利物流(深圳)有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176668, UUID(), 0, 2, 'EhGroups', 176138,'安富利物流(深圳)有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176669, UUID(), 0, 2, 'EhGroups', 176139,'安富利物流(深圳)有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176670, UUID(), 0, 2, 'EhGroups', 176140,'安富利物流(深圳)有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176671, UUID(), 0, 2, 'EhGroups', 176141,'安富利物流(深圳)有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176672, UUID(), 0, 2, 'EhGroups', 176142,'安富利物流(深圳)有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176673, UUID(), 0, 2, 'EhGroups', 176143,'萃冠电子贸易（深圳）有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176674, UUID(), 0, 2, 'EhGroups', 176144,'萃冠电子贸易（深圳）有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176675, UUID(), 0, 2, 'EhGroups', 176145,'萃冠电子贸易（深圳）有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176676, UUID(), 0, 2, 'EhGroups', 176146,'萃冠电子贸易（深圳）有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176677, UUID(), 0, 2, 'EhGroups', 176147,'萃冠电子贸易（深圳）有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176678, UUID(), 0, 2, 'EhGroups', 176148,'萃冠电子贸易（深圳）有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176679, UUID(), 0, 2, 'EhGroups', 176149,'安富利物流(深圳)有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176680, UUID(), 0, 2, 'EhGroups', 176150,'安霸半导体技术(上海)有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176681, UUID(), 0, 2, 'EhGroups', 176151,'安霸半导体技术(上海)有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176682, UUID(), 0, 2, 'EhGroups', 176152,'深圳中泽明芯集团有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176683, UUID(), 0, 2, 'EhGroups', 176153,'深圳市漫步者科技股份有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176684, UUID(), 0, 2, 'EhGroups', 176154,'是德科技（中国）有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176685, UUID(), 0, 2, 'EhGroups', 176155,'是德科技（中国）有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176686, UUID(), 0, 2, 'EhGroups', 176156,'博尔科通讯系统（深圳）有限公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176687, UUID(), 0, 2, 'EhGroups', 176157,'招商银行股份有限公司深圳车公庙支行','','0','0');                                              
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176688, UUID(), 0, 2, 'EhGroups', 176158,'招商银行股份有限公司深圳车公庙支行','','0','0');                                              
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176689, UUID(), 0, 2, 'EhGroups', 176159,'深圳市润利丰实业发展有限公司（老绍兴酒楼）','','0','0');                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176690, UUID(), 0, 2, 'EhGroups', 176160,'深圳市润利丰实业发展有限公司（老绍兴酒楼）','','0','0');                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176691, UUID(), 0, 2, 'EhGroups', 176161,'深圳市润利丰实业发展有限公司（老绍兴酒楼）','','0','0');                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176692, UUID(), 0, 2, 'EhGroups', 176162,'深圳市润利丰实业发展有限公司（老绍兴酒楼）','','0','0');                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176693, UUID(), 0, 2, 'EhGroups', 176163,'深圳市润利丰实业发展有限公司（老绍兴酒楼）','','0','0');                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176694, UUID(), 0, 2, 'EhGroups', 176164,'深圳市润利丰实业发展有限公司（老绍兴酒楼）','','0','0');                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176695, UUID(), 0, 2, 'EhGroups', 176165,'深圳市广源餐饮管理有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176696, UUID(), 0, 2, 'EhGroups', 176166,'深圳市广源餐饮管理有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176697, UUID(), 0, 2, 'EhGroups', 176167,'上海联广认证有限公司深圳分公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176698, UUID(), 0, 2, 'EhGroups', 176168,'上海联广认证有限公司深圳分公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176699, UUID(), 0, 2, 'EhGroups', 176169,'广东万诺律师事务所(李亚光)','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176700, UUID(), 0, 2, 'EhGroups', 176170,'杭州华三通信技术有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176701, UUID(), 0, 2, 'EhGroups', 176171,'杭州华三通信技术有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176702, UUID(), 0, 2, 'EhGroups', 176172,'深圳市恒源昊资产管理有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176703, UUID(), 0, 2, 'EhGroups', 176173,'深圳市恒源昊资产管理有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176704, UUID(), 0, 2, 'EhGroups', 176174,'深圳前海星润股权投资管理有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176705, UUID(), 0, 2, 'EhGroups', 176175,'深圳前海星润股权投资管理有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176706, UUID(), 0, 2, 'EhGroups', 176176,'深圳前海星润股权投资管理有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176707, UUID(), 0, 2, 'EhGroups', 176177,'深圳前海星润股权投资管理有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176708, UUID(), 0, 2, 'EhGroups', 176178,'深圳前海星润股权投资管理有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176709, UUID(), 0, 2, 'EhGroups', 176179,'深圳前海星润股权投资管理有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176710, UUID(), 0, 2, 'EhGroups', 176180,'塔塔信息技术(中国)股份有限公司深圳分公司','','0','0');                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176711, UUID(), 0, 2, 'EhGroups', 176181,'塔塔信息技术(中国)股份有限公司深圳分公司','','0','0');                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176712, UUID(), 0, 2, 'EhGroups', 176182,'深圳格兰泰克科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176713, UUID(), 0, 2, 'EhGroups', 176183,'深圳格兰泰克科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176714, UUID(), 0, 2, 'EhGroups', 176184,'深圳市民声科技有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176715, UUID(), 0, 2, 'EhGroups', 176185,'深圳市民声科技有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176716, UUID(), 0, 2, 'EhGroups', 176186,'深圳市民声科技有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176717, UUID(), 0, 2, 'EhGroups', 176187,'深圳市民声科技有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176718, UUID(), 0, 2, 'EhGroups', 176188,'深圳证券通信有限公司','','0','0');                                                            
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176719, UUID(), 0, 2, 'EhGroups', 176189,'深圳证券通信有限公司','','0','0');                                                            
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176720, UUID(), 0, 2, 'EhGroups', 176190,'深圳五维微品金融信息服务有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176721, UUID(), 0, 2, 'EhGroups', 176191,'深圳五维微品金融信息服务有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176722, UUID(), 0, 2, 'EhGroups', 176192,'深圳新为软件股份有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176723, UUID(), 0, 2, 'EhGroups', 176193,'深圳新为软件股份有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176724, UUID(), 0, 2, 'EhGroups', 176194,'深圳市瑞格尔健康管理科技有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176725, UUID(), 0, 2, 'EhGroups', 176195,'深圳市瑞格尔健康管理科技有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176726, UUID(), 0, 2, 'EhGroups', 176196,'深圳市瑞格尔健康管理科技有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176727, UUID(), 0, 2, 'EhGroups', 176197,'深圳市瑞格尔健康管理科技有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176728, UUID(), 0, 2, 'EhGroups', 176198,'深圳市瑞格尔健康管理科技有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176729, UUID(), 0, 2, 'EhGroups', 176199,'深圳市瑞格尔健康管理科技有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176730, UUID(), 0, 2, 'EhGroups', 176200,'深圳市瑞格尔健康管理科技有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176731, UUID(), 0, 2, 'EhGroups', 176201,'深圳市瑞格尔健康管理科技有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176732, UUID(), 0, 2, 'EhGroups', 176202,'深圳格兰泰克汽车电子有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176733, UUID(), 0, 2, 'EhGroups', 176203,'华美优科网络技术（深圳）有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176734, UUID(), 0, 2, 'EhGroups', 176204,'华美优科网络技术（深圳）有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176735, UUID(), 0, 2, 'EhGroups', 176205,'华美优科网络技术（深圳）有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176736, UUID(), 0, 2, 'EhGroups', 176206,'华美优科网络技术（深圳）有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176737, UUID(), 0, 2, 'EhGroups', 176207,'华美优科网络技术（深圳）有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176738, UUID(), 0, 2, 'EhGroups', 176208,'华美优科网络技术（深圳）有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176739, UUID(), 0, 2, 'EhGroups', 176209,'华美优科网络技术（深圳）有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176740, UUID(), 0, 2, 'EhGroups', 176210,'安霸半导体技术(上海)有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176741, UUID(), 0, 2, 'EhGroups', 176211,'安霸半导体技术(上海)有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176742, UUID(), 0, 2, 'EhGroups', 176212,'吴炎雄(深圳市南山区南风舍餐馆)','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176743, UUID(), 0, 2, 'EhGroups', 176213,'深圳集群壹家股权投资基金管理有限公司','','0','0');                                            
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176744, UUID(), 0, 2, 'EhGroups', 176214,'深圳集群壹家股权投资基金管理有限公司','','0','0');                                            
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176745, UUID(), 0, 2, 'EhGroups', 176215,'深圳华智融科技有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176746, UUID(), 0, 2, 'EhGroups', 176216,'深圳华智融科技有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176747, UUID(), 0, 2, 'EhGroups', 176217,'深圳华智融科技有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176748, UUID(), 0, 2, 'EhGroups', 176218,'深圳华智融科技有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176749, UUID(), 0, 2, 'EhGroups', 176219,'深圳华智融科技有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176750, UUID(), 0, 2, 'EhGroups', 176220,'深圳华智融科技有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176751, UUID(), 0, 2, 'EhGroups', 176221,'深圳市智美达科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176752, UUID(), 0, 2, 'EhGroups', 176222,'深圳市智美达科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176753, UUID(), 0, 2, 'EhGroups', 176223,'深圳市智美达科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176754, UUID(), 0, 2, 'EhGroups', 176224,'深圳市智美达科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176755, UUID(), 0, 2, 'EhGroups', 176225,'深圳市智美达科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176756, UUID(), 0, 2, 'EhGroups', 176226,'深圳市智美达科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176757, UUID(), 0, 2, 'EhGroups', 176227,'深圳市智美达科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176758, UUID(), 0, 2, 'EhGroups', 176228,'深圳市智美达科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176759, UUID(), 0, 2, 'EhGroups', 176229,'东吴证券股份有限公司','','0','0');                                                            
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176760, UUID(), 0, 2, 'EhGroups', 176230,'东吴证券股份有限公司','','0','0');                                                            
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176761, UUID(), 0, 2, 'EhGroups', 176231,'湖南南方稀贵金属交易所股份有限公司深圳分公司','','0','0');                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176762, UUID(), 0, 2, 'EhGroups', 176232,'湖南南方稀贵金属交易所股份有限公司深圳分公司','','0','0');                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176763, UUID(), 0, 2, 'EhGroups', 176233,'湖南南方稀贵金属交易所股份有限公司深圳分公司','','0','0');                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176764, UUID(), 0, 2, 'EhGroups', 176234,'湖南南方稀贵金属交易所股份有限公司深圳分公司','','0','0');                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176765, UUID(), 0, 2, 'EhGroups', 176235,'深圳市佳信捷技术股份有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176766, UUID(), 0, 2, 'EhGroups', 176236,'深圳市佳信捷技术股份有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176767, UUID(), 0, 2, 'EhGroups', 176237,'深圳市佳信捷技术股份有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176768, UUID(), 0, 2, 'EhGroups', 176238,'深圳市佳信捷技术股份有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176769, UUID(), 0, 2, 'EhGroups', 176239,'深圳市中兴小额贷款有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176770, UUID(), 0, 2, 'EhGroups', 176240,'深圳市中兴小额贷款有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176771, UUID(), 0, 2, 'EhGroups', 176241,'深圳市中兴小额贷款有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176772, UUID(), 0, 2, 'EhGroups', 176242,'深圳市研信科技有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176773, UUID(), 0, 2, 'EhGroups', 176243,'深圳中兴飞贷金融科技有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176774, UUID(), 0, 2, 'EhGroups', 176244,'深圳中兴飞贷金融科技有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176775, UUID(), 0, 2, 'EhGroups', 176245,'深圳中兴飞贷金融科技有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176776, UUID(), 0, 2, 'EhGroups', 176246,'深圳中兴飞贷金融科技有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176777, UUID(), 0, 2, 'EhGroups', 176247,'深圳中兴飞贷金融科技有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176778, UUID(), 0, 2, 'EhGroups', 176248,'深圳中兴飞贷金融科技有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176779, UUID(), 0, 2, 'EhGroups', 176249,'深圳中兴飞贷金融科技有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176780, UUID(), 0, 2, 'EhGroups', 176250,'深圳中兴飞贷金融科技有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176781, UUID(), 0, 2, 'EhGroups', 176251,'深圳市芯智科技有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176782, UUID(), 0, 2, 'EhGroups', 176252,'深圳市芯智科技有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176783, UUID(), 0, 2, 'EhGroups', 176253,'深圳市芯智科技有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176784, UUID(), 0, 2, 'EhGroups', 176254,'深圳市芯智科技有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176785, UUID(), 0, 2, 'EhGroups', 176255,'珠海华润银行股份有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176786, UUID(), 0, 2, 'EhGroups', 176256,'珠海华润银行股份有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176787, UUID(), 0, 2, 'EhGroups', 176257,'珠海华润银行股份有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176788, UUID(), 0, 2, 'EhGroups', 176258,'珠海华润银行股份有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176789, UUID(), 0, 2, 'EhGroups', 176259,'珠海华润银行股份有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176790, UUID(), 0, 2, 'EhGroups', 176260,'珠海华润银行股份有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176791, UUID(), 0, 2, 'EhGroups', 176261,'珠海华润银行股份有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176792, UUID(), 0, 2, 'EhGroups', 176262,'珠海华润银行股份有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176793, UUID(), 0, 2, 'EhGroups', 176263,'中国银行股份有限公司深圳市分行','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176794, UUID(), 0, 2, 'EhGroups', 176264,'中国银行股份有限公司深圳市分行','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176795, UUID(), 0, 2, 'EhGroups', 176265,'中国银行股份有限公司深圳市分行','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176796, UUID(), 0, 2, 'EhGroups', 176266,'众华会计师事务所(特殊普通合伙)深圳分所','','0','0');                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176797, UUID(), 0, 2, 'EhGroups', 176267,'众华会计师事务所(特殊普通合伙)深圳分所','','0','0');                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176798, UUID(), 0, 2, 'EhGroups', 176268,'同方(深圳)云计算技术股份有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176799, UUID(), 0, 2, 'EhGroups', 176269,'同方(深圳)云计算技术股份有限公司','','0','0');                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176800, UUID(), 0, 2, 'EhGroups', 176270,'杭州银行股份有限公司深圳分行','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176801, UUID(), 0, 2, 'EhGroups', 176271,'杭州银行股份有限公司深圳分行','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176802, UUID(), 0, 2, 'EhGroups', 176272,'杭州银行股份有限公司深圳分行','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176803, UUID(), 0, 2, 'EhGroups', 176273,'泰诺风保泰（苏州）隔热材料有限公司深圳分公司','','0','0');                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176804, UUID(), 0, 2, 'EhGroups', 176274,'泰诺风保泰（苏州）隔热材料有限公司深圳分公司','','0','0');                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176805, UUID(), 0, 2, 'EhGroups', 176275,'泰诺风保泰（苏州）隔热材料有限公司深圳分公司','','0','0');                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176806, UUID(), 0, 2, 'EhGroups', 176276,'深圳太东资本管理有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176807, UUID(), 0, 2, 'EhGroups', 176277,'深圳太东资本管理有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176808, UUID(), 0, 2, 'EhGroups', 176278,'中国人民健康保险公司深圳分公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176809, UUID(), 0, 2, 'EhGroups', 176279,'中国人民健康保险公司深圳分公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176810, UUID(), 0, 2, 'EhGroups', 176280,'深圳排放权交易所有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176811, UUID(), 0, 2, 'EhGroups', 176281,'深圳排放权交易所有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176812, UUID(), 0, 2, 'EhGroups', 176282,'深圳排放权交易所有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176813, UUID(), 0, 2, 'EhGroups', 176283,'深圳排放权交易所有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176814, UUID(), 0, 2, 'EhGroups', 176284,'深圳市慧峰高科产业园投资发展有限公司','','0','0');                                            
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176815, UUID(), 0, 2, 'EhGroups', 176285,'深圳市慧峰高科产业园投资发展有限公司','','0','0');                                            
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176816, UUID(), 0, 2, 'EhGroups', 176286,'丰益(上海)信息技术有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176817, UUID(), 0, 2, 'EhGroups', 176287,'丰益(上海)信息技术有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176818, UUID(), 0, 2, 'EhGroups', 176288,'深圳市鼎恒瑞投资有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176819, UUID(), 0, 2, 'EhGroups', 176289,'惠州硕贝德无线科技股份有限公司深圳分公司','','0','0');                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176820, UUID(), 0, 2, 'EhGroups', 176290,'惠州硕贝德无线科技股份有限公司深圳分公司','','0','0');                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176821, UUID(), 0, 2, 'EhGroups', 176291,'深圳市前海德融资本管理有限公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176822, UUID(), 0, 2, 'EhGroups', 176292,'深圳市前海德融资本管理有限公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176823, UUID(), 0, 2, 'EhGroups', 176293,'深圳市前海德融资本管理有限公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176824, UUID(), 0, 2, 'EhGroups', 176294,'深圳市前海德融资本管理有限公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176825, UUID(), 0, 2, 'EhGroups', 176295,'深圳市新天域文化产业有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176826, UUID(), 0, 2, 'EhGroups', 176296,'深圳市新天域文化产业有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176827, UUID(), 0, 2, 'EhGroups', 176297,'深圳前海厚诚敏投资控股有限公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176828, UUID(), 0, 2, 'EhGroups', 176298,'深圳前海厚诚敏投资控股有限公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176829, UUID(), 0, 2, 'EhGroups', 176299,'深圳前海厚诚敏投资控股有限公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176830, UUID(), 0, 2, 'EhGroups', 176300,'深圳前海厚诚敏投资控股有限公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176831, UUID(), 0, 2, 'EhGroups', 176301,'深圳市多元世纪信息技术有限公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176832, UUID(), 0, 2, 'EhGroups', 176302,'深圳市多元世纪信息技术有限公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176833, UUID(), 0, 2, 'EhGroups', 176303,'深圳市多元世纪信息技术有限公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176834, UUID(), 0, 2, 'EhGroups', 176304,'深圳市多元世纪信息技术有限公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176835, UUID(), 0, 2, 'EhGroups', 176305,'深圳市工夫百味投资有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176836, UUID(), 0, 2, 'EhGroups', 176306,'深圳市工夫百味投资有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176837, UUID(), 0, 2, 'EhGroups', 176307,'北京集创北方科技有限公司深圳办事处','','0','0');                                              
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176838, UUID(), 0, 2, 'EhGroups', 176308,'北京集创北方科技有限公司深圳办事处','','0','0');                                              
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176839, UUID(), 0, 2, 'EhGroups', 176309,'北京集创北方科技有限公司深圳办事处','','0','0');                                              
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176840, UUID(), 0, 2, 'EhGroups', 176310,'北京集创北方科技有限公司深圳办事处','','0','0');                                              
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176841, UUID(), 0, 2, 'EhGroups', 176311,'普联技术有限公司','','0','0');                                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176842, UUID(), 0, 2, 'EhGroups', 176312,'普联技术有限公司','','0','0');                                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176843, UUID(), 0, 2, 'EhGroups', 176313,'普联技术有限公司','','0','0');                                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176844, UUID(), 0, 2, 'EhGroups', 176314,'普联技术有限公司','','0','0');                                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176845, UUID(), 0, 2, 'EhGroups', 176315,'普联技术有限公司','','0','0');                                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176846, UUID(), 0, 2, 'EhGroups', 176316,'普联技术有限公司','','0','0');                                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176847, UUID(), 0, 2, 'EhGroups', 176317,'普联技术有限公司','','0','0');                                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176848, UUID(), 0, 2, 'EhGroups', 176318,'普联技术有限公司','','0','0');                                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176849, UUID(), 0, 2, 'EhGroups', 176319,'普联技术有限公司','','0','0');                                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176850, UUID(), 0, 2, 'EhGroups', 176320,'普联技术有限公司','','0','0');                                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176851, UUID(), 0, 2, 'EhGroups', 176321,'普联技术有限公司','','0','0');                                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176852, UUID(), 0, 2, 'EhGroups', 176322,'普联技术有限公司','','0','0');                                                                
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176853, UUID(), 0, 2, 'EhGroups', 176323,'上海赫丝蒂化妆品有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176854, UUID(), 0, 2, 'EhGroups', 176324,'上海赫丝蒂化妆品有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176855, UUID(), 0, 2, 'EhGroups', 176325,'深圳市恒顺合鑫科技有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176856, UUID(), 0, 2, 'EhGroups', 176326,'深圳市华讯万通科技有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176857, UUID(), 0, 2, 'EhGroups', 176327,'中信证券股份有限公司深圳科技园科苑路证券营业部','','0','0');                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176858, UUID(), 0, 2, 'EhGroups', 176328,'深圳市百益亚太电效工程有限公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176859, UUID(), 0, 2, 'EhGroups', 176329,'移动财经软件（深圳）有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176860, UUID(), 0, 2, 'EhGroups', 176330,'深圳金桥融付科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176861, UUID(), 0, 2, 'EhGroups', 176331,'深圳金桥融付科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176862, UUID(), 0, 2, 'EhGroups', 176332,'深圳市浅葱小唱音乐餐厅有限公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176863, UUID(), 0, 2, 'EhGroups', 176333,'深圳市城道通环保科技有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176864, UUID(), 0, 2, 'EhGroups', 176334,'深圳市城道通环保科技有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176865, UUID(), 0, 2, 'EhGroups', 176335,'北京玖富时代投资顾问有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176866, UUID(), 0, 2, 'EhGroups', 176336,'深圳易兰德金融服务有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176867, UUID(), 0, 2, 'EhGroups', 176337,'深圳易兰德金融服务有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176868, UUID(), 0, 2, 'EhGroups', 176338,'深圳易兰德金融服务有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176869, UUID(), 0, 2, 'EhGroups', 176339,'深圳易兰德金融服务有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176870, UUID(), 0, 2, 'EhGroups', 176340,'深圳铂睿智恒科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176871, UUID(), 0, 2, 'EhGroups', 176341,'深圳铂睿智恒科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176872, UUID(), 0, 2, 'EhGroups', 176342,'深圳铂睿智恒科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176873, UUID(), 0, 2, 'EhGroups', 176343,'深圳铂睿智恒科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176874, UUID(), 0, 2, 'EhGroups', 176344,'深圳市宽域智联科技有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176875, UUID(), 0, 2, 'EhGroups', 176345,'深圳市宽域智联科技有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176876, UUID(), 0, 2, 'EhGroups', 176346,'深圳前海方舟资本管理有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176877, UUID(), 0, 2, 'EhGroups', 176347,'深圳前海方舟资本管理有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176878, UUID(), 0, 2, 'EhGroups', 176348,'前海东方金德资产管理有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176879, UUID(), 0, 2, 'EhGroups', 176349,'前海东方金德资产管理有限公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176880, UUID(), 0, 2, 'EhGroups', 176350,'中民保险经纪股份有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176881, UUID(), 0, 2, 'EhGroups', 176351,'中民保险经纪股份有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176882, UUID(), 0, 2, 'EhGroups', 176352,'中民保险经纪股份有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176883, UUID(), 0, 2, 'EhGroups', 176353,'中民电子商务股份有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176884, UUID(), 0, 2, 'EhGroups', 176354,'深圳市前海野文投资管理有限公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176885, UUID(), 0, 2, 'EhGroups', 176355,'深圳市前海野文投资管理有限公司','','0','0');                                                  
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176886, UUID(), 0, 2, 'EhGroups', 176356,'英伟达半导体(深圳)有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176887, UUID(), 0, 2, 'EhGroups', 176357,'英伟达半导体(深圳)有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176888, UUID(), 0, 2, 'EhGroups', 176358,'英伟达半导体(深圳)有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176889, UUID(), 0, 2, 'EhGroups', 176359,'英伟达半导体(深圳)有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176890, UUID(), 0, 2, 'EhGroups', 176360,'英伟达半导体(深圳)有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176891, UUID(), 0, 2, 'EhGroups', 176361,'英伟达半导体(深圳)有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176892, UUID(), 0, 2, 'EhGroups', 176362,'英伟达半导体(深圳)有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176893, UUID(), 0, 2, 'EhGroups', 176363,'英伟达半导体(深圳)有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176894, UUID(), 0, 2, 'EhGroups', 176364,'英伟达半导体(深圳)有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176895, UUID(), 0, 2, 'EhGroups', 176365,'百度(中国)有限公司深圳分公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176896, UUID(), 0, 2, 'EhGroups', 176366,'百度(中国)有限公司深圳分公司','','0','0');                                                    
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176897, UUID(), 0, 2, 'EhGroups', 176367,'北京百度网讯科技有限公司深圳分公司','','0','0');                                              
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176898, UUID(), 0, 2, 'EhGroups', 176368,'北京百度网讯科技有限公司深圳分公司','','0','0');                                              
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176899, UUID(), 0, 2, 'EhGroups', 176369,'深圳市福尔科技有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176900, UUID(), 0, 2, 'EhGroups', 176370,'深圳市德维莱科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176901, UUID(), 0, 2, 'EhGroups', 176371,'深圳市宝富利科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176902, UUID(), 0, 2, 'EhGroups', 176372,'深圳市大维纳米科技有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176903, UUID(), 0, 2, 'EhGroups', 176373,'金宝通电子(深圳)有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176904, UUID(), 0, 2, 'EhGroups', 176374,'深圳市亮信科技有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176905, UUID(), 0, 2, 'EhGroups', 176375,'汎达科技(深圳)有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176906, UUID(), 0, 2, 'EhGroups', 176376,'群锋电子(深圳)有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176907, UUID(), 0, 2, 'EhGroups', 176377,'诺华达电子(深圳)有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176908, UUID(), 0, 2, 'EhGroups', 176378,'新纬科技(深圳)有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176909, UUID(), 0, 2, 'EhGroups', 176379,'深圳市领平科技有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176910, UUID(), 0, 2, 'EhGroups', 176380,'北京美餐巧达科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176911, UUID(), 0, 2, 'EhGroups', 176381,'镭射谷科(深圳)有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176912, UUID(), 0, 2, 'EhGroups', 176382,'镭射谷科(深圳)有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176913, UUID(), 0, 2, 'EhGroups', 176383,'镭射谷科(深圳)有限公司','','0','0');                                                          
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176914, UUID(), 0, 2, 'EhGroups', 176384,'深圳市安普盛科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176915, UUID(), 0, 2, 'EhGroups', 176385,'深圳市安特讯科技有限公司','','0','0');                                                        
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176916, UUID(), 0, 2, 'EhGroups', 176386,'深圳市开拓汽车电子有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176917, UUID(), 0, 2, 'EhGroups', 176387,'深圳深港生产力基地有限公司','','0','0');                                                      
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES(176918, UUID(), 0, 2, 'EhGroups', 176388,'深圳市商业联合会','','0','0');                                                                




