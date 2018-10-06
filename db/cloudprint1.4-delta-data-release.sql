-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境

-- AUTHOR: st.zheng
-- REMARK: 增加商户管理模块及菜单
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES ('210000', '商户管理', '170000', '/200/170000/210000', '1', '3', '2', '110', '2018-03-19 17:52:57', NULL, NULL, '2018-03-19 17:53:11', '0', '0', '0', '0', 'community_control', '1', '1', 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('79820000', '商户管理', '16400000', NULL, 'business-management', '1', '2', '/16000000/16400000/79820000', 'zuolin', '120', '210000', '3', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('79830000', '商户管理', '56000000', NULL, 'business-management', '1', '2', '/40000040/56000000/79830000', 'park', '120', '210000', '3', 'system', 'module', '2');

-- AUTHOR: 缪洲 20180930
-- REMARK: issue-34780 增加企业支付授权页面
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES (79800000, '企业支付授权', 16300000, NULL, 'payment-privileges', 1, 2, '/16000000/16300000/79800000', 'zuolin', 8, 200000, 3, 'system', 'module', NULL, 1);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES (79810000, '企业支付授权', 55000000, NULL, 'payment-privileges', 1, 2, '/40000040/55000000/79810000', 'park', 2, 200000, 3, 'system', 'module', NULL, 1);
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`, `app_type`, `client_handler_type`, `system_app_flag`, `icon_uri`) VALUES (200000, '企业支付授权', 140000, '/200/140000', 1, 3, 2, 10, '2018-09-26 16:51:46', '{}', 13, '2018-09-26 16:51:46', 0, 0, '0', NULL, 'community_control', 1, 1, 'module', NULL, 0, NULL, NULL);

-- AUTHOR: liangqishi
-- REMARK: 把原来统一订单的配置项前缀gorder改为prmt 20181006
UPDATE `eh_configurations` SET `value`=REPLACE(`value`, 'gorder', 'prmt') WHERE `name`='gorder.server.connect_url';
UPDATE `eh_configurations` SET `name`='prmt.server.connect_url' WHERE `name`='gorder.server.connect_url';
UPDATE `eh_configurations` SET `name`='prmt.server.app_key' WHERE `name`='gorder.server.app_key';
UPDATE `eh_configurations` SET `name`='prmt.server.app_secret' WHERE `name`='gorder.server.app_secret';
UPDATE `eh_configurations` SET `name`='prmt.default.personal_bind_phone' WHERE `name`='gorder.default.personal_bind_phone';
UPDATE `eh_configurations` SET `name`='prmt.system_id' WHERE `name`='gorder.system_id';


-- --------------------- SECTION END ---------------------------------------------------------