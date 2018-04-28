ALTER TABLE `eh_payment_bills` ADD COLUMN `invoice_number` VARCHAR(128) COMMENT '发票编号';
ALTER TABLE `eh_contract_charging_changes` ADD COLUMN `change_duration_days` INTEGER DEFAULT NULL COMMENT '变化的天数，例如免租了xx天';
-- from 5.3.4 TODO delete
ALTER TABLE `eh_payment_bills` ADD COLUMN `customer_tel` VARCHAR(32) COMMENT '客户的手机号，用于存储个人客户的信息';