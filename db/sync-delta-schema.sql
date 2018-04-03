
-- 数据同步应用 Jannson
-- DROP TABLE IF EXISTS `eh_sync_apps`;
CREATE TABLE `eh_sync_apps` (
  `id` BIGINT NOT NULL,
  `app_key` VARCHAR(64),
  `secret_key` VARCHAR(1024),
  `name` VARCHAR(64),
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the item, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `policy_type` TINYINT NOT NULL COMMENT 'refresh, increment',
  `state` TINYINT NOT NULL COMMENT 'invalid, sleeping, processing, confict',
  `description` VARCHAR(1024),
  `last_update` DATETIME DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_sync_mapping`;
CREATE TABLE `eh_sync_mapping` (
  `id` BIGINT NOT NULL,
  `sync_app_id` BIGINT NOT NULL,
  `name` VARCHAR(64),
  `content` TEXT,
  `create_time` DATETIME DEFAULT NULL,
  `status` TINYINT NOT NULL COMMENT 'invalid, valid',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_sync_trans`;
CREATE TABLE `eh_sync_trans` (
  `id` BIGINT NOT NULL,
  `sync_app_id` BIGINT NOT NULL,
  `parent_trans_id` BIGINT NOT NULL COMMENT '0 means not has a parent trans',
  `version_id` BIGINT NOT NULL COMMENT 'version control',
  `content` TEXT,
  `result` TEXT,
  `state` TINYINT NOT NULL COMMENT 'invalid, refresh, merge, confict',
  `last_update` DATETIME DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

