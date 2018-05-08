ALTER TABLE eh_office_cubicle_categories ADD COLUMN  `unit_price`  DECIMAL(10,2);
ALTER TABLE eh_office_cubicle_orders ADD COLUMN  `employee_number`  INTEGER;
ALTER TABLE eh_office_cubicle_orders ADD COLUMN  `financing_flag`  TINYINT;

-- 工位城市表 , add by dengs, 20180403
-- DROP TABLE IF EXISTS `eh_office_cubicle_cities`;
CREATE TABLE `eh_office_cubicle_cities` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `province_id` bigint(20) DEFAULT NULL COMMENT '省份id',
  `province_name` varchar(100) DEFAULT NULL COMMENT '省份名称',
  `city_id` bigint(20) DEFAULT NULL COMMENT '城市id',
  `city_name` varchar(128) DEFAULT NULL COMMENT '城市名称',
	`icon_uri` VARCHAR(1024) COMMENT '城市图片uri',
  `default_order` BIGINT,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0:INACTIVE,2:ACTIVE',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 用户选中城市表 , add by dengs, 20180403
-- DROP TABLE IF EXISTS `eh_office_cubicle_selected_cities`;
CREATE TABLE `eh_office_cubicle_selected_cities` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `province_name` varchar(100) DEFAULT NULL COMMENT '省份名称',
  `city_name` varchar(128) DEFAULT NULL COMMENT '城市名称',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
