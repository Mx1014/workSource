-- 因为 5.7.0对模块和菜单重构了，这里要重新执行模块了菜单的sql。

-- 客户端处理方式
-- 默认所有是navtive应用
update eh_service_modules set client_handler_type = 0;
-- 内部链接
update eh_service_modules set client_handler_type = 2 WHERE id in (41400, 20400, 21200, 32500);
-- 离线包
update eh_service_modules set client_handler_type = 3 WHERE id in (49100, 20800, 20600);


-- 设置应用类型
-- 默认为园区模块
UPDATE eh_service_modules set app_type = 1;
-- 设置oa模块
UPDATE eh_service_modules set app_type = 0 WHERE id in (50100,  50300, 50500, 50400, 52000, 50600, 54000, 51300, 51400, 55000, 57000, 60100, 60200, 60210, 13000, 20650, 20830, 41020, 50700, 41410, 41420, 41430, 41400);
-- 更新应用信息
UPDATE eh_service_module_apps a set a.app_type = IFNULL((SELECT b.app_type from eh_service_modules b where b.id = a.module_id), 1);


-- 增加应用、项目和公司菜单  (仅在标准版执行)
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15010000', '左邻标准版管理', '15000000', NULL, NULL, '1', '2', '/15000000/15010000', 'zuolin', '20', NULL, '2', 'system', 'classify', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15040000', '项目管理', '15010000', NULL, 'project-management', '1', '2', '/15000000/15010000/15040000', 'zuolin', '10', NULL, '3', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15030000', '应用管理', '15010000', NULL, 'application-management', '1', '2', '/15000000/15010000/15030000', 'zuolin', '20', NULL, '3', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15025000', '应用入口', '15010000', NULL, 'servicemodule-entry', '1', '2', '/15000000/15010000/15025000', 'zuolin', '30', NULL, '3', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15050000', '企业管理', '15010000', NULL, 'business-admin', '1', '2', '/15000000/15010000/15050000', 'zuolin', '40', NULL, '3', 'system', 'module', NULL);
DELETE FROM `eh_web_menus` WHERE `id`='15030100';
DELETE FROM `eh_web_menus` WHERE `id`='15030200';
DELETE FROM `eh_web_menus` WHERE `id`='15030300';

-- 公司门禁 员工认证 职级管理 任务管理 设置为org控制  add by yanjun 20180614
UPDATE eh_service_modules SET module_control_type = 'org_control' WHERE id in (41020, 50500, 50300, 13000);
UPDATE eh_service_module_apps SET module_control_type = 'org_control' WHERE module_id in (41020, 50500, 50300, 13000);

-- janson 物品搬迁
UPDATE `eh_service_modules` SET `instance_config`='{"url":"${home.url}/goods-move/build/index.html?ns=2&hideNavigationBar=1&ehnavigatorstyle=0#/home#sign_suffix"}' WHERE `id`='49200';
UPDATE `eh_service_module_apps` SET `instance_config`='{"url":"${home.url}/goods-move/build/index.html?ns=2&hideNavigationBar=1&ehnavigatorstyle=0#/home#sign_suffix"}' WHERE `module_id`='49200';
UPDATE `eh_service_modules` SET `client_handler_type`='2' WHERE `id`='49200';
-- end


-- 增加电商模块，用于运营板块 add by yanjun 201807041725
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `app_type`, `client_handler_type`, `system_app_flag`) VALUES ('92000', '电商', '0', '/92000', '1', '1', '2', '0', '2018-07-04 17:22:11', NULL, NULL, '2018-07-04 17:22:20', '0', '0', '0', '0', NULL, '1', '0', NULL);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `app_type`, `client_handler_type`, `system_app_flag`) VALUES ('92100', '微商城', '92000', '/92000/92100', '1', '2', '2', '0', '2018-07-04 17:23:28', '{\"url\":\"${stat.biz.server.url}zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=${stat.biz.server.url}nar/biz/web/app/user/index.html?clientrecommend=1#/recommend?_k=zlbiz#sign_suffix\"}', NULL, '2018-07-04 17:23:33', '0', '0', '0', '0', NULL, '1', '2', NULL);


-- 更新企业客户管理模块
UPDATE eh_service_modules set client_handler_type = 2 where id = 21100;


-- 园区入驻从“敬请期待”改为“上线”
UPDATE eh_service_modules set client_handler_type = 0, instance_config = '{}' WHERE id = 40100;
UPDATE eh_service_module_apps set instance_config = '{}' WHERE module_id = 40100;


-- PC工作台入口分类基础数据
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('476', '企业办公', '0', '4', NULL, '1', '0');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('477', '资产管理', '0', '4', NULL, '2', '0');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('478', '物业服务', '0', '4', NULL, '3', '0');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('479', '园区运营', '0', '4', NULL, '4', '0');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('480', '服务联盟', '0', '4', NULL, '5', '0');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('3', 'OA管理', '476', '4', NULL, '1', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('4', 'HR管理', '476', '4', NULL, '2', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('8', '基础数据管理', '477', '4', NULL, '1', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('10', '招商与租赁管理', '477', '4', NULL, '2', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('14', '物业管理', '478', '4', NULL, '1', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('6', '信息发布', '479', '4', NULL, '1', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('7', '社群运营', '479', '4', NULL, '2', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('11', '客服管理', '479', '4', NULL, '3', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('15', '运营统计', '479', '4', NULL, '4', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('5', 'ERP', '476', '4', NULL, '3', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('13', '服务联盟', '480', '4', NULL, '1', '1');
