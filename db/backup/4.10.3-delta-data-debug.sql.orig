-- 园区入驻 add by sw 20171023
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ('1', '1000000', NULL, NULL, 'renewFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ('3', '1000000', NULL, NULL, 'rentAmountFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ('4', '1000000', NULL, NULL, 'rentAmountUnit', 'MONTH_UNIT', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ('5', '1000000', NULL, NULL, 'issuingLeaseFlag', '1', NULL, NULL);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `owner_type`, `owner_id`, `config_name`, `config_value`, `create_time`, `creator_uid`)
	VALUES ('6', '1000000', NULL, NULL, 'buildingIntroduceFlag', '1', NULL, NULL);

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


-- 招商测试入口 add by xiongying20171024
set @item_id = (select MAX(id) FROM eh_launch_pad_items);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
    VALUES ((@item_id := @item_id + 1), 999992, 0, 0, 0, '/home', 'Bizs', '招商', '招商', 'cs://1/image/aW1hZ2UvTVRwaE5qVTJNbVZoTW1KaU1qRmlNbVV6TVdabE5UQXdaRGN5TmpkbE5qTmlaQQ', 1, 1, 13, '{\"url\":\"http://alpha.lab.everhomes.com/customer/build/index.html?hideNavigationBar=1#/home#sign_suffix"}', 10, 0, 1, 1, '1', 0, NULL, NULL, NULL, 1, 'pm_admin', 0, 221, NULL, 10, NULL);

-- wentian 支付2.0 beta版本
-- config
set @eh_payment_service_configs_id = (select MAX(id) from `eh_payment_service_configs`);
INSERT INTO `eh_payment_service_configs` (`id`, `name`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_split_rule_id`, `payment_user_type`, `payment_user_id`, `create_time`, `update_time`)
VALUES
(@eh_payment_service_configs_id:=@eh_payment_service_configs_id+1, '张江高科缴费','zjgkrentalcode', '999971', 'EhOrganizations', '1012516', null, null, null, '2', 1037, UTC_TIMESTAMP(), NULL);


-- 支付类型
set @eh_payment_types_id = (select max(id) from `eh_payment_types`);
INSERT INTO `eh_payment_types`
(`id`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_type`, `payment_name`, `payment_logo`, `paymentParams`, `create_time`, `update_time`)
VALUES
(@eh_payment_types_id:=@eh_payment_types_id+1, 'zjgkrentalcode', '999971', 'EhOrganizations', '1012516', null, null, '8', '支付宝', 'cs://1/image/aW1hZ2UvTVRvelpEZ3pZalV6WmpGbFkyRXhNamRoTkdJd04yWTFNR0ZrTnpGaE5ERm1Zdw', '{\"payType\":\"A01\"}', UTC_TIMESTAMP(), NULL);


-- 收款方
set @eh_payment_users_id = (select MAX(id) from `eh_payment_users`);
INSERT INTO `eh_payment_users`
(`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`)
VALUES
(@eh_payment_users_id:=@eh_payment_users_id+1, 'EhOrganizations', '1012516', '2', 1037, UTC_TIMESTAMP());


-- config
set @eh_payment_service_configs_id = (select MAX(id) from `eh_payment_service_configs`);
INSERT INTO `eh_payment_service_configs` (`id`, `name`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_split_rule_id`, `payment_user_type`, `payment_user_id`, `create_time`, `update_time`)
VALUES
(@eh_payment_service_configs_id:=@eh_payment_service_configs_id+1, '左邻物业缴费','wuyeCode', 999992, 'EhOrganizations', '999992', null, null, null, '2', 1038, UTC_TIMESTAMP(), NULL);


-- 支付类型
set @eh_payment_types_id = (select max(id) from `eh_payment_types`);
INSERT INTO `eh_payment_types`
(`id`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_type`, `payment_name`, `payment_logo`, `paymentParams`, `create_time`, `update_time`)
VALUES
(@eh_payment_types_id:=@eh_payment_types_id+1, 'wuyeCode', 999992, 'EhOrganizations', 999992, null, null, '8', '支付宝', 'cs://1/image/aW1hZ2UvTVRvelpEZ3pZalV6WmpGbFkyRXhNamRoTkdJd04yWTFNR0ZrTnpGaE5ERm1Zdw', '{\"payType\":\"A01\"}', UTC_TIMESTAMP(), NULL);


-- 收款方
set @eh_payment_users_id = (select MAX(id) from `eh_payment_users`);
INSERT INTO `eh_payment_users`
(`id`, `owner_type`, `owner_id`, `payment_user_type`, `payment_user_id`, `create_time`)
VALUES
(@eh_payment_users_id:=@eh_payment_users_id+1, 'EhOrganizations', 999992, '2', 1038, UTC_TIMESTAMP());