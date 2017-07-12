--
-- 电商运营   add by xq.tian  2017/01/09
--
-- DROP TABLE IF EXISTS `eh_business_promotions`;
CREATE TABLE `eh_business_promotions`(
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `subject` VARCHAR(128) COMMENT 'commodity subject',
  `description` TEXT COMMENT 'commodity description',
  `poster_uri` VARCHAR(1024) COMMENT 'commodity poster uri',
  `price` DECIMAL(10, 2) COMMENT 'commodity price',
  `commodity_url` VARCHAR(1024) COMMENT 'commodity url',
  `default_order` INTEGER NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `creator_uid` BIGINT,
  `update_time` DATETIME,
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;