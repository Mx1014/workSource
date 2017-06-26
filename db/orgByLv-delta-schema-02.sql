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

ALTER TABLE `eh_organization_members` ADD COLUMN `detail_id` BIGINT COMMENT 'id for detail records';

SET FOREIGN_KEY_CHECKS = 1;


-- 园区入驻3.3 add by sw
ALTER TABLE `eh_lease_configs` ADD COLUMN `display_name_str` VARCHAR(128);
ALTER TABLE `eh_lease_configs` ADD COLUMN `display_order_str` VARCHAR(128);


ALTER TABLE `eh_buildings` ADD COLUMN `general_form_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of eh_general_form';
ALTER TABLE `eh_buildings` ADD COLUMN `custom_form_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: not add custom field, 1: add custom field';
ALTER TABLE `eh_buildings` ADD COLUMN `default_order` BIGINT NOT NULL;
ALTER TABLE `eh_buildings` ADD COLUMN `manager_name` VARCHAR(128);


ALTER TABLE `eh_lease_promotions` ADD COLUMN `longitude` DOUBLE DEFAULT NULL;
ALTER TABLE `eh_lease_promotions` ADD COLUMN `latitude` DOUBLE DEFAULT NULL;
ALTER TABLE `eh_lease_promotions` ADD COLUMN `address` VARCHAR(512);

ALTER TABLE `eh_lease_promotions` ADD COLUMN `general_form_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of eh_general_form';
ALTER TABLE `eh_lease_promotions` ADD COLUMN `custom_form_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: not add custom field, 1: add custom field';
ALTER TABLE `eh_lease_promotions` ADD COLUMN `default_order` BIGINT NOT NULL DEFAULT 0;

CREATE TABLE `eh_lease_form_requests` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`namespace_id` INT NOT NULL DEFAULT '0',
	`owner_id` BIGINT NOT NULL,
	`owner_type` VARCHAR (64) NOT NULL,

    `source_id` BIGINT NOT NULL,
	`source_type` VARCHAR (64) NOT NULL,

	`create_time` datetime DEFAULT NULL COMMENT 'record create time',

	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4;

CREATE TABLE `eh_lease_configs2` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) COMMENT 'owner type, e.g EhCommunities',
  `owner_id` BIGINT COMMENT 'owner id, e.g eh_communities id',  
  `config_name` VARCHAR(128),
  `config_value` VARCHAR(128),  

  `create_time` DATETIME,
  `creator_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_general_form_vals` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`namespace_id` INT NOT NULL DEFAULT '0',
	`organization_id` BIGINT NOT NULL DEFAULT '0',
	`owner_id` BIGINT NOT NULL,
	`owner_type` VARCHAR (64) NOT NULL,
	`module_id` BIGINT DEFAULT NULL COMMENT 'the module id',
	`module_type` VARCHAR (64) DEFAULT NULL,

    `source_id` BIGINT NOT NULL,
	`source_type` VARCHAR (64) NOT NULL,

	`form_origin_id` BIGINT DEFAULT NULL,
	`form_version` BIGINT DEFAULT NULL,
	`field_name` VARCHAR (128) DEFAULT NULL,
	`field_type` VARCHAR (128) DEFAULT NULL,
	`field_value` text,
	`create_time` datetime DEFAULT NULL COMMENT 'record create time',

    `string_tag1` VARCHAR (128) DEFAULT NULL,
	`string_tag2` VARCHAR (128) DEFAULT NULL,
	`string_tag3` VARCHAR (128) DEFAULT NULL,
	`string_tag4` VARCHAR (128) DEFAULT NULL,
	`string_tag5` VARCHAR (128) DEFAULT NULL,
	`integral_tag1` BIGINT (20) DEFAULT '0',
	`integral_tag2` BIGINT (20) DEFAULT '0',
	`integral_tag3` BIGINT (20) DEFAULT '0',
	`integral_tag4` BIGINT (20) DEFAULT '0',
	`integral_tag5` BIGINT (20) DEFAULT '0',
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4;

-- 人才表，add by tt, 20170511
-- DROP TABLE IF EXISTS `eh_talents`;
CREATE TABLE `eh_talents` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `name` VARCHAR(64),
  `avatar_uri` VARCHAR(2048),
  `phone` VARCHAR(32),
  `gender` TINYINT,
  `position` VARCHAR(64),
  `category_id` BIGINT,
  `experience` INTEGER,
  `graduate_school` VARCHAR(64),
  `degree` TINYINT,
  `remark` TEXT,
  `enabled` TINYINT,
  `default_order` BIGINT,
  `status` TINYINT NOT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 人才分类表，add by tt, 20170511
-- DROP TABLE IF EXISTS `eh_talent_categories`;
CREATE TABLE `eh_talent_categories` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `name` VARCHAR(64),
  `status` TINYINT NOT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 查询历史记录表，add by tt, 20170511
-- DROP TABLE IF EXISTS `eh_talent_query_histories`;
CREATE TABLE `eh_talent_query_histories` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `keyword` VARCHAR(64),
  `status` TINYINT NOT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 添加是否设置了管理员标记，add by tt, 20170522
ALTER TABLE `eh_organizations` ADD COLUMN `set_admin_flag` TINYINT DEFAULT 0;
-- 增加索引，add by tt, 20170522
ALTER TABLE `eh_organization_community_requests` ADD INDEX `member_id` (`member_id`);
ALTER TABLE `eh_organization_community_requests` ADD INDEX `community_id` (`community_id`);

-- 服务联盟 增加排序和是否显示在app端的字段 by dengs, 20170523
ALTER TABLE `eh_service_alliances` ADD COLUMN `display_flag` TINYINT NOT NULL DEFAULT '1' COMMENT '0:hide,1:display';
ALTER TABLE `eh_service_alliances` CHANGE COLUMN `default_order` `default_order` BIGINT COMMENT 'default value is id';

-- 给flowCase增加申请人在当前场景下的公司id字段   add by xq.tian  2017/06/08
ALTER TABLE `eh_flow_cases` ADD COLUMN `applier_organization_id` BIGINT COMMENT 'applier current organization_id';

  