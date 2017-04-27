-- 举报管理增加“处理状态status”、“核实情况”、“处理方式”和“域空间”四个字段 add by yanjun 20170425
ALTER TABLE `eh_feedbacks` ADD COLUMN `status` TINYINT(4) DEFAULT '0' NULL COMMENT '0: does not handle, 1: have handled';
ALTER TABLE `eh_feedbacks` ADD COLUMN `verify_type` TINYINT(4) NULL COMMENT '0: verify false, 1: verify true';
ALTER TABLE `eh_feedbacks` ADD COLUMN `handle_type` TINYINT(4) NULL COMMENT '0: none, 1 delete';
ALTER TABLE `eh_feedbacks` ADD COLUMN `namespace_id` INT(11) NULL;

-- 举报管理中有用到target_id进行查询 add by yanjun 20170427
ALTER TABLE `eh_feedbacks` ADD INDEX i_eh_feedbacks_target_id(`target_id`);