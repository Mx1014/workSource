INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`,`level`, `status`, `group_type`, `group_id`, `namespace_id`)
	VALUES(1000705, 0, 'ENTERPRISE', '雅居乐中心', 0, NULL, '/1000705', 1, 2, 'ENTERPRISE', 1000705, 0); 
	
INSERT INTO `eh_organization_members` (`id`,  `organization_id`,  `target_type`,  `target_id`,  `member_group`,  `contact_name`,  `contact_type`,  `contact_token`,  `contact_description`,  `status`)
	VALUES (2101280, 1000705, 'USER', 211208, 'manager', '陈文杰', 0, '13600369922', NULL, 3);
INSERT INTO `eh_organization_members` (`id`,  `organization_id`,  `target_type`,  `target_id`,  `member_group`,  `contact_name`,  `contact_type`,  `contact_token`,  `contact_description`,  `status`)
	VALUES (2101281, 1000705, 'USER', 211209, 'manager', '梁鸿杰', 0, '18565064842', NULL, 3);
INSERT INTO `eh_organization_members` (`id`,  `organization_id`,  `target_type`,  `target_id`,  `member_group`,  `contact_name`,  `contact_type`,  `contact_token`,  `contact_description`,  `status`)
	VALUES (2101282, 1000705, 'USER', 211210, 'manager', '苏志幸', 0, '13631410336', NULL, 3);
	
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1000705, UUID(), '雅居乐中心', '雅居乐中心', 1, 0, 0, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 179865, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(179865, UUID(), 0, 2, 'EhGroups', 1000705,'雅居乐中心','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 