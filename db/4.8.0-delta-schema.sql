-- merge from pmtaskzj xiongying 20170810
ALTER TABLE eh_pm_tasks ADD COLUMN `remark_source` VARCHAR(32);
ALTER TABLE eh_pm_tasks ADD COLUMN `remark` VARCHAR(1024);