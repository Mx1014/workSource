
#
# eh_organization_members
#
ALTER TABLE `eh_organization_members` ADD COLUMN `employee_no` VARCHAR(128) COMMENT '';
ALTER TABLE `eh_organization_members` ADD COLUMN `gender` TINYINT DEFAULT '0' COMMENT '0: undisclosured, 1: male, 2: female';
ALTER TABLE `eh_organization_members` ADD COLUMN `namespace_id` INTEGER DEFAULT 0;
ALTER TABLE `eh_organization_members` ADD COLUMN `integral_tag1` BIGINT;
ALTER TABLE `eh_organization_members` ADD COLUMN `integral_tag2` BIGINT;
ALTER TABLE `eh_organization_members` ADD COLUMN `integral_tag3` BIGINT;
ALTER TABLE `eh_organization_members` ADD COLUMN `integral_tag4` BIGINT;
ALTER TABLE `eh_organization_members` ADD COLUMN `integral_tag5` BIGINT;
ALTER TABLE `eh_organization_members` ADD COLUMN `string_tag1` VARCHAR(128);
ALTER TABLE `eh_organization_members` ADD COLUMN `string_tag2` VARCHAR(128);
ALTER TABLE `eh_organization_members` ADD COLUMN `string_tag3` VARCHAR(128);
ALTER TABLE `eh_organization_members` ADD COLUMN `string_tag4` VARCHAR(128);
ALTER TABLE `eh_organization_members` ADD COLUMN `string_tag5` VARCHAR(128);

#
# eh_organizations
#
ALTER TABLE `eh_organizations` ADD COLUMN `create_time` DATETIME COMMENT '';
ALTER TABLE `eh_organizations` ADD COLUMN `update_time` DATETIME COMMENT '';
ALTER TABLE `eh_organizations` ADD COLUMN `directly_enterprise_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'directly under the company';
ALTER TABLE `eh_organizations` ADD COLUMN `group_id` BIGINT COMMENT 'eh_group id';
ALTER TABLE `eh_organizations` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0 AFTER directly_enterprise_id;
ALTER TABLE `eh_organizations` ADD COLUMN `integral_tag1` BIGINT;
ALTER TABLE `eh_organizations` ADD COLUMN `integral_tag2` BIGINT;
ALTER TABLE `eh_organizations` ADD COLUMN `integral_tag3` BIGINT;
ALTER TABLE `eh_organizations` ADD COLUMN `integral_tag4` BIGINT;
ALTER TABLE `eh_organizations` ADD COLUMN `integral_tag5` BIGINT;
ALTER TABLE `eh_organizations` ADD COLUMN `string_tag1` VARCHAR(128);
ALTER TABLE `eh_organizations` ADD COLUMN `string_tag2` VARCHAR(128);
ALTER TABLE `eh_organizations` ADD COLUMN `string_tag3` VARCHAR(128);
ALTER TABLE `eh_organizations` ADD COLUMN `string_tag4` VARCHAR(128);
ALTER TABLE `eh_organizations` ADD COLUMN `string_tag5` VARCHAR(128);

#
# modify
#
ALTER TABLE `eh_organization_members` ADD COLUMN `create_time` DATETIME COMMENT '';
ALTER TABLE `eh_organization_members` ADD COLUMN `update_time` DATETIME COMMENT '';
ALTER TABLE `eh_organization_members` ADD COLUMN `avatar` VARCHAR(128) COMMENT '';
ALTER TABLE `eh_organization_members` MODIFY COLUMN `employee_no` VARCHAR(128) COMMENT '' AFTER group_id;
ALTER TABLE `eh_organization_members` MODIFY COLUMN `update_time` DATETIME COMMENT '' AFTER namespace_id;
ALTER TABLE `eh_organization_members` MODIFY COLUMN `create_time` DATETIME COMMENT '' AFTER update_time;
ALTER TABLE `eh_organization_members` MODIFY COLUMN `avatar` VARCHAR(128) COMMENT '' AFTER employee_no;
ALTER TABLE `eh_organization_members` MODIFY COLUMN `group_id` BIGINT DEFAULT 0 COMMENT 'refer to the organization id' AFTER status;

#
# modify eh_organizations unique
#
ALTER TABLE `eh_organizations` DROP INDEX u_eh_org_name;

