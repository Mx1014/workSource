INSERT INTO `eh_namespaces`(`id`, `name`) VALUES(999982, '清华天安');

INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES(1130, 999982, 'community_commercial', UTC_TIMESTAMP()); 

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES (650, 'app.agreements.url', 'http://core.zuolin.com/mobile/static/app_agreements/Tspace_agreements.html', 'the relative path for T-space app agreements', '999982', NULL);

INSERT INTO `eh_version_realm` VALUES ('75', 'Android_Tspace', NULL, UTC_TIMESTAMP(), '999982');
INSERT INTO `eh_version_realm` VALUES ('76', 'iOS_Tspace', NULL, UTC_TIMESTAMP(), '999982');

INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	VALUES(172,75,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	VALUES(173,76,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());

INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) 
	VALUES(999982, 'sms.default.yzx', 1, 'zh_CN', '验证码-清华天安', '33739');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) 
	VALUES(999982, 'sms.default.yzx', 4, 'zh_CN', '派单-清华天安', '33740');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) 
	VALUES(999982, 'sms.default.yzx', 6, 'zh_CN', '任务2-清华天安', '33742');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('sms.default.yzx', '11', 'zh_CN', '物业任务-清华天安', '33746', '999982');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('sms.default.yzx', '10', 'zh_CN', '物业任务2-清华天安', '30897', '999982');

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (242845  , UUID(), '9203218', '张盛', 'cs://1/image/aW1hZ2UvTVRvMlkySmhNbVZqTm1SaU1UQXdPREkxWkRjME5HVmxNVFU1TXpBNE5UUTBZdw', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (237218 , 242845  ,  '0',  '18638080988',  '221616',  3, UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (242846  , UUID(), '9203219', '朱定中', 'cs://1/image/aW1hZ2UvTVRvMlkySmhNbVZqTm1SaU1UQXdPREkxWkRjME5HVmxNVFU1TXpBNE5UUTBZdw', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (237219 , 242846  ,  '0',  '18520879295',  '221616',  3, UTC_TIMESTAMP(), 999982);

	
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1008228, UUID(), '天安梯空间科技服务（深圳）有限公司', '天安梯空间科技服务（深圳）有限公司', 1, 1, 1008851, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 186973, 1, 999982); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(186973, UUID(), 999982, 2, 'EhGroups', 1008228,'天安梯空间科技服务（深圳）有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());	
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(1008851, 0, 'PM', '天安梯空间科技服务（深圳）有限公司', 0, '', '/1008851', 1, 2, 'ENTERPRISE', 999982, 1008228);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1117090, 240111044331055936, 'organization', 1008851, 3, 0, UTC_TIMESTAMP());

INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, STATUS, `namespace_id`)
	VALUES(2119979, 1008851, 'USER', 242845  , 'manager', '张盛', 0, '18638080988', 3, 999982);	
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(13503, 'EhOrganizations', 1008851, 'EhUsers', 242845  , 1001, 1, UTC_TIMESTAMP());
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, STATUS, `namespace_id`)
	VALUES(2119980, 1008851, 'USER', 242846  , 'manager', '朱定中', 0, '18520879295', 3, 999982);	
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(13504, 'EhOrganizations', 1008851, 'EhUsers', 242846  , 1001, 1, UTC_TIMESTAMP());

	
INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES(1694, 999982, 'COMMUNITY', 240111044331055936, UTC_TIMESTAMP());

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(186974, UUID(), 999982, 2, 'EhGroups', 0,'梯空间论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(186975, UUID(), 999982, 2, 'EhGroups', 0,'梯空间意见反馈论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 


INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES ('15405', '0', '广东', 'GUANGDONG', 'GD', '/广东', '1', '1', '', '', '2', '2', '999982');
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES ('15406', '15405', '深圳市', 'SHENZHENSHI', 'SZS', '/广东/深圳市', '2', '2', NULL, '0755', '2', '1', '999982');
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES ('15407', '15406', '福田区', 'FUTIANQU', 'NSQ', '/广东/深圳市/福田区', '3', '3', NULL, '0755', '2', '0', '999982');
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES ('15408', '15406', '龙岗区', 'LONGGANGQU', 'NSQ', '/广东/深圳市/龙岗区', '3', '3', NULL, '0755', '2', '0', '999982');


INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES(240111044331055936, UUID(), 15406, '深圳市',  15407, '福田区', 'T+SPACE深圳福田', 'T+SPACE深圳福田创业园', '深圳市福田区泰然五路福田天安科技创业园5楼', NULL, '',
	NULL, NULL, NULL, NULL, NULL, NULL,NULL, 168, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'1', 186974, 186975, UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES(240111044331051939, 240111044331055936, '', 114.036668, 22.539236, 'ws104znuwfne');
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(1008851, 240111044331055936);
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES(240111044331055937, UUID(), 15406, '深圳市',  15408, '龙岗区', 'T+SPACE深圳龙岗', 'T+SPACE深圳龙岗天安', '深圳市龙岗区黄阁北路与清林路交汇处', NULL, '',
	NULL, NULL, NULL, NULL, NULL, NULL,NULL, 168, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'1', 186974, 186975, UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES(240111044331051940, 240111044331055937, '', 114.223521, 22.726826, 'ws11r1r12gqm');
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(1008851, 240111044331055937);


INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(181635, 240111044331055936, '福田天安科技创业园', '福田天安科技创业园', 0, NULL, '广东省深圳市福田区泰然五路', NULL, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999982);


INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387135171,UUID(),240111044331055936, 15406, '深圳市',  15407, '福田区' ,'科技创业园-A501-01','科技创业园','A501-01','2','0',UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387135172,UUID(),240111044331055936, 15406, '深圳市',  15407, '福田区' ,'科技创业园-A501-02','科技创业园','A501-02','2','0',UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387135173,UUID(),240111044331055936, 15406, '深圳市',  15407, '福田区' ,'科技创业园-A501-03','科技创业园','A501-03','2','0',UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387135174,UUID(),240111044331055936, 15406, '深圳市',  15407, '福田区' ,'科技创业园-A501-04','科技创业园','A501-04','2','0',UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387135175,UUID(),240111044331055936, 15406, '深圳市',  15407, '福田区' ,'科技创业园-A501-05','科技创业园','A501-05','2','0',UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387135176,UUID(),240111044331055936, 15406, '深圳市',  15407, '福田区' ,'科技创业园-A501-06','科技创业园','A501-06','2','0',UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387135177,UUID(),240111044331055936, 15406, '深圳市',  15407, '福田区' ,'科技创业园-A501-07','科技创业园','A501-07','2','0',UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387135178,UUID(),240111044331055936, 15406, '深圳市',  15407, '福田区' ,'科技创业园-A501-08','科技创业园','A501-08','2','0',UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387135179,UUID(),240111044331055936, 15406, '深圳市',  15407, '福田区' ,'科技创业园-A501-09','科技创业园','A501-09','2','0',UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387135180,UUID(),240111044331055936, 15406, '深圳市',  15407, '福田区' ,'科技创业园-A501-10','科技创业园','A501-10','2','0',UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387135181,UUID(),240111044331055936, 15406, '深圳市',  15407, '福田区' ,'科技创业园-A501-11','科技创业园','A501-11','2','0',UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387135182,UUID(),240111044331055936, 15406, '深圳市',  15407, '福田区' ,'科技创业园-B501','科技创业园','B501','2','0',UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387135183,UUID(),240111044331055936, 15406, '深圳市',  15407, '福田区' ,'科技创业园-B502','科技创业园','B502','2','0',UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387135184,UUID(),240111044331055936, 15406, '深圳市',  15407, '福田区' ,'科技创业园-B503','科技创业园','B503','2','0',UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387135185,UUID(),240111044331055936, 15406, '深圳市',  15407, '福田区' ,'科技创业园-B504','科技创业园','B504','2','0',UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387135186,UUID(),240111044331055936, 15406, '深圳市',  15407, '福田区' ,'科技创业园-B505','科技创业园','B505','2','0',UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387135187,UUID(),240111044331055936, 15406, '深圳市',  15407, '福田区' ,'科技创业园-B506','科技创业园','B506','2','0',UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387135188,UUID(),240111044331055936, 15406, '深圳市',  15407, '福田区' ,'科技创业园-B507','科技创业园','B507','2','0',UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387135189,UUID(),240111044331055936, 15406, '深圳市',  15407, '福田区' ,'科技创业园-B508','科技创业园','B508','2','0',UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387135190,UUID(),240111044331055936, 15406, '深圳市',  15407, '福田区' ,'科技创业园-B509','科技创业园','B509','2','0',UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387135191,UUID(),240111044331055936, 15406, '深圳市',  15407, '福田区' ,'科技创业园-B510','科技创业园','B510','2','0',UTC_TIMESTAMP(), 999982);
	
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26446, 1008851, 240111044331055936, 239825274387135171, '科技创业园-A501-01', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26447, 1008851, 240111044331055936, 239825274387135172, '科技创业园-A501-02', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26448, 1008851, 240111044331055936, 239825274387135173, '科技创业园-A501-03', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26449, 1008851, 240111044331055936, 239825274387135174, '科技创业园-A501-04', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26450, 1008851, 240111044331055936, 239825274387135175, '科技创业园-A501-05', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26451, 1008851, 240111044331055936, 239825274387135176, '科技创业园-A501-06', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26452, 1008851, 240111044331055936, 239825274387135177, '科技创业园-A501-07', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26453, 1008851, 240111044331055936, 239825274387135178, '科技创业园-A501-08', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26454, 1008851, 240111044331055936, 239825274387135179, '科技创业园-A501-09', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26455, 1008851, 240111044331055936, 239825274387135180, '科技创业园-A501-10', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26456, 1008851, 240111044331055936, 239825274387135181, '科技创业园-A501-11', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26457, 1008851, 240111044331055936, 239825274387135182, '科技创业园-B501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26458, 1008851, 240111044331055936, 239825274387135183, '科技创业园-B502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26459, 1008851, 240111044331055936, 239825274387135184, '科技创业园-B503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26460, 1008851, 240111044331055936, 239825274387135185, '科技创业园-B504', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26461, 1008851, 240111044331055936, 239825274387135186, '科技创业园-B505', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26462, 1008851, 240111044331055936, 239825274387135187, '科技创业园-B506', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26463, 1008851, 240111044331055936, 239825274387135188, '科技创业园-B507', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26464, 1008851, 240111044331055936, 239825274387135189, '科技创业园-B508', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26465, 1008851, 240111044331055936, 239825274387135190, '科技创业园-B509', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (26466, 1008851, 240111044331055936, 239825274387135191, '科技创业园-B510', '0');


INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES (12105, 999982, 0, '/home', 'Default', '0', '0', 'innospring', 'innospring', 'cs://1/image/aW1hZ2UvTVRwaE4yRXdNRFEwTUdReFkyUTVPV05oTVROa1l6STJZakF6WWpnNFlqWXhPUQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'park_tourist');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES (12106, 999982, 0, '/home', 'Default', '0', '0', 'innospring', 'innospring', 'cs://1/image/aW1hZ2UvTVRwaE4yRXdNRFEwTUdReFkyUTVPV05oTVROa1l6STJZakF6WWpnNFlqWXhPUQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'pm_admin');

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES('10500','会议室预约','0',NULL,'0','999982');


-- 园区管理员场景

 INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, NAME, layout_json, version_code, min_version_code, STATUS, create_time, scene_type) 
	VALUES (512, 999982, 'ServiceMarketLayout', '{"versionCode":"2016121201","versionName":"3.12.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"Gallery"},"style":"Gallery","defaultOrder":2,"separatorFlag":0,"separatorHeight":0,"columnCount":1}]}', '2016121201', '0', '2', UTC_TIMESTAMP(), 'pm_admin');


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112816, 999982, 0, 0, 0, '/home', 'Bizs', '工位预订', '工位预订', 'cs://1/image/aW1hZ2UvTVRvNU9XTXlZVEUwTkROa00yWmtZV1ExTVRjM1kyWXdabUk0WVRrMVpEQm1ZZw', 1, 1, 14,'{"url":"http://core.zuolin.com/station-booking/index.html?hideNavigationBar=1#/station_booking#sign_suffix"}', 0, 0, 1, 1, '', '1', NULL, NULL, NULL, '1', 'pm_admin');        
	
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112817, 999982, 0, 0, 0, '/home', 'Bizs', 'MEETING', '会议室', 'cs://1/image/aW1hZ2UvTVRwa05EY3dZbU01Wm1Jek9HSXpOMkpsTW1ZelpURTNaRGd4WmpZMU5HWTFNdw', 1, 1, 49,'{\"resourceTypeId\":10500,\"pageType\":0}', 0, 0, 1, 1, '', '1', NULL, NULL, NULL, '1', 'pm_admin');        
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112818, 999982, '0', '0', '0', '/home', 'Bizs', 'DoorManagement', '门禁', 'cs://1/image/aW1hZ2UvTVRveE9EUm1PVE0zTVdSa01tWXlPVGd3TkdZME1UY3hNR0kyWWpFek9URTNZZw', '1', '1', '40', '{"isSupportQR":1,"isSupportSmart":1}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112819, 999982, '0', '0', '0', '/home', 'Bizs', '更多', '更多', 'cs://1/image/aW1hZ2UvTVRvMk5tWTJZemt3WVRjeE5HVmhabUZtTXprNFptVm1ZbUk0WVRBME16a3pOQQ', '1', '1', '1', '{\"itemLocation\":\"/home\", \"itemGroup\":\"Bizs\"}', '30', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112820, 999982, '0', '0', '0', '/home', 'Bizs', 'VIDEO_MEETING', '视频会议', 'cs://1/image/aW1hZ2UvTVRwbFpEYzFOVEJpWlRJNFltTXpZV00xT0dRME9HUmxOakEwTnpFd09XTmxPUQ', '1', '1', '27', '', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112821, 999982, '0', '0', '0', '/home', 'Bizs', 'CONTACTS', '通讯录', 'cs://1/image/aW1hZ2UvTVRveFpqQmlNVFF4WmpFNU5qUTNZalV5TVdRMU9UZ3pOakpsWlRSaE5qazFZUQ', '1', '1', '46', '', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112822, 999982, '0', '0', '0', '/home', 'Gallery', 'OFFICIAL_ACTIVITY', '活动', 'cs://1/image/aW1hZ2UvTVRvd1lqWTRNbUUxTW1FeE4yVmxZMlJrT0RnMllUQmxOekpqT0dZM1lUSmlZUQ', '1', '1', '50', NULL, '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112823, 999982, '0', '0', '0', '/home', 'Gallery', 'CLUB', '俱乐部', 'cs://1/image/aW1hZ2UvTVRvd1pUUXlNVFF6TVRoaU1UazVOVEJoTlRsa016QTVPVEZqT1RZNFlUVXlZZw', '1', '1', '36', '{"privateFlag": 0}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');
	

-- 园区游客场景    
 INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, NAME, layout_json, version_code, min_version_code, STATUS, create_time, scene_type) 
	VALUES (513, 999982, 'ServiceMarketLayout', '{"versionCode":"2016121201","versionName":"3.12.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"Gallery"},"style":"Gallery","defaultOrder":2,"separatorFlag":0,"separatorHeight":0,"columnCount":1}]}', '2016121201', '0', '2', UTC_TIMESTAMP(), 'park_tourist');


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112824, 999982, 0, 0, 0, '/home', 'Bizs', '工位预订', '工位预订', 'cs://1/image/aW1hZ2UvTVRvNU9XTXlZVEUwTkROa00yWmtZV1ExTVRjM1kyWXdabUk0WVRrMVpEQm1ZZw', 1, 1, 14,'{"url":"http://core.zuolin.com/station-booking/index.html?hideNavigationBar=1#/station_booking#sign_suffix"}', 0, 0, 1, 1, '', '1', NULL, NULL, NULL, '1', 'park_tourist');        
	
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112825, 999982, 0, 0, 0, '/home', 'Bizs', 'MEETING', '会议室', 'cs://1/image/aW1hZ2UvTVRwa05EY3dZbU01Wm1Jek9HSXpOMkpsTW1ZelpURTNaRGd4WmpZMU5HWTFNdw', 1, 1, 49,'{\"resourceTypeId\":10500,\"pageType\":0}', 0, 0, 1, 1, '', '1', NULL, NULL, NULL, '1', 'park_tourist');        
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112826, 999982, '0', '0', '0', '/home', 'Bizs', 'DoorManagement', '门禁', 'cs://1/image/aW1hZ2UvTVRveE9EUm1PVE0zTVdSa01tWXlPVGd3TkdZME1UY3hNR0kyWWpFek9URTNZZw', '1', '1', '40', '{"isSupportQR":1,"isSupportSmart":1}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112827, 999982, '0', '0', '0', '/home', 'Bizs', '更多', '更多', 'cs://1/image/aW1hZ2UvTVRvMk5tWTJZemt3WVRjeE5HVmhabUZtTXprNFptVm1ZbUk0WVRBME16a3pOQQ', '1', '1', '1', '{\"itemLocation\":\"/home\", \"itemGroup\":\"Bizs\"}', '30', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112828, 999982, '0', '0', '0', '/home', 'Bizs', 'VIDEO_MEETING', '视频会议', 'cs://1/image/aW1hZ2UvTVRwbFpEYzFOVEJpWlRJNFltTXpZV00xT0dRME9HUmxOakEwTnpFd09XTmxPUQ', '1', '1', '27', '', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112829, 999982, '0', '0', '0', '/home', 'Bizs', 'CONTACTS', '通讯录', 'cs://1/image/aW1hZ2UvTVRveFpqQmlNVFF4WmpFNU5qUTNZalV5TVdRMU9UZ3pOakpsWlRSaE5qazFZUQ', '1', '1', '46', '', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112830, 999982, '0', '0', '0', '/home', 'Gallery', 'OFFICIAL_ACTIVITY', '活动', 'cs://1/image/aW1hZ2UvTVRvd1lqWTRNbUUxTW1FeE4yVmxZMlJrT0RnMllUQmxOekpqT0dZM1lUSmlZUQ', '1', '1', '50', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (112831, 999982, '0', '0', '0', '/home', 'Gallery', 'CLUB', '俱乐部', 'cs://1/image/aW1hZ2UvTVRvd1pUUXlNVFF6TVRoaU1UazVOVEJoTlRsa016QTVPVEZqT1RZNFlUVXlZZw', '1', '1', '36', '{"privateFlag": 0}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');


SET @eh_categories = (SELECT MAX(id) FROM `eh_categories`);   
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES((@eh_categories := @eh_categories + 1), 2, 0, '运动与音乐', '兴趣/运动与音乐', 10, 2, UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES((@eh_categories := @eh_categories + 1), 2, 0, '旅游摄影', '兴趣/旅游摄影', 20, 2, UTC_TIMESTAMP(), 999982);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES((@eh_categories := @eh_categories + 1), 2, 0, '亲子与教育', '兴趣/亲子与教育', 30, 2, UTC_TIMESTAMP(), 999982);

INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
	VALUES ((@eh_categories := @eh_categories + 1), '4', '0', '科技园大讲堂', '活动/科技园大讲堂', '0', '2', UTC_TIMESTAMP(), NULL, NULL, NULL, '999982');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
	VALUES ((@eh_categories := @eh_categories + 1), '4', '0', '项目对接会', '活动/项目对接会', '0', '2', UTC_TIMESTAMP(), NULL, NULL, NULL, '999982');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
	VALUES ((@eh_categories := @eh_categories + 1), '4', '0', '总裁沙龙', '活动/总裁沙龙', '0', '2', UTC_TIMESTAMP(), NULL, NULL, NULL, '999982');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
	VALUES ((@eh_categories := @eh_categories + 1), '4', '0', '创客', '活动/创客', '0', '2', UTC_TIMESTAMP(), NULL, NULL, NULL, '999982');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
	VALUES ((@eh_categories := @eh_categories + 1), '4', '0', '联谊', '活动/联谊', '0', '2', UTC_TIMESTAMP(), NULL, NULL, NULL, '999982');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
	VALUES ((@eh_categories := @eh_categories + 1), '4', '0', '其他', '活动/其他', '1', '2', UTC_TIMESTAMP(), NULL, NULL, NULL, '999982');

INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
	VALUES ((@eh_categories := @eh_categories + 1), '1', '0', '产业平台', '帖子/产业平台', '0', '2', UTC_TIMESTAMP(), NULL, NULL, NULL, '999982');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
	VALUES ((@eh_categories := @eh_categories + 1), '1', '0', '产业服务', '帖子/产业服务', '0', '2', UTC_TIMESTAMP(), NULL, NULL, NULL, '999982');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
	VALUES ((@eh_categories := @eh_categories + 1), '1', '0', '入园服务', '帖子/入园服务', '0', '2', UTC_TIMESTAMP(), NULL, NULL, NULL, '999982');

-- 菜单	
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10000,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10100,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10400,'', 'EhNamespaces', 999982,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10600,'', 'EhNamespaces', 999982,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10750,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10751,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10752,'', 'EhNamespaces', 999982,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),11000,'', 'EhNamespaces', 999982,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40000,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40200,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40210,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40220,'', 'EhNamespaces', 999982,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40400,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40410,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40420,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40430,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40440,'', 'EhNamespaces', 999982,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41000,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41010,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41020,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41030,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41040,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41050,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41060,'', 'EhNamespaces', 999982,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),30000,'', 'EhNamespaces', 999982,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),31000,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),32000,'', 'EhNamespaces', 999982,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),33000,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),34000,'', 'EhNamespaces', 999982,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50000,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50100,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50110,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50200,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50210,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50220,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50300,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50400,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50500,'', 'EhNamespaces', 999982,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50700,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50710,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50720,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50730,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50800,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50810,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50820,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50830,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50840,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50850,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50860,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),60000,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),60100,'', 'EhNamespaces', 999982,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),60200,'', 'EhNamespaces', 999982,2);


--  2017-2-28 by wuhan 增加考勤

SET @id =(SELECT MAX(id) FROM eh_web_menu_scopes ); 
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@id:=@id+1),'50600','','EhNamespaces','999982','2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@id:=@id+1),'50630','','EhNamespaces','999982','2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@id:=@id+1),'50631','','EhNamespaces','999982','2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@id:=@id+1),'50632','','EhNamespaces','999982','2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@id:=@id+1),'50633','','EhNamespaces','999982','2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@id:=@id+1),'50640','','EhNamespaces','999982','2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@id:=@id+1),'50650','','EhNamespaces','999982','2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@id:=@id+1),'50651','','EhNamespaces','999982','2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@id:=@id+1),'50652','','EhNamespaces','999982','2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@id:=@id+1),'50653','','EhNamespaces','999982','2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@id:=@id+1),'50660','','EhNamespaces','999982','2');


SET @eh_launch_pad_items =(SELECT MAX(id) FROM eh_launch_pad_items ); 
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999982', '0', '0', '0', '/home', 'Bizs', 'PUNCH', '打卡考勤', 'cs://1/image/aW1hZ2UvTVRwaU1qQmtObVExTkRKbVlqTmpNR0k0T0RZd01qTm1Oekl6WWpGa1lqUXdNQQ', '1', '1', '23', '', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', '7');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999982', '0', '0', '0', '/home', 'Bizs', 'PUNCH', '打卡考勤', 'cs://1/image/aW1hZ2UvTVRwaU1qQmtObVExTkRKbVlqTmpNR0k0T0RZd01qTm1Oekl6WWpGa1lqUXdNQQ', '1', '1', '23', '', '0', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', '7');

-- redmine 9172 by xiongying20170504
update eh_launch_pad_items set display_flag = 0 where namespace_id = 999982 and item_label in('活动','俱乐部');
update eh_launch_pad_items set item_group = 'Bizs' where namespace_id = 999982 and item_label in('活动','俱乐部');
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRwbU9XWTJaakl6Tm1JelpHSTROR1ZqT1dObU1tTmpaak16WXpsbU5XSTBOUQ' where namespace_id = 999982 and item_label = '活动';
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvMk56QTROV0kzWXpOaE56VmlaVFppWTJKaE5qSTVOVE0zTlRZNU1XVXdaZw' where namespace_id = 999982 and item_label = '俱乐部';


update eh_launch_pad_layouts set layout_json = '{"versionCode":"2017050402","versionName":"3.12.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"News","instanceConfig":{"timeWidgetStyle":"datetime","categoryId":0,"itemGroup":"Default","newsSize":5},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0}]}' where id in(512, 513) and namespace_id = 999982;
update eh_launch_pad_layouts set version_code = '2017050402' where id in(512, 513) and namespace_id = 999982;

-- 后台同步增加“信息发布-新闻管理”菜单 by xiongying20170504
SET @id =(SELECT MAX(id) FROM eh_web_menu_scopes ); 
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@id:=@id+1),'10800','','EhNamespaces','999982','2');