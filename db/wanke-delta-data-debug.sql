-- 万科初始数据
SET @eh_configurations_id = (SELECT MAX(id) FROM `eh_community_geopoints`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
    VALUES ((@eh_configurations_id := @eh_configurations_id + 1), 'wanke.mashen.url', 'https://api.open.imasheng.com/openapi', 'the url for wanke.mashen', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
    VALUES ((@eh_configurations_id := @eh_configurations_id + 1), 'wanke.mashen.appId', '15725632', 'the appId of wanke.mashen', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
    VALUES ((@eh_configurations_id := @eh_configurations_id + 1), 'wanke.mashen.appSecret', 'b4b994d5ed0a9990', 'the appSecret of wanke.mashen', 0, NULL);
INSERT INTO `eh_community_services` (`id`, `namespace_id`, `owner_type`, `owner_id`, `scope_code`, `scope_id`, `item_name`, `item_label`, `icon_uri`, `action_type`, `action_data`, `scene_type`) 
	VALUES ('1', '0', 'community', '240111044331051460', '1', '1', '物业报修', '物业报修', 'cs://1/image/aW1hZ2UvTVRveU5tSXhOMlV6TmpSak5qQmpOMlk1T1RVMFpqSTNPR0U1TXpNMll6QmlNdw', '1', '{\"contentCategory\":1004,\"actionCategory\":0,\"forumId\":1,\"embedAppId\":27,\"entityTag\":\"PM\"}', 'default');

SET @eh_organizations_id = (SELECT MAX(id) FROM `eh_community_geopoints`);
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(1012757, 0, 'PM', '万科test', '', '/1012757', 1, 2, 'ENTERPRISE', 0, 1005093);
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1005093, UUID(), '万科test', '万科test', 1, 1, 1012757, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 181772, 1, 0); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(181772, UUID(), 0, 2, 'EhGroups', 1005093,'万科test','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());

INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES( 240111045331053517, UUID(), 14978, '深圳市',  14979, '南山区', '万科test', '万科test', '深圳市南山区高新南九道', NULL, '高新区里程碑式的研发办公建筑，企业总部基地。运用科技和设计，打造甲级品质的节能、低耗、绿色生态商务空间，塑造立体的艺术、活力、科技体验生活方式中心，为高新园区产业升级提供了宝贵的空间载体。', NULL, NULL, NULL, NULL, NULL, NULL,NULL, 113, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'1', 1, 2, UTC_TIMESTAMP(), 0);
SET @eh_community_geopoints_id = (SELECT MAX(id) FROM `eh_community_geopoints`);

INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES((@eh_community_geopoints_id := @eh_community_geopoints_id + 1), 240111045331053517, '', 113.956081, 22.533245, 'ws101nh39jkd');	
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(1012757, 240111045331053517);

UPDATE `eh_organizations` SET `namespace_organization_token` = '80320', `namespace_organization_type` = 'wanke' WHERE `id` = '1012757';

