ALTER TABLE `eh_payment_notice_config` ADD COLUMN `notice_day_after` INTEGER DEFAULT NULL COMMENT '欠费日期后多少天';
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `notice_day_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1:欠费前；2：欠费后';
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `notice_obj_type` TINYINT DEFAULT NULL COMMENT '催缴对象的type';
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `notice_obj_id` BIGINT DEFAULT NULL COMMENT '催缴对象的id';
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `notice_app_id` BIGINT DEFAULT NULL COMMENT '催缴app信息模板的id';
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `notice_msg_id` BIGINT DEFAULT NULL COMMENT '催缴sms信息模板的id';




