ALTER TABLE eh_parking_lots DROP COLUMN `lock_car_flag`;
ALTER TABLE eh_parking_lots DROP COLUMN `contact`;
ALTER TABLE eh_parking_lots DROP COLUMN `tempfee_flag`;
ALTER TABLE eh_parking_lots DROP COLUMN `rate_flag`;
ALTER TABLE eh_parking_lots DROP COLUMN `max_request_num`;

ALTER TABLE eh_parking_lots ADD COLUMN `config_json` VARCHAR(1024) DEFAULT NULL;

