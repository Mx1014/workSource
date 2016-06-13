INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`,`level`, `status`, `group_type`, `group_id`, `namespace_id`)
	VALUES(1000700, 0, 'ENTERPRISE', '上海联合通信有限公司', 0, NULL, '/1000700', 1, 2, 'ENTERPRISE', 1000700, 0); 
	
INSERT INTO `eh_organization_members` (`id`,  `organization_id`,  `target_type`,  `target_id`,  `member_group`,  `contact_name`,  `contact_type`,  `contact_token`,  `contact_description`,  `status`)
	VALUES (2101270, 1000700, 'USER', 70691, 'manager', '白顺来', 0, '18601721203', NULL, 3);


INSERT INTO `eh_enterprise_contacts` (`id`,  `enterprise_id`,  `name`,  `nick_name`,  `avatar`,  `user_id`,  `role`,  `status`,  `creator_uid`,  `create_time`)
	VALUES (101255, 1000700, '白顺来', '白顺来', NULL, '70691', '5', '3', NULL, UTC_TIMESTAMP());  
INSERT INTO `eh_enterprise_contact_entries` (`id`,  `enterprise_id`,  `contact_id`,  `entry_type`,  `entry_value`,  `creator_uid`,  `create_time`)
	VALUES (101205, 1000700, 101255, '0', '18601721203', NULL, UTC_TIMESTAMP());  

	
INSERT INTO `eh_organization_members` (`id`,  `organization_id`,  `target_type`,  `target_id`,  `member_group`,  `contact_name`,  `contact_type`,  `contact_token`,  `contact_description`,  `status`)
	VALUES (2101271, 1000700, 'USER', 192642, 'manager', '李磊', 0, '15307935896', NULL, 3);


INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(180540, UUID(), '上海联合通信有限公司', '上海联合通信有限公司', 1, 0, 0, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 179860, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(179860, UUID(), 0, 2, 'EhGroups', 180540,'上海联合通信有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 