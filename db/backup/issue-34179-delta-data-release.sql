-- AUTHOR: yanlong.liang 20180734
-- REMARK: 用户认证权限
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (42007, null, '查看审核列表', '用户认证查看审核列表', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (42008, null, '审核权限', '用户认证审核权限', NULL);

set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@mp_id:=@mp_id+1, '35000', '0', 42007, '查看审核列表', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@mp_id:=@mp_id+1, '35000', '0', 42008, '审核权限', '0', NOW());

UPDATE eh_service_modules SET module_control_type = 'community_control' WHERE id =35000;
UPDATE eh_service_module_apps SET module_control_type = 'community_control' WHERE module_id = 35000;
UPDATE eh_authorizations SET owner_type = 'EhAll' WHERE auth_id = 35000;
UPDATE eh_authorizations SET module_control_type = 'community_control' WHERE auth_id = 35000;
-- END