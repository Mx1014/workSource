ALTER TABLE eh_parking_card_requests ADD column identity_card VARCHAR(40);

-- binding log id add by xq.tian  2018/01/25
ALTER TABLE eh_point_logs ADD COLUMN binding_log_id BIGINT NOT NULL DEFAULT 0;

-- by xiongying 2018/1/26
CREATE TABLE `eh_sync_data_tasks` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `type` VARCHAR(64) NOT NULL DEFAULT '',
  `status` TINYINT NOT NULL,
  `result` LONGTEXT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_addresses` ADD COLUMN `version` VARCHAR(32) COMMENT '版本号';