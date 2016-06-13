
DROP TABLE IF EXISTS `eh_wifi_settings`;
CREATE TABLE `eh_wifi_settings` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `ssid` VARCHAR(128) NOT NULL COMMENT 'the name of address resource',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `delete_uid` BIGINT DEFAULT 0,
  `create_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0 : inactive, 1: active',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;



