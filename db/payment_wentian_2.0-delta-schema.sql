ALTER TABLE `eh_payment_charging_standards` ADD COLUMN `instruction` VARCHAR(1024) DEFAULT NULL COMMENT '说明';
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `due_day` INTEGER DEFAULT NULL COMMENT '最晚还款日，距离账单日的距离，单位可以为月 ';
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `due_day_type` TINYINT DEFAULT 1 COMMENT '1:日，2：月 ';

DROP TABLE IF EXISTS `eh_payment_formula`;
CREATE TABLE `eh_payment_formula` (
  `id` bigint(20) NOT NULL,
  `charging_standard_id` bigint(20) DEFAULT NULL,
  `name` varchar(10) DEFAULT NULL,
  `constraint_variable_identifer` varchar(255) DEFAULT NULL,
  `start_constraint` tinyint(4) DEFAULT NULL COMMENT '1:大于；2：大于等于；3：小于；4：小于等于',
  `start_num` decimal(10,2) DEFAULT '0.00',
  `end_constraint` tinyint(4) DEFAULT NULL COMMENT '1:大于；2：大于等于；3：小于；4：小于等于',
  `end_num` decimal(10,2) DEFAULT '0.00',
  `variables_json_string` varchar(2048) DEFAULT NULL COMMENT 'json strings of variables injected for a particular formula',
  `formula` varchar(1024) DEFAULT NULL,
  `formula_json` varchar(2048) DEFAULT NULL,
  `formula_type` tinyint(4) DEFAULT NULL COMMENT '1: fixed fee; 2: normal formula; 3: gradient varied on variable price; 4: gradients varied functions on each variable section',
  `price_unit_type` tinyint(4) DEFAULT NULL COMMENT '1:日单价; 2:月单价; 3:季单价; 4:年单价',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收费标准公式表';

ALTER TABLE `eh_payment_charging_standards` ADD COLUMN `suggest_unit_price` DECIMAL(10,2) DEFAULT NULL COMMENT '建议单价';
ALTER TABLE `eh_payment_bill_groups_rules` ADD COLUMN `bill_item_month_offset` INTEGER DEFAULT NULL COMMENT '收费项产生时间偏离当前月的月数';
ALTER TABLE `eh_payment_bill_groups_rules` ADD COLUMN `bill_item_day_offset` INTEGER DEFAULT NULL COMMENT '收费项产生时间偏离当前月的日数';
ALTER TABLE `eh_payment_charging_standards` ADD COLUMN `area_size_type` INTEGER DEFAULT 1 COMMENT '计费面积类型,1：合同面积；2.建筑面积；3：使用面积；4：出租面积';
ALTER TABLE `eh_payment_bill_groups` MODIFY COLUMN `name` VARCHAR(50) DEFAULT NULL COMMENT '账单组名称';

ALTER TABLE `eh_payment_bill_items` ADD COLUMN `due_day_deadline` varchar(30) DEFAULT NULL COMMENT '最后付款日期';
ALTER TABLE `eh_payment_bills` ADD COLUMN `date_str_begin` varchar(30) DEFAULT NULL COMMENT '计费开始日期';
ALTER TABLE `eh_payment_bills` ADD COLUMN `date_str_end` varchar(30) DEFAULT NULL COMMENT '计费结束日期';
ALTER TABLE `eh_payment_bills` ADD COLUMN `date_str_due` varchar(30) DEFAULT NULL COMMENT '出账单日期';
ALTER TABLE `eh_payment_bills` ADD COLUMN `due_day_deadline` varchar(30) DEFAULT NULL COMMENT '最后付款日期';

ALTER TABLE `eh_payment_charging_item_scopes` ADD COLUMN `project_level_name` VARCHAR(30) DEFAULT NULL COMMENT '园区自定义的收费项目名字';