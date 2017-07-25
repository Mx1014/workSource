-- 停车充值 add by sw 20170703
ALTER TABLE eh_parking_recharge_orders ADD COLUMN `parking_time` INT DEFAULT NULL COMMENT 'parking-time';
ALTER TABLE eh_parking_recharge_orders ADD COLUMN `error_description` TEXT DEFAULT NULL COMMENT 'error description';
ALTER TABLE eh_parking_recharge_orders ADD COLUMN `error_description_json` TEXT DEFAULT NULL COMMENT 'error description';

ALTER TABLE eh_parking_recharge_orders ADD COLUMN `refund_time` datetime DEFAULT NULL COMMENT 'refund time';
ALTER TABLE eh_parking_recharge_orders ADD COLUMN `delay_time` INT DEFAULT NULL COMMENT 'delay time';


ALTER TABLE eh_parking_lots ADD COLUMN `contact` VARCHAR(128) DEFAULT NULL COMMENT 'service contact';


ALTER TABLE eh_parking_recharge_orders CHANGE old_expired_time start_period datetime;
ALTER TABLE eh_parking_recharge_orders CHANGE new_expired_time end_period datetime;


-- merge from forum-2.0 by yanjun 20170703
-- 投票增加标签字段  add by yanjun 20170613
ALTER TABLE `eh_polls` ADD COLUMN `tag` VARCHAR(32) NULL;
-- merge from forum-2.0 by yanjun 20170703

-- merge from orgByLv started by lei.lv 20170703
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

-- ----------------------------
-- Table structure for eh_user_organization
-- ----------------------------
-- DROP TABLE IF EXISTS `eh_user_organizations`;
CREATE TABLE `eh_user_organizations` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `user_id` bigint(20) NOT NULL,
  `organization_id` bigint(20) DEFAULT 0,
  `group_path` varchar(128) COMMENT 'refer to the organization path',
  `group_type` varchar(64) COMMENT 'ENTERPRISE, DEPARTMENT, GROUP, JOB_POSITION, JOB_LEVEL, MANAGER',
  `status` tinyint(4) COMMENT '0: inactive, 1: confirming, 2: active',
  `namespace_id` int(11) DEFAULT '0',
  `create_time` datetime,
  `visible_flag` tinyint(4) DEFAULT '0' COMMENT '0 show 1 hide',
  `update_time` datetime,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --备份eh_organization_members表为eh_organization_members_temp
-- -- DROP TABLE IF EXISTS `eh_organization_members_temp`;
-- CREATE TABLE `eh_organization_members_temp` select * from `eh_organization_members`;

-- merge from orgByLv end by lei.lv 20170703
-- 资源预约价格规则表，add by tt, 20170613
-- DROP TABLE IF EXISTS `eh_rentalv2_price_rules`;
CREATE TABLE `eh_rentalv2_price_rules` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'default, resource, cell',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'default_rule_id, resource_id, cell_id',
  `rental_type` TINYINT COMMENT '0: as hour:min 1-as half day 2-as day 3-支持晚上的半天 4按月',
  `workday_price` DECIMAL(10,2) COMMENT '工作日价格',
  `weekend_price` DECIMAL(10,2) COMMENT '周末价格',
  `org_member_workday_price` DECIMAL(10,2) COMMENT '企业内部工作日价格',
  `org_member_weekend_price` DECIMAL(10,2) COMMENT '企业内部节假日价格',
  `approving_user_workday_price` DECIMAL(10,2) COMMENT '外部客户工作日价格',
  `approving_user_weekend_price` DECIMAL(10,2) COMMENT '外部客户节假日价格',
  `discount_type` TINYINT COMMENT '折扣信息：0不打折 1满减优惠 2满天减 3比例折扣',
  `full_price` DECIMAL(10,2) COMMENT '满XX',
  `cut_price` DECIMAL(10,2) COMMENT '减XX元',
  `discount_ratio` DOUBLE COMMENT '折扣比例',
  `cell_begin_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'cells begin id',
  `cell_end_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'cells end id',
  `creator_uid` BIGINT,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 非认证用户是否可见，add by tt, 20170623
ALTER TABLE `eh_rentalv2_resource_types` ADD COLUMN `unauth_visible` TINYINT DEFAULT '0';

-- 添加索引 add by sfyan 20170703
ALTER TABLE `eh_user_activities` ADD INDEX user_activitie_user_id ( `uid` );
ALTER TABLE `eh_user_organizations` ADD INDEX user_organization_user_id (`user_id`);
ALTER TABLE `eh_user_organizations` ADD INDEX user_organization_organization_id (`organization_id`);
ALTER TABLE `eh_organization_details` ADD INDEX organization_detail_orgnaization_id (`organization_id`);
ALTER TABLE `eh_organization_addresses` ADD INDEX organization_address_orgnaization_id (`organization_id`);



