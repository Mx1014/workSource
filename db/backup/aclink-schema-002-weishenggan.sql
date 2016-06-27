
ALTER TABLE `eh_door_access` ADD COLUMN `groupId` BIGINT NOT NULL DEFAULT 0;

ALTER TABLE `eh_aclinks` DROP COLUMN `driver`;
ALTER TABLE `eh_aclinks` ADD COLUMN `driver` VARCHAR(32) NOT NULL DEFAULT 'zuolin';

ALTER TABLE `eh_aclinks` ADD COLUMN `integral_tag1` BIGINT DEFAULT 0;
ALTER TABLE `eh_aclinks` ADD COLUMN `integral_tag2` BIGINT DEFAULT 0;
ALTER TABLE `eh_aclinks` ADD COLUMN `integral_tag3` BIGINT DEFAULT 0;
ALTER TABLE `eh_aclinks` ADD COLUMN `integral_tag4` BIGINT DEFAULT 0;
ALTER TABLE `eh_aclinks` ADD COLUMN `integral_tag5` BIGINT DEFAULT 0;

ALTER TABLE `eh_aclinks` ADD COLUMN `string_tag1` VARCHAR(128);
ALTER TABLE `eh_aclinks` ADD COLUMN `string_tag2` VARCHAR(128);
ALTER TABLE `eh_aclinks` ADD COLUMN `string_tag3` VARCHAR(128);
ALTER TABLE `eh_aclinks` ADD COLUMN `string_tag4` VARCHAR(128);
ALTER TABLE `eh_aclinks` ADD COLUMN `string_tag5` VARCHAR(128);

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

# modify at 2016-05-31
ALTER TABLE `eh_door_auth` ADD COLUMN `string_tag6` TEXT;

# modify at 2016-06-01

ALTER TABLE `eh_aclink_firmware` DROP COLUMN `firmware_type`;
ALTER TABLE `eh_aclink_firmware` ADD COLUMN `firmware_type` VARCHAR(128);

ALTER TABLE `eh_door_auth` ADD COLUMN `right_open` TINYINT NOT NULL default 1;
ALTER TABLE `eh_door_auth` ADD COLUMN `right_visitor` TINYINT NOT NULL default 0;
ALTER TABLE `eh_door_auth` ADD COLUMN `right_remote` TINYINT NOT NULL default 0;
ALTER TABLE `eh_door_auth` MODIFY COLUMN `right_open` TINYINT NOT NULL DEFAULT 0;
