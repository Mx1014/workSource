--
-- eh_group_member_logs
--
ALTER TABLE `eh_group_member_logs` ADD COLUMN `uuid` VARCHAR(128) NOT NULL DEFAULT '';
ALTER TABLE `eh_group_member_logs` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT '0';
ALTER TABLE `eh_group_member_logs` ADD COLUMN `community_id` BIGINT(20) NOT NULL;
ALTER TABLE `eh_group_member_logs` ADD COLUMN `address_id` BIGINT(20) NOT NULL;
ALTER TABLE `eh_group_member_logs` ADD COLUMN `group_id` BIGINT(20) NOT NULL;
ALTER TABLE `eh_group_member_logs` ADD COLUMN `member_type` VARCHAR(32) NOT NULL COMMENT 'member object type; for example; type could be User; Group; etc';
ALTER TABLE `eh_group_member_logs` ADD COLUMN `member_id` BIGINT(20) NULL DEFAULT NULL;
ALTER TABLE `eh_group_member_logs` ADD COLUMN `member_role` BIGINT(20) NOT NULL DEFAULT '7' COMMENT 'Default to ResourceUser role';
ALTER TABLE `eh_group_member_logs` ADD COLUMN `member_avatar` VARCHAR(128) NULL DEFAULT NULL COMMENT 'avatar image identifier in storage sub-system';
ALTER TABLE `eh_group_member_logs` ADD COLUMN `member_nick_name` VARCHAR(128) NULL DEFAULT NULL COMMENT 'member nick name within the group';
ALTER TABLE `eh_group_member_logs` ADD COLUMN `operator_uid` BIGINT(20) NULL DEFAULT NULL COMMENT 'redundant auditing info';
ALTER TABLE `eh_group_member_logs` ADD COLUMN `process_code` TINYINT(4) NULL DEFAULT NULL;
ALTER TABLE `eh_group_member_logs` ADD COLUMN `process_details` TEXT NULL;
ALTER TABLE `eh_group_member_logs` ADD COLUMN `proof_resource_uri` VARCHAR(1024) NULL DEFAULT NULL;
ALTER TABLE `eh_group_member_logs` ADD COLUMN `approve_time` DATETIME NULL DEFAULT NULL COMMENT 'redundant auditing info';
ALTER TABLE `eh_group_member_logs` ADD COLUMN `requestor_comment` TEXT NULL;
ALTER TABLE `eh_group_member_logs` ADD COLUMN `operation_type` TINYINT(4) NULL DEFAULT NULL COMMENT '1: request to join; 2: invite to join';
ALTER TABLE `eh_group_member_logs` ADD COLUMN `inviter_uid` BIGINT(20) NULL DEFAULT NULL COMMENT 'record inviter user id';
ALTER TABLE `eh_group_member_logs` ADD COLUMN `invite_time` DATETIME NULL DEFAULT NULL COMMENT 'the time the member is invited';
ALTER TABLE `eh_group_member_logs` ADD COLUMN `update_time` DATETIME NOT NULL;
ALTER TABLE `eh_group_member_logs` ADD COLUMN `integral_tag1` BIGINT(20) NULL DEFAULT NULL;
ALTER TABLE `eh_group_member_logs` ADD COLUMN `integral_tag2` BIGINT(20) NULL DEFAULT NULL;
ALTER TABLE `eh_group_member_logs` ADD COLUMN `integral_tag3` BIGINT(20) NULL DEFAULT NULL;
ALTER TABLE `eh_group_member_logs` ADD COLUMN `integral_tag4` BIGINT(20) NULL DEFAULT NULL;
ALTER TABLE `eh_group_member_logs` ADD COLUMN `integral_tag5` BIGINT(20) NULL DEFAULT NULL;
ALTER TABLE `eh_group_member_logs` ADD COLUMN `string_tag1` VARCHAR(128) NULL DEFAULT NULL;
ALTER TABLE `eh_group_member_logs` ADD COLUMN `string_tag2` VARCHAR(128) NULL DEFAULT NULL;
ALTER TABLE `eh_group_member_logs` ADD COLUMN `string_tag3` VARCHAR(128) NULL DEFAULT NULL;
ALTER TABLE `eh_group_member_logs` ADD COLUMN `string_tag4` VARCHAR(128) NULL DEFAULT NULL;
ALTER TABLE `eh_group_member_logs` ADD COLUMN `string_tag5` VARCHAR(128) NULL DEFAULT NULL;

ALTER TABLE `eh_group_member_logs` CHANGE COLUMN `status` `member_status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive; 1: waitingForApproval; 2: waitingForAcceptance 3: active';
ALTER TABLE `eh_group_member_logs` DROP COLUMN `process_message`;

-- 给organization_member_log添加描述字段 add by xq.tian  2017/07/20
ALTER TABLE `eh_organization_member_logs` ADD COLUMN `contact_description` TEXT;

-- 企业人才消息推送者， add by tt, 20170705
-- DROP TABLE IF EXISTS `eh_talent_message_senders`;
CREATE TABLE `eh_talent_message_senders` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `organization_member_id` BIGINT NOT NULL,
  `user_id` BIGINT,
  `status` TINYINT NOT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 企业人才申请记录表， add by tt, 20170705
-- DROP TABLE IF EXISTS `eh_talent_requests`;
CREATE TABLE `eh_talent_requests` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `requestor` VARCHAR(64),
  `phone` VARCHAR(64),
  `organization_name` VARCHAR(128),
  `content` TEXT,
  `talent_id` BIGINT,
  `form_origin_id` BIGINT,
  `flow_case_id` BIGINT,
  `status` TINYINT NOT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




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
-- ALTER TABLE `eh_user_activities` ADD INDEX user_activitie_user_id ( `uid` );
ALTER TABLE `eh_user_organizations` ADD INDEX user_organization_user_id (`user_id`);
ALTER TABLE `eh_user_organizations` ADD INDEX user_organization_organization_id (`organization_id`);
ALTER TABLE `eh_organization_details` ADD INDEX organization_detail_orgnaization_id (`organization_id`);
ALTER TABLE `eh_organization_addresses` ADD INDEX organization_address_orgnaization_id (`organization_id`);


-- 短信黑名单  add by xq.tian  2017/07/04
-- DROP TABLE IF EXISTS `eh_sms_black_lists`;
CREATE TABLE `eh_sms_black_lists` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `contact_token` VARCHAR(32) NOT NULL COMMENT 'contact token',
  `reason` VARCHAR(128) DEFAULT NULL COMMENT 'reason',
  `status` TINYINT NOT NULL DEFAULT '1' COMMENT '0: pass, 1: block',
  `create_type` TINYINT NOT NULL DEFAULT '0' COMMENT '0: Created by system, 1: Manually created',
  `creator_uid` BIGINT DEFAULT NULL,
  `create_time` DATETIME(3) DEFAULT NULL,
  `update_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `u_eh_contact_token` (`contact_token`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;




--
-- 权限管理  merge wuyeprivilege  add by sfyan 20170705
--
ALTER TABLE `eh_web_menus` ADD `level` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_web_menus` ADD `condition_type` varchar(32) DEFAULT NULL;
ALTER TABLE `eh_web_menus` ADD `category` varchar(32) DEFAULT NULL;

ALTER TABLE `eh_service_module_assignments` ADD `all_module_flag` tinyint(4) COMMENT '0 not all, 1 all';
ALTER TABLE `eh_service_module_assignments` ADD `include_child_flag` tinyint(4) COMMENT '0 not include, 1 include';
ALTER TABLE `eh_service_module_assignments` ADD `relation_id` bigint(20) NOT NULL;

ALTER TABLE `eh_acl_roles` ADD COLUMN `creator_uid` BIGINT DEFAULT 0 COMMENT 'creator uid' ;
ALTER TABLE `eh_acl_roles` ADD COLUMN `create_time` DATETIME DEFAULT now() COMMENT 'record create time';

ALTER TABLE `eh_acl_roles` ADD INDEX `i_eh_acl_role_creator_uid`(`creator_uid`);
ALTER TABLE `eh_acl_roles` ADD INDEX `i_eh_acl_role_create_time`(`create_time`);


-- 授权表，包括模块管理员角色管理员授权
CREATE TABLE `eh_authorizations` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `target_type` varchar(32) NOT NULL COMMENT 'EhOrganizations, EhUsers',
  `target_id` bigint(20) NOT NULL,
  `target_name` varchar(128),
  `owner_type` varchar(32) NOT NULL COMMENT 'EhOrganizations, EhCommunities',
  `owner_id` bigint(20) NOT NULL,
  `auth_type` varchar(64) NOT NULL COMMENT 'EhServiceModules, EhRoles',
  `auth_id` bigint(20) NOT NULL,
  `identity_type` varchar(64) NOT NULL COMMENT 'manage, ordinary',
  `all_flag` tinyint(4) COMMENT '0 not all, 1 all',
  `scope` varchar(128) DEFAULT NULL,
  `creator_uid` bigint(20) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 用户授权关系
CREATE TABLE `eh_authorization_relations` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL COMMENT 'EhOrganizations, EhCommunities',
  `owner_id` bigint(20) NOT NULL,
  `module_id` bigint(20) NOT NULL,
  `target_json` text,
  `project_json` text,
  `privilege_json` text,
  `all_flag` tinyint(4) COMMENT '0 not all, 1 all',
  `all_project_flag` tinyint(4) COMMENT '0 not all, 1 all',
  `creator_uid` bigint(20) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 业务授权关系授权表
CREATE TABLE `eh_service_module_assignment_relations` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_type` varchar(32) NOT NULL COMMENT 'EhOrganizations, EhCommunities',
  `owner_id` bigint(20) NOT NULL,
  `all_module_flag` tinyint(4) COMMENT '0 not all, 1 all',
  `all_project_flag` tinyint(4) COMMENT '0 not all, 1 all',
  `target_json` text,
  `project_json` text,
  `module_json` text,
  `update_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) NOT NULL,
  `creator_uid` bigint(20) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 域名配置表
CREATE TABLE `eh_domains` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `portal_type` varchar(32) NOT NULL COMMENT 'zuolin, pm, enterprise, user',
  `portal_id` bigint(20) NOT NULL,
  `domain` varchar(32) NOT NULL COMMENT 'domain',
  `create_uid` bigint(20) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- identifier修改记录  add by xq.tian  2017/06/26
-- DROP TABLE IF EXISTS `eh_user_identifier_logs`;
CREATE TABLE `eh_user_identifier_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
  `identifier_type` TINYINT NOT NULL DEFAULT '0' COMMENT '0: mobile, 1: email',
  `identifier_token` VARCHAR(128),
  `verification_code` VARCHAR(16),
  `claim_status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: free standing, 1: claiming, 2: claim verifying, 3: claimed',
  `region_code` INTEGER NOT NULL DEFAULT '86' COMMENT 'region code 86 852',
  `notify_time` DATETIME(3),
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4;

-- 用户申诉记录  add by xq.tian  2017/06/26
-- DROP TABLE IF EXISTS `eh_user_appeal_logs`;
CREATE TABLE `eh_user_appeal_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
  `identifier_type` TINYINT NOT NULL DEFAULT '0' COMMENT '0: mobile, 1: email',
  `old_identifier` VARCHAR(128),
  `old_region_code` INTEGER DEFAULT '86' COMMENT 'region code 86 852',
  `new_identifier` VARCHAR(128),
  `new_region_code` INTEGER DEFAULT '86' COMMENT 'region code 86 852',
  `name` VARCHAR(128) COMMENT 'user name',
  `email` VARCHAR(128) COMMENT 'user email',
  `remarks` VARCHAR(512) COMMENT 'remarks',
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4;

-- 云之讯短信记录  add by xq.tian  2017/07/10
-- DROP TABLE IF EXISTS `eh_yzx_sms_logs`;
CREATE TABLE `eh_yzx_sms_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `scope` VARCHAR(64),
  `code` INTEGER,
  `locale` VARCHAR(16),
  `mobile` VARCHAR(128),
  `text` TEXT NULL,
  `variables` VARCHAR(512),
  `resp_code` VARCHAR(32),
  `failure` TINYINT,
  `create_date` VARCHAR(32),
  `sms_id` VARCHAR(128),
  `type` TINYINT COMMENT '1:状态报告，2：上行',
  `status` TINYINT COMMENT '0:成功；1：提交失败，4：失败，5：关键字（keys），6：黑/白名单，7：超频（overrate），8：unknown',
  `desc` TEXT,
  `report_time` DATETIME,
  `create_time` DATETIME,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `u_eh_contact_token` (`sms_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

