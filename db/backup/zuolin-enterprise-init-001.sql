INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(180040, UUID(), '深圳市永佳天成科技发展有限公司', '左邻', 1, 0, 0, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 179820, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(179820, UUID(), 0, 2, 'EhGroups', 180040,'深圳市永佳天成科技发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(180041, UUID(), '深业集团（深圳）物业管理有限公司', '深业集团（深圳）物业管理有限公司', 1, 0, 0, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 179821, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(179821, UUID(), 0, 2, 'EhGroups', 180041,'深业集团（深圳）物业管理有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 

INSERT INTO `eh_enterprise_contacts` (`id`,  `enterprise_id`,  `name`,  `nick_name`,  `avatar`,  `user_id`,  `role`,  `status`,  `creator_uid`,  `create_time`)
	VALUES (101100, 180040, '林园', '林园', NULL, '113766', '5', '3', NULL, UTC_TIMESTAMP());  
INSERT INTO `eh_enterprise_contact_entries` (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`)
	VALUES (101054, 180040, 101100, '0', '13380343639', NULL, UTC_TIMESTAMP());  
INSERT INTO `eh_user_groups` (`id`,  `owner_uid`,  `group_discriminator`,  `group_id`,  `region_scope`,  `region_scope_id`,  `member_role`,  `member_status`,  `create_time`)
	VALUES (316100, 113766, 'enterprise', 180040, 0, 0, 7, 3, UTC_TIMESTAMP());

INSERT INTO `eh_enterprise_contacts` (`id`,  `enterprise_id`,  `name`,  `nick_name`,  `avatar`,  `user_id`,  `role`,  `status`,  `creator_uid`,  `create_time`)
	VALUES (101101, 180040, '李磊', '李磊', NULL, '192642', '7', '3', NULL, UTC_TIMESTAMP());  
INSERT INTO `eh_enterprise_contact_entries` (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`)
	VALUES (101055, 180040, 101101, '0', '15307935896', NULL, UTC_TIMESTAMP());  
INSERT INTO `eh_user_groups` (`id`,  `owner_uid`,  `group_discriminator`,  `group_id`,  `region_scope`,  `region_scope_id`,  `member_role`,  `member_status`,  `create_time`)
	VALUES (316101, 192642, 'enterprise', 180040, 0, 0, 7, 3, UTC_TIMESTAMP());
	
INSERT INTO `eh_enterprise_contacts` (`id`,  `enterprise_id`,  `name`,  `nick_name`,  `avatar`,  `user_id`,  `role`,  `status`,  `creator_uid`,  `create_time`)
	VALUES (101102, 180040, '郭晓晶', '郭晓晶', NULL, '1028', '7', '3', NULL, UTC_TIMESTAMP());  
INSERT INTO `eh_enterprise_contact_entries` (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`)
	VALUES (101056, 180040, 101102, '0', '18600158807', NULL, UTC_TIMESTAMP());  
INSERT INTO `eh_user_groups` (`id`,  `owner_uid`,  `group_discriminator`,  `group_id`,  `region_scope`,  `region_scope_id`,  `member_role`,  `member_status`,  `create_time`)
	VALUES (316102, 1028, 'enterprise', 180040, 0, 0, 7, 3, UTC_TIMESTAMP());
	
INSERT INTO `eh_enterprise_contacts` (`id`,  `enterprise_id`,  `name`,  `nick_name`,  `avatar`,  `user_id`,  `role`,  `status`,  `creator_uid`,  `create_time`)
	VALUES (101103, 180040, '姚业', '姚业', NULL, '10713', '7', '3', NULL, UTC_TIMESTAMP());  
INSERT INTO `eh_enterprise_contact_entries` (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`)
	VALUES (101057, 180040, 101103, '0', '15099931812', NULL, UTC_TIMESTAMP());  
INSERT INTO `eh_user_groups` (`id`,  `owner_uid`,  `group_discriminator`,  `group_id`,  `region_scope`,  `region_scope_id`,  `member_role`,  `member_status`,  `create_time`)
	VALUES (316103, 10713, 'enterprise', 180040, 0, 0, 7, 3, UTC_TIMESTAMP());
	
INSERT INTO `eh_enterprise_contacts` (`id`,  `enterprise_id`,  `name`,  `nick_name`,  `avatar`,  `user_id`,  `role`,  `status`,  `creator_uid`,  `create_time`)
	VALUES (101104, 180040, 'jack feng', 'jack feng', NULL, '1030', '7', '3', NULL, UTC_TIMESTAMP());  
INSERT INTO `eh_enterprise_contact_entries` (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`)
	VALUES (101058, 180040, 101104, '0', '13911950186', NULL, UTC_TIMESTAMP());  
INSERT INTO `eh_user_groups` (`id`,  `owner_uid`,  `group_discriminator`,  `group_id`,  `region_scope`,  `region_scope_id`,  `member_role`,  `member_status`,  `create_time`)
	VALUES (316104, 1030, 'enterprise', 180040, 0, 0, 7, 3, UTC_TIMESTAMP());


	
	
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (203600, UUID(), '9200900', '唐玮', 'cs://1/image/aW1hZ2UvTVRvMU1EQTVZVEZrTkdVek9EQXhZbVE0WlRZd1l6UXdOVE0zWVdJNFkyTmlNUQ', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 0);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (215340, 203600,  '0',  '13510551322',  '221616',  3, UTC_TIMESTAMP(), 0);

INSERT INTO `eh_enterprise_contacts` (`id`,  `enterprise_id`,  `name`,  `nick_name`,  `avatar`,  `user_id`,  `role`,  `status`,  `creator_uid`,  `create_time`)
	VALUES (101105, 180041, '唐玮', '唐玮', NULL, '203600', '5', '3', NULL, UTC_TIMESTAMP());  
INSERT INTO `eh_enterprise_contact_entries` (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`)
	VALUES (101059, 180041, 101105, '0', '13510551322', NULL, UTC_TIMESTAMP());  
INSERT INTO `eh_user_groups` (`id`,  `owner_uid`,  `group_discriminator`,  `group_id`,  `region_scope`,  `region_scope_id`,  `member_role`,  `member_status`,  `create_time`)
	VALUES (316105, 203600, 'enterprise', 180041, 0, 0, 7, 3, UTC_TIMESTAMP());
	
