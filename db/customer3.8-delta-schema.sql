-- 跟进信息增加字段 by jiarui 20180620
ALTER TABLE `eh_customer_trackings`
  ADD COLUMN `contact_phone`  varchar(255) NULL AFTER `content`,
  ADD COLUMN `visit_time_length`  decimal(10,2) NULL AFTER `contact_phone`,
  ADD COLUMN `visit_person_uid`  bigint(20) NULL AFTER `visit_time_length`,
  ADD COLUMN `visit_person_name`  varchar(64) NULL AFTER `visit_person_uid`;