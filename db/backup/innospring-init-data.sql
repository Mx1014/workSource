INSERT INTO `eh_namespaces`(`id`, `name`) VALUES(999986, 'innospring');

INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES(1014, 999986, 'community_commercial', '2016-09-26 18:07:50'); 

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('280', 'app.agreements.url', 'http://sywy.zuolin.com/mobile/static/app_agreements/cy_agreements.html', 'the relative path for innospring app agreements', '999986', NULL);

INSERT INTO `eh_version_realm` VALUES ('65', 'Android_Innospring', null, UTC_TIMESTAMP(), '999986');
INSERT INTO `eh_version_realm` VALUES ('66', 'iOS_Innospring', null, UTC_TIMESTAMP(), '999986');

insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) values(113,65,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) values(114,66,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());

INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999986, 'sms.default.yzx', 1, 'zh_CN', '验证码-innospring', '30099');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999986, 'sms.default.yzx', 4, 'zh_CN', '派单-innospring', '30100');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999986, 'sms.default.yzx', 6, 'zh_CN', '任务2-innospring', '30102');    

INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('sms.default.yzx', '11', 'zh_CN', '物业任务-创源', '30109', '999986');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('sms.default.yzx', '10', 'zh_CN', '物业任务2-创源', '30110', '999986');


INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (233082  , UUID(), '9202013', 'innospring', 'cs://1/image/aW1hZ2UvTVRvMlkySmhNbVZqTm1SaU1UQXdPREkxWkRjME5HVmxNVFU1TXpBNE5UUTBZdw', 1, 45, '1', '2',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (228615 , 233082  ,  '0',  '18051307125',  '221616',  3, UTC_TIMESTAMP(), 999986);

INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`)
	VALUES ('15102', '11982', '崇川区', 'CHONGCHUANQU', 'CCQ', '/江苏/南通市/崇川区', '3', '3', NULL, '0513', '2', '0', '0');

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`) 
	VALUES(1004140, 0, 'PM', '南通创源科技园发展有限公司', 0, '', '/1004140', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1112511, 240111044331054735, 'organization', 1004140, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`)
	VALUES(2111953, 1004140, 'USER', 233082  , 'manager', 'innospring', 0, '18051307125', 3, 999986);	

INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(11668, 'EhOrganizations', 1004140, 'EhUsers', 233082  , 1001, 1, UTC_TIMESTAMP());
	
INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES(1554, 999986, 'COMMUNITY', 240111044331054735, UTC_TIMESTAMP());

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(182102, UUID(), 999986, 2, 'EhGroups', 0,'南通创源科技园论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(182103, UUID(), 999986, 2, 'EhGroups', 0,'南通创源科技园意见反馈论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1004537, UUID(), '南通创源', '南通创源', 1, 1, 1004140, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 182104, 1, 999986); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(182104, UUID(), 999986, 2, 'EhGroups', 1004537,'南通创源','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());

INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES(240111044331054735, UUID(), 11982, '南通市',  15102, '崇川区', '南通创源科技园', '南通创源科技园', '南通市崇川区、星明路299号、世伦路300号、盘香路2号。', NULL, '南通创源科技园由创源（InnoSpring）与南通市政府于2013年4月2日正式签署协议合作发起设立，是南通市确定的重点科技工程和重点产业工程。园区位于星明路东、中南世纪花城南、崇川路北、通富路西，总规划建筑面积约15万平方米，由南通创源科技园发展有限公司负责运营管理。南通创源科技园发展有限公司在园区的建设与发展过程中，紧抓南通市创新发展的重要机遇，充分融入南通市以研究机构为引领，科技园区为依托、产业基地为支撑的“三位一体”创新体系，同时构建起产业发展支撑体系，聚焦智慧建筑、新材料、先进制造等与南通产业优势密切结合的新兴产业。',
	NULL, NULL, NULL, NULL, NULL, NULL,NULL, 457, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'1', 182102, 182103, UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES(240111044331049438, 240111044331054735, '', 120.926473, 31.983689, 'wttvzwryrpc8');
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(1004140, 240111044331054735);
	
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(180000, 240111044331054735, '1幢', '1幢', 0, NULL, '江苏省南通市崇川区南通创源科技园', 4412.61, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999986);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(180001, 240111044331054735, '2幢', '2幢', 0, NULL, '江苏省南通市崇川区南通创源科技园', 12951.86, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999986);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(180002, 240111044331054735, '3幢', '3幢', 0, NULL, '江苏省南通市崇川区南通创源科技园', 11481.96, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999986);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(180003, 240111044331054735, '4幢', '4幢', 0, NULL, '江苏省南通市崇川区南通创源科技园', NULL, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999986);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(180004, 240111044331054735, '5幢', '5幢', 0, NULL, '江苏省南通市崇川区南通创源科技园', 13052.56, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999986);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(180005, 240111044331054735, '6幢', '6幢', 0, NULL, '江苏省南通市崇川区南通创源科技园', 14866, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999986);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(180006, 240111044331054735, '7幢', '7幢', 0, NULL, '江苏省南通市崇川区南通创源科技园', 14610.19, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999986);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(180007, 240111044331054735, '8幢', '8幢', 0, NULL, '江苏省南通市崇川区南通创源科技园', 25856.82, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999986);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(180008, 240111044331054735, '9幢', '9幢', 0, NULL, '江苏省南通市崇川区南通创源科技园', 11683.04, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125229,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-101','1幢','101','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125230,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-102','1幢','102','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125231,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-103','1幢','103','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125232,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-104','1幢','104','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125233,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-105','1幢','105','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125234,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-106','1幢','106','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125235,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-107','1幢','107','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125236,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-108','1幢','108','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125237,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-109','1幢','109','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125238,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-1010','1幢','1010','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125239,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-1011','1幢','1011','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125240,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-1012','1幢','1012','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125241,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-201','1幢','201','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125242,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-202','1幢','202','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125243,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-203','1幢','203','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125244,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-204','1幢','204','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125245,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-205','1幢','205','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125246,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-206','1幢','206','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125247,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-207','1幢','207','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125248,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-208','1幢','208','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125249,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-209','1幢','209','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125250,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-2010','1幢','2010','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125251,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-2011','1幢','2011','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125252,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-302','1幢','302','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125253,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-101','2幢','101','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125254,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-102','2幢','102','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125255,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-103','2幢','103','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125256,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-104','2幢','104','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125257,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-105','2幢','105','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125258,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-106','2幢','106','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125259,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-201','2幢','201','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125260,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-202','2幢','202','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125261,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-203','2幢','203','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125262,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-204','2幢','204','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125263,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-205','2幢','205','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125264,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-301','2幢','301','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125265,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-302','2幢','302','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125266,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-303','2幢','303','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125267,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-304','2幢','304','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125268,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-305','2幢','305','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125269,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-306','2幢','306','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125270,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-401','2幢','401','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125271,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-402','2幢','402','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125272,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-403','2幢','403','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125273,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-404','2幢','404','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125274,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-405','2幢','405','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125275,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-406','2幢','406','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125276,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-501','2幢','501','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125277,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-502','2幢','502','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125278,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-503','2幢','503','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125279,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-504','2幢','504','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125280,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-505','2幢','505','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125281,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-506','2幢','506','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125282,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-601','2幢','601','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125283,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-602','2幢','602','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125284,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-603','2幢','603','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125285,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-604','2幢','604','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125286,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-605','2幢','605','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125287,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-606','2幢','606','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125288,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-701','2幢','701','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125289,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-702','2幢','702','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125290,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-703','2幢','703','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125291,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-704','2幢','704','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125292,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-705','2幢','705','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125293,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-706','2幢','706','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125294,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-801','2幢','801','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125295,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-802','2幢','802','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125296,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-803','2幢','803','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125297,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-804','2幢','804','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125298,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-805','2幢','805','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125299,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-806','2幢','806','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125300,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-901','2幢','901','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125301,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-902','2幢','902','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125302,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-903','2幢','903','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125303,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-904','2幢','904','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125304,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-905','2幢','905','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125305,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-906','2幢','906','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125306,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-101','3幢','101','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125307,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-102','3幢','102','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125308,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-103','3幢','103','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125309,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-104','3幢','104','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125310,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-105','3幢','105','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125311,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-201','3幢','201','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125312,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-202','3幢','202','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125313,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-203','3幢','203','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125314,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-204','3幢','204','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125315,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-205','3幢','205','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125316,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-206','3幢','206','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125317,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-301','3幢','301','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125318,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-302','3幢','302','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125319,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-303','3幢','303','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125320,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-304','3幢','304','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125321,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-305','3幢','305','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125322,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-306','3幢','306','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125323,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-401','3幢','401','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125324,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-402','3幢','402','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125325,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-403','3幢','403','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125326,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-404','3幢','404','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125327,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-405','3幢','405','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125328,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-406','3幢','406','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125329,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-501','3幢','501','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125330,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-502','3幢','502','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125331,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-503','3幢','503','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125332,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-504','3幢','504','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125333,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-505','3幢','505','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125334,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-506','3幢','506','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125335,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-601','3幢','601','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125336,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-602','3幢','602','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125337,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-603','3幢','603','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125338,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-604','3幢','604','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125339,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-605','3幢','605','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125340,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-606','3幢','606','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125341,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-701','3幢','701','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125342,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-702','3幢','702','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125343,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-703','3幢','703','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125344,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-704','3幢','704','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125345,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-705','3幢','705','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125346,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-706','3幢','706','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125347,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-801','3幢','801','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125348,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-802','3幢','802','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125349,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-803','3幢','803','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125350,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-804','3幢','804','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125351,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-805','3幢','805','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125352,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-806','3幢','806','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125353,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-101','5幢','101','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125354,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-102','5幢','102','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125355,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-103','5幢','103','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125356,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-104','5幢','104','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125357,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-105','5幢','105','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125358,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-106','5幢','106','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125359,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-107','5幢','107','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125360,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-201','5幢','201','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125361,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-202','5幢','202','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125362,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-203','5幢','203','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125363,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-204','5幢','204','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125364,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-205','5幢','205','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125365,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-206','5幢','206','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125366,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-301','5幢','301','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125367,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-302','5幢','302','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125368,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-303','5幢','303','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125369,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-304','5幢','304','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125370,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-305','5幢','305','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125371,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-306','5幢','306','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125372,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-401','5幢','401','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125373,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-402','5幢','402','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125374,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-403','5幢','403','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125375,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-404','5幢','404','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125376,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-405','5幢','405','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125377,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-406','5幢','406','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125378,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-501','5幢','501','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125379,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-502','5幢','502','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125380,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-503','5幢','503','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125381,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-504','5幢','504','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125382,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-505','5幢','505','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125383,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-506','5幢','506','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125384,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-601','5幢','601','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125385,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-602','5幢','602','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125386,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-603','5幢','603','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125387,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-604','5幢','604','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125388,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-605','5幢','605','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125389,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-606','5幢','606','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125390,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-701','5幢','701','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125391,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-702','5幢','702','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125392,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-703','5幢','703','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125393,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-704','5幢','704','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125394,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-705','5幢','705','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125395,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-706','5幢','706','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125396,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-801','5幢','801','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125397,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-802','5幢','802','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125398,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-803','5幢','803','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125399,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-804','5幢','804','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125400,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-805','5幢','805','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125401,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-806','5幢','806','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125402,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-901','5幢','901','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125403,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-902','5幢','902','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125404,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-903','5幢','903','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125405,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-904','5幢','904','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125406,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-905','5幢','905','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125407,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-906','5幢','906','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125408,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-101','6幢','101','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125409,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-102','6幢','102','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125410,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-103','6幢','103','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125411,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-104','6幢','104','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125412,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-201','6幢','201','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125413,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-202','6幢','202','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125414,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-203','6幢','203','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125415,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-204','6幢','204','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125416,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-205','6幢','205','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125417,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-206','6幢','206','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125418,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-301','6幢','301','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125419,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-302','6幢','302','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125420,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-303','6幢','303','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125421,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-304','6幢','304','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125422,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-305','6幢','305','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125423,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-306','6幢','306','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125424,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-401','6幢','401','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125425,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-402','6幢','402','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125426,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-403','6幢','403','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125427,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-404','6幢','404','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125428,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-405','6幢','405','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125429,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-406','6幢','406','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125430,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-501','6幢','501','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125431,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-502','6幢','502','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125432,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-503','6幢','503','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125433,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-504','6幢','504','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125434,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-505','6幢','505','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125435,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-506','6幢','506','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125436,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-601','6幢','601','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125437,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-602','6幢','602','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125438,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-603','6幢','603','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125439,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-604','6幢','604','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125440,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-605','6幢','605','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125441,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-606','6幢','606','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125442,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-701','6幢','701','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125443,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-702','6幢','702','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125444,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-703','6幢','703','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125445,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-704','6幢','704','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125446,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-705','6幢','705','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125447,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-706','6幢','706','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125448,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-801','6幢','801','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125449,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-802','6幢','802','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125450,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-803','6幢','803','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125451,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-804','6幢','804','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125452,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-805','6幢','805','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125453,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-806','6幢','806','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125454,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-901','6幢','901','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125455,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-902','6幢','902','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125456,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-903','6幢','903','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125457,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-904','6幢','904','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125458,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-905','6幢','905','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125459,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-906','6幢','906','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125460,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-1001','6幢','1001','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125461,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-1002','6幢','1002','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125462,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-1003','6幢','1003','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125463,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-1004','6幢','1004','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125464,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-1005','6幢','1005','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125465,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-1006','6幢','1006','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125466,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-101','7幢','101','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125467,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-102','7幢','102','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125468,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-103','7幢','103','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125469,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-105','7幢','105','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125470,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-201','7幢','201','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125471,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-202','7幢','202','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125472,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-203','7幢','203','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125473,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-204','7幢','204','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125474,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-205','7幢','205','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125475,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-301','7幢','301','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125476,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-302','7幢','302','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125477,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-303','7幢','303','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125478,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-304','7幢','304','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125479,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-305','7幢','305','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125480,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-306','7幢','306','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125481,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-401','7幢','401','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125482,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-402','7幢','402','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125483,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-403','7幢','403','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125484,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-404','7幢','404','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125485,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-405','7幢','405','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125486,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-406','7幢','406','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125487,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-501','7幢','501','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125488,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-502','7幢','502','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125489,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-503','7幢','503','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125490,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-504','7幢','504','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125491,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-505','7幢','505','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125492,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-506','7幢','506','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125493,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-601','7幢','601','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125494,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-602','7幢','602','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125495,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-603','7幢','603','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125496,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-604','7幢','604','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125497,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-605','7幢','605','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125498,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-606','7幢','606','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125499,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-701','7幢','701','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125500,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-702','7幢','702','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125501,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-703','7幢','703','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125502,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-704','7幢','704','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125503,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-705','7幢','705','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125504,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-706','7幢','706','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125505,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-801','7幢','801','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125506,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-802','7幢','802','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125507,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-803','7幢','803','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125508,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-804','7幢','804','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125509,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-805','7幢','805','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125510,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-806','7幢','806','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125511,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-901','7幢','901','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125512,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-902','7幢','902','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125513,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-903','7幢','903','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125514,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-904','7幢','904','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125515,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-905','7幢','905','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125516,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-906','7幢','906','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125517,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-1001','7幢','1001','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125518,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-1002','7幢','1002','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125519,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-1003','7幢','1003','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125520,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-1004','7幢','1004','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125521,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-1005','7幢','1005','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125522,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-1006','7幢','1006','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125523,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-101','8幢','101','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125524,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-102','8幢','102','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125525,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-103','8幢','103','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125526,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-104','8幢','104','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125527,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-105','8幢','105','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125528,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-106','8幢','106','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125529,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-107','8幢','107','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125530,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-108','8幢','108','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125531,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-109','8幢','109','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125532,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1010','8幢','1010','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125533,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1011','8幢','1011','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125534,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1012','8幢','1012','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125535,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-201','8幢','201','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125536,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-202','8幢','202','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125537,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-203','8幢','203','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125538,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-207','8幢','207','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125539,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-208','8幢','208','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125540,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-209','8幢','209','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125541,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-2012','8幢','2012','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125542,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-2013','8幢','2013','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125543,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-2014','8幢','2014','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125544,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-301','8幢','301','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125545,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-302','8幢','302','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125546,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-303','8幢','303','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125547,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-304','8幢','304','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125548,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-305','8幢','305','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125549,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-306','8幢','306','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125550,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-307','8幢','307','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125551,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-308','8幢','308','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125552,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-309','8幢','309','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125553,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-3010','8幢','3010','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125554,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-3011','8幢','3011','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125555,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-3012','8幢','3012','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125556,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-3013','8幢','3013','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125557,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-401','8幢','401','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125558,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-402','8幢','402','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125559,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-403','8幢','403','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125560,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-404','8幢','404','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125561,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-405','8幢','405','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125562,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-406','8幢','406','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125563,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-407','8幢','407','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125564,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-408','8幢','408','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125565,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-501','8幢','501','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125566,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-502','8幢','502','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125567,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-503','8幢','503','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125568,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-504','8幢','504','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125569,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-505','8幢','505','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125570,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-506','8幢','506','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125571,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-507','8幢','507','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125572,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-507','8幢','507','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125573,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-601','8幢','601','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125574,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-602','8幢','602','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125575,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-603','8幢','603','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125576,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-604','8幢','604','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125577,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-605','8幢','605','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125578,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-606','8幢','606','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125579,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-607','8幢','607','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125580,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-608','8幢','608','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125581,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-701','8幢','701','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125582,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-702','8幢','702','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125583,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-703','8幢','703','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125584,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-704','8幢','704','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125585,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-705','8幢','705','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125586,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-706','8幢','706','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125587,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-707','8幢','707','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125588,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-708','8幢','708','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125589,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-801','8幢','801','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125590,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-802','8幢','802','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125591,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-803','8幢','803','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125592,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-804','8幢','804','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125593,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-805','8幢','805','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125594,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-806','8幢','806','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125595,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-807','8幢','807','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125596,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-808','8幢','808','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125597,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-901','8幢','901','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125598,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-902','8幢','902','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125599,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-903','8幢','903','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125600,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-904','8幢','904','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125601,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-905','8幢','905','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125602,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-906','8幢','906','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125603,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-907','8幢','907','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125604,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-908','8幢','908','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125605,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1001','8幢','1001','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125606,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1002','8幢','1002','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125607,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1003','8幢','1003','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125608,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1004','8幢','1004','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125609,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1005','8幢','1005','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125610,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1006','8幢','1006','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125611,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1007','8幢','1007','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125612,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1008','8幢','1008','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125613,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1101','8幢','1101','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125614,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1102','8幢','1102','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125615,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1103','8幢','1103','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125616,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1104','8幢','1104','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125617,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1105','8幢','1105','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125618,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1106','8幢','1106','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125619,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1107','8幢','1107','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125620,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1108','8幢','1108','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125621,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1201','8幢','1201','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125622,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1202','8幢','1202','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125623,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1203','8幢','1203','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125624,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1204','8幢','1204','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125625,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1205','8幢','1205','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125626,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1206','8幢','1206','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125627,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1207','8幢','1207','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125628,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1208','8幢','1208','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125629,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1301','8幢','1301','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125630,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1302','8幢','1302','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125631,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1303','8幢','1303','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125632,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1304','8幢','1304','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125633,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1305','8幢','1305','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125634,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1306','8幢','1306','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125635,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1401','8幢','1401','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125636,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1402','8幢','1402','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125637,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1403','8幢','1403','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125638,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1404','8幢','1404','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125639,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1405','8幢','1405','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125640,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1406','8幢','1406','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125641,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-101','9幢','101','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125642,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-102','9幢','102','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125643,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-103','9幢','103','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125644,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-104','9幢','104','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125645,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-201','9幢','201','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125646,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-202','9幢','202','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125647,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-203','9幢','203','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125648,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-204','9幢','204','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125649,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-205','9幢','205','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125650,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-301','9幢','301','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125651,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-302','9幢','302','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125652,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-303','9幢','303','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125653,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-304','9幢','304','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125654,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-305','9幢','305','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125655,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-306','9幢','306','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125656,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-401','9幢','401','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125657,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-402','9幢','402','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125658,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-403','9幢','403','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125659,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-404','9幢','404','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125660,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-405','9幢','405','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125661,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-406','9幢','406','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125662,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-501','9幢','501','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125663,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-502','9幢','502','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125664,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-503','9幢','503','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125665,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-504','9幢','504','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125666,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-505','9幢','505','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125667,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-506','9幢','506','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125668,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-601','9幢','601','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125669,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-602','9幢','602','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125670,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-603','9幢','603','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125671,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-604','9幢','604','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125672,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-605','9幢','605','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125673,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-606','9幢','606','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125674,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-701','9幢','701','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125675,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-702','9幢','702','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125676,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-703','9幢','703','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125677,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-704','9幢','704','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125678,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-705','9幢','705','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125679,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-706','9幢','706','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125680,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-801','9幢','801','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125681,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-802','9幢','802','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125682,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-803','9幢','803','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125683,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-804','9幢','804','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125684,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-805','9幢','805','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387125685,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-806','9幢','806','2','0',UTC_TIMESTAMP(), 999986);
	
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24695, 1004140, 240111044331054735, 239825274387125229, '1幢-101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24696, 1004140, 240111044331054735, 239825274387125230, '1幢-102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24697, 1004140, 240111044331054735, 239825274387125231, '1幢-103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24698, 1004140, 240111044331054735, 239825274387125232, '1幢-104', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24699, 1004140, 240111044331054735, 239825274387125233, '1幢-105', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24700, 1004140, 240111044331054735, 239825274387125234, '1幢-106', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24701, 1004140, 240111044331054735, 239825274387125235, '1幢-107', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24702, 1004140, 240111044331054735, 239825274387125236, '1幢-108', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24703, 1004140, 240111044331054735, 239825274387125237, '1幢-109', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24704, 1004140, 240111044331054735, 239825274387125238, '1幢-1010', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24705, 1004140, 240111044331054735, 239825274387125239, '1幢-1011', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24706, 1004140, 240111044331054735, 239825274387125240, '1幢-1012', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24707, 1004140, 240111044331054735, 239825274387125241, '1幢-201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24708, 1004140, 240111044331054735, 239825274387125242, '1幢-202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24709, 1004140, 240111044331054735, 239825274387125243, '1幢-203', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24710, 1004140, 240111044331054735, 239825274387125244, '1幢-204', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24711, 1004140, 240111044331054735, 239825274387125245, '1幢-205', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24712, 1004140, 240111044331054735, 239825274387125246, '1幢-206', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24713, 1004140, 240111044331054735, 239825274387125247, '1幢-207', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24714, 1004140, 240111044331054735, 239825274387125248, '1幢-208', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24715, 1004140, 240111044331054735, 239825274387125249, '1幢-209', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24716, 1004140, 240111044331054735, 239825274387125250, '1幢-2010', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24717, 1004140, 240111044331054735, 239825274387125251, '1幢-2011', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24718, 1004140, 240111044331054735, 239825274387125252, '1幢-302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24719, 1004140, 240111044331054735, 239825274387125253, '2幢-101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24720, 1004140, 240111044331054735, 239825274387125254, '2幢-102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24721, 1004140, 240111044331054735, 239825274387125255, '2幢-103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24722, 1004140, 240111044331054735, 239825274387125256, '2幢-104', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24723, 1004140, 240111044331054735, 239825274387125257, '2幢-105', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24724, 1004140, 240111044331054735, 239825274387125258, '2幢-106', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24725, 1004140, 240111044331054735, 239825274387125259, '2幢-201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24726, 1004140, 240111044331054735, 239825274387125260, '2幢-202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24727, 1004140, 240111044331054735, 239825274387125261, '2幢-203', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24728, 1004140, 240111044331054735, 239825274387125262, '2幢-204', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24729, 1004140, 240111044331054735, 239825274387125263, '2幢-205', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24730, 1004140, 240111044331054735, 239825274387125264, '2幢-301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24731, 1004140, 240111044331054735, 239825274387125265, '2幢-302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24732, 1004140, 240111044331054735, 239825274387125266, '2幢-303', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24733, 1004140, 240111044331054735, 239825274387125267, '2幢-304', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24734, 1004140, 240111044331054735, 239825274387125268, '2幢-305', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24735, 1004140, 240111044331054735, 239825274387125269, '2幢-306', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24736, 1004140, 240111044331054735, 239825274387125270, '2幢-401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24737, 1004140, 240111044331054735, 239825274387125271, '2幢-402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24738, 1004140, 240111044331054735, 239825274387125272, '2幢-403', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24739, 1004140, 240111044331054735, 239825274387125273, '2幢-404', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24740, 1004140, 240111044331054735, 239825274387125274, '2幢-405', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24741, 1004140, 240111044331054735, 239825274387125275, '2幢-406', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24742, 1004140, 240111044331054735, 239825274387125276, '2幢-501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24743, 1004140, 240111044331054735, 239825274387125277, '2幢-502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24744, 1004140, 240111044331054735, 239825274387125278, '2幢-503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24745, 1004140, 240111044331054735, 239825274387125279, '2幢-504', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24746, 1004140, 240111044331054735, 239825274387125280, '2幢-505', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24747, 1004140, 240111044331054735, 239825274387125281, '2幢-506', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24748, 1004140, 240111044331054735, 239825274387125282, '2幢-601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24749, 1004140, 240111044331054735, 239825274387125283, '2幢-602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24750, 1004140, 240111044331054735, 239825274387125284, '2幢-603', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24751, 1004140, 240111044331054735, 239825274387125285, '2幢-604', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24752, 1004140, 240111044331054735, 239825274387125286, '2幢-605', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24753, 1004140, 240111044331054735, 239825274387125287, '2幢-606', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24754, 1004140, 240111044331054735, 239825274387125288, '2幢-701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24755, 1004140, 240111044331054735, 239825274387125289, '2幢-702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24756, 1004140, 240111044331054735, 239825274387125290, '2幢-703', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24757, 1004140, 240111044331054735, 239825274387125291, '2幢-704', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24758, 1004140, 240111044331054735, 239825274387125292, '2幢-705', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24759, 1004140, 240111044331054735, 239825274387125293, '2幢-706', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24760, 1004140, 240111044331054735, 239825274387125294, '2幢-801', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24761, 1004140, 240111044331054735, 239825274387125295, '2幢-802', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24762, 1004140, 240111044331054735, 239825274387125296, '2幢-803', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24763, 1004140, 240111044331054735, 239825274387125297, '2幢-804', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24764, 1004140, 240111044331054735, 239825274387125298, '2幢-805', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24765, 1004140, 240111044331054735, 239825274387125299, '2幢-806', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24766, 1004140, 240111044331054735, 239825274387125300, '2幢-901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24767, 1004140, 240111044331054735, 239825274387125301, '2幢-902', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24768, 1004140, 240111044331054735, 239825274387125302, '2幢-903', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24769, 1004140, 240111044331054735, 239825274387125303, '2幢-904', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24770, 1004140, 240111044331054735, 239825274387125304, '2幢-905', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24771, 1004140, 240111044331054735, 239825274387125305, '2幢-906', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24772, 1004140, 240111044331054735, 239825274387125306, '3幢-101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24773, 1004140, 240111044331054735, 239825274387125307, '3幢-102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24774, 1004140, 240111044331054735, 239825274387125308, '3幢-103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24775, 1004140, 240111044331054735, 239825274387125309, '3幢-104', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24776, 1004140, 240111044331054735, 239825274387125310, '3幢-105', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24777, 1004140, 240111044331054735, 239825274387125311, '3幢-201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24778, 1004140, 240111044331054735, 239825274387125312, '3幢-202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24779, 1004140, 240111044331054735, 239825274387125313, '3幢-203', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24780, 1004140, 240111044331054735, 239825274387125314, '3幢-204', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24781, 1004140, 240111044331054735, 239825274387125315, '3幢-205', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24782, 1004140, 240111044331054735, 239825274387125316, '3幢-206', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24783, 1004140, 240111044331054735, 239825274387125317, '3幢-301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24784, 1004140, 240111044331054735, 239825274387125318, '3幢-302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24785, 1004140, 240111044331054735, 239825274387125319, '3幢-303', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24786, 1004140, 240111044331054735, 239825274387125320, '3幢-304', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24787, 1004140, 240111044331054735, 239825274387125321, '3幢-305', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24788, 1004140, 240111044331054735, 239825274387125322, '3幢-306', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24789, 1004140, 240111044331054735, 239825274387125323, '3幢-401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24790, 1004140, 240111044331054735, 239825274387125324, '3幢-402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24791, 1004140, 240111044331054735, 239825274387125325, '3幢-403', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24792, 1004140, 240111044331054735, 239825274387125326, '3幢-404', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24793, 1004140, 240111044331054735, 239825274387125327, '3幢-405', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24794, 1004140, 240111044331054735, 239825274387125328, '3幢-406', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24795, 1004140, 240111044331054735, 239825274387125329, '3幢-501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24796, 1004140, 240111044331054735, 239825274387125330, '3幢-502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24797, 1004140, 240111044331054735, 239825274387125331, '3幢-503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24798, 1004140, 240111044331054735, 239825274387125332, '3幢-504', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24799, 1004140, 240111044331054735, 239825274387125333, '3幢-505', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24800, 1004140, 240111044331054735, 239825274387125334, '3幢-506', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24801, 1004140, 240111044331054735, 239825274387125335, '3幢-601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24802, 1004140, 240111044331054735, 239825274387125336, '3幢-602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24803, 1004140, 240111044331054735, 239825274387125337, '3幢-603', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24804, 1004140, 240111044331054735, 239825274387125338, '3幢-604', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24805, 1004140, 240111044331054735, 239825274387125339, '3幢-605', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24806, 1004140, 240111044331054735, 239825274387125340, '3幢-606', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24807, 1004140, 240111044331054735, 239825274387125341, '3幢-701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24808, 1004140, 240111044331054735, 239825274387125342, '3幢-702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24809, 1004140, 240111044331054735, 239825274387125343, '3幢-703', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24810, 1004140, 240111044331054735, 239825274387125344, '3幢-704', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24811, 1004140, 240111044331054735, 239825274387125345, '3幢-705', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24812, 1004140, 240111044331054735, 239825274387125346, '3幢-706', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24813, 1004140, 240111044331054735, 239825274387125347, '3幢-801', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24814, 1004140, 240111044331054735, 239825274387125348, '3幢-802', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24815, 1004140, 240111044331054735, 239825274387125349, '3幢-803', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24816, 1004140, 240111044331054735, 239825274387125350, '3幢-804', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24817, 1004140, 240111044331054735, 239825274387125351, '3幢-805', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24818, 1004140, 240111044331054735, 239825274387125352, '3幢-806', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24819, 1004140, 240111044331054735, 239825274387125353, '5幢-101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24820, 1004140, 240111044331054735, 239825274387125354, '5幢-102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24821, 1004140, 240111044331054735, 239825274387125355, '5幢-103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24822, 1004140, 240111044331054735, 239825274387125356, '5幢-104', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24823, 1004140, 240111044331054735, 239825274387125357, '5幢-105', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24824, 1004140, 240111044331054735, 239825274387125358, '5幢-106', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24825, 1004140, 240111044331054735, 239825274387125359, '5幢-107', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24826, 1004140, 240111044331054735, 239825274387125360, '5幢-201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24827, 1004140, 240111044331054735, 239825274387125361, '5幢-202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24828, 1004140, 240111044331054735, 239825274387125362, '5幢-203', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24829, 1004140, 240111044331054735, 239825274387125363, '5幢-204', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24830, 1004140, 240111044331054735, 239825274387125364, '5幢-205', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24831, 1004140, 240111044331054735, 239825274387125365, '5幢-206', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24832, 1004140, 240111044331054735, 239825274387125366, '5幢-301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24833, 1004140, 240111044331054735, 239825274387125367, '5幢-302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24834, 1004140, 240111044331054735, 239825274387125368, '5幢-303', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24835, 1004140, 240111044331054735, 239825274387125369, '5幢-304', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24836, 1004140, 240111044331054735, 239825274387125370, '5幢-305', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24837, 1004140, 240111044331054735, 239825274387125371, '5幢-306', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24838, 1004140, 240111044331054735, 239825274387125372, '5幢-401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24839, 1004140, 240111044331054735, 239825274387125373, '5幢-402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24840, 1004140, 240111044331054735, 239825274387125374, '5幢-403', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24841, 1004140, 240111044331054735, 239825274387125375, '5幢-404', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24842, 1004140, 240111044331054735, 239825274387125376, '5幢-405', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24843, 1004140, 240111044331054735, 239825274387125377, '5幢-406', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24844, 1004140, 240111044331054735, 239825274387125378, '5幢-501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24845, 1004140, 240111044331054735, 239825274387125379, '5幢-502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24846, 1004140, 240111044331054735, 239825274387125380, '5幢-503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24847, 1004140, 240111044331054735, 239825274387125381, '5幢-504', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24848, 1004140, 240111044331054735, 239825274387125382, '5幢-505', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24849, 1004140, 240111044331054735, 239825274387125383, '5幢-506', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24850, 1004140, 240111044331054735, 239825274387125384, '5幢-601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24851, 1004140, 240111044331054735, 239825274387125385, '5幢-602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24852, 1004140, 240111044331054735, 239825274387125386, '5幢-603', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24853, 1004140, 240111044331054735, 239825274387125387, '5幢-604', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24854, 1004140, 240111044331054735, 239825274387125388, '5幢-605', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24855, 1004140, 240111044331054735, 239825274387125389, '5幢-606', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24856, 1004140, 240111044331054735, 239825274387125390, '5幢-701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24857, 1004140, 240111044331054735, 239825274387125391, '5幢-702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24858, 1004140, 240111044331054735, 239825274387125392, '5幢-703', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24859, 1004140, 240111044331054735, 239825274387125393, '5幢-704', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24860, 1004140, 240111044331054735, 239825274387125394, '5幢-705', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24861, 1004140, 240111044331054735, 239825274387125395, '5幢-706', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24862, 1004140, 240111044331054735, 239825274387125396, '5幢-801', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24863, 1004140, 240111044331054735, 239825274387125397, '5幢-802', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24864, 1004140, 240111044331054735, 239825274387125398, '5幢-803', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24865, 1004140, 240111044331054735, 239825274387125399, '5幢-804', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24866, 1004140, 240111044331054735, 239825274387125400, '5幢-805', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24867, 1004140, 240111044331054735, 239825274387125401, '5幢-806', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24868, 1004140, 240111044331054735, 239825274387125402, '5幢-901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24869, 1004140, 240111044331054735, 239825274387125403, '5幢-902', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24870, 1004140, 240111044331054735, 239825274387125404, '5幢-903', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24871, 1004140, 240111044331054735, 239825274387125405, '5幢-904', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24872, 1004140, 240111044331054735, 239825274387125406, '5幢-905', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24873, 1004140, 240111044331054735, 239825274387125407, '5幢-906', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24874, 1004140, 240111044331054735, 239825274387125408, '6幢-101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24875, 1004140, 240111044331054735, 239825274387125409, '6幢-102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24876, 1004140, 240111044331054735, 239825274387125410, '6幢-103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24877, 1004140, 240111044331054735, 239825274387125411, '6幢-104', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24878, 1004140, 240111044331054735, 239825274387125412, '6幢-201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24879, 1004140, 240111044331054735, 239825274387125413, '6幢-202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24880, 1004140, 240111044331054735, 239825274387125414, '6幢-203', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24881, 1004140, 240111044331054735, 239825274387125415, '6幢-204', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24882, 1004140, 240111044331054735, 239825274387125416, '6幢-205', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24883, 1004140, 240111044331054735, 239825274387125417, '6幢-206', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24884, 1004140, 240111044331054735, 239825274387125418, '6幢-301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24885, 1004140, 240111044331054735, 239825274387125419, '6幢-302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24886, 1004140, 240111044331054735, 239825274387125420, '6幢-303', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24887, 1004140, 240111044331054735, 239825274387125421, '6幢-304', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24888, 1004140, 240111044331054735, 239825274387125422, '6幢-305', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24889, 1004140, 240111044331054735, 239825274387125423, '6幢-306', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24890, 1004140, 240111044331054735, 239825274387125424, '6幢-401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24891, 1004140, 240111044331054735, 239825274387125425, '6幢-402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24892, 1004140, 240111044331054735, 239825274387125426, '6幢-403', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24893, 1004140, 240111044331054735, 239825274387125427, '6幢-404', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24894, 1004140, 240111044331054735, 239825274387125428, '6幢-405', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24895, 1004140, 240111044331054735, 239825274387125429, '6幢-406', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24896, 1004140, 240111044331054735, 239825274387125430, '6幢-501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24897, 1004140, 240111044331054735, 239825274387125431, '6幢-502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24898, 1004140, 240111044331054735, 239825274387125432, '6幢-503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24899, 1004140, 240111044331054735, 239825274387125433, '6幢-504', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24900, 1004140, 240111044331054735, 239825274387125434, '6幢-505', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24901, 1004140, 240111044331054735, 239825274387125435, '6幢-506', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24902, 1004140, 240111044331054735, 239825274387125436, '6幢-601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24903, 1004140, 240111044331054735, 239825274387125437, '6幢-602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24904, 1004140, 240111044331054735, 239825274387125438, '6幢-603', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24905, 1004140, 240111044331054735, 239825274387125439, '6幢-604', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24906, 1004140, 240111044331054735, 239825274387125440, '6幢-605', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24907, 1004140, 240111044331054735, 239825274387125441, '6幢-606', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24908, 1004140, 240111044331054735, 239825274387125442, '6幢-701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24909, 1004140, 240111044331054735, 239825274387125443, '6幢-702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24910, 1004140, 240111044331054735, 239825274387125444, '6幢-703', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24911, 1004140, 240111044331054735, 239825274387125445, '6幢-704', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24912, 1004140, 240111044331054735, 239825274387125446, '6幢-705', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24913, 1004140, 240111044331054735, 239825274387125447, '6幢-706', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24914, 1004140, 240111044331054735, 239825274387125448, '6幢-801', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24915, 1004140, 240111044331054735, 239825274387125449, '6幢-802', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24916, 1004140, 240111044331054735, 239825274387125450, '6幢-803', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24917, 1004140, 240111044331054735, 239825274387125451, '6幢-804', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24918, 1004140, 240111044331054735, 239825274387125452, '6幢-805', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24919, 1004140, 240111044331054735, 239825274387125453, '6幢-806', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24920, 1004140, 240111044331054735, 239825274387125454, '6幢-901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24921, 1004140, 240111044331054735, 239825274387125455, '6幢-902', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24922, 1004140, 240111044331054735, 239825274387125456, '6幢-903', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24923, 1004140, 240111044331054735, 239825274387125457, '6幢-904', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24924, 1004140, 240111044331054735, 239825274387125458, '6幢-905', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24925, 1004140, 240111044331054735, 239825274387125459, '6幢-906', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24926, 1004140, 240111044331054735, 239825274387125460, '6幢-1001', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24927, 1004140, 240111044331054735, 239825274387125461, '6幢-1002', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24928, 1004140, 240111044331054735, 239825274387125462, '6幢-1003', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24929, 1004140, 240111044331054735, 239825274387125463, '6幢-1004', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24930, 1004140, 240111044331054735, 239825274387125464, '6幢-1005', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24931, 1004140, 240111044331054735, 239825274387125465, '6幢-1006', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24932, 1004140, 240111044331054735, 239825274387125466, '7幢-101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24933, 1004140, 240111044331054735, 239825274387125467, '7幢-102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24934, 1004140, 240111044331054735, 239825274387125468, '7幢-103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24935, 1004140, 240111044331054735, 239825274387125469, '7幢-105', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24936, 1004140, 240111044331054735, 239825274387125470, '7幢-201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24937, 1004140, 240111044331054735, 239825274387125471, '7幢-202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24938, 1004140, 240111044331054735, 239825274387125472, '7幢-203', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24939, 1004140, 240111044331054735, 239825274387125473, '7幢-204', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24940, 1004140, 240111044331054735, 239825274387125474, '7幢-205', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24941, 1004140, 240111044331054735, 239825274387125475, '7幢-301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24942, 1004140, 240111044331054735, 239825274387125476, '7幢-302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24943, 1004140, 240111044331054735, 239825274387125477, '7幢-303', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24944, 1004140, 240111044331054735, 239825274387125478, '7幢-304', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24945, 1004140, 240111044331054735, 239825274387125479, '7幢-305', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24946, 1004140, 240111044331054735, 239825274387125480, '7幢-306', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24947, 1004140, 240111044331054735, 239825274387125481, '7幢-401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24948, 1004140, 240111044331054735, 239825274387125482, '7幢-402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24949, 1004140, 240111044331054735, 239825274387125483, '7幢-403', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24950, 1004140, 240111044331054735, 239825274387125484, '7幢-404', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24951, 1004140, 240111044331054735, 239825274387125485, '7幢-405', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24952, 1004140, 240111044331054735, 239825274387125486, '7幢-406', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24953, 1004140, 240111044331054735, 239825274387125487, '7幢-501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24954, 1004140, 240111044331054735, 239825274387125488, '7幢-502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24955, 1004140, 240111044331054735, 239825274387125489, '7幢-503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24956, 1004140, 240111044331054735, 239825274387125490, '7幢-504', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24957, 1004140, 240111044331054735, 239825274387125491, '7幢-505', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24958, 1004140, 240111044331054735, 239825274387125492, '7幢-506', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24959, 1004140, 240111044331054735, 239825274387125493, '7幢-601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24960, 1004140, 240111044331054735, 239825274387125494, '7幢-602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24961, 1004140, 240111044331054735, 239825274387125495, '7幢-603', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24962, 1004140, 240111044331054735, 239825274387125496, '7幢-604', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24963, 1004140, 240111044331054735, 239825274387125497, '7幢-605', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24964, 1004140, 240111044331054735, 239825274387125498, '7幢-606', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24965, 1004140, 240111044331054735, 239825274387125499, '7幢-701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24966, 1004140, 240111044331054735, 239825274387125500, '7幢-702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24967, 1004140, 240111044331054735, 239825274387125501, '7幢-703', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24968, 1004140, 240111044331054735, 239825274387125502, '7幢-704', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24969, 1004140, 240111044331054735, 239825274387125503, '7幢-705', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24970, 1004140, 240111044331054735, 239825274387125504, '7幢-706', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24971, 1004140, 240111044331054735, 239825274387125505, '7幢-801', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24972, 1004140, 240111044331054735, 239825274387125506, '7幢-802', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24973, 1004140, 240111044331054735, 239825274387125507, '7幢-803', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24974, 1004140, 240111044331054735, 239825274387125508, '7幢-804', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24975, 1004140, 240111044331054735, 239825274387125509, '7幢-805', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24976, 1004140, 240111044331054735, 239825274387125510, '7幢-806', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24977, 1004140, 240111044331054735, 239825274387125511, '7幢-901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24978, 1004140, 240111044331054735, 239825274387125512, '7幢-902', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24979, 1004140, 240111044331054735, 239825274387125513, '7幢-903', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24980, 1004140, 240111044331054735, 239825274387125514, '7幢-904', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24981, 1004140, 240111044331054735, 239825274387125515, '7幢-905', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24982, 1004140, 240111044331054735, 239825274387125516, '7幢-906', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24983, 1004140, 240111044331054735, 239825274387125517, '7幢-1001', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24984, 1004140, 240111044331054735, 239825274387125518, '7幢-1002', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24985, 1004140, 240111044331054735, 239825274387125519, '7幢-1003', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24986, 1004140, 240111044331054735, 239825274387125520, '7幢-1004', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24987, 1004140, 240111044331054735, 239825274387125521, '7幢-1005', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24988, 1004140, 240111044331054735, 239825274387125522, '7幢-1006', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24989, 1004140, 240111044331054735, 239825274387125523, '8幢-101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24990, 1004140, 240111044331054735, 239825274387125524, '8幢-102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24991, 1004140, 240111044331054735, 239825274387125525, '8幢-103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24992, 1004140, 240111044331054735, 239825274387125526, '8幢-104', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24993, 1004140, 240111044331054735, 239825274387125527, '8幢-105', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24994, 1004140, 240111044331054735, 239825274387125528, '8幢-106', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24995, 1004140, 240111044331054735, 239825274387125529, '8幢-107', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24996, 1004140, 240111044331054735, 239825274387125530, '8幢-108', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24997, 1004140, 240111044331054735, 239825274387125531, '8幢-109', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24998, 1004140, 240111044331054735, 239825274387125532, '8幢-1010', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (24999, 1004140, 240111044331054735, 239825274387125533, '8幢-1011', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25000, 1004140, 240111044331054735, 239825274387125534, '8幢-1012', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25001, 1004140, 240111044331054735, 239825274387125535, '8幢-201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25002, 1004140, 240111044331054735, 239825274387125536, '8幢-202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25003, 1004140, 240111044331054735, 239825274387125537, '8幢-203', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25004, 1004140, 240111044331054735, 239825274387125538, '8幢-207', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25005, 1004140, 240111044331054735, 239825274387125539, '8幢-208', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25006, 1004140, 240111044331054735, 239825274387125540, '8幢-209', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25007, 1004140, 240111044331054735, 239825274387125541, '8幢-2012', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25008, 1004140, 240111044331054735, 239825274387125542, '8幢-2013', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25009, 1004140, 240111044331054735, 239825274387125543, '8幢-2014', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25010, 1004140, 240111044331054735, 239825274387125544, '8幢-301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25011, 1004140, 240111044331054735, 239825274387125545, '8幢-302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25012, 1004140, 240111044331054735, 239825274387125546, '8幢-303', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25013, 1004140, 240111044331054735, 239825274387125547, '8幢-304', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25014, 1004140, 240111044331054735, 239825274387125548, '8幢-305', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25015, 1004140, 240111044331054735, 239825274387125549, '8幢-306', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25016, 1004140, 240111044331054735, 239825274387125550, '8幢-307', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25017, 1004140, 240111044331054735, 239825274387125551, '8幢-308', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25018, 1004140, 240111044331054735, 239825274387125552, '8幢-309', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25019, 1004140, 240111044331054735, 239825274387125553, '8幢-3010', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25020, 1004140, 240111044331054735, 239825274387125554, '8幢-3011', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25021, 1004140, 240111044331054735, 239825274387125555, '8幢-3012', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25022, 1004140, 240111044331054735, 239825274387125556, '8幢-3013', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25023, 1004140, 240111044331054735, 239825274387125557, '8幢-401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25024, 1004140, 240111044331054735, 239825274387125558, '8幢-402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25025, 1004140, 240111044331054735, 239825274387125559, '8幢-403', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25026, 1004140, 240111044331054735, 239825274387125560, '8幢-404', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25027, 1004140, 240111044331054735, 239825274387125561, '8幢-405', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25028, 1004140, 240111044331054735, 239825274387125562, '8幢-406', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25029, 1004140, 240111044331054735, 239825274387125563, '8幢-407', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25030, 1004140, 240111044331054735, 239825274387125564, '8幢-408', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25031, 1004140, 240111044331054735, 239825274387125565, '8幢-501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25032, 1004140, 240111044331054735, 239825274387125566, '8幢-502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25033, 1004140, 240111044331054735, 239825274387125567, '8幢-503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25034, 1004140, 240111044331054735, 239825274387125568, '8幢-504', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25035, 1004140, 240111044331054735, 239825274387125569, '8幢-505', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25036, 1004140, 240111044331054735, 239825274387125570, '8幢-506', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25037, 1004140, 240111044331054735, 239825274387125571, '8幢-507', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25038, 1004140, 240111044331054735, 239825274387125572, '8幢-507', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25039, 1004140, 240111044331054735, 239825274387125573, '8幢-601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25040, 1004140, 240111044331054735, 239825274387125574, '8幢-602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25041, 1004140, 240111044331054735, 239825274387125575, '8幢-603', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25042, 1004140, 240111044331054735, 239825274387125576, '8幢-604', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25043, 1004140, 240111044331054735, 239825274387125577, '8幢-605', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25044, 1004140, 240111044331054735, 239825274387125578, '8幢-606', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25045, 1004140, 240111044331054735, 239825274387125579, '8幢-607', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25046, 1004140, 240111044331054735, 239825274387125580, '8幢-608', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25047, 1004140, 240111044331054735, 239825274387125581, '8幢-701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25048, 1004140, 240111044331054735, 239825274387125582, '8幢-702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25049, 1004140, 240111044331054735, 239825274387125583, '8幢-703', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25050, 1004140, 240111044331054735, 239825274387125584, '8幢-704', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25051, 1004140, 240111044331054735, 239825274387125585, '8幢-705', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25052, 1004140, 240111044331054735, 239825274387125586, '8幢-706', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25053, 1004140, 240111044331054735, 239825274387125587, '8幢-707', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25054, 1004140, 240111044331054735, 239825274387125588, '8幢-708', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25055, 1004140, 240111044331054735, 239825274387125589, '8幢-801', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25056, 1004140, 240111044331054735, 239825274387125590, '8幢-802', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25057, 1004140, 240111044331054735, 239825274387125591, '8幢-803', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25058, 1004140, 240111044331054735, 239825274387125592, '8幢-804', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25059, 1004140, 240111044331054735, 239825274387125593, '8幢-805', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25060, 1004140, 240111044331054735, 239825274387125594, '8幢-806', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25061, 1004140, 240111044331054735, 239825274387125595, '8幢-807', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25062, 1004140, 240111044331054735, 239825274387125596, '8幢-808', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25063, 1004140, 240111044331054735, 239825274387125597, '8幢-901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25064, 1004140, 240111044331054735, 239825274387125598, '8幢-902', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25065, 1004140, 240111044331054735, 239825274387125599, '8幢-903', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25066, 1004140, 240111044331054735, 239825274387125600, '8幢-904', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25067, 1004140, 240111044331054735, 239825274387125601, '8幢-905', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25068, 1004140, 240111044331054735, 239825274387125602, '8幢-906', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25069, 1004140, 240111044331054735, 239825274387125603, '8幢-907', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25070, 1004140, 240111044331054735, 239825274387125604, '8幢-908', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25071, 1004140, 240111044331054735, 239825274387125605, '8幢-1001', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25072, 1004140, 240111044331054735, 239825274387125606, '8幢-1002', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25073, 1004140, 240111044331054735, 239825274387125607, '8幢-1003', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25074, 1004140, 240111044331054735, 239825274387125608, '8幢-1004', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25075, 1004140, 240111044331054735, 239825274387125609, '8幢-1005', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25076, 1004140, 240111044331054735, 239825274387125610, '8幢-1006', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25077, 1004140, 240111044331054735, 239825274387125611, '8幢-1007', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25078, 1004140, 240111044331054735, 239825274387125612, '8幢-1008', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25079, 1004140, 240111044331054735, 239825274387125613, '8幢-1101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25080, 1004140, 240111044331054735, 239825274387125614, '8幢-1102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25081, 1004140, 240111044331054735, 239825274387125615, '8幢-1103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25082, 1004140, 240111044331054735, 239825274387125616, '8幢-1104', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25083, 1004140, 240111044331054735, 239825274387125617, '8幢-1105', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25084, 1004140, 240111044331054735, 239825274387125618, '8幢-1106', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25085, 1004140, 240111044331054735, 239825274387125619, '8幢-1107', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25086, 1004140, 240111044331054735, 239825274387125620, '8幢-1108', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25087, 1004140, 240111044331054735, 239825274387125621, '8幢-1201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25088, 1004140, 240111044331054735, 239825274387125622, '8幢-1202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25089, 1004140, 240111044331054735, 239825274387125623, '8幢-1203', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25090, 1004140, 240111044331054735, 239825274387125624, '8幢-1204', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25091, 1004140, 240111044331054735, 239825274387125625, '8幢-1205', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25092, 1004140, 240111044331054735, 239825274387125626, '8幢-1206', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25093, 1004140, 240111044331054735, 239825274387125627, '8幢-1207', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25094, 1004140, 240111044331054735, 239825274387125628, '8幢-1208', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25095, 1004140, 240111044331054735, 239825274387125629, '8幢-1301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25096, 1004140, 240111044331054735, 239825274387125630, '8幢-1302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25097, 1004140, 240111044331054735, 239825274387125631, '8幢-1303', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25098, 1004140, 240111044331054735, 239825274387125632, '8幢-1304', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25099, 1004140, 240111044331054735, 239825274387125633, '8幢-1305', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25100, 1004140, 240111044331054735, 239825274387125634, '8幢-1306', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25101, 1004140, 240111044331054735, 239825274387125635, '8幢-1401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25102, 1004140, 240111044331054735, 239825274387125636, '8幢-1402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25103, 1004140, 240111044331054735, 239825274387125637, '8幢-1403', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25104, 1004140, 240111044331054735, 239825274387125638, '8幢-1404', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25105, 1004140, 240111044331054735, 239825274387125639, '8幢-1405', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25106, 1004140, 240111044331054735, 239825274387125640, '8幢-1406', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25107, 1004140, 240111044331054735, 239825274387125641, '9幢-101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25108, 1004140, 240111044331054735, 239825274387125642, '9幢-102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25109, 1004140, 240111044331054735, 239825274387125643, '9幢-103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25110, 1004140, 240111044331054735, 239825274387125644, '9幢-104', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25111, 1004140, 240111044331054735, 239825274387125645, '9幢-201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25112, 1004140, 240111044331054735, 239825274387125646, '9幢-202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25113, 1004140, 240111044331054735, 239825274387125647, '9幢-203', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25114, 1004140, 240111044331054735, 239825274387125648, '9幢-204', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25115, 1004140, 240111044331054735, 239825274387125649, '9幢-205', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25116, 1004140, 240111044331054735, 239825274387125650, '9幢-301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25117, 1004140, 240111044331054735, 239825274387125651, '9幢-302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25118, 1004140, 240111044331054735, 239825274387125652, '9幢-303', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25119, 1004140, 240111044331054735, 239825274387125653, '9幢-304', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25120, 1004140, 240111044331054735, 239825274387125654, '9幢-305', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25121, 1004140, 240111044331054735, 239825274387125655, '9幢-306', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25122, 1004140, 240111044331054735, 239825274387125656, '9幢-401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25123, 1004140, 240111044331054735, 239825274387125657, '9幢-402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25124, 1004140, 240111044331054735, 239825274387125658, '9幢-403', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25125, 1004140, 240111044331054735, 239825274387125659, '9幢-404', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25126, 1004140, 240111044331054735, 239825274387125660, '9幢-405', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25127, 1004140, 240111044331054735, 239825274387125661, '9幢-406', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25128, 1004140, 240111044331054735, 239825274387125662, '9幢-501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25129, 1004140, 240111044331054735, 239825274387125663, '9幢-502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25130, 1004140, 240111044331054735, 239825274387125664, '9幢-503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25131, 1004140, 240111044331054735, 239825274387125665, '9幢-504', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25132, 1004140, 240111044331054735, 239825274387125666, '9幢-505', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25133, 1004140, 240111044331054735, 239825274387125667, '9幢-506', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25134, 1004140, 240111044331054735, 239825274387125668, '9幢-601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25135, 1004140, 240111044331054735, 239825274387125669, '9幢-602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25136, 1004140, 240111044331054735, 239825274387125670, '9幢-603', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25137, 1004140, 240111044331054735, 239825274387125671, '9幢-604', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25138, 1004140, 240111044331054735, 239825274387125672, '9幢-605', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25139, 1004140, 240111044331054735, 239825274387125673, '9幢-606', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25140, 1004140, 240111044331054735, 239825274387125674, '9幢-701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25141, 1004140, 240111044331054735, 239825274387125675, '9幢-702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25142, 1004140, 240111044331054735, 239825274387125676, '9幢-703', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25143, 1004140, 240111044331054735, 239825274387125677, '9幢-704', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25144, 1004140, 240111044331054735, 239825274387125678, '9幢-705', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25145, 1004140, 240111044331054735, 239825274387125679, '9幢-706', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25146, 1004140, 240111044331054735, 239825274387125680, '9幢-801', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25147, 1004140, 240111044331054735, 239825274387125681, '9幢-802', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25148, 1004140, 240111044331054735, 239825274387125682, '9幢-803', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25149, 1004140, 240111044331054735, 239825274387125683, '9幢-804', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25150, 1004140, 240111044331054735, 239825274387125684, '9幢-805', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (25151, 1004140, 240111044331054735, 239825274387125685, '9幢-806', '0');

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005939, UUID(), '南通崇川区布艺乐乎家纺设计工作室', '南通崇川区布艺乐乎家纺设计工作室', 1, 1, 1006053, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184391, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184391, UUID(), 999986, 2, 'EhGroups', 1005939,'南通崇川区布艺乐乎家纺设计工作室','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006053, 0, 'ENTERPRISE', '南通崇川区布艺乐乎家纺设计工作室', 0, '', '/1006053', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114899, 240111044331054735, 'organization', 1006053, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005940, UUID(), '南通泽阳智能科技有限公司', '南通泽阳智能科技有限公司', 1, 1, 1006054, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184392, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184392, UUID(), 999986, 2, 'EhGroups', 1005940,'南通泽阳智能科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006054, 0, 'ENTERPRISE', '南通泽阳智能科技有限公司', 0, '', '/1006054', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114900, 240111044331054735, 'organization', 1006054, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005941, UUID(), '南通华建智能科技有限公司', '南通华建智能科技有限公司', 1, 1, 1006055, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184393, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184393, UUID(), 999986, 2, 'EhGroups', 1005941,'南通华建智能科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006055, 0, 'ENTERPRISE', '南通华建智能科技有限公司', 0, '', '/1006055', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114901, 240111044331054735, 'organization', 1006055, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005942, UUID(), '南通新华传媒有限公司', '南通新华传媒有限公司', 1, 1, 1006056, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184394, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184394, UUID(), 999986, 2, 'EhGroups', 1005942,'南通新华传媒有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006056, 0, 'ENTERPRISE', '南通新华传媒有限公司', 0, '', '/1006056', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114902, 240111044331054735, 'organization', 1006056, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005943, UUID(), '江苏益美智能科技有限公司', '江苏益美智能科技有限公司', 1, 1, 1006057, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184395, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184395, UUID(), 999986, 2, 'EhGroups', 1005943,'江苏益美智能科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006057, 0, 'ENTERPRISE', '江苏益美智能科技有限公司', 0, '', '/1006057', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114903, 240111044331054735, 'organization', 1006057, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005944, UUID(), '江苏全通工程技术有限公司', '江苏全通工程技术有限公司', 1, 1, 1006058, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184396, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184396, UUID(), 999986, 2, 'EhGroups', 1005944,'江苏全通工程技术有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006058, 0, 'ENTERPRISE', '江苏全通工程技术有限公司', 0, '', '/1006058', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114904, 240111044331054735, 'organization', 1006058, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005945, UUID(), '上海泛微网络科技股份有限公司', '上海泛微网络科技股份有限公司', 1, 1, 1006059, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184397, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184397, UUID(), 999986, 2, 'EhGroups', 1005945,'上海泛微网络科技股份有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006059, 0, 'ENTERPRISE', '上海泛微网络科技股份有限公司', 0, '', '/1006059', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114905, 240111044331054735, 'organization', 1006059, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005946, UUID(), '南通车安电子科技有限公司', '南通车安电子科技有限公司', 1, 1, 1006060, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184398, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184398, UUID(), 999986, 2, 'EhGroups', 1005946,'南通车安电子科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006060, 0, 'ENTERPRISE', '南通车安电子科技有限公司', 0, '', '/1006060', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114906, 240111044331054735, 'organization', 1006060, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005947, UUID(), '中国民族建筑研究会建筑装饰研究院', '中国民族建筑研究会建筑装饰研究院', 1, 1, 1006061, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184399, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184399, UUID(), 999986, 2, 'EhGroups', 1005947,'中国民族建筑研究会建筑装饰研究院','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006061, 0, 'ENTERPRISE', '中国民族建筑研究会建筑装饰研究院', 0, '', '/1006061', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114907, 240111044331054735, 'organization', 1006061, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005948, UUID(), '南通智慧建筑产业研究院有限公司', '南通智慧建筑产业研究院有限公司', 1, 1, 1006062, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184400, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184400, UUID(), 999986, 2, 'EhGroups', 1005948,'南通智慧建筑产业研究院有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006062, 0, 'ENTERPRISE', '南通智慧建筑产业研究院有限公司', 0, '', '/1006062', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114908, 240111044331054735, 'organization', 1006062, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005949, UUID(), '江苏公证天业会计师事务所', '江苏公证天业会计师事务所', 1, 1, 1006063, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184401, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184401, UUID(), 999986, 2, 'EhGroups', 1005949,'江苏公证天业会计师事务所','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006063, 0, 'ENTERPRISE', '江苏公证天业会计师事务所', 0, '', '/1006063', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114909, 240111044331054735, 'organization', 1006063, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005950, UUID(), '南通天行健环境科技有限公司', '南通天行健环境科技有限公司', 1, 1, 1006064, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184402, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184402, UUID(), 999986, 2, 'EhGroups', 1005950,'南通天行健环境科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006064, 0, 'ENTERPRISE', '南通天行健环境科技有限公司', 0, '', '/1006064', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114910, 240111044331054735, 'organization', 1006064, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005951, UUID(), '南通欣诚教育软件有限公司', '南通欣诚教育软件有限公司', 1, 1, 1006065, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184403, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184403, UUID(), 999986, 2, 'EhGroups', 1005951,'南通欣诚教育软件有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006065, 0, 'ENTERPRISE', '南通欣诚教育软件有限公司', 0, '', '/1006065', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114911, 240111044331054735, 'organization', 1006065, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005952, UUID(), '江苏金土地房产评估测绘咨询有限公司', '江苏金土地房产评估测绘咨询有限公司', 1, 1, 1006066, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184404, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184404, UUID(), 999986, 2, 'EhGroups', 1005952,'江苏金土地房产评估测绘咨询有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006066, 0, 'ENTERPRISE', '江苏金土地房产评估测绘咨询有限公司', 0, '', '/1006066', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114912, 240111044331054735, 'organization', 1006066, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005953, UUID(), '南通三和市政水利建设工程有限公公司', '南通三和市政水利建设工程有限公公司', 1, 1, 1006067, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184405, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184405, UUID(), 999986, 2, 'EhGroups', 1005953,'南通三和市政水利建设工程有限公公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006067, 0, 'ENTERPRISE', '南通三和市政水利建设工程有限公公司', 0, '', '/1006067', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114913, 240111044331054735, 'organization', 1006067, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005954, UUID(), '南通禾墅装饰工程有限公司', '南通禾墅装饰工程有限公司', 1, 1, 1006068, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184406, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184406, UUID(), 999986, 2, 'EhGroups', 1005954,'南通禾墅装饰工程有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006068, 0, 'ENTERPRISE', '南通禾墅装饰工程有限公司', 0, '', '/1006068', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114914, 240111044331054735, 'organization', 1006068, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005955, UUID(), '南通黑白灰家纺设计有限公司', '南通黑白灰家纺设计有限公司', 1, 1, 1006069, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184407, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184407, UUID(), 999986, 2, 'EhGroups', 1005955,'南通黑白灰家纺设计有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006069, 0, 'ENTERPRISE', '南通黑白灰家纺设计有限公司', 0, '', '/1006069', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114915, 240111044331054735, 'organization', 1006069, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005956, UUID(), '南通杰思策划创意有限公司', '南通杰思策划创意有限公司', 1, 1, 1006070, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184408, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184408, UUID(), 999986, 2, 'EhGroups', 1005956,'南通杰思策划创意有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006070, 0, 'ENTERPRISE', '南通杰思策划创意有限公司', 0, '', '/1006070', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114916, 240111044331054735, 'organization', 1006070, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005957, UUID(), '南通睿祺智能科技有限公司', '南通睿祺智能科技有限公司', 1, 1, 1006071, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184409, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184409, UUID(), 999986, 2, 'EhGroups', 1005957,'南通睿祺智能科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006071, 0, 'ENTERPRISE', '南通睿祺智能科技有限公司', 0, '', '/1006071', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114917, 240111044331054735, 'organization', 1006071, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005958, UUID(), '江苏得得空间信息科技有限公司', '江苏得得空间信息科技有限公司', 1, 1, 1006072, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184410, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184410, UUID(), 999986, 2, 'EhGroups', 1005958,'江苏得得空间信息科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006072, 0, 'ENTERPRISE', '江苏得得空间信息科技有限公司', 0, '', '/1006072', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114918, 240111044331054735, 'organization', 1006072, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005959, UUID(), '南通云景信息科技有限公司', '南通云景信息科技有限公司', 1, 1, 1006073, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184411, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184411, UUID(), 999986, 2, 'EhGroups', 1005959,'南通云景信息科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006073, 0, 'ENTERPRISE', '南通云景信息科技有限公司', 0, '', '/1006073', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114919, 240111044331054735, 'organization', 1006073, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005960, UUID(), '南通信息化建设发展有限公司', '南通信息化建设发展有限公司', 1, 1, 1006074, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184412, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184412, UUID(), 999986, 2, 'EhGroups', 1005960,'南通信息化建设发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006074, 0, 'ENTERPRISE', '南通信息化建设发展有限公司', 0, '', '/1006074', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114920, 240111044331054735, 'organization', 1006074, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005961, UUID(), '南通绿业中试技术研究院有限公司', '南通绿业中试技术研究院有限公司', 1, 1, 1006075, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184413, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184413, UUID(), 999986, 2, 'EhGroups', 1005961,'南通绿业中试技术研究院有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006075, 0, 'ENTERPRISE', '南通绿业中试技术研究院有限公司', 0, '', '/1006075', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114921, 240111044331054735, 'organization', 1006075, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005962, UUID(), '江苏正远信息技术有限公司', '江苏正远信息技术有限公司', 1, 1, 1006076, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184414, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184414, UUID(), 999986, 2, 'EhGroups', 1005962,'江苏正远信息技术有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006076, 0, 'ENTERPRISE', '江苏正远信息技术有限公司', 0, '', '/1006076', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114922, 240111044331054735, 'organization', 1006076, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005963, UUID(), '江苏宁创智能科技有限公司', '江苏宁创智能科技有限公司', 1, 1, 1006077, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184415, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184415, UUID(), 999986, 2, 'EhGroups', 1005963,'江苏宁创智能科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006077, 0, 'ENTERPRISE', '江苏宁创智能科技有限公司', 0, '', '/1006077', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114923, 240111044331054735, 'organization', 1006077, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005964, UUID(), '南通信息化建设发展有限公司', '南通信息化建设发展有限公司', 1, 1, 1006078, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184416, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184416, UUID(), 999986, 2, 'EhGroups', 1005964,'南通信息化建设发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006078, 0, 'ENTERPRISE', '南通信息化建设发展有限公司', 0, '', '/1006078', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114924, 240111044331054735, 'organization', 1006078, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005965, UUID(), '上海路图信息科技有限公司', '上海路图信息科技有限公司', 1, 1, 1006079, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184417, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184417, UUID(), 999986, 2, 'EhGroups', 1005965,'上海路图信息科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006079, 0, 'ENTERPRISE', '上海路图信息科技有限公司', 0, '', '/1006079', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114925, 240111044331054735, 'organization', 1006079, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005966, UUID(), '江苏创时信息科技有限公司', '江苏创时信息科技有限公司', 1, 1, 1006080, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184418, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184418, UUID(), 999986, 2, 'EhGroups', 1005966,'江苏创时信息科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006080, 0, 'ENTERPRISE', '江苏创时信息科技有限公司', 0, '', '/1006080', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114926, 240111044331054735, 'organization', 1006080, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005967, UUID(), '南通市上午家纺设计有限公司', '南通市上午家纺设计有限公司', 1, 1, 1006081, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184419, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184419, UUID(), 999986, 2, 'EhGroups', 1005967,'南通市上午家纺设计有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006081, 0, 'ENTERPRISE', '南通市上午家纺设计有限公司', 0, '', '/1006081', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114927, 240111044331054735, 'organization', 1006081, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005968, UUID(), '上海讯生信息科技有限公司', '上海讯生信息科技有限公司', 1, 1, 1006082, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184420, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184420, UUID(), 999986, 2, 'EhGroups', 1005968,'上海讯生信息科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006082, 0, 'ENTERPRISE', '上海讯生信息科技有限公司', 0, '', '/1006082', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114928, 240111044331054735, 'organization', 1006082, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1005969, UUID(), '南通江海大数据管理有限公司', '南通江海大数据管理有限公司', 1, 1, 1006083, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 184421, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(184421, UUID(), 999986, 2, 'EhGroups', 1005969,'南通江海大数据管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1006083, 0, 'ENTERPRISE', '南通江海大数据管理有限公司', 0, '', '/1006083', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1114929, 240111044331054735, 'organization', 1006083, 3, 0, UTC_TIMESTAMP());


INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES (11797, 999986, 0, '/home', 'Default', '0', '0', 'innospring', 'innospring', 'cs://1/image/aW1hZ2UvTVRveFkySmxZamRrWTJFeE0yVTFOV0V4WTJOaFlURTBaakV4TUdFek9UZ3dOZw', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'park_tourist');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES (11798, 999986, 0, '/home', 'Default', '0', '0', 'innospring', 'innospring', 'cs://1/image/aW1hZ2UvTVRveFkySmxZamRrWTJFeE0yVTFOV0V4WTJOaFlURTBaakV4TUdFek9UZ3dOZw', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'pm_admin');

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES('10062','资源预订','0',NULL,'0','999986');
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ('60', 'community', '240111044331054735', '0', '创源服务', '创源服务', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999986', '');
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ('61', 'community', '240111044331054735', '0', '增值服务', '增值服务', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999986', '');

SET @sa_id = (SELECT max(id) FROM `eh_service_alliances`);    
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`) 
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331054735', '创源服务', '创源服务', '60', '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`) 
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331054735', '增值服务', '增值服务', '61', '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

INSERT INTO `eh_news_categories` (`id`,  `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`) 
    VALUES('60','0','活动快讯',NULL,NULL,'0','1',UTC_TIMESTAMP(),'0',NULL,'999986', 'cs://1/image/aW1hZ2UvTVRvNU1qRmtOamxtT0dJMU5qRmpNVGhqT1RZM1lUSTBOelZrTVRFeE9UVXlPQQ');

-- 园区管理员场景
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (390, 999986, 'ServiceMarketLayout', '{"versionCode":"2016092901","versionName":"3.10.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"News","instanceConfig":{"timeWidgetStyle":"datetime","categoryId":60,"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21}]}', '2016092901', '0', '2', '2016-09-29 17:02:30', 'pm_admin');

INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (391, 999986, 'aclinkAndpunchLayout', '{"versionCode":"2016092901","versionName":"3.10.0","layoutName":"aclinkAndpunchLayout","displayName":"门禁考勤","groups":[{"groupName":"门禁考勤Banner","widget":"Navigator","instanceConfig":{"itemGroup":"aclinkAndpunchBanner"},"style":"Gallery","defaultOrder":1,"separatorFlag":0,"separatorHeight":0,"columnCount": 1,"editFlag":0},{"groupName":"门禁考勤","widget":"Navigator","instanceConfig":{"itemGroup":"aclinkAndpunchBiz"},"style":"Gallery","defaultOrder":5,"separatorFlag":0,"separatorHeight":0,"columnCount":2,"editFlag":0}]}', '2016092901', '0', '2', '2016-09-29 17:02:30', 'pm_admin');
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (392, 999986, 'serviceAllianceLayout', '{"versionCode":"2016092901","versionName":"3.10.0","layoutName":"serviceAllianceLayout","displayName":"创新服务","groups":[{"groupName":"创新服务Banner","widget":"Navigator","instanceConfig":{"itemGroup":"serviceAllianceBanner"},"style":"Gallery","defaultOrder":1,"separatorFlag":0,"separatorHeight":0,"columnCount": 1,"editFlag":0},{"groupName":"创新服务Biz","widget":"Navigator","instanceConfig":{"itemGroup":"serviceAllianceBiz"},"style":"Gallery","defaultOrder":5,"separatorFlag":0,"separatorHeight":0,"columnCount":2,"editFlag":0}]}', '2016092901', '0', '2', '2016-09-29 17:02:30', 'pm_admin');
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (393, 999986, 'communityLayout', '{"versionCode":"2016092901","versionName":"3.10.0","layoutName":"communityLayout","displayName":"园区配套","groups":[{"groupName":"园区配套Banner","widget":"Navigator","instanceConfig":{"itemGroup":"communityBanner"},"style":"Gallery","defaultOrder":1,"separatorFlag":0,"separatorHeight":0,"columnCount": 1,"editFlag":0},{"groupName":"园区配套Biz","widget":"Navigator","instanceConfig":{"itemGroup":"communityBiz"},"style":"Gallery","defaultOrder":5,"separatorFlag":0,"separatorHeight":0,"columnCount":2,"editFlag":0}]}', '2016092901', '0', '2', '2016-09-29 17:02:30', 'pm_admin');

	
	
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112301, 999986, '0', '0', '0', '/home', 'Bizs', 'OFFICIAL_ACTIVITY', '园区活动', 'cs://1/image/aW1hZ2UvTVRvMU9HUXpaR1UwT1RFNFlXVTVNemd6TkdFd09XTmhZemt4TXpjMVl6aGtPQQ', '1', '1', 50,'', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112302, 999986, '0', '0', '0', '/home', 'Bizs', '门禁考勤', '门禁考勤', 'cs://1/image/aW1hZ2UvTVRvNE9UWTVaREkzTm1KbE9XSmxNakZqT1dFeVpEUXhPR0kyTURVMlpUSTVPQQ', '1', '1', 2,'{"itemLocation":"/home/aclinkAndpunch","layoutName":"aclinkAndpunchLayout"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');

INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (112303, 999986, 0, 0, 0, '/home/aclinkAndpunch', 'aclinkAndpunchBanner', '', '', 'cs://1/image/aW1hZ2UvTVRwaU9XSTJObVF5WkdGaFkyTXhaakJsWlRNMU9HWXlPRGxrWkdZM01ESmhPUQ', 1, 1, 0, NULl, 0, 0, 1, 1, '', 0, NULL, NULL, NULL, '0', 'pm_admin');

INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (112304, 999986, 0, 0, 0, '/home/aclinkAndpunch', 'aclinkAndpunchBiz', 'DoorManagement', '智能门禁', 'cs://1/image/aW1hZ2UvTVRveVltRTFOamd4T0RRNE5UTmlZVGRrWW1NeVkyVmhaRGd5WW1NeE5qY3dZdw', 1, 1, 40, '{"isSupportQR":1,"isSupportSmart":1}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (112305, 999986, 0, 0, 0, '/home/aclinkAndpunch', 'aclinkAndpunchBiz', 'PUNCH', '打卡考勤', 'cs://1/image/aW1hZ2UvTVRvM05ETm1NbU13TVdWbFlqUTNNakV6Tm1Ka01EbGxZakkzT1dNMFlXWTBaQQ', '1', '1', '23', '', 0, 0, 1, 1, '', '0', NULL, NULL, NULL, '0', 'pm_admin');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112306, 999986, '0', '0', '0', '/home', 'Bizs', '创新服务', '创新服务', 'cs://1/image/aW1hZ2UvTVRvNFltTTBOelJpTXpNME9EWmlNekJsWmpSaU1qbGhOV05pT1RsbU9UWTJaQQ', '1', '1', 2,'{"itemLocation":"/home/serviceAlliance","layoutName":"serviceAllianceLayout"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');

	
INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (112307, 999986, 0, 0, 0, '/home/serviceAlliance', 'serviceAllianceBanner', '', '', 'cs://1/image/aW1hZ2UvTVRvMFl6STRNVEkyTnpKalltRmxNR1V4WlRFMk9XUTROemsyWm1ZellqZzFaUQ', 1, 1, 0, NULl, 0, 0, 1, 1, '', 0, NULL, NULL, NULL, '0', 'pm_admin');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
    VALUES (112308, 999986, 0, 0, 0, '/home/serviceAlliance', 'serviceAllianceBiz', '创源服务', '创源服务', 'cs://1/image/aW1hZ2UvTVRwa05HVTBNMlF5T1dZd1pURXpOV1ExTVdNellURTJPR0kyWXpnMU9XSmlOQQ', 1, 1, 33, '{"type":60,"parentId":60,"displayType": "grid"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'pm_admin', '0');    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (112309, 999986, 0, 0, 0, '/home/serviceAlliance', 'serviceAllianceBiz', 'PM_TASK', '物业服务', 'cs://1/image/aW1hZ2UvTVRvMU4yRXdNR1F5TVRaaU1XRTVZamt3WWpnMk1qYzRPVEU1T0RNNE1UVTVPQQ', 1, 1, 51, '', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'pm_admin');    

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
    VALUES (112310, 999986, 0, 0, 0, '/home', 'Bizs', '增值服务', '增值服务', 'cs://1/image/aW1hZ2UvTVRvNU9XTmhOMkl6T1RNNU1ESTJNbVJqTURjM05qTTBaRFUxT1dFM1pXWXlNQQ', 1, 1, 33, '{"type":61,"parentId":61,"displayType": "grid"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'pm_admin', '0');    


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112311, 999986, '0', '0', '0', '/home', 'Bizs', 'CONTACTS', '园区通讯录', 'cs://1/image/aW1hZ2UvTVRwbE16ZG1OVGN4TWpnMU56UmpaR1JqTlRCaVlUUTFZelpqWVRabU1XSXdNZw', '1', '1', '46', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112312, 999986, 0, 0, 0, '/home', 'Bizs', 'RENTAL', '公共资源预订', 'cs://1/image/aW1hZ2UvTVRwbU5qY3lOV0kzTURVNE5EYzNZell4TUdReE5USTRZbVJqWmpjMU1USXdOUQ', 1, 1, 49,'{\"resourceTypeId\":10062,\"pageType\":0}', 0, 0, 1, 1, '', '0', NULL, NULL, NULL, '1', 'pm_admin');        

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112313, 999986, '0', '0', '0', '/home', 'Bizs', 'VIDEO_MEETING', '视频会议', 'cs://1/image/aW1hZ2UvTVRwaE9UVTVZMk00TkdFMFpXUXhaamd3WkRWak5USXlOR0kzTTJZMk56RTFPUQ', '1', '1', '27', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112314, 999986, '0', '0', '0', '/home', 'Bizs', '更多服务', '更多服务', 'cs://1/image/aW1hZ2UvTVRveVptTXlZelU0TlRFeVlUUTRPV0ZoTldSbFlUaGtOVGhoWm1FNU5ERmlaZw', '1', '1', '1', '{\"itemLocation\":\"/home\", \"itemGroup\":\"Bizs\"}', '30', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112315, 999986, '0', '0', '0', '/home', 'Bizs', '园区配套', '园区配套', 'cs://1/image/aW1hZ2UvTVRvMk1UZzJZV0kxT1RZME9UTTJOamhpWWpGaE1qZzBNVGxsWkRVeU9HWTRZUQ', '1', '1', 2,'{"itemLocation":"/home/community","layoutName":"communityLayout"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin');        
INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (112344, 999986, 0, 0, 0, '/home/community', 'communityBanner', '', '', 'cs://1/image/aW1hZ2UvTVRvNFlqazJPREV5WXpjNU5UazJPRGd6WTJKak5tSmtOVGRsT1Rnd056WTVZZw', 1, 1, 0, NULl, 0, 0, 1, 1, '', 0, NULL, NULL, NULL, '0', 'pm_admin');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
    VALUES (112345, 999986, 0, 0, 0, '/home/community', 'communityBiz', '园区食堂', '园区食堂', 'cs://1/image/aW1hZ2UvTVRwa01XVXlPV1l5TmpWbVltWmlORFl6WmpRM01tUTNOR05oTjJZd04yWm1aUQ', 1, 1, 14, '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'pm_admin', '0');    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (112346, 999986, 0, 0, 0, '/home/community', 'communityBiz', '园区商业', '园区商业', 'cs://1/image/aW1hZ2UvTVRwbE1UZzRNbVV5WmpFNU1qVmxOV1kzTW1WalptWTVORFprWmpZNFlqQmpaZw', 1, 1, 14, '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'pm_admin');    

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112316, 999986, '0', '0', '0', '/home', 'Bizs', '工位预订', '工位预订', 'cs://1/image/aW1hZ2UvTVRvM1lXRXdNR1V6WXpVeE5XUmxOR1ZpTVdFeE56ZG1ZVFZqWmpRME1tSm1ZUQ', '1', '1', 14,'{"url":"http://core.zuolin.com/station-booking/index.html?hideNavigationBar=1#/station_booking#sign_suffix"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin');        

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112317, 999986, '0', '0', '0', '/home', 'Bizs', '智慧家居', '智慧家居', 'cs://1/image/aW1hZ2UvTVRwbU5qSmhNekpsWVRJeU5tRTBNR1V6WW1NNFpHWTROemt4WkRsaU5EaGtZdw', '1', '1', 14,'{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin');        
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112318, 999986, '0', '0', '0', '/home', 'Bizs', '智能停车场', '智能停车场', 'cs://1/image/aW1hZ2UvTVRwaU5ESTBZMk01TmpCak1XTTJZVEpsWXprek1EZzBZbVV6T0RNMU1XRTNNZw', '1', '1', 14,'{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin');        
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112319, 999986, '0', '0', '0', '/home', 'Bizs', '园区一卡通', '园区一卡通', 'cs://1/image/aW1hZ2UvTVRvM1pUVXpZalZtWkRGall6YzFPRFpsTURVNFkyVmpZMlkxWXpsbE56VXlaUQ', '1', '1', 14,'{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin');        
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112320, 999986, '0', '0', '0', '/home', 'Bizs', '智能楼控', '智能楼控', 'cs://1/image/aW1hZ2UvTVRvM1kyVTFZamswWldWbE9URXhaRFJsTVRNeE4yRTVZVEU0TlRJek5qUTJNQQ', '1', '1', 14,'{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin');        

	

-- 园区游客场景    
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (394, 999986, 'ServiceMarketLayout', '{"versionCode":"2016092901","versionName":"3.10.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"News","instanceConfig":{"timeWidgetStyle":"datetime","categoryId":60,"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21}]}', '2016092901', '0', '2', '2016-09-29 17:02:30', 'park_tourist');

INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (395, 999986, 'aclinkAndpunchLayout', '{"versionCode":"2016092901","versionName":"3.10.0","layoutName":"aclinkAndpunchLayout","displayName":"门禁考勤","groups":[{"groupName":"门禁考勤Banner","widget":"Navigator","instanceConfig":{"itemGroup":"aclinkAndpunchBanner"},"style":"Gallery","defaultOrder":1,"separatorFlag":0,"separatorHeight":0,"columnCount": 1,"editFlag":0},{"groupName":"门禁考勤","widget":"Navigator","instanceConfig":{"itemGroup":"aclinkAndpunchBiz"},"style":"Gallery","defaultOrder":5,"separatorFlag":0,"separatorHeight":0,"columnCount":2,"editFlag":0}]}', '2016092901', '0', '2', '2016-09-29 17:02:30', 'park_tourist');
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (396, 999986, 'serviceAllianceLayout', '{"versionCode":"2016092901","versionName":"3.10.0","layoutName":"serviceAllianceLayout","displayName":"创新服务","groups":[{"groupName":"创新服务Banner","widget":"Navigator","instanceConfig":{"itemGroup":"serviceAllianceBanner"},"style":"Gallery","defaultOrder":1,"separatorFlag":0,"separatorHeight":0,"columnCount": 1,"editFlag":0},{"groupName":"创新服务Biz","widget":"Navigator","instanceConfig":{"itemGroup":"serviceAllianceBiz"},"style":"Gallery","defaultOrder":5,"separatorFlag":0,"separatorHeight":0,"columnCount":2,"editFlag":0}]}', '2016092901', '0', '2', '2016-09-29 17:02:30', 'park_tourist');
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (397, 999986, 'communityLayout', '{"versionCode":"2016092901","versionName":"3.10.0","layoutName":"communityLayout","displayName":"园区配套","groups":[{"groupName":"园区配套Banner","widget":"Navigator","instanceConfig":{"itemGroup":"communityBanner"},"style":"Gallery","defaultOrder":1,"separatorFlag":0,"separatorHeight":0,"columnCount": 1,"editFlag":0},{"groupName":"园区配套Biz","widget":"Navigator","instanceConfig":{"itemGroup":"communityBiz"},"style":"Gallery","defaultOrder":5,"separatorFlag":0,"separatorHeight":0,"columnCount":2,"editFlag":0}]}', '2016092901', '0', '2', '2016-09-29 17:02:30', 'park_tourist');

	
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112321, 999986, '0', '0', '0', '/home', 'Bizs', 'OFFICIAL_ACTIVITY', '园区活动', 'cs://1/image/aW1hZ2UvTVRvMU9HUXpaR1UwT1RFNFlXVTVNemd6TkdFd09XTmhZemt4TXpjMVl6aGtPQQ', '1', '1', 50,'', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112322, 999986, '0', '0', '0', '/home', 'Bizs', '门禁考勤', '门禁考勤', 'cs://1/image/aW1hZ2UvTVRvNE9UWTVaREkzTm1KbE9XSmxNakZqT1dFeVpEUXhPR0kyTURVMlpUSTVPQQ', '1', '1', 2,'{"itemLocation":"/home/aclinkAndpunch","layoutName":"aclinkAndpunchLayout"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');

INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (112323, 999986, 0, 0, 0, '/home/aclinkAndpunch', 'aclinkAndpunchBanner', '', '', 'cs://1/image/aW1hZ2UvTVRwaU9XSTJObVF5WkdGaFkyTXhaakJsWlRNMU9HWXlPRGxrWkdZM01ESmhPUQ', 1, 1, 0, NULl, 0, 0, 1, 1, '', 0, NULL, NULL, NULL, '0', 'park_tourist');

INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (112324, 999986, 0, 0, 0, '/home/aclinkAndpunch', 'aclinkAndpunchBiz', 'DoorManagement', '智能门禁', 'cs://1/image/aW1hZ2UvTVRveVltRTFOamd4T0RRNE5UTmlZVGRrWW1NeVkyVmhaRGd5WW1NeE5qY3dZdw', 1, 1, 40, '{"isSupportQR":1,"isSupportSmart":1}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (112325, 999986, 0, 0, 0, '/home/aclinkAndpunch', 'aclinkAndpunchBiz', 'PUNCH', '打卡考勤', 'cs://1/image/aW1hZ2UvTVRvM05ETm1NbU13TVdWbFlqUTNNakV6Tm1Ka01EbGxZakkzT1dNMFlXWTBaQQ', '1', '1', '23', '', 0, 0, 1, 1, '', '0', NULL, NULL, NULL, '0', 'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112326, 999986, '0', '0', '0', '/home', 'Bizs', '创新服务', '创新服务', 'cs://1/image/aW1hZ2UvTVRvNFltTTBOelJpTXpNME9EWmlNekJsWmpSaU1qbGhOV05pT1RsbU9UWTJaQQ', '1', '1', 2,'{"itemLocation":"/home/serviceAlliance","layoutName":"serviceAllianceLayout"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (112340, 999986, 0, 0, 0, '/home/serviceAlliance', 'serviceAllianceBanner', '', '', 'cs://1/image/aW1hZ2UvTVRvMFl6STRNVEkyTnpKalltRmxNR1V4WlRFMk9XUTROemsyWm1ZellqZzFaUQ', 1, 1, 0, NULl, 0, 0, 1, 1, '', 0, NULL, NULL, NULL, '0', 'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
    VALUES (112327, 999986, 0, 0, 0, '/home/serviceAlliance', 'serviceAllianceBiz', '创源服务', '创源服务', 'cs://1/image/aW1hZ2UvTVRwa05HVTBNMlF5T1dZd1pURXpOV1ExTVdNellURTJPR0kyWXpnMU9XSmlOQQ', 1, 1, 33, '{"type":60,"parentId":60,"displayType": "grid"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'park_tourist', '0');    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
    VALUES (112328, 999986, 0, 0, 0, '/home', 'Bizs', '增值服务', '增值服务', 'cs://1/image/aW1hZ2UvTVRvNU9XTmhOMkl6T1RNNU1ESTJNbVJqTURjM05qTTBaRFUxT1dFM1pXWXlNQQ', 1, 1, 33, '{"type":61,"parentId":61,"displayType": "grid"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'park_tourist', '0');    
	
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112329, 999986, 0, 0, 0, '/home/serviceAlliance', 'serviceAllianceBiz', 'PM_TASK', '物业服务', 'cs://1/image/aW1hZ2UvTVRvMU4yRXdNR1F5TVRaaU1XRTVZamt3WWpnMk1qYzRPVEU1T0RNNE1UVTVPQQ', 1, 1, 14, '{"url":"http://core.zuolin.com/property_service/index.html?hideNavigationBar=1#/my_service#sign_suffix"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'park_tourist');    


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112330, 999986, '0', '0', '0', '/home', 'Bizs', 'CONTACTS', '园区通讯录', 'cs://1/image/aW1hZ2UvTVRwbE16ZG1OVGN4TWpnMU56UmpaR1JqTlRCaVlUUTFZelpqWVRabU1XSXdNZw', '1', '1', '46', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112331, 999986, 0, 0, 0, '/home', 'Bizs', 'RENTAL', '公共资源预订', 'cs://1/image/aW1hZ2UvTVRwbU5qY3lOV0kzTURVNE5EYzNZell4TUdReE5USTRZbVJqWmpjMU1USXdOUQ', 1, 1, 49,'{\"resourceTypeId\":10062,\"pageType\":0}', 0, 0, 1, 1, '', '0', NULL, NULL, NULL, '1', 'park_tourist');        

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112332, 999986, '0', '0', '0', '/home', 'Bizs', 'VIDEO_MEETING', '视频会议', 'cs://1/image/aW1hZ2UvTVRwaE9UVTVZMk00TkdFMFpXUXhaamd3WkRWak5USXlOR0kzTTJZMk56RTFPUQ', '1', '1', '27', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112333, 999986, '0', '0', '0', '/home', 'Bizs', '更多服务', '更多服务', 'cs://1/image/aW1hZ2UvTVRveVptTXlZelU0TlRFeVlUUTRPV0ZoTldSbFlUaGtOVGhoWm1FNU5ERmlaZw', '1', '1', '1', '{\"itemLocation\":\"/home\", \"itemGroup\":\"Bizs\"}', '30', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112334, 999986, '0', '0', '0', '/home', 'Bizs', '园区配套', '园区配套', 'cs://1/image/aW1hZ2UvTVRvMk1UZzJZV0kxT1RZME9UTTJOamhpWWpGaE1qZzBNVGxsWkRVeU9HWTRZUQ', '1', '1', 2,'{"itemLocation":"/home/community","layoutName":"communityLayout"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist');        
INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (112341, 999986, 0, 0, 0, '/home/community', 'communityBanner', '', '', 'cs://1/image/aW1hZ2UvTVRvNFlqazJPREV5WXpjNU5UazJPRGd6WTJKak5tSmtOVGRsT1Rnd056WTVZZw', 1, 1, 0, NULl, 0, 0, 1, 1, '', 0, NULL, NULL, NULL, '0', 'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
    VALUES (112342, 999986, 0, 0, 0, '/home/community', 'communityBiz', '园区食堂', '园区食堂', 'cs://1/image/aW1hZ2UvTVRwa01XVXlPV1l5TmpWbVltWmlORFl6WmpRM01tUTNOR05oTjJZd04yWm1aUQ', 1, 1, 14, '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'park_tourist', '0');    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (112343, 999986, 0, 0, 0, '/home/community', 'communityBiz', '园区商业', '园区商业', 'cs://1/image/aW1hZ2UvTVRwbE1UZzRNbVV5WmpFNU1qVmxOV1kzTW1WalptWTVORFprWmpZNFlqQmpaZw', 1, 1, 14, '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'park_tourist');    

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112335, 999986, '0', '0', '0', '/home', 'Bizs', '工位预订', '工位预订', 'cs://1/image/aW1hZ2UvTVRvM1lXRXdNR1V6WXpVeE5XUmxOR1ZpTVdFeE56ZG1ZVFZqWmpRME1tSm1ZUQ', '1', '1', 14,'{"url":"http://core.zuolin.com/station-booking/index.html?hideNavigationBar=1#/station_booking#sign_suffix"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist');        

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112336, 999986, '0', '0', '0', '/home', 'Bizs', '智慧家居', '智慧家居', 'cs://1/image/aW1hZ2UvTVRwbU5qSmhNekpsWVRJeU5tRTBNR1V6WW1NNFpHWTROemt4WkRsaU5EaGtZdw', '1', '1', 14,'{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist');        
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112337, 999986, '0', '0', '0', '/home', 'Bizs', '智能停车场', '智能停车场', 'cs://1/image/aW1hZ2UvTVRwaU5ESTBZMk01TmpCak1XTTJZVEpsWXprek1EZzBZbVV6T0RNMU1XRTNNZw', '1', '1', 14,'{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist');        
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112338, 999986, '0', '0', '0', '/home', 'Bizs', '园区一卡通', '园区一卡通', 'cs://1/image/aW1hZ2UvTVRvM1pUVXpZalZtWkRGall6YzFPRFpsTURVNFkyVmpZMlkxWXpsbE56VXlaUQ', '1', '1', 14,'{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist');        
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112339, 999986, '0', '0', '0', '/home', 'Bizs', '智能楼控', '智能楼控', 'cs://1/image/aW1hZ2UvTVRvM1kyVTFZamswWldWbE9URXhaRFJsTVRNeE4yRTVZVEU0TlRJek5qUTJNQQ', '1', '1', 14,'{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist');        

	
	
-- 菜单	
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10000,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),11000,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),11100,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),11200,'', 'EhNamespaces', 999986,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),12000,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20000,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),24000,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),25000,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),26000,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),27000,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),30000,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),30500,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),31000,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),32000,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),33000,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),34000,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),35000,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),36000,'', 'EhNamespaces', 999986,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40000,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41000,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41100,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41200,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41300,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41400,'', 'EhNamespaces', 999986,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43300,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43310,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43320,'', 'EhNamespaces', 999986,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43400,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43410,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43420,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43430,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43440,'', 'EhNamespaces', 999986,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43600,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43610,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43620,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43630,'', 'EhNamespaces', 999986,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),44000,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),44100,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),44200,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),44300,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),44400,'', 'EhNamespaces', 999986,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),46000,'', 'EhNamespaces', 999986,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),47000,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),47100,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),47150,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),47160,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),47200,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),47300,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),49600,'', 'EhNamespaces', 999986,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50000,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),51000,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),51100,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),52000,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),52100,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),52200,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),52300,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),52400,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),53000,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),53100,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56000,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56100,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56110,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56111,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56112,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56113,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56114,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56115,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56120,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56121,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56122,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56130,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56131,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56132,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56140,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56141,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56150,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56151,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56152,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56160,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56161,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56170,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56171,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56200,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56210,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56220,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56230,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58000,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58100,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58110,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58111,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58112,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58113,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58120,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58121,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58122,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58123,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58130,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58131,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58132,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58140,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58200,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58210,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58211,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58212,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58220,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58221,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58222,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58230,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58231,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),59000,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),59100,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),59150,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),59160,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),59200,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),59300,'', 'EhNamespaces', 999986,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),59400,'', 'EhNamespaces', 999986,2);


-- 物业报修

INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
	VALUES ('50', '0', '0', '任务', '任务', '0', '2', '2016-09-29 06:09:03', NULL, NULL, NULL, '999986');
-- 小店
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('600', 'business.url', 'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdefault%3Fpos%3D1%26_k%3Dzlbiz#sign_suffix', 'business url', '999986', NULL);


-- 根据http://devops.lab.everhomes.com/issues/5507要求修改 by lqs 20170109
-- 把“创新服务->创源服务”提取到服务市场替换原“创新服务”，把“创新服务->物业服务”提取到服务市场替换原“增值服务”
UPDATE `eh_launch_pad_items` SET `action_type`=33, `action_data`='{"type":60,"parentId":60,"displayType": "grid"}', `icon_uri`='cs://1/image/aW1hZ2UvTVRveE1HWm1NREZsWWpFNFpEUmtPVE5oTWpOa1pUSmtZVE5rWldSaVpHRXdaUQ' WHERE `id` in (112306, 112326);
UPDATE `eh_launch_pad_items` SET `action_type`=51, `action_data`='', `item_name`='物业服务', `item_label`='物业服务', `icon_uri`='cs://1/image/aW1hZ2UvTVRwa00yVTNNV1JtWXpsbFlURmlabU5pTkdSak56ZGtPR0kwTlRVNFl6VmtaZw' WHERE `id`=112310;
UPDATE `eh_launch_pad_items` SET `action_type`=14, `action_data`='{"url":"http://core.zuolin.com/property_service/index.html?hideNavigationBar=1#/my_service#sign_suffix"}', `item_name`='物业服务', `item_label`='物业服务', `icon_uri`='cs://1/image/aW1hZ2UvTVRwa00yVTNNV1JtWXpsbFlURmlabU5pTkdSak56ZGtPR0kwTlRVNFl6VmtaZw' WHERE `id`=112328;
-- “智能家居”里的即将上线页面更换为没有左邻信息的即将上线
UPDATE `eh_launch_pad_items` SET `action_type`=14, `action_data`='{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}' WHERE `id` IN (112317, 112336);
-- “工位预定”改为“众创空间”，把后台管理菜单名称也改为“众创空间"
UPDATE `eh_launch_pad_items` SET `item_name`='众创空间', `item_label`='众创空间' WHERE `id` IN (112316, 112335);
UPDATE `eh_web_menu_scopes` SET `menu_name`='众创空间', `apply_policy`='1' where owner_id=999986 and menu_id=40200;
-- 把“园区配套”的action_type/action_data换成原来的“增值服务”
UPDATE `eh_service_alliance_categories` SET `name`='园区配套' WHERE `id`=61;
UPDATE `eh_launch_pad_items` SET `action_type`=33, `action_data`='{"type":61,"parentId":61,"displayType": "grid"}' WHERE `id` IN (112315, 112334);
-- 删除“园区一卡通”图标
DELETE FROM `eh_launch_pad_items` WHERE `id` IN (112319, 112338);
-- 添加VIP车位
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) 
	VALUES(10076,'VIP车位预约','0',NULL,'0', 999986);
DELETE FROM `eh_launch_pad_items` WHERE `id` IN (112319, 112338);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
	VALUES (112319, 999986, '0', '0', '0', '/home', 'Bizs', 'RENTALV2', 'VIP车位', 'cs://1/image/aW1hZ2UvTVRwbU5ETTFNamhtWXpFME16WXhNMk5oTWpJelpqWTVPREZpTXpCaE5XSXpNdw', '1', '1', '49', CONCAT('{\"resourceTypeId\":',10076,',\"pageType\":0}'), '55', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
	VALUES (112338, 999986, '0', '0', '0', '/home', 'Bizs', 'RENTALV2', 'VIP车位', 'cs://1/image/aW1hZ2UvTVRwbU5ETTFNamhtWXpFME16WXhNMk5oTWpJelpqWTVPREZpTXpCaE5XSXpNdw', '1', '1', '49', CONCAT('{\"resourceTypeId\":',10076,',\"pageType\":0}'), '55', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0');
-- 调整排序
UPDATE `eh_launch_pad_items` SET default_order=10 WHERE `id`=112302 or `id`=112322; -- 1 门禁考勤
UPDATE `eh_launch_pad_items` SET default_order=20 WHERE `id`=112311 or `id`=112330; -- 2 园区通讯录
UPDATE `eh_launch_pad_items` SET default_order=30 WHERE `id`=112301 or `id`=112321; -- 3 园区活动
UPDATE `eh_launch_pad_items` SET default_order=40 WHERE `id`=112306 or `id`=112326; -- 4 创新服务
UPDATE `eh_launch_pad_items` SET default_order=50 WHERE `id`=112310 or `id`=112328; -- 5 物业服务
UPDATE `eh_launch_pad_items` SET default_order=60 WHERE `id`=112312 or `id`=112331; -- 6 公共资源预订
UPDATE `eh_launch_pad_items` SET default_order=70 WHERE `id`=112316 or `id`=112335; -- 7 众创空间
UPDATE `eh_launch_pad_items` SET default_order=80 WHERE `id`=112318 or `id`=112337; -- 8 智能停车场
UPDATE `eh_launch_pad_items` SET default_order=90 WHERE `id`=112315 or `id`=112334; -- 9 园区配套
UPDATE `eh_launch_pad_items` SET default_order=100 WHERE `id`=112313 or `id`=112332; -- 10 视频会议
UPDATE `eh_launch_pad_items` SET default_order=110 WHERE `id`=112319 or `id`=112338; -- 11 VIP车位预订
UPDATE `eh_launch_pad_items` SET default_order=120 WHERE `id`=112834 or `id`=112835; -- 12 咨询热线
UPDATE `eh_launch_pad_items` SET default_order=130 WHERE `id`=112317 or `id`=112336; -- 13 智慧家居
UPDATE `eh_launch_pad_items` SET default_order=999 WHERE `id`=112314 or `id`=112333; -- 99 更多服务
-- 视频会议移到更多、众创空间移到首页
UPDATE `eh_launch_pad_items` SET `display_flag`=1 WHERE `id`=112316 or `id`=112335; -- 7 众创空间
UPDATE `eh_launch_pad_items` SET `display_flag`=0 WHERE `id`=112313 or `id`=112332; -- 10 视频会议


-- 添加健身房 add by sw 20170217
SET @eh_launch_pad_items = (SELECT max(id) FROM `eh_launch_pad_items`);

-- 添加服务广场健身房 add by sw 20170217
SET @eh_launch_pad_items = (SELECT max(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) 
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999986', '0', '0', '0', '/home', 'Bizs', '健身房', '健身房', 'cs://1/image/aW1hZ2UvTVRvek5UUTBObU0wT0dReU1tWmhNbVZpTTJNMU1UVTVNRGN4WVRobE5qbGtPUQ', '1', '1', '14', '{"url":"http://core.zuolin.com/mobile/static/banner/jsf.html"}', '40', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', NULL, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) 
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999986', '0', '0', '0', '/home', 'Bizs', '健身房', '健身房', 'cs://1/image/aW1hZ2UvTVRvek5UUTBObU0wT0dReU1tWmhNbVZpTTJNMU1UVTVNRGN4WVRobE5qbGtPUQ', '1', '1', '14', '{"url":"http://core.zuolin.com/mobile/static/banner/jsf.html"}', '40', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', NULL, NULL);


-- 创源屏蔽"智慧楼控" add by  xujuan 20170221
delete from eh_launch_pad_items where id in (112320,112339) and namespace_id = 999986;

-- 增加创源停车工作流菜单 add by sw 20170314
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 40850, '', 'EhNamespaces', 999986, 2);










