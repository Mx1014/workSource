-- 增加详情字段
ALTER TABLE `eh_rentalv2_items` ADD COLUMN `description` VARCHAR(1024) NULL DEFAULT NULL AFTER `item_type`;