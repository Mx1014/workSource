SET FOREIGN_KEY_CHECKS=0;

-- DROP TABLE IF EXISTS `eh_organization_member_details`;
CREATE TABLE `eh_organization_member_details` (
  `id` BIGINT NOT NULL COMMENT 'id for members',
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
  `emergency_tel` VARCHAR(128) COMMENT 'emergency contact tel-number',
  `address` VARCHAR(255) COMMENT 'address for the member',
  `employee_type` VARCHAR(64) COMMENT 'full-time, part-time, internship, labor dispatch',
  `employee_status` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'on the job, probation, leave the job',
  `positive_time` DATE NOT NULL COMMENT 'time to be regular employee',
  `expiry_time` DATE COMMENT 'quit time',
  `salary_number` VARCHAR(128) COMMENT '工资卡号',
  `social_secu_number` VARCHAR(128) COMMENT '社保号',
  `provident_fund_number` VARCHAR(128) COMMENT '公积金号',
  `profile_flag` INTEGER DEFAULT 0 COMMENT '1-100',
  `entry_time` DATE NOT NULL COMMENT 'timing of join the company',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_organization_member_education`;
CREATE TABLE `eh_organization_member_education` (
  `id` BIGINT NOT NULL COMMENT 'id for members',
  `schoole_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the member''s school name',
  `degree` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'doctor, master, bachelor, etc',
  `major` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the major of the member',
  `enrollment_time` DATE NOT NULL COMMENT 'commencement time',
  `graduation_time` DATE NOT NULL COMMENT 'when the member graduated form the school',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_organization_member_working`;
CREATE TABLE `eh_organization_member_working` (
  `id` BIGINT NOT NULL COMMENT 'id for members',
  `company_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the name of factory',
  `position` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the position of the member',
  `job_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'full-time, part-time',
  `entry_time` DATE NOT NULL COMMENT 'timing of start the job',
  `departure_time` DATE NOT NULL COMMENT 'timing of quit the job',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_organization_member_insurance`;
CREATE TABLE `eh_organization_member_insurance` (
  `id` BIGINT NOT NULL COMMENT 'id for members',
  `insu_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the name of the insurance',
  `insu_company` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the company name of the insurance',
  `insu_number` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the number of the insurance',
  `effective_time` DATE NOT NULL COMMENT '生效时间',
  `expiry_time` DATE NOT NULL COMMENT '到期时间',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_organization_member_contract`;
CREATE TABLE `eh_organization_member_contract` (
  `id` BIGINT NOT NULL COMMENT 'id for members',
  `contract_number` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the number of the contract',
  `effective_time` DATE NOT NULL COMMENT '生效时间',
  `expiry_time` DATE NOT NULL COMMENT '到期时间',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
