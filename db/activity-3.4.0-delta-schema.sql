-- 在帖子表中增加一个字段，标识这个帖子是否是克隆帖子   add by yanjun 20170805
ALTER TABLE `eh_forum_posts` ADD COLUMN `clone_flag`  tinyint(4) NULL COMMENT 'clone_flag post 0-real post, 1-clone post';
ALTER TABLE `eh_forum_posts` ADD COLUMN `real_post_id`  bigint(20) NULL COMMENT 'if this is clone post, then it should have a real post id';

ALTER TABLE `eh_activities` ADD COLUMN `clone_flag`  tinyint(4) NULL COMMENT 'clone_flag post 0-real post, 1-clone post';

-- -- 活动报名导入错误信息  add by yanjun 20170828
-- CREATE TABLE `eh_activity_roster_error` (
--   `id` bigint(20) NOT NULL COMMENT 'id',
--   `uuid` varchar(36) DEFAULT NULL COMMENT 'uuid',
--   `job_id` bigint(20) DEFAULT NULL COMMENT 'jobId, one job may has several error',
--   `row_num` int(11) DEFAULT NULL COMMENT 'row_num',
--   `description` varchar(255) DEFAULT NULL COMMENT 'description',
--   `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
--   `create_uid` bigint(20) DEFAULT NULL,
--   PRIMARY KEY (`id`),
--   KEY `eh_activity_roster_error_jobId` (`job_id`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;