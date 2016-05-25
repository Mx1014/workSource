
ALTER TABLE `eh_door_access` ADD COLUMN `groupId` BIGINT NOT NULL DEFAULT 0;

ALTER TABLE `eh_aclinks` DROP COLUMN `driver`;
ALTER TABLE `eh_aclinks` ADD COLUMN `driver` VARCHAR(32) NOT NULL DEFAULT 'zuolin';

ALTER TABLE `eh_door_auth` ADD COLUMN `driver` VARCHAR(32) NOT NULL DEFAULT 'zuolin';
ALTER TABLE `eh_door_auth` ADD COLUMN `integral_tag1` BIGINT DEFAULT 0;
ALTER TABLE `eh_door_auth` ADD COLUMN `integral_tag2` BIGINT DEFAULT 0;
ALTER TABLE `eh_door_auth` ADD COLUMN `integral_tag3` BIGINT DEFAULT 0;
ALTER TABLE `eh_door_auth` ADD COLUMN `integral_tag4` BIGINT DEFAULT 0;
ALTER TABLE `eh_door_auth` ADD COLUMN `integral_tag5` BIGINT DEFAULT 0;

ALTER TABLE `eh_door_auth` ADD COLUMN `string_tag1` VARCHAR(128);
ALTER TABLE `eh_door_auth` ADD COLUMN `string_tag2` VARCHAR(128);
ALTER TABLE `eh_door_auth` ADD COLUMN `string_tag3` VARCHAR(128);
ALTER TABLE `eh_door_auth` ADD COLUMN `string_tag4` VARCHAR(128);
ALTER TABLE `eh_door_auth` ADD COLUMN `string_tag5` VARCHAR(128);
