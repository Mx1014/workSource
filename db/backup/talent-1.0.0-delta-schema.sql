-- 人才表，add by tt, 20170511
-- DROP TABLE IF EXISTS `eh_talents`;
CREATE TABLE `eh_talents` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `name` VARCHAR(64),
  `avatar_uri` VARCHAR(2048),
  `phone` VARCHAR(32),
  `gender` TINYINT,
  `position` VARCHAR(64),
  `category_id` BIGINT,
  `experience` INTEGER,
  `graduate_school` VARCHAR(64),
  `degree` TINYINT,
  `remark` TEXT,
  `enabled` TINYINT,
  `default_order` BIGINT,
  `status` TINYINT NOT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 人才分类表，add by tt, 20170511
-- DROP TABLE IF EXISTS `eh_talent_categories`;
CREATE TABLE `eh_talent_categories` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `name` VARCHAR(64),
  `status` TINYINT NOT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 查询历史记录表，add by tt, 20170511
-- DROP TABLE IF EXISTS `eh_talent_query_histories`;
CREATE TABLE `eh_talent_query_histories` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `keyword` VARCHAR(64),
  `status` TINYINT NOT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

