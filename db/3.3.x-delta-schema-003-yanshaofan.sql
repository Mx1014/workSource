#
# eh_enterprise_details
#
ALTER TABLE `eh_enterprise_details` ADD COLUMN `display_name` VARCHAR(64)  COMMENT ''; 
ALTER TABLE `eh_enterprise_details` ADD COLUMN `contactor` VARCHAR(64)  COMMENT ''; 
ALTER TABLE `eh_enterprise_details` ADD COLUMN `member_count` BIGINT DEFAULT 0 COMMENT '';
ALTER TABLE `eh_enterprise_details` ADD COLUMN `checkin_date` DATETIME COMMENT 'checkin date';
ALTER TABLE `eh_enterprise_details` ADD COLUMN `avatar` VARCHAR(128) COMMENT '';
ALTER TABLE `eh_enterprise_details` ADD COLUMN `post_uri` VARCHAR(128) COMMENT '';

#
# eh_organization_members
#
ALTER TABLE `eh_organization_members` ADD COLUMN `employee_no` BIGINT COMMENT '';
ALTER TABLE `eh_organization_members` ADD COLUMN `gender` TINYINT DEFAULT '0' COMMENT '0: undisclosured, 1: male, 2: female';

#
# eh_organizations
#
ALTER TABLE `eh_organizations` ADD COLUMN `create_time` DATETIME COMMENT '';
ALTER TABLE `eh_organizations` ADD COLUMN `update_time` DATETIME COMMENT '';
ALTER TABLE `eh_organizations` ADD COLUMN `directly_enterprise_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'directly under the company';