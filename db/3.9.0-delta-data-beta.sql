-- merge from community-import-2.0.0-delta-data-debug.sql 20160826
-- 升级使用的url
set @configuration_id = (select max(id) from eh_configurations) +1;
INSERT INTO `eh_configurations` VALUES (@configuration_id, 'upgrade.url', '/management/views/upgrade.html', 'upgrade url', 0, NULL);


-- merge from organization-delta-data-release.sql 20160826
-- 支持多部门，需要队之前的数据模型进行处理  by sfyan 20160812
SET @organization_member_id = (SELECT MAX(`id`) FROM `eh_organization_members`);
INSERT INTO `eh_organization_members` (`id`,`organization_id`,`target_type`,`target_id`,`member_group`,`contact_name`,`contact_type`,`contact_token`,`contact_description`,`status`,`employee_no`,`avatar`,`group_id`,`group_path`,`gender`,`update_time`,`create_time`,`integral_tag1`,`integral_tag2`,`integral_tag3`,`integral_tag4`,`integral_tag5`,`string_tag1`,`string_tag2`,`string_tag3`,`string_tag4`,`string_tag5`,`namespace_id`)
SELECT (@organization_member_id := @organization_member_id + 1),`group_id`,`target_type`,`target_id`,`member_group`,`contact_name`,`contact_type`,`contact_token`,`contact_description`,`status`,`employee_no`,`avatar`,0,`group_path`,`gender`,`update_time`,`create_time`,`integral_tag1`,`integral_tag2`,`integral_tag3`,`integral_tag4`,`integral_tag5`,`string_tag1`,`string_tag2`,`string_tag3`,`string_tag4`,`string_tag5`,`namespace_id` FROM `eh_organization_members` WHERE `group_id` IS NOT NULL AND `group_id` != 0;

-- 保证数据处理完后 再修改 之前的数据
UPDATE `eh_organization_members` SET `group_id` = 0 WHERE `group_id` IS NOT NULL AND `group_id` != 0;



-- merge from  serviceAlliance3.0-delta-data-release.sql 20160826
-- 迁移yellowpage中的数据
INSERT INTO `eh_service_alliances` (`id`,`parent_id`,`owner_type`,`owner_id`,`name`,`display_name`,`type`,`address`,`contact`,`description`,`poster_uri`,`status`,`default_order`,`longitude`,`latitude`,`geohash`,`discount`,`category_id`,`contact_name`,`contact_mobile`,`service_type`,`service_url`,`discount_desc`,`creator_uid`,`create_time`)
SELECT `id`,`parent_id`,`owner_type`,`owner_id`,`name`,`nick_name`,`type`,`address`,`contact`,`description`,`poster_uri`,`status`,`default_order`,`longitude`,`latitude`,`geohash`,`integral_tag1`,`integral_tag2`,`string_tag1`,`string_tag2`,`string_tag3`,`string_tag4`,`string_tag5`,`creator_uid`,`create_time` FROM `eh_yellow_pages` WHERE `type` = 2;

-- 迁移yellowpage attachment中的数据
INSERT INTO `eh_service_alliance_attachments` (`id`,`owner_id`,`content_type`,`content_uri`,`creator_uid`,`create_time`)
SELECT `id`,`owner_id`,`content_type`,`content_uri`,`creator_uid`,`create_time` FROM `eh_yellow_page_attachments`;


INSERT INTO `eh_service_alliance_categories` (`id`, `parent_id`, `name`, `path`, `status`, `creator_uid`, `create_time`, `namespace_id`) 
    VALUES ('1', '0', '服务联盟类型', '服务联盟类型', '2', '1', UTC_TIMESTAMP(), '1000000');
INSERT INTO `eh_service_alliance_categories` (`id`, `parent_id`, `name`, `path`, `status`, `creator_uid`, `create_time`, `namespace_id`) 
    VALUES ('2', '0', '服务联盟类型', '服务联盟类型', '2', '1', UTC_TIMESTAMP(), '999990');
INSERT INTO `eh_service_alliance_categories` (`id`, `parent_id`, `name`, `path`, `status`, `creator_uid`, `create_time`, `namespace_id`) 
    VALUES ('3', '0', '服务联盟类型', '服务联盟类型', '2', '1', UTC_TIMESTAMP(), '999999');
INSERT INTO `eh_service_alliance_categories` (`id`, `parent_id`, `name`, `path`, `status`, `creator_uid`, `create_time`, `namespace_id`) 
    VALUES ('4', '0', '政府资源类型', '政府资源类型', '2', '1', UTC_TIMESTAMP(), '999999');
    

update eh_service_alliances set type = 1 where owner_id = 240111044331048623;
update eh_service_alliances set type = 2 where owner_id = 240111044331051500;
update eh_service_alliances set type = 3 where owner_id = 240111044331049963;

INSERT INTO `eh_service_alliance_categories` (`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`)
SELECT `id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id` FROM `eh_categories` WHERE `parent_id` = 100001;

update eh_service_alliance_categories SET owner_type = 'community';
update eh_service_alliance_categories SET owner_id = 240111044331048623 WHERE namespace_id = 1000000;
update eh_service_alliance_categories SET parent_id = 1 WHERE namespace_id = 1000000;
update eh_service_alliance_categories SET owner_id = 240111044331051500 WHERE namespace_id = 999990;
update eh_service_alliance_categories SET parent_id = 2 WHERE namespace_id = 999990;
update eh_service_alliance_categories SET owner_id = 240111044331049963 WHERE namespace_id = 999999;
update eh_service_alliance_categories SET parent_id = 3 WHERE namespace_id = 999999;

update eh_launch_pad_items set action_data = '{"type":1,"parentId":100001}' where action_type = 33 and namespace_id = 1000000;
update eh_launch_pad_items set action_data = '{"type":2,"parentId":100001}' where action_type = 33 and namespace_id = 999990;
update eh_launch_pad_items set action_data = '{"type":3,"parentId":100001}' where action_type = 33 and namespace_id = 999999;

-- 物业报修2.0
SET @eh_launch_pad_items_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
  VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 999992, 0, 0, 0, '/home', 'Bizs', '物业报修2.0', '物业报修2.0', 'cs://1/image/aW1hZ2UvTVRwak5ETXlaakJsWkRJNFlqQmhabVJpTmpnd1lXSTFOelF4TjJGbU0yUXpOQQ', 1, 1, 14, '{"url":"http://beta.zuolin.com/property_service/index.html?hideNavigationBar=1#/my_service#sign_suffix"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'park_tourist');    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
  VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 999992, 0, 0, 0, '/home', 'Bizs', '物业报修2.0', '物业报修2.0', 'cs://1/image/aW1hZ2UvTVRwak5ETXlaakJsWkRJNFlqQmhabVJpTmpnd1lXSTFOelF4TjJGbU0yUXpOQQ', 1, 1, 14, '{"url":"http://beta.zuolin.com/property_service/index.html?hideNavigationBar=1#/my_service#sign_suffix"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'pm_admin');  

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
  VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 999992, 0, 0, 0, '/home', 'Bizs', 'PM_TASK', '任务2.0', 'cs://1/image/aW1hZ2UvTVRwak5ETXlaakJsWkRJNFlqQmhabVJpTmpnd1lXSTFOelF4TjJGbU0yUXpOQQ', 1, 1, 51, '', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'park_tourist');    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
  VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 999992, 0, 0, 0, '/home', 'Bizs', 'PM_TASK', '任务2.0', 'cs://1/image/aW1hZ2UvTVRwak5ETXlaakJsWkRJNFlqQmhabVJpTmpnd1lXSTFOelF4TjJGbU0yUXpOQQ', 1, 1, 51, '', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'pm_admin');  


INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('150', 'pmtask.category.ancestor', '任务', '任务分类名称', '0', NULL);

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ('180', 'pmtask.notification', '1', 'zh_CN', '任务操作模版', '${operatorName} ${operatorPhone}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ('181', 'pmtask.notification', '2', 'zh_CN', '任务操作模版', '${operatorName} ${operatorPhone} 已将任务分配给了 ${targetName} ${targetPhone}，将会很快联系您。', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ('182', 'pmtask.notification', '3', 'zh_CN', '任务操作模版', '${operatorName} ${operatorPhone} 已完成该单，稍后我们将进行回访。', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ('183', 'pmtask.notification', '4', 'zh_CN', '任务操作模版', '您的任务已被 ${operatorName} ${operatorPhone} 关闭', '0');

INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
VALUES ('6', '0', '0', '任务', '任务', '0', '2', '2015-09-28 06:09:03', NULL, NULL, NULL, '999992');


INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
  VALUES (250, 'pmtask', '10001', 'zh_CN', '任务分类已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
  VALUES (251, 'pmtask', '10002', 'zh_CN', '服务类型不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
  VALUES (252, 'pmtask', '10003', 'zh_CN', '目标用户不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
  VALUES (253, 'pmtask', '10004', 'zh_CN', '内容不能为空');

-- 物业保修2.0的菜单权限 by sunwen 20160830
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
  VALUES (24000,'任务列表',20000,NULL,'task_management_list',0,2,'/20000/24000','park',456);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
  VALUES (25000,'服务类型设置',20000,NULL,'service_type_setting',0,2,'/20000/25000','park',457);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
  VALUES (26000,'分类设置',20000,NULL,'classify_setting',0,2,'/20000/26000','park',458);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
  VALUES (27000,'统计',20000,NULL,'task_statistics',0,2,'/20000/27000','park',459);

  
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
  VALUES (904,0,'任务管理 任务列表','任务管理 任务列表 全部权限',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
  VALUES (905,0,'任务管理 服务类型设置','任务管理 服务类型设置 全部权限',NULL);
  
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ('331', '0', '分派任务', '分派任务', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ('332', '0', '完成任务', '完成任务', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ('333', '0', '关闭任务', '关闭任务', NULL);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
  VALUES (906,0,'任务管理 分类设置','任务管理 分类设置 全部权限',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
  VALUES (907,0,'任务管理 统计','任务管理 统计 全部权限',NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),904,24000,'任务列表',1,1,'任务列表 全部权限',603);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),331,24000,'分派任务',0,1,'分派任务 全部权限',604);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),332,24000,'完成任务',0,1,'完成任务 全部权限',605);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),333,24000,'关闭任务',0,1,'关闭任务 全部权限',606);
  
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),905,25000,'服务类型设置',1,1,'服务类型设置 全部权限',604);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),906,26000,'分类设置',1,1,'分类设置 全部权限',605);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),907,27000,'统计',1,1,'统计 全部权限',606);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);

INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1001,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%20000/%');
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1002,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%20000/%');



-- merge from videoconf3.0-delta-data-release.sql 20160829
update `eh_locale_strings` set `text` = "抱歉您当前不可更换此账号（最短更换频率为7天）" where `scope` = "videoConf" and `code` = "10005";

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('videoConf', '10010', 'zh_CN', '公司不存在或已删除');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('videoConf', '10011', 'zh_CN', '未过期状态不可删除');

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('organization', '500001', 'zh_CN', '该域下该公司已存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('organization', '500002', 'zh_CN', '公司类型错误，只能为普通公司或物业公司');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('organization', '500003', 'zh_CN', '手机号只能为11位的数字');


-- 储能交流大厅改官方活动
update eh_launch_pad_items set item_name = '园区活动' where id in(10617, 10635);
update eh_launch_pad_items set item_label = '园区活动' where id in(10617, 10635);
update eh_launch_pad_items set action_type = '50' where id in(10617, 10635);
update eh_launch_pad_items set action_data = '' where id in(10617, 10635);
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvMk9ETTNZMk14TVRFeVl6bGlZek5pTVRnNFlUQXpOV0ZrWWpabE4yVXpOUQ' where id in(10617, 10635);
-- delete from eh_launch_pad_items where id in(10613, 10631, 10614, 10632);

-- 海岸会议室预约和广告租赁

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) 
	VALUES(10,'会议室预约','0',NULL,'0', 999993);
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) 
	VALUES(11,'广告租赁','0',NULL,'0', 999993);
    
update eh_launch_pad_items set action_type = 49 where id in(1768, 1769, 1773, 1774);
update eh_launch_pad_items set action_type = '{"resourceTypeId":10,"pageType":0}' where id in(1768, 1769);
update eh_launch_pad_items set action_type = '{"resourceTypeId":11,"pageType":0}' where id in(1773, 1774);

-- 生成设备巡检任务时间配置
INSERT INTO `eh_configurations`(`namespace_id`, `name`, `value`, `description`) VALUES (0, 'equipment.task.schedule.time','0 0 0 * * ? ','schedule equipment task time');

-- 更新系统小助手、电商小助手默认头像（图片已经上传到alpha/beta/release） by lqs 20160831
UPDATE `eh_users` SET `avatar`='cs://1/image/aW1hZ2UvTVRwbE1UY3lOVFk0TVRZNU5HTXlPR014TVRSbU1UTTJNems1TmpVNE5UZzNZZw' WHERE `id`=2;
UPDATE `eh_users` SET `avatar`='cs://1/image/aW1hZ2UvTVRvNE0yWXdOVE15TlRJeE5UZzVPVFl3TjJFek5EZGpZemN4TURJMllUa3lZZw' WHERE `id`=3;


-- 更新讯美服务市场 by lqs 20160831
-- 创客空间layout
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (141, 999999, 'MakerLayout', '{"versionCode":"2016083101","versionName":"3.9.0","layoutName":"MakerLayout","displayName":"创客空间","groups":[{"groupName":"创客banner","widget":"Navigator","instanceConfig":{"itemGroup":"MakerBanners"},"style":"Gallery","defaultOrder":5,"separatorFlag":1,"separatorHeight":21,"columnCount":1},{"groupName":"创客空间","widget":"Navigator","instanceConfig":{"itemGroup":"MakerSpaces"},"style":"Gallery","defaultOrder":5,"separatorFlag":0,"separatorHeight":0,"columnCount":3}]}', '2016083101', '0', '2', '2016-08-31 13:40:30', 'pm_admin');
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES (142, 999999, 'MakerLayout', '{"versionCode":"2016083101","versionName":"3.9.0","layoutName":"MakerLayout","displayName":"创客空间","groups":[{"groupName":"创客banner","widget":"Navigator","instanceConfig":{"itemGroup":"MakerBanners"},"style":"Gallery","defaultOrder":5,"separatorFlag":1,"separatorHeight":21,"columnCount":1},{"groupName":"创客空间","widget":"Navigator","instanceConfig":{"itemGroup":"MakerSpaces"},"style":"Gallery","defaultOrder":5,"separatorFlag":0,"separatorHeight":0,"columnCount":3}]}', '2016083101', '0', '2', '2016-08-31 13:40:30', 'park_tourist');

-- 删除掉Bizs中的免费wifi/
DELETE FROM `eh_launch_pad_items` WHERE `id` IN (904, 911) AND `namespace_id`=999999;
-- 视频会议移到更多里面
UPDATE `eh_launch_pad_items` SET `display_flag`=0 WHERE `id` IN (902, 909);
	
-- 把便捷生活里面的两个店铺复制一份到广场上（齐彩网->办公采购、楼下Leisure ade->饮品）
INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (16001, 999999, '0', '0', '0', '/home', 'Bizs', 'RoutineBus', '通勤巴士', 'cs://1/image/aW1hZ2UvTVRvd056azNPVFkxWkRJMFpUZzFNR1JtWVRSak9EVTVaRFU0WkdSak1XSXdNQQ', '1', '1', '14', '{\"url\":\"http://wx.dudubashi.com\"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items`(`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
	VALUES (16002, 999999, '0', '0', '0', '/home', 'Bizs', 'RoutineBus', '通勤巴士', 'cs://1/image/aW1hZ2UvTVRvd056azNPVFkxWkRJMFpUZzFNR1JtWVRSak9EVTVaRFU0WkdSak1XSXdNQQ', '1', '1', '14', '{\"url\":\"http://wx.dudubashi.com\"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag,target_type, target_id, scene_type) 
	VALUES (16003, 999999, 0, '0', 0, '/home', 'Bizs', 'OfficePurchase', '办公采购', 'shop/3010/1471502227046-2040368847.jpg', 1, 1, 14, '', 1, 0, 1, 1, '', 0, NULL, 'biz', 105, 'park_tourist');
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag,target_type, target_id, scene_type) 
	VALUES (16004, 999999, 0, '0', 0, '/home', 'Bizs', 'OfficePurchase', '办公采购', 'shop/3010/1471502227046-2040368847.jpg', 1, 1, 14, '', 1, 0, 1, 1, '', 0, NULL, 'biz', 105, 'pm_admin');
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag,target_type, target_id, scene_type) 
	VALUES (16005, 999999, 0, '0', 0, '/home', 'Bizs', 'BizDrink', '饮品', 'shop/3007/14715010230651035403229.jpg', 1, 1, 14, '', 1, 0, 1, 1, '', 0, NULL, 'biz', 106, 'park_tourist');
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag,target_type, target_id, scene_type) 
	VALUES (16006, 999999, 0, '0', 0, '/home', 'Bizs', 'BizDrink', '饮品', 'shop/3007/14715010230651035403229.jpg', 1, 1, 14, '', 1, 0, 1, 1, '', 0, NULL, 'biz', 106, 'pm_admin');	
	
-- 更新讯美的创客空间为有layout形式的：一个大banner图和三个item
UPDATE `eh_launch_pad_items` SET `action_type`=2, `action_data`='{"itemLocation":"/home/Makers","layoutName":"MakerLayout"}' WHERE `id` in (10088, 10327) AND `namespace_id`=999999;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
	VALUES (16021, 999999, '0', '0', '0', '/home/Makers', 'MakerBanners', 'MakerBanner', '', 'cs://1/image/aW1hZ2UvTVRvell6RXlNVEE0TjJNelpEVTFPREZsWTJKaVptVXdNRFZtWm1FNVlUWTRZZw', '1', '1', '0', NULL, '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
	VALUES (16022, 999999, '0', '0', '0', '/home/Makers', 'MakerBanners', 'MakerBanner', '', 'cs://1/image/aW1hZ2UvTVRvell6RXlNVEE0TjJNelpEVTFPREZsWTJKaVptVXdNRFZtWm1FNVlUWTRZZw', '1', '1', '0', NULL, '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
	VALUES (16023, 999999, '0', '0', '0', '/home/Makers', 'MakerSpaces', '工位预订', '工位预订', 'cs://1/image/aW1hZ2UvTVRvNU1ESXdPR1ZrTkdZNFltRm1Oams0TWpBM1pHUTRZMkZsWW1JeFptTmpOZw', '1', '1', '14', '{"url":"http://beta.zuolin.com/station-booking/index.html?hideNavigationBar=1#/station_booking#sign_suffix"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
	VALUES (16024, 999999, '0', '0', '0', '/home/Makers', 'MakerSpaces', '工位预订', '工位预订', 'cs://1/image/aW1hZ2UvTVRvNU1ESXdPR1ZrTkdZNFltRm1Oams0TWpBM1pHUTRZMkZsWW1JeFptTmpOZw', '1', '1', '14', '{"url":"http://beta.zuolin.com/station-booking/index.html?hideNavigationBar=1#/station_booking#sign_suffix"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
	VALUES (16025, 999999, '0', '0', '0', '/home/Makers', 'MakerSpaces', 'DoorManagement', '门禁', 'cs://1/image/aW1hZ2UvTVRvelpUZG1ObUUyTVRneFltRmpaVGM1TnpVMU1USTFObVZoTm1Zd1pUTmpNUQ', '1', '1', '40', '{\"isSupportQR\":1,\"isSupportSmart\":0}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
	VALUES (16026, 999999, '0', '0', '0', '/home/Makers', 'MakerSpaces', 'DoorManagement', '门禁', 'cs://1/image/aW1hZ2UvTVRvelpUZG1ObUUyTVRneFltRmpaVGM1TnpVMU1USTFObVZoTm1Zd1pUTmpNUQ', '1', '1', '40', '{\"isSupportQR\":1,\"isSupportSmart\":0}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
	VALUES (16027, 999999, '0', '0', '0', '/home/Makers', 'MakerSpaces', 'MEETINGROOM', '会议室预订', 'cs://1/image/aW1hZ2UvTVRwaU5HSTJPRFEyTWpZMVlUSmtaVEF5T0dRd05qVmpaREZsWkRObU1URmtPUQ', '1', '1', '49', '{\"resourceTypeId\":4,\"pageType\":0}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
	VALUES (16028, 999999, '0', '0', '0', '/home/Makers', 'MakerSpaces', 'MEETINGROOM', '会议室预订', 'cs://1/image/aW1hZ2UvTVRwaU5HSTJPRFEyTWpZMVlUSmtaVEF5T0dRd05qVmpaREZsWkRObU1URmtPUQ', '1', '1', '49', '{\"resourceTypeId\":4,\"pageType\":0}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0');

-- 产业服务体系使用服务联盟V2.0（上面已进行数据迁移）
-- 政府资源使用服务联盟V2.0
UPDATE `eh_launch_pad_items` SET `action_data` = '{"type":4,"parentId":100001}', action_type = 33 WHERE ID IN (10086, 10325);
