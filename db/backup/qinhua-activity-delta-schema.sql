-- 添加人数限制字段
ALTER TABLE `eh_forum_posts` ADD COLUMN `max_quantity` INT COMMENT 'max person quantity';
ALTER TABLE `eh_activities` ADD COLUMN `max_quantity` INT COMMENT 'max person quantity';
ALTER TABLE `eh_activities` ADD COLUMN `content_type` varchar(128) COMMENT 'content type, text/rich_text';
ALTER TABLE `eh_activities` ADD COLUMN `version` varchar(128) COMMENT 'version';

-- 添加消息提醒设置表
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