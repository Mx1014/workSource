-- 物业报修2.8 add by sw 20170306
ALTER TABLE eh_pm_tasks ADD COLUMN `building_name` VARCHAR(128);
ALTER TABLE eh_pm_tasks ADD COLUMN `organization_uid` BIGINT;

ALTER TABLE eh_pm_task_targets ADD COLUMN `create_time` DATETIME;
ALTER TABLE eh_pm_task_targets ADD COLUMN `creator_uid` BIGINT;

-- 修改dataType 长度 add by sw 20170306
ALTER TABLE eh_web_menus MODIFY COLUMN data_type VARCHAR (256);
