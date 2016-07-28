
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (225498, UUID(), '9201903', '喻超文', 'cs://1/image/aW1hZ2UvTVRvMlkySmhNbVZqTm1SaU1UQXdPREkxWkRjME5HVmxNVFU1TXpBNE5UUTBZdw', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 0);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (223765, 225498,  '0',  '13430865789',  '221616',  3, UTC_TIMESTAMP(), 0);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (225499, UUID(), '9201904', '孔静', 'cs://1/image/aW1hZ2UvTVRvMlkySmhNbVZqTm1SaU1UQXdPREkxWkRjME5HVmxNVFU1TXpBNE5UUTBZdw', 1, 45, '1', '2',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 0);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (223766, 225499,  '0',  '13798499059',  '221616',  3, UTC_TIMESTAMP(), 0);
    
       
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES( 240111044331053516, UUID(), 13905, '深圳市',  13909, '宝安区', '东方建富大厦', '东方建富大厦', '深圳市宝安区宝安47区自由路东方建富大厦', NULL, '东方建富大厦，屹立于深圳市宝安中心区，以东方建富大厦为商业核心聚点，与天虹商场、旺轩酒楼、宝晖商务酒店、新一佳商场等构成的集购物、娱乐、餐饮、酒店等繁华商业圈，具有独特优越的商业发展潜力。大厦集办公、酒店、会展、娱乐等多功能于一体，其人性化、智能化的设计足以体现现代化商业大厦特质，东方建富实业有限公司总部设于大厦七楼。', NULL, NULL, NULL, NULL, NULL, NULL,NULL, 44, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'1', 1, 2, UTC_TIMESTAMP(), 0);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES(240111044331049116, 240111044331053516, '', 113.896821, 22.574873, 'ws0brv9hhm34');	
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(1002756, 240111044331053516);


    
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1003092, UUID(), 'ibase', '东方建富', 1, 1, 1002756, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 180771, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(180771, UUID(), 0, 2, 'EhGroups', 1003092,'东方建富','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());       

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(1002756, 0, 'PM', '深圳市东方建富实业有限公司', '', '/1002756', 1, 2, 'ENTERPRISE', 0, 1003092);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1111160, 240111044331053516, 'organization', 1002756, 3, 0, UTC_TIMESTAMP());    
  
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status)
	VALUES(2105190, 1002756, 'USER', 225498, 'manager', '喻超文', 0, '13430865789', 3);	
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status)
	VALUES(2105191, 1002756, 'USER', 225499, 'manager', '孔静', 0, '13798499059', 3);	


INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(10993, 'EhOrganizations', 1002756, 'EhUsers', 225498, 1001, 1, UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(10994, 'EhOrganizations', 1002756, 'EhUsers', 225499, 1001, 1, UTC_TIMESTAMP());

  
INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES(1224, 0, 'COMMUNITY', 240111044331053516, UTC_TIMESTAMP());

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
	VALUES(179053, 240111044331053516, '东方建富大厦1栋', '东方建富大厦1栋', manager_uid, '15818689633', '宝安47区自由路东方建富大厦', 12500, NULL, NULL, NULL, '东方建富大厦，屹立于深圳市宝安中心区，以东方建富大厦为商业核心聚点，与天虹商场、旺轩酒楼、宝晖商务酒店、新一佳商场等构成的集购物、娱乐、餐饮、酒店等繁华商业圈，具有独特优越的商业发展潜力。大厦集办公、酒店、会展、娱乐等多功能于一体，其人性化、智能化的设计足以体现现代化商业大厦特质，东方建富实业有限公司总部设于大厦七楼。', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
	VALUES(179054, 240111044331053516, '东方建富大厦2栋', '东方建富大厦2栋', manager_uid, '15818689633', '宝安47区自由路东方建富大厦', 12500, NULL, NULL, NULL, '东方建富大厦，屹立于深圳市宝安中心区，以东方建富大厦为商业核心聚点，与天虹商场、旺轩酒楼、宝晖商务酒店、新一佳商场等构成的集购物、娱乐、餐饮、酒店等繁华商业圈，具有独特优越的商业发展潜力。大厦集办公、酒店、会展、娱乐等多功能于一体，其人性化、智能化的设计足以体现现代化商业大厦特质，东方建富实业有限公司总部设于大厦七楼。', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
	VALUES(179055, 240111044331053516, '东方建富大厦3栋', '东方建富大厦3栋', manager_uid, '15818689633', '宝安47区自由路东方建富大厦', 12500, NULL, NULL, NULL, '东方建富大厦，屹立于深圳市宝安中心区，以东方建富大厦为商业核心聚点，与天虹商场、旺轩酒楼、宝晖商务酒店、新一佳商场等构成的集购物、娱乐、餐饮、酒店等繁华商业圈，具有独特优越的商业发展潜力。大厦集办公、酒店、会展、娱乐等多功能于一体，其人性化、智能化的设计足以体现现代化商业大厦特质，东方建富实业有限公司总部设于大厦七楼。', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
    
  
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101368,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-301','东方建富大厦1栋','301','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101369,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-302','东方建富大厦1栋','302','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101370,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-303','东方建富大厦1栋','303','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101371,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-304','东方建富大厦1栋','304','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101372,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-305','东方建富大厦1栋','305','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101373,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-306','东方建富大厦1栋','306','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101374,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-307','东方建富大厦1栋','307','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101375,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-308','东方建富大厦1栋','308','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101376,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-309','东方建富大厦1栋','309','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101377,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-310','东方建富大厦1栋','310','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101378,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-311','东方建富大厦1栋','311','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101379,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-312','东方建富大厦1栋','312','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101380,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-313','东方建富大厦1栋','313','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101381,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-314','东方建富大厦1栋','314','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101382,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-315','东方建富大厦1栋','315','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101383,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-316','东方建富大厦1栋','316','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101384,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-317','东方建富大厦1栋','317','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101385,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-401','东方建富大厦1栋','401','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101386,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-402','东方建富大厦1栋','402','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101387,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-403','东方建富大厦1栋','403','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101388,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-404','东方建富大厦1栋','404','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101389,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-405','东方建富大厦1栋','405','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101390,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-406','东方建富大厦1栋','406','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101391,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-407','东方建富大厦1栋','407','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101392,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-408','东方建富大厦1栋','408','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101393,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-409','东方建富大厦1栋','409','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101394,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-410','东方建富大厦1栋','410','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101395,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-411','东方建富大厦1栋','411','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101396,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-412','东方建富大厦1栋','412','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101397,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-413','东方建富大厦1栋','413','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101398,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-414','东方建富大厦1栋','414','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101399,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-415','东方建富大厦1栋','415','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101400,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-416','东方建富大厦1栋','416','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101401,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-417','东方建富大厦1栋','417','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101402,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-418','东方建富大厦1栋','418','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101403,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-501','东方建富大厦1栋','501','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101404,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-502','东方建富大厦1栋','502','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101405,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-503','东方建富大厦1栋','503','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101406,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-504','东方建富大厦1栋','504','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101407,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-505','东方建富大厦1栋','505','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101408,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-506','东方建富大厦1栋','506','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101409,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-507','东方建富大厦1栋','507','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101410,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-508','东方建富大厦1栋','508','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101411,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-509','东方建富大厦1栋','509','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101412,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-510','东方建富大厦1栋','510','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101413,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-511','东方建富大厦1栋','511','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101414,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-512','东方建富大厦1栋','512','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101415,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-513','东方建富大厦1栋','513','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101416,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-514','东方建富大厦1栋','514','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101417,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-515','东方建富大厦1栋','515','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101418,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-516','东方建富大厦1栋','516','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101419,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-517','东方建富大厦1栋','517','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101420,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-518','东方建富大厦1栋','518','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101421,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-601','东方建富大厦1栋','601','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101422,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-602','东方建富大厦1栋','602','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101423,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-603','东方建富大厦1栋','603','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101424,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-604','东方建富大厦1栋','604','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101425,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-605','东方建富大厦1栋','605','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101426,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-606','东方建富大厦1栋','606','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101427,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-607','东方建富大厦1栋','607','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101428,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-608','东方建富大厦1栋','608','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101429,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-609','东方建富大厦1栋','609','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101430,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-610','东方建富大厦1栋','610','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101431,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-611','东方建富大厦1栋','611','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101432,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-612','东方建富大厦1栋','612','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101433,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-613','东方建富大厦1栋','613','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101434,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-614','东方建富大厦1栋','614','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101435,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-615','东方建富大厦1栋','615','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101436,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-616','东方建富大厦1栋','616','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101437,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-617','东方建富大厦1栋','617','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101438,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦1栋-618','东方建富大厦1栋','618','2','0',UTC_TIMESTAMP(), 0);	


INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101439,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-301','东方建富大厦2栋','301','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101440,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-302','东方建富大厦2栋','302','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101441,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-303','东方建富大厦2栋','303','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101442,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-304','东方建富大厦2栋','304','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101443,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-401','东方建富大厦2栋','401','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101444,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-402','东方建富大厦2栋','402','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101445,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-403','东方建富大厦2栋','403','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101446,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-404','东方建富大厦2栋','404','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101447,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-501','东方建富大厦2栋','501','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101448,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-502','东方建富大厦2栋','502','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101449,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-503','东方建富大厦2栋','503','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101450,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-504','东方建富大厦2栋','504','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101451,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-601','东方建富大厦2栋','601','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101452,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-602','东方建富大厦2栋','602','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101453,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-603','东方建富大厦2栋','603','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101454,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-604','东方建富大厦2栋','604','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101455,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-701','东方建富大厦2栋','701','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101456,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-702','东方建富大厦2栋','702','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101457,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-703','东方建富大厦2栋','703','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101458,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-704','东方建富大厦2栋','704','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101459,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-F601','东方建富大厦2栋','F601','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101460,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-F602','东方建富大厦2栋','F602','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101461,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-F603','东方建富大厦2栋','F603','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101462,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-F604','东方建富大厦2栋','F604','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101463,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-F605','东方建富大厦2栋','F605','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101464,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-F606','东方建富大厦2栋','F606','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101465,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-F607','东方建富大厦2栋','F607','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101466,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-F701','东方建富大厦2栋','F701','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101467,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-F702','东方建富大厦2栋','F702','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101468,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-F703','东方建富大厦2栋','F703','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101469,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-F704','东方建富大厦2栋','F704','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101470,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-F705','东方建富大厦2栋','F705','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101471,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-F706','东方建富大厦2栋','F706','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101472,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-F707','东方建富大厦2栋','F707','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101473,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-F708','东方建富大厦2栋','F708','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101474,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-F709','东方建富大厦2栋','F709','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101475,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-F710','东方建富大厦2栋','F710','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101476,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-F711','东方建富大厦2栋','F711','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101477,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-F712','东方建富大厦2栋','F712','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101478,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-F713','东方建富大厦2栋','F713','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101479,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦2栋-F714','东方建富大厦2栋','F714','2','0',UTC_TIMESTAMP(), 0);	



INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101480,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-301','东方建富大厦3栋','301','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101481,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-302','东方建富大厦3栋','302','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101482,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-303','东方建富大厦3栋','303','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101483,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-304','东方建富大厦3栋','304','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101484,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-305','东方建富大厦3栋','305','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101485,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-306','东方建富大厦3栋','306','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101486,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-307','东方建富大厦3栋','307','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101487,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-308','东方建富大厦3栋','308','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101488,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-309','东方建富大厦3栋','309','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101489,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-310','东方建富大厦3栋','310','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101490,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-311','东方建富大厦3栋','311','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101491,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-312','东方建富大厦3栋','312','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101492,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-401','东方建富大厦3栋','401','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101493,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-402','东方建富大厦3栋','402','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101494,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-403','东方建富大厦3栋','403','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101495,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-404','东方建富大厦3栋','404','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101496,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-405','东方建富大厦3栋','405','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101497,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-406','东方建富大厦3栋','406','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101498,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-407','东方建富大厦3栋','407','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101499,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-408','东方建富大厦3栋','408','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101500,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-409','东方建富大厦3栋','409','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101501,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-410','东方建富大厦3栋','410','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101502,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-411','东方建富大厦3栋','411','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101503,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-412','东方建富大厦3栋','412','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101504,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-413','东方建富大厦3栋','413','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101505,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-414','东方建富大厦3栋','414','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101506,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-501','东方建富大厦3栋','501','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101507,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-502','东方建富大厦3栋','502','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101508,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-503','东方建富大厦3栋','503','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101509,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-504','东方建富大厦3栋','504','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101510,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-505','东方建富大厦3栋','505','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101511,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-506','东方建富大厦3栋','506','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101512,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-507','东方建富大厦3栋','507','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101513,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-508','东方建富大厦3栋','508','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101514,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-509','东方建富大厦3栋','509','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101515,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-510','东方建富大厦3栋','510','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101516,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-511','东方建富大厦3栋','511','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101517,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-512','东方建富大厦3栋','512','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101518,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-513','东方建富大厦3栋','513','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101519,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-514','东方建富大厦3栋','514','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101520,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-601','东方建富大厦3栋','601','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101521,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-602','东方建富大厦3栋','602','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101522,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-603','东方建富大厦3栋','603','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101523,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-604','东方建富大厦3栋','604','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101524,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-605','东方建富大厦3栋','605','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101525,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-606','东方建富大厦3栋','606','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101526,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-607','东方建富大厦3栋','607','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101527,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-608','东方建富大厦3栋','608','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101528,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-609','东方建富大厦3栋','609','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101529,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-610','东方建富大厦3栋','610','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101530,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-611','东方建富大厦3栋','611','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101531,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-612','东方建富大厦3栋','612','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101532,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-613','东方建富大厦3栋','613','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101533,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-614','东方建富大厦3栋','614','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101534,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-701','东方建富大厦3栋','701','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101535,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-702','东方建富大厦3栋','702','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101536,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-703','东方建富大厦3栋','703','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101537,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-704','东方建富大厦3栋','704','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101538,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-705','东方建富大厦3栋','705','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101539,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-706','东方建富大厦3栋','706','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101540,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-707','东方建富大厦3栋','707','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101541,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-708','东方建富大厦3栋','708','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101542,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-709','东方建富大厦3栋','709','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101543,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-710','东方建富大厦3栋','710','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101544,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-711','东方建富大厦3栋','711','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101545,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-712','东方建富大厦3栋','712','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101546,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-713','东方建富大厦3栋','713','2','0',UTC_TIMESTAMP(), 0);	
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`) 
VALUES(239825274387101547,UUID(),240111044331053516, 13905, '深圳市',  13909, '宝安区' ,'东方建富大厦3栋-714','东方建富大厦3栋','714','2','0',UTC_TIMESTAMP(), 0);	




INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19161, 1002756, 240111044331053516, 239825274387101368, '东方建富大厦1栋-301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19162, 1002756, 240111044331053516, 239825274387101369, '东方建富大厦1栋-302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19163, 1002756, 240111044331053516, 239825274387101370, '东方建富大厦1栋-303', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19164, 1002756, 240111044331053516, 239825274387101371, '东方建富大厦1栋-304', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19165, 1002756, 240111044331053516, 239825274387101372, '东方建富大厦1栋-305', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19166, 1002756, 240111044331053516, 239825274387101373, '东方建富大厦1栋-306', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19167, 1002756, 240111044331053516, 239825274387101374, '东方建富大厦1栋-307', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19168, 1002756, 240111044331053516, 239825274387101375, '东方建富大厦1栋-308', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19169, 1002756, 240111044331053516, 239825274387101376, '东方建富大厦1栋-309', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19170, 1002756, 240111044331053516, 239825274387101377, '东方建富大厦1栋-310', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19171, 1002756, 240111044331053516, 239825274387101378, '东方建富大厦1栋-311', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19172, 1002756, 240111044331053516, 239825274387101379, '东方建富大厦1栋-312', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19173, 1002756, 240111044331053516, 239825274387101380, '东方建富大厦1栋-313', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19174, 1002756, 240111044331053516, 239825274387101381, '东方建富大厦1栋-314', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19175, 1002756, 240111044331053516, 239825274387101382, '东方建富大厦1栋-315', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19176, 1002756, 240111044331053516, 239825274387101383, '东方建富大厦1栋-316', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19177, 1002756, 240111044331053516, 239825274387101384, '东方建富大厦1栋-317', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19178, 1002756, 240111044331053516, 239825274387101385, '东方建富大厦1栋-401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19179, 1002756, 240111044331053516, 239825274387101386, '东方建富大厦1栋-402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19180, 1002756, 240111044331053516, 239825274387101387, '东方建富大厦1栋-403', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19181, 1002756, 240111044331053516, 239825274387101388, '东方建富大厦1栋-404', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19182, 1002756, 240111044331053516, 239825274387101389, '东方建富大厦1栋-405', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19183, 1002756, 240111044331053516, 239825274387101390, '东方建富大厦1栋-406', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19184, 1002756, 240111044331053516, 239825274387101391, '东方建富大厦1栋-407', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19185, 1002756, 240111044331053516, 239825274387101392, '东方建富大厦1栋-408', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19186, 1002756, 240111044331053516, 239825274387101393, '东方建富大厦1栋-409', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19187, 1002756, 240111044331053516, 239825274387101394, '东方建富大厦1栋-410', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19188, 1002756, 240111044331053516, 239825274387101395, '东方建富大厦1栋-411', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19189, 1002756, 240111044331053516, 239825274387101396, '东方建富大厦1栋-412', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19190, 1002756, 240111044331053516, 239825274387101397, '东方建富大厦1栋-413', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19191, 1002756, 240111044331053516, 239825274387101398, '东方建富大厦1栋-414', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19192, 1002756, 240111044331053516, 239825274387101399, '东方建富大厦1栋-415', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19193, 1002756, 240111044331053516, 239825274387101400, '东方建富大厦1栋-416', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19194, 1002756, 240111044331053516, 239825274387101401, '东方建富大厦1栋-417', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19195, 1002756, 240111044331053516, 239825274387101402, '东方建富大厦1栋-418', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19196, 1002756, 240111044331053516, 239825274387101403, '东方建富大厦1栋-501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19197, 1002756, 240111044331053516, 239825274387101404, '东方建富大厦1栋-502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19198, 1002756, 240111044331053516, 239825274387101405, '东方建富大厦1栋-503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19199, 1002756, 240111044331053516, 239825274387101406, '东方建富大厦1栋-504', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19200, 1002756, 240111044331053516, 239825274387101407, '东方建富大厦1栋-505', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19201, 1002756, 240111044331053516, 239825274387101408, '东方建富大厦1栋-506', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19202, 1002756, 240111044331053516, 239825274387101409, '东方建富大厦1栋-507', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19203, 1002756, 240111044331053516, 239825274387101410, '东方建富大厦1栋-508', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19204, 1002756, 240111044331053516, 239825274387101411, '东方建富大厦1栋-509', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19205, 1002756, 240111044331053516, 239825274387101412, '东方建富大厦1栋-510', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19206, 1002756, 240111044331053516, 239825274387101413, '东方建富大厦1栋-511', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19207, 1002756, 240111044331053516, 239825274387101414, '东方建富大厦1栋-512', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19208, 1002756, 240111044331053516, 239825274387101415, '东方建富大厦1栋-513', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19209, 1002756, 240111044331053516, 239825274387101416, '东方建富大厦1栋-514', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19210, 1002756, 240111044331053516, 239825274387101417, '东方建富大厦1栋-515', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19211, 1002756, 240111044331053516, 239825274387101418, '东方建富大厦1栋-516', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19212, 1002756, 240111044331053516, 239825274387101419, '东方建富大厦1栋-517', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19213, 1002756, 240111044331053516, 239825274387101420, '东方建富大厦1栋-518', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19214, 1002756, 240111044331053516, 239825274387101421, '东方建富大厦1栋-601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19215, 1002756, 240111044331053516, 239825274387101422, '东方建富大厦1栋-602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19216, 1002756, 240111044331053516, 239825274387101423, '东方建富大厦1栋-603', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19217, 1002756, 240111044331053516, 239825274387101424, '东方建富大厦1栋-604', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19218, 1002756, 240111044331053516, 239825274387101425, '东方建富大厦1栋-605', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19219, 1002756, 240111044331053516, 239825274387101426, '东方建富大厦1栋-606', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19220, 1002756, 240111044331053516, 239825274387101427, '东方建富大厦1栋-607', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19221, 1002756, 240111044331053516, 239825274387101428, '东方建富大厦1栋-608', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19222, 1002756, 240111044331053516, 239825274387101429, '东方建富大厦1栋-609', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19223, 1002756, 240111044331053516, 239825274387101430, '东方建富大厦1栋-610', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19224, 1002756, 240111044331053516, 239825274387101431, '东方建富大厦1栋-611', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19225, 1002756, 240111044331053516, 239825274387101432, '东方建富大厦1栋-612', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19226, 1002756, 240111044331053516, 239825274387101433, '东方建富大厦1栋-613', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19227, 1002756, 240111044331053516, 239825274387101434, '东方建富大厦1栋-614', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19228, 1002756, 240111044331053516, 239825274387101435, '东方建富大厦1栋-615', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19229, 1002756, 240111044331053516, 239825274387101436, '东方建富大厦1栋-616', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19230, 1002756, 240111044331053516, 239825274387101437, '东方建富大厦1栋-617', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19231, 1002756, 240111044331053516, 239825274387101438, '东方建富大厦1栋-618', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19232, 1002756, 240111044331053516, 239825274387101439, '东方建富大厦2栋-301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19233, 1002756, 240111044331053516, 239825274387101440, '东方建富大厦2栋-302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19234, 1002756, 240111044331053516, 239825274387101441, '东方建富大厦2栋-303', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19235, 1002756, 240111044331053516, 239825274387101442, '东方建富大厦2栋-304', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19236, 1002756, 240111044331053516, 239825274387101443, '东方建富大厦2栋-401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19237, 1002756, 240111044331053516, 239825274387101444, '东方建富大厦2栋-402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19238, 1002756, 240111044331053516, 239825274387101445, '东方建富大厦2栋-403', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19239, 1002756, 240111044331053516, 239825274387101446, '东方建富大厦2栋-404', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19240, 1002756, 240111044331053516, 239825274387101447, '东方建富大厦2栋-501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19241, 1002756, 240111044331053516, 239825274387101448, '东方建富大厦2栋-502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19242, 1002756, 240111044331053516, 239825274387101449, '东方建富大厦2栋-503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19243, 1002756, 240111044331053516, 239825274387101450, '东方建富大厦2栋-504', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19244, 1002756, 240111044331053516, 239825274387101451, '东方建富大厦2栋-601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19245, 1002756, 240111044331053516, 239825274387101452, '东方建富大厦2栋-602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19246, 1002756, 240111044331053516, 239825274387101453, '东方建富大厦2栋-603', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19247, 1002756, 240111044331053516, 239825274387101454, '东方建富大厦2栋-604', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19248, 1002756, 240111044331053516, 239825274387101455, '东方建富大厦2栋-701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19249, 1002756, 240111044331053516, 239825274387101456, '东方建富大厦2栋-702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19250, 1002756, 240111044331053516, 239825274387101457, '东方建富大厦2栋-703', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19251, 1002756, 240111044331053516, 239825274387101458, '东方建富大厦2栋-704', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19252, 1002756, 240111044331053516, 239825274387101459, '东方建富大厦2栋-F601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19253, 1002756, 240111044331053516, 239825274387101460, '东方建富大厦2栋-F602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19254, 1002756, 240111044331053516, 239825274387101461, '东方建富大厦2栋-F603', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19255, 1002756, 240111044331053516, 239825274387101462, '东方建富大厦2栋-F604', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19256, 1002756, 240111044331053516, 239825274387101463, '东方建富大厦2栋-F605', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19257, 1002756, 240111044331053516, 239825274387101464, '东方建富大厦2栋-F606', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19258, 1002756, 240111044331053516, 239825274387101465, '东方建富大厦2栋-F607', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19259, 1002756, 240111044331053516, 239825274387101466, '东方建富大厦2栋-F701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19260, 1002756, 240111044331053516, 239825274387101467, '东方建富大厦2栋-F702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19261, 1002756, 240111044331053516, 239825274387101468, '东方建富大厦2栋-F703', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19262, 1002756, 240111044331053516, 239825274387101469, '东方建富大厦2栋-F704', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19263, 1002756, 240111044331053516, 239825274387101470, '东方建富大厦2栋-F705', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19264, 1002756, 240111044331053516, 239825274387101471, '东方建富大厦2栋-F706', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19265, 1002756, 240111044331053516, 239825274387101472, '东方建富大厦2栋-F707', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19266, 1002756, 240111044331053516, 239825274387101473, '东方建富大厦2栋-F708', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19267, 1002756, 240111044331053516, 239825274387101474, '东方建富大厦2栋-F709', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19268, 1002756, 240111044331053516, 239825274387101475, '东方建富大厦2栋-F710', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19269, 1002756, 240111044331053516, 239825274387101476, '东方建富大厦2栋-F711', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19270, 1002756, 240111044331053516, 239825274387101477, '东方建富大厦2栋-F712', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19271, 1002756, 240111044331053516, 239825274387101478, '东方建富大厦2栋-F713', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19272, 1002756, 240111044331053516, 239825274387101479, '东方建富大厦2栋-F714', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19273, 1002756, 240111044331053516, 239825274387101480, '东方建富大厦3栋-301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19274, 1002756, 240111044331053516, 239825274387101481, '东方建富大厦3栋-302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19275, 1002756, 240111044331053516, 239825274387101482, '东方建富大厦3栋-303', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19276, 1002756, 240111044331053516, 239825274387101483, '东方建富大厦3栋-304', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19277, 1002756, 240111044331053516, 239825274387101484, '东方建富大厦3栋-305', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19278, 1002756, 240111044331053516, 239825274387101485, '东方建富大厦3栋-306', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19279, 1002756, 240111044331053516, 239825274387101486, '东方建富大厦3栋-307', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19280, 1002756, 240111044331053516, 239825274387101487, '东方建富大厦3栋-308', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19281, 1002756, 240111044331053516, 239825274387101488, '东方建富大厦3栋-309', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19282, 1002756, 240111044331053516, 239825274387101489, '东方建富大厦3栋-310', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19283, 1002756, 240111044331053516, 239825274387101490, '东方建富大厦3栋-311', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19284, 1002756, 240111044331053516, 239825274387101491, '东方建富大厦3栋-312', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19285, 1002756, 240111044331053516, 239825274387101492, '东方建富大厦3栋-401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19286, 1002756, 240111044331053516, 239825274387101493, '东方建富大厦3栋-402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19287, 1002756, 240111044331053516, 239825274387101494, '东方建富大厦3栋-403', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19288, 1002756, 240111044331053516, 239825274387101495, '东方建富大厦3栋-404', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19289, 1002756, 240111044331053516, 239825274387101496, '东方建富大厦3栋-405', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19290, 1002756, 240111044331053516, 239825274387101497, '东方建富大厦3栋-406', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19291, 1002756, 240111044331053516, 239825274387101498, '东方建富大厦3栋-407', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19292, 1002756, 240111044331053516, 239825274387101499, '东方建富大厦3栋-408', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19293, 1002756, 240111044331053516, 239825274387101500, '东方建富大厦3栋-409', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19294, 1002756, 240111044331053516, 239825274387101501, '东方建富大厦3栋-410', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19295, 1002756, 240111044331053516, 239825274387101502, '东方建富大厦3栋-411', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19296, 1002756, 240111044331053516, 239825274387101503, '东方建富大厦3栋-412', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19297, 1002756, 240111044331053516, 239825274387101504, '东方建富大厦3栋-413', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19298, 1002756, 240111044331053516, 239825274387101505, '东方建富大厦3栋-414', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19299, 1002756, 240111044331053516, 239825274387101506, '东方建富大厦3栋-501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19300, 1002756, 240111044331053516, 239825274387101507, '东方建富大厦3栋-502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19301, 1002756, 240111044331053516, 239825274387101508, '东方建富大厦3栋-503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19302, 1002756, 240111044331053516, 239825274387101509, '东方建富大厦3栋-504', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19303, 1002756, 240111044331053516, 239825274387101510, '东方建富大厦3栋-505', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19304, 1002756, 240111044331053516, 239825274387101511, '东方建富大厦3栋-506', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19305, 1002756, 240111044331053516, 239825274387101512, '东方建富大厦3栋-507', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19306, 1002756, 240111044331053516, 239825274387101513, '东方建富大厦3栋-508', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19307, 1002756, 240111044331053516, 239825274387101514, '东方建富大厦3栋-509', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19308, 1002756, 240111044331053516, 239825274387101515, '东方建富大厦3栋-510', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19309, 1002756, 240111044331053516, 239825274387101516, '东方建富大厦3栋-511', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19310, 1002756, 240111044331053516, 239825274387101517, '东方建富大厦3栋-512', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19311, 1002756, 240111044331053516, 239825274387101518, '东方建富大厦3栋-513', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19312, 1002756, 240111044331053516, 239825274387101519, '东方建富大厦3栋-514', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19313, 1002756, 240111044331053516, 239825274387101520, '东方建富大厦3栋-601', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19314, 1002756, 240111044331053516, 239825274387101521, '东方建富大厦3栋-602', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19315, 1002756, 240111044331053516, 239825274387101522, '东方建富大厦3栋-603', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19316, 1002756, 240111044331053516, 239825274387101523, '东方建富大厦3栋-604', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19317, 1002756, 240111044331053516, 239825274387101524, '东方建富大厦3栋-605', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19318, 1002756, 240111044331053516, 239825274387101525, '东方建富大厦3栋-606', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19319, 1002756, 240111044331053516, 239825274387101526, '东方建富大厦3栋-607', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19320, 1002756, 240111044331053516, 239825274387101527, '东方建富大厦3栋-608', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19321, 1002756, 240111044331053516, 239825274387101528, '东方建富大厦3栋-609', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19322, 1002756, 240111044331053516, 239825274387101529, '东方建富大厦3栋-610', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19323, 1002756, 240111044331053516, 239825274387101530, '东方建富大厦3栋-611', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19324, 1002756, 240111044331053516, 239825274387101531, '东方建富大厦3栋-612', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19325, 1002756, 240111044331053516, 239825274387101532, '东方建富大厦3栋-613', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19326, 1002756, 240111044331053516, 239825274387101533, '东方建富大厦3栋-614', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19327, 1002756, 240111044331053516, 239825274387101534, '东方建富大厦3栋-701', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19328, 1002756, 240111044331053516, 239825274387101535, '东方建富大厦3栋-702', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19329, 1002756, 240111044331053516, 239825274387101536, '东方建富大厦3栋-703', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19330, 1002756, 240111044331053516, 239825274387101537, '东方建富大厦3栋-704', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19331, 1002756, 240111044331053516, 239825274387101538, '东方建富大厦3栋-705', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19332, 1002756, 240111044331053516, 239825274387101539, '东方建富大厦3栋-706', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19333, 1002756, 240111044331053516, 239825274387101540, '东方建富大厦3栋-707', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19334, 1002756, 240111044331053516, 239825274387101541, '东方建富大厦3栋-708', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19335, 1002756, 240111044331053516, 239825274387101542, '东方建富大厦3栋-709', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19336, 1002756, 240111044331053516, 239825274387101543, '东方建富大厦3栋-710', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19337, 1002756, 240111044331053516, 239825274387101544, '东方建富大厦3栋-711', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19338, 1002756, 240111044331053516, 239825274387101545, '东方建富大厦3栋-712', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19339, 1002756, 240111044331053516, 239825274387101546, '东方建富大厦3栋-713', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)VALUES (19340, 1002756, 240111044331053516, 239825274387101547, '东方建富大厦3栋-714', '0');
    
   