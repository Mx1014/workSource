ALTER TABLE `eh_customer_commercials` ADD COLUMN `main_business` VARCHAR(256) COMMENT '主营业务';
ALTER TABLE `eh_customer_commercials` ADD COLUMN `branch_company_name` VARCHAR(256) COMMENT '分公司名称';
ALTER TABLE `eh_customer_commercials` ADD COLUMN `branch_registered_date` DATETIME COMMENT '分公司登记日期';
ALTER TABLE `eh_customer_commercials` ADD COLUMN `legal_representative_name` VARCHAR(256) COMMENT '法人代表名称';
ALTER TABLE `eh_customer_commercials` ADD COLUMN `legal_representative_contact` VARCHAR(256) COMMENT '法人联系方式';
ALTER TABLE `eh_customer_commercials` ADD COLUMN `shareholder_name` VARCHAR(256) COMMENT '股东姓名';
ALTER TABLE `eh_customer_commercials` ADD COLUMN `actual_capital_injection_situation` VARCHAR(256) COMMENT '实际注资情况';
ALTER TABLE `eh_customer_commercials` ADD COLUMN `shareholding_situation` VARCHAR(256) COMMENT '股权占比情况';

CREATE TABLE `eh_service_module_functions` (
  `id` BIGINT NOT NULL,
  `module_id` BIGINT NOT NULL DEFAULT 0,
  `privilege_id` BIGINT NOT NULL DEFAULT 0,
  `explain` VARCHAR(64) NOT NULL DEFAULT 0,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_service_module_exclude_functions` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT,
  `module_id` BIGINT NOT NULL DEFAULT 0,
  `function_id` BIGINT NOT NULL DEFAULT 0,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `eh_customer_accounts` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `customer_id` BIGINT,
  `customer_name` VARCHAR(64),
  `bank_name` VARCHAR(128) COMMENT '开户行名称',
  `branch_name` VARCHAR(128) COMMENT '开户网点',
  `account_holder` VARCHAR(128) COMMENT '开户人',
  `account_number`  VARCHAR(128) COMMENT '账号',
  `account_number_type` VARCHAR(128) COMMENT '账号类型',
  `branch_province` VARCHAR(128) COMMENT '开户行所在省',
  `branch_city` VARCHAR(128) COMMENT '开户行所在市',
  `account_type_id` BIGINT COMMENT '账户类型 refer to the id of eh_var_field_items',
  `memo` VARCHAR(128) COMMENT '备注',
  `status` TINYINT NOT NULL DEFAULT 2,
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `eh_customer_taxes` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `customer_id` BIGINT,
  `customer_name` VARCHAR(64),
  `tax_name` VARCHAR(128) COMMENT '报税人',
  `tax_no` VARCHAR(128) COMMENT '报税人税号',
  `tax_address` VARCHAR(128) COMMENT '地址',
  `tax_phone` VARCHAR(128) COMMENT '联系电话',
  `tax_bank` VARCHAR(128) COMMENT '开户行名称',
  `tax_bank_no` VARCHAR(128) COMMENT '开户行账号',
  `tax_payer_type_id` BIGINT COMMENT '报税人类型 refer to the id of eh_var_field_items',
  `status` TINYINT NOT NULL DEFAULT 2,
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- fix-pm1.0
-- 增加字段
ALTER TABLE `eh_var_field_items` ADD COLUMN `business_value` TINYINT COMMENT 'the value defined in special business like status';
ALTER TABLE `eh_var_field_item_scopes` ADD COLUMN `business_value` TINYINT COMMENT 'the value defined in special business like status';

       