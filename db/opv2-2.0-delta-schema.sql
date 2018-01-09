-- domain 增加icon
ALTER TABLE `eh_domains` ADD COLUMN `icon_uri`  varchar(255) NULL AFTER `create_time`;