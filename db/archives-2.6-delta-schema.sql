-- 人事2.6新需求 by ryan 03/21/2018
ALTER TABLE `eh_organization_member_details` DROP COLUMN `profile_integrity`;
ALTER TABLE `eh_organization_member_details` DROP COLUMN `department`;
ALTER TABLE `eh_organization_member_details` DROP COLUMN `department_ids`;
ALTER TABLE `eh_organization_member_details` DROP COLUMN `job_position`;
ALTER TABLE `eh_organization_member_details` DROP COLUMN `job_position_ids`;
ALTER TABLE `eh_organization_member_details` DROP COLUMN `job_level`;
ALTER TABLE `eh_organization_member_details` DROP COLUMN `job_level_ids`;

ALTER TABLE `eh_organization_member_details` ADD COLUMN `check_in_time_index` VARCHAR(64) NOT NULL DEFAULT '0000' COMMENT'only month like 0304' AFTER `check_in_time`;
ALTER TABLE `eh_organization_member_details` ADD COLUMN `birthday_index` VARCHAR(64) COMMENT'only month like 0304' AFTER `birthday`;
UPDATE eh_organization_member_details AS t SET check_in_time_index = (CONCAT(SUBSTRING(t.check_in_time,6,2),SUBSTRING(t.check_in_time,9,2)));
UPDATE eh_organization_member_details AS t SET birthday_index = (CONCAT(SUBSTRING(t.birthday_index,6,2),SUBSTRING(t.birthday_index,9,2)));

SET @template_id = (SELECT MAX(id) from eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'archives.notification', '5', 'zh_CN', '人事提醒开头', '你好，${contactName}\r\n\r\n\r\n${companyName}近一周需要注意的人事日程如下：\r\n\r\n', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'archives.notification', '6', 'zh_CN', '人事转正提醒', '转正：${contactNames}\r\n', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'archives.notification', '7', 'zh_CN', '人事合同到期提醒', '合同到期：${contactNames}\r\n', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'archives.notification', '8', 'zh_CN', '人事身份证到期提醒', '身份证到期：${contactNames}\r\n', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'archives.notification', '9', 'zh_CN', '人事周年提醒', '${contactName} 在${companyName}工作满 ${count} 年\r\n', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'archives.notification', '10', 'zh_CN', '人事生日提醒', '${contactName} ${count} 岁生日\r\n', '0');

ALTER TABLE `eh_archives_notifications` DROP COLUMN `notify_emails`;
ALTER TABLE `eh_archives_notifications` CHANGE COLUMN `notify_hour` `notify_time` INTEGER COMMENT 'the hour of sending notifications';
ALTER TABLE `eh_archives_notifications` ADD COLUMN `mail_flag` TINYINT DEFAULT 0 NOT NULL COMMENT 'email sending, 0-no 1-yes' AFTER `notify_time`;
ALTER TABLE `eh_archives_notifications` ADD COLUMN `message_flag` TINYINT DEFAULT 0 NOT NULL COMMENT 'message sending, 0-no 1-yes' AFTER `mail_flag`;
ALTER TABLE `eh_archives_notifications` ADD COLUMN `notify_target` TEXT COMMENT 'the target email address' AFTER `message_flag`;


