-- add by xiongying20171024
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('2300000', '客户管理', '2000000', NULL, 'customer-management', '1', '2', '/2000000/2300000', 'zuolin', '2', '21100', '2', 'system', 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('2310000', '客户管理', '2300000', NULL, 'customer-management', '1', '2', '/2000000/2300000/2310000', 'zuolin', '2', '21100', '3', 'system', 'page');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('2400000', '合同管理', '2000000', NULL, 'contract-management', '1', '2', '/2000000/2400000', 'zuolin', '2', '21200', '2', 'system', 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('2410000', '合同管理', '2400000', NULL, 'contract-management', '1', '2', '/2000000/2400000/2410000', 'zuolin', '2', '21200', '3', 'system', 'page');

SET @eh_var_fields_id = (SELECT MAX(id) FROM `eh_var_fields`);
SET @eh_var_field_items_id = (SELECT MAX(id) FROM `eh_var_field_items`);
update eh_var_fields set field_param = '{\"fieldParamType\": \"text\", \"length\": 32}' where id = 7;
update eh_var_fields set field_param = '{\"fieldParamType\": \"select\", \"length\": 32}' where id = 188;
update eh_var_fields set field_param = '{\"fieldParamType\": \"text\", \"length\": 32}' where id = 211;
update eh_var_fields set field_param = '{\"fieldParamType\": \"text\", \"length\": 32}' where id = 212;
update eh_var_fields set field_param = '{\"fieldParamType\": \"text\", \"length\": 32}' where id = 213;
update eh_var_fields set field_param = '{\"fieldParamType\": \"select\", \"length\": 32}' where id = 214;
update eh_var_fields set field_param = '{\"fieldParamType\": \"text\", \"length\": 32}' where id = 215;

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES ((@eh_var_fields_id := @eh_var_fields_id + 1 ), 'enterprise_customer', 'intentionGrade', '意向等级', 'Long', '19', '/19', '1', NULL, '2', '1', '2017-08-25 02:27:30', NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');


INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES ((@eh_var_fields_id := @eh_var_fields_id + 1 ), 'enterprise_customer', 'trackingTime', '跟进时间', 'Long', '19', '/19', '1', NULL, '2', '1', '2017-08-25 02:27:30', NULL, NULL, '{\"fieldParamType\": \"datetimeWithM\", \"length\": 32}');


INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES ((@eh_var_fields_id := @eh_var_fields_id + 1 ), 'enterprise_customer', 'content', '跟进内容', 'Long', '19', '/19', '1', NULL, '2', '1', '2017-08-25 02:27:30', NULL, NULL, '{\"fieldParamType\": \"multiText\", \"length\": 32}');


INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES ((@eh_var_fields_id := @eh_var_fields_id + 1 ), 'enterprise_customer', 'trackingType', '计划类型', 'Long', '20', '/20', '1', NULL, '2', '1', '2017-08-25 02:27:30', NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');


INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES ((@eh_var_fields_id := @eh_var_fields_id + 1 ), 'enterprise_customer', 'customerName', '客户名称', 'String', '20', '/20', '1', NULL, '2', '1', '2017-08-25 02:27:30', NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES ((@eh_var_fields_id := @eh_var_fields_id + 1 ), 'enterprise_customer', 'trackingTime', '跟进时间', 'Long', '20', '/20', '1', NULL, '2', '1', '2017-08-25 02:27:30', NULL, NULL, '{\"fieldParamType\": \"datetimeWithM\", \"length\": 32}');

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES ((@eh_var_fields_id := @eh_var_fields_id + 1 ), 'enterprise_customer', 'notifyTime', '提前提醒时间', 'Long', '20', '/20', '1', NULL, '2', '1', '2017-08-25 02:27:30', NULL, NULL, '{\"fieldParamType\": \"datetimeWithM\", \"length\": 32}');


INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES ((@eh_var_fields_id := @eh_var_fields_id + 1 ), 'enterprise_customer', 'title', '标题', 'Long', '20', '/20', '1', NULL, '2', '1', '2017-08-25 02:27:30', NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');


INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`)
VALUES ((@eh_var_fields_id := @eh_var_fields_id + 1 ), 'enterprise_customer', 'content', '内容', 'String', '20', '/20', '1', NULL, '2', '1', '2017-08-25 02:27:30', NULL, NULL, '{\"fieldParamType\": \"multiText\", \"length\": 32}');


INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`)
VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', 214, '写字楼', '1', '2', '1', '2017-09-14 08:03:29', NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`)
VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', 214, '商铺', '1', '2', '1', '2017-09-14 08:03:29', NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`)
VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', 214, '厂房', '1', '2', '1', '2017-09-14 08:03:29', NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`)
VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', 214, '车位', '1', '2', '1', '2017-09-14 08:03:29', NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`)
VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', 214, '其他', '1', '2', '1', '2017-09-14 08:03:29', NULL, NULL);

INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', '188', '个人', '1', '2', '1', '2017-08-24 04:26:25', NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', '188', '商品房', '1', '2', '1', '2017-10-10 02:35:31', NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', '188', '存量房', '1', '2', '1', '2017-10-10 02:35:31', NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', '188', '集资房', '1', '2', '1', '2017-10-10 02:35:31', NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', '188', '平价房', '1', '2', '1', '2017-10-10 02:35:31', NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', '188', '廉租住房', '1', '2', '1', '2017-10-10 02:35:31', NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', '188', '经济适用住房', '1', '2', '1', '2017-10-10 02:35:31', NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', '188', '公寓式住宅', '1', '2', '1', '2017-10-10 02:35:31', NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ((@eh_var_field_items_id:= @eh_var_field_items_id +1 ), 'enterprise_customer', '188', '其他', '1', '2', '1', '2017-10-10 02:35:31', NULL, NULL);

-- 默认去处理按钮文本  add by xq.tian 2017/10/25
update eh_flow_nodes set goto_process_button_name='去处理' where goto_process_button_name is null or goto_process_button_name='';
-- by Sir Xiongying.V.Breg
update eh_var_field_groups set name = 'com.everhomes.customer.CustomerTracking' where title = '跟进信息';
update eh_var_field_groups set name = 'com.everhomes.customer.CustomerTrackingPlan' where title = '计划信息'

-- 工作流的文本修改  add by xq.tian 2017/10/25
UPDATE eh_locale_templates SET `text` = '在 ${nodeName} 执行 ${buttonName}' WHERE scope='flow' AND code=20001;
UPDATE eh_locale_templates SET `text` = '任务超时 已取消任务' WHERE scope='flow' AND code=20003;

