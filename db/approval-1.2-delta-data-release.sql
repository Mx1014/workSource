INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
VALUES('51000','审批管理','50000',NULL,'react:/approval-management/approval-list/51000','0','2','/50000/51000','park','591','51000');
-- INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
-- VALUES('51001','审批列表','51000',NULL,'react:/approval-management/approval-list/51000','1','2','/50000/51000/51001','park','591','51000');
-- INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
-- VALUES('51002','申请记录','51000',NULL,'apply_record','1','2','/50000/51000/51002','park','591','51000'); 

-- 给科兴加
SET @id = (SELECT MAX(id) FROM eh_web_menu_scopes);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@id:=@id+1),'51000','','EhNamespaces','999983','2');
-- INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@id:=@id+1),'51001','','EhNamespaces','999983','2');
-- INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@id:=@id+1),'51002','','EhNamespaces','999983','2');

-- 加module
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) 
VALUES('51000','表单管理','50000','/50000/51000','0','2','2','0','2017-05-19 11:50:20');

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) 
VALUES (10137, 0, '表单管理', '表单管理 全部权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) 
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10137, 51000, '审批管理', 1, 1, '审批管理  全部权限', 202); 
-- INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) 
-- VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10137, 51001, '审批列表', 1, 1, '审批列表  全部权限', 202); 
-- INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) 
-- VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10137, 51002, '申请记录', 1, 1, '申请记录  全部权限', 202); 

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, 10137, 1005, 'EhAclRoles', 0, 1, NOW()); 
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, 10137, 1001, 'EhAclRoles', 0, 1, NOW()); 

SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), '51000', '1', '10137', NULL, '0', UTC_TIMESTAMP());
SET @eh_service_module_scopes_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999983, 51000, '表单管理', 'EhNamespaces', 999983, NULL, 2);