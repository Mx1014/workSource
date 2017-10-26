-- add by xiongying20171024
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('2300000', '客户管理', '2000000', NULL, 'customer-management', '1', '2', '/2000000/2300000', 'zuolin', '2', '21100', '2', 'system', 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('2310000', '客户管理', '2300000', NULL, 'customer-management', '1', '2', '/2000000/2300000/2310000', 'zuolin', '2', '21100', '3', 'system', 'page');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('2400000', '合同管理', '2000000', NULL, 'contract-management', '1', '2', '/2000000/2400000', 'zuolin', '2', '21200', '2', 'system', 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('2410000', '合同管理', '2400000', NULL, 'contract-management', '1', '2', '/2000000/2400000/2410000', 'zuolin', '2', '21200', '3', 'system', 'page');

SET @eh_var_fields_id = (SELECT MAX(id) FROM `eh_var_fields`);
SET @eh_var_field_items_id = (SELECT MAX(id) FROM `eh_var_field_items`);
update eh_var_fields set field_param = '{\"fieldParamType\": \"text\", \"length\": 32}' where id = 7;
update eh_var_fields set field_param = '{\"fieldParamType\": \"select\", \"length\": 32}' where id = 188;
update eh_var_fields set field_param = '{\"fieldParamType\": \"text\", \"length\": 32}' where id = 211;
update eh_var_fields set field_param = '{\"fieldParamType\": \"text\", \"length\": 32}' where id = 212;
update eh_var_fields set field_param = '{\"fieldParamType\": \"text\", \"length\": 32}' where id = 213;
update eh_var_fields set field_param = '{\"fieldParamType\": \"select\", \"length\": 32}' where id = 214;
update eh_var_fields set field_param = '{\"fieldParamType\": \"text\", \"length\": 32}' where id = 215;

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES ((@eh_var_fields_id := @eh_var_fields_id + 1 ), 'enterprise_customer', 'intentionGrade', '意向等级', 'Long', '19', '/19', '1', NULL, '2', '1', '2017-08-25 02:27:30', NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');


INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES ((@eh_var_fields_id := @eh_var_fields_id + 1 ), 'enterprise_customer', 'trackingTime', '跟进时间', 'Long', '19', '/19', '1', NULL, '2', '1', '2017-08-25 02:27:30', NULL, NULL, '{\"fieldParamType\": \"datetimeWithM\", \"length\": 32}');


INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES ((@eh_var_fields_id := @eh_var_fields_id + 1 ), 'enterprise_customer', 'content', '跟进内容', 'Long', '19', '/19', '1', NULL, '2', '1', '2017-08-25 02:27:30', NULL, NULL, '{\"fieldParamType\": \"multiText\", \"length\": 32}');


INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES ((@eh_var_fields_id := @eh_var_fields_id + 1 ), 'enterprise_customer', 'trackingType', '计划类型', 'Long', '20', '/20', '1', NULL, '2', '1', '2017-08-25 02:27:30', NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');


INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES ((@eh_var_fields_id := @eh_var_fields_id + 1 ), 'enterprise_customer', 'customerName', '客户名称', 'String', '20', '/20', '1', NULL, '2', '1', '2017-08-25 02:27:30', NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES ((@eh_var_fields_id := @eh_var_fields_id + 1 ), 'enterprise_customer', 'trackingTime', '跟进时间', 'Long', '20', '/20', '1', NULL, '2', '1', '2017-08-25 02:27:30', NULL, NULL, '{\"fieldParamType\": \"datetimeWithM\", \"length\": 32}');

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES ((@eh_var_fields_id := @eh_var_fields_id + 1 ), 'enterprise_customer', 'notifyTime', '提前提醒时间', 'Long', '20', '/20', '1', NULL, '2', '1', '2017-08-25 02:27:30', NULL, NULL, '{\"fieldParamType\": \"datetimeWithM\", \"length\": 32}');


INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES ((@eh_var_fields_id := @eh_var_fields_id + 1 ), 'enterprise_customer', 'title', '标题', 'Long', '20', '/20', '1', NULL, '2', '1', '2017-08-25 02:27:30', NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');


INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES ((@eh_var_fields_id := @eh_var_fields_id + 1 ), 'enterprise_customer', 'content', '内容', 'String', '20', '/20', '1', NULL, '2', '1', '2017-08-25 02:27:30', NULL, NULL, '{\"fieldParamType\": \"multiText\", \"length\": 32}');


INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`)
VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', 214, '写字楼', '1', '2', '1', '2017-09-14 08:03:29', NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`)
VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', 214, '商铺', '1', '2', '1', '2017-09-14 08:03:29', NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`)
VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', 214, '厂房', '1', '2', '1', '2017-09-14 08:03:29', NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`)
VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', 214, '车位', '1', '2', '1', '2017-09-14 08:03:29', NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`)
VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', 214, '其他', '1', '2', '1', '2017-09-14 08:03:29', NULL, NULL);

INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', '188', '个人', '1', '2', '1', '2017-08-24 04:26:25', NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', '188', '商品房', '1', '2', '1', '2017-10-10 02:35:31', NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', '188', '存量房', '1', '2', '1', '2017-10-10 02:35:31', NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', '188', '集资房', '1', '2', '1', '2017-10-10 02:35:31', NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', '188', '平价房', '1', '2', '1', '2017-10-10 02:35:31', NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', '188', '廉租住房', '1', '2', '1', '2017-10-10 02:35:31', NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', '188', '经济适用住房', '1', '2', '1', '2017-10-10 02:35:31', NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', '188', '公寓式住宅', '1', '2', '1', '2017-10-10 02:35:31', NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', '188', '其他', '1', '2', '1', '2017-10-10 02:35:31', NULL, NULL);

-- 默认去处理按钮文本  add by xq.tian 2017/10/25
update eh_flow_nodes set goto_process_button_name='去处理' where goto_process_button_name is null or goto_process_button_name='';
-- by Sir Xiongying.V.Breg
update eh_var_field_groups set name = 'com.everhomes.customer.CustomerTracking' where title = '跟进信息';
update eh_var_field_groups set name = 'com.everhomes.customer.CustomerTrackingPlan' where title = '计划信息'

-- 工作流的文本修改  add by xq.tian 2017/10/25
UPDATE eh_locale_templates SET `text` = '在 ${nodeName} 执行 ${buttonName}' WHERE scope='flow' AND code=20001;
UPDATE eh_locale_templates SET `text` = '任务超时 已取消任务' WHERE scope='flow' AND code=20003;

-- 园区入驻 add by sw 20171026
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ('apply.entry.lease.project.detail.url', '/park-entry/dist/index.html?hideNavigationBar=1#/project_intro/%s', NULL, '0', NULL);

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
	VALUES ('40103', '项目介绍', '40100', NULL, 'react:/project-intro/project-list', '0', '2', '/40000/40100/40103', 'park', '411', '40100', '3', NULL, 'module');
update eh_web_menus set name = '楼栋介绍' where id = 40105;
set @id = (select MAX(id) FROM eh_web_menu_scopes);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	select (@id := @id + 1), '40103', '', 'EhNamespaces', owner_id, '2' from eh_web_menu_scopes where menu_id = 40100;

UPDATE eh_general_forms set template_text = '[{\"dataSourceType\":\"USER_NAME\",\"dynamicFlag\":1,\"fieldDisplayName\":\"用户姓名\",\"fieldExtra\":\"{\\\"limitWord\\\":10}\",\"fieldName\":\"USER_NAME\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"USER_PHONE\",\"dynamicFlag\":1,\"fieldDisplayName\":\"手机号码\",\"fieldExtra\":\"{\\\"limitWord\\\":11}\",\"fieldName\":\"USER_PHONE\",\"fieldType\":\"INTEGER_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"USER_COMPANY\",\"dynamicFlag\":1,\"fieldDisplayName\":\"公司名称\",\"fieldExtra\":\"{\\\"limitWord\\\":20}\",\"fieldName\":\"USER_COMPANY\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"LEASE_PROJECT_NAME\",\"dynamicFlag\":1,\"fieldDisplayName\":\"项目名称\",\"fieldExtra\":\"{\\\"limitWord\\\":20}\",\"fieldName\":\"LEASE_PROJECT_NAME\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"LEASE_PROMOTION_BUILDING\",\"dynamicFlag\":1,\"fieldDisplayName\":\"楼栋名称\",\"fieldExtra\":\"{\\\"limitWord\\\":20}\",\"fieldName\":\"LEASE_PROMOTION_BUILDING\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"LEASE_PROMOTION_APARTMENT\",\"dynamicFlag\":1,\"fieldDisplayName\":\"门牌号码\",\"fieldExtra\":\"{\\\"limitWord\\\":20}\",\"fieldName\":\"LEASE_PROMOTION_APARTMENT\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"LEASE_PROMOTION_DESCRIPTION\",\"dynamicFlag\":0,\"fieldDisplayName\":\"备注说明\",\"fieldName\":\"LEASE_PROMOTION_DESCRIPTION\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"CUSTOM_DATA\",\"dynamicFlag\":0,\"fieldName\":\"CUSTOM_DATA\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"visibleType\":\"HIDDEN\"}]' where owner_type = 'EhLeasePromotions';
UPDATE eh_general_forms set template_text = '[{\"dataSourceType\":\"USER_NAME\",\"dynamicFlag\":1,\"fieldDisplayName\":\"用户姓名\",\"fieldExtra\":\"{\\\"limitWord\\\":10}\",\"fieldName\":\"USER_NAME\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"USER_PHONE\",\"dynamicFlag\":1,\"fieldDisplayName\":\"手机号码\",\"fieldExtra\":\"{\\\"limitWord\\\":11}\",\"fieldName\":\"USER_PHONE\",\"fieldType\":\"INTEGER_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"USER_COMPANY\",\"dynamicFlag\":1,\"fieldDisplayName\":\"公司名称\",\"fieldExtra\":\"{\\\"limitWord\\\":20}\",\"fieldName\":\"USER_COMPANY\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"LEASE_PROJECT_NAME\",\"dynamicFlag\":1,\"fieldDisplayName\":\"项目名称\",\"fieldExtra\":\"{\\\"limitWord\\\":20}\",\"fieldName\":\"LEASE_PROJECT_NAME\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"LEASE_PROMOTION_BUILDING\",\"dynamicFlag\":1,\"fieldDisplayName\":\"楼栋名称\",\"fieldExtra\":\"{\\\"limitWord\\\":20}\",\"fieldName\":\"LEASE_PROMOTION_BUILDING\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"LEASE_PROMOTION_DESCRIPTION\",\"dynamicFlag\":0,\"fieldDisplayName\":\"备注说明\",\"fieldName\":\"LEASE_PROMOTION_DESCRIPTION\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"CUSTOM_DATA\",\"dynamicFlag\":0,\"fieldName\":\"CUSTOM_DATA\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"visibleType\":\"HIDDEN\"}]' where owner_type = 'EhBuildings';

set @id = (select MAX(id) FROM eh_general_forms);
set @id = @id + 1;
INSERT INTO `eh_general_forms` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `form_origin_id`, `form_version`, `template_type`, `template_text`, `status`, `update_time`, `create_time`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`)
  VALUES (@id, '0', '0', '0', 'EhLeaseProjects', NULL, NULL, '招租管理默认表单', @id, '0', 'DEFAULT_JSON', '[{\"dataSourceType\":\"USER_NAME\",\"dynamicFlag\":1,\"fieldDisplayName\":\"用户姓名\",\"fieldExtra\":\"{\\\"limitWord\\\":10}\",\"fieldName\":\"USER_NAME\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"USER_PHONE\",\"dynamicFlag\":1,\"fieldDisplayName\":\"手机号码\",\"fieldExtra\":\"{\\\"limitWord\\\":11}\",\"fieldName\":\"USER_PHONE\",\"fieldType\":\"INTEGER_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"USER_COMPANY\",\"dynamicFlag\":1,\"fieldDisplayName\":\"公司名称\",\"fieldExtra\":\"{\\\"limitWord\\\":20}\",\"fieldName\":\"USER_COMPANY\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"LEASE_PROJECT_NAME\",\"dynamicFlag\":1,\"fieldDisplayName\":\"项目名称\",\"fieldExtra\":\"{\\\"limitWord\\\":20}\",\"fieldName\":\"LEASE_PROJECT_NAME\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"LEASE_PROMOTION_DESCRIPTION\",\"dynamicFlag\":0,\"fieldDisplayName\":\"备注说明\",\"fieldName\":\"LEASE_PROMOTION_DESCRIPTION\",\"fieldType\":\"MULTI_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":0,\"validatorType\":\"TEXT_LIMIT\",\"visibleType\":\"EDITABLE\"},{\"dataSourceType\":\"CUSTOM_DATA\",\"dynamicFlag\":0,\"fieldName\":\"CUSTOM_DATA\",\"fieldType\":\"SINGLE_LINE_TEXT\",\"renderType\":\"DEFAULT\",\"requiredFlag\":1,\"visibleType\":\"HIDDEN\"}]', '2', '2017-06-10 18:46:36', '2017-06-10 18:45:48', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

set @id = IFNULL((select MAX(id) FROM eh_lease_configs), 1);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999975', NULL, NULL, 'displayNameStr', '项目介绍,待租物业', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999975', NULL, NULL, 'displayOrderStr', '1,2', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999971', NULL, NULL, 'displayNameStr', '办公招租,商户招租', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999971', NULL, NULL, 'displayOrderStr', '1,2', NULL, NULL);

INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '1000000', NULL, NULL, 'buildingIntroduceFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999983', NULL, NULL, 'buildingIntroduceFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999985', NULL, NULL, 'buildingIntroduceFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999977', NULL, NULL, 'buildingIntroduceFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999975', NULL, NULL, 'buildingIntroduceFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999976', NULL, NULL, 'buildingIntroduceFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999974', NULL, NULL, 'buildingIntroduceFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999993', NULL, NULL, 'buildingIntroduceFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999972', NULL, NULL, 'buildingIntroduceFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999971', NULL, NULL, 'buildingIntroduceFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999970', NULL, NULL, 'buildingIntroduceFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999991', NULL, NULL, 'buildingIntroduceFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999969', NULL, NULL, 'buildingIntroduceFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999965', NULL, NULL, 'buildingIntroduceFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999967', NULL, NULL, 'buildingIntroduceFlag', '1', NULL, NULL);

INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '1000000', NULL, NULL, 'renewFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999983', NULL, NULL, 'renewFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999977', NULL, NULL, 'renewFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999975', NULL, NULL, 'renewFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999976', NULL, NULL, 'renewFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999974', NULL, NULL, 'renewFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999993', NULL, NULL, 'renewFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999973', NULL, NULL, 'renewFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999972', NULL, NULL, 'renewFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999970', NULL, NULL, 'renewFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999965', NULL, NULL, 'renewFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999967', NULL, NULL, 'renewFlag', '1', NULL, NULL);

INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999985', NULL, NULL, 'rentAmountFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999985', NULL, NULL, 'rentAmountUnit', 'MONTH_UNIT', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999977', NULL, NULL, 'rentAmountFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999977', NULL, NULL, 'rentAmountUnit', 'MONTH_UNIT', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999975', NULL, NULL, 'rentAmountFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999975', NULL, NULL, 'rentAmountUnit', 'MONTH_UNIT', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999976', NULL, NULL, 'rentAmountFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999976', NULL, NULL, 'rentAmountUnit', 'MONTH_UNIT', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999974', NULL, NULL, 'rentAmountFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999974', NULL, NULL, 'rentAmountUnit', 'MONTH_UNIT', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999993', NULL, NULL, 'rentAmountFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999993', NULL, NULL, 'rentAmountUnit', 'MONTH_UNIT', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999973', NULL, NULL, 'rentAmountFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999973', NULL, NULL, 'rentAmountUnit', 'MONTH_UNIT', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999970', NULL, NULL, 'rentAmountFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999970', NULL, NULL, 'rentAmountUnit', 'MONTH_UNIT', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999991', NULL, NULL, 'rentAmountFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999991', NULL, NULL, 'rentAmountUnit', 'MONTH_UNIT', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999969', NULL, NULL, 'rentAmountFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999969', NULL, NULL, 'rentAmountUnit', 'MONTH_UNIT', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999965', NULL, NULL, 'rentAmountFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999965', NULL, NULL, 'rentAmountUnit', 'MONTH_UNIT', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999967', NULL, NULL, 'rentAmountFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999967', NULL, NULL, 'rentAmountUnit', 'MONTH_UNIT', NULL, NULL);

INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ((@id := @id + 1), '999985', NULL, NULL, 'issuingLeaseFlag', '1', NULL, NULL);

-- janson 20171025
SET @max_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@max_id := @max_id + 1), '50800', '0', '10041', '', '0', now());

-- 招商 add by xiongying20171026
set @eh_locale_templates_id = (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
VALUES ((@eh_locale_templates_id:=@eh_locale_templates_id +1 ), 'customer.tracking', '1', 'zh_CN', '跟进方式', '电话', '0');

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
VALUES ((@eh_locale_templates_id:=@eh_locale_templates_id +1 ), 'customer.tracking', '2', 'zh_CN', '跟进方式', '短信', '0');

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
VALUES ((@eh_locale_templates_id:=@eh_locale_templates_id +1 ), 'customer.tracking', '3', 'zh_CN', '跟进方式', '邮件', '0');

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
VALUES ((@eh_locale_templates_id:=@eh_locale_templates_id +1 ), 'customer.tracking', '4', 'zh_CN', '跟进方式', '其他', '0');


INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
VALUES ((@eh_locale_templates_id:=@eh_locale_templates_id +1 ), 'customer.tracking', '10', 'zh_CN', '客户事件', '新增客户', '0');

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
VALUES ((@eh_locale_templates_id:=@eh_locale_templates_id +1 ), 'customer.tracking', '20', 'zh_CN', '客户事件', '删除客户', '0');

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
VALUES ((@eh_locale_templates_id:=@eh_locale_templates_id +1 ), 'customer.tracking', '30', 'zh_CN', '客户事件', '修改${display}:由${oldData}更改为${newData}', '0');


INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
 VALUES ((@eh_locale_templates_id:=@eh_locale_templates_id +1 ), 'tracking.notification', '1', 'zh_CN', '计划开始提醒', '【跟进计划】${customerName}${taskName}将于${time}开始。', '0');
 
-- fix 17566 add by xiongying20171026
set @id = (select MAX(id) FROM eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES
	((@id := @id + 1), 'customer', '10018', 'zh_CN', '有合同的客户不能删除');