-- 企业客户管理增加的权限 将企业管理中的管理员作为权限项  by jiarui
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ('21115', '0', '查看管理员', '企业客户管理 查看管理员', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ('21116', '0', '新增管理员', '企业客户管理 新增管理员', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ('21117', '0', '修改管理员', '企业客户管理 修改管理员', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ('21118', '0', '删除管理员', '企业客户管理 删除管理员', NULL);

-- 企业客户类型由必选更改成非必选 合并动态表单多个字段   by jiarui
UPDATE eh_var_fields SET mandatory_flag = 0 WHERE name = 'categoryItemId' AND module_name = 'enterprise_customer';
UPDATE eh_var_fields SET display_name = '联系电话' WHERE name = 'contactPhone' AND module_name = 'enterprise_customer';
DELETE FROM eh_var_fields  WHERE name = 'contactMobile' AND module_name = 'enterprise_customer';

-- 迁移合并之后的字段数据
