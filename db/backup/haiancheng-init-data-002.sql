INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('65', 'app.agreements.url', 'http://core.zuolin.com/mobile/static/app_agreements/haian_agreements.html', 'the relative path for app agreements', '999993', NULL);
	
UPDATE `eh_banners` SET `poster_path` = 'cs://1/image/aW1hZ2UvTVRvelpXVXpOMk5sTVRoaE1UWXdZak16TnpNMFkyTTNOV1l5TURGbFpqaGhNQQ' WHERE `id` = 52;

UPDATE `eh_launch_pad_items` SET icon_uri = 'cs://1/image/aW1hZ2UvTVRveE9HRmlOelV6TUdJNE9ERTRaalF6Wm1Nd05EWmxabVkyTkRjMlltVTNaZw' WHERE id = 1772;

UPDATE `eh_launch_pad_items` SET icon_uri = 'cs://1/image/aW1hZ2UvTVRvNU5URTRPVGcxWVdOaE5HVXhZbUk1TkRRM1pEQmpNVE14WVRsa1ptUXhZdw' WHERE id = 1761;

UPDATE `eh_launch_pad_items` SET icon_uri = 'cs://1/image/aW1hZ2UvTVRwa09EVXlPR0prTUdNeVlqSXpZbUV6WkRnMVlqZ3dObVV5WVRZek56VmpZZw' WHERE id = 1764;
UPDATE `eh_launch_pad_items` SET icon_uri = 'cs://1/image/aW1hZ2UvTVRwak0yTmpPRGs1WkRZME5UWTFZall5WmpObFlUUTBaV1EzT1RReE5HSTFaZw' WHERE id = 1765;
UPDATE `eh_launch_pad_items` SET icon_uri = 'cs://1/image/aW1hZ2UvTVRvMk9EWXpZamxqTUdRd05URTRPRGczWkdKaE1UUmlNREJqTkdabU1tTTBaUQ' WHERE id = 1766;

UPDATE `eh_launch_pad_items` SET action_data = '{"itemLocation":"/home/Pm","layoutName":"PmLayout","title":"物业服务","entityTag":"PM"}' WHERE id = 1760;

DELETE FROM `eh_launch_pad_items` WHERE id=1771;
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) 
	VALUES (1771, 999993, 0, 0, 0, '/home', 'Coupons', '左邻小站', '左邻小站', 'cs://1/image/aW1hZ2UvTVRvek5XTXdZalZpWW1FM05Ua3pZbUppWWpVM00ySmhaamszTkRObU16RTJaZw', 5, 1, 14, '{"url":"https://biz.zuolin.com/zl-ec/index.jsp?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14477417463124576784#sign_suffix"}', 2, 0, 1, 1, '', 0,NULL);

UPDATE `eh_launch_pad_items` SET action_data = '{"url":"https://biz.zuolin.com/zl-ec/index.jsp?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fallpromotions#sign_suffix"}' WHERE id=1770;

UPDATE `eh_locale_templates` SET text = 20208 AND description='验证码-海岸城' WHERE id = 121;
UPDATE `eh_locale_templates` SET text = 20209 AND description='给被分配任务人员发短信-海岸城' WHERE id = 122;


