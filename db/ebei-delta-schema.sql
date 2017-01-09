ALTER TABLE `eh_resource_categories` ADD COLUMN `type` tinyint(4)  DEFAULT '1' COMMENT '1:分类, 2：子项目';

ALTER TABLE `eh_pm_tasks` ADD COLUMN `string_tag1` VARCHAR(128);