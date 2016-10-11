
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (231350, UUID(), '9201913', '刘兰', 'cs://1/image/aW1hZ2UvTVRvMlkySmhNbVZqTm1SaU1UQXdPREkxWkRjME5HVmxNVFU1TXpBNE5UUTBZdw', 1, 45, '1', '2',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 0);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (226382, 231350,  '0',  '13728614307',  '221616',  3, UTC_TIMESTAMP(), 0);
    
       
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES( 240111044331053735, UUID(), 13905, '深圳市',  13909, '光明新区', '怡景工业城', '怡景工业城', '公明办事处田寮社区松白路3055号', NULL, '怡景工业城位于深圳市光明新区公明田寮松柏公路旁（长圳村牌坊对面），辅道出口直通工业区大门。往返深圳南头关口仅20分钟，至深圳国际机场仅15分钟车程，交通相当便利。园区占地面积约8.3万平方米，建筑总面积约23万平方米，共建房屋22栋（其中管理处大楼1栋，厂房14栋，职工宿舍6栋，高级公寓1栋），厂房为框架结构，耐火等级为二级，7度防震；每栋宿舍首层为工业园配套饭堂、商场、自助银行、服场所、电信、医疗等生活齐全配套。本工业园设计为园林式工业园，市政水电、电信等配套全面到位（电引至总配电房），有足够的停车位，由品牌工业物业管理公司—深圳市东方建富物业管理有限公司为您提供精湛、全方位的服务。', NULL, NULL, NULL, NULL, NULL, NULL,NULL, 180, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'1', 1, 2, UTC_TIMESTAMP(), 0);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES(240111044331049338, 240111044331053735, '', 113.913396, 22.725719, 'ws1121j4yt06');	
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(1004005, 240111044331053735);

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1004376, UUID(), '唐山大亚链条有限公司', '唐山大亚链条有限公司', 1, 1, 1003991, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 181967, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(181967, UUID(), 0, 2, 'EhGroups', 1004376,'唐山大亚链条有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());       
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1004377, UUID(), '深圳市丝淼机电有限公司', '深圳市丝淼机电有限公司', 1, 1, 1003992, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 181968, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(181968, UUID(), 0, 2, 'EhGroups', 1004377,'深圳市丝淼机电有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());       
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1004378, UUID(), '深圳市清锋照明科技有限公司', '深圳市清锋照明科技有限公司', 1, 1, 1003993, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 181969, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(181969, UUID(), 0, 2, 'EhGroups', 1004378,'深圳市清锋照明科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());       
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1004379, UUID(), '深圳市威林制衣有限公司', '深圳市威林制衣有限公司', 1, 1, 1003994, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 181970, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(181970, UUID(), 0, 2, 'EhGroups', 1004379,'深圳市威林制衣有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());       
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1004380, UUID(), '深圳市新永联电子有限公司', '深圳市新永联电子有限公司', 1, 1, 1003995, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 181971, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(181971, UUID(), 0, 2, 'EhGroups', 1004380,'深圳市新永联电子有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());       
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1004381, UUID(), '深圳市伟发科技有限公司', '深圳市伟发科技有限公司', 1, 1, 1003996, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 181972, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(181972, UUID(), 0, 2, 'EhGroups', 1004381,'深圳市伟发科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());       
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1004382, UUID(), '深圳市清锋照明有限公司', '深圳市清锋照明有限公司', 1, 1, 1003997, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 181973, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(181973, UUID(), 0, 2, 'EhGroups', 1004382,'深圳市清锋照明有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());       
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1004383, UUID(), '清华大学深圳研究生院', '清华大学深圳研究生院', 1, 1, 1003998, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 181974, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(181974, UUID(), 0, 2, 'EhGroups', 1004383,'清华大学深圳研究生院','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());       
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1004384, UUID(), '深圳市比尔德科技有限公司', '深圳市比尔德科技有限公司', 1, 1, 1003999, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 181975, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(181975, UUID(), 0, 2, 'EhGroups', 1004384,'深圳市比尔德科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());       
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1004385, UUID(), '深圳市鑫博艺金属制品厂', '深圳市鑫博艺金属制品厂', 1, 1, 1004001, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 181976, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(181976, UUID(), 0, 2, 'EhGroups', 1004385,'深圳市鑫博艺金属制品厂','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());       
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1004386, UUID(), '深圳市福瑞达铭板有限公司', '深圳市福瑞达铭板有限公司', 1, 1, 1004002, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 181977, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(181977, UUID(), 0, 2, 'EhGroups', 1004386,'深圳市福瑞达铭板有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());       
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1004387, UUID(), '深圳市美仕达数码科技开发有限公司', '深圳市美仕达数码科技开发有限公司', 1, 1, 1004003, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 181978, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(181978, UUID(), 0, 2, 'EhGroups', 1004387,'深圳市美仕达数码科技开发有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());       
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1004388, UUID(), '深圳市祥晖科技有限公司', '深圳市祥晖科技有限公司', 1, 1, 1004004, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 181979, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(181979, UUID(), 0, 2, 'EhGroups', 1004388,'深圳市祥晖科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());       


INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(1003991, 0, 'ENTERPRISE', '唐山大亚链条有限公司', '', '/1003991', 1, 2, 'ENTERPRISE', 0, 1004376);
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(1003992, 0, 'ENTERPRISE', '深圳市丝淼机电有限公司', '', '/1003992', 1, 2, 'ENTERPRISE', 0, 1004377);
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(1003993, 0, 'ENTERPRISE', '深圳市清锋照明科技有限公司', '', '/1003993', 1, 2, 'ENTERPRISE', 0, 1004378);
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(1003994, 0, 'ENTERPRISE', '深圳市威林制衣有限公司 ', '', '/1003994', 1, 2, 'ENTERPRISE', 0, 1004379);
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(1003995, 0, 'ENTERPRISE', '深圳市新永联电子有限公司', '', '/1003995', 1, 2, 'ENTERPRISE', 0, 1004380);
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(1003996, 0, 'ENTERPRISE', '深圳市伟发科技有限公司', '', '/1003996', 1, 2, 'ENTERPRISE', 0, 1004381);
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(1003997, 0, 'ENTERPRISE', '深圳市清锋照明有限公司 ', '', '/1003997', 1, 2, 'ENTERPRISE', 0, 1004382);
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(1003998, 0, 'ENTERPRISE', '清华大学深圳研究生院', '', '/1003998', 1, 2, 'ENTERPRISE', 0, 1004383);
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(1003999, 0, 'ENTERPRISE', '深圳市比尔德科技有限公司', '', '/1003999', 1, 2, 'ENTERPRISE', 0, 1004384);
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(1004001, 0, 'ENTERPRISE', '深圳市鑫博艺金属制品厂', '', '/1004001', 1, 2, 'ENTERPRISE', 0, 1004385);
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(1004002, 0, 'ENTERPRISE', '深圳市福瑞达铭板有限公司', '', '/1004002', 1, 2, 'ENTERPRISE', 0, 1004386);
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(1004003, 0, 'ENTERPRISE', '深圳市美仕达数码科技开发有限公司', '', '/1004003', 1, 2, 'ENTERPRISE', 0, 1004387);
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(1004004, 0, 'ENTERPRISE', '深圳市祥晖科技有限公司', '', '/1004004', 1, 2, 'ENTERPRISE', 0, 1004388);

INSERT INTO `eh_organization_addresses` (`id`, `organization_id`, `address_id`, `status`, `creator_uid`, `create_time`, `operator_uid`, `process_code`, `process_details`, `proof_resource_uri`, `approve_time`, `update_time`, `building_id`, `building_name`) 
	VALUES ('113170', '1003991', '239825274387112834', '2', NULL, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, '179989', '怡景工业城A4栋');
INSERT INTO `eh_organization_addresses` (`id`, `organization_id`, `address_id`, `status`, `creator_uid`, `create_time`, `operator_uid`, `process_code`, `process_details`, `proof_resource_uri`, `approve_time`, `update_time`, `building_id`, `building_name`) 
	VALUES ('113171', '1003992', '239825274387112835', '2', NULL, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, '179989', '怡景工业城A4栋');
INSERT INTO `eh_organization_addresses` (`id`, `organization_id`, `address_id`, `status`, `creator_uid`, `create_time`, `operator_uid`, `process_code`, `process_details`, `proof_resource_uri`, `approve_time`, `update_time`, `building_id`, `building_name`) 
	VALUES ('113172', '1003993', '239825274387112836', '2', NULL, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, '179989', '怡景工业城A4栋');
INSERT INTO `eh_organization_addresses` (`id`, `organization_id`, `address_id`, `status`, `creator_uid`, `create_time`, `operator_uid`, `process_code`, `process_details`, `proof_resource_uri`, `approve_time`, `update_time`, `building_id`, `building_name`) 
	VALUES ('113173', '1003994', '239825274387112837', '2', NULL, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, '179989', '怡景工业城A4栋');
INSERT INTO `eh_organization_addresses` (`id`, `organization_id`, `address_id`, `status`, `creator_uid`, `create_time`, `operator_uid`, `process_code`, `process_details`, `proof_resource_uri`, `approve_time`, `update_time`, `building_id`, `building_name`) 
	VALUES ('113174', '1003995', '239825274387112838', '2', NULL, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, '179989', '怡景工业城A4栋');
INSERT INTO `eh_organization_addresses` (`id`, `organization_id`, `address_id`, `status`, `creator_uid`, `create_time`, `operator_uid`, `process_code`, `process_details`, `proof_resource_uri`, `approve_time`, `update_time`, `building_id`, `building_name`) 
	VALUES ('113175', '1003996', '239825274387112839', '2', NULL, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, '179989', '怡景工业城A4栋');
INSERT INTO `eh_organization_addresses` (`id`, `organization_id`, `address_id`, `status`, `creator_uid`, `create_time`, `operator_uid`, `process_code`, `process_details`, `proof_resource_uri`, `approve_time`, `update_time`, `building_id`, `building_name`) 
	VALUES ('113176', '1003997', '239825274387112841', '2', NULL, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, '179989', '怡景工业城A4栋');
INSERT INTO `eh_organization_addresses` (`id`, `organization_id`, `address_id`, `status`, `creator_uid`, `create_time`, `operator_uid`, `process_code`, `process_details`, `proof_resource_uri`, `approve_time`, `update_time`, `building_id`, `building_name`) 
	VALUES ('113177', '1003998', '239825274387112842', '2', NULL, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, '179990', '怡景工业城A5栋');
INSERT INTO `eh_organization_addresses` (`id`, `organization_id`, `address_id`, `status`, `creator_uid`, `create_time`, `operator_uid`, `process_code`, `process_details`, `proof_resource_uri`, `approve_time`, `update_time`, `building_id`, `building_name`) 
	VALUES ('113178', '1003999', '239825274387112843', '2', NULL, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, '179990', '怡景工业城A5栋');
INSERT INTO `eh_organization_addresses` (`id`, `organization_id`, `address_id`, `status`, `creator_uid`, `create_time`, `operator_uid`, `process_code`, `process_details`, `proof_resource_uri`, `approve_time`, `update_time`, `building_id`, `building_name`) 
	VALUES ('113179', '1004001', '239825274387112845', '2', NULL, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, '179990', '怡景工业城A5栋');
INSERT INTO `eh_organization_addresses` (`id`, `organization_id`, `address_id`, `status`, `creator_uid`, `create_time`, `operator_uid`, `process_code`, `process_details`, `proof_resource_uri`, `approve_time`, `update_time`, `building_id`, `building_name`) 
	VALUES ('113180', '1004002', '239825274387112846', '2', NULL, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, '179990', '怡景工业城A5栋');
INSERT INTO `eh_organization_addresses` (`id`, `organization_id`, `address_id`, `status`, `creator_uid`, `create_time`, `operator_uid`, `process_code`, `process_details`, `proof_resource_uri`, `approve_time`, `update_time`, `building_id`, `building_name`) 
	VALUES ('113181', '1004003', '239825274387112847', '2', NULL, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, '179990', '怡景工业城A5栋');
INSERT INTO `eh_organization_addresses` (`id`, `organization_id`, `address_id`, `status`, `creator_uid`, `create_time`, `operator_uid`, `process_code`, `process_details`, `proof_resource_uri`, `approve_time`, `update_time`, `building_id`, `building_name`) 
	VALUES ('113182', '1004004', '239825274387112848', '2', NULL, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, '179990', '怡景工业城A5栋');


INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1112388, 240111044331053735, 'organization', 1003991, 3, 0, UTC_TIMESTAMP());    
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1112389, 240111044331053735, 'organization', 1003992, 3, 0, UTC_TIMESTAMP());    
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1112390, 240111044331053735, 'organization', 1003993, 3, 0, UTC_TIMESTAMP());    
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1112391, 240111044331053735, 'organization', 1003994, 3, 0, UTC_TIMESTAMP());    
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1112392, 240111044331053735, 'organization', 1003995, 3, 0, UTC_TIMESTAMP());    
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1112393, 240111044331053735, 'organization', 1003996, 3, 0, UTC_TIMESTAMP());    
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1112394, 240111044331053735, 'organization', 1003997, 3, 0, UTC_TIMESTAMP());    
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1112395, 240111044331053735, 'organization', 1003998, 3, 0, UTC_TIMESTAMP());    
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1112396, 240111044331053735, 'organization', 1003999, 3, 0, UTC_TIMESTAMP());    
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1112397, 240111044331053735, 'organization', 1004001, 3, 0, UTC_TIMESTAMP());    
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1112398, 240111044331053735, 'organization', 1004002, 3, 0, UTC_TIMESTAMP());    
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1112399, 240111044331053735, 'organization', 1004003, 3, 0, UTC_TIMESTAMP());    
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1112400, 240111044331053735, 'organization', 1004004, 3, 0, UTC_TIMESTAMP());    
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1112401, 240111044331053735, 'organization', 1004005, 3, 0, UTC_TIMESTAMP());    

  

  
INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES(1524, 0, 'COMMUNITY', 240111044331053735, UTC_TIMESTAMP());


INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
	VALUES(179988, 240111044331053735, '管理处大楼', '管理处', manager_uid, '13923856726', '深圳市光明新区公明办事处田寮社区松白路3055号', 1514.8, NULL, NULL, NULL, '', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
	VALUES(179989, 240111044331053735, '怡景工业城A4栋', '怡景工业城A4栋', manager_uid, '13923856726', '深圳市光明新区公明办事处田寮社区松白路3055号', 7548.6, NULL, NULL, NULL, '', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) 
	VALUES(179990, 240111044331053735, '怡景工业城A5栋', '怡景工业城A5栋', manager_uid, '13923856726', '深圳市光明新区公明办事处田寮社区松白路3055号', 6995.2, NULL, NULL, NULL, '', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
    
-- INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
--	VALUES(239825274387112816,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'﻿管理处大楼-301','﻿管理处大楼','301','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112816,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'管理处大楼-301','管理处大楼','301','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112817,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'管理处大楼-302','管理处大楼','302','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112818,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'管理处大楼-303','管理处大楼','303','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112819,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'管理处大楼-304','管理处大楼','304','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112820,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'管理处大楼-305','管理处大楼','305','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112821,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'管理处大楼-306','管理处大楼','306','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112822,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'管理处大楼-401','管理处大楼','401','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112823,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'管理处大楼-402','管理处大楼','402','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112824,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'管理处大楼-403','管理处大楼','403','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112825,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'管理处大楼-404','管理处大楼','404','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112826,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'管理处大楼-405','管理处大楼','405','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112827,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'管理处大楼-406','管理处大楼','406','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112828,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'管理处大楼-501','管理处大楼','501','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112829,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'管理处大楼-502','管理处大楼','502','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112830,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'管理处大楼-503','管理处大楼','503','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112831,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'管理处大楼-504','管理处大楼','504','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112832,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'管理处大楼-505','管理处大楼','505','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112833,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'管理处大楼-506','管理处大楼','506','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112834,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'怡景工业城A4栋-1A','怡景工业城A4栋','1A','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112835,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'怡景工业城A4栋-1B','怡景工业城A4栋','1B','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112836,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'怡景工业城A4栋-2','怡景工业城A4栋','2','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112837,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'怡景工业城A4栋-3A','怡景工业城A4栋','3A','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112838,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'怡景工业城A4栋-3B','怡景工业城A4栋','3B','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112839,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'怡景工业城A4栋-4A','怡景工业城A4栋','4A','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112840,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'怡景工业城A4栋-4B','怡景工业城A4栋','4B','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112841,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'怡景工业城A4栋-5','怡景工业城A4栋','5','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112842,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'怡景工业城A5栋-1A','怡景工业城A5栋','1A','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112843,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'怡景工业城A5栋-1B','怡景工业城A5栋','1B','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112844,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'怡景工业城A5栋-1C','怡景工业城A5栋','1C','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112845,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'怡景工业城A5栋-2','怡景工业城A5栋','2','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112846,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'怡景工业城A5栋-3','怡景工业城A5栋','3','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112847,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'怡景工业城A5栋-4','怡景工业城A5栋','4','2','0',UTC_TIMESTAMP(), 0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387112848,UUID(),240111044331053735, 13905, '深圳市',  3912, '光明新区' ,'怡景工业城A5栋-5','怡景工业城A5栋','5','2','0',UTC_TIMESTAMP(), 0);

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1004375, UUID(), '怡景管理处', '怡景管理处', 1, 1, 1004005, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 181966, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(181966, UUID(), 0, 2, 'EhGroups', 1004375,'怡景管理处','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());       
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(1004005, 1002756, 'PM', '怡景管理处', '', '/1002756/1004005', 1, 2, 'ENTERPRISE', 0, 1004375);
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, create_time)
	VALUES(2110301, 1004005, 'USER', 231350, 'manager', '刘兰', 0, '13728614307', 3, UTC_TIMESTAMP());	

INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(11529, 'EhOrganizations', 1004005, 'EhUsers', 231350, 1001, 1, UTC_TIMESTAMP());


INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20841, 1004005, 240111044331053735, 239825274387112816, '管理处大楼-301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20842, 1004005, 240111044331053735, 239825274387112817, '管理处大楼-302', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20843, 1004005, 240111044331053735, 239825274387112818, '管理处大楼-303', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20844, 1004005, 240111044331053735, 239825274387112819, '管理处大楼-304', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20845, 1004005, 240111044331053735, 239825274387112820, '管理处大楼-305', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20846, 1004005, 240111044331053735, 239825274387112821, '管理处大楼-306', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20847, 1004005, 240111044331053735, 239825274387112822, '管理处大楼-401', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20848, 1004005, 240111044331053735, 239825274387112823, '管理处大楼-402', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20849, 1004005, 240111044331053735, 239825274387112824, '管理处大楼-403', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20850, 1004005, 240111044331053735, 239825274387112825, '管理处大楼-404', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20851, 1004005, 240111044331053735, 239825274387112826, '管理处大楼-405', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20852, 1004005, 240111044331053735, 239825274387112827, '管理处大楼-406', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20853, 1004005, 240111044331053735, 239825274387112828, '管理处大楼-501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20854, 1004005, 240111044331053735, 239825274387112829, '管理处大楼-502', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20855, 1004005, 240111044331053735, 239825274387112830, '管理处大楼-503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20856, 1004005, 240111044331053735, 239825274387112831, '管理处大楼-504', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20857, 1004005, 240111044331053735, 239825274387112832, '管理处大楼-505', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20858, 1004005, 240111044331053735, 239825274387112833, '管理处大楼-506', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20859, 1004005, 240111044331053735, 239825274387112834, '怡景工业城A4栋-1A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20860, 1004005, 240111044331053735, 239825274387112835, '怡景工业城A4栋-1B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20861, 1004005, 240111044331053735, 239825274387112836, '怡景工业城A4栋-2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20862, 1004005, 240111044331053735, 239825274387112837, '怡景工业城A4栋-3A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20863, 1004005, 240111044331053735, 239825274387112838, '怡景工业城A4栋-3B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20864, 1004005, 240111044331053735, 239825274387112839, '怡景工业城A4栋-4A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20865, 1004005, 240111044331053735, 239825274387112840, '怡景工业城A4栋-4B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20866, 1004005, 240111044331053735, 239825274387112841, '怡景工业城A4栋-5', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20867, 1004005, 240111044331053735, 239825274387112842, '怡景工业城A5栋-1A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20868, 1004005, 240111044331053735, 239825274387112843, '怡景工业城A5栋-1B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20869, 1004005, 240111044331053735, 239825274387112844, '怡景工业城A5栋-1C', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20870, 1004005, 240111044331053735, 239825274387112845, '怡景工业城A5栋-2', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20871, 1004005, 240111044331053735, 239825274387112846, '怡景工业城A5栋-3', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20872, 1004005, 240111044331053735, 239825274387112847, '怡景工业城A5栋-4', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20873, 1004005, 240111044331053735, 239825274387112848, '怡景工业城A5栋-5', '0');
	

-- layout	
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type, apply_policy,scope_code,scope_id) 
	VALUES (374, '0', 'ServiceMarketLayout', '{"versionCode":"2015072815","versionName":"3.0.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"CmntyServices"},"style":"Gallery","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":1},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0}]}', '2015082914', '2015061701', '2', '2015-06-24 16:09:30', 'park_tourist', 3, 1, 240111044331053735);
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type, apply_policy,scope_code,scope_id) 
	VALUES (375, 0, 'PmLayout', '{"versionCode":"2015120406","versionName":"3.0.0","displayName":"物业","layoutName":"PmLayout","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GaActions"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"Posts","instanceConfig":{"itemGroup":"GaPosts"},"style":"Default","defaultOrder":3,"separatorFlag":0,"separatorHeight":0},{"groupName":"CallPhone","widget":"CallPhones","instanceConfig":{"itemGroup":"CallPhones","position":"bottom"},"style":"Default","defaultOrder":3,"separatorFlag":0,"separatorHeight":0}]}', '2015120406', '0', '2', '2015-06-27 14:04:57', 'park_tourist', 3, 1, 240111044331053735);

INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type, apply_policy,scope_code,scope_id) 
	VALUES (376, '0', 'ServiceMarketLayout', '{"versionCode":"2015072815","versionName":"3.0.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"CmntyServices"},"style":"Gallery","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":1},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0}]}', '2015082914', '2015061701', '2', '2015-06-24 16:09:30', 'pm_admin', 3, 1, 240111044331053735);
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type, apply_policy,scope_code,scope_id) 
	VALUES (377, 0, 'PmLayout', '{"versionCode":"2015120406","versionName":"3.0.0","displayName":"物业","layoutName":"PmLayout","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GaActions"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"Posts","instanceConfig":{"itemGroup":"GaPosts"},"style":"Default","defaultOrder":3,"separatorFlag":0,"separatorHeight":0},{"groupName":"CallPhone","widget":"CallPhones","instanceConfig":{"itemGroup":"CallPhones","position":"bottom"},"style":"Default","defaultOrder":3,"separatorFlag":0,"separatorHeight":0}]}', '2015120406', '0', '2', '2015-06-27 14:04:57', 'pm_admin', 3, 1, 240111044331053735);


INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`) 
    VALUES (10785, 0, 0, '/home', 'Default', 1, 240111044331053735, 'dongfangjianfu', 'dongfangjianfu', 'cs://1/image/aW1hZ2UvTVRvNVlUSm1OalkzTTJNMk5qaGpZV0ZtTUdVek56bGtNbVEyWVRabVltRXpNZw', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'park_tourist', 3);
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`) 
    VALUES (10786, 0, 0, '/home', 'Default', 1, 240111044331053735, 'dongfangjianfu', 'dongfangjianfu', 'cs://1/image/aW1hZ2UvTVRvNVlUSm1OalkzTTJNMk5qaGpZV0ZtTUdVek56bGtNbVEyWVRabVltRXpNZw', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'pm_admin', 3);
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`) 
    VALUES (10787, 0, 0, '/home', 'Default', 1, 240111044331053735, 'dongfangjianfu', 'dongfangjianfu', 'cs://1/image/aW1hZ2UvTVRveVlUSTBOVEF6TVdJM016STJaVE00WlRBNU1URmhZbVF6WldVelpqTTJOUQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'park_tourist', 3);
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`) 
    VALUES (10788, 0, 0, '/home', 'Default', 1, 240111044331053735, 'dongfangjianfu', 'dongfangjianfu', 'cs://1/image/aW1hZ2UvTVRveVlUSTBOVEF6TVdJM016STJaVE00WlRBNU1URmhZbVF6WldVelpqTTJOUQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'pm_admin', 3);


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (111277, 0, 0, 1, 240111044331053735, '/home', 'CmntyServices', 'CmntyActivities', '园区活动', 'cs://1/image/aW1hZ2UvTVRvM09UazJOVGd4TVRFek5qaGxNMkl5TlRneU1tTmlZVFEwTURJNVpUWTJNZw', '1', '1', 50,'', '0', '3', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');        
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (111278, 0, 0, 1, 240111044331053735, '/home', 'CmntyServices', 'CmntyActivities', '园区活动', 'cs://1/image/aW1hZ2UvTVRvM09UazJOVGd4TVRFek5qaGxNMkl5TlRneU1tTmlZVFEwTURJNVpUWTJNZw', '1', '1', 50,'', '0', '3', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');        


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
	VALUES (111279, 0, 0, 1, 240111044331053735, '/home', 'Bizs', 'PM', '物业服务', 'cs://1/image/aW1hZ2UvTVRvMVlqVTBNMll5WlRoak9EUTRaRGN4TlRBMk9HSTJabUZtWVRaaU1EWTFOUQ', '1', '1', '2', '{\"itemLocation\":\"/home/Pm\",\"layoutName\":\"PmLayout\",\"title\":\"物业服务\",\"entityTag\":\"PM\"}', '0', '3', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
	VALUES (111280, 0, 0, 1, 240111044331053735, '/home', 'Bizs', 'PM', '物业服务', 'cs://1/image/aW1hZ2UvTVRvMVlqVTBNMll5WlRoak9EUTRaRGN4TlRBMk9HSTJabUZtWVRaaU1EWTFOUQ', '1', '1', '2', '{\"itemLocation\":\"/home/Pm\",\"layoutName\":\"PmLayout\",\"title\":\"物业服务\",\"entityTag\":\"PM\"}', '0', '3', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0');
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (111281, 0, 0, 1, 240111044331053735, '/home/Pm', 'GaActions', 'ADVISE', '投诉建议', 'cs://1/image/aW1hZ2UvTVRvMVl6aGlOekkxT0dGbVlXSTBZbVZtTURjMU5EQmtOMkU0WmpCbU1tSXpOdw', 1, 1, 19, '{"contentCategory":1006,"actionCategory":0,"forumId":1,"targetEntityTag":"PM","embedAppId":27,"visibleRegionType":0}', 0, 3, 1, 1, '', 0,NULL,NULL,NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (111282, 0, 0, 1, 240111044331053735, '/home/Pm', 'GaActions', 'HELP', '咨询求助', 'cs://1/image/aW1hZ2UvTVRvMFlqazJOalppWkRjME4yTXpOVEE1TlRnNFlqRTFNemMwWXpjeVlqTTNZUQ', 1, 1, 19, '{"contentCategory":1005,"actionCategory":0,"forumId":1,"targetEntityTag":"PM","embedAppId":27,"visibleRegionType":0}', 0, 3, 1, 1, '', 0,NULL,NULL,NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (111283, 0, 0, 1, 240111044331053735, '/home/Pm', 'GaActions', 'REPAIR', '报修', 'cs://1/image/aW1hZ2UvTVRwaFpqUmxNRGd6WkRJME1URmtNMlJtWm1Sak1USmtNamN6WTJJelltSTJZUQ', 1, 1, 19, '{"contentCategory":1004,"actionCategory":0,"forumId":1,"targetEntityTag":"PM","embedAppId":27,"visibleRegionType":0}', 0, 3, 1, 1, '', 0,NULL,NULL,NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (111284, 0, 0, 1, 240111044331053735, '/home/Pm', 'GaPosts', 'ADVISE', '投诉建议', NULL, 1, 1, 15, '{"contentCategory":1006,"actionCategory":0,"forumId":1,"embedAppId":27}', 0, 3, 1, 1, '', 0,NULL,NULL,NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (111285, 0, 0, 1, 240111044331053735, '/home/Pm', 'GaPosts', 'HELP', '咨询求助', NULL, 1, 1, 15, '{"contentCategory":1005,"actionCategory":0,"forumId":1,"embedAppId":27}', 0, 3, 1, 1, '', 0,NULL,NULL,NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (111286, 0, 0, 1, 240111044331053735, '/home/Pm', 'GaPosts', 'REPAIR', '报修', NULL, 1, 1, 15, '{"contentCategory":1004,"actionCategory":0,"forumId":1,"embedAppId":27}', 0, 3, 1, 1, '', 0,NULL,NULL,NULL, '0', 'pm_admin');


INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (111287, 0, 0, 1, 240111044331053735, '/home/Pm', 'GaActions', 'ADVISE', '投诉建议', 'cs://1/image/aW1hZ2UvTVRvMVl6aGlOekkxT0dGbVlXSTBZbVZtTURjMU5EQmtOMkU0WmpCbU1tSXpOdw', 1, 1, 19, '{"contentCategory":1006,"actionCategory":0,"forumId":1,"targetEntityTag":"PM","embedAppId":27,"visibleRegionType":0}', 0, 3, 1, 1, '', 0,NULL,NULL,NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (111288, 0, 0, 1, 240111044331053735, '/home/Pm', 'GaActions', 'HELP', '咨询求助', 'cs://1/image/aW1hZ2UvTVRvMFlqazJOalppWkRjME4yTXpOVEE1TlRnNFlqRTFNemMwWXpjeVlqTTNZUQ', 1, 1, 19, '{"contentCategory":1005,"actionCategory":0,"forumId":1,"targetEntityTag":"PM","embedAppId":27,"visibleRegionType":0}', 0, 3, 1, 1, '', 0,NULL,NULL,NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (111289, 0, 0, 1, 240111044331053735, '/home/Pm', 'GaActions', 'REPAIR', '报修', 'cs://1/image/aW1hZ2UvTVRwaFpqUmxNRGd6WkRJME1URmtNMlJtWm1Sak1USmtNamN6WTJJelltSTJZUQ', 1, 1, 19, '{"contentCategory":1004,"actionCategory":0,"forumId":1,"targetEntityTag":"PM","embedAppId":27,"visibleRegionType":0}', 0, 3, 1, 1, '', 0,NULL,NULL,NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (111290, 0, 0, 1, 240111044331053735, '/home/Pm', 'GaPosts', 'ADVISE', '投诉建议', NULL, 1, 1, 15, '{"contentCategory":1006,"actionCategory":0,"forumId":1,"embedAppId":27}', 0, 3, 1, 1, '', 0,NULL,NULL,NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (111291, 0, 0, 1, 240111044331053735, '/home/Pm', 'GaPosts', 'HELP', '咨询求助', NULL, 1, 1, 15, '{"contentCategory":1005,"actionCategory":0,"forumId":1,"embedAppId":27}', 0, 3, 1, 1, '', 0,NULL,NULL,NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (111292, 0, 0, 1, 240111044331053735, '/home/Pm', 'GaPosts', 'REPAIR', '报修', NULL, 1, 1, 15, '{"contentCategory":1004,"actionCategory":0,"forumId":1,"embedAppId":27}', 0, 3, 1, 1, '', 0,NULL,NULL,NULL, '0', 'park_tourist');


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (111293, 0, 0, 1, 240111044331053735, '/home', 'Bizs', 'SERVICE_HOT_LINE', '服务热线', 'cs://1/image/aW1hZ2UvTVRvM09ETTRZakUwTXpFNVpUVTNaR0kzWW1JMlpqSTJNMlF4WmpRMVpXWXhNdw', '1', '1', '45', '', '0', '3', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (111294, 0, 0, 1, 240111044331053735, '/home', 'Bizs', 'SERVICE_HOT_LINE', '服务热线', 'cs://1/image/aW1hZ2UvTVRvM09ETTRZakUwTXpFNVpUVTNaR0kzWW1JMlpqSTJNMlF4WmpRMVpXWXhNdw', '1', '1', '45', '', '0', '3', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
	VALUES (111295, 0, 0, 1, 240111044331053735, '/home', 'Bizs', 'CONTACTS', '通讯录', 'cs://1/image/aW1hZ2UvTVRwak16azJOelEwWW1RNU5HRTFZalF4T1dGaE1qWTBOelE1TVRjNU4yTmhNQQ', '1', '1', '46', '', '0', '3', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
	VALUES (111296, 0, 0, 1, 240111044331053735, '/home', 'Bizs', 'CONTACTS', '通讯录', 'cs://1/image/aW1hZ2UvTVRwak16azJOelEwWW1RNU5HRTFZalF4T1dGaE1qWTBOelE1TVRjNU4yTmhNQQ', '1', '1', '46', '', '0', '3', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
	VALUES (111297, 0, 0, 1, 240111044331053735, '/home', 'Bizs', 'PUNCH', '打卡考勤', 'cs://1/image/aW1hZ2UvTVRwak9EUTVaRFF4TmpkbE5UTmxNalF4WTJRM05UUXdOMlJtTWpaaU5XUm1Odw', '1', '1', '23', '', '0', '3', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
	VALUES (111298, 0, 0, 1, 240111044331053735, '/home', 'Bizs', 'PUNCH', '打卡考勤', 'cs://1/image/aW1hZ2UvTVRwak9EUTVaRFF4TmpkbE5UTmxNalF4WTJRM05UUXdOMlJtTWpaaU5XUm1Odw', '1', '1', '23', '', '0', '3', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (111299, 0, 0, 1, 240111044331053735, '/home', 'Bizs', 'VIPPARKING', 'VIP车位', 'cs://1/image/aW1hZ2UvTVRwaU9EazVZalU0TVRjMVlqVmxNakl4T0dFeU16UTNaREZoT1dJd09HRXlNZw', '1', '1', '49', '{\"resourceTypeId\":8,\"pageType\":0}', '0', '3', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (111300, 0, 0, 1, 240111044331053735, '/home', 'Bizs', 'VIPPARKING', 'VIP车位', 'cs://1/image/aW1hZ2UvTVRwaU9EazVZalU0TVRjMVlqVmxNakl4T0dFeU16UTNaREZoT1dJd09HRXlNZw', '1', '1', '49', '{\"resourceTypeId\":8,\"pageType\":0}', '0', '3', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (111301, 0, 0, 1, 240111044331053735, '/home', 'Bizs', 'VIDEO_MEETING', '视频会议', 'cs://1/image/aW1hZ2UvTVRvMU5qVmhOakUzTm1ZM01HSm1aakpqTURVeVltTXhZemhsT1dRek5HWTRPUQ', '1', '1', '27', '', '0', '3', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (111302, 0, 0, 1, 240111044331053735, '/home', 'Bizs', 'VIDEO_MEETING', '视频会议', 'cs://1/image/aW1hZ2UvTVRvMU5qVmhOakUzTm1ZM01HSm1aakpqTURVeVltTXhZemhsT1dRek5HWTRPUQ', '1', '1', '27', '', '0', '3', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist');

-- 删除东方建富线网重复用户 by sw 2016/09/21
DELETE from eh_users where id = 229429 and namespace_id = 0;
DELETE from eh_user_identifiers where owner_uid = 229429 and namespace_id = 0;
DELETE from eh_organization_members where target_id = 229429;

-- 东方建富2菜单 by sw 2016/09/21
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 10000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 11000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 11100, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 12000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 30000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 30500, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 31000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 32000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 33000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 34000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 35000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 36000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 40000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 43400, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 43410, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 43420, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 43430, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 43440, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 46000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 49600, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 50000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 51000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 51100, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 52000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 52100, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 52200, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 52300, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 52400, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 53000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 53100, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 55000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 55100, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 55200, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 56000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 56100, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 56105, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 56106, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 56107, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 56108, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 56110, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 56120, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 56130, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 56140, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 56150, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 56160, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 56170, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 56181, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 56186, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 56191, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 56200, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 56210, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 56220, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 56230, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 58200, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 58210, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 58211, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 58212, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 58220, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 58221, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 58222, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 58230, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 58231, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 21000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 22000, '', 'EhOrganizations', 1004005 , 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 23000, '', 'EhOrganizations', 1004005 , 2);

