-- 
-- 客户资料管理细分权限 add by xq.tian  2017/01/16
--
SELECT max(id) FROM `eh_service_module_privileges` INTO @service_module_pri_id;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10127, '0', '添加客户', '客户资料细分权限 添加客户', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@service_module_pri_id := @service_module_pri_id + 1), '37000', '0', 10127, NULL, '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10128, '0', '批量导入及下载模版', '客户资料细分权限 批量导入及下载模版', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@service_module_pri_id := @service_module_pri_id + 1), '37000', '0', 10128, NULL, '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10129, '0', '导出EXCEL', '客户资料细分权限 导出EXCEL', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@service_module_pri_id := @service_module_pri_id + 1), '37000', '0', 10129, NULL, '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10130, '0', '查看统计信息', '客户资料细分权限 查看统计信息', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@service_module_pri_id := @service_module_pri_id + 1), '37000', '0', 10130, NULL, '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10131, '0', '修改客户资料', '客户资料细分权限 修改客户资料', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@service_module_pri_id := @service_module_pri_id + 1), '37000', '0', 10131, NULL, '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10132, '0', '查看客户资料', '客户资料细分权限 查看客户资料', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@service_module_pri_id := @service_module_pri_id + 1), '37000', '0', 10132, NULL, '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10133, '0', '管理客户资料', '客户资料细分权限 管理客户资料', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@service_module_pri_id := @service_module_pri_id + 1), '37000', '0', 10133, NULL, '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10134, '0', '删除客户资料', '客户资料细分权限 删除客户资料', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@service_module_pri_id := @service_module_pri_id + 1), '37000', '0', 10134, NULL, '0', NOW());

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10127, 37000, '添加客户', 1, 1, '添加客户 权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10128, 37000, '批量导入及下载模版', 1, 1, '批量导入及下载模版 权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10129, 37000, '导出EXCEL', 1, 1, '导出EXCEL 权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10130, 37000, '查看统计信息', 1, 1, '查看统计信息 权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10131, 37000, '修改客户资料', 1, 1, '修改客户资料 权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10132, 37000, '查看客户资料', 1, 1, '查看客户资料 权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10133, 37000, '管理客户资料', 1, 1, '管理客户资料 权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10134, 37000, '删除客户资料', 1, 1, '删除客户资料 权限', 202);


-- 开放项目管理业务模块 by sfyan 20170119
UPDATE `eh_service_modules` SET `type` = 0 WHERE `id` in (30000, 30500, 31000, 32000, 33000, 34000, 35000, 37000, 38000);


SELECT max(id) FROM `eh_service_module_scopes` INTO @eh_service_module_scopes;
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) 
select (@eh_service_module_scopes := @eh_service_module_scopes + 1), owner_id, menu_id, '', owner_type, owner_id, NULL, '2' from eh_web_menu_scopes where 
menu_id in (select id from eh_service_modules where id in (30000, 30500, 31000, 32000, 33000, 34000, 35000, 37000, 38000));
