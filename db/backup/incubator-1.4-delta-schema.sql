-- 增加记录查看字段 add by yanjun 20180306
ALTER TABLE `eh_incubator_applies` ADD COLUMN `check_flag`  tinyint(4) NULL DEFAULT 0 COMMENT '查看状态，0-未查看、1-已查看。这是一个很傻逼的功能，用于区分都是未审核的时候，管理员有没有看过这条记录。';

