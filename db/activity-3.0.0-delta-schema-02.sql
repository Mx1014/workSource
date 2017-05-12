-- meger feedback-1.0 start
-- 举报管理增加“处理状态status”、“核实情况”、“处理方式”和“域空间”四个字段 add by yanjun 20170425
ALTER TABLE `eh_feedbacks` ADD COLUMN `status` TINYINT(4) DEFAULT '0' NULL COMMENT '0: does not handle, 1: have handled';
ALTER TABLE `eh_feedbacks` ADD COLUMN `verify_type` TINYINT(4) NULL COMMENT '0: verify false, 1: verify true';
ALTER TABLE `eh_feedbacks` ADD COLUMN `handle_type` TINYINT(4) NULL COMMENT '0: none, 1 delete';
ALTER TABLE `eh_feedbacks` ADD COLUMN `namespace_id` INT(11) NULL;
ALTER TABLE `eh_feedbacks` ADD COLUMN `handle_time` DATETIME NULL;

-- 举报管理中有用到target_id进行查询 add by yanjun 20170427
ALTER TABLE `eh_feedbacks` ADD INDEX i_eh_feedbacks_target_id(`target_id`);

-- 修改举报content_category的注释
ALTER TABLE `eh_feedbacks` CHANGE `content_category` `content_category` BIGINT(20) DEFAULT '0' NOT NULL COMMENT '0-其它、1-产品bug、2-产品改进、3-版本问题;11-敏感信息、12-版权问题、13-暴力色情、14-诈骗和虚假信息、15-骚扰；16-谣言、17-恶意营销、18-诱导分享；19-政治';

-- meger feedback-1.0 end

-- meger msg-2.0 start
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

-- meger msg-2.0 end