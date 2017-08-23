--
-- 短信日志 add by xq.tian  2017/08/23
--
ALTER TABLE `eh_sms_logs` ADD COLUMN `handler` VARCHAR(128) NOT NULL COMMENT 'YunZhiXun, YouXunTong, LianXinTong';
ALTER TABLE `eh_sms_logs` ADD COLUMN `sms_id` VARCHAR(128) NOT NULL COMMENT 'sms identifier';
ALTER TABLE `eh_sms_logs` ADD COLUMN `status` TINYINT NOT NULL DEFAULT 1 COMMENT '1: send success, 2: send failed, 4: report success, 5: report failed';
ALTER TABLE `eh_sms_logs` ADD COLUMN `http_status_code` INTEGER COMMENT 'http response code, e.g: 200, 400, 500';
ALTER TABLE `eh_sms_logs` ADD COLUMN `report_text` TEXT COMMENT 'report text';
ALTER TABLE `eh_sms_logs` ADD COLUMN `report_time` DATETIME(3);
