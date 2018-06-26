-- 通用脚本  beta 环境执行
-- ADD BY 丁建民 
-- #28874 合同管理（多应用） 产品功能（不同的合同支持不同的部门可见，同时支持一个资源签多份合同）

-- 查询发布过包含合同管理的域空间
SELECT * from eh_service_module_apps where module_id='21200' group by namespace_id; -- 查询发布过包含合同管理的域空间 

-- 生成categoryid
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1001, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '2', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1002, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '11', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1003, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999944', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1004, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999945', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1005, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999946', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1006, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999947', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1007, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999948', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1008, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999949', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1009, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999950', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1010, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999952', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1011, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999954', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1012, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999956', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1013, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999957', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1014, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999958', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1015, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999961', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1016, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999969', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1017, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999970', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1018, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999971', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1019, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999972', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1020, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999980', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1021, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999983', NULL, NULL, '0');
INSERT INTO `eh_contract_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_uri`, `entry_id`, `contract_application_scene`) VALUES  (1022, '0', '0', '0', '合同管理', NULL, NULL, '2', '1', NOW(), '1', NULL, '999992', NULL, NULL, '0');


-- 更新eh_service_module_apps表的instance_config
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1001,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1001' WHERE module_id='21200' and namespace_id='2';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1002,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1002' WHERE module_id='21200' and namespace_id='11';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1003,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1003' WHERE module_id='21200' and namespace_id='999944';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1004,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1004' WHERE module_id='21200' and namespace_id='999945';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1005,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1005' WHERE module_id='21200' and namespace_id='999946';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1006,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1006' WHERE module_id='21200' and namespace_id='999947';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1007,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1007' WHERE module_id='21200' and namespace_id='999948';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1008,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1008' WHERE module_id='21200' and namespace_id='999949';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1009,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1009' WHERE module_id='21200' and namespace_id='999950';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1010,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1010' WHERE module_id='21200' and namespace_id='999952';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1011,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1011' WHERE module_id='21200' and namespace_id='999954';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1012,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1012' WHERE module_id='21200' and namespace_id='999956';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1013,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1013' WHERE module_id='21200' and namespace_id='999957';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1014,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1014' WHERE module_id='21200' and namespace_id='999958';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1015,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1015' WHERE module_id='21200' and namespace_id='999961';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1016,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1016' WHERE module_id='21200' and namespace_id='999969';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1017,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1017' WHERE module_id='21200' and namespace_id='999970';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1018,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1018' WHERE module_id='21200' and namespace_id='999971';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1019,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1019' WHERE module_id='21200' and namespace_id='999972';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1020,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1020' WHERE module_id='21200' and namespace_id='999980';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1021,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1021' WHERE module_id='21200' and namespace_id='999983';
UPDATE `eh_service_module_apps` SET  `instance_config`='{\"categoryId\":1022,\"contractApplicationScene\":0}', `action_type`='13',`custom_tag`='1022' WHERE module_id='21200' and namespace_id='999992';

-- 更新旧合同的categoryid
UPDATE `eh_contracts` SET `category_id`='1001' WHERE namespace_id='2';
UPDATE `eh_contracts` SET `category_id`='1002' WHERE namespace_id='11';
UPDATE `eh_contracts` SET `category_id`='1003' WHERE namespace_id='999944';
UPDATE `eh_contracts` SET `category_id`='1004' WHERE namespace_id='999945';
UPDATE `eh_contracts` SET `category_id`='1005' WHERE namespace_id='999946';
UPDATE `eh_contracts` SET `category_id`='1006' WHERE namespace_id='999947';
UPDATE `eh_contracts` SET `category_id`='1007' WHERE namespace_id='999948';
UPDATE `eh_contracts` SET `category_id`='1008' WHERE namespace_id='999949';
UPDATE `eh_contracts` SET `category_id`='1009' WHERE namespace_id='999950';
UPDATE `eh_contracts` SET `category_id`='1010' WHERE namespace_id='999952';
UPDATE `eh_contracts` SET `category_id`='1011' WHERE namespace_id='999954';
UPDATE `eh_contracts` SET `category_id`='1012' WHERE namespace_id='999956';
UPDATE `eh_contracts` SET `category_id`='1013' WHERE namespace_id='999957';
UPDATE `eh_contracts` SET `category_id`='1014' WHERE namespace_id='999958';
UPDATE `eh_contracts` SET `category_id`='1015' WHERE namespace_id='999961';
UPDATE `eh_contracts` SET `category_id`='1016' WHERE namespace_id='999969';
UPDATE `eh_contracts` SET `category_id`='1017' WHERE namespace_id='999970';
UPDATE `eh_contracts` SET `category_id`='1018' WHERE namespace_id='999971';
UPDATE `eh_contracts` SET `category_id`='1019' WHERE namespace_id='999972';
UPDATE `eh_contracts` SET `category_id`='1020' WHERE namespace_id='999980';
UPDATE `eh_contracts` SET `category_id`='1021' WHERE namespace_id='999983';
UPDATE `eh_contracts` SET `category_id`='1022' WHERE namespace_id='999992';

-- 更新合同基础参数设置
UPDATE `eh_contract_params` SET `category_id`='1001' WHERE namespace_id='2';
UPDATE `eh_contract_params` SET `category_id`='1002' WHERE namespace_id='11';
UPDATE `eh_contract_params` SET `category_id`='1003' WHERE namespace_id='999944';
UPDATE `eh_contract_params` SET `category_id`='1004' WHERE namespace_id='999945';
UPDATE `eh_contract_params` SET `category_id`='1005' WHERE namespace_id='999946';
UPDATE `eh_contract_params` SET `category_id`='1006' WHERE namespace_id='999947';
UPDATE `eh_contract_params` SET `category_id`='1007' WHERE namespace_id='999948';
UPDATE `eh_contract_params` SET `category_id`='1008' WHERE namespace_id='999949';
UPDATE `eh_contract_params` SET `category_id`='1009' WHERE namespace_id='999950';
UPDATE `eh_contract_params` SET `category_id`='1010' WHERE namespace_id='999952';
UPDATE `eh_contract_params` SET `category_id`='1011' WHERE namespace_id='999954';
UPDATE `eh_contract_params` SET `category_id`='1012' WHERE namespace_id='999956';
UPDATE `eh_contract_params` SET `category_id`='1013' WHERE namespace_id='999957';
UPDATE `eh_contract_params` SET `category_id`='1014' WHERE namespace_id='999958';
UPDATE `eh_contract_params` SET `category_id`='1015' WHERE namespace_id='999961';
UPDATE `eh_contract_params` SET `category_id`='1016' WHERE namespace_id='999969';
UPDATE `eh_contract_params` SET `category_id`='1017' WHERE namespace_id='999970';
UPDATE `eh_contract_params` SET `category_id`='1018' WHERE namespace_id='999971';
UPDATE `eh_contract_params` SET `category_id`='1019' WHERE namespace_id='999972';
UPDATE `eh_contract_params` SET `category_id`='1020' WHERE namespace_id='999980';
UPDATE `eh_contract_params` SET `category_id`='1021' WHERE namespace_id='999983';
UPDATE `eh_contract_params` SET `category_id`='1022' WHERE namespace_id='999992';

-- 更新表单 ，categoryid为空会用原来的表单，
UPDATE `eh_var_field_scopes` SET `category_id`='1001' WHERE module_name='contract' and namespace_id=2; 
UPDATE `eh_var_field_scopes` SET `category_id`='1002' WHERE module_name='contract' and namespace_id=11; 
UPDATE `eh_var_field_scopes` SET `category_id`='1003' WHERE module_name='contract' and namespace_id=999944; 
UPDATE `eh_var_field_scopes` SET `category_id`='1004' WHERE module_name='contract' and namespace_id=999945; 
UPDATE `eh_var_field_scopes` SET `category_id`='1005' WHERE module_name='contract' and namespace_id=999946; 
UPDATE `eh_var_field_scopes` SET `category_id`='1006' WHERE module_name='contract' and namespace_id=999947; 
UPDATE `eh_var_field_scopes` SET `category_id`='1007' WHERE module_name='contract' and namespace_id=999948; 
UPDATE `eh_var_field_scopes` SET `category_id`='1008' WHERE module_name='contract' and namespace_id=999949; 
UPDATE `eh_var_field_scopes` SET `category_id`='1009' WHERE module_name='contract' and namespace_id=999950; 
UPDATE `eh_var_field_scopes` SET `category_id`='1010' WHERE module_name='contract' and namespace_id=999952; 
UPDATE `eh_var_field_scopes` SET `category_id`='1011' WHERE module_name='contract' and namespace_id=999954; 
UPDATE `eh_var_field_scopes` SET `category_id`='1012' WHERE module_name='contract' and namespace_id=999956; 
UPDATE `eh_var_field_scopes` SET `category_id`='1013' WHERE module_name='contract' and namespace_id=999957; 
UPDATE `eh_var_field_scopes` SET `category_id`='1014' WHERE module_name='contract' and namespace_id=999958; 
UPDATE `eh_var_field_scopes` SET `category_id`='1015' WHERE module_name='contract' and namespace_id=999961; 
UPDATE `eh_var_field_scopes` SET `category_id`='1016' WHERE module_name='contract' and namespace_id=999969; 
UPDATE `eh_var_field_scopes` SET `category_id`='1017' WHERE module_name='contract' and namespace_id=999970; 
UPDATE `eh_var_field_scopes` SET `category_id`='1018' WHERE module_name='contract' and namespace_id=999971; 
UPDATE `eh_var_field_scopes` SET `category_id`='1019' WHERE module_name='contract' and namespace_id=999972; 
UPDATE `eh_var_field_scopes` SET `category_id`='1020' WHERE module_name='contract' and namespace_id=999980; 
UPDATE `eh_var_field_scopes` SET `category_id`='1021' WHERE module_name='contract' and namespace_id=999983; 
UPDATE `eh_var_field_scopes` SET `category_id`='1022' WHERE module_name='contract' and namespace_id=999992; 

UPDATE `eh_var_field_item_scopes` SET `category_id`='1001' WHERE module_name='contract' and namespace_id=2;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1002' WHERE module_name='contract' and namespace_id=11;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1003' WHERE module_name='contract' and namespace_id=999944;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1004' WHERE module_name='contract' and namespace_id=999945;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1005' WHERE module_name='contract' and namespace_id=999946;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1006' WHERE module_name='contract' and namespace_id=999947;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1007' WHERE module_name='contract' and namespace_id=999948;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1008' WHERE module_name='contract' and namespace_id=999949;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1009' WHERE module_name='contract' and namespace_id=999950;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1010' WHERE module_name='contract' and namespace_id=999952;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1011' WHERE module_name='contract' and namespace_id=999954;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1012' WHERE module_name='contract' and namespace_id=999956;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1013' WHERE module_name='contract' and namespace_id=999957;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1014' WHERE module_name='contract' and namespace_id=999958;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1015' WHERE module_name='contract' and namespace_id=999961;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1016' WHERE module_name='contract' and namespace_id=999969;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1017' WHERE module_name='contract' and namespace_id=999970;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1018' WHERE module_name='contract' and namespace_id=999971;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1019' WHERE module_name='contract' and namespace_id=999972;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1020' WHERE module_name='contract' and namespace_id=999980;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1021' WHERE module_name='contract' and namespace_id=999983;
UPDATE `eh_var_field_item_scopes` SET `category_id`='1022' WHERE module_name='contract' and namespace_id=999992;

UPDATE `eh_var_field_group_scopes` SET `category_id`='1001' WHERE module_name='contract' and namespace_id=2;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1002' WHERE module_name='contract' and namespace_id=11;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1003' WHERE module_name='contract' and namespace_id=999944;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1004' WHERE module_name='contract' and namespace_id=999945;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1005' WHERE module_name='contract' and namespace_id=999946;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1006' WHERE module_name='contract' and namespace_id=999947;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1007' WHERE module_name='contract' and namespace_id=999948;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1008' WHERE module_name='contract' and namespace_id=999949;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1009' WHERE module_name='contract' and namespace_id=999950;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1010' WHERE module_name='contract' and namespace_id=999952;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1011' WHERE module_name='contract' and namespace_id=999954;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1012' WHERE module_name='contract' and namespace_id=999956;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1013' WHERE module_name='contract' and namespace_id=999957;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1014' WHERE module_name='contract' and namespace_id=999958;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1015' WHERE module_name='contract' and namespace_id=999961;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1016' WHERE module_name='contract' and namespace_id=999969;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1017' WHERE module_name='contract' and namespace_id=999970;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1018' WHERE module_name='contract' and namespace_id=999971;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1019' WHERE module_name='contract' and namespace_id=999972;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1020' WHERE module_name='contract' and namespace_id=999980;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1021' WHERE module_name='contract' and namespace_id=999983;
UPDATE `eh_var_field_group_scopes` SET `category_id`='1022' WHERE module_name='contract' and namespace_id=999992;

-- 更新工作流相关的表
UPDATE `eh_flows` SET `owner_id`='1001' WHERE  module_id='21200' AND namespace_id=2;
UPDATE `eh_flows` SET `owner_id`='1002' WHERE  module_id='21200' AND namespace_id=11;
UPDATE `eh_flows` SET `owner_id`='1003' WHERE  module_id='21200' AND namespace_id=999944;
UPDATE `eh_flows` SET `owner_id`='1004' WHERE  module_id='21200' AND namespace_id=999945;
UPDATE `eh_flows` SET `owner_id`='1005' WHERE  module_id='21200' AND namespace_id=999946;
UPDATE `eh_flows` SET `owner_id`='1006' WHERE  module_id='21200' AND namespace_id=999947;
UPDATE `eh_flows` SET `owner_id`='1007' WHERE  module_id='21200' AND namespace_id=999948;
UPDATE `eh_flows` SET `owner_id`='1008' WHERE  module_id='21200' AND namespace_id=999949;
UPDATE `eh_flows` SET `owner_id`='1009' WHERE  module_id='21200' AND namespace_id=999950;
UPDATE `eh_flows` SET `owner_id`='1010' WHERE  module_id='21200' AND namespace_id=999952;
UPDATE `eh_flows` SET `owner_id`='1011' WHERE  module_id='21200' AND namespace_id=999954;
UPDATE `eh_flows` SET `owner_id`='1012' WHERE  module_id='21200' AND namespace_id=999956;
UPDATE `eh_flows` SET `owner_id`='1013' WHERE  module_id='21200' AND namespace_id=999957;
UPDATE `eh_flows` SET `owner_id`='1014' WHERE  module_id='21200' AND namespace_id=999958;
UPDATE `eh_flows` SET `owner_id`='1015' WHERE  module_id='21200' AND namespace_id=999961;
UPDATE `eh_flows` SET `owner_id`='1016' WHERE  module_id='21200' AND namespace_id=999969;
UPDATE `eh_flows` SET `owner_id`='1017' WHERE  module_id='21200' AND namespace_id=999970;
UPDATE `eh_flows` SET `owner_id`='1018' WHERE  module_id='21200' AND namespace_id=999971;
UPDATE `eh_flows` SET `owner_id`='1019' WHERE  module_id='21200' AND namespace_id=999972;
UPDATE `eh_flows` SET `owner_id`='1020' WHERE  module_id='21200' AND namespace_id=999980;
UPDATE `eh_flows` SET `owner_id`='1021' WHERE  module_id='21200' AND namespace_id=999983;
UPDATE `eh_flows` SET `owner_id`='1022' WHERE  module_id='21200' AND namespace_id=999992;

UPDATE `eh_flow_cases` SET `owner_id`='1001' WHERE module_id='21200' AND namespace_id=2;
UPDATE `eh_flow_cases` SET `owner_id`='1002' WHERE module_id='21200' AND namespace_id=11;
UPDATE `eh_flow_cases` SET `owner_id`='1003' WHERE module_id='21200' AND namespace_id=999944;
UPDATE `eh_flow_cases` SET `owner_id`='1004' WHERE module_id='21200' AND namespace_id=999945;
UPDATE `eh_flow_cases` SET `owner_id`='1005' WHERE module_id='21200' AND namespace_id=999946;
UPDATE `eh_flow_cases` SET `owner_id`='1006' WHERE module_id='21200' AND namespace_id=999947;
UPDATE `eh_flow_cases` SET `owner_id`='1007' WHERE module_id='21200' AND namespace_id=999948;
UPDATE `eh_flow_cases` SET `owner_id`='1008' WHERE module_id='21200' AND namespace_id=999949;
UPDATE `eh_flow_cases` SET `owner_id`='1009' WHERE module_id='21200' AND namespace_id=999950;
UPDATE `eh_flow_cases` SET `owner_id`='1010' WHERE module_id='21200' AND namespace_id=999952;
UPDATE `eh_flow_cases` SET `owner_id`='1011' WHERE module_id='21200' AND namespace_id=999954;
UPDATE `eh_flow_cases` SET `owner_id`='1012' WHERE module_id='21200' AND namespace_id=999956;
UPDATE `eh_flow_cases` SET `owner_id`='1013' WHERE module_id='21200' AND namespace_id=999957;
UPDATE `eh_flow_cases` SET `owner_id`='1014' WHERE module_id='21200' AND namespace_id=999958;
UPDATE `eh_flow_cases` SET `owner_id`='1015' WHERE module_id='21200' AND namespace_id=999961;
UPDATE `eh_flow_cases` SET `owner_id`='1016' WHERE module_id='21200' AND namespace_id=999969;
UPDATE `eh_flow_cases` SET `owner_id`='1017' WHERE module_id='21200' AND namespace_id=999970;
UPDATE `eh_flow_cases` SET `owner_id`='1018' WHERE module_id='21200' AND namespace_id=999971;
UPDATE `eh_flow_cases` SET `owner_id`='1019' WHERE module_id='21200' AND namespace_id=999972;
UPDATE `eh_flow_cases` SET `owner_id`='1020' WHERE module_id='21200' AND namespace_id=999980;
UPDATE `eh_flow_cases` SET `owner_id`='1021' WHERE module_id='21200' AND namespace_id=999983;
UPDATE `eh_flow_cases` SET `owner_id`='1022' WHERE module_id='21200' AND namespace_id=999992;


-- 插入合同与缴费关系。



SELECT * from eh_service_module_apps where module_id='21200' group by namespace_id; -- 查询发布过包含合同管理的域空间
SELECT 	* FROM eh_service_module_app_mappings;

-- END BY 丁建民 