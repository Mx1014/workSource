ALTER TABLE `eh_punch_exception_requests` ADD COLUMN `punch_type` TINYINT DEFAULT 2 COMMENT ' 0- 上班打卡 ; 1- 下班打卡';  
ALTER TABLE `eh_punch_exception_approvals` ADD COLUMN `punch_type` TINYINT DEFAULT 2 COMMENT ' 0- 上班打卡 ; 1- 下班打卡';  
