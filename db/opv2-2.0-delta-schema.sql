-- domain 增加name, icon
ALTER TABLE `eh_domains` ADD COLUMN `icon_uri`  varchar(255) NULL AFTER `create_time`;
ALTER TABLE `eh_domains` ADD COLUMN `name`  varchar(255) NULL AFTER `namespace_id`;