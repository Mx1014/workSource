-- 薪酬2.2 begin
-- added by wh :薪酬工资条发放消息
SET @template_id = (SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'salary.notification', 1, 'zh_CN', '后台发工资条', '${salaryDate} 工资已发放。', 0);

-- added by wh 薪酬的模块对应action_type 改为74(工资条)
UPDATE eh_service_modules SET action_type = 74 WHERE id = 51400;
UPDATE eh_service_module_apps SET action_type = 74 WHERE module_id = 51400;
-- 薪酬2.2 end


-- 客户管理第三方数据跟进人  by jiarui
UPDATE  eh_enterprise_customers set tracking_uid = NULL  WHERE  tracking_uid = -1;
update eh_enterprise_customer_admins  set namespace_id  = (select namespace_id from eh_enterprise_customers where id = customer_id);

update eh_enterprise_customer_admins t2  set contact_type  = (select target_type from eh_organization_members t1 where t1.contact_token = t2.contact_token and t1.namespace_id = t2.namespace_id limit 1);




-- 模块Id 41900
-- 新增模块 eh_service_modules
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `update_time`, `operator_uid`, `creator_uid`, `multiple_flag`, `module_control_type`) VALUES ('41900', '政务服务', '40000', '/40000/41900', '1', '2', '2', '0', UTC_TIMESTAMP(), UTC_TIMESTAMP(), '0', '0', '0', 'community_control');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41910', '政策管理', '41900', '/40000/41900/41910', '1', '3', '2', '0', UTC_TIMESTAMP(), '', NULL, UTC_TIMESTAMP(), '0', '0', '', '0', 'community_control');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41920', '查询记录', '41900', '/40000/41900/41920', '1', '3', '2', '0', UTC_TIMESTAMP(), '', NULL, UTC_TIMESTAMP(), '0', '0', '', '0', 'community_control');
-- 新增模块菜单 eh_web_menus
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `data_type`, `leaf_flag`, `path`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('16032200', '政务服务', '16030000', 'policy-service', '1', '/16000000/16030000/16032200', '22', '41900', '3', 'system', 'module')
-- 权限
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`) VALUES ('4190041910', '0', '政策管理', '政策管理 全部权限');
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`) VALUES ('4190041920', '0', '查询记录', '查询记录 全部权限');
-- 权限中间表 设置Id
SET @pri_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@pri_id := @pri_id + 1, '41910', '0', '4190041910', '全部权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@pri_id := @pri_id + 1, '41920', '0', '4190041920', '全部权限', '0', UTC_TIMESTAMP());
