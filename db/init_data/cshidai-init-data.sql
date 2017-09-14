-- 服务广场配置
DELETE FROM eh_launch_pad_layouts WHERE namespace_id = 999965;
SET @layout_id = (SELECT MAX(id) FROM `eh_launch_pad_layouts`);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) VALUES ((@layout_id := @layout_id + 1), '999965', 'ServiceMarketLayout',
'{"versionCode":"2017091101","versionName":"3.0.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies","cssStyleFlag":1,"paddingLeft":20,"paddingTop":20,"paddingRight":20,"paddingBottom":20,"lineSpacing":16,"columnSpacing":16,"backgroundColor":"FFFFFF"},"style":"Gallery","defaultOrder":2,"separatorFlag":1,"columnCount":2,"separatorHeight":1},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":16},{"title":"园区服务","iconUrl":"https://content-1.zuolin.com:443/image/aW1hZ2UvTVRveU5UVmtNVFpqTnpBek1URXpOREkzTkRnMU1qWTFOMlZrWVdKa1lXTTNZdw?token=XlYdOjlDVEVb4XyQO4_dd5RI37zTkV3jCKm_-XbRyLIGVUorWGnyRCwLAgMGV86baX30BnQW4nqzF9nlXGe4M0DbZxWBVTqnL019xazIDuhE6A0OXiMQwRqGX84_1HHv","groupName":"园区服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":4,"separatorFlag":1,"separatorHeight":16,"columnCount":4},{"title":"企业服务","iconUrl":"https://content-1.zuolin.com:443/image/aW1hZ2UvTVRveU5UVmtNVFpqTnpBek1URXpOREkzTkRnMU1qWTFOMlZrWVdKa1lXTTNZdw?token=XlYdOjlDVEVb4XyQO4_dd5RI37zTkV3jCKm_-XbRyLIGVUorWGnyRCwLAgMGV86baX30BnQW4nqzF9nlXGe4M0DbZxWBVTqnL019xazIDuhE6A0OXiMQwRqGX84_1HHv","groupName":"企业服务","widget":"Navigator","instanceConfig":{"itemGroup":"Enterprise"},"style":"Default","defaultOrder":5,"separatorFlag":0,"columnCount":4}]}',
'2017091101', '0', '2', NOW(), 'pm_admin', '0', '0', '0');
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) VALUES ((@layout_id := @layout_id + 1), '999965', 'ServiceMarketLayout',
'{"versionCode":"2017091101","versionName":"3.0.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies","cssStyleFlag":1,"paddingLeft":20,"paddingTop":20,"paddingRight":20,"paddingBottom":20,"lineSpacing":16,"columnSpacing":16,"backgroundColor":"FFFFFF"},"style":"Gallery","defaultOrder":2,"separatorFlag":1,"columnCount":2,"separatorHeight":1},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":16},{"title":"园区服务","iconUrl":"https://content-1.zuolin.com:443/image/aW1hZ2UvTVRveU5UVmtNVFpqTnpBek1URXpOREkzTkRnMU1qWTFOMlZrWVdKa1lXTTNZdw?token=XlYdOjlDVEVb4XyQO4_dd5RI37zTkV3jCKm_-XbRyLIGVUorWGnyRCwLAgMGV86baX30BnQW4nqzF9nlXGe4M0DbZxWBVTqnL019xazIDuhE6A0OXiMQwRqGX84_1HHv","groupName":"园区服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":4,"separatorFlag":1,"separatorHeight":16,"columnCount":4},{"title":"企业服务","iconUrl":"https://content-1.zuolin.com:443/image/aW1hZ2UvTVRveU5UVmtNVFpqTnpBek1URXpOREkzTkRnMU1qWTFOMlZrWVdKa1lXTTNZdw?token=XlYdOjlDVEVb4XyQO4_dd5RI37zTkV3jCKm_-XbRyLIGVUorWGnyRCwLAgMGV86baX30BnQW4nqzF9nlXGe4M0DbZxWBVTqnL019xazIDuhE6A0OXiMQwRqGX84_1HHv","groupName":"企业服务","widget":"Navigator","instanceConfig":{"itemGroup":"Enterprise"},"style":"Default","defaultOrder":5,"separatorFlag":0,"columnCount":4}]}',
'2017091101', '0', '2', NOW(), 'park_tourist', '0', '0', '0');



-- banner配置
SET @eh_banners_id = (SELECT MAX(id) FROM `eh_banners`);
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`)
    VALUES ((@eh_banners_id:=@eh_banners_id+1), 999965, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRvME9HVTBPV013WldFMVlqSm1PR0prTlRZMlpUaGxZMkU0WTJRMlltSTNaZw', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'pm_admin');

INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`)
    VALUES ((@eh_banners_id:=@eh_banners_id+1), 999965, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRvME9HVTBPV013WldFMVlqSm1PR0prTlRZMlpUaGxZMkU0WTJRMlltSTNaZw', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'park_tourist');

INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`)
    VALUES ((@eh_banners_id:=@eh_banners_id+1), 999965, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRvME9HVTBPV013WldFMVlqSm1PR0prTlRZMlpUaGxZMkU0WTJRMlltSTNaZw', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'default');
-- 服务广场
SET @item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
set @homeurl = (select VALUE from eh_configurations WHERE NAME = 'home.url' LIMIT 1);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','GovAgencies','园区介绍','园区介绍','cs://1/image/aW1hZ2UvTVRveE9XUXpOREZqWkRRM1pXTXlZemhsTVdSaFlUQmxOalkwWm1FNU5tTXhOUQ',1,1,13,CONCAT('{"url":"',@homeurl,'/park-introduction/index.html?hideNavigationBar=1#/#sign_suffix"}'),0,0,1,1,'1',0,NULL,NULL,NULL,0,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','GovAgencies','园区招商','园区招商','cs://1/image/aW1hZ2UvTVRveU9ERmlabUpoTlRkaU56VmhZemN5TmpOaU9ESXdNR1ZoT1dVMU1ETmxZUQ',1,1,28,'',1,0,1,1,'1',0,NULL,NULL,NULL,0,'pm_admin',0,NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Bizs','园区快讯','园区快讯','cs://1/image/aW1hZ2UvTVRvME1tSTFOakkyT0RObU4ySmtNemt4TldOa04yUTROV05tTldNMU4yTTBPQQ',1,1,48,'{"categoryId":0,"timeWidgetStyle":"date"}',3,0,1,1,'1',0,NULL,NULL,NULL,1,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Bizs','项目孵化','项目孵化','cs://1/image/aW1hZ2UvTVRvMVptSmxabUZoTmpGaFlXWm1NRGMzWW1VME1HTTNaR1l4T1RRMlpHVTBZdw',1,1,32,'{"type":1,"forumId":191712,"categoryId":1010,"parentId":110001,"tag":"创客"}',4,0,1,1,'1',0,NULL,NULL,NULL,1,'pm_admin',0,NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Bizs','投诉报修','投诉报修','cs://1/image/aW1hZ2UvTVRvMFlUWmhOR0ZqTmpJeFpEVmlPRGd5TmpabFpUUXhNV05qTlRaak5qRTROUQ',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=物业报修"}',6,0,1,1,'1',0,NULL,NULL,NULL,1,'pm_admin',0,NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Bizs','企业展示','企业展示','cs://1/image/aW1hZ2UvTVRvNU1qSmhObVU0WVdNd016VTBNRGt4WVRJM1pXUTBOR1l3WWpReE5UYzRPQQ',1,1,34,'{"type":3}',7,0,1,1,'1',0,NULL,NULL,NULL,1,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Bizs','工位预约','工位预约','cs://1/image/aW1hZ2UvTVRvNU1qSmhObVU0WVdNd016VTBNRGt4WVRJM1pXUTBOR1l3WWpReE5UYzRPQQ',1,1,13,CONCAT('{"url":"',@homeurl,'/station-booking/index.html?hideNavigationBar=1#/station_booking#sign_suffix"}'),8,0,1,1,'1',0,NULL,NULL,NULL,1,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Bizs','班车预约','班车预约','cs://1/image/aW1hZ2UvTVRvNE5tWmpPR00zTVdZMk5EaGxNamsxTkRnME5qUTFOamsyWWpkbVpUVXhOdw',1,1,14,'{"url":"http://wx.dudubashi.com/index.php/zuolin/Webauth/auth_login"}',9,0,1,1,'1',0,NULL,NULL,NULL,1,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Bizs','俱乐部','俱乐部','cs://1/image/aW1hZ2UvTVRwak5qY3pOakpsTmpObE9XWXdZVEEwWVRnMU9EZzNZelJsTmpBeE1UTmpZZw',1,1,36,'{"privateFlag": 0}',10,0,1,1,'1',0,NULL,NULL,NULL,1,'pm_admin',0,NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Bizs','官方活动','官方活动','cs://1/image/aW1hZ2UvTVRwaU1UWTRabVl5WkdKa01Ea3pNemxqTkdRNE1qTTJNV0UwTWpNMlkySXdNZw',1,1,50,'',11,0,1,1,'1',0,NULL,NULL,NULL,1,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Bizs','更多','更多','cs://1/image/aW1hZ2UvTVRvNE9UWTBaREZrTm1KaFptSXlNVFk1TnpoaU9UZGhOMlZtTnpFMk5qWTFNdw',1,1,1,'{"itemLocation":"/home", "itemGroup":"Bizs"}',10000,0,1,1,'1',0,NULL,NULL,NULL,0,'pm_admin',0,NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Enterprise','任务管理','任务管理','cs://1/image/aW1hZ2UvTVRwaVkyRmhZbUZsTUdVeE5EbGlaRFprTWpRNE9EQmxZV1E1WVdWaU5EVTBaQQ',1,1,56,'',12,0,1,1,'1',0,NULL,NULL,NULL,1,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Enterprise','打卡考勤','打卡考勤','cs://1/image/aW1hZ2UvTVRwbE1EUXpPVEF3Tm1Oa05EQXlaR05tTUdWa1lUTTVNREE0WmpWbE5UWTVZZw',1,1,23,'',13,0,1,1,'1',0,NULL,NULL,NULL,1,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Enterprise','通讯录','通讯录','cs://1/image/aW1hZ2UvTVRwak1XSXhPVFk0TnpneE1qazVZell4WWpCbFpXRXhNamt6TjJFM04yRXlPUQ',1,1,46,'',14,0,1,1,'1',0,NULL,NULL,NULL,1,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Enterprise','更多','更多','cs://1/image/aW1hZ2UvTVRwbE9HTTVZbUkxT1RVNFpERTVOekU1TldGa09ERmlOakJsTmpCaE16QTNNZw',1,1,1,'{"itemLocation":"/home", "itemGroup":"Enterprise"}',10000,0,1,1,'1',0,NULL,NULL,NULL,0,'pm_admin',0,NULL);

-- 服务广场
SET @item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
set @homeurl = (select VALUE from eh_configurations WHERE NAME = 'home.url' LIMIT 1);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','GovAgencies','园区介绍','园区介绍','cs://1/image/aW1hZ2UvTVRveE9XUXpOREZqWkRRM1pXTXlZemhsTVdSaFlUQmxOalkwWm1FNU5tTXhOUQ',1,1,13,CONCAT('{"url":"',@homeurl,'/park-introduction/index.html?hideNavigationBar=1#/#sign_suffix"}'),0,0,1,1,'1',0,NULL,NULL,NULL,0,'park_tourist',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','GovAgencies','园区招商','园区招商','cs://1/image/aW1hZ2UvTVRveU9ERmlabUpoTlRkaU56VmhZemN5TmpOaU9ESXdNR1ZoT1dVMU1ETmxZUQ',1,1,28,'',1,0,1,1,'1',0,NULL,NULL,NULL,0,'park_tourist',0,NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Bizs','园区快讯','园区快讯','cs://1/image/aW1hZ2UvTVRvME1tSTFOakkyT0RObU4ySmtNemt4TldOa04yUTROV05tTldNMU4yTTBPQQ',1,1,48,'{"categoryId":0,"timeWidgetStyle":"date"}',3,0,1,1,'1',0,NULL,NULL,NULL,1,'park_tourist',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Bizs','项目孵化','项目孵化','cs://1/image/aW1hZ2UvTVRvMVptSmxabUZoTmpGaFlXWm1NRGMzWW1VME1HTTNaR1l4T1RRMlpHVTBZdw',1,1,32,'{"type":1,"forumId":191712,"categoryId":1010,"parentId":110001,"tag":"创客"}',4,0,1,1,'1',0,NULL,NULL,NULL,1,'park_tourist',0,NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Bizs','投诉报修','投诉报修','cs://1/image/aW1hZ2UvTVRvMFlUWmhOR0ZqTmpJeFpEVmlPRGd5TmpabFpUUXhNV05qTlRaak5qRTROUQ',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=物业报修"}',6,0,1,1,'1',0,NULL,NULL,NULL,1,'park_tourist',0,NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Bizs','企业展示','企业展示','cs://1/image/aW1hZ2UvTVRvNU1qSmhObVU0WVdNd016VTBNRGt4WVRJM1pXUTBOR1l3WWpReE5UYzRPQQ',1,1,34,'{"type":3}',7,0,1,1,'1',0,NULL,NULL,NULL,1,'park_tourist',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Bizs','工位预约','工位预约','cs://1/image/aW1hZ2UvTVRvNU1qSmhObVU0WVdNd016VTBNRGt4WVRJM1pXUTBOR1l3WWpReE5UYzRPQQ',1,1,13,CONCAT('{"url":"',@homeurl,'/station-booking/index.html?hideNavigationBar=1#/station_booking#sign_suffix"}'),8,0,1,1,'1',0,NULL,NULL,NULL,1,'park_tourist',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Bizs','班车预约','班车预约','cs://1/image/aW1hZ2UvTVRvNE5tWmpPR00zTVdZMk5EaGxNamsxTkRnME5qUTFOamsyWWpkbVpUVXhOdw',1,1,14,'{"url":"http://wx.dudubashi.com/index.php/zuolin/Webauth/auth_login"}',9,0,1,1,'1',0,NULL,NULL,NULL,1,'park_tourist',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Bizs','俱乐部','俱乐部','cs://1/image/aW1hZ2UvTVRwak5qY3pOakpsTmpObE9XWXdZVEEwWVRnMU9EZzNZelJsTmpBeE1UTmpZZw',1,1,36,'{"privateFlag": 0}',10,0,1,1,'1',0,NULL,NULL,NULL,1,'park_tourist',0,NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Bizs','官方活动','官方活动','cs://1/image/aW1hZ2UvTVRwaU1UWTRabVl5WkdKa01Ea3pNemxqTkdRNE1qTTJNV0UwTWpNMlkySXdNZw',1,1,50,'',11,0,1,1,'1',0,NULL,NULL,NULL,1,'park_tourist',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Bizs','更多','更多','cs://1/image/aW1hZ2UvTVRvNE9UWTBaREZrTm1KaFptSXlNVFk1TnpoaU9UZGhOMlZtTnpFMk5qWTFNdw',1,1,1,'{"itemLocation":"/home", "itemGroup":"Bizs"}',10000,0,1,1,'1',0,NULL,NULL,NULL,0,'park_tourist',0,NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Enterprise','任务管理','任务管理','cs://1/image/aW1hZ2UvTVRwaVkyRmhZbUZsTUdVeE5EbGlaRFprTWpRNE9EQmxZV1E1WVdWaU5EVTBaQQ',1,1,56,'',12,0,1,1,'1',0,NULL,NULL,NULL,1,'park_tourist',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Enterprise','打卡考勤','打卡考勤','cs://1/image/aW1hZ2UvTVRwbE1EUXpPVEF3Tm1Oa05EQXlaR05tTUdWa1lUTTVNREE0WmpWbE5UWTVZZw',1,1,23,'',13,0,1,1,'1',0,NULL,NULL,NULL,1,'park_tourist',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Enterprise','通讯录','通讯录','cs://1/image/aW1hZ2UvTVRwak1XSXhPVFk0TnpneE1qazVZell4WWpCbFpXRXhNamt6TjJFM04yRXlPUQ',1,1,46,'',14,0,1,1,'1',0,NULL,NULL,NULL,1,'park_tourist',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Enterprise','更多','更多','cs://1/image/aW1hZ2UvTVRwbE9HTTVZbUkxT1RVNFpERTVOekU1TldGa09ERmlOakJsTmpCaE16QTNNZw',1,1,1,'{"itemLocation":"/home", "itemGroup":"Enterprise"}',10000,0,1,1,'1',0,NULL,NULL,NULL,0,'park_tourist',0,NULL);


-- 服务联盟
SET @category_id = (SELECT MAX(id) FROM eh_service_alliance_categories);
SET @sa_id = (SELECT MAX(id) FROM `eh_service_alliances`);
-- 园区食堂、园区商业、家园卡
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ((@category_id:=@category_id+1), 'community', 240111044332059898, '0', '服务联盟', '服务联盟', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, 999965, '');
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
    VALUES ((@sa_id := @sa_id + 1), '0', 'organaization', 1024525, '服务联盟', '服务联盟', @category_id, '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Bizs','服务联盟','服务联盟','cs://1/image/aW1hZ2UvTVRwaVlqUTVNalV4WVRReE9HRXhZMlV6TUdNek5qY3hZek5tWXpFeE5qSTRNQQ',1,1,33,CONCAT('{"type":',@category_id,',"parentId":',@category_id,',"displayType": "grid"}'),5,0,1,1,'1',0,NULL,NULL,NULL,1,'pm_admin',0,NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES
	((@item_id := @item_id + 1),999965,0,0,0,'/home','Bizs','服务联盟','服务联盟','cs://1/image/aW1hZ2UvTVRwaVlqUTVNalV4WVRReE9HRXhZMlV6TUdNek5qY3hZek5tWXpFeE5qSTRNQQ',1,1,33,CONCAT('{"type":',@category_id,',"parentId":',@category_id,',"displayType": "grid"}'),5,0,1,1,'1',0,NULL,NULL,NULL,1,'park_tourist',0,NULL);


-- 短信
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999965, 'sms.default.yzx', 1, 'zh_CN', '验证码-C时代', '147016');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999965, 'sms.default.yzx', 4, 'zh_CN', '派单-C时代', '147018');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999965, 'sms.default.yzx', 5, 'zh_CN', '任务-C时代', '147019');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999965, 'sms.default.yzx', 6, 'zh_CN', '任务2-C时代', '147020');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999965, 'sms.default.yzx', 7, 'zh_CN', '新报修-C时代', '147022');

INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999965, 'sms.default.yzx', 15, 'zh_CN', '物业任务3-C时代', '147021');

INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999965, 'sms.default.yzx', 51, 'zh_CN', '视频会-C时代', '147023');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999965, 'sms.default.yzx', 52, 'zh_CN', '视测会-C时代', '147024');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999965, 'sms.default.yzx', 53, 'zh_CN', '申诉-C时代', '147025');


-- ---------------自动生成的代码

INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES (114821,'EhOrganizations',1024525,'EhUsers',350687,1001,1,UTC_TIMESTAMP());

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458688,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-304','电子商务大厦','304','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458720,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-607','电子商务大厦','607','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458752,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-110','盈创大厦','110','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458784,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-705','盈创大厦','705','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458689,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-305','电子商务大厦','305','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458721,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-608','电子商务大厦','608','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458753,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-111','盈创大厦','111','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458785,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-706','盈创大厦','706','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458690,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-306','电子商务大厦','306','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458722,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-609','电子商务大厦','609','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458754,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-112','盈创大厦','112','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458786,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-707','盈创大厦','707','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458691,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-307','电子商务大厦','307','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458723,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-610','电子商务大厦','610','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458755,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-113','盈创大厦','113','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458787,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-708','盈创大厦','708','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458692,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-308','电子商务大厦','308','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458724,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-701','电子商务大厦','701','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458756,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-114','盈创大厦','114','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458788,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-801','盈创大厦','801','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458693,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-309','电子商务大厦','309','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458725,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-702','电子商务大厦','702','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458757,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-115','盈创大厦','115','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458789,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-802','盈创大厦','802','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458694,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-401','电子商务大厦','401','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458726,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-703','电子商务大厦','703','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458758,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-116','盈创大厦','116','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458790,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-803','盈创大厦','803','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458695,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-402','电子商务大厦','402','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458727,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-704','电子商务大厦','704','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458759,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-117','盈创大厦','117','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458791,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-804','盈创大厦','804','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458696,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-403','电子商务大厦','403','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458728,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-705','电子商务大厦','705','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458760,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-118','盈创大厦','118','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458792,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-805','盈创大厦','805','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458697,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-404','电子商务大厦','404','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458729,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-706','电子商务大厦','706','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458761,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-119','盈创大厦','119','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458793,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-806','盈创大厦','806','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458698,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-405','电子商务大厦','405','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458730,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-707','电子商务大厦','707','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458762,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-120','盈创大厦','120','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458794,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-807','盈创大厦','807','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458667,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-101','电子商务大厦','101','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458699,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-406','电子商务大厦','406','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458731,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-708','电子商务大厦','708','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458763,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-121','盈创大厦','121','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458795,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-808','盈创大厦','808','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458668,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-102','电子商务大厦','102','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458700,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-407','电子商务大厦','407','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458732,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-709','电子商务大厦','709','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458764,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-122','盈创大厦','122','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458796,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-809','盈创大厦','809','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458669,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-103','电子商务大厦','103','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458701,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-408','电子商务大厦','408','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458733,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-710','电子商务大厦','710','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458765,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-123','盈创大厦','123','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458797,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-810','盈创大厦','810','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458670,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-104','电子商务大厦','104','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458702,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-409','电子商务大厦','409','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458734,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-711','电子商务大厦','711','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458766,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-124','盈创大厦','124','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458798,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-811','盈创大厦','811','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458671,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-105','电子商务大厦','105','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458703,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-410','电子商务大厦','410','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458735,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-801','电子商务大厦','801','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458767,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-125','盈创大厦','125','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458799,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-812','盈创大厦','812','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458672,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-106','电子商务大厦','106','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458704,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-411','电子商务大厦','411','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458736,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-802','电子商务大厦','802','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458768,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-126','盈创大厦','126','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458800,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-813','盈创大厦','813','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458673,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-107','电子商务大厦','107','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458705,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-412','电子商务大厦','412','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458737,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-803','电子商务大厦','803','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458769,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-601','盈创大厦','601','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458801,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-901','盈创大厦','901','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458674,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-108','电子商务大厦','108','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458706,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-413','电子商务大厦','413','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458738,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-804','电子商务大厦','804','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458770,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-602','盈创大厦','602','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458802,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-902','盈创大厦','902','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458675,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-201','电子商务大厦','201','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458707,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-414','电子商务大厦','414','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458739,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-805','电子商务大厦','805','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458771,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-603','盈创大厦','603','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458803,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-903','盈创大厦','903','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458676,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-202','电子商务大厦','202','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458708,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-415','电子商务大厦','415','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458740,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-806','电子商务大厦','806','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458772,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-604','盈创大厦','604','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458804,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-904','盈创大厦','904','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458677,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-203','电子商务大厦','203','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458709,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-416','电子商务大厦','416','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458741,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-807','电子商务大厦','807','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458773,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-605','盈创大厦','605','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458805,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-905','盈创大厦','905','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458678,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-204','电子商务大厦','204','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458710,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-417','电子商务大厦','417','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458742,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-808','电子商务大厦','808','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458774,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-606','盈创大厦','606','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458806,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-906','盈创大厦','906','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458679,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-205','电子商务大厦','205','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458711,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-418','电子商务大厦','418','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458743,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-101','盈创大厦','101','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458775,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-607','盈创大厦','607','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458807,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-907','盈创大厦','907','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458680,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-206','电子商务大厦','206','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458712,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-419','电子商务大厦','419','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458744,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-102','盈创大厦','102','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458776,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-608','盈创大厦','608','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458808,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-908','盈创大厦','908','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458681,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-207','电子商务大厦','207','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458713,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-501','电子商务大厦','501','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458745,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-103','盈创大厦','103','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458777,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-609','盈创大厦','609','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458809,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-909','盈创大厦','909','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458682,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-208','电子商务大厦','208','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458714,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-601','电子商务大厦','601','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458746,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-104','盈创大厦','104','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458778,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-610','盈创大厦','610','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458683,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-209','电子商务大厦','209','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458715,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-602','电子商务大厦','602','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458747,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-105','盈创大厦','105','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458779,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-611','盈创大厦','611','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458684,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-210','电子商务大厦','210','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458716,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-603','电子商务大厦','603','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458748,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-106','盈创大厦','106','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458780,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-701','盈创大厦','701','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458685,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-301','电子商务大厦','301','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458717,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-604','电子商务大厦','604','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458749,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-107','盈创大厦','107','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458781,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-702','盈创大厦','702','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458686,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-302','电子商务大厦','302','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458718,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-605','电子商务大厦','605','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458750,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-108','盈创大厦','108','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458782,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-703','盈创大厦','703','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458687,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-303','电子商务大厦','303','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458719,UUID(),240111044332059898,64,'佛山市',65,'南海区','电子商务大厦-606','电子商务大厦','606','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458751,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-109','盈创大厦','109','2','0',UTC_TIMESTAMP(),999965,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387458783,UUID(),240111044332059898,64,'佛山市',65,'南海区','盈创大厦-704','盈创大厦','704','2','0',UTC_TIMESTAMP(),999965,NULL);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order)
	VALUES (1970658,240111044332059898,'盈创大厦','1号',0,18028165768,'宝石西路1号',NULL,'113.1011','23.336','ws0kv8jwn9sw','',NULL,2,0,NULL,1,NOW(),999965,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order)
	VALUES (1970659,240111044332059898,'盈富大厦','2号楼',0,18028165768,'宝石西路1号',NULL,'113.1011','23.336','ws0kv8jwn9sw','',NULL,2,0,NULL,1,NOW(),999965,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order)
	VALUES (1970660,240111044332059898,'电子商务大厦','3号楼',0,18028165768,'宝石西路1号',NULL,'113.1011','23.336','ws0kv8jwn9sw','',NULL,2,0,NULL,1,NOW(),999965,1);

INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `description`, `apt_count`, `creator_uid`, `status`, `create_time`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES (240111044332059898,UUID(),64,'佛山市',65,'南海区','凯泰C时代','C时代','佛山市南海区桂城街道宝石西路1号C时代南海互联网产业园夏北综合楼主楼801B室',' 凯泰C时代产业园（原名：C时代南海互联网产业园产业园）佛山市电子商务产业以及制造升级工业4.0的核心产业园，2015年被广东省科技厅认定为国家级科技企业孵化器培育单位，隶属凯泰投资控股集团，由旗下佛山市凯泰创展科技园有限公司进行全资开发管理。',0,1,2,UTC_TIMESTAMP(),0,191712,191713,UTC_TIMESTAMP(),999965);

INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`)
	VALUES (240111044331092703,240111044332059898,'',113.1011,23.336,'ws0kv8jwn9sw');

set @homeurl = (select VALUE from eh_configurations WHERE NAME = 'home.url' LIMIT 1);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` )
	VALUES (1775,'app.agreements.url',CONCAT(@homeurl,'/mobile/static/app_agreements/agreements.html?ns=999965'),'the relative path for cshidai app agreements',999965,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` )
	VALUES (1776,'business.url','https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix','biz access url for changfazhan',999965,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` )
	VALUES (1777,'pmtask.handler-999965','flow','0',999965,'物业报修工作流');

  INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES (191711,UUID(),999965,2,'EhGroups',1004280,'广东百花物业管理有限公司论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES (191712,UUID(),999965,2,'',0,'C时代社区论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES (191713,UUID(),999965,2,'',0,'C时代意见反馈论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1004280,UUID(),'广东百花物业管理有限公司','广东百花物业管理有限公司',1,1,1024525,'enterprise',1,1,UTC_TIMESTAMP(),UTC_TIMESTAMP(),191711,1,999965);

INSERT INTO `eh_namespaces` (`id`, `name`)
	VALUES (999965,'凯泰C时代');

INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`)
	VALUES (1350,999965,'community_residential',UTC_TIMESTAMP());

INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`)
	VALUES (19980,999965,'COMMUNITY',240111044332059898,UTC_TIMESTAMP());

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES (1024525,0,'PM','广东百花物业管理有限公司','广东百花物业管理有限公司成立于1996年，秉承“创新励行，精细至善”的服务宗旨，凭借多年的品牌、人才、管理经验的厚重积累，一直深耕于物业管理的专业领域。','/1024525',1,2,'ENTERPRISE',999965,1004280);

INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40680,1024525,240111044332059898,239825274387458687,'电子商务大厦-303',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40712,1024525,240111044332059898,239825274387458719,'电子商务大厦-606',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40744,1024525,240111044332059898,239825274387458751,'盈创大厦-109',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40776,1024525,240111044332059898,239825274387458783,'盈创大厦-704',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40681,1024525,240111044332059898,239825274387458688,'电子商务大厦-304',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40713,1024525,240111044332059898,239825274387458720,'电子商务大厦-607',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40745,1024525,240111044332059898,239825274387458752,'盈创大厦-110',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40777,1024525,240111044332059898,239825274387458784,'盈创大厦-705',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40682,1024525,240111044332059898,239825274387458689,'电子商务大厦-305',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40714,1024525,240111044332059898,239825274387458721,'电子商务大厦-608',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40746,1024525,240111044332059898,239825274387458753,'盈创大厦-111',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40778,1024525,240111044332059898,239825274387458785,'盈创大厦-706',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40683,1024525,240111044332059898,239825274387458690,'电子商务大厦-306',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40715,1024525,240111044332059898,239825274387458722,'电子商务大厦-609',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40747,1024525,240111044332059898,239825274387458754,'盈创大厦-112',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40779,1024525,240111044332059898,239825274387458786,'盈创大厦-707',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40684,1024525,240111044332059898,239825274387458691,'电子商务大厦-307',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40716,1024525,240111044332059898,239825274387458723,'电子商务大厦-610',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40748,1024525,240111044332059898,239825274387458755,'盈创大厦-113',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40780,1024525,240111044332059898,239825274387458787,'盈创大厦-708',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40685,1024525,240111044332059898,239825274387458692,'电子商务大厦-308',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40717,1024525,240111044332059898,239825274387458724,'电子商务大厦-701',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40749,1024525,240111044332059898,239825274387458756,'盈创大厦-114',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40781,1024525,240111044332059898,239825274387458788,'盈创大厦-801',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40686,1024525,240111044332059898,239825274387458693,'电子商务大厦-309',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40718,1024525,240111044332059898,239825274387458725,'电子商务大厦-702',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40750,1024525,240111044332059898,239825274387458757,'盈创大厦-115',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40782,1024525,240111044332059898,239825274387458789,'盈创大厦-802',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40687,1024525,240111044332059898,239825274387458694,'电子商务大厦-401',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40719,1024525,240111044332059898,239825274387458726,'电子商务大厦-703',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40751,1024525,240111044332059898,239825274387458758,'盈创大厦-116',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40783,1024525,240111044332059898,239825274387458790,'盈创大厦-803',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40688,1024525,240111044332059898,239825274387458695,'电子商务大厦-402',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40720,1024525,240111044332059898,239825274387458727,'电子商务大厦-704',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40752,1024525,240111044332059898,239825274387458759,'盈创大厦-117',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40784,1024525,240111044332059898,239825274387458791,'盈创大厦-804',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40689,1024525,240111044332059898,239825274387458696,'电子商务大厦-403',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40721,1024525,240111044332059898,239825274387458728,'电子商务大厦-705',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40753,1024525,240111044332059898,239825274387458760,'盈创大厦-118',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40785,1024525,240111044332059898,239825274387458792,'盈创大厦-805',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40690,1024525,240111044332059898,239825274387458697,'电子商务大厦-404',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40722,1024525,240111044332059898,239825274387458729,'电子商务大厦-706',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40754,1024525,240111044332059898,239825274387458761,'盈创大厦-119',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40786,1024525,240111044332059898,239825274387458793,'盈创大厦-806',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40691,1024525,240111044332059898,239825274387458698,'电子商务大厦-405',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40723,1024525,240111044332059898,239825274387458730,'电子商务大厦-707',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40755,1024525,240111044332059898,239825274387458762,'盈创大厦-120',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40787,1024525,240111044332059898,239825274387458794,'盈创大厦-807',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40660,1024525,240111044332059898,239825274387458667,'电子商务大厦-101',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40692,1024525,240111044332059898,239825274387458699,'电子商务大厦-406',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40724,1024525,240111044332059898,239825274387458731,'电子商务大厦-708',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40756,1024525,240111044332059898,239825274387458763,'盈创大厦-121',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40788,1024525,240111044332059898,239825274387458795,'盈创大厦-808',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40661,1024525,240111044332059898,239825274387458668,'电子商务大厦-102',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40693,1024525,240111044332059898,239825274387458700,'电子商务大厦-407',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40725,1024525,240111044332059898,239825274387458732,'电子商务大厦-709',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40757,1024525,240111044332059898,239825274387458764,'盈创大厦-122',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40789,1024525,240111044332059898,239825274387458796,'盈创大厦-809',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40662,1024525,240111044332059898,239825274387458669,'电子商务大厦-103',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40694,1024525,240111044332059898,239825274387458701,'电子商务大厦-408',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40726,1024525,240111044332059898,239825274387458733,'电子商务大厦-710',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40758,1024525,240111044332059898,239825274387458765,'盈创大厦-123',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40790,1024525,240111044332059898,239825274387458797,'盈创大厦-810',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40663,1024525,240111044332059898,239825274387458670,'电子商务大厦-104',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40695,1024525,240111044332059898,239825274387458702,'电子商务大厦-409',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40727,1024525,240111044332059898,239825274387458734,'电子商务大厦-711',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40759,1024525,240111044332059898,239825274387458766,'盈创大厦-124',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40791,1024525,240111044332059898,239825274387458798,'盈创大厦-811',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40664,1024525,240111044332059898,239825274387458671,'电子商务大厦-105',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40696,1024525,240111044332059898,239825274387458703,'电子商务大厦-410',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40728,1024525,240111044332059898,239825274387458735,'电子商务大厦-801',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40760,1024525,240111044332059898,239825274387458767,'盈创大厦-125',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40792,1024525,240111044332059898,239825274387458799,'盈创大厦-812',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40665,1024525,240111044332059898,239825274387458672,'电子商务大厦-106',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40697,1024525,240111044332059898,239825274387458704,'电子商务大厦-411',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40729,1024525,240111044332059898,239825274387458736,'电子商务大厦-802',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40761,1024525,240111044332059898,239825274387458768,'盈创大厦-126',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40793,1024525,240111044332059898,239825274387458800,'盈创大厦-813',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40666,1024525,240111044332059898,239825274387458673,'电子商务大厦-107',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40698,1024525,240111044332059898,239825274387458705,'电子商务大厦-412',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40730,1024525,240111044332059898,239825274387458737,'电子商务大厦-803',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40762,1024525,240111044332059898,239825274387458769,'盈创大厦-601',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40794,1024525,240111044332059898,239825274387458801,'盈创大厦-901',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40667,1024525,240111044332059898,239825274387458674,'电子商务大厦-108',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40699,1024525,240111044332059898,239825274387458706,'电子商务大厦-413',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40731,1024525,240111044332059898,239825274387458738,'电子商务大厦-804',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40763,1024525,240111044332059898,239825274387458770,'盈创大厦-602',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40795,1024525,240111044332059898,239825274387458802,'盈创大厦-902',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40668,1024525,240111044332059898,239825274387458675,'电子商务大厦-201',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40700,1024525,240111044332059898,239825274387458707,'电子商务大厦-414',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40732,1024525,240111044332059898,239825274387458739,'电子商务大厦-805',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40764,1024525,240111044332059898,239825274387458771,'盈创大厦-603',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40796,1024525,240111044332059898,239825274387458803,'盈创大厦-903',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40669,1024525,240111044332059898,239825274387458676,'电子商务大厦-202',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40701,1024525,240111044332059898,239825274387458708,'电子商务大厦-415',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40733,1024525,240111044332059898,239825274387458740,'电子商务大厦-806',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40765,1024525,240111044332059898,239825274387458772,'盈创大厦-604',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40797,1024525,240111044332059898,239825274387458804,'盈创大厦-904',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40670,1024525,240111044332059898,239825274387458677,'电子商务大厦-203',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40702,1024525,240111044332059898,239825274387458709,'电子商务大厦-416',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40734,1024525,240111044332059898,239825274387458741,'电子商务大厦-807',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40766,1024525,240111044332059898,239825274387458773,'盈创大厦-605',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40798,1024525,240111044332059898,239825274387458805,'盈创大厦-905',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40671,1024525,240111044332059898,239825274387458678,'电子商务大厦-204',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40703,1024525,240111044332059898,239825274387458710,'电子商务大厦-417',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40735,1024525,240111044332059898,239825274387458742,'电子商务大厦-808',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40767,1024525,240111044332059898,239825274387458774,'盈创大厦-606',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40799,1024525,240111044332059898,239825274387458806,'盈创大厦-906',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40672,1024525,240111044332059898,239825274387458679,'电子商务大厦-205',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40704,1024525,240111044332059898,239825274387458711,'电子商务大厦-418',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40736,1024525,240111044332059898,239825274387458743,'盈创大厦-101',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40768,1024525,240111044332059898,239825274387458775,'盈创大厦-607',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40800,1024525,240111044332059898,239825274387458807,'盈创大厦-907',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40673,1024525,240111044332059898,239825274387458680,'电子商务大厦-206',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40705,1024525,240111044332059898,239825274387458712,'电子商务大厦-419',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40737,1024525,240111044332059898,239825274387458744,'盈创大厦-102',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40769,1024525,240111044332059898,239825274387458776,'盈创大厦-608',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40801,1024525,240111044332059898,239825274387458808,'盈创大厦-908',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40674,1024525,240111044332059898,239825274387458681,'电子商务大厦-207',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40706,1024525,240111044332059898,239825274387458713,'电子商务大厦-501',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40738,1024525,240111044332059898,239825274387458745,'盈创大厦-103',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40770,1024525,240111044332059898,239825274387458777,'盈创大厦-609',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40802,1024525,240111044332059898,239825274387458809,'盈创大厦-909',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40675,1024525,240111044332059898,239825274387458682,'电子商务大厦-208',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40707,1024525,240111044332059898,239825274387458714,'电子商务大厦-601',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40739,1024525,240111044332059898,239825274387458746,'盈创大厦-104',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40771,1024525,240111044332059898,239825274387458778,'盈创大厦-610',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40676,1024525,240111044332059898,239825274387458683,'电子商务大厦-209',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40708,1024525,240111044332059898,239825274387458715,'电子商务大厦-602',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40740,1024525,240111044332059898,239825274387458747,'盈创大厦-105',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40772,1024525,240111044332059898,239825274387458779,'盈创大厦-611',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40677,1024525,240111044332059898,239825274387458684,'电子商务大厦-210',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40709,1024525,240111044332059898,239825274387458716,'电子商务大厦-603',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40741,1024525,240111044332059898,239825274387458748,'盈创大厦-106',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40773,1024525,240111044332059898,239825274387458780,'盈创大厦-701',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40678,1024525,240111044332059898,239825274387458685,'电子商务大厦-301',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40710,1024525,240111044332059898,239825274387458717,'电子商务大厦-604',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40742,1024525,240111044332059898,239825274387458749,'盈创大厦-107',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40774,1024525,240111044332059898,239825274387458781,'盈创大厦-702',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40679,1024525,240111044332059898,239825274387458686,'电子商务大厦-302',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40711,1024525,240111044332059898,239825274387458718,'电子商务大厦-605',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40743,1024525,240111044332059898,239825274387458750,'盈创大厦-108',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (40775,1024525,240111044332059898,239825274387458782,'盈创大厦-703',2);

SET @req_id = (SELECT  MAX(id) from eh_organization_community_requests);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES ((@req_id := @req_id + 1),240111044332059898,'organization',1024525,3,0,UTC_TIMESTAMP());

SET @member_id = (SELECT  MAX(id) from eh_organization_members);
INSERT INTO `eh_organization_members` (id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`)
	VALUES ((@member_id := @member_id + 1),1024525,'USER',350687,'manager','孔文豪',0,'15815656103',3,999965);

INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`)
	VALUES (63,0,'广东','GUANGDONG','GD','/广东',1,1,NULL,NULL,2,0,999965);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`)
	VALUES (64,63,'佛山市','FOSHANSHI','FSS','/广东/佛山市',2,2,NULL,'0757',2,0,999965);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`)
	VALUES (65,64,'南海区','NANHAIQU','NHQ','/广东/佛山市/南海区',3,3,NULL,'0757',2,0,999965);

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (350687,UUID(),'19156795824','孔文豪',NULL,1,45,'1','1','zh_CN','4b418c6b1788094a03209921b256334a','e2ba571fa684f6bbd53681bb6aad6de439459b94683f7e6ce356c9aaba03dc39',UTC_TIMESTAMP(),999965);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `comment_tag1`, `comment_tag2`, `comment_tag3`, `comment_tag4`, `comment_tag5`)
VALUES((@acl_id := @acl_id + 1),'EhOrganizations','1024525','1','10','350687','0','350687',now(),'999965','EhUsers','admin',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (323210,350687,0,'15815656103',NULL,3,UTC_TIMESTAMP(),999965);

INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` )
	VALUES (340,'Android_CShiDai',NULL,UTC_TIMESTAMP(),999965);
INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` )
	VALUES (341,'iOS_CShiDai',NULL,UTC_TIMESTAMP(),999965);

INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`)
	VALUES (625,340,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`)
	VALUES (626,341,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3046,32000,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3047,33000,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3048,34000,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3049,35000,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3050,50000,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3051,50100,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3052,50110,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3053,50200,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3054,50210,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3055,50220,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3056,50300,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3057,50400,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3058,50500,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3059,50600,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3060,50630,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3061,50631,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3062,50632,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3063,50633,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3064,50640,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3065,50650,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3066,50651,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3067,50652,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3068,50653,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3069,56161,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3070,60000,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3071,60100,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3072,60200,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3003,10000,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3004,10100,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3005,10400,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3006,10200,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3007,10600,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3008,10800,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3009,11000,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3010,20000,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3011,20100,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3012,20110,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3013,20120,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3014,20130,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3015,20140,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3016,20150,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3017,20155,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3018,20170,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3019,20180,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3020,20190,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3021,20191,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3022,20192,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3023,40000,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3024,40100,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3025,40110,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3026,40120,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3027,40200,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3028,40210,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3029,40220,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3030,40300,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3031,40400,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3032,40410,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3033,40420,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3034,40430,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3035,40440,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3036,40500,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3037,40510,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3038,40520,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3039,40530,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3040,40750,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3041,40700,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3042,41100,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3043,30000,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3044,30500,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3045,31000,NULL,'EhNamespaces',999965,2);
INSERT INTO `eh_organization_communities` (`organization_id`, `community_id`)
	VALUES (1024525,240111044332059898);

SET @lease_config_id = (SELECT MAX(id) FROM `eh_lease_configs`);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `rent_amount_flag`, `issuing_lease_flag`, `issuer_manage_flag`, `park_indroduce_flag`, `renew_flag`, `area_search_flag`) VALUES ((@lease_config_id := @lease_config_id + 1), '999965', '1', '1', '1', '1', '1', '0');

SET @eh_app_id = (SELECT MAX(id) FROM `eh_app_urls`);
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
	VALUES ((@eh_app_id := @eh_app_id+1), '999965', '凯泰C时代', '1', '', '', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
	VALUES ((@eh_app_id := @eh_app_id+1), '999965', '凯泰C时代', '2', '', '', '移动平台聚合服务，助力园区效能提升');

-- 仅仅beta执行
-- set @group_id = 1004280; -- 1042112
-- set @organization_id = 1024525; -- 1035855
-- update eh_forums set owner_id = @group_id WHERE id = 191711;

-- update eh_service_alliances SET owner_id = @organization_id WHERE type  = (SELECT id from eh_service_alliance_categories WHERE namespace_id = 999965) AND `name` = '服务联盟';
-- update eh_acl_role_assignments SET owner_id = @organization_id WHERE id = 114821;
-- update eh_organization_address_mappings SET organization_id = @organization_id WHERE id in (40680,40712,40744,40776,40681,40713,40745,40777,40682,40714,40746,40778,40683,40715,40747,40779,40684,40716,40748,40780,40685,40717,40749,40781,40686,40718,40750,40782,40687,40719,40751,40783,40688,40720,40752,40784,40689,40721,40753,40785,40690,40722,40754,40786,40691,40723,40755,40787,40660,40692,40724,40756,40788,40661,40693,40725,40757,40789,40662,40694,40726,40758,40790,40663,40695,40727,40759,40791,40664,40696,40728,40760,40792,40665,40697,40729,40761,40793,40666,40698,40730,40762,40794,40667,40699,40731,40763,40795,40668,40700,40732,40764,40796,40669,40701,40733,40765,40797,40670,40702,40734,40766,40798,40671,40703,40735,40767,40799,40672,40704,40736,40768,40800,40673,40705,40737,40769,40801,40674,40706,40738,40770,40802,40675,40707,40739,40771,40676,40708,40740,40772,40677,40709,40741,40773,40678,40710,40742,40774,40679,40711,40743,40775);
-- update eh_configurations SET `value` = REPLACE(`value`,'biz.zuolin.com','biz-beta.zuolin.com') WHERE `name` = 'business.url' AND namespace_id = 999965;
