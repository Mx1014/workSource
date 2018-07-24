-- 能耗增加分摊比例  by jiarui 20180416
ALTER TABLE `eh_energy_meter_addresses`
  ADD COLUMN `burden_rate`  decimal(10,2) NULL DEFAULT NULL AFTER `status`;
-- 能耗增表计是否远程自动抄表 by jiarui 20180416
ALTER TABLE `eh_energy_meters`
  ADD COLUMN `auto_flag`  tinyint(4) NOT NULL DEFAULT 0 AFTER `status`;

-- 客户事件设备类型  by jiarui
ALTER TABLE `eh_customer_events`
  ADD COLUMN `device_type`  tinyint(4) NOT NULL DEFAULT 0 AFTER `content`;

-- 客户的跟进人  by jiarui
ALTER TABLE `eh_enterprise_customers`
  MODIFY COLUMN `tracking_uid`  bigint(20) NULL DEFAULT NULL COMMENT 'tracking uid' AFTER `update_time`;

