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


-- 积分 add by xq.tian 2017/12/14

SET @eh_configurations_id = IFNULL((SELECT MAX(id) FROM `eh_configurations`), 0);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ((@eh_configurations_id := @eh_configurations_id + 1), 'point.detail.path', '/integral-management/build/index.html?systemId=%s&ehnavigatorstyle=2#/home#sign_suffix', 'point detail path', 0, '');

-- 积分的基础数据 --------------------- start
-- 积分规则分类
SET @point_rule_categories_id = IFNULL((SELECT MAX(id) FROM `eh_point_rule_categories`), 0);
INSERT INTO `eh_point_rule_categories` (`id`, `namespace_id`, `display_name`, `status`, `server_id`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES ((@point_rule_categories_id := @point_rule_categories_id + 1), 0, '账户', 2, 'default', NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rule_categories` (`id`, `namespace_id`, `display_name`, `status`, `server_id`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES ((@point_rule_categories_id := @point_rule_categories_id + 1), 0, '论坛', 2, 'default', NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rule_categories` (`id`, `namespace_id`, `display_name`, `status`, `server_id`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES ((@point_rule_categories_id := @point_rule_categories_id + 1), 0, '活动', 2, 'default', NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rule_categories` (`id`, `namespace_id`, `display_name`, `status`, `server_id`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES ((@point_rule_categories_id := @point_rule_categories_id + 1), 0, '俱乐部', 2, 'default', NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rule_categories` (`id`, `namespace_id`, `display_name`, `status`, `server_id`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES ((@point_rule_categories_id := @point_rule_categories_id + 1), 0, '行业协会', 2, 'default', NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rule_categories` (`id`, `namespace_id`, `display_name`, `status`, `server_id`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES ((@point_rule_categories_id := @point_rule_categories_id + 1), 0, '意见反馈', 2, 'default', NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rule_categories` (`id`, `namespace_id`, `display_name`, `status`, `server_id`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES ((@point_rule_categories_id := @point_rule_categories_id + 1), 0, '电商', 2, 'default', NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rule_categories` (`id`, `namespace_id`, `display_name`, `status`, `server_id`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES ((@point_rule_categories_id := @point_rule_categories_id + 1), 0, '园区服务', 2, 'default', NULL, NULL, NULL, NULL);

-- 积分规则
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (1, 0, 0, 1, 0, '注册成功', '注册成功', 1, 10, 2, '{"times":1}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (2, 0, 0, 1, 0, '认证成功', '首次通过认证', 1, 10, 2, '{"times":1}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (3, 0, 0, 1, 0, '完善个人信息', '完善个人信息', 1, 10, 2, '{"times":1}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (4, 0, 0, 2, 0, '发布帖子', '发布帖子', 1, 5, 1, '{"times":3}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (5, 0, 0, 2, 0, '评论帖子', '发布评论', 1, 2, 1, '{"times":5}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (6, 0, 0, 2, 0, '点赞帖子', '点赞帖子', 1, 1, 1, '{"times":5}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (7, 0, 0, 2, 0, '分享帖子', '分享帖子', 1, 1, 1, '{"times":2}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (8, 0, 0, 2, 0, '参与投票', '参与投票', 1, 2, 1, '{"times":5}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (9, 0, 0, 3, 0, '成功发起活动', '发起活动', 1, 10, 1, '{"times":1}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (10, 0, 0, 3, 0, '报名活动', '报名活动', 1, 3, 1, '{"times":2}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (11, 0, 0, 3, 0, '分享活动', '分享活动', 1, 2, 1, '{"times":5}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (12, 0, 0, 3, 0, '评论活动', '评论活动', 1, 2, 1, '{"times":5}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (13, 0, 0, 3, 0, '点赞活动', '点赞活动', 1, 1, 1, '{"times":5}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (14, 0, 0, 4, 0, '成功发起俱乐部', '发起俱乐部', 1, 10, 1, '{"times":5}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (15, 0, 0, 4, 0, '成功加入俱乐部', '加入俱乐部', 1, 3, 1, '{"times":5}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (16, 0, 0, 5, 0, '成功发起行业协会', '发起行业协会', 1, 10, 1, '{"times":5}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (17, 0, 0, 5, 0, '成功加入行业协会', '加入行业协会', 1, 3, 1, '{"times":5}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (18, 0, 0, 6, 0, '在意见反馈中发布帖子', '发布意见反馈', 1, 2, 1, '{"times":2}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (19, 0, 0, 7, 0, '电商消费', '电商每天首次购物', 1, 2, 1, '{"times":1}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (20, 0, 0, 7, 0, '连续三天电商消费', '电商连续购物3天', 1, 8, 1, '{"times":1}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (21, 0, 0, 8, 0, '园区资源预约成功', '园区资源预约成功', 1, 2, 1, '{"times":5}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (22, 0, 0, 2, 0, '论坛帖子/评论被举报且管理员核实', '论坛帖子/评论被举报', 2, 10, 1, '{"times":2}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (23, 0, 0, 2, 0, '论坛评论被举报且管理员核实', '论坛评论被举报', 2, 5, 1, '{"times":2}', 0, 2, 0, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (24, 0, 0, 2, 0, '删除帖子', '删除论坛帖子', 2, 5, 1, '{"times":5}', 4, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (25, 0, 0, 2, 0, '删除评论', '删除论坛评论', 2, 2, 1, '{"times":5}', 5, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (26, 0, 0, 2, 0, '取消论坛点赞', '取消论坛点赞', 2, 1, 1, '{"times":5}', 6, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (27, 0, 0, 3, 0, '活动/评论被举报且管理员核实', '活动/评论被举报', 2, 10, 1, '{"times":2}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (28, 0, 0, 3, 0, '删除活动', '删除活动', 2, 10, 1, '{"times":2}', 9, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (29, 0, 0, 3, 0, '取消活动报名', '取消活动报名', 2, 2, 1, '{"times":2}', 10, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (30, 0, 0, 3, 0, '删除活动评论', '删除活动评论', 2, 2, 1, '{"times":2}', 12, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (31, 0, 0, 3, 0, '取消活动点赞', '取消活动点赞', 2, 1, 1, '{"times":2}', 13, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (32, 0, 0, 4, 0, '解散俱乐部', '解散俱乐部', 2, 10, 1, '{"times":2}', 14, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (33, 0, 0, 4, 0, '退出俱乐部', '退出俱乐部', 2, 2, 1, '{"times":5}', 15, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (34, 0, 0, 5, 0, '解散行业协会', '解散行业协会', 2, 10, 1, '{"times":2}', 16, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (35, 0, 0, 5, 0, '退出行业协会', '退出行业协会', 2, 2, 1, '{"times":2}', 17, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (36, 0, 0, 6, 0, '删除意见反馈帖子', '删除意见反馈', 2, 2, 1, '{"times":2}', 18, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (37, 0, 0, 7, 0, '消费抵现', '电商购物抵现', 2, 0, 3, ' ', 0, 2, 0, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (38, 0, 0, 8, 0, '取消资源预约', '取消资源预约', 2, 2, 1, '{"times":5}', 21, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (39, 0, 0, 1, 0, '每日首次登录', '每日首次登录', 1, 2, 1, '{"times":1}', 0, 2, 1, NULL, NULL, NULL, NULL);

INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (1, 0, 0, 1, 1, 'account.register_success.default');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (2, 0, 0, 1, 2, 'account.auth_success.default');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (3, 0, 0, 1, 3, 'account.complete_info.default');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (4, 0, 0, 2, 4, 'forum.post_create.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (5, 0, 0, 2, 4, 'forum.post_create.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (6, 0, 0, 2, 5, 'forum.comment_create.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (7, 0, 0, 2, 5, 'forum.comment_create.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (8, 0, 0, 2, 6, 'forum.post_like.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (9, 0, 0, 2, 6, 'forum.post_like.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (10, 0, 0, 2, 7, 'forum.post_share.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (11, 0, 0, 2, 7, 'forum.post_share.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (13, 0, 0, 2, 8, 'forum.post_vote.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (14, 0, 0, 3, 9, 'forum.post_create.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (15, 0, 0, 3, 10, 'activity.activity_enter.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (16, 0, 0, 3, 11, 'forum.post_share.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (17, 0, 0, 3, 12, 'forum.comment_create.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (18, 0, 0, 3, 13, 'forum.post_like.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (19, 0, 0, 4, 14, 'club.club_create.0');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (20, 0, 0, 4, 15, 'club.club_join.0');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (21, 0, 0, 5, 16, 'club.club_create.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (22, 0, 0, 5, 17, 'club.club_join.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (23, 0, 0, 6, 18, 'forum.post_create.1001.6');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (24, 0, 0, 7, 19, 'biz.order_pay_complete.default');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (25, 0, 0, 7, 20, 'biz.order_pay_complete.default');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (26, 0, 0, 8, 21, 'rental.resource_apply.default');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (27, 0, 0, 2, 22, 'forum.post_report.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (28, 0, 0, 2, 22, 'forum.post_report.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (29, 0, 0, 2, 23, 'forum.comment_report.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (30, 0, 0, 2, 23, 'forum.comment_report.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (31, 0, 0, 2, 24, 'forum.post_delete.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (32, 0, 0, 2, 24, 'forum.post_delete.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (33, 0, 0, 2, 25, 'forum.comment_delete.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (34, 0, 0, 2, 25, 'forum.comment_delete.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (35, 0, 0, 2, 26, 'forum.post_like_cancel.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (36, 0, 0, 2, 26, 'forum.post_like_cancel.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (37, 0, 0, 3, 27, 'forum.post_report.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (38, 0, 0, 3, 28, 'forum.comment_report.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (39, 0, 0, 3, 29, 'activity.activity_enter_cancel.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (40, 0, 0, 3, 30, 'forum.comment_delete.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (41, 0, 0, 3, 31, 'forum.post_like_cancel.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (42, 0, 0, 4, 32, 'club.club_release.0');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (43, 0, 0, 4, 33, 'club.club_leave.0');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (44, 0, 0, 5, 34, 'club.club_release.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (45, 0, 0, 5, 35, 'club.club_leave.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (46, 0, 0, 6, 36, 'forum.post_delete.1001.6');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (47, 0, 0, 7, 37, 'biz.order_create.default');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (48, 0, 0, 8, 38, 'rental.resource_apply_cancel.default');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (49, 0, 0, 1, 39, 'account.open_app.default');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (50, 0, 0, 2, 4, 'forum.post_create.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (51, 0, 0, 2, 5, 'forum.comment_create.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (52, 0, 0, 2, 6, 'forum.post_like.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (53, 0, 0, 2, 7, 'forum.post_share.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (54, 0, 0, 2, 22, 'forum.post_report.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (55, 0, 0, 2, 23, 'forum.comment_report.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (56, 0, 0, 2, 24, 'forum.post_delete.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (57, 0, 0, 2, 25, 'forum.comment_delete.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (58, 0, 0, 2, 26, 'forum.post_like_cancel.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (59, 0, 0, 3, 28, 'forum.post_delete.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (60, 0, 0, 2, 4, 'forum.post_create.1003');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (61, 0, 0, 2, 5, 'forum.comment_create.1003');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (62, 0, 0, 2, 6, 'forum.post_like.1003');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (63, 0, 0, 2, 7, 'forum.post_share.1003');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (64, 0, 0, 2, 22, 'forum.post_report.1003');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (65, 0, 0, 2, 23, 'forum.comment_report.1003');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (66, 0, 0, 2, 24, 'forum.post_delete.1003');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (67, 0, 0, 2, 25, 'forum.comment_delete.1003');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (68, 0, 0, 2, 26, 'forum.post_like_cancel.1003');


SET @point_actions_id = IFNULL((SELECT MAX(id) FROM `eh_point_actions`), 0);
INSERT INTO `eh_point_actions` (`id`, `namespace_id`, `system_id`, `owner_type`, `owner_id`, `display_name`, `action_type`, `description`, `content`, `status`, `create_time`, `creator_uid`, `update_time`, `update_uid`)
VALUES ((@point_actions_id := @point_actions_id + 1), 0, 0, 'EhPointRules', 1, '用户注册积分消息', 'MESSAGE', '用户注册积分消息', '恭喜您注册成功，获得${points}积分奖励。积分可以在部分消费中抵现。', 2, NULL, NULL, NULL, NULL);
-- 积分的基础数据 --------------------- end

-- 给威新配置的积分系统
INSERT INTO `eh_point_systems` (`id`, `namespace_id`, `display_name`, `point_name`, `point_exchange_flag`, `exchange_point`, `exchange_cash`, `user_agreement`, `status`, `create_time`, `creator_uid`, `update_time`, `update_uid`)
VALUES (84, 999991, '积分系统', '积分', 1, 100, 1, '1. 积分权益实现限制\r\n1.1 积分仅在用户本人于威新LINK+付费抵扣的场景下方具备相应的申明折扣让利功能，积分任何时候均不可用于兑换现金；\r\n\r\n1.2 积分权益仅限本人享有，积分不具备可让与性，不可转赠亦不得为本人以外的其他人士实现积分权益；\r\n\r\n1.3 积分不予开发票：积分属于用户回馈行为，抵消金额的部分不支持开具发票；\r\n\r\n2. 积分权益的退还\r\n2.1 用户应当遵照威新LINK+的规定合法正当地获取积分，合规使用积分。威新LINK+将基于有限的技术手段，对涉嫌以非正当途径获取、使用积分的行为予以坚决打击，任何涉嫌非正当途径获取的积分，威新LINK+均有权随时取消；\r\n\r\n2.2 用户获取积分的前提是“完成应用内指定操作”，因此如用户对获得积分的行为进行了撤销操作，用户积分系统直接扣除通过该行为所获取的全部基本积分，如果涉及部分退款的行为，会员积分系统将按比例扣除相应的积分；\r\n\r\n2.3 用户应当秉承诚实信用原则，如果是因交易行为奖励的积分，在获得积分的订单未完全确认无需退款前，请保持积分账户中充足的积分，以便系统根据会员的授权即时扣除相应的基本积分；', 2, NOW(), NULL, NULL, NULL);

INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (836, 999991, 84, 1, 0, '每日首次登录', '每日首次登录', 1, 2, 1, '{"times":1}', 0, 2, 1, '2017-12-14 13:58:23.377', 195506, '2017-12-14 13:58:28.481', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (837, 999991, 84, 8, 0, '取消资源预约', '取消资源预约', 2, 2, 1, '{"times":5}', 851, 2, 1, '2017-12-14 13:58:23.468', 195506, '2017-12-14 13:58:28.422', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (838, 999991, 84, 6, 0, '删除意见反馈帖子', '删除意见反馈', 2, 2, 1, '{"times":2}', 854, 2, 1, '2017-12-14 13:58:23.552', 195506, '2017-12-14 13:58:28.347', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (839, 999991, 84, 5, 0, '退出行业协会', '退出行业协会', 2, 2, 1, '{"times":2}', 855, 2, 1, '2017-12-14 13:58:23.602', 195506, '2017-12-14 13:58:28.314', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (840, 999991, 84, 5, 0, '解散行业协会', '解散行业协会', 2, 10, 1, '{"times":2}', 856, 2, 1, '2017-12-14 13:58:23.686', 195506, '2017-12-14 13:58:28.072', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (841, 999991, 84, 4, 0, '退出俱乐部', '退出俱乐部', 2, 2, 1, '{"times":5}', 857, 2, 1, '2017-12-14 13:58:23.832', 195506, '2017-12-14 13:58:27.972', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (842, 999991, 84, 4, 0, '解散俱乐部', '解散俱乐部', 2, 10, 1, '{"times":2}', 858, 2, 1, '2017-12-14 13:58:23.886', 195506, '2017-12-14 13:58:27.889', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (843, 999991, 84, 3, 0, '取消活动点赞', '取消活动点赞', 2, 1, 1, '{"times":2}', 859, 2, 1, '2017-12-14 13:58:23.978', 195506, '2017-12-14 13:58:27.847', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (844, 999991, 84, 3, 0, '删除活动评论', '删除活动评论', 2, 2, 1, '{"times":2}', 860, 2, 1, '2017-12-14 13:58:24.087', 195506, '2017-12-14 13:58:27.797', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (845, 999991, 84, 3, 0, '取消活动报名', '取消活动报名', 2, 2, 1, '{"times":2}', 862, 2, 1, '2017-12-14 13:58:24.178', 195506, '2017-12-14 13:58:27.739', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (846, 999991, 84, 3, 0, '活动/评论被举报且管理员核实', '活动/评论被举报', 2, 10, 1, '{"times":2}', 0, 2, 1, '2017-12-14 13:58:24.228', 195506, '2017-12-14 13:58:27.622', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (847, 999991, 84, 2, 0, '取消论坛点赞', '取消论坛点赞', 2, 1, 1, '{"times":5}', 866, 2, 1, '2017-12-14 13:58:24.311', 195506, '2017-12-14 13:58:27.589', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (848, 999991, 84, 2, 0, '删除评论', '删除论坛评论', 2, 2, 1, '{"times":5}', 867, 2, 1, '2017-12-14 13:58:24.361', 195506, '2017-12-14 13:58:27.447', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (849, 999991, 84, 2, 0, '删除帖子', '删除论坛帖子', 2, 5, 1, '{"times":5}', 868, 2, 1, '2017-12-14 13:58:24.411', 195506, '2017-12-14 13:58:27.381', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (850, 999991, 84, 2, 0, '论坛帖子/评论被举报且管理员核实', '论坛帖子/评论被举报', 2, 10, 1, '{"times":2}', 0, 2, 1, '2017-12-14 13:58:24.453', 195506, '2017-12-14 13:58:27.330', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (851, 999991, 84, 8, 0, '园区资源预约成功', '园区资源预约成功', 1, 2, 1, '{"times":5}', 0, 2, 1, '2017-12-14 13:58:24.520', 195506, '2017-12-14 13:58:27.297', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (852, 999991, 84, 7, 0, '连续三天电商消费', '电商连续购物3天', 1, 8, 1, '{"times":1}', 0, 2, 1, '2017-12-14 13:58:24.578', 195506, '2017-12-14 13:58:27.280', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (853, 999991, 84, 7, 0, '电商消费', '电商每天首次购物', 1, 2, 1, '{"times":1}', 0, 2, 1, '2017-12-14 13:58:24.628', 195506, '2017-12-14 13:58:27.255', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (854, 999991, 84, 6, 0, '在意见反馈中发布帖子', '发布意见反馈', 1, 2, 1, '{"times":2}', 0, 2, 1, '2017-12-14 13:58:24.678', 195506, '2017-12-14 13:58:27.222', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (855, 999991, 84, 5, 0, '成功加入行业协会', '加入行业协会', 1, 3, 1, '{"times":5}', 0, 2, 1, '2017-12-14 13:58:24.762', 195506, '2017-12-14 13:58:27.172', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (856, 999991, 84, 5, 0, '成功发起行业协会', '发起行业协会', 1, 10, 1, '{"times":5}', 0, 2, 1, '2017-12-14 13:58:24.837', 195506, '2017-12-14 13:58:27.113', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (857, 999991, 84, 4, 0, '成功加入俱乐部', '加入俱乐部', 1, 3, 1, '{"times":5}', 0, 2, 1, '2017-12-14 13:58:24.978', 195506, '2017-12-14 13:58:27.080', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (858, 999991, 84, 4, 0, '成功发起俱乐部', '发起俱乐部', 1, 10, 1, '{"times":5}', 0, 2, 1, '2017-12-14 13:58:25.104', 195506, '2017-12-14 13:58:27.029', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (859, 999991, 84, 3, 0, '点赞活动', '点赞活动', 1, 1, 1, '{"times":5}', 0, 2, 1, '2017-12-14 13:58:25.220', 195506, '2017-12-14 13:58:26.996', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (860, 999991, 84, 3, 0, '评论活动', '评论活动', 1, 2, 1, '{"times":5}', 0, 2, 1, '2017-12-14 13:58:25.279', 195506, '2017-12-14 13:58:26.971', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (861, 999991, 84, 3, 0, '分享活动', '分享活动', 1, 2, 1, '{"times":5}', 0, 2, 1, '2017-12-14 13:58:25.395', 195506, '2017-12-14 13:58:26.913', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (862, 999991, 84, 3, 0, '报名活动', '报名活动', 1, 3, 1, '{"times":2}', 0, 2, 1, '2017-12-14 13:58:25.429', 195506, '2017-12-14 13:58:26.846', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (863, 999991, 84, 3, 0, '成功发起活动', '发起活动', 1, 10, 1, '{"times":1}', 0, 2, 1, '2017-12-14 13:58:25.495', 195506, '2017-12-14 13:58:26.821', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (864, 999991, 84, 2, 0, '参与投票', '参与投票', 1, 2, 1, '{"times":5}', 0, 2, 1, '2017-12-14 13:58:25.554', 195506, '2017-12-14 13:58:26.788', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (865, 999991, 84, 2, 0, '分享帖子', '分享帖子', 1, 1, 1, '{"times":2}', 0, 2, 1, '2017-12-14 13:58:25.632', 195506, '2017-12-14 13:58:26.746', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (866, 999991, 84, 2, 0, '点赞帖子', '点赞帖子', 1, 1, 1, '{"times":5}', 0, 2, 1, '2017-12-14 13:58:25.779', 195506, '2017-12-14 13:58:26.696', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (867, 999991, 84, 2, 0, '评论帖子', '发布评论', 1, 2, 1, '{"times":5}', 0, 2, 1, '2017-12-14 13:58:25.821', 195506, '2017-12-14 13:58:26.612', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (868, 999991, 84, 2, 0, '发布帖子', '发布帖子', 1, 5, 1, '{"times":3}', 0, 2, 1, '2017-12-14 13:58:25.862', 195506, '2017-12-14 13:58:26.562', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (869, 999991, 84, 1, 0, '完善个人信息', '完善个人信息', 1, 10, 2, '{"times":1}', 0, 2, 1, '2017-12-14 13:58:25.904', 195506, '2017-12-14 13:58:26.487', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (870, 999991, 84, 1, 0, '认证成功', '首次通过认证', 1, 10, 2, '{"times":1}', 0, 2, 1, '2017-12-14 13:58:25.946', 195506, '2017-12-14 13:58:26.371', 195506);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`, `system_id`, `category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (871, 999991, 84, 1, 0, '注册成功', '注册成功', 1, 10, 2, '{"times":1}', 0, 2, 1, '2017-12-14 13:58:26.104', 195506, '2017-12-14 13:58:26.218', 195506);

INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (247, 999991, 84, 1, 871, 'account.register_success.default');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (248, 999991, 84, 1, 870, 'account.auth_success.default');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (249, 999991, 84, 1, 869, 'account.complete_info.default');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (250, 999991, 84, 2, 868, 'forum.post_create.1003');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (251, 999991, 84, 2, 868, 'forum.post_create.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (252, 999991, 84, 2, 868, 'forum.post_create.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (253, 999991, 84, 2, 868, 'forum.post_create.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (254, 999991, 84, 2, 867, 'forum.comment_create.1003');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (255, 999991, 84, 2, 867, 'forum.comment_create.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (256, 999991, 84, 2, 867, 'forum.comment_create.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (257, 999991, 84, 2, 867, 'forum.comment_create.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (258, 999991, 84, 2, 866, 'forum.post_like.1003');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (259, 999991, 84, 2, 866, 'forum.post_like.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (260, 999991, 84, 2, 866, 'forum.post_like.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (261, 999991, 84, 2, 866, 'forum.post_like.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (262, 999991, 84, 2, 865, 'forum.post_share.1003');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (263, 999991, 84, 2, 865, 'forum.post_share.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (264, 999991, 84, 2, 865, 'forum.post_share.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (265, 999991, 84, 2, 865, 'forum.post_share.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (266, 999991, 84, 2, 864, 'forum.post_vote.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (267, 999991, 84, 3, 863, 'forum.post_create.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (268, 999991, 84, 3, 862, 'activity.activity_enter.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (269, 999991, 84, 3, 861, 'forum.post_share.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (270, 999991, 84, 3, 860, 'forum.comment_create.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (271, 999991, 84, 3, 859, 'forum.post_like.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (272, 999991, 84, 4, 858, 'club.club_create.0');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (273, 999991, 84, 4, 857, 'club.club_join.0');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (274, 999991, 84, 5, 856, 'club.club_create.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (275, 999991, 84, 5, 855, 'club.club_join.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (276, 999991, 84, 6, 854, 'forum.post_create.1001.6');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (277, 999991, 84, 7, 853, 'biz.order_pay_complete.default');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (278, 999991, 84, 7, 852, 'biz.order_pay_complete.default');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (279, 999991, 84, 8, 851, 'rental.resource_apply.default');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (280, 999991, 84, 2, 850, 'forum.post_report.1003');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (281, 999991, 84, 2, 850, 'forum.post_report.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (282, 999991, 84, 2, 850, 'forum.post_report.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (283, 999991, 84, 2, 850, 'forum.post_report.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (284, 999991, 84, 2, 849, 'forum.post_delete.1003');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (285, 999991, 84, 2, 849, 'forum.post_delete.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (286, 999991, 84, 2, 849, 'forum.post_delete.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (287, 999991, 84, 2, 849, 'forum.post_delete.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (288, 999991, 84, 2, 848, 'forum.comment_delete.1003');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (289, 999991, 84, 2, 848, 'forum.comment_delete.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (290, 999991, 84, 2, 848, 'forum.comment_delete.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (291, 999991, 84, 2, 848, 'forum.comment_delete.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (292, 999991, 84, 2, 847, 'forum.post_like_cancel.1003');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (293, 999991, 84, 2, 847, 'forum.post_like_cancel.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (294, 999991, 84, 2, 847, 'forum.post_like_cancel.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (295, 999991, 84, 2, 847, 'forum.post_like_cancel.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (296, 999991, 84, 3, 846, 'forum.post_report.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (297, 999991, 84, 3, 845, 'activity.activity_enter_cancel.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (298, 999991, 84, 3, 844, 'forum.comment_delete.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (299, 999991, 84, 3, 843, 'forum.post_like_cancel.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (300, 999991, 84, 4, 842, 'club.club_release.0');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (301, 999991, 84, 4, 841, 'club.club_leave.0');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (302, 999991, 84, 5, 840, 'club.club_release.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (303, 999991, 84, 5, 839, 'club.club_leave.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (304, 999991, 84, 6, 838, 'forum.post_delete.1001.6');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (305, 999991, 84, 8, 837, 'rental.resource_apply_cancel.default');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `system_id`, `category_id`, `rule_id`, `event_name`) VALUES (306, 999991, 84, 1, 836, 'account.open_app.default');

INSERT INTO `eh_point_actions` (`id`, `namespace_id`, `system_id`, `action_type`, `owner_type`, `owner_id`, `display_name`, `description`, `content`, `status`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (16, 999991, 84, 'MESSAGE', 'EhPointRules', 871, '用户注册积分消息', '用户注册积分消息', '恭喜您注册成功，获得${points}积分奖励。积分可以在部分消费中抵现。', 2, '2017-12-14 13:58:28.541', NULL, NULL, NULL);

INSERT INTO `eh_point_tutorials` (`id`, `namespace_id`, `system_id`, `display_name`, `description`, `poster_uri`, `status`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (1, 999991, 84, '社区讨论', '话题/活动/投票', 'cs://1/image/aW1hZ2UvTVRwaU5EWmxOamMwTlRVek5UbGhNemxsTXpWa05tUmpZMkkwTldNMFpUWTRZUQ', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_tutorials` (`id`, `namespace_id`, `system_id`, `display_name`, `description`, `poster_uri`, `status`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (2, 999991, 84, '购物消费', '园区消费攒积分', 'cs://1/image/aW1hZ2UvTVRvNU5HTXdaREJoTTJZNVpHUXhZVGMwTTJRNE5XSTBZVEZsTkdWa1ptVXlOQQ', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_tutorials` (`id`, `namespace_id`, `system_id`, `display_name`, `description`, `poster_uri`, `status`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (3, 999991, 84, '园区服务', '使用公共服务送积分', 'cs://1/image/aW1hZ2UvTVRvM09HRTJZbU5sWXpFek9UUmtaREF5TkRNek1tVTRORFUzT1dJeE5ESXpNdw', 2, NULL, NULL, NULL, NULL);

INSERT INTO `eh_point_goods` (`id`, `namespace_id`, `number`, `display_name`, `poster_uri`, `poster_url`, `detail_url`, `points`, `sold_amount`, `original_price`, `discount_price`, `point_rule`, `status`, `top_status`, `top_time`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (1, 999991, '1', '黑巧克力甜甜圈(白巧克力条纹)', 'cs://1/image/aW1hZ2UvTVRvMU1EUTFNR1U0Wm1SbU5Ea3lOR1EyTjJVNE1HUmpORGMxTVdNd1l6azRaUQ', '', 'http://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fapp%2fuser%2findex.html%3fmallId%3d1999990%26originId%3d1%26fromclient%3d1%23%2fgood%2fdetails%2f14622779991551986272%2f09529987701%2f1#sign_suffix', 15, 0, 8.00, 6.50, '', 2, 2, '2017-12-14 21:15:01.000', NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_goods` (`id`, `namespace_id`, `number`, `display_name`, `poster_uri`, `poster_url`, `detail_url`, `points`, `sold_amount`, `original_price`, `discount_price`, `point_rule`, `status`, `top_status`, `top_time`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (3, 999991, '1', '家常小炒鸡', 'cs://1/image/aW1hZ2UvTVRvNFlXWTRNamM1WXpRMk1UTTVPRFkwTnpVd1l6WTNaVGd3T1RFeE1EazRZdw', '', 'http://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fapp%2fuser%2findex.html%3fmallId%3d1999990%26originId%3d1%26fromclient%3d1%23%2fgood%2fdetails%2f14622779991551986272%2f08522184413%2f1#sign_suffix', 30, 41, 19.00, 16.00, '', 2, 2, '2017-12-12 21:15:05.000', NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_goods` (`id`, `namespace_id`, `number`, `display_name`, `poster_uri`, `poster_url`, `detail_url`, `points`, `sold_amount`, `original_price`, `discount_price`, `point_rule`, `status`, `top_status`, `top_time`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (2, 999991, '1', '抹茶红豆瑞士卷', 'cs://1/image/aW1hZ2UvTVRwak1UazJNREE0TmpreVl6a3lOakprTnpsaVlUWTBOV1EwTXpkak1EY3haQQ', '', 'http://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fapp%2fuser%2findex.html%3fmallId%3d1999990%26originId%3d1%26fromclient%3d1%23%2fgood%2fdetails%2f14622779991551986272%2f09805545175%2f1#sign_suffix', 10, 0, 8.00, 7.00, '', 2, 2, '2017-12-13 21:15:03.000', NULL, NULL, NULL, NULL);

INSERT INTO `eh_point_tutorial_to_point_rule_mappings` (`id`, `namespace_id`, `system_id`, `tutorial_id`, `rule_id`, `description`, `create_time`) VALUES (1, 999991, 1, 1, 4, '', NULL);
INSERT INTO `eh_point_tutorial_to_point_rule_mappings` (`id`, `namespace_id`, `system_id`, `tutorial_id`, `rule_id`, `description`, `create_time`) VALUES (2, 999991, 1, 1, 9, '', NULL);
INSERT INTO `eh_point_tutorial_to_point_rule_mappings` (`id`, `namespace_id`, `system_id`, `tutorial_id`, `rule_id`, `description`, `create_time`) VALUES (3, 999991, 1, 1, 14, '', NULL);
INSERT INTO `eh_point_tutorial_to_point_rule_mappings` (`id`, `namespace_id`, `system_id`, `tutorial_id`, `rule_id`, `description`, `create_time`) VALUES (5, 999991, 1, 1, 8, '', NULL);
INSERT INTO `eh_point_tutorial_to_point_rule_mappings` (`id`, `namespace_id`, `system_id`, `tutorial_id`, `rule_id`, `description`, `create_time`) VALUES (6, 999991, 1, 2, 19, '', NULL);
INSERT INTO `eh_point_tutorial_to_point_rule_mappings` (`id`, `namespace_id`, `system_id`, `tutorial_id`, `rule_id`, `description`, `create_time`) VALUES (7, 999991, 1, 2, 20, '', NULL);
INSERT INTO `eh_point_tutorial_to_point_rule_mappings` (`id`, `namespace_id`, `system_id`, `tutorial_id`, `rule_id`, `description`, `create_time`) VALUES (12, 999991, 1, 3, 1, '', NULL);
INSERT INTO `eh_point_tutorial_to_point_rule_mappings` (`id`, `namespace_id`, `system_id`, `tutorial_id`, `rule_id`, `description`, `create_time`) VALUES (13, 999991, 1, 3, 2, '', NULL);
INSERT INTO `eh_point_tutorial_to_point_rule_mappings` (`id`, `namespace_id`, `system_id`, `tutorial_id`, `rule_id`, `description`, `create_time`) VALUES (14, 999991, 1, 3, 3, '', NULL);