--
-- App启动广告   add by xq.tian  2016/11/28
--
-- DROP TABLE IF EXISTS `eh_launch_advertisements`;
CREATE TABLE `eh_launch_advertisements` (
  `id` BIGINT  NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `content_type` VARCHAR(32) COMMENT '1: IMAGE, 2: VIDEO',
  `content_uri` VARCHAR(1024) COMMENT 'advertisement image/gif/video uri',
  `times_per_day` INTEGER DEFAULT 0 COMMENT 'The maximum number of times to display per day',
  `display_interval` INTEGER DEFAULT 0 COMMENT 'Minimum display time interval, ',
  `duration_time` INTEGER COMMENT 'duration time',
  `skip_flag` TINYINT COMMENT '0: can not skip, 1: skip',
  `action_type` TINYINT COMMENT '0: can not click, 1: click',
  `action_data` TEXT COMMENT 'If allow click, the jumped url',
  `status` TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4;