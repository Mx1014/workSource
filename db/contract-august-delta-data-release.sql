INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('customer', '10001', 'zh_CN', '客户不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('customer', '10002', 'zh_CN', '客户人才不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('customer', '10003', 'zh_CN', '客户申报项目不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('customer', '10004', 'zh_CN', '客户工商信息不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('customer', '10005', 'zh_CN', '客户专利不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('customer', '10006', 'zh_CN', '客户商标不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('customer', '10007', 'zh_CN', '客户经济指标不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('customer', '10008', 'zh_CN', '客户投融资情况不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('customer', '10009', 'zh_CN', '客户证书不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('customer', '10010', 'zh_CN', '客户名称为空');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('customer', '10011', 'zh_CN', '客户联系人为空');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('customer', '10012', 'zh_CN', '客户联系人手机号为空');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('customer', '10013', 'zh_CN', '客户名称已存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('customer', '10014', 'zh_CN', '客户类型不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('customer', '10015', 'zh_CN', '客户级别不存在');


INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`) 
VALUES (21100, '客户管理', 20000, NULL, '', 1, 2, '/20000/21100', 'park', 390);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`) 
VALUES (21110, '客户列表', 21100, NULL, 'react:/customer-management/customer-list', 0, 2, '/20000/21100/21110', 'park', 490);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`) 
VALUES (21120, '统计分析', 21100, NULL, 'react:/customer-management/statistics', 0, 2, '/20000/21100/21120', 'park', 490);

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`) 
VALUES (21200, '合同管理', 20000, NULL, '', 1, 2, '/20000/21200', 'park', 390);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`) 
VALUES (21210, '合同列表', 21200, NULL, 'react:/contract-management/contract-list', 0, 2, '/20000/21200/21210', 'park', 490);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`) 
VALUES (21220, '合同基础参数配置', 21200, NULL, 'react:/contract-management/params-config', 0, 2, '/20000/21200/21220', 'park', 490);

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 21100, '', 'EhNamespaces', 999985, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 21110, '', 'EhNamespaces', 999985, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 21120, '', 'EhNamespaces', 999985, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 21200, '', 'EhNamespaces', 999985, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 21210, '', 'EhNamespaces', 999985, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 21220, '', 'EhNamespaces', 999985, 2);


SET @privilege_id = (SELECT MAX(id) FROM `eh_acl_privileges`);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) 
VALUES ((@privilege_id := @privilege_id + 1), 0, '客户管理', '客户管理 全部权限', NULL);
SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) 
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @privilege_id, 21100, '客户管理', 1, 1, '客户管理  全部权限', 202); 
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1001, 'EhAclRoles', 0, 1, NOW()); 
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1005, 'EhAclRoles', 0, 1, NOW()); 

SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), '21100', '1', @privilege_id, NULL, '0', UTC_TIMESTAMP());
SET @eh_service_module_scopes_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999985, 21100, '客户管理', 'EhNamespaces', 999985, NULL, 2);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) 
VALUES ((@privilege_id := @privilege_id + 1), 0, '客户列表', '客户列表', NULL);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) 
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @privilege_id, 21110, '客户列表', 1, 1, '客户列表', 202); 
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1001, 'EhAclRoles', 0, 1, NOW()); 
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1005, 'EhAclRoles', 0, 1, NOW()); 

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) 
VALUES ((@privilege_id := @privilege_id + 1), 0, '客户管理-统计分析', '客户管理-统计分析', NULL);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) 
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @privilege_id, 21120, '客户管理-统计分析', 1, 1, '客户管理-统计分析', 202); 
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1001, 'EhAclRoles', 0, 1, NOW()); 
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1005, 'EhAclRoles', 0, 1, NOW()); 

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) 
VALUES ((@privilege_id := @privilege_id + 1), 0, '合同管理', '合同管理', NULL);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) 
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @privilege_id, 21200, '合同管理', 1, 1, '合同管理', 202); 
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1001, 'EhAclRoles', 0, 1, NOW()); 
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1005, 'EhAclRoles', 0, 1, NOW()); 

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), '21200', '1', @privilege_id, NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999985, 21200, '合同管理', 'EhNamespaces', 999985, NULL, 2);


INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) 
VALUES ((@privilege_id := @privilege_id + 1), 0, '合同列表', '合同列表', NULL);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) 
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @privilege_id, 21210, '合同列表', 1, 1, '合同列表', 202); 
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1001, 'EhAclRoles', 0, 1, NOW()); 
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1005, 'EhAclRoles', 0, 1, NOW()); 

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) 
VALUES ((@privilege_id := @privilege_id + 1), 0, '合同基础参数配置', '合同基础参数配置', NULL);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) 
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @privilege_id, 21220, '合同基础参数配置', 1, 1, '合同基础参数配置', 202); 
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1001, 'EhAclRoles', 0, 1, NOW()); 
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1005, 'EhAclRoles', 0, 1, NOW()); 

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) 
VALUES ('21100', '客户管理', '20000', '/20000/21100', '0', '2', '2', '0', UTC_TIMESTAMP()); 
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) 
VALUES ('21200', '合同管理', '20000', '/20000/21200', '0', '2', '2', '0', UTC_TIMESTAMP()); 


-- payment_wentian
SET @eh_locale_templates_id = (SELECT max(id) FROM `eh_locale_templates`);
INSERT INTO `ehcore`.`eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'user.notification', '3', 'zh_CN', '物业账单通知用户', '尊敬的${targetName}先生/女士,您的账单已出，请在app内查询', '999985');
SET @eh_locale_templates_id = (SELECT max(id) FROM `eh_locale_templates`);
INSERT INTO `ehcore`.`eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'sms.default.yzx', '54', 'zh_CN', '物业费催缴', '119704', '999971');
SET @eh_locale_templates_id = (SELECT max(id) FROM `eh_locale_templates`);
INSERT INTO `ehcore`.`eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'sms.default.yzx', '54', 'zh_CN', '物业费催缴', '119706', '999974');
SET @eh_locale_templates_id = (SELECT max(id) FROM `eh_locale_templates`);
INSERT INTO `ehcore`.`eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'sms.default.yzx', '54', 'zh_CN', '物业费催缴', '117613', '999973');
SET @eh_asset_vendor_id = (SELECT max(id) FROM `eh_asset_vendor`);
INSERT INTO `ehcore`.`eh_asset_vendor` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `status`, `namespace_id`) VALUES ((@eh_asset_vendor_id := @eh_asset_vendor_id + 1), 'community', '240111044331050388', '张江高科缴费', 'ZJGK', '2', '999971');
INSERT INTO `ehcore`.`eh_asset_vendor` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `status`, `namespace_id`) VALUES ((@eh_asset_vendor_id := @eh_asset_vendor_id + 1), 'community', '240111044331050389', '张江高科人才公寓缴费', 'ZJGK', '2', '999971');

UPDATE `eh_web_menus` set leaf_flag = '0';


SET @eh_web_menus_id_paym = (SELECT MAX(id) FROM `eh_web_menus`);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`,`module_id`,`level`)
VALUES (@eh_web_menus_id_paym := @eh_web_menus_id_paym+1, '账单管理', 20700, NULL, 'react:/payment-management/bills-manage', 1, 2, CONCAT('/20000/20700/',@eh_web_menus_id_paym), 'park', 990,20700,3);

SET @eh_web_menus_id_payb = (SELECT MAX(id) FROM `eh_web_menus`);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`,`module_id`,`level`)
VALUES (@eh_web_menus_id_payb := @eh_web_menus_id_payb+1, '账单统计', 20700, NULL, 'react:/payment-management/bills-statistics', 1, 2, CONCAT('/20000/20700/',@eh_web_menus_id_payb), 'park', 999,20700,3);


SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), @eh_web_menus_id_paym, '', 'EhNamespaces', 999985, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), @eh_web_menus_id_payb, '', 'EhNamespaces', 999985, 2);


SET @privilege_id = (SELECT MAX(id) FROM `eh_acl_privileges`);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES ((@privilege_id := @privilege_id + 1), 0, '账单管理', '账单管理 全部权限', NULL);
SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @privilege_id, @eh_web_menus_id_paym, '账单管理', 1, 1, '账单管理  全部权限', 990);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1001, 'EhAclRoles', 0, 1, NOW());
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1005, 'EhAclRoles', 0, 1, NOW());

SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), '20700', '1', @privilege_id, NULL, '0', UTC_TIMESTAMP());

SET @privilege_id = (SELECT MAX(id) FROM `eh_acl_privileges`);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES ((@privilege_id := @privilege_id + 1), 0, '账单统计', '账单统计 全部权限', NULL);
SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @privilege_id, @eh_web_menus_id_payb, '账单统计', 1, 1, '账单统计  全部权限', 999);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1001, 'EhAclRoles', 0, 1, NOW());
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1005, 'EhAclRoles', 0, 1, NOW());

SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), '20700', '1', @privilege_id, NULL, '0', UTC_TIMESTAMP());



INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`)
VALUES (@eh_web_menus_id_paym, '账单管理', '20700', '/20000/20700/', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`)
VALUES (@eh_web_menus_id_payb, '账单统计', '20700', '/20000/20700/', '0', '2', '2', '0', UTC_TIMESTAMP());



SET @eh_service_module_scopes_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999985, 20700, '账单管理', 'EhNamespaces', 999985, NULL, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999985, 20700, '账单统计', 'EhNamespaces', 999985, NULL, 2);
