-- merge from pmtaskzj xiongying 20170810
ALTER TABLE eh_pm_tasks ADD COLUMN `remark_source` VARCHAR(32);
ALTER TABLE eh_pm_tasks ADD COLUMN `remark` VARCHAR(1024);

-- 添加字段默认值  add by xq.tian 2017/08/10
ALTER TABLE `eh_group_member_logs` MODIFY COLUMN `community_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_group_member_logs` MODIFY COLUMN `address_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_group_member_logs` MODIFY COLUMN `group_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_group_member_logs` MODIFY COLUMN `update_time` DATETIME;
ALTER TABLE `eh_group_member_logs` MODIFY COLUMN `member_type` VARCHAR(32) NOT NULL DEFAULT '';