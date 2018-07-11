-- 通用脚本  
-- ADD BY 丁建民 
-- #21713 合同管理V3.0（合同套打）
SET @id = IFNULL((SELECT MAX(`id`) FROM `eh_service_module_privileges`),0);
INSERT INTO `ehcore`.`eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES 
((@id:=@id+1), '21210', '0', '21215', '打印预览', '9', NOW());
INSERT INTO `ehcore`.`eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES 
((@id:=@id+1), '21210', '0', '21216', '编辑、清空数据、打印', '10', NOW());

SET @id = (SELECT MAX(id) from eh_var_fields);

INSERT INTO `ehcore`.`eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES 
((@id:=@id+1), 'contract', 'associationTemplate', '关联合同模板', 'String', '15', '/13/15/', '0', NULL, '2', '1', '2017-12-04 20:49:53', NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

INSERT INTO `ehcore`.`eh_service_module_functions` (`id`, `module_id`, `privilege_id`, `explain`) VALUES ('21215', '21200', '21215', '打印预览');
INSERT INTO `ehcore`.`eh_service_module_functions` (`id`, `module_id`, `privilege_id`, `explain`) VALUES ('21216', '21200', '21216', '编辑、清空数据、打印');

-- END BY 丁建民  

-- 通用脚本  
-- ADD BY 丁建民 
-- #30591 【合同管理3.5】初始化合同管理的字段未定义
SET @id = (SELECT MAX(id) from eh_var_field_group_scopes);
INSERT INTO `ehcore`.`eh_var_field_group_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES 
((@id:=@id+1), 0, 'contract', '14', '基本信息', '1', '2', '1', NOW(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ehcore`.`eh_var_field_group_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES 
((@id:=@id+1), 0, 'contract', '13', '收款合同', '2', '2', '1', NOW(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ehcore`.`eh_var_field_group_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES 
((@id:=@id+1), 0, 'contract', '15', '合同概览', '3', '2', '1', NOW(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ehcore`.`eh_var_field_group_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES 
((@id:=@id+1), 0, 'contract', '19', '计价条款', '4', '2', '1', NOW(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ehcore`.`eh_var_field_group_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES 
((@id:=@id+1), 0, 'contract', '20', '调租计划', '5', '2', '1', NOW(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ehcore`.`eh_var_field_group_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES 
((@id:=@id+1), 0, 'contract', '21', '免租计划', '6', '2', '1', NOW(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ehcore`.`eh_var_field_group_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES 
((@id:=@id+1), 0, 'contract', '22', '费用清单', '7', '2', '1', NOW(), NULL, NULL, NULL, NULL, NULL);


SET @id = (SELECT MAX(id) from eh_var_field_scopes);
INSERT INTO `ehcore`.`eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@id:=@id+1), '0', 'contract', '14', '101', '{\"fieldParamType\": \"text\", \"length\": 32}', '合同名称', '1', '0', '2', '1', NOW(), NULL, NULL, NULL, '/13/14/', NULL);
INSERT INTO `ehcore`.`eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@id:=@id+1), '0', 'contract', '14', '102', '{\"fieldParamType\": \"text\", \"length\": 32}', '客户名称', '1', '1', '2', '1', NOW(), NULL, NULL, NULL, '/13/14/', NULL);
INSERT INTO `ehcore`.`eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@id:=@id+1), '0', 'contract', '14', '104', '{\"fieldParamType\": \"text\", \"length\": 32}', '合同属性', '1', '2', '2', '1', NOW(), NULL, NULL, NULL, '/13/14/', NULL);
INSERT INTO `ehcore`.`eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@id:=@id+1), '0', 'contract', '14', '105', '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}', '合同类型', '1', '3', '2', '1', NOW(), NULL, NULL, NULL, '/13/14/', NULL);
INSERT INTO `ehcore`.`eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@id:=@id+1), '0', 'contract', '14', '110', '{\"fieldParamType\": \"datetime\", \"length\": 32}', '开始日期', '1', '4', '2', '1', NOW(), NULL, NULL, NULL, '/13/14/', NULL);
INSERT INTO `ehcore`.`eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@id:=@id+1), '0', 'contract', '14', '111', '{\"fieldParamType\": \"datetime\", \"length\": 32}', '结束日期', '1', '5', '2', '1', NOW(), NULL, NULL, NULL, '/13/14/', NULL);
INSERT INTO `ehcore`.`eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@id:=@id+1), '0', 'contract', '14', '115', '{\"fieldParamType\": \"text\", \"length\": 32}', '合同资产', '1', '6', '2', '1', NOW(), NULL, NULL, NULL, '/13/14/', NULL);
INSERT INTO `ehcore`.`eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@id:=@id+1), '0', 'contract', '15', '131', '{\"fieldParamType\": \"text\", \"length\": 32}', '合同状态', '1', '0', '2', '1', NOW(), NULL, NULL, NULL, '/13/15/', NULL);
INSERT INTO `ehcore`.`eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@id:=@id+1), '0', 'contract', '14', '100', '{\"fieldParamType\": \"text\", \"length\": 32}', '合同编号', '0', '8', '2', '1', NOW(), NULL, NULL, NULL, '/13/14/', NULL);
INSERT INTO `ehcore`.`eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@id:=@id+1), '0', 'contract', '15', '119', '{\"fieldParamType\": \"text\", \"length\": 32}', '押金', '0', '28', '2', '1', NOW(), NULL, NULL, NULL, '/13/15/', NULL);
INSERT INTO `ehcore`.`eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@id:=@id+1), '0', 'contract', '15', '132', '{\"fieldParamType\": \"text\", \"length\": 32}', '经办人', '0', '32', '2', '1', NOW(), NULL, NULL, NULL, '/13/15/', NULL);
INSERT INTO `ehcore`.`eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@id:=@id+1), '0', 'contract', '15', '135', '{\"fieldParamType\": \"multiText\", \"length\": 2048}', '备注', '0', '33', '2', '1', NOW(), NULL, NULL, NULL, '/13/15/', NULL);
INSERT INTO `ehcore`.`eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`, `category_id`) VALUES ((@id:=@id+1), '0', 'contract', '15', '136', '{\"fieldParamType\": \"file\", \"length\": 9}', '附件', '0', '40', '2', '1', NOW(), NULL, NULL, NULL, '/13/15/', NULL);

SET @id = (SELECT MAX(id) from eh_var_field_item_scopes);
INSERT INTO `ehcore`.`eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES 
((@id:=@id+1), '0', 'contract', '105', '28', '资源租赁合同', '1', '2', '1', NOW(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ehcore`.`eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES 
((@id:=@id+1), '0', 'contract', '105', '29', '物业服务合同', '2', '2', '1', NOW(), NULL, NULL, NULL, NULL, NULL);
INSERT INTO `ehcore`.`eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES 
((@id:=@id+1), '0', 'contract', '105', '30', '车位服务合同', '3', '2', '1', NOW(), NULL, NULL, NULL, NULL, NULL);

-- END BY 丁建民 