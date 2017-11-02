-- version 1 的手动复制出来
SET @conf_id := (SELECT MAX(id) FROM eh_uniongroup_configures);
SET @member_id := (SELECT MAX(id) FROM eh_uniongroup_configures);

INSERT INTO eh_uniongroup_configures 
SELECT
  @conf_id + id id,
  `namespace_id`,
  `enterprise_id`,
  `group_type`,
  `group_id`,
  `current_id`,
  `current_type`,
  `current_name`,
  `operator_uid`,
  `update_time`,
  1 version_code
FROM `eh_uniongroup_configures`;
INSERT INTO eh_uniongroup_member_details 
SELECT
  @member_id  + `id` id,
  `namespace_id`,
  `group_type`,
  `group_id`,
  `detail_id`,
  `target_type`,
  `target_id`,
  `enterprise_id`,
  `contact_name`,
  `contact_token`,
  `update_time`,
  `operator_uid`,
  1 version_code
FROM `eh_uniongroup_member_details`;

-- by dengs,菜单名称修改， 20171027
UPDATE eh_web_menus SET `name` = '问卷调查' WHERE id = 40150 AND `name` = '企业问卷调查';
INSERT INTO `eh_configurations` (`name`, `value`, `description`) VALUES ('questionnaire.detail.url', '/questionnaire-survey/build/index.html#/question/%s/0', '问卷地址URL');
INSERT INTO `eh_configurations` (`name`, `value`, `description`) VALUES ('questionnaire.send.message.express', '0 0 1 * * ?', '定时任务表达式');
INSERT INTO `eh_configurations` (`name`, `value`, `description`) VALUES ('questionnaire.remind.time.interval', '24', '通知value小时内，没有回答问卷的用户');




-- merge from forum-2.4 add by yanjun 201710311836
-- 论坛入口表默认入口
set @m_id = (select ifnull(max(id), 0) from eh_forum_categories);
INSERT INTO `eh_forum_categories` (id, `uuid`, `namespace_id`, `forum_id`, `entry_id`, `name`, `activity_entry_id`, `create_time`, `update_time`)
SELECT (@m_id:=@m_id+1),UUID(), namespace_id, default_forum_id, 0, '发现', '0', NOW(), NOW() from eh_communities where `status` = 2 GROUP BY namespace_id, default_forum_id ORDER BY namespace_id ;

set @id := (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id := @id + 1), 'forum', '10020', 'zh_CN', '此版本目前不支持评论功能。由此带来的不便请谅解。');

-- merge from forum-2.4 add by yanjun 201710311836

-- 所有配置的收费项目第一版只能计费周期为月 -- by wentian
update `eh_payment_charging_standards` set billing_cycle = 2;
-- 物业缴费错误码 -- by wentian
SET @eh_locale_strings_id = (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'assetv2', '10001', 'zh_CN', '正在生成中');

-- 园区入驻 add by sw 20171102
UPDATE eh_configurations set `value` = '/park-entry/dist/index.html?hideNavigationBar=1#/project_intro/%s/%s' where `name` = 'apply.entry.lease.project.detail.url';
UPDATE eh_configurations set `value` = '/park-entry/dist/index.html?hideNavigationBar=1#/building_detail/%s/%s' where `name` = 'apply.entry.lease.building.detail.url';
UPDATE eh_configurations set `value` = '/park-entry/dist/index.html?hideNavigationBar=1#/rent_detail/%s/%s' where `name` = 'apply.entry.detail.url';

-- 删除分类  add by yanjun 20171102 已在beta、alpha执行
DELETE from eh_categories WHERE name in ('二手和租售', '免费物品', '失物招领', '紧急通知');

-- 缴费2.0的收费项目init data  by wentian
TRUNCATE `eh_payment_charging_items`;
INSERT INTO `eh_payment_charging_items`(`id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`)VALUES('1', '租金', '0', UTC_TIMESTAMP(), NULL, NULL, '1');
INSERT INTO `eh_payment_charging_items`(`id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`)VALUES('2', '物业费', '0', UTC_TIMESTAMP(), NULL, NULL, '1');
INSERT INTO `eh_payment_charging_items`(`id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`)VALUES('3', '押金', '0', UTC_TIMESTAMP(), NULL, NULL, '1');
INSERT INTO `eh_payment_charging_items`(`id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`)VALUES('4', '水费', '0', UTC_TIMESTAMP(), NULL, NULL, '1');
INSERT INTO `eh_payment_charging_items`(`id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`)VALUES('5', '电费', '0', UTC_TIMESTAMP(), NULL, NULL, '1');
INSERT INTO `eh_payment_charging_items`(`id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`)VALUES('6', '滞纳金', '0', UTC_TIMESTAMP(), NULL, NULL, '1');

-- 物业缴费2.0的变量 by wentian
TRUNCATE `eh_payment_variables`;
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES ('1', NULL, NULL , '单价', 0, UTC_TIMESTAMP(), NULL, UTC_TIMESTAMP(), 'dj');
set @eh_payment_variables_id = (SELECT MAX(id) from `eh_payment_variables`);
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@eh_payment_variables_id:=@eh_payment_variables_id+1,  null,'1', '面积', 0, UTC_TIMESTAMP(), NULL,UTC_TIMESTAMP(), 'mj');
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@eh_payment_variables_id:=@eh_payment_variables_id+1,  null,'6', '固定金额', 0, UTC_TIMESTAMP(), NULL, UTC_TIMESTAMP(), 'gdje');
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@eh_payment_variables_id:=@eh_payment_variables_id+1,  null,'5', '用量', 0, UTC_TIMESTAMP(), NULL, UTC_TIMESTAMP(), 'yl');

-- ERROR CODE
SET @eh_locale_strings_id = (SELECT MAX(id) from `eh_locale_strings`);
UPDATE `eh_locale_strings` set text = '正在生成中(本版本不支持调租免租),请稍后查看' WHERE scope = 'assetv2' and code = '10001';
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'assetv2', '10002', 'zh_CN', '操作失败，该域名下没有可用园区');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'assetv2', '10003', 'zh_CN', '生成失败，请返回上一步重新设置进行生成');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'assetv2', '10004', 'zh_CN', '暂不支持调租和免租，请返回上一步并重新设置');

-- 设置上线前的设置，ps：eh_payment相关，eh_asset支付无关，物业支付暂时未开放给除了张江高科外的域空间
TRUNCATE eh_payment_charging_item_scopes;
TRUNCATE eh_payment_charging_standards;
TRUNCATE eh_payment_charging_standards_scopes;
truncate eh_payment_bill_groups;
truncate eh_payment_bill_groups_rules;
truncate eh_payment_formula;
INSERT INTO `eh_payment_charging_item_scopes` (`id`, `charging_item_id`, `namespace_id`, `owner_id`, `owner_type`, `project_level_name`, `decoupling_flag`) VALUES ('651', '1', '999970', '240111044331050367', 'community', '租金', '1');
INSERT INTO `eh_payment_charging_item_scopes` (`id`, `charging_item_id`, `namespace_id`, `owner_id`, `owner_type`, `project_level_name`, `decoupling_flag`) VALUES ('652', '2', '999970', '240111044331050367', 'community', '物业费', '1');
INSERT INTO `eh_payment_charging_standards` (`id`, `name`, `charging_items_id`, `formula`, `formula_json`, `formula_type`, `billing_cycle`, `price_unit_type`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `instruction`, `suggest_unit_price`, `bill_item_month_offset`, `bill_item_day_offset`, `area_size_type`) VALUES ('209', '租金(月固定金额)', '1', '固定金额', NULL, '1', '2', NULL, '0', '2017-11-02 18:14:18', NULL, '2017-11-02 18:14:18', NULL, '5000.00', NULL, NULL, '1');
INSERT INTO `eh_payment_charging_standards` (`id`, `name`, `charging_items_id`, `formula`, `formula_json`, `formula_type`, `billing_cycle`, `price_unit_type`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `instruction`, `suggest_unit_price`, `bill_item_month_offset`, `bill_item_day_offset`, `area_size_type`) VALUES ('210', '物业费(固定金额）', '2', '固定金额', NULL, '1', '2', NULL, '0', '2017-11-02 18:14:29', NULL, '2017-11-02 18:14:29', NULL, '100.00', NULL, NULL, '1');
INSERT INTO `eh_payment_charging_standards_scopes` (`id`, `charging_standard_id`, `owner_type`, `owner_id`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ('195', '209', 'community', '240111044331050367', '0', '2017-11-02 18:14:18', NULL, '2017-11-02 18:14:18');
INSERT INTO `eh_payment_charging_standards_scopes` (`id`, `charging_standard_id`, `owner_type`, `owner_id`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ('196', '210', 'community', '240111044331050367', '0', '2017-11-02 18:14:29', NULL, '2017-11-02 18:14:29');
INSERT INTO `eh_payment_bill_groups` (`id`, `namespace_id`, `owner_id`, `owner_type`, `name`, `balance_date_type`, `bills_day`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`, `due_day`, `due_day_type`) VALUES ('162', '999970', '240111044331050367', 'community', '租金', '2', '5', '67663', '2017-11-02 18:14:44', NULL, '2017-11-02 18:14:44', '1', '5', '1');
INSERT INTO `eh_payment_bill_groups` (`id`, `namespace_id`, `owner_id`, `owner_type`, `name`, `balance_date_type`, `bills_day`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`, `due_day`, `due_day_type`) VALUES ('163', '999970', '240111044331050367', 'community', '物业费', '2', '5', '67663', '2017-11-02 18:15:02', NULL, '2017-11-02 18:15:02', '1', '5', '1');
INSERT INTO `eh_payment_bill_groups_rules` (`id`, `namespace_id`, `bill_group_id`, `charging_item_id`, `charging_standards_id`, `charging_item_name`, `variables_json_string`, `ownerType`, `ownerId`, `bill_item_month_offset`, `bill_item_day_offset`) VALUES ('105', '999970', '162', '1', '209', NULL, NULL, 'community', '240111044331050367', '0', NULL);
INSERT INTO `eh_payment_bill_groups_rules` (`id`, `namespace_id`, `bill_group_id`, `charging_item_id`, `charging_standards_id`, `charging_item_name`, `variables_json_string`, `ownerType`, `ownerId`, `bill_item_month_offset`, `bill_item_day_offset`) VALUES ('106', '999970', '163', '2', '210', NULL, NULL, 'community', '240111044331050367', '0', NULL);
INSERT INTO `eh_payment_formula` (`id`, `charging_standard_id`, `name`, `constraint_variable_identifer`, `start_constraint`, `start_num`, `end_constraint`, `end_num`, `variables_json_string`, `formula`, `formula_json`, `formula_type`, `price_unit_type`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ('226', '209', '固定金额', NULL, NULL, NULL, NULL, NULL, NULL, '固定金额', 'gdje', '1', NULL, '0', '2017-11-02 18:14:18', NULL, '2017-11-02 18:14:18');
INSERT INTO `eh_payment_formula` (`id`, `charging_standard_id`, `name`, `constraint_variable_identifer`, `start_constraint`, `start_num`, `end_constraint`, `end_num`, `variables_json_string`, `formula`, `formula_json`, `formula_type`, `price_unit_type`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ('227', '210', '固定金额', NULL, NULL, NULL, NULL, NULL, NULL, '固定金额', 'gdje', '1', NULL, '0', '2017-11-02 18:14:29', NULL, '2017-11-02 18:14:29');
INSERT INTO `eh_payment_charging_item_scopes` (`id`, `charging_item_id`, `namespace_id`, `owner_id`, `owner_type`, `project_level_name`, `decoupling_flag`) VALUES ('657', '1', '999971', '240111044331050388', 'community', '租金', '1');
INSERT INTO `eh_payment_charging_item_scopes` (`id`, `charging_item_id`, `namespace_id`, `owner_id`, `owner_type`, `project_level_name`, `decoupling_flag`) VALUES ('658', '1', '999971', '240111044331050389', 'community', '租金', '1');
INSERT INTO `eh_payment_charging_item_scopes` (`id`, `charging_item_id`, `namespace_id`, `owner_id`, `owner_type`, `project_level_name`, `decoupling_flag`) VALUES ('659', '1', '999971', '240111044332059902', 'community', '租金', '1');
INSERT INTO `eh_payment_charging_item_scopes` (`id`, `charging_item_id`, `namespace_id`, `owner_id`, `owner_type`, `project_level_name`, `decoupling_flag`) VALUES ('660', '1', '999971', '240111044332059903', 'community', '租金', '1');
INSERT INTO `eh_payment_charging_item_scopes` (`id`, `charging_item_id`, `namespace_id`, `owner_id`, `owner_type`, `project_level_name`, `decoupling_flag`) VALUES ('661', '1', '999971', '240111044332059904', 'community', '租金', '1');
INSERT INTO `eh_payment_charging_standards` (`id`, `name`, `charging_items_id`, `formula`, `formula_json`, `formula_type`, `billing_cycle`, `price_unit_type`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `instruction`, `suggest_unit_price`, `bill_item_month_offset`, `bill_item_day_offset`, `area_size_type`) VALUES ('213', '租金(月固定金额)', '1', '固定金额', NULL, '1', '2', NULL, '0', '2017-11-02 18:34:50', NULL, '2017-11-02 18:34:50', NULL, '5000.00', NULL, NULL, '1');
INSERT INTO `eh_payment_charging_standards` (`id`, `name`, `charging_items_id`, `formula`, `formula_json`, `formula_type`, `billing_cycle`, `price_unit_type`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `instruction`, `suggest_unit_price`, `bill_item_month_offset`, `bill_item_day_offset`, `area_size_type`) VALUES ('214', '物业费(固定金额）', '2', '固定金额', NULL, '1', '2', NULL, '0', '2017-11-02 18:35:03', NULL, '2017-11-02 18:35:03', NULL, '100.00', NULL, NULL, '1');
INSERT INTO `eh_payment_charging_standards` (`id`, `name`, `charging_items_id`, `formula`, `formula_json`, `formula_type`, `billing_cycle`, `price_unit_type`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `instruction`, `suggest_unit_price`, `bill_item_month_offset`, `bill_item_day_offset`, `area_size_type`) VALUES ('215', '租金(月固定金额)', '1', '固定金额', NULL, '1', '2', NULL, '0', '2017-11-02 18:36:39', NULL, '2017-11-02 18:36:39', NULL, '5000.00', NULL, NULL, NULL);
INSERT INTO `eh_payment_charging_standards` (`id`, `name`, `charging_items_id`, `formula`, `formula_json`, `formula_type`, `billing_cycle`, `price_unit_type`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `instruction`, `suggest_unit_price`, `bill_item_month_offset`, `bill_item_day_offset`, `area_size_type`) VALUES ('216', '租金(月固定金额)', '1', '固定金额', NULL, '1', '2', NULL, '0', '2017-11-02 18:37:04', NULL, '2017-11-02 18:37:04', NULL, '5000.00', NULL, NULL, '1');
INSERT INTO `eh_payment_charging_standards` (`id`, `name`, `charging_items_id`, `formula`, `formula_json`, `formula_type`, `billing_cycle`, `price_unit_type`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `instruction`, `suggest_unit_price`, `bill_item_month_offset`, `bill_item_day_offset`, `area_size_type`) VALUES ('217', '租金(月固定金额)', '1', '固定金额', NULL, '1', '2', NULL, '0', '2017-11-02 18:37:52', NULL, '2017-11-02 18:37:52', NULL, '5000.00', NULL, NULL, '1');
INSERT INTO `eh_payment_charging_standards` (`id`, `name`, `charging_items_id`, `formula`, `formula_json`, `formula_type`, `billing_cycle`, `price_unit_type`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `instruction`, `suggest_unit_price`, `bill_item_month_offset`, `bill_item_day_offset`, `area_size_type`) VALUES ('218', '租金(月固定金额)', '1', '固定金额', NULL, '1', '2', NULL, '0', '2017-11-02 18:38:31', NULL, '2017-11-02 18:38:31', NULL, '5000.00', NULL, NULL, '1');
INSERT INTO `eh_payment_charging_standards_scopes` (`id`, `charging_standard_id`, `owner_type`, `owner_id`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ('199', '213', 'community', '240111044331050388', '0', '2017-11-02 18:34:50', NULL, '2017-11-02 18:34:50');
INSERT INTO `eh_payment_charging_standards_scopes` (`id`, `charging_standard_id`, `owner_type`, `owner_id`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ('201', '215', 'community', '240111044331050389', '0', '2017-11-02 18:36:39', NULL, '2017-11-02 18:36:39');
INSERT INTO `eh_payment_charging_standards_scopes` (`id`, `charging_standard_id`, `owner_type`, `owner_id`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ('202', '216', 'community', '240111044332059902', '0', '2017-11-02 18:37:04', NULL, '2017-11-02 18:37:04');
INSERT INTO `eh_payment_charging_standards_scopes` (`id`, `charging_standard_id`, `owner_type`, `owner_id`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ('203', '217', 'community', '240111044332059903', '0', '2017-11-02 18:37:52', NULL, '2017-11-02 18:37:52');
INSERT INTO `eh_payment_charging_standards_scopes` (`id`, `charging_standard_id`, `owner_type`, `owner_id`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ('204', '218', 'community', '240111044332059904', '0', '2017-11-02 18:38:31', NULL, '2017-11-02 18:38:31');
INSERT INTO `eh_payment_bill_groups` (`id`, `namespace_id`, `owner_id`, `owner_type`, `name`, `balance_date_type`, `bills_day`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`, `due_day`, `due_day_type`) VALUES ('166', '999971', '240111044331050388', 'community', '租金', '2', '5', '67663', '2017-11-02 18:35:18', NULL, '2017-11-02 18:35:18', '1', '5', '1');
INSERT INTO `eh_payment_bill_groups` (`id`, `namespace_id`, `owner_id`, `owner_type`, `name`, `balance_date_type`, `bills_day`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`, `due_day`, `due_day_type`) VALUES ('167', '999971', '240111044331050389', 'community', '租金', '2', '5', '67663', '2017-11-02 18:36:47', NULL, '2017-11-02 18:36:47', '1', '5', '1');
INSERT INTO `eh_payment_bill_groups` (`id`, `namespace_id`, `owner_id`, `owner_type`, `name`, `balance_date_type`, `bills_day`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`, `due_day`, `due_day_type`) VALUES ('168', '999971', '240111044332059902', 'community', '租金', '2', '5', '67663', '2017-11-02 18:37:16', NULL, '2017-11-02 18:37:16', '1', '5', '1');
INSERT INTO `eh_payment_bill_groups` (`id`, `namespace_id`, `owner_id`, `owner_type`, `name`, `balance_date_type`, `bills_day`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`, `due_day`, `due_day_type`) VALUES ('169', '999971', '240111044332059903', 'community', '租金', '2', '5', '67663', '2017-11-02 18:38:03', NULL, '2017-11-02 18:38:03', '1', '5', NULL);
INSERT INTO `eh_payment_bill_groups` (`id`, `namespace_id`, `owner_id`, `owner_type`, `name`, `balance_date_type`, `bills_day`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`, `due_day`, `due_day_type`) VALUES ('170', '999971', '240111044332059904', 'community', '租金', '2', '5', '67663', '2017-11-02 18:38:38', NULL, '2017-11-02 18:38:38', '1', '5', '1');
INSERT INTO `eh_payment_bill_groups_rules` (`id`, `namespace_id`, `bill_group_id`, `charging_item_id`, `charging_standards_id`, `charging_item_name`, `variables_json_string`, `ownerType`, `ownerId`, `bill_item_month_offset`, `bill_item_day_offset`) VALUES ('109', '999971', '166', '1', '213', NULL, NULL, 'community', '240111044331050388', '0', NULL);
INSERT INTO `eh_payment_bill_groups_rules` (`id`, `namespace_id`, `bill_group_id`, `charging_item_id`, `charging_standards_id`, `charging_item_name`, `variables_json_string`, `ownerType`, `ownerId`, `bill_item_month_offset`, `bill_item_day_offset`) VALUES ('110', '999971', '168', '1', '216', NULL, NULL, 'community', '240111044332059902', '0', NULL);
INSERT INTO `eh_payment_bill_groups_rules` (`id`, `namespace_id`, `bill_group_id`, `charging_item_id`, `charging_standards_id`, `charging_item_name`, `variables_json_string`, `ownerType`, `ownerId`, `bill_item_month_offset`, `bill_item_day_offset`) VALUES ('111', '999971', '167', '1', '215', NULL, NULL, 'community', '240111044331050389', '0', NULL);
INSERT INTO `eh_payment_bill_groups_rules` (`id`, `namespace_id`, `bill_group_id`, `charging_item_id`, `charging_standards_id`, `charging_item_name`, `variables_json_string`, `ownerType`, `ownerId`, `bill_item_month_offset`, `bill_item_day_offset`) VALUES ('112', '999971', '169', '1', '217', NULL, NULL, 'community', '240111044332059903', '0', NULL);
INSERT INTO `eh_payment_bill_groups_rules` (`id`, `namespace_id`, `bill_group_id`, `charging_item_id`, `charging_standards_id`, `charging_item_name`, `variables_json_string`, `ownerType`, `ownerId`, `bill_item_month_offset`, `bill_item_day_offset`) VALUES ('113', '999971', '170', '1', '218', NULL, NULL, 'community', '240111044332059904', '0', NULL);
INSERT INTO `eh_payment_formula` (`id`, `charging_standard_id`, `name`, `constraint_variable_identifer`, `start_constraint`, `start_num`, `end_constraint`, `end_num`, `variables_json_string`, `formula`, `formula_json`, `formula_type`, `price_unit_type`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ('230', '213', '固定金额', NULL, NULL, NULL, NULL, NULL, NULL, '固定金额', 'gdje', '1', NULL, '0', '2017-11-02 18:34:50', NULL, '2017-11-02 18:34:50');
INSERT INTO `eh_payment_formula` (`id`, `charging_standard_id`, `name`, `constraint_variable_identifer`, `start_constraint`, `start_num`, `end_constraint`, `end_num`, `variables_json_string`, `formula`, `formula_json`, `formula_type`, `price_unit_type`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ('231', '214', '固定金额', NULL, NULL, NULL, NULL, NULL, NULL, '固定金额', 'gdje', '1', NULL, '0', '2017-11-02 18:35:03', NULL, '2017-11-02 18:35:03');
INSERT INTO `eh_payment_formula` (`id`, `charging_standard_id`, `name`, `constraint_variable_identifer`, `start_constraint`, `start_num`, `end_constraint`, `end_num`, `variables_json_string`, `formula`, `formula_json`, `formula_type`, `price_unit_type`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ('232', '215', '固定金额', NULL, NULL, NULL, NULL, NULL, NULL, '固定金额', 'gdje', '1', NULL, '0', '2017-11-02 18:36:39', NULL, '2017-11-02 18:36:39');
INSERT INTO `eh_payment_formula` (`id`, `charging_standard_id`, `name`, `constraint_variable_identifer`, `start_constraint`, `start_num`, `end_constraint`, `end_num`, `variables_json_string`, `formula`, `formula_json`, `formula_type`, `price_unit_type`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ('233', '216', '固定金额', NULL, NULL, NULL, NULL, NULL, NULL, '固定金额', 'gdje', '1', NULL, '0', '2017-11-02 18:37:04', NULL, '2017-11-02 18:37:04');
INSERT INTO `eh_payment_formula` (`id`, `charging_standard_id`, `name`, `constraint_variable_identifer`, `start_constraint`, `start_num`, `end_constraint`, `end_num`, `variables_json_string`, `formula`, `formula_json`, `formula_type`, `price_unit_type`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ('234', '217', '固定金额', NULL, NULL, NULL, NULL, NULL, NULL, '固定金额', 'gdje', '1', NULL, '0', '2017-11-02 18:37:52', NULL, '2017-11-02 18:37:52');
INSERT INTO `eh_payment_formula` (`id`, `charging_standard_id`, `name`, `constraint_variable_identifer`, `start_constraint`, `start_num`, `end_constraint`, `end_num`, `variables_json_string`, `formula`, `formula_json`, `formula_type`, `price_unit_type`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ('235', '218', '固定金额', NULL, NULL, NULL, NULL, NULL, NULL, '固定金额', 'gdje', '1', NULL, '0', '2017-11-02 18:38:31', NULL, '2017-11-02 18:38:31');


