
-- AUTHOR: 丁建民 20181210
-- REMARK: issue-43782 合同套打权限
UPDATE `eh_service_module_functions` SET  `explain`='打印' WHERE `id`='21216';

SET @id = (SELECT MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 21210, 0, 21226, '生成合同文档', 12, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (21226, 0, '合同管理 生成合同文档', '合同管理生成合同文档权限', NULL);
INSERT INTO `eh_service_module_functions` (`id`, `module_id`, `privilege_id`, `explain`) VALUES (21226, '21200', '21226', '生成合同文档');

INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 21210, 0, 21227, '查看合同文档', 13, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (21227, 0, '合同管理 查看合同文档', '合同管理 查看合同文档权限', NULL);
INSERT INTO `eh_service_module_functions` (`id`, `module_id`, `privilege_id`, `explain`) VALUES (21227, '21200', '21227', '查看合同文档');
