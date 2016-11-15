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

SET @locale_string_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@locale_string_id := @locale_string_id + 1), 'pm', '18001', 'zh_CN', '该记录已经处于未认证状态');

-- 物业报修2.5 by sunwen 20161025
delete from eh_categories where id = 6;
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`)
	VALUES ('6', '0', '0', '任务', '任务', '0', '2', '2015-09-28 06:09:03', NULL, NULL, NULL, '0');
UPDATE `eh_configurations` SET `value`='6' WHERE `name`='pmtask.category.ancestor';

INSERT INTO `eh_acl_roles` (`id`, `app_id`, `name`, `description`, `tag`, `namespace_id`, `owner_type`, `owner_id`)
	VALUES ('1017', '32', '执行人员', '任务管理 执行人员', NULL, '0', 'EhOrganizations', NULL);
INSERT INTO `eh_acl_roles` (`id`, `app_id`, `name`, `description`, `tag`, `namespace_id`, `owner_type`, `owner_id`)
	VALUES ('1018', '32', '维修人员', '任务管理 维修人员', NULL, '0', 'EhOrganizations', NULL);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);

INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, '331', 1017,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, '332', 1017,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, '333', 1017,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, '904', 1017,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, '805', 1018,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, '332', 1018,0,1,now());

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
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, '920', 1001,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, '920', 1002,0,1,now());

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

-- by Janson add qr driver support
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (999985, 'aclink.qr_driver_ext', 'phone_visit', 'the driver extend of this namespace.(zuolin/phone_visit)');




-- 以下为3.10.4合过来的脚本--------

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

-- 储能停车充值 add by sunwen 20161025
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
	VALUES ('400', 'parking', '10010', 'zh_CN', '费用已过期，请重新查询费用');

-- 华润OE配search type add by xiongying 20161026
SET @search_type_id = (SELECT MAX(id) FROM `eh_search_types`);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999985', '', '0', '投票', 'poll', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999985', '', '0', '活动', 'activity', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999985', '', '0', '话题', 'topic', '1', NULL, NULL);

-- 华润OE配置企业定制服务联盟入口   add by xq.tian  2016/10/27
SET @alliance_category_id = (SELECT MAX(id) FROM `eh_service_alliance_categories`);
INSERT INTO `eh_service_alliance_categories` (`id`, `parent_id`, `name`, `path`, `status`, `creator_uid`, `create_time`, `namespace_id`, `display_mode`)
VALUES ((@alliance_category_id := @alliance_category_id + 1), '0', '企业定制', '企业定制', '2', '1', UTC_TIMESTAMP(), '999985', '1');


-- 配置活动提醒消息, add by tt, 20161027
SET @id := (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'activity.notification', 5, 'zh_CN', '创建者删除活动', '很抱歉通知您：您报名的活动<${tag} 丨 ${title}>因故取消。\n更多活动敬请继续关注。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'activity.notification', 6, 'zh_CN', '活动提醒', '您报名的活动 <${tag} 丨 ${title}> 还有 ${time}就要开始了 >>', 0);

SELECT @id:=MAX(id) FROM `eh_locale_strings`;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'activity', '6', 'zh_CN', '开始时间：');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'activity', '7', 'zh_CN', '结束时间：');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'activity', '8', 'zh_CN', '活动地点：');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'activity', '9', 'zh_CN', '活动嘉宾：');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'activity', '10', 'zh_CN', '抱歉，您的APP版本不支持查看该内容！');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'activity', '10013', 'zh_CN', '活动报名人数已满，感谢关注');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'activity', '10014', 'zh_CN', '报名人数上限应大于0！');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'activity', '10015', 'zh_CN', '报名为数上限不能大于1万！');

-- 配置活动详情里面的内容的链接, add by tt, 20161027
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (178, 'activity.content.url', '/web/lib/html/activity_text_review.html', 'activity content url', 0, NULL);

-- 以上为3.10.4合过来的脚本--------

-- 更新创源 group_id add by sunwen 20161028
UPDATE eh_organizations r INNER JOIN eh_groups g ON r.id = g.visible_region_id
SET r.group_id = g.id where r.namespace_id = 999986 and g.visible_region_type = 1;

-- by Janson add qr driver support
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (999992, 'aclink.qr_driver_ext', 'phone_visit', 'the driver extend of this namespace.(zuolin/phone_visit)');


-- π星球爱特家更新banner by xiongying20161101
delete from eh_banners where namespace_id = 999988;

SET @banner_id := (SELECT MAX(id) FROM `eh_banners`);
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`) 
    VALUES (@banner_id:=@banner_id+1, '999988', '0', '/home', 'Default', '0', '0', '爱特家迷你居', 'atjia', 'cs://1/image/aW1hZ2UvTVRwaE1EYzJOR1JtWWpOa1l6ZG1Oemd3WkdGa1l6UTJNbVUxWWpnMk1Ea3hZUQ', 14, '{"url":"http://core.zuolin.com/zweb/mobile/static/banner/star.html"}', NULL, NULL, '2', '1', '0', UTC_TIMESTAMP(), NULL, 'default', '0');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`) 
    VALUES (@banner_id:=@banner_id+1, '999988', '0', '/home', 'Default', '0', '0', '爱特家迷你居', 'atjia', 'cs://1/image/aW1hZ2UvTVRwaE1EYzJOR1JtWWpOa1l6ZG1Oemd3WkdGa1l6UTJNbVUxWWpnMk1Ea3hZUQ', 14, '{"url":"http://core.zuolin.com/zweb/mobile/static/banner/star.html"}', NULL, NULL, '2', '1', '0', UTC_TIMESTAMP(), NULL, 'pm_admin', '0');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`) 
    VALUES (@banner_id:=@banner_id+1, '999988', '0', '/home', 'Default', '0', '0', '爱特家迷你居', 'atjia', 'cs://1/image/aW1hZ2UvTVRveVkySmhabU5pT1RZNE9UQTJZbU5sTmpVMFpUUXlNekJrTlRBMVpHSmlNQQ', 14, '{"url":"http://core.zuolin.com/zweb/mobile/static/banner/star.html"}', NULL, NULL, '2', '1', '0', UTC_TIMESTAMP(), NULL, 'default', '0');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`) 
    VALUES (@banner_id:=@banner_id+1, '999988', '0', '/home', 'Default', '0', '0', '爱特家迷你居', 'atjia', 'cs://1/image/aW1hZ2UvTVRveVkySmhabU5pT1RZNE9UQTJZbU5sTmpVMFpUUXlNekJrTlRBMVpHSmlNQQ', 14, '{"url":"http://core.zuolin.com/zweb/mobile/static/banner/star.html"}', NULL, NULL, '2', '1', '0', UTC_TIMESTAMP(), NULL, 'pm_admin', '0');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`) 
    VALUES (@banner_id:=@banner_id+1, '999988', '0', '/home', 'Default', '0', '0', '爱特家迷你居', 'atjia', 'cs://1/image/aW1hZ2UvTVRvMU56RTFaalUzWlRVM1pEaG1ZMll6T0dNeE5ERTFaakEzWlRsbFl6aG1ZUQ', 14, '{"url":"http://core.zuolin.com/zweb/mobile/static/banner/star.html"}', NULL, NULL, '2', '1', '0', UTC_TIMESTAMP(), NULL, 'default', '0');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`) 
    VALUES (@banner_id:=@banner_id+1, '999988', '0', '/home', 'Default', '0', '0', '爱特家迷你居', 'atjia', 'cs://1/image/aW1hZ2UvTVRvMU56RTFaalUzWlRVM1pEaG1ZMll6T0dNeE5ERTFaakEzWlRsbFl6aG1ZUQ', 14, '{"url":"http://core.zuolin.com/zweb/mobile/static/banner/star.html"}', NULL, NULL, '2', '1', '0', UTC_TIMESTAMP(), NULL, 'pm_admin', '0');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`) 
    VALUES (@banner_id:=@banner_id+1, '999988', '0', '/home', 'Default', '0', '0', '爱特家迷你居', 'atjia', 'cs://1/image/aW1hZ2UvTVRvelpEa3pOV00xTnpFNU5XVXpaRGRtWWpZNVl6ZzBZMkk1T0dabU5XWmpNQQ', 14, '{"url":"http://core.zuolin.com/zweb/mobile/static/banner/star.html"}', NULL, NULL, '2', '1', '0', UTC_TIMESTAMP(), NULL, 'default', '0');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`, `apply_policy`) 
    VALUES (@banner_id:=@banner_id+1, '999988', '0', '/home', 'Default', '0', '0', '爱特家迷你居', 'atjia', 'cs://1/image/aW1hZ2UvTVRvelpEa3pOV00xTnpFNU5XVXpaRGRtWWpZNVl6ZzBZMkk1T0dabU5XWmpNQQ', 14, '{"url":"http://core.zuolin.com/zweb/mobile/static/banner/star.html"}', NULL, NULL, '2', '1', '0', UTC_TIMESTAMP(), NULL, 'pm_admin', '0');

-- 科技园更新服务广场
UPDATE `eh_launch_pad_layouts` SET `layout_json`='{"versionCode":"2016110101","versionName":"3.0.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0}]}', `version_code` = '2016110101' WHERE `id`=11 AND `namespace_id`=1000000;
UPDATE `eh_launch_pad_layouts` SET `layout_json`='{"versionCode":"2016110101","versionName":"3.0.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0}]}', `version_code` = '2016110101' WHERE `id`=111 AND `namespace_id`=1000000;

-- 物业报修2.5 数据迁移 add by sunwen 20161102
UPDATE eh_pm_tasks r1 JOIN eh_pm_tasks r2 on r1.id = r2.id join eh_pm_task_logs l on r1.id = l.task_id 
join eh_categories c1 on r2.category_id = c1.id JOIN eh_categories c2 on c1.parent_id = c2.id
set r1.task_category_id = r2.category_id, r1.source_type = 'app', r1.requestor_name = l.operator_name, 
r1.requestor_phone = l.operator_phone where c2.parent_id = 0 and l.status = 1;

UPDATE eh_pm_tasks r1 JOIN eh_pm_tasks r2 on r1.id = r2.id join eh_pm_task_logs l on r1.id = l.task_id 
join eh_categories c1 on r2.category_id = c1.id JOIN eh_categories c2 on c1.parent_id = c2.id
set r1.task_category_id = c2.id, r1.source_type = 'app', r1.requestor_name = l.operator_name, 
r1.requestor_phone = l.operator_phone where c2.parent_id != 0 and l.status = 1;

-- 物业报修2.5 数据迁移 add by sunwen 20161102
UPDATE eh_categories c1 JOIN eh_categories c2 on c1.parent_id = c2.id
set c1.parent_id = 6 where c2.parent_id = 0 and c1.path like '任务/%';

select * from eh_categories c1 JOIN eh_categories c2 on c1.parent_id = c2.id
 where c2.parent_id = 0 and c1.path like '任务/%';

DELETE FROM eh_categories where name = '任务' and id != 6;
-- 威新Link	物业报修2.5菜单 add by sunwen 20161102
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES((@menu_scope_id := @menu_scope_id + 1),28000,'', 'EhNamespaces', 999991,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES((@menu_scope_id := @menu_scope_id + 1),29000,'', 'EhNamespaces', 999991,2);

-- 物业报修2.5增加权限 add by sunwen 20161102
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
  VALUES (805,0,'查看我的任务','任务管理 查看我的任务',NULL);
  
UPDATE eh_acl_privileges SET name = '查看所有任务', description = '任务管理 查看所有任务' WHERE id = 904;

UPDATE eh_web_menu_privileges SET name = '查看所有任务', discription = '查看所有任务 全部权限' where privilege_id = 904 and menu_id = 24000;

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
	VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),805,24000,'查看我的任务',1,1,'查看我的任务 全部权限',603);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
	VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),920,24000,'完成回访',1,1,'完成回访 全部权限',603);
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, '805', 1001,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, '805', 1002,0,1,now());
    
-- 用uuid代替原有encodetoken生成的二维码好扫一点 add by xiongying 20161104
update eh_equipment_inspection_equipments set qr_code_token = UUID();



--
-- 删除深业的家庭及家庭里的成员信息		add by xq.tian	2016/11/08
--
DELETE FROM eh_user_groups WHERE group_id IN (
  SELECT id FROM eh_groups WHERE discriminator = 'family' AND integral_tag1 IN (
    SELECT id FROM eh_addresses WHERE namespace_id = '999992'
  )
);

DELETE FROM eh_group_members WHERE group_id IN (
  SELECT id FROM eh_groups WHERE discriminator = 'family' AND integral_tag1 IN (
    SELECT id FROM eh_addresses WHERE namespace_id = '999992'
  )
);

DELETE FROM eh_groups WHERE discriminator = 'family' AND integral_tag1 IN (
  SELECT id FROM eh_addresses WHERE namespace_id = '999992'
);

UPDATE eh_organization_address_mappings SET living_status = '0' WHERE address_id IN (
  SELECT id FROM eh_addresses WHERE namespace_id = '999992'
);

--
-- 删除深业的客户资料信息		add by xq.tian	2016/11/08
--
DELETE FROM eh_organization_owner_address WHERE organization_owner_id IN (
  SELECT id FROM eh_organization_owners WHERE namespace_id = '999992' AND community_id IN (
    SELECT community_id FROM eh_addresses WHERE namespace_id = '999992'
  )
);

DELETE FROM eh_organization_owners WHERE namespace_id = '999992' AND community_id IN (
  SELECT community_id FROM eh_addresses WHERE namespace_id = '999992'
);

DELETE FROM eh_organization_owner_behaviors WHERE namespace_id = '999992';
DELETE FROM eh_organization_owner_attachments WHERE namespace_id = '999992';
DELETE FROM eh_organization_owner_cars WHERE namespace_id = '999992';
DELETE FROM eh_organization_owner_car_attachments WHERE namespace_id = '999992';
DELETE FROM eh_organization_owner_owner_car WHERE namespace_id = '999992';

--
-- 删除深业的家庭及家庭里的成员信息		add by xq.tian	2016/11/010
--
DELETE FROM eh_user_groups WHERE group_id IN (
  SELECT id FROM eh_groups WHERE discriminator = 'family' AND integral_tag2 IN (240111044331051300, 240111044331051301, 240111044331051302, 240111044331051303, 240111044331051304)
);

DELETE FROM eh_group_members WHERE group_id IN (
  SELECT id FROM eh_groups WHERE discriminator = 'family' AND integral_tag2 IN (240111044331051300, 240111044331051301, 240111044331051302, 240111044331051303, 240111044331051304)
);

DELETE FROM eh_groups WHERE discriminator = 'family' AND integral_tag2 IN (240111044331051300, 240111044331051301, 240111044331051302, 240111044331051303, 240111044331051304);


-- 新增科技服务模板 add by xiongying 20161111
INSERT INTO `eh_request_templates` (`id`, `template_type`, `name`, `button_title`, `email_flag`, `msg_flag`, `fields_json`, `status`, `creator_uid`, `create_time`)
    VALUES ('8', 'ReserveTech', '科技服务', '我要预约', '1', '1', '{"fields":[{"fieldName":"reserveOrganization","fieldDisplayName":"预约机构","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入公司名称","requiredFlag":"1"},{"fieldName":"reserveTime","fieldDisplayName":"预约时间","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入预约时间段","requiredFlag":"1"},{"fieldName":"contact","fieldDisplayName":"联系人","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入联系人姓名","requiredFlag":"1"},{"fieldName":"mobile","fieldDisplayName":"联系电话","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入联系电话","requiredFlag":"1"},{"fieldName":"remarks","fieldDisplayName":"备注","fieldType":"string","fieldContentType":"text","fieldDesc":"选填，若还有其他要求，可在此填写","requiredFlag":"0"}]}', '1', '1', UTC_TIMESTAMP());
INSERT INTO `eh_request_templates_namespace_mapping` (`id`, `namespace_id`, `template_id`) VALUES (8, '999984', '8');
INSERT INTO `eh_request_templates_namespace_mapping` (`id`, `namespace_id`, `template_id`) VALUES (9, '999984', '1');