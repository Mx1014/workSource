-- 为账单组管理增加“收款方账户”字段
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `biz_payee_account` VARCHAR(1024) COMMENT '收款方账户';
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `biz_payee_type` VARCHAR(1024) COMMENT '收款方账户类型：EhUsers/EhOrganizations';
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `biz_payee_id` VARCHAR(1024) COMMENT '收款方账户id';