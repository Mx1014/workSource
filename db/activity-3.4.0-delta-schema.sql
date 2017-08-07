-- 在帖子表中增加一个字段，标识这个帖子是否是克隆帖子   add by yanjun 20170805
ALTER TABLE `eh_forum_posts` ADD COLUMN `clone_flag`  tinyint(4) NULL COMMENT 'clone_flag post 0-real post, 1-clone post';
ALTER TABLE `eh_forum_posts` ADD COLUMN `real_post_id`  bigint(20) NULL COMMENT 'if this is clone post, then it should have a real post id';

ALTER TABLE `eh_activities` ADD COLUMN `clone_flag`  tinyint(4) NULL COMMENT 'clone_flag post 0-real post, 1-clone post';
ALTER TABLE `eh_activities` ADD COLUMN `real_post_id`  bigint(20) NULL COMMENT 'if this is clone post, then it should have a real post id';