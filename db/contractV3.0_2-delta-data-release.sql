-- 通用脚本  
-- ADD BY 丁建民 
-- #21713 合同管理V3.0（合同套打）
SET @id = IFNULL((SELECT MAX(`id`) FROM `eh_service_module_privileges`),0);
INSERT INTO `ehcore`.`eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES 
((@id:=@id+1), '21210', '0', '21215', '打印预览', '9', NOW());
INSERT INTO `ehcore`.`eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES 
((@id:=@id+1), '21210', '0', '21216', '打印', '10', NOW());

-- END BY 丁建民  