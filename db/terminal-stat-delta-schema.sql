-- 终端日统计
DROP TABLE IF EXISTS `eh_terminal_day_statistics`;
CREATE TABLE `eh_terminal_day_statistics` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `new_user_number` BIGINT NOT NULL COMMENT 'number of new users',
  `active_user_number` BIGINT NOT NULL COMMENT 'number of active users',
  `change_rate` DECIMAL NOT NULL COMMENT 'today compared to yesterdays rate of change.',
  `active_rate` DECIMAL NOT NUll COMMENT 'active_user_number/accumulative_user_number',
  `start_number` BIGINT NOT NULL COMMENT 'number of starts',
  `cumulative_user_number` BIGINT NOT NULL COMMENT 'cumulative number of users',
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
  `change_rate` DECIMAL NOT NULL COMMENT 'today compared to yesterdays rate of change.',
  `active_rate` DECIMAL NOT NUll COMMENT 'active_user_number/accumulative_user_number',
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
  `version_active_rate` DECIMAL NOT NUll,
  `stat_start_date` VARCHAR(32) DEFAULT NULL COMMENT 'stat start date',
  `stat_end_date` VARCHAR(32) DEFAULT NULL  COMMENT 'stat end date',
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_terminal_statistics_tasks` (
  `id` BIGINT NOT NULL,
  `task_no` VARCHAR(20) NOT NULL,
  `status` TINYINT DEFAULT NULL COMMENT '10 生成日统计数据 20 生成时统计数据 30 生成版本统计数据 100 完成',
  `update_Time` DATETIME DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `task_no` (`task_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER TABLE `eh_user_activities` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_user_activities` ADD COLUMN `version_realm` varchar(128) DEFAULT NULL;