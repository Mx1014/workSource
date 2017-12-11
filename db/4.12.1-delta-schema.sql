
-- added by janson
ALTER TABLE `eh_organization_address_mappings`
ADD COLUMN `building_id` BIGINT(20) NOT NULL DEFAULT 0 AFTER `namespace_type`,
ADD COLUMN `building_name` VARCHAR(128) NULL AFTER `building_id`;
