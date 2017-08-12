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
  `organization_id` BIGINT NOT NULL COMMENT 'id of eh_organizations',
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
  `corp_reg_capital` VARCHAR(128) COMMENT '注册资金(万元)',
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
  `corp_entry_date` DATE COMMENT '入住园区日期',
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
  `corp_employee_returnee_rate` INTEGER COMMENT '海归人数占比(%)',
  `corp_employee_average_age` INTEGER COMMENT '员工平均年龄',
  `corp_manager_average_age` INTEGER COMMENT '高管平均年龄',
  
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
 
-- 小区信息：
ALTER TABLE `eh_communities` ADD COLUMN `shared_area` DOUBLE COMMENT '公摊面积';
ALTER TABLE `eh_communities` ADD COLUMN `charge_area` DOUBLE COMMENT '收费面积';
ALTER TABLE `eh_communities` ADD COLUMN `build_area` DOUBLE COMMENT '建筑面积';
ALTER TABLE `eh_communities` ADD COLUMN `rent_area` DOUBLE COMMENT '出租面积';
ALTER TABLE `eh_communities` ADD COLUMN `namespace_community_type` VARCHAR(128);
ALTER TABLE `eh_communities` ADD COLUMN `namespace_community_token` VARCHAR(128);

-- 楼栋信息：
ALTER TABLE `eh_buildings` ADD COLUMN `construction_company` VARCHAR(128) COMMENT '施工单位';
ALTER TABLE `eh_buildings` ADD COLUMN `entry_date` DATETIME COMMENT '入驻时间';
ALTER TABLE `eh_buildings` ADD COLUMN `shared_area` DOUBLE COMMENT '公摊面积';
ALTER TABLE `eh_buildings` ADD COLUMN `charge_area` DOUBLE COMMENT '收费面积';
ALTER TABLE `eh_buildings` ADD COLUMN `build_area` DOUBLE COMMENT '建筑面积';
ALTER TABLE `eh_buildings` ADD COLUMN `rent_area` DOUBLE COMMENT '出租面积';

-- 门牌信息：
ALTER TABLE `eh_addresses` ADD COLUMN `shared_area` DOUBLE COMMENT '公摊面积';
ALTER TABLE `eh_addresses` ADD COLUMN `charge_area` DOUBLE COMMENT '收费面积';
ALTER TABLE `eh_addresses` ADD COLUMN  `category_item_id` BIGINT COMMENT '资产类型：住宅、写字楼、酒店式公寓、厂房、库房、车位、其他..., refer to the id of eh_var_field_items',
ALTER TABLE `eh_addresses` ADD COLUMN  `category_item_name` VARCHAR(128) COMMENT '资产类型：住宅、写字楼、酒店式公寓、厂房、库房、车位、其他..., refer to the display_name of eh_var_field_items',
ALTER TABLE `eh_addresses` ADD COLUMN  `source_item_id` BIGINT COMMENT '资产来源：自管、业主放盘、大业主交管、其他..., refer to the id of eh_var_field_items',
ALTER TABLE `eh_addresses` ADD COLUMN  `source_item_name` VARCHAR(128) COMMENT '资产来源：自管、业主放盘、大业主交管、其他..., refer to the display_name of eh_var_field_items',
ALTER TABLE `eh_addresses` ADD COLUMN `decorate_status` TINYINT COMMENT '装修状态';
ALTER TABLE `eh_addresses` ADD COLUMN `orientation` VARCHAR(32) COMMENT '朝向';