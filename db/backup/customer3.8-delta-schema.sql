-- 跟进信息增加字段 by jiarui 20180620
ALTER TABLE `eh_customer_trackings`  ADD COLUMN `contact_phone`  varchar(255) NULL AFTER `content`;
ALTER TABLE `eh_customer_trackings`  ADD COLUMN `visit_time_length`  decimal(10,2) NULL AFTER `contact_phone`;
ALTER TABLE `eh_customer_trackings`  ADD COLUMN `visit_person_name`  varchar(64) NULL AFTER `contact_phone`;