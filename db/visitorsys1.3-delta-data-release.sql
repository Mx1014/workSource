-- 马世亨 2018-10-10
-- 访客管理1.3 合并访客应用
update eh_service_modules set instance_config = '{"url":"${home.url}/visitor-management/build/index.html?ns=%s&appId=%s&ownerType=community#/home#sign_suffix"}' where id = 41800;
update eh_service_modules set instance_config = '{"url":"${home.url}/visitor-appointment/build/index.html?ns=%s&appId=%s&ownerType=enterprise#/home#sign_suffix"}' where id = 52100;
delete from eh_service_modules where id in (42100,52200);
-- end

-- 马世亨 2018-10-10
-- 访客管理1.3 企业访客权限
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`)
VALUES ('52110', '预约管理', '52100', '/100/50000/52100/52110', '1', '4', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '', '1', '1', NULL);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`)
VALUES ('52120', '访客管理', '52100', '/100/50000/52100/52120', '1', '4', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '', '1', '1', NULL);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`)
VALUES ('52130', '设备管理', '52100', '/100/50000/52100/52130', '1', '4', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '', '1', '1', NULL);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`)
VALUES ('52140', '移动端管理', '52100', '/100/50000/52100/52140', '1', '4', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '', '1', '1', NULL);


set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@privilege_id:=@privilege_id+1, '52100', '0', '5210052100', '全部权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (5210052110, '0', '企业访客 预约管理权限', '企业访客 预约管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@privilege_id:=@privilege_id+1, '52110', '0', 5210052110, '预约管理权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (5210052120, '0', '企业访客 访客管理权限', '企业访客 访客管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@privilege_id:=@privilege_id+1, '52120', '0', 5210052120, '访客管理权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (5210052130, '0', '企业访客 设备管理权限', '企业访客 设备管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@privilege_id:=@privilege_id+1, '52130', '0', 5210052130, '设备管理权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (5210052140, '0', '企业访客 移动端管理权限', '企业访客 移动端管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@privilege_id:=@privilege_id+1, '52140', '0', 5210052140, '移动端管理权限', '0', now());
-- end