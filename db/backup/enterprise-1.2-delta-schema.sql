-- 添加是否设置了管理员标记，add by tt, 20170522
ALTER TABLE `eh_organizations` ADD COLUMN `set_admin_flag` TINYINT DEFAULT 0;
-- 增加索引，add by tt, 20170522
ALTER TABLE `eh_organization_community_requests` ADD INDEX `member_id` (`member_id`);
ALTER TABLE `eh_organization_community_requests` ADD INDEX `community_id` (`community_id`);