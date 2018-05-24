-- 能耗更新离线包版本   by jiarui
update eh_version_urls set download_url = replace(download_url,'1-0-1','1-0-3'),
  info_url = replace(info_url,'1-0-1','1-0-3'),
  target_version = '1.0.3' where realm_id = (select id from eh_version_realm where realm = 'energyManagement');

-- 删除能耗无用数据   by jiarui
DELETE FROM  eh_energy_meter_tasks WHERE  plan_id in (SELECT  id from eh_energy_plans where name ='autoPlans');

DELETE  FROM  eh_energy_plans WHERE  name = 'autoPlans';

ALTER TABLE `eh_energy_meter_reading_logs`
  MODIFY COLUMN `reading`  decimal(10,2) NULL DEFAULT NULL AFTER `meter_id`;
ALTER TABLE `eh_energy_meter_tasks`
  MODIFY COLUMN `last_task_reading`  decimal(10,2) NULL DEFAULT NULL AFTER `executive_expire_time`;
ALTER TABLE `eh_energy_meter_tasks`
  MODIFY COLUMN `reading`  decimal(10,2) NULL DEFAULT NULL AFTER `last_task_reading`;
