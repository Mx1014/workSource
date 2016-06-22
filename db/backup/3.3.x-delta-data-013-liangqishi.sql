INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'post.menu.avatar.all', 'cs://1/image/aW1hZ2UvTVRveVlUTTNPR1ZqWW1ZelltWXpOR1F3TlRBMVkySTRNMlpqWlRVM09EUmlNUQ', '帖子菜单默认头像：全部');
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'post.menu.avatar.community_only', 'cs://1/image/aW1hZ2UvTVRwaE1HSmhaR0V5TWpWbFkyVm1ZakkxT0RKak9ETTVZemN6TnpFeU56bGhOZw', '帖子菜单默认头像：本小区');
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'post.menu.avatar.community_nearby', 'cs://1/image/aW1hZ2UvTVRvMllURTVZekJsTmpRelpXUXdabUZtTXpBM1lUWTNZVEZqT0dSbU9EbGlOZw', '帖子菜单默认头像：周边小区');
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'post.menu.avatar.organization', 'cs://1/image/aW1hZ2UvTVRvM1pUWXlaVEExTmpOaE5qWXhNVFEzWmpabE5ERmpPRFV4TkRWaE56RTJOUQ', '帖子菜单默认头像：公司');
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'post.menu.avatar.group', 'cs://1/image/aW1hZ2UvTVRwaE5HWTJaV0ptWlRJek4yTmtPR0UyTlRaaU5UQTNOekpqT0RsaU1UZzFaQQ', '帖子菜单默认头像：兴趣圈');
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'user.avatar.system_assistant', 'cs://1/image/aW1hZ2UvTVRwbU1ERXlaV05rWWpNME1UWTBaREJtWldFMVpUQmlPR0U1TVdJeVlXSmtaZw', '系统小助手默认头像');

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(1000795, UUID(), '深业物业','', 1, 0, 240111044331051300, 'group',  1, 1, '2016-04-28 22:05:33', '2016-04-28 22:05:33', 179902, 1, 999992); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(179902, UUID(), 999992, 2, 'EhGroups', 1000795,'深业物业论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
update eh_organizations set group_id=1000795 where id=1000750;


INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(1000796, UUID(), '深圳科技工业园','', 1, 0, 240111044331048623, 'group',  1, 1, '2016-04-28 22:05:33', '2016-04-28 22:05:33', 179903, 1, 1000000); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(179903, UUID(), 1000000, 2, 'EhGroups', 1000796,'深圳科技工业园论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
update eh_organizations set group_id=1000796 where id=1000001;	

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(1000797, UUID(), '讯美科技','', 1, 0, 240111044331049963, 'group',  1, 1, '2016-04-28 22:05:33', '2016-04-28 22:05:33', 179904, 1, 999999); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(179904, UUID(), 999999, 2, 'EhGroups', 1000797,'讯美科技论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
update eh_organizations set group_id=1000797 where id=1000100;	

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(1000798, UUID(), '龙岗智慧社区','', 1, 0, 240111044331050395, 'group',  1, 1, '2016-04-28 22:05:33', '2016-04-28 22:05:33', 179905, 1, 999994); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(179905, UUID(), 999994, 2, 'EhGroups', 1000798,'龙岗智慧论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
update eh_organizations set group_id=1000798 where id=1000531;

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES(1000799, UUID(), '海岸物业','', 1, 0, 240111044331050812, 'group',  1, 1, '2016-04-28 22:05:33', '2016-04-28 22:05:33', 179906, 1, 999993); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(179906, UUID(), 999993, 2, 'EhGroups', 1000799,'海岸物业论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
update eh_organizations set group_id=1000799 where id=1000631;
