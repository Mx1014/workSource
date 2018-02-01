ALTER TABLE `eh_rentalv2_items`
  CHANGE COLUMN `rental_resource_id` `source_id` BIGINT(20) NULL DEFAULT NULL;
ALTER TABLE `eh_rentalv2_items`
  ADD COLUMN `source_type` VARCHAR(45) NULL COMMENT 'DEFAULT: default_rule  RESOURCE: resource_rule' AFTER `id`;
