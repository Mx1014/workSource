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