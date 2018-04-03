-- 错误提示，add by tt, 20170527
select max(id) into @id from `eh_locale_strings`;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'talent', '1', 'zh_CN', '本功能仅对企业管理员开放，如有需要请联系企业管理员');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'talent', '2', 'zh_CN', '分类名称不能重复');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'talent', '3', 'zh_CN', '导入失败，请检查数据是否按要求填写');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'parameters.error', '10002', 'zh_CN', '参数长度不足');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'parameters.error', '10003', 'zh_CN', '参数不能为空');

-- 默认头像，add by tt, 20170527
select max(id) into @id from `eh_configurations`;
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@id:=@id+1, 'talent.male.uri', 'cs://1/image/aW1hZ2UvTVRwaE1tRmpOalEwWVRJME1UZ3pOalUwT0RKa09HRTNZV0ZtWkdabFpHTmtOdw', 'talen default male uri', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@id:=@id+1, 'talent.female.uri', 'cs://1/image/aW1hZ2UvTVRwa04yRTNaR1ZrWlRreU1XWmhabVV3WkdZd05qQTFZamxqTURSa1pHSmlNQQ', 'talen default female uri', 0, NULL);

-- 菜单，add by tt, 20170527
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) VALUES (40730, '企业人才', 40000, NULL, 'react:/enterprise-management/talent-list', 1, 2, '/40730/70100', 'park', 498, 40730);

-- 权限，add by tt, 20170527
select max(id) into @pri_id from `eh_acl_privileges`;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@pri_id:=@pri_id+1, 0, '企业人才 管理员', '企业人才 业务模块权限', NULL);

-- 菜单关联权限，add by tt, 20170527
select max(id) into @id from `eh_web_menu_privileges`;
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES (@id+1, @pri_id, 40730, '企业人才', 1, 1, '企业人才 全部权限', 714);

-- 权限赋予超管，add by tt, 20170527
select max(id) into @id from `eh_acls`;
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `comment_tag1`, `comment_tag2`, `comment_tag3`, `comment_tag4`, `comment_tag5`) VALUES (@id+1, 'EhOrganizations', NULL, 1, @pri_id, 1001, 0, 1, '2017-06-05 09:41:07', 0, 'EhAclRoles', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- 显示菜单，add by tt, 20170527
select max(id) into @id from `eh_web_menu_scopes`;
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id+1, 40730, '', 'EhNamespaces', 999990, 2);

-- 不同环境执行不同的语句！！！！
-- 服务市场配置，add by tt, 20170527(90)
-- select max(id) into @id from `eh_launch_pad_items`;
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES (@id:=@id+1, 999990, 0, 0, 0, '/home', 'Bizs', '企业人才', '企业人才', 'cs://1/image/aW1hZ2UvTVRvNE56STNNamxsWW1GalpUUTJOamhpTVRKbFpqTTBOVEJoTkRrelpUZzJaUQ', 1, 1, 14, '{"url":"http://10.1.10.90/enterprise-talent/build/index.html?hideNavigationBar=1#/home_page#sign_suffix"}', 2, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 1, NULL, NULL, 0, NULL);
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES (@id:=@id+1, 999990, 0, 0, 0, '/home', 'Bizs', '企业人才', '企业人才', 'cs://1/image/aW1hZ2UvTVRvNE56STNNamxsWW1GalpUUTJOamhpTVRKbFpqTTBOVEJoTkRrelpUZzJaUQ', 1, 1, 14, '{"url":"http://10.1.10.90/enterprise-talent/build/index.html?hideNavigationBar=1#/home_page#sign_suffix"}', 2, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 1, NULL, NULL, 0, NULL);

-- 服务市场配置，add by tt, 20170527(alpha)
-- select max(id) into @id from `eh_launch_pad_items`;
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES (@id:=@id+1, 999990, 0, 0, 0, '/home', 'Bizs', '企业人才', '企业人才', 'cs://1/image/aW1hZ2UvTVRvNE56STNNamxsWW1GalpUUTJOamhpTVRKbFpqTTBOVEJoTkRrelpUZzJaUQ', 1, 1, 14, '{"url":"http://alpha.lab.everhomes.com/enterprise-talent/build/index.html?hideNavigationBar=1#/home_page#sign_suffix"}', 2, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 1, NULL, NULL, 0, NULL);
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES (@id:=@id+1, 999990, 0, 0, 0, '/home', 'Bizs', '企业人才', '企业人才', 'cs://1/image/aW1hZ2UvTVRvNE56STNNamxsWW1GalpUUTJOamhpTVRKbFpqTTBOVEJoTkRrelpUZzJaUQ', 1, 1, 14, '{"url":"http://alpha.lab.everhomes.com/enterprise-talent/build/index.html?hideNavigationBar=1#/home_page#sign_suffix"}', 2, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 1, NULL, NULL, 0, NULL);

-- 服务市场配置，add by tt, 20170527(beta)
-- select max(id) into @id from `eh_launch_pad_items`;
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES (@id:=@id+1, 999990, 0, 0, 0, '/home', 'Bizs', '企业人才', '企业人才', 'cs://1/image/aW1hZ2UvTVRvNE56STNNamxsWW1GalpUUTJOamhpTVRKbFpqTTBOVEJoTkRrelpUZzJaUQ', 1, 1, 14, '{"url":"http://beta.zuolin.com/enterprise-talent/build/index.html?hideNavigationBar=1#/home_page#sign_suffix"}', 2, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 1, NULL, NULL, 0, NULL);
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES (@id:=@id+1, 999990, 0, 0, 0, '/home', 'Bizs', '企业人才', '企业人才', 'cs://1/image/aW1hZ2UvTVRvNE56STNNamxsWW1GalpUUTJOamhpTVRKbFpqTTBOVEJoTkRrelpUZzJaUQ', 1, 1, 14, '{"url":"http://beta.zuolin.com/enterprise-talent/build/index.html?hideNavigationBar=1#/home_page#sign_suffix"}', 2, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 1, NULL, NULL, 0, NULL);

-- 服务市场配置，add by tt, 20170527(core)
-- select max(id) into @id from `eh_launch_pad_items`;
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES (@id:=@id+1, 999990, 0, 0, 0, '/home', 'Bizs', '企业人才', '企业人才', 'cs://1/image/aW1hZ2UvTVRvNE56STNNamxsWW1GalpUUTJOamhpTVRKbFpqTTBOVEJoTkRrelpUZzJaUQ', 1, 1, 14, '{"url":"http://core.zuolin.com/enterprise-talent/build/index.html?hideNavigationBar=1#/home_page#sign_suffix"}', 2, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 1, NULL, NULL, 0, NULL);
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) VALUES (@id:=@id+1, 999990, 0, 0, 0, '/home', 'Bizs', '企业人才', '企业人才', 'cs://1/image/aW1hZ2UvTVRvNE56STNNamxsWW1GalpUUTJOamhpTVRKbFpqTTBOVEJoTkRrelpUZzJaUQ', 1, 1, 14, '{"url":"http://core.zuolin.com/enterprise-talent/build/index.html?hideNavigationBar=1#/home_page#sign_suffix"}', 2, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 1, NULL, NULL, 0, NULL);




