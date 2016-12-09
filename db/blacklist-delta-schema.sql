-- 用户黑名单
DROP TABLE IF EXISTS `eh_user_blacklists`;
CREATE TABLE `eh_user_blacklists` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `scope_type` VARCHAR(64) DEFAULT NULL,
  `scope_id` BIGINT DEFAULT NULL,
  `owner_uid` BIGINT NOT NULL,
  `contact_type` TINYINT NOT NULL DEFAULT '0',
  `contact_token` varchar(128) DEFAULT '',
  `contact_name` VARCHAR(64) DEFAULT NULL,
  `gender` TINYINT DEFAULT '0' COMMENT '0: undisclosured, 1: male, 2: female',
  `status` TINYINT NOT NULL DEFAULT '1' COMMENT '0: inactive, 1: confirming, 2: active',
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

