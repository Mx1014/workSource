-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.3 （欠费时间筛选、费项进多账单组）
ALTER TABLE `eh_payment_bills` ADD COLUMN `due_day_count` BIGINT COMMENT '欠费天数' after due_day_deadline;
-- REMARK: 因为新增计价条款时先选组再选费项，故查看合同概览时在收费项目前多加一列字段。概览中前4个Tab均需要在收费项目前多加一列账单组。
ALTER TABLE `eh_contract_charging_items` ADD COLUMN `bill_group_id` BIGINT COMMENT '账单组ID';
ALTER TABLE `eh_contract_charging_changes` ADD COLUMN `bill_group_id` BIGINT COMMENT '账单组ID';

-- --------------------- SECTION END ---------------------------------------------------------
