-- 更新海岸东座论坛id
update eh_communities set default_forum_id = 179511, feedback_forum_id = 179512 where namespace_id = 999993 and id = 240111044331054835;
update eh_forum_posts set forum_id = 179511 where forum_id = 183102;

-- 更新海岸东座管理处名字
update eh_organizations set name = '海岸大厦东座物业服务中心' where id = 1004937 and namespace_id = 999993;

-- 更新停车充值订单为月卡充值订单 
update eh_parking_recharge_orders set recharge_type = 1;

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('351', 'parking.chuneng.url', 'http://113.108.41.29:8099', '储能停车充值key', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('352', 'parking.chuneng.key', 'F7A0B971B199FD2A52468575', '储能停车充值key', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('353', 'parking.chuneng.user', 'ktapi', '储能停车充值用户名', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('354', 'parking.chuneng.pwd', '0306C3', '储能停车充值密码', '0', NULL);
	
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `card_reserve_days`, `status`, `creator_uid`, `create_time`, `max_request_num`, `tempfee_flag`) 
	VALUES ('10004', 'community', '240111044331051500', '中国储能大厦停车场', 'KETUO', NULL, '1', '2', '1025', '2016-08-29 17:28:10', '1', '1');

	
-- 储能停车充值广场
delete from eh_launch_pad_items where id in (10604, 10622);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
	VALUES ('10604', '999990', '0', '0', '0', '/home', 'Bizs', 'PARKING_RECHARGE', '停车', 'cs://1/image/aW1hZ2UvTVRwaVpUWTVNemc0Wm1Fd01qSTRNVEpqTmpsaU1tWTROVFExTW1FMU1qZ3paZw', '1', '1', '30', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin', '1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
	VALUES ('10622', '999990', '0', '0', '0', '/home', 'Bizs', 'PARKING_RECHARGE', '停车', 'cs://1/image/aW1hZ2UvTVRwaVpUWTVNemc0Wm1Fd01qSTRNVEpqTmpsaU1tWTROVFExTW1FMU1qZ3paZw', '1', '1', '30', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist', '1');

-- 停车充值菜单

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),41000,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),41100,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),41300,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),41400,'', 'EhNamespaces', 999990,2);

UPDATE `eh_web_menu_privileges` SET `privilege_id`='556', `menu_id`='41100', `name`='充值项管理列表',  `discription`='查询充值项管理列表' WHERE (`id`='17');
UPDATE `eh_web_menu_privileges` SET `privilege_id`='512', `menu_id`='41100', `name`='增加停车充值项',  `discription`='增加停车充值项' WHERE (`id`='18');
UPDATE `eh_web_menu_privileges` SET `privilege_id`='513', `menu_id`='41100', `name`='删除停车充值项',  `discription`='删除停车充值项' WHERE (`id`='19');
UPDATE `eh_web_menu_privileges` SET `privilege_id`='557', `menu_id`='41200', `name`='设置活动规则',  `discription`='设置活动规则' WHERE (`id`='20');
UPDATE `eh_web_menu_privileges` SET `privilege_id`='515', `menu_id`='41400', `name`='缴费记录',  `discription`='查询缴费记录' WHERE (`id`='21');
UPDATE `eh_web_menu_privileges` SET `privilege_id`='516', `menu_id`='41300', `name`='月卡申请记录',  `discription`='查询月卡申请记录' WHERE (`id`='22');
UPDATE `eh_web_menu_privileges` SET `privilege_id`='514', `menu_id`='41300', `name`='设置月卡申请参数',  `discription`='设置月卡申请参数' WHERE (`id`='23');
UPDATE `eh_web_menu_privileges` SET `privilege_id`='517', `menu_id`='41300', `name`='发放月卡',  `discription`='发放月卡' WHERE (`id`='24');
UPDATE `eh_web_menu_privileges` SET `privilege_id`='518', `menu_id`='41300', `name`='领取月卡',  `discription`='领取月卡' WHERE (`id`='25');

UPDATE `eh_web_menus` SET `name`='停车缴费' WHERE (`id`='41000');

UPDATE `eh_web_menus` SET `name`='充值项管理' WHERE (`id`='41100');
UPDATE `eh_web_menus` SET `name`='活动规则' WHERE (`id`='41200');
UPDATE `eh_web_menus` SET `name`='月卡申请' WHERE (`id`='41300');
UPDATE `eh_web_menus` SET `name`='缴费记录' WHERE (`id`='41400');

DELETE from eh_web_menu_scopes where menu_id = 41200;

UPDATE `eh_acl_privileges` SET `name`='增加停车充值项', `description`='增加停车充值项' WHERE (`id`='512');
UPDATE `eh_acl_privileges` SET `name`='删除停车充值项', `description`='删除停车充值项' WHERE (`id`='513');
UPDATE `eh_acl_privileges` SET `name`='设置月卡申请参数', `description`='设置月卡申请参数' WHERE (`id`='514');
UPDATE `eh_acl_privileges` SET `name`='查询缴费记录', `description`='查询缴费记录' WHERE (`id`='515');
UPDATE `eh_acl_privileges` SET `name`='查询月卡申请记录', `description`='查询月卡申请记录' WHERE (`id`='516');
UPDATE `eh_acl_privileges` SET `name`='发放月卡', `description`='发放月卡' WHERE (`id`='517');
UPDATE `eh_acl_privileges` SET `name`='领取月卡', `description`='领取月卡' WHERE (`id`='518');
UPDATE `eh_acl_privileges` SET `name`='查询充值项管理列表', `description`='查询充值项管理列表' WHERE (`id`='556');
UPDATE `eh_acl_privileges` SET `name`='设置活动规则', `description`='设置活动规则' WHERE (`id`='557');

-- 搜索类型配置 by xiongying20161017

INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES (1, '0', '', '0', '投票', 'poll', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES (2, '0', '', '0', '活动', 'activity', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES (3, '0', '', '0', '话题', 'topic', '1', NULL, NULL);

SET @search_type_id = (SELECT MAX(id) FROM `eh_search_types`);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '1000000', '', '0', '投票', 'poll', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '1000000', '', '0', '活动', 'activity', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '1000000', '', '0', '话题', 'topic', '1', NULL, NULL);

INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999992', '', '0', '投票', 'poll', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999992', '', '0', '活动', 'activity', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999992', '', '0', '话题', 'topic', '1', NULL, NULL);

INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999991', '', '0', '投票', 'poll', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999991', '', '0', '活动', 'activity', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999991', '', '0', '话题', 'topic', '1', NULL, NULL);

INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999989', '', '0', '投票', 'poll', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999989', '', '0', '活动', 'activity', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999989', '', '0', '话题', 'topic', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999989', '', '0', '快讯', 'news', '1', NULL, NULL);

INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999990', '', '0', '投票', 'poll', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999990', '', '0', '活动', 'activity', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999990', '', '0', '话题', 'topic', '1', NULL, NULL);

INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999993', '', '0', '投票', 'poll', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999993', '', '0', '活动', 'activity', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999993', '', '0', '话题', 'topic', '1', NULL, NULL);

INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999999', '', '0', '投票', 'poll', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999999', '', '0', '活动', 'activity', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999999', '', '0', '话题', 'topic', '1', NULL, NULL);

INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999988', '', '0', '投票', 'poll', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999988', '', '0', '活动', 'activity', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999988', '', '0', '话题', 'topic', '1', NULL, NULL);

INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999987', '', '0', '投票', 'poll', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999987', '', '0', '活动', 'activity', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999987', '', '0', '话题', 'topic', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999987', '', '0', '快讯', 'news', '1', NULL, NULL);

INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999986', '', '0', '投票', 'poll', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999986', '', '0', '活动', 'activity', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999986', '', '0', '话题', 'topic', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999986', '', '0', '快讯', 'news', '1', NULL, NULL);

-- 科技园添加左邻会议室
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) 
	VALUES('50','左邻会议室','0',NULL,'0','1000000');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
	VALUES (112372, '1000000', '0', '4', '178945', '/home', 'Bizs', 'MEETINGROOM', '左邻会议室', 'cs://1/image/aW1hZ2UvTVRvME5HVTNZVEZsTXpNeU16VXhNbVF3Wm1GbU9UUTBPV0ZoTUdRNFpUSmpaQQ', '1', '1', '49', '{\"resourceTypeId\":50,\"pageType\":0}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
	VALUES (112373, '1000000', '0', '4', '178945', '/home', 'Bizs', 'MEETINGROOM', '左邻会议室', 'cs://1/image/aW1hZ2UvTVRvME5HVTNZVEZsTXpNeU16VXhNbVF3Wm1GbU9UUTBPV0ZoTUdRNFpUSmpaQQ', '1', '1', '49', '{\"resourceTypeId\":50,\"pageType\":0}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0');

-- 踢出设备提示 by janson 20161017
UPDATE `eh_locale_strings` SET `text`='你的账号已在另一台设备登录，你被迫下线，若非本人操作，请立即修改密码。' WHERE `scope`='user' AND `code`='100018' AND `locale`='zh_CN';

-- 爱特家迷你居 去掉新闻管理菜单
DELETE FROM `eh_web_menu_scopes` WHERE `menu_id` = 11200 AND `owner_type` = 'EhNamespaces' AND `owner_id` = 999988 ;

-- 海岸服务广场修改by xiongying20161017
delete from eh_launch_pad_items where id in(10394, 10395) and namespace_id = 999993;

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
    VALUES ('10394', '999993', '0', '0', '0', '/home', 'Bizs', '饮用水服务', '饮用水服务', 'cs://1/image/aW1hZ2UvTVRvd01qa3lOVGcxWWpSa01qUmhOekpoWm1Gak1XUTVZVFkzWkRNM00yUXhZUQ', '1', '1', '14', '{\"url\":\"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14661537517644218191%3F_k%3Dzlbiz#sign_suffix\"}', '5', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin', '1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
    VALUES ('10395', '999993', '0', '0', '0', '/home', 'Bizs', '饮用水服务', '饮用水服务', 'cs://1/image/aW1hZ2UvTVRvd01qa3lOVGcxWWpSa01qUmhOekpoWm1Gak1XUTVZVFkzWkRNM00yUXhZUQ', '1', '1', '14', '{\"url\":\"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14661537517644218191%3F_k%3Dzlbiz#sign_suffix\"}', '5', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist', '1');
update eh_launch_pad_items set default_order = 10 where id in(10370,10371,10372,10373,10374,10375,10376,10377) and namespace_id = 999993;

-- 更新储能物业报修到2.0
UPDATE `eh_launch_pad_items` SET `item_name`='PM_TASK', `item_label`='物业服务', `action_type`='51', `action_data`='' WHERE (`id`='10610');
UPDATE `eh_launch_pad_items` SET `item_name`='物业服务', `item_label`='物业服务', `action_type`='14', `action_data`='http://core.zuolin.com/property_service/index.html?hideNavigationBar=1#/my_service#sign_suffix' WHERE (`id`='10628');

DELETE FROM eh_web_menu_scopes WHERE owner_id = 999990 AND menu_id IN (21000, 22000, 23000);
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),24000,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),25000,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),26000,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),27000,'', 'EhNamespaces', 999990,2);
	
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
	VALUES ('31', '0', '0', '任务', '任务', '0', '2', '2015-09-28 06:09:03', NULL, NULL, NULL, '999990');

-- 储能安卓升级规则 by xiongying20161018
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`)
    VALUES(123,34,'-0.1','3155970','0','3.10.2','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (77, 34, '3.10.2', 'http://apk.zuolin.com/apk/UFinePark-3.10.2.2016101719-release.apk', '${homeurl}/web/download/apk/andriod-UFinePark-3-10-2.html', '0');

--
-- 更新场景显示名称 add by xq.tian  2016/10/19
--
UPDATE `eh_scene_types` SET `display_name`='普通用户场景' WHERE (`name`='park_tourist');