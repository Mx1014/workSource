
--DROP TABLE IF EXISTS `eh_organization_members_test`;
CREATE TABLE `eh_organization_members_test` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `organization_id` BIGINT NOT NULL,
  `target_type` VARCHAR(32) COMMENT 'untrack, user',
  `target_id` BIGINT NOT NULL COMMENT 'target user id if target_type is a user',
  `member_group` VARCHAR(32) COMMENT 'pm group the member belongs to',
  `contact_name` VARCHAR(64),
  `contact_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: mobile, 1: email',
  `contact_token` VARCHAR(128) COMMENT 'phone number or email address',
  `contact_description` TEXT,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: confirming, 2: active',
  `group_id` BIGINT DEFAULT 0 COMMENT 'refer to the organization id',
  `employee_no` VARCHAR(128),
  `avatar` VARCHAR(128),
  `group_path` VARCHAR(128) COMMENT 'refer to the organization path',
  `gender` TINYINT DEFAULT 0 COMMENT '0: undisclosured, 1: male, 2: female',
  `update_time` DATETIME,
  `create_time` DATETIME,
  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `namespace_id` INTEGER DEFAULT 0,
  `visible_flag` TINYINT DEFAULT 0 COMMENT '0 show 1 hide',
  `group_type` VARCHAR(64) COMMENT 'ENTERPRISE, DEPARTMENT, GROUP, JOB_POSITION, JOB_LEVEL, MANAGER',
  `creator_uid` BIGINT,
  `operator_uid` BIGINT,
  `detail_id` BIGINT COMMENT 'id for detail records',
  PRIMARY KEY (`id`),
  KEY `fk_eh_orgm_owner` (`organization_id`),
  KEY `i_eh_corg_group` (`member_group`),
  KEY `i_target_id` (`target_id`),
  KEY `i_contact_token` (`contact_token`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
