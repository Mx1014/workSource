ALTER TABLE `eh_resource_categories` ADD COLUMN `type` tinyint(4)  DEFAULT '1' COMMENT '1:分类, 2：子项目';

-- move from db/3.12.4-delta-schema.sql for it's already released by lqs 20170116
ALTER TABLE `eh_users` ADD COLUMN `update_time` DATETIME;
ALTER TABLE `eh_organization_address_mappings` ADD COLUMN `create_time` DATETIME;
ALTER TABLE `eh_organization_address_mappings` ADD COLUMN `update_time` DATETIME;
ALTER TABLE `eh_activities` ADD COLUMN `update_time` DATETIME;
ALTER TABLE `eh_office_cubicle_orders` ADD COLUMN `create_time` DATETIME;
ALTER TABLE `eh_office_cubicle_orders` ADD COLUMN `update_time` DATETIME;

