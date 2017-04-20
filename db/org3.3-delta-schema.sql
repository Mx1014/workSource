-- 机构表里增加字段 记录操作人  2017-04-20 add by sfyan
ALTER TABLE `eh_organizations` ADD COLUMN `creator_uid` BIGINT;
ALTER TABLE `eh_organizations` ADD COLUMN `operator_uid` BIGINT;

-- 机构人员表里增加字段 记录操作人  2017-04-20 add by sfyan
ALTER TABLE `eh_organization_members` ADD COLUMN `creator_uid` BIGINT;
ALTER TABLE `eh_organization_members` ADD COLUMN `operator_uid` BIGINT;