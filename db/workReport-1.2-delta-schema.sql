ALTER TABLE `eh_work_report_val_receiver_map` ADD COLUMN `organization_id` BIGINT DEFAULT 0 NOT NULL COMMENT 'the orgId for the user' AFTER `namespace_id`;

ALTER TABLE `eh_work_reports` ADD COLUMN `validity_settings` VARCHAR(512) COMMENT 'the expiry date of the work report' AFTER `form_version`;
ALTER TABLE `eh_work_reports` ADD COLUMN `receiver_msg_type` TINYINT COMMENT 'the type of the receiver message settings' AFTER `validity_settings`;
ALTER TABLE `eh_work_reports` ADD COLUMN `receiver_msg_seetings` VARCHAR(512) COMMENT 'the time range of the receiver message' AFTER `receiver_msg_type`;
ALTER TABLE `eh_work_reports` ADD COLUMN `author_msg_type` TINYINT COMMENT 'the type of the author message settings' AFTER `validity_settings`;
ALTER TABLE `eh_work_reports` ADD COLUMN `author_msg_seetings` VARCHAR(512) COMMENT 'the time range of the author message' AFTER `receiver_msg_type`;

