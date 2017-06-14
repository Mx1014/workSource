
ALTER TABLE `eh_rentalv2_config_attachments`  ADD COLUMN `string_tag1` VARCHAR(128);
ALTER TABLE `eh_rentalv2_config_attachments`  ADD COLUMN `string_tag2` VARCHAR(128);
ALTER TABLE `eh_rentalv2_config_attachments`  ADD COLUMN `string_tag3` VARCHAR(128);
ALTER TABLE `eh_rentalv2_config_attachments`  ADD COLUMN `string_tag4` VARCHAR(128);
ALTER TABLE `eh_rentalv2_config_attachments`  ADD COLUMN `string_tag5` VARCHAR(128);


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
ALTER TABLE `eh_rentalv2_resources`  ADD COLUMN `default_order` BIGINT NOT NULL DEFAULT 0 COMMENT 'order';

ALTER TABLE `eh_rentalv2_cells`  ADD COLUMN `org_member_original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '原价-如果打折则有(企业内部价)';
ALTER TABLE `eh_rentalv2_cells`  ADD COLUMN `org_member_price` DECIMAL(10,2) DEFAULT NULL COMMENT '实际价格-打折则为折后价(企业内部价)';
ALTER TABLE `eh_rentalv2_cells`  ADD COLUMN `approving_user_original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '原价-如果打折则有（外部客户价）';
ALTER TABLE `eh_rentalv2_cells`  ADD COLUMN `approving_user_price` DECIMAL(10,2) DEFAULT NULL COMMENT '实际价格-打折则为折后价（外部客户价）';

ALTER TABLE `eh_rentalv2_cells`  ADD COLUMN `half_org_member_original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '半场-原价-如果打折则有(企业内部价)';
ALTER TABLE `eh_rentalv2_cells`  ADD COLUMN `half_org_member_price` DECIMAL(10,2) DEFAULT NULL COMMENT '半场-实际价格-打折则为折后价(企业内部价)';
ALTER TABLE `eh_rentalv2_cells`  ADD COLUMN `half_approving_user_original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '半场-原价-如果打折则有（外部客户价）';
ALTER TABLE `eh_rentalv2_cells`  ADD COLUMN `half_approving_user_price` DECIMAL(10,2) DEFAULT NULL COMMENT '半场-实际价格-打折则为折后价（外部客户价）';



ALTER TABLE `eh_rentalv2_orders`  ADD COLUMN `flow_case_id` BIGINT DEFAULT NULL COMMENT 'id of the flow_case';
