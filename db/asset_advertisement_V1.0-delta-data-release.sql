-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: tangcen
-- REMARK: 添加招商申请表单的默认字段
SET @general_form_templates_id = (SELECT MAX(id) FROM `eh_general_form_templates`);
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES (@general_form_templates_id:=@general_form_templates_id+1, '0', '0', '0', 'EhOrganizations', '150000', 'business_invitation', '招商租赁', '0', 'DEFAULT_JSON', '[{\n	\"dynamicFlag\": 0,\n	\"fieldDesc\": \"用户姓名\",\n	\"fieldDisplayName\": \"用户姓名\",\n	\"fieldExtra\": \"{}\",\n	\"fieldName\": \"USER_NAME\",\n	\"fieldType\": \"SINGLE_LINE_TEXT\",\n	\"renderType\": \"DEFAULT\",\n	\"requiredFlag\": 1,\n	\"validatorType\": \"TEXT_LIMIT\",\n	\"visibleType\": \"EDITABLE\",\n	\"filterFlag\": 1\n},\n{\n	\"dynamicFlag\": 0,\n	\"fieldDesc\": \"手机号码\",\n	\"fieldDisplayName\": \"手机号码\",\n	\"fieldExtra\": \"{}\",\n	\"fieldName\": \"USER_PHONE\",\n	\"fieldType\": \"INTEGER_TEXT\",\n	\"renderType\": \"DEFAULT\",\n	\"requiredFlag\": 1,\n	\"validatorType\": \"TEXT_LIMIT\",\n	\"visibleType\": \"EDITABLE\",\n	\"filterFlag\": 1\n},\n{\n	\"dynamicFlag\": 0,\n	\"fieldDesc\": \"承租方\",\n	\"fieldDisplayName\": \"承租方\",\n	\"fieldExtra\": \"{}\",\n	\"fieldName\": \"ENTERPRISE_NAME\",\n	\"fieldType\": \"SINGLE_LINE_TEXT\",\n	\"renderType\": \"DEFAULT\",\n	\"requiredFlag\": 1,\n	\"validatorType\": \"TEXT_LIMIT\",\n	\"visibleType\": \"EDITABLE\",\n	\"filterFlag\": 1\n}]', '1', '1', NULL, NOW());

-- AUTHOR: tangcen
-- REMARK: 添加房源招商的权限
SET @id = (select max(id) from eh_service_module_privileges);
-- 发布招商信息权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150000, 0, 150101, '发布招商信息', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150101, 0, '房源招商 发布招商信息权限', '招商管理 业务模块权限', NULL);
-- 编辑招商信息权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150000, 0, 150102, '编辑招商信息', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150102, 0, '房源招商 编辑招商信息权限', '招商管理 业务模块权限', NULL);
-- 删除招商信息权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150000, 0, 150103, '删除招商信息', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150103, 0, '房源招商 删除招商信息权限', '招商管理 业务模块权限', NULL);
-- 导出招商信息权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150000, 0, 150104, '导出招商信息', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150104, 0, '房源招商 导出招商信息权限', '招商管理 业务模块权限', NULL);
-- 排序权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150000, 0, 150105, '排序', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150105, 0, '房源招商 排序权限', '招商管理 业务模块权限', NULL);
-- 导出申请记录权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150000, 0, 150106, '导出申请记录', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150106, 0, '房源招商 导出申请记录权限', '招商管理 业务模块权限', NULL);
-- 转为意向客户权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150000, 0, 150107, '转为意向客户', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150107, 0, '房源招商 转为意向客户权限', '招商管理 业务模块权限', NULL);
-- 删除申请记录权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150000, 0, 150108, '删除申请记录', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150108, 0, '房源招商 删除申请记录权限', '招商管理 业务模块权限', NULL);


-- --------------------- SECTION END ---------------------------------------------------------