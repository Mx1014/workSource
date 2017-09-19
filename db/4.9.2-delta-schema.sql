-- 增加域空间左上角显示场景名称的配置项
ALTER TABLE eh_namespace_details ADD COLUMN name_type TINYINT(4) DEFAULT 0;

-- 增加考勤统计字段
ALTER TABLE eh_punch_statistics ADD COLUMN exception_day_count INT  DEFAULT NULL COMMENT '异常天数',