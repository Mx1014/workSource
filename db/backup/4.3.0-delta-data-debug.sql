
-- merge from sa2.1 by xiongying20170224
SET @layout_id = (SELECT max(id) FROM `eh_launch_pad_layouts`);  
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES ((@layout_id := @layout_id + 1), 999985, 'SecondServiceMarketLayout', '{"versionCode":"2017022202","versionName":"4.1.3","layoutName":"SecondServiceMarketLayout","displayName":"资产管理","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"OPPush","instanceConfig":{"itemGroup":"Gallery", "entityCount": 6, "newsSize":  5},"style":"LargeImageListView","defaultOrder":2,"separatorFlag":0,"separatorHeight":0}]}', '2017022202', '0', '2', UTC_TIMESTAMP(), 'pm_admin');	
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES ((@layout_id := @layout_id + 1), 999985, 'SecondServiceMarketLayout', '{"versionCode":"2017022202","versionName":"4.1.3","layoutName":"SecondServiceMarketLayout","displayName":"资产管理","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"OPPush","instanceConfig":{"itemGroup":"Gallery", "entityCount": 6, "newsSize":  5},"style":"LargeImageListView","defaultOrder":2,"separatorFlag":0,"separatorHeight":0}]}', '2017022202', '0', '2', UTC_TIMESTAMP(), 'park_tourist');	

    
    
    
    
SET @item_id = (SELECT max(id) FROM `eh_launch_pad_layouts`);    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999985, '0', '0', '0', '/secondhome', 'GovAgencies', '功能模块', '功能模块', 'cs://1/image/aW1hZ2UvTVRveE5qZGlOREUwT0RCaFkyUTFNakl4Wm1ReU4yTmxNV1F6WkRNeVpEVXlZdw', '1', '1', 14,'{"url":""}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999985, '0', '0', '0', '/secondhome', 'GovAgencies', '功能模块', '功能模块', 'cs://1/image/aW1hZ2UvTVRveE5qZGlOREUwT0RCaFkyUTFNakl4Wm1ReU4yTmxNV1F6WkRNeVpEVXlZdw', '1', '1', 14,'{"url":""}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999985, '0', '0', '0', '/secondhome', 'GovAgencies', '功能模块', '功能模块', 'cs://1/image/aW1hZ2UvTVRveE5qZGlOREUwT0RCaFkyUTFNakl4Wm1ReU4yTmxNV1F6WkRNeVpEVXlZdw', '1', '1', 14,'{"url":""}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999985, '0', '0', '0', '/secondhome', 'GovAgencies', '功能模块', '功能模块', 'cs://1/image/aW1hZ2UvTVRveE5qZGlOREUwT0RCaFkyUTFNakl4Wm1ReU4yTmxNV1F6WkRNeVpEVXlZdw', '1', '1', 14,'{"url":""}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
    VALUES ((@item_id := @item_id + 1), 999985, 0, 0, 0, '/secondhome', 'Gallery', '服务联盟', '服务联盟', 'cs://1/image/aW1hZ2UvTVRvMVkyVm1Oak5oWlRoaE56UTNNV1EwWVRKaU4yRmtNalptT1RZNFpEazFPQQ', 1, 1, 33, '{"type":150,"parentId":150,"displayType": "grid"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '0', 'pm_admin');    


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999985, '0', '0', '0', '/secondhome', 'GovAgencies', '功能模块', '功能模块', 'cs://1/image/aW1hZ2UvTVRveE5qZGlOREUwT0RCaFkyUTFNakl4Wm1ReU4yTmxNV1F6WkRNeVpEVXlZdw', '1', '1', 14,'{"url":""}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999985, '0', '0', '0', '/secondhome', 'GovAgencies', '功能模块', '功能模块', 'cs://1/image/aW1hZ2UvTVRveE5qZGlOREUwT0RCaFkyUTFNakl4Wm1ReU4yTmxNV1F6WkRNeVpEVXlZdw', '1', '1', 14,'{"url":""}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999985, '0', '0', '0', '/secondhome', 'GovAgencies', '功能模块', '功能模块', 'cs://1/image/aW1hZ2UvTVRveE5qZGlOREUwT0RCaFkyUTFNakl4Wm1ReU4yTmxNV1F6WkRNeVpEVXlZdw', '1', '1', 14,'{"url":""}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES ((@item_id := @item_id + 1), 999985, '0', '0', '0', '/secondhome', 'GovAgencies', '功能模块', '功能模块', 'cs://1/image/aW1hZ2UvTVRveE5qZGlOREUwT0RCaFkyUTFNakl4Wm1ReU4yTmxNV1F6WkRNeVpEVXlZdw', '1', '1', 14,'{"url":""}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
    
    
    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
    VALUES ((@item_id := @item_id + 1), 999985, 0, 0, 0, '/secondhome', 'Gallery', '服务联盟', '服务联盟', 'cs://1/image/aW1hZ2UvTVRvMVkyVm1Oak5oWlRoaE56UTNNV1EwWVRKaU4yRmtNalptT1RZNFpEazFPQQ', 1, 1, 33, '{"type":150,"parentId":150,"displayType": "grid"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '0', 'park_tourist');    

    
-- app url更新数据
delete from eh_app_urls;


INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('1', '0', '左邻', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.yjtc.everhomes', 'cs://1/image/aW1hZ2UvTVRwbU5qQXhOVFJtWW1FNU5UazNObUkyTldFeU5HWTFOekJpTWpWaU5XUTNNUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('2', '0', '左邻', '1', 'https://itunes.apple.com/cn/app/%E5%B7%A6%E9%82%BB/id767285002?mt=8', 'cs://1/image/aW1hZ2UvTVRwbU5qQXhOVFJtWW1FNU5UazNObUkyTldFeU5HWTFOekJpTWpWaU5XUTNNUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('3', '1000000', '深圳科技园', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.park.tec_park', 'cs://1/image/aW1hZ2UvTVRvMllqaG1NalpoT1RkaU9HVm1OalJoTVRnek1tWTNPV1l5WmpOalpHUTNOZw', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('4', '1000000', '深圳科技园', '1', 'https://itunes.apple.com/cn/app/%E6%B7%B1%E5%9C%B3%E7%A7%91%E6%8A%80%E5%9B%AD/id1067766906?mt=8', 'cs://1/image/aW1hZ2UvTVRvMllqaG1NalpoT1RkaU9HVm1OalJoTVRnek1tWTNPV1l5WmpOalpHUTNOZw', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('5', '999993', '海岸馨服务', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.haian.park', 'cs://1/image/aW1hZ2UvTVRvME9XTTRNV0ZsTm1FMU9XWTFNR1ExT0RGa05ERTFZMlppTVRBMFpqVXhZUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('6', '999993', '海岸馨服务', '1', 'https://itunes.apple.com/cn/app/%E6%B5%B7%E5%B2%B8%E9%A6%A8%E6%9C%8D%E5%8A%A1/id1084272463?mt=8', 'cs://1/image/aW1hZ2UvTVRvME9XTTRNV0ZsTm1FMU9XWTFNR1ExT0RGa05ERTFZMlppTVRBMFpqVXhZUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('7', '999992', '深业物业', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.shenye', 'cs://1/image/aW1hZ2UvTVRvM09EZ3daR1JrWkRZM01qZGxObUU1T0dFeFlqWTBOemxoWW1JMU1qZzNOdw', '做最值得托付的物业服务集成商');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('8', '999992', '深业物业', '1', 'https://itunes.apple.com/cn/app/%E6%B7%B1%E4%B8%9A%E7%89%A9%E4%B8%9A/id1113956010?mt=8', 'cs://1/image/aW1hZ2UvTVRvM09EZ3daR1JrWkRZM01qZGxObUU1T0dFeFlqWTBOemxoWW1JMU1qZzNOdw', '做最值得托付的物业服务集成商');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('9', '999999', '中洲智邦', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.park.xmtec', 'cs://1/image/aW1hZ2UvTVRveE1tRTBOVGN6TmpBMllqVXpOems1WlRnMVpUZzJaalF5WW1SbE56Z3lPUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('10', '999999', '中洲智邦', '1', 'https://itunes.apple.com/cn/app/%E4%B8%AD%E6%B4%B2%E6%99%BA%E9%82%A6/id1070099370?mt=8', 'cs://1/image/aW1hZ2UvTVRveE1tRTBOVGN6TmpBMllqVXpOems1WlRnMVpUZzJaalF5WW1SbE56Z3lPUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('11', '999990', 'UFine', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.chuneng.park', 'cs://1/image/aW1hZ2UvTVRwak1ESXhNelEzT1dVMU5XTmlZbUk1TldNNE1EQXpZMkUwTkdWaE5qVXpNQQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('12', '999990', 'UFine', '1', 'https://itunes.apple.com/cn/app/ufine/id1112434179?mt=8', 'cs://1/image/aW1hZ2UvTVRwak1ESXhNelEzT1dVMU5XTmlZbUk1TldNNE1EQXpZMkUwTkdWaE5qVXpNQQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('13', '999989', 'Ibase', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.ibase', 'cs://1/image/aW1hZ2UvTVRveU5tSTRaVFZoTWpRM05EaGpOVEUxTlRKaE16RTFZVE5oTW1WbVlUQTJOdw', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('14', '999989', 'Ibase', '1', 'https://itunes.apple.com/cn/app/ibase/id1133031529?mt=8', 'cs://1/image/aW1hZ2UvTVRveU5tSTRaVFZoTWpRM05EaGpOVEUxTlRKaE16RTFZVE5oTW1WbVlUQTJOdw', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('15', '999991', '威新LINK+', '2', 'http://www.wandoujia.com/apps/com.everhomes.android.jindi.park', 'cs://1/image/aW1hZ2UvTVRwaFl6VXhaV0ZpTldNNVpXSTVNMkppTnpSak1qVmtNREkyTldRMU1qTXlPUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('16', '999991', '威新LINK+', '1', 'https://itunes.apple.com/cn/app/%E5%A8%81%E6%96%B0link/id1112433619?mt=8', 'cs://1/image/aW1hZ2UvTVRwaFl6VXhaV0ZpTldNNVpXSTVNMkppTnpSak1qVmtNREkyTldRMU1qTXlPUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('17', '999988', 'π星球爱特家', '2', 'http://app.mi.com/details?id=com.everhomes.android.atmini', 'cs://1/image/aW1hZ2UvTVRveU5qa3lZVFUzTlRRd01XWXpNRGd6WldWaU5ETTBNMk13TVdZd1lqUTVOZw', 'π星球智慧社区');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('18', '999988', 'π星球爱特家', '1', 'https://itunes.apple.com/cn/app/%CF%80%E6%98%9F%E7%90%83%E7%88%B1%E7%89%B9%E5%AE%B6/id1153945560?mt=8', 'cs://1/image/aW1hZ2UvTVRveU5qa3lZVFUzTlRRd01XWXpNRGd6WldWaU5ETTBNMk13TVdZd1lqUTVOZw', 'π星球智慧社区');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('19', '999987', '深圳湾', '2', 'http://www.wandoujia.com/apps/com.everhomes.android.shenzhenbay', 'cs://1/image/aW1hZ2UvTVRvNVlUSTNOR1l4TlRsaVlUTXlPRFJrWWpRMk9EQmtPRGd5T1dZMU9HWTBZdw', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('20', '999987', '深圳湾', '1', 'https://itunes.apple.com/cn/app/%E6%B7%B1%E5%9C%B3%E6%B9%BE%E5%88%9B%E4%B8%9A%E5%B9%BF%E5%9C%BA/id1163458833?mt=8', 'cs://1/image/aW1hZ2UvTVRvNVlUSTNOR1l4TlRsaVlUTXlPRFJrWWpRMk9EQmtPRGd5T1dZMU9HWTBZdw', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('21', '999986', '创源', '2', 'http://sj.qq.com/myapp/detail.htm?apkName=com.everhomes.android.innospring', 'cs://1/image/aW1hZ2UvTVRvd1pXRTVOV1UxWVdJeU0ySXlZemxoWlRKaFlqazBZVGswTlRobVlqZGpaZw', '全球创业梦想，从创源开始');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('22', '999986', '创源', '1', 'https://itunes.apple.com/cn/app/%E5%88%9B%E6%BA%90/id1160563917?mt=8', 'cs://1/image/aW1hZ2UvTVRvd1pXRTVOV1UxWVdJeU0ySXlZemxoWlRKaFlqazBZVGswTlRobVlqZGpaZw', '全球创业梦想，从创源开始');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('23', '999985', 'Officeasy', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.officeasy', 'cs://1/image/aW1hZ2UvTVRvd1pUVm1OemRtWXpWak5UZGhOMlF5TkRNNVlqTTNZalV4T0RNM05qZzFPUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('24', '999985', 'Officeasy', '1', 'https://itunes.apple.com/cn/app/officeasy/id1168934037?mt=8', 'cs://1/image/aW1hZ2UvTVRvd1pUVm1OemRtWXpWak5UZGhOMlF5TkRNNVlqTTNZalV4T0RNM05qZzFPUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('25', '999983', '正中会', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.kexin', 'cs://1/image/aW1hZ2UvTVRwa01tSTJZVFUzTVRrMk1qZ3pZekJpWXpkaVpUY3pOemc1WVdVeU1qQTFZUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('26', '999983', '正中会', '1', 'https://itunes.apple.com/cn/app/%E6%AD%A3%E4%B8%AD%E4%BC%9A/id1186600259?mt=8', 'cs://1/image/aW1hZ2UvTVRwa01tSTJZVFUzTVRrMk1qZ3pZekJpWXpkaVpUY3pOemc1WVdVeU1qQTFZUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('27', '999984', '清华信息港', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.tsinghua.infobay', 'cs://1/image/aW1hZ2UvTVRwaU1UVmpNelk1T0dOa00ySXlaVE5pWlRVMU1XRXpZV1ZsTUdZMU1HUmlaQQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('28', '999984', '清华信息港', '1', 'https://itunes.apple.com/cn/app/%E7%B4%AB%E8%8D%86/id1187170142?mt=8', 'cs://1/image/aW1hZ2UvTVRwaU1UVmpNelk1T0dOa00ySXlaVE5pWlRVMU1XRXpZV1ZsTUdZMU1HUmlaQQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('29', '999982', 'T+SPACE', '2', 'http://www.wandoujia.com/apps/com.everhomes.android.tspace', 'cs://1/image/aW1hZ2UvTVRvME5ETTJOR1pqTVRJM1ptUm1NbUk0WldSbU1tTXlZV0k0T1RCbVlqZzJPQQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('30', '999982', 'T+SPACE', '1', 'https://itunes.apple.com/cn/app/t-space/id1185749287?mt=8', 'cs://1/image/aW1hZ2UvTVRvME5ETTJOR1pqTVRJM1ptUm1NbUk0WldSbU1tTXlZV0k0T1RCbVlqZzJPQQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('31', '999981', '星商汇园区', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.vanke.xsh', 'cs://1/image/aW1hZ2UvTVRvNU5UaGlaV0U0WlRWaU1UZzNOVEU0Tm1Zd1lqTTRNVFZqWVRjek1EUXlPUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('32', '999981', '星商汇园区', '1', 'https://itunes.apple.com/cn/app/%E6%98%9F%E5%95%86%E6%B1%87%E5%9B%AD%E5%8C%BA/id1194430405?mt=8', 'cs://1/image/aW1hZ2UvTVRvNU5UaGlaV0U0WlRWaU1UZzNOVEU0Tm1Zd1lqTTRNVFZqWVRjek1EUXlPUQ', '移动平台聚合服务，助力园区效能提升');
