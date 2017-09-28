
ALTER TABLE eh_parking_recharge_orders ADD COLUMN original_price decimal(10,2) DEFAULT NULL;
ALTER TABLE eh_parking_recharge_orders ADD COLUMN card_request_id bigint(20) DEFAULT NULL;
ALTER TABLE eh_parking_recharge_orders ADD COLUMN invoice_type bigint(4) DEFAULT NULL;
ALTER TABLE eh_parking_recharge_orders ADD COLUMN paid_version bigint(4) DEFAULT NULL;


ALTER TABLE eh_parking_card_requests ADD COLUMN card_type_id VARCHAR(64) DEFAULT NULL;
ALTER TABLE eh_parking_card_requests ADD COLUMN address_id bigint(20) DEFAULT NULL;
ALTER TABLE eh_parking_card_requests ADD COLUMN invoice_type bigint(4) DEFAULT NULL;

ALTER TABLE eh_parking_lots CHANGE COLUMN `expired_recharge_json` recharge_json varchar(1024) DEFAULT NULL;

ALTER TABLE eh_parking_flow ADD COLUMN card_type_tip_flag tinyint(4) NOT NULL DEFAULT '0' COMMENT '1: support , 0: not';
ALTER TABLE eh_parking_flow ADD COLUMN card_type_tip text;


CREATE TABLE `eh_parking_invoice_types` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parking_lot_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `invoice_token` varchar(256) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_parking_user_invoices` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parking_lot_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `user_id` bigint(20) DEFAULT NULL,
  `invoice_type_id` bigint(20) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_parking_card_types` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parking_lot_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `card_type_id` varchar(128) DEFAULT NULL,
  `card_type_name` varchar(128) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
