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
  
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;