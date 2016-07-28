-- 删除电商相关图标
delete from eh_launch_pad_items where target_type='biz' and scope_code = 0;

-- 配置电商链接
INSERT INTO `ehcore`.`eh_configurations` (`name`, `value`, `description`) VALUES ('business.realm', 'biz', 'business realm');
INSERT INTO `ehcore`.`eh_configurations` (`name`, `value`, `description`) VALUES ('business.url', 'https://biz-beta.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz-beta.zuolin.com%2Fnar%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14477417463124576784%3F_k%3Dzlbiz#sign_suffix', 'business url');

-- group下增加错误提示
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('group', '10030', 'zh_CN', '您不是群主，无权操作');

-- 删除左邻小站和优惠券的入口
delete from `eh_launch_pad_items` where id in (809, 10306, 810, 10307);

-- 添加社区活动的入口，替代原左邻小站和优惠券的位置
--INSERT INTO `eh_launch_pad_items` ( `id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
--VALUES ( 809, '1000000', '0', '0', '0', '/home', 'GovAgencies', '社区活动', '社区活动', 'cs://1/image/aW1hZ2UvTVRwalptSm1PRFE1T1dObVpqTXdZelZqWVRNME56azBNelk1TkdNNU1UUm1Zdw', '4', '1', '50', '', '1', '0', '1', '1', '', '1', NULL, NULL, NULL, '1', 'default');
		
--INSERT INTO `eh_launch_pad_items` ( `id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
--VALUES ( 810, '1000000', '0', '0', '0', '/home', 'GovAgencies', '社区活动', '社区活动', 'cs://1/image/aW1hZ2UvTVRwalptSm1PRFE1T1dObVpqTXdZelZqWVRNME56azBNelk1TkdNNU1UUm1Zdw', '4', '1', '50', '', '1', '0', '1', '1', '', '1', NULL, NULL, NULL, '1', 'pm_admin');

-- 电商离线包配置
INSERT INTO `eh_version_realm` VALUES ('49', 'biz', null, UTC_TIMESTAMP(), '0');
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES(52,49,'-0.1','2099200','0','2.2.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES (53,49,'2099199.9','2100224','0','2.3.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES ('24', '49', '2.2.0', 'http://biz-beta.zuolin.com/nar/biz/web/app/dist/biz-2-2-0-tag.zip', 'http://biz-beta.zuolin.com/nar/biz/web/app/dist/biz-2-2-0-tag.zip', '0');
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES ('25', '49', '2.3.0', 'http://biz-beta.zuolin.com/nar/biz/web/app/dist/biz-2-3-0-tag.zip', 'http://biz-beta.zuolin.com/nar/biz/web/app/dist/biz-2-3-0-tag.zip', '0');

-- 添加官方活动权限
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (310,0,'官方活动','官方活动',null);

set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),310,11100,'官方活动',0,1,'官方活动',16);

-- 门禁配置
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'aclink.qr_driver_type', 'zuolin', 'the driver type of this namespace.(zuolin/lingling)');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (1000000, 'aclink.qr_driver_type', 'zuolin', 'zuolin for techpark');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (999990, 'aclink.qr_driver_type', 'lingling', 'lingling for chuneng');

INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'aclink.qr_timeout', '6000', 'timeout in second for qr');
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'aclink.visitor_shorts', '临时来访|入职|面试|送货|出差|开会|施工|其它原因', 'shorts for visitors');
