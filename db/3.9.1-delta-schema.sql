-- merge from dev-banner-delta-schema.sql
--
-- banner表增加更新时间字段
--
ALTER TABLE `eh_banners` ADD COLUMN `update_time`  DATETIME;