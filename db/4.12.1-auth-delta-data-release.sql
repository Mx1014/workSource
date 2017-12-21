-- 仓库管理 权限细化脚の本 by wentian

-- 需要把service—module中改为园区control


-- imitate service since we don't have an entry at app side
set @reflect_id = (select MAX(`id`) from `eh_reflection_service_module_apps`);
set @app_id = (select MAX(`active_app_id`) from `eh_reflection_service_module_apps`);
INSERT INTO `eh_reflection_service_module_apps`
(`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`)
VALUES
(@reflect_id:=@reflect_id+1, @app_id:=@app_id+1, '999992', '仓库管理', '21000', NULL, '2', 13, NULL, NOW(), 'community-control', '0', '', '', '21000');

-- privilege
set @module_id = 21010;
set @p_id = 210001001;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '仓库维护 查找', '仓库维护 查找', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '查看', '0', NOW());

set @module_id = 21010;
set @p_id = 210001002;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '仓库维护 新增，编辑，删除', '仓库维护 新增，编辑，删除', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '新增，编辑，删除', '0', NOW());

set @module_id = 21020;
set @p_id = 210001003;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '物品维护 物品分类新增、编辑、删除、导入', '物品维护 物品分类新增、编辑、删除、导入', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '物品分类新增、编辑、删除、导入', '0', NOW());

set @module_id = 21020;
set @p_id = 210001004;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '物品维护 物品信息新增、编辑、删除、导入', '物品维护 物品信息新增、编辑、删除、导入', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '物品信息新增、编辑、删除、导入', '0', NOW());

set @module_id = 21030;
set @p_id = 210001005;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '库存维护 库存查找', '库存维护 库存查找', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '库存查询', '0', NOW());

set @module_id = 21030;
set @p_id = 210001006;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '库存维护 入库', '库存维护 入库', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '入库', '0', NOW());

set @module_id = 21030;
set @p_id = 210001007;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '库存维护 日志查找', '库存维护 日志查找', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '日志查找', '0', NOW());

set @module_id = 21030;
set @p_id = 210001008;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '库存维护 日志导出', '库存维护 日志导出', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '日志导出', '0', NOW());

set @module_id = 21040;
set @p_id = 210001009;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '领用管理 领用查找', '领用管理 领用查找', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '领用查找', '0', NOW());

set @module_id = 21040;
set @p_id = 210001010;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '领用管理 领用申请', '领用管理 领用申请', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '领用申请', '0', NOW());

set @module_id = 21050;
set @p_id = 210001011;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '参数配置 工作流配置', '参数配置 工作流配置', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '工作流配置', '0', NOW());

set @module_id = 21050;
set @p_id = 210001012;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '参数配置 参数配置', '参数配置 参数配置', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '参数配置', '0', NOW());


set @module_id = 21040;
set @p_id = 210001013;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '领用管理 入库', '领用管理 入库', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '入库', '0', NOW());

-- end of wentian's script, farewell


-- custoemrAuth xiongying
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `creator_uid`, `operator_uid`) VALUES(21110,'客户列表',21100,'/20000/21100/21110','1','3','2','0',NOW(),1,1);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `creator_uid`, `operator_uid`) VALUES(21120,'统计分析',21100,'/20000/21100/21120','1','3','2','0',NOW(),1,1);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `creator_uid`, `operator_uid`) VALUES(21210,'合同列表',21200,'/20000/21200/21210','1','3','2','0',NOW(),1,1);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `creator_uid`, `operator_uid`) VALUES(21220,'合同基础参数配置',21200,'/20000/21200/21220','1','3','2','0',NOW(),1,1);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `creator_uid`, `operator_uid`) VALUES(21230,'工作流配置',21200,'/20000/21200/21230','1','3','2','0',NOW(),1,1);

update eh_service_modules set parent_id = 20000, path = '/20000/21100' where name = '客户管理' and id = 21100;
update eh_service_modules set parent_id = 20000, path = '/20000/21200' where name = '合同管理' and id = 21200;

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21100, '0', '客户管理 管理员', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21200, '0', '合同管理 管理员', '合同管理 业务模块权限', NULL);

SET @module_privilege_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@module_privilege_id := @module_privilege_id + 1), '21100', '1', '21100', '客户管理管理权限', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@module_privilege_id := @module_privilege_id + 1), '21100', '2', '21100', '客户管理全部权限', '0', NOW());

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@module_privilege_id := @module_privilege_id + 1), '21200', '1', '21200', '合同管理管理权限', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@module_privilege_id := @module_privilege_id + 1), '21200', '2', '21200', '合同管理全部权限', '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21101, '0', '客户管理 新增权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21102, '0', '客户管理 修改权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21103, '0', '客户管理 导入权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21104, '0', '客户管理 同步权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21105, '0', '客户管理 删除权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21106, '0', '客户管理 查看权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21107, '0', '客户管理 管理查看权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21108, '0', '客户管理 管理新增权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21109, '0', '客户管理 管理修改权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21110, '0', '客户管理 管理删除权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21111, '0', '客户管理 管理导入权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21112, '0', '客户管理 管理导出权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21113, '0', '客户管理 统计分析查看权限', '客户管理 业务模块权限', NULL);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21201, '0', '合同管理 新增权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21202, '0', '合同管理 签约、发起审批权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21203, '0', '合同管理 修改权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21204, '0', '合同管理 删除权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21205, '0', '合同管理 作废权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21206, '0', '合同管理 入场权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21207, '0', '合同管理 查看权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21208, '0', '合同管理 续约权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21209, '0', '合同管理 变更权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21210, '0', '合同管理 合同参数查看权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21211, '0', '合同管理 合同参数修改权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21212, '0', '合同管理 合同工作流设置权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21213, '0', '合同管理 合同同步', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21214, '0', '合同管理 退约权限', '合同管理 业务模块权限', NULL);

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21110','0',21101,'客户管理 新增权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21110','0',21102,'客户管理 修改权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21110','0',21103,'客户管理 导入权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21110','0',21104,'客户管理 同步权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21110','0',21105,'客户管理 删除权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21110','0',21106,'客户管理 查看权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21110','0',21107,'客户管理 管理查看权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21110','0',21108,'客户管理 管理新增权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21110','0',21109,'客户管理 管理修改权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21110','0',21110,'客户管理 管理删除权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21110','0',21111,'客户管理 管理导入权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21110','0',21112,'客户管理 管理导出权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21120','0',21113,'客户管理 统计分析查看权限','0',NOW());

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21210','0',21201,'合同管理 新增权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21210','0',21202,'合同管理 签约、发起审批权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21210','0',21203,'合同管理 修改权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21210','0',21204,'合同管理 删除权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21210','0',21205,'合同管理 作废权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21210','0',21206,'合同管理 入场权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21210','0',21207,'合同管理 查看权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21210','0',21208,'合同管理 续约权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21210','0',21209,'合同管理 变更权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21220','0',21210,'合同管理 合同参数查看权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21220','0',21211,'合同管理 合同参数修改权限','0',NOW());  
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21230','0',21212,'合同管理 合同工作流设置权限','0',NOW());   
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21210','0',21213,'合同管理 合同同步','0',NOW());    
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'21210','0',21214,'合同管理 退约权限','0',NOW());   
    
    

SET @app_id = (SELECT MAX(id) FROM `eh_reflection_service_module_apps`);   
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES ((@app_id := @app_id + 1), @app_id, '999992', '客户管理', '21100', NULL, '2', '13', 'customer', NOW(), 'community_control', '0', '', NULL, '21100');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES ((@app_id := @app_id + 1), @app_id, '999992', '合同管理', '21200', NULL, '2', '13', 'contract', NOW(), 'community_control', '0', '', NULL, '21200');
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES ((@app_id := @app_id + 1), @app_id, '999992', '能耗管理', '49100', NULL, '2', '13', '{"url":"http://xiongying.lab.everhomes.com/energy-management/build/index.html?hideNavigationBar=1#/address_choose#sign_suffix"}', NOW(), 'community_control', '0', '', NULL, '49100');
    
DROP PROCEDURE IF EXISTS create_app;
DELIMITER //
CREATE PROCEDURE `create_app` ()
BEGIN
  DECLARE ns INTEGER;
  DECLARE moduleId LONG;
  DECLARE done INT DEFAULT FALSE;
  DECLARE cur CURSOR FOR SELECT module_id, namespace_id FROM eh_service_module_scopes where module_id in (21100, 21200, 49100) and apply_policy = 2;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
  OPEN cur;
  read_loop: LOOP
                FETCH cur INTO moduleId, ns;
                IF done THEN
                    LEAVE read_loop;
                END IF;

        SET @app_id = (SELECT MAX(id) FROM `eh_reflection_service_module_apps`);   
        INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES ((@app_id := @app_id + 1), @app_id, ns, '', moduleId, NULL, '2', '13', '', NOW(), 'community_control', '0', '', NULL, moduleId);

  END LOOP;
  CLOSE cur;
END
//
DELIMITER ;
CALL create_app;
DROP PROCEDURE IF EXISTS create_app;    
    
update eh_reflection_service_module_apps set name = '客户管理', action_data = 'customer' where module_id = 21100;      
update eh_reflection_service_module_apps set name = '合同管理', action_data = 'contract' where module_id = 21200;      
update eh_reflection_service_module_apps set name = '能耗管理', action_data = '{"url":"http://core.zuolin.com/energy-management/build/index/energy-management/build/index.html?hideNavigationBar=1#/address_choose#sign_suffix"}' where module_id = 49100;      
-- custoemrAuth xiongying end    
    

-- 物业巡检菜单显示不全  by jiarui
UPDATE eh_service_module_privileges
SET module_id = 20810
WHERE module_id = 20811;

UPDATE eh_service_modules
SET STATUS =2
WHERE id = 20840;
UPDATE eh_service_modules
SET LEVEL =4
WHERE id = 20841;

UPDATE eh_service_modules
SET STATUS =2
WHERE id = 20841;

-- 删除旧的里面的  privilege_id 为管理员
DELETE  from eh_acls
where privilege_id = 10011;

-- 删除旧数据 管理员权限 by jiarui 20171220
delete from  eh_acls
where privilege_id = 10011;

