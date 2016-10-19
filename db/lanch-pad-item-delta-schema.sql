-- DROP TABLE IF EXISTS `eh_item_service_types`;
CREATE TABLE `eh_item_service_categries` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `name` VARCHAR(64) NOT NULL COMMENT 'service categry name',
  `icon_uri` VARCHAR(1024) COMMENT 'service categry icon uri',
  `order` INTEGER COMMENT 'order ',
  `align` TINYINT DEFAULT '0' COMMENT '0: left, 1: center',
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_launch_pad_items` ADD COLUMN `service_categry_id` BIGINT COMMENT 'service categry id';