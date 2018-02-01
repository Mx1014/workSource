ALTER TABLE `eh_rentalv2_items`
  ADD COLUMN `source_type` VARCHAR(45) NULL COMMENT 'DEFAULT: default_rule  RESOURCE: resource_rule' AFTER `id`;
