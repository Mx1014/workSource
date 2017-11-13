CREATE TABLE `eh_parking_card_requests` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parking_lot_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `plate_number` varchar(64) DEFAULT NULL,
  `plate_owner_name` varchar(64) DEFAULT NULL COMMENT 'the name of plate owner',
  `plate_owner_phone` varchar(64) DEFAULT NULL COMMENT 'the phone of plate owner',
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 1: , 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `card_number` varchar(64) DEFAULT NULL COMMENT 'card number',
  `card_name` varchar(64) DEFAULT NULL COMMENT 'card name',
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `card_type_id` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
