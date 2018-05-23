-- added by wh 
-- 增加应打卡时间给punch_log
ALTER TABLE `eh_punch_logs` ADD should_punch_time BIGINT COMMENT '应该打卡时间(用以计算早退迟到时长)';