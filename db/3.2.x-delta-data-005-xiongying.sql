INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(180340, UUID(), '深圳市金地商置集团有限公司', '深圳市金地商置集团有限公司', 1, 0, 0, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 179845, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(179845, UUID(), 0, 2, 'EhGroups', 180340,'深圳市金地商置集团有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
	
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (207050, UUID(), '9200890', '郑粤元', 'cs://1/image/aW1hZ2UvTVRvMU1EQTVZVEZrTkdVek9EQXhZbVE0WlRZd1l6UXdOVE0zWVdJNFkyTmlNUQ', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 0);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (216200, 207050,  '0',  '13808834210',  '221616',  3, UTC_TIMESTAMP(), 0);

INSERT INTO `eh_enterprise_contacts` (`id`,  `enterprise_id`,  `name`,  `nick_name`,  `avatar`,  `user_id`,  `role`,  `status`,  `creator_uid`,  `create_time`)
	VALUES (101200, 180340, '郑粤元', '郑粤元', NULL, '207050', '5', '3', NULL, UTC_TIMESTAMP());  
INSERT INTO `eh_enterprise_contact_entries` (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`)
	VALUES (101150, 180340, 101200, '0', '13808834210', NULL, UTC_TIMESTAMP());  
INSERT INTO `eh_user_groups` (`id`,  `owner_uid`,  `group_discriminator`,  `group_id`,  `region_scope`,  `region_scope_id`,  `member_role`,  `member_status`,  `create_time`)
	VALUES (316600, 207050, 'enterprise', 180340, 0, 0, 7, 3, UTC_TIMESTAMP());
	

		
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(180341, UUID(), '罗技中国科技有限公司', '罗技中国科技有限公司', 1, 0, 0, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 179846, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(179846, UUID(), 0, 2, 'EhGroups', 180341,'罗技中国科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
	
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (207051, UUID(), '9200891', 'zhenw', 'cs://1/image/aW1hZ2UvTVRvMU1EQTVZVEZrTkdVek9EQXhZbVE0WlRZd1l6UXdOVE0zWVdJNFkyTmlNUQ', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 0);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (216201, 207051,  '0',  '18516790899',  '221616',  3, UTC_TIMESTAMP(), 0);


INSERT INTO `eh_enterprise_contacts` (`id`,  `enterprise_id`,  `name`,  `nick_name`,  `avatar`,  `user_id`,  `role`,  `status`,  `creator_uid`,  `create_time`)
	VALUES (101201, 180341, 'zhenw', 'zhenw', NULL, '207051', '5', '3', NULL, UTC_TIMESTAMP());  
INSERT INTO `eh_enterprise_contact_entries` (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`)
	VALUES (101151, 180341, 101201, '0', '18516790899', NULL, UTC_TIMESTAMP());  
INSERT INTO `eh_user_groups` (`id`,  `owner_uid`,  `group_discriminator`,  `group_id`,  `region_scope`,  `region_scope_id`,  `member_role`,  `member_status`,  `create_time`)
	VALUES (316601, 207051, 'enterprise', 180341, 0, 0, 7, 3, UTC_TIMESTAMP());

	
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`,`level`, `status`, `group_type`, `group_id`, `namespace_id`)
	VALUES(180340, 0, 'ENTERPRISE', '深圳市金地商置集团有限公司', 0, NULL, '/180340', 1, 2, 'ENTERPRISE', 180340, 0); 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`,`level`, `status`, `group_type`, `group_id`, `namespace_id`)
	VALUES(180341, 0, 'ENTERPRISE', '罗技中国科技有限公司', 0, NULL, '/180341', 1, 2, 'ENTERPRISE', 180341, 0); 

	
INSERT INTO `eh_organization_members` (`id`,  `organization_id`,  `target_type`,  `target_id`,  `member_group`,  `contact_name`,  `contact_type`,  `contact_token`,  `contact_description`,  `status`)
	VALUES (2101140, 180340, 'USER', 207050, 'manager', '郑粤元', 0, '13808834210', NULL, 3);
INSERT INTO `eh_organization_members` (`id`,  `organization_id`,  `target_type`,  `target_id`,  `member_group`,  `contact_name`,  `contact_type`,  `contact_token`,  `contact_description`,  `status`)
	VALUES (2101141, 180341, 'USER', 207051, 'manager', 'zhenw', 0, '18516790899', NULL, 3);
	
	
INSERT INTO `eh_acl_role_assignments` (`id`,  `owner_type`,  `owner_id`,  `target_type`,  `target_id`,  `role_id`,  `creator_uid`,  `create_time`)
	VALUES (10309, 'system', NULL, 'EhUsers', 207050, 1005, 0, UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (`id`,  `owner_type`,  `owner_id`,  `target_type`,  `target_id`,  `role_id`,  `creator_uid`,  `create_time`)
	VALUES (10310, 'system', NULL, 'EhUsers', 207051, 1005, 0, UTC_TIMESTAMP());