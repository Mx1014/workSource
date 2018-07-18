-- 通用脚本
-- add by yanlong.liang 20180713
-- 帖子和活动表增加最低限制人数
ALTER TABLE `eh_forum_posts` ADD COLUMN `min_quantity` INT(11) COMMENT '最低限制人数';
ALTER TABLE `eh_activities` ADD COLUMN `min_quantity` INT(11) COMMENT '最低限制人数';
ALTER TABLE `eh_forum_posts` ADD COLUMN `cancel_time` DATETIME COMMENT '取消时间';
ALTER TABLE `eh_activities` ADD COLUMN `cancel_time` DATETIME COMMENT '取消时间';
-- END BY yanlong.liang