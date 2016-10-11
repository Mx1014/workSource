INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('249', 'parking.chuneng.url', 'http://220.160.111.114:9099', '储能停车充值key', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('250', 'parking.chuneng.key', 'F7A0B971B199FD2A1017CEC5', '储能停车充值key', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('251', 'parking.chuneng.user', 'ktapi', '储能停车充值用户名', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('252', 'parking.chuneng.pwd', '0306A9', '储能停车充值密码', '0', NULL);
	
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `card_reserve_days`, `status`, `creator_uid`, `create_time`, `max_request_num`) 
	VALUES ('10004', 'community', '240111044331051500', '中国储能大厦停车场', 'KETUO', NULL, '1', '2', '1025', '2016-08-29 17:28:10', '1');

	
	
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
	VALUES ('116318', '999990', '0', '0', '0', '/home', 'Bizs', 'PARKING_RECHARGE', '停车充值', 'cs://1/image/aW1hZ2UvTVRwaFpXRmtZek5qTWpobE1UWTRaVE5qWlRjek4yWTFaRFU1WlRJeVlqUXlNQQ', '1', '1', '30', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin', '1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
	VALUES ('116319', '999990', '0', '0', '0', '/home', 'Bizs', 'PARKING_RECHARGE', '停车充值', 'cs://1/image/aW1hZ2UvTVRwaFpXRmtZek5qTWpobE1UWTRaVE5qWlRjek4yWTFaRFU1WlRJeVlqUXlNQQ', '1', '1', '30', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist', '1');
