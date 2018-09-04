set @eecid=(select max(id)+1 from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'admissionItemId', '入驻状态', 'Long', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');
INSERT INTO `eh_var_field_items`(`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES (@eeciId, 'enterprise_customer', @eecid, '已入驻', 1, 2, 1, sysdate(), NULL, NULL, NULL);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_field_items`(`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES (@eeciId, 'enterprise_customer', @eecid, '未入驻', 2, 2, 1, sysdate(), NULL, NULL, NULL);



INSERT INTO `eh_var_field_groups`(`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES (20001, 'investment_enterprise', 0, '/20001' , '招商客户信息', 'com.everhomes.customer.EnterpriseCustomer', 0, NULL, 2, 1, SYSDATE(), NULL, NULL);

INSERT INTO `eh_var_field_groups`(`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES (20002, 'investment_enterprise', 20001, '/20001/20002' , '基本信息', 'com.everhomes.customer.EnterpriseCustomer', 0, NULL, 2, 1, SYSDATE(), NULL, NULL);



