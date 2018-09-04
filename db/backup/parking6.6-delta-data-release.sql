-- 备份表eh_parking_lots， 调用接口/parking/initFuncLists

-- 新权限dengs
DELETE from eh_service_modules WHERE id in (40810,40820,40830,40840);
DELETE from eh_acl_privileges WHERE id in (4080040810,4080040820,4080040830,4080040840);
DELETE from eh_service_module_privileges WHERE module_id in (40810,40820,40830,40840);

SELECT * from eh_service_modules WHERE id in (40800,40810,40820,40830,40840);
SELECT * from eh_acl_privileges WHERE id in (4080040800,4080040810,4080040820,4080040830,4080040840);
SELECT * from eh_service_module_privileges WHERE module_id in (40800,40810,40820,40830,40840);


INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40810', '申请管理', '40800', '/200/40000/40800/40810', '1', '4', '2', '61', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40820', '订单记录', '40800', '/200/40000/40800/40820', '1', '4', '2', '62', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');

set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4080040810, '0', '停车缴费 申请管理', '停车缴费 申请管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40810', '0', 4080040810, '申请管理权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4080040820, '0', '停车缴费 订单记录', '停车缴费 订单记录权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40820', '0', 4080040820, '订单记录权限', '0', now());

