-- AUTHOR: djm 20181206
-- REMARK: 合同套打开关
ALTER TABLE eh_contract_categories ADD COLUMN print_switch_status tinyint(4) NOT NULL DEFAULT 0 COMMENT '合同套打(0 关闭，任何合同状态都能打印 1 打开 只有审批通过，正常合同、即将到期合同才能进行此操作)打开关';
ALTER TABLE eh_contract_categories ADD COLUMN editor_switch_status tinyint(4) NOT NULL DEFAULT 0 COMMENT '合同文档编辑开关';

-- AUTHOR: tangcen 2018年12月10日
-- REMARK: 合同文档表
CREATE TABLE `eh_contract_documents` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `community_id` bigint(20) NOT NULL,
  `category_id` bigint(20) NOT NULL COMMENT '合同多入口id',
  `contract_id` bigint(20) NOT NULL COMMENT '合同文档关联的合同id',
  `contract_template_id` bigint(20) DEFAULT NULL COMMENT '关联的合同模板id',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '上一版合同文档的id',
  `name` varchar(255) DEFAULT NULL COMMENT '合同文档名称',
  `content_type` varchar(32) DEFAULT NULL COMMENT '文本存储方式（gogs）',
  `content` varchar(128) DEFAULT NULL COMMENT '文本内容（在gogs存储时，存的是文本的commit id）',
  `version` int(11) DEFAULT '0' COMMENT '合同文档的版本号',
  `status` tinyint(4) DEFAULT NULL COMMENT '合同文档的状态-0: inactive, 1: confirming, 2: active',
  `creator_uid` bigint(20) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_uid` bigint(20) NOT NULL,
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='合同文档表';

-- AUTHOR: tangcen 2018年12月10日
-- REMARK: 在合同表中添加用于记录合同文档的字段
ALTER TABLE `eh_contracts` ADD COLUMN `document_id` bigint NULL COMMENT '当前生效的合同文档id';

-- 固定添加的动态字段
ALTER TABLE `eh_contracts` ADD COLUMN `apartment_delivery_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '房屋交付日期';
ALTER TABLE `eh_contracts` ADD COLUMN `down_payment_rent_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '首期租金开始日期';
ALTER TABLE `eh_contracts` ADD COLUMN `monthly_margin` INT DEFAULT '0' COMMENT '保证金月数';
ALTER TABLE `eh_contracts` ADD COLUMN `margin_amount` DECIMAL (12, 2) DEFAULT '0.00' COMMENT '保证金金额';
ALTER TABLE `eh_contracts` ADD COLUMN `monthly_service_charge` DECIMAL (12, 2) DEFAULT '0.00' COMMENT '月服务费金额';
ALTER TABLE `eh_contracts` ADD COLUMN `pre_amount` DECIMAL (12, 2) DEFAULT '0.00' COMMENT '预付金额';
ALTER TABLE `eh_contracts` ADD COLUMN `contracting_place` VARCHAR (64) DEFAULT NULL COMMENT '签约地点';
-- 预留字段
ALTER TABLE `eh_contracts` ADD COLUMN `string_tag1` VARCHAR (64) DEFAULT NULL COMMENT '预留字段1';
ALTER TABLE `eh_contracts` ADD COLUMN `string_tag2` VARCHAR (64) DEFAULT NULL COMMENT '预留字段2';
ALTER TABLE `eh_contracts` ADD COLUMN `string_tag3` VARCHAR (64) DEFAULT NULL COMMENT '预留字段3';
ALTER TABLE `eh_contracts` ADD COLUMN `string_tag4` VARCHAR (64) DEFAULT NULL COMMENT '预留字段4';
ALTER TABLE `eh_contracts` ADD COLUMN `string_tag5` VARCHAR (64) DEFAULT NULL COMMENT '预留字段5';
ALTER TABLE `eh_contracts` ADD COLUMN `string_tag6` VARCHAR (64) DEFAULT NULL COMMENT '预留字段6';
ALTER TABLE `eh_contracts` ADD COLUMN `string_tag7` VARCHAR (64) DEFAULT NULL COMMENT '预留字段7';
ALTER TABLE `eh_contracts` ADD COLUMN `string_tag8` VARCHAR (64) DEFAULT NULL COMMENT '预留字段8';
ALTER TABLE `eh_contracts` ADD COLUMN `string_tag9` VARCHAR (64) DEFAULT NULL COMMENT '预留字段9';
ALTER TABLE `eh_contracts` ADD COLUMN `string_tag10` VARCHAR (64) DEFAULT NULL COMMENT '预留字段10';

-- AUTHOR: tangcen 2018年12月11日
-- REMARK: 在合同模板表中添加用于记录合同模板初始化参数的字段
ALTER TABLE `eh_contract_templates` ADD COLUMN `init_params` varchar(1024) COMMENT '合同模板初始化参数（计价条款、关联资产等的数目），前端会解析这个json';

-- AUTHOR: 黄鹏宇 2018年12月11日
-- REMARK: 新建字段
ALTER table eh_enterprise_customers add column legal_address varchar(512) comment '法定地址';
ALTER table eh_enterprise_customers add column legal_address_zip varchar(32) comment '法定地址邮编';
ALTER table eh_enterprise_customers add column postal_address varchar(512) comment '通讯地址';
ALTER table eh_enterprise_customers add column postal_address_zip varchar(32) comment '通讯地址邮编';
ALTER table eh_enterprise_customers add column taxpayer_identification_code varchar(32) comment '纳税人识别号';
ALTER table eh_enterprise_customers add column identify_card_number varchar(32) comment '身份证号码';
ALTER table eh_enterprise_customers add column opening_bank varchar(64) comment '开户行';
ALTER table eh_enterprise_customers add column opening_name varchar(64) comment '开户名';
ALTER table eh_enterprise_customers add column opening_account varchar(32) comment '开户行账号';
ALTER table eh_enterprise_customers add column string_tag17 varchar(32) comment '预留字段17';
ALTER table eh_enterprise_customers add column string_tag18 varchar(32) comment '预留字段18';
ALTER table eh_enterprise_customers add column string_tag19 varchar(32) comment '预留字段19';
ALTER table eh_enterprise_customers add column string_tag20 varchar(32) comment '预留字段20';
ALTER table eh_enterprise_customers add column string_tag21 varchar(32) comment '预留字段21';
ALTER table eh_enterprise_customers add column corp_legal_person_duty varchar(32) comment '法人代表职务';

-- AUTHOR: tangcen 2018年12月12日
-- REMARK: 给eh_contract_documents添加合同编号字段
ALTER TABLE `eh_contract_documents` ADD COLUMN `contract_number` varchar(128) COMMENT '合同编号' AFTER `name`;
