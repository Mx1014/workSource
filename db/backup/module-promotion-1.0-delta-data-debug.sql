--
-- 华润OE首页layout配置   add by xq.tian  2017/01/09
--
UPDATE `eh_launch_pad_layouts` SET `version_code`='2017011001', `layout_json`='{"versionCode": "2017011001","versionName": "3.12.4","layoutName": "ServiceMarketLayout","displayName": "服务市场","groups": [{"groupName": "","widget": "Banners","instanceConfig": {"itemGroup": "Default"},"style": "Default","defaultOrder": 1,"separatorFlag": 0,"separatorHeight": 0},{"groupName": "","widget": "Navigator","instanceConfig": {"itemGroup": "GovAgencies"},"style": "Default","defaultOrder": 2,"separatorFlag": 1,"separatorHeight": 21,"columnCount": 3},{"groupName": "","widget": "Bulletins","instanceConfig": {"itemGroup": "Default"},"style": "Default","defaultOrder": 3,"separatorFlag": 1,"separatorHeight": 21},{"groupName": "商家服务","widget": "Navigator","instanceConfig": {"itemGroup": "Bizs"},"style": "Default","defaultOrder": 5,"separatorFlag": 1,"separatorHeight": 21},{"groupName": "","widget": "OPPush","instanceConfig": {"itemGroup": "OPPushActivity","entityCount": 3,"subjectHeight": 1,"descriptionHeight": 0},"style": "ListView","defaultOrder": 6,"separatorFlag": 1,"separatorHeight": 21,"columnCount": 1},{"groupName": "","widget": "OPPush","instanceConfig": {"itemGroup": "OPPushBiz","entityCount": 6,"subjectHeight": 1,"descriptionHeight": 0},"style": "HorizontalScrollView","defaultOrder": 7,"separatorFlag": 1,"separatorHeight": 0,"columnCount": 0}]}'
WHERE `namespace_id`='999985' AND `name`='ServiceMarketLayout';

--
-- 华润OE运营launchPadItem配置   add by xq.tian  2017/01/09
--
SELECT MAX(id) FROM `eh_launch_pad_items` INTO @max_id;
INSERT INTO `eh_launch_pad_items` (`id`,`namespace_id`,`app_id`,`scope_code`,`scope_id`,`item_location`,`item_group`,`item_name`,`item_label`,`icon_uri`,`item_width`,`item_height`,`action_type`,`action_data`,`default_order`,`apply_policy`,`min_version`,`display_flag`,`display_layout`,`bgcolor`,`tag`,`target_type`,`target_id`,`delete_flag`,`scene_type`,`scale_type`,`service_categry_id`)
VALUES((@max_id := @max_id+1),'999985','0','0','0','/home','OPPushActivity','NEWEST_ACTIVITY','最新活动','cs://1/image/aW1hZ2UvTVRvME5tTXpZVEEwWlRKa1lqQXlaVFEwTmpkaU5XRTJORGN5WVdJM056QmpZUQ','1','1','50','{\"publishPrivilege\":0,\"livePrivilege\":1,\"categoryId\": 100001}','0','0','1','1','','0',NULL,NULL,NULL,'1','pm_admin','1',NULL);
INSERT INTO `eh_launch_pad_items` (`id`,`namespace_id`,`app_id`,`scope_code`,`scope_id`,`item_location`,`item_group`,`item_name`,`item_label`,`icon_uri`,`item_width`,`item_height`,`action_type`,`action_data`,`default_order`,`apply_policy`,`min_version`,`display_flag`,`display_layout`,`bgcolor`,`tag`,`target_type`,`target_id`,`delete_flag`,`scene_type`,`scale_type`,`service_categry_id`)
VALUES((@max_id := @max_id+1),'999985','0','0','0','/home','OPPushBiz','OE_SELECTION','OE精选','cs://1/image/aW1hZ2UvTVRvME5tTXpZVEEwWlRKa1lqQXlaVFEwTmpkaU5XRTJORGN5WVdJM056QmpZUQ','1','1','14','{\"url\":\"http://www.baidu.com\"}','0','0','1','1','','0',NULL,NULL,NULL,'1','pm_admin','1',NULL);
INSERT INTO `eh_launch_pad_items` (`id`,`namespace_id`,`app_id`,`scope_code`,`scope_id`,`item_location`,`item_group`,`item_name`,`item_label`,`icon_uri`,`item_width`,`item_height`,`action_type`,`action_data`,`default_order`,`apply_policy`,`min_version`,`display_flag`,`display_layout`,`bgcolor`,`tag`,`target_type`,`target_id`,`delete_flag`,`scene_type`,`scale_type`,`service_categry_id`)
VALUES((@max_id := @max_id+1),'999985','0','0','0','/home','OPPushActivity','NEWEST_ACTIVITY','最新活动','cs://1/image/aW1hZ2UvTVRvME5tTXpZVEEwWlRKa1lqQXlaVFEwTmpkaU5XRTJORGN5WVdJM056QmpZUQ','1','1','50','{\"publishPrivilege\":0,\"livePrivilege\":1,\"categoryId\": 100001}','0','0','1','1','','0',NULL,NULL,NULL,'1','park_tourist','1',NULL);
INSERT INTO `eh_launch_pad_items` (`id`,`namespace_id`,`app_id`,`scope_code`,`scope_id`,`item_location`,`item_group`,`item_name`,`item_label`,`icon_uri`,`item_width`,`item_height`,`action_type`,`action_data`,`default_order`,`apply_policy`,`min_version`,`display_flag`,`display_layout`,`bgcolor`,`tag`,`target_type`,`target_id`,`delete_flag`,`scene_type`,`scale_type`,`service_categry_id`)
VALUES((@max_id := @max_id+1),'999985','0','0','0','/home','OPPushBiz','OE_SELECTION','OE精选','cs://1/image/aW1hZ2UvTVRvME5tTXpZVEEwWlRKa1lqQXlaVFEwTmpkaU5XRTJORGN5WVdJM056QmpZUQ','1','1','14','{\"url\":\"http://www.baidu.com\"}','0','0','1','1','','0',NULL,NULL,NULL,'1','park_tourist','1',NULL);

--
-- 电商运营测试数据   add by xq.tian  2017/01/09
--
SELECT 1 INTO @max_id;
INSERT INTO `eh_business_promotions` (`id`, `namespace_id`, `subject`, `description`, `poster_uri`, `price`, `commodity_url`, `default_order`, `create_time`, `creator_uid`, `update_time`, `update_uid`)
VALUES ((@max_id := @max_id + 1), '999985', '简单的土豆泥沙拉', '简单的土豆泥沙拉', 'cs://1/image/aW1hZ2UvTVRvd05tWmpaVFZtWVRkaE0yUTVNMlZpTVdFME9EbG1abVl3WldRMU16RTJZZw', '14.02', 'http://www.baidu.com', '10', '2017-01-10 16:20:23', '1', NULL, NULL);
INSERT INTO `eh_business_promotions` (`id`, `namespace_id`, `subject`, `description`, `poster_uri`, `price`, `commodity_url`, `default_order`, `create_time`, `creator_uid`, `update_time`, `update_uid`)
VALUES ((@max_id := @max_id + 1), '999985', '藜麦减脂沙拉', '藜麦减脂沙拉', 'cs://1/image/aW1hZ2UvTVRvNVpURXdaR0UzT1RjeE5qWXpPV1U1WmpBeE9XSmhPVGs0WldFMFptUXhaUQ', '22.22', 'http://www.baidu.com', '10', '2017-01-10 16:20:23', '1', NULL, NULL);
INSERT INTO `eh_business_promotions` (`id`, `namespace_id`, `subject`, `description`, `poster_uri`, `price`, `commodity_url`, `default_order`, `create_time`, `creator_uid`, `update_time`, `update_uid`)
VALUES ((@max_id := @max_id + 1), '999985', '三色金枪鱼沙拉', '简单的土豆泥沙拉', 'cs://1/image/aW1hZ2UvTVRvME1EZzFZV1JrTnpZNVlqQXlNVGd5WVdZNE1HSmpOR00xWWpnd05HWXlNUQ', '1080.56', 'http://www.baidu.com', '10', '2017-01-10 16:20:23', '1', NULL, NULL);
INSERT INTO `eh_business_promotions` (`id`, `namespace_id`, `subject`, `description`, `poster_uri`, `price`, `commodity_url`, `default_order`, `create_time`, `creator_uid`, `update_time`, `update_uid`)
VALUES ((@max_id := @max_id + 1), '999985', '简单的土豆泥沙拉', '简单的土豆泥沙拉', 'cs://1/image/aW1hZ2UvTVRvd05tWmpaVFZtWVRkaE0yUTVNMlZpTVdFME9EbG1abVl3WldRMU16RTJZZw', '14.02', 'http://www.baidu.com', '10', '2017-01-10 16:20:23', '1', NULL, NULL);
INSERT INTO `eh_business_promotions` (`id`, `namespace_id`, `subject`, `description`, `poster_uri`, `price`, `commodity_url`, `default_order`, `create_time`, `creator_uid`, `update_time`, `update_uid`)
VALUES ((@max_id := @max_id + 1), '999985', '藜麦减脂沙拉', '藜麦减脂沙拉', 'cs://1/image/aW1hZ2UvTVRvNVpURXdaR0UzT1RjeE5qWXpPV1U1WmpBeE9XSmhPVGs0WldFMFptUXhaUQ', '22.22', 'http://www.baidu.com', '10', '2017-01-10 16:20:23', '1', NULL, NULL);
INSERT INTO `eh_business_promotions` (`id`, `namespace_id`, `subject`, `description`, `poster_uri`, `price`, `commodity_url`, `default_order`, `create_time`, `creator_uid`, `update_time`, `update_uid`)
VALUES ((@max_id := @max_id + 1), '999985', '三色金枪鱼沙拉', '简单的土豆泥沙拉', 'cs://1/image/aW1hZ2UvTVRvME1EZzFZV1JrTnpZNVlqQXlNVGd5WVdZNE1HSmpOR00xWWpnd05HWXlNUQ', '1080.56', 'http://www.baidu.com', '10', '2017-01-10 16:20:23', '1', NULL, NULL);

-- 修改活动运营icon action_data   add by xq.tian  2017/01/18
UPDATE `eh_launch_pad_items` SET `action_data` = '{\"publishPrivilege\":1,\"livePrivilege\":1,\"categoryId\": 1000001}' WHERE `namespace_id`='999985' AND `item_group` = 'OPPushActivity';