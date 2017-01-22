-

--
-- 表单管理
--
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (50900, '表单管理', 50000, NULL, 'react:/form-management/form-list', 1, 2, '/50000/50900', 'park', 590);


INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10126, 0, '表单管理', '表单管理 全部权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10126, 50900, '表单管理', 1, 1, '表单管理  全部权限', 590);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `role_type`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 10126, 1001, 0, 1, NOW(), 'EhAclRoles');

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50900, '表单管理', 'EhNamespaces', 999983, 2);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`)
VALUES ('50900', '表单管理', '50000', '/50000/50900', '0', '2', '2', '0', UTC_TIMESTAMP());

SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id +1), '50900', '1', '10126', NULL, '0', UTC_TIMESTAMP());



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

-- 物业报修科兴菜单 add by sw 20160117
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`)
	VALUES ('20200', '物业报修', '20000', NULL, NULL, '1', '2', '/20000/20200', 'park', '201', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`)
	VALUES (20210, '任务列表', 20200, NULL, 'task_management_list/1', 0, 2, '/20000/20200/20210', 'park', 419, NULL);

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`)
	VALUES ('20230', '投诉建议', '20000', NULL, NULL, '1', '2', '/20000/20230', 'park', '201', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`)
	VALUES ('20240', '任务列表', '20230', NULL, 'task_management_list/202564', '0', '2', '/20000/20230/20240', 'park', '220', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`)
	VALUES ('20250', '服务录入', '20230', NULL, 'task_management_service_entry', '0', '2', '/20000/20230/20250', 'park', '225', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`)
	VALUES ('20255', '设置', '20230', NULL, NULL, '0', '2', '/20000/20230/20255', 'park', '228', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`)
	VALUES ('20258', '工作流设置', '20255', NULL, 'react:/working-flow/flow-list/property-service/20100', '0', '2', '/20000/20230/20255/20258', 'park', '230', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`)
	VALUES ('20280', '分类设置', '20255', NULL, 'classify_setting', '0', '2', '/20000/20230/20255/20280', 'park', '240', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`)
	VALUES ('20290', '统计', '20230', NULL, NULL, '0', '2', '/20000/20230/20290', 'park', '245', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`)
	VALUES ('20291', '服务统计', '20290', NULL, 'task_statistics', '0', '2', '/20000/20230/20290/20291', 'park', '180', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
	VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),10008,20200,'物业报修',1,1,'物业报修 管理员权限',710);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
	VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),10008,20210,'物业报修',1,1,'物业报修 管理员权限',710);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
	VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),10008,20230,'物业报修',1,1,'物业报修 管理员权限',710);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
	VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),10008,20240,'物业报修',1,1,'物业报修 管理员权限',710);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
	VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),10008,20250,'物业报修',1,1,'物业报修 管理员权限',710);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
	VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),10008,20255,'物业报修',1,1,'物业报修 管理员权限',710);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
	VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),10008,20258,'物业报修',1,1,'物业报修 管理员权限',710);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
	VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),10008,20280,'物业报修',1,1,'物业报修 管理员权限',710);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
	VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),10008,20290,'物业报修',1,1,'物业报修 管理员权限',710);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
	VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),10008,20291,'物业报修',1,1,'物业报修 管理员权限',710);

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20200, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes`    (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20210, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20230, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20240, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20250, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20255, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20258, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20280, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20290, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20291, '', 'EhNamespaces', 999983, 2);

DELETE FROM eh_web_menu_scopes WHERE menu_id IN (20100, 20140, 20150, 20155, 20158, 20160, 20170, 20180, 20190, 20191, 20192) AND owner_id = 999983;

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('pmtask.handler-999983-1', 'ebei', '', '0', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('pmtask.handler-999983-202564', 'flow', '', '0', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('pmtask.ebei.url', 'http://120.24.88.192:13902/ebeitest/', '', '0', NULL);


INSERT INTO `eh_locale_strings` ( `scope`, `code`, `locale`, `text`) VALUES ('pmtask', '10010', 'zh_CN', '分类不能为空！');
INSERT INTO `eh_locale_strings` ( `scope`, `code`, `locale`, `text`) VALUES ('pmtask', '10011', 'zh_CN', '任务已在处理中，不能取消！');

-- 更新科兴投诉建议 add by sw 20170119
UPDATE eh_launch_pad_items SET action_data = '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=202564&displayName=投诉建议"}',action_type = 60 WHERE id IN (112844, 112874) AND namespace_id = 999983;
UPDATE eh_launch_pad_items SET action_data = '{\"url\":\"http://core.zuolin.com/property_service/index.html?hideNavigationBar=1&taskCategoryId=1#/my_service#sign_suffix\"}' WHERE action_type = 14 AND item_label = '报修' AND namespace_id = 999983;

-- 万科停车 add by sw 20160117
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ('parking.wanke.url', 'http://122.224.250.35:7021', '万科立方系统', '0', NULL);


-- 黑名单配置到清华信息港 by sfyan 20170117
 SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
 INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
 VALUES ((@menu_scope_id := @menu_scope_id + 1), 30600, '', 'EhNamespaces', 999984, 2);



-- 正中汇修改广场图标和名字 by xiongying20170117
UPDATE eh_launch_pad_items SET item_label = "物业查费" WHERE item_label = "费用查询" AND namespace_id = 999983;
UPDATE eh_launch_pad_items SET icon_uri = "cs://1/image/aW1hZ2UvTVRwaU1tVTJNbUV4Wm1Jd05HRTBZV1F4T0Roa09HUXhNMkUwTldReFpHVXpOUQ" WHERE item_label = "任务管理" AND namespace_id = 999983;


-- 黑名单配置到清华信息港 by sfyan 20170117
 SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
 INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
 VALUES ((@menu_scope_id := @menu_scope_id + 1), 30600, '', 'EhNamespaces', 999984, 2);

 -- 黑名单配置到创源 by sfyan 20170117
 SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
 INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
 VALUES ((@menu_scope_id := @menu_scope_id + 1), 30600, '', 'EhNamespaces', 999986, 2);

 -- 黑名单配置到左邻 by sfyan 20170117
  SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
 INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
 VALUES ((@menu_scope_id := @menu_scope_id + 1), 30600, '', 'EhNamespaces', 0, 2);

 -- 增加审批 by xiongying20170117
 SET @jump_id = (SELECT MAX(id) FROM eh_service_alliance_jump_module);
 INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_url`) VALUES ((@jump_id := @jump_id + 1), '999983', '审批', 'zl://approval/create?approvalId={}&sourceId={}');


-- 修改活动运营icon action_data   add by xq.tian  2017/01/18
UPDATE `eh_launch_pad_items` SET `action_data` = '{\"publishPrivilege\":1,\"livePrivilege\":1,\"categoryId\": 1000001}' WHERE `namespace_id`='999985' AND `item_group` = 'OPPushActivity';

-- 上一步节点处理人 by janson
UPDATE `eh_flow_variables` SET `label`='上一步节点处理人' WHERE `id`='2001';


-- merge from serviceAlliance2.0 add by xiongying20170119
UPDATE eh_service_alliance_categories SET selected_logo_url = logo_url;

<<<<<<< HEAD

=======
>>>>>>> master
-- modify serviceDetail url add by xiongying20170120
update eh_configurations set value = '/service-alliance/index.html#/service_detail/%s/%s?_k=%s&ownerType=%s&ownerId=%s' where name like '%serviceAlliance.serviceDetail.url%';

-- 删除下个节点处理人
DELETE FROM `eh_flow_variables` WHERE id=2003;
DELETE FROM `eh_flow_variables` WHERE id=2004;



--
-- 车辆放行模块   add by xq.tian  2016/12/05
--
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`)
VALUES ('20900', '车辆放行', '20000', '/20000/20900', '0', '2', '2', '0', UTC_TIMESTAMP());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES ('10056', '0', '车辆放行 申请放行', '车辆放行 申请放行权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES ('10057', '0', '车辆放行 处理放行任务', '车辆放行 处理放行任务权限', NULL);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `role_type`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 10056, 1001, 0, 'EhAclRoles', 1, NOW());
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `role_type`,  `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 10057, 1001, 0, 'EhAclRoles', 1, NOW());

SET @eh_service_module_privileges = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges := @eh_service_module_privileges + 1), '20900', '1', '10056', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges := @eh_service_module_privileges + 1), '20900', '1', '10057', NULL, '0', UTC_TIMESTAMP());

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10056, 20900, '车辆放行', 1, 1, '车辆放行  全部权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10057, 20900, '车辆放行', 1, 1, '车辆放行  全部权限', 202);

INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10056, 20910, '车辆放行', 1, 1, '车辆放行  全部权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10057, 20910, '车辆放行', 1, 1, '车辆放行  全部权限', 202);

INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10056, 20920, '车辆放行', 1, 1, '车辆放行  全部权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10057, 20920, '车辆放行', 1, 1, '车辆放行  全部权限', 202);

INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10056, 20930, '车辆放行', 1, 1, '车辆放行  全部权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10057, 20930, '车辆放行', 1, 1, '车辆放行  全部权限', 202);

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (20900, '车辆放行', 20000, NULL, 'parking_clearance', 1, 2, '/20000/20900', 'park', 300);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (20910, '权限设置', 20900, NULL, 'vehicle_setting', 0, 2, '/20000/20900/20910', 'park', 301);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (20920, '放行记录', 20900, NULL, 'release_record', 0, 2, '/20000/20900/20920', 'park', 302);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (20930, '工作流设置', 20900, NULL, 'react:/working-flow/flow-list/vehicle-release/20900', 0, 2, '/20000/20900/20930', 'park', 303);

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 20900, '', 'EhNamespaces', 999984, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 20910, '', 'EhNamespaces', 999984, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 20920, '', 'EhNamespaces', 999984, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 20930, '', 'EhNamespaces', 999984, 2);

--
-- 模板
--
SET @eh_locale_templates = (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@eh_locale_templates := @eh_locale_templates + 1), 'parking.clearance', '1', 'zh_CN', '停车放行申请人看到的内容', '[{"key":"停车场名","value":"${parkingLotName}","entityType":"list"},{"key":"车牌号码","value":"${plateNumber}","entityType":"list"},{"key":"预计来访时间","value":"${clearanceTime}","entityType":"list"},{"key":"备注","value":"${remarks}","entityType":"multi_line"}]', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@eh_locale_templates := @eh_locale_templates + 1), 'parking.clearance', '2', 'zh_CN', '停车放行处理人看到的内容', '[{"key":"停车场名","value":"${parkingLotName}","entityType":"list"},{"key":"申请人","value":"${applicant}","entityType":"list"},{"key":"手机号","value":"${identifierToken}","entityType":"list"},{"key":"车牌号码","value":"${plateNumber}","entityType":"list"},{"key":"预计来访时间","value":"${clearanceTime}","entityType":"list"},{"key":"备注","value":"${remarks}","entityType":"multi_line"}]', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@eh_locale_templates := @eh_locale_templates + 1), 'parking.clearance', '3', 'zh_CN', '工作流摘要内容', '车牌号码：${plateNumber}\n来访时间：${clearanceTime}', '0');

SET @eh_locale_strings = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance', '1', 'zh_CN', '无');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance', '2', 'zh_CN', '对不起,您没有权限申请放行');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance', '3', 'zh_CN', '对不起,您没有权限处理放行任务');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance', '10001', 'zh_CN', '删除用户失败');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance', '10002', 'zh_CN', '没有启用的工作流');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance.log.status', '1', 'zh_CN', '处理中');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance.log.status', '2', 'zh_CN', '已完成');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance.log.status', '3', 'zh_CN', '已取消');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance.log.status', '4', 'zh_CN', '待处理');

SELECT MAX(id) FROM `eh_launch_pad_items` INTO @launch_pad_item_id;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), '999984', '0', '0', '0', '/home', 'Bizs', 'PARKING_CLEARANCE', '车辆放行', 'cs://1/image/aW1hZ2UvTVRvME5tTXpZVEEwWlRKa1lqQXlaVFEwTmpkaU5XRTJORGN5WVdJM056QmpZUQ', '1', '1', '57', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '1', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), '999984', '0', '0', '0', '/home', 'Bizs', 'PARKING_CLEARANCE_TASK', '放行任务', 'cs://1/image/aW1hZ2UvTVRvME5tTXpZVEEwWlRKa1lqQXlaVFEwTmpkaU5XRTJORGN5WVdJM056QmpZUQ', '1', '1', '58', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '1', NULL);


--
-- 新增的模板  add by xq.tian  2016/12/16
--
SET @eh_locale_strings = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parking.clearance', '10003', 'zh_CN', '用户已添加');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'parameters.error', '10001', 'zh_CN', '参数长度超过限制');



-- 增加新的error 字段 by wuhan 2017-1-20

INSERT INTO `eh_locale_strings` ( `scope`, `code`, `locale`, `text`) VALUES ('general_approval', '10001', 'zh_CN', '暂不支持申请，请联系管理员');


-- 补充菜单 by sfyan 20170120
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
	VALUES ('80120', '我的申请', '80100', NULL, 'service_list/155', '0', '2', '/80000/80100/80120', 'park', '820', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
	VALUES ('80220', '我的申请', '80200', NULL, 'service_list/151', '0', '2', '/80000/80200/80220', 'park', '820', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
	VALUES ('80320', '我的申请', '80300', NULL, 'service_list/152', '0', '2', '/80000/80300/80320', 'park', '820', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
	VALUES ('80420', '我的申请', '80400', NULL, 'service_list/153', '0', '2', '/80000/80400/80420', 'park', '820', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
	VALUES ('80520', '我的申请', '80500', NULL, 'service_list/154', '0', '2', '/80000/80500/80520', 'park', '820', NULL);

SET @eh_web_menu_privileges = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10121', '80120', '我的申请', '1', '1', '服务联盟 全部权限', '711');
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10122', '80220', '企业服务', '1', '1', '服务联盟 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10123', '80320', '厂房出租', '1', '1', '服务联盟 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10124', '80420', '公寓出租', '1', '1', '服务联盟 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10125', '80520', '医疗', '1', '1', '服务联盟 全部权限', '710');

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 80120, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 80220, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 80320, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 80420, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 80520, '', 'EhNamespaces', 999983, 2);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_type`, `role_id`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10121', 'EhAclRoles' ,'1005', '0', '1', UTC_TIMESTAMP());

--办事指南 add by xiongying20170120
update eh_web_menu_scopes set menu_name = '办事指南', apply_policy=1 where menu_id = 80100 and owner_id = 999983;

