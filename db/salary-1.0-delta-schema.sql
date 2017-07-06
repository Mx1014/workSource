 

-- 薪酬批次可用的选项 基础数据
CREATE TABLE `eh_salary_default_entities` (
  `id` BIGINT COMMENT 'id of the record', 
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT,
  `namespace_id` INT COMMENT '并不用,现在是所有域空间通用',
  `default_flag` TINYINT COMMENT '是否是缺省参数:0-否   1-是',
  `type` TINYINT COMMENT '字段类型:0文本类;1数值类',
  `category_id` BIGINT COMMENT '标签(统计分类) category表pk',
  `category_name` VARCHAR(64) COMMENT '标签(统计分类)名称 example:基础,应发,应收,合计',
  `name` VARCHAR(32),  
  `editable_flag` TINYINT COMMENT '是否可编辑(对文本类):0-否   1-是',
  `template_name` VARCHAR(32) COMMENT '',
  `default_order` INT,
  `creator_uid` BIGINT,
  `create_time` DATETIME, 
  `status` TINYINT ,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;



-- 薪酬批次可用的选项的标签类型 基础数据
CREATE TABLE `eh_salary_entity_categories` (
  `id` BIGINT,
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT,
  `namespace_id` INT COMMENT '并不用,现在是所有域空间通用', 
  `category_name` VARCHAR(64)  COMMENT 'name of category',
  `status` TINYINT ,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT, 
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;


-- 薪酬批次包含的选项
CREATE TABLE `eh_salary_group_entities` (
  `id` BIGINT COMMENT 'id of the record', 
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT,
  `namespace_id` INT ,
  `group_id` BIGINT COMMENT '标签(统计分类) 薪酬组表pk',
  `origin_entity_id` BIGINT,
  `type` TINYINT COMMENT '字段类型:0文本类;1数值类',
  `category_id` BIGINT COMMENT '标签(统计分类) category表pk',
  `category_name` VARCHAR(64) COMMENT '标签(统计分类)名称 example:基础,应发,应收,合计',
  `name` VARCHAR(32),  
  `editable_flag` TINYINT COMMENT '是否可编辑(对文本类):0-否   1-是',
  `template_name` VARCHAR(32) COMMENT '模板名称',
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
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT,
  `namespace_id` INT ,
  `group_id` BIGINT COMMENT '标签(统计分类) organization group表pk', 
  `user_id` BIGINT ,
  `user_detail_id` BIGINT ,
  `group_entity_id` BIGINT COMMENT '标签(统计分类) salary group entity表pk', 
  `group_entity_name` VARCHAR(32),  
  `origin_entity_id` BIGINT,
  `salary_value` TEXT , 
  `creator_uid` BIGINT,
  `create_time` DATETIME, 
  `status` TINYINT ,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;


-- 薪酬批次每期的数据
CREATE TABLE `eh_salary_groups` (
  `id` BIGINT COMMENT 'id of the record', 
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT,
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
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT,
  `namespace_id` INT ,
  `salary_period` VARCHAR(32) COMMENT 'example:201705',
  `user_id` BIGINT ,
  `user_detail_id` BIGINT ,
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
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT,
  `namespace_id` INT ,
  `salary_employee_id` BIGINT COMMENT '标签(统计分类) salary_employee表pk', 
  `group_entity_id` BIGINT COMMENT '标签(统计分类) salary group entity表pk', 
  `group_entity_name` VARCHAR(32),  
  `origin_entity_id` BIGINT,
  `salary_value` TEXT , 
  `creator_uid` BIGINT,
  `create_time` DATETIME, 
  `status` TINYINT COMMENT '',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;


-- DROP TABLE IF EXISTS `eh_uniongroup_configures`;
CREATE TABLE `eh_uniongroup_configures` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `enterprise_id` BIGINT(20) DEFAULT 0,
  `group_type` VARCHAR(32) COMMENT 'SalaryGroup,PunchGroup',
  `group_id` BIGINT(20) NOT NULL COMMENT 'id of group',
  `target_id` BIGINT(20) DEFAULT NULL COMMENT 'id of target, organization or memberDetail',
  `target_type` VARCHAR(32) COMMENT 'organziation,memberDetail',
  `operator_uid` BIGINT(20),
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--  DROP TABLE IF EXISTS `eh_uniongroup_member_details`;
CREATE TABLE `eh_uniongroup_member_details` (
  `id` BIGINT(20) NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `group_type` VARCHAR(32) COMMENT 'SalaryGroup,PunchGroup',
  `group_id` BIGINT(20) NOT NULL COMMENT 'id of group',
  `detail_id` BIGINT(20) DEFAULT NULL COMMENT 'id of target, only memberDetail',
  `target_type` VARCHAR(64),
  `target_id` BIGINT NOT NULL,
  `enterprise_id` BIGINT NOT NULL COMMENT 'enterprise_id' ,
  `contact_name` VARCHAR(64) COMMENT 'the name of the member',
  `contact_token` VARCHAR(128) COMMENT 'phone number, reference for eh_organization_member contact_token',
  `update_time` DATETIME,
  `operator_uid` BIGINT(20),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
