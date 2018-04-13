-- ���������˵�����ʾ���� by xiongying20180413
INSERT INTO `eh_service_module_functions` (`id`, `module_id`, `privilege_id`, `explain`) VALUES ('95', '20400', '0', '���������˵�');

SET @exclude_id = (SELECT MAX(id) FROM `eh_service_module_exclude_functions`);
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES ((@exclude_id := @exclude_id + 1), '999971', NULL, '20400', '95');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES ((@exclude_id := @exclude_id + 1), '999983', NULL, '20400', '95');

-- 为深圳湾增加物业查费入口（物业缴费模块还没有对接严军的应用发布） by chongxin.yang written by wentian.wang
set @id = (select MAX(`id`) from `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`, `preview_portal_version_id`) VALUES (@id:=@id+1, '999966', '0', '1', '0', '/home', 'Bizs', '费用查询', '物业查费', null, '1', '1', '13', '{\"url\":\"${home.url}/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\"}', '1', '0', '1', '1', NULL, '0', NULL, NULL, NULL, '1', 'park_tourist', '1', NULL, NULL, '0', NULL, NULL, NULL);


