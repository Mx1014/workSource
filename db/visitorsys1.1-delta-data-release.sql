-- 访客管理1.1 移动端管理权限
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41850', '移动端管理', '41800', '/200/20000/41800/41850', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
update eh_service_modules SET  path='/200/20000/41800/41810' WHERE id = 41810;
update eh_service_modules SET  path='/200/20000/41800/41840' WHERE id = 41840;

set @privilege_id = (select max(id) from eh_service_module_privileges);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4180041850, '0', '园区访客 移动端管理权限', '园区访客 移动端管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '41850', '0', 4180041850, '移动端管理权限', '0', now());


--
SET @homeurl = (select `value` from eh_configurations WHERE `name`='home.url' AND namespace_id = 0 limit 1);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('52200', '企业访客管理', '50000', '/100/50000/52200', '1', '2', '2', '220', now(), CONCAT('{"url":"',@homeurl,'/visitor-management/build/index.html?ns=%s&appId=%s&ownerType=enterprise#/home#sign_suffix"}'), '13', now(), '0', '0', '0', '0', 'community_control');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('42100', '园区访客管理', '40000', '/200/20000/42100', '1', '2', '2', '210', now(), CONCAT('{"url":"',@homeurl,'/visitor-management/build/index.html?ns=%s&appId=%s&ownerType=community#/home#sign_suffix"}'), '13', now(), '0', '0', '0', '0', 'org_control');

-- 更新访客的instance_config
update eh_service_modules SET instance_config = REPLACE(instance_config,' ','') WHERE id = 52100;
update eh_service_module_apps SET instance_config = REPLACE(instance_config,' ','') WHERE module_id = 52100;
