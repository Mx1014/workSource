CREATE TABLE `eh_community_approve` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `organization_id` bigint(20) NOT NULL,
  `owner_id` bigint(20) NOT NULL,
  `owner_type` varchar(64) NOT NULL,
  `module_id` bigint(20) DEFAULT NULL,
  `module_type` varchar(64) DEFAULT NULL,
  `approve_name` varchar(64) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0',
  `form_origin_id` bigint(20) DEFAULT NULL,
  `form_version` int(11) DEFAULT '0',
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_community_approve_vals` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `flow_case_id` bigint(20) DEFAULT '0',
  `approval_id` bigint(20) DEFAULT '0',
  `source_id` bigint(20) NOT NULL DEFAULT '0',
  `source_type` varchar(64) NOT NULL DEFAULT 'EhCommunityApprove',
  `name` varchar(45) DEFAULT NULL,
  `phone` bigint(20) DEFAULT '0',
  `company` varchar(45) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

