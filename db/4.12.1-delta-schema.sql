-- merge from forum2.6 by yanjun 201712121010 start

-- added by janson
ALTER TABLE `eh_organization_address_mappings`
ADD COLUMN `building_id` BIGINT(20) NOT NULL DEFAULT 0 AFTER `namespace_type`,
ADD COLUMN `building_name` VARCHAR(128) NULL AFTER `building_id`;


-- 在帖子表里增加模块类型，以及类型入口  add by yanjun 20171205
ALTER TABLE `eh_forum_posts` ADD COLUMN `module_type`  tinyint(4) NULL, ADD COLUMN `module_category_id`  bigint(20);

-- 评论功能使用域空间、module、categoryid唯一定位  add by yanjun 20171205
DELETE from eh_interact_settings;
ALTER TABLE `eh_interact_settings` DROP COLUMN `forum_id`, CHANGE COLUMN `type` `module_type`  tinyint(4) NOT NULL COMMENT 'forum, activity, announcement' AFTER `namespace_id`, CHANGE COLUMN `entry_id` `category_id`  bigint(20) NULL DEFAULT NULL AFTER `module_type`;

-- merge from forum2.6 by yanjun 201712121010 end