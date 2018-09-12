-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等

-- AUTHOR: liangqishi 20180823
-- REMARK: 为统一订单申请专有域名gorder.zuolin.com

-- --------------------- SECTION END ---------------------------------------------------------




-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: tangcen
-- REMARK: 添加招商申请表单的默认字段
SET @general_form_templates_id = (SELECT MAX(id) FROM `eh_general_form_templates`);
INSERT INTO `eh_general_form_templates` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `update_time`, `create_time`) VALUES (@general_form_templates_id:=@general_form_templates_id+1, '0', '0', '0', 'EhOrganizations', '150000', 'business_invitation', '招商租赁', '0', 'DEFAULT_JSON', '[{\n	\"dynamicFlag\": 0,\n	\"fieldDesc\": \"用户姓名\",\n	\"fieldDisplayName\": \"用户姓名\",\n	\"fieldExtra\": \"{}\",\n	\"fieldName\": \"USER_NAME\",\n	\"fieldType\": \"SINGLE_LINE_TEXT\",\n	\"renderType\": \"DEFAULT\",\n	\"requiredFlag\": 1,\n	\"validatorType\": \"TEXT_LIMIT\",\n	\"visibleType\": \"EDITABLE\",\n	\"filterFlag\": 1\n},\n{\n	\"dynamicFlag\": 0,\n	\"fieldDesc\": \"手机号码\",\n	\"fieldDisplayName\": \"手机号码\",\n	\"fieldExtra\": \"{}\",\n	\"fieldName\": \"USER_PHONE\",\n	\"fieldType\": \"INTEGER_TEXT\",\n	\"renderType\": \"DEFAULT\",\n	\"requiredFlag\": 1,\n	\"validatorType\": \"TEXT_LIMIT\",\n	\"visibleType\": \"EDITABLE\",\n	\"filterFlag\": 1\n},\n{\n	\"dynamicFlag\": 0,\n	\"fieldDesc\": \"承租方\",\n	\"fieldDisplayName\": \"承租方\",\n	\"fieldExtra\": \"{}\",\n	\"fieldName\": \"ENTERPRISE_NAME\",\n	\"fieldType\": \"SINGLE_LINE_TEXT\",\n	\"renderType\": \"DEFAULT\",\n	\"requiredFlag\": 1,\n	\"validatorType\": \"TEXT_LIMIT\",\n	\"visibleType\": \"EDITABLE\",\n	\"filterFlag\": 1\n}]', '1', '1', NULL, NOW());




-- --------------------- SECTION END ---------------------------------------------------------



-- --------------------- SECTION BEGIN -------------------------------------------------------