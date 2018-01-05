INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `creator_uid`, `operator_uid`) VALUES(21300,'付款管理',20000,'/20000/21300','1','2','2','0',NOW(),1,1);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `creator_uid`, `operator_uid`) VALUES(21310,'付款申请单',21300,'/20000/21300/21310','1','3','2','0',NOW(),1,1);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `creator_uid`, `operator_uid`) VALUES(21320,'工作流设置',21300,'/20000/21300/21320','1','3','2','0',NOW(),1,1);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `creator_uid`, `operator_uid`) VALUES(21215,'付款合同',21200,'/20000/21200/21215','1','3','2','0',NOW(),1,1);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21300, '0', '付款管理 管理员', '付款管理 业务模块权限', NULL);

SET @module_privilege_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@module_privilege_id := @module_privilege_id + 1), '21300', '1', '21300', '付款管理管理权限', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@module_privilege_id := @module_privilege_id + 1), '21300', '2', '21300', '付款管理全部权限', '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21301, '0', '付款管理 新增权限', '付款管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21302, '0', '付款管理 查看权限', '付款管理 业务模块权限', NULL);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21215, '0', '付款合同 新增付款合同', '付款合同 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21216, '0', '付款合同 签约 发起 付款审批', '付款合同 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21217, '0', '付款合同 修改 付款合同', '付款合同 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21218, '0', '付款合同 删除 付款合同', '付款合同 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21219, '0', '付款合同 作废 付款合同', '付款合同 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21220, '0', '付款合同 查看 付款合同', '付款合同 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21221, '0', '付款合同 续约 付款合同', '付款合同 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21222, '0', '付款合同 变更 付款合同', '付款合同 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21223, '0', '付款合同 退约 付款合同', '付款合同 业务模块权限', NULL);

    
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'21310','0',21301,'付款管理 新增权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'21310','0',21302,'付款管理 查看权限','0',NOW());

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'21215','0',21215,'付款管理 新增付款合同权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'21215','0',21216,'付款管理 签约 发起 付款审批权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'21215','0',21217,'付款管理 修改 付款合同权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'21215','0',21218,'付款管理 删除 付款合同权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'21215','0',21219,'付款管理 作废 付款合同权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'21215','0',21220,'付款管理 查看 付款合同权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'21215','0',21221,'付款管理 续约 付款合同权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'21215','0',21222,'付款管理 变更 付款合同权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'21215','0',21223,'付款管理 退约 付款合同权限','0',NOW());
