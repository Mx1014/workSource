-- 修改认知途径  by jiarui 20180622
UPDATE  eh_var_fields set display_name ='客户来源' WHERE  name = 'sourceItemId';

SET  @var_id = (SELECT  max(id) from eh_var_fields);
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ('143', 'enterprise_customer', 'talentSourceItemId', '信息来源', 'Long', '4', '/4/', '0', NULL, '2', '1', now(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');


