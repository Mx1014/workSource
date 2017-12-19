ALTER TABLE `eh_point_logs` ADD COLUMN `event_happen_time` BIGINT;

-- 积分banner
DROP TABLE IF EXISTS `eh_point_banners`;
CREATE TABLE `eh_point_banners` (
  `id` BIGINT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `system_id` BIGINT,
  `name` VARCHAR(128),
  `poster_uri` VARCHAR(128),
  `action_type` TINYINT NOT NULL DEFAULT '0' COMMENT 'according to document',
  `action_data` TEXT COMMENT 'the parameters depend on item_type, json format',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: disabled, 2: enabled',
  `default_order` INTEGER NOT NULL DEFAULT 0,
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 积分商品
-- DROP TABLE IF EXISTS `eh_point_goods`;
-- CREATE TABLE `eh_point_goods` (
--   `id` BIGINT NOT NULL,
--   `namespace_id` INTEGER NOT NULL DEFAULT 0,
--   `number` VARCHAR(32) NOT NULL DEFAULT '',
--   `display_name` VARCHAR(32) NOT NULL DEFAULT '',
--   `poster_uri` VARCHAR(512) NOT NULL DEFAULT '',
--   `poster_url` VARCHAR(512) NOT NULL DEFAULT '',
--   `detail_url` VARCHAR(512) NOT NULL DEFAULT '',
--   `points` BIGINT NOT NULL DEFAULT 0,
--   `sold_amount` BIGINT NOT NULL DEFAULT 0,
--   `original_price` DECIMAL(10, 2) NOT NULL DEFAULT 0,
--   `discount_price` DECIMAL(10, 2) NOT NULL DEFAULT 0,
--   `point_rule` VARCHAR(256) NOT NULL DEFAULT '',
--   `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: disabled, 2: enabled',
--   `top_status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: disabled, 2: enabled',
--   `top_time` DATETIME(3),
--   `create_time` DATETIME(3),
--   `creator_uid` BIGINT,
--   `update_time` DATETIME(3),
--   `update_uid` BIGINT,
--   PRIMARY KEY (`id`)
-- ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_point_goods` ADD COLUMN shop_no VARCHAR(128);
ALTER TABLE `eh_point_goods` ADD COLUMN commodity_no VARCHAR(128);

ALTER TABLE `eh_point_goods` DROP COLUMN number;
ALTER TABLE `eh_point_goods` DROP COLUMN poster_uri;
ALTER TABLE `eh_point_goods` DROP COLUMN poster_url;
ALTER TABLE `eh_point_goods` DROP COLUMN detail_url;
ALTER TABLE `eh_point_goods` DROP COLUMN points;
ALTER TABLE `eh_point_goods` DROP COLUMN sold_amount;
ALTER TABLE `eh_point_goods` DROP COLUMN original_price;
ALTER TABLE `eh_point_goods` DROP COLUMN discount_price;
ALTER TABLE `eh_point_goods` DROP COLUMN point_rule;
ALTER TABLE `eh_point_goods` DROP COLUMN display_name;