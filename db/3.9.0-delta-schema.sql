-- merge from community-import-2.0.0-delta-schema.sql 20160826
-- 增加一列存储升级内容
ALTER TABLE `eh_version_urls` ADD COLUMN `upgrade_description` TEXT NULL DEFAULT NULL AFTER `info_url`;

-- 增加app名称和发布时间列
ALTER TABLE `eh_version_urls` ADD COLUMN `app_name` VARCHAR(50) NULL,ADD COLUMN `publish_time` DATETIME NULL;


-- merge from 2.0.0-punch-delta-schema.sql 20160826
ALTER TABLE `eh_punch_statistics` CHANGE `work_count` `work_count` DOUBLE DEFAULT NULL COMMENT '实际上班天数';


-- merge from repair-delta-schema.sql 20160826
-- DROP TABLE IF EXISTS `eh_pm_tasks`;
CREATE TABLE `eh_pm_tasks` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,

  `category_id` BIGINT NOT NULL DEFAULT 0,

  `address` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'detail address',
  `content` TEXT COMMENT 'content data',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive 1: wating, 2: allocated 3: completed 4: closed',
  `star` TINYINT COMMENT 'evaluate score',

  `unprocessed_time` DATETIME,
  `processing_time` DATETIME,
  `processed_time` DATETIME,
  `closed_time` DATETIME,

  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `delete_uid` BIGINT NOT NULL DEFAULT 0,
  `delete_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_pm_task_statistics`;
CREATE TABLE `eh_pm_task_statistics` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,

  `category_id` BIGINT NOT NULL DEFAULT 0,
  `total_count` INT NOT NULL DEFAULT 0,
  `unprocess_count` INT NOT NULL DEFAULT 0,
  `processing_count` INT NOT NULL DEFAULT 0,
  `processed_count` INT NOT NULL DEFAULT 0,
  `close_count` INT NOT NULL DEFAULT 0,

  `star1` INT NOT NULL DEFAULT 0,
  `star2` INT NOT NULL DEFAULT 0,
  `star3` INT NOT NULL DEFAULT 0,
  `star4` INT NOT NULL DEFAULT 0,
  `star5` INT NOT NULL DEFAULT 0,

  `date_str` DATETIME,

  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- DROP TABLE IF EXISTS `eh_pm_task_logs`;
CREATE TABLE `eh_pm_task_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,

  `task_id` BIGINT NOT NULL DEFAULT 0,

  `content` TEXT COMMENT 'content data',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive 1: wating, 2: allocated 3: completed 4: closed',
  `target_type` VARCHAR(32) COMMENT 'user',
  `target_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'target user id if target_type is a user',

  `operator_uid` BIGINT NOT NULL DEFAULT 0,
  `operator_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_pm_task_attachments`;
CREATE TABLE `eh_pm_task_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) COMMENT 'attachment object owner type',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner id, e.g comment_id',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- merge from serviceAlliance3.0-delta-schema.sql 20160826
-- DROP TABLE IF EXISTS `eh_service_alliance_categories`;
CREATE TABLE `eh_service_alliance_categories` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `parent_id` BIGINT NOT NULL DEFAULT '0',
  `name` VARCHAR(64) NOT NULL,
  `path` VARCHAR(128),
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` DATETIME,
  `delete_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record deleter user id',
  `delete_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- DROP TABLE IF EXISTS `eh_service_alliances`;
CREATE TABLE `eh_service_alliances` (
  `id` BIGINT NOT NULL,
  `parent_id` BIGINT NOT NULL DEFAULT '0',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community;group,organaization,exhibition,',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'organization name',
  `display_name` VARCHAR(128) NOT NULL DEFAULT '',
  `type` BIGINT NOT NULL DEFAULT '0' COMMENT 'the id reference to eh_service_alliance_categories',
  `address` VARCHAR(255) NOT NULL DEFAULT '',
  `contact` VARCHAR(64) ,
  `description` text,
  `poster_uri` VARCHAR(128) ,
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `default_order` int(11) ,
  `longitude` double ,
  `latitude` double ,
  `geohash` VARCHAR(32) ,
  `discount` BIGINT ,
  `category_id` BIGINT ,
  `contact_name` VARCHAR(128) ,
  `contact_mobile` VARCHAR(128) ,
  `service_type` VARCHAR(128) ,
  `service_url` VARCHAR(128) ,
  `discount_desc` VARCHAR(128) ,
  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128) DEFAULT NULL,
  `string_tag2` VARCHAR(128) DEFAULT NULL,
  `string_tag3` VARCHAR(128) DEFAULT NULL,
  `string_tag4` VARCHAR(128) DEFAULT NULL,
  `string_tag5` VARCHAR(128) DEFAULT NULL,
  `creator_uid` BIGINT ,
  `create_time` datetime ,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- DROP TABLE IF EXISTS `eh_service_alliance_attachments`;
CREATE TABLE `eh_service_alliance_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'the id reference to eh_service_alliances',
  `content_type` VARCHAR(32)  COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024)  COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL DEFAULT '0',
  `create_time` datetime ,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
