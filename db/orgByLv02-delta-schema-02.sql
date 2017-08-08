 

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
  `member_id` BIGINT ,
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
  `member_id` BIGINT ,
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
  `current_id` BIGINT(20) DEFAULT NULL COMMENT 'id of target, organization or memberDetail',
  `current_type` VARCHAR(32) COMMENT 'organziation,memberDetail',
  `current_name` VARCHAR(32) COMMENT 'name',
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

ALTER TABLE eh_organizations ADD COLUMN `email_content` TEXT COMMENT '工资条发送邮件内容';



SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for members' details
-- ----------------------------
-- DROP TABLE IF EXISTS `eh_organization_member_details`;
CREATE TABLE `eh_organization_member_details` (
  `id` BIGINT NOT NULL COMMENT 'id for members， reference for eh_organization_member detail_id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `target_type` VARCHAR(64),
  `target_id` BIGINT NOT NULL,
  `birthday` DATE COMMENT 'the birthday of the member',
  `organization_id` BIGINT NOT NULL COMMENT 'reference for eh_organization_member organization_id' ,
  `contact_name` VARCHAR(64) COMMENT 'the name of the member',
  `contact_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: mobile, 1: email',
  `contact_token` VARCHAR(128) COMMENT 'phone number, reference for eh_organization_member contact_token',
  `contact_description` TEXT,
  `employee_no` VARCHAR(128) COMMENT 'the employee number for the member',
  `avatar` varchar(128) COMMENT '头像',
  `gender` TINYINT DEFAULT 0 COMMENT '0: undisclosured, 1: male, 2: female',
  `marital_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: undisclosured, 1: married, 2: unmarried',
  `political_status` VARCHAR(128) COMMENT '政治面貌',
  `native_place` VARCHAR(128) COMMENT '籍贯',
  `en_name` VARCHAR(128) COMMENT 'english name',
  `reg_residence` VARCHAR(128) COMMENT '户口',
  `id_number` VARCHAR(64) COMMENT 'ID Card number',
  `email` VARCHAR(128) COMMENT 'email for members',
  `wechat` VARCHAR(128),
  `qq` VARCHAR(128),
  `emergency_name` VARCHAR(128) COMMENT 'emergency contact name',
  `emergency_contact` VARCHAR(128) COMMENT 'emergency contact tel-number',
  `address` VARCHAR(255) COMMENT 'address for the member',
  `employee_type` TINYINT COMMENT '0: full-time, 1: part-time, 2: internship, 3: labor dispatch',
  `employee_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: probation, 1: on the job, 2: leave the job',
  `employment_time` DATE COMMENT '转正日期',
  `dimission_time` DATE COMMENT '离职日期',
  `salary_card_number` VARCHAR(128) COMMENT '工资卡号',
  `social_security_number` VARCHAR(128) COMMENT '社保号',
  `provident_fund_number` VARCHAR(128) COMMENT '公积金号',
  `profile_integrity` INTEGER DEFAULT 0 COMMENT '档案完整度，0-100%',
  `check_in_time` DATE NOT NULL COMMENT '入职日期',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for members' education experiences
-- ----------------------------
-- DROP TABLE IF EXISTS `eh_organization_member_education`;
CREATE TABLE `eh_organization_member_educations` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `detail_id` BIGINT NOT NULL COMMENT 'id for members, reference for eh_organization_member_details id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `school_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the member''s school name',
  `degree` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'doctor, master, bachelor, etc',
  `major` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the major of the member',
  `enrollment_time` DATE NOT NULL COMMENT 'the time to start a new semester',
  `graduation_time` DATE NOT NULL COMMENT 'when the member graduated form the school',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `status` TINYINT COMMENT '0: inactive, 3: active',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for members' working experiences
-- ----------------------------
-- DROP TABLE IF EXISTS `eh_organization_member_working`;
CREATE TABLE `eh_organization_member_work_experiences` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `detail_id` BIGINT NOT NULL COMMENT 'id for members, reference for eh_organization_member_details id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `enterprise_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the name of company',
  `position` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the position of the member',
  `job_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: full-time, 1: part-time',
  `entry_time` DATE NOT NULL COMMENT 'timing of start the job',
  `departure_time` DATE NOT NULL COMMENT 'timing of quit the job',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `status` TINYINT COMMENT '0: inactive, 3: active',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for the situation of members' insurance situation
-- ----------------------------
-- DROP TABLE IF EXISTS `eh_organization_member_insurance`;
CREATE TABLE `eh_organization_member_insurances` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `detail_id` BIGINT NOT NULL COMMENT 'id for members, reference for eh_organization_member_details id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the name of the insurance',
  `enterprise` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the company name of the insurance',
  `number` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the number of the insurance',
  `start_time` DATE NOT NULL COMMENT '生效时间',
  `end_time` DATE NOT NULL COMMENT '到期时间',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `status` TINYINT COMMENT '0: inactive, 3: active',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for the situation of members' contracts
-- ----------------------------
-- DROP TABLE IF EXISTS `eh_organization_member_contracts`;
CREATE TABLE `eh_organization_member_contracts` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `detail_id` BIGINT NOT NULL COMMENT 'id for members, reference for eh_organization_member_details id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `contract_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the type of the contract',
  `contract_number` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the number of the contract',
  `start_time` DATE NOT NULL COMMENT '生效时间',
  `end_time` DATE NOT NULL COMMENT '到期时间',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `status` TINYINT COMMENT '0: inactive, 3: active',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for the members' operation records
-- ----------------------------
-- DROP TABLE IF EXISTS `eh_organization_member_profile_logs`;
CREATE TABLE `eh_organization_member_profile_logs` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `detail_id` BIGINT NOT NULL COMMENT 'id for members, reference for eh_organization_member_details id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `person_change_type` VARCHAR(64) COMMENT '人员变动类型:入职,转正,离职;变更部门,岗位,职级',
  `person_change_reason` VARCHAR(256) COMMENT 'person change reason',
  `operation_type` VARCHAR(32) COMMENT 'add,update,delete',
  `operation_time` DATETIME NOT NULL COMMENT 'when the information of the employee has been changed',
  `operator_uid` BIGINT COMMENT 'id of operator',
  `resource_type` VARCHAR(32) COMMENT 'the name of the table',
  `resource_id` BIGINT COMMENT 'reference for table_id',
  `result_code` INTEGER COMMENT '0: unsucceed, 1: succeed',
  `original_content` LONGTEXT COMMENT 'original records, use json format',
  `audit_content` LONGTEXT COMMENT 'modified records, use json format',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 新增字段
ALTER TABLE `eh_organization_members` ADD COLUMN `detail_id` BIGINT COMMENT 'id for detail records';

ALTER TABLE `eh_service_alliances` CHANGE COLUMN `default_order` `default_order` BIGINT COMMENT 'default value is id';


--3.备份eh_organization_members表为eh_organization_members_temp
-- DROP TABLE IF EXISTS `eh_organization_members_temp`;
create table eh_organization_members_temp select * from eh_organization_members;