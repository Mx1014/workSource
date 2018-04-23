-- 能耗增加分摊比例  by jiarui 20180416
ALTER TABLE `eh_energy_meter_addresses`
  ADD COLUMN `burden_rate`  decimal(10,2) NULL DEFAULT NULL AFTER `status`;
-- 能耗增表计是否远程自动抄表 by jiarui 20180416
ALTER TABLE `eh_energy_meters`
  ADD COLUMN `auto_flag`  tinyint(4) NOT NULL DEFAULT 0 AFTER `status`;

-- 增加比例系数 by jiarui
set @id = ifnull((SELECT MAX(`id`) FROM eh_payment_variables), 0);
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@id:=@id+1, NULL, NULL , '比例系数', '0', now(), NULL, now(), 'blxs');

-- 客户事件设备类型  by jiarui
ALTER TABLE `eh_customer_events`
  ADD COLUMN `device_type`  tinyint(4) NOT NULL DEFAULT 0 AFTER `content`;