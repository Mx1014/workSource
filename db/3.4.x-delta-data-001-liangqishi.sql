INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'forum', '107', 'zh_CN', '本园区');

INSERT INTO `eh_scene_types` (`id`, `namespace_id`, `name`, `display_name`, `create_time`, `parent_id`) VALUES(3, 0, 'family', '家庭场景', '2016-05-11 09:15:50', 1);
INSERT INTO `eh_scene_types` (`id`, `namespace_id`, `name`, `display_name`, `create_time`, `parent_id`) VALUES(1001, 0, 'park_tourist', '园区游客场景', '2016-05-11 09:15:50', 0);
INSERT INTO `eh_scene_types` (`id`, `namespace_id`, `name`, `display_name`, `create_time`, `parent_id`) VALUES(1002, 0, 'park_enterprise', '园区加入公司且认证通过的场景', '2016-05-11 09:15:50', 1001);
INSERT INTO `eh_scene_types` (`id`, `namespace_id`, `name`, `display_name`, `create_time`, `parent_id`) VALUES(1003, 0, 'park_enterprise_noauth', '园区加入公司且未认证通过的场景', '2016-05-11 09:15:50', 1001);
INSERT INTO `eh_scene_types` (`id`, `namespace_id`, `name`, `display_name`, `create_time`, `parent_id`) VALUES(1004, 0, 'park_pm_admin', '园区管理员场景', '2016-05-11 09:15:50', 1001);

-- UPDATE `eh_organizations` SET `community_id`=24210090697425925 WHERE `id`=1; -- 万科城物业管理处
-- UPDATE `eh_organizations` SET `community_id`=24210090697427178 WHERE `id`=2; -- 龙悦居物业管理处
-- UPDATE `eh_organizations` SET `community_id`=24210090697425921 WHERE `id`=3; -- 天鹅堡物业管理处
-- UPDATE `eh_organizations` SET `community_id`=24210090697427064 WHERE `id`=4; -- 蔚蓝海岸物业
-- UPDATE `eh_organizations` SET `community_id`=240111044331048623 WHERE `id`=1000001; -- 深圳科技工业园（集团）有限公司
-- UPDATE `eh_organizations` SET `community_id`=240111044331049963 WHERE `id`=1000100; -- 讯美科技
-- UPDATE `eh_organizations` SET `community_id`=24206890946790810 WHERE `id`=1000106; -- 天通苑北二区物业
-- UPDATE `eh_organizations` SET `community_id`=240111044331050395 WHERE `id`=1000531; -- 龙岗智慧社区物业
-- UPDATE `eh_organizations` SET `community_id`=240111044331050812 WHERE `id`=1000631; -- 深圳市海岸物业管理有限公司
-- UPDATE `eh_organizations` SET `community_id`=0 WHERE `id`=1000750; -- 深业集团（深圳）物业管理有限公司 (暂时没有)

UPDATE `eh_banners` SET `scene_type`='park_tourist' WHERE `namespace_id` > 0;
UPDATE `eh_launch_pad_items` SET `scene_type`='park_tourist' WHERE `namespace_id` > 0;


-- 万科城物业管理处
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(110801, 24210090697425925, 'organization', 1, 3, 0, '2016-05-11 09:15:50');
-- 龙悦居物业管理处
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(110802, 24210090697427178, 'organization', 2, 3, 0, '2016-05-11 09:15:50');
-- 天鹅堡物业管理处	
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(110803, 24210090697425921, 'organization', 3, 3, 0, '2016-05-11 09:15:50');
-- 蔚蓝海岸物业	
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(110804, 24210090697427064, 'organization', 4, 3, 0, '2016-05-11 09:15:50');
-- 深圳科技工业园（集团）有限公司	
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(110805, 240111044331048623, 'organization', 1000001, 3, 0, '2016-05-11 09:15:50');
-- 讯美科技
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(110806, 240111044331049963, 'organization', 1000100, 3, 0, '2016-05-11 09:15:50');
-- 天通苑北二区物业
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(110807, 24206890946790810, 'organization', 1000106, 3, 0, '2016-05-11 09:15:50');
-- 龙岗智慧社区物业
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(110808, 240111044331050395, 'organization', 1000531, 3, 0, '2016-05-11 09:15:50');
-- 深圳市海岸物业管理有限公司
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(110809, 240111044331050812, 'organization', 1000631, 3, 0, '2016-05-11 09:15:50');

INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES(1001, 1000001, 'community_residential', '2016-05-12 18:07:50');
INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES(1002, 1000000, 'community_commercial', '2016-05-12 18:07:50');
INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES(1003, 999999, 'community_commercial', '2016-05-12 18:07:50');
INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES(1004, 999998, 'community_commercial', '2016-05-12 18:07:50');
INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES(1005, 999997, 'community_residential', '2016-05-12 18:07:50');
INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES(1006, 999996, 'community_residential', '2016-05-12 18:07:50');
INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES(1007, 999995, 'community_residential', '2016-05-12 18:07:50');
INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES(1008, 999994, 'community_residential', '2016-05-12 18:07:50');
INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES(1009, 999993, 'community_commercial', '2016-05-12 18:07:50');
INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES(1010, 999992, 'community_residential', '2016-05-12 18:07:50');







	