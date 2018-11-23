-- AUTHOR: tangcen 2018年11月15日
-- REMARK: 添加房源招商的权限
SET @id = (select max(id) from eh_service_module_privileges);
-- 新增房源权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38101, '新增房源', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38101, 0, '楼宇资产管理 新增房源', '楼宇资产管理 业务模块权限', NULL);
-- 编辑房源权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38102, '编辑房源', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38102, 0, '楼宇资产管理 编辑房源', '楼宇资产管理 业务模块权限', NULL);
-- 删除房源权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38103, '删除房源', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38103, 0, '楼宇资产管理 删除房源', '楼宇资产管理 业务模块权限', NULL);
-- 查看所有的预定记录
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38104, '查看所有的预定记录', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38104, 0, '楼宇资产管理 查看所有的预定记录', '楼宇资产管理 业务模块权限', NULL);
-- 新增预定记录
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38105, '新增预定记录', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38105, 0, '楼宇资产管理 新增预定记录', '楼宇资产管理 业务模块权限', NULL);
-- 编辑预定记录
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38106, '编辑预定记录', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38106, 0, '楼宇资产管理 编辑预定记录', '楼宇资产管理 业务模块权限', NULL);
-- 删除预定记录
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38107, '删除预定记录', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38107, 0, '楼宇资产管理 删除预定记录', '楼宇资产管理 业务模块权限', NULL);
-- 取消预定记录
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38108, '取消预定记录', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38108, 0, '楼宇资产管理 取消预定记录', '楼宇资产管理 业务模块权限', NULL);	
-- 删除楼宇
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38109, '删除楼宇', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38109, 0, '楼宇资产管理 删除楼宇', '楼宇资产管理 业务模块权限', NULL);	
-- 新增楼宇
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38110, '新增楼宇', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38110, 0, '楼宇资产管理 新增楼宇', '楼宇资产管理 业务模块权限', NULL);
-- 编辑楼宇
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38111, '编辑楼宇', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38111, 0, '楼宇资产管理 编辑楼宇', '楼宇资产管理 业务模块权限', NULL);
-- 导入楼宇
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38112, '导入楼宇', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38112, 0, '楼宇资产管理 导入楼宇', '楼宇资产管理 业务模块权限', NULL);	
-- 导出楼宇
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38113, '导出楼宇', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38113, 0, '楼宇资产管理 导出楼宇', '楼宇资产管理 业务模块权限', NULL);
-- 编辑园区信息
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38114, '编辑园区信息', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38114, 0, '楼宇资产管理 编辑园区信息', '楼宇资产管理 业务模块权限', NULL);
-- 楼宇排序
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38115, '楼宇排序', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38115, 0, '楼宇资产管理 楼宇排序', '楼宇资产管理 业务模块权限', NULL);
-- 拆分房源
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38116, '拆分房源', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38116, 0, '楼宇资产管理 拆分房源', '楼宇资产管理 业务模块权限', NULL);
-- 合并房源
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38117, '合并房源', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38117, 0, '楼宇资产管理 合并房源', '楼宇资产管理 业务模块权限', NULL);
-- 查看房源授权价记录
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38118, '查看房源授权价记录', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38118, 0, '楼宇资产管理 查看房源授权价记录', '楼宇资产管理 业务模块权限', NULL);
-- 新增授权价
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38119, '新增授权价', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38119, 0, '楼宇资产管理 新增授权价', '楼宇资产管理 业务模块权限', NULL);
-- 编辑授权价
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38120, '编辑授权价', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38120, 0, '楼宇资产管理 编辑授权价', '楼宇资产管理 业务模块权限', NULL);
-- 删除授权价
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38121, '删除授权价', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38121, 0, '楼宇资产管理 删除授权价', '楼宇资产管理 业务模块权限', NULL);
-- 查看房源
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38122, '查看房源', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38122, 0, '楼宇资产管理 查看房源', '楼宇资产管理 业务模块权限', NULL);
-- 批量导入授权价
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38123, '批量导入授权价', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38123, 0, '楼宇资产管理 批量导入授权价', '楼宇资产管理 业务模块权限', NULL);
-- 上传房源附件
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38124, '上传房源附件', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38124, 0, '楼宇资产管理 上传房源附件', '楼宇资产管理 业务模块权限', NULL);
-- 删除房源附件
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38125, '删除房源附件', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38125, 0, '楼宇资产管理 删除房源附件', '楼宇资产管理 业务模块权限', NULL);
-- 按房源导出
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38126, '按房源导出', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38126, 0, '楼宇资产管理 按房源导出', '楼宇资产管理 业务模块权限', NULL);
	
	
-- AUTHOR: tangcen 2018年11月15日
-- REMARK: 添加房源日志模板
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '1', 'zh_CN', '房源事件', '创建房源', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '2', 'zh_CN', '房源事件', '删除房源', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '3', 'zh_CN', '房源事件', '修改${display}:由${oldData}更改为${newData}	', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '4', 'zh_CN', '房源拆分、合并计划事件', '创建房源合并计划，生效时间为${dateBegin}', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '5', 'zh_CN', '房源拆分、合并计划事件', '创建房源拆分计划，生效时间为${dateBegin}', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '6', 'zh_CN', '房源拆分、合并计划事件', '删除房源合并计划', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '7', 'zh_CN', '房源拆分、合并计划事件', '删除房源拆分计划', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '8', 'zh_CN', '房源拆分、合并计划事件', '修改${display}:由${oldData}更改为${newData}', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '9', 'zh_CN', '房源拆分、合并计划事件', '修改${display}:由${oldData}更改为${newData}', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '10', 'zh_CN', '房源授权价事件', '创建房源授权价', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '11', 'zh_CN', '房源授权价事件', '删除房源授权价', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '12', 'zh_CN', '房源授权价事件', '修改${display}:由${oldData}更改为${newData}', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '13', 'zh_CN', '房源预定事件', '创建房源预定计划', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '14', 'zh_CN', '房源预定事件', '删除房源预定计划', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '15', 'zh_CN', '房源预定事件', '取消房源预定计划', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '16', 'zh_CN', '房源预定事件', '修改${display}:由${oldData}更改为${newData}', '0');	

-- AUTHOR: tangcen 2018年11月20日
-- REMARK: 添加房源日志tab
INSERT INTO `eh_var_field_groups` (`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) 
	VALUES (1010, 'asset_management', '0', '/1010', '房源日志', '', '0', NULL, '2', '1', NOW(), NULL, NULL);
	
-- AUTHOR: tangcen
-- REMARK: 资产管理的管理配置页面添加默认的tab卡
set @item_id = (select max(id) from `eh_var_field_group_scopes`);
INSERT INTO `eh_var_field_group_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) 
	VALUES (((@item_id:=@item_id+1)), 0, 'asset_management', 1010, '房源日志', 1, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);