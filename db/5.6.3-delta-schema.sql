-- 人事档案 2.7 (基线已经执行过) start by ryan
ALTER TABLE `eh_organization_member_details` DROP COLUMN `profile_integrity`;
ALTER TABLE `eh_organization_member_details` DROP COLUMN `department`;
ALTER TABLE `eh_organization_member_details` DROP COLUMN `department_ids`;
ALTER TABLE `eh_organization_member_details` DROP COLUMN `job_position`;
ALTER TABLE `eh_organization_member_details` DROP COLUMN `job_position_ids`;
ALTER TABLE `eh_organization_member_details` DROP COLUMN `job_level`;
ALTER TABLE `eh_organization_member_details` DROP COLUMN `job_level_ids`;

ALTER TABLE `eh_organization_member_details` ADD COLUMN `check_in_time_index` VARCHAR(64) NOT NULL DEFAULT '0000' COMMENT'only month&day like 0304' AFTER `check_in_time`;
ALTER TABLE `eh_organization_member_details` ADD COLUMN `birthday_index` VARCHAR(64) COMMENT'only month like 0304' AFTER `birthday`;

ALTER TABLE `eh_archives_notifications` DROP COLUMN `notify_emails`;
ALTER TABLE `eh_archives_notifications` CHANGE COLUMN `notify_hour` `notify_time` INTEGER COMMENT 'the hour of sending notifications';
ALTER TABLE `eh_archives_notifications` ADD COLUMN `mail_flag` TINYINT DEFAULT 0 NOT NULL COMMENT 'email sending, 0-no 1-yes' AFTER `notify_time`;
ALTER TABLE `eh_archives_notifications` ADD COLUMN `message_flag` TINYINT DEFAULT 0 NOT NULL COMMENT 'message sending, 0-no 1-yes' AFTER `mail_flag`;
ALTER TABLE `eh_archives_notifications` ADD COLUMN `notify_target` TEXT COMMENT 'the target email address' AFTER `message_flag`;

ALTER TABLE `eh_organization_member_details` MODIFY `check_in_time` DATE COMMENT '入职日期';
ALTER TABLE `eh_organization_member_details` MODIFY `check_in_time_index` VARCHAR(64) COMMENT '入职日期索引字段';

-- DROP TABLE IF EXISTS `eh_archives_operational_configurations`;
CREATE TABLE `eh_archives_operational_configurations` (
	`id` BIGINT NOT NULL,
	`namespace_id` INT NOT NULL DEFAULT '0',
	`organization_id` BIGINT NOT NULL DEFAULT '0',
  `detail_id` BIGINT NOT NULL COMMENT 'the detail id that belongs to the employee which is the change target',
  `operation_type` TINYINT NOT NULL COMMENT 'the type of operation',
  `operation_date` DATE COMMENT 'the date of executing the operation',
  `additional_info` TEXT COMMENT 'the addition information for the operation',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-cancel, 1-pending, 2-complete',
  `create_time` DATETIME DEFAULT NULL COMMENT 'create time',
  `operator_uid` BIGINT DEFAULT NULL COMMENT 'the id of the operator',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_archives_operational_logs`;
CREATE TABLE `eh_archives_operational_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the log',
  `namespace_id` INT NOT NULL DEFAULT '0',
  `organization_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'the id of the organization',
  `detail_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'the detail id that belongs to the employee',
  `operation_type` TINYINT NOT NULL COMMENT 'the type of the operate',
  `operation_time` DATE NOT NULL COMMENT 'the time of the operate',
  `string_tag1` VARCHAR(2048) COMMENT 'redundant information for the operate',
  `string_tag2` VARCHAR(2048) COMMENT 'redundant information for the operate',
  `string_tag3` VARCHAR(2048) COMMENT 'redundant information for the operate',
  `string_tag4` VARCHAR(2048) COMMENT 'redundant information for the operate',
  `string_tag5` VARCHAR(2048) COMMENT 'redundant information for the operate',
  `string_tag6` VARCHAR(2048) COMMENT 'redundant information for the operate',
  `operator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'the id of the operator',
  `operator_name` VARCHAR(64) NOT NULL DEFAULT '0' COMMENT 'the id of the operator',
  `create_time` DATETIME DEFAULT NULL COMMENT 'create time',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- end


-- 下载中心 搬迁代码  by yanjun start
-- 注意：core已经上线过了，此处是搬迁代码过来的。以后合并分支的时候要注意

-- 任务中心添加执行开始时间和上传开始时间  add by yanjun 201805241345
ALTER TABLE `eh_tasks` ADD COLUMN `execute_start_time`  datetime NULL;
ALTER TABLE `eh_tasks` ADD COLUMN `upload_file_start_time`  datetime NULL;
ALTER TABLE `eh_tasks` ADD COLUMN `upload_file_finish_time`  datetime NULL;

-- 下载中心 搬迁代码  by yanjun end

-- 修复 workplace 的问题 janson TODO 这里需要弄新的分支
ALTER TABLE `eh_communityandbuilding_relationes` ADD COLUMN `workplace_id` BIGINT(20) NOT NULL DEFAULT 0 AFTER `update_time`;
-- by janson end

