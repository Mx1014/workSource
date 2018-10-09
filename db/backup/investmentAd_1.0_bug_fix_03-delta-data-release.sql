-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: tangcen
-- REMARK: 添加权限web菜单
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES ('150010', '房源招商', '150000', '/200/110000/150000/150010', '1', '4', '2', '0', NOW(), '{"url":"${home.url}/park-entry-web/build/index.html?hideNavigationBar=1#/home#sign_suffix"}', 14, NOW(), '0', '0', '0', '0', 'community_control', '1', '1', 'subModule');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES ('150020', '客户管理', '150000', '/200/110000/150000/150020', '1', '4', '2', '0', NOW(), '{"url":"${home.url}/rentCustomer/build/index.html?hideNavigationBar=1#/home#sign_suffix"}', 14, NOW(), '0', '0', '0', '0', 'community_control', '1', '1', 'subModule');

-- AUTHOR: tangcen
-- REMARK: 添加房源招商的权限
SET @id = (select max(id) from eh_service_module_privileges);
-- 发布招商信息权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150010, 0, 150101, '发布招商信息', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150101, 0, '房源招商 发布招商信息权限', '招商管理 业务模块权限', NULL);
-- 编辑招商信息权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150010, 0, 150102, '编辑招商信息', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150102, 0, '房源招商 编辑招商信息权限', '招商管理 业务模块权限', NULL);
-- 删除招商信息权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150010, 0, 150103, '删除招商信息', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150103, 0, '房源招商 删除招商信息权限', '招商管理 业务模块权限', NULL);
-- 导出招商信息权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150010, 0, 150104, '导出招商信息', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150104, 0, '房源招商 导出招商信息权限', '招商管理 业务模块权限', NULL);
-- 排序权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150010, 0, 150105, '排序', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150105, 0, '房源招商 排序权限', '招商管理 业务模块权限', NULL);
-- 导出申请记录权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150010, 0, 150106, '导出申请记录', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150106, 0, '房源招商 导出申请记录权限', '招商管理 业务模块权限', NULL);
-- 转为意向客户权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150010, 0, 150107, '转为意向客户', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150107, 0, '房源招商 转为意向客户权限', '招商管理 业务模块权限', NULL);
-- 删除申请记录权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150010, 0, 150108, '删除申请记录', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150108, 0, '房源招商 删除申请记录权限', '招商管理 业务模块权限', NULL);
-- 一键转为意向客户权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150010, 0, 150109, '一键转为意向客户', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150109, 0, '房源招商 一键转为意向客户权限', '招商管理 业务模块权限', NULL);

SET @id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150020, 0, 150001, '查看客户权限', 0, SYSDATE());
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150020, 0, 150002, '创建客户权限', 0, SYSDATE());
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150020, 0, 150003, '编辑客户权限', 0, SYSDATE());
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150020, 0, 150004, '删除客户权限', 0, SYSDATE());
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150020, 0, 150005, '一键转为租客权限', 0, SYSDATE());
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150020, 0, 150006, '签约权限', 0, SYSDATE());
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150020, 0, 150007, '续约权限', 0, SYSDATE());
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150020, 0, 150008, '导入权限', 0, SYSDATE());
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150020, 0, 150009, '导出权限', 0, SYSDATE());

set @item_id = (select max(id) from `eh_var_field_group_scopes`);
INSERT INTO `eh_var_field_group_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES (((@item_id:=@item_id+1)), 0, 'asset_management', 1001, '租客管理', 1, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_group_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES (((@item_id:=@item_id+1)), 0, 'asset_management', 1003, '合同管理', 1, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_group_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES (((@item_id:=@item_id+1)), 0, 'asset_management', 1004, '账单管理', 1, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_group_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES (((@item_id:=@item_id+1)), 0, 'asset_management', 1005, '活动记录', 1, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_group_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES (((@item_id:=@item_id+1)), 0, 'asset_management', 1007, '服务信息', 1, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_group_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES (((@item_id:=@item_id+1)), 0, 'asset_management', 1008, '历史房源', 1, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_group_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES (((@item_id:=@item_id+1)), 0, 'asset_management', 1009, '附件', 1, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);
-- --------------------- SECTION END ---------------------------------------------------------
