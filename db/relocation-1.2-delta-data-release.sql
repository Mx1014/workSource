-- by st.zheng
-- 物品放行1.2
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`, `app_type`, `client_handler_type`, `system_app_flag`, `icon_uri`, `host`, `enable_enterprise_pay_flag`)
VALUES ('49210', '申请记录', '49200', '/200/40000/49200/49210', '1', '4', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '', '1', '1', 'subModule', '1', '0', NULL, NULL, NULL, NULL);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`, `app_type`, `client_handler_type`, `system_app_flag`, `icon_uri`, `host`, `enable_enterprise_pay_flag`)
VALUES ('49220', '统计信息', '49200', '/200/40000/49200/49220', '1', '4', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '', '1', '1', 'subModule', '1', '0', NULL, NULL, NULL, NULL);

set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4920049210, '0', '物品放行 申请记录权限', '物品放行 申请记录权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '49210', '0', 4920049210, '全部权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4920049220, '0', '物品放行 统计信息权限', '物品放行 统计信息权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '49220', '0', 4920049220, '全部权限', '0', now());
