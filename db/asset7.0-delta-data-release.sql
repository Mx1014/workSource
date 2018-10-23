-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境

-- AUTHOR:唐岑2018年10月23日14:29:41
-- REMARK:添加资产报表定时配置
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('schedule.property.task.time', '0 30 2 * * ?', '资产报表定时任务', '0', NULL, '1');

-- AUTHOR: 杨崇鑫 20181023
-- REMARK: 新增“资产报表中心”菜单
set @module_id=20500; -- 模块Id（运维分配的）
set @data_type='property-statistic';-- 前端发给你的页面跳转链接
set @module_parent_parent_id=100;-- select * from eh_service_modules where name='企业访客'
set @module_parent_id=50000; -- select * from eh_service_modules where name='物业缴费'
set @path=CONCAT("/", @module_parent_parent_id,"/",@module_parent_id,"/",@module_id); -- 如：/100/50000/52100
set @bill_module_id=(select max(id) + 1 from eh_service_modules);-- 账单管理标签页的id
set @bill_path=CONCAT(@path,"/",@bill_module_id); -- 如：/100/50000/52100/101
set @order_module_id=(select max(id) + 2 from eh_service_modules);-- 交易明细标签页的id
set @order_path=CONCAT(@path,"/",@order_module_id); -- 如：/100/50000/52100/102
-- 左邻后台菜单路径（三层）
set @zuolin_menu_id=(select max(id) + 1 from eh_web_menus);-- 如：16032200
set @zuolin_menu_parent_parent_id=23000000;-- select * from eh_web_menus where name='企业办公业务'
set @zuolin_menu_parent_id=23010000; -- select * from eh_web_menus where name='OA管理' and type='zuolin'
set @zuolin_menu_path=CONCAT("/",@zuolin_menu_parent_parent_id,"/",@zuolin_menu_parent_id,"/",@zuolin_menu_id);-- 如：/23000000/23010000/XXXX
-- 园区后台菜单路径（三层）
-- set @park_menu_id=(select max(id) + 2 from eh_web_menus);-- 如：16032200
-- set @park_menu_parent_parent_id=40000010;-- select * from eh_web_menus where name='企业办公' and type='park'
-- set @park_menu_parent_id=53000000; -- select * from eh_web_menus where name='OA管理' and type='park'
-- set @park_menu_path=CONCAT("/",@park_menu_parent_parent_id,"/",@park_menu_parent_id,"/",@park_menu_id);-- 如：/40000010/53000000/XXX
-- 普通公司后台菜单路径（三层）
set @organization_menu_id=(select max(id) + 3 from eh_web_menus);-- 如：16032200
set @organization_menu_parent_parent_id=70000010;-- select * from eh_web_menus where name='企业办公' and type='organization'
set @organization_menu_parent_id=77000000; -- select * from eh_web_menus where name='OA管理' and type='organization'
set @organization_menu_path=CONCAT("/",@organization_menu_parent_parent_id,"/",@organization_menu_parent_id,"/",@organization_menu_id);-- 如：/40000010/53000000/XXX

-- 新增模块 eh_service_modules
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `update_time`, `operator_uid`, `creator_uid`, `multiple_flag`, `module_control_type`,`category`) 
VALUES (@module_id, '企业账单', @module_parent_id, @path, '1', '3', '2', '0', UTC_TIMESTAMP(), UTC_TIMESTAMP(), '0', '0', '0', 'community_control', 'module');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `category`) 
VALUES (@bill_module_id, '账单管理', @module_id, @bill_path, '1', '4', '2', '0', UTC_TIMESTAMP(), '', NULL, UTC_TIMESTAMP(), '0', '0', '', '0', 'community_control','subModule');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `category`) 
VALUES (@order_module_id, '交易明细', @module_id, @order_path, '1', '4', '2', '0', UTC_TIMESTAMP(), '', NULL, UTC_TIMESTAMP(), '0', '0', '', '0', 'community_control','subModule');
-- 新增模块菜单 eh_web_menus
-- 左邻后台（levle=3)
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) 
VALUES (@zuolin_menu_id, '企业账单', @zuolin_menu_parent_id, NULL, @data_type, 1, 2, @zuolin_menu_path, 'zuolin', 220, @module_id, 3, 'system', 'module', NULL);
-- 园区（levle=3)
-- INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) 
-- VALUES (@park_menu_id, '企业账单', @park_menu_parent_id, NULL, @data_type, 1, 2, @park_menu_path, 'park', 220, @module_id, 3, 'system', 'module', NULL);
-- 普通公司（levle=3)
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) 
VALUES (@organization_menu_id, '企业账单', @organization_menu_parent_id, NULL, @data_type, 1, 2, @organization_menu_path, 'organization', 220, @module_id, 3, 'system', 'module', NULL);

-- --------------------- SECTION END ---------------------------------------------------------
