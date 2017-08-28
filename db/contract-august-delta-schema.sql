-- 张江高科同步数据备份表
-- DROP TABLE IF EXISTS `eh_zj_syncdata_backup`;
CREATE TABLE `eh_zj_syncdata_backup` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `data_type` TINYINT NOT NULL COMMENT '1: community, 2: building, 3: apartment, 4: enterprise',
  `all_flag` tinyint(4) NOT NULL COMMENT '1: all data, 0: special community',
  `update_community` VARCHAR(64) COMMENT 'if all flag is 0, the special community identifier',
  `next_page_offset` INTEGER COMMENT 'next page offset',
  `name` VARCHAR(64) NOT NULL,
  `data` LONGTEXT COMMENT 'data list',
  `status` TINYINT NOT NULL COMMENT '0: inactive, 2: active',
  `create_time` DATETIME,
  `creator_uid` BIGINT,
  `update_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_thirdpart_configurations`;
CREATE TABLE `eh_thirdpart_configurations` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `name` VARCHAR(64) NOT NULL,
  `value` VARCHAR(512) NOT NULL,
  `description` VARCHAR(256),
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- In some module, there are one or more tables which have lots of fields, it need to be grouped to be more readable in the view,
-- this table will store these groups. The groups may be hierarchical, mainly for subgroups.
-- DROP TABLE IF EXISTS `eh_var_field_groups`;
CREATE TABLE `eh_var_field_groups` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `module_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the module who has many fields need to be grouped',
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of the parent group, it is 0 when there is no parent',
  `path` VARCHAR(128) COMMENT 'path from the root',
  `title` VARCHAR(128) COMMENT 'the title of the group',
  `name` VARCHAR(128) COMMENT 'user name',
  `mandatory_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'the field is mandatory to input something or not, 0-not mandatory, 1-mandatory',
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
 
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
 
 
-- The definition of available fields in db, it is global instead of related to the namespace.
-- When the db table of module, the fields are determinated, all of them need to be copied to 
-- this table, and help to display in the view.
-- DROP TABLE IF EXISTS `eh_var_fields`;
CREATE TABLE `eh_var_fields` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `module_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the module which the field belong to',
  `name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'The field logic name, it map to the field in db',
  `display_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'The field display name',
  `field_type` VARCHAR(128) COMMENT '',
  `group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to eh_var_field_groups',
  `group_path` VARCHAR(128) COMMENT 'path from the root',
  `mandatory_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'the field is mandatory to input something or not, 0-not mandatory, 1-mandatory',
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
 
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
 
 
-- For some fields, there is a list of items attaching to it, such as <select> tag.
-- This table stores these items, and fetch by genneral api.
-- DROP TABLE IF EXISTS `eh_var_field_items`;
CREATE TABLE `eh_var_field_items` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `module_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the module which the field belong to',
  `field_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'The field which the item belong to',
  `display_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'The item display name',
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
 
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
 
 
-- The really used groups in namespace
-- DROP TABLE IF EXISTS `eh_var_field_group_scopes`;
CREATE TABLE `eh_var_field_group_scopes` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `module_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the module which the field belong to',
  `group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to eh_var_field_groups',
  `group_display_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the group name, it will use the name in eh_var_field_groups if not defined',
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
 
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
 
 
-- The really used fields in namespace
-- DROP TABLE IF EXISTS `eh_var_field_scopes`;
CREATE TABLE `eh_var_field_scopes` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `module_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the module which the field belong to',
  `group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to eh_var_field_groups',
  `group_path` VARCHAR(128) COMMENT 'path from the root',
  `field_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to eh_var_fields',
  `field_param` VARCHAR(128) COMMENT '',
  `field_display_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the field name, it will use the name in eh_var_fields if not defined',
  `mandatory_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'the field is mandatory to input something or not, 0-not mandatory, 1-mandatory',
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
 
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
 
 
-- The really used field items in namespace
-- DROP TABLE IF EXISTS `eh_var_field_item_scopes`;
CREATE TABLE `eh_var_field_item_scopes` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `module_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the module which the field belong to',
  `field_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to eh_var_fields',
  `item_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to eh_var_field_items',
  `item_display_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the field name, it will use the name in eh_var_field_items if not defined',
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
 
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_enterprise_customers`;
CREATE TABLE `eh_enterprise_customers` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of eh_organizations',
  `community_id` BIGINT NOT NULL COMMENT 'id of eh_communities',
  `customer_number` VARCHAR(128) COMMENT 'default is id if not defined',
  `name` VARCHAR(128),
  `nick_name` VARCHAR(128),
  `category_item_id` BIGINT COMMENT '客户类型：业主、租户, refer to the id of eh_var_field_items',
  `category_item_name` VARCHAR(128) COMMENT '客户类型：业主、租户, refer to the display_name of eh_var_field_items',
  `level_item_id` BIGINT COMMENT '客户级别：普通客户、重要客户、意向客户、已成交客户、其他, refer to the id of eh_var_field_items',
  `level_item_name` VARCHAR(128) COMMENT '客户级别：普通客户、重要客户、意向客户、已成交客户、其他, refer to the display_name of eh_var_field_items',
  `source_item_id` BIGINT COMMENT '来源途径, refer to the id of eh_var_field_items',
  `source_item_name` VARCHAR(128) COMMENT '来源途径, refer to the display_name of eh_var_field_items',
  `contact_avatar_uri` VARCHAR(2048) COMMENT '联系人头像',
  `contact_name` VARCHAR(128) COMMENT '联系人名称',
  `contact_gender_item_id` BIGINT COMMENT '联系人性别, refer to the id of eh_var_field_items',
  `contact_gender_item_name` VARCHAR(128) COMMENT '联系人性别, refer to the display_name of eh_var_field_items',
  `contact_mobile` VARCHAR(64) COMMENT '联系人手机号码',
  `contact_phone` VARCHAR(64) COMMENT '联系人座机号码',
  `contact_offfice_phone` VARCHAR(64) COMMENT '办公电话',
  `contact_family_phone` VARCHAR(64) COMMENT '家庭电话',
  `contact_email` VARCHAR(128) COMMENT '电子邮件',
  `contact_fax` VARCHAR(128) COMMENT '传真',
  `contact_address_id` BIGINT COMMENT 'refer to id of eh_addresses',
  `contact_address` VARCHAR(1024) COMMENT '地址',
  `corp_email` VARCHAR(128) COMMENT '企业邮箱',
  `corp_website` VARCHAR(128) COMMENT '企业网址',
  `corp_reg_address` VARCHAR(1024) COMMENT '企业注册地址',
  `corp_op_address` VARCHAR(1024) COMMENT '企业运营地址',
  `corp_legal_person` VARCHAR(128) COMMENT '法人代表',
  `corp_reg_capital` DECIMAL(10,2) COMMENT '注册资金(万元)',
  `corp_nature_item_id` BIGINT COMMENT '企业性质: 国企、外企、港企、合资、民企、自然人、其他, refer to the id of eh_var_field_items',
  `corp_nature_item_name` VARCHAR(128) COMMENT '企业性质: 国企、外企、港企、合资、民企、自然人、其他, refer to the display_name of eh_var_field_items',
  `corp_scale` DECIMAL(10,2) COMMENT '企业规模',
  `corp_industry_item_id` BIGINT COMMENT '行业类型: 科技类、服务类, refer to the id of eh_var_field_items',
  `corp_industry_item_name` VARCHAR(128) COMMENT '行业类型: 科技类、服务类, refer to the display_name of eh_var_field_items',
  `corp_purpose_item_id` BIGINT COMMENT '企业定位, refer to the id of eh_var_field_items',
  `corp_purpose_item_name` VARCHAR(128) COMMENT '企业定位, refer to the display_name of eh_var_field_items',
  `corp_annual_turnover` DECIMAL(10,2) COMMENT '年营业额（万元）',
  `corp_business_scope` TEXT COMMENT '营业范围',
  `corp_business_license` VARCHAR(128) COMMENT '营业执照号',
  `corp_site_area` DECIMAL(10,2) COMMENT '场地面积',
  `corp_entry_date` DATETIME COMMENT '入住园区日期',
  `corp_product_category_item_id` BIGINT COMMENT '产品类型, refer to the id of eh_var_field_items',
  `corp_product_category_item_name` VARCHAR(128) COMMENT '产品类型, refer to the display_name of eh_var_field_items',
  `corp_product_desc` TEXT COMMENT '主要技术及产品',
  `corp_qualification_item_id` BIGINT COMMENT '企业资质认证: 高新技术企业、软件企业..., refer to the id of eh_var_field_items',
  `corp_qualification_item_name` VARCHAR(128) COMMENT '企业资质认证: 高新技术企业、软件企业..., refer to the display_name of eh_var_field_items',
  `corp_logo_uri` VARCHAR(2048) COMMENT '企业LOGO',
  `corp_description` TEXT COMMENT '企业简介',
  `corp_employee_amount` INTEGER COMMENT '员工总数',
  `corp_employee_amount_male` INTEGER COMMENT '男员工总数',
  `corp_employee_amount_female` INTEGER COMMENT '女员工总数',
  `corp_employee_amount_rd` INTEGER COMMENT '研发员工总数',
  `corp_employee_returnee_rate` DOUBLE COMMENT '海归人数占比(%)',
  `corp_employee_average_age` DOUBLE COMMENT '员工平均年龄',
  `corp_manager_average_age` DOUBLE COMMENT '高管平均年龄',
  
  `manager_name` VARCHAR(128) COMMENT '总经理名称',
  `manager_phone` VARCHAR(64) COMMENT '总经理电话',
  `manager_email` VARCHAR(128) COMMENT '总经理邮箱',
  
  `remark` TEXT COMMENT '备注',
  
  `namespace_customer_type` VARCHAR(128),
  `namespace_customer_token` VARCHAR(128),
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_contracts` CHANGE `contract_end_date` `contract_end_date` DATETIME COMMENT '合同结束日期';
ALTER TABLE `eh_contracts` ADD COLUMN `community_id` BIGINT COMMENT '园区id';
ALTER TABLE `eh_contracts` ADD COLUMN `contract_start_date` DATETIME COMMENT '合同开始日期';
ALTER TABLE `eh_contracts` ADD COLUMN `name` VARCHAR(128) COMMENT '合同名称';
ALTER TABLE `eh_contracts` ADD COLUMN `contract_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0:新签合同、1:续约合同、2:变更合同、3:退约合同';
ALTER TABLE `eh_contracts` ADD COLUMN `create_uid` BIGINT COMMENT '经办人id';
ALTER TABLE `eh_contracts` ADD COLUMN `party_a_type` TINYINT NOT NULL DEFAULT 0 COMMENT '合同甲方类型 0: organization; 1: individual';
ALTER TABLE `eh_contracts` ADD COLUMN `party_a_id` BIGINT COMMENT '合同甲方id';
ALTER TABLE `eh_contracts` ADD COLUMN `party_a_name` VARCHAR(64) COMMENT '合同甲方名称';
ALTER TABLE `eh_contracts` ADD COLUMN `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual';
ALTER TABLE `eh_contracts` CHANGE `organization_id` `customer_id` BIGINT;
ALTER TABLE `eh_contracts` CHANGE `organization_name` `customer_name` VARCHAR(64);
ALTER TABLE `eh_contracts` ADD COLUMN `contract_situation` TEXT COMMENT '合同情况';
 
-- 合同状态 重新定义一下枚举

ALTER TABLE `eh_contracts` ADD COLUMN  `category_item_id` BIGINT COMMENT '合同类型: 资源租赁合同、物业服务合同、车位服务合同..., refer to the id of eh_var_field_items';
ALTER TABLE `eh_contracts` ADD COLUMN  `category_item_name` VARCHAR(128) COMMENT '合同类型: 资源租赁合同、物业服务合同、车位服务合同..., refer to the display_name of eh_var_field_items';
ALTER TABLE `eh_contracts` ADD COLUMN `advanced_notify_days` INTEGER NOT NULL DEFAULT 0 COMMENT '提前提醒天数';
ALTER TABLE `eh_contracts` ADD COLUMN `filing_place` VARCHAR(64) COMMENT '归档地';
ALTER TABLE `eh_contracts` ADD COLUMN `record_number` VARCHAR(32) COMMENT '备案号';
ALTER TABLE `eh_contracts` ADD COLUMN `invalid_uid` BIGINT COMMENT '作废人id';
ALTER TABLE `eh_contracts` ADD COLUMN `invalid_time` DATETIME COMMENT '作废时间';
ALTER TABLE `eh_contracts` ADD COLUMN `invalid_reason` VARCHAR(256) COMMENT '作废原因';
ALTER TABLE `eh_contracts` ADD COLUMN `review_uid` BIGINT COMMENT '审阅人id';
ALTER TABLE `eh_contracts` ADD COLUMN `review_time` DATETIME COMMENT '审阅时间';
ALTER TABLE `eh_contracts` ADD COLUMN `delete_uid` BIGINT COMMENT '删除人id';
ALTER TABLE `eh_contracts` ADD COLUMN `delete_time` DATETIME COMMENT '删除时间';
 
ALTER TABLE `eh_contracts` ADD COLUMN `signed_time` DATETIME COMMENT '签约时间';
ALTER TABLE `eh_contracts` ADD COLUMN `parent_id` BIGINT COMMENT '父合同id';
ALTER TABLE `eh_contracts` ADD COLUMN `root_parent_id` BIGINT COMMENT '根合同id';
ALTER TABLE `eh_contracts` ADD COLUMN `rent_size` DOUBLE COMMENT '出租面积';
ALTER TABLE `eh_contracts` ADD COLUMN `rent` DECIMAL(10,2) COMMENT '租金';
ALTER TABLE `eh_contracts` ADD COLUMN `downpayment` DECIMAL(10,2) COMMENT '首付款';
ALTER TABLE `eh_contracts` ADD COLUMN `downpayment_time` DATETIME COMMENT '首付截止日期';
ALTER TABLE `eh_contracts` ADD COLUMN `deposit` DECIMAL(10,2) COMMENT '定金';
ALTER TABLE `eh_contracts` ADD COLUMN `deposit_time` DATETIME COMMENT '定金最迟收取日期';
ALTER TABLE `eh_contracts` ADD COLUMN `contractual_penalty` DECIMAL(10,2) COMMENT '违约金';
ALTER TABLE `eh_contracts` ADD COLUMN `penalty_remark` VARCHAR(256) COMMENT '违约说明';
ALTER TABLE `eh_contracts` ADD COLUMN `commission` DECIMAL(10,2) COMMENT '佣金';
ALTER TABLE `eh_contracts` ADD COLUMN `paid_type` VARCHAR(32) COMMENT '付款方式';
ALTER TABLE `eh_contracts` ADD COLUMN `free_days` INTEGER COMMENT '免租期天数';
ALTER TABLE `eh_contracts` ADD COLUMN `free_parking_space` INTEGER COMMENT '赠送车位数量';
ALTER TABLE `eh_contracts` ADD COLUMN `decorate_begin_date` DATETIME COMMENT '装修开始日期';
ALTER TABLE `eh_contracts` ADD COLUMN `decorate_end_date` DATETIME COMMENT '装修结束日期';
ALTER TABLE `eh_contracts` ADD COLUMN `signed_purpose` VARCHAR(128) COMMENT '签约原因';


ALTER TABLE `eh_contracts` ADD COLUMN `source` VARCHAR(32) COMMENT 'contract source like zuolin...';
ALTER TABLE `eh_contracts` ADD COLUMN `source_id` VARCHAR(128) COMMENT 'contract source unique identifier...';
 
 
ALTER TABLE `eh_contracts` ADD COLUMN `denunciation_reason` VARCHAR(256) COMMENT '为退约合同的时候';
-- 如果退约合同只是改状态而不是一个新合同时:
ALTER TABLE `eh_contracts` ADD COLUMN `denunciation_time` DATETIME COMMENT '为退约合同的时候';
ALTER TABLE `eh_contracts` ADD COLUMN `denunciation_uid` BIGINT COMMENT '为退约合同的时候';
ALTER TABLE `eh_contracts` ADD COLUMN `remark` TEXT COMMENT '备注';
 
 
-- 合同-资产表： 
ALTER TABLE eh_contract_building_mappings ADD COLUMN `address_id` BIGINT;

-- 合同附件：
CREATE TABLE `eh_contract_attachments` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `contract_id` bigint(20) NOT NULL DEFAULT 0,
  `name` varchar(128) DEFAULT NULL,
  `file_size` int(11) NOT NULL DEFAULT '0',
  `content_type` varchar(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` varchar(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
 
-- 合同计价条款表： 
CREATE TABLE `eh_contract_charging_items` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `contract_id` BIGINT NOT NULL COMMENT 'id of eh_contracts',
  `charging_item_id` BIGINT COMMENT '收费项',
  `charging_standard_id` BIGINT COMMENT '收费标准',  
  `formula` varchar(1024) DEFAULT NULL,
  `formula_type` tinyint(4) DEFAULT NULL COMMENT '1: fixed fee; 2: normal formula; 3: gradient varied on variable price; 4: gradients varied functions on each variable section',
  `billing_cycle` tinyint(4) DEFAULT NULL,
  `late_fee_standard_id` BIGINT COMMENT '滞纳金标准',
  `charging_variables` varchar(1024) COMMENT '计费金额参数 json: {"variables":[{"variableIdentifier":"22","variableName":"面积","variableValue":"960.00"}]}',
  `charging_start_time` DATETIME,
  `charging_expired_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: active', 
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 条款作用资产：
CREATE TABLE `eh_contract_charging_item_addresses` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `contract_charging_item_id` BIGINT NOT NULL COMMENT 'id of eh_contract_charging_items',
  `address_id` BIGINT,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: active', 
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 合同参数表
CREATE TABLE `eh_contract_params` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `community_id` BIGINT COMMENT '园区id',
  `expiring_period` INTEGER NOT NULL DEFAULT 0 COMMENT '合同到期日前多久为即将到期合同',
  `expiring_unit` TINYINT NOT NULL DEFAULT 0 COMMENT '单位：0: 分; 1: 小时; 2: 天; 3: 月; 4: 年',
  `notify_period` INTEGER NOT NULL DEFAULT 0 COMMENT '提醒时间',
  `notify_unit` TINYINT NOT NULL DEFAULT 0 COMMENT '单位：0: 分; 1: 小时; 2: 天; 3: 月; 4: 年',
  `expired_period` INTEGER NOT NULL DEFAULT 0 COMMENT '审批通过合同转为过期的时间',
  `expired_unit` TINYINT NOT NULL DEFAULT 0 COMMENT '单位：0: 分; 1: 小时; 2: 天; 3: 月; 4: 年',
  `receivable_date` INTEGER NOT NULL DEFAULT 0 COMMENT '合同费用清单应收日期',
  `receivable_unit` TINYINT NOT NULL DEFAULT 0 COMMENT '单位：0: 分; 1: 小时; 2: 天; 3: 月; 4: 年',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 小区信息：
ALTER TABLE `eh_communities` ADD COLUMN `shared_area` DOUBLE COMMENT '公摊面积';
ALTER TABLE `eh_communities` ADD COLUMN `charge_area` DOUBLE COMMENT '收费面积';
ALTER TABLE `eh_communities` ADD COLUMN `build_area` DOUBLE COMMENT '建筑面积';
ALTER TABLE `eh_communities` ADD COLUMN `rent_area` DOUBLE COMMENT '出租面积';
ALTER TABLE `eh_communities` ADD COLUMN `namespace_community_type` VARCHAR(128);
ALTER TABLE `eh_communities` ADD COLUMN `namespace_community_token` VARCHAR(128);

-- 楼栋信息：
ALTER TABLE `eh_buildings` ADD COLUMN `construction_company` VARCHAR(128) COMMENT '施工单位';
ALTER TABLE `eh_buildings` ADD COLUMN `height` DOUBLE COMMENT '楼高 单位米';
ALTER TABLE `eh_buildings` ADD COLUMN `entry_date` DATETIME COMMENT '入驻时间';
ALTER TABLE `eh_buildings` ADD COLUMN `shared_area` DOUBLE COMMENT '公摊面积';
ALTER TABLE `eh_buildings` ADD COLUMN `charge_area` DOUBLE COMMENT '收费面积';
ALTER TABLE `eh_buildings` ADD COLUMN `build_area` DOUBLE COMMENT '建筑面积';
ALTER TABLE `eh_buildings` ADD COLUMN `rent_area` DOUBLE COMMENT '出租面积';

-- 门牌信息：
ALTER TABLE `eh_addresses` ADD COLUMN `shared_area` DOUBLE COMMENT '公摊面积';
ALTER TABLE `eh_addresses` ADD COLUMN `charge_area` DOUBLE COMMENT '收费面积';
ALTER TABLE `eh_addresses` ADD COLUMN  `category_item_id` BIGINT COMMENT '资产类型：住宅、写字楼、酒店式公寓、厂房、库房、车位、其他..., refer to the id of eh_var_field_items';
ALTER TABLE `eh_addresses` ADD COLUMN  `category_item_name` VARCHAR(128) COMMENT '资产类型：住宅、写字楼、酒店式公寓、厂房、库房、车位、其他..., refer to the display_name of eh_var_field_items';
ALTER TABLE `eh_addresses` ADD COLUMN  `source_item_id` BIGINT COMMENT '资产来源：自管、业主放盘、大业主交管、其他..., refer to the id of eh_var_field_items';
ALTER TABLE `eh_addresses` ADD COLUMN  `source_item_name` VARCHAR(128) COMMENT '资产来源：自管、业主放盘、大业主交管、其他..., refer to the display_name of eh_var_field_items';
ALTER TABLE `eh_addresses` ADD COLUMN `decorate_status` TINYINT COMMENT '装修状态';
ALTER TABLE `eh_addresses` ADD COLUMN `orientation` VARCHAR(32) COMMENT '朝向';


-- 企业人才：
CREATE TABLE `eh_customer_talents` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0 ,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `customer_id` BIGINT,
  `name` VARCHAR(64),
  `gender` BIGINT COMMENT '性别 refer to the id of eh_var_field_items',
  `phone` VARCHAR(32),
  `nationality_item_id` BIGINT COMMENT '国籍, refer to the id of eh_var_field_items',
  `degree_item_id` BIGINT COMMENT '最高学历, refer to the id of eh_var_field_items',
  `graduate_school` VARCHAR(128) COMMENT '毕业学校',
  `major` VARCHAR(128) COMMENT '所属专业',
  `experience` INTEGER COMMENT '工作经验',
  `returnee_flag` BIGINT COMMENT '是否海归 refer to the id of eh_var_field_items',
  `abroad_item_id` BIGINT COMMENT '留学国家, refer to the id of eh_var_field_items',
  `job_position` VARCHAR(128),
  `technical_title_item_id` BIGINT COMMENT '技术职称, refer to the id of eh_var_field_items',
  `individual_evaluation_item_id` BIGINT COMMENT '个人评定, refer to the id of eh_var_field_items',
  `personal_certificate` VARCHAR(256) COMMENT '个人证书',
  `career_experience` TEXT COMMENT '主要职业经历',
  `remark` TEXT,
  `status` TINYINT NOT NULL COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; 
-- 客户知识产权：
CREATE TABLE `eh_customer_trademarks` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 ,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `customer_id` BIGINT,
  `customer_name` VARCHAR(64),
  `name` VARCHAR(128) COMMENT '商标名称',
  `registe_date` DATETIME COMMENT '注册日期',
  `trademark_type_item_id` BIGINT COMMENT '商标类型: 文字商标、图片商标、品牌商标、著名商标..., refer to the id of eh_var_field_items',
  `trademark_type_item_name` VARCHAR(128) COMMENT '商标类型: 文字商标、图片商标、品牌商标、著名商标..., refer to the display_name of eh_var_field_items',
--  `trademark_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none; 1: text; 2: brand; 3: famous; 4: picture',
  `trademark_amount` INTEGER COMMENT '商标数量',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: active', 
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_customer_patents` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 ,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `customer_id` BIGINT,
  `customer_name` VARCHAR(64),
  `name` VARCHAR(128) COMMENT '证书名称',
  `registe_date` DATETIME COMMENT '注册日期',
  `patent_status_item_id` BIGINT COMMENT '专利状态 申请 授权..., refer to the id of eh_var_field_items',
  `patent_status_item_name` VARCHAR(128) COMMENT '专利状态 申请 授权..., refer to the display_name of eh_var_field_items',
  `patent_type_item_id` BIGINT COMMENT '专利类型 发明专利\实用新型\外观设计\集成电路布图\软件著作权\证书..., refer to the id of eh_var_field_items',
  `patent_type_item_name` VARCHAR(128) COMMENT '专利类型 发明专利\实用新型\外观设计\集成电路布图\软件著作权\证书..., refer to the display_name of eh_var_field_items',
  `patent_name` VARCHAR(128) COMMENT '专利名称',
  `application_number` VARCHAR(64) COMMENT '授权号',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: active', 
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
 
 
-- 客户申报项目：
CREATE TABLE `eh_customer_apply_projects` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 ,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `customer_id` BIGINT,
  `customer_name` VARCHAR(64),
  `project_name` VARCHAR(128) COMMENT '获批项目名称',
  `project_source` VARCHAR(128) COMMENT 'json of id list from eh_var_field_items and customer input text, split by ,',
  `project_establish_date` DATETIME COMMENT '项目立项日期',
  `project_complete_date` DATETIME COMMENT '项目完成日期',
  `project_amount` DECIMAL(10,2) COMMENT '获批项目金额 “万元”为单位',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: in progress; 2: completed', 
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

 
-- 客户工商信息：
CREATE TABLE `eh_customer_commercials` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 ,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: enterprise; 1: individual',
  `customer_id` BIGINT,
  `customer_name` VARCHAR(64),
  `enterprise_type_item_id` BIGINT COMMENT '企业类型: 企业、事业单位、政府机关、社会团体、民办非企业单位、基金会、其他组织机构..., refer to the id of eh_var_field_items',
  `enterprise_type_item_name` VARCHAR(128) COMMENT '企业类型: 企业、事业单位、政府机关、社会团体、民办非企业单位、基金会、其他组织机构..., refer to the display_name of eh_var_field_items',
  `share_type_item_id` BIGINT COMMENT '控股情况: 国有控股、集体控股、私人控股、港澳台商控股、外商投资、其他..., refer to the id of eh_var_field_items',
  `share_type_item_name` VARCHAR(128) COMMENT '控股情况: 国有控股、集体控股、私人控股、港澳台商控股、外商投资、其他..., refer to the display_name of eh_var_field_items',
--  `enterprise_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none; 1: enterprise; 2: institution; 3: government; 4: social group; 5: Private Non-enterprise Units; 6: Foundation; 7: other organizations',
--  `share_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none; 1: State holding; 2: collective holding; 3: private holding; 4: Hong Kong, Macao, Taiwan holding; 5: foreign investment; 6: others',
  `contact` VARCHAR(32)  COMMENT '联系人',
  `contact_number` VARCHAR(32) COMMENT '联系电话',
  `unified_social_credit_code` VARCHAR(64) COMMENT '统一社会信用代码',
  `business_scope` VARCHAR(128) COMMENT '主营业务',
  `foundation_date` DATETIME COMMENT '成立日期',
  `tax_registration_date` DATETIME COMMENT '税务登记日期',
  `validity_begin_date` DATETIME COMMENT '',
  `validity_end_date` DATETIME,
  `registered_addr` VARCHAR(128),
  `registered_capital` DECIMAL(10,2) COMMENT '注册资金',
  `paidup_apital` DECIMAL(10,2) COMMENT '实到资金',
  `property_type` BIGINT COMMENT ' refer to the id of eh_var_field_items', 
  `change_date` DATETIME COMMENT '变更日期',
  `business_licence_date` DATETIME,
  `liquidation_committee_recored_date` DATETIME COMMENT '清算组备案日期',
  `cancel_date` DATETIME COMMENT '注销日期',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: active', 
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
 
-- 客户投融情况：
CREATE TABLE `eh_customer_investments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 ,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `customer_id` BIGINT,
  `customer_name` VARCHAR(64),
  `government_project` VARCHAR(128),
  `bank_loans` DECIMAL(10,2) COMMENT '银行贷款',
  `equity_financing` DECIMAL(10,2) COMMENT '股权融资',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: active', 
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 客户经济指标：
CREATE TABLE `eh_customer_economic_indicators` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 ,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `customer_id` BIGINT,
  `customer_name` VARCHAR(64),
  `total_assets` DECIMAL(10,2) COMMENT '资产总计',
  `total_profit` DECIMAL(10,2) COMMENT '利润总额',
  `sales` DECIMAL(10,2) COMMENT '销售额',
  `turnover` DECIMAL(10,2) COMMENT '营业额',
  `tax_index` DECIMAL(10,2) COMMENT '税收指标',
  `tax_payment` DECIMAL(10,2) COMMENT '纳税额',
  `value_added_tax` DECIMAL(10,2) COMMENT '增值税',
  `business_tax` DECIMAL(10,2) COMMENT '营业税',
  `business_income_tax` DECIMAL(10,2) COMMENT '企业所得税',
  `foreign_company_income_tax` DECIMAL(10,2) COMMENT '外企所得税',
  `individual_income_tax` DECIMAL(10,2) COMMENT '个人所得税',
  `total_tax_amount` DECIMAL(10,2) COMMENT '税额合计',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: active', 
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 缴费模块表结构

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for eh_payment_bill_groups
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_bill_groups`;
CREATE TABLE `eh_payment_bill_groups` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `owner_type` varchar(64) NOT NULL,
  `name` varchar(10) DEFAULT NULL,
  `balance_date_type` tinyint(4) DEFAULT NULL COMMENT '1:pay each month; 2:each quarter; 3:each year',
  `bills_day` int(11) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `default_order` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单组表';

-- ----------------------------
-- Table structure for eh_payment_bill_groups_rules
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_bill_groups_rules`;
CREATE TABLE `eh_payment_bill_groups_rules` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `bill_group_id` bigint(20) NOT NULL DEFAULT '0',
  `charging_item_id` bigint(20) NOT NULL DEFAULT '0',
  `charging_standards_id` bigint(20) NOT NULL DEFAULT '0',
  `charging_item_name` varchar(32) DEFAULT NULL,
  `variables_json_string` varchar(2048) DEFAULT NULL COMMENT 'json strings of variables injected for a particular formula',
  `ownerType` varchar(32) NOT NULL,
  `ownerId` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='变量注入表';

-- ----------------------------
-- Table structure for eh_payment_bill_items
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_bill_items`;
CREATE TABLE `eh_payment_bill_items` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(20) NOT NULL DEFAULT '0',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `owner_type` varchar(64) NOT NULL,
  `bill_group_id` bigint(20) DEFAULT NULL,
  `charging_items_id` bigint(20) NOT NULL DEFAULT '0',
  `bill_id` bigint(20) NOT NULL DEFAULT '0',
  `amount_receivable` decimal(10,2) DEFAULT '0.00',
  `amount_received` decimal(10,2) DEFAULT '0.00',
  `amount_owed` decimal(10,2) DEFAULT '0.00',
  `target_type` varchar(32) DEFAULT NULL,
  `target_id` bigint(20) DEFAULT NULL,
  `target_name` varchar(32) DEFAULT NULL COMMENT '客户名称，客户没有在系统中时填写',
  `contract_num` varchar(255) DEFAULT NULL,
  `address_id` bigint(20) DEFAULT NULL,
  `date_str` varchar(10) DEFAULT NULL COMMENT '账期',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `charging_item_name` varchar(32) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未缴费;1:已缴费',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='明细';

-- ----------------------------
-- Table structure for eh_payment_bill_notice_records
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_bill_notice_records`;
CREATE TABLE `eh_payment_bill_notice_records` (
  `id` bigint(20) NOT NULL,
  `bill_id` bigint(20) NOT NULL DEFAULT '0',
  `notice_date` datetime DEFAULT NULL,
  `target_type` varchar(32) DEFAULT NULL COMMENT 'untrack, user',
  `target_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'target user id if target_type is a user',
  `target_name` varchar(32) DEFAULT NULL,
  `target_contact_tel` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='发送记录表';

-- ----------------------------
-- Table structure for eh_payment_bills
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_bills`;
CREATE TABLE `eh_payment_bills` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `owner_type` varchar(64) NOT NULL,
  `bill_group_id` bigint(20) DEFAULT NULL,
  `date_str` varchar(10) DEFAULT NULL,
  `address_id` bigint(20) DEFAULT NULL,
  `amount_receivable` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT 'amount should be received',
  `amount_received` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT 'amount actually received by far',
  `amount_owed` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT 'unpaid amount',
  `amount_exemption` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT 'amount reduced',
  `amount_supplement` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT 'amount increased',
  `target_type` varchar(32) DEFAULT NULL COMMENT 'untrack, user',
  `target_id` bigint(20) DEFAULT '0' COMMENT 'target user id if target_type is a user',
  `contract_num` varchar(255) DEFAULT NULL,
  `target_name` varchar(32) DEFAULT '' COMMENT '客户名称',
  `apartment_name` varchar(255) DEFAULT NULL,
  `building_name` varchar(255) DEFAULT NULL,
  `noticeTel` varchar(32) DEFAULT '' COMMENT '催缴电话',
  `status` tinyint(4) DEFAULT '0' COMMENT '0: upfinished; 1: paid off',
  `notice_times` int(11) DEFAULT '0' COMMENT 'times bill owner has been called for dued payments',
  `switch` tinyint(4) DEFAULT '0' COMMENT '0:未出账单；1：已出账单；3：其他作用状态',
  `creator_id` bigint(20) DEFAULT NULL,
  `creat_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `operator_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单表';

-- ----------------------------
-- Table structure for eh_payment_charging_item_scopes
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_charging_item_scopes`;
CREATE TABLE `eh_payment_charging_item_scopes` (
  `id` bigint(20) NOT NULL,
  `charging_item_id` bigint(20) NOT NULL DEFAULT '0',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `owner_type` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收费项目范围表';

-- ----------------------------
-- Table structure for eh_payment_charging_items
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_charging_items`;
CREATE TABLE `eh_payment_charging_items` (
  `id` bigint(20) NOT NULL,
  `name` varchar(15) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `default_order` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收费项目表';

-- ----------------------------
-- Table structure for eh_payment_charging_standards
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_charging_standards`;
CREATE TABLE `eh_payment_charging_standards` (
  `id` bigint(20) NOT NULL,
  `name` varchar(10) DEFAULT NULL,
  `charging_items_id` bigint(20) NOT NULL DEFAULT '0',
  `formula` varchar(1024) DEFAULT NULL,
  `formula_json` varchar(2048) DEFAULT NULL,
  `formula_type` tinyint(4) DEFAULT NULL COMMENT '1: fixed fee; 2: normal formula; 3: gradient varied on variable price; 4: gradients varied functions on each variable section',
  `billing_cycle` tinyint(4) DEFAULT NULL,
  `price_unit_type` tinyint(4) DEFAULT NULL COMMENT '1:日单价; 2:月单价; 3:季单价; 4:年单价',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收费标准表';

-- ----------------------------
-- Table structure for eh_payment_charging_standards_scopes
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_charging_standards_scopes`;
CREATE TABLE `eh_payment_charging_standards_scopes` (
  `id` bigint(20) NOT NULL,
  `charging_standard_id` bigint(20) NOT NULL DEFAULT '0',
  `owner_type` varchar(64) NOT NULL,
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收费标准表';

-- ----------------------------
-- Table structure for eh_payment_exemption_items
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_exemption_items`;
CREATE TABLE `eh_payment_exemption_items` (
  `id` bigint(20) NOT NULL,
  `bill_id` bigint(20) NOT NULL DEFAULT '0',
  `bill_group_id` bigint(20) DEFAULT NULL,
  `target_type` varchar(255) DEFAULT NULL,
  `target_id` bigint(20) DEFAULT NULL,
  `targetName` varchar(255) DEFAULT NULL COMMENT '客户名称，客户没有在系统中时填写',
  `remarks` varchar(255) DEFAULT NULL,
  `amount` decimal(10,2) NOT NULL DEFAULT '0.00',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='额外项';

-- ----------------------------
-- Table structure for eh_payment_variables
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_variables`;
CREATE TABLE `eh_payment_variables` (
  `id` bigint(20) NOT NULL,
  `charging_standard_id` bigint(20) DEFAULT NULL,
  `charging_items_id` bigint(20) NOT NULL,
  `name` varchar(10) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `identifier` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='变量表';
-- ----------------------------
-- Table structure for eh_payment_contract_receiver
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_contract_receiver`;
CREATE TABLE `eh_payment_contract_receiver` (
  `id` bigint(20) NOT NULL,
  `namespace_id` bigint(20) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `owner_type` varchar(255) DEFAULT NULL,
  `target_id` bigint(20) DEFAULT NULL,
  `target_type` varchar(255) DEFAULT NULL,
  `variables_json_string` varchar(2048) DEFAULT NULL,
  `eh_payment_charging_standard_id` bigint(20) DEFAULT NULL,
  `eh_payment_charging_item_id` bigint(20) DEFAULT NULL,
  `contract_num` varchar(255) DEFAULT NULL,
  `target_name` varchar(255) DEFAULT NULL,
  `notice_tel` varchar(255) DEFAULT NULL,
  `building_name` varchar(255) DEFAULT NULL,
  `apartment_name` varchar(255) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '0' COMMENT '1:有效；0：无效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;