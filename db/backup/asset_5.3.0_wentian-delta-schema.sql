ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `bills_day_type` TINYINT NOT NULL DEFAULT 4 COMMENT '1. 本周期前几日；2.本周期第几日；3.本周期结束日；4.下周期首月第几日';

