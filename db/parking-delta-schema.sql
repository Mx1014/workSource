
ALTER TABLE eh_parking_recharge_orders ADD COLUMN `parking_time` INT DEFAULT NULL COMMENT 'parking-time';
ALTER TABLE eh_parking_recharge_orders ADD COLUMN `error_description` INT DEFAULT NULL COMMENT 'error description';

ALTER TABLE eh_parking_recharge_orders CHANGE old_expired_time start_period datetime;　
ALTER TABLE eh_parking_recharge_orders CHANGE new_expired_time end_period datetime;　
