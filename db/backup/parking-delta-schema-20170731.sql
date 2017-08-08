
ALTER TABLE eh_parking_clearance_logs ADD COLUMN `log_token` VARCHAR(1024) DEFAULT NULL COMMENT 'The info from third';
ALTER TABLE eh_parking_clearance_logs ADD COLUMN `log_json` TEXT DEFAULT NULL COMMENT 'The info from third';

