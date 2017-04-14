 ALTER TABLE `eh_punch_time_rules` ADD COLUMN `description` VARCHAR(128)  COMMENT 'rule description';
 ALTER TABLE `eh_punch_time_rules` ADD COLUMN `target_type` VARCHAR(128)  COMMENT 'target resource(user/organization) type';
 ALTER TABLE `eh_punch_time_rules` ADD COLUMN `target_id` BIGINT  COMMENT 'target resource(user/organization) id';
 
 --  打卡排班表
 CREATE TABLE `eh_punch_schedulings` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(128)  COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT  COMMENT 'owner resource(user/organization) id',
  `target_type` VARCHAR(128)  COMMENT 'target resource(user/organization) type',
  `target_id` BIGINT  COMMENT 'target resource(user/organization) id', 
  `rule_date` DATE  COMMENT 'date',
  `punch_rule_id` BIGINT COMMENT 'eh_punch_rules id  ',
  `time_rule_id` BIGINT COMMENT 'eh_punch_time_rules id --null:not work day',
  `creator_uid` BIGINT ,
  `create_time` DATETIME ,
  `operator_uid` BIGINT ,
  `operate_time` DATETIME , 
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4
;
-- 增加字段
ALTER TABLE `eh_punch_statistics` ADD COLUMN `exts` VARCHAR(1024) COMMENT 'json string exts:eq[{"name":"事假","timeCount":"1天2小时"},{"name":"丧假","timeCount":"3天2小时30分钟"}]';
ALTER TABLE `eh_punch_statistics` ADD COLUMN `user_status` TINYINT DEFAULT 0 COMMENT '0:normal普通 1:NONENTRY未入职 2:RESIGNED已离职';

ALTER TABLE `eh_punch_time_rules` ADD COLUMN `start_early_time_long` BIGINT  COMMENT 'how early can i arrive';
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `start_late_time_long` BIGINT  COMMENT 'how late can i arrive ';
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `work_time_long` BIGINT  COMMENT 'how long do i must be work';
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `noon_leave_time_long` BIGINT ;
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `afternoon_arrive_time_long` BIGINT ;
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `day_split_time_long` BIGINT DEFAULT 18000000 COMMENT 'the time a day begin'  ;

-- 请假申请增加统计表,用以每月的考勤统计
-- 这个表在申请被审批的时候修改.
CREATE TABLE `eh_approval_range_statistics` (
  `id` BIGINT NOT NULL,
  `punch_month` VARCHAR(8) DEFAULT NULL COMMENT 'yyyymm',
  `owner_type` VARCHAR(128) DEFAULT NULL COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT DEFAULT NULL COMMENT 'owner resource(user/organization) id',
  `user_id` BIGINT DEFAULT NULL COMMENT 'user id',   
  `approval_type` TINYINT NOT NULL COMMENT '1. ask for leave, 2. forget to punch',
  `category_id` BIGINT DEFAULT NULL COMMENT 'concrete category id',
  `actual_result` VARCHAR(128) DEFAULT NULL COMMENT 'actual result, e.g 1day3hours 1.3.0',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;