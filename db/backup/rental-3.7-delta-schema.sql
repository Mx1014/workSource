-- 资源预约3.7
ALTER TABLE `eh_rentalv2_order_records`
ADD COLUMN `account_name`  varchar(255) NULL AFTER `account_id`;
ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `account_name`  varchar(255) NULL AFTER `old_custom_object`;