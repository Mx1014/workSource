
update eh_organizations set name = '中洲科技' where id = 1000100;

update eh_launch_pad_layouts set layout_json='{"versionCode":"2015111401","versionName":"3.0.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Metro","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"CmntyServices"},"style":"Default","defaultOrder":5,"separatorFlag":1,"separatorHeight":21,"columnCount": 6}]}' where id = 21;


-- 删除优惠劵
delete from eh_launch_pad_items where id in (908,909);

update eh_launch_pad_items set bgcolor = 03 where id in (910,10331);

update eh_launch_pad_items set item_name='产业服务体系' where id = 914;
update eh_launch_pad_items set item_label='产业服务体系' where id = 914;
UPDATE eh_launch_pad_items set bgcolor = 10 where id = 914;
update eh_launch_pad_items set item_width= 1 where id = 914;
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvMlkyWmhPVE5qWWpNd1pEZ3dZMk16TlRReVpqTXpaREE1WXpsbE1UTTFOUQ' where id = 914;

delete from eh_launch_pad_items where id = 913;
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag,target_type, target_id, delete_flag, scene_type) 
	VALUES (913, 999999, 0, 0, 0, '/home', 'GovAgencies', '政府资源', '政府资源', 'cs://1/image/aW1hZ2UvTVRvM05UYzVZVGcwWldOak5qTmlaak16TVdZME4ySmtPREZsTkRVMk0yVmlPUQ', 1, 1, 14, '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', 3, 0, 1, 1, '', 02,NULL, NULL, NULL, '0', 'park_tourist');

UPDATE eh_launch_pad_items set item_name='公共服务平台' where id = 911;
UPDATE eh_launch_pad_items set item_label='公共服务平台' where id = 911;
UPDATE eh_launch_pad_items set bgcolor = 09 where id = 911;


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES ('929', '999999', '0', '0', '0', '/home', 'GovAgencies', '创客空间', '创客空间', 'cs://1/image/aW1hZ2UvTVRveU1qQTBNalkyTmpBMllqUXhZemxpWldZMVkyRXhPVFExTlRrMVl6WTRNZw', '1', '1', '32', '{"type":1,"forumId":177000,"categoryId":1003,"parentId":110001,"tag":"创客"}', '0', '0', '1', '1', '', '08', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES ('930', '999999', '0', '0', '0', '/home', 'GovAgencies', '企业共享平台', '企业共享平台', 'cs://1/image/aW1hZ2UvTVRvNE5qVTVNbVZpWWpCaE1EVTRPVEpqTnpZd1pURTJOR1ZtTW1VNFlUVTJOZw', '1', '1', '34', '{"type":3}', '0', '0', '1', '1', '', '07', NULL, NULL, NULL, '0', 'park_tourist');		
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES ('931', '999999', '0', '0', '0', '/home', 'GovAgencies', '便捷生活', '便捷生活', 'cs://1/image/aW1hZ2UvTVRwak56ZzRNMlV6TkRWbU5qZG1aVGxpWm1KaVpqZ3dPV0ZqWkRreVpEVm1aZw', '2', '1', '14', '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fpromotion%2Fall%3F_k%3Dzlbiz#sign_suffix"}', '7', '0', '1', '1', '', '06', NULL, NULL, NULL, '0', 'park_tourist');

	
	
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES ('932', '999999', '0', '0', '0', '/home', 'CmntyServices', '去认证', '去认证', 'cs://1/image/aW1hZ2UvTVRwbVlUa3pNbU0yTmpSall6Sm1aVGxqTldNME9HVmpPVFJqTlRrMk1XTTRaUQ', '2', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '1', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES ('933', '999999', '0', '0', '0', '/home', 'CmntyServices', '免费wifi', '免费wifi', 'cs://1/image/aW1hZ2UvTVRwalptRmpNbUZoTkdFMk0ySmxNREUxTlRGak1HUTBOemRtTWpFNU1EQm1OQQ', '2', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '2', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES ('944', '999999', '0', '0', '0', '/home', 'CmntyServices', '园区热线', '园区热线', 'cs://1/image/aW1hZ2UvTVRvNVpXWXhPR00wT0dSaU9ERTNNREUwTmprM05EUTJaVGRsWmpZeFltUXpPUQ', '2', '1', '45', '', '4', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');

	
update eh_launch_pad_items set id = 935 where id = 912;
update eh_launch_pad_items set item_group='CmntyServices' where id = 935;
update eh_launch_pad_items set item_width = 2 where id = 935;
update eh_launch_pad_items set default_order = 5 where id = 935;
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvMU5ETTFZMk5oWTJVME9HRTBNRFZrTUdSaFlqWTJNakE0T0dWak5XVXpNQQ' where id = 935;

	
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES ('936', '999999', '0', '0', '0', '/home', 'CmntyServices', '园区简介', '园区简介', 'cs://1/image/aW1hZ2UvTVRwaE5HVTNObVJoTWpJeU9EUXdNalZtWkRReE1qaG1PREl4T0RJeU5UZ3pOZw', '2', '1', '14', '{"url":"https://core.zuolin.com/web/lib/html/rich_text_review.html?id=56&banner=1"}', '3', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES ('938', '999999', '0', '0', '0', '/home', 'CmntyServices', '关于中州', '关于中州', 'cs://1/image/aW1hZ2UvTVRveVptUm1OV1JqWlRGa01UbG1PRFV5WmpkaE5USXlNR0prWXprM056WXhOZw', '2', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '6', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');


update eh_launch_pad_items set default_order = 1 where id = 910;
update eh_launch_pad_items set default_order = 2 where id = 914;
update eh_launch_pad_items set default_order = 3 where id = 913;
update eh_launch_pad_items set default_order = 4 where id = 911;
update eh_launch_pad_items set default_order = 5 where id = 929;
update eh_launch_pad_items set default_order = 6 where id = 930;
update eh_launch_pad_items set default_order = 7 where id = 931;






-- pm_admin
update eh_launch_pad_layouts set layout_json='{"versionCode":"2015111401","versionName":"3.0.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Metro","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"CmntyServices"},"style":"Default","defaultOrder":5,"separatorFlag":1,"separatorHeight":21,"columnCount": 6}]}' where id = 121;

delete from eh_launch_pad_items where id in (10329,10330);
update eh_launch_pad_items set item_name='产业服务体系' where id = 10335;
update eh_launch_pad_items set item_label='产业服务体系' where id = 10335;
UPDATE eh_launch_pad_items set bgcolor = 10 where id = 10335;
update eh_launch_pad_items set item_width= 1 where id = 10335;
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvMlkyWmhPVE5qWWpNd1pEZ3dZMk16TlRReVpqTXpaREE1WXpsbE1UTTFOUQ' where id = 10335;

-- 删除产业平台
delete from eh_launch_pad_items where id = 10334;

INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag,target_type, target_id, `delete_flag`, `scene_type`) 
	VALUES (10637, 999999, 0, 0, 0, '/home', 'GovAgencies', '政府资源', '政府资源', 'cs://1/image/aW1hZ2UvTVRvM05UYzVZVGcwWldOak5qTmlaak16TVdZME4ySmtPREZsTkRVMk0yVmlPUQ', 1, 1, 14, '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', 3, 0, 1, 1, '', 02,NULL, NULL, NULL, '0', 'pm_admin');

-- 将公告资源修改成公共服务平台
UPDATE eh_launch_pad_items set item_name='公共服务平台' where id = 10332;
UPDATE eh_launch_pad_items set item_label='公共服务平台' where id = 10332;
UPDATE eh_launch_pad_items set bgcolor = 09 where id = 10332;


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES ('10638', '999999', '0', '0', '0', '/home', 'GovAgencies', '创客空间', '创客空间', 'cs://1/image/aW1hZ2UvTVRveU1qQTBNalkyTmpBMllqUXhZemxpWldZMVkyRXhPVFExTlRrMVl6WTRNZw', '1', '1', '32', '{"type":1,"forumId":177000,"categoryId":1003,"parentId":110001,"tag":"创客"}', '0', '0', '1', '1', '', '08', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES ('10639', '999999', '0', '0', '0', '/home', 'GovAgencies', '企业共享平台', '企业共享平台', 'cs://1/image/aW1hZ2UvTVRvNE5qVTVNbVZpWWpCaE1EVTRPVEpqTnpZd1pURTJOR1ZtTW1VNFlUVTJOZw', '1', '1', '34', '{"type":3}', '0', '0', '1', '1', '', '07', NULL, NULL, NULL, '0', 'pm_admin');		
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES ('10649', '999999', '0', '0', '0', '/home', 'GovAgencies', '便捷生活', '便捷生活', 'cs://1/image/aW1hZ2UvTVRwak56ZzRNMlV6TkRWbU5qZG1aVGxpWm1KaVpqZ3dPV0ZqWkRreVpEVm1aZw', '2', '1', '14', '{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fpromotion%2Fall%3F_k%3Dzlbiz#sign_suffix"}', '2', '0', '1', '1', '', '06', NULL, NULL, NULL, '0', 'pm_admin');

	
	
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES ('10650', '999999', '0', '0', '0', '/home', 'CmntyServices', '去认证', '去认证', 'cs://1/image/aW1hZ2UvTVRwbVlUa3pNbU0yTmpSall6Sm1aVGxqTldNME9HVmpPVFJqTlRrMk1XTTRaUQ', '2', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '1', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES ('10651', '999999', '0', '0', '0', '/home', 'CmntyServices', '免费wifi', '免费wifi', 'cs://1/image/aW1hZ2UvTVRwalptRmpNbUZoTkdFMk0ySmxNREUxTlRGak1HUTBOemRtTWpFNU1EQm1OQQ', '2', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '2', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES ('10652', '999999', '0', '0', '0', '/home', 'CmntyServices', '园区热线', '园区热线', 'cs://1/image/aW1hZ2UvTVRvNVpXWXhPR00wT0dSaU9ERTNNREUwTmprM05EUTJaVGRsWmpZeFltUXpPUQ', '2', '1', '45', '', '4', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');

	
update eh_launch_pad_items set id = 10656 where id = 10333;
update eh_launch_pad_items set item_group='CmntyServices' where id = 10656;
update eh_launch_pad_items set item_width = 2 where id = 10656;
update eh_launch_pad_items set default_order = 5 where id = 10656;
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvMU5ETTFZMk5oWTJVME9HRTBNRFZrTUdSaFlqWTJNakE0T0dWak5XVXpNQQ' where id = 10656;

	
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES ('10657', '999999', '0', '0', '0', '/home', 'CmntyServices', '园区简介', '园区简介', 'cs://1/image/aW1hZ2UvTVRwaE5HVTNObVJoTWpJeU9EUXdNalZtWkRReE1qaG1PREl4T0RJeU5UZ3pOZw', '2', '1', '14', '{"url":"https://core.zuolin.com/web/lib/html/rich_text_review.html?id=56&banner=1"}', '3', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES ('10658', '999999', '0', '0', '0', '/home', 'CmntyServices', '关于中州', '关于中州', 'cs://1/image/aW1hZ2UvTVRveVptUm1OV1JqWlRGa01UbG1PRFV5WmpkaE5USXlNR0prWXprM056WXhOZw', '2', '1', '14', '{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '6', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');


update eh_launch_pad_items set default_order = 1 where id = 10331;
update eh_launch_pad_items set default_order = 2 where id = 10335;
update eh_launch_pad_items set default_order = 3 where id = 10367;
update eh_launch_pad_items set default_order = 4 where id = 10332;
update eh_launch_pad_items set default_order = 5 where id = 10638;
update eh_launch_pad_items set default_order = 6 where id = 10639;
update eh_launch_pad_items set default_order = 7 where id = 10649;