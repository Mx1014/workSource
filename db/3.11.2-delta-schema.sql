
-- added by wh  2016-10-19  申请表添加时长和生效日期  

ALTER TABLE `eh_approval_requests` ADD COLUMN `effective_date` DATE COMMENT 'when the request approval effective';
ALTER TABLE `eh_approval_requests` ADD COLUMN `hour_length` DOUBLE COMMENT 'how long (hours) does the request effective'; 

-- added by wh 2016-11-09 打卡计算结果还要对异常的设备进行计算

ALTER TABLE `eh_punch_day_logs` ADD COLUMN `device_change_flag` TINYINT DEFAULT 0 COMMENT '0- unchange 1-changed' ;
 