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

-- 回滚banner
-- -- 更新banner的覆盖策略 add by yanjun 20171211
-- UPDATE eh_banners SET apply_policy = 0 WHERE apply_policy = 3;

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

SET @id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
	VALUES ((@id := @id + 1), '999983', '0', '0', '0', '/home', 'Bizs', '物品搬迁', '物品搬迁', 'cs://1/image/aW1hZ2UvTVRveVl6RTFOREF4WTJKbU56ZG1ZMlkyTkdVMk1EUTRaREUwWlRFeU1URXdaUQ', '1', '1', '13', '{\"url\":\"https://core.zuolin.com/goods-move/build/index.html?ns=1000000&hideNavigationBar=1&ehnavigatorstyle=0#home#sign_suffix\"}', '3', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '1', NULL, NULL, '0', NULL, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
	VALUES ((@id := @id + 1), '999983', '0', '0', '0', '/home', 'Bizs', '物品搬迁', '物品搬迁', 'cs://1/image/aW1hZ2UvTVRveVl6RTFOREF4WTJKbU56ZG1ZMlkyTkdVMk1EUTRaREUwWlRFeU1URXdaUQ', '1', '1', '13', '{\"url\":\"https://core.zuolin.com/goods-move/build/index.html?ns=1000000&hideNavigationBar=1&ehnavigatorstyle=0#home#sign_suffix\"}', '3', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '1', NULL, NULL, '0', NULL, NULL);

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
  VALUES((@menu_scope_id := @menu_scope_id + 1),49200,'', 'EhNamespaces', 999983,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
  VALUES((@menu_scope_id := @menu_scope_id + 1),49202,'', 'EhNamespaces', 999983,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
  VALUES((@menu_scope_id := @menu_scope_id + 1),49204,'', 'EhNamespaces', 999983,2);


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

UPDATE eh_web_menus set data_type = 'react:/working-flow/flow-list/rent-manage/40100?moduleType=lease_promotion_1' where id = 40130;
UPDATE eh_flows SET module_type = 'lease_promotion_1' WHERE module_type = 'any-module' AND module_id = 40100;
UPDATE eh_flows SET module_type = 'lease_promotion_1' WHERE module_type = 'any-module' AND module_id = 40100;
UPDATE eh_flow_cases SET module_type = 'lease_promotion_1' WHERE module_type = 'any-module' AND module_id = 40100;
UPDATE eh_flow_evaluates SET module_type = 'lease_promotion_1' WHERE module_type = 'any-module' AND module_id = 40100;


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
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (1, 0, 1, 0, '注册成功', '注册成功', 1, 10, 2, '{"times":1}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (2, 0, 1, 0, '认证成功', '首次通过认证', 1, 10, 2, '{"times":1}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (3, 0, 1, 0, '完善个人信息', '完善个人信息', 1, 10, 2, '{"times":1}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (4, 0, 2, 0, '发布帖子', '发布帖子', 1, 5, 1, '{"times":3}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (5, 0, 2, 0, '评论帖子', '发布评论', 1, 2, 1, '{"times":5}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (6, 0, 2, 0, '点赞帖子', '点赞帖子', 1, 1, 1, '{"times":5}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (7, 0, 2, 0, '分享帖子', '分享帖子', 1, 1, 1, '{"times":2}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (8, 0, 2, 0, '参与投票', '参与投票', 1, 2, 1, '{"times":5}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (9, 0, 3, 0, '成功发起活动', '发起活动', 1, 10, 1, '{"times":1}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (10, 0, 3, 0, '报名活动', '报名活动', 1, 3, 1, '{"times":2}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (11, 0, 3, 0, '分享活动', '分享活动', 1, 2, 1, '{"times":5}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (12, 0, 3, 0, '评论活动', '评论活动', 1, 2, 1, '{"times":5}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (13, 0, 3, 0, '点赞活动', '点赞活动', 1, 1, 1, '{"times":5}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (14, 0, 4, 0, '成功发起俱乐部', '发起俱乐部', 1, 10, 1, '{"times":5}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (15, 0, 4, 0, '成功加入俱乐部', '加入俱乐部', 1, 3, 1, '{"times":5}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (16, 0, 5, 0, '成功发起行业协会', '发起行业协会', 1, 10, 1, '{"times":5}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (17, 0, 5, 0, '成功加入行业协会', '加入行业协会', 1, 3, 1, '{"times":5}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (18, 0, 6, 0, '在意见反馈中发布帖子', '发布意见反馈', 1, 2, 1, '{"times":2}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (19, 0, 7, 0, '电商消费', '电商每天首次购物', 1, 2, 1, '{"times":1}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (20, 0, 7, 0, '连续三天电商消费', '电商连续购物3天', 1, 8, 1, '{"times":1}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (21, 0, 8, 0, '园区资源预约成功', '园区资源预约成功', 1, 2, 1, '{"times":5}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (22, 0, 2, 0, '论坛帖子/评论被举报且管理员核实', '论坛帖子/评论被举报', 2, 10, 1, '{"times":2}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (23, 0, 2, 0, '论坛评论被举报且管理员核实', '论坛评论被举报', 2, 5, 1, '{"times":2}', 0, 2, 0, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (24, 0, 2, 0, '删除帖子', '删除论坛帖子', 2, 3, 1, '{"times":5}', 4, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (25, 0, 2, 0, '删除评论', '删除论坛评论', 2, 2, 1, '{"times":5}', 5, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (26, 0, 2, 0, '取消论坛点赞', '取消论坛点赞', 2, 1, 1, '{"times":5}', 6, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (27, 0, 3, 0, '活动/评论被举报且管理员核实', '活动/评论被举报', 2, 10, 1, '{"times":2}', 0, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (28, 0, 3, 0, '删除活动', '删除活动', 2, 10, 1, '{"times":2}', 9, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (29, 0, 3, 0, '取消活动报名', '取消活动报名', 2, 2, 1, '{"times":2}', 10, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (30, 0, 3, 0, '删除活动评论', '删除活动评论', 2, 2, 1, '{"times":2}', 12, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (31, 0, 3, 0, '取消活动点赞', '取消活动点赞', 2, 1, 1, '{"times":2}', 13, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (32, 0, 4, 0, '解散俱乐部', '解散俱乐部', 2, 10, 1, '{"times":2}', 14, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (33, 0, 4, 0, '退出俱乐部', '退出俱乐部', 2, 2, 1, '{"times":5}', 15, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (34, 0, 5, 0, '解散行业协会', '解散行业协会', 2, 10, 1, '{"times":2}', 16, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (35, 0, 5, 0, '退出行业协会', '退出行业协会', 2, 2, 1, '{"times":2}', 17, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (36, 0, 6, 0, '删除意见反馈帖子', '删除意见反馈', 2, 2, 1, '{"times":2}', 18, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (37, 0, 7, 0, '消费抵现', '电商购物抵现', 2, 0, 3, ' ', 0, 2, 0, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (38, 0, 8, 0, '取消资源预约', '取消资源预约', 2, 2, 1, '{"times":5}', 21, 2, 1, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_rules` (`id`, `namespace_id`,`category_id`, `module_id`, `display_name`, `description`, `arithmetic_type`, `points`, `limit_type`, `limit_data`, `binding_rule_id`, `status`, `display_flag`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (39, 0, 1, 0, '每日首次登录', '每日首次登录', 1, 2, 1, '{"times":1}', 0, 2, 1, NULL, NULL, NULL, NULL);

INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (1, 0, 1, 1, 'account.register_success.default');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (2, 0, 1, 2, 'account.auth_success.default');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (3, 0, 1, 3, 'account.complete_info.default');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (4, 0, 2, 4, 'forum.post_create.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (5, 0, 2, 4, 'forum.post_create.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (6, 0, 2, 5, 'forum.comment_create.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (7, 0, 2, 5, 'forum.comment_create.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (8, 0, 2, 6, 'forum.post_like.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (9, 0, 2, 6, 'forum.post_like.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (10, 0, 2, 7, 'forum.post_share.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (11, 0, 2, 7, 'forum.post_share.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (13, 0, 2, 8, 'forum.post_vote.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (14, 0, 3, 9, 'forum.post_create.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (15, 0, 3, 10, 'activity.activity_enter.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (16, 0, 3, 11, 'forum.post_share.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (17, 0, 3, 12, 'forum.comment_create.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (18, 0, 3, 13, 'forum.post_like.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (19, 0, 4, 14, 'club.club_create.0');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (20, 0, 4, 15, 'club.club_join.0');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (21, 0, 5, 16, 'club.club_create.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (22, 0, 5, 17, 'club.club_join.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (23, 0, 6, 18, 'forum.post_create.1001.6');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (24, 0, 7, 19, 'biz.order_pay_complete.default');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (25, 0, 7, 20, 'biz.order_pay_complete.default');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (26, 0, 8, 21, 'rental.resource_apply.default');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (27, 0, 2, 22, 'forum.post_report.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (28, 0, 2, 22, 'forum.post_report.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (29, 0, 2, 23, 'forum.comment_report.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (30, 0, 2, 23, 'forum.comment_report.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (31, 0, 2, 24, 'forum.post_delete.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (32, 0, 2, 24, 'forum.post_delete.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (33, 0, 2, 25, 'forum.comment_delete.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (34, 0, 2, 25, 'forum.comment_delete.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (35, 0, 2, 26, 'forum.post_like_cancel.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (36, 0, 2, 26, 'forum.post_like_cancel.1011');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (37, 0, 3, 27, 'forum.post_report.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (38, 0, 3, 28, 'forum.comment_report.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (39, 0, 3, 29, 'activity.activity_enter_cancel.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (40, 0, 3, 30, 'forum.comment_delete.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (41, 0, 3, 31, 'forum.post_like_cancel.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (42, 0, 4, 32, 'club.club_release.0');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (43, 0, 4, 33, 'club.club_leave.0');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (44, 0, 5, 34, 'club.club_release.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (45, 0, 5, 35, 'club.club_leave.1');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (46, 0, 6, 36, 'forum.post_delete.1001.6');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (47, 0, 7, 37, 'biz.order_create.default');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (48, 0, 8, 38, 'rental.resource_apply_cancel.default');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (49, 0, 1, 39, 'account.open_app.default');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (50, 0, 2, 4, 'forum.post_create.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (51, 0, 2, 5, 'forum.comment_create.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (52, 0, 2, 6, 'forum.post_like.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (53, 0, 2, 7, 'forum.post_share.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (54, 0, 2, 22, 'forum.post_report.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (55, 0, 2, 23, 'forum.comment_report.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (56, 0, 2, 24, 'forum.post_delete.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (57, 0, 2, 25, 'forum.comment_delete.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (58, 0, 2, 26, 'forum.post_like_cancel.1001');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (59, 0, 3, 28, 'forum.post_delete.1010');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (60, 0, 2, 4, 'forum.post_create.1003');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (61, 0, 2, 5, 'forum.comment_create.1003');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (62, 0, 2, 6, 'forum.post_like.1003');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (63, 0, 2, 7, 'forum.post_share.1003');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (64, 0, 2, 22, 'forum.post_report.1003');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (65, 0, 2, 23, 'forum.comment_report.1003');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (66, 0, 2, 24, 'forum.post_delete.1003');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (67, 0, 2, 25, 'forum.comment_delete.1003');
INSERT INTO `eh_point_rule_to_event_mappings` (`id`, `namespace_id`, `category_id`, `rule_id`, `event_name`) VALUES (68, 0, 2, 26, 'forum.post_like_cancel.1003');


SET @point_actions_id = IFNULL((SELECT MAX(id) FROM `eh_point_actions`), 0);
INSERT INTO `eh_point_actions` (`id`, `namespace_id`, `owner_type`, `owner_id`, `display_name`, `action_type`, `description`, `content`, `status`, `create_time`, `creator_uid`, `update_time`, `update_uid`)
VALUES ((@point_actions_id := @point_actions_id + 1), 0, 'EhPointRules', 1, '用户注册积分消息', 'MESSAGE', '用户注册积分消息', '恭喜您注册成功，获得${points}积分奖励。积分可以在部分消费中抵现。', 2, NULL, NULL, NULL, NULL);
-- 积分的基础数据 --------------------- end

-- 给威新配置的积分系统
INSERT INTO `eh_point_systems` (`id`, `namespace_id`, `display_name`, `point_name`, `point_exchange_flag`, `exchange_point`, `exchange_cash`, `user_agreement`, `status`, `create_time`, `creator_uid`, `update_time`, `update_uid`)
VALUES (84, 999991, '积分系统', '积分', 1, 100, 1, '1. 积分权益实现限制\r\n1.1 积分仅在用户本人于威新LINK+付费抵扣的场景下方具备相应的申明折扣让利功能，积分任何时候均不可用于兑换现金；\r\n\r\n1.2 积分权益仅限本人享有，积分不具备可让与性，不可转赠亦不得为本人以外的其他人士实现积分权益；\r\n\r\n1.3 积分不予开发票：积分属于用户回馈行为，抵消金额的部分不支持开具发票；\r\n\r\n2. 积分权益的退还\r\n2.1 用户应当遵照威新LINK+的规定合法正当地获取积分，合规使用积分。威新LINK+将基于有限的技术手段，对涉嫌以非正当途径获取、使用积分的行为予以坚决打击，任何涉嫌非正当途径获取的积分，威新LINK+均有权随时取消；\r\n\r\n2.2 用户获取积分的前提是“完成应用内指定操作”，因此如用户对获得积分的行为进行了撤销操作，用户积分系统直接扣除通过该行为所获取的全部基本积分，如果涉及部分退款的行为，会员积分系统将按比例扣除相应的积分；\r\n\r\n2.3 用户应当秉承诚实信用原则，如果是因交易行为奖励的积分，在获得积分的订单未完全确认无需退款前，请保持积分账户中充足的积分，以便系统根据会员的授权即时扣除相应的基本积分；', 2, NOW(), NULL, NULL, NULL);

INSERT INTO `eh_point_tutorials` (`id`, `namespace_id`, `system_id`, `display_name`, `description`, `poster_uri`, `status`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (1, 999991, 84, '社区讨论', '话题/活动/投票', 'cs://1/image/aW1hZ2UvTVRwaU5EWmxOamMwTlRVek5UbGhNemxsTXpWa05tUmpZMkkwTldNMFpUWTRZUQ', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_tutorials` (`id`, `namespace_id`, `system_id`, `display_name`, `description`, `poster_uri`, `status`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (2, 999991, 84, '购物消费', '园区消费攒积分', 'cs://1/image/aW1hZ2UvTVRvNU5HTXdaREJoTTJZNVpHUXhZVGMwTTJRNE5XSTBZVEZsTkdWa1ptVXlOQQ', 2, NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_tutorials` (`id`, `namespace_id`, `system_id`, `display_name`, `description`, `poster_uri`, `status`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (3, 999991, 84, '园区服务', '使用公共服务送积分', 'cs://1/image/aW1hZ2UvTVRvM09HRTJZbU5sWXpFek9UUmtaREF5TkRNek1tVTRORFUzT1dJeE5ESXpNdw', 2, NULL, NULL, NULL, NULL);

INSERT INTO `eh_point_goods` (`id`, `namespace_id`, `number`, `display_name`, `poster_uri`, `poster_url`, `detail_url`, `points`, `sold_amount`, `original_price`, `discount_price`, `point_rule`, `status`, `top_status`, `top_time`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (1, 999991, '1', '好丽友.派', 'cs://1/image/aW1hZ2UvTVRvM01ESTNabVF4WWpRMVlqZGxOVFZrTW1ObFpERTJOR00wTVRnM01qZ3hPQQ', '', 'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fapp%2fuser%2findex.html%3ffromclient%3d1%23%2fgood%2fdetails%2f15133062972693658555%2f02555862652%2f1%3f_k%3dzlbiz#sign_suffix', 5, 0, 8.00, 1, '', 2, 2, '2017-12-14 21:15:01.000', NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_goods` (`id`, `namespace_id`, `number`, `display_name`, `poster_uri`, `poster_url`, `detail_url`, `points`, `sold_amount`, `original_price`, `discount_price`, `point_rule`, `status`, `top_status`, `top_time`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (3, 999991, '1', '丹麦蓝罐曲奇饼90g', 'cs://1/image/aW1hZ2UvTVRveU9XUTVOV1E1TXpCaE9ERmhNbUpsTW1JelpUaGpZamRrTUdVMllUZ3hOdw', '', 'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fapp%2fuser%2findex.html%3ffromclient%3d1%23%2fgood%2fdetails%2f15133062972693658555%2f11336905126%2f1%3f_k%3dzlbiz#sign_suffix', 20, 0, 9.50, 7.50, '', 2, 2, '2017-12-12 21:15:05.000', NULL, NULL, NULL, NULL);
INSERT INTO `eh_point_goods` (`id`, `namespace_id`, `number`, `display_name`, `poster_uri`, `poster_url`, `detail_url`, `points`, `sold_amount`, `original_price`, `discount_price`, `point_rule`, `status`, `top_status`, `top_time`, `create_time`, `creator_uid`, `update_time`, `update_uid`) VALUES (2, 999991, '1', '迪乐司马来西亚芝麻酥100g', 'cs://1/image/aW1hZ2UvTVRvNE16UmxOalJqWldVek4yVTRNekJpTlRBNFl6Um1Zak13WXpreVpqUTNOdw', '', 'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fapp%2fuser%2findex.html%3ffromclient%3d1%23%2fgood%2fdetails%2f15133062972693658555%2f11807220144%2f1%3f_k%3dzlbiz#sign_suffix', 10, 0, 7.00, 6.00, '', 2, 2, '2017-12-13 21:15:03.000', NULL, NULL, NULL, NULL);

INSERT INTO `eh_point_tutorial_to_point_rule_mappings` (`id`, `namespace_id`, `system_id`, `tutorial_id`, `rule_id`, `description`, `create_time`) VALUES (1, 999991, 84, 1, 4, '', NULL);
INSERT INTO `eh_point_tutorial_to_point_rule_mappings` (`id`, `namespace_id`, `system_id`, `tutorial_id`, `rule_id`, `description`, `create_time`) VALUES (2, 999991, 84, 1, 9, '', NULL);
INSERT INTO `eh_point_tutorial_to_point_rule_mappings` (`id`, `namespace_id`, `system_id`, `tutorial_id`, `rule_id`, `description`, `create_time`) VALUES (3, 999991, 84, 1, 14, '', NULL);
INSERT INTO `eh_point_tutorial_to_point_rule_mappings` (`id`, `namespace_id`, `system_id`, `tutorial_id`, `rule_id`, `description`, `create_time`) VALUES (5, 999991, 84, 1, 8, '', NULL);
INSERT INTO `eh_point_tutorial_to_point_rule_mappings` (`id`, `namespace_id`, `system_id`, `tutorial_id`, `rule_id`, `description`, `create_time`) VALUES (6, 999991, 84, 2, 19, '', NULL);
INSERT INTO `eh_point_tutorial_to_point_rule_mappings` (`id`, `namespace_id`, `system_id`, `tutorial_id`, `rule_id`, `description`, `create_time`) VALUES (7, 999991, 84, 2, 20, '', NULL);
INSERT INTO `eh_point_tutorial_to_point_rule_mappings` (`id`, `namespace_id`, `system_id`, `tutorial_id`, `rule_id`, `description`, `create_time`) VALUES (12, 999991, 84, 3, 1, '', NULL);
INSERT INTO `eh_point_tutorial_to_point_rule_mappings` (`id`, `namespace_id`, `system_id`, `tutorial_id`, `rule_id`, `description`, `create_time`) VALUES (13, 999991, 84, 3, 2, '', NULL);
INSERT INTO `eh_point_tutorial_to_point_rule_mappings` (`id`, `namespace_id`, `system_id`, `tutorial_id`, `rule_id`, `description`, `create_time`) VALUES (14, 999991, 84, 3, 3, '', NULL);

  -- by dengs,云打印连接更换20171214
SELECT * FROM  eh_launch_pad_items  WHERE action_data LIKE "%cloud-print%" AND action_type IN (13,14);
UPDATE eh_launch_pad_items SET action_data = '{"url":"http://core.zuolin.com/cloud-print/build/index.html#/home#sign_suffix"}' WHERE action_data LIKE "%cloud-print%" AND action_type IN (13,14);
update eh_siyin_print_settings a,eh_siyin_print_settings b SET a.print_course = b.scan_copy_course,a.scan_copy_course=b.print_course WHERE a.setting_type = 2 AND b.setting_type = 2 AND a.id = b.id;

-- 更新园区控制的模块
UPDATE eh_service_modules SET module_control_type = 'community_control' WHERE id IN (10100,10200,10400,10600,10700,10800,10850,11000,12200,20100,20400,20600,20800,20900,21100,21200,30500,31000,32000,32500,33000,37000,40100,40200,40300,40400,40500,40600,40700,40800,40900,41000,41100,41200,41400,41500,41600,41700,49100);
-- 更新OA控制的模块
UPDATE eh_service_modules SET module_control_type = 'org_control' WHERE id IN( 50100,50400,50600,50700,52000);
-- 更新不受限制控制的模块
UPDATE eh_service_modules SET module_control_type = 'unlimit_control' WHERE id IN(10750,13000,21000,30600,34000,35000,40750,41330,50300,50500,50900,51000);


-- 更新园区控制的模块关联的应用
UPDATE eh_service_module_apps SET module_control_type = 'community_control' WHERE module_id IN (10100,10200,10400,10600,10700,10800,10850,11000,12200,20100,20400,20600,20800,20900,21100,21200,30500,31000,32000,32500,33000,37000,40100,40200,40300,40400,40500,40600,40700,40800,40900,41000,41100,41200,41400,41500,41600,41700,49100);
-- 更新OA控制的模块
UPDATE eh_service_module_apps SET module_control_type = 'org_control' WHERE module_id IN( 50100,50400,50600,50700,52000);
-- 更新不受限制控制的模块
UPDATE eh_service_module_apps SET module_control_type = 'unlimit_control' WHERE module_id IN(10750,13000,21000,30600,34000,35000,40750,41330,50300,50500,50900,51000);

-- 物业缴费 权限细化脚本 by wentian
-- 物业缴费，查看权限id是否被占用，如果被占用，此段先不要执行，告知author by wentian
SELECT 1 FROM `eh_acl_privileges` WHERE  ID IN (40078,40073,40074,40075,40076,40077);
-- 物业缴费，删除以往的冗杂数据，物业缴费以及其子菜单，但不包括熊颖的计价条款设置 by wentian
DELETE FROM eh_acl_privileges WHERE id IN (SELECT privilege_id FROM eh_service_module_privileges WHERE module_id IN (SELECT id FROM eh_service_modules WHERE path LIKE '/20000/20400%' AND id != 20422));
DELETE FROM eh_service_module_privileges WHERE module_id IN (SELECT id FROM eh_service_modules WHERE path LIKE '/20000/20400%' AND id != 20422);
DELETE FROM eh_service_modules WHERE path LIKE '/20000/20400%' AND id != 20422;
-- 物业缴费，模块和权限配置
INSERT INTO `eh_service_modules`
(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`,`module_control_type`)
VALUES
(20400, '物业缴费', '20000', '/20000/20400', '1', '2', '2', '0', NOW(),NULL, '13', NOW(), '0', '0', '0', '0','community_control');
-- 三级菜单，没有action_type
INSERT INTO `eh_service_modules`
(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`,`module_control_type`)
VALUES
(204011, '账单管理', '20400', '/20000/20400/204011', '1', '3', '2', '0', NOW(),NULL, NULL, NOW(), '0', '0', '0', '0','community_control');
INSERT INTO `eh_service_modules`
(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`,`module_control_type`)
VALUES
(204021, '账单统计', '20400', '/20000/20400/204021', '1', '3', '2', '0', NOW(),NULL, NULL, NOW(), '0', '0', '0', '0','community_control');
INSERT INTO `eh_service_modules`
(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`,`module_control_type`)
VALUES
(20430, '交易明细', '20400', '/20000/20400/20430', '1', '3', '2', '0', NOW(),NULL, NULL, NOW(), '0', '0', '0', '0','community_control');

-- SET @p_id = 40073;
SET @p_id = 204001001;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '账单查看、筛选', '账单管理 账单查看、筛选', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '204011', '0', @p_id, '账单查看、筛选', '0', NOW());

-- SET @p_id = 40074;
SET @p_id = 204001002;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '新增账单', '账单管理 新增账单', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '204011', '0', @p_id, '新增账单', '0', NOW());

-- SET @p_id = 40075;
SET @p_id = 204001003;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '催缴', '账单管理 催缴', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '204011', '0', @p_id, '账单管理 催缴', '0', NOW());

-- SET @p_id = 40076;
SET @p_id = 204001004;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '修改缴费状态', '账单管理 修改缴费状态', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '204011', '0', @p_id, '账单管理 修改缴费状态', '0', NOW());

-- SET @p_id = 40077;
SET @p_id = 204001005;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '查看', '账单统计 查看', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '204021', '0', @p_id, '账单统计 查看', '0', NOW());


-- SET @p_id = 40078;
SET @p_id = 204001006;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '查看', '交易明细 查看', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '20430', '0', @p_id, '交易明细 查看', '0', NOW());

-- end of script by wentian


-- added by wh


SET @p_id = 42000;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '查看打卡规则', '查看打卡规则', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '50600', '0', @p_id, '查看打卡规则', '0', NOW());


SET @p_id = 42001;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '查看自己创建的打卡规则', '查看自己创建的打卡规则', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '50600', '0', @p_id, '查看自己创建的打卡规则', '0', NOW());

SET @p_id = 42002;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '新建打卡规则', '新建打卡规则', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '50600', '0', @p_id, '新建打卡规则', '0', NOW());

SET @p_id = 42003;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '编辑打卡规则', '编辑打卡规则', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '50600', '0', @p_id, '编辑打卡规则', '0', NOW());

SET @p_id = 42004;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '删除打卡规则', '删除打卡规则', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '50600', '0', @p_id, '删除打卡规则', '0', NOW());


SET @p_id = 42005;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '查看打卡记录', '查看打卡记录', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '50600', '0', @p_id, '查看打卡记录', '0', NOW());

SET @p_id = 42006;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '导出打卡记录', '导出打卡记录', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
(@mp_id:=@mp_id+1, '50600', '0', @p_id, '导出打卡记录', '0', NOW());


-- lei.lv
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (41001, null, '新增部门', '新增部门', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (41002, null, '修改部门', '修改部门', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (41003, null, '删除部门', '删除部门', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (41004, null, '调整部门顺序', '调整部门顺序', NULL);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (41005, null, '新增通用岗位', '新增通用岗位', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (41006, null, '编辑通用岗位', '编辑通用岗位', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (41007, null, '删除通用岗位', '删除通用岗位', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (41008, null, '编辑部门岗位', '编辑部门岗位', NULL);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (41009, null, '新增/修改人员', '新增/修改人员', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (41010, null, '删除人员', '删除人员', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (41011, null, '批量导入人员', '批量导入人员', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (41012, null, '批量导出人员', '批量导出人员', NULL);

set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES
(@mp_id:=@mp_id+1, '50100', '0', 41001, '新增部门', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES
(@mp_id:=@mp_id+1, '50100', '0', 41002, '修改部门', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES
(@mp_id:=@mp_id+1, '50100', '0', 41003, '删除部门', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES
(@mp_id:=@mp_id+1, '50100', '0', 41004, '调整部门顺序', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES
(@mp_id:=@mp_id+1, '50100', '0', 41005, '新增通用岗位', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES
(@mp_id:=@mp_id+1, '50100', '0', 41006, '编辑通用岗位', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES
(@mp_id:=@mp_id+1, '50100', '0', 41007, '删除通用岗位', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES
(@mp_id:=@mp_id+1, '50100', '0', 41008, '编辑部门岗位', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES
(@mp_id:=@mp_id+1, '50100', '0', 41009, '新增/修改人员', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES
(@mp_id:=@mp_id+1, '50100', '0', 41010, '删除人员', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES
(@mp_id:=@mp_id+1, '50100', '0', 41011, '批量导入人员', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES
(@mp_id:=@mp_id+1, '50100', '0', 41012, '批量导出人员', '0', NOW());

-- 给control_id一个初始值
update eh_authorizations set control_id = 1 where id = 33;


-- 更新action_type
update eh_service_modules set action_type = 46 WHERE id = 50100 and name = '组织架构';

update eh_service_modules set action_type = null where id = 50400 and name = '人事档案';

-- 更新提示
SET @sid = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@sid:=@sid+1), 'privilege', '100055', 'zh_CN', '校验应用权限失败');

-- 更新应用名
update eh_reflection_service_module_apps set name = '考勤管理' where name like '%打卡%';
update eh_reflection_service_module_apps set name = '组织架构' where name like '%通讯录%';

-- 更新多应用标志
UPDATE `eh_service_modules` SET `multiple_flag`='1' WHERE (`id`='20100');

-- 车辆放行功能菜单位置迁移 add by sw 20171215
delete from eh_web_menus where id in (20900, 20910, 20920, 20930);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
	VALUES ('20900', '车辆放行', '40000', NULL, 'parking_clearance', '1', '2', '/40000/20900', 'park', '300', '20900', '2', NULL, 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
	VALUES ('20910', '权限设置', '20900', NULL, 'vehicle_setting', '0', '2', '/40000/20900/20910', 'park', '301', '20900', '3', NULL, 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
	VALUES ('20920', '放行记录', '20900', NULL, 'release_record', '0', '2', '/40000/20900/20920', 'park', '302', '20900', '3', NULL, 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`)
	VALUES ('20930', '工作流设置', '20900', NULL, 'react:/working-flow/flow-list/vehicle-release/20900', '0', '2', '/40000/20900/20930', 'park', '303', '20900', '3', NULL, 'module');

-- 物业报修数据清理 add by sw 20171218
DELETE from eh_pm_tasks where task_category_id not in (SELECT id from eh_categories where parent_id in (6, 9)) and task_category_id != 1;
DELETE from eh_pm_task_statistics where task_category_id not in (SELECT id from eh_categories where parent_id in (6, 9)) and task_category_id != 1;
DELETE from eh_pm_task_target_statistics where task_category_id not in (SELECT id from eh_categories where parent_id in (6, 9)) and task_category_id != 1;


-- added by janon building_id
update eh_organization_address_mappings gg INNER JOIN eh_addresses as t1 on t1.id = gg.address_id set gg.building_name = t1.building_name where gg.building_name is null;
update eh_organization_address_mappings gg INNER JOIN eh_buildings as t1 on (t1.community_id = gg.community_id and t1.`name` = gg.building_name) set gg.building_id = t1.id where gg.building_id = 0;

