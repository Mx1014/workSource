-- 为账单组管理增加“收款方账户”字段
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `payee_account` VARCHAR(1024) COMMENT '收款方账户';