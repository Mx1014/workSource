-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: tangcen
-- REMARK:添加房源招商、招商客户管理模块
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES ('150010', '房源招商', '110000', '/200/110000/150010', '1', '3', '2', '0', NOW(), '{\"url\":\"${home.url}/park-entry-web/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0#/home/#sign_suffix\"}', '14', NOW(), '0', '0', '0', '0', 'community_control', '1', '1', 'module');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES ('150020', '招商客户管理', '110000', '/200/110000/150020', '1', '3', '2', '0', NOW(), '{\"url\":\"${home.url}/rentCustomer/build/index.html?hideNavigationBar=1#/home#sign_suffix\"}', '14', NOW(), '0', '0', '0', '0', 'community_control', '1', '1', 'module');

-- -- AUTHOR: tangcen
-- REMARK:添加房源招商、招商客户管理的web menu
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('79500000', '房源招商', '16210000', NULL, 'investment-ad', '1', '2', '/16000000/16210000/79500000', 'zuolin', '80', '150010', '3', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('79600000', '房源招商', '16210000', NULL, 'investment-ad', '1', '2', '/16000000/16210000/79600000', 'park', '80', '150010', '3', 'system', 'module', NULL);

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('79710000', '招商客户管理', '16210000', NULL, 'invited-customer', '1', '2', '/16000000/16210000/79710000', 'zuolin', '80', '150020', '3', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('79720000', '招商客户管理', '16210000', NULL, 'invited-customer', '1', '2', '/16000000/16210000/79720000', 'park', '80', '150020', '3', 'system', 'module', NULL);

-- --------------------- SECTION END ---------------------------------------------------------
