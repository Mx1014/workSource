CREATE TABLE `eh_community_map_shops` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the type, community, enterprise, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `shop_name` varchar(128) NOT NULL DEFAULT '',
  `shop_type` varchar(128) NOT NULL DEFAULT '',
  `business_hours` varchar(128) NOT NULL DEFAULT '',
  `contact_name` varchar(128) NOT NULL DEFAULT '',
  `contact_phone` varchar(128) NOT NULL DEFAULT '',
  `address_id` varchar(128) NOT NULL DEFAULT '',
  `description` varchar(128) NOT NULL DEFAULT '',
  `shop_Avatar` varchar(128) NOT NULL DEFAULT '',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active',
  `create_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  `order` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

