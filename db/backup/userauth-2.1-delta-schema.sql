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