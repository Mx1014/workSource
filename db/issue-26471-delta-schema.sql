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
  `delivery_option` int(2) DEFAULT '0',
  `create_time` datetime NOT NULL COMMENT 'message creation time',
  `session_token` varchar(128),
  `device_id` varchar(2048),
  `index_id` bigint(20),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- #issue-26471 服务热线V1.5（客服聊天记录保存和导出）  add by 黄明波  2018/04/28
ALTER TABLE `eh_service_hotlines` ADD COLUMN `status` TINYINT NOT NULL DEFAULT '1' COMMENT '0-deleted 1-active';
