-- 帖子表添加父评论id, add by tt, 20170314
ALTER TABLE `eh_forum_posts` ADD COLUMN `parent_comment_id` BIGINT(20) COMMENT 'parent comment id';