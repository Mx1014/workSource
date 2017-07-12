-- update eh_launch_pad_items 
-- set action_type = 14, 
--      action_data = '{"url": "http://alpha.lab.everhomes.com/mobile/static/overseas_supplier/guide.html","declareFlag":"1"}' 
-- where namespace_id = 999983 
-- and item_name = '海外电商';

-- select max(id) into @id from `eh_launch_pad_items`;
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES (@id+1, 999983, 0, 0, 0, '/home', 'Bizs', 'FLOW_TASKS', '任务管理', 'cs://1/image/aW1hZ2UvTVRwaU1tVTJNbUV4Wm1Jd05HRTBZV1F4T0Roa09HUXhNMkUwTldReFpHVXpOUQ', 1, 1, 56, '', 8, 0, 1, 0, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 1, NULL, NULL, 0, NULL);
