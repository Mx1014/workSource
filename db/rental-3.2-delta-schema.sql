ALTER TABLE `eh_rentalv2_items`
  CHANGE COLUMN `rental_resource_id` `source_id` BIGINT(20) NULL DEFAULT NULL;
ALTER TABLE `eh_rentalv2_items`
  ADD COLUMN `source_type` VARCHAR(45) NULL COMMENT 'DEFAULT: default_rule  RESOURCE: resource_rule' AFTER `id`;

UPDATE eh_rentalv2_items set source_type = 'default_rule' where source_type IS NULL;

ALTER TABLE `eh_rentalv2_config_attachments` 
ADD COLUMN `default_order` INT NULL DEFAULT '0' AFTER `string_tag5`;
