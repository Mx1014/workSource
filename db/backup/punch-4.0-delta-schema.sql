
-- Designer: wuhan
-- Description: 考勤4.0 
-- Created: 2018-4-12


CREATE TABLE `eh_punch_vacation_balances` (
  `id` BIGINT NOT NULL,
  `owner_id` BIGINT COMMENT 'organization_id',
  `owner_type` VARCHAR(32) DEFAULT '' COMMENT 'organization', 
  `user_id` BIGINT COMMENT 'user_id',
  `detail_id` BIGINT COMMENT 'user_id',
  `annual_leave_balance` DOUBLE COMMENT '年假余额',
  `overtime_compensation_balance` DOUBLE COMMENT '调休余额',
  `creator_uid` BIGINT DEFAULT '0',
  `create_time` DATETIME ,
  `operator_uid` BIGINT ,
  `update_time` DATETIME ,
  `namespace_id` INT(11) ,
  PRIMARY KEY (`id`),
  KEY `ix_detail_id`(`detail_id`) 
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '假期余额表';


CREATE TABLE `eh_punch_vacation_balance_logs` (
  `id` BIGINT NOT NULL,
  `owner_id` BIGINT COMMENT 'organization_id',
  `owner_type` VARCHAR(32) DEFAULT '' COMMENT 'organization', 
  `user_id` BIGINT COMMENT 'user_id',
  `detail_id` BIGINT COMMENT 'user_id',
  `annual_leave_balance_correction` DOUBLE COMMENT'年假余额修改',
  `overtime_compensation_balance_correction` DOUBLE COMMENT '调休余额修改',
  `annual_leave_balance` DOUBLE COMMENT '修改后年假余额',
  `overtime_compensation_balance` DOUBLE COMMENT '修改后调休余额',
  `description` TEXT COMMENT '备注',
  `creator_uid` BIGINT DEFAULT '0',
  `create_time` DATETIME ,
  `operator_uid` BIGINT ,
  `update_time` DATETIME ,
  `namespace_id` INT(11) ,
  PRIMARY KEY (`id`),
  KEY `ix_detail_id`(`detail_id`) 
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '假期余额操作日志表';

ALTER TABLE eh_punch_statistics ADD COLUMN `annual_leave_balance` DOUBLE COMMENT '年假余额';
ALTER TABLE eh_punch_statistics ADD COLUMN `overtime_compensation_balance` DOUBLE COMMENT '调休余额';
ALTER TABLE eh_punch_statistics ADD COLUMN `device_change_counts` INT COMMENT '设备异常次数';
ALTER TABLE eh_punch_statistics ADD COLUMN `exception_request_counts` INT COMMENT '异常申报次数';
ALTER TABLE eh_punch_statistics ADD COLUMN `belate_time` BIGINT COMMENT '迟到时长(毫秒数)';
ALTER TABLE eh_punch_statistics ADD COLUMN `leave_early_time` BIGINT COMMENT '早退时长(毫秒数)';
ALTER TABLE eh_punch_statistics ADD COLUMN `forgot_count` INT COMMENT '下班缺卡次数';
ALTER TABLE eh_punch_statistics ADD COLUMN `status_list` VARCHAR(1024) COMMENT '校正后状态列表(月初到月末)';
ALTER TABLE eh_punch_logs ADD COLUMN `wifi_info` VARCHAR(1024) COMMENT '打卡用到的WiFi信息';
ALTER TABLE eh_punch_logs ADD COLUMN `location_info` VARCHAR(1024) COMMENT '打卡用到的地址定位';

-- end 考勤4.0

-- Designer: zhiwei zhang
-- Description: ISSUE#25301 【打卡考勤】按日导出打卡记录导出文件时间过长
-- Created: 2018-4-4


ALTER TABLE eh_punch_exception_requests ADD INDEX i_eh_enterprise_user_punch_date(`enterprise_id`,`user_id`,`punch_date`);
ALTER TABLE eh_punch_day_logs ADD INDEX i_eh_enterprise_user_punch_date(`enterprise_id`,`user_id`,`punch_date`);

-- End by: zhiwei zhang
