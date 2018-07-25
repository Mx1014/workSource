ALTER TABLE `eh_work_report_val_receiver_map` ADD COLUMN `organizationId` BIGINT DEFAULT 0 NOT NULL COMMENT 'the orgId for the user' AFTER `namespace_id`;
