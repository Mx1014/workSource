--
-- member of global sharding group
--
DROP TABLE IF EXISTS `eh_activity_video`;
CREATE TABLE `eh_activity_video` (
  `id` BIGINT NOT NULL,
  `video_state` TINYINT NOT NULL DEFAULT 0 COMMENT '0:UN_READY, 1:DEBUG, 2:LIVE, 3:RECORDING, 4:INVALID',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'activity',
  `owner_id` BIGINT NOT NULL DEFAULT 0 ,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 ,
  `start_time` BIGINT NOT NULL DEFAULT '',
  `end_time` BIGINT NOT NULL DEFAULT '',
  `channel_type` VARCHAR(64),
  `channel_id` VARCHAR(64),
  `manufacturer_type` VARCHAR(64) COMMENT 'YZB',
  `extra` TEXT,
  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
