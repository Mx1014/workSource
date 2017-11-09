ALTER TABLE `eh_punch_exception_requests` ADD COLUMN `punch_type` TINYINT DEFAULT 2 COMMENT ' 0- 上班打卡 ; 1- 下班打卡';  
ALTER TABLE `eh_punch_exception_requests` ADD COLUMN `begin_time` DATETIME COMMENT ' 请假/加班 生效开始时间';  
ALTER TABLE `eh_punch_exception_requests` ADD COLUMN `end_time` DATETIME COMMENT ' 请假/加班 生效结束时间';  
ALTER TABLE `eh_punch_exception_requests` ADD COLUMN `duration` DOUBLE COMMENT ' 请假/加班 时长-可供计算';  
ALTER TABLE `eh_punch_exception_requests` ADD COLUMN `approval_attribute` VARCHAR(128) COMMENT 'DEFAULT,CUSTOMIZE';

ALTER TABLE `eh_punch_exception_approvals` ADD COLUMN `punch_type` TINYINT DEFAULT 2 COMMENT ' 0- 上班打卡 ; 1- 下班打卡';  

ALTER TABLE `eh_punch_logs` ADD COLUMN `approval_status` TINYINT DEFAULT NULL COMMENT '校正后的打卡状态 0-正常 null-没有异常校准';
ALTER TABLE `eh_punch_logs` ADD COLUMN `smart_alignment` TINYINT DEFAULT 0 COMMENT '只能校准状态 0-非校准 1-校准';
ALTER TABLE `eh_punch_day_logs` ADD COLUMN `smart_alignment` VARCHAR(128) DEFAULT NULL COMMENT '智能校准状态:1-未智能校准 0-未校准 例如:0;1/0;1/1/0/1';
