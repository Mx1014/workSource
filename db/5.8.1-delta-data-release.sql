-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 黄鹏宇
-- REMARK: 防止模板表单占用

update `eh_general_form_templates` set namespace_id = 0, id = 2500001 where module_id = 25000;

SET @id = (SELECT IFNULL(MAX(id),1) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:= @id +1, 21100, 0, 21116, '删除请示', 0, SYSDATE());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21116, null, '客户管理 一键转为资质客户权限', '客户管理 业务模块权限', NULL);

-- END