-- 深业停车充值
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
VALUES ('160', 'parking.shenye.projectId', '4403000043', '深业停车充值项目ID', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
VALUES ('161', 'parking.default.nickname', '默认昵称', '停车充值默认昵称', '0', NULL);

INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `card_reserve_days`, `status`, `creator_uid`, `create_time`) 
VALUES ('10003', 'community', '240111044331051302', '深发花园停车场', 'BOSIGAO2', NULL, '1', '2', '1025', '2016-08-29 17:28:10');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
	VALUES ('10030', '999992', '0', '0', '0', '/home', 'Bizs', 'PARKING_RECHARGE', '停车充值', 'cs://1/image/aW1hZ2UvTVRwaFpXRmtZek5qTWpobE1UWTRaVE5qWlRjek4yWTFaRFU1WlRJeVlqUXlNQQ', '1', '1', '30', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'default', '1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
	VALUES ('10059', '999992', '0', '0', '0', '/home', 'Bizs', 'PARKING_RECHARGE', '停车充值', 'cs://1/image/aW1hZ2UvTVRwaFpXRmtZek5qTWpobE1UWTRaVE5qWlRjek4yWTFaRFU1WlRJeVlqUXlNQQ', '1', '1', '30', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '1');

-- 科技园新增 左邻能量加油站 add by sw 20161202	 
update eh_launch_pad_items set default_order = default_order + 3 where id not in (812, 10308) and namespace_id = 1000000;

SET @eh_launch_pad_items = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) 
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '1000000', '0', '4', '178945', '/home', 'Bizs', '左邻能量加油站', '左邻能量加油站', 'cs://1/image/aW1hZ2UvTVRvM05tRXdNakkzWmpZM016RXdaVE5qWVRCaVpqUTVaV0kxTlRBMllUazROdw', '1', '1', '14', '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14803348590903554653%3F_k%3Dzlbiz#sign_suffix"}', '2', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) 
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '1000000', '0', '4', '178945', '/home', 'Bizs', '左邻能量加油站', '左邻能量加油站', 'cs://1/image/aW1hZ2UvTVRvM05tRXdNakkzWmpZM016RXdaVE5qWVRCaVpqUTVaV0kxTlRBMllUazROdw', '1', '1', '14', '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14803348590903554653%3F_k%3Dzlbiz#sign_suffix"}', '2', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', NULL);