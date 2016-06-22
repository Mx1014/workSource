ALTER TABLE `eh_quality_inspection_task_attachments` CHANGE `task_id` `record_id` bigint(20) NOT NULL DEFAULT '0'COMMENT 'refernece to the id of eh_quality_inspection_task_records';

