--
-- member of global sharding group
--
DROP TABLE IF EXISTS `eh_activity_video`;
CREATE TABLE `eh_activity_video` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `video_state` TINYINT NOT NULL DEFAULT 0 COMMENT '0:UN_READY, 1:DEBUG, 2:LIVE, 3:RECORDING, 4:EXCEPTION, 5:INVALID',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'activity',
  `owner_id` BIGINT NOT NULL DEFAULT 0 ,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 ,
  `start_time` BIGINT NOT NULL DEFAULT 0,
  `end_time` BIGINT NOT NULL DEFAULT 0,
  `room_type` VARCHAR(64),
  `room_id` VARCHAR(64),
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

-- ALTER TABLE `eh_activities` ADD COLUMN `video_url` VARCHAR(128) COMMENT 'url of video support' AFTER `official_flag`;
ALTER TABLE `eh_activities` ADD COLUMN `video_support` TINYINT NOT NULL DEFAULT 0 COMMENT 'is video support' AFTER `video_url`;

DROP TABLE IF EXISTS `eh_yzb_devices`;
CREATE TABLE `eh_yzb_devices` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `device_id` VARCHAR(64) NOT NULL COMMENT 'device_id of yzb',
  `room_id` VARCHAR(64) NOT NULL COMMENT 'room_id of this devices',
  `relative_id` BIGINT NOT NULL COMMENT 'activity_id',
  `relative_type` VARCHAR(64) NOT NULL DEFAULT "activity",
  `last_vid` VARCHAR(64) COMMENT 'the last vid',
  `state` TINYINT NOT NULL DEFAULT 0 COMMENT 'the current state of this devices',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'INVALID, VALID',
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
