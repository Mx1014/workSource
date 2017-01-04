INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ('155', 'community', '240111044331055940', '0', '办事指南', '办事指南', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999983', '');

SET @sa_id = (SELECT max(id) FROM `eh_service_alliances`);   
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`) 
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331055940', '办事指南', '办事指南', '155', '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

    
update eh_launch_pad_items set action_type = 33 where id in(112845, 112875) and namespace_id = 999983;
update eh_launch_pad_items set action_data = '{"type":155,"parentId":155,"displayType": "list"}' where id in(112845, 112875) and namespace_id = 999983;

INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_url`) VALUES (1, 999983, '物业报修', 'zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=物业报修');
