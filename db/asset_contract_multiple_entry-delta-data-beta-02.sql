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

-- END BY 丁建民 


-- 通用脚本  
-- ADD BY 杨崇鑫
-- #28874 物业缴费（多应用） 产品功能  缴费旧数据迁移

-- 查询发布过包含物业缴费的域空间
SELECT * from eh_service_module_apps where module_id='20400' group by namespace_id;

-- 生成categoryid
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1001, 1001, UTC_TIMESTAMP(), 1, 2, '{\"url\":\"https://beta.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1001}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1002, 1002, UTC_TIMESTAMP(), 1, 11, '{\"categoryId\":1002}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1003, 1003, UTC_TIMESTAMP(), 1, 999944, '{\"categoryId\":1003}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1004, 1004, UTC_TIMESTAMP(), 1, 999945, '{\"categoryId\":1004}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1005, 1005, UTC_TIMESTAMP(), 1, 999946, '{\"categoryId\":1005}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1006, 1006, UTC_TIMESTAMP(), 1, 999947, '{\"categoryId\":1006}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1007, 1007, UTC_TIMESTAMP(), 1, 999948, '{\"categoryId\":1007}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1008, 1008, UTC_TIMESTAMP(), 1, 999949, '{\"url\":\"${home.url}/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1008}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1009, 1009, UTC_TIMESTAMP(), 1, 999950, '{\"url\":\"https://beta.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1009}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1010, 1010, UTC_TIMESTAMP(), 1, 999951, '{\"categoryId\":1010}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1011, 1011, UTC_TIMESTAMP(), 1, 999952, '{\"url\":\"https://beta.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1011}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1012, 1012, UTC_TIMESTAMP(), 1, 999953, '{\"categoryId\":1012}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1013, 1013, UTC_TIMESTAMP(), 1, 999954, '{\"categoryId\":1013}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1014, 1014, UTC_TIMESTAMP(), 1, 999956, '{\"categoryId\":1014}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1015, 1015, UTC_TIMESTAMP(), 1, 999957, '{\"url\":\"https://beta.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1015}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1016, 1016, UTC_TIMESTAMP(), 1, 999958, '{\"categoryId\":1016}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1017, 1017, UTC_TIMESTAMP(), 1, 999961, '{\"url\":\"${home.url}/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1017}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1018, 1018, UTC_TIMESTAMP(), 1, 999962, '{\"categoryId\":1018}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1019, 1019, UTC_TIMESTAMP(), 1, 999967, '{\"categoryId\":1019}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1020, 1020, UTC_TIMESTAMP(), 1, 999969, '{\"categoryId\":1020}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1021, 1021, UTC_TIMESTAMP(), 1, 999970, '{\"url\":\"https://beta.zuolin.com/property-management/build/index.html?hideNavigationBar\u003d1\u0026name\u003d1#/verify_account#sign_suffix\",\"categoryId\":1021}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1022, 1022, UTC_TIMESTAMP(), 1, 999971, '{\"url\":\"https://beta.zuolin.com/property-management/build/index.html?hideNavigationBar\u003d1\u0026name\u003d1#/verify_account#sign_suffix\",\"categoryId\":1022}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1023, 1023, UTC_TIMESTAMP(), 1, 999972, '{\"categoryId\":1023}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1024, 1024, UTC_TIMESTAMP(), 1, 999975, '{\"categoryId\":1024}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1025, 1025, UTC_TIMESTAMP(), 1, 999980, '{\"categoryId\":1025}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1026, 1026, UTC_TIMESTAMP(), 1, 999983, '{\"url\":\"https://beta.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1026}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1027, 1027, UTC_TIMESTAMP(), 1, 999992, '{\"categoryId\":1027}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
VALUES (1028, 1028, UTC_TIMESTAMP(), 1, 999993, '{\"categoryId\":1028}');
INSERT INTO `eh_asset_app_categories`(`id`, `category_id`, `create_time`, `create_uid`, `namespace_id`, `instance_flag`) 
 VALUES (1029, 1029, UTC_TIMESTAMP(), 1, 999938, '{\"categoryId\":1029}');

-- 更新eh_service_module_apps表的instance_config
UPDATE `eh_service_module_apps` SET `instance_config`='{\"url\":\"https://beta.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1001}' 
 WHERE module_id='20400' and namespace_id='2';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1002}' 
 WHERE module_id='20400' and namespace_id='11';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1003}' 
 WHERE module_id='20400' and namespace_id='999944';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1004}' 
 WHERE module_id='20400' and namespace_id='999945';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1005}' 
 WHERE module_id='20400' and namespace_id='999946';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1006}' 
 WHERE module_id='20400' and namespace_id='999947';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1007}' 
 WHERE module_id='20400' and namespace_id='999948';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"url\":\"${home.url}/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1008}' 
 WHERE module_id='20400' and namespace_id='999949';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"url\":\"https://beta.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1009}' 
 WHERE module_id='20400' and namespace_id='999950';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1010}' 
 WHERE module_id='20400' and namespace_id='999951';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"url\":\"https://beta.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1011}' 
 WHERE module_id='20400' and namespace_id='999952';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1012}' 
 WHERE module_id='20400' and namespace_id='999953';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1013}' 
 WHERE module_id='20400' and namespace_id='999954';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1014}' 
 WHERE module_id='20400' and namespace_id='999956';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"url\":\"https://beta.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1015}' 
 WHERE module_id='20400' and namespace_id='999957';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"url\":\"https://beta.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1016}' 
 WHERE module_id='20400' and namespace_id='999958';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"url\":\"${home.url}/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1017}' 
 WHERE module_id='20400' and namespace_id='999961';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1018}' 
 WHERE module_id='20400' and namespace_id='999962';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1019}' 
 WHERE module_id='20400' and namespace_id='999967';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1020}' 
 WHERE module_id='20400' and namespace_id='999969';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"url\":\"https://beta.zuolin.com/property-management/build/index.html?hideNavigationBar\u003d1\u0026name\u003d1#/verify_account#sign_suffix\",\"categoryId\":1021}' 
 WHERE module_id='20400' and namespace_id='999970';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"url\":\"https://beta.zuolin.com/property-management/build/index.html?hideNavigationBar\u003d1\u0026name\u003d1#/verify_account#sign_suffix\",\"categoryId\":1022}' 
 WHERE module_id='20400' and namespace_id='999971';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1023}' 
 WHERE module_id='20400' and namespace_id='999972';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1024}' 
 WHERE module_id='20400' and namespace_id='999975';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1025}' 
 WHERE module_id='20400' and namespace_id='999980';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"url\":\"https://beta.zuolin.com/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix\",\"categoryId\":1026}' 
 WHERE module_id='20400' and namespace_id='999983';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1027}' 
 WHERE module_id='20400' and namespace_id='999992';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1028}' 
 WHERE module_id='20400' and namespace_id='999993';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"categoryId\":1029}' 
 WHERE module_id='20400' and namespace_id='999938';

-- 更新eh_payment_bills表的categoryid
update eh_payment_bills set category_id=1001  where namespace_id=2;
update eh_payment_bills set category_id=1002  where namespace_id=11;
update eh_payment_bills set category_id=1003  where namespace_id=999944;
update eh_payment_bills set category_id=1004  where namespace_id=999945;
update eh_payment_bills set category_id=1005  where namespace_id=999946;
update eh_payment_bills set category_id=1006  where namespace_id=999947;
update eh_payment_bills set category_id=1007  where namespace_id=999948;
update eh_payment_bills set category_id=1008  where namespace_id=999949;
update eh_payment_bills set category_id=1009  where namespace_id=999950;
update eh_payment_bills set category_id=1010  where namespace_id=999951;
update eh_payment_bills set category_id=1011  where namespace_id=999952;
update eh_payment_bills set category_id=1012  where namespace_id=999953;
update eh_payment_bills set category_id=1013  where namespace_id=999954;
update eh_payment_bills set category_id=1014  where namespace_id=999956;
update eh_payment_bills set category_id=1015  where namespace_id=999957;
update eh_payment_bills set category_id=1016  where namespace_id=999958;
update eh_payment_bills set category_id=1017  where namespace_id=999961;
update eh_payment_bills set category_id=1018  where namespace_id=999962;
update eh_payment_bills set category_id=1019  where namespace_id=999967;
update eh_payment_bills set category_id=1020  where namespace_id=999969;
update eh_payment_bills set category_id=1021  where namespace_id=999970;
update eh_payment_bills set category_id=1022  where namespace_id=999971;
update eh_payment_bills set category_id=1023  where namespace_id=999972;
update eh_payment_bills set category_id=1024  where namespace_id=999975;
update eh_payment_bills set category_id=1025  where namespace_id=999980;
update eh_payment_bills set category_id=1026  where namespace_id=999983;
update eh_payment_bills set category_id=1027  where namespace_id=999992;
update eh_payment_bills set category_id=1028  where namespace_id=999993;
update eh_payment_bills set category_id=1029  where namespace_id=999938;

-- 更新 eh_payment_notice_config 表的categoryid
update eh_payment_notice_config set category_id=1001  where namespace_id=2;
update eh_payment_notice_config set category_id=1002  where namespace_id=11;
update eh_payment_notice_config set category_id=1003  where namespace_id=999944;
update eh_payment_notice_config set category_id=1004  where namespace_id=999945;
update eh_payment_notice_config set category_id=1005  where namespace_id=999946;
update eh_payment_notice_config set category_id=1006  where namespace_id=999947;
update eh_payment_notice_config set category_id=1007  where namespace_id=999948;
update eh_payment_notice_config set category_id=1008  where namespace_id=999949;
update eh_payment_notice_config set category_id=1009  where namespace_id=999950;
update eh_payment_notice_config set category_id=1010  where namespace_id=999951;
update eh_payment_notice_config set category_id=1011  where namespace_id=999952;
update eh_payment_notice_config set category_id=1012  where namespace_id=999953;
update eh_payment_notice_config set category_id=1013  where namespace_id=999954;
update eh_payment_notice_config set category_id=1014  where namespace_id=999956;
update eh_payment_notice_config set category_id=1015  where namespace_id=999957;
update eh_payment_notice_config set category_id=1016  where namespace_id=999958;
update eh_payment_notice_config set category_id=1017  where namespace_id=999961;
update eh_payment_notice_config set category_id=1018  where namespace_id=999962;
update eh_payment_notice_config set category_id=1019  where namespace_id=999967;
update eh_payment_notice_config set category_id=1020  where namespace_id=999969;
update eh_payment_notice_config set category_id=1021  where namespace_id=999970;
update eh_payment_notice_config set category_id=1022  where namespace_id=999971;
update eh_payment_notice_config set category_id=1023  where namespace_id=999972;
update eh_payment_notice_config set category_id=1024  where namespace_id=999975;
update eh_payment_notice_config set category_id=1025  where namespace_id=999980;
update eh_payment_notice_config set category_id=1026  where namespace_id=999983;
update eh_payment_notice_config set category_id=1027  where namespace_id=999992;
update eh_payment_notice_config set category_id=1028  where namespace_id=999993;
update eh_payment_notice_config set category_id=1029  where namespace_id=999938;

-- 更新 eh_payment_charging_item_scopes 表的categoryid
update eh_payment_charging_item_scopes set category_id=1001  where namespace_id=2;
update eh_payment_charging_item_scopes set category_id=1002  where namespace_id=11;
update eh_payment_charging_item_scopes set category_id=1003  where namespace_id=999944;
update eh_payment_charging_item_scopes set category_id=1004  where namespace_id=999945;
update eh_payment_charging_item_scopes set category_id=1005  where namespace_id=999946;
update eh_payment_charging_item_scopes set category_id=1006  where namespace_id=999947;
update eh_payment_charging_item_scopes set category_id=1007  where namespace_id=999948;
update eh_payment_charging_item_scopes set category_id=1008  where namespace_id=999949;
update eh_payment_charging_item_scopes set category_id=1009  where namespace_id=999950;
update eh_payment_charging_item_scopes set category_id=1010  where namespace_id=999951;
update eh_payment_charging_item_scopes set category_id=1011  where namespace_id=999952;
update eh_payment_charging_item_scopes set category_id=1012  where namespace_id=999953;
update eh_payment_charging_item_scopes set category_id=1013  where namespace_id=999954;
update eh_payment_charging_item_scopes set category_id=1014  where namespace_id=999956;
update eh_payment_charging_item_scopes set category_id=1015  where namespace_id=999957;
update eh_payment_charging_item_scopes set category_id=1016  where namespace_id=999958;
update eh_payment_charging_item_scopes set category_id=1017  where namespace_id=999961;
update eh_payment_charging_item_scopes set category_id=1018  where namespace_id=999962;
update eh_payment_charging_item_scopes set category_id=1019  where namespace_id=999967;
update eh_payment_charging_item_scopes set category_id=1020  where namespace_id=999969;
update eh_payment_charging_item_scopes set category_id=1021  where namespace_id=999970;
update eh_payment_charging_item_scopes set category_id=1022  where namespace_id=999971;
update eh_payment_charging_item_scopes set category_id=1023  where namespace_id=999972;
update eh_payment_charging_item_scopes set category_id=1024  where namespace_id=999975;
update eh_payment_charging_item_scopes set category_id=1025  where namespace_id=999980;
update eh_payment_charging_item_scopes set category_id=1026  where namespace_id=999983;
update eh_payment_charging_item_scopes set category_id=1027  where namespace_id=999992;
update eh_payment_charging_item_scopes set category_id=1028  where namespace_id=999993;
update eh_payment_charging_item_scopes set category_id=1029  where namespace_id=999938;

-- 更新 eh_payment_charging_standards_scopes 表的categoryid
update eh_payment_charging_standards_scopes set category_id=1001  where namespace_id=2;
update eh_payment_charging_standards_scopes set category_id=1002  where namespace_id=11;
update eh_payment_charging_standards_scopes set category_id=1003  where namespace_id=999944;
update eh_payment_charging_standards_scopes set category_id=1004  where namespace_id=999945;
update eh_payment_charging_standards_scopes set category_id=1005  where namespace_id=999946;
update eh_payment_charging_standards_scopes set category_id=1006  where namespace_id=999947;
update eh_payment_charging_standards_scopes set category_id=1007  where namespace_id=999948;
update eh_payment_charging_standards_scopes set category_id=1008  where namespace_id=999949;
update eh_payment_charging_standards_scopes set category_id=1009  where namespace_id=999950;
update eh_payment_charging_standards_scopes set category_id=1010  where namespace_id=999951;
update eh_payment_charging_standards_scopes set category_id=1011  where namespace_id=999952;
update eh_payment_charging_standards_scopes set category_id=1012  where namespace_id=999953;
update eh_payment_charging_standards_scopes set category_id=1013  where namespace_id=999954;
update eh_payment_charging_standards_scopes set category_id=1014  where namespace_id=999956;
update eh_payment_charging_standards_scopes set category_id=1015  where namespace_id=999957;
update eh_payment_charging_standards_scopes set category_id=1016  where namespace_id=999958;
update eh_payment_charging_standards_scopes set category_id=1017  where namespace_id=999961;
update eh_payment_charging_standards_scopes set category_id=1018  where namespace_id=999962;
update eh_payment_charging_standards_scopes set category_id=1019  where namespace_id=999967;
update eh_payment_charging_standards_scopes set category_id=1020  where namespace_id=999969;
update eh_payment_charging_standards_scopes set category_id=1021  where namespace_id=999970;
update eh_payment_charging_standards_scopes set category_id=1022  where namespace_id=999971;
update eh_payment_charging_standards_scopes set category_id=1023  where namespace_id=999972;
update eh_payment_charging_standards_scopes set category_id=1024  where namespace_id=999975;
update eh_payment_charging_standards_scopes set category_id=1025  where namespace_id=999980;
update eh_payment_charging_standards_scopes set category_id=1026  where namespace_id=999983;
update eh_payment_charging_standards_scopes set category_id=1027  where namespace_id=999992;
update eh_payment_charging_standards_scopes set category_id=1028  where namespace_id=999993;
update eh_payment_charging_standards_scopes set category_id=1029  where namespace_id=999938;

-- 更新 eh_payment_bill_groups 表的categoryid
update eh_payment_bill_groups set category_id=1001  where namespace_id=2;
update eh_payment_bill_groups set category_id=1002  where namespace_id=11;
update eh_payment_bill_groups set category_id=1003  where namespace_id=999944;
update eh_payment_bill_groups set category_id=1004  where namespace_id=999945;
update eh_payment_bill_groups set category_id=1005  where namespace_id=999946;
update eh_payment_bill_groups set category_id=1006  where namespace_id=999947;
update eh_payment_bill_groups set category_id=1007  where namespace_id=999948;
update eh_payment_bill_groups set category_id=1008  where namespace_id=999949;
update eh_payment_bill_groups set category_id=1009  where namespace_id=999950;
update eh_payment_bill_groups set category_id=1010  where namespace_id=999951;
update eh_payment_bill_groups set category_id=1011  where namespace_id=999952;
update eh_payment_bill_groups set category_id=1012  where namespace_id=999953;
update eh_payment_bill_groups set category_id=1013  where namespace_id=999954;
update eh_payment_bill_groups set category_id=1014  where namespace_id=999956;
update eh_payment_bill_groups set category_id=1015  where namespace_id=999957;
update eh_payment_bill_groups set category_id=1016  where namespace_id=999958;
update eh_payment_bill_groups set category_id=1017  where namespace_id=999961;
update eh_payment_bill_groups set category_id=1018  where namespace_id=999962;
update eh_payment_bill_groups set category_id=1019  where namespace_id=999967;
update eh_payment_bill_groups set category_id=1020  where namespace_id=999969;
update eh_payment_bill_groups set category_id=1021  where namespace_id=999970;
update eh_payment_bill_groups set category_id=1022  where namespace_id=999971;
update eh_payment_bill_groups set category_id=1023  where namespace_id=999972;
update eh_payment_bill_groups set category_id=1024  where namespace_id=999975;
update eh_payment_bill_groups set category_id=1025  where namespace_id=999980;
update eh_payment_bill_groups set category_id=1026  where namespace_id=999983;
update eh_payment_bill_groups set category_id=1027  where namespace_id=999992;
update eh_payment_bill_groups set category_id=1028  where namespace_id=999993;
update eh_payment_bill_groups set category_id=1029  where namespace_id=999938;

-- 更新 eh_payment_bill_items 表的categoryid
update eh_payment_bill_items set category_id=1001  where namespace_id=2;
update eh_payment_bill_items set category_id=1002  where namespace_id=11;
update eh_payment_bill_items set category_id=1003  where namespace_id=999944;
update eh_payment_bill_items set category_id=1004  where namespace_id=999945;
update eh_payment_bill_items set category_id=1005  where namespace_id=999946;
update eh_payment_bill_items set category_id=1006  where namespace_id=999947;
update eh_payment_bill_items set category_id=1007  where namespace_id=999948;
update eh_payment_bill_items set category_id=1008  where namespace_id=999949;
update eh_payment_bill_items set category_id=1009  where namespace_id=999950;
update eh_payment_bill_items set category_id=1010  where namespace_id=999951;
update eh_payment_bill_items set category_id=1011  where namespace_id=999952;
update eh_payment_bill_items set category_id=1012  where namespace_id=999953;
update eh_payment_bill_items set category_id=1013  where namespace_id=999954;
update eh_payment_bill_items set category_id=1014  where namespace_id=999956;
update eh_payment_bill_items set category_id=1015  where namespace_id=999957;
update eh_payment_bill_items set category_id=1016  where namespace_id=999958;
update eh_payment_bill_items set category_id=1017  where namespace_id=999961;
update eh_payment_bill_items set category_id=1018  where namespace_id=999962;
update eh_payment_bill_items set category_id=1019  where namespace_id=999967;
update eh_payment_bill_items set category_id=1020  where namespace_id=999969;
update eh_payment_bill_items set category_id=1021  where namespace_id=999970;
update eh_payment_bill_items set category_id=1022  where namespace_id=999971;
update eh_payment_bill_items set category_id=1023  where namespace_id=999972;
update eh_payment_bill_items set category_id=1024  where namespace_id=999975;
update eh_payment_bill_items set category_id=1025  where namespace_id=999980;
update eh_payment_bill_items set category_id=1026  where namespace_id=999983;
update eh_payment_bill_items set category_id=1027  where namespace_id=999992;
update eh_payment_bill_items set category_id=1028  where namespace_id=999993;
update eh_payment_bill_items set category_id=1029  where namespace_id=999938;

-- 初始化缴费、合同之间的映射关系
set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 2, 1001, 1001, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 11, 1002, 1002, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999944, 1003, 1003, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999945, 1004, 1004, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999946, 1005, 1005, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999947, 1006, 1006, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999948, 1007, 1007, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999949, 1008, 1008, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999950, 1009, 1009, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999952, 1011, 1010, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999954, 1013, 1011, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999956, 1014, 1012, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999957, 1015, 1013, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999958, 1016, 1014, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999961, 1017, 1015, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999969, 1020, 1016, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999970, 1021, 1017, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999971, 1022, 1018, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999972, 1023, 1019, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999980, 1025, 1020, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999983, 1026, 1021, NULL, 1, 2, NOW(), 1, NULL, NULL);

set @id = IFNULL((select MAX(`id`) from `eh_asset_module_app_mappings`),0);
INSERT INTO `eh_asset_module_app_mappings` 
(`id`, `namespace_id`, `asset_category_id`, `contract_category_id`, `energy_category_id`, `energy_flag`, `status`, `create_time`, `create_uid`, `update_time`, `update_uid`) 
VALUES 
(@id:=@id+1, 999992, 1027, 1022, NULL, 1, 2, NOW(), 1, NULL, NULL);
-- END BY 杨崇鑫

