-- AUTHOR: 杨崇鑫 20181101
-- REMARK: issue-40954 【标准版全量】【物业缴费】【集成】计价条款设置，新增计价条款后，列表中“账单组”不显示
ALTER TABLE `eh_default_charging_items` ADD COLUMN `bill_group_id` BIGINT COMMENT '账单组ID';