
-- 运营统计的数据修改 add sfyan 20170112
UPDATE `eh_terminal_day_statistics` SET `start_change_rate` = `start_change_rate` * -1, `new_change_rate` = `new_change_rate` * -1, `active_change_rate` = `active_change_rate` * -1, `cumulative_change_rate` = `cumulative_change_rate` * -1;