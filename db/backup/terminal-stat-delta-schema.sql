-- 终端app版本累计用户
DROP TABLE IF EXISTS `eh_terminal_app_version_cumulatives`;
CREATE TABLE `eh_terminal_app_version_cumulatives` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `app_version_realm` varchar(128) DEFAULT NULL,
  `app_version` varchar(128) DEFAULT NULL,
  `imei_number` varchar(128) DEFAULT '',
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 终端app版本活跃用户
DROP TABLE IF EXISTS `eh_terminal_app_version_actives`;
CREATE TABLE `eh_terminal_app_version_actives` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `app_version_realm` varchar(128) DEFAULT NULL,
  `app_version` varchar(128) DEFAULT NULL,
  `imei_number` varchar(128) DEFAULT '',
  `date` VARCHAR(32) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 终端日统计
DROP TABLE IF EXISTS `eh_terminal_day_statistics`;
CREATE TABLE `eh_terminal_day_statistics` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `new_user_number` BIGINT NOT NULL COMMENT 'number of new users',
  `active_user_number` BIGINT NOT NULL COMMENT 'number of active users',
  `start_change_rate` DECIMAL(10,2) NOT NULL COMMENT 'today compared to yesterdays rate of change.',
  `new_change_rate` DECIMAL(10,2) NOT NULL COMMENT 'today compared to yesterdays rate of change.',
  `active_change_rate` DECIMAL(10,2) NOT NULL COMMENT 'today compared to yesterdays rate of change.',
  `cumulative_change_rate` DECIMAL(10,2) NOT NULL COMMENT 'today compared to yesterdays rate of change.',
  `active_rate` DECIMAL(10,2) NOT NUll COMMENT 'active_user_number/accumulative_user_number',
  `start_number` BIGINT NOT NULL COMMENT 'number of starts',
  `cumulative_user_number` BIGINT NOT NULL COMMENT 'cumulative number of users',
  `seven_active_user_number` BIGINT NOT NULL COMMENT '7 active number of users',
  `thirty_active_user_number` BIGINT NOT NULL COMMENT '30 active number of users',
  `date` VARCHAR(32) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 终端时统计
DROP TABLE IF EXISTS `eh_terminal_hour_statistics`;
CREATE TABLE `eh_terminal_hour_statistics` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `new_user_number` BIGINT NOT NULL COMMENT 'number of new users',
  `active_user_number` BIGINT NOT NULL COMMENT 'number of active users',
  `change_rate` DECIMAL(10,2) NOT NULL COMMENT 'today compared to yesterdays rate of change.',
  `active_rate` DECIMAL(10,2) NOT NUll COMMENT 'active_user_number/accumulative_user_number',
  `start_number` BIGINT NOT NULL COMMENT 'number of starts',
  `cumulative_user_number` BIGINT NOT NULL COMMENT 'cumulative number of users',
  `date` VARCHAR(32) DEFAULT NULL,
  `hour` VARCHAR(16) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 终端app版本统计
DROP TABLE IF EXISTS `eh_terminal_app_version_statistics`;
CREATE TABLE `eh_terminal_app_version_statistics` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `app_version_realm` varchar(128) DEFAULT NULL,
  `app_version` varchar(128) DEFAULT NULL,
  `new_user_number` BIGINT NOT NULL COMMENT 'number of new users',
  `start_number` BIGINT NOT NULL COMMENT 'number of starts',
  `active_user_number` BIGINT NOT NULL COMMENT 'number of active users',
  `cumulative_user_number` BIGINT NOT NULL COMMENT 'cumulative of active users',
  `version_cumulative_rate` DECIMAL(10,2) NOT NUll,
  `version_active_rate` DECIMAL(10,2) NOT NUll,
  `date` VARCHAR(32) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;



DROP TABLE IF EXISTS `eh_app_version`;
CREATE TABLE `eh_terminal_statistics_tasks` (
  `id` BIGINT NOT NULL,
  `task_no` VARCHAR(20) NOT NULL,
  `status` TINYINT DEFAULT NULL COMMENT '10 生成日统计数据 20 生成时统计数据 30 生成版本统计数据 100 完成',
  `update_Time` DATETIME DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `task_no` (`task_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_app_version`;
CREATE TABLE `eh_app_version` (
  `id` BIGINT NOT NULL,
  `type` VARCHAR(20) NOT NULL,
  `name` VARCHAR(64) NOT NULL,
  `realm` VARCHAR(64) NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `default_order` INTEGER NOT NULL DEFAULT '0',
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


ALTER TABLE `eh_user_activities` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_user_activities` ADD COLUMN `version_realm` varchar(128) DEFAULT NULL;

ALTER TABLE `eh_web_menus` ADD COLUMN `module_id` BIGINT DEFAULT NULL;