-- merge from forum2.6 by yanjun 201712121010 start
-- 在post表中更新模块和入口信息
-- 活动帖子
UPDATE eh_forum_posts SET module_type = 2, module_category_id = activity_category_id WHERE activity_category_id IS NOT NULL AND activity_category_id != 0;
-- 论坛帖子
UPDATE eh_forum_posts SET module_type = 1, module_category_id = forum_entry_id WHERE forum_entry_id IS NOT NULL AND category_id != 1003  AND (activity_category_id IS NULL OR activity_category_id = 0);
-- 公告帖子
UPDATE eh_forum_posts SET module_type = 3 WHERE category_id = 1003;

-- 我-我的发布，按钮是否需要展示
SET @id = (SELECT MAX(id) FROM eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@id := @id + 1), 'my.publish.flag', 0, 'my.publish.flag 0-hide, 1-display', '999973', NULL);

-- 更新banner的覆盖策略 add by yanjun 20171211
UPDATE eh_banners SET apply_policy = 0 WHERE apply_policy = 3;

-- merge from forum2.6 by yanjun 201712121010 end

-- merge from customer1129 by xiongying20171212
UPDATE eh_var_field_items SET business_value = 0 WHERE display_name = '新签合同';
UPDATE eh_var_field_items SET business_value = 1 WHERE display_name = '续约合同';
UPDATE eh_var_field_items SET business_value = 2 WHERE display_name = '变更合同';

UPDATE eh_var_field_item_scopes SET business_value = 0 WHERE item_display_name = '新签合同';
UPDATE eh_var_field_item_scopes SET business_value = 1 WHERE item_display_name = '续约合同';
UPDATE eh_var_field_item_scopes SET business_value = 2 WHERE item_display_name = '变更合同';

SET @item_id = (SELECT MAX(id) FROM `eh_var_field_items`);
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

SET @field_id = (SELECT MAX(id) FROM `eh_var_fields`);
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'mainBusiness', '主营业务', 'String', '7', '/7', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'branchCompanyName', '分公司名称', 'String', '7', '/7', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'branchRegisteredDate', '分公司登记日期', 'Long', '7', '/7', '0', NULL, '2', '1', NOW(), NULL, NULL, '{"fieldParamType": "datetime", "length": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'legalRepresentativeName', '法人代表名称', 'String', '7', '/7', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'legalRepresentativeContact', '法人联系方式', 'String', '7', '/7', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'shareholderName', '股东姓名', 'String', '7', '/7', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'actualCapitalInjectionSituation', '实际注资情况', 'String', '7', '/7', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'shareholdingSituation', '股权占比情况', 'String', '7', '/7', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

-- 功能表
INSERT INTO eh_service_module_functions(id, module_id, privilege_id) SELECT privilege_id, module_id, privilege_id FROM eh_service_module_privileges WHERE privilege_type = 0;
UPDATE eh_service_module_functions f SET f.explain = (SELECT description FROM eh_acl_privileges WHERE id = f.privilege_id);

INSERT INTO `eh_service_module_functions` (`id`, `module_id`, `privilege_id`, `explain`) VALUES ('99', '21100', '0', '同步客户');
INSERT INTO `eh_service_module_functions` (`id`, `module_id`, `privilege_id`, `explain`) VALUES ('98', '21200', '0', '同步合同');
INSERT INTO `eh_service_module_functions` (`id`, `module_id`, `privilege_id`, `explain`) VALUES ('97', '37000', '0', '同步客户资料');
INSERT INTO `eh_service_module_functions` (`id`, `module_id`, `privilege_id`, `explain`) VALUES ('96', '20400', '0', '缴费未出账单tab');


INSERT INTO eh_service_module_exclude_functions (id, namespace_id, module_id, function_id)
    VALUES(1, 999983, 20400, 96);
INSERT INTO eh_service_module_exclude_functions (id, namespace_id, module_id, function_id)
    VALUES(2, 999971, 20400, 96);
    
DROP PROCEDURE IF EXISTS create_exclude_function;
DELIMITER //
CREATE PROCEDURE `create_exclude_function` ()
BEGIN  
  DECLARE ns INTEGER;
  DECLARE done INT DEFAULT FALSE;
  DECLARE cur CURSOR FOR SELECT id FROM eh_namespaces; 
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
DELIMITER ;
CALL create_exclude_function;
DROP PROCEDURE IF EXISTS create_exclude_function;

DELETE FROM eh_service_module_exclude_functions WHERE namespace_id = 999983 AND function_id = 99;
DELETE FROM eh_service_module_exclude_functions WHERE namespace_id = 999971 AND function_id = 99;
DELETE FROM eh_service_module_exclude_functions WHERE namespace_id = 999983 AND function_id = 98;
DELETE FROM eh_service_module_exclude_functions WHERE namespace_id = 999971 AND function_id = 97;

-- 银行账号 & 税务信息
SET @field_id = (SELECT MAX(id) FROM `eh_var_fields`);
SET @item_id = (SELECT MAX(id) FROM `eh_var_field_items`);
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'taxName', '报税人', 'String', '2', '/2/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'taxNo', '报税人税号', 'String', '2', '/2/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'taxAddress', '地址', 'String', '2', '/2/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 64}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'taxPhone', '联系电话', 'String', '2', '/2/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'taxBank', '开户行名称', 'String', '2', '/2/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 64}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'taxBankNo', '开户行账号', 'String', '2', '/2/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 64}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'taxPayerTypeId', '报税人类型', 'Long', '2', '/2/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"select\", \"length\": 32}');

INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '个人', '1', '2', '1', NOW(), NULL, NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '一般纳税人', '2', '2', '1', NOW(), NULL, NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '小规模纳税人', '3', '2', '1', NOW(), NULL, NULL, NULL);

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'bankName', '开户行名称', 'String', '3', '/3/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'branchName', '开户网点', 'String', '3', '/3/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'accountHolder', '开户人', 'String', '3', '/3/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'accountNumber', '账号', 'String', '3', '/3/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'accountNumberTypeId', '账号类型', 'Long', '3', '/3/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"select\", \"length\": 32}');

INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '中国工商银行', '1', '2', '1', NOW(), NULL, NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '中国农业银行', '2', '2', '1', NOW(), NULL, NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '中国银行', '3', '2', '1', NOW(), NULL, NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '中国建设银行', '4', '2', '1', NOW(), NULL, NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '交通银行', '5', '2', '1', NOW(), NULL, NULL, NULL);
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'branchProvince', '开户行所在省', 'String', '3', '/3/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'branchCity', '开户行所在市', 'String', '3', '/3/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'accountTypeId', '账户类型', 'Long', '3', '/3/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"select\", \"length\": 32}');

INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '个人', '1', '2', '1', NOW(), NULL, NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '公司', '2', '2', '1', NOW(), NULL, NULL, NULL);
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'memo', '备注', 'String', '3', '/3/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 1024}');

-- merge from customer1129 by xiongying20171212 end

-- 物品搬迁 add by sw 20171212
INSERT INTO `eh_web_menus` (id, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
	VALUES ('49200', '物品搬迁', '40000', NULL, NULL, '1', '2', '/40000/49200', 'park', '470', '49200', '2', NULL, 'module');
INSERT INTO `eh_web_menus` (id, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
	VALUES ('49202', '申请列表', '49200', NULL, 'react:/goods-move/apply-list', '0', '2', '/40000/49200/49202', 'park', '471', '49200', '3', NULL, 'module');
INSERT INTO `eh_web_menus` (id, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
	VALUES ('49204', '工作流设置', '49200', NULL, 'react:/working-flow/flow-list/goods-move/49200', '0', '2', '/40000/49200/49204', 'park', '475', '49200', '3', NULL, 'module');

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
  VALUES ('809000', '物品搬迁记录', '800000', NULL, 'react:/goods-move/apply-list/organization', '0', '2', '/800000/809000', 'organization', '890', '49200', '2', NULL, 'module');

INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('relocation', '1', 'zh_CN', '物品搬迁', '物品搬迁', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('relocation', '2', 'zh_CN', '物品搬迁工作流申请人显示内容', '搬迁物品：${items}共${totalNum}件\r\n搬迁时间：${relocationDate}', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('relocation', '3', 'zh_CN', '物品搬迁工作流处理人显示内容', '申请人：${requestorName}  企业名称：${requestorEnterpriseName}\r\n搬迁物品：${items}共${totalNum}件\r\n搬迁时间：${relocationDate}', '0');

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`)
	VALUES (49200, '物品搬迁', 40000, '/40000/49200', 1, 2, 2, 0, UTC_TIME(), NULL, NULL, UTC_TIME(), 0, 0, 0, 0);

SET @flow_predefined_params_id = IFNULL((SELECT MAX(id) FROM `eh_flow_predefined_params`), 1);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
	VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), '0', '0', '', '49200', 'any-module', 'flow_node', '通过审核', '通过审核', '{\"nodeType\":\"APPROVED\"}', '2', NULL, NULL, NULL, NULL);

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ('relocation.flowCase.url', 'zl://workflow/detail?flowCaseId=%s&flowUserType=%s&moduleId=49200', NULL, '0', NULL);


-- 园区入驻多入口 add by sw 20171212
UPDATE eh_web_menus SET data_type = 'react:/project-intro/project-list/1' WHERE id = 40103;
UPDATE eh_web_menus SET data_type = 'projects_introduce/1' WHERE id = 40105;
UPDATE eh_web_menus SET data_type = 'rent_manage/1' WHERE id = 40110;
UPDATE eh_web_menus SET data_type = 'enter_apply/1' WHERE id = 40120;

UPDATE eh_enterprise_op_requests SET category_id = 1;
UPDATE eh_lease_promotions SET category_id = 1;
UPDATE eh_lease_projects SET category_id = 1;
UPDATE eh_lease_project_communities SET category_id = 1;
UPDATE eh_lease_issuers SET category_id = 1;
UPDATE eh_lease_form_requests SET category_id = 1;
UPDATE eh_lease_configs SET category_id = 1;
UPDATE eh_lease_buildings SET category_id = 1;

UPDATE eh_configurations SET `value` = '/park-entry/dist/index.html?hideNavigationBar=1#/project_intro/%s/%s/%s' WHERE `name` = 'apply.entry.lease.project.detail.url';

-- dengs,20171212,服务联盟消息格式修改
UPDATE eh_locale_templates SET TEXT = '您收到一条${categoryName}的申请；

服务名称：${serviceAllianceName}
提交者信息：
预订人：${creatorName}
手机号：${creatorMobile}
公司名称：${creatorOrganization}

提交的信息：
${notemessage}
您可以登录管理后台查看详情
' WHERE scope = 'serviceAlliance.request.notification' AND `code` IN (1,2);

-- 新增错误提示 by wentian
SET @local_id = (SELECT MAX(`id`) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@local_id:=@local_id+1, 'assetv2', '10007', 'zh_CN', '存在已支付的账单，请刷新后再尝试支付');





-- add by wh 考勤菜单分二级

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) 
VALUES('50601','打卡记录','50600',NULL,'react:/attendance-management/attendance-record/1','1','2','/50000/50600/50601','park','543','50600','3',NULL,'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) 
VALUES('50602','考勤规则','50600',NULL,'react:/attendance-management/attendance-record/2','1','2','/50000/50600/50602','park','544','50600','3',NULL,'module');

SET @scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50601', '打卡记录', 'EhNamespaces', '999971', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50602', '考勤规则', 'EhNamespaces', '999971', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50601', '打卡记录', 'EhNamespaces', '999975', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50602', '考勤规则', 'EhNamespaces', '999975', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50601', '打卡记录', 'EhNamespaces', '999970', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50602', '考勤规则', 'EhNamespaces', '999970', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50601', '打卡记录', 'EhNamespaces', '999981', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50602', '考勤规则', 'EhNamespaces', '999981', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50601', '打卡记录', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50602', '考勤规则', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50601', '打卡记录', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50602', '考勤规则', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50601', '打卡记录', 'EhNamespaces', '1', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50602', '考勤规则', 'EhNamespaces', '1', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50601', '打卡记录', 'EhNamespaces', '999978', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50602', '考勤规则', 'EhNamespaces', '999978', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50601', '打卡记录', 'EhNamespaces', '1000000', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50602', '考勤规则', 'EhNamespaces', '1000000', '2'); 
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50601', '打卡记录', 'EhNamespaces', '999958', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50602', '考勤规则', 'EhNamespaces', '999958', '2'); 
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50601', '打卡记录', 'EhNamespaces', '999965', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50602', '考勤规则', 'EhNamespaces', '999965', '2'); 
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50601', '打卡记录', 'EhNamespaces', '999972', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50602', '考勤规则', 'EhNamespaces', '999972', '2'); 
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50601', '打卡记录', 'EhNamespaces', '999969', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50602', '考勤规则', 'EhNamespaces', '999969', '2'); 
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50601', '打卡记录', 'EhNamespaces', '999973', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50602', '考勤规则', 'EhNamespaces', '999973', '2'); 
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50601', '打卡记录', 'EhNamespaces', '999974', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@scope_id:=@scope_id+1), '50602', '考勤规则', 'EhNamespaces', '999974', '2');   
 