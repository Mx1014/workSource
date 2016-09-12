-- merge from dev-banner-delta-schema.sql
--
-- banner表增加更新时间字段
--
ALTER TABLE `eh_banners` ADD COLUMN `update_time`  DATETIME;

-- 物业报修2.0新增操作人昵称，手机号字段
ALTER TABLE `eh_pm_task_logs` ADD COLUMN `operator_name` VARCHAR(64) COMMENT 'the name of user';
ALTER TABLE `eh_pm_task_logs` ADD COLUMN `operator_phone` VARCHAR(64) COMMENT 'the phone of user';

-- 2016-9-12 added by wuhan 预订2.1修改表
ALTER TABLE `eh_rentalv2_resources` ADD COLUMN `cell_begin_id` BIGINT(20) NOT NULL DEFAULT 0 COMMENT 'cells begin id'; 
ALTER TABLE `eh_rentalv2_resources` ADD COLUMN `cell_end_id` BIGINT(20) NOT NULL DEFAULT 0 COMMENT 'cells end id'; 
ALTER TABLE `eh_rentalv2_resources` ADD COLUMN `unit` DOUBLE DEFAULT 1 COMMENT '1-整租, 0.5-可半个租'; 
ALTER TABLE `eh_rentalv2_resources` ADD COLUMN `begin_date` DATE DEFAULT NULL COMMENT '开始日期'; 
ALTER TABLE `eh_rentalv2_resources` ADD COLUMN `end_date` DATE DEFAULT NULL COMMENT '结束日期'; 
ALTER TABLE `eh_rentalv2_resources` ADD COLUMN `open_weekday` VARCHAR(7) DEFAULT NULL COMMENT '7位二进制，0000000每一位表示星期7123456'; 
ALTER TABLE `eh_rentalv2_resources` ADD COLUMN `workday_price` DECIMAL(10,2) DEFAULT NULL COMMENT '工作日价格'; 
ALTER TABLE `eh_rentalv2_resources` ADD COLUMN `weekend_price` DECIMAL(10,2) DEFAULT NULL COMMENT '周末价格'; 

ALTER TABLE `eh_rentalv2_time_interval` ADD COLUMN `time_step` DOUBLE DEFAULT NULL COMMENT '按小时预约：最小单元格是多少小时，浮点型';