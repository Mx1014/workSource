-- 
-- 考勤时间管理
-- 
-- DROP TABLE IF EXISTS eh_punch_time_rules;
CREATE TABLE `eh_punch_time_rules` (
  `id` BIGINT COMMENT 'id',
  `name` VARCHAR(128) COMMENT 'time rule name ',
  `start_early_time` TIME DEFAULT NULL COMMENT 'how early can i arrive',
  `start_late_time` TIME DEFAULT NULL COMMENT 'how late can i arrive ',
  `work_time` TIME DEFAULT NULL COMMENT 'how long do i must be work',
  `noon_leave_time` TIME DEFAULT NULL,
  `afternoon_arrive_time` TIME DEFAULT NULL,
  `punch_times_per_day` TINYINT NOT NULL DEFAULT '2' COMMENT '2 or  4 times',
  `creator_uid` BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `operator_uid` BIGINT DEFAULT NULL,
  `operate_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 
-- 具体打卡地点
-- 
-- DROP TABLE IF EXISTS eh_punch_geopoints;
CREATE TABLE `eh_punch_geopoints` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `location_rule_id` BIGINT DEFAULT NULL COMMENT 'fk:eh_punch_geopoints id', 
  `description` VARCHAR(256) DEFAULT NULL,
  `longitude` DOUBLE DEFAULT NULL,
  `latitude` DOUBLE DEFAULT NULL,
  `geohash` VARCHAR(32) DEFAULT NULL,
  `distance` DOUBLE DEFAULT NULL,
  `creator_uid` BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_eh_punch_location_rules` (`location_rule_id`),
  CONSTRAINT `fk_eh_punch_geopoints_ibfk_1` FOREIGN KEY (`location_rule_id`) REFERENCES `eh_punch_location_rules` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 
-- 考勤地点表
-- 
-- DROP TABLE IF EXISTS eh_punch_location_rules;
CREATE TABLE `eh_punch_location_rules` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `name` VARCHAR(128) COMMENT 'location rule name ',
  `description` VARCHAR(256) DEFAULT NULL,
  `creator_uid` BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;



-- 
-- 具体wifi列表
-- 
-- DROP TABLE IF EXISTS eh_punch_wifis;
CREATE TABLE `eh_punch_wifis` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `wifi_rule_id` BIGINT DEFAULT NULL COMMENT 'fk:eh_punch_wifi_rules id', 
  `ssid` VARCHAR(64) DEFAULT NULL COMMENT 'wifi ssid', 
  `mac_address` VARCHAR(32) DEFAULT NULL COMMENT 'wifi mac address', 
  `creator_uid` BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_eh_punch_wifi_rules` (`wifi_rule_id`),
  CONSTRAINT `fk_eh_punch_wifis_ibfk_1` FOREIGN KEY (`wifi_rule_id`) REFERENCES `eh_punch_wifi_rules` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 
-- 考勤wifi表
-- 
-- DROP TABLE IF EXISTS eh_punch_location_rules;
CREATE TABLE `eh_punch_wifi_rules` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `name` VARCHAR(128) COMMENT 'wifi rule name ',
  `description` VARCHAR(256) DEFAULT NULL,
  `creator_uid` BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;



-- 
-- 假期表
-- 
-- DROP TABLE IF EXISTS eh_punch_holidays;
CREATE TABLE `eh_punch_holidays` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `scheduling_rule_id` BIGINT DEFAULT NULL COMMENT 'fk:eh_punch_scheduling_rules id', 
  `status` TINYINT DEFAULT NULL COMMENT '0:workday ;  1:holiday',
  `rule_date` DATE DEFAULT NULL COMMENT 'date',
  `creator_uid` BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_eh_punch_scheduling_rules` (`scheduling_rule_id`),
  CONSTRAINT `fk_eh_punch_holidays_ibfk_1` FOREIGN KEY (`scheduling_rule_id`) REFERENCES `eh_punch_scheduling_rules` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4



-- 
-- 打卡排班表
-- 
-- DROP TABLE IF EXISTS eh_punch_scheduling_rules;
CREATE TABLE `eh_punch_scheduling_rules` (
  `id` BIGINT NOT NULL COMMENT 'id', 
  `name` VARCHAR(128) COMMENT 'wifi rule name ',
  `description` VARCHAR(256) DEFAULT NULL,
  `work_week_dates` VARCHAR(8) DEFAULT '0000000' COMMENT '7位，从左至右每一位表示星期7123456,值:0-关闭 1-开放 example:周12345上班[0111110]',
  `creator_uid` BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL, 
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4

-- 给打卡规则表增加外键
-- 考勤时间
ALTER TABLE `eh_punch_rules` ADD COLUMN  time_rule_id  BIGINT DEFAULT NULL COMMENT 'fk:eh_punch_time_rules id';
ALTER TABLE `eh_punch_rules` ADD KEY `fk_eh_punch_time_rules` (`time_rule_id`);
ALTER TABLE `eh_punch_rules` ADD CONSTRAINT `fk_eh_punch_rules_ibfk_1` FOREIGN KEY (`time_rule_id`) REFERENCES `eh_punch_time_rules` (`id`);

-- 考勤地点
ALTER TABLE `eh_punch_rules` ADD COLUMN  `location_rule_id` BIGINT DEFAULT NULL COMMENT 'fk:eh_punch_geopoints id';
ALTER TABLE `eh_punch_rules` ADD KEY `fk_eh_punch_location_rules` (`location_rule_id`);
ALTER TABLE `eh_punch_rules` ADD CONSTRAINT `fk_eh_punch_rules_ibfk_2` FOREIGN KEY (`location_rule_id`) REFERENCES `eh_punch_location_rules` (`id`);

-- 考勤WIFI
ALTER TABLE `eh_punch_rules` ADD COLUMN  `wifi_rule_id` BIGINT DEFAULT NULL COMMENT 'fk:eh_punch_wifi_rules id';
ALTER TABLE `eh_punch_rules` ADD KEY `fk_eh_punch_wifi_rules` (`wifi_rule_id`);
ALTER TABLE `eh_punch_rules` ADD CONSTRAINT `fk_eh_punch_rules_ibfk_3` FOREIGN KEY (`wifi_rule_id`) REFERENCES `eh_punch_wifi_rules` (`id`);

-- 考勤排班
ALTER TABLE `eh_punch_rules` ADD COLUMN  `scheduling_rule_id` BIGINT DEFAULT NULL COMMENT 'fk:eh_punch_scheduling_rules id';
ALTER TABLE `eh_punch_rules` ADD KEY `fk_eh_punch_scheduling_rules` (`scheduling_rule_id`);
ALTER TABLE `eh_punch_rules` ADD CONSTRAINT `fk_eh_punch_rules_ibfk_4` FOREIGN KEY (`scheduling_rule_id`) REFERENCES `eh_punch_scheduling_rules` (`id`);



-- 
-- 打卡排班表
-- 
-- DROP TABLE IF EXISTS eh_punch_scheduling_rules;
CREATE TABLE `eh_punch_rule_owner_map` (
  `id` BIGINT NOT NULL COMMENT 'id', 
  `owner_type` VARCHAR(128) COMMENT 'owner table : EhUsers ; EhGroups',
  `owner_id` BIGINT DEFAULT NULL COMMENT 'owner tables id',
  `punch_rule_id` BIGINT DEFAULT NULL COMMENT 'fk:eh_punch_rules id',
  `approval_rule_id` BIGINT DEFAULT NULL COMMENT 'fk:approval rule id',
  `description` VARCHAR(256) DEFAULT NULL,
  `creator_uid` BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL, 
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4