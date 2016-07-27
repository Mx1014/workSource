delete from eh_launch_pad_items where target_type='biz' and `target_id`>0 and scope_code = 0;

INSERT INTO `ehcore`.`eh_configurations` (`name`, `value`, `description`) VALUES ('business.realm', 'biz', 'business realm');
INSERT INTO `ehcore`.`eh_configurations` (`name`, `value`, `description`) VALUES ('business.url', 'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14477417463124576784%3F_k%3Dzlbiz#sign_suffix', 'business url');

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('group', '10030', 'zh_CN', '您不是群主，无权操作');

-- 电商离线包配置
INSERT INTO `eh_version_realm` VALUES ('49', 'biz', null, UTC_TIMESTAMP(), '0');
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES(52,49,'-0.1','2100224','0','2.3.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES ('24', '49', '2.3.0', 'http://biz.zuolin.com/nar/biz/web/app/dist/biz-2-3-0-tag.zip', 'http://biz.zuolin.com/nar/biz/web/app/dist/biz-2-3-0-tag.zip', '0');

-- 发帖要收到消息的配置  20160725 by sfyan
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES(1,'EhCommunities',240111044331052505,'EhUsers',222568,'REPAIRS','push');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES(2,'EhCommunities',240111044331052505,'EhUsers',222569,'REPAIRS','push');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES(3,'EhCommunities',240111044331052506,'EhUsers',222568,'REPAIRS','push');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES(4,'EhCommunities',240111044331052506,'EhUsers',222569,'REPAIRS','push');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES(5,'EhCommunities',240111044331052507,'EhUsers',222568,'REPAIRS','push');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES(6,'EhCommunities',240111044331052507,'EhUsers',222569,'REPAIRS','push');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES(7,'EhCommunities',240111044331052508,'EhUsers',222568,'REPAIRS','push');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES(8,'EhCommunities',240111044331052508,'EhUsers',222569,'REPAIRS','push');

-- 补充菜单屏蔽的完整数据  20160725 by sfyan
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',1000000,0  FROM `eh_web_menus` WHERE `path` LIKE '%48000/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',1000000,0  FROM `eh_web_menus` WHERE `path` LIKE '%43500/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',1000000,0  FROM `eh_web_menus` WHERE `path` LIKE '%47000/%';

INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999992,0  FROM `eh_web_menus` WHERE `path` LIKE '%41000/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999992,0  FROM `eh_web_menus` WHERE `path` LIKE '%43000/%';

INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999999,0  FROM `eh_web_menus` WHERE `path` LIKE '%53000/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999999,0  FROM `eh_web_menus` WHERE `path` LIKE '%56100/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999999,0  FROM `eh_web_menus` WHERE `path` LIKE '%58000/%';

INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999993,0  FROM `eh_web_menus` WHERE `path` LIKE '%41000/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999993,0  FROM `eh_web_menus` WHERE `path` LIKE '%47000/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999993,0  FROM `eh_web_menus` WHERE `path` LIKE '%48000/%';

INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999990,0  FROM `eh_web_menus` WHERE `path` LIKE '%42000/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999990,0  FROM `eh_web_menus` WHERE `path` LIKE '%41000/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999990,0  FROM `eh_web_menus` WHERE `path` LIKE '%56100/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999990,0  FROM `eh_web_menus` WHERE `path` LIKE '%58100/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999990,0  FROM `eh_web_menus` WHERE `path` LIKE '%43000/%';

INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999989,0  FROM `eh_web_menus` WHERE `path` LIKE '%41000/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999989,0  FROM `eh_web_menus` WHERE `path` LIKE '%43500/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999989,0  FROM `eh_web_menus` WHERE `path` LIKE '%48000/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999989,0  FROM `eh_web_menus` WHERE `path` LIKE '%51000/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999989,0  FROM `eh_web_menus` WHERE `path` LIKE '%53000/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999989,0  FROM `eh_web_menus` WHERE `path` LIKE '%56000/%';
INSERT INTO `eh_web_menu_scopes` (`id`,`menu_id`,`owner_type`,`owner_id`,`apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),id,'EhNamespaces',999989,0  FROM `eh_web_menus` WHERE `path` LIKE '%58000/%';

-- 添加官方活动权限
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (310,0,'官方活动','官方活动',null);

set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),310,11100,'官方活动',0,1,'官方活动',16);


-- add service market items for techpark by lqs 20160725
UPDATE `eh_launch_pad_layouts` SET `layout_json`='{"versionCode":"2015111401","versionName":"3.0.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0}]}' WHERE `id`=11 AND `namespace_id`=1000000;
UPDATE `eh_launch_pad_layouts` SET `layout_json`='{"versionCode":"2015111401","versionName":"3.0.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0}]}' WHERE `id`=111 AND `namespace_id`=1000000;
UPDATE `eh_launch_pad_items` SET `item_name`='EXCHANGE_HALL', `item_label`='交流大厅', `action_type`=50, `icon_uri`='cs://1/image/aW1hZ2UvTVRwbE5UTTFNRE0wWXpBd1lqVTNaVFF4WWpCaVpEa3dZVGhrTlRGbVl6UTJNUQ' WHERE `id`=802 AND `namespace_id`=1000000;
UPDATE `eh_launch_pad_items` SET `item_name`='EXCHANGE_HALL', `item_label`='交流大厅', `action_type`=50, `icon_uri`='cs://1/image/aW1hZ2UvTVRwbE5UTTFNRE0wWXpBd1lqVTNaVFF4WWpCaVpEa3dZVGhrTlRGbVl6UTJNUQ' WHERE `id`=10303 AND `namespace_id`=1000000;
DELETE FROM `eh_launch_pad_items` WHERE `id` IN (809, 810, 10306, 10307);

-- 左邻通用版：删除优惠券和左邻小店，换为服务联盟和园区活动、删除通讯录  by lqs 20160725
UPDATE `eh_launch_pad_layouts` SET `layout_json`='{"versionCode":"2016031201","versionName":"3.3.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"园区服务","widget":"Navigator","instanceConfig":{"itemGroup":"CmntyServices"},"style":"Gallery","defaultOrder":3,"separatorFlag":1,"separatorHeight":21,"columnCount":2},{"groupName":"滚动广告","widget":"Bulletins","instanceConfig":{"itemGroup":""},"style":"Default","defaultOrder":4,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0,"editFlag":1}]}' WHERE `id`=1 AND `namespace_id`=0;
UPDATE `eh_launch_pad_layouts` SET `layout_json`='{"versionCode":"2016031201","versionName":"3.3.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"园区服务","widget":"Navigator","instanceConfig":{"itemGroup":"CmntyServices"},"style":"Gallery","defaultOrder":3,"separatorFlag":1,"separatorHeight":21,"columnCount":2},{"groupName":"滚动广告","widget":"Bulletins","instanceConfig":{"itemGroup":""},"style":"Default","defaultOrder":4,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0,"editFlag":1}]}' WHERE `id`=2 AND `namespace_id`=0;
UPDATE `eh_launch_pad_layouts` SET `layout_json`='{"versionCode":"2016031201","versionName":"3.3.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"园区服务","widget":"Navigator","instanceConfig":{"itemGroup":"CmntyServices"},"style":"Gallery","defaultOrder":3,"separatorFlag":1,"separatorHeight":21,"columnCount":2},{"groupName":"滚动广告","widget":"Bulletins","instanceConfig":{"itemGroup":""},"style":"Default","defaultOrder":4,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0,"editFlag":1}]}' WHERE `id`=3 AND `namespace_id`=0;

DELETE FROM `eh_launch_pad_items` WHERE `id` IN (1, 2, 3, 4, 6);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (55, 0, '0', '0', '0', '/home', 'CmntyServices', 'SERVICEALLIANCE', '服务联盟', 'cs://1/image/aW1hZ2UvTVRveE9HRTBObVUzTkRZMk16UTJNVEUxTWpnNU0yTTRZemt6TnpObU5XRm1NQQ', '1', '1', 33,'{"type":2,"parentId":100001}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');        
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (56, 0, '0', '0', '0', '/home', 'CmntyServices', 'CmntyActivities', '园区活动', 'cs://1/image/aW1hZ2UvTVRvMlpXSm1ZVFZpTURkbFpqRmpOamxsTURCa01UY3hNRFUzTURWbFlqVTVNQQ', '1', '1', 50,'', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist');        
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (57, 0, '0', '0', '0', '/home', 'CmntyServices', 'CmntyActivities', '园区活动', 'cs://1/image/aW1hZ2UvTVRvM09UazJOVGd4TVRFek5qaGxNMkl5TlRneU1tTmlZVFEwTURJNVpUWTJNZw', '2', '1', 50,'', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'default');        
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (58, 0, '0', '0', '0', '/home', 'CmntyServices', 'CmntyActivities', '园区活动', 'cs://1/image/aW1hZ2UvTVRvM09UazJOVGd4TVRFek5qaGxNMkl5TlRneU1tTmlZVFEwTURJNVpUWTJNZw', '2', '1', 50,'', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin');        
-- 删除通讯录
DELETE FROM `eh_launch_pad_items` WHERE `id` IN (10428, 10435) AND `item_label` LIKE '%通讯录%';

-- 门禁配置
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'aclink.qr_driver_type', 'zuolin', 'the driver type of this namespace.(zuolin/lingling)');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (1000000, 'aclink.qr_driver_type', 'zuolin', 'zuolin for techpark');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (999990, 'aclink.qr_driver_type', 'lingling', 'lingling for chuneng');
