ALTER TABLE `eh_resource_category_assignments` ADD COLUMN `resource_categry_type` VARCHAR(32) DEFAULT 'EhResourceCategories' COMMENT 'EhResourceCategories, EhCommunities';

ALTER TABLE `eh_resource_categories` ADD COLUMN `type` tinyint(4)  DEFAULT '1' COMMENT '1:分类, 2：子项目';