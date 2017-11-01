-- 增加缴费的工作id   wentian
ALTER TABLE `eh_payment_bills` ADD COLUMN `next_switch` TINYINT DEFAULT 0 COMMENT '下一次switch的值';
ALTER TABLE `eh_payment_contract_receiver` ADD COLUMN `in_work` TINYINT DEFAULT 0 COMMENT '0:工作完成；1：正在生成';
ALTER TABLE `eh_payment_contract_receiver` ADD COLUMN `is_recorder` TINYINT DEFAULT 1 COMMENT '0：合同状态记录者，不保存计价数据；1：不是合同状态记录者';
