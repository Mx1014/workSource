
DROP TABLE IF EXISTS `eh_customer_events`;

CREATE TABLE `eh_customer_events` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER  NOT NULL COMMENT '域空间id',
  `customer_type` TINYINT NOT NULL COMMENT '所属客户类型',
  `customer_id`  BIGINT    COMMENT '所属客户id',
  `customer_name` VARCHAR(128)   COMMENT '客户名称',
  `contact_name` VARCHAR(64) ,
  `content`  TEXT,
  `creator_uid`  BIGINT   COMMENT '创建人uid',
  `create_time` DATETIME  ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




DROP TABLE IF EXISTS `eh_customer_tracking_plans`;

CREATE TABLE `eh_customer_tracking_plans` (
  `id` BIGINT  NOT NULL,
  `namespace_id` INTEGER  NOT NULL COMMENT '域空间id',
  `customer_type` TINYINT  NOT NULL COMMENT '所属客户类型',
  `customer_id`  BIGINT   COMMENT '所属客户id',
  `customer_name` VARCHAR(128)   COMMENT '客户名称',
  `contact_name` VARCHAR(64)   COMMENT '联系人',
  `tracking_type` TINYINT   COMMENT '计划跟进类型',
  `tracking_time` DATETIME   COMMENT '跟进时间',
  `notify_time` DATETIME   COMMENT '提醒时间',
  `title` VARCHAR(128) DEFAULT NULL COMMENT '标题',
  `content` TEXT  COMMENT '内容',
  `status`  TINYINT  NOT NULL COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `creator_uid` BIGINT     COMMENT '创建人uid',
  `create_time` DATETIME   COMMENT '创建时间',
  `update_uid`  BIGINT     COMMENT '修改人uid',
  `update_time` DATETIME   COMMENT '修改时间',
  `delete_uid`  BIGINT     COMMENT '删除人uid',
  `delete_time` DATETIME   COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



DROP TABLE IF EXISTS `eh_customer_trackings`;

CREATE TABLE `eh_customer_trackings` (
  `id` BIGINT  NOT NULL,
  `namespace_id` INTEGER NOT NULL COMMENT '域空间id',
  `customer_type` TINYINT NOT NULL COMMENT '所属客户类型',
  `customer_id` BIGINT    COMMENT '所属客户id',
  `customer_name` VARCHAR(128)   COMMENT '客户名称',
  `contact_name` VARCHAR(64)    COMMENT '联系人',
  `tracking_type` TINYINT    COMMENT '跟进类型',
  `tracking_uid` BIGINT     COMMENT '跟进人uid ',
  `intention_grade` INTEGER    COMMENT '意向等级',
  `tracking_time` DATETIME   COMMENT '跟进时间',
  `content` text COMMENT '跟进内容',
  `status` TINYINT NOT NULL COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `creator_uid` BIGINT     COMMENT '创建人uid',
  `create_time` DATETIME   COMMENT '创建时间',
  `update_uid` BIGINT     COMMENT '修改人uid',
  `update_time` DATETIME   COMMENT '修改时间',
  `delete_uid` BIGINT     COMMENT '删除人uid',
  `delete_time` DATETIME    COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




ALTER TABLE `eh_enterprise_customers`
ADD COLUMN `tracking_uid`  BIGINT  NULL COMMENT '跟进人uid' AFTER `update_time`,
ADD COLUMN `property_area`  DOUBLE NULL COMMENT '资产面积' AFTER `tracking_uid`,
ADD COLUMN `property_unit_price`  DOUBLE NULL COMMENT '资产单价' AFTER `property_area`,
ADD COLUMN `property_type`  TINYINT NULL COMMENT '资产类型' AFTER `property_unit_price`,
ADD COLUMN `longitude`  DOUBLE NULL COMMENT '经度' AFTER `property_type`,
ADD COLUMN `latitude`  DOUBLE NULL COMMENT '纬度' AFTER `longitude`,
ADD COLUMN `geohash`  VARCHAR(32) NULL DEFAULT NULL AFTER `latitude`;






