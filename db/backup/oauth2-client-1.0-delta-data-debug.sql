-- oauth2client 1.0   add by xq.tian 2017/03/09
--
-- 门禁icon
--
SET @max_id = (SELECT max(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)
VALUES ((@max_id := @max_id + 1), 999992, 0, 0, 0, '/home', 'Bizs', 'DOOR', '门禁', 'cs://1/image/aW1hZ2UvTVRwaVl6ZGtPVFE0TURZd1pUZzRZekppTTJNMVl6QmlPVFprTWpWbFpHRXlNUQ', 1, 1, 13, '{"url":"http://10.1.10.79/evh/oauth2cli/redirect/huanteng?serviceUrl=%2fzlapp%2fdist%2f%3fhideNavigationBar%3d1%23%2faccess-control%2flock-list-cy&hideNavigationBar=1#sign_suffix"}', 0, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'default', 0, NULL, NULL);
SET @max_id = (SELECT max(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`)
VALUES ((@max_id := @max_id + 1), 999992, 0, 0, 0, '/home', 'Bizs', 'DOOR', '门禁', 'cs://1/image/aW1hZ2UvTVRwaVl6ZGtPVFE0TURZd1pUZzRZekppTTJNMVl6QmlPVFprTWpWbFpHRXlNUQ', 1, 1, 13, '{"url":"http://10.1.10.79/evh/oauth2cli/redirect/huanteng?serviceUrl=%2fzlapp%2fdist%2f%3fhideNavigationBar%3d1%23%2faccess-control%2flock-list-cy&hideNavigationBar=1#sign_suffix"}', 0, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL);


