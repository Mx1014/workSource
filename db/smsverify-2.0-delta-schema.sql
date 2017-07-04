-- 短信黑名单  add by xq.tian  2017/07/04
-- DROP TABLE IF EXISTS `eh_sms_black_lists`;
CREATE TABLE `eh_sms_black_lists` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `contact_token` VARCHAR(32) NOT NULL COMMENT 'contact token',
  `reason` VARCHAR(128) DEFAULT NULL COMMENT 'reason',
  `status` TINYINT NOT NULL DEFAULT '1' COMMENT '0: pass, 1: block',
  `create_type` TINYINT NOT NULL DEFAULT '0' COMMENT '0: Created by system, 1: Manually created',
  `creator_uid` BIGINT DEFAULT NULL,
  `create_time` DATETIME(3) DEFAULT NULL,
  `update_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `u_eh_contact_token` (`contact_token`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;