-- 增加一个“活动时间：”中文字符串 2017-03-31 19:17 add by yanjun
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('activity','11','zh_CN','活动时间：');

-- 配置ufine会议室预订，并把app上快递改成会议室add by tt, 20170407
update eh_rentalv2_resource_types set pay_mode = 1 where id = 61;
select id into @id from eh_launch_pad_items where namespace_id = 999990 and item_label = '快递' and scene_type = 'park_tourist';
delete from eh_launch_pad_items where id = @id;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id, 999990, 0, 0, 0, '/home', 'Bizs', 'MEETINGROOM', '会议室', 'cs://1/image/aW1hZ2UvTVRvME5HVTNZVEZsTXpNeU16VXhNbVF3Wm1GbU9UUTBPV0ZoTUdRNFpUSmpaQQ', 1, 1, 49, '{"resourceTypeId":60,"pageType":0,"payMode":1}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 0, NULL, NULL);
select id into @id from eh_launch_pad_items where namespace_id = 999990 and item_label = '快递' and scene_type = 'pm_admin';
delete from eh_launch_pad_items where id = @id;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id, 999990, 0, 0, 0, '/home', 'Bizs', 'MEETINGROOM', '会议室', 'cs://1/image/aW1hZ2UvTVRvME5HVTNZVEZsTXpNeU16VXhNbVF3Wm1GbU9UUTBPV0ZoTUdRNFpUSmpaQQ', 1, 1, 49, '{"resourceTypeId":60,"pageType":0,"payMode":1}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 0, NULL, NULL);

update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRwaE9EUTJOV1k1WXpBME1EY3pOR0V3TXpJM1l6QmhaakpqT1dVM1lUTTRNUQ' where namespace_id = 999990 and action_type = 49;
