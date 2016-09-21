-- DROP TABLE IF EXISTS `eh_search_types`;
CREATE TABLE `eh_search_types` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the type, community, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(128) NOT NULL DEFAULT '',
  `content_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'search content type',
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active', 
  `create_time` DATETIME,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 园区快讯的建表语句 by wh 2016-9-21
--
-- DROP TABLE IF EXISTS `eh_news_categories`;
CREATE TABLE `eh_news_categories` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(64) NOT NULL,
  `path` VARCHAR(128),
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `delete_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record deleter user id',
  `delete_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 给园区快讯增加类型字段 by wh 2016-9-21
ALTER TABLE `eh_news` ADD COLUMN `category_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'category id';