ALTER TABLE `eh_payment_charging_standards` ADD COLUMN `instruction` VARCHAR(1024) DEFAULT NULL COMMENT '说明';

-- 缴费2.0新增公式表，因为一个收费标准对应多个公式，故提取出来
CREATE TABLE `eh_payment_formula` (
  `id` BIGINT NOT NULL,
  `charging_standard_id` BIGINT DEFAULT NULL,
  `name` VARCHAR(10),
  `constraint_variable_identifer` VARCHAR(255) DEFAULT NULL,
  `constraint_variable_relation` TINYINT COMMENT '1:大于；2：大于等于；3：小于；4：小于等于',
  `constraint_variable_value_limit` DECIMAL(10,2) DEFAULT '0.00',
  `variables_json_string` VARCHAR(2048) DEFAULT NULL COMMENT 'json strings of variables injected for a particular formula',
  `formula` VARCHAR(1024),
  `formula_json` VARCHAR(2048),
  `formula_type` TINYINT COMMENT '1: fixed fee; 2: normal formula; 3: gradient varied on variable price; 4: gradients varied functions on each variable section',
  `price_unit_type` TINYINT COMMENT '1:日单价; 2:月单价; 3:季单价; 4:年单价',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收费标准表';