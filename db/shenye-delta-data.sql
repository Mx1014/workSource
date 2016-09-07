-- 深业

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) 
	VALUES(12,'服务预约','0',NULL,'0', 999992);

delete from eh_launch_pad_items where id = 10026;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
	VALUES ('10026', '999992', '0', '0', '0', '/home', 'Bizs', '服务预约', '服务预约', 'cs://1/image/aW1hZ2UvTVRvek5USmpZV1JtTm1FMll6UXdZVFpoWlRNeFlqVXdObU0xTXpRME16WmlPUQ', '1', '1', '49', '{\"resourceTypeId\":12,\"pageType\":1}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'default', '1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
	VALUES ('10026', '999992', '0', '0', '0', '/home', 'Bizs', '服务预约', '服务预约', 'cs://1/image/aW1hZ2UvTVRvek5USmpZV1JtTm1FMll6UXdZVFpoWlRNeFlqVXdObU0xTXpRME16WmlPUQ', '1', '1', '49', '{\"resourceTypeId\":12,\"pageType\":1}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '1');


INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES( 240111044331051304, UUID(), 14956, '深圳市',  14958, '罗湖区', '深业中心大厦', '深业中心大厦', '深圳市罗湖区深南东路5045号', NULL, '深业中心大厦地处深南大道的金融中心区，是由深业中心发展（深圳）有限公司投资开发的5A智能化高级写字楼。大厦总建筑面积73542M2，总高33层 155米，深圳证券交易所入驻其中，决定其具有极其特殊的地位和影响。大厦建立了准军事化的保安队伍，人防、技防相结合的防范网络，以及与派出所和其它小区保安队形成的快速反应体系。根据《全国城市物业管理大厦考核标准》，结合金融机构的运作特点和星级酒店服务要求，形成了独具特色的金融商厦管理模式。1998年被建设部授予"全国物业管理示范大厦"称号，1999年"全国物业管理工作会议"指定为高层写字楼参观点，建设部部长俞振声高度评价为"点睛之作"。', NULL, NULL, NULL, NULL, NULL, NULL,NULL, 682, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'0', 179900, 179901, UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES(240111044331047904, 240111044331051304, '', 114.11492, 22.54703, 'ws10k8xcyr58');
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(1000750, 240111044331051304);

