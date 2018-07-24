ALTER TABLE `eh_payment_bills` ADD COLUMN `customer_tel` VARCHAR(32) COMMENT '客户的手机号，用于存储个人客户的信息';

ALTER TABLE `eh_parking_card_requests` ADD COLUMN `card_type_name` VARCHAR(128) DEFAULT NULL COMMENT '冗余存储卡类型名称';
