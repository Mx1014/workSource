
ALTER TABLE eh_parking_recharge_orders ADD COLUMN `parking_time` INT DEFAULT NULL COMMENT 'parking-time';
ALTER TABLE eh_parking_recharge_orders ADD COLUMN `error_description` TEXT DEFAULT NULL COMMENT 'error description';
ALTER TABLE eh_parking_recharge_orders ADD COLUMN `error_description_json` TEXT DEFAULT NULL COMMENT 'error description';

ALTER TABLE eh_parking_recharge_orders ADD COLUMN `refund_time` datetime DEFAULT NULL COMMENT 'refund time';
ALTER TABLE eh_parking_recharge_orders ADD COLUMN `delay_time` INT DEFAULT NULL COMMENT 'delay time';


ALTER TABLE eh_parking_lots ADD COLUMN `contact` VARCHAR(128) DEFAULT NULL COMMENT 'service contact';


ALTER TABLE eh_parking_recharge_orders CHANGE old_expired_time start_period datetime;
ALTER TABLE eh_parking_recharge_orders CHANGE new_expired_time end_period datetime;


