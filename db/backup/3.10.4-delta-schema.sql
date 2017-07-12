--
-- 服务联盟category添加显示类型字段   add by xq.tian  2016/10/18
--
ALTER TABLE `eh_service_alliance_categories` ADD COLUMN `display_mode` TINYINT DEFAULT 1;


-- 服务联盟添加新的预约表 add by xiongying20161027
CREATE TABLE `eh_service_alliance_reservation_requests` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `template_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'i.e. EhServiceAllianceApplies type',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `type` BIGINT NOT NULL DEFAULT '0',
  `category_id` BIGINT NOT NULL DEFAULT '0',
  `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `creator_name` VARCHAR(128) DEFAULT NULL,
  `creator_mobile` VARCHAR(128) DEFAULT NULL,
  `creator_organization_id` BIGINT NOT NULL DEFAULT '0',
  `service_alliance_id` BIGINT NOT NULL DEFAULT '0',
  `reserve_type` VARCHAR(128) DEFAULT NULL,
  `reserve_organization` VARCHAR(128) DEFAULT NULL,
  `reserve_time` VARCHAR(128) DEFAULT NULL,
  `contact` VARCHAR(128) DEFAULT NULL,
  `mobile` VARCHAR(128) DEFAULT NULL,
  `remarks` VARCHAR(1024) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 添加人数限制字段, add by tt, 20161027
ALTER TABLE `eh_forum_posts` ADD COLUMN `max_quantity` INT COMMENT 'max person quantity';
ALTER TABLE `eh_activities` ADD COLUMN `max_quantity` INT COMMENT 'max person quantity';
ALTER TABLE `eh_activities` ADD COLUMN `content_type` varchar(128) COMMENT 'content type, text/rich_text';
ALTER TABLE `eh_activities` ADD COLUMN `version` varchar(128) COMMENT 'version';

-- 添加消息提醒设置表, add by tt, 20161027
-- DROP TABLE IF EXISTS `eh_warning_settings`;
CREATE TABLE `eh_warning_settings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL COMMENT 'namespace id',
  `type` varchar(64) COMMENT 'type',
  `time` BIGINT COMMENT 'millisecond',
  `create_time` DATETIME,
  `creator_uid` BIGINT,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;