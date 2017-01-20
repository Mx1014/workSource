-- merge from  activity-2.0.0-delta-data-release.sql by lqs 20170119
-- 初始化数据，把官方活动的category_id全部配成1, add by tt, 20170116
update eh_activities set category_id = 1 where official_flag = 1;
update eh_forum_posts set category_id = 1 where official_flag = 1;




-- merge from equipment2.1-delta-data-release.sql by lqs 20170119
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('1', '999992', 'PM', '1000750', '0', '设备', '/设备', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('2', '999992', 'PM', '1000750', '0', '装修', '/装修', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('3', '999992', 'PM', '1000750', '0', '空置房', '/空置房', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('4', '999992', 'PM', '1000750', '0', '安保', '/安保', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('5', '999992', 'PM', '1000750', '0', '日常工作检查', '/日常工作检查', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('6', '999992', 'PM', '1000750', '0', '公共设施检查', '/公共设施检查', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('7', '999992', 'PM', '1000750', '0', '周末值班', '/周末值班', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('8', '999992', 'PM', '1000750', '0', '安全检查', '/安全检查', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);
INSERT INTO `eh_equipment_inspection_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `deletor_uid`, `delete_time`) VALUES ('9', '999992', 'PM', '1000750', '0', '其他', '/其他', NULL, '2', '0', '2017-01-11 16:59:23', '0', NULL);


update eh_equipment_inspection_equipments set inspection_category_id = 1;
update eh_equipment_inspection_standards set inspection_category_id = 1;
update eh_equipment_inspection_tasks set inspection_category_id = 1;






-- merge from authorization-delta-data-release.sql by lqs 20170120
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
