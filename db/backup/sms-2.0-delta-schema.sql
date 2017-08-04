-- 云之讯短信记录  add by xq.tian  2017/07/10
-- DROP TABLE IF EXISTS `eh_yzx_sms_logs`;
CREATE TABLE `eh_yzx_sms_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `scope` VARCHAR(64),
  `code` INTEGER,
  `locale` VARCHAR(16),
  `mobile` VARCHAR(128),
  `text` TEXT NULL,
  `variables` VARCHAR(512),
  `resp_code` VARCHAR(32),
  `failure` TINYINT,
  `create_date` VARCHAR(32),
  `sms_id` VARCHAR(128),
  `type` TINYINT COMMENT '1:状态报告，2：上行',
  `status` TINYINT COMMENT '0:成功；1：提交失败，4：失败，5：关键字（keys），6：黑/白名单，7：超频（overrate），8：unknown',
  `desc` TEXT,
  `report_time` DATETIME,
  `create_time` DATETIME,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `u_eh_contact_token` (`sms_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;