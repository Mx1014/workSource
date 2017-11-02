ALTER TABLE `eh_punch_exception_requests` ADD COLUMN `punch_type` TINYINT DEFAULT 2 COMMENT ' 0- 上班打卡 ; 1- 下班打卡';  
ALTER TABLE `eh_punch_exception_requests` ADD COLUMN `begin_time` DATETIME COMMENT ' 请假/加班 生效开始时间';  
ALTER TABLE `eh_punch_exception_requests` ADD COLUMN `end_time` DATETIME COMMENT ' 请假/加班 生效结束时间';  
ALTER TABLE `eh_punch_exception_requests` ADD COLUMN `approval_attribute` VARCHAR(128) COMMENT 'DEFAULT,CUSTOMIZE';

ALTER TABLE `eh_punch_exception_approvals` ADD COLUMN `punch_type` TINYINT DEFAULT 2 COMMENT ' 0- 上班打卡 ; 1- 下班打卡';  
