
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 黄鹏宇 20180731
-- REMARK:客户字段无法满足并导入，新增字段见详情
-- 购买/租赁
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES (@eecid, 'enterprise_customer', 'buyOrLease', '购买/租赁', 'Byte', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');
INSERT INTO `eh_var_field_items`(`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES (@eeciId, 'enterprise_customer', @eecid, '租赁', 1, 2, 1, sysdate(), NULL, NULL, NULL);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_field_items`(`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES (@eeciId, 'enterprise_customer', @eecid, '购买', 2, 2, 1, sysdate(), NULL, NULL, NULL);


-- 办公地址
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'bizAddress', '办公地址', 'String', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');


-- 经营年限
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'bizLife', '经营年限', 'String', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');


-- 经营年限
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'customerIntentionLevel', '客户意向程度', 'String', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');


-- 企业发展方向及目标
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'enterDevGoal', '企业发展方向及目标', 'String', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"multiText\", \"length\": 2048}');


-- 实际控制人姓名
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'controllerName', '实际控制人姓名', 'String', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');


-- 实际控制人生日（阳历）
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'controllerSunBirth', '实际控制人生日（阳历）', 'String', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');


-- 实际控制人生日（阴历）
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'controllerLunarBirth', '实际控制人生日（阴历）', 'String', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');


-- 有无融资需求
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'financingDemand', '融资需求', 'Byte', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');
INSERT INTO `eh_var_field_items`(`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES (@eeciId, 'enterprise_customer', @eecid, '无', 1, 2, 1, sysdate(), NULL, NULL, NULL);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_field_items`(`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES (@eeciId, 'enterprise_customer', @eecid, '有', 2, 2, 1, sysdate(), NULL, NULL, NULL);


-- 预留字段
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'stringTag1', '预留字段1', 'String', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'stringTag2', '预留字段2', 'String', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'stringTag3', '预留字段3', 'String', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'stringTag4', '预留字段4', 'String', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'stringTag5', '预留字段5', 'String', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'stringTag6', '预留字段6', 'String', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'stringTag7', '预留字段7', 'String', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'stringTag8', '预留字段8', 'String', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'stringTag9', '预留字段9', 'String', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'stringTag10', '预留字段10', 'String', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');


-- 10
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'stringTag11', '预留字段11', 'String', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'stringTag12', '预留字段12', 'String', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'stringTag13', '预留字段13', 'String', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

-- 12
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'stringTag14', '预留字段14', 'String', 12, '/1/12/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'stringTag15', '预留字段15', 'String', 12, '/1/12/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'stringTag16', '预留字段16', 'String', 12, '/1/12/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');


-- 预留下拉框
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox1', '预留下拉框1', 'Byte', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox2', '预留下拉框2', 'Byte', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox3', '预留下拉框3', 'Byte', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox4', '预留下拉框4', 'Byte', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox5', '预留下拉框5', 'Byte', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


-- 10
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox6', '预留下拉框6', 'Byte', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox7', '预留下拉框7', 'Byte', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox8', '预留下拉框8', 'Byte', 12, '/1/12/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox9', '预留下拉框9', 'Byte', 12, '/1/12/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');

-- END
-- --------------------- SECTION END ---------------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 黄鹏宇 20180801
-- REMARK:20180731新增字段插入数据修改
-- 购买/租赁
set @eecid=(select id from `eh_var_fields` where name='buyOrLease');
update `eh_var_field_items` set field_id = @eecid where field_id = @eecid-1 and display_name in ('租赁','购买');
update `eh_var_fields` set name = 'buyOrLeaseItemId' where id = @eecid;

set @eecid=(select id from `eh_var_fields` where name='financingDemand');
update `eh_var_field_items` set field_id = @eecid where field_id = @eecid-1 and display_name in ('有','无');
update `eh_var_fields` set name = 'financingDemandItemId' where id = @eecid;

UPDATE `eh_var_fields` set field_type = 'Long' where name in ('buyOrLeaseItemId','financingDemandItemId',
'dropBox1','dropBox2','dropBox3','dropBox4','dropBox5','dropBox6','dropBox7','dropBox8','dropBox9');

-- END
-- --------------------- SECTION END ---------------------------------------------------------
