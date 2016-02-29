ALTER TABLE `eh_organizations` ADD COLUMN `group_type` VARCHAR(64)  COMMENT 'enterprise, department, service_group'; 
ALTER TABLE `eh_organization_members` ADD COLUMN `group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to the organization id';
ALTER TABLE `eh_organization_members` ADD COLUMN `group_path` VARCHAR(128) COMMENT 'refer to the organization path';

