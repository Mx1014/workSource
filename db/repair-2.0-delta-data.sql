
-- 物业报修2.0
SET @eh_launch_pad_items_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
  VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 999992, 0, 0, 0, '/home', 'Bizs', '物业报修2.0', '物业报修2.0', 'cs://1/image/aW1hZ2UvTVRwak5ETXlaakJsWkRJNFlqQmhabVJpTmpnd1lXSTFOelF4TjJGbU0yUXpOQQ', 1, 1, 14, '{"url":"http://beta.zuolin.com/property_service/index.html?hideNavigationBar=1#/my_service#sign_suffix"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'default');   


INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
  VALUES (280, 'pmtask', '10005', 'zh_CN', '服务类型已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
  VALUES (280, 'pmtask', '10006', 'zh_CN', '服务类型不存在');