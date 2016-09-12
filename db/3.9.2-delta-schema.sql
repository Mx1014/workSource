-- merge from dev-banner-delta-schema.sql
--
-- banner表增加更新时间字段
--
ALTER TABLE `eh_banners` ADD COLUMN `update_time`  DATETIME;

-- 物业报修2.0新增操作人昵称，手机号字段
ALTER TABLE `eh_pm_task_logs` ADD COLUMN `operator_name` VARCHAR(64) COMMENT 'the name of user';
ALTER TABLE `eh_pm_task_logs` ADD COLUMN `operator_phone` VARCHAR(64) COMMENT 'the phone of user';