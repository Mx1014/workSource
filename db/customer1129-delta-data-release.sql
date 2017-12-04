update eh_var_field_items set business_value = 0 where display_name = '新签合同';
update eh_var_field_items set business_value = 1 where display_name = '续约合同';
update eh_var_field_items set business_value = 2 where display_name = '变更合同';

update eh_var_field_item_scopes set business_value = 0 where item_display_name = '新签合同';
update eh_var_field_item_scopes set business_value = 1 where item_display_name = '续约合同';
update eh_var_field_item_scopes set business_value = 2 where item_display_name = '变更合同';

SET @item_id = (SELECT MAX(id) from `eh_var_field_items`);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', '131', '待发起', '1', '2', '1', NOW(), NULL, NULL, 1);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', '131', '正常合同', '1', '2', '1', NOW(), NULL, NULL, 2);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', '131', '审批中', '1', '2', '1', NOW(), NULL, NULL, 3);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', '131', '审批通过', '1', '2', '1', NOW(), NULL, NULL, 4);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', '131', '审批不通过', '1', '2', '1', NOW(), NULL, NULL, 5);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', '131', '即将到期', '1', '2', '1', NOW(), NULL, NULL, 6);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', '131', '已过期', '1', '2', '1', NOW(), NULL, NULL, 7);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', '131', '历史合同', '1', '2', '1', NOW(), NULL, NULL, 8);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', '131', '作废合同', '1', '2', '1', NOW(), NULL, NULL, 9);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', '131', '退约合同', '1', '2', '1', NOW(), NULL, NULL, 10);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', '131', '草稿', '1', '2', '1', NOW(), NULL, NULL, 11);

SET @field_id = (SELECT MAX(id) from `eh_var_fields`);
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'mainBusiness', '主营业务', 'String', '7', '/7', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'branchCompanyName', '分公司名称', 'String', '7', '/7', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'branchRegisteredDate', '分公司登记日期', 'Long', '7', '/7', '0', NULL, '2', '1', NOW(), NULL, NULL, '{"fieldParamType": "datetime", "length": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'legalRepresentativeName', '法人代表名称', 'String', '7', '/7', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'legalRepresentativeContact', '法人联系方式', 'String', '7', '/7', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'shareholderName', '股东姓名', 'String', '7', '/7', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'actualCapitalInjectionSituation', '实际注资情况', 'String', '7', '/7', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'shareholdingSituation', '股权占比情况', 'String', '7', '/7', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

-- 功能表
INSERT INTO eh_service_module_functions(id, module_id, privilege_id) SELECT privilege_id, module_id, privilege_id FROM eh_service_module_privileges where privilege_type = 0;
update eh_service_module_functions f set f.explain = (select description from eh_acl_privileges where id = f.privilege_id);

INSERT INTO `eh_service_module_functions` (`id`, `module_id`, `privilege_id`, `explain`) VALUES ('99', '21100', '0', '同步客户');
INSERT INTO `eh_service_module_functions` (`id`, `module_id`, `privilege_id`, `explain`) VALUES ('98', '21200', '0', '同步合同');
INSERT INTO `eh_service_module_functions` (`id`, `module_id`, `privilege_id`, `explain`) VALUES ('97', '37000', '0', '同步客户资料');
INSERT INTO `eh_service_module_functions` (`id`, `module_id`, `privilege_id`, `explain`) VALUES ('96', '20400', '0', '缴费未出账单tab');


INSERT INTO eh_service_module_exclude_functions (id, namespace_id, module_id, function_id)
    VALUES(1, 999983, 20400, 96);
INSERT INTO eh_service_module_exclude_functions (id, namespace_id, module_id, function_id)
    VALUES(2, 999971, 20400, 96);
    
DROP PROCEDURE if exists create_exclude_function;
delimiter //
CREATE PROCEDURE `create_exclude_function` ()
BEGIN  
  DECLARE ns INTEGER;
  DECLARE done INT DEFAULT FALSE;
  DECLARE cur CURSOR FOR select id from eh_namespaces; 
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
  OPEN cur;
  read_loop: LOOP
                FETCH cur INTO ns;
                IF done THEN
                    LEAVE read_loop;
                END IF;

        SET @exclude_function_id = (SELECT MAX(id) FROM `eh_service_module_exclude_functions`);
        INSERT INTO eh_service_module_exclude_functions (id, namespace_id, module_id, function_id)
            VALUES((@exclude_function_id := @exclude_function_id + 1), ns, 21100, 99);
        INSERT INTO eh_service_module_exclude_functions (id, namespace_id, module_id, function_id)
            VALUES((@exclude_function_id := @exclude_function_id + 1), ns, 21200, 98);
        INSERT INTO eh_service_module_exclude_functions (id, namespace_id, module_id, function_id)
            VALUES((@exclude_function_id := @exclude_function_id + 1), ns, 37000, 97);
  END LOOP;
  CLOSE cur;
END
//
delimiter ;
CALL create_exclude_function;
DROP PROCEDURE if exists create_exclude_function;

delete from eh_service_module_exclude_functions where namespace_id = 999983 and function_id = 99;
delete from eh_service_module_exclude_functions where namespace_id = 999971 and function_id = 99;
delete from eh_service_module_exclude_functions where namespace_id = 999983 and function_id = 98;
delete from eh_service_module_exclude_functions where namespace_id = 999971 and function_id = 97;