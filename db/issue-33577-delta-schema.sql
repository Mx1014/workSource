-- 通用脚本
-- ADD BY wh
-- ISSUE-33577 增加update_time 给punch_logs表(为金蝶对接接口提供)
ALTER TABLE eh_punch_logs ADD COLUMN `update_time` DATETIME ;


-- ISSUE-33577 END
 