-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: tangcen
-- REMARK: 添加招商申请表单的默认字段
SET @general_form_templates_id = (SELECT MAX(id) FROM `eh_general_form_templates`);
INSERT INTO `ehcore`.`eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES (@general_form_templates_id:=@general_form_templates_id+1, '0', '0', '0', 'EhOrganizations', '150000', 'business_invitation', '招商租赁', '0', 'DEFAULT_JSON', '[{\r\n	\"dynamicFlag\": 0,\r\n	\"fieldDesc\": \"用户姓名\",\r\n	\"fieldDisplayName\": \"用户姓名\",\r\n	\"fieldExtra\": \"{}\",\r\n	\"fieldName\": \"USER_NAME\",\r\n	\"fieldType\": \"SINGLE_LINE_TEXT\",\r\n	\"remark\": \"系统自动获取APP端登录用户的姓名；\",\r\n	\"disabled\": true,\r\n	\"renderType\": \"DEFAULT\",\r\n	\"requiredFlag\": 1,\r\n	\"validatorType\": \"TEXT_LIMIT\",\r\n	\"visibleType\": \"EDITABLE\",\r\n	\"filterFlag\": 1\r\n},\r\n{\r\n	\"dynamicFlag\": 0,\r\n	\"fieldDesc\": \"手机号码\",\r\n	\"fieldDisplayName\": \"手机号码\",\r\n	\"fieldExtra\": \"{}\",\r\n	\"fieldName\": \"USER_PHONE\",\r\n	\"fieldType\": \"NUMBER_TEXT\",\r\n	\"remark\": \"系统自动获取APP端登录用户的手机号码；\",\r\n	\"disabled\": true,\r\n	\"renderType\": \"DEFAULT\",\r\n	\"requiredFlag\": 1,\r\n	\"validatorType\": \"TEXT_LIMIT\",\r\n	\"visibleType\": \"EDITABLE\",\r\n	\"filterFlag\": 1\r\n},\r\n{\r\n	\"dynamicFlag\": 0,\r\n	\"fieldDesc\": \"承租方\",\r\n	\"fieldDisplayName\": \"承租方\",\r\n	\"fieldExtra\": \"{}\",\r\n	\"fieldName\": \"ENTERPRISE_NAME\",\r\n	\"fieldType\": \"SINGLE_LINE_TEXT\",\r\n	\"remark\": \"允许用户手动输入；\",\r\n	\"disabled\": true,\r\n	\"renderType\": \"DEFAULT\",\r\n	\"requiredFlag\": 1,\r\n	\"validatorType\": \"TEXT_LIMIT\",\r\n	\"visibleType\": \"EDITABLE\",\r\n	\"filterFlag\": 1\r\n},\r\n{\r\n	\"dynamicFlag\": 0,\r\n	\"fieldDesc\": \"楼栋门牌\",\r\n	\"fieldDisplayName\": \"楼栋门牌\",\r\n	\"fieldExtra\": \"{}\",\r\n	\"fieldName\": \"楼栋门牌\",\r\n	\"fieldType\": \"SINGLE_LINE_TEXT\",\r\n	\"remark\": \"允许用户手动选择；\",\r\n	\"disabled\": true,\r\n	\"renderType\": \"DEFAULT\",\r\n	\"requiredFlag\": 1,\r\n	\"validatorType\": \"TEXT_LIMIT\",\r\n	\"visibleType\": \"EDITABLE\",\r\n	\"filterFlag\": 1\r\n}]', '1', '1', NULL, '2018-09-12 11:52:26');

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
-- 一键转为意向客户权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 150000, 0, 150109, '一键转为意向客户', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (150109, 0, '房源招商 一键转为意向客户权限', '招商管理 业务模块权限', NULL);

-- AUTHOR: tangcen
-- REMARK: 添加房源招商的报错信息
SET @id = (select max(id) from eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'investmentAd', '100000', 'zh_CN', '无招商广告数据');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'investmentAd', '100001', 'zh_CN', '招商广告不存在');
-- --------------------- SECTION END ---------------------------------------------------------