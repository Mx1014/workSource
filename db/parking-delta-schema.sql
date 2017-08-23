ALTER TABLE eh_parking_lots DROP COLUMN `lock_car_flag`;
ALTER TABLE eh_parking_lots DROP COLUMN `contact`;
ALTER TABLE eh_parking_lots DROP COLUMN `tempfee_flag`;
ALTER TABLE eh_parking_lots DROP COLUMN `rate_flag`;
ALTER TABLE eh_parking_lots DROP COLUMN `max_request_num`;

ALTER TABLE eh_parking_lots DROP COLUMN `card_reserve_days`;
ALTER TABLE eh_parking_lots DROP COLUMN `recharge_month_count`;
ALTER TABLE eh_parking_lots DROP COLUMN `recharge_type`;
ALTER TABLE eh_parking_lots DROP COLUMN `is_support_recharge`;

ALTER TABLE eh_parking_lots ADD COLUMN `expired_recharge_json` VARCHAR(1024) DEFAULT NULL;
ALTER TABLE eh_parking_lots ADD COLUMN `config_json` VARCHAR(1024) DEFAULT NULL;

