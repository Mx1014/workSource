
INSERT INTO `eh_configurations`(`name`, `value`, `description`) VALUES ('biz.op.serverUrl', 'https://biz.zuolin.com/zl-ec', '弹窗的电商服务器地址');
INSERT INTO `eh_configurations`(`name`, `value`, `description`) VALUES ('biz.op.appKey', '39628d1c-0646-4ff6-9691-2c327b03f9c4', '弹窗的appKey');
INSERT INTO `eh_configurations`(`name`, `value`, `description`) VALUES ('biz.op.secretKey', 'PSsIB9nZm3ENS3stei8oAvGa2afRW7wT+UxBn9li4C7JCfjCtHYJY6x76XDtUCUcXOUhkPYK9V/5r03pD2rquQ==', '弹窗的secretKey');

-- 海岸物业初始数据
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES 
('94', 'haian.siyuan', 'http://183.62.221.2:8082/NetApp/SYS86Service.asmx/GetSYS86Service', 'the url of siyuan wuye', '0', NULL);
INSERT INTO `eh_pmsy_communities` (`id`, `namespace_id`, `community_id`, `community_token`, `contact`, `bill_tip`) VALUES 
('1', '0', '24210090697427178', '00100120131200000003', '联系电话', '提示信息');
INSERT INTO `eh_pmsy_communities` (`id`, `namespace_id`, `community_id`, `community_token`, `contact`, `bill_tip`) VALUES 
('2', '0', '24210090697427178', '00100120131200000005', '联系电话', '提示信息');
INSERT INTO `eh_pmsy_communities` (`id`, `namespace_id`, `community_id`, `community_token`, `contact`, `bill_tip`) VALUES 
('3', '0', '24210090697427178', '00100120131200000007', '联系电话', '提示信息');
INSERT INTO `eh_pmsy_communities` (`id`, `namespace_id`, `community_id`, `community_token`, `contact`, `bill_tip`) VALUES 
('4', '0', '24210090697427178', '00100120131200000009', '联系电话', '提示信息');
INSERT INTO `eh_pmsy_communities` (`id`, `namespace_id`, `community_id`, `community_token`, `contact`, `bill_tip`) VALUES 
('5', '0', '24210090697427178', '00100120131200000011', '联系电话', '提示信息');
INSERT INTO `eh_pmsy_communities` (`id`, `namespace_id`, `community_id`, `community_token`, `contact`, `bill_tip`) VALUES 
('6', '0', '24210090697427178', '00100120131200000013', '联系电话', '提示信息');
INSERT INTO `eh_pmsy_communities` (`id`, `namespace_id`, `community_id`, `community_token`, `contact`, `bill_tip`) VALUES 
('7', '0', '24210090697427178', '00100120131200000015', '联系电话', '提示信息');
INSERT INTO `eh_pmsy_communities` (`id`, `namespace_id`, `community_id`, `community_token`, `contact`, `bill_tip`) VALUES 
('8', '0', '24210090697427178', '00100120131200000017', '联系电话', '提示信息');



-- 20160629
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1001899, UUID(), '上海创源科技发展有限公司', '上海创源科技发展有限公司', 1, 0, 0, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 180599, 1, 0);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(180599, UUID(), 0, 2, 'EhGroups', 1001899,'上海创源科技发展有限公司','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_user_groups` (`id`,  `owner_uid`,  `group_discriminator`,  `group_id`,  `region_scope`,  `region_scope_id`,  `member_role`,  `member_status`,  `create_time`)
	VALUES (317987, 218291, 'enterprise', 1001599, 0, 0, 7, 3, UTC_TIMESTAMP());
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`,`level`, `status`, `group_type`, `group_id`, `namespace_id`)
	VALUES(1001599, 0, 'ENTERPRISE', '上海创源科技发展有限公司', 0, NULL, '/1001599', 1, 2, 'ENTERPRISE', 1001899, 0);
INSERT INTO `eh_organization_members` (`id`,  `organization_id`,  `target_type`,  `target_id`,  `member_group`,  `contact_name`,  `contact_type`,  `contact_token`,  `contact_description`,  `status`)
	VALUES (2102950, 1001599, 'USER', 218291, 'manager', '王立杰', 0, '13917202938', NULL, 3);
INSERT INTO `eh_acl_role_assignments` (`id`,  `owner_type`,  `owner_id`,  `target_type`,  `target_id`,  `role_id`,  `creator_uid`,  `create_time`)
	VALUES (10640, 'EhOrganizations', 1001599, 'EhUsers', 218291, 1005, 0, UTC_TIMESTAMP());
INSERT INTO `eh_organization_community_requests`(`id`, `community_id`, `member_type`, `member_id`, `member_status`, `create_time`, `update_time`)
    VALUES(1109989,240111044331051380, 'organization', 1001599, 3, UTC_TIMESTAMP(), UTC_TIMESTAMP());