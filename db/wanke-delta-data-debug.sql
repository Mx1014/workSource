-- 万科初始数据
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
    VALUES (86, 'wanke.mashen.url', 'https://api.open.imasheng.com/openapi', 'the url for wanke.mashen', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
    VALUES (87, 'wanke.mashen.appId', '15725632', 'the appId of wanke.mashen', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
    VALUES (88, 'wanke.mashen.appSecret', 'b4b994d5ed0a9990', 'the appSecret of wanke.mashen', 0, NULL);
UPDATE `eh_organizations` SET `namespace_organization_token` = '80320', `namespace_organization_type` = 'wanke' WHERE `id` = '1001027';
INSERT INTO `eh_community_services` (`id`, `namespace_id`, `owner_type`, `owner_id`, `scope_code`, `scope_id`, `item_name`, `item_label`, `icon_uri`, `action_type`, `action_data`, `scene_type`) 
	VALUES ('1', '0', 'community', '240111044331051460', '1', '1', '物业报修', '物业报修', 'cs://1/image/aW1hZ2UvTVRveU5tSXhOMlV6TmpSak5qQmpOMlk1T1RVMFpqSTNPR0U1TXpNMll6QmlNdw', '1', '{\"contentCategory\":1004,\"actionCategory\":0,\"forumId\":1,\"embedAppId\":27,\"entityTag\":\"PM\"}', 'default');

