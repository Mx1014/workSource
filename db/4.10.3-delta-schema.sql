-- 园区入驻3.6 add by sw 20171023
DROP TABLE IF EXISTS `eh_lease_configs`;
DROP TABLE IF EXISTS `eh_lease_configs2`;

CREATE TABLE `eh_lease_configs` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) DEFAULT NULL COMMENT 'owner type, e.g EhCommunities',
  `owner_id` bigint(20) DEFAULT NULL COMMENT 'owner id, e.g eh_communities id',
  `config_name` varchar(128) DEFAULT NULL,
  `config_value` varchar(128) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_lease_project_communities` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `lease_project_id` bigint(20) NOT NULL COMMENT 'lease project id',
  `community_id` bigint(20) NOT NULL COMMENT 'community id',
  `creator_uid` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `eh_lease_projects` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `project_id` bigint(20) NOT NULL COMMENT 'id of the record',
  `city_id` bigint(20) NOT NULL COMMENT 'city id in region table',
  `city_name` varchar(64) DEFAULT NULL COMMENT 'redundant for query optimization',
  `area_id` bigint(20) NOT NULL COMMENT 'area id in region table',
  `area_name` varchar(64) DEFAULT NULL COMMENT 'redundant for query optimization',
  `address` varchar(512) DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `contact_name` varchar(128) DEFAULT NULL,
  `contact_phone` varchar(128) DEFAULT NULL COMMENT 'the phone number',
  `description` text,
  `traffic_description` text,
  `poster_uri` varchar(256) DEFAULT NULL,
  `extra_info_json` text,

  `creator_uid` bigint(20) DEFAULT NULL COMMENT 'user who suggested the creation',
  `create_time` datetime DEFAULT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;