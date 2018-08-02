-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.3 （欠费时间筛选、费项进多账单组）
ALTER TABLE `eh_payment_bills` ADD COLUMN `due_day_count` BIGINT COMMENT '欠费天数' after due_day_deadline;


-- --------------------- SECTION END ---------------------------------------------------------
