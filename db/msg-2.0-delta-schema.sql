-- 会话推送参数设置  add by xq.tian  2017/04/17
-- DROP TABLE IF EXISTS `eh_user_notification_settings`;
CREATE TABLE `eh_user_notification_settings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `app_id` BIGINT NOT NULL DEFAULT '1' COMMENT 'default to messaging app itself',
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'e.g: EhUsers',
  `owner_id` BIGINT NOT NULL COMMENT 'owner type identify token',
  `target_type` VARCHAR(32) NOT NULL COMMENT 'e.g: EhUsers',
  `target_id` BIGINT NOT NULL COMMENT 'target type identify token',
  `mute_flag` TINYINT NOT NULL DEFAULT '0' COMMENT '0: none, 1: mute',
  `creator_uid` BIGINT DEFAULT NULL COMMENT 'creator uid',
  `create_time` DATETIME(3) DEFAULT NULL COMMENT 'message creation time',
  `update_uid` BIGINT DEFAULT NULL COMMENT 'update uid',
  `update_time` DATETIME(3) DEFAULT NULL COMMENT 'message creation time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 用于记录groupMember表记录的删除还是拒绝的状态
-- DROP TABLE IF EXISTS `eh_group_member_logs`;
CREATE TABLE `eh_group_member_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `group_member_id` BIGINT,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'the same as group member status',
  `creator_uid` BIGINT,
  `process_message` TEXT,
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;