
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
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999990, 'sms.default', 8, 'zh_CN', '${username}已授权给你${doorname}门禁二维码，请点击以下链接使用：${link}（24小时有效）', '24901');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999990, 'sms.default.yzx', 8, 'zh_CN', '${username}已授权给你${doorname}门禁二维码，请点击以下链接使用：${link}（24小时有效）', '24901');


