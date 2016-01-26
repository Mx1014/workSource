#
# eh_enterprise_details
#
ALTER TABLE `eh_enterprise_details` ADD COLUMN `display_name` VARCHAR(64)  COMMENT ''; 
ALTER TABLE `eh_enterprise_details` ADD COLUMN `contactor` VARCHAR(64)  COMMENT ''; 
ALTER TABLE `eh_enterprise_details` ADD COLUMN `member_count` BIGINT DEFAULT 0 COMMENT '';
ALTER TABLE `eh_enterprise_details` ADD COLUMN `checkin_date` DATETIME COMMENT 'checkin date';
ALTER TABLE `eh_enterprise_details` ADD COLUMN `avatar` VARCHAR(128) COMMENT '';
ALTER TABLE `eh_enterprise_details` ADD COLUMN `post_uri` VARCHAR(128) COMMENT '';
ALTER TABLE `eh_enterprise_details` ADD COLUMN `integral_tag1` BIGINT;
ALTER TABLE `eh_enterprise_details` ADD COLUMN `integral_tag2` BIGINT;
ALTER TABLE `eh_enterprise_details` ADD COLUMN `integral_tag3` BIGINT;
ALTER TABLE `eh_enterprise_details` ADD COLUMN `integral_tag4` BIGINT;
ALTER TABLE `eh_enterprise_details` ADD COLUMN `integral_tag5` BIGINT;
ALTER TABLE `eh_enterprise_details` ADD COLUMN `string_tag1` VARCHAR(128);
ALTER TABLE `eh_enterprise_details` ADD COLUMN `string_tag2` VARCHAR(128);
ALTER TABLE `eh_enterprise_details` ADD COLUMN `string_tag3` VARCHAR(128);
ALTER TABLE `eh_enterprise_details` ADD COLUMN `string_tag4` VARCHAR(128);
ALTER TABLE `eh_enterprise_details` ADD COLUMN `string_tag5` VARCHAR(128);

#
# eh_organization_members
#
ALTER TABLE `eh_organization_members` ADD COLUMN `employee_no` BIGINT COMMENT '';
ALTER TABLE `eh_organization_members` ADD COLUMN `gender` TINYINT DEFAULT '0' COMMENT '0: undisclosured, 1: male, 2: female';
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

ALTER TABLE `eh_organization_members` DROP `group_id`;
ALTER TABLE `eh_organization_members` ADD COLUMN `group_id` BIGINT DEFAULT 0 COMMENT 'refer to the organization id';
ALTER TABLE `eh_organizations` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0 AFTER directly_enterprise_id;


