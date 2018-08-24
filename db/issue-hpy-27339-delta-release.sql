set @eecid=(select max(id)+1 from `eh_var_fields`);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (@eecid, 'enterprise_customer', 'aptitudeFlagItemId', '资质客户', 'Long', 10, '/1/10/', 0, NULL, 2, 1, sysdate(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');
  INSERT INTO `eh_var_field_items`(`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES (@eeciId, 'enterprise_customer', @eecid, '是', 1, 2, 1, sysdate(), NULL, NULL, 1);
set @eeciId=(select max(id)+1 from `eh_var_field_items`);
INSERT INTO `eh_var_field_items`(`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES (@eeciId, 'enterprise_customer', @eecid, '否', 2, 2, 1, sysdate(), NULL, NULL, 0);



set @id=(select max(id)+1 from `eh_general_form_templates`) ;
INSERT INTO `eh_general_form_templates`(`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`,  `version`, `template_type`, `template_text`, `modify_flag`, `delete_flag`, `create_time` ) VALUES (@id, 0, 0, 0, 'EhOrganizations', 25000, 'requisition', '请示单',  0, 'DEFAULT_JSON', '[{
	"dynamicFlag": 0,
	"fieldDesc": "客户名称",
	"fieldDisplayName": "客户名称",
	"fieldExtra": "{}",
	"fieldName": "客户名称",
	"fieldType": "SINGLE_LINE_TEXT",
	"renderType": "DEFAULT",
	"requiredFlag": 1,
	"validatorType": "TEXT_LIMIT",
	"visibleType": "EDITABLE",
	"filterFlag": 1
},
{
	"dynamicFlag": 0,
	"fieldDesc": "楼栋门牌",
	"fieldDisplayName": "楼栋门牌",
	"fieldExtra": "{}",
	"fieldName": "楼栋门牌",
	"fieldType": "SINGLE_LINE_TEXT",
	"renderType": "DEFAULT",
	"requiredFlag": 1,
	"validatorType": "TEXT_LIMIT",
	"visibleType": "EDITABLE",
	"filterFlag": 1
},
{
	"dynamicFlag": 0,
	"fieldDesc": "审批状态",
	"fieldDisplayName": "审批状态",
	"fieldExtra": "{}",
	"fieldName": "审批状态",
	"fieldType": "SINGLE_LINE_TEXT",
	"renderType": "DEFAULT",
	"requiredFlag": 1,
	"validatorType": "TEXT_LIMIT",
	"visibleType": "EDITABLE",
	"filterFlag": 1
}]',1, 1,SYSDATE()) ;



-- 更改请示单module name jiarui 20180823
update eh_service_modules set name = '请示单管理' where id = 25000;

-- 更改现网所有的资质都为有资质
update eh_enterprise_customers set aptitude_flag_item_id = 1;
