INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1001130, UUID(), '上海财经大学', '上海财经大学', 1, 0, 0, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 180030, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(180030, UUID(), 0, 2, 'EhGroups', 1001130,'上海财经大学','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
	
INSERT INTO `eh_user_groups` (`id`,  `owner_uid`,  `group_discriminator`,  `group_id`,  `region_scope`,  `region_scope_id`,  `member_role`,  `member_status`,  `create_time`)
	VALUES (317830, 216181, 'enterprise', 1001050, 0, 0, 7, 3, UTC_TIMESTAMP());		
	
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`,`level`, `status`, `group_type`, `group_id`, `namespace_id`)
	VALUES(1001050, 0, 'ENTERPRISE', '上海财经大学', 0, NULL, '/1001050', 1, 2, 'ENTERPRISE', 1001130, 0); 	
INSERT INTO `eh_organization_members` (`id`,  `organization_id`,  `target_type`,  `target_id`,  `member_group`,  `contact_name`,  `contact_type`,  `contact_token`,  `contact_description`,  `status`)
	VALUES (2102630, 1001050, 'USER', 216181, 'manager', '王琛', 0, '18621886136', NULL, 3);	
INSERT INTO `eh_acl_role_assignments` (`id`,  `owner_type`,  `owner_id`,  `target_type`,  `target_id`,  `role_id`,  `creator_uid`,  `create_time`)
	VALUES (10580, 'system', NULL, 'EhUsers', 216181, 1005, 0, UTC_TIMESTAMP());
INSERT INTO `eh_organization_community_requests`(`id`, `community_id`, `member_type`, `member_id`, `member_status`, `create_time`, `update_time`) 
    VALUES(111430,240111044331051380, 'organization', 1001050, 3, UTC_TIMESTAMP(), UTC_TIMESTAMP());

    
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1001129, UUID(), '左邻园区', '左邻园区', 1, 0, 0, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 180029, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(180029, UUID(), 0, 2, 'EhGroups', 1001129,'左邻园区','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
	
INSERT INTO `eh_user_groups` (`id`,  `owner_uid`,  `group_discriminator`,  `group_id`,  `region_scope`,  `region_scope_id`,  `member_role`,  `member_status`,  `create_time`)
	VALUES (317829, 192642, 'enterprise', 1001049, 0, 0, 7, 3, UTC_TIMESTAMP());		

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`,`level`, `status`, `group_type`, `group_id`, `namespace_id`)
	VALUES(1001049, 0, 'ENTERPRISE', '左邻园区物业', 0, NULL, '/1001049', 1, 2, 'ENTERPRISE', 1001129, 0);
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status)
	VALUES(2102629, 1001049, 'USER', 192642, 'manager', '李磊', 0, '15307935896', 3);	

INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(10579, 'EhOrganizations', 1001049, 'EhUsers', 192642, 1001, 1, UTC_TIMESTAMP());

INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(1001049, 240111044331051380);
INSERT INTO `eh_organization_community_requests`(`id`, `community_id`, `member_type`, `member_id`, `member_status`, `create_time`, `update_time`) 
    VALUES(111429, 240111044331051380, 'organization', 1001049, 3, UTC_TIMESTAMP(), UTC_TIMESTAMP());