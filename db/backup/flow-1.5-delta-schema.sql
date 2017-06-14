
-- 给工作流评价项增加允许输入评价内容flag字段  add by xq.tian  2017/05/12
ALTER TABLE `eh_flow_evaluate_items` ADD COLUMN `input_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: false, 1: true, input evaluate content flag';

-- 给工作流评价增加评价内容字段  add by xq.tian  2017/05/12
ALTER TABLE `eh_flow_evaluates` ADD COLUMN `content` VARCHAR(1024) DEFAULT NULL COMMENT 'evaluate content';

-- 给工作流按钮增加文字及图片是否必填字段  add by xq.tian  2017/05/12
ALTER TABLE `eh_flow_buttons` ADD COLUMN `subject_required_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: false, 1: true, subject required flag';

-- 给flowCase增加公司字段，以便在普通公司下按照公司进行搜索  add by xq.tian  2017/05/12
ALTER TABLE `eh_flow_cases` ADD COLUMN `organization_id` BIGINT DEFAULT NULL COMMENT 'the same as eh_flows organization_id';