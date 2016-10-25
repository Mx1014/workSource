--
-- 服务联盟4.5.0   add by xq.tian  2016/10/18
--
SET @locale_string_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_string_id := @locale_string_id + 1), 'serviceAlliance.category.display', '1', 'zh_CN', '列表-介绍');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_string_id := @locale_string_id + 1), 'serviceAlliance.category.display', '2', 'zh_CN', '列表-大图');

--
-- 服务联盟详情页面配置   add by xq.tian  2016/10/19
--
SET @configuration_id = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'serviceAlliance.serviceDetail.url', 'http://core.zuolin.com/service-alliance/index.html#/service_detail/%s/%s?_k=%s', '服务联盟详情页面URL', '0', NULL);


-- merge from customer-manage-1.1-delta-data-release.sql by lqs 20161025
--
-- 车辆停车类型     add by xq.tian 2016/10/11
--
INSERT INTO `eh_parking_card_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `card_type`, `category_name`, `status`, `creator_uid`, `create_time`)
  VALUES ('1', '0', NULL, NULL, '1', '临时卡', '1', NULL, NULL);
INSERT INTO `eh_parking_card_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `card_type`, `category_name`, `status`, `creator_uid`, `create_time`)
  VALUES ('2', '0', NULL, NULL, '2', '月临时', '1', NULL, NULL);
INSERT INTO `eh_parking_card_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `card_type`, `category_name`, `status`, `creator_uid`, `create_time`)
  VALUES ('3', '0', NULL, NULL, '3', '充值卡', '1', NULL, NULL);
INSERT INTO `eh_parking_card_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `card_type`, `category_name`, `status`, `creator_uid`, `create_time`)
  VALUES ('4', '0', NULL, NULL, '4', '固定卡', '1', NULL, NULL);
INSERT INTO `eh_parking_card_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `card_type`, `category_name`, `status`, `creator_uid`, `create_time`)
  VALUES ('5', '0', NULL, NULL, '5', '免费卡', '1', NULL, NULL);
INSERT INTO `eh_parking_card_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `card_type`, `category_name`, `status`, `creator_uid`, `create_time`)
  VALUES ('6', '0', NULL, NULL, '6', '其它', '1', NULL, NULL);

--
-- 客户资料分类     add by xq.tian 2016/10/13
--
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`, `create_time`, `update_time`, `update_uid`)
  VALUES ('8', '0', 'none', '无', '1', NULL, NULL, NULL);

SET @locale_string_id = (SELECT MAX (id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@locale_string_id := @locale_string_id + 1), 'pm', '18001', 'zh_CN', '该记录已经处于未认证状态');
  
-- 物业报修2.5 by sunwen 20161025
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES ('920', '0', '完成回访', '任务管理 完成回访', NULL);

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('313', 'pmtask.notification', '8', 'zh_CN', '任务操作模版', '${operatorName} ${operatorPhone} 已回访该任务', '0');

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
  VALUES (28000,'服务录入',20000,NULL,'task_management_service_entry',0,2,'/20000/28000','park',456);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
  VALUES (29000,'执行人员设置',20000,NULL,'executive_setting',0,2,'/20000/29000','park',458);


INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
  VALUES (936,0,'服务录入','任务管理 服务录入 全部权限',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
  VALUES (937,0,'执行人员设置','任务管理 执行人员设置 全部权限',NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),936,28000,'服务录入',1,1,'分类设置 服务录入',605);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
  VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),937,29000,'执行人员设置',1,1,'统计 执行人员设置',606);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);

INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, '936', 1001,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, '936', 1002,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, '937', 1001,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, '937', 1002,0,1,now());	
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),28000,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),29000,'', 'EhNamespaces', 999992,2);	
	
	
	
	
	