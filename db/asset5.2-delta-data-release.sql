-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 杨崇鑫
-- REMARK: 新增对公转账菜单
set @module_id=20500; -- 模块Id 41900（家声分配的）
set @data_type='public-transfer';-- 前端发给你的页面跳转链接
set @module_parent_id=110000; -- select * from eh_service_modules where name='租赁管理'
set @menu_parent_parent_id=16000000;-- select * from eh_web_menus where name='业务模块管理' and parent_id=0
set @menu_parent_id=16050000; -- select * from eh_web_menus where name='物业管控模块'
set @path=CONCAT("/",@module_parent_id,"/",@module_id); -- 如：/110000/20500
set @bill_module_id=(select max(id) + 1 from eh_service_modules);-- 账单管理标签页的id
set @bill_path=CONCAT(@path,"/",@bill_module_id); -- 如：/110000/20500/101
set @order_module_id=(select max(id) + 2 from eh_service_modules);-- 交易明细标签页的id
set @order_path=CONCAT(@path,"/",@order_module_id); -- 如：/110000/20500/102
set @menu_id=(select max(id) + 1 from eh_web_menus);-- 如：16032200
set @ment_path=CONCAT("/",@menu_parent_parent_id,"/",@menu_parent_id,"/",@menu_id);-- 如：/16000000/16030000/16032200
-- 新增模块 eh_service_modules
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `update_time`, `operator_uid`, `creator_uid`, `multiple_flag`, `module_control_type`) 
VALUES (@module_id, '对公转账', @module_parent_id, @path, '1', '2', '2', '0', UTC_TIMESTAMP(), UTC_TIMESTAMP(), '0', '0', '0', 'community_control');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) 
VALUES (@bill_module_id, '账单管理', @module_id, @bill_path, '1', '3', '2', '0', UTC_TIMESTAMP(), '', NULL, UTC_TIMESTAMP(), '0', '0', '', '0', 'community_control');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) 
VALUES (@order_module_id, '交易明细', @module_id, @order_path, '1', '3', '2', '0', UTC_TIMESTAMP(), '', NULL, UTC_TIMESTAMP(), '0', '0', '', '0', 'community_control');
-- 新增模块菜单 eh_web_menus
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `data_type`, `leaf_flag`, `path`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) 
VALUES (@menu_id, '对公转账', @menu_parent_id, @data_type, '1', @ment_path, '22', @module_id, '3', 'system', 'module');

-- --------------------- SECTION END ---------------------------------------------------------


