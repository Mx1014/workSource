-- 资源预定增加统计权限 by st.zheng
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40430', '统计信息', '40400', '/40000/40400/40430', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');

set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4040040430, '0', '资源预约 统计管理权限', '资源预约 统计管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40430', '0', 4040040430, '统计信息权限', '0', now());

update eh_rentalv2_orders set status = 7 where status = 8;