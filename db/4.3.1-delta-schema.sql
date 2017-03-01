-- 物业报修2.8
ALTER TABLE eh_pm_tasks ADD COLUMN `building_name` VARCHAR(128);
ALTER TABLE eh_pm_tasks ADD COLUMN `organization_uid` BIGINT;

ALTER TABLE eh_pm_task_targets ADD COLUMN `create_time` DATETIME;
ALTER TABLE eh_pm_task_targets ADD COLUMN `creator_uid` BIGINT;