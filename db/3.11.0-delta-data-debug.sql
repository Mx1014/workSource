-- merge from customer-manage-1.1-delta-data-debug.sql by lqs 20161025
--
-- 车辆停车类型     add by xq.tian 2016/10/11
--
INSERT INTO `eh_parking_card_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `card_type`, `category_name`, `status`, `creator_uid`, `create_time`)
  VALUES ('1', '0', NULL, NULL, '1', '临时卡', '1', NULL, NULL);
INSERT INTO `eh_parking_card_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `card_type`, `category_name`, `status`, `creator_uid`, `create_time`)
  VALUES ('2', '0', NULL, NULL, '2', '月临时', '1', NULL, NULL);
INSERT INTO `eh_parking_card_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `card_type`, `category_name`, `status`, `creator_uid`, `create_time`)
  VALUES ('3', '0', NULL, NULL, '3', '充值卡', '1', NULL, NULL);
INSERT INTO `eh_parking_card_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `card_type`, `category_name`, `status`, `creator_uid`, `create_time`)
  VALUES ('4', '0', NULL, NULL, '4', '固定卡', '1', NULL, NULL);
INSERT INTO `eh_parking_card_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `card_type`, `category_name`, `status`, `creator_uid`, `create_time`)
  VALUES ('5', '0', NULL, NULL, '5', '免费卡', '1', NULL, NULL);
INSERT INTO `eh_parking_card_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `card_type`, `category_name`, `status`, `creator_uid`, `create_time`)
  VALUES ('6', '0', NULL, NULL, '6', '其它', '1', NULL, NULL);

--
-- 客户资料分类     add by xq.tian 2016/10/13
--
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`, `create_time`, `update_time`, `update_uid`)
  VALUES ('8', '0', 'none', '无', '1', NULL, NULL, NULL);

SET @locale_string_id = (SELECT MAX (id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@locale_string_id := @locale_string_id + 1), 'pm', '18001', 'zh_CN', '该记录已经处于未认证状态');
  
  
  
-- 服务市场类别 by sfyan 20161025
SET @launch_pad_item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag,delete_flag,scene_type,scale_type,service_categry_id) VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), 0, 0, 0, 0, '/home', 'Bizs', '全部', '全部', 'cs://1/image/aW1hZ2UvTVRvNE1XRTNZVEUxT0RGaE1EQmpZakF6TmprNE1EZ3paR1k0TVRVMFptUTJZdw', 1, 1, 53,'{"itemLocation":"/home","itemGroup":"Bizs"}', 30, 0, 1, 1, '', 0,NULL,1,'default',1,1);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag,delete_flag,scene_type,scale_type,service_categry_id) VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), 0, 0, 0, 0, '/home', 'Bizs', '全部', '全部', 'cs://1/image/aW1hZ2UvTVRvNE1XRTNZVEUxT0RGaE1EQmpZakF6TmprNE1EZ3paR1k0TVRVMFptUTJZdw', 1, 1, 53,'{"itemLocation":"/home","itemGroup":"Bizs"}', 30, 0, 1, 1, '', 0,NULL,1,'park_tourist',1,1);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag,delete_flag,scene_type,scale_type,service_categry_id) VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), 0, 0, 0, 0, '/home', 'Bizs', '全部', '全部', 'cs://1/image/aW1hZ2UvTVRvNE1XRTNZVEUxT0RGaE1EQmpZakF6TmprNE1EZ3paR1k0TVRVMFptUTJZdw', 1, 1, 53,'{"itemLocation":"/home","itemGroup":"Bizs"}', 30, 0, 1, 1, '', 0,NULL,1,'pm_admin',1,1);

INSERT INTO `eh_item_service_categries`(`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`) VALUES(1, '基本服务', '', 1, 0, 1, 0);
INSERT INTO `eh_item_service_categries`(`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`) VALUES(2, '物业服务', '', 1, 0, 1, 0);
INSERT INTO `eh_item_service_categries`(`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`) VALUES(3, '公司服务', '', 1, 0, 1, 0);

UPDATE `eh_launch_pad_items` SET service_categry_id = null,default_order=100000  WHERE namespace_id = 0 AND action_type IN (1,53);

DELETE FROM `eh_user_launch_pad_items` WHERE `item_id` IN (SELECT id FROM `eh_launch_pad_items` WHERE namespace_id = 0 AND action_type IN (53));
DELETE FROM `eh_launch_pad_items` WHERE namespace_id = 0 AND action_type IN (1);

UPDATE `eh_launch_pad_items` SET service_categry_id = 1 WHERE namespace_id = 0;
UPDATE `eh_launch_pad_layouts` SET layout_json = '{"versionCode":"2016102403","versionName":"3.3.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"园区服务","widget":"Navigator","instanceConfig":{"itemGroup":"CmntyServices"},"style":"Gallery","defaultOrder":3,"separatorFlag":1,"separatorHeight":21,"columnCount":8},{"groupName":"滚动广告","widget":"Bulletins","instanceConfig":{"itemGroup":""},"style":"Default","defaultOrder":4,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0,"editFlag":1,"title":"我的应用","iconUrl":"http://10.1.10.135:5000/image/aW1hZ2UvTVRvMlkyUmtNVEppTUdFellqVXpOR1JtTXpSa05XTmtPRFUyTlRneE1tWmhNQQ?token=bzW84VuKXgx7wfxYp1WmnWG5fyPOB2PeKoHm04UwN-Cs_udZWepmGMDY7SgjhNvBmt9M5AX9Y-IX7hHEdaExVnY3dNQ1phDeXcyD_Y3YAJM","align":"0"}]}' WHERE name = 'ServiceMarketLayout' AND namespace_id = 0 AND scene_type = 'default';
UPDATE `eh_launch_pad_layouts` SET layout_json = '{"versionCode":"2016102403","versionName":"3.3.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"园区服务","widget":"Navigator","instanceConfig":{"itemGroup":"CmntyServices"},"style":"Gallery","defaultOrder":3,"separatorFlag":1,"separatorHeight":21,"columnCount":8},{"groupName":"滚动广告","widget":"Bulletins","instanceConfig":{"itemGroup":""},"style":"Default","defaultOrder":4,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0,"editFlag":1,"title":"我的应用","iconUrl":"http://10.1.10.135:5000/image/aW1hZ2UvTVRvMlkyUmtNVEppTUdFellqVXpOR1JtTXpSa05XTmtPRFUyTlRneE1tWmhNQQ?token=bzW84VuKXgx7wfxYp1WmnWG5fyPOB2PeKoHm04UwN-Cs_udZWepmGMDY7SgjhNvBmt9M5AX9Y-IX7hHEdaExVnY3dNQ1phDeXcyD_Y3YAJM","align":"0"}]}' WHERE name = 'ServiceMarketLayout' AND namespace_id = 0 AND scene_type = 'park_tourist';
UPDATE `eh_launch_pad_layouts` SET layout_json = '{"versionCode":"2016102403","versionName":"3.3.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"园区服务","widget":"Navigator","instanceConfig":{"itemGroup":"CmntyServices"},"style":"Gallery","defaultOrder":3,"separatorFlag":1,"separatorHeight":21,"columnCount":8},{"groupName":"滚动广告","widget":"Bulletins","instanceConfig":{"itemGroup":""},"style":"Default","defaultOrder":4,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0,"editFlag":1,"title":"我的应用","iconUrl":"http://10.1.10.135:5000/image/aW1hZ2UvTVRvMlkyUmtNVEppTUdFellqVXpOR1JtTXpSa05XTmtPRFUyTlRneE1tWmhNQQ?token=bzW84VuKXgx7wfxYp1WmnWG5fyPOB2PeKoHm04UwN-Cs_udZWepmGMDY7SgjhNvBmt9M5AX9Y-IX7hHEdaExVnY3dNQ1phDeXcyD_Y3YAJM","align":"0"}]}' WHERE name = 'ServiceMarketLayout' AND namespace_id = 0 AND scene_type = 'pm_admin';
