ALTER TABLE `eh_payment_notice_config` ADD COLUMN `notice_day_after` INTEGER DEFAULT NULL COMMENT '欠费日期后多少天';
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `notice_day_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1:欠费前；2：欠费后';
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `notice_objs` BIGINT DEFAULT NULL COMMENT '催缴对象,格式为{type+id,type+id,...}';
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `notice_app_id` BIGINT DEFAULT NULL COMMENT '催缴app信息模板的id';
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `notice_msg_id` BIGINT DEFAULT NULL COMMENT '催缴sms信息模板的id';
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `create_time` DATETIME DEFAULT NULL COMMENT '创建时间';
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `create_uid` DATETIME DEFAULT NULL COMMENT '创建账号id';
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `update_time` DATETIME DEFAULT NULL COMMENT '更新时间';
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `update_uid` DATETIME DEFAULT NULL COMMENT '更新账户的id';







