-- 更新园区控制的模块
UPDATE eh_service_modules SET module_control_type = 'community_control' WHERE id IN (10100,10200,10400,10600,10700,10800,10850,11000,12200,20100,20400,20600,20800,20900,21100,21200,30500,31000,32000,32500,33000,37000,40100,40200,40300,40400,40500,40600,40700,40800,40900,41000,41100,41200,41400,41500,41600,41700,49100);
-- 更新OA控制的模块
UPDATE eh_service_modules SET module_control_type = 'org_control' WHERE id IN( 50100,50400,50600,50700,52000);
-- 更新不受限制控制的模块
UPDATE eh_service_modules SET module_control_type = 'unlimit_control' WHERE id IN(10750,13000,21000,30600,34000,35000,40750,41330,50300,50500,50900,51000);


-- 更新园区控制的模块关联的应用
UPDATE eh_service_module_apps SET module_control_type = 'community_control' WHERE module_id IN (10100,10200,10400,10600,10700,10800,10850,11000,12200,20100,20400,20600,20800,20900,21100,21200,30500,31000,32000,32500,33000,37000,40100,40200,40300,40400,40500,40600,40700,40800,40900,41000,41100,41200,41400,41500,41600,41700,49100);
-- 更新OA控制的模块
UPDATE eh_service_module_apps SET module_control_type = 'org_control' WHERE module_id IN( 50100,50400,50600,50700,52000);
-- 更新不受限制控制的模块
UPDATE eh_service_module_apps SET module_control_type = 'unlimit_control' WHERE module_id IN(10750,13000,21000,30600,34000,35000,40750,41330,50300,50500,50900,51000);

-- 物业缴费 权限细化脚本 by wentian
-- 物业缴费，查看权限id是否被占用，如果被占用，此段先不要执行，告知author by wentian
SELECT 1 FROM `eh_acl_privileges` WHERE  ID IN (40078,40073,40074,40075,40076,40077);
-- 物业缴费，删除以往的冗杂数据，物业缴费以及其子菜单，但不包括熊颖的计价条款设置 by wentian
DELETE FROM eh_acl_privileges WHERE id IN (SELECT privilege_id FROM eh_service_module_privileges WHERE module_id IN (SELECT id FROM eh_service_modules WHERE path LIKE '/20000/20400%' AND id != 20422));
DELETE FROM eh_service_module_privileges WHERE module_id IN (SELECT id FROM eh_service_modules WHERE path LIKE '/20000/20400%' AND id != 20422);
DELETE FROM eh_service_modules WHERE path LIKE '/20000/20400%' AND id != 20422;
-- 物业缴费，模块和权限配置
INSERT INTO `ehcore`.`eh_service_modules`
(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`,`module_control_type`)
VALUES
(20400, '物业缴费', '20000', '/20000/20400', '1', '2', '2', '0', NOW(),NULL, '13', NOW(), '0', '0', '0', '0','community_control');
-- 三级菜单，没有action_type
INSERT INTO `ehcore`.`eh_service_modules`
(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`,`module_control_type`)
VALUES
(204011, '账单管理', '20400', '/20000/20400/204011', '1', '3', '2', '0', NOW(),NULL, NULL, NOW(), '0', '0', '0', '0','community_control');
INSERT INTO `ehcore`.`eh_service_modules`
(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`,`module_control_type`)
VALUES
(204021, '账单统计', '20400', '/20000/20400/204021', '1', '3', '2', '0', NOW(),NULL, NULL, NOW(), '0', '0', '0', '0','community_control');
INSERT INTO `ehcore`.`eh_service_modules`
(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`,`module_control_type`)
VALUES
(20430, '交易明细', '20400', '/20000/20400/20430', '1', '3', '2', '0', NOW(),NULL, NULL, NOW(), '0', '0', '0', '0','community_control');

SET @p_id = 40073;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '账单查看、筛选', '账单管理 账单查看、筛选', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '204011', '0', @p_id, '账单查看、筛选', '0', NOW());

SET @p_id = 40074;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '新增账单', '账单管理 新增账单', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '204011', '0', @p_id, '新增账单', '0', NOW());

SET @p_id = 40075;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '催缴', '账单管理 催缴', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '204011', '0', @p_id, '账单管理 催缴', '0', NOW());

SET @p_id = 40076;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '修改缴费状态', '账单管理 修改缴费状态', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '204011', '0', @p_id, '账单管理 修改缴费状态', '0', NOW());

SET @p_id = 40077;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '查看', '账单统计 查看', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '204021', '0', @p_id, '账单统计 查看', '0', NOW());


SET @p_id = 40078;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '查看', '交易明细 查看', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '20430', '0', @p_id, '交易明细 查看', '0', NOW());

-- end of script by wentian


-- added by wh  


SET @p_id = 42000;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '查看打卡规则', '查看打卡规则', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '50600', '0', @p_id, '查看打卡规则', '0', NOW());


SET @p_id = 42001;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '查看自己创建的打卡规则', '查看自己创建的打卡规则', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '50600', '0', @p_id, '查看自己创建的打卡规则', '0', NOW());

SET @p_id = 42002;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '新建打卡规则', '新建打卡规则', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '50600', '0', @p_id, '新建打卡规则', '0', NOW());

SET @p_id = 42003;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '编辑打卡规则', '编辑打卡规则', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '50600', '0', @p_id, '编辑打卡规则', '0', NOW());

SET @p_id = 42004;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '删除打卡规则', '删除打卡规则', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '50600', '0', @p_id, '删除打卡规则', '0', NOW());


SET @p_id = 42005;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '查看打卡记录', '查看打卡记录', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '50600', '0', @p_id, '查看打卡记录', '0', NOW());

SET @p_id = 42006;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '导出打卡记录', '导出打卡记录', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '50600', '0', @p_id, '导出打卡记录', '0', NOW());


-- lei.lv
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (41001, null, '新增部门', '新增部门', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (41002, null, '修改部门', '修改部门', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (41003, null, '删除部门', '删除部门', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (41004, null, '调整部门顺序', '调整部门顺序', NULL);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (41005, null, '新增通用岗位', '新增通用岗位', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (41006, null, '编辑通用岗位', '编辑通用岗位', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (41007, null, '删除通用岗位', '删除通用岗位', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (41008, null, '编辑部门岗位', '编辑部门岗位', NULL);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (41009, null, '新增/修改人员', '新增/修改人员', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (41010, null, '删除人员', '删除人员', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (41011, null, '批量导入人员', '批量导入人员', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (41012, null, '批量导出人员', '批量导出人员', NULL);

set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES 
(@mp_id:=@mp_id+1, '50100', '0', 41001, '新增部门', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES 
(@mp_id:=@mp_id+1, '50100', '0', 41002, '修改部门', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES 
(@mp_id:=@mp_id+1, '50100', '0', 41003, '删除部门', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES 
(@mp_id:=@mp_id+1, '50100', '0', 41004, '调整部门顺序', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES 
(@mp_id:=@mp_id+1, '50100', '0', 41005, '新增通用岗位', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES 
(@mp_id:=@mp_id+1, '50100', '0', 41006, '编辑通用岗位', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES 
(@mp_id:=@mp_id+1, '50100', '0', 41007, '删除通用岗位', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES 
(@mp_id:=@mp_id+1, '50100', '0', 41008, '编辑部门岗位', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES 
(@mp_id:=@mp_id+1, '50100', '0', 41009, '新增/修改人员', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES 
(@mp_id:=@mp_id+1, '50100', '0', 41010, '删除人员', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES 
(@mp_id:=@mp_id+1, '50100', '0', 41011, '批量导入人员', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES 
(@mp_id:=@mp_id+1, '50100', '0', 41012, '批量导出人员', '0', NOW());


-- 更新action_type
update eh_service_modules set action_type = 46 WHERE id = 50100 and name = '组织架构';

update eh_service_modules set action_type = null where id = 50400 and name = '人事档案';
