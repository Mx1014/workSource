ALTER TABLE `eh_punch_rules` ADD COLUMN `rule_type` TINYINT DEFAULT '0' COMMENT '0- 排班制 ; 1- 固定班次';
ALTER TABLE `eh_punch_rules` ADD COLUMN `punch_organization_id` BIGINT;
ALTER TABLE `eh_punch_rules` ADD COLUMN `china_holiday_flag` TINYINT COMMENT '同步法定节假日0- no  ; 1- yes ';

ALTER TABLE `eh_punch_holidays` ADD COLUMN `exchange_from_date` DATE DEFAULT NULL COMMENT '特殊上班日:上原本哪天的班次';
ALTER TABLE `eh_punch_day_logs` ADD COLUMN status_list VARCHAR(20) COMMENT '多次打卡的状态用/分隔 example: 1 ; 1/13 ; 13/3/4 ';
ALTER TABLE `eh_punch_day_logs` ADD COLUMN punch_count INT COMMENT '打卡次数';
ALTER TABLE `eh_punch_day_logs` ADD COLUMN `punch_organization_id` BIGINT;
ALTER TABLE `eh_punch_day_logs` ADD COLUMN `rule_type` TINYINT DEFAULT '0' COMMENT '0- 排班制 ; 1- 固定班次';
ALTER TABLE `eh_punch_day_logs` ADD COLUMN `time_rule_name` VARCHAR(64) COMMENT '排班规则名称';
ALTER TABLE `eh_punch_day_logs` ADD COLUMN `time_rule_id` BIGINT COMMENT '排班规则id';
ALTER TABLE `eh_punch_day_logs` ADD COLUMN `approval_status_list` VARCHAR(20) COMMENT 'null 没审批, 0-审批正常';

ALTER TABLE `eh_punch_logs` ADD COLUMN `punch_type` TINYINT DEFAULT '0' COMMENT '0- 上班打卡 ; 1- 下班打卡'; 
ALTER TABLE `eh_punch_logs` ADD COLUMN `punch_interval_no` INT DEFAULT '1' COMMENT '第几次排班的打卡'; 
ALTER TABLE `eh_punch_logs` ADD COLUMN `rule_time` BIGINT COMMENT '规则设置的该次打卡时间';  
ALTER TABLE `eh_punch_logs` ADD COLUMN `status` TINYINT COMMENT '打卡状态 0-正常 1-迟到 2-早退 3-缺勤 14-缺卡';  

ALTER TABLE `eh_punch_statistics` ADD COLUMN `punch_org_name` VARCHAR(64) COMMENT '所属规则-考勤组';
ALTER TABLE `eh_punch_statistics` ADD COLUMN `detail_id` BIGINT COMMENT '用户detailId';

ALTER TABLE `eh_punch_time_rules` ADD COLUMN `rule_type` TINYINT DEFAULT '1' COMMENT '0- 排班制 ; 1- 固定班次'; 
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `hommization_type` TINYINT DEFAULT '0' COMMENT '人性化设置:0-无 1-弹性 2晚到晚走'; 
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `flex_time_long` BIGINT COMMENT '弹性时间 ';
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `begin_punch_time` BIGINT COMMENT '上班多久之前可以打卡';
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `end_punch_time` BIGINT COMMENT '下班多久之后可以打卡';
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `punch_organization_id` BIGINT  COMMENT 'fk:eh_punch_workday_rules id';
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `punch_rule_id` BIGINT COMMENT 'eh_punch_rules id  ';
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `open_weekday` VARCHAR(7) COMMENT '7位二进制，0000000每一位表示星期7123456';
  

-- 固定时间制:特殊日期
CREATE TABLE `eh_punch_special_days` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(128)  COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT  COMMENT 'owner resource(user/organization) id',
  `punch_organization_id` BIGINT  COMMENT 'fk:eh_punch_workday_rules id',
  `punch_rule_id` BIGINT DEFAULT NULL COMMENT 'eh_punch_rules id  ',
  `time_rule_id` BIGINT DEFAULT NULL COMMENT 'eh_punch_time_rules id ', 
  `status` TINYINT  COMMENT 'its holiday or workday:0-workday ; 1-holiday',
  `rule_date` DATE  COMMENT 'date',
  `description` TEXT ,
  `creator_uid` BIGINT ,
  `create_time` DATETIME ,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 多时间段的打卡时段表
CREATE TABLE `eh_punch_time_intervals` (
  `id` BIGINT NOT NULL DEFAULT '0' COMMENT 'id',
  `owner_type` VARCHAR(128)  COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT  COMMENT 'owner resource(user/organization) id',
  `punch_times_per_day` TINYINT NOT NULL DEFAULT '2' COMMENT 'how many times should punch everyday :2/4/6', 
  `punch_organization_id` BIGINT  COMMENT 'fk:eh_punch_workday_rules id',
  `punch_rule_id` BIGINT DEFAULT NULL COMMENT 'eh_punch_rules id  ',
  `time_rule_id` BIGINT DEFAULT NULL COMMENT 'eh_punch_time_rules id  ', 
  `arrive_time_long` BIGINT  COMMENT ' arrive',
  `leave_time_long` BIGINT  COMMENT 'leave',
  `description` TEXT ,
  `creator_uid` BIGINT ,
  `create_time` DATETIME ,
  `operator_uid` BIGINT ,
  `operate_time` DATETIME ,
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

