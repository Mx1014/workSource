-- 因为后面会出现clone帖的功能，现在现将原有帖子设置为正常帖  add by yanjun 20170809
UPDATE eh_forum_posts set clone_flag = 2 where clone_flag is null;
UPDATE eh_activities set clone_flag = 2 where clone_flag is null;

-- 活动报名导入异常信息
set @id = (select MAX(id) FROM eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES
	((@id := @id + 1), 'activity', '20', 'zh_CN', '手机号为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES
	((@id := @id + 1), 'activity', '21', 'zh_CN', '手机号无效');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES
	((@id := @id + 1), 'activity', '22', 'zh_CN', '真实姓名为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES
	((@id := @id + 1), 'activity', '23', 'zh_CN', '报名信息已经存在，可在报名详情页修改该用户信息');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES
	((@id := @id + 1), 'activity', '24', 'zh_CN', '该用户在Excel表重复');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES
	((@id := @id + 1), 'activity', '25', 'zh_CN', '报名信息存在，现已更新');

UPDATE eh_locale_strings SET text = '呃，报名信息已经存在，可在报名详情页修改该用户信息' WHERE scope = 'activity' AND code = 10031;


-- 管理后台增加菜单 add by yanjun 20170906
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('7000000', '园区服务模块管理', '0', null, null, '1', '2', '/7000000', 'zuolin', '20', '10600', '1', 'project', 'classify');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('7100000', '活动模块', '7000000', NULL, 'activity-application', '1', '2', '/7000000/7100000', 'zuolin', '1', '10600', '2', 'project', 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('7110000', '活动模块', '7100000', NULL, 'activity-application', '1', '2', '/7000000/7100000/7110000', 'zuolin', '1', '10600', '3', 'project', 'page');




-- 将以前的官方活动50，改成61  add by yanjun 20170912
UPDATE eh_launch_pad_items SET action_type = 61, action_data = CONCAT('{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":3,"style":4,"title": "',item_label, '"}') WHERE action_type = 50 and id in (802, 10303, 10617, 10635, 111404, 111419, 112301, 112321, 112822, 112830, 114239, 114240, 114308, 114309, 114311, 114322, 114380, 114392, 114421, 114422, 115454, 115473, 119040, 119041);

	-- 给入口增加一个默认的分类-“全部”，parent_id为入口的entry_id
set @id = 1101051;
set @id = @id + 1;
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`)
    VALUES (@id, '', '0', @id, 1, 'all', CONCAT('/1/',@id), '0', '2', '1', NOW(), '0', NULL, '1000000', '0', '1', 'cs://1/image/aW1hZ2UvTVRvM09HWmxNRFptWldNM1lqQm1aakEyWVdRMVpEZ3dNelEzTVRrMk1XRmpPUQ', 'cs://1/image/aW1hZ2UvTVRvd016YzVZVE5tT1dFeU9XUTRPRGcxTkdNME5HUTFabVE1T0RBd00yWmpZdw', NULL, '1');

set @id = @id + 1;
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`)
    VALUES (@id, '', '0', @id, 1, 'all', CONCAT('/1/',@id), '0', '2', '1', NOW(), '0', NULL, '999990', '0', '1', 'cs://1/image/aW1hZ2UvTVRvM09HWmxNRFptWldNM1lqQm1aakEyWVdRMVpEZ3dNelEzTVRrMk1XRmpPUQ', 'cs://1/image/aW1hZ2UvTVRvd016YzVZVE5tT1dFeU9XUTRPRGcxTkdNME5HUTFabVE1T0RBd00yWmpZdw', NULL, '1');

set @id = @id + 1;
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`)
    VALUES (@id, '', '0', @id, 1, 'all', CONCAT('/1/',@id), '0', '2', '1', NOW(), '0', NULL, '999987', '0', '1', 'cs://1/image/aW1hZ2UvTVRvM09HWmxNRFptWldNM1lqQm1aakEyWVdRMVpEZ3dNelEzTVRrMk1XRmpPUQ', 'cs://1/image/aW1hZ2UvTVRvd016YzVZVE5tT1dFeU9XUTRPRGcxTkdNME5HUTFabVE1T0RBd00yWmpZdw', NULL, '1');

set @id = @id + 1;
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`)
    VALUES (@id, '', '0', @id, 1, 'all', CONCAT('/1/',@id), '0', '2', '1', NOW(), '0', NULL, '999986', '0', '1', 'cs://1/image/aW1hZ2UvTVRvM09HWmxNRFptWldNM1lqQm1aakEyWVdRMVpEZ3dNelEzTVRrMk1XRmpPUQ', 'cs://1/image/aW1hZ2UvTVRvd016YzVZVE5tT1dFeU9XUTRPRGcxTkdNME5HUTFabVE1T0RBd00yWmpZdw', NULL, '1');

set @id = @id + 1;
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`)
    VALUES (@id, '', '0', @id, 1, 'all', CONCAT('/1/',@id), '0', '2', '1', NOW(), '0', NULL, '999982', '0', '1', 'cs://1/image/aW1hZ2UvTVRvM09HWmxNRFptWldNM1lqQm1aakEyWVdRMVpEZ3dNelEzTVRrMk1XRmpPUQ', 'cs://1/image/aW1hZ2UvTVRvd016YzVZVE5tT1dFeU9XUTRPRGcxTkdNME5HUTFabVE1T0RBd00yWmpZdw', NULL, '1');

set @id = @id + 1;
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`)
    VALUES (@id, '', '0', @id, 1, 'all', CONCAT('/1/',@id), '0', '2', '1', NOW(), '0', NULL, '999977', '0', '1', 'cs://1/image/aW1hZ2UvTVRvM09HWmxNRFptWldNM1lqQm1aakEyWVdRMVpEZ3dNelEzTVRrMk1XRmpPUQ', 'cs://1/image/aW1hZ2UvTVRvd016YzVZVE5tT1dFeU9XUTRPRGcxTkdNME5HUTFabVE1T0RBd00yWmpZdw', NULL, '1');

set @id = @id + 1;
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`)
    VALUES (@id, '', '0', @id, 1, 'all', CONCAT('/1/',@id), '0', '2', '1', NOW(), '0', NULL, '1', '0', '1', 'cs://1/image/aW1hZ2UvTVRvM09HWmxNRFptWldNM1lqQm1aakEyWVdRMVpEZ3dNelEzTVRrMk1XRmpPUQ', 'cs://1/image/aW1hZ2UvTVRvd016YzVZVE5tT1dFeU9XUTRPRGcxTkdNME5HUTFabVE1T0RBd00yWmpZdw', NULL, '1');

set @id = @id + 1;
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`)
    VALUES (@id, '', '0', @id, 1, 'all', CONCAT('/1/',@id), '0', '2', '1', NOW(), '0', NULL, '999974', '0', '1', 'cs://1/image/aW1hZ2UvTVRvM09HWmxNRFptWldNM1lqQm1aakEyWVdRMVpEZ3dNelEzTVRrMk1XRmpPUQ', 'cs://1/image/aW1hZ2UvTVRvd016YzVZVE5tT1dFeU9XUTRPRGcxTkdNME5HUTFabVE1T0RBd00yWmpZdw', NULL, '1');

set @id = @id + 1;
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`)
    VALUES (@id, '', '0', @id, 1, 'all', CONCAT('/1/',@id), '0', '2', '1', NOW(), '0', NULL, '999981', '0', '1', 'cs://1/image/aW1hZ2UvTVRvM09HWmxNRFptWldNM1lqQm1aakEyWVdRMVpEZ3dNelEzTVRrMk1XRmpPUQ', 'cs://1/image/aW1hZ2UvTVRvd016YzVZVE5tT1dFeU9XUTRPRGcxTkdNME5HUTFabVE1T0RBd00yWmpZdw', NULL, '1');

set @id = @id + 1;
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`)
    VALUES (@id, '', '0', @id, 1, 'all', CONCAT('/1/',@id), '0', '2', '1', NOW(), '0', NULL, '999972', '0', '1', 'cs://1/image/aW1hZ2UvTVRvM09HWmxNRFptWldNM1lqQm1aakEyWVdRMVpEZ3dNelEzTVRrMk1XRmpPUQ', 'cs://1/image/aW1hZ2UvTVRvd016YzVZVE5tT1dFeU9XUTRPRGcxTkdNME5HUTFabVE1T0RBd00yWmpZdw', NULL, '1');

set @id = @id + 1;
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`)
    VALUES (@id, '', '0', @id, 1, 'all', CONCAT('/1/',@id), '0', '2', '1', NOW(), '0', NULL, '999969', '0', '1', 'cs://1/image/aW1hZ2UvTVRvM09HWmxNRFptWldNM1lqQm1aakEyWVdRMVpEZ3dNelEzTVRrMk1XRmpPUQ', 'cs://1/image/aW1hZ2UvTVRvd016YzVZVE5tT1dFeU9XUTRPRGcxTkdNME5HUTFabVE1T0RBd00yWmpZdw', NULL, '1');


-- 将eh_service_modules表活动的默认scope设置为3  add by yanjun 20170912
UPDATE eh_service_modules set instance_config = '{"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":3,"style":4,"title": "活动管理"}'  where id = 10600;