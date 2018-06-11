ALTER TABLE `eh_energy_meter_reading_logs`
  MODIFY COLUMN `reading`  decimal(10,2) NULL DEFAULT NULL AFTER `meter_id`;
ALTER TABLE `eh_energy_meter_tasks`
  MODIFY COLUMN `last_task_reading`  decimal(10,2) NULL DEFAULT NULL AFTER `executive_expire_time`;
ALTER TABLE `eh_energy_meter_tasks`
  MODIFY COLUMN `reading`  decimal(10,2) NULL DEFAULT NULL AFTER `last_task_reading`;
