INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1001927, UUID(), '成都市虚拟现实科技有限公司', '成都市虚拟现实科技有限公司', 1, 0, 0, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 180620, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(180620, UUID(), 0, 2, 'EhGroups', 1001927,'成都市虚拟现实科技有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
	
INSERT INTO `eh_user_groups` (`id`,  `owner_uid`,  `group_discriminator`,  `group_id`,  `region_scope`,  `region_scope_id`,  `member_role`,  `member_status`,  `create_time`)
	VALUES (318030, 192642, 'enterprise', 1001620, 0, 0, 7, 3, UTC_TIMESTAMP());		
	
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`,`level`, `status`, `group_type`, `group_id`, `namespace_id`)
	VALUES(1001620, 0, 'ENTERPRISE', '成都市虚拟现实科技有限公司', 0, NULL, '/1001620', 1, 2, 'ENTERPRISE', 1001927, 0); 	
INSERT INTO `eh_organization_members` (`id`,  `organization_id`,  `target_type`,  `target_id`,  `member_group`,  `contact_name`,  `contact_type`,  `contact_token`,  `contact_description`,  `status`)
	VALUES (2103060, 1001620, 'USER', 192642, 'manager', '李磊', 0, '15307935896', NULL, 3);	
INSERT INTO `eh_acl_role_assignments` (`id`,  `owner_type`,  `owner_id`,  `target_type`,  `target_id`,  `role_id`,  `creator_uid`,  `create_time`)
	VALUES (10663, 'EhOrganizations', 1001620, 'EhUsers', 192642, 1005, 0, UTC_TIMESTAMP());
INSERT INTO `eh_organization_community_requests`(`id`, `community_id`, `member_type`, `member_id`, `member_status`, `create_time`, `update_time`) 
    VALUES(1109999,240111044331051380, 'organization', 1001620, 3, UTC_TIMESTAMP(), UTC_TIMESTAMP());