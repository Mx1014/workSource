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

  update eh_user_favorites set target_type = 'activity' where target_id in (
  select id from eh_forum_posts where id in (select a.target_id from (select target_id from eh_user_favorites where target_type = 'topic') a) 
  and category_id = 1010);
  
  update eh_user_posts set target_type = 'topic';
  
  update eh_user_posts set target_type = 'activity' where target_id in (
  select id from eh_forum_posts where id in (select a.target_id from (select target_id from eh_user_posts where target_type = 'topic') a) 
  and category_id = 1010);
