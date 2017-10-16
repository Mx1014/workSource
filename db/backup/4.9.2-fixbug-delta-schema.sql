-- 增加考勤统计字段
ALTER TABLE eh_punch_statistics ADD COLUMN exception_day_count INT  DEFAULT NULL COMMENT '异常天数';
