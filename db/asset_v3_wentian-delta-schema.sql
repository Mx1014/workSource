ALTER TABLE `eh_payment_bills` ADD COLUMN `customer_tel` VARCHAR(32) COMMENT '客户的手机号，用于存储个人客户的信息';
ALTER TABLE `eh_payment_bills` MODIFY COLUMN `charge_status` TINYINT NOT NULL DEFAULT 0 COMMENT '缴费状态，0：正常；1：欠费';
