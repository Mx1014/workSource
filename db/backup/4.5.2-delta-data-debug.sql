-- 修改审批字符串模板 add by xq.tian  2017/04/17
UPDATE `eh_locale_templates` SET `text` = '${userName}申请加入“${groupName}”（理由：${reason}），是否同意？'
WHERE `scope` = 'group.notification' AND `code` = 44 AND `namespace_id` = 0;
UPDATE `eh_locale_templates` SET `text` = '${userName}${description}申请加入公司“${enterpriseName}”，是否同意？'
WHERE `scope` = 'enterprise.notification' AND `code` = 7 AND `namespace_id` = 0;
UPDATE `eh_locale_templates` SET `text` = '您的家人${userName}申请加入${address}，是否同意？'
WHERE `scope` = 'family.notification' AND `code` = 2 AND `namespace_id` = 0;

-- 修改原来的“系统小助手” 名称为“系统消息”，并且修改成新的头像
UPDATE `eh_users` SET `nick_name` = '系统消息', `avatar` = 'cs://1/image/aW1hZ2UvTVRvM09HRXhaakF5TWpFek5UZGlOV00zWVRObE9Ua3lORGRrT0dJek1ETTVaZw' WHERE `id` = 2;
UPDATE `eh_users` SET `nick_name` = '电商小助手', `avatar` = 'cs://1/image/aW1hZ2UvTVRwbVptTXhZVFJrTVROaU16SmtZMkk1WXpBeE9XTXhZVEl5WmpSa1l6YzNaZw' WHERE `id` = 3;

 
-- 打卡/审批状态的文字描述，add by wh, 20170411
SET @id =(SELECT MAX(id) FROM eh_locale_strings); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','0','zh_CN','正常');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','1','zh_CN','迟到');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','2','zh_CN','早退');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','3','zh_CN','缺勤');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','4','zh_CN','迟到且早退');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','5','zh_CN','事假');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','6','zh_CN','病假');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','7','zh_CN','调休');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','8','zh_CN','公出');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','9','zh_CN','加班');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','10','zh_CN','半天事假'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','11','zh_CN','半天病假'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','12','zh_CN','半天调休'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','13','zh_CN','半天公出'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','14','zh_CN','忘打卡'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','15','zh_CN','已离职'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','16','zh_CN','未入职'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.excel','schedule','zh_CN','排班表'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.excel','rule','zh_CN','排班');  
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.push','1','zh_CN','考勤提醒：还有5分钟就要上班了，别忘记打卡哦');   
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.default','timeRuleName','zh_CN','休息');  
 

-- 考勤排班的菜单
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) VALUES('50634','查看排班表','50630',NULL,'punch--viewScheduling','0','2','/50000/50600/50630/50634','park',556,NULL);

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 1000000, 2); 
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 999992, 2);



SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10039, 50634, '考勤管理', 1, 1, '考勤管理 全部权限', 710);


-- 储能 add by sfyan 20170510
update `eh_launch_pad_layouts` set `version_code` = '2017051002', `layout_json` = replace(`layout_json`, '"versionCode":"2016081701"', '"versionCode":"2017051002"'), `layout_json` = replace(`layout_json`, '"columnCount":6', '"columnCount":4')), `layout_json` = replace(`layout_json`, '"style":"Gallery"', '"style":"Collection"') where `namespace_id` = 999990 and `name` = 'ServiceMarketLayout';

update `eh_launch_pad_layouts` set `version_code` = '2017051101', `layout_json` = '{"versionCode":"2017051101","versionName":"3.0.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"EnterpriseServices"},"style":"Collection","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Collection","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"Coupons","instanceConfig":{"itemGroup":"Coupons"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":21}]}' where `namespace_id` = 999990 and `name` = 'ServiceMarketLayout';

update `eh_launch_pad_items` set `icon_uri` = 'cs://1/image/aW1hZ2UvTVRwak16QmlNVFUzTjJNMU9UVXdOV1UxT0RCak5URXlNemd5WVRnMU5HVXpOUQ', `item_width` = 2 where `item_label` = '物业服务' and `namespace_id` = 999990;
update `eh_launch_pad_items` set `icon_uri` = 'cs://1/image/aW1hZ2UvTVRvMU5qQTFZMlZsTlRCbU9XUXhZVGRrTVRKbU1XRTJNMkUxWm1aallUbGtOZw', `item_width` = 2 where `item_label` = '通讯录' and `namespace_id` = 999990;
SET @launch_pad_item_id = (SELECT max(id) FROM `eh_launch_pad_items`);
insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) values((@launch_pad_item_id := @launch_pad_item_id + 1),'999990','0','0','0','/home','EnterpriseServices','企业采供','企业采供','cs://1/image/aW1hZ2UvTVRwalkySTNaVGhoWTJVeVpUZzFOREJtWm1ZMk5XSm1ORFJrTURnd05ESTNPUQ','2','1','14','','11','0','1','1','','0',NULL,NULL,NULL,'1','pm_admin','1',NULL,NULL,'0');
insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) values((@launch_pad_item_id := @launch_pad_item_id + 1),'999990','0','0','0','/home','EnterpriseServices','企业送水','企业送水','cs://1/image/aW1hZ2UvTVRvMFpqUTVZMkppWlRKaE9EZzFOamcwT0dSa1pHVmxPR0UyTURreE1HSmxZUQ','1','1','14','','12','0','1','1','','0',NULL,NULL,NULL,'1','pm_admin','1',NULL,NULL,'0');
insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) values((@launch_pad_item_id := @launch_pad_item_id + 1),'999990','0','0','0','/home','EnterpriseServices','企业人才','企业人才','cs://1/image/aW1hZ2UvTVRvMVpqaGtNamszT1RVM05UVXhaVEF5T0dNNU9EQXpOV1JrWkRJM05ERXhOUQ','1','1','14','','13','0','1','1','','0',NULL,NULL,NULL,'1','pm_admin','1',NULL,NULL,'0');
insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) values((@launch_pad_item_id := @launch_pad_item_id + 1),'999990','0','0','0','/home','EnterpriseServices','企业代理','企业代理','cs://1/image/aW1hZ2UvTVRvNU5HUXdNV0l3TW1OaVlUbGhOVGt4TURObE1qSTJPR0V6WkdOak9EZzBZdw','2','1','14','','14','0','1','1','','0',NULL,NULL,NULL,'1','pm_admin','1',NULL,NULL,'0');
insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) values((@launch_pad_item_id := @launch_pad_item_id + 1),'999990','0','0','0','/home','EnterpriseServices','企业IT服务','企业IT服务','cs://1/image/aW1hZ2UvTVRveU0yUmpaamcwTldaaU1HUTBPR1k1T0dFME4yVXlaR1V6WVRRNVptWmpaZw','1','1','14','','15','0','1','1','','0',NULL,NULL,NULL,'1','pm_admin','1',NULL,NULL,'0');
insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`) values((@launch_pad_item_id := @launch_pad_item_id + 1),'999990','0','0','0','/home','EnterpriseServices','项目定制','项目定制','cs://1/image/aW1hZ2UvTVRvMVpEQmxaV1JtTkRjMFltVTJNVEU0WTJObE1qTTFZamMzWVdNME1EQXpZUQ','1','1','14','','16','0','1','1','','0',NULL,NULL,NULL,'1','pm_admin','1',NULL,NULL,'0');

