ALTER TABLE `ehcore`.`eh_configurations` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `ehcore`.`eh_configurations` ADD COLUMN `display_name` VARCHAR(128);
ALTER TABLE `ehcore`.`eh_configurations` DROP INDEX `u_eh_conf_name`;
ALTER TABLE `ehcore`.`eh_configurations` ADD UNIQUE KEY `u_eh_conf_namespace_name`(`namespace_id`, `name`);
