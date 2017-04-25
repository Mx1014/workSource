-- 举报管理增加“处理状态status”和“处理方式”两个字段 add by yanjun 20170424
ALTER TABLE `eh_feedbacks` ADD COLUMN `status` TINYINT(4) DEFAULT '0' NULL COMMENT '0: does not handle, 1: verify true, 2: verify false';
ALTER TABLE `eh_feedbacks` ADD COLUMN `handle_type` TINYINT(4) NULL COMMENT '0: none, 1 delete';
ALTER TABLE `eh_feedbacks` ADD COLUMN `namespace_id` INT(11) NULL;