-- 文档管理1.2 start by ryan.
ALTER TABLE `eh_file_management_catalog_scopes` ADD COLUMN `source_type` VARCHAR(64) COMMENT'the type of the source' AFTER `source_id`;
UPDATE `eh_file_management_catalog_scopes` SET source_type = 'MEMBERDETAIL';
-- 文档管理1.2 end by ryan.


-- issue-26471 服务热线V1.5（客服聊天记录保存和导出) by tianxiaoqiang 2018.5.8
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


-- issue-26471 服务热线V1.5（客服聊天记录保存和导出) by huangmingbo 2018.5.8
ALTER TABLE `eh_service_hotlines` ADD COLUMN `status` TINYINT NOT NULL DEFAULT '1' COMMENT '0-deleted 1-active';

-- by zheng
ALTER TABLE `eh_configurations` modify `value` varchar(1024);
