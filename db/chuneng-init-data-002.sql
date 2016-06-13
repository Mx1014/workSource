-- update wifi 上网 actiontype 47
delete from `eh_launch_pad_items` where id = 10601;
delete from `eh_launch_pad_items` where id = 10619;

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ('10601', '999990', '0', '0', '0', '/home', 'Bizs', '上网', '上网', 'cs://1/image/aW1hZ2UvTVRvNE5ETTJPREZpT0dGa1lUZzJOelpqTm1NeE1ETXdNMlU0TTJJeU16STBOUQ', '1', '1', '47', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ('10619', '999990', '0', '0', '0', '/home', 'Bizs', '上网', '上网', 'cs://1/image/aW1hZ2UvTVRvNE5ETTJPREZpT0dGa1lUZzJOelpqTm1NeE1ETXdNMlU0TTJJeU16STBOUQ', '1', '1', '47', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
