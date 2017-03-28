-- 服务联盟 表单 add by sw 20170322
CREATE TABLE `eh_service_alliance_golf_requests` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT 0,
  `template_type` varchar(128) NOT NULL DEFAULT '' COMMENT 'i.e. EhServiceAllianceApplies type',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT 0,
  `type` bigint(20) NOT NULL DEFAULT 0,
  `category_id` bigint(20) NOT NULL DEFAULT 0,
  `creator_uid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `creator_name` varchar(128) DEFAULT NULL,
  `creator_mobile` varchar(128) DEFAULT NULL,
  `creator_organization_id` bigint(20) NOT NULL DEFAULT 0,
  `service_alliance_id` bigint(20) NOT NULL DEFAULT 0,
  `create_time` datetime DEFAULT NULL,

  `name` varchar(128) DEFAULT NULL,
  `mobile` varchar(128) DEFAULT NULL,
  `organization_name` varchar(128) DEFAULT NULL,
  `organization_floor` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `eh_service_alliance_gym_requests` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT 0,
  `template_type` varchar(128) NOT NULL DEFAULT '' COMMENT 'i.e. EhServiceAllianceApplies type',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT 0,
  `type` bigint(20) NOT NULL DEFAULT 0,
  `category_id` bigint(20) NOT NULL DEFAULT 0,
  `creator_uid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `creator_name` varchar(128) DEFAULT NULL,
  `creator_mobile` varchar(128) DEFAULT NULL,
  `creator_organization_id` bigint(20) NOT NULL DEFAULT 0,
  `service_alliance_id` bigint(20) NOT NULL DEFAULT 0,
  `create_time` datetime DEFAULT NULL,

  `name` varchar(128) DEFAULT NULL,
  `mobile` varchar(128) DEFAULT NULL,
  `organization_name` varchar(128) DEFAULT NULL,
  `profession` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_service_alliance_server_requests` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT 0,
  `template_type` varchar(128) NOT NULL DEFAULT '' COMMENT 'i.e. EhServiceAllianceApplies type',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT 0,
  `type` bigint(20) NOT NULL DEFAULT 0,
  `category_id` bigint(20) NOT NULL DEFAULT 0,
  `creator_uid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `creator_name` varchar(128) DEFAULT NULL,
  `creator_mobile` varchar(128) DEFAULT NULL,
  `creator_organization_id` bigint(20) NOT NULL DEFAULT 0,
  `service_alliance_id` bigint(20) NOT NULL DEFAULT 0,
  `create_time` datetime DEFAULT NULL,

  `name` varchar(128) DEFAULT NULL,
  `mobile` varchar(128) DEFAULT NULL,
  `organization_name` varchar(128) DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL,
  `destination` varchar(128) DEFAULT NULL,
  `departure_city` varchar(128) DEFAULT NULL,
  `departure_date` int(11) DEFAULT NULL,
  `departure_days` int(11) DEFAULT NULL,
  `estimated_cost` int(11) DEFAULT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
