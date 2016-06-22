INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1001300, UUID(), '上海创造软件系统有限公司', '上海创造软件系统有限公司', 1, 0, 0, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 180050, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(180050, UUID(), 0, 2, 'EhGroups', 1001300,'上海创造软件系统有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
	
INSERT INTO `eh_user_groups` (`id`,  `owner_uid`,  `group_discriminator`,  `group_id`,  `region_scope`,  `region_scope_id`,  `member_role`,  `member_status`,  `create_time`)
	VALUES (317950, 217506, 'enterprise', 1001190, 0, 0, 7, 3, UTC_TIMESTAMP());		
	
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`,`level`, `status`, `group_type`, `group_id`, `namespace_id`)
	VALUES(1001190, 0, 'ENTERPRISE', '上海创造软件系统有限公司', 0, NULL, '/1001190', 1, 2, 'ENTERPRISE', 1001300, 0); 	
INSERT INTO `eh_organization_members` (`id`,  `organization_id`,  `target_type`,  `target_id`,  `member_group`,  `contact_name`,  `contact_type`,  `contact_token`,  `contact_description`,  `status`)
	VALUES (2102830, 1001190, 'USER', 217506, 'manager', '城市概念', 0, '13816624227', NULL, 3);	
INSERT INTO `eh_acl_role_assignments` (`id`,  `owner_type`,  `owner_id`,  `target_type`,  `target_id`,  `role_id`,  `creator_uid`,  `create_time`)
	VALUES (10630, 'EhOrganizations', 1001190, 'EhUsers', 217506, 1005, 0, UTC_TIMESTAMP());
INSERT INTO `eh_organization_community_requests`(`id`, `community_id`, `member_type`, `member_id`, `member_status`, `create_time`, `update_time`) 
    VALUES(1109020,240111044331051380, 'organization', 1001190, 3, UTC_TIMESTAMP(), UTC_TIMESTAMP());
