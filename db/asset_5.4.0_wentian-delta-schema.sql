ALTER TABLE `eh_payment_bills` ADD COLUMN `invoice_number` VARCHAR(128) COMMENT '发票编号';

-- from 5.3.4 TODO delete
ALTER TABLE `eh_payment_bills` ADD COLUMN `customer_tel` VARCHAR(32) COMMENT '客户的手机号，用于存储个人客户的信息';