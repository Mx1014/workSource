-- 资源预约订单统计 By st.zheng
CREATE TABLE `eh_rentalv2_order_statistics` (
  `id` BIGINT(20) NOT NULL,
  `order_id` BIGINT(20) NOT NULL,
  `rental_resource_id` BIGINT(20) NOT NULL,
  `rental_uid` BIGINT(20) NULL DEFAULT NULL,
  `rental_date` DATE NULL DEFAULT NULL,
  `start_time` DATETIME NULL DEFAULT NULL,
  `end_time` DATETIME NULL DEFAULT NULL,
  `reserve_time` DATETIME NULL DEFAULT NULL,
  `valid_time_long` BIGINT(20) NULL DEFAULT NULL,
  `community_id` BIGINT(20) NULL DEFAULT NULL,
  `namespace_id` INT(11) NULL DEFAULT NULL,
  `user_enterprise_id` BIGINT(20) NULL DEFAULT NULL,
  `rental_type` TINYINT(4) NULL DEFAULT NULL,
  `resource_type` VARCHAR(64) NULL DEFAULT NULL,
  `resource_type_id` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`));

  CREATE TABLE `eh_rentalv2_dayopen_time` (
  `id` BIGINT(20) NOT NULL,
  `owner_id` BIGINT(20) NULL DEFAULT NULL,
  `owner_type` VARCHAR(255) NULL DEFAULT NULL,
  `open_time` DOUBLE NULL DEFAULT NULL,
  `close_time` DOUBLE NULL DEFAULT NULL,
  `rental_type` TINYINT(4) NULL DEFAULT NULL,
  `resource_type` VARCHAR(64) NULL DEFAULT NULL,
  PRIMARY KEY (`id`));

