-- fix 21422 add by xiongying20171222
delete from eh_var_fields where group_id in(19,20);

SET @field_id = (SELECT MAX(id) FROM `eh_var_fields`);  
SET @item_id = (SELECT MAX(id) FROM `eh_var_field_items`);  
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'intentionGrade', '����ȼ�', 'Long', '19', '/19/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '20', '1', '2', '1', NOW(), NULL, NULL, '20');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '40', '2', '2', '1', NOW(), NULL, NULL, '40');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '60', '3', '2', '1', NOW(), NULL, NULL, '60');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '80', '4', '2', '1', NOW(), NULL, NULL, '80');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '100', '5', '2', '1', NOW(), NULL, NULL, '100');

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'trackingTime', '����ʱ��', 'Long', '19', '/19/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"datetimeWithM\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'content', '��������', 'Long', '19', '/19/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"multiText\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'trackingType', '������ʽ', 'Long', '20', '/20/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '�绰', '1', '2', '1', NOW(), NULL, NULL, '1');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '�ʼ�', '2', '2', '1', NOW(), NULL, NULL, '3');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '����', '3', '2', '1', NOW(), NULL, NULL, '2');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '����', '4', '2', '1', NOW(), NULL, NULL, '4');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'customerName', '�ͻ�����', 'String', '20', '/20/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'trackingTime', '����ʱ��', 'Long', '20', '/20/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"datetimeWithM\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'notifyTime', '��ǰ����ʱ��', 'Long', '20', '/20/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"datetimeWithM\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'title', '����', 'Long', '20', '/20/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'content', '����', 'String', '20', '/20/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"multiText\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'trackingType', '������ʽ', 'Long', '19', '/19/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '�绰', '1', '2', '1', NOW(), NULL, NULL, '1');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '�ʼ�', '2', '2', '1', NOW(), NULL, NULL, '3');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '����', '3', '2', '1', NOW(), NULL, NULL, '2');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '����', '4', '2', '1', NOW(), NULL, NULL, '4');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'trackingUid', '������', 'Long', '19', '/19/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');


-- fix 20310
INSERT INTO eh_service_module_functions(id, module_id, privilege_id) SELECT privilege_id, module_id, privilege_id FROM eh_service_module_privileges WHERE privilege_type = 0 and module_id = 21210;
SET @exclude_id = (SELECT MAX(id) FROM `eh_service_module_exclude_functions`);   
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES ((@exclude_id := @exclude_id + 1), '999983', NULL, '21210', '21201');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES ((@exclude_id := @exclude_id + 1), '999983', NULL, '21210', '21208');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES ((@exclude_id := @exclude_id + 1), '999983', NULL, '21210', '21209');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES ((@exclude_id := @exclude_id + 1), '999983', NULL, '21210', '21214');

-- 服务热线1.3 by st.zheng
set @eh_service_configurations_id = (select max(id) from eh_service_configurations);
INSERT INTO `eh_service_configurations` (`id`, `owner_type`, `owner_id`, `name`, `value`, `namespace_id`, `display_name`) VALUES (@eh_service_configurations_id+1, 'community', '240111044331050370', 'hotline-notshow', '0', '999968', '专属客服');

UPDATE `eh_web_menus` SET `data_type`='react:/service-online/management' WHERE `id`='40300';

-- 初始化为按时长定价 by st.zheng
update `eh_rentalv2_price_rules` set `price_type` = 0;
update `eh_rentalv2_price_packages` set `price_type` = 0;

set @eh_locale_templates_id = (select max(id) from eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`) VALUES (@eh_locale_templates_id+1, 'rental.notification', '12', 'zh_CN', '修改金额的推送', '您申请预订的${resourceName}，使用时间：${startTime}，订单金额调整为${amount}');

-- if_use_feelist字段初始化
update `eh_pm_tasks` set `if_use_feelist` = 1;


-- merge from forum-2.2 by yanjun 20171225 start

-- 更新活动分享页面链接 add by yanjun 20171219
UPDATE eh_configurations set `value` = '/activity/build/index.html#detail' where name = 'activity.share.url';

-- merge from forum-2.2 by yanjun 20171225 end


-- merge from incubator-1.2 by yanjun 201712252013 start
-- 文件有效时间 add by yanjun 20171219
SET @id = (SELECT MAX(id) from eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (( @id :=  @id + 1), 'filedownload.valid.interval', '7', 'filedownload valid interval', '0', NULL);


SET @id = (SELECT MAX(id) from eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'task', '10001', 'zh_CN', '任务不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'task', '10002', 'zh_CN', '权限不足');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'task', '10003', 'zh_CN', '任务已执行完成，不可取消');


-- 文件中心菜单  add by yanjun 20171220
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`)
VALUES ('61000', '文件中心', '60000', '/60000/61000', '0', '2', '2', '0', UTC_TIMESTAMP());

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
VALUES ('61000', '文件中心', '60000', NULL, 'react:/file-center/file-list', '0', '2', '/60000/61000', 'park', '630', '61000', '2', NULL, 'module');

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
SELECT (@menu_scope_id := @menu_scope_id + 1), 61000, '', 'EhNamespaces', id, 2 from eh_namespaces;


-- “入孵申请”菜单改成“入驻申请”  add by yanjun 20171223
UPDATE eh_web_menus set name = '入驻申请' where id = 36000 and name = '入孵申请';

UPDATE eh_launch_pad_layouts set version_code = '2017121901', layout_json  = '{"versionCode":"2017121901","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":1,"separatorHeight":16,"defaultOrder":10},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup2","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":2},{"title":"园区快讯","iconUrl": "https://content-1.zuolin.com:443/image/aW1hZ2UvTVRveU5UVmtNVFpqTnpBek1URXpOREkzTkRnMU1qWTFOMlZrWVdKa1lXTTNZdw?token=XlYdOjlDVEVb4XyQO4_dd5RI37zTkV3jCKm_-XbRyLIGVUorWGnyRCwLAgMGV86baX30BnQW4nqzF9nlXGe4M0DbZxWBVTqnL019xazIDuhE6A0OXiMQwRqGX84_1HHv","groupName":"","widget":"NewsFlash","instanceConfig":{"timeWidgetStyle":"datetime","itemGroup":"Default","categoryId":2,"newsSize":3},"defaultOrder":30,"separatorFlag":0,"separatorHeight":0}]}' where namespace_id = 999964 and `name` = 'ServiceMarketLayout' and `status` = 2;
UPDATE eh_launch_pad_items set display_flag = 1 where namespace_id = 999964;

UPDATE eh_launch_pad_items set item_label = '入驻申请', item_name = '入驻申请' where namespace_id = 999964 and item_name = '入孵申请';
UPDATE eh_launch_pad_items set item_label = '产品信息查询', item_name = '产品信息查询' where namespace_id = 999964 and item_name = '产品信息发布';
UPDATE eh_launch_pad_items set item_label = '机构信息查询', item_name = '机构信息查询' where namespace_id = 999964 and item_name = '机构信息发布';
DELETE from eh_launch_pad_items where namespace_id = 999964 and item_name in ('新闻快讯', '更多');

-- merge from incubator-1.2 by yanjun 201712252013 end


