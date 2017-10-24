-- 园区入驻 add by sw 20171023
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ('1', '1000000', NULL, NULL, 'renewFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ('3', '1000000', NULL, NULL, 'rentAmountFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ('4', '1000000', NULL, NULL, 'rentAmountUnit', 'MONTH_UNIT', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ('5', '1000000', NULL, NULL, 'issuingLeaseFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ('6', '1000000', NULL, NULL, 'buildingIntroduceFlag', '1', NULL, NULL);

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ('apply.entry.lease.project.detail.url', '/park-entry/dist/index.html?hideNavigationBar=1#/project_intro/%s', NULL, '0', NULL);

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
	VALUES ('40103', '项目介绍', '40100', NULL, 'react:/project-intro/project-list', '0', '2', '/40000/40100/40103', 'park', '411', '40100', '3', NULL, 'module');
update eh_web_menus set name = '楼栋介绍' where id = 40105;
set @id = (select MAX(id) FROM eh_web_menu_scopes);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	select (@id := @id + 1), '40103', '', 'EhNamespaces', owner_id, '2' from eh_web_menu_scopes where menu_id = 40100;

-- 招商测试入口 add by xiongying20171024
set @item_id = (select MAX(id) FROM eh_launch_pad_items);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
    VALUES ((@item_id := @item_id + 1), 999992, 0, 0, 0, '/home', 'Bizs', '招商', '招商', 'cs://1/image/aW1hZ2UvTVRwaE5qVTJNbVZoTW1KaU1qRmlNbVV6TVdabE5UQXdaRGN5TmpkbE5qTmlaQQ', 1, 1, 13, '{\"url\":\"http://alpha.lab.everhomes.com/customer/build/index.html?hideNavigationBar=1#/home#sign_suffix"}', 10, 0, 1, 1, '1', 0, NULL, NULL, NULL, 1, 'pm_admin', 0, 221, NULL, 10, NULL);
