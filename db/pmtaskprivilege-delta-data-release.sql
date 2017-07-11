INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `operator_uid`, `creator_uid`, `create_time`) VALUES(20140,'任务列表',20100,'/20000/20100/20140','1','3','2','0',1,1,NOW());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `operator_uid`, `creator_uid`, `create_time`) VALUES(20150,'服务录入',20100,'/20000/20100/20150','1','3','2','0',1,1,NOW());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `operator_uid`, `creator_uid`, `create_time`) VALUES(20155,'设置',20100,'/20000/20100/20155','1','3','0','0',1,1,NOW());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `operator_uid`, `creator_uid`, `create_time`) VALUES(20190,'统计',20100,'/20000/20100/20190','1','3','2','0',1,1,NOW());

SET @module_privilege_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@module_privilege_id := @module_privilege_id + 1), '20100', '1', '10008', '物业报修管理权限', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@module_privilege_id := @module_privilege_id + 1), '20100', '2', '10008', '物业报修全部权限', '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (30090, '0', '物业报修 任务查看权限', '物业报修 业务模块权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20140','0',30090,'物业报修 任务查看权限','0',NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (30091, '0', '物业报修 服务录入代发权限', '物业报修 业务模块权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20150','0',30091,'物业报修 服务录入代发权限','0',NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (30092, '0', '物业报修 新增服务类型权限', '物业报修 业务模块权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20155','0',30092,'物业报修 新增服务类型权限','0',NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (30093, '0', '物业报修 删除服务类型权限', '物业报修 业务模块权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20155','0',30093,'物业报修 删除服务类型权限','0',NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (30094, '0', '物业报修 新增分类权限', '物业报修 业务模块权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20155','0',30094,'物业报修 新增分类权限','0',NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (30095, '0', '物业报修 删除分类权限', '物业报修 业务模块权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20155','0',30095,'物业报修 删除分类权限','0',NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (30096, '0', '物业报修 服务统计查看权限', '物业报修 业务模块权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20190','0',30096,'物业报修 服务统计查看权限','0',NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (30097, '0', '物业报修 查看全部项目报表权限', '物业报修 业务模块权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'20190','0',30097,'物业报修 查看全部项目报表权限','0',NOW());    
    
    
    
    
    
    
    