
ALTER TABLE `eh_organization_tasks` ADD COLUMN `task_category` VARCHAR(128) COMMENT '1:PUBLIC_AREA 2:PRIVATE_OWNER';


ALTER TABLE `eh_organization_members` DROP COLUMN `namespace_id`;