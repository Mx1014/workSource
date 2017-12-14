INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(21110,'客户列表',21100,'/20000/21100/21110','1','3','2','0',NOW());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(21120,'统计分析',21100,'/20000/21100/21120','1','3','2','0',NOW());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(21210,'合同列表',21200,'/20000/21200/21210','1','3','2','0',NOW());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(21220,'合同基础参数配置',21200,'/20000/21200/21220','1','3','2','0',NOW());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(21230,'工作流配置',21200,'/20000/21200/21230','1','3','2','0',NOW());

update eh_service_modules set parent_id = 20000, path = '/20000/21100' where name = '客户管理' and id = 21100;
update eh_service_modules set parent_id = 20000, path = '/20000/21200' where name = '合同管理' and id = 21200;

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21100, '0', '客户管理 管理员', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21200, '0', '合同管理 管理员', '合同管理 业务模块权限', NULL);

SET @module_privilege_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@module_privilege_id := @module_privilege_id + 1), '21100', '1', '21100', '客户管理管理权限', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@module_privilege_id := @module_privilege_id + 1), '21100', '2', '21100', '客户管理全部权限', '0', NOW());

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@module_privilege_id := @module_privilege_id + 1), '21200', '1', '21200', '合同管理管理权限', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@module_privilege_id := @module_privilege_id + 1), '21200', '2', '21200', '合同管理全部权限', '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21101, '0', '新增权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21102, '0', '修改权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21103, '0', '导入权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21104, '0', '同步权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21105, '0', '删除权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21106, '0', '查看权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21107, '0', '管理查看权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21108, '0', '管理新增权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21109, '0', '管理修改权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21110, '0', '管理删除权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21111, '0', '管理导入权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21112, '0', '管理导出权限', '客户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21113, '0', '统计分析查看权限', '客户管理 业务模块权限', NULL);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21201, '0', '新增权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21202, '0', '签约、发起审批权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21203, '0', '修改权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21204, '0', '删除权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21205, '0', '作废权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21206, '0', '入场权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21207, '0', '查看权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21208, '0', '续约权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21209, '0', '变更权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21210, '0', '合同参数查看权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21211, '0', '合同参数修改权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21212, '0', '合同工作流设置权限', '合同管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21213, '0', '合同同步', '合同管理 业务模块权限', NULL);

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
    
    
    
INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES ('225', '225', '999999', '园区热线', '40300', NULL, '2', '45', NULL, '2017-12-13 10:01:14', 'community_control', '0', '', NULL, '40300');
    
    
    
    
    
    