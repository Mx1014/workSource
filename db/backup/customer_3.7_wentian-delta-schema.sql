-- 通用脚本
-- ADD BY shiheng.ma
-- issue-26467 物业报修 增加企业Id字段
ALTER TABLE `eh_pm_tasks` ADD COLUMN `enterprise_id`  bigint(20) NOT NULL DEFAULT 0 COMMENT '需求人公司Id';
-- END