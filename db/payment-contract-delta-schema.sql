ALTER TABLE `eh_contracts` ADD COLUMN `remaining_amount` DECIMAL(10,2) COMMENT '剩余金额';
ALTER TABLE `eh_contracts` ADD COLUMN `bid_item_id` BIGINT COMMENT '是否通过招投标';
ALTER TABLE `eh_contracts` ADD COLUMN `create_org_id` BIGINT COMMENT '经办部门';
ALTER TABLE `eh_contracts` ADD COLUMN `create_position_id` BIGINT COMMENT '岗位';
ALTER TABLE `eh_contracts` ADD COLUMN `our_legal_representative` VARCHAR(256) COMMENT '我方法人代表';
ALTER TABLE `eh_contracts` ADD COLUMN `taxpayer_identification_code` VARCHAR(256) COMMENT '纳税人识别码';
ALTER TABLE `eh_contracts` ADD COLUMN `registered_address` VARCHAR(512) COMMENT '注册地址';
ALTER TABLE `eh_contracts` ADD COLUMN `registered_phone` VARCHAR(256) COMMENT '注册电话';
ALTER TABLE `eh_contracts` ADD COLUMN `payee` VARCHAR(256) COMMENT '收款单位';
ALTER TABLE `eh_contracts` ADD COLUMN `payer` VARCHAR(256) COMMENT '付款单位';
ALTER TABLE `eh_contracts` ADD COLUMN `due_bank` VARCHAR(256) COMMENT '收款银行';
ALTER TABLE `eh_contracts` ADD COLUMN `bank_account` VARCHAR(256) COMMENT '银行账号';
ALTER TABLE `eh_contracts` ADD COLUMN `exchange_rate` DECIMAL(10,2) COMMENT '兑换汇率';
ALTER TABLE `eh_contracts` ADD COLUMN `age_limit` INTEGER COMMENT '年限';
ALTER TABLE `eh_contracts` ADD COLUMN `application_id` BIGINT COMMENT '关联请示单';
ALTER TABLE `eh_contracts` ADD COLUMN `payment_mode_item_id` BIGINT COMMENT '预计付款方式';
ALTER TABLE `eh_contracts` ADD COLUMN `paid_time` DATETIME COMMENT '预计付款时间';
ALTER TABLE `eh_contracts` ADD COLUMN `lump_sum_payment` DECIMAL(10,2) COMMENT '一次性付款金额';
ALTER TABLE `eh_contracts` ADD COLUMN `treaty_particulars` text COMMENT '合同摘要';

ALTER TABLE `eh_contracts` ADD COLUMN `payment_flag` TINYINT NOT NULL DEFAULT '0' COMMENT '0:普通合同；1：付款合同';

ALTER TABLE eh_contract_params ADD COLUMN `paid_period` INTEGER NOT NULL DEFAULT '0' COMMENT '付款日期';

CREATE TABLE `eh_contract_param_group_map` (		
  `id` BIGINT NOT NULL COMMENT 'id',	
  `param_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_contract_params',		
  `group_type` TINYINT NOT NULL DEFAULT '0' COMMENT '0: none, 1: notify group, 2: pay group',
  `group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organizations',		
  `position_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organization_job_positions',
  `name` VARCHAR(256) COMMENT '部门名',  
  `create_time` DATETIME,
  		
  PRIMARY KEY (`id`)		
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_contract_payment_plans` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `contract_id` BIGINT NOT NULL COMMENT 'id of eh_contracts',
  `paid_amount` DECIMAL(10,2) COMMENT '应付金额',
  `paid_time` DATETIME COMMENT '应付日期',
  `remark` VARCHAR(256) COMMENT '备注',
  `notify_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: no; 1: notified',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_payment_applications` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `community_id` BIGINT COMMENT '园区id',
  `owner_id` BIGINT COMMENT '公司id',
  `title` VARCHAR(256) COMMENT '标题',
  `contract_id` BIGINT NOT NULL COMMENT 'id of eh_contracts',
  `request_id` BIGINT,
  `applicant_uid` BIGINT NOT NULL COMMENT '申请人id',
  `applicant_org_id` BIGINT NOT NULL COMMENT '申请人所属部门id',
  `payee` VARCHAR(256) COMMENT '收款单位',
  `payer` VARCHAR(256) COMMENT '付款单位',
  `due_bank` VARCHAR(256) COMMENT '收款银行',
  `bank_account` VARCHAR(256) COMMENT '银行账号',
  `payment_amount` DECIMAL(10,2) COMMENT '付款金额',
  `payment_rate` DOUBLE COMMENT '付款百分比',
  `remark` VARCHAR(256) COMMENT '备注',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: QUALIFIED; 3: UNQUALIFIED',
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;