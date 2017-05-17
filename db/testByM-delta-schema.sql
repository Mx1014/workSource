SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for members' details
-- ----------------------------
-- DROP TABLE IF EXISTS `eh_organization_member_details`;
CREATE TABLE `eh_organization_member_details` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `member_id` BIGINT NOT NULL COMMENT 'id for members, reference for eh_organization_member id' ,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `birthday` DATE COMMENT 'members'' birthday',
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
  `employee_type` VARCHAR(64) COMMENT 'full-time, part-time, internship, labor dispatch',
  `employee_status` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'on the job, probation, leave the job',
  `employment_time` DATE NOT NULL COMMENT '转正日期',
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
  `member_id` BIGINT NOT NULL COMMENT 'id for members, reference for eh_organization_member id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `schoole_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the member''s school name',
  `degree` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'doctor, master, bachelor, etc',
  `major` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the major of the member',
  `enrollment_time` DATE NOT NULL COMMENT 'commencement time',
  `graduation_time` DATE NOT NULL COMMENT 'when the member graduated form the school',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `status` TINYINT COMMENT '0: inactive, 1: active',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for members' working experiences
-- ----------------------------
-- DROP TABLE IF EXISTS `eh_organization_member_working`;
CREATE TABLE `eh_organization_member_work_experiences` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `member_id` BIGINT NOT NULL COMMENT 'id for members, reference for eh_organization_member id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `enterprise_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the name of company',
  `position` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the position of the member',
  `job_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'full-time, part-time',
  `entry_time` DATE NOT NULL COMMENT 'timing of start the job',
  `departure_time` DATE NOT NULL COMMENT 'timing of quit the job',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `status` TINYINT COMMENT '0: inactive, 1: active',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for the situation of members' insurance situation
-- ----------------------------
-- DROP TABLE IF EXISTS `eh_organization_member_insurance`;
CREATE TABLE `eh_organization_member_insurances` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `member_id` BIGINT NOT NULL COMMENT 'id for members, reference for eh_organization_member id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the name of the insurance',
  `enterprise` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the company name of the insurance',
  `number` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the number of the insurance',
  `start_time` DATE NOT NULL COMMENT '生效时间',
  `end_time` DATE NOT NULL COMMENT '到期时间',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `status` TINYINT COMMENT '0: inactive, 1: active',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for the situation of members' contract
-- ----------------------------
-- DROP TABLE IF EXISTS `eh_organization_member_contract`;
CREATE TABLE `eh_organization_member_contract` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `member_id` BIGINT NOT NULL COMMENT 'id for members, reference for eh_organization_member id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `contract_number` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the number of the contract',
  `start_time` DATE NOT NULL COMMENT '生效时间',
  `end_time` DATE NOT NULL COMMENT '到期时间',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `status` TINYINT COMMENT '0: inactive, 1: active',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for the members' operation records
-- ----------------------------
-- DROP TABLE IF EXISTS `eh_organization_member_profile_logs`;
CREATE TABLE `eh_organization_member_profile_logs` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `operation_type` VARCHAR(32),
  `operator_uid` BIGINT,
  `resource_type` VARCHAR(32) COMMENT 'operation related resource type',
  `resource_id` BIGINT COMMENT 'operation related resource id',
  `result_code` INTEGER COMMENT '0: common positive result, otherwise, application defined result code',
  `reason` VARCHAR(256),
  `original_content` LONGTEXT COMMENT 'use json format',
  `audit_content` LONGTEXT,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
