-- by zheng 招租增加园区id 和 客服id
ALTER TABLE `eh_lease_promotions`
ADD COLUMN `community_id` BIGINT(20) NOT NULL DEFAULT '0' AFTER `namespace_id`;
ALTER TABLE `eh_lease_promotions`
ADD COLUMN `contact_uid` BIGINT(20) NULL DEFAULT NULL AFTER `contact_phone`;
ALTER TABLE `eh_lease_projects`
ADD COLUMN `contact_uid` BIGINT(20) NULL DEFAULT NULL AFTER `contact_phone`;
ALTER TABLE `eh_lease_buildings`
ADD COLUMN `manager_uid` BIGINT(20) NULL DEFAULT NULL AFTER `manager_contact`;