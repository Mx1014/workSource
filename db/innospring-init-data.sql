INSERT INTO `eh_namespaces`(`id`, `name`) VALUES(999986, 'innospring');

INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES(1014, 999986, 'community_commercial', '2016-09-26 18:07:50'); 

-- INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
--	VALUES ('77', 'app.agreements.url', 'http://sywy.zuolin.com/mobile/static/app_agreements/sywy_agreements.html', 'the relative path for innospring app agreements', '999986', NULL);

INSERT INTO `eh_version_realm` VALUES ('65', 'Android_Innospring', null, UTC_TIMESTAMP(), '999986');
INSERT INTO `eh_version_realm` VALUES ('66', 'iOS_Innospring', null, UTC_TIMESTAMP(), '999986');

insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) values(113,65,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) values(114,66,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());

-- INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999986, 'sms.default.yzx', 1, 'zh_CN', '验证码-innospring', '25889');
-- INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999986, 'sms.default.yzx', 4, 'zh_CN', '派单-innospring', '25890');
-- INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999986, 'sms.default.yzx', 6, 'zh_CN', '任务2-innospring', '25892');    


INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (233082  , UUID(), '9202013', 'innospring', 'cs://1/image/aW1hZ2UvTVRvMlkySmhNbVZqTm1SaU1UQXdPREkxWkRjME5HVmxNVFU1TXpBNE5UUTBZdw', 1, 45, '1', '2',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (228015 , 233082  ,  '0',  '18051307125',  '221616',  3, UTC_TIMESTAMP(), 999986);

INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`)
	VALUES ('15102', '11982', '崇川区', 'CHONGCHUANQU', 'CCQ', '/江苏/南通市/崇川区', '3', '3', NULL, '0513', '2', '0', '0');

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`) 
	VALUES(1004140, 0, 'PM', '南通创源科技园发展有限公司', 0, '', '/1004140', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1112511, 240111044331054735, 'organization', 1004140, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`)
	VALUES(2111453, 1004140, 'USER', 233082  , 'manager', 'innospring', 0, '18051307125', 3, 999986);	

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
	NULL, NULL, NULL, NULL, NULL, NULL,NULL, 457, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'0', 182102, 182103, UTC_TIMESTAMP(), 999986);
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
	VALUES(239825274387113864,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-101','1幢','101','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113865,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-102','1幢','102','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113866,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-103','1幢','103','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113867,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-104','1幢','104','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113868,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-105','1幢','105','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113869,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-106','1幢','106','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113870,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-107','1幢','107','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113871,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-108','1幢','108','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113872,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-109','1幢','109','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113873,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-1010','1幢','1010','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113874,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-1011','1幢','1011','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113875,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-1012','1幢','1012','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113876,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-201','1幢','201','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113877,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-202','1幢','202','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113878,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-203','1幢','203','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113879,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-204','1幢','204','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113880,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-205','1幢','205','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113881,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-206','1幢','206','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113882,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-207','1幢','207','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113883,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-208','1幢','208','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113884,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-209','1幢','209','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113885,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-2010','1幢','2010','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113886,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-2011','1幢','2011','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113887,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'1幢-302','1幢','302','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113888,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-101','2幢','101','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113889,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-102','2幢','102','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113890,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-103','2幢','103','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113891,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-104','2幢','104','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113892,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-105','2幢','105','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113893,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-106','2幢','106','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113894,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-201','2幢','201','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113895,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-202','2幢','202','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113896,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-203','2幢','203','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113897,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-204','2幢','204','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113898,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-205','2幢','205','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113899,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-301','2幢','301','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113900,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-302','2幢','302','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113901,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-303','2幢','303','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113902,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-304','2幢','304','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113903,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-305','2幢','305','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113904,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-306','2幢','306','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113905,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-401','2幢','401','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113906,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-402','2幢','402','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113907,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-403','2幢','403','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113908,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-404','2幢','404','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113909,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-405','2幢','405','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113910,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-406','2幢','406','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113911,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-501','2幢','501','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113912,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-502','2幢','502','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113913,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-503','2幢','503','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113914,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-504','2幢','504','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113915,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-505','2幢','505','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113916,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-506','2幢','506','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113917,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-601','2幢','601','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113918,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-602','2幢','602','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113919,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-603','2幢','603','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113920,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-604','2幢','604','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113921,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-605','2幢','605','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113922,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-606','2幢','606','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113923,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-701','2幢','701','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113924,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-702','2幢','702','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113925,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-703','2幢','703','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113926,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-704','2幢','704','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113927,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-705','2幢','705','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113928,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-706','2幢','706','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113929,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-801','2幢','801','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113930,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-802','2幢','802','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113931,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-803','2幢','803','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113932,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-804','2幢','804','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113933,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-805','2幢','805','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113934,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-806','2幢','806','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113935,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-901','2幢','901','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113936,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-902','2幢','902','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113937,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-903','2幢','903','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113938,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-904','2幢','904','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113939,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-905','2幢','905','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113940,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'2幢-906','2幢','906','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113941,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-101','3幢','101','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113942,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-102','3幢','102','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113943,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-103','3幢','103','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113944,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-104','3幢','104','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113945,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-105','3幢','105','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113946,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-201','3幢','201','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113947,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-202','3幢','202','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113948,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-203','3幢','203','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113949,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-204','3幢','204','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113950,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-205','3幢','205','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113951,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-206','3幢','206','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113952,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-301','3幢','301','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113953,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-302','3幢','302','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113954,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-303','3幢','303','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113955,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-304','3幢','304','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113956,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-305','3幢','305','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113957,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-306','3幢','306','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113958,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-401','3幢','401','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113959,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-402','3幢','402','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113960,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-403','3幢','403','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113961,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-404','3幢','404','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113962,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-405','3幢','405','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113963,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-406','3幢','406','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113964,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-501','3幢','501','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113965,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-502','3幢','502','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113966,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-503','3幢','503','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113967,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-504','3幢','504','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113968,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-505','3幢','505','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113969,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-506','3幢','506','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113970,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-601','3幢','601','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113971,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-602','3幢','602','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113972,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-603','3幢','603','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113973,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-604','3幢','604','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113974,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-605','3幢','605','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113975,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-606','3幢','606','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113976,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-701','3幢','701','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113977,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-702','3幢','702','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113978,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-703','3幢','703','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113979,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-704','3幢','704','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113980,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-705','3幢','705','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113981,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-706','3幢','706','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113982,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-801','3幢','801','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113983,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-802','3幢','802','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113984,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-803','3幢','803','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113985,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-804','3幢','804','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113986,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-805','3幢','805','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113987,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'3幢-806','3幢','806','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113988,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-101','5幢','101','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113989,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-102','5幢','102','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113990,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-103','5幢','103','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113991,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-104','5幢','104','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113992,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-105','5幢','105','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113993,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-106','5幢','106','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113994,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-107','5幢','107','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113995,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-201','5幢','201','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113996,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-202','5幢','202','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113997,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-203','5幢','203','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113998,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-204','5幢','204','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387113999,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-205','5幢','205','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114000,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-206','5幢','206','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114001,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-301','5幢','301','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114002,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-302','5幢','302','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114003,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-303','5幢','303','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114004,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-304','5幢','304','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114005,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-305','5幢','305','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114006,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-306','5幢','306','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114007,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-401','5幢','401','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114008,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-402','5幢','402','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114009,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-403','5幢','403','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114010,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-404','5幢','404','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114011,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-405','5幢','405','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114012,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-406','5幢','406','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114013,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-501','5幢','501','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114014,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-502','5幢','502','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114015,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-503','5幢','503','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114016,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-504','5幢','504','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114017,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-505','5幢','505','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114018,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-506','5幢','506','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114019,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-601','5幢','601','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114020,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-602','5幢','602','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114021,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-603','5幢','603','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114022,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-604','5幢','604','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114023,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-605','5幢','605','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114024,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-606','5幢','606','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114025,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-701','5幢','701','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114026,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-702','5幢','702','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114027,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-703','5幢','703','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114028,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-704','5幢','704','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114029,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-705','5幢','705','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114030,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-706','5幢','706','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114031,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-801','5幢','801','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114032,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-802','5幢','802','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114033,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-803','5幢','803','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114034,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-804','5幢','804','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114035,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-805','5幢','805','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114036,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-806','5幢','806','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114037,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-901','5幢','901','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114038,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-902','5幢','902','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114039,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-903','5幢','903','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114040,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-904','5幢','904','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114041,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-905','5幢','905','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114042,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'5幢-906','5幢','906','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114043,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-101','6幢','101','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114044,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-102','6幢','102','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114045,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-103','6幢','103','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114046,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-104','6幢','104','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114047,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-201','6幢','201','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114048,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-202','6幢','202','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114049,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-203','6幢','203','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114050,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-204','6幢','204','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114051,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-205','6幢','205','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114052,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-206','6幢','206','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114053,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-301','6幢','301','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114054,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-302','6幢','302','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114055,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-303','6幢','303','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114056,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-304','6幢','304','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114057,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-305','6幢','305','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114058,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-306','6幢','306','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114059,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-401','6幢','401','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114060,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-402','6幢','402','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114061,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-403','6幢','403','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114062,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-404','6幢','404','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114063,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-405','6幢','405','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114064,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-406','6幢','406','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114065,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-501','6幢','501','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114066,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-502','6幢','502','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114067,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-503','6幢','503','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114068,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-504','6幢','504','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114069,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-505','6幢','505','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114070,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-506','6幢','506','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114071,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-601','6幢','601','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114072,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-602','6幢','602','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114073,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-603','6幢','603','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114074,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-604','6幢','604','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114075,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-605','6幢','605','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114076,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-606','6幢','606','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114077,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-701','6幢','701','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114078,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-702','6幢','702','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114079,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-703','6幢','703','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114080,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-704','6幢','704','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114081,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-705','6幢','705','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114082,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-706','6幢','706','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114083,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-801','6幢','801','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114084,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-802','6幢','802','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114085,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-803','6幢','803','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114086,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-804','6幢','804','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114087,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-805','6幢','805','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114088,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-806','6幢','806','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114089,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-901','6幢','901','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114090,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-902','6幢','902','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114091,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-903','6幢','903','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114092,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-904','6幢','904','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114093,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-905','6幢','905','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114094,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-906','6幢','906','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114095,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-1001','6幢','1001','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114096,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-1002','6幢','1002','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114097,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-1003','6幢','1003','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114098,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-1004','6幢','1004','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114099,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-1005','6幢','1005','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114100,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'6幢-1006','6幢','1006','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114101,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-101','7幢','101','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114102,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-102','7幢','102','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114103,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-103','7幢','103','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114104,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-105','7幢','105','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114105,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-201','7幢','201','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114106,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-202','7幢','202','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114107,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-203','7幢','203','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114108,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-204','7幢','204','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114109,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-205','7幢','205','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114110,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-301','7幢','301','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114111,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-302','7幢','302','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114112,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-303','7幢','303','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114113,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-304','7幢','304','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114114,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-305','7幢','305','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114115,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-306','7幢','306','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114116,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-401','7幢','401','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114117,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-402','7幢','402','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114118,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-403','7幢','403','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114119,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-404','7幢','404','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114120,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-405','7幢','405','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114121,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-406','7幢','406','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114122,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-501','7幢','501','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114123,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-502','7幢','502','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114124,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-503','7幢','503','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114125,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-504','7幢','504','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114126,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-505','7幢','505','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114127,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-506','7幢','506','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114128,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-601','7幢','601','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114129,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-602','7幢','602','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114130,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-603','7幢','603','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114131,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-604','7幢','604','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114132,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-605','7幢','605','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114133,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-606','7幢','606','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114134,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-701','7幢','701','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114135,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-702','7幢','702','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114136,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-703','7幢','703','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114137,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-704','7幢','704','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114138,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-705','7幢','705','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114139,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-706','7幢','706','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114140,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-801','7幢','801','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114141,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-802','7幢','802','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114142,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-803','7幢','803','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114143,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-804','7幢','804','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114144,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-805','7幢','805','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114145,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-806','7幢','806','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114146,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-901','7幢','901','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114147,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-902','7幢','902','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114148,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-903','7幢','903','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114149,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-904','7幢','904','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114150,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-905','7幢','905','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114151,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-906','7幢','906','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114152,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-1001','7幢','1001','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114153,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-1002','7幢','1002','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114154,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-1003','7幢','1003','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114155,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-1004','7幢','1004','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114156,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-1005','7幢','1005','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114157,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'7幢-1006','7幢','1006','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114158,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-101','8幢','101','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114159,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-102','8幢','102','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114160,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-103','8幢','103','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114161,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-104','8幢','104','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114162,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-105','8幢','105','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114163,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-106','8幢','106','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114164,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-107','8幢','107','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114165,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-108','8幢','108','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114166,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-109','8幢','109','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114167,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1010','8幢','1010','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114168,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1011','8幢','1011','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114169,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1012','8幢','1012','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114170,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-201','8幢','201','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114171,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-202','8幢','202','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114172,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-203','8幢','203','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114173,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-207','8幢','207','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114174,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-208','8幢','208','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114175,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-209','8幢','209','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114176,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-2012','8幢','2012','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114177,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-2013','8幢','2013','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114178,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-2014','8幢','2014','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114179,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-301','8幢','301','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114180,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-302','8幢','302','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114181,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-303','8幢','303','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114182,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-304','8幢','304','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114183,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-305','8幢','305','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114184,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-306','8幢','306','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114185,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-307','8幢','307','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114186,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-308','8幢','308','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114187,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-309','8幢','309','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114188,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-3010','8幢','3010','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114189,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-3011','8幢','3011','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114190,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-3012','8幢','3012','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114191,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-3013','8幢','3013','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114192,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-401','8幢','401','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114193,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-402','8幢','402','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114194,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-403','8幢','403','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114195,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-404','8幢','404','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114196,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-405','8幢','405','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114197,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-406','8幢','406','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114198,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-407','8幢','407','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114199,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-408','8幢','408','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114200,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-501','8幢','501','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114201,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-502','8幢','502','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114202,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-503','8幢','503','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114203,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-504','8幢','504','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114204,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-505','8幢','505','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114205,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-506','8幢','506','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114206,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-507','8幢','507','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114207,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-507','8幢','507','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114208,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-601','8幢','601','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114209,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-602','8幢','602','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114210,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-603','8幢','603','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114211,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-604','8幢','604','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114212,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-605','8幢','605','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114213,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-606','8幢','606','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114214,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-607','8幢','607','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114215,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-608','8幢','608','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114216,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-701','8幢','701','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114217,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-702','8幢','702','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114218,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-703','8幢','703','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114219,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-704','8幢','704','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114220,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-705','8幢','705','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114221,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-706','8幢','706','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114222,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-707','8幢','707','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114223,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-708','8幢','708','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114224,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-801','8幢','801','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114225,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-802','8幢','802','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114226,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-803','8幢','803','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114227,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-804','8幢','804','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114228,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-805','8幢','805','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114229,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-806','8幢','806','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114230,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-807','8幢','807','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114231,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-808','8幢','808','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114232,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-901','8幢','901','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114233,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-902','8幢','902','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114234,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-903','8幢','903','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114235,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-904','8幢','904','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114236,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-905','8幢','905','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114237,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-906','8幢','906','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114238,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-907','8幢','907','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114239,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-908','8幢','908','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114240,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1001','8幢','1001','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114241,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1002','8幢','1002','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114242,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1003','8幢','1003','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114243,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1004','8幢','1004','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114244,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1005','8幢','1005','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114245,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1006','8幢','1006','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114246,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1007','8幢','1007','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114247,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1008','8幢','1008','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114248,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1101','8幢','1101','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114249,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1102','8幢','1102','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114250,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1103','8幢','1103','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114251,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1104','8幢','1104','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114252,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1105','8幢','1105','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114253,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1106','8幢','1106','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114254,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1107','8幢','1107','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114255,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1108','8幢','1108','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114256,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1201','8幢','1201','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114257,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1202','8幢','1202','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114258,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1203','8幢','1203','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114259,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1204','8幢','1204','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114260,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1205','8幢','1205','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114261,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1206','8幢','1206','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114262,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1207','8幢','1207','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114263,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1208','8幢','1208','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114264,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1301','8幢','1301','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114265,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1302','8幢','1302','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114266,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1303','8幢','1303','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114267,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1304','8幢','1304','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114268,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1305','8幢','1305','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114269,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1306','8幢','1306','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114270,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1401','8幢','1401','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114271,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1402','8幢','1402','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114272,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1403','8幢','1403','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114273,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1404','8幢','1404','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114274,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1405','8幢','1405','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114275,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'8幢-1406','8幢','1406','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114276,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-101','9幢','101','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114277,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-102','9幢','102','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114278,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-103','9幢','103','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114279,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-104','9幢','104','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114280,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-201','9幢','201','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114281,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-202','9幢','202','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114282,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-203','9幢','203','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114283,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-204','9幢','204','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114284,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-205','9幢','205','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114285,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-301','9幢','301','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114286,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-302','9幢','302','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114287,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-303','9幢','303','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114288,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-304','9幢','304','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114289,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-305','9幢','305','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114290,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-306','9幢','306','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114291,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-401','9幢','401','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114292,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-402','9幢','402','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114293,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-403','9幢','403','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114294,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-404','9幢','404','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114295,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-405','9幢','405','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114296,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-406','9幢','406','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114297,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-501','9幢','501','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114298,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-502','9幢','502','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114299,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-503','9幢','503','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114300,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-504','9幢','504','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114301,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-505','9幢','505','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114302,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-506','9幢','506','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114303,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-601','9幢','601','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114304,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-602','9幢','602','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114305,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-603','9幢','603','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114306,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-604','9幢','604','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114307,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-605','9幢','605','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114308,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-606','9幢','606','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114309,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-701','9幢','701','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114310,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-702','9幢','702','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114311,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-703','9幢','703','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114312,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-704','9幢','704','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114313,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-705','9幢','705','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114314,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-706','9幢','706','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114315,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-801','9幢','801','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114316,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-802','9幢','802','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114317,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-803','9幢','803','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114318,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-804','9幢','804','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114319,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-805','9幢','805','2','0',UTC_TIMESTAMP(), 999986);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387114320,UUID(),240111044331054735, 15101, '南通市',  15102, '崇川区' ,'9幢-806','9幢','806','2','0',UTC_TIMESTAMP(), 999986);
	

INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21874, 1004140, 240111044331054735, 239825274387113864, '1幢-101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21875, 1004140, 240111044331054735, 239825274387113865, '1幢-102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21876, 1004140, 240111044331054735, 239825274387113866, '1幢-103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21877, 1004140, 240111044331054735, 239825274387113867, '1幢-104', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21878, 1004140, 240111044331054735, 239825274387113868, '1幢-105', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21879, 1004140, 240111044331054735, 239825274387113869, '1幢-106', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21880, 1004140, 240111044331054735, 239825274387113870, '1幢-107', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21881, 1004140, 240111044331054735, 239825274387113871, '1幢-108', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21882, 1004140, 240111044331054735, 239825274387113872, '1幢-109', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21883, 1004140, 240111044331054735, 239825274387113873, '1幢-1010', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21884, 1004140, 240111044331054735, 239825274387113874, '1幢-1011', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21885, 1004140, 240111044331054735, 239825274387113875, '1幢-1012', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21886, 1004140, 240111044331054735, 239825274387113876, '1幢-201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21887, 1004140, 240111044331054735, 239825274387113877, '1幢-202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21888, 1004140, 240111044331054735, 239825274387113878, '1幢-203', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21889, 1004140, 240111044331054735, 239825274387113879, '1幢-204', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21890, 1004140, 240111044331054735, 239825274387113880, '1幢-205', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21891, 1004140, 240111044331054735, 239825274387113881, '1幢-206', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21892, 1004140, 240111044331054735, 239825274387113882, '1幢-207', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21893, 1004140, 240111044331054735, 239825274387113883, '1幢-208', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21894, 1004140, 240111044331054735, 239825274387113884, '1幢-209', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21895, 1004140, 240111044331054735, 239825274387113885, '1幢-2010', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21896, 1004140, 240111044331054735, 239825274387113886, '1幢-2011', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21897, 1004140, 240111044331054735, 239825274387113887, '1幢-302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21898, 1004140, 240111044331054735, 239825274387113888, '2幢-101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21899, 1004140, 240111044331054735, 239825274387113889, '2幢-102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21900, 1004140, 240111044331054735, 239825274387113890, '2幢-103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21901, 1004140, 240111044331054735, 239825274387113891, '2幢-104', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21902, 1004140, 240111044331054735, 239825274387113892, '2幢-105', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21903, 1004140, 240111044331054735, 239825274387113893, '2幢-106', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21904, 1004140, 240111044331054735, 239825274387113894, '2幢-201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21905, 1004140, 240111044331054735, 239825274387113895, '2幢-202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21906, 1004140, 240111044331054735, 239825274387113896, '2幢-203', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21907, 1004140, 240111044331054735, 239825274387113897, '2幢-204', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21908, 1004140, 240111044331054735, 239825274387113898, '2幢-205', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21909, 1004140, 240111044331054735, 239825274387113899, '2幢-301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21910, 1004140, 240111044331054735, 239825274387113900, '2幢-302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21911, 1004140, 240111044331054735, 239825274387113901, '2幢-303', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21912, 1004140, 240111044331054735, 239825274387113902, '2幢-304', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21913, 1004140, 240111044331054735, 239825274387113903, '2幢-305', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21914, 1004140, 240111044331054735, 239825274387113904, '2幢-306', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21915, 1004140, 240111044331054735, 239825274387113905, '2幢-401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21916, 1004140, 240111044331054735, 239825274387113906, '2幢-402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21917, 1004140, 240111044331054735, 239825274387113907, '2幢-403', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21918, 1004140, 240111044331054735, 239825274387113908, '2幢-404', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21919, 1004140, 240111044331054735, 239825274387113909, '2幢-405', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21920, 1004140, 240111044331054735, 239825274387113910, '2幢-406', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21921, 1004140, 240111044331054735, 239825274387113911, '2幢-501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21922, 1004140, 240111044331054735, 239825274387113912, '2幢-502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21923, 1004140, 240111044331054735, 239825274387113913, '2幢-503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21924, 1004140, 240111044331054735, 239825274387113914, '2幢-504', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21925, 1004140, 240111044331054735, 239825274387113915, '2幢-505', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21926, 1004140, 240111044331054735, 239825274387113916, '2幢-506', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21927, 1004140, 240111044331054735, 239825274387113917, '2幢-601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21928, 1004140, 240111044331054735, 239825274387113918, '2幢-602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21929, 1004140, 240111044331054735, 239825274387113919, '2幢-603', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21930, 1004140, 240111044331054735, 239825274387113920, '2幢-604', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21931, 1004140, 240111044331054735, 239825274387113921, '2幢-605', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21932, 1004140, 240111044331054735, 239825274387113922, '2幢-606', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21933, 1004140, 240111044331054735, 239825274387113923, '2幢-701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21934, 1004140, 240111044331054735, 239825274387113924, '2幢-702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21935, 1004140, 240111044331054735, 239825274387113925, '2幢-703', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21936, 1004140, 240111044331054735, 239825274387113926, '2幢-704', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21937, 1004140, 240111044331054735, 239825274387113927, '2幢-705', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21938, 1004140, 240111044331054735, 239825274387113928, '2幢-706', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21939, 1004140, 240111044331054735, 239825274387113929, '2幢-801', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21940, 1004140, 240111044331054735, 239825274387113930, '2幢-802', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21941, 1004140, 240111044331054735, 239825274387113931, '2幢-803', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21942, 1004140, 240111044331054735, 239825274387113932, '2幢-804', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21943, 1004140, 240111044331054735, 239825274387113933, '2幢-805', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21944, 1004140, 240111044331054735, 239825274387113934, '2幢-806', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21945, 1004140, 240111044331054735, 239825274387113935, '2幢-901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21946, 1004140, 240111044331054735, 239825274387113936, '2幢-902', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21947, 1004140, 240111044331054735, 239825274387113937, '2幢-903', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21948, 1004140, 240111044331054735, 239825274387113938, '2幢-904', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21949, 1004140, 240111044331054735, 239825274387113939, '2幢-905', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21950, 1004140, 240111044331054735, 239825274387113940, '2幢-906', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21951, 1004140, 240111044331054735, 239825274387113941, '3幢-101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21952, 1004140, 240111044331054735, 239825274387113942, '3幢-102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21953, 1004140, 240111044331054735, 239825274387113943, '3幢-103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21954, 1004140, 240111044331054735, 239825274387113944, '3幢-104', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21955, 1004140, 240111044331054735, 239825274387113945, '3幢-105', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21956, 1004140, 240111044331054735, 239825274387113946, '3幢-201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21957, 1004140, 240111044331054735, 239825274387113947, '3幢-202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21958, 1004140, 240111044331054735, 239825274387113948, '3幢-203', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21959, 1004140, 240111044331054735, 239825274387113949, '3幢-204', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21960, 1004140, 240111044331054735, 239825274387113950, '3幢-205', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21961, 1004140, 240111044331054735, 239825274387113951, '3幢-206', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21962, 1004140, 240111044331054735, 239825274387113952, '3幢-301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21963, 1004140, 240111044331054735, 239825274387113953, '3幢-302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21964, 1004140, 240111044331054735, 239825274387113954, '3幢-303', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21965, 1004140, 240111044331054735, 239825274387113955, '3幢-304', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21966, 1004140, 240111044331054735, 239825274387113956, '3幢-305', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21967, 1004140, 240111044331054735, 239825274387113957, '3幢-306', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21968, 1004140, 240111044331054735, 239825274387113958, '3幢-401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21969, 1004140, 240111044331054735, 239825274387113959, '3幢-402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21970, 1004140, 240111044331054735, 239825274387113960, '3幢-403', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21971, 1004140, 240111044331054735, 239825274387113961, '3幢-404', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21972, 1004140, 240111044331054735, 239825274387113962, '3幢-405', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21973, 1004140, 240111044331054735, 239825274387113963, '3幢-406', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21974, 1004140, 240111044331054735, 239825274387113964, '3幢-501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21975, 1004140, 240111044331054735, 239825274387113965, '3幢-502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21976, 1004140, 240111044331054735, 239825274387113966, '3幢-503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21977, 1004140, 240111044331054735, 239825274387113967, '3幢-504', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21978, 1004140, 240111044331054735, 239825274387113968, '3幢-505', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21979, 1004140, 240111044331054735, 239825274387113969, '3幢-506', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21980, 1004140, 240111044331054735, 239825274387113970, '3幢-601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21981, 1004140, 240111044331054735, 239825274387113971, '3幢-602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21982, 1004140, 240111044331054735, 239825274387113972, '3幢-603', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21983, 1004140, 240111044331054735, 239825274387113973, '3幢-604', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21984, 1004140, 240111044331054735, 239825274387113974, '3幢-605', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21985, 1004140, 240111044331054735, 239825274387113975, '3幢-606', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21986, 1004140, 240111044331054735, 239825274387113976, '3幢-701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21987, 1004140, 240111044331054735, 239825274387113977, '3幢-702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21988, 1004140, 240111044331054735, 239825274387113978, '3幢-703', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21989, 1004140, 240111044331054735, 239825274387113979, '3幢-704', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21990, 1004140, 240111044331054735, 239825274387113980, '3幢-705', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21991, 1004140, 240111044331054735, 239825274387113981, '3幢-706', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21992, 1004140, 240111044331054735, 239825274387113982, '3幢-801', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21993, 1004140, 240111044331054735, 239825274387113983, '3幢-802', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21994, 1004140, 240111044331054735, 239825274387113984, '3幢-803', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21995, 1004140, 240111044331054735, 239825274387113985, '3幢-804', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21996, 1004140, 240111044331054735, 239825274387113986, '3幢-805', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21997, 1004140, 240111044331054735, 239825274387113987, '3幢-806', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21998, 1004140, 240111044331054735, 239825274387113988, '5幢-101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (21999, 1004140, 240111044331054735, 239825274387113989, '5幢-102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22000, 1004140, 240111044331054735, 239825274387113990, '5幢-103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22001, 1004140, 240111044331054735, 239825274387113991, '5幢-104', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22002, 1004140, 240111044331054735, 239825274387113992, '5幢-105', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22003, 1004140, 240111044331054735, 239825274387113993, '5幢-106', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22004, 1004140, 240111044331054735, 239825274387113994, '5幢-107', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22005, 1004140, 240111044331054735, 239825274387113995, '5幢-201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22006, 1004140, 240111044331054735, 239825274387113996, '5幢-202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22007, 1004140, 240111044331054735, 239825274387113997, '5幢-203', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22008, 1004140, 240111044331054735, 239825274387113998, '5幢-204', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22009, 1004140, 240111044331054735, 239825274387113999, '5幢-205', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22010, 1004140, 240111044331054735, 239825274387114000, '5幢-206', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22011, 1004140, 240111044331054735, 239825274387114001, '5幢-301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22012, 1004140, 240111044331054735, 239825274387114002, '5幢-302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22013, 1004140, 240111044331054735, 239825274387114003, '5幢-303', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22014, 1004140, 240111044331054735, 239825274387114004, '5幢-304', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22015, 1004140, 240111044331054735, 239825274387114005, '5幢-305', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22016, 1004140, 240111044331054735, 239825274387114006, '5幢-306', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22017, 1004140, 240111044331054735, 239825274387114007, '5幢-401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22018, 1004140, 240111044331054735, 239825274387114008, '5幢-402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22019, 1004140, 240111044331054735, 239825274387114009, '5幢-403', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22020, 1004140, 240111044331054735, 239825274387114010, '5幢-404', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22021, 1004140, 240111044331054735, 239825274387114011, '5幢-405', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22022, 1004140, 240111044331054735, 239825274387114012, '5幢-406', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22023, 1004140, 240111044331054735, 239825274387114013, '5幢-501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22024, 1004140, 240111044331054735, 239825274387114014, '5幢-502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22025, 1004140, 240111044331054735, 239825274387114015, '5幢-503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22026, 1004140, 240111044331054735, 239825274387114016, '5幢-504', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22027, 1004140, 240111044331054735, 239825274387114017, '5幢-505', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22028, 1004140, 240111044331054735, 239825274387114018, '5幢-506', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22029, 1004140, 240111044331054735, 239825274387114019, '5幢-601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22030, 1004140, 240111044331054735, 239825274387114020, '5幢-602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22031, 1004140, 240111044331054735, 239825274387114021, '5幢-603', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22032, 1004140, 240111044331054735, 239825274387114022, '5幢-604', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22033, 1004140, 240111044331054735, 239825274387114023, '5幢-605', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22034, 1004140, 240111044331054735, 239825274387114024, '5幢-606', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22035, 1004140, 240111044331054735, 239825274387114025, '5幢-701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22036, 1004140, 240111044331054735, 239825274387114026, '5幢-702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22037, 1004140, 240111044331054735, 239825274387114027, '5幢-703', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22038, 1004140, 240111044331054735, 239825274387114028, '5幢-704', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22039, 1004140, 240111044331054735, 239825274387114029, '5幢-705', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22040, 1004140, 240111044331054735, 239825274387114030, '5幢-706', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22041, 1004140, 240111044331054735, 239825274387114031, '5幢-801', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22042, 1004140, 240111044331054735, 239825274387114032, '5幢-802', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22043, 1004140, 240111044331054735, 239825274387114033, '5幢-803', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22044, 1004140, 240111044331054735, 239825274387114034, '5幢-804', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22045, 1004140, 240111044331054735, 239825274387114035, '5幢-805', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22046, 1004140, 240111044331054735, 239825274387114036, '5幢-806', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22047, 1004140, 240111044331054735, 239825274387114037, '5幢-901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22048, 1004140, 240111044331054735, 239825274387114038, '5幢-902', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22049, 1004140, 240111044331054735, 239825274387114039, '5幢-903', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22050, 1004140, 240111044331054735, 239825274387114040, '5幢-904', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22051, 1004140, 240111044331054735, 239825274387114041, '5幢-905', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22052, 1004140, 240111044331054735, 239825274387114042, '5幢-906', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22053, 1004140, 240111044331054735, 239825274387114043, '6幢-101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22054, 1004140, 240111044331054735, 239825274387114044, '6幢-102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22055, 1004140, 240111044331054735, 239825274387114045, '6幢-103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22056, 1004140, 240111044331054735, 239825274387114046, '6幢-104', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22057, 1004140, 240111044331054735, 239825274387114047, '6幢-201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22058, 1004140, 240111044331054735, 239825274387114048, '6幢-202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22059, 1004140, 240111044331054735, 239825274387114049, '6幢-203', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22060, 1004140, 240111044331054735, 239825274387114050, '6幢-204', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22061, 1004140, 240111044331054735, 239825274387114051, '6幢-205', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22062, 1004140, 240111044331054735, 239825274387114052, '6幢-206', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22063, 1004140, 240111044331054735, 239825274387114053, '6幢-301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22064, 1004140, 240111044331054735, 239825274387114054, '6幢-302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22065, 1004140, 240111044331054735, 239825274387114055, '6幢-303', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22066, 1004140, 240111044331054735, 239825274387114056, '6幢-304', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22067, 1004140, 240111044331054735, 239825274387114057, '6幢-305', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22068, 1004140, 240111044331054735, 239825274387114058, '6幢-306', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22069, 1004140, 240111044331054735, 239825274387114059, '6幢-401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22070, 1004140, 240111044331054735, 239825274387114060, '6幢-402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22071, 1004140, 240111044331054735, 239825274387114061, '6幢-403', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22072, 1004140, 240111044331054735, 239825274387114062, '6幢-404', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22073, 1004140, 240111044331054735, 239825274387114063, '6幢-405', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22074, 1004140, 240111044331054735, 239825274387114064, '6幢-406', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22075, 1004140, 240111044331054735, 239825274387114065, '6幢-501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22076, 1004140, 240111044331054735, 239825274387114066, '6幢-502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22077, 1004140, 240111044331054735, 239825274387114067, '6幢-503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22078, 1004140, 240111044331054735, 239825274387114068, '6幢-504', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22079, 1004140, 240111044331054735, 239825274387114069, '6幢-505', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22080, 1004140, 240111044331054735, 239825274387114070, '6幢-506', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22081, 1004140, 240111044331054735, 239825274387114071, '6幢-601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22082, 1004140, 240111044331054735, 239825274387114072, '6幢-602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22083, 1004140, 240111044331054735, 239825274387114073, '6幢-603', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22084, 1004140, 240111044331054735, 239825274387114074, '6幢-604', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22085, 1004140, 240111044331054735, 239825274387114075, '6幢-605', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22086, 1004140, 240111044331054735, 239825274387114076, '6幢-606', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22087, 1004140, 240111044331054735, 239825274387114077, '6幢-701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22088, 1004140, 240111044331054735, 239825274387114078, '6幢-702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22089, 1004140, 240111044331054735, 239825274387114079, '6幢-703', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22090, 1004140, 240111044331054735, 239825274387114080, '6幢-704', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22091, 1004140, 240111044331054735, 239825274387114081, '6幢-705', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22092, 1004140, 240111044331054735, 239825274387114082, '6幢-706', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22093, 1004140, 240111044331054735, 239825274387114083, '6幢-801', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22094, 1004140, 240111044331054735, 239825274387114084, '6幢-802', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22095, 1004140, 240111044331054735, 239825274387114085, '6幢-803', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22096, 1004140, 240111044331054735, 239825274387114086, '6幢-804', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22097, 1004140, 240111044331054735, 239825274387114087, '6幢-805', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22098, 1004140, 240111044331054735, 239825274387114088, '6幢-806', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22099, 1004140, 240111044331054735, 239825274387114089, '6幢-901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22100, 1004140, 240111044331054735, 239825274387114090, '6幢-902', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22101, 1004140, 240111044331054735, 239825274387114091, '6幢-903', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22102, 1004140, 240111044331054735, 239825274387114092, '6幢-904', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22103, 1004140, 240111044331054735, 239825274387114093, '6幢-905', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22104, 1004140, 240111044331054735, 239825274387114094, '6幢-906', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22105, 1004140, 240111044331054735, 239825274387114095, '6幢-1001', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22106, 1004140, 240111044331054735, 239825274387114096, '6幢-1002', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22107, 1004140, 240111044331054735, 239825274387114097, '6幢-1003', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22108, 1004140, 240111044331054735, 239825274387114098, '6幢-1004', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22109, 1004140, 240111044331054735, 239825274387114099, '6幢-1005', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22110, 1004140, 240111044331054735, 239825274387114100, '6幢-1006', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22111, 1004140, 240111044331054735, 239825274387114101, '7幢-101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22112, 1004140, 240111044331054735, 239825274387114102, '7幢-102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22113, 1004140, 240111044331054735, 239825274387114103, '7幢-103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22114, 1004140, 240111044331054735, 239825274387114104, '7幢-105', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22115, 1004140, 240111044331054735, 239825274387114105, '7幢-201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22116, 1004140, 240111044331054735, 239825274387114106, '7幢-202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22117, 1004140, 240111044331054735, 239825274387114107, '7幢-203', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22118, 1004140, 240111044331054735, 239825274387114108, '7幢-204', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22119, 1004140, 240111044331054735, 239825274387114109, '7幢-205', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22120, 1004140, 240111044331054735, 239825274387114110, '7幢-301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22121, 1004140, 240111044331054735, 239825274387114111, '7幢-302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22122, 1004140, 240111044331054735, 239825274387114112, '7幢-303', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22123, 1004140, 240111044331054735, 239825274387114113, '7幢-304', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22124, 1004140, 240111044331054735, 239825274387114114, '7幢-305', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22125, 1004140, 240111044331054735, 239825274387114115, '7幢-306', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22126, 1004140, 240111044331054735, 239825274387114116, '7幢-401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22127, 1004140, 240111044331054735, 239825274387114117, '7幢-402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22128, 1004140, 240111044331054735, 239825274387114118, '7幢-403', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22129, 1004140, 240111044331054735, 239825274387114119, '7幢-404', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22130, 1004140, 240111044331054735, 239825274387114120, '7幢-405', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22131, 1004140, 240111044331054735, 239825274387114121, '7幢-406', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22132, 1004140, 240111044331054735, 239825274387114122, '7幢-501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22133, 1004140, 240111044331054735, 239825274387114123, '7幢-502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22134, 1004140, 240111044331054735, 239825274387114124, '7幢-503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22135, 1004140, 240111044331054735, 239825274387114125, '7幢-504', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22136, 1004140, 240111044331054735, 239825274387114126, '7幢-505', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22137, 1004140, 240111044331054735, 239825274387114127, '7幢-506', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22138, 1004140, 240111044331054735, 239825274387114128, '7幢-601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22139, 1004140, 240111044331054735, 239825274387114129, '7幢-602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22140, 1004140, 240111044331054735, 239825274387114130, '7幢-603', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22141, 1004140, 240111044331054735, 239825274387114131, '7幢-604', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22142, 1004140, 240111044331054735, 239825274387114132, '7幢-605', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22143, 1004140, 240111044331054735, 239825274387114133, '7幢-606', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22144, 1004140, 240111044331054735, 239825274387114134, '7幢-701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22145, 1004140, 240111044331054735, 239825274387114135, '7幢-702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22146, 1004140, 240111044331054735, 239825274387114136, '7幢-703', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22147, 1004140, 240111044331054735, 239825274387114137, '7幢-704', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22148, 1004140, 240111044331054735, 239825274387114138, '7幢-705', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22149, 1004140, 240111044331054735, 239825274387114139, '7幢-706', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22150, 1004140, 240111044331054735, 239825274387114140, '7幢-801', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22151, 1004140, 240111044331054735, 239825274387114141, '7幢-802', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22152, 1004140, 240111044331054735, 239825274387114142, '7幢-803', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22153, 1004140, 240111044331054735, 239825274387114143, '7幢-804', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22154, 1004140, 240111044331054735, 239825274387114144, '7幢-805', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22155, 1004140, 240111044331054735, 239825274387114145, '7幢-806', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22156, 1004140, 240111044331054735, 239825274387114146, '7幢-901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22157, 1004140, 240111044331054735, 239825274387114147, '7幢-902', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22158, 1004140, 240111044331054735, 239825274387114148, '7幢-903', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22159, 1004140, 240111044331054735, 239825274387114149, '7幢-904', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22160, 1004140, 240111044331054735, 239825274387114150, '7幢-905', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22161, 1004140, 240111044331054735, 239825274387114151, '7幢-906', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22162, 1004140, 240111044331054735, 239825274387114152, '7幢-1001', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22163, 1004140, 240111044331054735, 239825274387114153, '7幢-1002', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22164, 1004140, 240111044331054735, 239825274387114154, '7幢-1003', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22165, 1004140, 240111044331054735, 239825274387114155, '7幢-1004', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22166, 1004140, 240111044331054735, 239825274387114156, '7幢-1005', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22167, 1004140, 240111044331054735, 239825274387114157, '7幢-1006', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22168, 1004140, 240111044331054735, 239825274387114158, '8幢-101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22169, 1004140, 240111044331054735, 239825274387114159, '8幢-102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22170, 1004140, 240111044331054735, 239825274387114160, '8幢-103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22171, 1004140, 240111044331054735, 239825274387114161, '8幢-104', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22172, 1004140, 240111044331054735, 239825274387114162, '8幢-105', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22173, 1004140, 240111044331054735, 239825274387114163, '8幢-106', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22174, 1004140, 240111044331054735, 239825274387114164, '8幢-107', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22175, 1004140, 240111044331054735, 239825274387114165, '8幢-108', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22176, 1004140, 240111044331054735, 239825274387114166, '8幢-109', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22177, 1004140, 240111044331054735, 239825274387114167, '8幢-1010', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22178, 1004140, 240111044331054735, 239825274387114168, '8幢-1011', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22179, 1004140, 240111044331054735, 239825274387114169, '8幢-1012', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22180, 1004140, 240111044331054735, 239825274387114170, '8幢-201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22181, 1004140, 240111044331054735, 239825274387114171, '8幢-202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22182, 1004140, 240111044331054735, 239825274387114172, '8幢-203', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22183, 1004140, 240111044331054735, 239825274387114173, '8幢-207', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22184, 1004140, 240111044331054735, 239825274387114174, '8幢-208', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22185, 1004140, 240111044331054735, 239825274387114175, '8幢-209', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22186, 1004140, 240111044331054735, 239825274387114176, '8幢-2012', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22187, 1004140, 240111044331054735, 239825274387114177, '8幢-2013', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22188, 1004140, 240111044331054735, 239825274387114178, '8幢-2014', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22189, 1004140, 240111044331054735, 239825274387114179, '8幢-301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22190, 1004140, 240111044331054735, 239825274387114180, '8幢-302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22191, 1004140, 240111044331054735, 239825274387114181, '8幢-303', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22192, 1004140, 240111044331054735, 239825274387114182, '8幢-304', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22193, 1004140, 240111044331054735, 239825274387114183, '8幢-305', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22194, 1004140, 240111044331054735, 239825274387114184, '8幢-306', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22195, 1004140, 240111044331054735, 239825274387114185, '8幢-307', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22196, 1004140, 240111044331054735, 239825274387114186, '8幢-308', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22197, 1004140, 240111044331054735, 239825274387114187, '8幢-309', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22198, 1004140, 240111044331054735, 239825274387114188, '8幢-3010', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22199, 1004140, 240111044331054735, 239825274387114189, '8幢-3011', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22200, 1004140, 240111044331054735, 239825274387114190, '8幢-3012', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22201, 1004140, 240111044331054735, 239825274387114191, '8幢-3013', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22202, 1004140, 240111044331054735, 239825274387114192, '8幢-401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22203, 1004140, 240111044331054735, 239825274387114193, '8幢-402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22204, 1004140, 240111044331054735, 239825274387114194, '8幢-403', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22205, 1004140, 240111044331054735, 239825274387114195, '8幢-404', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22206, 1004140, 240111044331054735, 239825274387114196, '8幢-405', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22207, 1004140, 240111044331054735, 239825274387114197, '8幢-406', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22208, 1004140, 240111044331054735, 239825274387114198, '8幢-407', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22209, 1004140, 240111044331054735, 239825274387114199, '8幢-408', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22210, 1004140, 240111044331054735, 239825274387114200, '8幢-501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22211, 1004140, 240111044331054735, 239825274387114201, '8幢-502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22212, 1004140, 240111044331054735, 239825274387114202, '8幢-503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22213, 1004140, 240111044331054735, 239825274387114203, '8幢-504', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22214, 1004140, 240111044331054735, 239825274387114204, '8幢-505', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22215, 1004140, 240111044331054735, 239825274387114205, '8幢-506', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22216, 1004140, 240111044331054735, 239825274387114206, '8幢-507', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22217, 1004140, 240111044331054735, 239825274387114207, '8幢-507', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22218, 1004140, 240111044331054735, 239825274387114208, '8幢-601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22219, 1004140, 240111044331054735, 239825274387114209, '8幢-602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22220, 1004140, 240111044331054735, 239825274387114210, '8幢-603', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22221, 1004140, 240111044331054735, 239825274387114211, '8幢-604', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22222, 1004140, 240111044331054735, 239825274387114212, '8幢-605', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22223, 1004140, 240111044331054735, 239825274387114213, '8幢-606', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22224, 1004140, 240111044331054735, 239825274387114214, '8幢-607', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22225, 1004140, 240111044331054735, 239825274387114215, '8幢-608', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22226, 1004140, 240111044331054735, 239825274387114216, '8幢-701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22227, 1004140, 240111044331054735, 239825274387114217, '8幢-702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22228, 1004140, 240111044331054735, 239825274387114218, '8幢-703', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22229, 1004140, 240111044331054735, 239825274387114219, '8幢-704', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22230, 1004140, 240111044331054735, 239825274387114220, '8幢-705', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22231, 1004140, 240111044331054735, 239825274387114221, '8幢-706', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22232, 1004140, 240111044331054735, 239825274387114222, '8幢-707', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22233, 1004140, 240111044331054735, 239825274387114223, '8幢-708', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22234, 1004140, 240111044331054735, 239825274387114224, '8幢-801', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22235, 1004140, 240111044331054735, 239825274387114225, '8幢-802', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22236, 1004140, 240111044331054735, 239825274387114226, '8幢-803', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22237, 1004140, 240111044331054735, 239825274387114227, '8幢-804', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22238, 1004140, 240111044331054735, 239825274387114228, '8幢-805', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22239, 1004140, 240111044331054735, 239825274387114229, '8幢-806', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22240, 1004140, 240111044331054735, 239825274387114230, '8幢-807', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22241, 1004140, 240111044331054735, 239825274387114231, '8幢-808', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22242, 1004140, 240111044331054735, 239825274387114232, '8幢-901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22243, 1004140, 240111044331054735, 239825274387114233, '8幢-902', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22244, 1004140, 240111044331054735, 239825274387114234, '8幢-903', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22245, 1004140, 240111044331054735, 239825274387114235, '8幢-904', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22246, 1004140, 240111044331054735, 239825274387114236, '8幢-905', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22247, 1004140, 240111044331054735, 239825274387114237, '8幢-906', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22248, 1004140, 240111044331054735, 239825274387114238, '8幢-907', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22249, 1004140, 240111044331054735, 239825274387114239, '8幢-908', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22250, 1004140, 240111044331054735, 239825274387114240, '8幢-1001', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22251, 1004140, 240111044331054735, 239825274387114241, '8幢-1002', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22252, 1004140, 240111044331054735, 239825274387114242, '8幢-1003', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22253, 1004140, 240111044331054735, 239825274387114243, '8幢-1004', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22254, 1004140, 240111044331054735, 239825274387114244, '8幢-1005', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22255, 1004140, 240111044331054735, 239825274387114245, '8幢-1006', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22256, 1004140, 240111044331054735, 239825274387114246, '8幢-1007', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22257, 1004140, 240111044331054735, 239825274387114247, '8幢-1008', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22258, 1004140, 240111044331054735, 239825274387114248, '8幢-1101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22259, 1004140, 240111044331054735, 239825274387114249, '8幢-1102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22260, 1004140, 240111044331054735, 239825274387114250, '8幢-1103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22261, 1004140, 240111044331054735, 239825274387114251, '8幢-1104', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22262, 1004140, 240111044331054735, 239825274387114252, '8幢-1105', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22263, 1004140, 240111044331054735, 239825274387114253, '8幢-1106', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22264, 1004140, 240111044331054735, 239825274387114254, '8幢-1107', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22265, 1004140, 240111044331054735, 239825274387114255, '8幢-1108', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22266, 1004140, 240111044331054735, 239825274387114256, '8幢-1201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22267, 1004140, 240111044331054735, 239825274387114257, '8幢-1202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22268, 1004140, 240111044331054735, 239825274387114258, '8幢-1203', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22269, 1004140, 240111044331054735, 239825274387114259, '8幢-1204', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22270, 1004140, 240111044331054735, 239825274387114260, '8幢-1205', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22271, 1004140, 240111044331054735, 239825274387114261, '8幢-1206', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22272, 1004140, 240111044331054735, 239825274387114262, '8幢-1207', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22273, 1004140, 240111044331054735, 239825274387114263, '8幢-1208', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22274, 1004140, 240111044331054735, 239825274387114264, '8幢-1301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22275, 1004140, 240111044331054735, 239825274387114265, '8幢-1302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22276, 1004140, 240111044331054735, 239825274387114266, '8幢-1303', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22277, 1004140, 240111044331054735, 239825274387114267, '8幢-1304', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22278, 1004140, 240111044331054735, 239825274387114268, '8幢-1305', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22279, 1004140, 240111044331054735, 239825274387114269, '8幢-1306', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22280, 1004140, 240111044331054735, 239825274387114270, '8幢-1401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22281, 1004140, 240111044331054735, 239825274387114271, '8幢-1402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22282, 1004140, 240111044331054735, 239825274387114272, '8幢-1403', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22283, 1004140, 240111044331054735, 239825274387114273, '8幢-1404', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22284, 1004140, 240111044331054735, 239825274387114274, '8幢-1405', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22285, 1004140, 240111044331054735, 239825274387114275, '8幢-1406', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22286, 1004140, 240111044331054735, 239825274387114276, '9幢-101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22287, 1004140, 240111044331054735, 239825274387114277, '9幢-102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22288, 1004140, 240111044331054735, 239825274387114278, '9幢-103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22289, 1004140, 240111044331054735, 239825274387114279, '9幢-104', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22290, 1004140, 240111044331054735, 239825274387114280, '9幢-201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22291, 1004140, 240111044331054735, 239825274387114281, '9幢-202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22292, 1004140, 240111044331054735, 239825274387114282, '9幢-203', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22293, 1004140, 240111044331054735, 239825274387114283, '9幢-204', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22294, 1004140, 240111044331054735, 239825274387114284, '9幢-205', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22295, 1004140, 240111044331054735, 239825274387114285, '9幢-301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22296, 1004140, 240111044331054735, 239825274387114286, '9幢-302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22297, 1004140, 240111044331054735, 239825274387114287, '9幢-303', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22298, 1004140, 240111044331054735, 239825274387114288, '9幢-304', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22299, 1004140, 240111044331054735, 239825274387114289, '9幢-305', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22300, 1004140, 240111044331054735, 239825274387114290, '9幢-306', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22301, 1004140, 240111044331054735, 239825274387114291, '9幢-401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22302, 1004140, 240111044331054735, 239825274387114292, '9幢-402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22303, 1004140, 240111044331054735, 239825274387114293, '9幢-403', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22304, 1004140, 240111044331054735, 239825274387114294, '9幢-404', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22305, 1004140, 240111044331054735, 239825274387114295, '9幢-405', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22306, 1004140, 240111044331054735, 239825274387114296, '9幢-406', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22307, 1004140, 240111044331054735, 239825274387114297, '9幢-501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22308, 1004140, 240111044331054735, 239825274387114298, '9幢-502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22309, 1004140, 240111044331054735, 239825274387114299, '9幢-503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22310, 1004140, 240111044331054735, 239825274387114300, '9幢-504', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22311, 1004140, 240111044331054735, 239825274387114301, '9幢-505', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22312, 1004140, 240111044331054735, 239825274387114302, '9幢-506', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22313, 1004140, 240111044331054735, 239825274387114303, '9幢-601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22314, 1004140, 240111044331054735, 239825274387114304, '9幢-602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22315, 1004140, 240111044331054735, 239825274387114305, '9幢-603', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22316, 1004140, 240111044331054735, 239825274387114306, '9幢-604', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22317, 1004140, 240111044331054735, 239825274387114307, '9幢-605', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22318, 1004140, 240111044331054735, 239825274387114308, '9幢-606', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22319, 1004140, 240111044331054735, 239825274387114309, '9幢-701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22320, 1004140, 240111044331054735, 239825274387114310, '9幢-702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22321, 1004140, 240111044331054735, 239825274387114311, '9幢-703', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22322, 1004140, 240111044331054735, 239825274387114312, '9幢-704', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22323, 1004140, 240111044331054735, 239825274387114313, '9幢-705', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22324, 1004140, 240111044331054735, 239825274387114314, '9幢-706', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22325, 1004140, 240111044331054735, 239825274387114315, '9幢-801', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22326, 1004140, 240111044331054735, 239825274387114316, '9幢-802', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22327, 1004140, 240111044331054735, 239825274387114317, '9幢-803', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22328, 1004140, 240111044331054735, 239825274387114318, '9幢-804', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22329, 1004140, 240111044331054735, 239825274387114319, '9幢-805', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (22330, 1004140, 240111044331054735, 239825274387114320, '9幢-806', '0');

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004539, UUID(), '南通崇川区布艺乐乎家纺设计工作室', '南通崇川区布艺乐乎家纺设计工作室', 1, 1, 1004541, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183003, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183003, UUID(), 999986, 2, 'EhGroups', 1004539,'南通崇川区布艺乐乎家纺设计工作室','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004541, 0, 'ENTERPRISE', '南通崇川区布艺乐乎家纺设计工作室', 0, '', '/1004541', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113412, 240111044331054735, 'organization', 1004541, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004540, UUID(), '南通泽阳智能科技有限公司', '南通泽阳智能科技有限公司', 1, 1, 1004542, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183004, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183004, UUID(), 999986, 2, 'EhGroups', 1004540,'南通泽阳智能科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004542, 0, 'ENTERPRISE', '南通泽阳智能科技有限公司', 0, '', '/1004542', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113413, 240111044331054735, 'organization', 1004542, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004541, UUID(), '南通华建智能科技有限公司', '南通华建智能科技有限公司', 1, 1, 1004543, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183005, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183005, UUID(), 999986, 2, 'EhGroups', 1004541,'南通华建智能科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004543, 0, 'ENTERPRISE', '南通华建智能科技有限公司', 0, '', '/1004543', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113414, 240111044331054735, 'organization', 1004543, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004542, UUID(), '南通新华传媒有限公司', '南通新华传媒有限公司', 1, 1, 1004544, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183006, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183006, UUID(), 999986, 2, 'EhGroups', 1004542,'南通新华传媒有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004544, 0, 'ENTERPRISE', '南通新华传媒有限公司', 0, '', '/1004544', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113415, 240111044331054735, 'organization', 1004544, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004543, UUID(), '江苏益美智能科技有限公司', '江苏益美智能科技有限公司', 1, 1, 1004545, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183007, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183007, UUID(), 999986, 2, 'EhGroups', 1004543,'江苏益美智能科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004545, 0, 'ENTERPRISE', '江苏益美智能科技有限公司', 0, '', '/1004545', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113416, 240111044331054735, 'organization', 1004545, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004544, UUID(), '江苏全通工程技术有限公司', '江苏全通工程技术有限公司', 1, 1, 1004546, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183008, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183008, UUID(), 999986, 2, 'EhGroups', 1004544,'江苏全通工程技术有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004546, 0, 'ENTERPRISE', '江苏全通工程技术有限公司', 0, '', '/1004546', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113417, 240111044331054735, 'organization', 1004546, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004545, UUID(), '上海泛微网络科技股份有限公司', '上海泛微网络科技股份有限公司', 1, 1, 1004547, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183009, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183009, UUID(), 999986, 2, 'EhGroups', 1004545,'上海泛微网络科技股份有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004547, 0, 'ENTERPRISE', '上海泛微网络科技股份有限公司', 0, '', '/1004547', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113418, 240111044331054735, 'organization', 1004547, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004546, UUID(), '南通车安电子科技有限公司', '南通车安电子科技有限公司', 1, 1, 1004548, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183010, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183010, UUID(), 999986, 2, 'EhGroups', 1004546,'南通车安电子科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004548, 0, 'ENTERPRISE', '南通车安电子科技有限公司', 0, '', '/1004548', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113419, 240111044331054735, 'organization', 1004548, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004547, UUID(), '中国民族建筑研究会建筑装饰研究院', '中国民族建筑研究会建筑装饰研究院', 1, 1, 1004549, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183011, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183011, UUID(), 999986, 2, 'EhGroups', 1004547,'中国民族建筑研究会建筑装饰研究院','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004549, 0, 'ENTERPRISE', '中国民族建筑研究会建筑装饰研究院', 0, '', '/1004549', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113420, 240111044331054735, 'organization', 1004549, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004548, UUID(), '南通智慧建筑产业研究院有限公司', '南通智慧建筑产业研究院有限公司', 1, 1, 1004550, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183012, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183012, UUID(), 999986, 2, 'EhGroups', 1004548,'南通智慧建筑产业研究院有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004550, 0, 'ENTERPRISE', '南通智慧建筑产业研究院有限公司', 0, '', '/1004550', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113421, 240111044331054735, 'organization', 1004550, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004549, UUID(), '江苏公证天业会计师事务所', '江苏公证天业会计师事务所', 1, 1, 1004551, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183013, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183013, UUID(), 999986, 2, 'EhGroups', 1004549,'江苏公证天业会计师事务所','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004551, 0, 'ENTERPRISE', '江苏公证天业会计师事务所', 0, '', '/1004551', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113422, 240111044331054735, 'organization', 1004551, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004550, UUID(), '南通天行健环境科技有限公司', '南通天行健环境科技有限公司', 1, 1, 1004552, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183014, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183014, UUID(), 999986, 2, 'EhGroups', 1004550,'南通天行健环境科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004552, 0, 'ENTERPRISE', '南通天行健环境科技有限公司', 0, '', '/1004552', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113423, 240111044331054735, 'organization', 1004552, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004551, UUID(), '南通欣诚教育软件有限公司', '南通欣诚教育软件有限公司', 1, 1, 1004553, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183015, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183015, UUID(), 999986, 2, 'EhGroups', 1004551,'南通欣诚教育软件有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004553, 0, 'ENTERPRISE', '南通欣诚教育软件有限公司', 0, '', '/1004553', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113424, 240111044331054735, 'organization', 1004553, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004552, UUID(), '江苏金土地房产评估测绘咨询有限公司', '江苏金土地房产评估测绘咨询有限公司', 1, 1, 1004554, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183016, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183016, UUID(), 999986, 2, 'EhGroups', 1004552,'江苏金土地房产评估测绘咨询有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004554, 0, 'ENTERPRISE', '江苏金土地房产评估测绘咨询有限公司', 0, '', '/1004554', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113425, 240111044331054735, 'organization', 1004554, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004553, UUID(), '南通三和市政水利建设工程有限公公司', '南通三和市政水利建设工程有限公公司', 1, 1, 1004555, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183017, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183017, UUID(), 999986, 2, 'EhGroups', 1004553,'南通三和市政水利建设工程有限公公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004555, 0, 'ENTERPRISE', '南通三和市政水利建设工程有限公公司', 0, '', '/1004555', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113426, 240111044331054735, 'organization', 1004555, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004554, UUID(), '南通禾墅装饰工程有限公司', '南通禾墅装饰工程有限公司', 1, 1, 1004556, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183018, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183018, UUID(), 999986, 2, 'EhGroups', 1004554,'南通禾墅装饰工程有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004556, 0, 'ENTERPRISE', '南通禾墅装饰工程有限公司', 0, '', '/1004556', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113427, 240111044331054735, 'organization', 1004556, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004555, UUID(), '南通黑白灰家纺设计有限公司', '南通黑白灰家纺设计有限公司', 1, 1, 1004557, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183019, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183019, UUID(), 999986, 2, 'EhGroups', 1004555,'南通黑白灰家纺设计有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004557, 0, 'ENTERPRISE', '南通黑白灰家纺设计有限公司', 0, '', '/1004557', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113428, 240111044331054735, 'organization', 1004557, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004556, UUID(), '南通杰思策划创意有限公司', '南通杰思策划创意有限公司', 1, 1, 1004558, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183020, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183020, UUID(), 999986, 2, 'EhGroups', 1004556,'南通杰思策划创意有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004558, 0, 'ENTERPRISE', '南通杰思策划创意有限公司', 0, '', '/1004558', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113429, 240111044331054735, 'organization', 1004558, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004557, UUID(), '南通睿祺智能科技有限公司', '南通睿祺智能科技有限公司', 1, 1, 1004559, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183021, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183021, UUID(), 999986, 2, 'EhGroups', 1004557,'南通睿祺智能科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004559, 0, 'ENTERPRISE', '南通睿祺智能科技有限公司', 0, '', '/1004559', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113430, 240111044331054735, 'organization', 1004559, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004558, UUID(), '江苏得得空间信息科技有限公司', '江苏得得空间信息科技有限公司', 1, 1, 1004560, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183022, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183022, UUID(), 999986, 2, 'EhGroups', 1004558,'江苏得得空间信息科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004560, 0, 'ENTERPRISE', '江苏得得空间信息科技有限公司', 0, '', '/1004560', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113431, 240111044331054735, 'organization', 1004560, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004559, UUID(), '南通云景信息科技有限公司', '南通云景信息科技有限公司', 1, 1, 1004561, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183023, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183023, UUID(), 999986, 2, 'EhGroups', 1004559,'南通云景信息科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004561, 0, 'ENTERPRISE', '南通云景信息科技有限公司', 0, '', '/1004561', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113432, 240111044331054735, 'organization', 1004561, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004560, UUID(), '南通信息化建设发展有限公司', '南通信息化建设发展有限公司', 1, 1, 1004562, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183024, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183024, UUID(), 999986, 2, 'EhGroups', 1004560,'南通信息化建设发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004562, 0, 'ENTERPRISE', '南通信息化建设发展有限公司', 0, '', '/1004562', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113433, 240111044331054735, 'organization', 1004562, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004561, UUID(), '南通绿业中试技术研究院有限公司', '南通绿业中试技术研究院有限公司', 1, 1, 1004563, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183025, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183025, UUID(), 999986, 2, 'EhGroups', 1004561,'南通绿业中试技术研究院有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004563, 0, 'ENTERPRISE', '南通绿业中试技术研究院有限公司', 0, '', '/1004563', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113434, 240111044331054735, 'organization', 1004563, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004562, UUID(), '江苏正远信息技术有限公司', '江苏正远信息技术有限公司', 1, 1, 1004564, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183026, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183026, UUID(), 999986, 2, 'EhGroups', 1004562,'江苏正远信息技术有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004564, 0, 'ENTERPRISE', '江苏正远信息技术有限公司', 0, '', '/1004564', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113435, 240111044331054735, 'organization', 1004564, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004563, UUID(), '江苏宁创智能科技有限公司', '江苏宁创智能科技有限公司', 1, 1, 1004565, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183027, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183027, UUID(), 999986, 2, 'EhGroups', 1004563,'江苏宁创智能科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004565, 0, 'ENTERPRISE', '江苏宁创智能科技有限公司', 0, '', '/1004565', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113436, 240111044331054735, 'organization', 1004565, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004564, UUID(), '南通信息化建设发展有限公司', '南通信息化建设发展有限公司', 1, 1, 1004566, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183028, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183028, UUID(), 999986, 2, 'EhGroups', 1004564,'南通信息化建设发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004566, 0, 'ENTERPRISE', '南通信息化建设发展有限公司', 0, '', '/1004566', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113437, 240111044331054735, 'organization', 1004566, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004565, UUID(), '上海路图信息科技有限公司', '上海路图信息科技有限公司', 1, 1, 1004567, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183029, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183029, UUID(), 999986, 2, 'EhGroups', 1004565,'上海路图信息科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004567, 0, 'ENTERPRISE', '上海路图信息科技有限公司', 0, '', '/1004567', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113438, 240111044331054735, 'organization', 1004567, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004566, UUID(), '江苏创时信息科技有限公司', '江苏创时信息科技有限公司', 1, 1, 1004568, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183030, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183030, UUID(), 999986, 2, 'EhGroups', 1004566,'江苏创时信息科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004568, 0, 'ENTERPRISE', '江苏创时信息科技有限公司', 0, '', '/1004568', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113439, 240111044331054735, 'organization', 1004568, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004567, UUID(), '南通市上午家纺设计有限公司', '南通市上午家纺设计有限公司', 1, 1, 1004569, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183031, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183031, UUID(), 999986, 2, 'EhGroups', 1004567,'南通市上午家纺设计有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004569, 0, 'ENTERPRISE', '南通市上午家纺设计有限公司', 0, '', '/1004569', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113440, 240111044331054735, 'organization', 1004569, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004568, UUID(), '上海讯生信息科技有限公司', '上海讯生信息科技有限公司', 1, 1, 1004570, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183032, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183032, UUID(), 999986, 2, 'EhGroups', 1004568,'上海讯生信息科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004570, 0, 'ENTERPRISE', '上海讯生信息科技有限公司', 0, '', '/1004570', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113441, 240111044331054735, 'organization', 1004570, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004569, UUID(), '南通江海大数据管理有限公司', '南通江海大数据管理有限公司', 1, 1, 1004571, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 183033, 1, 999986);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(183033, UUID(), 999986, 2, 'EhGroups', 1004569,'南通江海大数据管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`)
	VALUES(1004571, 0, 'ENTERPRISE', '南通江海大数据管理有限公司', 0, '', '/1004571', 1, 2, 'ENTERPRISE', 999986);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1113442, 240111044331054735, 'organization', 1004571, 3, 0, UTC_TIMESTAMP());


INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES (11797, 999986, 0, '/home', 'Default', '0', '0', 'innospring', 'innospring', 'cs://1/image/aW1hZ2UvTVRwbU5EbG1ZbVJpTVRBeU4yTmpZakk0Wm1aa05UWmtNRGczWXprME4ySTNNZw', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'park_tourist');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES (11798, 999986, 0, '/home', 'Default', '0', '0', 'innospring', 'innospring', 'cs://1/image/aW1hZ2UvTVRwaFptUTBPVEU1TkdFM05HUTRNakUzTlRJeE4yRTROV1UxT1RaaFpEVmhOUQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'park_tourist');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES (11799, 999986, 0, '/home', 'Default', '0', '0', 'innospring', 'innospring', 'cs://1/image/aW1hZ2UvTVRvMllqYzNOemMwTjJVeVpUUmlNVGhtTnpWaVpUYzBaR0U0TVdGaE9EVTNOZw', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'park_tourist');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES (11800, 999986, 0, '/home', 'Default', '0', '0', 'innospring', 'innospring', 'cs://1/image/aW1hZ2UvTVRwbU5EbG1ZbVJpTVRBeU4yTmpZakk0Wm1aa05UWmtNRGczWXprME4ySTNNZw', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'pm_admin');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES (11801, 999986, 0, '/home', 'Default', '0', '0', 'innospring', 'innospring', 'cs://1/image/aW1hZ2UvTVRwaFptUTBPVEU1TkdFM05HUTRNakUzTlRJeE4yRTROV1UxT1RaaFpEVmhOUQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'pm_admin');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES (11802, 999986, 0, '/home', 'Default', '0', '0', 'innospring', 'innospring', 'cs://1/image/aW1hZ2UvTVRvMllqYzNOemMwTjJVeVpUUmlNVGhtTnpWaVpUYzBaR0U0TVdGaE9EVTNOZw', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'pm_admin');

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES('10062','资源预订','0',NULL,'0','999986');

-- 园区管理员场景
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (390, 999986, 'ServiceMarketLayout', '{"versionCode":"2016092901","versionName":"3.10.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"News","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21}]}', '2016092901', '0', '2', '2016-09-29 17:02:30', 'pm_admin');

INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (391, 999986, 'aclinkAndpunchLayout', '{"versionCode":"2016092901","versionName":"3.10.0","layoutName":"aclinkAndpunchLayout","displayName":"门禁考勤","groups":[{"groupName":"门禁考勤Banner","widget":"Navigator","instanceConfig":{"itemGroup":"aclinkAndpunchBanner"},"style":"Gallery","defaultOrder":1,"separatorFlag":0,"separatorHeight":0,"columnCount": 1,"editFlag":0},{"groupName":"门禁考勤","widget":"Navigator","instanceConfig":{"itemGroup":"aclinkAndpunchBiz"},"style":"Gallery","defaultOrder":5,"separatorFlag":0,"separatorHeight":0,"columnCount":2,"editFlag":0}]}', '2016092901', '0', '2', '2016-09-29 17:02:30', 'pm_admin');

	
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112301, 999986, '0', '0', '0', '/home', 'Bizs', 'OFFICIAL_ACTIVITY', '园区活动', 'cs://1/image/aW1hZ2UvTVRvell6RXlNVEE0TjJNelpEVTFPREZsWTJKaVptVXdNRFZtWm1FNVlUWTRZZw', '1', '1', 50,'', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112302, 999986, '0', '0', '0', '/home', 'Bizs', '门禁考勤', '门禁考勤', 'cs://1/image/aW1hZ2UvTVRvell6RXlNVEE0TjJNelpEVTFPREZsWTJKaVptVXdNRFZtWm1FNVlUWTRZZw', '1', '1', 2,'{"itemLocation":"/home/aclinkAndpunch","layoutName":"aclinkAndpunchLayout"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');

INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (112303, 999986, 0, 0, 0, '/home/aclinkAndpunch', 'aclinkAndpunchBanner', '', '', 'cs://1/image/aW1hZ2UvTVRvME5qazBPV00yTVRKa1pXVTVZV1ZpWlROa1lqTmpZek0yTWpjMU5EWmxZZw', 1, 1, 0, NULl, 0, 0, 1, 1, '', 0, NULL, NULL, NULL, '0', 'pm_admin');

INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (112304, 999986, 0, 0, 0, '/home/aclinkAndpunch', 'aclinkAndpunchBiz', 'DoorManagement', '门禁', 'cs://1/image/aW1hZ2UvTVRvME5qazBPV00yTVRKa1pXVTVZV1ZpWlROa1lqTmpZek0yTWpjMU5EWmxZZw', 1, 1, 40, '', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (112305, 999986, 0, 0, 0, '/home/aclinkAndpunch', 'aclinkAndpunchBiz', 'PUNCH', '打卡考勤', 'cs://1/image/aW1hZ2UvTVRwa056WmlPRGc1WVRsbE9EWTFaRFpoTkRVM1ptUXpPR0l5WmpSbFltSXdZdw', '1', '1', '23', '', 0, 0, 1, 1, '', '0', NULL, NULL, NULL, '0', 'pm_admin');


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112306, 999986, '0', '0', '0', '/home', 'Bizs', 'CONTACTS', '园区通讯录', 'cs://1/image/aW1hZ2UvTVRveU1USTNZMkk0WkdNME1EbGpaRFJoWkRFM01XWTVOelF3TTJSbU1tSm1aZw', '1', '1', '46', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112307, 999986, 0, 0, 0, '/home', 'Bizs', 'RENTAL', '公共资源预订', 'cs://1/image/aW1hZ2UvTVRwa01EZGtZMlJtWmpWak0yUTNZekkxTVRSaFpEVTBORGRrTWpFNE5tWTVNQQ', 1, 1, 49,'{\"resourceTypeId\":10062,\"pageType\":0}', 0, 0, 1, 1, '', '0', NULL, NULL, NULL, '1', 'pm_admin');        

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112308, 999986, '0', '0', '0', '/home', 'Bizs', 'VIDEO_MEETING', '视频会议', 'cs://1/image/aW1hZ2UvTVRveE5UVmxZV1JrWkRaa09ESmpZak13TVRJd09UQm1PR00wWlROaE16QmpOdw', '1', '1', '27', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112309, 999986, '0', '0', '0', '/home', 'Bizs', '更多服务', '更多服务', 'cs://1/image/aW1hZ2UvTVRwa00yVmlOVFl6TmpRM01EQTFabU5pT1dKbU5Ua3lZalZoTVdVeU5qWm1NZw', '1', '1', '1', '{\"itemLocation\":\"/home\", \"itemGroup\":\"Bizs\"}', '30', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');



-- 园区游客场景    
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (390, 999986, 'ServiceMarketLayout', '{"versionCode":"2016092901","versionName":"3.10.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"News","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21}]}', '2016092901', '0', '2', '2016-09-29 17:02:30', 'park_tourist');

INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (391, 999986, 'aclinkAndpunchLayout', '{"versionCode":"2016092901","versionName":"3.10.0","layoutName":"aclinkAndpunchLayout","displayName":"门禁考勤","groups":[{"groupName":"门禁考勤Banner","widget":"Navigator","instanceConfig":{"itemGroup":"aclinkAndpunchBanner"},"style":"Gallery","defaultOrder":1,"separatorFlag":0,"separatorHeight":0,"columnCount": 1,"editFlag":0},{"groupName":"门禁考勤","widget":"Navigator","instanceConfig":{"itemGroup":"aclinkAndpunchBiz"},"style":"Gallery","defaultOrder":5,"separatorFlag":0,"separatorHeight":0,"columnCount":2,"editFlag":0}]}', '2016092901', '0', '2', '2016-09-29 17:02:30', 'park_tourist');

	
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112301, 999986, '0', '0', '0', '/home', 'Bizs', 'OFFICIAL_ACTIVITY', '园区活动', 'cs://1/image/aW1hZ2UvTVRvell6RXlNVEE0TjJNelpEVTFPREZsWTJKaVptVXdNRFZtWm1FNVlUWTRZZw', '1', '1', 50,'', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112302, 999986, '0', '0', '0', '/home', 'Bizs', '门禁考勤', '门禁考勤', 'cs://1/image/aW1hZ2UvTVRvell6RXlNVEE0TjJNelpEVTFPREZsWTJKaVptVXdNRFZtWm1FNVlUWTRZZw', '1', '1', 2,'{"itemLocation":"/home/aclinkAndpunch","layoutName":"aclinkAndpunchLayout"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');

INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (112303, 999986, 0, 0, 0, '/home/aclinkAndpunch', 'aclinkAndpunchBanner', '', '', 'cs://1/image/aW1hZ2UvTVRvME5qazBPV00yTVRKa1pXVTVZV1ZpWlROa1lqTmpZek0yTWpjMU5EWmxZZw', 1, 1, 0, NULl, 0, 0, 1, 1, '', 0, NULL, NULL, NULL, '0', 'park_tourist');

INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (112304, 999986, 0, 0, 0, '/home/aclinkAndpunch', 'aclinkAndpunchBiz', 'DoorManagement', '门禁', 'cs://1/image/aW1hZ2UvTVRvME5qazBPV00yTVRKa1pXVTVZV1ZpWlROa1lqTmpZek0yTWpjMU5EWmxZZw', 1, 1, 40, '', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (112305, 999986, 0, 0, 0, '/home/aclinkAndpunch', 'aclinkAndpunchBiz', 'PUNCH', '打卡考勤', 'cs://1/image/aW1hZ2UvTVRwa056WmlPRGc1WVRsbE9EWTFaRFpoTkRVM1ptUXpPR0l5WmpSbFltSXdZdw', '1', '1', '23', '', 0, 0, 1, 1, '', '0', NULL, NULL, NULL, '0', 'park_tourist');


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112306, 999986, '0', '0', '0', '/home', 'Bizs', 'CONTACTS', '园区通讯录', 'cs://1/image/aW1hZ2UvTVRveU1USTNZMkk0WkdNME1EbGpaRFJoWkRFM01XWTVOelF3TTJSbU1tSm1aZw', '1', '1', '46', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112307, 999986, 0, 0, 0, '/home', 'Bizs', 'RENTAL', '公共资源预订', 'cs://1/image/aW1hZ2UvTVRwa01EZGtZMlJtWmpWak0yUTNZekkxTVRSaFpEVTBORGRrTWpFNE5tWTVNQQ', 1, 1, 49,'{\"resourceTypeId\":10062,\"pageType\":0}', 0, 0, 1, 1, '', '0', NULL, NULL, NULL, '1', 'park_tourist');        

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112308, 999986, '0', '0', '0', '/home', 'Bizs', 'VIDEO_MEETING', '视频会议', 'cs://1/image/aW1hZ2UvTVRveE5UVmxZV1JrWkRaa09ESmpZak13TVRJd09UQm1PR00wWlROaE16QmpOdw', '1', '1', '27', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112309, 999986, '0', '0', '0', '/home', 'Bizs', '更多服务', '更多服务', 'cs://1/image/aW1hZ2UvTVRwa00yVmlOVFl6TmpRM01EQTFabU5pT1dKbU5Ua3lZalZoTVdVeU5qWm1NZw', '1', '1', '1', '{\"itemLocation\":\"/home\", \"itemGroup\":\"Bizs\"}', '30', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');




	
	
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

















