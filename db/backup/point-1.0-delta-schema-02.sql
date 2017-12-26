
-- 在帖子表里增加模块类型，以及类型入口  add by yanjun 20171205
ALTER TABLE `eh_forum_posts` ADD COLUMN `module_type`  tinyint(4) NULL, ADD COLUMN `module_category_id`  bigint(20);

-- 评论功能使用域空间、module、categoryid唯一定位  add by yanjun 20171205
DELETE from eh_interact_settings;
ALTER TABLE `eh_interact_settings` DROP COLUMN `forum_id`, CHANGE COLUMN `type` `module_type`  tinyint(4) NOT NULL COMMENT 'forum, activity, announcement' AFTER `namespace_id`, CHANGE COLUMN `entry_id` `category_id`  bigint(20) NULL DEFAULT NULL AFTER `module_type`;