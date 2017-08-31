CREATE TABLE `eh_community_map_search_types` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the type, community, enterprise, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `name` varchar(128) NOT NULL DEFAULT '',
  `content_type` varchar(128) NOT NULL DEFAULT '' COMMENT 'search content type',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active',
  `create_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  `order` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_community_map_infos` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `community_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refering to eh_communities',
  `map_uri` varchar(128) DEFAULT NULL,
  `map_name` varchar(128) DEFAULT NULL,
  `version` varchar(128) DEFAULT NULL,
  `center_longitude` double DEFAULT NULL,
  `center_latitude` double DEFAULT NULL,
  `north_east_longitude` double DEFAULT NULL,
  `north_east_latitude` double DEFAULT NULL,
  `south_west_longitude` double DEFAULT NULL,
  `south_west_latitude` double DEFAULT NULL,
  `longitude_delta` double DEFAULT NULL,
  `latitude_delta` double DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: confirming, 2: active',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_community_building_geos` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `building_id` bigint(20) NOT NULL DEFAULT '0',
  `building_name` varchar(128) DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: confirming, 2: active',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;