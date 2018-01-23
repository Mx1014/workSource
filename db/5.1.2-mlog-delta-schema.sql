-- ----------------------------
-- Table structure for eh_messages
-- ----------------------------
-- DROP TABLE IF EXISTS `eh_message_records`;
CREATE TABLE `eh_message_records` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) DEFAULT '0',
  `app_id` bigint(20) DEFAULT '1' COMMENT 'default to messaging app itself',
  `message_seq` bigint(20) DEFAULT NULL COMMENT 'message sequence id generated at server side',
  `sender_uid` bigint(20) DEFAULT NULL,
  `sender_tag` varchar(32) DEFAULT NULL COMMENT 'sender generated tag',
  `dst_channel_type` varchar(32) DEFAULT NULL,
  `dst_channel_token` varchar(32) DEFAULT NULL,
  `channels_info` varchar(32) DEFAULT NULL,
  `body_type`varchar(32),
  `body` varchar(128),
  `deliveryOption` int(2) DEFAULT '0',
  `create_time` datetime NOT NULL COMMENT 'message creation time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
