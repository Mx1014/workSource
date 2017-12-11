CREATE TABLE `eh_energy_meter_category_map`(
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'enterprise id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'category id',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_energy_meter_fomular_map`(
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'enterprise id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `fomular_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'fomular id',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;