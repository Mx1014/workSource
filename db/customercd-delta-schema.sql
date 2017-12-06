ALTER TABLE `eh_enterprise_customers` ADD COLUMN `business_value` TEXT COMMENT '创始人介绍';
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `business_value` DATETIME COMMENT '企业成立时间';
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `business_value` TINYINT COMMENT '企业登记注册类型';
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `business_value` TINYINT COMMENT '企业所属技术领域';
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `business_value` TINYINT COMMENT '企业纳税人类型';
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `business_value` TINYINT COMMENT '是否愿意与创业导师建立辅导关系';
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `business_value` TINYINT COMMENT '是否高新技术企业';
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `business_value` TINYINT COMMENT '企业主要负责人创业特征';
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `business_value` TINYINT COMMENT '企业主要负责人是否为连续创业者';
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `business_value` DECIMAL(10,2) COMMENT '获天使或风险投资总金额（万元）';

ALTER TABLE `eh_customer_talents` ADD COLUMN `business_value` INTEGER COMMENT '从业人员总人数';
ALTER TABLE `eh_customer_talents` ADD COLUMN `business_value` INTEGER COMMENT '大专';
ALTER TABLE `eh_customer_talents` ADD COLUMN `business_value` INTEGER COMMENT '本科';
ALTER TABLE `eh_customer_talents` ADD COLUMN `business_value` INTEGER COMMENT '硕士';
ALTER TABLE `eh_customer_talents` ADD COLUMN `business_value` INTEGER COMMENT '博士';
ALTER TABLE `eh_customer_talents` ADD COLUMN `business_value` INTEGER COMMENT '留学人员';
ALTER TABLE `eh_customer_talents` ADD COLUMN `business_value` INTEGER COMMENT '千人计划人数';
ALTER TABLE `eh_customer_talents` ADD COLUMN `business_value` INTEGER COMMENT '吸纳应届大学毕业生';

ALTER TABLE `eh_customer_trademarks` ADD COLUMN `business_value` INTEGER COMMENT '知识产权申请总数';
ALTER TABLE `eh_customer_trademarks` ADD COLUMN `business_value` INTEGER COMMENT '申请发明专利数';
ALTER TABLE `eh_customer_trademarks` ADD COLUMN `business_value` INTEGER COMMENT '知识产权授权总数';
ALTER TABLE `eh_customer_trademarks` ADD COLUMN `business_value` INTEGER COMMENT '授权发明专利数
';
ALTER TABLE `eh_customer_patents` ADD COLUMN `business_value` INTEGER COMMENT '拥有有效知识产权总数';
ALTER TABLE `eh_customer_patents` ADD COLUMN `business_value` INTEGER COMMENT '发明专利';
ALTER TABLE `eh_customer_patents` ADD COLUMN `business_value` INTEGER COMMENT '软件著作权';
ALTER TABLE `eh_customer_patents` ADD COLUMN `business_value` INTEGER COMMENT '集成电路布图';

ALTER TABLE `eh_customer_certificates` ADD COLUMN `business_value` INTEGER COMMENT '购买国外专利';
ALTER TABLE `eh_customer_certificates` ADD COLUMN `business_value` INTEGER COMMENT '技术合同交易数量';
ALTER TABLE `eh_customer_certificates` ADD COLUMN `business_value` INTEGER COMMENT '技术合同交易总金额（万元）';
ALTER TABLE `eh_customer_certificates` ADD COLUMN `business_value` INTEGER COMMENT '当年承担国家级科技计划项目数';
ALTER TABLE `eh_customer_certificates` ADD COLUMN `business_value` INTEGER COMMENT '当年获得省级以上奖励';

ALTER TABLE `eh_customer_apply_projects` ADD COLUMN `business_value` TEXT COMMENT '主要项目介绍';

-- 入驻信息
CREATE TABLE `eh_customer_entry_infos` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `customer_id` BIGINT,
  `customer_name` VARCHAR(64),
  `area` VARCHAR(128) COMMENT '区域',
  `address` VARCHAR(128) COMMENT '地址',
  `address_id` BIGINT COMMENT '楼栋门牌',
  `area_size` DECIMAL(10,2) COMMENT '面积',
  `contract_start_date` DATETIME COMMENT '合同开始日期',
  `contract_end_date` DATETIME COMMENT '合同结束日期',
  `contract_end_month` INTEGER COMMENT '合同结束月份',
  `remark` VARCHAR(512) COMMENT '企业评级',
  `status` TINYINT NOT NULL DEFAULT 2,
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;    
    
-- 离场信息
CREATE TABLE `eh_customer_departure_infos` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `customer_id` BIGINT,
  `customer_name` VARCHAR(64),
  `review_time` DATETIME COMMENT '评审时间',
  `hatch_months` INTEGER COMMENT '孵化时间(X月)',
  `departure_nature` TINYINT COMMENT '离场性质',
  `departure_direction` TINYINT COMMENT '离场去向',
  `remark` VARCHAR(512) COMMENT '备注',
  `status` TINYINT NOT NULL DEFAULT 2,
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;     
