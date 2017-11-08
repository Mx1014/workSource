set @field_id = (SELECT MAX(id) from eh_var_fields);
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) 
VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'month', '年月', 'Long', '9', '/9', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"datetime\", \"length\": 32}');
