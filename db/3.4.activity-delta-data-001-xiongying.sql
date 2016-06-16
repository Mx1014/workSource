ALTER TABLE `eh_activities` ADD COLUMN media_url VARCHAR(1024) DEFAULT NULL;
ALTER TABLE `eh_user_posts` ADD COLUMN target_type VARCHAR(32) DEFAULT NULL;
ALTER TABLE `eh_user_posts` CHANGE post_id target_id BIGINT NOT NULL DEFAULT '0';

CREATE TABLE `eh_hot_tags` (
`id` BIGINT NOT NULL,
`namespace_id` INTEGER NOT NULL DEFAULT '0',
`service_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'service type, eg: activity',
`name` VARCHAR(128) NOT NULL,
`status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active',
`default_order` INTEGER NOT NULL DEFAULT '0',
`create_time` DATETIME DEFAULT NULL,
`create_uid` BIGINT DEFAULT NULL,
`delete_time` DATETIME DEFAULT NULL,
`delete_uid` BIGINT DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`, `namespace_id`) 
    VALUES( 'user.notification', 2, 'zh_CN', '注册天数描述', '我已加入左邻“${days}”天', 0);
  
UPDATE eh_user_favorites SET target_type = 'activity' WHERE target_id IN (
  SELECT id FROM eh_forum_posts WHERE id IN (SELECT a.target_id FROM (SELECT target_id FROM eh_user_favorites WHERE target_type = 'topic') a) 
  AND category_id = 1010);
  
UPDATE eh_user_posts SET target_type = 'topic';
  
UPDATE eh_user_posts SET target_type = 'activity' WHERE target_id IN (
  SELECT id FROM eh_forum_posts WHERE id IN (SELECT a.target_id FROM (SELECT target_id FROM eh_user_posts WHERE target_type = 'topic') a) 
  AND category_id = 1010);
