-- 
-- 考勤时间管理
-- 
-- DROP TABLE IF EXISTS `eh_punch_time_rules`;
CREATE TABLE `eh_punch_time_rules` (
  `id` BIGINT COMMENT 'id',
  `owner_type` VARCHAR (128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `name` VARCHAR (128) COMMENT 'time rule name ',
  `start_early_time` TIME COMMENT 'how early can i arrive',
  `start_late_time` TIME COMMENT 'how late can i arrive ',
  `work_time` TIME COMMENT 'how long do i must be work',
  `noon_leave_time` TIME ,
  `afternoon_arrive_time` TIME ,
  `punch_times_per_day` TINYINT NOT NULL DEFAULT 2 COMMENT 'how many times should punch everyday :2/4',
  `creator_uid` BIGINT ,
  `create_time` DATETIME ,
  `operator_uid` BIGINT ,
  `operate_time` DATETIME ,
  
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 ;


-- 
-- 具体打卡地点
-- 

ALTER TABLE `eh_punch_geopoints` ADD COLUMN `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type';
ALTER TABLE `eh_punch_geopoints` ADD COLUMN `owner_id` BIGINT COMMENT 'owner resource(user/organization) id';
ALTER TABLE `eh_punch_geopoints` ADD COLUMN `location_rule_id` BIGINT COMMENT 'fk:eh_punch_geopoints id'; 


-- 
-- 考勤地点表
-- 
-- DROP TABLE IF EXISTS `eh_punch_location_rules`;
CREATE TABLE `eh_punch_location_rules` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `name` VARCHAR(128) COMMENT 'location rule name ',
  `description` VARCHAR(256) ,
  `creator_uid` BIGINT ,
  `create_time` DATETIME ,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 
-- 具体wifi列表
-- 
-- DROP TABLE IF EXISTS `eh_punch_wifis`;
CREATE TABLE `eh_punch_wifis` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `wifi_rule_id` BIGINT COMMENT 'fk:eh_punch_wifi_rules id', 
  `ssid` VARCHAR(64) COMMENT 'wifi ssid', 
  `mac_address` VARCHAR(32) COMMENT 'wifi mac address', 
  `creator_uid` BIGINT ,
  `create_time` DATETIME ,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 
-- 考勤wifi表
-- 
-- DROP TABLE IF EXISTS `eh_punch_wifi_rules`;
CREATE TABLE `eh_punch_wifi_rules` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR (128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `name` VARCHAR (128) COMMENT 'wifi rule name ',
  `description` VARCHAR (256),
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 ;


-- 
-- 假期表
-- 
-- DROP TABLE IF EXISTS `eh_punch_holidays`;
CREATE TABLE `eh_punch_holidays` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `workday_rule_id` BIGINT COMMENT 'fk:eh_punch_workday_rules id', 
  `status` TINYINT COMMENT 'its holiday or workday:0-workday ; 1-holiday',
  `rule_date` DATE COMMENT 'date',
  `creator_uid` BIGINT ,
  `create_time` DATETIME ,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 
-- 打卡排班表
-- 
-- DROP TABLE IF EXISTS `eh_punch_workday_rules`;
CREATE TABLE `eh_punch_workday_rules` (
  `id` BIGINT NOT NULL COMMENT 'id', 
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `name` VARCHAR(128) COMMENT 'wifi rule name ',
  `description` VARCHAR(256) ,
  `work_week_dates` VARCHAR(8) DEFAULT '0000000' COMMENT '7位，从左至右每一位表示星期7123456,值:0-关闭 1-开放 example:周12345上班[0111110]',
  `creator_uid` BIGINT ,
  `create_time` DATETIME , 
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 
-- 打卡总规则表
-- 
DROP TABLE IF EXISTS `eh_punch_rules`;
CREATE TABLE `eh_punch_rules` (
  `id` BIGINT NOT NULL COMMENT 'id', 
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `name` VARCHAR(128) COMMENT 'wifi rule name ',
  `time_rule_id` BIGINT COMMENT 'fk:eh_punch_time_rules id',
  `location_rule_id` BIGINT COMMENT 'fk:eh_punch_geopoints id',
  `wifi_rule_id` BIGINT COMMENT 'fk:eh_punch_wifi_rules id',
  `scheduling_rule_id` BIGINT COMMENT 'fk:eh_punch_scheduling_rules id',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT ,
  `operate_time` DATETIME ,
  
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 ;


-- 
-- 打卡规则和owner的映射表
-- 
-- DROP TABLE IF EXISTS eh_punch_scheduling_rules;
CREATE TABLE `eh_punch_rule_owner_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR (128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `target_type` VARCHAR (128) COMMENT 'target resource(user/organization) type',
  `target_id` BIGINT COMMENT 'target resource(user/organization) id',
  `punch_rule_id` BIGINT COMMENT 'fk:eh_punch_rules id',
  `review_rule_id` BIGINT COMMENT 'fk:review rule id',
  `description` VARCHAR (256),
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 ;