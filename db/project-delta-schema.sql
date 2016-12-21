ALTER TABLE `eh_resource_category_assignments` ADD COLUMN `resource_categry_type` VARCHAR(32) COMMENT 'EhResourceCategories, EhCommunities';

ALTER TABLE `eh_resource_categories` ADD COLUMN `type` VARCHAR(32) COMMENT '1:分类, 2：子项目';