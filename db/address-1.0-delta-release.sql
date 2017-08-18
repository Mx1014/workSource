-- 配置服务广场图标
SET @id = (SELECT MAX(id) from eh_launch_pad_items);
INSERT INTO `eh_launch_pad_items` 
(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
VALUES 
(@id+1, 999971, '0', '0', '0', '/home', 'Bizs', 'Talent Apartment', '人才公寓', 'cs://1/image/aW1hZ2UvTVRvME1HVmxOV1k1WlRZd09UQTNZelUwTnpRMU0yUXpNVFJoT1dZM09ERmpZUQ', '1', '1', '45', '', 10, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');

INSERT INTO `eh_launch_pad_items` 
(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
VALUES 
(@id+2, 999971, '0', '0', '0', '/home', 'Bizs', 'Talent Apartment', '人才公寓', 'cs://1/image/aW1hZ2UvTVRvME1HVmxOV1k1WlRZd09UQTNZelUwTnpRMU0yUXpNVFJoT1dZM09ERmpZUQ', '1', '1', '45', '', 10, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
