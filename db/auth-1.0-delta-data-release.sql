-- 更新园区控制的模块
UPDATE eh_service_modules set module_control_type = 'community_control' where id in (10100,10200,10400,10600,10700,10800,10850,11000,12200,20100,20400,20600,20800,20900,21100,21200,30500,31000,32000,32500,33000,37000,40100,40200,40300,40400,40500,40600,40700,40800,40900,41000,41100,41200,41400,41500,41600,41700,49100);
-- 更新OA控制的模块
UPDATE eh_service_modules set module_control_type = 'org_control' where id in( 50100,50400,50600,50700,52000);
-- 更新不受限制控制的模块
UPDATE eh_service_modules set module_control_type = 'unlimit_control' where id in(10750,13000,21000,30600,34000,35000,40750,41330,50300,50500,50900,51000);


-- 更新园区控制的模块关联的应用
update eh_service_module_apps set module_control_type = 'community_control' where module_id in (10100,10200,10400,10600,10700,10800,10850,11000,12200,20100,20400,20600,20800,20900,21100,21200,30500,31000,32000,32500,33000,37000,40100,40200,40300,40400,40500,40600,40700,40800,40900,41000,41100,41200,41400,41500,41600,41700,49100);
-- 更新OA控制的模块
UPDATE eh_service_module_apps set module_control_type = 'org_control' where module_id in( 50100,50400,50600,50700,52000);
-- 更新不受限制控制的模块
UPDATE eh_service_module_apps set module_control_type = 'unlimit_control' where module_id in(10750,13000,21000,30600,34000,35000,40750,41330,50300,50500,50900,51000);

-- 物业缴费 权限细化脚本 by wentian
-- 物业缴费，查看权限id是否被占用，如果被占用，此段先不要执行，告知author by wentian
SELECT 1 FROM `eh_acl_privileges` WHERE  ID IN (40078,40073,40074,40075,40076,40077);
-- 物业缴费，删除以往的冗杂数据，物业缴费以及其子菜单，但不包括熊颖的计价条款设置 by wentian
delete from eh_acl_privileges where id in (select privilege_id from eh_service_module_privileges where module_id in (select id from eh_service_modules where path like '/20000/20400%' and id != 20422));
delete from eh_service_module_privileges where module_id in (select id from eh_service_modules where path like '/20000/20400%' and id != 20422);
delete from eh_service_modules where path like '/20000/20400%' and id != 20422;
-- 物业缴费，模块和权限配置
INSERT INTO `ehcore`.`eh_service_modules`
(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`,`module_control_type`)
VALUES
(20400, '物业缴费', '20000', '/20000/20400', '1', '2', '2', '0', NOW(),null, '13', NOW(), '0', '0', '0', '0','community_control');
-- 三级菜单，没有action_type
INSERT INTO `ehcore`.`eh_service_modules`
(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`,`module_control_type`)
VALUES
(204011, '账单管理', '20400', '/20000/20400/204011', '1', '3', '2', '0', NOW(),null, null, NOW(), '0', '0', '0', '0','community_control');
INSERT INTO `ehcore`.`eh_service_modules`
(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`,`module_control_type`)
VALUES
(204021, '账单统计', '20400', '/20000/20400/204021', '1', '3', '2', '0', NOW(),null, null, NOW(), '0', '0', '0', '0','community_control');
INSERT INTO `ehcore`.`eh_service_modules`
(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`,`module_control_type`)
VALUES
(20430, '交易明细', '20400', '/20000/20400/20430', '1', '3', '2', '0', NOW(),null, null, NOW(), '0', '0', '0', '0','community_control');

set @p_id = 40073;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '账单查看、筛选', '账单管理 账单查看、筛选', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '204011', '0', @p_id, '账单查看、筛选', '0', NOW());

set @p_id = 40074;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '新增账单', '账单管理 新增账单', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '204011', '0', @p_id, '新增账单', '0', NOW());

set @p_id = 40075;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '催缴', '账单管理 催缴', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '204011', '0', @p_id, '账单管理 催缴', '0', NOW());

set @p_id = 40076;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '修改缴费状态', '账单管理 修改缴费状态', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '204011', '0', @p_id, '账单管理 修改缴费状态', '0', NOW());

set @p_id = 40077;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '查看', '账单统计 查看', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '204021', '0', @p_id, '账单统计 查看', '0', NOW());


set @p_id = 40078;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '查看', '交易明细 查看', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '20430', '0', @p_id, '交易明细 查看', '0', NOW());

-- end of script by wentian