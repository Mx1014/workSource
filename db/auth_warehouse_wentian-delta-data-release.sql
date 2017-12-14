-- 仓库管理 权限细化脚の本 by wentian
-- imitate service since we don't have an entry at app side
set @reflect_id = (select MAX(`id`) from `eh_reflection_service_module_apps`);
set @app_id = (select MAX(`active_app_id`) from `eh_reflection_service_module_apps`);
INSERT INTO `eh_reflection_service_module_apps`
(`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`)
VALUES
(@reflect_id:=@reflect_id+1, @app_id:=@app_id+1, '999992', '仓库管理', '21000', NULL, '2', NULL, NULL, NOW(), 'community-control', '0', '', '', '21000');

-- privilege
set @module_id = 21010;
set @p_id = 210001001;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '仓库维护 查找', '仓库维护 查找', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '查看', '0', NOW());

set @module_id = 21010;
set @p_id = 210001002;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '仓库维护 新增，编辑，删除', '仓库维护 新增，编辑，删除', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '新增，编辑，删除', '0', NOW());

set @module_id = 21020;
set @p_id = 210001003;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '物品维护 物品分类新增、编辑、删除、导入', '物品维护 物品分类新增、编辑、删除、导入', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '物品分类新增、编辑、删除、导入', '0', NOW());

set @module_id = 21020;
set @p_id = 210001004;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '物品维护 物品信息新增、编辑、删除、导入', '物品维护 物品信息新增、编辑、删除、导入', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '物品信息新增、编辑、删除、导入', '0', NOW());

set @module_id = 21030;
set @p_id = 210001005;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '库存维护 库存查找', '库存维护 库存查找', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '库存查询', '0', NOW());

set @module_id = 21030;
set @p_id = 210001006;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '库存维护 入库', '库存维护 入库', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '入库', '0', NOW());

set @module_id = 21030;
set @p_id = 210001007;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '库存维护 日志查找', '库存维护 日志查找', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '日志查找', '0', NOW());

set @module_id = 21030;
set @p_id = 210001008;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '库存维护 日志导出', '库存维护 日志导出', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '日志导出', '0', NOW());

set @module_id = 21040;
set @p_id = 210001009;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '领用管理 领用查找', '领用管理 领用查找', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '领用查找', '0', NOW());

set @module_id = 21040;
set @p_id = 210001010;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '领用管理 领用申请', '领用管理 领用申请', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '领用申请', '0', NOW());

set @module_id = 21050;
set @p_id = 210001011;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '参数配置 工作流配置', '参数配置 工作流配置', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '工作流配置', '0', NOW());

set @module_id = 21050;
set @p_id = 210001012;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '参数配置 参数配置', '参数配置 参数配置', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '参数配置', '0', NOW());


set @module_id = 21040;
set @p_id = 210001013;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '领用管理 入库', '领用管理 入库', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '入库', '0', NOW());

-- end of wentian's script, farewell