
ALTER TABLE eh_parking_lots ADD COLUMN `recharge_month_count` INT NOT NUll DEFAULT 0 COMMENT 'organization of address';
ALTER TABLE eh_parking_lots ADD COLUMN `recharge_type` TINYINT NOT NULL DEFAULT 0 COMMENT '1: all month, 2: number of days';
ALTER TABLE eh_parking_lots ADD COLUMN `is_support_recharge` TINYINT NOT NULL DEFAULT 0 COMMENT 'out date card recharge flag , 1: support recharge , 0: not ';
ALTER TABLE eh_parking_lots ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;

ALTER TABLE eh_parking_card_requests ADD COLUMN `car_brand` VARCHAR(64) COMMENT 'car brand';
ALTER TABLE eh_parking_card_requests ADD COLUMN `car_color` VARCHAR(64) COMMENT 'car color';
ALTER TABLE eh_parking_card_requests ADD COLUMN `car_serie_name` VARCHAR(64) COMMENT 'car serie name';
ALTER TABLE eh_parking_card_requests ADD COLUMN `car_serie_id` BIGINT COMMENT 'car serie id';
ALTER TABLE eh_parking_card_requests ADD COLUMN `flow_id` BIGINT COMMENT 'flow id';
ALTER TABLE eh_parking_card_requests ADD COLUMN `flow_version` INTEGER NOT NULL DEFAULT 0 COMMENT 'current flow version';
ALTER TABLE eh_parking_card_requests ADD COLUMN `flow_case_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'flow case id';

ALTER TABLE eh_parking_card_requests ADD COLUMN `audit_succeed_time` DATETIME;
ALTER TABLE eh_parking_card_requests ADD COLUMN `process_succeed_time` DATETIME;
ALTER TABLE eh_parking_card_requests ADD COLUMN `open_card_time` DATETIME;
ALTER TABLE eh_parking_card_requests ADD COLUMN `cancel_time` DATETIME;



-- DROP TABLE IF EXISTS `eh_parking_flow`;
CREATE TABLE `eh_parking_flow` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `parking_lot_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_parking_lots',

  `request_month_count` INT NOT NUll DEFAULT 0 COMMENT 'organization of address',
  `request_recharge_type` TINYINT NOT NULL DEFAULT 0 COMMENT '1: all month, 2: number of days',
  `card_request_tip_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '1: support , 0: not ',
  `card_agreement_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '1: support , 0: not ',
  `max_request_num_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '1: support , 0: not ',
  `max_issue_num_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '1: support , 0: not ',
  `card_request_tip` TEXT,
  `card_agreement` TEXT,
  `max_issue_num` INTEGER NOT NULL DEFAULT 1 COMMENT 'the max num of the issue card',
  `max_request_num` INTEGER NOT NULL DEFAULT 1 COMMENT 'the max num of the request card',
  `flow_id` BIGINT NOT NULL COMMENT 'flow id',


  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_parking_statistics`;
CREATE TABLE `eh_parking_statistics` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `parking_lot_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_parking_lots',

  `amount` DECIMAL(10,2),

  `date_str` DATETIME,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_parking_car_series`;
CREATE TABLE `eh_parking_car_series`(
  `id` BIGINT NOT NULL,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(64) NOT NULL,
  `path` VARCHAR(128),
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `create_time` DATETIME,
  `delete_time` DATETIME,

  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- DROP TABLE IF EXISTS `eh_parking_attachments`;
CREATE TABLE `eh_parking_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) COMMENT 'attachment object owner type',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner id, e.g comment_id',
  `data_type` TINYINT NOT NULL DEFAULT 0 COMMENT '1: 身份证, 2: 行驶证 3:驾驶证',

  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;





