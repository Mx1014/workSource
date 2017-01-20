

--
-- 表单管理
--
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (50900, '表单管理', 50000, NULL, 'react:/form-management/form-list', 1, 2, '/50000/50900', 'park', 590);


INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10123, 0, '表单管理', '表单管理 全部权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10123, 50900, '表单管理', 1, 1, '表单管理  全部权限', 590);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `role_type`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 10123, 1001, 0, 1, NOW(), 'EhAclRoles');

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50900, '表单管理', 'EhNamespaces', 999983, 2);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`)
VALUES ('50900', '表单管理', '50000', '/50000/50900', '0', '2', '2', '0', UTC_TIMESTAMP());

SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id +1), '50900', '1', '10123', NULL, '0', UTC_TIMESTAMP());



--
-- 服务联盟 审批管理菜单
--
UPDATE `eh_web_menus` SET NAME = '审批管理' ,data_type =NULL WHERE id = 40540;
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (40541, '审批列表', 40540, NULL, 'react:/approval-management/approval-list/service-alliance/40500', 1, 2, '/40000/40500/40540/40541', 'park', 458);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (40542, '申请记录', 40540, NULL, 'apply_record', 1, 2, '/40000/40500/40540/40542', 'park', 459);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES((@web_menu_privilege_id := @web_menu_privilege_id + 1),'10024','40541','服务联盟','1','1','服务联盟 全部权限','710');
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES((@web_menu_privilege_id := @web_menu_privilege_id + 1),'10024','40542','服务联盟','1','1','服务联盟 全部权限','710');


SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `role_type`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 422, 1001, 0, 1, NOW(), 'EhAclRoles');

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 40541, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 40542, '', 'EhNamespaces', 999983, 2);





--
-- 我的任务 广场图标
--

SET @eh_launch_pad_items = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999983', '0', '0', '0', '/home', 'Bizs', 'FLOW_TASKS', '任务管理', 'cs://1/image/aW1hZ2UvTVRveE4yVmxOak0wWkdReU9UY3dPVGMzTlRrM05UWmxOV1U1TVRneFltTTVaZw', '1', '1', '56', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999983', '0', '0', '0', '/home', 'Bizs', 'FLOW_TASKS', '任务管理', 'cs://1/image/aW1hZ2UvTVRveE4yVmxOak0wWkdReU9UY3dPVGMzTlRrM05UWmxOV1U1TVRneFltTTVaZw', '1', '1', '56', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', NULL);


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


-- 正中汇修改广场图标和名字 by xiongying20170117
UPDATE eh_launch_pad_items SET item_label = "物业查费" WHERE id IN(SELECT id FROM eh_launch_pad_items WHERE item_label = "费用查询" AND namespace_id = 999983);
UPDATE eh_launch_pad_items SET icon_uri = "cs://1/image/aW1hZ2UvTVRwaU1tVTJNbUV4Wm1Jd05HRTBZV1F4T0Roa09HUXhNMkUwTldReFpHVXpOUQ" WHERE id IN(SELECT id FROM eh_launch_pad_items WHERE item_label = "任务管理" AND namespace_id = 999983);


-- 修改活动运营icon action_data   add by xq.tian  2017/01/18
UPDATE `eh_launch_pad_items` SET `action_data` = '{\"publishPrivilege\":1,\"livePrivilege\":1,\"categoryId\": 1000001}' WHERE `namespace_id`='999985' AND `item_group` = 'OPPushActivity';

-- 上一步节点处理人 by janson
UPDATE `eh_flow_variables` SET `label`='上一步节点处理人' WHERE `id`='2001';

-- 北环门禁
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
VALUES ('1112290', '1000000', '0', '4', '0', '/home', 'Bizs', '北环门禁', '北环门禁', 'cs://1/image/aW1hZ2UvTVRwa05ERmhNMlkzWW1SalpqYzROREEzTVRJM01qQXpNREl5TnpJM1ptVm1Ndw', '1', '1', '63', '{\"hardwareId\":\"F9:86:16:F2:2F:8B\"}', '0', '0', '1', '1', NULL, '0', NULL, '', '', '1', 'park_tourist', '1');



-- 增加新的error 字段 by wuhan 2017-1-20

INSERT INTO `eh_locale_strings` ( `scope`, `code`, `locale`, `text`) VALUES ('general_approval', '10001', 'zh_CN', '暂不支持申请，请联系管理员');