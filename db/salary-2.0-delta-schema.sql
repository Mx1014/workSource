 

-- 薪酬设置可以用的基础字段项(可以被公司继承,不可删除)
DROP TABLE IF EXISTS eh_salary_default_entities;
CREATE TABLE `eh_salary_default_entities` (
  `id` BIGINT COMMENT 'id of the record', 
  `editable_flag` TINYINT COMMENT '是否可编辑:-1 数值也不能编辑 0-否   1-是',
  `delete_flag` TINYINT COMMENT '是否可删除:0-否   1-是',
  `type` TINYINT COMMENT '字段类型:0-发放项;1-扣款项;2-成本项;3-冗余项',
  `data_policy` TINYINT COMMENT '数据策略:0-次月延用 1-次月清空',
  `grant_policy` TINYINT COMMENT '发放策略:0-税前 1-税后',
  `tax_policy` TINYINT COMMENT '纳税策略:0-工资 1-年终',
  `category_id` BIGINT COMMENT '标签(薪酬结构) category表pk',
  `category_name` VARCHAR(64) COMMENT '标签(统计分类)名称 example:固定工资,浮动工资,津补贴',
  `name` VARCHAR(32), 
  `description` TEXT COMMENT '说明文字',
  `template_name` VARCHAR(32) COMMENT '',
  `default_order` INT,
  `creator_uid` BIGINT,
  `create_time` DATETIME, 
  `status` TINYINT COMMENT '默认是否开启0不开启 2-开启',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;



-- 薪酬批次可用的选项的标签类型 基础数据
DROP TABLE IF EXISTS eh_salary_entity_categories;
CREATE TABLE `eh_salary_entity_categories` (
  `id` BIGINT,
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT,
  `namespace_id` INT COMMENT '并不用,现在是所有域空间通用', 
  `category_name` VARCHAR(64)  COMMENT 'name of category',
  `description` TEXT COMMENT '说明文字',
  `custom_flag` TINYINT COMMENT '是否可以自定义: 0-否 1-是',
  `custom_type` TINYINT COMMENT '自定义字段的类型:0-发放项;1-扣款项;2-成本项;3-冗余项',
  `status` TINYINT ,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT, 
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;


-- 薪酬批次包含的选项
DROP TABLE IF EXISTS eh_salary_group_entities;
CREATE TABLE `eh_salary_group_entities` (
  `id` BIGINT COMMENT 'id of the record', 
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT COMMENT '属于哪一个总公司的', 
  `origin_entity_id` BIGINT,
  `namespace_id` INT COMMENT '并不用,现在是所有域空间通用',
  `default_flag` TINYINT COMMENT '是否是缺省启用参数:0-否 1-是',
  `editable_flag` TINYINT COMMENT '是否可编辑:0-否   1-是',
  `delete_flag` TINYINT COMMENT '是否可删除:0-否   1-是',
  `type` TINYINT COMMENT '字段类型:0-发放项;1-扣款项;2-成本项;3-冗余项',
  `data_policy` TINYINT COMMENT '数据策略:0-次月延用 1-次月清空',
  `grant_policy` TINYINT COMMENT '发放策略:0-税前 1-税后',
  `tax_policy` TINYINT COMMENT '纳税策略:0-工资 1-年终',
  `category_id` BIGINT COMMENT '标签(薪酬结构) category表pk',
  `category_name` VARCHAR(64) COMMENT '标签(统计分类)名称 example:固定工资,浮动工资,津补贴',
  `name` VARCHAR(32), 
  `description` TEXT COMMENT '说明文字',
  `creator_uid` BIGINT,
  `create_time` DATETIME, 
  `operator_uid` BIGINT,
  `update_time` DATETIME, 
  `status` TINYINT ,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;


-- 某个人的薪酬设定
DROP TABLE IF EXISTS eh_salary_employee_origin_vals;
CREATE TABLE `eh_salary_employee_origin_vals` (
  `id` BIGINT COMMENT 'id of the record', 
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT COMMENT '属于哪一个总公司的',
  `namespace_id` INT ,
  `user_id` BIGINT ,
  `user_detail_id` BIGINT , 
  `group_entity_id` BIGINT COMMENT '标签(统计分类) salary group entity表pk', 
  `group_entity_name` VARCHAR(32),
  `type` TINYINT COMMENT '字段类型:0-发放项;1-扣款项;2-成本项;3-冗余项',
  `data_policy` TINYINT COMMENT '数据策略:0-次月延用 1-次月清空',
  `grant_policy` TINYINT COMMENT '发放策略:0-税前 1-税后',
  `tax_policy` TINYINT COMMENT '纳税策略:0-工资 1-年终',
  `category_id` BIGINT COMMENT '标签(薪酬结构) category表pk',
  `category_name` VARCHAR(64) COMMENT '标签(统计分类)名称 example:固定工资,浮动工资,津补贴',
  `salary_value` TEXT COMMENT '值:如果次月清空则在新建报表时候置为null', 
  `creator_uid` BIGINT,
  `create_time` DATETIME, 
  `status` TINYINT ,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;

DROP TABLE IF EXISTS eh_salary_groups;
-- 薪酬批次每期的数据 

-- 薪酬批次每个人的每期数据
DROP TABLE IF EXISTS eh_salary_employees;
CREATE TABLE `eh_salary_employees` (
  `id` BIGINT COMMENT 'id of the record', 
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT COMMENT '属于哪一个总公司的',
  `namespace_id` INT ,
  `salary_period` VARCHAR(8) COMMENT 'example:201705',
  `user_id` BIGINT ,
  `user_detail_id` BIGINT , 
  `regular_salary` DECIMAL (10, 2) COMMENT '固定工资合计',
  `should_pay_salary` DECIMAL (10, 2) COMMENT '应发工资合计',
  `real_pay_salary` DECIMAL (10, 2) COMMENT '实发工资合计',
  `creator_uid` BIGINT COMMENT'人员id',
  `create_time` DATETIME, 
  `status` TINYINT COMMENT '状态0-正常 1-实发合计为负  2-未定薪',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;


-- 薪酬薪酬人员每期的实际值
DROP TABLE IF EXISTS eh_salary_employee_period_vals;
CREATE TABLE `eh_salary_employee_period_vals` (
  `id` BIGINT COMMENT 'id of the record', 
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT,
  `namespace_id` INT ,
  `salary_period` VARCHAR(8) COMMENT 'example:201705',
  `salary_employee_id` BIGINT COMMENT '标签(统计分类) salary_employee表pk', 
  `group_entity_id` BIGINT COMMENT '标签(统计分类) salary group entity表pk', 
  `group_entity_name` VARCHAR(32),
  `type` TINYINT COMMENT '字段类型:0-发放项;1-扣款项;2-成本项;3-冗余项',
  `data_policy` TINYINT COMMENT '数据策略:0-次月延用 1-次月清空',
  `grant_policy` TINYINT COMMENT '发放策略:0-税前 1-税后',
  `tax_policy` TINYINT COMMENT '纳税策略:0-工资 1-年终',
  `category_id` BIGINT COMMENT '标签(薪酬结构) category表pk',
  `category_name` VARCHAR(64) COMMENT '标签(统计分类)名称 example:固定工资,浮动工资,津补贴',
  `salary_value` TEXT COMMENT '值:如果次月清空则在新建报表时候置为null', 
  `creator_uid` BIGINT,
  `create_time` DATETIME, 
  `status` TINYINT COMMENT '',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;

 
   