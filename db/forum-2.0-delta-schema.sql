
-- 增加一般标签和热门标签的区分   add  by yanjun 20170612
ALTER TABLE `eh_hot_tags` ADD COLUMN `hot_flag` TINYINT(4) DEFAULT '0' NOT NULL COMMENT '0: normal, 1: hot';