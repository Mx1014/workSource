INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`,`level`, `status`, `group_type`, `group_id`, `namespace_id`)
	VALUES(1000999, 0, 'ENTERPRISE', '上海荣广集团', 0, NULL, '/1000999', 1, 2, 'ENTERPRISE', 1000999, 0); 
	
INSERT INTO `eh_organization_members` (`id`,  `organization_id`,  `target_type`,  `target_id`,  `member_group`,  `contact_name`,  `contact_type`,  `contact_token`,  `contact_description`,  `status`)
	VALUES (2102399, 1000999, 'USER', 214082, 'manager', '沈昭', 0, '13901638145', NULL, 3);
	
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1000999, UUID(), '上海荣广集团', '上海荣广集团', 1, 0, 0, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 179999, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(179999, UUID(), 0, 2, 'EhGroups', 1000999,'上海荣广集团','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
    
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(10550, 'EhOrganizations', 1000999, 'EhUsers', 214082, 1001, 1, UTC_TIMESTAMP());