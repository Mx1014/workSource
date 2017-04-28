
ALTER TABLE `eh_rentalv2_config_attachments`  ADD COLUMN `string_tag1` VARCHAR(128);
ALTER TABLE `eh_rentalv2_config_attachments`  ADD COLUMN `string_tag2` VARCHAR(128);
ALTER TABLE `eh_rentalv2_config_attachments`  ADD COLUMN `string_tag3` VARCHAR(128);
ALTER TABLE `eh_rentalv2_config_attachments`  ADD COLUMN `string_tag4` VARCHAR(128);
ALTER TABLE `eh_rentalv2_config_attachments`  ADD COLUMN `string_tag5` VARCHAR(128);

ALTER TABLE `eh_rentalv2_order_attachments`  ADD COLUMN `string_tag1` VARCHAR(128);
ALTER TABLE `eh_rentalv2_order_attachments`  ADD COLUMN `string_tag2` VARCHAR(128);
ALTER TABLE `eh_rentalv2_order_attachments`  ADD COLUMN `string_tag3` VARCHAR(128);
ALTER TABLE `eh_rentalv2_order_attachments`  ADD COLUMN `string_tag4` VARCHAR(128);
ALTER TABLE `eh_rentalv2_order_attachments`  ADD COLUMN `string_tag5` VARCHAR(128);


ALTER TABLE `eh_rentalv2_default_rules`  ADD COLUMN `rental_start_time_flag` TINYINT DEFAULT 0 COMMENT '至少提前预约时间标志: 1-限制, 0-不限制';
ALTER TABLE `eh_rentalv2_default_rules`  ADD COLUMN `rental_end_time_flag` TINYINT DEFAULT 0 COMMENT '最多提前预约时间标志: 1-限制, 0-不限制';
ALTER TABLE `eh_rentalv2_default_rules`  ADD COLUMN `org_member_workday_price` DECIMAL(10,2) DEFAULT NULL COMMENT '企业内部工作日价格';
ALTER TABLE `eh_rentalv2_default_rules`  ADD COLUMN `org_member_weekend_price` DECIMAL(10,2) DEFAULT NULL COMMENT '企业内部节假日价格';
ALTER TABLE `eh_rentalv2_default_rules`  ADD COLUMN `approving_user_workday_price` DECIMAL(10,2) DEFAULT NULL COMMENT '外部客户工作日价格';
ALTER TABLE `eh_rentalv2_default_rules`  ADD COLUMN `approving_user_weekend_price` DECIMAL(10,2) DEFAULT NULL COMMENT '外部客户节假日价格';

ALTER TABLE `eh_rentalv2_resources`  ADD COLUMN `rental_start_time_flag` TINYINT DEFAULT 0 COMMENT '至少提前预约时间标志: 1-限制, 0-不限制';
ALTER TABLE `eh_rentalv2_resources`  ADD COLUMN `rental_end_time_flag` TINYINT DEFAULT 0 COMMENT '最多提前预约时间标志: 1-限制, 0-不限制';
ALTER TABLE `eh_rentalv2_resources`  ADD COLUMN `org_member_workday_price` DECIMAL(10,2) DEFAULT NULL COMMENT '企业内部工作日价格';
ALTER TABLE `eh_rentalv2_resources`  ADD COLUMN `org_member_weekend_price` DECIMAL(10,2) DEFAULT NULL COMMENT '企业内部节假日价格';
ALTER TABLE `eh_rentalv2_resources`  ADD COLUMN `approving_user_workday_price` DECIMAL(10,2) DEFAULT NULL COMMENT '外部客户工作日价格';
ALTER TABLE `eh_rentalv2_resources`  ADD COLUMN `approving_user_weekend_price` DECIMAL(10,2) DEFAULT NULL COMMENT '外部客户节假日价格';


