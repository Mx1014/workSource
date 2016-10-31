-- 圈添加审核状态，add by tt, 20161028
ALTER TABLE `eh_groups` ADD COLUMN `approval_status` TINYINT COMMENT 'approval status';

-- 设置圈（俱乐部）参数，add by tt, 20161028
-- DROP TABLE IF EXISTS `eh_group_settings`;
CREATE TABLE `eh_group_settings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `create_flag` TINYINT COMMENT 'whether allow create club',
  `verify_flag` TINYINT COMMENT 'whether need verify',
  `member_post_flag` TINYINT COMMENT 'whether allow members create post',
  `member_comment_flag` TINYINT COMMENT 'whether allow members comment on the post',
  `admin_broadcast_flag` TINYINT COMMENT 'whether allow admin broadcast',
  `broadcast_count` TINYINT COMMENT 'how many broadcasts can be created per day',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'news creator uid',
  `create_time` DATETIME COMMENT 'create time',
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'operator uid',
  `update_time` DATETIME COMMENT 'update time',
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 广播消息，add by tt, 20161028
-- DROP TABLE IF EXISTS `eh_broadcasts`;
CREATE TABLE `eh_broadcasts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(32) COMMENT 'group',
  `owner_id` BIGINT DEFAULT 0 COMMENT 'group_id',
  `title` VARCHAR(1024) COMMENT 'title',
  `content_type` VARCHAR(32) COMMENT 'object content type: text、rich text',
  `content` LONGTEXT COMMENT 'content data, depends on value of content_type',
  `content_abstract` TEXT COMMENT 'abstract of content data',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'creator uid',
  `create_time` DATETIME COMMENT 'create time',
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'operator uid',
  `update_time` DATETIME COMMENT 'update time',
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;