
-- 增加企业后台菜单及动态表单附件
-- by jiarui 20180625
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('48035000', '企业后台', '72000000', NULL, 'customer-info', '1', '2', '/72000000/48035000', 'organization', '3', '21400', '2', 'system', 'module', '2');
SET  @id  = (SELECT max(id) from eh_var_fields);
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'enterprise_customer', 'attachments', '附件', 'List<ContractAttachmentDTO>', '11', '/1/11', '0', NULL, '2', '1', now(), NULL, NULL, '{\"fieldParamType\": \"file\", \"length\": 9}');
UPDATE  eh_var_fields SET field_param = '{"fieldParamType": "text", "length": 6}' WHERE name LIKE 'corpEmployeeAmount%';
UPDATE  eh_var_field_scopes SET  field_param = (SELECT eh_var_fields.field_param from eh_var_fields WHERE eh_var_fields.id = field_id);
-- end