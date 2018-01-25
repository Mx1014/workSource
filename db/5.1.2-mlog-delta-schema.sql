-- ----------------------------
-- Table structure for eh_messages
-- ----------------------------
-- DROP TABLE IF EXISTS `eh_message_records`;
CREATE TABLE `eh_message_records` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) DEFAULT '0',
  `dst_channel_token` varchar(32) DEFAULT NULL,
  `dst_channel_type` varchar(32) DEFAULT NULL,
  `status` varchar(32) COMMENT 'message status',
  `app_id` bigint(20) DEFAULT '1' COMMENT 'default to messaging app itself',
  `message_seq` bigint(20) DEFAULT NULL COMMENT 'message sequence id generated at server side',
  `sender_uid` bigint(20) DEFAULT NULL,
  `sender_tag` varchar(32) DEFAULT NULL COMMENT 'sender generated tag',
  `channels_info` varchar(2048) DEFAULT NULL,
  `body_type`varchar(32),
  `body` varchar(2048),
  `deliveryOption` int(2) DEFAULT '0',
  `create_time` datetime NOT NULL COMMENT 'message creation time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 考勤管理员操作日志 记录更新考勤组,导入排班表等操作 
CREATE TABLE `eh_punch_operation_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the report template',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `operator_uid` BIGINT NOT NULL DEFAULT 0,
  `operator_name` VARCHAR(64) COMMENT 'the module type',
  `rule_id` BIGINT COMMENT 'the module id',
  `rule_name` VARCHAR(64) COMMENT 'the module type',
  `operate_api` VARCHAR(128) ,
  `request_parameter` TEXT ,
  `create_time` DATETIME COMMENT 'record create time',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;