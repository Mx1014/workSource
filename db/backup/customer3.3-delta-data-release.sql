-- 删除管理员设置界面的企业管理module
DELETE FROM  eh_web_menus WHERE module_id = '33000';

-- 企业客户管理增加的权限 将企业管理中的管理员作为权限项  by jiarui
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ('21115', '0', '设置管理员', '企业客户管理 设置管理员', NULL);

SET  @id = (SELECT  max(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@id:=@id+1), '21110', '0', '21115', '设置管理员', '14', now());


-- 企业客户类型由必选更改成非必选 合并动态表单多个字段   by jiarui
UPDATE eh_var_fields SET mandatory_flag = 0 WHERE name = 'categoryItemId' AND module_name = 'enterprise_customer';
UPDATE eh_var_fields SET display_name = '联系电话' WHERE name = 'contactPhone' AND module_name = 'enterprise_customer';

set @fieldId = IFNULL((SELECT id FROM eh_var_fields WHERE name = 'contactMobile' AND module_name = 'enterprise_customer' LIMIT 1),0);
DELETE FROM eh_var_field_scopes  where field_id = @fieldId and  module_name = 'enterprise_customer';

DELETE FROM eh_var_fields  WHERE name = 'contactMobile' AND module_name = 'enterprise_customer';

-- 迁移合并之后的字段数据  联系人电话 座机电话  by jiarui
UPDATE eh_enterprise_customers
SET contact_phone = CONCAT(contact_mobile,',',contact_phone)
WHERE contact_phone IS NOT NULL AND contact_mobile IS NOT NULL;

UPDATE eh_enterprise_customers
SET contact_phone = contact_mobile
WHERE contact_phone IS NULL AND contact_mobile IS NOT NULL;

UPDATE eh_enterprise_customers SET contact_phone = NULL WHERE contact_phone = '';
-- 迁移企业管理的是否设置管理员
UPDATE  eh_enterprise_customers SET admin_flag = IFNULL((SELECT set_admin_flag FROM eh_organizations WHERE id = eh_enterprise_customers.organization_id) ,0);

-- 增加企业管理中的动态字段  by jiarui

SET  @id = (SELECT MAX(id) FROM eh_var_fields);
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'enterprise_customer', 'unifiedSocialCreditCode', '统一社会信用代码', 'String', '11', '/1/11/', '0', NULL, '2', '1', now(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'enterprise_customer', 'postUri', '标题图', 'String', '11', '/1/11/', '0', NULL, '2', '1', now(), NULL, NULL, '{\"fieldParamType\": \"image\", \"length\": 1}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'enterprise_customer', 'banner', 'banner图', 'String', '11', '/1/11/', '0', NULL, '2', '1', now(), NULL, NULL, '{\"fieldParamType\": \"image\", \"length\": 9}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'enterprise_customer', 'hotline', '咨询电话', 'String', '11', '/1/11/', '0', NULL, '2', '1', now(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');


-- 企业简介改成richText
UPDATE `eh_var_fields` SET `field_param`='{\"fieldParamType\": \"richText\", \"length\": 204800}' WHERE `name`='corpDescription' and `module_name`='enterprise_customer';