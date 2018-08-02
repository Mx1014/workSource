-- 通用脚本
-- ADD BY xq.tian
-- ISSUE-32697 运营统计重构
ALTER TABLE eh_terminal_hour_statistics ADD COLUMN cumulative_active_user_number BIGINT;

ALTER TABLE eh_terminal_day_statistics ADD COLUMN average_active_user_number BIGINT;
ALTER TABLE eh_terminal_day_statistics ADD COLUMN average_active_user_change_rate DECIMAL(10, 2);

-- ISSUE-32697 END