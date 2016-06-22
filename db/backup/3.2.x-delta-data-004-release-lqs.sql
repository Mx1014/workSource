-- 更新海岸大厦海报图 20160223
UPDATE eh_buildings SET poster_uri='cs://1/image/aW1hZ2UvTVRwa09EQXlNell6WlRNMU1qZGlOVEUwTlRBM1pqQmxZakF3TXpKbVlqVTNZZw' WHERE id=176966;
INSERT INTO `eh_building_attachments` (`id`, `building_id`, `content_type`, `content_uri`, `creator_uid`, `create_time`) 
	VALUES(112,176966,NULL,'cs://1/image/aW1hZ2UvTVRwa09EQXlNell6WlRNMU1qZGlOVEUwTlRBM1pqQmxZakF3TXpKbVlqVTNZZw','1025','2016-02-23 14:27:15');

-- 海岸城小区修改为“海岸大厦” 20160223
UPDATE eh_communities set name="海岸大厦", alias_name="海岸大厦" WHERE id = 240111044331050812;

-- 龙岗增加缴费layout 20160223
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`) 
	VALUES (27, 999994, 'PaymentLayout', '{"versionCode":"2015120406","versionName":"3.0.0","layoutName":"PaymentLayout","displayName":"缴费首页","groups":[{"groupName":"pay","widget":"Navigator","instanceConfig":{"itemGroup":"PayActions"},"style":"Light","defaultOrder":1,"separatorFlag":0,"separatorHeight":0,"columnCount":3}]}', '2015120406', '0', '2', '2015-06-27 14:04:57');
	
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag)  
	VALUES (1846, 999994, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '物业费', '物业费', 'cs://1/image/aW1hZ2UvTVRwa05EQTJOVGxqTkRJMk56UmtaVEpqTm1ZeFlqazRPR1pqTkRJM01UTmpNQQ', '1', '1', '22', '', '0', '0', '1', '1', '', '0', null);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) 
	VALUES (1847, 999994, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '水费', '水费', 'cs://1/image/aW1hZ2UvTVRvd01XTXhPRGd5TnpZek9HUTFObVE1Wldaa01qQTNORFF6WlRjeU9XTmhZUQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', null);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag)  
	VALUES (1848, 999994, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '电费', '电费', 'cs://1/image/aW1hZ2UvTVRvd09UazFOMlkzWkRObFl6RTJZakEzT1RobU5EY3lZamN6TXpRM01tUTVOUQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', null);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag)  
	VALUES (1849, 999994, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '燃气费', '燃气费', 'cs://1/image/aW1hZ2UvTVRvNFpqVXhNREl6TmpneE1XVmxZVGhrTnpJd09XVXlaakZqWlRJeE1XUTFNQQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', null);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag)  
	VALUES (1850, 999994, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '固话宽带', '固话宽带', 'cs://1/image/aW1hZ2UvTVRwa05UWmxNVGt4TURObE9EbG1Oamd3TVdFMllUSTVOV1kwTlRNMk9HWmtNUQ', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', null);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag)  
	VALUES (1851, 999994, 0, '0', '0', '/home/Pm/Payment', 'PayActions', '有线电视', '有线电视', 'cs://1/image/aW1hZ2UvTVRwaU9EWTFObVJpTmpCallUSTFOams1T0RJNFpHWTJNV1E1T1dOaE1UWmtOdw', '1', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', null);
	
