
-- merge from activity1.6-delta-schema.sql by lqs 20161214
ALTER TABLE eh_activities ADD COLUMN `achievement` TEXT;
ALTER TABLE eh_activities ADD COLUMN `achievement_type` VARCHAR(32) COMMENT 'richtext, link';
ALTER TABLE eh_activities ADD COLUMN `achievement_richtext_url` VARCHAR(512) COMMENT 'richtext page';

-- 活动附件表
-- DROP TABLE IF EXISTS  `eh_activity_attachments`;
CREATE TABLE `eh_activity_attachments` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`activity_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner id, e.g application_id',
    `name` VARCHAR(128),
    `file_size` INTEGER NOT NULL DEFAULT 0,
	`content_type` VARCHAR(32) COMMENT 'attachment object content type',
	`content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
    `download_count` INTEGER NOT NULL DEFAULT 0,
	`creator_uid` BIGINT NOT NULL DEFAULT 0,
	`create_time` DATETIME NOT NULL, 
	
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 活动物资管理表
-- DROP TABLE IF EXISTS  `eh_activity_goods`;
CREATE TABLE `eh_activity_goods` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`activity_id` BIGINT NOT NULL COMMENT 'owner id, e.g application_id',
	`name` VARCHAR(64) COMMENT 'attachment object content type',
	`price` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
    `quantity` INTEGER NOT NULL DEFAULT 0,
    `total_price` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
	`handlers` VARCHAR(64),
	`create_time` DATETIME NOT NULL, 
    `creator_uid` BIGINT,
	
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- merge from terminal-stat-delta-schema.sql by by sfyan 20161214
-- 运营统计 by sfyan 20161214
-- 终端app版本累计用户
DROP TABLE IF EXISTS `eh_terminal_app_version_cumulatives`;
CREATE TABLE `eh_terminal_app_version_cumulatives` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `app_version_realm` VARCHAR(128) DEFAULT NULL,
  `app_version` VARCHAR(128) DEFAULT NULL,
  `imei_number` VARCHAR(128) DEFAULT '',
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 终端app版本活跃用户
DROP TABLE IF EXISTS `eh_terminal_app_version_actives`;
CREATE TABLE `eh_terminal_app_version_actives` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `app_version_realm` VARCHAR(128) DEFAULT NULL,
  `app_version` VARCHAR(128) DEFAULT NULL,
  `imei_number` VARCHAR(128) DEFAULT '',
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
  `active_rate` DECIMAL(10,2) NOT NULL COMMENT 'active_user_number/accumulative_user_number',
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
  `active_rate` DECIMAL(10,2) NOT NULL COMMENT 'active_user_number/accumulative_user_number',
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
  `app_version_realm` VARCHAR(128) DEFAULT NULL,
  `app_version` VARCHAR(128) DEFAULT NULL,
  `new_user_number` BIGINT NOT NULL COMMENT 'number of new users',
  `start_number` BIGINT NOT NULL COMMENT 'number of starts',
  `active_user_number` BIGINT NOT NULL COMMENT 'number of active users',
  `cumulative_user_number` BIGINT NOT NULL COMMENT 'cumulative of active users',
  `version_cumulative_rate` DECIMAL(10,2) NOT NULL,
  `version_active_rate` DECIMAL(10,2) NOT NULL,
  `date` VARCHAR(32) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 统计任务记录
DROP TABLE IF EXISTS `eh_app_version`;
CREATE TABLE `eh_terminal_statistics_tasks` (
  `id` BIGINT NOT NULL,
  `task_no` VARCHAR(20) NOT NULL,
  `status` TINYINT DEFAULT NULL COMMENT '10 生成日统计数据 20 生成时统计数据 30 生成版本统计数据 100 完成',
  `update_Time` DATETIME DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `task_no` (`task_no`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- app版本
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

-- 活动数据增加域空间字段
ALTER TABLE `eh_user_activities` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_user_activities` ADD COLUMN `version_realm` VARCHAR(128) DEFAULT NULL;

-- 菜单增加模块id字段
ALTER TABLE `eh_web_menus` ADD COLUMN `module_id` BIGINT DEFAULT NULL;



-- 
-- 服务热线配置表
-- 
-- DROP TABLE IF EXISTS `eh_service_configurations`;

CREATE TABLE `eh_service_configurations` (
  `id` BIGINT AUTO_INCREMENT COMMENT 'id of the record',
  `owner_type` VARCHAR(64) COMMENT 'community;group,organaization,exhibition,',
  `owner_id` BIGINT DEFAULT '0',
  `name` VARCHAR(64),
  `value` VARCHAR(64),
  `description` VARCHAR(256) ,
  `namespace_id` INT DEFAULT '0',
  `display_name` VARCHAR(128) ,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_config` (`owner_type`,`owner_id`,`name`,`value`,`namespace_id`)
) ENGINE=INNODB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8mb4 ;



-- 
-- 服务热线表
-- 
-- DROP TABLE IF EXISTS `eh_service_hotlines`;
CREATE TABLE `eh_service_hotlines` (
  `id` BIGINT COMMENT 'id of the record',
  `namespace_id` INTEGER DEFAULT '0',
  `owner_type` VARCHAR(64) COMMENT 'community;group,organaization,exhibition,',
  `owner_id` BIGINT(20) DEFAULT '0',
  `service_type` INT COMMENT'1-公共热线 2-专属客服 4- 8-', 
  `name` VARCHAR(64) COMMENT '热线/客服名称', 
  `contact` VARCHAR(64) COMMENT '热线/客服 联系电话',
  `user_id` BIGINT COMMENT '客服 userid', 
  `description` VARCHAR(400) COMMENT '客服 描述',
  `default_order` INTEGER DEFAULT '0' COMMENT '排序字段',
  `create_time` DATETIME DEFAULT NULL,
  `creator_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

