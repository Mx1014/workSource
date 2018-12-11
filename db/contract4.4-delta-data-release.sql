
-- AUTHOR: 丁建民 20181210
-- REMARK: issue-43782 合同套打权限
UPDATE `eh_service_module_functions` SET  `explain`='打印' WHERE `id`='21216';

SET @id = (SELECT MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 21210, 0, 21226, '生成合同文档', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (21226, 0, '合同管理 生成合同文档', '合同管理生成合同文档权限', NULL);
INSERT INTO `eh_service_module_functions` (`id`, `module_id`, `privilege_id`, `explain`) VALUES (21226, '21200', '21226', '生成合同文档');

INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 21210, 0, 21227, '查看合同文档权限', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (21227, 0, '合同管理 查看合同文档', '合同管理 查看合同文档权限', NULL);
INSERT INTO `eh_service_module_functions` (`id`, `module_id`, `privilege_id`, `explain`) VALUES (21227, '21200', '21227', '查看合同文档');


-- AUTHOR: 黄鹏宇 2018年12月11日
-- REMARK: 新建字段
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12120, 'enterprise_customer', 'legalAddress', '法定地址', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 512}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12121, 'enterprise_customer', 'legalAddressZip', '法定地址邮编', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12122, 'enterprise_customer', 'postalAddress', '通讯地址', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 512}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12123, 'enterprise_customer', 'postalAddressZip', '通讯地址邮编', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12124, 'enterprise_customer', 'taxpayerIdentificationCode', '纳税人识别号', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12125, 'enterprise_customer', 'identifyCardNumber', '身份证号码', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12126, 'enterprise_customer', 'openingBank', '开户行', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 64}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12127, 'enterprise_customer', 'openingName', '开户名', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 64}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12128, 'enterprise_customer', 'openingAccount', '开户行账号', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12129, 'enterprise_customer', 'stringTag17', '预留字段17', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 128}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12130, 'enterprise_customer', 'stringTag18', '预留字段18', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 128}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12131, 'enterprise_customer', 'stringTag19', '预留字段19', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 128}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12132, 'enterprise_customer', 'stringTag20', '预留字段20', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 128}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12133, 'enterprise_customer', 'stringTag21', '预留字段21', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 128}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12134, 'enterprise_customer', 'corpLegalPersonDuty', '法人代表职务', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 32}');


SET @id = IFNULL((select max(id) from eh_var_field_ranges), 1);
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12120,'enterprise_customer','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12121,'enterprise_customer','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12122,'enterprise_customer','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12123,'enterprise_customer','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12124,'enterprise_customer','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12125,'enterprise_customer','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12126,'enterprise_customer','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12127,'enterprise_customer','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12128,'enterprise_customer','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12134,'enterprise_customer','enterprise_customer');


INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12120,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12121,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12122,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12123,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12124,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12125,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12126,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12127,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12128,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12129,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12130,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12131,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12132,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12133,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12134,'investment_promotion','enterprise_customer');


INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',14,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',16,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',19,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',20,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',3,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',28,'investment_promotion','enterprise_customer');

