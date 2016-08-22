-- 增加一列存储升级内容
ALTER TABLE `eh_version_urls`	ADD COLUMN `upgrade_description` TEXT NULL DEFAULT NULL AFTER `info_url`;