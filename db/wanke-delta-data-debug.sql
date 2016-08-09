-- 万科初始数据
SET @eh_configurations_id = (SELECT MAX(id) FROM `eh_community_geopoints`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
    VALUES ((@eh_configurations_id := @eh_configurations_id + 1), 'wanke.mashen.url', 'https://api.open.imasheng.com/openapi', 'the url for wanke.mashen', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
    VALUES ((@eh_configurations_id := @eh_configurations_id + 1), 'wanke.mashen.appId', '15725632', 'the appId of wanke.mashen', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
    VALUES ((@eh_configurations_id := @eh_configurations_id + 1), 'wanke.mashen.appSecret', 'b4b994d5ed0a9990', 'the appSecret of wanke.mashen', 0, NULL);
INSERT INTO `eh_community_services` (`id`, `namespace_id`, `owner_type`, `owner_id`, `scope_code`, `scope_id`, `item_name`, `item_label`, `icon_uri`, `action_type`, `action_data`, `scene_type`) 
	VALUES ('1', '0', 'community', '240111044331053621', '1', '1', '物业报修', '物业报修', 'cs://1/image/aW1hZ2UvTVRveU5tSXhOMlV6TmpSak5qQmpOMlk1T1RVMFpqSTNPR0U1TXpNMll6QmlNdw', '1', '{\"contentCategory\":1004,\"actionCategory\":0,\"forumId\":1,\"embedAppId\":27,\"entityTag\":\"PM\"}', 'default');

UPDATE `eh_organizations` SET `namespace_organization_token` = '80320', `namespace_organization_type` = 'wanke' WHERE `id` = '1002863';



INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (226690, UUID(), '9202903', '万科test用户', 'cs://1/image/aW1hZ2UvTVRvMlkySmhNbVZqTm1SaU1UQXdPREkxWkRjME5HVmxNVFU1TXpBNE5UUTBZdw', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 0);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (224262, 226690,  '0',  '13265549907',  '221616',  3, UTC_TIMESTAMP(), 0);
    
       
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES( 240111044331053621, UUID(), 13905, '深圳市',  13909, '宝安区', '万科小区', '万科小区', '深圳市宝安区宝安47区自由路万科小区', NULL, '万科小区，屹立于深圳市宝安中心区，以万科小区为商业核心聚点，与天虹商场、旺轩酒楼、宝晖商务酒店、新一佳商场等构成的集购物、娱乐、餐饮、酒店等繁华商业圈，具有独特优越的商业发展潜力。大厦集办公、酒店、会展、娱乐等多功能于一体，其人性化、智能化的设计足以体现现代化商业大厦特质，东方建富实业有限公司总部设于大厦七楼。', NULL, NULL, NULL, NULL, NULL, NULL,NULL, 180, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'1', 1, 2, UTC_TIMESTAMP(), 0);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES(240111044331049224, 240111044331053621, '', 113.896821, 22.574873, 'ws0brv9hhm34');	
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(1002863, 240111044331053621);


    
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1003232, UUID(), '万科公司', '万科公司', 1, 1, 1002863, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 180881, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(180881, UUID(), 0, 2, 'EhGroups', 1003232,'万科公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());       

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(1002863, 0, 'PM', '万科公司', '', '/1002863', 1, 2, 'ENTERPRISE', 0, 1003232);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1111261, 240111044331053621, 'organization', 1002863, 3, 0, UTC_TIMESTAMP());    
  
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, create_time)
	VALUES(2105542, 1002863, 'USER', 226690, 'manager', '万科test用户', 0, '13430865789', 3, UTC_TIMESTAMP());	


INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(11116, 'EhOrganizations', 1002863, 'EhUsers', 226690, 1001, 1, UTC_TIMESTAMP());


  
INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES(1245, 0, 'COMMUNITY', 240111044331053621, UTC_TIMESTAMP());