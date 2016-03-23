ALTER TABLE `eh_quality_inspection_tasks` ADD COLUMN `inspection_text` TEXT;
ALTER TABLE `eh_quality_inspection_tasks` DELETE COLUMN `inspection_text` TEXT;

ALTER TABLE `eh_quality_inspection_task_attachments` ADD COLUMN `task_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: verify task, 2: rectify task';
