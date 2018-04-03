-- 活动报名表添加活动报名信息等字段, add by tt, 20170227
ALTER TABLE `eh_activities` ADD COLUMN `signup_end_time` DATETIME;
ALTER TABLE `eh_activity_roster` ADD COLUMN `phone` VARCHAR(32);
ALTER TABLE `eh_activity_roster` ADD COLUMN `real_name` VARCHAR(128);
ALTER TABLE `eh_activity_roster` ADD COLUMN `gender` TINYINT;
ALTER TABLE `eh_activity_roster` ADD COLUMN `community_name` VARCHAR(64);
ALTER TABLE `eh_activity_roster` ADD COLUMN `organization_name` VARCHAR(128);
ALTER TABLE `eh_activity_roster` ADD COLUMN `position` VARCHAR(64);
ALTER TABLE `eh_activity_roster` ADD COLUMN `leader_flag` TINYINT;
ALTER TABLE `eh_activity_roster` ADD COLUMN `source_flag` TINYINT;

ALTER TABLE `eh_activity_roster` DROP INDEX `u_eh_act_roster_user`;