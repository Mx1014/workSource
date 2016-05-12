INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'forum', '107', 'zh_CN', '本园区');

INSERT INTO `eh_scene_types` (`id`, `namespace_id`, `name`, `display_name`, `create_time`, `parent_id`) VALUES(3, 0, 'family', '家庭场景', '2016-05-11 09:15:50', 1);
INSERT INTO `eh_scene_types` (`id`, `namespace_id`, `name`, `display_name`, `create_time`, `parent_id`) VALUES(1001, 0, 'park_tourist', '园区游客场景', '2016-05-11 09:15:50', 0);
INSERT INTO `eh_scene_types` (`id`, `namespace_id`, `name`, `display_name`, `create_time`, `parent_id`) VALUES(1002, 0, 'park_enterprise', '园区加入公司且认证通过的场景', '2016-05-11 09:15:50', 1001);
INSERT INTO `eh_scene_types` (`id`, `namespace_id`, `name`, `display_name`, `create_time`, `parent_id`) VALUES(1003, 0, 'park_enterprise_noauth', '园区加入公司且未认证通过的场景', '2016-05-11 09:15:50', 1001);
INSERT INTO `eh_scene_types` (`id`, `namespace_id`, `name`, `display_name`, `create_time`, `parent_id`) VALUES(1004, 0, 'park_pm_admin', '园区管理员场景', '2016-05-11 09:15:50', 0);

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




|       1 |            0 |         0 | 万科城物业管理处                                 | ENTERPRISE | PM                |      2 |
|       2 |            0 |         0 | 龙悦居物业管理处                                 | ENTERPRISE | PM                |      2 |
|       3 |            0 |         0 | 天鹅堡物业管理处                                 | ENTERPRISE | PM                |      2 |
|       4 |            0 |         0 | 蔚蓝海岸物业                                     | ENTERPRISE | PM                |      2 |
| 1000001 |      1000000 |         0 | 深圳科技工业园（集团）有限公司                   | ENTERPRISE | PM                |      2 |
| 1000100 |       999999 |         0 | 讯美科技                                         | ENTERPRISE | PM                |      2 |
| 1000106 |            0 |         0 | 天通苑北二区物业                                 | ENTERPRISE | PM                |      2 |
| 1000531 |       999994 |         0 | 龙岗智慧社区物业                                 | ENTERPRISE | PM                |      2 |
| 1000631 |       999993 |         0 | 深圳市海岸物业管理有限公司                       | ENTERPRISE | PM                |      2 |
| 1000716 |       999992 |   1000714 | 岭秀名苑                                         | ENTERPRISE | PM                |      2 |
| 1000717 |       999992 |   1000714 | 风临左岸                                         | ENTERPRISE | PM                |      2 |
| 1000718 |       999992 |   1000714 | 关山月美术馆                                     | ENTERPRISE | PM                |      2 |
| 1000719 |       999992 |   1000714 | 紫荆苑                                           | ENTERPRISE | PM                |      2 |
| 1000720 |       999992 |   1000714 | 东海附小                                         | ENTERPRISE | PM                |      2 |
| 1000750 |       999992 |         0 | 深业集团（深圳）物业管理有限公司                 | ENTERPRISE | PM                |      2 |
| 1000760 |       999992 |   1000710 | 经营部                                           | ENTERPRISE | PM                |      2 |
| 1000761 |       999992 |   1000710 | 财务部                                           | ENTERPRISE | PM                |      2 |
| 1000762 |       999992 |   1000710 | 发展部                                           | ENTERPRISE | PM                |      2 |
| 1000763 |       999992 |   1000710 | 法律事务部                                       | ENTERPRISE | PM                |      2 |
| 1000764 |       999992 |   1000710 | 安保部                                           | ENTERPRISE | PM                |      2 |
| 1000765 |       999992 |   1000710 | 人力资源部                                       | ENTERPRISE | PM                |      2 |
| 1000777 |       999992 |   1000710 | 行政部                                           | ENTERPRISE | PM                |      2 |
| 1000955 |       999992 |   1000750 | 长沙分公司                                       | ENTERPRISE | PM                |      2 |








	