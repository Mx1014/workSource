-- 公司的组 比如考勤组,薪酬组
-- 这个表待定
CREATE TABLE `eh_organization_groups` (
  `id` BIGINT COMMENT 'id of the record', 
  `namespace_id` INT DEFAULT '0',
  `organization_id` BIGINT,
  `name` VARCHAR(128) ,
  `display_name` VARCHAR(64) ,
  `organization_group_type` TINYINT COMMENT '公司组类型: 1-考勤组类型 2-薪酬组类型',
  `description` TEXT,
  `operator_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME,
  `creator_uid` BIGINT,
  `create_time` DATETIME, 
  `status` TINYINT ,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_organization_displayname` (`organization_group_type`,`display_name`,`organization_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;

-- 组囊括公司的人/部门
-- 待定
CREATE TABLE `eh_organization_group_members` (
  `id` BIGINT COMMENT 'id of the record', 
  `namespace_id` INT DEFAULT '0',
  `organization_id` BIGINT,
  `group_id` BIGINT COMMENT 'pk:organization gourp id',
  `member_type` VARCHAR(32) COMMENT 'member object type,ep: organization / user',
  `member_id` BIGINT,
  `creator_uid` BIGINT,
  `create_time` DATETIME, 
  `status` TINYINT ,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_organization_member` (`member_type`,`member_id`,`organization_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;


-- 薪酬批次可用的选项 基础数据
CREATE TABLE `eh_salary_default_entities` (
  `id` BIGINT COMMENT 'id of the record', 
  `namespace_id` INT COMMENT '并不用,现在是所有域空间通用',
  `default_flag` TINYINT COMMENT '是否是缺省参数:0-否   1-是',
  `type` TINYINT COMMENT '字段类型:0文本类;1数值类',
  `category_id` BIGINT COMMENT '标签(统计分类) category表pk',
  `category_name` VARCHAR(64) COMMENT '标签(统计分类)名称 example:基础,应发,应收,合计',
  `name` VARCHAR(32),  
  `editable_flag` TINYINT COMMENT '是否可编辑(对文本类):0-否   1-是',
  `templete_name` VARCHAR(32) COMMENT '',
  `default_order` INT,
  `creator_uid` BIGINT,
  `create_time` DATETIME, 
  `status` TINYINT ,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;



-- 薪酬批次可用的选项的标签类型 基础数据
CREATE TABLE `eh_salary_entity_categories` (
  `id` BIGINT,
  `namespace_id` INT COMMENT '并不用,现在是所有域空间通用',
  `owner_type` VARCHAR(32),
  `owner_id` BIGINT,
  `entity_type` TINYINT COMMENT '1. ask for leave, 2. forget to punch',
  `category_name` VARCHAR(64)  COMMENT 'name of category',
  `status` TINYINT COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT, 
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;


-- 薪酬批次包含的选项
CREATE TABLE `eh_salary_group_entities` (
  `id` BIGINT COMMENT 'id of the record', 
  `namespace_id` INT ,
  `group_id` BIGINT COMMENT '标签(统计分类) organization group表pk',
  `origin_entity_id` BIGINT,
  `type` TINYINT COMMENT '字段类型:0文本类;1数值类',
  `category_id` BIGINT COMMENT '标签(统计分类) category表pk',
  `category_name` VARCHAR(64) COMMENT '标签(统计分类)名称 example:基础,应发,应收,合计',
  `name` VARCHAR(32),  
  `editable_flag` TINYINT COMMENT '是否可编辑(对文本类):0-否   1-是',
  `number_type` TINYINT COMMENT '数值类型:0-普通数值 1-计算公式',
  `default_value` TEXT COMMENT'默认值/默认数值/计算公式',
  `need_check` TINYINT COMMENT'是否需要核算:0-否 1-是',
  `default_order` INT,
  `visible_flag` TINYINT DEFAULT 1 COMMENT'是否展示到工资条:0-否 1-是',
  `creator_uid` BIGINT,
  `create_time` DATETIME, 
  `status` TINYINT ,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;


-- 某个人的薪酬设定
CREATE TABLE `eh_salary_employee_origin_vals` (
  `id` BIGINT COMMENT 'id of the record', 
  `namespace_id` INT ,
  `group_id` BIGINT COMMENT '标签(统计分类) organization group表pk', 
  `user_id` BIGINT ,
  `group_entity_id` BIGINT COMMENT '标签(统计分类) salary group entity表pk', 
  `origin_entity_id` BIGINT,
  `entity_name` VARCHAR(64) COMMENT '项目字段名称',
  `salary_value` TEXT , 
  `creator_uid` BIGINT,
  `create_time` DATETIME, 
  `status` TINYINT ,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;


-- 薪酬批次每期的数据
CREATE TABLE `eh_salary_groups` (
  `id` BIGINT COMMENT 'id of the record', 
  `namespace_id` INT ,
  `salary_period` VARCHAR(32) COMMENT 'example:201705',
  `organization_group_id` BIGINT COMMENT '标签(统计分类) organization group表pk',  
  `group_name` VARCHAR(64) COMMENT '批次名',
  `send_time` DATETIME COMMENT '预发送时间:不预发送则为null',  
  `email_content` TEXT COMMENT '工资条发送邮件内容',
  `creator_uid` BIGINT,
  `create_time` DATETIME, 
  `status` TINYINT COMMENT '状态0-未核算 1-已核算  3-定时发放等待中 4-已发放',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;

-- 薪酬批次每个人的每期数据
CREATE TABLE `eh_salary_employees` (
  `id` BIGINT COMMENT 'id of the record', 
  `namespace_id` INT ,
  `salary_period` VARCHAR(32) COMMENT 'example:201705',
  `user_id` BIGINT ,
  `creator_uid` BIGINT COMMENT'人员id',
  `organization_group_id` BIGINT COMMENT '标签(统计分类) organization group表pk',  
  `salary_group_id` BIGINT COMMENT '标签(统计分类) salary group表pk',   
  `email_content` TEXT COMMENT '工资条发送邮件内容',
  `create_time` DATETIME, 
  `status` TINYINT COMMENT '状态0-未核算 1-已核算  3-定时发放等待中 4-已发放',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;


-- 薪酬薪酬人员每期的实际值
CREATE TABLE `eh_salary_employee_period_vals` (
  `id` BIGINT COMMENT 'id of the record', 
  `namespace_id` INT ,
  `salary_employee_id` BIGINT COMMENT '标签(统计分类) salary_employee表pk', 
  `group_entity_id` BIGINT COMMENT '标签(统计分类) salary group entity表pk', 
  `origin_entity_id` BIGINT,
  `salary_value` TEXT , 
  `creator_uid` BIGINT,
  `create_time` DATETIME, 
  `status` TINYINT COMMENT '',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;


