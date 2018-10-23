-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 杨崇鑫 20181023
-- REMARK: 新增“资产报表中心”模块和菜单
set @module_id=230000; -- 模块Id（运维分配的）
set @data_type='property-statistic';-- 前端发给你的页面跳转链接
-- 新增模块 eh_service_modules
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `app_type`, `client_handler_type`, `system_app_flag`, `icon_uri`, `access_control_type`, `menu_auth_flag`, `category`) 
VALUES (230000, '资产报表中心', 130000, '/200/130000/230000', 1, 3, 2, 10, UTC_TIMESTAMP(), NULL, NULL, UTC_TIMESTAMP(), 0, 0, '0', 0, 'unlimit_control', 1, 0, NULL, NULL, 1, 1, 'module');
-- 新增模块菜单 eh_web_menus
-- 左邻后台:zuolin、园区：park
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) 
	VALUES (79850000, '资产报表中心', 17000000, NULL, @data_type, 1, 2, '/27000000/17000000/79850000', 'zuolin', 10, 41300, 3, 'system', 'module', NULL, 1);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) 
	VALUES (79860000, '资产报表中心', 49000000, NULL, @data_type, 1, 2, '/40000040/49000000/79860000', 'park', 10, 41300, 3, 'system', 'module', 2, 1);


-- --------------------- SECTION END ---------------------------------------------------------


