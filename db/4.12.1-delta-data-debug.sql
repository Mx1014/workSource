-- 物品搬迁测试数据 add by sw 20171212
SET @id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
	VALUES ((@id := @id + 1), '1000000', '0', '0', '0', '/home', 'Bizs', '物品搬迁', '物品搬迁', 'cs://1/image/aW1hZ2UvTVRwaE5UZGhNVFUzWW1Gak5HTmtZMkk1TTJKaFpHWTFOREk1WWpaak5tRTVOUQ', '1', '1', '13', '{\"url\":\"http://sunwen.lab.everhomes.com/goods-move/build/index.html?ns=1000000&hideNavigationBar=1&ehnavigatorstyle=0#home#sign_suffix\"}', '3', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '1', NULL, NULL, '0', NULL, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
	VALUES ((@id := @id + 1), '1000000', '0', '0', '0', '/home', 'Bizs', '物品搬迁', '物品搬迁', 'cs://1/image/aW1hZ2UvTVRwaE5UZGhNVFUzWW1Gak5HTmtZMkk1TTJKaFpHWTFOREk1WWpaak5tRTVOUQ', '1', '1', '13', '{\"url\":\"http://sunwen.lab.everhomes.com/goods-move/build/index.html?ns=1000000&hideNavigationBar=1&ehnavigatorstyle=0#home#sign_suffix\"}', '3', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '1', NULL, NULL, '0', NULL, NULL);

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
  VALUES((@menu_scope_id := @menu_scope_id + 1),49200,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
  VALUES((@menu_scope_id := @menu_scope_id + 1),49202,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
  VALUES((@menu_scope_id := @menu_scope_id + 1),49204,'', 'EhNamespaces', 1000000,2);

-- 园区入驻测试数据 add by sw 20171212
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
  VALUES ('40140', '招租管理', '40000', NULL, NULL, '1', '2', '/40000/40140', 'park', '410', '40100', '2', NULL, 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
  VALUES ('40142', '项目介绍', '40140', NULL, 'react:/project-intro/project-list/2', '0', '2', '/40000/40140/40142', 'park', '411', '40100', '3', NULL, 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
  VALUES ('40144', '楼栋介绍', '40140', NULL, 'projects_introduce/2', '0', '2', '/40000/40140/40144', 'park', '412', '40100', '3', NULL, 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
  VALUES ('40146', '房源招租', '40140', NULL, 'rent_manage/2', '0', '2', '/40000/40140/40146', 'park', '412', '40100', '3', NULL, 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
  VALUES ('40148', '申请记录', '40140', NULL, 'enter_apply/2', '0', '2', '/40000/40140/40148', 'park', '414', '40100', '3', NULL, 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
  VALUES ('40149', '工作流设置', '40140', NULL, 'react:/working-flow/flow-list/rent-manage/40100', '0', '2', '/40000/40140/40149', 'park', '419', '40100', '3', NULL, 'module');

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
  VALUES((@menu_scope_id := @menu_scope_id + 1),40140,'招租管理2', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
  VALUES((@menu_scope_id := @menu_scope_id + 1),40142,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
  VALUES((@menu_scope_id := @menu_scope_id + 1),40144,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
  VALUES((@menu_scope_id := @menu_scope_id + 1),40146,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
  VALUES((@menu_scope_id := @menu_scope_id + 1),40148,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
  VALUES((@menu_scope_id := @menu_scope_id + 1),40149,'', 'EhNamespaces', 1000000,2);

