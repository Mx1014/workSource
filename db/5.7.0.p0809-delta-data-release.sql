-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 马世亨  20180806
-- REMARK: 物品放行任务状态迁移
update eh_relocation_requests set status = 0 where status = 3;
-- --------------------- SECTION END ---------------------------------------------------------



-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本
-- AUTHOR: 黄鹏宇 20180801
-- REMARK:20180731新增字段插入数据修改
-- 购买/租赁

update `eh_var_field_items` set field_id = 12079 where field_id = 12078 and display_name in ('租赁','购买');
update `eh_var_fields` set name = 'buyOrLeaseItemId' where id = 12079;

update `eh_var_field_items` set field_id = 12087 where field_id = 12086 and display_name in ('有','无');
update `eh_var_fields` set name = 'financingDemandItemId' where id = 12087;

UPDATE `eh_var_fields` set field_type = 'Long' where id in (12079,12087,12104,12105,12106,12107,12108,12109,12110,12111,12112);
-- END

-- AUTHOR: 黄鹏宇 20180802
-- REMARK:修复导入导出不能使用的问题
UPDATE `eh_var_fields` SET NAME='dropBox1ItemId' where name = 'dropBox1';
UPDATE `eh_var_fields` SET NAME='dropBox1ItemId' where name = 'dropBox1';
UPDATE `eh_var_fields` SET NAME='dropBox2ItemId' where name = 'dropBox2';
UPDATE `eh_var_fields` SET NAME='dropBox3ItemId' where name = 'dropBox3';
UPDATE `eh_var_fields` SET NAME='dropBox4ItemId' where name = 'dropBox4';
UPDATE `eh_var_fields` SET NAME='dropBox5ItemId' where name = 'dropBox5';
UPDATE `eh_var_fields` SET NAME='dropBox6ItemId' where name = 'dropBox6';
UPDATE `eh_var_fields` SET NAME='dropBox7ItemId' where name = 'dropBox7';
UPDATE `eh_var_fields` SET NAME='dropBox8ItemId' where name = 'dropBox8';
UPDATE `eh_var_fields` SET NAME='dropBox9ItemId' where name = 'dropBox9';

-- END
-- --------------------- SECTION END ---------------------------------------------------------





-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: dev
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 黄鹏宇 20180731
-- REMARK:客户字段无法满足并导入，新增字段见详情
-- 购买/租赁
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES (@eecid, 'enterprise_customer', 'buyOrLeaseItemId', '购买/租赁', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');
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
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'financingDemandItemId', '融资需求', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');
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
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox1ItemId', '预留下拉框1', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox2ItemId', '预留下拉框2', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox3ItemId', '预留下拉框3', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox4ItemId', '预留下拉框4', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox5ItemId', '预留下拉框5', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


-- 10
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox6ItemId', '预留下拉框6', 'Long', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox7ItemId', '预留下拉框7', 'Long', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox8ItemId', '预留下拉框8', 'Long', 12, '/1/12/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox9ItemId', '预留下拉框9', 'Long', 12, '/1/12/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


-- END
-- --------------------- SECTION END -----------------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zijing
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 黄鹏宇 20180731
-- REMARK:客户字段无法满足并导入，新增字段见详情
-- 购买/租赁
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES (@eecid, 'enterprise_customer', 'buyOrLeaseItemId', '购买/租赁', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');
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
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'financingDemandItemId', '融资需求', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');
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
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox1ItemId', '预留下拉框1', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox2ItemId', '预留下拉框2', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox3ItemId', '预留下拉框3', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox4ItemId', '预留下拉框4', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox5ItemId', '预留下拉框5', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


-- 10
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox6ItemId', '预留下拉框6', 'Long', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox7ItemId', '预留下拉框7', 'Long', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox8ItemId', '预留下拉框8', 'Long', 12, '/1/12/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox9ItemId', '预留下拉框9', 'Long', 12, '/1/12/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


-- END
-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guangda
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 黄鹏宇 20180731
-- REMARK:客户字段无法满足并导入，新增字段见详情
-- 购买/租赁
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES (@eecid, 'enterprise_customer', 'buyOrLeaseItemId', '购买/租赁', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');
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
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'financingDemandItemId', '融资需求', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');
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
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox1ItemId', '预留下拉框1', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox2ItemId', '预留下拉框2', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox3ItemId', '预留下拉框3', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox4ItemId', '预留下拉框4', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox5ItemId', '预留下拉框5', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


-- 10
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox6ItemId', '预留下拉框6', 'Long', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox7ItemId', '预留下拉框7', 'Long', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox8ItemId', '预留下拉框8', 'Long', 12, '/1/12/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox9ItemId', '预留下拉框9', 'Long', 12, '/1/12/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


-- END
-- --------------------- SECTION END ---------------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: szbay
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 黄鹏宇 20180731
-- REMARK:客户字段无法满足并导入，新增字段见详情
-- 购买/租赁
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES (@eecid, 'enterprise_customer', 'buyOrLeaseItemId', '购买/租赁', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');
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
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'financingDemandItemId', '融资需求', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');
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
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox1ItemId', '预留下拉框1', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox2ItemId', '预留下拉框2', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox3ItemId', '预留下拉框3', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox4ItemId', '预留下拉框4', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox5ItemId', '预留下拉框5', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


-- 10
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox6ItemId', '预留下拉框6', 'Long', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox7ItemId', '预留下拉框7', 'Long', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox8ItemId', '预留下拉框8', 'Long', 12, '/1/12/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox9ItemId', '预留下拉框9', 'Long', 12, '/1/12/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


-- END
-- --------------------- SECTION END ---------------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: chuangyechang
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 黄鹏宇 20180731
-- REMARK:客户字段无法满足并导入，新增字段见详情
-- 购买/租赁
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES (@eecid, 'enterprise_customer', 'buyOrLeaseItemId', '购买/租赁', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');
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
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'financingDemandItemId', '融资需求', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');
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
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox1ItemId', '预留下拉框1', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox2ItemId', '预留下拉框2', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox3ItemId', '预留下拉框3', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox4ItemId', '预留下拉框4', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox5ItemId', '预留下拉框5', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


-- 10
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox6ItemId', '预留下拉框6', 'Long', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox7ItemId', '预留下拉框7', 'Long', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox8ItemId', '预留下拉框8', 'Long', 12, '/1/12/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox9ItemId', '预留下拉框9', 'Long', 12, '/1/12/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


-- END
-- --------------------- SECTION END ---------------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: anbang
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 黄鹏宇 20180731
-- REMARK:客户字段无法满足并导入，新增字段见详情
-- 购买/租赁
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES (@eecid, 'enterprise_customer', 'buyOrLeaseItemId', '购买/租赁', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');
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
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'financingDemandItemId', '融资需求', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');
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
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox1ItemId', '预留下拉框1', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox2ItemId', '预留下拉框2', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox3ItemId', '预留下拉框3', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox4ItemId', '预留下拉框4', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox5ItemId', '预留下拉框5', 'Long', 11, '/1/11/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


-- 10
set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox6ItemId', '预留下拉框6', 'Long', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');

set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox7ItemId', '预留下拉框7', 'Long', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox8ItemId', '预留下拉框8', 'Long', 12, '/1/12/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


set @eecid=(select max(id)+1 from `eh_var_fields`);
set @preId=(select max(id) from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'dropBox9ItemId', '预留下拉框9', 'Long', 12, '/1/12/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


-- END
-- --------------------- SECTION END ---------------------------------------------------------