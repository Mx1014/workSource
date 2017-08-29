-- by dengs,2017.08.28 快递2.0
update eh_configurations SET `value`='http://pay-beta.zuolin.com/EDS_PAY/rest/pay_common/payInfo_record/save_payInfo_record' WHERE `name` = 'guomao.payserver.url' AND namespace_id = 999901;
set @eh_configurations_id = (select Max(id) from eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@eh_configurations_id:=@eh_configurations_id+1), 
'debug.flag', 'true', '国贸 debug flag', 0, NULL);
-- by dengs,2017.08.28 快递2.0 end

DELETE from eh_parking_lots where id = 10002;
UPDATE `eh_parking_lots` SET `config_json`='{\"tempfeeFlag\": 1, \"rateFlag\": 1, \"lockCarFlag\": 1, \"searchCarFlag\": 0, \"currentInfoType\": 1, \"contact\": \"13632650699\"}' WHERE `id`='10001';
UPDATE `eh_parking_lots` SET `config_json`='{\"tempfeeFlag\": 0, \"rateFlag\": 0, \"lockCarFlag\": 0, \"searchCarFlag\": 0, \"currentInfoType\": 0, \"contact\": \"13510551322\"}' WHERE `id`='10003';
UPDATE `eh_parking_lots` SET `config_json`='{\"tempfeeFlag\": 1, \"rateFlag\": 0, \"lockCarFlag\": 0, \"searchCarFlag\": 0, \"currentInfoType\": 0, \"contact\": \"18927485550\"}' WHERE `id`='10004';
UPDATE `eh_parking_lots` SET `config_json`='{\"tempfeeFlag\": 1, \"rateFlag\": 0, \"lockCarFlag\": 0, \"searchCarFlag\": 1, \"currentInfoType\": 2, \"contact\": \"18718523489\"}', expired_recharge_json='{"expiredRechargeFlag":1, "maxExpiredDay":365, "expiredRechargeMonthCount":1, "expiredRechargeType":1}' WHERE `id`='10006';
UPDATE `eh_parking_lots` SET `config_json`='{\"tempfeeFlag\": 1, \"rateFlag\": 1, \"lockCarFlag\": 0, \"searchCarFlag\": 0, \"currentInfoType\": 0, \"contact\": \"13918348877\"}' WHERE `id`='10021';
UPDATE `eh_parking_lots` SET `config_json`='{\"tempfeeFlag\": 1, \"rateFlag\": 0, \"lockCarFlag\": 0, \"searchCarFlag\": 0, \"currentInfoType\": 0, \"contact\": \"18051307125\"}' WHERE `id`='10023';
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `expired_recharge_json`, `config_json`)
  VALUES ('10011', 'community', '240111044331050370', '软件产业基地停车场', 'XIAOMAO', NULL, '2', '1025', '2016-12-16 17:07:20', '0', NULL, '{\"tempfeeFlag\": 1, \"rateFlag\": 1, \"lockCarFlag\": 1, \"searchCarFlag\": 0, \"currentInfoType\": 1, \"contact\": \"18665331243\"}');
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `expired_recharge_json`, `config_json`)
  VALUES ('10012', 'community', '240111044331050371', '创投大厦停车场', 'XIAOMAO', NULL, '2', '1025', '2016-12-16 17:07:20', '0', NULL, '{\"tempfeeFlag\": 1, \"rateFlag\": 1, \"lockCarFlag\": 1, \"searchCarFlag\": 0, \"currentInfoType\": 1, \"contact\": \"18665331243\"}');
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `expired_recharge_json`, `config_json`)
  VALUES ('10013', 'community', '240111044331050369', '生态园停车场', 'Mybay', NULL, '2', '1025', '2016-12-16 17:07:20', '0', NULL, '{\"tempfeeFlag\": 1, \"rateFlag\": 0, \"lockCarFlag\": 0, \"searchCarFlag\": 0, \"currentInfoType\": 2, \"contact\": \"18665331243\"}');


--
-- 用户行为统计菜单   add by xq.tian  2017/08/18
--
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
VALUES (41330, '用户行为统计', 41300, NULL, 'react:/statistics-management/user-behavior', 0, 2, '/40000/41300/41330', 'park', 500, 41300, 3, NULL, 'module');

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (20074, 0, '用户行为统计', '用户行为统计 全部权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 20074, 41330, '用户行为统计', 1, 1, '用户行为统计  全部权限', 202);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, 20074, 1001, 'EhAclRoles', 0, 1, NOW());

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 41330, '', 'EhNamespaces', 1000000, 2);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `operator_uid`, `creator_uid`)
VALUES (41330, '用户行为统计', '41300', '/40000/41300/41330', '0', '2', '2', '0', UTC_TIMESTAMP(), 0, 0);

SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), 41330, '1', '100135', NULL, '0', UTC_TIMESTAMP());

SET @eh_service_module_scopes_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 1000000, 41500, '用户行为统计', 'EhNamespaces', 1000000, NULL, 2);

--
-- 事件
--
INSERT INTO `eh_stat_events` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_name`, `event_version`, `event_display_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (1, 0, 1, 1, 'portal_on_bottom_navigation_click', '1.0', '底部导航栏点击事件', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_events` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_name`, `event_version`, `event_display_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (2, 0, 1, 1, 'portal_on_navigation_click', '1.0', '顶部导航栏（工具栏）点击事件', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_events` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_name`, `event_version`, `event_display_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (3, 0, 1, 1, 'launchpad_on_banner_click', '1.0', 'Banner点击事件', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_events` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_name`, `event_version`, `event_display_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (4, 0, 1, 1, 'launchpad_on_bulletin_click', '1.0', '公告栏点击事件', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_events` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_name`, `event_version`, `event_display_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (5, 0, 1, 1, 'launchpad_on_launch_pad_item_click', '1.0', 'item点击事件', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_events` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_name`, `event_version`, `event_display_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (6, 0, 1, 1, 'launchpad_on_oppush_activity_item_click', '1.0', '活动运营点击事件', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_events` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_name`, `event_version`, `event_display_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (7, 0, 1, 1, 'launchpad_on_news_item_click', '1.0', '新闻快讯News点击事件', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_events` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_name`, `event_version`, `event_display_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (8, 0, 1, 1, 'launchpad_on_news_flash_item_click', '1.0', '新闻快讯NewsFlash点击事件', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_events` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_name`, `event_version`, `event_display_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (9, 0, 1, 1, 'launchpad_on_oppush_biz_item_click', '1.0', '电商运营点击事件', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_events` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_name`, `event_version`, `event_display_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (10, 0, 1, 1, 'launchpad_on_oppush_service_alliance_item_click', '1.0', '服务联盟运营点击事件', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_events` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_name`, `event_version`, `event_display_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (11, 0, 1, 1, 'system_on_app_startup', '1.0', 'App启动事件', 2, NULL, NULL, NULL, NULL);


--
-- 事件参数
--
INSERT INTO `eh_stat_event_params` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_version`, `multiple`, `event_name`, `param_type`, `param_key`, `param_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (1, 0, 1, 1, '1.0', 1, 'portal_on_bottom_navigation_click', 1, 'identifier', 'identifier', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_params` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_version`, `multiple`, `event_name`, `param_type`, `param_key`, `param_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (2, 0, 1, 1, '1.0', 1, 'portal_on_bottom_navigation_click', 1, 'position', 'position', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_params` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_version`, `multiple`, `event_name`, `param_type`, `param_key`, `param_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (3, 0, 1, 1, '1.0', 1, 'portal_on_navigation_click', 1, 'identifier', 'identifier', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_params` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_version`, `multiple`, `event_name`, `param_type`, `param_key`, `param_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (4, 0, 1, 1, '1.0', 1, 'portal_on_navigation_click', 1, 'position', 'position', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_params` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_version`, `multiple`, `event_name`, `param_type`, `param_key`, `param_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (5, 0, 1, 1, '1.0', 1, 'launchpad_on_banner_click', 1, 'id', 'id', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_params` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_version`, `multiple`, `event_name`, `param_type`, `param_key`, `param_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (6, 0, 1, 1, '1.0', 1, 'launchpad_on_banner_click', 1, 'layoutId', 'layoutId', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_params` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_version`, `multiple`, `event_name`, `param_type`, `param_key`, `param_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (7, 0, 1, 1, '1.0', 1, 'launchpad_on_banner_click', 1, 'location', 'location', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_params` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_version`, `multiple`, `event_name`, `param_type`, `param_key`, `param_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (8, 0, 1, 1, '1.0', 1, 'launchpad_on_bulletin_click', 1, 'layoutId', 'layoutId', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_params` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_version`, `multiple`, `event_name`, `param_type`, `param_key`, `param_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (9, 0, 1, 1, '1.0', 1, 'launchpad_on_bulletin_click', 1, 'location', 'location', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_params` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_version`, `multiple`, `event_name`, `param_type`, `param_key`, `param_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (10, 0, 1, 1, '1.0', 1, 'launchpad_on_launch_pad_item_click', 1, 'id', 'id', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_params` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_version`, `multiple`, `event_name`, `param_type`, `param_key`, `param_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (11, 0, 1, 1, '1.0', 1, 'launchpad_on_launch_pad_item_click', 1, 'layoutId', 'layoutId', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_params` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_version`, `multiple`, `event_name`, `param_type`, `param_key`, `param_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (12, 0, 1, 1, '1.0', 1, 'launchpad_on_launch_pad_item_click', 1, 'location', 'location', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_params` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_version`, `multiple`, `event_name`, `param_type`, `param_key`, `param_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (13, 0, 1, 1, '1.0', 1, 'launchpad_on_oppush_biz_item_click', 1, 'id', 'id', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_params` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_version`, `multiple`, `event_name`, `param_type`, `param_key`, `param_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (14, 0, 1, 1, '1.0', 1, 'launchpad_on_oppush_biz_item_click', 1, 'layoutId', 'layoutId', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_params` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_version`, `multiple`, `event_name`, `param_type`, `param_key`, `param_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (15, 0, 1, 1, '1.0', 1, 'launchpad_on_oppush_activity_item_click', 1, 'topicId', 'topicId', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_params` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_version`, `multiple`, `event_name`, `param_type`, `param_key`, `param_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (16, 0, 1, 1, '1.0', 1, 'launchpad_on_oppush_activity_item_click', 1, 'forumId', 'forumId', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_params` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_version`, `multiple`, `event_name`, `param_type`, `param_key`, `param_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (17, 0, 1, 1, '1.0', 1, 'launchpad_on_oppush_activity_item_click', 1, 'layoutId', 'layoutId', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_params` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_version`, `multiple`, `event_name`, `param_type`, `param_key`, `param_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (18, 0, 1, 1, '1.0', 1, 'launchpad_on_news_item_click', 1, 'newsToken', 'newsToken', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_params` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_version`, `multiple`, `event_name`, `param_type`, `param_key`, `param_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (19, 0, 1, 1, '1.0', 1, 'launchpad_on_news_item_click', 1, 'layoutId', 'layoutId', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_params` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_version`, `multiple`, `event_name`, `param_type`, `param_key`, `param_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (20, 0, 1, 1, '1.0', 1, 'launchpad_on_news_flash_item_click', 1, 'newsToken', 'newsToken', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_params` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_version`, `multiple`, `event_name`, `param_type`, `param_key`, `param_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (21, 0, 1, 1, '1.0', 1, 'launchpad_on_news_flash_item_click', 1, 'layoutId', 'layoutId', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_params` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_version`, `multiple`, `event_name`, `param_type`, `param_key`, `param_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (22, 0, 1, 1, '1.0', 1, 'launchpad_on_oppush_service_alliance_item_click', 1, 'id', 'id', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_params` (`id`, `namespace_id`, `event_scope`, `event_type`, `event_version`, `multiple`, `event_name`, `param_type`, `param_key`, `param_name`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (23, 0, 1, 1, '1.0', 1, 'launchpad_on_oppush_service_alliance_item_click', 1, 'layoutId', 'layoutId', 2, NULL, NULL, NULL, NULL);

--
-- 上传策略
--
INSERT INTO `eh_stat_event_upload_strategies` (`id`, `namespace_id`, `owner_type`, `owner_id`, `access`, `log_type`, `strategy`, `interval_seconds`, `times_per_day`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (1, 1000000, 'EhNamespaces', 1000000, 'WIFI', 1, 2, NULL, NULL, 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_upload_strategies` (`id`, `namespace_id`, `owner_type`, `owner_id`, `access`, `log_type`, `strategy`, `interval_seconds`, `times_per_day`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (2, 1000000, 'EhNamespaces', 1000000, 'WIFI', 2, 1, 60, NULL, 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_upload_strategies` (`id`, `namespace_id`, `owner_type`, `owner_id`, `access`, `log_type`, `strategy`, `interval_seconds`, `times_per_day`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (3, 1000000, 'EhNamespaces', 1000000, 'GSM', 1, 1, 50, NULL, 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_upload_strategies` (`id`, `namespace_id`, `owner_type`, `owner_id`, `access`, `log_type`, `strategy`, `interval_seconds`, `times_per_day`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (4, 1000000, 'EhNamespaces', 1000000, 'GSM', 2, 0, NULL, NULL, 2, NULL, NULL, NULL, NULL);

--
-- 门户配置
--
INSERT INTO `eh_stat_event_portal_configs` (`id`, `namespace_id`, `parent_id`, `config_type`, `config_name`, `identifier`, `display_name`, `description`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (1, 1000000, 0, 2, '科技园', '0', '科技园', '服务广场', 4, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_portal_configs` (`id`, `namespace_id`, `parent_id`, `config_type`, `config_name`, `identifier`, `display_name`, `description`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (2, 1000000, 0, 2, '小站', '1', '小站', '电商', 4, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_portal_configs` (`id`, `namespace_id`, `parent_id`, `config_type`, `config_name`, `identifier`, `display_name`, `description`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (3, 1000000, 0, 2, '论坛', '2', '论坛', '论坛', 4, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_portal_configs` (`id`, `namespace_id`, `parent_id`, `config_type`, `config_name`, `identifier`, `display_name`, `description`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (4, 1000000, 0, 2, '我', '3', '我', '个人中心', 4, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_portal_configs` (`id`, `namespace_id`, `parent_id`, `config_type`, `config_name`, `identifier`, `display_name`, `description`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (5, 1000000, 0, 1, 'Address', 'Address', '公司认证', '公司认证', 4, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_portal_configs` (`id`, `namespace_id`, `parent_id`, `config_type`, `config_name`, `identifier`, `display_name`, `description`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (6, 1000000, 0, 1, 'Search', 'Search', '搜索', '搜索', 4, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_portal_configs` (`id`, `namespace_id`, `parent_id`, `config_type`, `config_name`, `identifier`, `display_name`, `description`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (7, 1000000, 0, 1, 'Scan', 'Scan', '扫一扫', '扫一扫', 4, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_portal_configs` (`id`, `namespace_id`, `parent_id`, `config_type`, `config_name`, `identifier`, `display_name`, `description`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (8, 1000000, 0, 1, 'MessageBox', 'MessageBox', '消息', '消息', 4, NULL, NULL, NULL, NULL);
INSERT INTO `eh_stat_event_portal_configs` (`id`, `namespace_id`, `parent_id`, `config_type`, `config_name`, `identifier`, `display_name`, `description`, `status`, `creator_uid`, `update_uid`, `create_time`, `update_time`) VALUES (9, 1000000, 0, 1, 'Settings', 'Settings', '设置', '设置', 4, NULL, NULL, NULL, NULL);

--
-- 字符串模板
--
SELECT MAX(id) FROM `eh_locale_strings` INTO @locale_strings_id;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'stat.event', '1', 'zh_CN', 'Banner');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'stat.event', '2', 'zh_CN', '公告栏');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'stat.event', '3', 'zh_CN', '新闻快讯');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'stat.event', '4', 'zh_CN', '电商运营');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'stat.event', '5', 'zh_CN', '活动运营');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'stat.event', '6', 'zh_CN', '服务联盟运营');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'stat.event', '7', 'zh_CN', '业务应用组');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'stat.event', '8', 'zh_CN', '标签栏');

SELECT MAX(id) FROM `eh_configurations` INTO @configurations_id;
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ((@configurations_id := @configurations_id + 1), 'biz.queryCommodityDetail.api', 'zl-ec/rest/openapi/commodity/queryCommodityByCommoNos', 'biz commodity detail api', 0, '电商商品详情api');
