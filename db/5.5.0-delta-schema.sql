-- 文档管理1.2 start by ryan.
ALTER TABLE `eh_file_management_catalog_scopes` ADD COLUMN `source_type` VARCHAR(64) COMMENT'the type of the source' AFTER `source_id`;
UPDATE `eh_file_management_catalog_scopes` SET source_type = 'MEMBERDETAIL';
-- 文档管理1.2 end by ryan.


-- issue-26471 服务热线V1.5（客服聊天记录保存和导出) by tianxiaoqiang 2018.5.8
CREATE TABLE `eh_message_records` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INT(11) DEFAULT '0',
  `dst_channel_token` VARCHAR(32) DEFAULT NULL,
  `dst_channel_type` VARCHAR(32) DEFAULT NULL,
  `status` VARCHAR(32) COMMENT 'message status',
  `app_id` BIGINT(20) DEFAULT '1' COMMENT 'default to messaging app itself',
  `message_seq` BIGINT(20) DEFAULT NULL COMMENT 'message sequence id generated at server side',
  `sender_uid` BIGINT(20) DEFAULT NULL,
  `sender_tag` VARCHAR(32) DEFAULT NULL COMMENT 'sender generated tag',
  `channels_info` VARCHAR(2048) DEFAULT NULL,
  `body_type`VARCHAR(32),
  `body` VARCHAR(2048),
  `delivery_option` INT(2) DEFAULT '0',
  `create_time` DATETIME NOT NULL COMMENT 'message creation time',
  `session_token` VARCHAR(128),
  `device_id` VARCHAR(2048),
  `index_id` BIGINT(20),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- issue-26471 服务热线V1.5（客服聊天记录保存和导出) by huangmingbo 2018.5.8
ALTER TABLE `eh_service_hotlines` ADD COLUMN `status` TINYINT NOT NULL DEFAULT '1' COMMENT '0-deleted 1-active';

-- by zheng
ALTER TABLE `eh_configurations` MODIFY `value` VARCHAR(1024);

-- add by yanjun 增加字段长度 201805091806
 
ALTER TABLE `eh_service_module_apps` MODIFY COLUMN `custom_tag`  VARCHAR(256)  NULL DEFAULT '';
-- issue-23470 by liuyilin
ALTER TABLE `eh_door_access`
ADD `enable_amount` TINYINT COMMENT '是否支持按次数开门的授权';

ALTER TABLE `eh_door_auth`
ADD `auth_rule_type` TINYINT COMMENT '授权规则的种类,0 按时间,1 按次数';
ALTER TABLE `eh_door_auth`
ADD `total_auth_amount` INT COMMENT '授权的总开门次数';
ALTER TABLE `eh_door_auth`
ADD `valid_auth_amount` INT COMMENT '剩余的开门次数';
-- End by liuyilin 


-- added by wh 
-- 增加应打卡时间给punch_log
ALTER TABLE `eh_punch_logs` ADD should_punch_time BIGINT COMMENT '应该打卡时间(用以计算早退迟到时长)';
