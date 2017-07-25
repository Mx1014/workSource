
-- 增加活动和发现入口  add by yanjuun 20170712
SET @eh_launch_pad_items_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
    VALUES((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),1000000,'0','0','0','/home','Bizs','OFFICIAL_ACTIVITY','活动入口二','','1','1','50','{\"categoryId\":2}',200,'0','1','1','','0',NULL,NULL,NULL,'0','pm_admin','0',NULL,NULL,'0',NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) 
    VALUES((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),1000000,'0','0','0','/home','Bizs','OFFICIAL_ACTIVITY','活动入口二','','1','1','50','{\"categoryId\":2}',200,'0','1','1','','0',NULL,NULL,NULL,'0','park_tourist','0',NULL,NULL,'0',NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 1000000, '0', '0', '0', '/home', 'Bizs', 'POST_LIST', '发现二', 'cs://1/image/aW1hZ2UvTVRwbE5UQXhNVGhsTXpJMlkyVmxObU13TjJRM05XVTRNbUk0T0RRNU1qa3hOUQ', '1', '1', '62', '{"tag":"创客"}', 210, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 1000000, '0', '0', '0', '/home', 'Bizs', 'POST_LIST', '发现二', 'cs://1/image/aW1hZ2UvTVRwbE5UQXhNVGhsTXpJMlkyVmxObU13TjJRM05XVTRNbUk0T0RRNU1qa3hOUQ', '1', '1', '62', '{"tag":"创客"}', 210, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');


-- merge from activity-3.4.0 start 20170719  by yanjun

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`,`level`,`condition_type`,`category`)
VALUES (10640, '旅游与团建', 10000, null, 'forum_activity/3', 0, 2, '/10000/10640', 'park', 600, 10600, 2, 'project', 'module');

SET @namespace_id = 999983;
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 10640, '', 'EhNamespaces', @namespace_id, 2);

SET @item_id = (SELECT max(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) select @item_id:=@item_id+1, @namespace_id, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, '旅游与团建', '旅游与团建', 'cs://1/image/aW1hZ2UvTVRvMVlXSTNOalEyTWpaa01XUTRPRGRrWXpJell6YzBNalk0TkdFNVlXWTBaQQ', `item_width`, `item_height`, 50, '{"categoryId":3}', `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri` from `eh_launch_pad_items` where `item_label` = '通知' and namespace_id = @namespace_id;

insert into `eh_activity_categories`
(`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`)
values('1000049','','0','3','-1','旅游与团建','/3','0','2','1','2017-05-24 11:01:42','0',NULL,@namespace_id,'0','1','cs://1/image/aW1hZ2UvTVRvMVlXSTNOalEyTWpaa01XUTRPRGRrWXpJell6YzBNalk0TkdFNVlXWTBaQQ',NULL,NULL,'0');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) select @item_id:=@item_id+1, @namespace_id, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, '招聘与求职', '招聘与求职', 'cs://1/image/aW1hZ2UvTVRvek4yWmpNMlU1WkRJek5EQXpNMk16WVRrd1ltTXlPR1E1WlRRNVpqVmxOUQ', `item_width`, `item_height`, 62, '{"tag":"招聘与求职"}', `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri` from `eh_launch_pad_items` where `item_label` = '通知' and namespace_id = @namespace_id;

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) select @item_id:=@item_id+1, @namespace_id, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, '园区生活', '园区生活', 'cs://1/image/aW1hZ2UvTVRveVl6RTFOREF4WTJKbU56ZG1ZMlkyTkdVMk1EUTRaREUwWlRFeU1URXdaUQ', `item_width`, `item_height`, 62, '{"tag":"二手交易"}', `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri` from `eh_launch_pad_items` where `item_label` = '通知' and namespace_id = @namespace_id;

-- merge from activity-3.4.0 end 20170719  by yanjun

