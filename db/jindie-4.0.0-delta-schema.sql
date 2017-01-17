-- 招租管理添加门牌列，以便金蝶同步数据使用，add by tt, 20170117
ALTER TABLE `eh_lease_promotions` ADD COLUMN `namespace_type` VARCHAR(128) NULL DEFAULT NULL;
ALTER TABLE `eh_lease_promotions` ADD COLUMN `namespace_token` VARCHAR(256) NULL DEFAULT NULL;