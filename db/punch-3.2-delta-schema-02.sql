-- 增加考勤统计字段
ALTER TABLE eh_punch_statistics ADD COLUMN exception_day_count INT  DEFAULT NULL COMMENT '异常天数';
-- 薪酬组加上版本 By lei.lv 
ALTER TABLE eh_uniongroup_configures ADD COLUMN `version_code` INT DEFAULT 0 COMMENT '版本号';
ALTER TABLE eh_uniongroup_member_details ADD COLUMN `version_code` INT DEFAULT 0 COMMENT '版本号';