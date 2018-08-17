-- AUTHOR: 丁建民
-- REMARK: 合同2.8
ALTER TABLE `eh_contracts` ADD COLUMN `cost_generation_method`  tinyint DEFAULT NULL COMMENT '合同截断时的费用收取方式，0：按计费周期，1：按实际天数';