CREATE TABLE `eh_sync_offline_tasks` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `type` VARCHAR(64) NOT NULL DEFAULT '',
  `status` TINYINT NOT NULL,
  `result` LONGTEXT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;