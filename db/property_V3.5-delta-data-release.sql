-- AUTHOR: tangcen 2018年11月15日
-- REMARK: 添加房源招商的权限
SET @id = (select max(id) from eh_service_module_privileges);
-- 新增房源权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38101, '新增房源', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38101, 0, '楼宇资产管理 新增房源', '楼宇资产管理 业务模块权限', NULL);
-- 修改房源权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38102, '修改房源', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38102, 0, '楼宇资产管理 修改房源', '楼宇资产管理 业务模块权限', NULL);
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
-- 创建预定记录
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38105, '创建预定记录', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38105, 0, '楼宇资产管理 创建预定记录', '楼宇资产管理 业务模块权限', NULL);
-- 更新预定记录
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38106, '更新预定记录', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38106, 0, '楼宇资产管理 更新预定记录', '楼宇资产管理 业务模块权限', NULL);
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
-- 新建楼宇
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38110, '新建楼宇', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38110, 0, '楼宇资产管理 新建楼宇', '楼宇资产管理 业务模块权限', NULL);
-- 更新楼宇
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38111, '更新楼宇', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38111, 0, '楼宇资产管理 更新楼宇', '楼宇资产管理 业务模块权限', NULL);
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
-- 更新园区信息
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38114, '更新园区信息', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38114, 0, '楼宇资产管理 更新园区信息', '楼宇资产管理 业务模块权限', NULL);
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
-- 修改授权价
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38120, '修改授权价', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38120, 0, '楼宇资产管理 修改授权价', '楼宇资产管理 业务模块权限', NULL);
-- 删除授权价
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38121, '删除授权价', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38121, 0, '楼宇资产管理 删除授权价', '楼宇资产管理 业务模块权限', NULL);
-- 查看房源相关信息
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38122, '查看房源相关信息', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38122, 0, '楼宇资产管理 查看房源相关信息', '楼宇资产管理 业务模块权限', NULL);
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
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ('address.tracking', '1', 'zh_CN', '创建房源', '创建房源', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ('address.tracking', '2', 'zh_CN', '删除房源', '删除房源', '0');	
	
	
	