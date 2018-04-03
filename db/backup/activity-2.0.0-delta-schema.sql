-- 增加主题分类id，added by tt, 20170106
ALTER TABLE `eh_activities` ADD COLUMN `content_category_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'content category id';

-- 增加活动分类及子分类，因为后台管理接口还是从帖子这里查的，added by tt, 20170116
ALTER TABLE `eh_forum_posts` ADD COLUMN `activity_category_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'activity category id';
ALTER TABLE `eh_forum_posts` ADD COLUMN `activity_content_category_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'activity content category id';

-- 增加一些字段用于主题分类，added by tt, 20170106
-- 此表对于parent_id为0的表示入口id，否则表示主题分类id
ALTER TABLE `eh_activity_categories` ADD COLUMN `enabled` TINYINT NOT NULL DEFAULT '1' COMMENT '0: no, 1: yes';
ALTER TABLE `eh_activity_categories` ADD COLUMN `icon_uri` VARCHAR(1024) NULL;
ALTER TABLE `eh_activity_categories` ADD COLUMN `selected_icon_uri` VARCHAR(1024) NULL;
ALTER TABLE `eh_activity_categories` ADD COLUMN `show_name` VARCHAR(64) NULL;
ALTER TABLE `eh_activity_categories` ADD COLUMN `all_flag` TINYINT NOT NULL DEFAULT '0' COMMENT '0: no, 1: yes';

-- 增加选中时的图片，added by tt, 20170106
ALTER TABLE `eh_launch_pad_items` ADD COLUMN `selected_icon_uri` VARCHAR(1024) NULL DEFAULT NULL;

