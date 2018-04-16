-- 能耗增加分摊比例  by jiarui 20180416
ALTER TABLE `eh_energy_meter_addresses`
ADD COLUMN `burden_rate`  decimal(10,2) NULL DEFAULT NULL AFTER `status`;