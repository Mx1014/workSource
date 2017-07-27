ALTER TABLE `eh_punch_rules` ADD COLUMN `rule_type` TINYINT DEFAULT '0' COMMENT '0- 排班制 ; 1- 固定班次';
ALTER TABLE `eh_punch_rules` ADD COLUMN `punch_organization_id` BIGINT;
ALTER TABLE `eh_punch_rules` ADD COLUMN `china_holiday_flag` TINYINT COMMENT '同步法定节假日0- no  ; 1- yes ';

ALTER TABLE `eh_punch_holidays` ADD COLUMN `exchange_from_date` DATE DEFAULT NULL COMMENT '特殊上班日:上原本哪天的班次';
ALTER TABLE `eh_punch_day_logs` ADD COLUMN status_list VARCHAR(20) COMMENT '多次打卡的状态用/分隔 example: 1 ; 1/13 ; 13/3/4 ';

ALTER TABLE `eh_punch_time_rules` ADD COLUMN `rule_type` TINYINT DEFAULT '0' COMMENT '0- 排班制 ; 1- 固定班次';
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `open_weekday` VARCHAR(7) DEFAULT NULL COMMENT '7位二进制，0000000每一位表示星期7123456';

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
  `flex_time_long` BIGINT  COMMENT 'how late can i arrive ',
  `work_time_long` BIGINT  COMMENT 'how long do i must be work',
  `status` TINYINT  COMMENT 'its holiday or workday:0-workday ; 1-holiday',
  `description` TEXT ,
  `creator_uid` BIGINT ,
  `create_time` DATETIME ,
  `operator_uid` BIGINT ,
  `operate_time` DATETIME ,
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

