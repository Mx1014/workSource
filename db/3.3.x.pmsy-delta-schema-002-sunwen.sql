ALTER TABLE `eh_pmsy_orders` ADD COLUMN `project_id` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the id of siyuan project';
ALTER TABLE `eh_pmsy_orders` ADD COLUMN `customer_id` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the id of customer according the third system, siyuan';
