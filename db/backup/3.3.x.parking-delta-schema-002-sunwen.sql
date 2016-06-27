ALTER TABLE `eh_parking_recharge_orders` ADD COLUMN `old_expired_time` DATETIME COMMENT 'old_expired_time';
ALTER TABLE `eh_parking_recharge_orders` ADD COLUMN `new_expired_time` DATETIME COMMENT 'new_expired_time';
ALTER TABLE `eh_parking_recharge_orders` ADD COLUMN `order_no` BIGINT(20) DEFAULT NULL COMMENT 'order no';
ALTER TABLE `eh_parking_recharge_orders` ADD COLUMN `paid_type` VARCHAR(32) COMMENT 'the type of payer';
ALTER TABLE `eh_parking_recharge_orders` ADD COLUMN `is_delete` TINYINT NOT NULL DEFAULT 0 COMMENT 'the order is delete, 0 : is not deleted, 1: deleted';