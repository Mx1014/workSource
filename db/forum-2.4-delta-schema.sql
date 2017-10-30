CREATE TABLE `eh_forum_categories` (
  `id` bigint(20) NOT NULL,
  `uuid` varchar(128) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `forum_id` bigint(20) NOT NULL COMMENT 'forum id',
  `entry_id` bigint(20) NOT NULL COMMENT 'entry id',
  `name` varchar(255) DEFAULT NULL,
  `activity_entry_id` bigint(20) DEFAULT '0' COMMENT 'activity entry id',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 是否支持评论功能
CREATE TABLE `eh_interact_settings` (
  `id` bigint(20) NOT NULL,
  `uuid` varchar(128) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `forum_id` bigint(20) NOT NULL,
  `type` varchar(32) NOT NULL COMMENT 'forum, activity, announcement',
  `entry_id` bigint(20) DEFAULT NULL,
  `interact_flag` tinyint(4) NOT NULL COMMENT 'support interact, 0-no, 1-yes',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_forum_posts` ADD COLUMN `forum_entry_id`  bigint(20) NULL DEFAULT 0 COMMENT 'forum_category  entry_id' ;

ALTER TABLE `eh_forum_posts` ADD COLUMN `interact_flag`  tinyint(4) NOT NULL DEFAULT 1 COMMENT 'support interact, 0-no, 1-yes' ;