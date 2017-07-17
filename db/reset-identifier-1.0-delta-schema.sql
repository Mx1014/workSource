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