ALTER TABLE `eh_parking_recharge_orders` ADD COLUMN `old_expired_time` DATETIME COMMENT 'old_expired_time';
ALTER TABLE `eh_parking_recharge_orders` ADD COLUMN `new_expired_time` DATETIME COMMENT 'new_expired_time';
ALTER TABLE `eh_parking_recharge_orders` ADD COLUMN `order_no` BIGINT(20) DEFAULT NULL COMMENT 'order no';