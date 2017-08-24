-- 配置服务广场图标
SET @id = (SELECT MAX(id) from eh_launch_pad_items);
INSERT INTO `eh_launch_pad_items` 
(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
VALUES 
(@id+1, 999971, '0', '0', '0', '/home', 'Bizs', 'Park Services', '园区服务', 'cs://1/image/aW1hZ2UvTVRvNVpHUmxNalExWkdSaU16Y3hNelV3T1RjME5ERmhORGRtWm1aak16TTBNdw', '1', '1', '66', '{"viewTitle": "选择人才公寓"}', 10, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'default');

INSERT INTO `eh_launch_pad_items` 
(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
VALUES 
(@id+2, 999971, '0', '0', '0', '/home', 'Bizs', 'Talent Apartment', '人才公寓', 'cs://1/image/aW1hZ2UvTVRwalpHVmhaVGt3WldZM1pHSXhZemczWW1FME5UVmtaR05sTVdObU9HVXlZUQ', '1', '1', '66', '{"viewTitle": "选择人才公寓"}', 10, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');

-- 配置默认楼栋
SET @c_id = (SELECT MAX(id) from eh_communities);
INSERT INTO `eh_communities` VALUES (@c_id + 1, '9c63e9e7-6d48-11e7-a008-0242ac110017', '14809', '上海市', '14810', '浦东新区', '上海张江高科测试小区', '张江高科', '松涛路561号9楼', null, '', null, null, null, null, null, null, null, '214', '1', null, '2', '2017-07-20 12:40:28', null, null, null, null, null, null, null, null, null, null, null, '0', '190570', '190571', '2017-07-20 12:40:28', '999971', null);

-- 配置张江高科的默认园区
DELETE from eh_communities WHERE namespace_id = 999971 AND id in (240111044331050388,240111044332059789);
SET @c_id = (SELECT MAX(id) from eh_communities);
INSERT INTO `eh_communities` VALUES (@c_id + 1, '9c63e9e7-6d48-11e7-a008-0242ac110006', '14809', '上海市', '14810', '浦东新区', '张江高科技园区', '张江高科', '松涛路560号', null, '', null, null, null, null, null, null, null, '214', '1', null, '2', '2017-07-20 12:40:28', null, null, null, null, null, null, null, null, null, null, null, '1', '190570', '190571', '2017-07-20 12:40:28', '999971', null);
INSERT INTO `eh_communities` VALUES (@c_id + 2, '9c63e9e7-6d48-11e7-a008-0242ac110007', '14809', '上海市', '14810', '浦东新区', '天之骄子人才公寓', '人才公寓', '上海市浦东新区盛夏路58弄', null, '', null, null, null, null, null, null, null, '214', '1', null, '2', '2017-07-20 12:40:28', null, null, null, null, null, null, null, null, null, null, null, '0', '190570', '190571', '2017-07-20 12:40:28', '999971', null);

-- 增加经纬度
SET @ geo_id = (SELECT MAX(id) from eh_community_geopoints);
INSERT INTO `eh_community_geopoints` VALUES (@geo_id + 1, 240111044332059790, '',0,0, 's00000000000');
INSERT INTO `eh_community_geopoints` VALUES (@geo_id + 2, 240111044332059789, '',0,0, 's00000000000');



